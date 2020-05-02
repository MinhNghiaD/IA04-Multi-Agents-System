package gui;

import java.awt.Color;

import javax.swing.JFrame;

import agent.Food;
import agent.Ant;
import model.Beings;
import model.Constants;
import sim.display.Controller;
import sim.display.Display2D;
import sim.display.GUIState;
import sim.engine.SimState;
import sim.portrayal.Inspector;
import sim.portrayal.Portrayal;
import sim.portrayal.grid.SparseGridPortrayal2D;
import sim.portrayal.simple.OvalPortrayal2D;

public class BeingsWithUI extends GUIState {
	public Display2D display;
	public JFrame displayFrame;
	private SparseGridPortrayal2D yardPortrayal = new SparseGridPortrayal2D();
	
	public BeingsWithUI(SimState state) {
	    super(state);
	  }
	
	public static String getName() {
	   return "ants Simulation";
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
		display = new Display2D(Constants.FRAME_SIZE, Constants.FRAME_SIZE,this);
		display.setClipping(false);
		displayFrame = display.createFrame();
		displayFrame.setTitle("Number of ants"); 
		c.registerFrame(displayFrame); // so the frame appears in the "Display"
										// list
		displayFrame.setVisible(true);
		display.attach( yardPortrayal, "yard" );
	}
		
	public void setupPortrayals() { 
		Beings beings = (Beings) state;
		yardPortrayal.setyard(beings.yard);
		yardPortrayal.setPortrayalForClass(Ant.class, getAntPortrayal());
		yardPortrayal.setPortrayalForClass(Food.class, getFoodPortrayal());
		
		display.reset(); 
		display.setBackdrop(Color.LIGHT_GRAY); 
		display.repaint();
		 
	}

	private Portrayal getFoodPortrayal() {
		OvalPortrayal2D r = new OvalPortrayal2D(); 
		r.paint = Color.ORANGE;
		r.filled = true;
		return r;
	}

	private Portrayal getAntPortrayal() {
		OvalPortrayal2D r = new OvalPortrayal2D(); 
		r.paint = Color.BLACK;
		r.filled = true;
		return r;
	} 
	
	public Object getSimulationInspectedObject()  {  
		return state;  
	}
	
	public Inspector getInspector() {
		Inspector i = super.getInspector();
		i.setVolatile(true);
		return  i;
	}
}
