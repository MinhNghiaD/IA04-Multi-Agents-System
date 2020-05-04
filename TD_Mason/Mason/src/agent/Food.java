package agent;

import model.Beings;
import sim.engine.SimState;
import sim.engine.Steppable;

public class Food implements Steppable{
	
	public static int remainFood;
	
	private Beings model;
	
	public Food() {
	}
	
	
	@Override
	public void step(SimState arg0) {
		if ( remainFood == 0 ) {
			model.addFood();
		}
	}
	
	public void decreaseFood() {
		--remainFood;
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
	
	private int x;
	private int y;

	public void remove(Beings beings) {
		remainFood -= 1;
		beings.yard.remove(this);
		
		if (remainFood == 0) {
			beings.addFood();
		}
	}
}
