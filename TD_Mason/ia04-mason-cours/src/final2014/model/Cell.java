package final2014.model;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.util.Int2D;

public class Cell implements Steppable {
	public Int2D location;
	public int capacity = 0;
	@Override
	public void step(SimState arg0) {
		if (capacity > 0)
			capacity -=10 ;
	}

	

}
