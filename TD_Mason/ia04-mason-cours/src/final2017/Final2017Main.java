package final2017;

import sim.display.Console;

public class Final2017Main {

	public static void main(String[] args) {
		PlayerModel model = new PlayerModel(System.currentTimeMillis());
		PlayerView gui = new PlayerView(model);
		Console console = new Console(gui);
		console.setVisible(true);
	}

}
