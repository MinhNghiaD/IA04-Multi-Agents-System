package bookfile.model;

import java.util.HashMap;
import java.util.Map;

import sim.engine.SimState;
import sim.engine.Stoppable;
import sim.field.grid.SparseGrid2D;
import sim.util.Int2D;

public class Carter extends SimState {
	public SparseGrid2D yard;
	private int numInsects;
    Map<Integer,FollowingAgent> positions = new HashMap<Integer,FollowingAgent>();
	public Carter(long seed) {
		super(seed);
	}

	public void start() {
		super.start();
		yard = new SparseGrid2D(Constants.GRID_SIZE, Constants.GRID_SIZE);
		numInsects = Constants.NUM_INSECTS;
		// clear the yard
		yard.clear();
		/*
		 * Créer les insectes
		 */
		FirstAgent f = new FirstAgent();
		f.xcor = Constants.D_XCOR;
		f.ycor = Constants.D_YCOR;
		positions.put(f.name,f);
		Int2D location = new Int2D(Constants.D_XCOR, Constants.D_YCOR);
		Stoppable stoppable = schedule.scheduleRepeating(f);
		f.stoppable = stoppable;
		yard.setObjectLocation(f, location);
		 addCells();
		/**
		 * Création des suiveurs
		 */
		for (int i = 1; i <= Constants.NUM_INSECTS; i++) {
			FollowingAgent insect = new FollowingAgent();
			insect.name = i;
			insect.follow = i - 1;
			location = new Int2D(Constants.D_XCOR, Constants.D_YCOR);
			positions.put(i,insect);
			stoppable = schedule.scheduleRepeating(insect);
			insect.stoppable = stoppable;
			yard.setObjectLocation(insect, location);
			insect.stoppable = stoppable;
		}

	}
	private void addCells() {
		for (int i = 0; i < Constants.GRID_SIZE; i++) {
			for (int j = 0; j < Constants.GRID_SIZE; j++) {
			  Cell cell = new Cell();
			  Int2D location = new Int2D(i, j);
			  yard.setObjectLocation(cell, location);
			}
	
		}
	}
}
