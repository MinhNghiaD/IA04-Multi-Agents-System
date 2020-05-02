package agent;

import sim.engine.SimState;
import sim.engine.Steppable;

public class Food implements Steppable{
	public Food() {
		// TODO Auto-generated constructor stub
	}
	
	
	@Override
	public void step(SimState arg0) {
		// TODO Auto-generated method stub
		
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
}
