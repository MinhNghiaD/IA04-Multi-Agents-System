package final2014.model;

import sim.engine.SimState;
import sim.engine.Stoppable;
import sim.field.grid.SparseGrid2D;
import sim.util.Int2D;

public class Final2014Model extends SimState {
	public SparseGrid2D yard;

	public Final2014Model(long seed) {
		super(seed);
	}

	public void start() {
		System.out.println("Simulation started");
		super.start();
		yard = new SparseGrid2D(Constants.GRID_SIZE, Constants.GRID_SIZE);
		yard.clear();
		addCells();
		firstAgent();
		addFollowings();
	}

	private void addCells() {
		for (int i = 0; i < Constants.GRID_SIZE; i++) {
			for (int j = 0; j < Constants.GRID_SIZE; j++) {
				Cell cell = new Cell();
				Int2D location = new Int2D(i, j);
				cell.location = location;
				yard.setObjectLocation(cell, location);
				Stoppable stoppable = schedule.scheduleRepeating(cell);
			}

		}
	}

	private void firstAgent() {
		FirstAgent f = new FirstAgent();
		f.xcor = Constants.D_XCOR;
		f.ycor = Constants.D_YCOR;
		Int2D location = new Int2D(Constants.D_XCOR, Constants.D_YCOR);
		Stoppable stoppable = schedule.scheduleRepeating(f);
		f.stoppable = stoppable;
		yard.setObjectLocation(f, location);
	}

	private void addFollowings() {
		for (int i = 0; i < Constants.NUM_INSECTS; i++) {
			FollowingAgent f = new FollowingAgent();
			f.xcor = (Constants.A_XCOR + Constants.D_XCOR) / 2;
			f.ycor = (Constants.A_YCOR + Constants.D_YCOR) / 2;
			Int2D location = new Int2D(f.xcor, f.ycor);
			Stoppable stoppable = schedule.scheduleRepeating(f);
			f.stoppable = stoppable;
			yard.setObjectLocation(f, location);
		}
	}
}
