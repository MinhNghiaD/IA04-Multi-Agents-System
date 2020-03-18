package lib;

import java.util.Date;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;


public class MachineCafe extends Agent {
	private int dose = 10;
	
	// réception de la demande
	protected class RequestHandler extends CyclicBehaviour 
	{
		 public void action() 
		 {
			 // Recevoir uniquement le message en type REQUEST
			 MessageTemplate msgTemplate = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
			 ACLMessage msg = receive(msgTemplate);
			 
			 if (msg != null ) {
				 try {
					 int request = Integer.parseInt(msg.getContent());
					 System.out.println("[MachineCafe] - Message reçu à " + new Date() );
					 processOrder(request, msg); //Traiter la demande
				 } catch (Exception e) {
					 System.out.println("[MachineCafe] - MESSAGE INVALIDÉ");
				} 
			 } 
		 }
	}
	
	// Traitement la demmande en répondrant un message positive ou négative
	protected void processOrder(int i, ACLMessage message) {
		ACLMessage reply = message.createReply();
		if ( getDose() > 0 && getDose() >= i ) { //Réponse positive
			decreaseDose(i);//Décrémenter la dose
			//Définir la réponse
			reply.setPerformative(ACLMessage.INFORM);
			reply.setContent("Demande a été enregistrée ! Nombre de doses restantes : " + Integer.toString(getDose()));
		} else { //Réponse négative
			reply.setPerformative(ACLMessage.REFUSE);
			reply.setContent("[MachineCafe] - Il n'y a pas assez de dose pour votre demande.");
		}
		System.out.println("[MachineCafe] - Réponse envoyée");
		send(reply);
	}
	
	protected void decreaseDose(int i) {
		this.dose -= i;
	}
	
	protected int getDose() { // Récupérer la dose actuelle 
		return this.dose;
	}
	
	// Initialiser un agent
	protected void setup() 
	{
		System.out.println("Agent MachineCafe init!");
		
		addBehaviour(new RequestHandler()); // Exécuter de la réception d'une demande
	}
	
}
