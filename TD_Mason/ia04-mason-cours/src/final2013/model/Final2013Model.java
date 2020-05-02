package final2013.model;

import sim.engine.SimState;
import sim.field.grid.ObjectGrid2D;
import sim.util.Int2D;

public class Final2013Model extends SimState {
public static int GRID_SIZE = 10;
public static int MIN_XY = 1;
public static int MAX_XY = GRID_SIZE - 2;
public ObjectGrid2D yard = new ObjectGrid2D(GRID_SIZE,GRID_SIZE);
	public Final2013Model(long seed) {
		super(seed);
		
	}
	public void start() {
		System.out.println("Simulation started");
		super.start();
	    yard.clear();
	    addAgents();
  }
	public boolean isFree(int x,int y) {
		 if (x < 0 || x >= GRID_SIZE || y < 0 || y >= GRID_SIZE)
			 return false;
		 return yard.get(x,y) == null;
	  }
	public Int2D see(int x,int y,int d) {
		Int2D r = null;
		for (int i = x - d ; i < x + d + 1 ; i++)
			for (int j = y - d ; j < y + d + 1; j++)
				if ((i != x || j != y) && !(i < 0 || i >= GRID_SIZE || j < 0 || j >= GRID_SIZE))
					if (yard.get(i,j) != null)
						return new Int2D(i,j);
		return r;
	}
	
	private void addAgents() {
		
	TypeA  a1  =  new  TypeA(1);
	Int2D location = new Int2D(5,5);
	yard.set(location.x,location.y,a1);
	a1.x = location.x;
	a1.y = location.y;
	schedule.scheduleRepeating(a1);
	TypeA a2  =  new  TypeA(2);
	location = new Int2D(8,6);
	yard.set(location.x,location.y,a2);
	a2.x = location.x;
	a2.y = location.y;
	schedule.scheduleRepeating(a2);
	TypeB  b  =  new  TypeB(3);
	b.dir = 2;
	location = new Int2D(8,4);
	yard.set(location.x,location.y,b);
	b.x = location.x;
	b.y = location.y;
	schedule.scheduleRepeating(b);
   }  
}
