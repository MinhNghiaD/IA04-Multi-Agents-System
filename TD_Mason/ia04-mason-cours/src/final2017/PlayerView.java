package final2017;

import java.awt.Color;

import javax.swing.JFrame;

import sim.display.Controller;
import sim.display.Display2D;
import sim.display.GUIState;
import sim.engine.SimState;
import sim.portrayal.grid.ObjectGridPortrayal2D;
import sim.portrayal.simple.LabelledPortrayal2D;
import sim.portrayal.simple.OvalPortrayal2D;

public class PlayerView extends GUIState {
	public static int FRAME_SIZE = 600;
	public Display2D display;
	public JFrame displayFrame;
	ObjectGridPortrayal2D yardPortrayal = new ObjectGridPortrayal2D();

	public PlayerView(SimState state) {
		super(state);
	}

	public static String getName() {
		return "Mason and JADE";
	}

	public void start() {
		super.start();
		((PlayerModel) state).setIsStarted(true);
		setupPortrayals();
	}

	public void load(SimState state) {
		super.load(state);
		setupPortrayals();
	}
	public void init(Controller c) {
		  super.init(c);
		  display = new Display2D(FRAME_SIZE,FRAME_SIZE,this);
		  display.setClipping(false);
		  displayFrame = display.createFrame();
		  displayFrame.setTitle("Football player");
		  c.registerFrame(displayFrame); // so the frame appears in the "Display" list
		  displayFrame.setVisible(true);
		  display.attach( yardPortrayal, "Yard" );
		}
	public void setupPortrayals() {
		PlayerModel ants = (PlayerModel) state;
		yardPortrayal.setField(ants.yard);
		yardPortrayal.setPortrayalForClass(Player.class, getAgentPortrayal());
		yardPortrayal.setPortrayalForClass(Ball.class, getBallPortrayal());
		display.reset();
		display.setBackdrop(Color.orange);
		// redraw the display
		// addBackgroundImage();
		display.repaint();
	}

	private JadePortrayal getAgentPortrayal() {
		JadePortrayal r = new JadePortrayal();
		return r;
	}
	private OvalPortrayal2D getBallPortrayal() {
		OvalPortrayal2D r = new OvalPortrayal2D();
		r.paint = Color.GRAY;
		r.filled = true;
		return r;
	}
}
