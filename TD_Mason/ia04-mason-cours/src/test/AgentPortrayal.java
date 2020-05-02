package test;

import java.awt.Color;
import java.awt.Graphics2D;

import sim.portrayal.DrawInfo2D;
import sim.portrayal.simple.OvalPortrayal2D;

public class AgentPortrayal extends OvalPortrayal2D {

	public AgentPortrayal() {
		super();
		scale = 10;
		paint = Color.BLUE;
		filled = false;
	}

	@Override
	public void draw(Object object, Graphics2D graphics, DrawInfo2D info) {
		super.draw(object, graphics, info);
		graphics.drawString("Hello", 300, 300);
	}

}
