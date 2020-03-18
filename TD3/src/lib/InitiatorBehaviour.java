package lib;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;

public class InitiatorBehaviour extends BaseInitiatorBehaviour {

	public InitiatorBehaviour(Agent a, ACLMessage message) {
		super(a, message);
		// TODO Auto-generated constructor stub
	}

	protected void handleConfirm(ACLMessage msg) {
		System.out.println(msg.getContent());
	}
	
	protected void handleRefuse(ACLMessage msg) {
		System.out.println(msg.getContent());
	}
	
	protected void handleOther(ACLMessage msg) {
		System.out.println(msg.getContent());
	}
	
	@Override
	protected void handleResponse(ACLMessage response) {
		// TODO Auto-generated method stub
		if (response.getPerformative() == ACLMessage.CONFIRM) {
			handleConfirm(response);
		} else if (response.getPerformative() == ACLMessage.REFUSE) {
			handleRefuse(response);
		} else {
			handleOther(response);
		}	
	}

}
