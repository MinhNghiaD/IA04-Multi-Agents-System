package agents;

import java.util.Date;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.WakerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class MachineCafe extends Agent 
{	
	/**
	 * setup agent avec un cyclic behaviour, qui attente et procéde les messages
	 */
	protected void setup() 
	{
		register();
		
		System.out.println("Agent MachineCafe init!");
		
		Object[] args = getArguments();
		
		if (args.length != 1)
		{
			System.out.println("Faux Nombre d'arguments!");	
			
			return;
		}
		
		m_dose 	   	  = (Integer) args[0];
		m_capacity    = m_dose;
		
		addBehaviour(new RequestHandler()); 
	}
	
	/**
	 * Déinscrire sur la page blanche
	 */
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
	 * Classe requestHandler héritée de CyclicBehaviour
	 * Cette classe est en charge de la réception des demandes du client et les traiter
	 *
	 */
	private class RequestHandler extends CyclicBehaviour 
	{
		@Override
		public void action() 
		{
			 MessageTemplate msgTemplate = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
			 
			 ACLMessage msg = receive(msgTemplate);
			 
			 if (msg != null && isInteger(msg.getContent())) 
			 { 
				 try 
				 {
					 processOrder(msg); 
				 } 
				 catch (Exception e) 
				 {
					 System.out.println(getLocalName() + " : MESSAGE INVALIDÉ");
				 } 
			 }
			 else
			 {
					block();
			 }
		 }
	}
	
	/**
	 * Classe recharge héritée de WakerBehaviour
	 * Elle va recharger les doses après un certain délai
	 *
	 */
	private class Recharge extends WakerBehaviour
	{	
		public Recharge(Agent a, long timer) 
		{
			super(a, timer);
		}

		@Override
		public void handleElapsedTimeout()
		{
			m_dose = m_capacity;
		}
	}
	

	/**
	 * Traitement la demmande 
	 * en répondrant un message positive ou négative
	 */
	private void processOrder(ACLMessage message) 
	{
		int request = Integer.parseInt(message.getContent());
		
		String sender = message.getSender().getLocalName();
			 
		System.out.println(getLocalName() + 
						   " reçoit la demande de " + 
						   message.getContent() + 
						   " de " + sender + ".");
			
		ACLMessage reply = message.createReply();
		
		int cafes = Math.max(0, Math.min(request, m_dose));
		
		if (m_dose > 0 && cafes > 0) 
		{ 
			//Réponse positive
			reply.setPerformative(ACLMessage.INFORM);
			
			reply.setContent(Integer.toString(cafes));
				
			decreaseDose(cafes);
				
			System.out.println(getLocalName() + 
							   " : Demande a été enregistrée ! Nombre de doses restantes : " + 
							   Integer.toString(m_dose)
							  );
				
			if (m_dose == 0)
			{
				//Recharger les doses 
				long timeout = 500 + (long)(Math.random() * (10000 - 500));
				addBehaviour(new Recharge(this, timeout)); 
				
				System.out.println("\n" + getLocalName() + " : Recharger le café dans " + timeout + " milisecondes. \n");
			}
		} 
		else 
		{ 
			//Réponse négative
			reply.setPerformative(ACLMessage.REFUSE);
			reply.setContent("0");
				
			System.out.println(getLocalName() + " : Il n'y a plus de café dans le machine.");
		}

		send(reply);
	}
	
	/**
	 * Inscrire la machine a la page blanche
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
			
		} catch (FIPAException fe) 
		{
			fe.printStackTrace();
		}
	}
	
	private void decreaseDose(int dose) 
	{
		this.m_dose -= dose;
	}
	
	public static boolean isInteger(String s) 
	{
	    return isInteger(s,10);
	}

	public static boolean isInteger(String s, int radix) 
	{
	    if(s.isEmpty())
	    {
	    	return false;
	    }
	    
	    for(int i = 0; i < s.length(); ++i) 
	    {
	        if(i == 0 && s.charAt(i) == '-') 
	        {
	            if(s.length() == 1) 
	            {
	            	return false;
	            }
	            else 
	            {
	            	continue;
	            }
	        }
	        
	        if(Character.digit(s.charAt(i), radix) < 0) 
	        {
	        	return false;
	       	}
	    }
	    
	    return true;
	}
	
	static public String typeService = "Vendre";
	static public String nameService = "Cafe";
	
	private 	  int 	 m_dose;
	private 	  int	 m_capacity;
}