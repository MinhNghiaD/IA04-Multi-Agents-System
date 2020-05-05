package agent;

import model.Beings;
import model.Constants;
import sim.util.Bag;
import sim.util.Int2D;
import sim.engine.SimState;
import sim.engine.Steppable;
import sim.engine.Stoppable;
import sim.field.grid.SparseGrid2D;

public class Ant implements Steppable {
	
	private final int DISTANCE_DEPLACEMENT;
	private final int DISTANCE_PERCEPTION;
	private final int MAX_X;
	private final int MAX_Y;
	
	private int x;
	private int y;
	private int xdir;
	private int ydir;
	private int energy;
	private int nbLoad;
	private int numero;
	
	public Stoppable stoppable;
	
	public Ant() {
		DISTANCE_DEPLACEMENT = 5;
		DISTANCE_PERCEPTION = 5;
		MAX_X = Constants.GRID_SIZE - 1;
		MAX_Y = Constants.GRID_SIZE - 1;
		
		this.energy = Constants.MAX_ENERGY;
		this.nbLoad = 0;
	}

	@Override
	public void step(SimState state) {
		Beings beings = (Beings) state;
		SparseGrid2D yard = beings.yard;
		Int2D posAnt = yard.getObjectLocation(this);
		 
		if (beings.getNumInsects() > 0) {
			if (energy == 0) {
				suicide(beings);
			} else {
				Food food = hasFoodInCell(posAnt, yard);
				
				if (food != null) {
					System.out.print("has Food ---");
					
					if (this.energy == Constants.MAX_ENERGY && this.nbLoad == Constants.MAX_LOAD) {
						System.out.println("---> move to next food\n");
						moveToFood(getPosClosestFood(posAnt, yard), beings);
					} else {
						if (this.energy == Constants.MAX_ENERGY && this.nbLoad < Constants.MAX_LOAD) {
							while (food.getRemainFood() > 0 && this.nbLoad < Constants.MAX_LOAD) {
								loadFood(food, beings);
							}
						}
						
						if (this.energy < Constants.MAX_ENERGY && this.nbLoad == Constants.MAX_LOAD) {
							while (food.getRemainFood() > 0 && this.energy < Constants.MAX_ENERGY) {
								eatFood(food, beings);
							}
						}
						
						if (this.energy < Constants.MAX_ENERGY && this.nbLoad < Constants.MAX_LOAD) {
							
							if (food.getRemainFood() >= 2) {
								do {
									if (this.nbLoad < Constants.MAX_LOAD) {
										loadFood(food, beings);
									}
									
									if (this.energy < Constants.MAX_ENERGY) {
										eatFood(food, beings);
									}
									
								} while (food.getRemainFood() == 0 || (this.energy == Constants.MAX_ENERGY && this.nbLoad == Constants.MAX_LOAD));
								
							} else {
								if (this.energy <= ((int) Constants.MAX_ENERGY / 2)) {
									eatFood(food, beings);
								} else {
									loadFood(food, beings);
								}
							}
							
						}
							
					}
				} else {
					if (this.energy < Constants.MAX_ENERGY - Constants.FOOD_ENERGY && this.nbLoad > 0) {
						consumeLoad();
					}
					
					Int2D posClosetFood = getPosClosestFood(posAnt, yard);
					
					if ( posClosetFood != null ) {
						moveToFood(posClosetFood, beings);
					} else {
						moveRandom(beings);
					}
				}
			}
		} else {
			beings.finish();
		}
		
	}
		
	private void consumeLoad() {
		this.nbLoad -= 1;
		this.energy += Constants.FOOD_ENERGY;
		System.out.println("---> Ant[" + this.numero + "] eat Load \n");
	}

	private void moveToFood(Int2D posClosetFood, Beings beings) {
		System.out.print("Ant["+this.numero+"] Move to food ");
		changeLocation(posClosetFood.x, posClosetFood.y, beings);
		energy--;
		
	}

	private void eatFood(Food food, Beings beings) {
		System.out.println("---> Ant[" + this.numero + "] Eat food \n");
		this.energy += Constants.FOOD_ENERGY;
		
		if (this.energy > Constants.MAX_ENERGY) {
			this.energy = Constants.MAX_ENERGY;
		}
		food.remove(beings);
	}

