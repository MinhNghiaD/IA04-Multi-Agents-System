package lib;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import lib.Factorielle.Receiver;

public class Multiplicateur extends Agent  
{
	protected class Receiver extends CyclicBehaviour 
	{
		 public void action() 
		 {
			ACLMessage msg = receive();
				 
			if (msg != null) 
			{
				System.out.println( "Multiplicateur receive: " + msg.getContent());
				
				int command = Integer.parseInt(msg.getContent());
				
				//construct message type INFORM
				ACLMessage result = new ACLMessage(ACLMessage.INFORM);
				
				result.addReceiver(msg.getSender());
				
				long f = factorielle(command);
				
				System.out.println(f);
				
				result.setContent(String.valueOf(f));
					 
				//Return result to Factorielle
				send(result);
			}
		 }
	}
	
	public void setup() 
	{
		System.out.println("Agent Multiplicateur init!");
		
		addBehaviour(new Receiver());
	}
	
	private long factorielle(int x)
	{
		if (x == 1 || x == 0)
		{
			return 1;
		}
		
		return x * factorielle(x - 1);
	}
}