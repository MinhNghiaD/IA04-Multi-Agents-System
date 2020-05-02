package final2013.model;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.util.Int2D;

public class TypeB extends Agent {
int dir = 0; // bas, droite, haut, gauche

	public TypeB(int name) {
	super();
	this.name = name;
}
	@Override
	public void step(SimState state) {
		Final2013Model model = (Final2013Model) state;
		Int2D pos = next(model);
		if (model.isFree(pos.x, pos.y))
		 move(model);
		else {
		  move(model);move(model);	
		}
	}
	private Int2D next(Final2013Model model) {
		Int2D pos = new Int2D(0,0);
		switch(dir) {
		case 0 :
			if (y < Final2013Model.MAX_XY) {
				pos = new Int2D(x,y+1);
			}
			else {
				pos = new Int2D(x+1,y);
			}
		  break;
		case 1 :
			if (x < Final2013Model.MAX_XY) {
				pos = new Int2D(x+1,y);
			}
			else {
				pos = new Int2D(x,y-1);
			}
		  break;
		case 2 :
			if (y > Final2013Model.MIN_XY) {
				pos = new Int2D(x,y-1);
			}
			else {
				pos = new Int2D(x-1,y);
			}
		  break;
	 case 3 :
			if (x > Final2013Model.MIN_XY) {
				pos = new Int2D(x-1,y);
			}
			else {
				pos = new Int2D(x,y+1);
			}
	 }
		return pos;
	}
 private void move(Final2013Model model) {
	switch(dir) {
	case 0 :
		if (y < Final2013Model.MAX_XY) {
			model.yard.set(x, y, null);
			model.yard.set(x, y+1, this);
			y++; 
		}
		else {
			dir = 1;
			model.yard.set(x, y, null);
			model.yard.set(x+1, y, this);
			x++;
		}
	  break;
	case 1 :
		if (x < Final2013Model.MAX_XY) {
			model.yard.set(x, y, null);
			model.yard.set(x+1, y, this);
			x++; 
		}
		else {
			dir = 2;
			model.yard.set(x, y, null);
			model.yard.set(x, y-1, this);
			y--;
		}
	  break;
	case 2 :
		if (y > Final2013Model.MIN_XY) {
			model.yard.set(x, y, null);
			model.yard.set(x, y-1, this);
			y--; 
		}
		else {
			dir = 3;
			model.yard.set(x, y, null);
			model.yard.set(x-1, y, this);
			x--;
		}
	  break;
 case 3 :
		if (x > Final2013Model.MIN_XY) {
			model.yard.set(x, y, null);
			model.yard.set(x-1, y, this);
			x--; 
		}
		else {
			dir = 0;
			model.yard.set(x, y, null);
			model.yard.set(x, y+1, this);
			y++;
		}
 }

}
}
