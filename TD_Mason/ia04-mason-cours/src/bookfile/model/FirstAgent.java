package bookfile.model;

import sim.engine.SimState;
import sim.util.Int2D;

public class FirstAgent extends FollowingAgent {
    int xcap = 1 ;
    int ycap = 0 ;
	public FirstAgent() {
		name = 0;
		follow = -1;
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
	    Carter state = (Carter) model;
		if (ycap == Constants.YGAP && xcor <= 1) {
			xcap = 1;
			ycap = 0;
			xcor += xcap;
		}
		else if (ycap == Constants.YGAP && xcor >= Constants.GRID_SIZE - 1) {
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
			ycap = Constants.YGAP;
			xcor += xcap;
			ycor += ycap;
		}
		Int2D newPos = new Int2D(xcor, ycor);
		state.yard.setObjectLocation(this, newPos);
	}
	

}
