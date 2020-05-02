package tuto.main;
import sim.display.Console;
import sim.engine.SimState;
import tuto.tuto.Students;
import tuto.tuto.StudentsWithUI;
public class MainTuto {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//tuto1();
		//tuto2();
		tuto3();
	}
 public static void tuto1() {
	 SimState state = new Students(System.currentTimeMillis());
		state.start();
		do
		  if (!state.schedule.step(state)) break;
		while(state.schedule.getSteps() < 5000);
		state.finish();
		System.exit(0);
 }
 public static void tuto2() {
	 sim.engine.SimState.doLoop(Students.class,new String[]{"-for","20000"});
      System.exit(0);
 }
public static void tuto3() {
	StudentsWithUI vid = new StudentsWithUI();
	Console c = new Console(vid);
	c.setVisible(true);
}
}
