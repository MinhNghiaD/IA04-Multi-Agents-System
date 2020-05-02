package final2017;

import java.util.ArrayList;
import java.util.List;

import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import sim.engine.SimState;
import sim.field.grid.ObjectGrid2D;

public class PlayerModel extends SimState {
	public static String MAIN_PROPERTIES_FILE = "properties/main.properties";
	public jade.wrapper.AgentContainer mc;
	public static int GRID_SIZE = 20;
	public ObjectGrid2D yard = new ObjectGrid2D(GRID_SIZE, GRID_SIZE);
	private List<Player> agents = new ArrayList<>();
	private IntegerProperty number = new SimpleIntegerProperty(0);
    private Boolean isStarted;
    
	public void setIsStarted(Boolean isStarted) {
		this.isStarted = isStarted;
		System.out.println("Game is started");
	}

	public PlayerModel(long seed) {
		super(seed);
		number.addListener((obs, oldv, newv) -> {
			if ((int) newv == 2) {
				addAgents();
				System.out.println("Agents installed");
			}
		});
	}

	public void start() {
		System.out.println("Simulation started");
		super.start();
		yard.clear();
		createAgentContainer();
	}

	public void addAgent(Player agent) {
		agents.add(agent);
		number.setValue(agents.size());
	}

	private void addAgents() {
		Ball ball = new Ball(6,1);
		yard.set(6, 1, ball);
		schedule.scheduleRepeating(ball);
		Player agent = agents.get(0);
		agent.ball = ball;
		yard.set(5, 1, agent);
		agent.x = 5;
		agent.y = 1;
		agent.canKick = true;
		schedule.scheduleRepeating(agent);
		agent = agents.get(1);
		agent.ball = ball;
		yard.set(15, 1, agent);
		agent.x = 15;
		agent.y = 1;
		schedule.scheduleRepeating(agent);
		
	}

	private void createAgentContainer() {
		Runtime rt = Runtime.instance();
		ProfileImpl p = null;
		try {
			p = new ProfileImpl(MAIN_PROPERTIES_FILE);
			mc = rt.createMainContainer(p);
			AgentController ac = mc.createNewAgent("a", "final2017.Player", new Object[] { this, "a" });
			ac.start();
			ac = mc.createNewAgent("b", "final2017.Player", new Object[] { this, "b" });
			ac.start();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
