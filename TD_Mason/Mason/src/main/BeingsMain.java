package main;

import gui.BeingsWithUI;
import model.Beings;
import sim.display.Console;

public class BeingsMain {
	
	public static void main(String[] args) {
		runUI(); }
		public static void runUI() {
		Beings model = new Beings(System.currentTimeMillis()); 
		BeingsWithUI gui = new BeingsWithUI(model);
		Console console = new Console(gui); 
		console.setVisible(true);
	}

}


// Lorsque la nourriture tombe à 0, il se recrée