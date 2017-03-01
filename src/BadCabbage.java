import java.awt.Color;
import java.awt.Point;

import uwcse.graphics.*;

/**
 * Bad cabbge class extends from the cabbage class
 * 
 * @author Hanschristian
 * 
 */
public class BadCabbage extends Cabbage implements CaterpillarGameConstants {
	private Oval badCabbage;

	/**
	 * Bad Cabbage construtor
	 * 
	 * @param window
	 * @param center
	 */
	public BadCabbage(GWindow window, Point center) {
		super(window, center);
		draw();
	}

	/**
	 * draw method of bad cabbge
	 */
	protected void draw() {
		badCabbage = new Oval(center.x - CABBAGE_RADIUS, center.y
				- CABBAGE_RADIUS, 2 * CABBAGE_RADIUS, 2 * CABBAGE_RADIUS,
				Color.RED, true);
		window.add(badCabbage);

	}

	/**
	 * Is eaten method for bad cabbage
	 */
	public void isEatenBy(Caterpillar cp) {
		window.remove(badCabbage);
		cp.bad = true;
	}
}
