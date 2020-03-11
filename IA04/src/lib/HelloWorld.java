package lib;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.introspection.ACLMessage;

public class HelloWorld extends Agent 
{
	protected class HelloWorldBehaviour extends CyclicBehaviour 
	{
		 public void action() 
		 {
			 jade.lang.acl.ACLMessage msg = receive();
				 
			 if (msg != null) 
			 {
				 // Process the message
				 System.out.println( "Contact from : " + msg.getSender() + "Content: " + msg.getContent());
			 }
		 }
	}
	
	public void setup() 
	{
		System.out.println("Hello world!");
		
		addBehaviour(new HelloWorldBehaviour());
	}
}

