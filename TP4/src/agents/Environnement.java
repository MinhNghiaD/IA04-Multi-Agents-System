package src.agents;

import java.util.Vector;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import src.objects.Cell;

public class Environnement extends Agent {

	@Override
	protected void setup() {
		register();
		
		System.out.println("Agent " + getLocalName() + " init!");	

		addBehaviour(new MainHandler());
	}
	

	@Override
	protected void takeDown() {
		
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
	
	/**
	 * Classe MainHandler héritée de CyclicBehaviour
	 * Cette classe est en charge de la réception des mises à jour provenance d'agents Analyseur
	 *
	 */
	private class MainHandler extends CyclicBehaviour 
	{
		@Override
		public void action() 
		{
			 MessageTemplate msgTemplate = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
			 
			 ACLMessage msg = receive(msgTemplate);
			 
			 if (msg != null) 
			 { 
				 try 
				 {
					 System.out.println( getLocalName() + " receive from " + msg.getSender().getLocalName() + ". Message : " + msg.getContent());
					 
					 Cell cell = new Cell(msg.getContent());
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
	
	/**
	 * Chercher les analyseurs via la page jaune
	 * @return AID
	 */
	@SuppressWarnings("null")
	private Vector<AID> searchAnalyseur() 
	{
		DFAgentDescription template = new DFAgentDescription();

		ServiceDescription sd = new ServiceDescription();
		sd.setType(Analyseur.typeService);
		sd.setName(Analyseur.nameService);
		
		template.addServices(sd);

		Vector<AID> listReceiver = new Vector<>();

		try 
		{
			DFAgentDescription[] result = DFService.search(this, template, sc); 	

			if (result.length > 0 ) {

				for (int i = 0; i < result.length; i++) 
				{		
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
	
	
	/*--------------------------------------Attributes---------------------------------------------*/
		
	static public String typeService 	  = "Update";
	static public String nameService 	  = "Etat";
}
