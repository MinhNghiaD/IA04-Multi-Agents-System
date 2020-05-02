package tutorial.model;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.IntStream;

import sim.engine.SimState;
import sim.field.grid.ObjectGrid2D;
import sim.util.Int2D;

public class Beings extends SimState {
	public static int GRID_SIZE = 20;
	public static int NUM_A = 150;
	public static int NUM_B = 150;
	public static int NB_DIRECTIONS = 9;
	public ObjectGrid2D yard = new ObjectGrid2D(GRID_SIZE, GRID_SIZE);
	private List<Boolean> freeLocations = new ArrayList<>();

	public Beings(long seed) {
		super(seed);
	}
    // Transforme un row en ligne, colonne 
	// ex : grille = 100 * 100, row = 253, ligne = 2, colonne = 53
	// ex grille = 20 * 20, row = 178, ligne = 16, colonne = 18
	private Function<Integer,Int2D> locationFromRow = row -> new Int2D(row / GRID_SIZE, row % GRID_SIZE);
	
    
    // Indique si une place est libre
		Predicate<Integer> isFreeLocation = row -> freeLocations.get(row);

	public void start() {
		System.out.println("Simulation started");
		super.start();
		freeLocations = IntStream.range(0, GRID_SIZE*GRID_SIZE).mapToObj(p -> true)
				.collect(toList());
		yard.clear();
		addAgents("tutorial.model.TypeA");
		addAgents("tutorial.model.TypeB");
	}

	private void addAgents(String className) {

		for (int i = 0; i < NUM_A; i++) {
			AgentType agent = null;
			try {
				agent = (AgentType) Class.forName(className).getConstructor().newInstance();
			} catch (Exception e) {
				e.printStackTrace();
			}
			Int2D location = getFreeLocation();
			freeLocations.set(location.x * GRID_SIZE + location.y,false);
			yard.set(location.x, location.y, agent);
			agent.x = location.x;
			agent.y = location.y;
			schedule.scheduleRepeating(agent);
		}
	}

	public boolean free(int x, int y) {
		int xx = yard.stx(x);
		int yy = yard.sty(y);
		return yard.get(xx, yy) == null;
	}

	private Int2D getFreeLocation() {
     Long freeSize = freeLocations.stream().filter(place -> place == true).collect(counting());
		
		Long placeForAgent = random.nextLong(freeSize);
		
		int place = 0; int position = -1;
		while (place <= placeForAgent) {
			position++;
			if (isFreeLocation.test(position)) {
				place++ ;
			}
		}
		Int2D p = locationFromRow.apply(position);
		return p;
	}

	public int getNumBeings() {
		return NUM_A;
	}
}
