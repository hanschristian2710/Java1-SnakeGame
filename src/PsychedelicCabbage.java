import java.awt.Color;
import java.awt.Point;

import uwcse.graphics.GWindow;
import uwcse.graphics.Oval;

/**
 * psychedelic cabbage class extends from the cabbage class
 * 
 * @author Hanschristian
 * 
 */
public class PsychedelicCabbage extends Cabbage implements
		CaterpillarGameConstants {
	private Oval psyCabbageLayer1, psyCabbageLayer2, psyCabbageLayer3,
			psyCabbageLayer4;

	/**
	 * psychedelic cababge constructor
	 * 
	 * @param window
	 * @param center
	 */
	public PsychedelicCabbage(GWindow window, Point center) {
		super(window, center);
		draw();
	}

	/**
	 * draw method for the psychedelic cabbage
	 */
	protected void draw() {
		psyCabbageLayer1 = new Oval(center.x - CABBAGE_RADIUS, center.y
				- CABBAGE_RADIUS, 2 * CABBAGE_RADIUS, 2 * CABBAGE_RADIUS,
				Color.MAGENTA, true);
		window.add(psyCabbageLayer1);
		psyCabbageLayer2 = new Oval(center.x - CABBAGE_RADIUS + CABBAGE_RADIUS
				/ 4, center.y - CABBAGE_RADIUS + CABBAGE_RADIUS / 4,
				3 * CABBAGE_RADIUS / 2, 3 * CABBAGE_RADIUS / 2, Color.YELLOW,
				true);
		window.add(psyCabbageLayer2);
		psyCabbageLayer3 = new Oval(center.x - CABBAGE_RADIUS + CABBAGE_RADIUS
				/ 2, center.y - CABBAGE_RADIUS + CABBAGE_RADIUS / 2,
				CABBAGE_RADIUS, CABBAGE_RADIUS, Color.RED, true);
		window.add(psyCabbageLayer3);
		psyCabbageLayer4 = new Oval(center.x - CABBAGE_RADIUS + 3
				* CABBAGE_RADIUS / 4, center.y - CABBAGE_RADIUS + 3
				* CABBAGE_RADIUS / 4, CABBAGE_RADIUS / 2, CABBAGE_RADIUS / 2,
				Color.BLUE, true);
		window.add(psyCabbageLayer4);
	}

	/**
	 * psychedelic is eaten method
	 */
	public void isEatenBy(Caterpillar cp) {
		window.remove(psyCabbageLayer1);
		window.remove(psyCabbageLayer2);
		window.remove(psyCabbageLayer3);
		window.remove(psyCabbageLayer4);
		cp.grow();
		cp.blinkColor = true;
		cp.count += 1;

	}

}
