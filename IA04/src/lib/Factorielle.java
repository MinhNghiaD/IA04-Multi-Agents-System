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
import java.util.*;

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
				
				 List<AID> receivers = getReceivers();
				 if (receivers.size() != 0) 
				 {
					 for (int i = 0; i < receivers.size(); ++i)
					 {
						 forwardMessage.addReceiver(receivers.get(i));
						 
						 forwardMessage.setConversationId("factorielle");
						 String mirt = "rqt" + System.currentTimeMillis();
						 
						 
						 //---------------Request String to JSON------------------//
						 int msgVal = Integer.parseInt(msg.getContent());
						 OperationResult oResult = new OperationResult(msgVal,""); 
						 String msgJSON = oResult.toJson();
						 System.out.println("Request factorielle receives : " + msg.getContent());
						 System.out.println("Request JSON : " + msgJSON);
						 forwardMessage.setContent(msgJSON);
						//---------------END Request String to JSON---------------//
						 
						 
						 //forwardMessage.setContent(msg.getContent());
						 forwardMessage.setReplyWith(mirt);
						 
						 send(forwardMessage);
					 }
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
				
				//return result to customer
				ACLMessage result = new ACLMessage(ACLMessage.INFORM);
				
				result.addReceiver(msg.getSender());
				
				result.setContent(msg.getContent());
					 
				//Return result to Factorielle
				send(result);
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
	
	private List<AID> getReceivers() 
	{
		List<AID> list = new ArrayList<AID>();
		
		DFAgentDescription template = new DFAgentDescription();
		
		ServiceDescription service  = new ServiceDescription();
		service.setType("Operations");
		service.setName("Multiplication");
		
		template.addServices(service);
		
		try 
		{
			DFAgentDescription[] result = DFService.search(this, template);
			
			for (int i =0; i < result.length; ++i)
			{
				list.add(result[i].getName());
			}
			
		} catch(FIPAException fe) 
		{
			fe.printStackTrace();
		}
		
		return list;
	 }
	
}