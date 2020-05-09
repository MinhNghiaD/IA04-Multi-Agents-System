package model;

import agent.Food;
import agent.Ant;
import sim.engine.SimState;
import sim.engine.Stoppable;
import sim.field.grid.SparseGrid2D;
import sim.util.Int2D;

public class Beings extends SimState 
{
	/***
	 * DES PARAMÈTRES
	 */
	public  SparseGrid2D yard = new SparseGrid2D(Constants.GRID_SIZE,Constants.GRID_SIZE);
	private int 	     numInsects;
	
	public Beings(long seed) 
	{
	  super(seed);
	}
	
	
	public void start() 
	{
		super.start(); 
		
		yard.clear(); 
	
		for(int i = 0; i < Constants.NUM_INSECT; i++)
		{
			addAnt(i + 1);
		}	
		
		for(int i = 0; i < Constants.NUM_FOOD_CELL; i++)
		{
			addFood();
		}
	
		numInsects = Constants.NUM_INSECT;
	}

	/***
	 * Distribuer aléatoirement les Antes sur le champ
	 */
	public void addFood() 
	{
		int   nbFood   = this.random.nextInt(Constants.MAX_FOOD) + 1;
		
		Int2D location = getFreeLocation();
		Food  a        = new Food(nbFood, this, location);
		
		yard.setObjectLocation(a, location.x, location.y);
	}	
		

	
	/***
	 * Distribuer aléatoirement la nourriture sur le champ
	 */
	private void addAnt(int nb) 
	{
		Int2D location = getFreeLocation(); 
		Ant a 		   = new Ant(location, nb, 3, 3, 4);
		
		yard.setObjectLocation(a, location.x, location.y); 
		
		Stoppable stoppable = schedule.scheduleRepeating(a); 
		
		a.stoppable = stoppable;
	}

	
	/***
	 * Renvoie la position ou il n'y pas d'objet
	 */
	private Int2D getFreeLocation()
	{
		Int2D location;
		
		do
		{
			location = new Int2D(random.nextInt(yard.getWidth()), random.nextInt(yard.getHeight()));
		}
		while(yard.getObjectsAtLocation(location.x, location.y) != null);
		
		return location;
	}
		
	public void removeAnt() 
	{
		--numInsects;
		
		if (numInsects == 0)
		{
			finish();
		}
	}
}
