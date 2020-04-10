package src.agents;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import src.objects.Cell;

public class Environnement extends Agent {

	@Override
	protected void setup() {
		register();
		
		System.out.println("Agent " + getLocalName() + " init!");	
/*		
		Object[] args = getArguments();
		
		// Récupérer la grille sudoku passé en paramètre

		if (args.length != 1)
		{
			System.out.println("Faux Nombre d'arguments!");	
			
			return;
		}    
*/    
		addBehaviour(new MainHandler());
		
		//p_solution = false;
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
	
	
	/*--------------------------------------Attributes---------------------------------------------*/
		
	static public String typeService 	  = "Update";
	static public String nameService 	  = "Etat";
	static public Boolean p_solution;

}
