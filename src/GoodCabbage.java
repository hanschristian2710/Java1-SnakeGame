import java.awt.Color;
import java.awt.Point;

import uwcse.graphics.*;

/**
 * Good cabbage class extends from the cabbage class
 * 
 * @author Hanschristian
 * 
 */
public class GoodCabbage extends Cabbage implements CaterpillarGameConstants {
	private Oval goodCabbage;

	/**
	 * Good cabbage constructor
	 * 
	 * @param window
	 * @param center
	 */
	public GoodCabbage(GWindow window, Point center) {
		super(window, center);
		draw();
	}

	/**
	 * good cabbage draw method
	 */
	protected void draw() {
		goodCabbage = new Oval(center.x - CABBAGE_RADIUS, center.y
				- CABBAGE_RADIUS, 2 * CABBAGE_RADIUS, 2 * CABBAGE_RADIUS,
				Color.WHITE, true);
		window.add(goodCabbage);
	}

	/**
	 * is eaten method for good cabbage
	 */
	public void isEatenBy(Caterpillar cp) {
		window.remove(goodCabbage);
		cp.grow();
		cp.count += 1;

	}
}
