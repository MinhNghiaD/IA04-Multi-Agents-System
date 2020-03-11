package lib;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lib.HelloWorld.HelloWorldBehaviour;

public class Factorielle extends Agent 
{
	protected class RequestHandler extends CyclicBehaviour 
	{
		 public void action() 
		 {
			 //receive only REQUEST message
			 MessageTemplate msgTemplate = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
			 
			 ACLMessage msg = receive(msgTemplate);
				 
			 if (msg != null) 
			 {
				 ACLMessage forwardMessage = new ACLMessage(ACLMessage.REQUEST);
					 
				 forwardMessage.addReceiver(new AID("Multiplicateur", AID.ISLOCALNAME));
				 
				 forwardMessage.setConversationId("factorielle");
				 String mirt = "rqt" + System.currentTimeMillis();
				 
				 
				 forwardMessage.setContent(msg.getContent());
				 forwardMessage.setReplyWith(mirt);
				 
				 send(forwardMessage);
			 }
		}
	}
	
	protected class ResultHandler extends CyclicBehaviour
	{
		public void action()
		{
			//receive only INFORM message
			MessageTemplate msgTemplate = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
			
			ACLMessage msg = receive(msgTemplate);
			
			if (msg != null)
			{
				//show result 
				System.out.println( "Result: " + msg.getContent());
			}
		}
	}
	
	protected class Debug extends CyclicBehaviour
	{
		public void action()
		{
			//receive every message
			
			ACLMessage msg = receive();
			
			if (msg != null)
			{
				//show result 
				System.out.println( "Result: " + msg.getContent());
			}
		}
	}
	
	public void setup() 
	{
		System.out.println("Agent factorielle init!");
		
		addBehaviour(new RequestHandler());
		addBehaviour(new ResultHandler());
		//addBehaviour(new Debug());
	}
}