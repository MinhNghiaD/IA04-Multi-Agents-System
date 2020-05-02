package test;

import sim.display.Console;

public class TestMain {

	public static void main(String[] args) {
		run();
	}

	public static void run() {
		Model model = new Model(System.currentTimeMillis());
		ModelWithUI gui = new ModelWithUI(model);
		Console console = new Console(gui);
		console.setVisible(true);
	}
}
