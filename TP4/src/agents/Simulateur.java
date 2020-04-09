package agents;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;
import jade.wrapper.StaleProxyException;

public class Simulateur extends Agent {

	@Override
	protected void setup() 
	{	
		register();
		createAnalyseur();
		createEnvironnement();
		
		System.out.println("Agent " + getLocalName() + " init!");	
		
		Object[] args = getArguments();
		
		if (args.length != 1)
		{
			System.out.println("Faux Nombre d'arguments!");	
			
			return;
		}
        
		m_period 	   = (Integer) args[0];
        
		addBehaviour(new ContactAnalyseur(this, m_period)); 
	}
	

	@Override
	protected void takeDown()
	{
		System.out.println("---> "+ getLocalName() + " : Good bye");
		
		try 
		{
			DFService.deregister(this);
		} 
		catch (FIPAException e) 
		{
			e.printStackTrace();
		}

	}

	
	/**
	 * Classe ContactAnalyseur héritée de TickerBehaviour
	 * Elle va renvoyer la demande après une période pré-définie
	 *
	 */
	private class ContactAnalyseur extends TickerBehaviour 
	{
		public ContactAnalyseur(Agent agent, Integer period) 
		{
			super(agent, period);
		}
			
		@Override
		protected void onTick() 
		{	
			ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
				
			ArrayList<AID> listAnalyseur = searchAnalyseur();
			
			if (listAnalyseur.size() > 0)
			{
				for (AID a : listAnalyseur) {
					
					message.addReceiver(a);
					
					message.setContent("hi"); 
					
					String id = UUID.randomUUID().toString();
					message.setConversationId(id);
						
					String mirt = "rqt" + System.currentTimeMillis();
					message.setReplyWith(mirt);
					
					// Vérifier si l'agent survit
					addBehaviour(new HealthCheck(getAgent() , message));
					
					System.out.println("\n" + getLocalName() + " demande au " + a.getLocalName() + " à " + new Date());
				}
				
			}
		}

	}
	
	
	/**
	 * Classe HealthCheck hérité de AchieveREInitiator
	 * Elle attent le réponse seulement si elle vient d'envoyer un requete 
	 */
	private class HealthCheck extends AchieveREInitiator
	{
		HealthCheck(Agent agent, ACLMessage msg)
		{
			super(agent, msg);
		}
		
		@Override
		protected void handleInform(ACLMessage inform)
		{
			if (inform.getContent() != null)
			{
				System.out.println( getLocalName() + " reçoit de " + inform.getSender().getLocalName() + ". Message : " + inform.getContent());	
			} else {
				System.out.println("HealthCheck, empty inform.");
			}
		}
		
		protected void handleRefuse(ACLMessage refuse)
		{
			System.out.println(refuse.getSender().getLocalName() + " est désactive !");
		}
	}
	
	
	/**
	 * Inscrire simulateur a la page blanche pour avoir un AID valide
	 */
	private void register() 
	{
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		
		ServiceDescription sd = new ServiceDescription();
		
		sd.setType(typeService);
		sd.setName(nameService);
		
		dfd.addServices(sd);
	
		try 
		{
			DFService.register(this, dfd);
			
		} 
		catch (FIPAException fe) 
		{
			fe.printStackTrace();
		}
	}
	
	
	/**
	 * Créer et démarrer l'agent Environnement
	 */
	private void createEnvironnement() 
	{
		try
		{
			getContainerController().createNewAgent("Environnement", "agents.Environnement", null).start();

		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	
	/**
	 * Créer et démarrer l'agent Analyseur
	 */
	private void createAnalyseur() {
		
		ArrayList<Integer> caseToSolve = new ArrayList<>();
		
		for (int i = 0; i <= 9; i++) {
			caseToSolve.add(0);
		}
		
		for ( int i = 0; i < 27; i++ ) {
			
			Object[] arg = {(ArrayList<Integer>) caseToSolve};
			String name = "Analyseur" + i;
			
			try {
				getContainerController().createNewAgent(name, "agents.Analyseur", arg).start();
			} catch (StaleProxyException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	
	/**
	 * Chercher les analyseurs via la page jaune
	 * @return AID
	 */
	@SuppressWarnings("null")
	private ArrayList<AID> searchAnalyseur() 
	{
		DFAgentDescription template = new DFAgentDescription();
		
		ServiceDescription sd = new ServiceDescription();
		sd.setType(Analyseur.typeService);
		sd.setName(Analyseur.nameService);
		
		SearchConstraints sc = new SearchConstraints();
		sc.setMaxResults(MINUSONE);
		
		template.addServices(sd);
		
		ArrayList<AID> listReceiver = new ArrayList<>();
		
		try 
		{
			DFAgentDescription[] result = DFService.search(this, template, sc); 	
			
			if (result.length > 0 ) {
				
				for (int i = 0; i < result.length; i++) {		
					listReceiver.add(result[i].getName());
				}
			}
		} 
		catch (FIPAException fe) 
		{
			fe.printStackTrace();
		}
		
		return listReceiver;
	}
	
	static public String typeService 	  = "Simuler";
	static public String nameService 	  = "Sudoku";
	
	private 	  int    m_period;
	// constant used to set max results of SearchConstraints
    private static Long MINUSONE = new Long(-1);
}