	private void loadFood(Food food, Beings beings) {
		System.out.println("---> Ant[" + this.numero + "] Load food \n");
		this.nbLoad += 1;
		food.remove(beings);
	}

	
	private Int2D getPosClosestFood(Int2D posAnt, SparseGrid2D yard) {
		
		System.out.print("Ant["+this.numero+"] getPosClosestFood---");
		
		int high = DISTANCE_PERCEPTION;
		int low = DISTANCE_PERCEPTION * -1;
		int right = DISTANCE_PERCEPTION;
		int left = DISTANCE_PERCEPTION * -1;
		double minDist = Double.POSITIVE_INFINITY;
		Int2D posClosestFood = null;
		
		for (int i = low; i <= high; i++) {
			for (int j = left; j <= right; j++) {
				int testx = posAnt.x + i;
				int testy = posAnt.y + j;
				
				if (testx > MAX_X) {
					testx = MAX_X;
				}
				
				if (testy > MAX_Y) {
					testy = MAX_Y;
				}
				
				if (testx != this.x && testy != this.y) {
					Int2D posCell = new Int2D(testx, testy);
					Food food = hasFoodInCell(posCell, yard);

					if (food != null) {
						System.out.println("---Food in zone---");
						if (posAnt.distance(posCell) < minDist) {
							minDist = posAnt.distance(posCell);
							posClosestFood = posCell;
						}
					}		
				}
			}
		}
		
		if (posClosestFood != null) {
			System.out.println("PosClosestFood : " + posClosestFood.toCoordinates());
		}
		
		return posClosestFood;
	}

	private Food hasFoodInCell(Int2D posAnt, SparseGrid2D yard) {
		
		Bag bag = yard.getObjectsAtLocation(posAnt.x, posAnt.y);
		
		if (bag == null || (bag.numObjs <= 0)) return null;
		
		if (bag.numObjs > 1) {
			for (Object o : bag) {
				if ( o instanceof Food ) {
					return (Food) o;
				}
			}
		}
		
		return null;
	}

	private void suicide(SimState state) {
		System.out.println("Ant["+ this.numero +"]SUICIDE!!!!");
		Beings beings = (Beings) state;
		beings.yard.remove(this);  
		beings.decNumInsects();
		stoppable.stop();
	}
	
	
	
	public void moveRandom(Beings beings){
		System.out.println("move Random");
		
		// générer aléatoire des directions pour ne pas franchîr la frontière et la distance de déplacement
		if (this.x == 0) {
			// 0 <= xdir <= DISTANCE_DEPLACEMENT
			this.xdir = beings.random.nextInt(DISTANCE_DEPLACEMENT + 1);
		} else if (this.x == MAX_X) {
			// -DISTANCE_DEPLACEMENT <= xdir <= 0
			this.xdir = beings.random.nextInt(DISTANCE_DEPLACEMENT + 1) * - 1;
		} else {
			// -DISTANCE_DEPLACEMENT <= xdir <= DISTANCE_DEPLACEMENT
			this.xdir = beings.random.nextInt(DISTANCE_DEPLACEMENT + 1) * (beings.random .nextBoolean() ? -1 : 1);
		}
		
		if (this.y == 0) {
			this.ydir = beings.random.nextInt(DISTANCE_DEPLACEMENT + 1);
			
			while(this.ydir == 0  && this.xdir == 0) {
				this.ydir = beings.random.nextInt(DISTANCE_DEPLACEMENT + 1);
			}
		} else if (this.y == MAX_Y) {
			this.ydir = beings.random.nextInt(DISTANCE_DEPLACEMENT + 1) * - 1;
			
			while(this.ydir == 0  && this.xdir == 0) {
				this.ydir = beings.random.nextInt(DISTANCE_DEPLACEMENT + 1) * - 1;
			}
		} else {
			this.ydir = beings.random.nextInt(DISTANCE_DEPLACEMENT + 1) * (beings.random .nextBoolean() ? -1 : 1);
			
			while(this.ydir == 0  && this.xdir == 0) {
				this.ydir = beings.random.nextInt(DISTANCE_DEPLACEMENT + 1) * (beings.random .nextBoolean() ? -1 : 1);
			}
		}
		
		
		// déplacer l'insecte
		int tempx = this.x + this.xdir;
		int tempy = this.y + this.ydir;
		
		if ( tempx > MAX_X) {
			tempx = MAX_X;
		}
		
		if ( tempx < 0) {
			tempx = 0;
		}
		
		if ( tempy < 0) {
			tempy = 0;
		}
		
		changeLocation(tempx, tempy, beings);
		energy--;	
	}

	
	
	
	private void changeLocation(int tempx, int tempy, Beings beings) {
		beings.yard.stx(tempx);
		beings.yard.sty(tempy);
		beings.yard.setObjectLocation(this, tempx, tempy);
		this.x = tempx;
		this.y = tempy;
		
		System.out.println("Ant["+ this.numero +"] Move to ["+ this.x + "," + this.y +"]");
	}
	
	
	public int getnumero() {
		return numero;
		}
	
	public void setnumero(int numero) {
		this.numero = numero; }

	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public void setXdir(int xdir) {
		this.xdir = xdir;
	}
	
	public void setYdir(int ydir) {
		this.ydir = ydir;
	}
	
	public int getXdir() {
		return xdir;
	}
	
	public int getYdir() {
		return ydir;
	}
	
}
