package src.agents;

import java.util.ArrayList;
import java.util.Random;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class Analyseur extends Agent {

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
					 System.out.println(getLocalName() + " : MESSAGE INVALIDÉ");
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
	
	
	/**
	 * Classe requestHandler héritée de CyclicBehaviour
	 * Cette classe est en charge de la réception des demandes du Simulateur et les traiter
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
					 //replySimulateur(msg);
					 
					 System.out.println(getLocalName() + " reveice " + msg.getContent());
					 
					 ACLMessage reply = msg.createReply();
						
					 reply.setPerformative(ACLMessage.INFORM);
						
				   	 reply.setContent("Inform");
						
					 send(reply);
					 
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
	
/*
	private void replySimulateur(ACLMessage msg) {
		
		String sender = msg.getSender().getLocalName();
			
		ACLMessage reply = msg.createReply();
		
		if (!m_active) 
		{ 
			//Réponse positive
			reply.setPerformative(ACLMessage.INFORM);
			reply.setContent("Agent actif.");
		} 
		else 
		{ 
			//Réponse négative
			reply.setPerformative(ACLMessage.REFUSE);
			reply.setContent("Agent inactif");
		}

		send(reply);
	}
*/

/*	
	private class updateEnv extends OneShotBehaviour
	{
		@Override
		public void action() {
			ACLMessage message = new ACLMessage(ACLMessage.INFORM);
			
			AID env = searchEnvironnement();
			
			message.addReceiver(env);
					
			message.setContent("hi"); 
					
			send(message);
			System.out.println(getLocalName() + " update Environnement. ");
		}
	}
*/
	
	static public String typeService = "Analyser";
	static public String nameService = "Cell Value";	
}
