package tutorial.model;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.IntStream;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.util.Int2D;

public class AgentType implements Steppable {
	public int x, y;
	public static int LEVEL = 2;
	Beings beings;

	@Override
	public void step(SimState state) {
		beings = (Beings) state;
		long f = friendsNum(beings);
		if (f * 3 < 8) {
			tryMove(beings, f);
		}
	}

	protected long friendsNum(Beings beings) {
		return friendsNum(beings, x, y);
	}

	protected long friendsNum(Beings beings, int l, int c) {
		long nb = 0;
		Integer d = 1;
		// Fournit une liste de paires (x,-2), (x,-1), etc ...
		Function<Integer, List<Pair<Integer>>> fPairList = n -> IntStream.rangeClosed(-d, d)
				.mapToObj(p -> new Pair<Integer>(n, p)).collect(toList());

		// Retourne l'objet (agent) s'il existe à une distance dx, dy de l'agent courant
		Function<Pair<Integer>, Optional<Object>> fDObject = pair -> {
			Int2D flocation = new Int2D(beings.yard.stx(l + pair._1), beings.yard.sty(c + pair._2));
			return Optional.ofNullable(beings.yard.get(flocation.x, flocation.y));
		};
		// Indique qu'une location n'est pas la location courante
		Predicate<Pair<Integer>> isNotAgentPlace = pair -> pair._1 != 0 || pair._2 != 0;

		// Indique si un objet est present et est ami
		Predicate<Optional<Object>> isPresentFriend = ag -> ag.isPresent() && ag.get().getClass() == this.getClass();
		// Compte le nombre d'agents amis dans un carré de rayon d autour de l'agent
		nb = IntStream.rangeClosed(-d, d).mapToObj(x -> fPairList.apply(x)).flatMap(liste -> liste.stream())
				.filter(isNotAgentPlace)
				.map(pair -> fDObject.apply(pair))
				.filter(isPresentFriend).count();
		return nb;
	}

	public boolean move(Beings beings) {
		/**
		 * directions 0 1 2 3 4 5 6 7 8
		 */
		boolean done = false;
		int n = beings.random.nextInt(Beings.NB_DIRECTIONS);
		int dx = n % 3 - 1;
		int dy = n / 3 - 1;
		if (dx != 0 || dy != 0)
			done = isMoved(dx, dy);
		return done;
	}
	private boolean isMoved(int dx, int dy) {
		boolean done = false;
		if (beings.free(x + dx, y + dy) && friendsNum(beings, x + dx, y + dy) >= LEVEL) {
			beings.yard.set(x, y, null);
			x = beings.yard.stx(x + dx);
			y = beings.yard.sty(y + dy);
			beings.yard.set(x, y, this);
			
			done = true;
		}
		return done;
	}

	private boolean isMoved2(Int2D delta) {
		boolean done = false;
		if (beings.free(x + delta.x, y + delta.y)) {
			beings.yard.set(x, y, null);
			x = beings.yard.stx(x + delta.x);
			y = beings.yard.sty(y + delta.y);
			beings.yard.set(x, y, this);
			done = true;
		}
		return done;
	}

	public boolean move2(Beings beings) {
		Int2D deltaXY = randomDeltaXY();
		return isMoved2(deltaXY);
	}

	public boolean tryMove(Beings beings, long f) {
		Int2D deltaXY = randomDeltaXY();
		return isChanged(deltaXY,f);
	}
	// Donne un delta different de 0, 0
	private Int2D randomDeltaXY() {
    	int n = beings.random.nextInt(Beings.NB_DIRECTIONS - 1);
    	int value = IntStream.rangeClosed(0, Beings.NB_DIRECTIONS)
    			.boxed()
    			.filter(p -> p != 4)
    			.collect(toList())
    			.get(n);
    	return new Int2D(value % 3 - 1, n / 3 - 1);		
    }
	private boolean isChanged(Int2D delta, long f) {
		boolean done = false;
		if (beings.free(x + delta.x, y + delta.y) && friendsNum(beings, x + delta.x, y + delta.y) > f) {
					beings.yard.set(x, y, null);
			x = beings.yard.stx(x + delta.x);
			y = beings.yard.sty(y + delta.y);
			beings.yard.set(x, y, this);
			done = true;
		}
		return done;
	}
}
