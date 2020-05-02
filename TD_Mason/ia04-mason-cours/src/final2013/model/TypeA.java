package final2013.model;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.util.Int2D;
import tutorial.model.Beings;

public class TypeA extends Agent {
	
	int lock = 0;
	int lockname = 0;
	boolean see = false;
	public TypeA(int name) {
		super();
		this.name = name;
	}
	@Override
	public void step(SimState state) {
		Final2013Model model = (Final2013Model) state;
		if (lock == 0) {
			Int2D pos = model.see(x, y,1);
		    if (pos != null) {
			  	lockname = ((Agent) model.yard.get(pos.x,pos.y)).name;
			    lock = 1;
			}
			  else move(model);
		}
		else { 
			Int2D pos = model.see(x, y,MAX_SEE);
			setSee(pos != null); 
			
			if (isSee() && (lockname == ((Agent) model.yard.get(pos.x,pos.y)).name)) {
				int aname = ((Agent) model.yard.get(pos.x,pos.y)).name;
			    System.out.println("Lock: " + aname);
				int a = (x + pos.x)/2;
				int b = (y + pos.y)/2;
				if (model.isFree(a,b)) {
					model.yard.set(x, y, null);
	                model.yard.set(a, b, this);
					x = a;
					y = b; 
				}
			}
		}
		 
	}
	public void move(Final2013Model model) {
		int n = model.random.nextInt(Beings.NB_DIRECTIONS);
		switch(n) {
		case 0: 
			if (model.isFree(x-1, y)) {
			 model.yard.set(x, y, null);
			 model.yard.set(x-1, y, this);
			 x = x-1;
			}
			break;
		case 1:
			if (model.isFree(x+1, y)) {
				model.yard.set(x, y, null);
				model.yard.set(x+1, y, this);
			 x = x+1;
		    }
			break;
		case 2:
			if (model.isFree(x, y-1)) {
				model.yard.set(x, y, null);
				model.yard.set(x, y-1, this);
				y = y-1;
			}
			break;
		case 3: 
			if (model.isFree(x, y+1)) {
				model.yard.set(x, y, null);
				model.yard.set(x, y+1, this);
				y = y+1;
			}
			break;
		case 4:
			if (model.isFree(x-1, y-1)) {
				model.yard.set(x, y, null);
				model.yard.set(x-1, y-1, this);
				x = x-1;
				y = y-1;
			}
			break;
		case 5:
			if (model.isFree(x+1, y-1)) {
				model.yard.set(x, y, null);
				model.yard.set(x+1, y-1, this);
				x = x+1;
				y = y-1;
			}
			break;
		case 6:
			if (model.isFree(x+1, y+1)) {
				model.yard.set(x, y, null);
				model.yard.set(x+1, y+1, this);
				x = x+1;
				y = y+1;
			}
			break;
		case 7:
			if (model.isFree(x-1, y+1)) {
				model.yard.set(x, y, null);
				model.yard.set(x-1, y+1, this);
				x = x-1;
				y = y+1;
			}
			break;
		}
	 }
	public int getLockname() {
		return lockname;
	}
	public boolean isSee() {
		return see;
	}
	public void setSee(boolean see) {
		this.see = see;
	}
	
}
