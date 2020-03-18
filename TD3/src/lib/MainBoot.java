package lib;

import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.core.Profile;

public class MainBoot {

	public static void main(String[] args) 
	{
		Runtime rt = Runtime.instance();
		Profile p = null;
		
		try
		{
			//Créer le main container
			p = new ProfileImpl(MAIN_PROPERTIES_FILE);
			AgentContainer mainContainer = rt.createMainContainer(p);

			//Ajout l'agent MachineCafe au Main Container
			AgentController machine = mainContainer.createNewAgent("Machine", "lib.MachineCafe", null);
			
			//Ajout 3 agents Client au Main Container
			int dt1 = 5000, dt2 = 8000, dt3 = 11000;
			AgentController client1 = mainContainer.createNewAgent("Client1", "lib.Client", new Object[] {dt1});
			AgentController client2 = mainContainer.createNewAgent("Client2", "lib.Client", new Object[] {dt2});
			AgentController client3 = mainContainer.createNewAgent("Client3", "lib.Client", new Object[] {dt3});

			//Créer des agents
			machine.start();
			client1.start();
			client2.start();
			client3.start();
		}
		catch(Exception ex) 
		{
			ex.printStackTrace();
		}
	}

	private static String MAIN_PROPERTIES_FILE = "main_prop.txt";
}
