package final2017;

import java.awt.Color;
import java.awt.Graphics2D;

import sim.portrayal.DrawInfo2D;
import sim.portrayal.simple.OvalPortrayal2D;

public class JadePortrayal extends OvalPortrayal2D {

	public JadePortrayal() {
		super();
		paint = Color.RED;
		filled = true;
	}

	@Override
	public void draw(Object object, Graphics2D graphics, DrawInfo2D info) {
		String name = ((Player) object).name;
		graphics.drawString(name, 10, 10 );
		super.draw(object, graphics, info);
	}

}
