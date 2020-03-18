package lib;

import java.util.UUID;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public abstract class BaseInitiatorBehaviour extends Behaviour {

	boolean over = false; 
	MessageTemplate mt;
	public BaseInitiatorBehaviour(Agent a, ACLMessage message) {
		super(a);
		String id = UUID.randomUUID().toString();
		mt = MessageTemplate.MatchConversationId(id); 
		message.setConversationId(id); 
		myAgent.send(message);
	 }
	
	protected abstract void handleResponse(ACLMessage message);
		// ... Méthodes action et done ...
	
	@Override
	public void action() {
		ACLMessage response = myAgent.receive(mt); 
		if (response != null) {
			handleResponse(response);
			over = true; 
		} else {
			block();
		}
	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return over;
	}

	
}
