package src.agents;


import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import src.objects.Cell;

public class Analyseur extends Agent {

/* -----------------------------------------------------Setup and tear down --------------------------------------------------*/
	@Override
	protected void setup() 
	{
		register();
		
		System.out.println("Agent " + getLocalName() + " init!");	
     
		addBehaviour(new Pong());
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

/*-----------------------------------------------------------------Communication with Simulateur---------------------------------------------*/
	private class Pong extends CyclicBehaviour 
	{
		@Override
		public void action() 
		{
			//Simulator use QUERY_IF as message type to perform health check
			 MessageTemplate msgTemplate = MessageTemplate.MatchPerformative(ACLMessage.QUERY_IF);
			 
			 ACLMessage msg = receive(msgTemplate);
			 
			 if (msg != null) 
			 { 
				 try 
				 {
					 pong(msg); 
				 } 
				 catch (Exception e) 
				 {
					 System.out.println(getLocalName() + " : MESSAGE INVALIDÉ : " + msg.getContent());
					 e.printStackTrace();
				 } 
			 }
			 else
			 {
					block();
			 }
		}
		
		private void pong(ACLMessage request)
		{
			ACLMessage reply = request.createReply();
			
			reply.setPerformative(ACLMessage.INFORM);
			
			reply.setContent("Alive");
			
			send(reply);
		}
	}
	
/* ---------------------------------------------------------------------Communication with Environment-----------------------------------------------*/
	/**
	 * Classe RequestHandler héritée de CyclicBehaviour
	 * Cette classe est en charge de la réception des demandes du Environment et les traiter
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
					 handleRequest(msg);
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
	
	private void handleRequest(ACLMessage msg)
	{
		 //analyze cells
		 Vector<Cell> cells = Cell.jsonToCells(msg.getContent());
		 
		 process(cells);
		 
		 // Construct response
		 ACLMessage reply = msg.createReply();
			
		 reply.setPerformative(ACLMessage.INFORM);
		 
		 // reuse structure of message
		 Map<String, Object> map = Cell.jsonToMap(msg.getContent());
		 
		 for (int i = 0; i < cells.size(); ++i)
		 {
			 map.put("cell"+i, cells.elementAt(i).cellToJson());
		 }
		 
	   	 reply.setContent(Cell.mapToJson(map));
			
		 send(reply);
	}
	
	private void process(Vector<Cell> cells)
	{
		Vector<Integer> stableValues = new Vector<Integer>();
		
		int value = 0;
		
		for (Cell cell : cells)
		{
			value = cell.getValue();
			
			if (value > 0)
			{
				stableValues.add(value);
			}
		}
		
		for (Cell cell : cells)
		{
			cell.eliminateStableValues(stableValues);
		}
		
		for (int i = 2; i < (cells.size() - stableValues.size()); ++i)
		{
			advancedEliminate(cells, i);
		}
		
		findUniqueValue(cells);
	}
	
	private void advancedEliminate(Vector<Cell> cells, int degree)
	{	
		for (int i = 0; i < cells.size(); ++i)
		{
			if (cells.elementAt(i).getPossibleValues().size() == degree)
			{
				Vector<Integer> similarIndex = new Vector<Integer>();
				
				similarIndex.add(i);
				
				for (int j = 0; j < cells.size(); ++j)
				{
					if (i != j && cells.elementAt(i).equals(cells.elementAt(j)))
					{
						similarIndex.add(j);
					}
				}
				
				if (similarIndex.size() == degree)
				{
					for (int k = 0; k < cells.size(); ++k)
					{
						if (! similarIndex.contains(k))
						{
							cells.elementAt(k).eliminateStableValues(cells.elementAt(i).getPossibleValues());
						}
					}
				}
			}
		}
	}
	
	private void findUniqueValue(Vector<Cell> cells)
	{
		for (int i = 0; i < cells.size(); ++i)
		{
			Vector<Integer> vectorI = (Vector<Integer>) cells.elementAt(i).getPossibleValues().clone();
			
			if (vectorI != null)
			{
				for (int j = 0; j < cells.size(); ++j)
				{
					if (j != i)
					{
						Vector<Integer> vectorJ = cells.elementAt(j).getPossibleValues();
						
						if (vectorJ != null)
						{
							vectorI.removeAll(vectorJ);
							
							if (vectorI.isEmpty())
							{	
								break;
							}
						}
					}
				}
				
				if (vectorI.size() == 1)
				{	
					cells.elementAt(i).setPossibleValues(vectorI);
				}
			}
		}
	}
	
/*--------------------------------------------------------------Attributes----------------------------------------------------*/
	
	static public String typeService = "Analyser";
	static public String nameService = "Cell Value";	
}
