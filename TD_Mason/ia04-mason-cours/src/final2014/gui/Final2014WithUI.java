package final2014.gui;

import java.awt.Color;

import javax.swing.JFrame;
import java.awt.Graphics2D;
import bookfile.model.Constants;
import bookfile.model.FirstAgent;

import final2014.model.Cell;
import final2014.model.Final2014Model;

import sim.display.Controller;
import sim.display.Display2D;
import sim.display.GUIState;
import sim.engine.SimState;
import sim.portrayal.DrawInfo2D;
import sim.portrayal.grid.SparseGridPortrayal2D;
import sim.portrayal.simple.OvalPortrayal2D;
import sim.portrayal.simple.RectanglePortrayal2D;

public class Final2014WithUI extends GUIState {
	public Display2D display;
	public JFrame displayFrame;
	SparseGridPortrayal2D yardPortrayal = new SparseGridPortrayal2D();

	public Final2014WithUI(SimState state) {
		super(state);
	}

	public void start() {
		super.start();
		setupPortrayals();
	}

	public void load(SimState state) {
		super.load(state);
		setupPortrayals();
	}

	public void init(Controller c) {
		super.init(c);
		display = new Display2D(Constants.FRAME_SIZE, Constants.FRAME_SIZE,
				this);
		display.setClipping(false);
		displayFrame = display.createFrame();
		displayFrame.setTitle("Cells");
		c.registerFrame(displayFrame); // so the frame appears in the "Display"
										// list
		displayFrame.setVisible(true);
		display.attach(yardPortrayal, "Yard");
	}

	private void setupPortrayals() {
		Final2014Model model = (Final2014Model) state;
		yardPortrayal.setField(model.yard);
		yardPortrayal.setPortrayalForClass(FirstAgent.class,
				getFirstPortrayal());
		yardPortrayal.setPortrayalForClass(FirstAgent.class,
				getFollowingPortrayal());
		yardPortrayal.setPortrayalForClass(Cell.class, getCellPortrayal());
		display.reset();
		display.setBackdrop(Color.yellow);
		// redraw the display
		display.repaint();
	}

	private OvalPortrayal2D getFirstPortrayal() {
		OvalPortrayal2D r = new OvalPortrayal2D();
		r.paint = Color.red;
		return r;
	}
	private OvalPortrayal2D getFollowingPortrayal() {
		OvalPortrayal2D r = new OvalPortrayal2D();
		r.filled = true;
		r.paint = Color.darkGray;
		return r;
	}
	private RectanglePortrayal2D getCellPortrayal() {
		RectanglePortrayal2D r = new RectanglePortrayal2D() {

			public void draw(Object object, Graphics2D graphics, DrawInfo2D info) {
				filled = false;
				Cell myobject = (Cell) object;
				int a = myobject.capacity; // let's say it goes from 0 to 255
				if (a > 0) {
					paint = new java.awt.Color(255 - a, 255-a, a);
					/**
					 * 255 - a -> 255
					 * a -> 0
					 * yellow = 255, 255, 0
					 */
					filled = true;
				}
				else 
					paint = Color.blue;
				
				super.draw(object, graphics, info); // it'll use the new paint
													// and scale values
			}
		};
		return r;
	}
}
