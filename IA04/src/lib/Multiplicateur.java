package lib;

import com.fasterxml.jackson.databind.ObjectMapper;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.DFService;
import jade.domain.FIPAException;

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
				
				try 
				{
					OperationResult data = OperationResult.read(msg.getContent());
					
					if (data == null)
					{
						return;
					}
					
					int command = data.getValue();
					
					try
					{
						// wait
						Thread.sleep(500 + (long)(Math.random() * (10000 - 500)));
					}
					
					catch(InterruptedException e)
					{
						System.out.println(e);
					}  
					
					//construct message type INFORM
					ACLMessage result = new ACLMessage(ACLMessage.INFORM);
					
					result.addReceiver(msg.getSender());
					
					long f = factorielle(command);
					
					result.setContent(String.valueOf(f));
						 
					//Return result to Factorielle
					send(result);
					
				} catch (Exception e) 
				{
					// TODO: handle exception
					System.out.println(e);
				}
				
			}
		}
	}
	
	protected void setup() 
	{
		System.out.println("Agent Multiplicateur init!");
	
		addBehaviour(new Receiver());
		
		DFAgentDescription agentDescription = new DFAgentDescription();
		agentDescription.setName(getAID());
		
		ServiceDescription serviceDescription = new ServiceDescription();
		serviceDescription.setType("Operations");
		serviceDescription.setName("Multiplication");
		
		agentDescription.addServices(serviceDescription);
		try 
		{
			DFService.register(this, agentDescription);
		}
		catch (FIPAException fe) 
		{
			fe.printStackTrace();
		}
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