package masonfx;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javafx.application.Platform;
import sim.engine.SimState;

public class MasonFXController {
	MasonFXView view;
	MasonModel model;
	 ExecutorService executor = Executors.newFixedThreadPool(1);
	//ExecutorService executor = Executors.newSingleThreadExecutor();

	public MasonFXController(MasonFXView view) {
		super();
		this.view = view;
		view.start.setOnAction(evt -> startSimulation());
		view.stop.setOnAction(evt -> System.exit(0));
	}

	public void initialize() {
		System.out.println("Controller: initialize");

		model.agents.get(0).countProperty().addListener((obs, oldv, newv) -> view.value1.setText(String.valueOf(newv)));
		model.agents.get(1).countProperty().addListener((obs, oldv, newv) -> view.value2.setText(String.valueOf(newv)));
	}

	public void setModel(MasonModel model) {
		this.model = model;
		initialize();
	}

	private void startSimulation() {
		
		executor.execute(getTask());
		
	}

	private Runnable getTask() {
		Runnable delay = () -> {
			//Platform.runLater(() -> {
				SimState.doLoop(MasonModel.class, new String[] {});
			//});
		};
		return delay;
	}
}
