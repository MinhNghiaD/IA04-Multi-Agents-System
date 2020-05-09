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
	
	private final int MAX_X;
	private final int MAX_Y;
	
	private 	  int numero;
	private       int x;
	private 	  int y;
	
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
		
		// TODO :  init these variables
		DISTANCE_DEPLACEMENT = 5;
		DISTANCE_PERCEPTION  = 5;
		MAX_X 				 = Constants.GRID_SIZE - 1;
		MAX_Y 				 = Constants.GRID_SIZE - 1;
		
		energy 		 		 = Constants.MAX_ENERGY;
		nbLoad 		 		 = 0;
	}

	@Override
	public void step(SimState state) 
	{
		Beings beings = (Beings) state;
		
		Int2D location = new Int2D(x + 1, y + 1);
		
		move(location, beings);
		
		
/*		 
		if (beings.getNumInsects() > 0)
		{
			if (energy == 0) 
			{
				die(beings);
			} 
			else 
			{
				Food food = hasFoodInCell(posAnt, yard);
				
				if (food != null) 
				{
					System.out.print("has Food ---");
					
					if (energy == Constants.MAX_ENERGY && 
					    nbLoad == Constants.MAX_LOAD) 
					{
						System.out.println("---> move to next food\n");
						
						moveToFood(getPosClosestFood(posAnt, yard), beings);
					} 
					else 
					{
						if (energy == Constants.MAX_ENERGY && 
						    nbLoad  < Constants.MAX_LOAD) 
						{
							while (food.getRemainFood() > 0 && 
								   nbLoad 				< Constants.MAX_LOAD) 
							{
								loadFood(food, beings);
							}
						}
						
						if (energy  < Constants.MAX_ENERGY && 
						    nbLoad == Constants.MAX_LOAD) 
						{
							while (food.getRemainFood() > 0 && 
							       energy 			    < Constants.MAX_ENERGY) 
							{
								eatFood(food, beings);
							}
						}
						
						if (energy < Constants.MAX_ENERGY && 
						    nbLoad < Constants.MAX_LOAD) 
						{
							
							if (food.getRemainFood() >= 2) 
							{
								do {
									if (nbLoad < Constants.MAX_LOAD) {
										loadFood(food, beings);
									}
									
									if (energy < Constants.MAX_ENERGY) 
									{
										eatFood(food, beings);
									}
									
								} 
								while (food.getRemainFood() == 0   || 
									  (energy == Constants.MAX_ENERGY && nbLoad == Constants.MAX_LOAD));
								
							} 
							else 
							{
								if (energy <= ((int) Constants.MAX_ENERGY / 2)) 
								{
									eatFood(food, beings);
								} 
								else 
								{
									loadFood(food, beings);
								}
							}
						}
					}
				} else {
					if (energy < (Constants.MAX_ENERGY - Constants.FOOD_ENERGY) && 
					    nbLoad > 0) 
					{
						eatLoad();
					}
					
					Int2D posClosetFood = getPosClosestFood(posAnt, yard);
					
					if ( posClosetFood != null ) 
					{
						moveToFood(posClosetFood, beings);
					} 
					else
					{
						moveRandom(beings);
					}
				}
			}
		} 
		else 
		{
			beings.finish();
		}
*/		
	}
	
	private void trategy()
	{
		// TODO implement strategy
	}

	private void die(Beings state) 
	{	
		state.yard.remove(this);  
		state.decNumInsects();
		
		stoppable.stop();
		
		System.out.println("Ant["+ this.numero +"] die!!!!");
	}
	
	private void see(Beings state)
	{
		// TODO get matrix from simulation
		IntBag xPos   = new IntBag();
		IntBag yPos   = new IntBag();

		state.yard.getMooreLocations(x, x, DISTANCE_PERCEPTION, Grid2D.BOUNDED, false, xPos, yPos);
		
		for (int i = 0; i < xPos.size(); ++i)
		{
			int objX = xPos.get(i);
			int objY = yPos.get(i);
			
			Bag bag = state.yard.getObjectsAtLocation(objX, objY);
			
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
		x = beings.yard.stx(location.x);
		y = beings.yard.sty(location.y);
		
		if (! beings.yard.setObjectLocation(this, x, y))
		{
			System.out.println("---> Ant[" + numero + "] fails to move.");
			
			return;
		}
		
		System.out.println("---> Ant[" + numero + "] moves to (" + x + ", " + y + ").");
	}
	
		
/*	
		

	private void moveToFood(Int2D foodLoca, Beings beings) 
	{
		System.out.print("Ant[" + numero + "] Move to food ");
		
		changeLocation(posClosetFood.x, posClosetFood.y, beings);
		energy--;
	}

	


	
	private Int2D getPosClosestFood(Int2D posAnt, SparseGrid2D yard) 
	{	
		System.out.print("Ant["+this.numero+"] getPosClosestFood---");
		
		int    high    = DISTANCE_PERCEPTION;
		int    low     = DISTANCE_PERCEPTION * (-1);
		int    right   = DISTANCE_PERCEPTION;
		int    left    = DISTANCE_PERCEPTION * (-1);
		double minDist = Double.POSITIVE_INFINITY;
		
		Int2D posClosestFood = null;
		
		for (int i = low; i <= high; i++) 
		{
			for (int j = left; j <= right; j++) 
			{
				int testx = posAnt.x + i;
				int testy = posAnt.y + j;
				
				if (testx > MAX_X) 
				{
					testx = MAX_X;
				}
				
				if (testy > MAX_Y) 
				{
					testy = MAX_Y;
				}
				
				if (testx != this.x && testy != this.y) 
				{
					Int2D posCell = new Int2D(testx, testy);
					Food  food    = hasFoodInCell(posCell, yard);

					if (food != null) 
					{
						System.out.println("---Food in zone---");
						
						if (posAnt.distance(posCell) < minDist) 
						{
							minDist        = posAnt.distance(posCell);
							posClosestFood = posCell;
						}
					}		
				}
			}
		}
		
		if (posClosestFood != null) 
		{
			System.out.println("PosClosestFood : " + posClosestFood.toCoordinates());
		}
		
		return posClosestFood;
	}

	private Food hasFoodInCell(Int2D posAnt, SparseGrid2D yard) 
	{	
		Bag bag = yard.getObjectsAtLocation(posAnt.x, posAnt.y);
		
		if (bag == null || (bag.numObjs <= 0)) return null;
		
		if (bag.numObjs > 1) 
		{
			for (Object obj : bag) 
			{
				if ( obj instanceof Food ) 
				{
					return (Food) obj;
				}
			}
		}
		
		return null;
	}

	
	
	
	
	public void moveRandom(Beings beings)
	{
		System.out.println("move Random");
		
		// générer aléatoire des directions pour ne pas franchîr la frontière et la distance de déplacement
		if (x == 0) 
		{
			// 0 <= xdir <= DISTANCE_DEPLACEMENT
			xdir = beings.random.nextInt(DISTANCE_DEPLACEMENT + 1);
		} 
		else if (x == MAX_X) 
		{
			// -DISTANCE_DEPLACEMENT <= xdir <= 0
			xdir = beings.random.nextInt(DISTANCE_DEPLACEMENT + 1) * (-1);
		} 
		else 
		{
			// -DISTANCE_DEPLACEMENT <= xdir <= DISTANCE_DEPLACEMENT
			xdir = beings.random.nextInt(DISTANCE_DEPLACEMENT + 1) * (beings.random .nextBoolean() ? -1 : 1);
		}
		
		if (y == 0) 
		{
			ydir = beings.random.nextInt(DISTANCE_DEPLACEMENT + 1);
			
			while(ydir == 0  && xdir == 0) 
			{
				ydir = beings.random.nextInt(DISTANCE_DEPLACEMENT + 1);
			}
		}
		else if (y == MAX_Y) 
		{
			ydir = beings.random.nextInt(DISTANCE_DEPLACEMENT + 1) * (-1);
			
			while(ydir == 0  && xdir == 0) 
			{
				ydir = beings.random.nextInt(DISTANCE_DEPLACEMENT + 1) * (-1);
			}
		} 
		else 
		{
			ydir = beings.random.nextInt(DISTANCE_DEPLACEMENT + 1) * (beings.random .nextBoolean() ? -1 : 1);
			
			while(ydir == 0  && xdir == 0) 
			{
				ydir = beings.random.nextInt(DISTANCE_DEPLACEMENT + 1) * (beings.random .nextBoolean() ? -1 : 1);
			}
		}
		
		// déplacer l'insecte
		int tempx = x + xdir;
		int tempy = y + ydir;
		
		if (tempx > MAX_X) 
		{
			tempx = MAX_X;
		}
		
		if (tempx < 0) 
		{
			tempx = 0;
		}
		
		if (tempy < 0)
		{
			tempy = 0;
		}
		
		changeLocation(tempx, tempy, beings);
		
		energy--;	
	}
*/	
}
