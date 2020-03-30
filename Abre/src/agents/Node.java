package agents;


import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREInitiator;

public class Node extends Agent
{
	/** 
	 * Initialiser un agent
	 * 
	*/
	protected void setup() 
	{
		register();
		
		System.out.println("Agent " + getLocalName() + " init!");	
		
		Object[] args = getArguments();
		
		if (args.length != 1)
		{
			System.out.println("Faux Nombre d'arguments!");	
			
			return;
		}
		
		m_value        = (Integer) args[0];
		m_leftNode 	   = null;
		m_rightNode    = null;
        
		//ajoute les behaviours
	}
	
	@Override
	protected void takeDown() 
	{
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
	 * Inscrire client a la page blanche pour avoir un AID valide
	 */
	protected void register() 
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
					 processRequest(msg); 
				 } 
				 catch (Exception e) 
				 {
					 System.out.println(getLocalName() + " : MESSAGE INVALIDE");
				 } 
			 }
			 else
			 {
					block();
			 }
		 }
	}
	
	private void processRequest(ACLMessage message)
	{
		
	}
	
	private AID getNode(int value, String nodeName)
	{
		Object[] arg = {(Integer) value};

		try
		{
			getContainerController().createNewAgent(nodeName, "agents.Node", arg).start();

			return new AID(nodeName, AID.ISLOCALNAME);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		return null;
	}
	
	private boolean addNode(int value)
	{
		if (value > m_value)
		{
			
		}
		else if (value < m_value)
		{
			
		}
		
		return false;
	}

	
	static public String typeService = "Binary Tree";
	static public String nameService = "Node";
	
	private int m_value;
	
	private AID m_leftNode;
	private AID m_rightNode;
}
