package tuto.tuto;

import java.awt.Color;

import javax.swing.JFrame;

import sim.display.Controller;
import sim.display.Display2D;
import sim.display.GUIState;
import sim.engine.SimState;
import sim.portrayal.DrawInfo2D;
import sim.portrayal.continuous.ContinuousPortrayal2D;
import sim.portrayal.network.NetworkPortrayal2D;
import sim.portrayal.network.SimpleEdgePortrayal2D;
import sim.portrayal.network.SpatialNetwork2D;
import sim.portrayal.simple.OvalPortrayal2D;

import java.awt.Graphics2D;

public class StudentsWithUI extends GUIState {
	public Display2D display;
	public JFrame displayFrame;
	ContinuousPortrayal2D yardPortrayal = new ContinuousPortrayal2D();
	NetworkPortrayal2D buddiesPortrayal = new NetworkPortrayal2D();
	public StudentsWithUI(SimState state) {
		super(state);
	}
	public StudentsWithUI() {
		super(new Students(System.currentTimeMillis())); 
	}
	public static String getName() {
		return "Student Schoolyard Cliques"; 
	}
	public void start()	{
	  super.start();
	  setupPortrayals();
	}
	public void load(SimState state) {
	  super.load(state);
	  setupPortrayals();
	}
	public void setupPortrayals() {
	  Students students = (Students) state;
	  // tell the portrayals what to portray and how to portray them
	  yardPortrayal.setField( students.yard );
	  yardPortrayal.setPortrayalForAll(new OvalPortrayal2D()  {
	    public void draw(Object object, Graphics2D graphics,DrawInfo2D info) {
	     Student student = (Student)object;
	     int agitationShade = (int) (student.getAgitation() * 255 / 10.0);
	     if (agitationShade > 255) agitationShade = 255;
	       paint = new Color(agitationShade, 0, 255 - agitationShade);
	     super.draw(object, graphics, info);
	    }
	  });
	  
	  
	  
	  buddiesPortrayal.setField( new SpatialNetwork2D( students.yard, students.buddies ) );
	  buddiesPortrayal.setPortrayalForAll(new SimpleEdgePortrayal2D());
	  
	  // reschedule the displayer
	  display.reset();
	  display.setBackdrop(Color.orange);
	  // redraw the display
	  display.repaint();
	}
	public void init(Controller c) {
	  super.init(c);
	  display = new Display2D(600,600,this);
	  display.setClipping(false);
	  displayFrame = display.createFrame();
	  displayFrame.setTitle("Schoolyard Display");
	  c.registerFrame(displayFrame); // so the frame appears in the "Display" list
	  displayFrame.setVisible(true);
	  display.attach( buddiesPortrayal, "Buddies" );
	  display.attach( yardPortrayal, "Yard" );
	}
	public void quit() {
	  super.quit();
	  if (displayFrame!=null) displayFrame.dispose();
	  displayFrame = null;
	  display = null;
	}
}
