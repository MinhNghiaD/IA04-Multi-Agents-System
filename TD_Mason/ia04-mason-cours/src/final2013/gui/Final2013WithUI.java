package final2013.gui;

import java.awt.Color;

import javax.swing.JFrame;

import final2013.model.Final2013Model;
import final2013.model.TypeB;

import sim.display.Controller;
import sim.display.Display2D;
import sim.display.GUIState;
import sim.engine.SimState;
import sim.portrayal.grid.ObjectGridPortrayal2D;
import sim.portrayal.simple.OvalPortrayal2D;
import tutorial.model.TypeA;

public class Final2013WithUI extends GUIState {
	public static int FRAME_SIZE = 400;
	public Display2D display;
	public JFrame displayFrame;
	ObjectGridPortrayal2D yardPortrayal = new ObjectGridPortrayal2D();
	
	public Final2013WithUI(SimState state) {
		super(state);
	}
	public static String getName() {
		return "Poursuite"; 
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
		Final2013Model model = (Final2013Model) state;	
		yardPortrayal.setField(model.yard );
		yardPortrayal.setPortrayalForClass(TypeA.class, getTypeAPortrayal());
		yardPortrayal.setPortrayalForClass(TypeB.class, getTypeBPortrayal());
		// reschedule the displayer
		display.reset();
		display.setBackdrop(Color.yellow);
		// redraw the display
		display.repaint();
	}
	private OvalPortrayal2D getTypeAPortrayal() {
		OvalPortrayal2D r = new OvalPortrayal2D();
		r.paint = Color.BLUE;
		r.filled = true;
		return r;
	}
	private OvalPortrayal2D getTypeBPortrayal() {
		OvalPortrayal2D r = new OvalPortrayal2D();
		r.paint = Color.RED;
		r.filled = true;
		return r;
	}
	public void init(Controller c) {
		  super.init(c);
		  display = new Display2D(FRAME_SIZE,FRAME_SIZE,this);
		  display.setClipping(false);
		  displayFrame = display.createFrame();
		  displayFrame.setTitle("Poursuite");
		  c.registerFrame(displayFrame); // so the frame appears in the "Display" list
		  displayFrame.setVisible(true);
		  display.attach( yardPortrayal, "Yard" );
		}
}
