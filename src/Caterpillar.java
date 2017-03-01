import uwcse.graphics.*;

import java.awt.Point;
import java.util.*;
import java.awt.Color;

/**
 * A Caterpillar is the representation and the display of a caterpillar
 */

public class Caterpillar implements CaterpillarGameConstants {
	// The body of a caterpillar is made of Points stored
	// in an ArrayList
	private ArrayList<Point> body = new ArrayList<Point>();

	// Store the graphical elements of the caterpillar body
	// Useful to erase the body of the caterpillar on the screen
	private ArrayList<Rectangle> bodyUnits = new ArrayList<Rectangle>();

	// The window the caterpillar belongs to
	private GWindow window;

	// Direction of motion of the caterpillar (EAST initially)
	private int dir = EAST;

	// initialize the caterpillar if it ate the bad cabbage
	public boolean bad = false;
	public boolean blinkColor = false;
	public int count = 0;

	/**
	 * Constructs a caterpillar
	 * 
	 * @param window
	 *            the graphics where to draw the caterpillar.
	 */
	public Caterpillar(GWindow window) {
		// Initialize the graphics window for this Caterpillar
		this.window = window;

		// Create the caterpillar (35 points initially)
		// First point
		Point p = new Point();
		p.x = 35;
		p.y = WINDOW_HEIGHT / 2;
		body.add(p);

		// Other points
		for (int i = 0; i < 15; i++) {
			Point q = new Point(p);
			q.translate(STEP, 0);
			body.add(q);
			p = q;
		}

		// Other initializations (if you have more instance fields)
		// Display the caterpillar (call a private method)
		draw();
	}

	/**
	 * Draw the caterpillar in the graphics window
	 */
	private void draw() {
		// Connect with Rectangles the points of the body
		Point p = (Point) body.get(0);

		for (int i = 1; i < body.size(); i++) {
			Point q = (Point) body.get(i);
			// add a body unit between p and q
			addBodyUnit(p, q, bodyUnits.size());
			p = q;
		}

		window.doRepaint();
	}

	/**
	 * Move the caterpillar in the current direction
	 */
	public void move() {
		move(dir);
	}

	/**
	 * Move the caterpillar in the direction newDir. <br>
	 * If the new direction is illegal, select a legal direction of motion and
	 * move in that direction.<br>
	 */
	public void move(int newDir) {
		// Is the move illegal?
		boolean isMoveNotOK;

		// newDir might not be legal
		// Before trying a random direction, try first
		// the current direction of motion (if not newDir)
		boolean isFirstTry = true;

		// move the caterpillar in direction newDir
		do {
			// new position of the head
			Point head = new Point(body.get(body.size() - 1));
			switch (newDir) {
			case NORTH:
				head.y -= STEP;
				break;
			case SOUTH:
				head.y += STEP;
				break;
			case EAST:
				head.x += STEP;
				break;
			case WEST:
				head.x -= STEP;
				break;
			}
			// Is the new position in the window?
			if (isPointInTheWindow(head)) {
				isMoveNotOK = false;
				// Update the position of the caterpillar
				body.remove(0);
				body.add(head);
			} else {
				isMoveNotOK = true;
				// Select another direction
				// Try the current direction first
				if (newDir != dir && isFirstTry) {
					newDir = dir;
					isFirstTry = false;
				} else {
					// Assigned direction
					if (dir == NORTH) {
						newDir = EAST;
					} else if (dir == EAST) {
						newDir = SOUTH;
					} else if (dir == SOUTH) {
						newDir = WEST;
					} else {
						newDir = NORTH;
					}
				}

			}
		} while (isMoveNotOK);

		// Update the current direction of motion
		dir = newDir;

		// Show the new location of the caterpillar
		moveCaterpillarOnScreen();
	}

	/**
	 * check the point of the caterpillar movemnet
	 * 
	 * @param p
	 * @return whether the caterpillar is moving in a right place or not
	 */
	private boolean isPointInTheWindow(Point p) {
		return (p.x >= 0 && p.x <= WINDOW_WIDTH && p.y > 0 && p.y <= WINDOW_HEIGHT);
	}

	/**
	 * Move the caterpillar on the screen
	 */
	private void moveCaterpillarOnScreen() {
		// Erase the body unit at the getPart
		window.remove(bodyUnits.get(0));
		bodyUnits.remove(0);

		// Add a new body unit at the head
		Point q = body.get(body.size() - 1);
		Point p = body.get(body.size() - 2);
		addBodyUnit(p, q, bodyUnits.size());

		// show it
		window.doRepaint();
	}

