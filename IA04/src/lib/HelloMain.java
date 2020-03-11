//Program inits 2 secondary containers
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
		
			//create first secondary container
			ContainerController firstContainer = rt.createAgentContainer(p2);
	
			//AgentController hello1      = firstContainer.createNewAgent("HelloWorld1", "lib.HelloWorld", null);
			AgentController factorielle = firstContainer.createNewAgent("Factorielle", "lib.Factorielle", null);
			
			//create second secondary container
			// ContainerController secondContainer = rt.createAgentContainer(p2);
			ContainerController secondContainer = firstContainer;

			//AgentController hello2         = secondContainer.createNewAgent("HelloWorld2", "lib.HelloWorld", null);
		
			AgentController multiplicateur = secondContainer.createNewAgent("Multiplicateur", "lib.Multiplicateur", null);
		
			//hello1.start();
			factorielle.start();
			//hello2.start();
			multiplicateur.start();

		} catch (Exception ex) 
		{
			ex.printStackTrace();
		}

	}
	
	private static String MAIN_PROPERTIES_FILE      = "main_prop.txt";
	private static String SECONDARY_PROPERTIES_FILE = "container1_prop.txt";
}