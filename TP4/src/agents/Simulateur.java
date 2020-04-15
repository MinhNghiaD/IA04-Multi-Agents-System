package agents;

import java.util.Date;
import java.util.UUID;
import java.util.Vector;

import jade.content.ContentElement;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.JADEAgentManagement.JADEManagementOntology;
import jade.domain.JADEAgentManagement.KillAgent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREInitiator;
import jade.wrapper.StaleProxyException;

public class Simulateur extends Agent {

/*---------------------------------------------------------Setup and tear down---------------------------------------------------------------*/
	@Override
	protected void setup() 
	{	
		register();
		
		m_nbAnalyseurs = 0;
		
		System.out.println("Agent " + getLocalName() + " init!");	
		
		Object[] args = getArguments();
		
		if (args.length != 1)
		{
			System.out.println("Faux Nombre d'arguments!");	
			
			return;
		}
        
		m_period = (Integer) args[0];
		
		if (m_period < 3000)
		{
			System.out.println("Interval trop court!");	
			
			return;
		}
		
		createEnvironnement();
        
		//add 27 analyseurs
		m_aliveAnalyseur = new Vector<AID>();
		
		while (m_aliveAnalyseur.size() < 27)
		{
			AID agent = createAnalyseur();
			
			if (agent != null)
			{
				m_aliveAnalyseur.add(agent);
			}
		}
		
		addBehaviour(new Ping(this, m_period)); 
		addBehaviour(new RequestHandler());
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

/*------------------------------------------------------------- Main Request handler ------------------------------------------------------*/
	/**
	 * Classe RequestSudoku héritée de CyclicBehaviour
	 * Cette classe est en charge de la réception des message demande de entrée sudoku
	 *
	 */
	private class RequestHandler extends CyclicBehaviour 
	{
		@Override
		public void action() 
		{
			 MessageTemplate msgTemplate = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
			 
			 ACLMessage msg = receive(msgTemplate);
			 
			 if (msg != null) 
			 { 
				 try 
				 {
					 // forward to Environnement
					 msg.clearAllReceiver();
					 
					 String id = UUID.randomUUID().toString();
					 msg.setConversationId(id);
							
					 String mirt = "rqt" + System.currentTimeMillis();
					 msg.setReplyWith(mirt);
					 
					 msg.addReceiver(searchEnvironnement());
					 
					 send(msg);
				 } 
				 catch (Exception e) 
				 {
					 e.printStackTrace();
				 } 
			 }
			 else
			 {
					block();
			 }
		 }
	}
	
/* ------------------------------------------------------------------------ Health check and control Analyseur---------------------------------*/
	/**
	 * Classe Ping héritée de TickerBehaviour
	 * Elle va renvoyer un ping aux analyseurs après une période pré-définie
	 *
	 */
	private class Ping extends TickerBehaviour 
	{
		public Ping(Agent agent, int period) 
		{
			super(agent, period);
		}
			
		@Override
		protected void onTick() 
		{	
			//Use QUERY_IF as message type to verify agent is alive
			ACLMessage message = new ACLMessage(ACLMessage.QUERY_IF);
			
			String id = UUID.randomUUID().toString();
			message.setConversationId(id);
				
			String mirt = "rqt" + System.currentTimeMillis();
			message.setReplyWith(mirt);
			
			message.setContent("presence"); 
			
			if (m_aliveAnalyseur.size() > 0)
			{
				for (AID a : m_aliveAnalyseur) 
				{
					message.addReceiver(a);
				}
				
				//set timeout to 3000 ms
				Date timeout = new Date();
				timeout.setTime(timeout.getTime() + 3000);
				
				message.setReplyByDate(timeout);
				
				addBehaviour(new HealthCheck(getAgent() , message));
			}
		}

	}
	
	
	/**
	 * Classe HealthCheck hérité de AchieveREInitiator
	 * Elle attent la réponse seulement si elle vient d'envoyer une requete 
	 */
	private class HealthCheck extends AchieveREInitiator
	{
		HealthCheck(Agent agent, ACLMessage msg)
		{
			super(agent, msg);
			
			m_reponderAID = new Vector<AID>();
		}
		
		// In this case, if agent receives the ping, it will reply with a pong. If not, it means the agent is dead and need to be replace.
		@Override
		protected void handleInform(ACLMessage msg)
		{
			m_reponderAID.add(msg.getSender());
		}
		
		
		@Override
		protected void handleAllResponses(Vector v) 
		{
			for (AID a : m_aliveAnalyseur)
			{
			 	if (! m_reponderAID.contains(a))
			 	{			
			 		killAgent(a);
			 		
			 		//replace by adding one more client to complete the number of necessary clients
				    AID agent = createAnalyseur();
				    
				    if (agent != null)
				    {
				    	System.out.println(getLocalName() + ": add new agent: " + agent.getLocalName() + " at " + new Date());
				    	
				    	m_reponderAID.add(agent);
				    }
			 	}
			}
			
			m_aliveAnalyseur = m_reponderAID;
		}
		
		private Vector<AID> m_reponderAID;
	}
	
/* ------------------------------------------------------------Manage Agents ----------------------------------------------------------*/
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
	private AID createAnalyseur() 
	{
		String name = "Analyseur" + (m_nbAnalyseurs++);
			
		try 
		{
			getContainerController().createNewAgent(name, "agents.Analyseur", null).start();
			
			return new AID(name, AID.ISLOCALNAME);
		} 
		catch (StaleProxyException e) 
		{
			e.printStackTrace();
		}	
		
		return null;
	}
	
	/**
	 * Envoyer une demande à AMS pour terminer un agent bloquant
	 * @param name
	 */
	private void killAgent(AID name) 
	{
		ACLMessage request = createAMSRequest();
	    KillAgent  killer  = new KillAgent();
	    
	    killer.setAgent(name);
	    
	    Action act = new Action();
	    
	    act.setActor(getAMS());
	    act.setAction(killer);
	    
	    try 
	    {
	    	//Register the SL content language
	    	getContentManager().registerLanguage(new SLCodec(), FIPANames.ContentLanguage.FIPA_SL);

	    	//Register the mobility ontology
	    	getContentManager().registerOntology(JADEManagementOntology.getInstance());
	    	
	        getContentManager().fillContent(request, (ContentElement) act);
	        
	        FIPAService.doFipaRequestClient(this, request, 10000);
	        
	        System.out.println(getLocalName() + " request to kill " + name.getLocalName());
	    } 
	    catch (Exception e) 
	    {
	        e.printStackTrace();
	    }
	}
	
	private ACLMessage createAMSRequest() 
	{
		ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
		
		request.addReceiver(getAMS());
		request.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
		request.setLanguage(FIPANames.ContentLanguage.FIPA_SL);
		request.setOntology(JADEManagementOntology.getInstance().getName());
		
		return request;
	}
	
	/**
	 * Chercher Environnement via la page jaune
	 * @return AID
	 */
	private AID searchEnvironnement() 
	{
		DFAgentDescription template = new DFAgentDescription();

		ServiceDescription sd = new ServiceDescription();
		sd.setType(Environnement.typeService);
		sd.setName(Environnement.nameService);
		
		template.addServices(sd);

		try 
		{
			DFAgentDescription[] result = DFService.search(this, template); 	

			if (result.length > 0 ) 
			{
				return result[0].getName();
			}
		} 
		catch (FIPAException fe) 
		{
			fe.printStackTrace();
		}

		return null;
	}
	
/*---------------------------------------------- Attributes ----------------------------------------------*/
	
	static public  String typeService 	  = "Simuler";
	static public  String nameService 	  = "Sudoku";
	
	
	private int    m_period;
	
	//incremented after a new analyseur is created to keep the agent name unique
	private int    m_nbAnalyseurs;
	
	//keep track of alive analyseur
	private Vector<AID> m_aliveAnalyseur;
	
	// constant used to set max results of SearchConstraints
    private static Long MINUSONE = new Long(-1);
}