	/**
	 * Is the caterpillar crawling over itself?
	 * 
	 * @return true if the caterpillar is crawling over itself and false
	 *         otherwise.
	 */
	public boolean isCrawlingOverItself() {
		// Is the head point equal to any other point of the caterpillar?
		for (int i = 0; i < body.size() - 1; i++) {
			if (getHead().equals((Point) body.get(i))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Are all of the points of the caterpillar outside the garden
	 * 
	 * @return true if the caterpillar is outside the garden and false
	 *         otherwise.
	 */
	public boolean isOutsideGarden() {
		// possible snake direction when hitting the fence
		if (body.get(0).x >= 0 && body.get(0).x < 115 && body.get(0).y >= 0
				&& body.get(0).y < 500 && body.get(body.size() / 2).x >= 0
				&& body.get(body.size() / 2).x < 115
				&& body.get(body.size() / 2).y >= 0
				&& body.get(body.size() / 2).y < 500
				&& body.get(body.size() - 1).x >= 0
				&& body.get(body.size() - 1).x < 115
				&& body.get(body.size() - 1).y >= 0
				&& body.get(body.size() - 1).y < 500) {
			return true;

		}
		return false; //
	}

	/**
	 * Return the location of the head of the caterpillar (complete)
	 * 
	 * @return the location of the head of the caterpillar.
	 */
	public Point getHead() {
		return new Point((Point) body.get(body.size() - 1));
	}

	/**
	 * Increase the length of the caterpillar (by GROWTH_SPURT elements) Add the
	 * elements at the getPart of the caterpillar.
	 */
	public void grow() {
		// get the body point of the tail
		Point getPart = body.get(0);
		// get the body point before the tail
		Point beforegetPart = body.get(1);
		// make new point initialize
		Point additionPart;

		// Possible snake direction when it eats the cabbages
		if ((getPart.x <= beforegetPart.x) && (getPart.y == beforegetPart.y)) {
			additionPart = new Point(getPart.x - GROWTH_SPURT, getPart.y);
		} else if ((getPart.x >= beforegetPart.x)
				&& (getPart.y == beforegetPart.y)) {
			additionPart = new Point(getPart.x + GROWTH_SPURT, getPart.y);
		} else if ((getPart.x == beforegetPart.x)
				&& (getPart.y >= beforegetPart.y)) {
			additionPart = new Point(getPart.x, getPart.y + GROWTH_SPURT);
		} else {
			additionPart = new Point(getPart.x, getPart.y - GROWTH_SPURT);
		}

		// add the part of the body to the body list
		body.add(0, additionPart);
		// add back the body unit to the caterpillar
		// to the first part of body
		addBodyUnit(additionPart, getPart, 0);
	}

	/**
	 * Add a body unit to the caterpillar. The body unit connects Point p and
	 * Point q.<br>
	 * Insert this body unit at position index in bodyUnits.<br>
	 * e.g. 0 to insert at the last and bodyUnits.size() to insert at the head.
	 */
	private void addBodyUnit(Point p, Point q, int index) {
		// Connect p and q with a rectangle.
		// To allow for a smooth look of the caterpillar, p and q
		// are not on the edges of the Rectangle

		// initialize color of snake
		Color c;
		// Upper left corner of the rectangle
		int x = Math.min(q.x, p.x) - CATERPILLAR_WIDTH / 2;
		int y = Math.min(q.y, p.y) - CATERPILLAR_WIDTH / 2;

		// Width and height of the rectangle (vertical or horizontal rectangle?)
		int width = ((q.y == p.y) ? (STEP + CATERPILLAR_WIDTH)
				: CATERPILLAR_WIDTH);
		int height = ((q.x == p.x) ? (STEP + CATERPILLAR_WIDTH)
				: CATERPILLAR_WIDTH);

		// check if the color flashing is true or not
		if (blinkColor == true) {
			int red = (int) (Math.random() * 256);
			int green = (int) (Math.random() * 256);
			int blue = (int) (Math.random() * 256);
			c = new Color(red, green, blue);
		} else {
			c = Color.BLUE;
		}

		Rectangle r = new Rectangle(x, y, width, height, c, true);
		window.add(r);
		// keep track of that rectangle (we will erase it at some point)
		bodyUnits.add(index, r);
	}

}
