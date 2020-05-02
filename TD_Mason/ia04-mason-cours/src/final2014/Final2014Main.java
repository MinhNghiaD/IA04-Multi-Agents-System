package final2014;

import final2014.gui.Final2014WithUI;
import final2014.model.Final2014Model;
import sim.display.Console;


public class Final2014Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	        runUI();
		}
		public static void runUI() {
			Final2014Model model = new Final2014Model(System.currentTimeMillis());
			Final2014WithUI gui = new Final2014WithUI(model);
			Console console = new Console(gui);
			console.setVisible(true);
		}

}
