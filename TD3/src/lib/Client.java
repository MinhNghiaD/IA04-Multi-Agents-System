package lib;

import java.util.Date;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lib.MachineCafe.RequestHandler;

public class Client extends Agent {
	// Contructeur sert à exécuter la demande tous les 5 minutes
	protected class RequestHandler extends TickerBehaviour {
		public RequestHandler(Agent a, long dt) {
			super(a, dt);
		}
			 
		@Override
		//Envoie d'une demande à la machine
		protected void onTick() {
			ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
			message.addReceiver(new AID("Machine",AID.ISLOCALNAME));
			message.setContent("1"); 
			send(message);
			System.out.println("[Client] - Demande envoyée");
		}
	}
	
	// réception d'une réponse
	protected class ReponseHandler extends CyclicBehaviour {
		 public void action() {
			 // Recevoir uniquement le message en type INFORM ou REFUSE
			 ACLMessage msg = receive();
			 
			 // Récupérer le contenue du message 
			 if (msg != null &&
				(msg.getPerformative() == ACLMessage.INFORM || msg.getPerformative() == ACLMessage.REFUSE)) {
					 System.out.println("[Client] - Réponse réçue : " + msg.getContent());
					 System.out.println("");
					 System.out.println(""); 
			 }
		 }
	}
	
	/* 
	 * Initialiser un agent
	 * En xécutant le processus d'envoie d'une demande 
	 * et celui de réception d'une réponse
	*/
	protected void setup() {
		System.out.println("Agent Client init!");	
		//Récupérer la durée entre 2 demandes
		Object[] args = getArguments();
        String arg1 = args[0].toString();
        int dt = Integer.parseInt(arg1);
        
		addBehaviour(new RequestHandler(this, dt)); // Exécuter de la réception d'une demande
		addBehaviour(new ReponseHandler()); // Exécuter de l'envoie d'une réponse
	}
}
