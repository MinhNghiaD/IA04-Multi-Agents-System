package final2017;

import sim.engine.SimState;
import sim.engine.Steppable;

public class Ball implements Steppable {
 public int x, y;
 public boolean kick = false;
	public Ball(int x, int y) {
	super();
	this.x = x;
	this.y = y;
}

	@Override
	public void step(SimState state) {
		if (kick)
			move((PlayerModel) state);
		
	}
private void move(PlayerModel model) {
	model.yard.set(x, y, null);
	model.yard.set(model.yard.stx(x+1), model.yard.stx(y+1), this);
	 x = model.yard.stx(x+1);
	 y = model.yard.stx(y+1);
}
}
