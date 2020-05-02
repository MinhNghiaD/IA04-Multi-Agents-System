package bookfile.model;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.engine.Stoppable;
import sim.util.Int2D;

public class FollowingAgent implements Steppable {
	public int name = 0;
	public int xcor, ycor;
    public int number;
    public int follow = -1;
    public Stoppable stoppable;
	@Override
	public void step(SimState model) {
		if (Constants.A_XCOR - xcor <= 1 && Constants.A_YCOR - ycor <= 1) {
			stop(model);
		}
		else {
			move(model);
		}
	}
	protected void stop(SimState model) {
		Carter state = (Carter) model;
		Int2D newPos = new Int2D(Constants.A_XCOR, Constants.A_YCOR);
		state.yard.setObjectLocation(this, newPos);
		state.yard.remove(this);
		stoppable.stop();
	}
	private void move(SimState model) {
		Carter state = (Carter) model;
		FollowingAgent f = state.positions.get(follow);
		int xf = f.xcor;
		int yf = f.ycor;
		xcor = (xcor + xf)/2;
		ycor = (ycor + yf)/2;
		Int2D newPos = new Int2D(xcor, ycor);
		state.yard.setObjectLocation(this, newPos);
	}
	
}
