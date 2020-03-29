package mains;

import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.core.Profile;

public class MainBoot 
{

	public static void main(String[] args) 
	{
		Runtime rt = Runtime.instance();
		Profile p = null;
		
		try
		{
			//Créer le main container
			p = new ProfileImpl(MAIN_PROPERTIES_FILE);
			AgentContainer mainContainer = rt.createMainContainer(p);

			AgentController manager = mainContainer.createNewAgent("Manager", "agents.Manager", null);

			manager.start();
		}
		catch(Exception ex) 
		{
			ex.printStackTrace();
		}
	}

	private static String MAIN_PROPERTIES_FILE = "main_prop.txt";
}