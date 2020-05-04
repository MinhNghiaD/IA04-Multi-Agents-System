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
	private final int CHARGE_MAX;
	
	private int x;
	private int y;
	private int xdir;
	private int ydir;
	private int energy;
	private int nbLoad;
	
	public Stoppable stoppable;
	
	public Ant() {
		DISTANCE_DEPLACEMENT = 3;
		DISTANCE_PERCEPTION = 5;
		CHARGE_MAX = 3;
		
		this.energy = Constants.MAX_ENERGY;
		this.nbLoad = 0;
	}

	@Override
	public void step(SimState state) {
		Beings beings = (Beings) state;
		SparseGrid2D yard = beings.yard;
		Int2D posAnt = yard.getObjectLocation(this);
		 
		if (energy == 0) {
			suicide(beings);
		} else {
			Food food = hasFoodInCell(posAnt, yard); //TODO NO FUNCTIOIN
			
			if (food != null) {
				System.out.print("has Food\n");
				if (this.energy == Constants.MAX_ENERGY && this.nbLoad == Constants.MAX_LOAD) {
					move(beings);
				} else {
					if ((this.energy == Constants.MAX_ENERGY && this.nbLoad < Constants.MAX_LOAD)
						|| (this.energy < Constants.MAX_ENERGY && this.energy > Constants.MAX_ENERGY - 3 && this.nbLoad < Constants.MAX_LOAD)) {
						loadFood(food, beings);
					} else {
						eatFood(food, beings);
					}
				}
			} else {
				Int2D posClosetFood = getPosClosestFood(posAnt, yard); //TODO NO FUNCTIOIN
				if ( posClosetFood != null ) {
					moveToFood(posClosetFood, yard);
				} else {
					move(beings);
				}
			}
		}
	}
		
	private void moveToFood(Int2D posClosetFood, SparseGrid2D yard) {//TODO NO FUNCTIOIN
		energy--;
		System.out.print("Move to food");
	}

	private void eatFood(Food food, Beings beings) {
		this.energy += Constants.FOOD_ENERGY;
		
		if (this.energy > Constants.MAX_FOOD) {
			this.energy = Constants.MAX_FOOD;
		}
		food.remove(beings);
	}

	private void loadFood(Food food, Beings beings) {
		this.nbLoad += 1;
		food.remove(beings);
	}

	
	private Int2D getPosClosestFood(Int2D posAnt, SparseGrid2D yard) {
		double high = (double) DISTANCE_PERCEPTION;
		double low = (double) DISTANCE_PERCEPTION * -1;
		double right = (double) DISTANCE_PERCEPTION * -1;
		double left = (double) DISTANCE_PERCEPTION;
		double minDist = Double.POSITIVE_INFINITY;
		Int2D posClosestFood = null;
		
		for (double i = low; i <= high; i++) {
			for (double j = left; i <= right; i++) {
				int testx = (int) ((int) posAnt.x + i);
				int testy = (int) ((int) posAnt.y + j);
				Int2D posCell = new Int2D(testx, testy);
				
				if (hasFoodInCell(posCell, yard) != null) {
					if (posAnt.distance(posCell) < minDist) {
						minDist = posAnt.distance(posCell);
						posClosestFood = posCell;
					}
				}
			}
		}
		return posClosestFood;
	}

	private Food hasFoodInCell(Int2D posAnt, SparseGrid2D yard) {
		Bag bag = yard.getObjectsAtLocation(posAnt.x, posAnt.y);
		
		if (bag == null || (bag.numObjs <= 0)) return null;
		
		if (bag.numObjs > 1) {
			Object[] listObj = bag.toArray(); //NON FUNCTION
			
			for (Object o : listObj) {
				if ( o instanceof Food ) {
					System.out.print("Food at " + posAnt.toString() + "\n");
					return (Food) o;
				}
			}
		}
		
		return null;
	}

	private void suicide(SimState state) {
		Beings beings = (Beings) state;
		beings.yard.remove(this); //model est le SimState 
		stoppable.stop();
	}
	
	
	
	public void move(Beings beings){
		System.out.print("Move random");
		// générer la direction aléatoire dans la grid
		do {
			this.xdir = beings.random.nextInt(3)-1;
			this.ydir = beings.random.nextInt(3)-1;
		} while (this.xdir != 0 || this.ydir != 0);
		
		int tempx = beings.yard.stx(this.x + this.xdir);
		int tempy = beings.yard.sty(this.y + this.ydir);

		// TODO ADD CONDITION OF GRID SIZE
		x = tempx;
		y = tempy;
		beings.yard.setObjectLocation(this, x, y);
		energy--;
		
	}

	
	
	
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
