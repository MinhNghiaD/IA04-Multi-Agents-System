package masonfx;

import java.util.Arrays;
import java.util.List;

import sim.engine.SimState;
import sim.field.continuous.Continuous2D;
import sim.util.Double2D;

public class MasonModel extends SimState {
	public Continuous2D yard = new Continuous2D(1.0, 100, 100);
	List<Agent> agents;
	public MasonModel(long seed) {
		super(seed);
		System.out.println("Model created");
		createAgent();
		MasonFXApplication.controller.setModel(this);
	}
private void createAgent() {	
	agents = Arrays.asList(new Agent(10000000L),new Agent(20000000L));
}
	@Override
	public void start() {
		// TODO Auto-generated method stub
		super.start();
		yard.clear();
		// add some students to the yard
     agents.forEach(agent -> {
		yard.setObjectLocation(agents.get(0), new Double2D(yard.getWidth() * 0.5 + random.nextDouble() - 0.5,
				yard.getHeight() * 0.5 + random.nextDouble() - 0.5));
		schedule.scheduleRepeating(1000000,3000,agent,2000000);
     });
	}

}
