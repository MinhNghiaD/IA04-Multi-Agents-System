package lib;

import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.core.Profile;


public class HelloMain {

	public static void main(String[] args) 
	{
		Runtime rt = Runtime.instance();
		ProfileImpl p = null;
		try 
		{	
			ProfileImpl p2 = new ProfileImpl(SECONDARY_PROPERTIES_FILE);
		
			ContainerController containerControiller1 = rt.createAgentContainer(p2);
	
			AgentController agent1 = containerControiller1.createNewAgent("HelloWorld1", 
																		  "lib.HelloWorld", null);
			AgentController agent2 = containerControiller1.createNewAgent("Factorielle", 
																		  "lib.Factorielle", null);
			
			
			
			ContainerController containerControiller2 = rt.createAgentContainer(p2);

			AgentController agent3 = containerControiller2.createNewAgent("HelloWorld3", 
																	      "lib.HelloWorld", null);
		
			AgentController agent4 = containerControiller2.createNewAgent("Multiplicateur", 
																	      "lib.Multiplicateur", null);
		
			agent1.start();
			agent2.start();
			agent3.start();
			agent4.start();

		} catch (Exception ex) 
		{
			ex.printStackTrace();
		}

	}
	
	public static String MAIN_PROPERTIES_FILE = "main_prop.txt";
	public static String SECONDARY_PROPERTIES_FILE = "container1_prop.txt";
}
