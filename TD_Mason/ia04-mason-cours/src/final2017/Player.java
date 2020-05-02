package final2017;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import sim.engine.SimState;
import sim.engine.Steppable;

public class Player extends Agent implements Steppable{
	 public int x, y;
	 public String name;
	 public String partner;
	 public Ball ball;
	 public boolean forward;
	 public boolean canKick;

	@Override
	protected void setup() {
		super.setup();
		PlayerModel ants = (PlayerModel) getArguments()[0];
		name = (String) getArguments()[1];
		partner = name.equals("a") ? "b" : "a";
		ants.addAgent(this);
		System.out.println(getLocalName() + " --> installed");
		addBehaviour(new PartnerBehaviour());
	}

	@Override
	public void step(SimState state) {
		if (canKick) {
			ball.kick = true;
			addBehaviour(new KickBehaviour());
			canKick = false;
		}
		if (forward) {
			moveforward((PlayerModel) state);
		}
	}
	private void moveforward(PlayerModel ants) {
		ants.yard.set(x, y, null);
		 ants.yard.set(x, ants.yard.stx(y+1), this);
		 y = ants.yard.stx(y+1);
	}
	public class KickBehaviour extends OneShotBehaviour {

		@Override
		public void action() {
			ACLMessage message = new ACLMessage(ACLMessage.INFORM);
			message.setContent("kick");
			AID aid = new AID(partner, AID.ISLOCALNAME);
			message.addReceiver(aid);
			System.out.println(getAID().getLocalName() + " --> send kick");
			send(message);
		}
		
	}
	public class PartnerBehaviour extends CyclicBehaviour {

		@Override
		public void action() {
			ACLMessage message = receive();
			if (message != null) {
				forward = true;
				System.out.println(getAID().getLocalName() + " --> receive kick");
			}
		}
		
	}
}
