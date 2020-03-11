package lib;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lib.HelloWorld.HelloWorldBehaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.DFService;
import jade.domain.FIPAException;

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
				
				 AID receiver = getReceiver();
				 if (receiver != null) 
				 {
					 forwardMessage.addReceiver(receiver);
					 
					 forwardMessage.setConversationId("factorielle");
					 String mirt = "rqt" + System.currentTimeMillis();
					 
					 
					 forwardMessage.setContent(msg.getContent());
					 forwardMessage.setReplyWith(mirt);
					 
					 send(forwardMessage);
				 }
				 else
				 {
					 System.out.println("Cannot find receiver!");
				 }
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
	
	protected void setup() 
	{
		System.out.println("Agent factorielle init!");
		
		addBehaviour(new RequestHandler());
		addBehaviour(new ResultHandler());
		//addBehaviour(new Debug());
	}
	
	private AID getReceiver() 
	{
		AID rec = null;
		
		DFAgentDescription template = new DFAgentDescription();
		
		ServiceDescription service  = new ServiceDescription();
		service.setType("Operations");
		service.setName("Multiplication");
		
		template.addServices(service);
		
		try 
		{
			DFAgentDescription[] result = DFService.search(this, template);
			
			if (result.length > 0)
			{
				rec = result[0].getName();
			}
		} catch(FIPAException fe) 
		{
			fe.printStackTrace();
		}
		
		return rec;
	 }
	
}