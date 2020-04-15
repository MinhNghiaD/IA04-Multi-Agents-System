package agents;

import java.util.Map;
import java.util.UUID;
import java.util.Vector;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREInitiator;

import objects.Cell;
import objects.Grill;

public class Environnement extends Agent {

/* ----------------------------------------------------------------------Setup and tear down ----------------------------------------------*/
	@Override
	protected void setup() 
	{
		register();
		
		System.out.println("Agent " + getLocalName() + " init!");	

		//m_sudoku = new Grill();
		
		addBehaviour(new RequestHandler());
	}
	

	@Override
	protected void takeDown() {
		
		System.out.println("---> "+ getLocalName() + " : Good bye");
		
		try 
		{
			DFService.deregister(this);
		} 
		catch (FIPAException e) 
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Inscrire simulateur a la page blanche pour avoir un AID valide
	 */
	private void register() 
	{
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		
		ServiceDescription sd = new ServiceDescription();
		
		sd.setType(typeService);
		sd.setName(nameService);
		
		dfd.addServices(sd);
	
		try 
		{
			DFService.register(this, dfd);
		} 
		catch (FIPAException fe) 
		{
			fe.printStackTrace();
		}
	}
	

	/**
	 * Classe RequestSudoku héritée de CyclicBehaviour
	 * Cette classe est en charge de la réception des message demande de entrée sudoku
	 *
	 */
	private class RequestHandler extends CyclicBehaviour 
	{
		@Override
		public void action() 
		{
			 MessageTemplate msgTemplate = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
			 
			 ACLMessage msg = receive(msgTemplate);
			 
			 if (msg != null) 
			 { 
				 try 
				 {
					 m_sudoku = new Grill(msg.getContent());
					 
					 // Start analyzing
					 distributedCells();
				 } 
				 catch (Exception e) 
				 {
					 e.printStackTrace();
				 } 
			 }
			 else
			 {
					block();
			 }
		 }
	}

/* ---------------------------------------------------------------------- Handle Sudoku ----------------------------------------------------*/
	
	private class RequestAnalyseur extends AchieveREInitiator
	{
		RequestAnalyseur(Agent agent, ACLMessage msg)
		{
			super(agent, msg);
		}
		
		// In this case, if an analyseur response with a Inform message, the message is the useful data. We can ignore error cases which come with REFUSE messages
		@Override
		protected void handleInform(ACLMessage msg)
		{	
			String json  = msg.getContent();
			
			// Update cells
			Map<String, Object> map = Cell.jsonToMap(msg.getContent());
			
			Vector<Cell> cells = Cell.jsonToCells(json);
			
			m_sudoku.updateSudoku(cells, 
								 (Integer) map.get("high"), 
								 (Integer) map.get("low"), 
								 (Integer) map.get("left"), 
								 (Integer) map.get("right"));
			
			// Go to next iteration
			if (--m_nbWait == 0)
			{
				System.out.println("----------------------End of Step----------------------------");
				m_sudoku.printOutSudoku();
				
				if (m_sudoku.isFinished())
				{
					System.out.println("SUDOKU SOLVED !!!");
					//m_sudoku.printOutSudoku();
				}
				else
				{
					distributedCells();
				}
			}
		}
	}
	
	
	private void distributedCells()
	{
		Vector<AID> analyseurAIDs = searchAnalyseur();
		
		// Here we use only the amount of avalable Analyseur to solve the sudoku.
		// The order is not important and the number of Analyseur is manage by Simulateur
		m_nbWait = analyseurAIDs.size();
		
		int messageOrder = 0;
		
		// send row to analyseur
		for (int i = 0; i < 9; ++i)
		{
			if (messageOrder == m_nbWait)
			{
				return;
			}
			
			// organize cells to json and send it to Analyseur
			String json = m_sudoku.cellsToJson(i, i, 0, 8);
			
			sendRequest(analyseurAIDs.elementAt(messageOrder), json);
			
			++messageOrder;
		}
		
		// send column to analyseur
		for (int i = 0; i < 9; ++i)
		{
			if (messageOrder == m_nbWait)
			{
				return;
			}
			
			// organize cells to json and send it to Analyseur
			String json = m_sudoku.cellsToJson(0, 8, i, i);
			
			sendRequest(analyseurAIDs.elementAt(messageOrder), json);
			
			++messageOrder;
		}	
		
		// send quare to analyseur
		for (int i = 0; i < 3; ++i)
		{
			for (int j = 0; j < 3; ++j)
			{
				if (messageOrder == m_nbWait)
				{
					return;
				}
				
				// organize cells to json and send it to Analyseur
				String json = m_sudoku.cellsToJson((3 * i), (3 * (i + 1)) - 1 , (3 * j), (3 * (j + 1)) - 1);
				
				sendRequest(analyseurAIDs.elementAt(messageOrder), json);
				
				++messageOrder;
			}
		}
	}
	
	private void sendRequest(AID receiver, String content)
	{
		ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
					
		String id = UUID.randomUUID().toString();
		message.setConversationId(id);
						
		String mirt = "rqt" + System.currentTimeMillis();
		message.setReplyWith(mirt);
					
		message.addReceiver(receiver);
		
		message.setContent(content); 
					
		addBehaviour(new RequestAnalyseur(this, message));
	}
	
/* ------------------------------------------------------------- Manage agents -------------------------------------------------------------------*/
	/**
	 * Chercher les analyseurs via la page jaune
	 * @return AID
	 */
	@SuppressWarnings("null")
	private Vector<AID> searchAnalyseur() 
	{
		DFAgentDescription template = new DFAgentDescription();

		ServiceDescription sd = new ServiceDescription();
		sd.setType(Analyseur.typeService);
		sd.setName(Analyseur.nameService);
		
		template.addServices(sd);

		Vector<AID> listReceiver = new Vector<>();

		try 
		{
			DFAgentDescription[] result = DFService.search(this, template); 	

			if (result.length > 0 ) 
			{
				for (int i = 0; i < result.length; i++) 
				{		
					listReceiver.add(result[i].getName());
				}
			}
		} 
		catch (FIPAException fe) 
		{
			fe.printStackTrace();
		}

		return listReceiver;
	}
	
	
/*-------------------------------------------------------Attributes------------------------------------------------------------*/
		
	static public String typeService 	  = "Update";
	static public String nameService 	  = "Etat";
	
	private Grill m_sudoku;
	private int   m_nbWait;
}
