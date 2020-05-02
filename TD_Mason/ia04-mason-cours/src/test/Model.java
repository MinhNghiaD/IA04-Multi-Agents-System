package test;

import sim.engine.SimState;
import sim.field.grid.ObjectGrid2D;
import sim.util.Int2D;

public class Model extends SimState {
	public ObjectGrid2D yard = new ObjectGrid2D(20,20);

	public Model(long seed) {
		super(seed);
		
	}
	private void createAgent() {
		Agent agent = new Agent();
		Int2D location = new Int2D(10,10);
		yard.set(location.x, location.y, agent);
		agent.x = location.x;
		agent.y = location.y;
		schedule.scheduleRepeating(agent);
	}
	@Override
	public void start() {
		// TODO Auto-generated method stub
		super.start();
		createAgent();
	}
	
}
