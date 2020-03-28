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

			//Créer l'agent MachineCafe
			Object[] initDose = {(Integer) 10};
			//AgentController machine = mainContainer.createNewAgent("MachineCafe", "agents.MachineCafe", initDose);
			
			Object[] argsClient1 = {(Integer) 5000,  (Integer) 1};
			Object[] argsClient2 = {(Integer) 8000,  (Integer) 1}; 
			Object[] argsClient3 = {(Integer) 11000, (Integer) 1};
			
			//Créer les trois agents Client avec trois périodes différentes 
			//AgentController client1 = mainContainer.createNewAgent("Client1", "agents.Client", argsClient1);
			//AgentController client2 = mainContainer.createNewAgent("Client2", "agents.Client", argsClient2);
			//AgentController client3 = mainContainer.createNewAgent("Client3", "agents.Client", argsClient3);

			//Démarrer des agents
			//machine.start();
			//client1.start();
			//client2.start();
			//client3.start();
		}
		catch(Exception ex) 
		{
			ex.printStackTrace();
		}
	}

	private static String MAIN_PROPERTIES_FILE = "main_prop.txt";
}