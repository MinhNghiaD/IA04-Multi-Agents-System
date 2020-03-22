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
	protected void setup() 
	{
		register();
		
		System.out.println("Agent MachineCafe init!");
		
		Object[] args = getArguments();
		
		m_dose 	   = (Integer) args[0];
		m_capacity = m_dose;
		
		addBehaviour(new requestHandler()); 
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
	 * Classe requestHandler héritée de CyclicBehaviour
	 * Cette classe est en charge de la réception des demandes du client et les traiter
	 *
	 */
	private class requestHandler extends CyclicBehaviour 
	{
		@Override
		public void action() 
		{
			 MessageTemplate msgTemplate = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
			 
			 ACLMessage msg = receive(msgTemplate);
			 
			 if (msg != null && msg.getContent().equals("1")) 
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
	private class recharge extends WakerBehaviour
	{	
		public recharge(Agent a, long timer) 
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
			 
		System.out.println(getLocalName() + " Message reçu la demande de " + sender + ".");
			
		ACLMessage reply = message.createReply();
			
		if ( m_dose > 0 && m_dose >= request) 
		{ 
			//Réponse positive
			reply.setPerformative(ACLMessage.INFORM);
				
			decreaseDose(request);
				
			System.out.println(getLocalName() + 
							   " : Demande a été enregistrée ! Nombre de doses restantes : " + 
							   Integer.toString(m_dose)
							  );
				
			if (m_dose == 0)
			{
				//Recharger les doses 
				long timeout = 500 + (long)(Math.random() * (10000 - 500));
				addBehaviour(new recharge(this, timeout)); 
				System.out.println("\n" + getLocalName() + " : Recharger les doses dans " + timeout + " secondes. \n");
			}
		} 
		else 
		{ 
			//Réponse négative
			reply.setPerformative(ACLMessage.REFUSE);
				
			System.out.println(getLocalName() + " : Il n'y a pas assez de dose pour votre demande.");
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
	
	static public String typeService = "Vendre";
	static public String nameService = "Cafe";
	
	private 	  int 	 m_dose;
	private 	  int	 m_capacity;
}