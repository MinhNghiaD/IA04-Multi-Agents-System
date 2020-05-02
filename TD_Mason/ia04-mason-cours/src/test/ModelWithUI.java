package test;

import javax.swing.JFrame;

import sim.display.Controller;
import sim.display.Display2D;
import sim.display.GUIState;
import sim.engine.SimState;
import sim.portrayal.grid.ObjectGridPortrayal2D;
import tutorial.model.Beings;
import tutorial.model.TypeA;

public class ModelWithUI extends GUIState {
	public static int FRAME_SIZE = 600;
	public Display2D display;
	public JFrame displayFrame;
	ObjectGridPortrayal2D yardPortrayal = new ObjectGridPortrayal2D();
	
	public ModelWithUI(SimState state) {
		super(state);
	}
	public static String getName() {
		return "Test"; 
	}
	public void start() {
	  super.start();
	  setupPortrayals();
	}

	public void load(SimState state) {
	  super.load(state);
	  setupPortrayals();
	}
	private void setupPortrayals() {
		Model model = (Model) state;	
		  yardPortrayal.setField(model.yard );
		  yardPortrayal.setPortrayalForClass(Agent.class, new AgentPortrayal());
	}
	public void init(Controller c) {
		super.init(c);
		display = new Display2D(FRAME_SIZE, FRAME_SIZE, this);
		display.setClipping(false);
		displayFrame = display.createFrame();
		displayFrame.setTitle("Insects Display");
		c.registerFrame(displayFrame); // so the frame appears in the "Display"
										// list
		displayFrame.setVisible(true);
		// display.attach(trailsPortrayal,"Trails");
		display.attach(yardPortrayal, "Yard");
	}
}
