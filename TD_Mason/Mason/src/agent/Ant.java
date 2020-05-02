package agent;

import model.Beings;
import sim.engine.SimState;
import sim.engine.Steppable;

public class Ant implements Steppable {
	
	private final int DISTANCE_DEPLACEMENT;
	private final int DISTANCE_PERCEPTION;
	private final int CHARGE_MAX;
	
	private int x;
	private int y;
	private int energy;
	private int effectiveCharge;
	private Beings model;
	
	public Ant() {
		DISTANCE_DEPLACEMENT = 5;
		DISTANCE_PERCEPTION = 3;
		CHARGE_MAX = 1;
	}

	@Override
	public void step(SimState model) {
		
		
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
	
}
