package bookfile.main;

import sim.display.Console;
import bookfile.gui.CarterWithUI;
import bookfile.model.Carter;

public class BookFileMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Carter carter = new Carter(System.currentTimeMillis());
		CarterWithUI gui = new CarterWithUI(carter);
		Console console = new Console(gui);
		console.setVisible(true);
	}

}
