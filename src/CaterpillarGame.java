import uwcse.graphics.*;

import java.util.*;
import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Point2D;

import javax.swing.JOptionPane;

/**
 * A CaterpillarGame displays a garden that contains good and bad cabbages and a
 * constantly moving caterpillar. The player directs the moves of the
 * caterpillar. Every time the caterpillar eats a cabbage, the caterpillar
 * grows. The player wins when all of the good cabbages are eaten and the
 * caterpillar has left the garden. The player loses if the caterpillar eats a
 * bad cabbage or crawls over itself.
 */

public class CaterpillarGame extends GWindowEventAdapter implements
		CaterpillarGameConstants
// The class inherits from GWindowEventAdapter so that it can handle key events
// (in the method keyPressed), and timer events.
// All of the code to make this class able to handle key events and perform
// some animation is already written.
{
	// Game window
	private GWindow window;

	// The caterpillar
	private Caterpillar cp;

	// Direction of motion given by the player
	private int dirFromKeyboard;

	// Do we have a keyboard event
	private boolean isKeyboardEventNew = false;

	// The list of all the cabbages
	private ArrayList<Cabbage> cabbages;
	private ArrayList<Rectangle> fence;

	// is the current game over?
	// private boolean gameOver;
	private String messageGameOver;

	// fences rectangle
	protected Rectangle upperFence, lowerFence, rightFence, firstLeftFence,
			secondLeftFence;

	// Caterpillar blinkColor timer
	private int colorTimer = 100;

	/**
	 * Constructs a CaterpillarGame
	 */
	public CaterpillarGame() {
		// Create the graphics window
		window = new GWindow("Caterpillar Game", WINDOW_WIDTH, WINDOW_HEIGHT);
		window.setExitOnClose();

		// Any key or timer event while the window is active is sent to this
		// CaterpillarGame
		window.addEventHandler(this);

		// Set up the game (fence, cabbages, caterpillar)
		// Display the game rules
		int gameRulesOption = JOptionPane
				.showConfirmDialog(
						null,
						"Caterpillar Game"
								+ "\n--Instructions--"
								+ "\n1. Direct the caterpillar to eat good cabbages and psychedelic cabbages."
								+ "\n2. Eating psychedelic cabbages allow the caterpillar to change its color randomly."
								+ "\n3. Don't eat the bad cabbages or else the caterpillar will die."
								+ "\n4. Don't crash over the caterpillar body itself."
								+ "\n5. Don't hit the caterpillar to the fence, or else will game over."
								+ "\n6. Get out of the garden after all good and psychedelic cabbages have been eaten."
								+ "\n" + "\nHow to control the caterpillar: "
								+ "\nMove right: Press (a)."
								+ "\nMove left: Press(d)."
								+ "\nMove up: Press(w)."
								+ "\nMove down: Press(s)."
								+ "\nDirect Exit: Press(q).",
						"Playing Instructions", JOptionPane.DEFAULT_OPTION,
						JOptionPane.INFORMATION_MESSAGE);
		if (gameRulesOption == JOptionPane.OK_OPTION) {
			initializeGame();
		}
	}

	/**
	 * Initializes the game (draw the garden, garden fence, cabbages,
	 * caterpillar)
	 */
	private void initializeGame() {
		// Clear the window
		window.erase();

		// New game
		// gameOver = false;

		// No keyboard event yet
		isKeyboardEventNew = false;

		// Background (the garden)
		window.add(new Rectangle(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT,
				Color.GREEN, true));

		// call the private method to draw the fence
		gardenFence();

		// call the private method of cabbages
		varyCabbages();

		// Create the caterpillar
		cp = new Caterpillar(window);

		// start timer events (to do the animation)
		this.window.startTimerEvents(ANIMATION_PERIOD);
	}

	/**
	 * draw the fence of the garden
	 */
	private void gardenFence() {
		// Create an arraylist to store the fence
		fence = new ArrayList<Rectangle>();
		// Create the fence around the garden
		upperFence = new Rectangle(120, 0, 380, 10, Color.BLACK, true);
		lowerFence = new Rectangle(120, 490, 380, 10, Color.BLACK, true);
		rightFence = new Rectangle(490, 0, 10, 500, Color.BLACK, true);
		firstLeftFence = new Rectangle(120, 0, 10, 200, Color.BLACK, true);
		secondLeftFence = new Rectangle(120, 300, 10, 200, Color.BLACK, true);
		fence.add(upperFence);
		fence.add(lowerFence);
		fence.add(rightFence);
		fence.add(firstLeftFence);
		fence.add(secondLeftFence);
		for (int i = 0; i < fence.size(); i++) {
			window.add(fence.get(i));
		}
		Oval gate1 = new Oval(115, 190, 20, 20, Color.ORANGE, true);
		Oval gate2 = new Oval(115, 290, 20, 20, Color.ORANGE, true);
		window.add(gate1);
		window.add(gate2);

	}

	/**
	 * draw the cabbages (good cabbages, bad cabbages)
	 */
	private void varyCabbages() {
		// Cabbages List
		this.cabbages = new ArrayList<Cabbage>(N_GOOD_CABBAGES + N_BAD_CABBAGES
				+ N_PSY_CABBAGES);

		// Initialize the elements of the ArrayList = cabbages
		// (they should not overlap and be in the garden) ....
		Random rand = new Random();
		for (int i = 0; i < N_GOOD_CABBAGES + N_BAD_CABBAGES + N_PSY_CABBAGES; i++) {
			boolean overlap;
			int x, y;
			do {
				x = 140 + rand.nextInt(330);
				y = 20 + rand.nextInt(450);
				overlap = false;

				for (Cabbage c : cabbages) {
					double distance = Point2D.distance(c.center.x, c.center.y,
							x, y);
					if (distance < 3 * CABBAGE_RADIUS) {
						overlap = true;
						break;
					}
				}
			} while (overlap);

			// draw the cabbages according to the demand
			if (i < N_GOOD_CABBAGES) {
				cabbages.add(new GoodCabbage(window, new Point(x, y)));
			} else if (i < N_GOOD_CABBAGES + N_BAD_CABBAGES) {
				cabbages.add(new BadCabbage(window, new Point(x, y)));
			} else {
				cabbages.add(new PsychedelicCabbage(window, new Point(x, y)));
			}
		}
	}

	/**
	 * Moves the caterpillar within the graphics window every ANIMATION_PERIOD
	 * milliseconds.
	 * 
	 * @param e
	 *            the timer event
	 */
	public void timerExpired(GWindowEvent e) {
		// Did we get a new direction from the user?
		// Use isKeyboardEventNew to take the event into account
		// only once
		if (isKeyboardEventNew) {
			isKeyboardEventNew = false;
			cp.move(dirFromKeyboard);
		} else
			cp.move();

		// Is the caterpillar eating a cabbage?
		cabbagesEaten();
		psyCabbageColor();

		// Is the game over?
		if (gameOver()) {
			endTheGame();
		}
	}

	/**
	 * check the cabbages eaten by the caterpillar
	 */
	private void cabbagesEaten() {
		// loop through each list in the cabbages
		for (int i = 0; i < cabbages.size(); i++) {
			Cabbage getCabbage = cabbages.get(i);
			if (getCabbage.isPointInCabbage(cp.getHead())) {
				getCabbage.isEatenBy(cp);
				cabbages.remove(i);
			}
		}
	}

	/**
	 * check whether the caterpillar eat the psychedelic cabbage
	 */
	private void psyCabbageColor() {
		if (cp.blinkColor) {
			colorTimer--;
			window.startTimerEvents(ANIMATION_PERIOD);
			if (colorTimer == 0) {
				cp.blinkColor = false;
				colorTimer = 100;
			}
		}
	}

	/**
	 * Check whether the snake is hitting to the fence or not
	 */
	private boolean hitFence() {
		// get the snake head points
		Point snakeHead = cp.getHead();
		// loop through the fence list
		for (int i = 0; i < fence.size(); i++) {
			// get the fences inside the list
			Rectangle touchFence = fence.get(i);

			if ((snakeHead.x >= touchFence.getX())
					&& (snakeHead.x <= touchFence.getX()
							+ touchFence.getWidth())
					&& (snakeHead.y >= touchFence.getY())
					&& (snakeHead.y <= touchFence.getY()
							+ touchFence.getHeight())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * check whether the game is over or not
	 * 
	 * @return true if it is over
	 */
	private boolean gameOver() {
		// check if the caterpillar is crawling over itself or not
		if (cp.isCrawlingOverItself()) {
			messageGameOver = "You just died, you are crawling over yourself!"
					+ "\nGAME OVER!!";
			return true;
		}
		// check if the caterpillar is eating the bad cabbage
		if (cp.bad) {
			messageGameOver = "You Lose!! You ate a bad cabbage!"
					+ "\nGAME OVER!!";
			return true;

		}
		if (cp.count == N_GOOD_CABBAGES + N_PSY_CABBAGES
				&& cp.isOutsideGarden()) {
			messageGameOver = "You have won the game!!" + "\nCONGRATULATIONS!!";
			return true;
		}
		if (hitFence()) {
			messageGameOver = "Ooops!! You hit the garden fence!"
					+ "\nGAME OVER!!";
			return true;
		}
		return false;
	}

	/**
	 * Moves the caterpillar according to the selection of the user w: NORTH, a:
	 * WEST, s: SOUTH, d: EAST
	 * 
	 * @param e
	 *            the keyboard event
	 */
	public void keyPressed(GWindowEvent e) {
		switch (Character.toLowerCase(e.getKey())) {
		case 'w':
			dirFromKeyboard = NORTH;
			break;
		case 'a':
			dirFromKeyboard = WEST;
			break;
		case 's':
			dirFromKeyboard = SOUTH;
			break;
		case 'd':
			dirFromKeyboard = EAST;
			break;
		case 'q':
			System.exit(0); // abruptly ends the application
			break;
		default:
			return;
		}

		// new keyboard event
		isKeyboardEventNew = true;
	}

	/**
	 * The game is over. Starts a new game or ends the application
	 */
	private void endTheGame() {
		window.stopTimerEvents();
		// messageGameOver is an instance String that
		// describes the outcome of the game that just ended
		// (e.g. congratulations! you win)
		boolean again = anotherGame(messageGameOver);
		if (again) {
			initializeGame();
		} else {
			System.exit(0);
		}
	}

	/**
	 * Does the player want to play again?
	 */
	private boolean anotherGame(String s) {
		int choice = JOptionPane.showConfirmDialog(null, s
				+ "\nDo you want to play again?", "Caterpillar Game",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (choice == JOptionPane.YES_OPTION)
			return true;
		else
			return false;
	}

	/**
	 * Starts the application
	 */
	public static void main(String[] args) {
		new CaterpillarGame();
	}
}
