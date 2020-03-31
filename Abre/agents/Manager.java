package agents;

import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREInitiator;

public class Manager extends Agent
{
	static public Map<String, Object> mapJson(String json)
	{
		Map<String, Object> map = null;
		
		ObjectMapper mapper = new ObjectMapper();
		
		try 
		{
			map = mapper.readValue(json, Map.class);
		}
		catch(Exception ex) 
		{
			ex.printStackTrace();
		}
		
		return map;
	}
	
	
	/** 
	 * Initialiser un agent
	 * Executer le comportement RequestHander
	*/
	protected void setup() 
	{
		register();
		
		System.out.println("Agent " + getLocalName() + " init!");	
		
		m_root 	   = null;
        
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
	 * Inscrire client a la page blanche pour avoir un AID valide
	 */
	protected void register() 
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
	 * Classe requestHandler héritée de CyclicBehaviour
	 * Cette classe est en charge de la réception des demandes du client et les traiter
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
					 processRequest(msg); 
				 } 
				 catch (Exception e) 
				 {
					 System.out.println(getLocalName() + " : MESSAGE INVALIDE");
				 } 
			 }
			 else
			 {
					block();
			 }
		 }
	}
	
	
	/**
	 * Traitement du message reçu
	 *
	 */
	private void processRequest(ACLMessage msg)
	{
		//déserialise Json à Map
		Map<String, Object> map = mapJson(msg.getContent());
		
		if (map != null && map.containsKey("action"))
		{
			String action = (String) map.get("action");
			
			
			switch (action)
			{
				case "inserer":
					
					if (map.containsKey("value"))
					{
						int value = (Integer) map.get("value");
						
						if (m_root == null)
						{
							addRoot(value);
						}
						else
						{
							request(msg.getContent());
						}
					}
					else
					{
						System.out.println("Erreur: "  + getName() + 
										   ": action " + action    + " ne contient pas de value.");
					}
					
					break;
					
				case "presence":
					if (m_root == null)
					{
						if (map.containsKey("value"))
						{
							System.out.println(getLocalName() + ": " + 
											  (Integer) map.get("value") + 
											  " est absent.");
						}
					}
					else
					{
						request(msg.getContent());
					}
					
					break;
					
				case "affichage":
					if (m_root == null)
					{
						System.out.println(getLocalName() + ": aucun nombre insere.");
					}
					else
					{
						request(msg.getContent());
					}
					
					break;
					
				default:
					System.out.println(getLocalName() + ": action " + action + " n'est pas disponible!");
			}
			
		}
	}
	
	
	
	/**
	 * Création de la racine
	 *
	 */
	private void addRoot(int value)
	{
		Object[] arg = {(Integer) value};
		
		try
		{
			getContainerController().createNewAgent("root", "agents.Node", arg).start();
			
			m_root = new AID("root", AID.ISLOCALNAME);
			
			System.out.println(getLocalName() + value + " est insere.");	
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	
	
	/**
	 * set time pour envoyer la requête à la racine
	 *
	 */
	private void request(String json)
	{
		ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
			
		message.addReceiver(m_root);
		
		String id = UUID.randomUUID().toString();
		message.setConversationId(id);
		
		String mirt = "rqt" + System.currentTimeMillis();
		message.setReplyWith(mirt);
		
		message.setContent(json);
			
		addBehaviour(new SendRequest(this, message));
	}
	
	
	/**
	 * Création de la racine
	 *
	 */
	private class SendRequest extends AchieveREInitiator
	{
		SendRequest(Agent agent, ACLMessage msg)
		{
			super(agent, msg);
		}
		
		@Override
		protected void handleInform(ACLMessage inform)
		{
			System.out.println(getLocalName() + ": " + inform.getContent());	
		}
		
		@Override
		protected void handleRefuse(ACLMessage refuse)
		{
			System.out.println(getLocalName() + ": " + refuse.getContent());
		}
	}
	
	static public String typeService = "Manage";
	static public String nameService = "Manager";
	
	private AID m_root;
}
