package agents;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREInitiator;
import agents.MachineCafe;

public class Client extends Agent 
{	
	/* 
	 * Initialiser un agent
	 * En exécutant le processus d'envoie d'une demande 
	*/
	protected void setup() 
	{
		register();
		
		System.out.println("Agent " + getLocalName() + " init!");	
		
		Object[] args = getArguments();
		
		if (args.length != 2)
		{
			System.out.println("Faux Nombre d'arguments!");	
			
			return;
		}
        
		m_period 	   = (Integer) args[0];
		m_doseCommande = (Integer) args[1];
        
		addBehaviour(new CommanderCafe(this, m_period)); 
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
	 * Classe commanderCafe héritée de TickerBehaviour
	 * Elle va renvoyer la demande après une période pré-définie
	 *
	 */
	private class CommanderCafe extends TickerBehaviour 
	{
		public CommanderCafe(Agent agent, long period) 
		{
			super(agent, period);
		}
			
		@Override
		protected void onTick() 
		{	
			ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
				
			AID machineCafe = searchMachineCafe();
			
			if (machineCafe != null)
			{
				message.addReceiver(machineCafe);
				
				message.setContent(Integer.toString(m_doseCommande)); 
				
				String id = UUID.randomUUID().toString();
				message.setConversationId(id);
					
				String mirt = "rqt" + System.currentTimeMillis();
				message.setReplyWith(mirt);
				
				// Essayer d'acheter un café
				addBehaviour(new AcheterCafe(getAgent() , message));
				
				System.out.println("\n" + getLocalName() + " envoie demande à " + new Date());
			}
		}
	}
	
	/**
	 * Classe AcheterCafe hérité de AchieveREInitiator
	 * Elle attent le réponse seulement si elle vient d'envoyer un requete 
	 */
	private class AcheterCafe extends AchieveREInitiator
	{
		AcheterCafe(Agent agent, ACLMessage msg)
		{
			super(agent, msg);
		}
		
		@Override
		protected void handleInform(ACLMessage inform)
		{
			if (MachineCafe.isInteger(inform.getContent()))
			{
				System.out.println(getLocalName() + " reçoit " + inform.getContent() + " café!");	
			}
		}
		
		protected void handleRefuse(ACLMessage refuse)
		{
			System.out.println(getLocalName() + " est refusé!");
		}
	}
	
	/**
	 * Inscrire client a la page blanche pour avoir un AID valide
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
	 * Chercher le machine de café via la page jaune
	 * @return AID
	 */
	private AID searchMachineCafe() 
	{
		DFAgentDescription template = new DFAgentDescription();
		
		ServiceDescription sd = new ServiceDescription();
		sd.setType(MachineCafe.typeService);
		sd.setName(MachineCafe.nameService);
		
		template.addServices(sd);
		
		AID receiver = null;
		
		try 
		{
			DFAgentDescription[] result = DFService.search(this, template);
			
			int n = result.length;
			
			Random r = new Random();
			
			if (n > 0) 
			{
				int v = r.nextInt(n);
				receiver = result[v].getName();
			}
		} 
		catch (FIPAException fe) 
		{
			fe.printStackTrace();
		}
		
		return receiver;
	}
	
	static public String typeService 	  = "Buy";
	static public String nameService 	  = "Client";
	
	private 	  int    m_period;
	private 	  int	 m_doseCommande;
}