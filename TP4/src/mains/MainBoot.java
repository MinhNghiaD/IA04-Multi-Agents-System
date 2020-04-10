package src.mains;

import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import src.objects.Grill;
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

			//Créer l'agent Simulateur
			Object[] period = {(Integer) 5000};
			AgentController simulateur = mainContainer.createNewAgent("Simulateur", "src.agents.Simulateur", period);
			
			//Démarrer l'agent simulation
			simulateur.start();
		}
		catch(Exception ex) 
		{
			ex.printStackTrace();
		}
	}

	private static String MAIN_PROPERTIES_FILE = "main_prop.txt";
}