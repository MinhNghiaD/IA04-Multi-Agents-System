package bookfile.gui;

import java.awt.Color;

import javax.swing.JFrame;

import bookfile.model.Carter;
import bookfile.model.Cell;
import bookfile.model.Constants;
import bookfile.model.FirstAgent;
import bookfile.model.FollowingAgent;

import sim.display.Controller;
import sim.display.Display2D;
import sim.display.GUIState;
import sim.engine.SimState;
import sim.portrayal.grid.SparseGridPortrayal2D;
import sim.portrayal.simple.OvalPortrayal2D;
import sim.portrayal.simple.RectanglePortrayal2D;

public class CarterWithUI extends GUIState {
	public Display2D display;
	public JFrame displayFrame;
	SparseGridPortrayal2D yardPortrayal = new SparseGridPortrayal2D();
	public CarterWithUI(SimState state) {
		super(state);
	}

	public static String getName() {
		return "Simulation de file"; 
	}
	public void start() {
		   super.start();
		   setupPortrayals();
		}

		public void load(SimState state) {
		   super.load(state);
		  setupPortrayals();
		}
		public void setupPortrayals() {
			Carter carter = (Carter) state;
			
			// tell the portrayals what to
	        // portray and how to portray them
	 
			yardPortrayal.setField(carter.yard );
			
			yardPortrayal.setPortrayalForClass(FirstAgent.class, getFirstPortrayal());
			yardPortrayal.setPortrayalForClass(FollowingAgent.class,getFollowingPortrayal() );
			yardPortrayal.setPortrayalForClass(Cell.class, getCellPortrayal());
			// reschedule the displayer
			display.reset();
			display.setBackdrop(Color.yellow);
			// redraw the display
			display.repaint();
		}
		public void init(Controller c) {
		  super.init(c);
		  display = new Display2D(Constants.FRAME_SIZE,Constants.FRAME_SIZE,this);
		  display.setClipping(false);
		  displayFrame = display.createFrame();
		  displayFrame.setTitle("Insects Display");
		  c.registerFrame(displayFrame); // so the frame appears in the "Display" list
		  displayFrame.setVisible(true);
		  display.attach( yardPortrayal, "Yard" );
		}
		private OvalPortrayal2D getFirstPortrayal() {
			OvalPortrayal2D r = new OvalPortrayal2D();
			r.paint = Color.red;
			return r;
		}
		private OvalPortrayal2D getFollowingPortrayal() {
			OvalPortrayal2D r = new OvalPortrayal2D();
			r.paint = Color.blue;
			return r;
		}
		private RectanglePortrayal2D getCellPortrayal() {
			RectanglePortrayal2D r = new RectanglePortrayal2D();
			r.filled = false;
			r.paint = Color.blue;
			return r;
		}
}
