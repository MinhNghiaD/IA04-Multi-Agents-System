package final2013;

import final2013.gui.Final2013WithUI;
import final2013.model.Final2013Model;
import sim.display.Console;


public class Final2013Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	        runUI();
		}
		public static void runUI() {
			Final2013Model model = new Final2013Model(System.currentTimeMillis());
			Final2013WithUI gui = new Final2013WithUI(model);
			Console console = new Console(gui);
			console.setVisible(true);
		}

}
