package final2014.model;

import java.util.Comparator;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.engine.Stoppable;
import sim.util.Bag;
import sim.util.Int2D;

public class FollowingAgent implements Steppable {
	public int xcor, ycor;
	int xcap = 0;
	int ycap = 0;
	int d = 0;
	boolean capted = false;
	public Stoppable stoppable;

	public FollowingAgent() {
		d = new Random().nextInt(4);
		switch (d) {
		case 0:
			xcap = 1;
			ycap = 1;
			break;
		case 1:
			xcap = 1;
			ycap = -1;
			break;
		case 2:
			xcap = -1;
			ycap = -1;
			break;
		case 3:
			xcap = -1;
			ycap = +1;
			break;

		}
	}

	@Override
	public void step(SimState model) {
//		if (capted) {
//			if (Constants.A_XCOR - xcor <= 1 || Constants.A_YCOR - ycor <= 1) {
//				stop(model);
//			} else {
//				if (!findMove(model))
//				  move(model);
//			}
//
//		} else
			if (!findMove(model))
				  move(model);
	}

	private void move(SimState model) {
		Final2014Model state = (Final2014Model) model;
		xcor += xcap;
		ycor += ycap;
		if (xcor >= Constants.GRID_SIZE - 2 || xcor <= 2 || ycor >= Constants.GRID_SIZE - 2 || ycor <= 2 ) {
			xcap = -xcap;
			ycap = -ycap;
			xcor += xcap;
			ycor += ycap;
		}

		Int2D newPos = new Int2D(xcor, ycor);
		state.yard.setObjectLocation(this, newPos);
	}

	private void stop(SimState model) {
		Final2014Model state = (Final2014Model) model;
		Int2D newPos = new Int2D(Constants.A_XCOR, Constants.A_YCOR);
		state.yard.setObjectLocation(this, newPos);
		state.yard.remove(this);
		stoppable.stop();
	}

	private boolean findMove(SimState model) {
		boolean result = false;
		TreeSet<Cell> t = new TreeSet<Cell>(new Comparator<Cell>() {
			@Override
			public int compare(Cell c0, Cell c1) {
				return c0.capacity <= c1.capacity ? 0 : 1;
			}

		});
		Final2014Model state = (Final2014Model) model;
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				Int2D newPos = new Int2D(xcor + i, ycor + j);
				Bag b = state.yard.getObjectsAtLocation(newPos);
				if (b != null) {
					Cell c = (Cell) b.get(0);
					if (c.capacity > 0)
						t.add(c);
				}
			}
		}
		if (!t.isEmpty()) {
			capted = true;
			Int2D newPos = t.last().location;
			System.out.println(newPos.x + ";" + newPos.y);
			xcor = newPos.x;
			ycor = newPos.y;
			state.yard.setObjectLocation(this, newPos);
			result = true;
		}
		return result;
	}
}
