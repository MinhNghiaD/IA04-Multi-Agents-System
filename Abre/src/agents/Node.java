package agents;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

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

public class Node extends Agent
{
	/** 
	 * Initialiser un agent
	 * 
	*/
	protected void setup() 
	{
		register();
		
		//System.out.println("Agent " + getLocalName() + " init!");	
		
		Object[] args = getArguments();
		
		if (args.length != 1)
		{
			System.out.println("Faux Nombre d'arguments!");	
			
			return;
		}
		
		m_value        = (Integer) args[0];
		m_leftNode 	   = null;
		m_rightNode    = null;
        
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
	
	private void processRequest(ACLMessage msg)
	{
		//déserialise Json à Map
		Map<String, Object> map = Manager.mapJson(msg.getContent());
		
		if (map != null && map.containsKey("action"))
		{
			String action = (String) map.get("action");
			
			ACLMessage reply = msg.createReply();
			
			switch (action)
			{
				case "inserer":
					
					if (map.containsKey("value"))
					{
						addNode((Integer) map.get("value"), msg.getContent(), reply);
					}
					else
					{
						System.out.println("Erreur: "  + getName() + 
										   ": action " + action    + " ne contient pas de value.");
					}
					
					break;
					
				case "presence":
					
					if (map.containsKey("value"))
					{
						verifyNode((Integer) map.get("value"), msg.getContent(), reply);
					}
					else
					{
						System.out.println("Erreur: "  + getName() + 
										   ": action " + action    + " ne contient pas de value.");
					}
					
					break;
					
				case "affichage":
					
					getTree(msg.getContent(), reply);
					
					break;
				
				default:
					System.out.println(getName() + ": action " + action + " n'est pas disponible!");
			}
			
		}
	}
	
	private void verifyNode(int value, String json, ACLMessage reply)
	{
		if (m_value == value)
		{
			reply.setPerformative(ACLMessage.INFORM);
			reply.setContent(value + " est present");
			send(reply);
		}
		else if (value > m_value)
		{
			if (m_rightNode != null)
			{
				request(m_rightNode, json, reply);
			}
			else
			{
				reply.setPerformative(ACLMessage.REFUSE);
				reply.setContent(value + " est absent.");
				send(reply);
			}
		}
		else
		{
			if (m_leftNode != null)
			{
				request(m_leftNode, json, reply);
			}
			else
			{
				reply.setPerformative(ACLMessage.REFUSE);
				reply.setContent(value + " est absent.");
				send(reply);
			}
		}
	}
	
	private void addNode(int value, String json, ACLMessage reply)
	{	
		if (value == m_value)
		{
			reply.setPerformative(ACLMessage.REFUSE);
			reply.setContent(value + " est deja insere.");
			send(reply);
		}
		
		else if (value > m_value)
		{
			if (m_rightNode == null)
			{
				m_rightNode = getNode(value, getLocalName() + "-right");
				
				if (m_rightNode == null)
				{
					reply.setPerformative(ACLMessage.REFUSE);
					reply.setContent("Erreur se produit");
					send(reply);
				}
				else
				{
					reply.setPerformative(ACLMessage.INFORM);
					reply.setContent(value + " est insere.");
					send(reply);
				}
			}
			else
			{
				request(m_rightNode, json, reply);
			}
		}
		else 
		{
			if (m_leftNode == null)
			{
				m_leftNode = getNode(value, getLocalName() + "-left");
				
				if (m_leftNode == null)
				{
					reply.setPerformative(ACLMessage.REFUSE);
					reply.setContent("Erreur se produit");
					send(reply);
				}
				else
				{
					reply.setPerformative(ACLMessage.INFORM);
					reply.setContent(value + " est insere.");
					send(reply);
				}
			}
			else
			{
				request(m_leftNode, json, reply);
			}
		}
	}
	
	private AID getNode(int value, String nodeName)
	{
		Object[] arg = {(Integer) value};

		try
		{
			getContainerController().createNewAgent(nodeName, "agents.Node", arg).start();

			return new AID(nodeName, AID.ISLOCALNAME);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		return null;
	}
	
	private void request(AID receiver, String json, ACLMessage reply)
	{
		ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
			
		message.addReceiver(receiver);
		
		String id = UUID.randomUUID().toString();
		message.setConversationId(id);
		
		String mirt = "rqt" + System.currentTimeMillis();
		message.setReplyWith(mirt);
		
		message.setContent(json);
			
		addBehaviour(new SendRequest(this, message, reply));
	}
	
	private class SendRequest extends AchieveREInitiator
	{
		SendRequest(Agent agent, ACLMessage msg, ACLMessage reply)
		{
			super(agent, msg);
			
			m_reply = reply;
		}
		
		//forward result
		@Override
		protected void handleInform(ACLMessage inform)
		{
			m_reply.setPerformative(ACLMessage.INFORM);
			m_reply.setContent(inform.getContent());
			send(m_reply);
		}
		
		@Override
		protected void handleRefuse(ACLMessage refuse)
		{
			m_reply.setPerformative(ACLMessage.REFUSE);
			m_reply.setContent(refuse.getContent());
			send(m_reply);
		}
		
		private ACLMessage m_reply;
	}
	
	private void getTree(String json, ACLMessage reply)
	{
		if (m_leftNode == null && m_rightNode == null)
		{
			reply.setPerformative(ACLMessage.INFORM);
			reply.setContent("(" + Integer.toString(m_value) + ")");
			send(reply);
			
			return;
		}
		
		ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
		
		if (m_leftNode != null)
		{
			message.addReceiver(m_leftNode);
		}
		
		if (m_rightNode != null)
		{
			message.addReceiver(m_rightNode);
		}
		
		String id = UUID.randomUUID().toString();
		message.setConversationId(id);
		
		String mirt = "rqt" + System.currentTimeMillis();
		message.setReplyWith(mirt);
		
		message.setContent(json);
			
		addBehaviour(new Visualization(this, message, reply));
	}
	
	private class Visualization extends AchieveREInitiator
	{
		Visualization(Agent agent, ACLMessage msg, ACLMessage reply)
		{
			super(agent, msg);
			
			m_content = Integer.toString(m_value);
			m_reply   = reply;
			
			Iterator iter = msg.getAllReceiver();
			m_nbSubTrees  = 0;
			
			while (iter.hasNext())
			{
				++m_nbSubTrees;
				iter.next();
			}
		}
		
		//forward result
		@Override
		protected void handleInform(ACLMessage inform)
		{
			if (inform.getSender().equals(m_rightNode))
			{
				m_content  = m_content + " " + inform.getContent();
				
				--m_nbSubTrees;
			}
			
			if (inform.getSender().equals(m_leftNode))
			{
				m_content  = inform.getContent() + " " + m_content;
				
				--m_nbSubTrees;
			}
			
			if (m_nbSubTrees == 0)
			{
				sendReply();
			}
		}
		
		private void sendReply()
		{
			m_reply.setPerformative(ACLMessage.INFORM);
			m_reply.setContent("(" + m_content + ")");
			
			send(m_reply);
		}
		
		private String     m_content;
		private ACLMessage m_reply;
		private int        m_nbSubTrees;
	}

	
	static public String typeService = "Binary Tree";
	static public String nameService = "Node";
	
	private int m_value;
	
	private AID m_leftNode;
	private AID m_rightNode;
}
