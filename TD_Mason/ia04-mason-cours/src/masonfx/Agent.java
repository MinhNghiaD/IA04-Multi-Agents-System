package masonfx;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import sim.engine.SimState;
import sim.engine.Steppable;

public class Agent implements Steppable {
	LongProperty count;
	Long interval; 
long time = 0;
	public Agent(Long interval) {
		super();
		//this.interval = interval;
		this.interval = 0L;
		count = new SimpleLongProperty(0);
	}

	@Override
	public void step(SimState arg0) {
		time++;
		//if ((time + interval) % 20000000 == 0) {
			count.setValue(count.getValue() + 1);
			System.out.println("Agent: step -> " + count.getValue());
		//}
		
	}

	public LongProperty countProperty() {
		return count;
	}
}
