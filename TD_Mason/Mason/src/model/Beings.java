package model;

import agent.Food;
import agent.Ant;
import sim.engine.SimState;
import sim.field.grid.SparseGrid2D;
import sim.util.Int2D;

public class Beings extends SimState {
	/***
	 * DES PARAMÈTRES
	 */
	public SparseGrid2D yard = new SparseGrid2D(Constants.GRID_SIZE,Constants.GRID_SIZE);
	
	public Beings(long seed) {
	  super(seed);
	}
	
	
	public void start() {
		super.start(); 
		yard.clear(); 
		addAnt();
		addFood();
	}
	
	
	/***
	 * Distribuer aléatoirement les Antes sur le champ
	 */
	private void addFood() {
		for(int i = 0; i < Constants.NUM_FOOD_CELL; i++){
			Food a = new Food();
			Int2D location = getFreeLocation(); 
			yard.setObjectLocation(a,location.x,location.y);  
			a.setX(location.x);
			a.setY(location.y); 
			schedule.scheduleRepeating(a);
		}	
		
	}

	
	/***
	 * Distribuer aléatoirement la nourriture sur le champ
	 */
	private void addAnt() {
		for(int i = 0; i < Constants.NUM_INSECT; i++){
			Ant a = new Ant();
			Int2D location = getFreeLocation(); 
			yard.setObjectLocation(a,location.x,location.y); 
			a.setX(location.x);
			a.setY(location.y); 
			schedule.scheduleRepeating(a);
		}	
	}

	
	/***
	 * Renvoie la position initiale d'un agent
	 */
	private Int2D getFreeLocation() {
		Int2D location = new Int2D(random.nextInt(yard.getWidth()),random.nextInt(yard.getHeight()) ); 
		Object ag;
		
		while ((ag = yard.getObjectsAtLocation(location.x,location.y)) != null) { 
			location = new Int2D(random.nextInt(yard.getWidth()),
			random.nextInt(yard.getHeight()) );
		}
		return location;
	}

	
}