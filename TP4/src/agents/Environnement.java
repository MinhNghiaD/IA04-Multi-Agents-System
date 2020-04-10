package src.agents;

import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.Vector;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREInitiator;
import src.objects.Cell;
import src.objects.Grill;

public class Environnement extends Agent {

	@Override
	protected void setup() {
		register();
		
		System.out.println("Agent " + getLocalName() + " init!");	

		m_sudoku = new Grill();
		
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
			String json = msg.getContent();
			
			// TODO: update cells
			System.out.println(getLocalName() + "Receive infrom from " + msg.getSender().getLocalName());	
			
			// Go to next iteration
			if (--m_nbWait == 0)
			{
				distributedCells();
			}
		}
	}
	
	
	private void distributedCells()
	{
		Vector<AID> analyseurAIDs = searchAnalyseur();
		
		m_nbWait = analyseurAIDs.size();
		
		System.out.println(getLocalName() + " get nb of analyseur: " + m_nbWait);
		
		for (AID analyseur : analyseurAIDs)
		{
			// organize cells to json and send it to Analyseur
			ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
			
			String id = UUID.randomUUID().toString();
			message.setConversationId(id);
				
			String mirt = "rqt" + System.currentTimeMillis();
			message.setReplyWith(mirt);
			
			message.addReceiver(analyseur);
			
			//TODO: distributed cells
			message.setContent("request"); 
			
			System.out.println(getLocalName() + " send request to " + analyseur.getName());
			
			addBehaviour(new RequestAnalyseur(this, message));
		}
	}
	
	
	
	
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

			if (result.length > 0 ) {

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
	
	
	/*--------------------------------------Attributes---------------------------------------------*/
		
	static public String typeService 	  = "Update";
	static public String nameService 	  = "Etat";
	
	private Grill m_sudoku;
	private int   m_nbWait;
}
