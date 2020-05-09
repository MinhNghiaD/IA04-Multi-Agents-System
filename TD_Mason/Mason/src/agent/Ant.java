package agent;

import model.Beings;
import model.Constants;
import sim.util.Bag;
import sim.util.Int2D;
import sim.util.IntBag;
import sim.engine.SimState;
import sim.engine.Steppable;
import sim.engine.Stoppable;
import sim.field.grid.Grid2D;
import sim.field.grid.SparseGrid2D;

public class Ant implements Steppable {
	
	private final int DISTANCE_DEPLACEMENT;
	private final int DISTANCE_PERCEPTION;
	
	private 	  int numero;
	private       int x;
	private 	  int y;
	
	private       int directionX;
	private       int directionY;
	
	//private 	  int xdir;
	//private 	  int ydir;
	
	private 	  int energy;
	private 	  int nbLoad;
	
	
	
	public Stoppable  stoppable;
	
	public Ant(Int2D location, int nb) 
	{
		x       			 = location.x;
		y					 = location.y;
		numero   			 = nb;
		
		directionX = directionY = 1;
		
		
		// TODO :  init these variables
		DISTANCE_DEPLACEMENT = 5;
		DISTANCE_PERCEPTION  = 5;
		
		energy 		 		 = Constants.MAX_ENERGY;
		nbLoad 		 		 = 0;
	}

	@Override
	public void step(SimState state) 
	{
		Beings beings = (Beings) state;
		
		// TODO 
		trategy(beings);
		
		
		if (energy == 0) 
		{
			//die(beings);
		} 
	}
	
	private void trategy(Beings being)
	{
		// Observation
		IntBag xPos   = new IntBag();
		IntBag yPos   = new IntBag();
/*
		see(being, xPos, yPos);
		
		for (int i = 0; i < xPos.size(); ++i)
		{
			int objX = xPos.get(i);
			int objY = yPos.get(i);
					
			Bag bag = being.yard.getObjectsAtLocation(objX, objY);
					
			if (bag.numObjs > 1) 
			{
				for (Object obj : bag) 
				{
					if ( obj instanceof Food ) 
					{
						// TODO food detected
					}
							
					if (obj instanceof Ant)
					{
						// TODO ant detected
					}
				}
			}
		}
*/		
		move(getRandomPosition(Constants.GRID_SIZE), being);
	}
	
	private void see(Beings state, IntBag xPos, IntBag yPos)
	{
		state.yard.getMooreLocations(x, x, DISTANCE_PERCEPTION, Grid2D.BOUNDED, false, xPos, yPos);
	}

	private void die(Beings state) 
	{	
		state.yard.remove(this);  
		stoppable.stop();
		
		System.out.println("Ant["+ this.numero +"] die!!!!");
		
		state.removeAnt();
	}
	
	/**
	 * 
	 * @param state : return the next to food
	 * @return
	 */
	private Food getNearbyFood(Beings state)
	{
		IntBag xPos   = new IntBag();
		IntBag yPos   = new IntBag();
		
		state.yard.getMooreLocations(x, x, 1, Grid2D.BOUNDED, false, xPos, yPos);
		
		for (int i = 0; i < xPos.size(); ++i)
		{
			int objX = xPos.get(i);
			int objY = yPos.get(i);
			
			Bag bag = state.yard.getObjectsAtLocation(objX, objY);
			
			if (bag.numObjs == 1) 
			{
				for (Object obj : bag) 
				{
					if ( obj instanceof Food ) 
					{
						return (Food) obj;
					}
				}
			}
		}
		
		return null;
	}
	
	/**
	 * Eat foods
	 * @param food
	 * @param amount
	 */
	private void eat(Food food, int amount) 
	{
		amount = food.consume(amount);

		energy += (amount * Constants.FOOD_ENERGY);
		
		System.out.println("---> Ant[" + numero + "] eat " + amount + " food \n");
	}
	
	/**
	 * Eat load
	 * @param amount
	 */
	private void eat(int amount) 
	{
		if (nbLoad < amount)
		{
			amount = nbLoad;
		}
		
		nbLoad -= amount;
		
		energy += (amount * Constants.FOOD_ENERGY);
		
		System.out.println("---> Ant[" + numero + "] eat " + amount + " Load \n");
	}
	
	
	private int canEatFood()
	{
		return (Constants.MAX_ENERGY - energy) / Constants.FOOD_ENERGY;
	}
	
	private void loadFood(Food food)
	{
		// try to load 1 unit of food at a time
		int amount = food.consume(1);
		
		nbLoad += amount;
				
		System.out.println("---> Ant[" + numero + "] Load " + amount + " food \n");
	}
	
	private void move(Int2D location, Beings beings)
	{
		if (location.x <= 0)
		{
			x = 1;
		}
		else if (location.x >= beings.yard.getHeight())
		{
			x = beings.yard.getHeight() - 1;
		}
		else
		{
			x = location.x;
		}
		
		if (location.y <= 0)
		{
			y = 1;
		}
		else if (location.y >= beings.yard.getWidth())
		{
			y = beings.yard.getWidth() - 1;
		}
		else
		{
			y = location.y;
		}
		
		if (! beings.yard.setObjectLocation(this, x, y))
		{
			System.out.println("---> Ant[" + numero + "] fails to move.");
			
			return;
		}
		
		--energy;
		
		System.out.println("---> Ant[" + numero + "] moves to (" + x + ", " + y + ").");
	}
	
	
	Int2D getRandomPosition(int gridSize)
	{
		int distanceX =  (int) (Math.random() * (DISTANCE_DEPLACEMENT - 1));
		int distanceY =  (int) (Math.random() * (DISTANCE_DEPLACEMENT - 1));
		
		int newX      = x + directionX * distanceX;
		int newY	  = y + directionY * distanceY;
		
		// need to avoid the corners in order to find food
		if (newX <= 0)
		{
			// change direction
			directionX = 1;
		}
		else if (newX >= (gridSize - 1))
		{
			directionX = -1;
		}
		
		if (newY <= 0)
		{
			directionY = 1;
		}
		else if (newY >= (gridSize - 1))
		{
			directionY = -1;
		}
		
		newX = x + directionX * distanceX;
		newY = y + directionY * distanceY;
		
		return (new Int2D(newX, newY));
	}
}
