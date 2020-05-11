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
	private final int CHARGE_MAX;
	
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
	
	public Ant(Int2D location, int nb, int movePoint, int visionPoint, int loadPoint) 
	{
		x       			 = location.x;
		y					 = location.y;
		numero   			 = nb;
		
		directionX = directionY = 1;
		
		
		// TODO :  init these variables
		DISTANCE_DEPLACEMENT = movePoint;
		DISTANCE_PERCEPTION  = visionPoint;
		CHARGE_MAX			 = loadPoint;
		
		energy 		 		 = Constants.MAX_ENERGY;
		nbLoad 		 		 = 0;
	}

	@Override
	public void step(SimState state) 
	{
		Beings beings = (Beings) state;
		
		// TODO 
		trategy(beings);
		
		System.out.println("Ant["+ this.numero +"] anergy left " + energy);
		if (energy == 0) 
		{
			die(beings);
		} 
	}
	
	private void trategy(Beings being)
	{
		Food food = getNearbyFood(being);
		
		// if have food nearby
		if (food != null)
		{
			int amount = amountCanEat();
			
			if (amount > 0)
			{
				 eat(food, amount); 
			}
			else
			{
				// move to food for charging
				move(food.location, being);
			}
			
			return;
		}
		
		// try to load food
		if (loadFood(being))
		{
			return;
		}
		
		// eat load
		if ((energy == 1 && nbLoad > 0) || (nbLoad == CHARGE_MAX && amountCanEat() > 0))
		{
			eat(amountCanEat());
		
			return;	
		}
		
		// go to new location
		// Observation
		IntBag xPos   = new IntBag();
		IntBag yPos   = new IntBag();

		Bag objects = see(being, xPos, yPos);
				
		int index = closestPoint(xPos, yPos);
		
		if (index > -1)
		{
			move(optimiestLocation(xPos.get(index), yPos.get(index)), being);
		}
		else
		{
			move(getRandomPosition(Constants.GRID_SIZE), being);
		}
	}
	
	
	private int closestPoint(IntBag xPos, IntBag yPos)
	{
		double distance2 = (double) (Constants.GRID_SIZE * 2);
		int index     = -1;
		
		
		for (int i = 0; i < xPos.size(); ++i)
		{
			if (Math.sqrt((xPos.get(i)-x)*(xPos.get(i)-x) + (yPos.get(i)-y)*(yPos.get(i)-y))  < distance2)
			{
				distance2 = (xPos.get(i)-x)*(xPos.get(i)-x) + (yPos.get(i)-y)*(yPos.get(i)-y);
				index     = i;
			}
		}
		
		return index;
	}
	
	private Int2D optimiestLocation(int xFood, int yFood)
	{
		int xPos, yPos;
		
		if (xFood > x)
		{
			if (xFood > (x + DISTANCE_DEPLACEMENT + 1))
			{
				xPos = x + DISTANCE_DEPLACEMENT;
			}
			else
			{
				xPos = xFood - 1;
			}
		}
		else
		{
			if (xFood < (x - DISTANCE_DEPLACEMENT - 1))
			{
				xPos = x - DISTANCE_DEPLACEMENT;
			}
			else
			{
				xPos = xFood + 1;
			}
		}
		
		if (yFood > y)
		{
			if (yFood > (y + DISTANCE_DEPLACEMENT + 1))
			{
				yPos = y + DISTANCE_DEPLACEMENT;
			}
			else
			{
				yPos = yFood - 1;
			}
		}
		else
		{
			if (yFood < (y - DISTANCE_DEPLACEMENT - 1))
			{
				yPos = y - DISTANCE_DEPLACEMENT;
			}
			else
			{
				yPos = yFood + 1;
			}
		}
		
		return (new Int2D(xPos, yPos));
	}
	
	
	
	
	
	private Bag see(Beings state, IntBag xPos, IntBag yPos)
	{
		Bag objects = new Bag();
		
		state.yard.getMooreNeighborsAndLocations(x, y, DISTANCE_PERCEPTION, Grid2D.BOUNDED, objects, xPos, yPos);

		int i = 0;
		
		while (i < objects.size())
		{
			if (objects.get(i) instanceof Ant)
			{
				objects.remove(i);
				xPos.remove(i);
				yPos.remove(i);
			}
			else
			{
				++i;
			}
		}
		return objects;
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
		
		Bag objects = new Bag();
		
		state.yard.getMooreNeighborsAndLocations(x, y, 1, Grid2D.BOUNDED, objects, xPos, yPos);
		
		for (int i = 0; i < objects.size(); ++i)
		{
			if ((objects.get(i) instanceof Food) && (xPos.get(i) != x || yPos.get(i) != y))
			{	
				if (xPos.get(i) != x || yPos.get(i) != y)
				{
					return (Food) objects.get(i);
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
		
		System.out.println("---> Ant[" + numero + "] eat " + amount + " Load, amount of load left = " + nbLoad + "\n");
	}
	
	
	private int amountCanEat()
	{
		return (Constants.MAX_ENERGY - energy) / Constants.FOOD_ENERGY;
	}
	
	private boolean loadFood(Beings being)
	{
		// test if have food at the same location
		if (!canLoad())
		{
			return false;
		}
		
		Bag objects = being.yard.getObjectsAtLocation(x, y);
		
		Food food   = null;
		
		for (int i = 0; i < objects.size(); ++i)
		{
			if ((objects.get(i) instanceof Food))
			{	
				food = (Food) objects.get(i);
				
				break;
			}
		}
		
		if (food == null)
		{
			return false;
		}
		
		// try to load 1 unit of food at a time
		int amount = food.consume(1);
		
		if (amount <= 0)
		{
			return false;
		}
		
		nbLoad += amount;
				
		System.out.println("---> Ant[" + numero + "] Load " + amount + " food \n");
		
		return true;
	}
	
	private boolean canLoad()
	{
		return (CHARGE_MAX > nbLoad);
	}
	
	private void move(Int2D location, Beings beings)
	{
		int oldX = x, oldY = y;
		
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
		
		System.out.println("---> Ant[" + numero + "] moves from (" + oldX + ", " + oldY + ") to (" + x + ", " + y + ").");
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
