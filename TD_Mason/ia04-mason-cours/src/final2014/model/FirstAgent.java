package final2014.model;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.engine.Stoppable;
import sim.util.Bag;
import sim.util.Int2D;

public class FirstAgent implements Steppable {
	public int name = 0;
	public int xcor, ycor;
    int xcap = 1 ;
    int ycap = 0 ;
    int deltay = 0;
    public Stoppable stoppable;
	public FirstAgent() {
		name = 0;
	}
	@Override
	public void step(SimState model) {
		if (Constants.A_XCOR - xcor <= 1 && Constants.A_YCOR - ycor <= 1) {
			stop(model);
		}
		else {
			move(model);
		}
	}
	private void move(SimState model) {
		Final2014Model state = (Final2014Model) model;
		if (deltay == Constants.YGAP && xcor <= 1) {
			deltay = 0;
			xcap = 1;
			ycap = 0;
			xcor += xcap;
		}
		else if (deltay == Constants.YGAP && xcor >= Constants.GRID_SIZE - 1) {
			deltay = 0;
			xcap = -1;
			ycap = 0;
			xcor += xcap;
		}
		else if (xcap == 1 && xcor < Constants.GRID_SIZE - 1) {
			xcor += xcap;
		}
			
		else if (xcap == -1 && xcor > 1) {
			xcor += xcap;
		}
		
		else if (xcor >= Constants.GRID_SIZE - 1 || xcor <= 1) {
			xcap = 0;
			deltay++;
			ycap = 1;
			xcor += xcap;
			ycor += ycap;
		}
		Int2D newPos = new Int2D(xcor, ycor);
		Bag b = state.yard.getObjectsAtLocation(newPos);
		if (b != null) {
			Cell c = (Cell) b.get(0);
			c.capacity = Constants.MAX_CAPACITY;
		}
	
		state.yard.setObjectLocation(this, newPos);
	}
	protected void stop(SimState model) {
		Final2014Model state = (Final2014Model) model;
		Int2D newPos = new Int2D(Constants.A_XCOR, Constants.A_YCOR);
		state.yard.setObjectLocation(this, newPos);
		state.yard.remove(this);
		stoppable.stop();
	}

}
