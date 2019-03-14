import java.io.*;

/**
 * Runs the game with a human player and contains code needed to read inputs.
 *
 */
public class HumanPlayer extends Player
{
	// how the player is represented on the map
	private char mapRepresentation = 'P';
	// x and y coordinates of the human player on the map
	private int positionX;
	private int positionY;
	// current gold the human player owns
	private int gold;
	// bools to indicate whether player is on exit or gold or neither
	private boolean isOnGold;
	private boolean isOnExit;

	/**
	 * Default constructor
	 */
	public HumanPlayer()
	{
		positionX = 1;
		positionY = 1;
		isOnGold = false;
		gold = 0;
		isOnExit = false;
	}

	/**
	 * Constructor to set with location and whether player is on exit or not
	 * 
	 * @param x        : the x coordinate of the player
	 * @param y        : the y coordinate of the player
	 * @param isOnExit : whether player is on top of an exit or not
	 */
	public HumanPlayer(int x, int y, boolean isOnExit)
	{
		positionX = x;
		positionY = y;
		isOnGold = false;
		gold = 0;
		this.isOnExit = isOnExit;
	}

	/**
	 * @return : the char map representation of the player
	 */
	protected char getMapRepresentation()
	{
		return mapRepresentation;
	}

	/**
	 * @return : position x of the player
	 */
	protected int getPositionX()
	{
		return positionX;
	}

	/**
	 * @return : position y of the player
	 */
	protected int getPositionY()
	{
		return positionY;
	}

	/**
	 * @return : whether player is on top of gold
	 */
	protected boolean isOnGold()
	{
		return isOnGold;
	}

	/**
	 * @return : whether player is on top of exit
	 */
	protected boolean isOnExit()
	{
		return isOnExit;
	}

	/**
	 * @param newX : new position x of the player
	 */
	protected void setPositionX(int newX)
	{
		positionX = newX;
	}

	/**
	 * @param newY : new position Y of the player
	 */
	protected void setPositionY(int newY)
	{
		positionY = newY;
	}

	/**
	 * @param isOnGold : set whether player is on gold
	 */
	protected void setIsOnGold(boolean isOnGold)
	{
		this.isOnGold = isOnGold;
	}

	/**
	 * @param isOnGold : set whether player is on gold
	 */
	protected void setIsOnExit(boolean isOnExit)
	{
		this.isOnExit = isOnExit;
	}

	protected void increaseGold()
	{
		gold++;
	}

	protected int getGold()
	{
		return gold;
	}

	/**
	 * Reads player's input from the console.
	 * <p>
	 * return : A string containing the input the player entered.
	 */
	protected String getInputFromConsole()
	{
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

		try
		{
			while (true)
			{
				// get the user input
				String input = reader.readLine();
				if (input.equals(""))
				{
					// keep reading input if the previous one was just enter(empty line)
				}
				else if (input.equals("EXIT"))
				{
					// the player can exit the application with EXIT(and has to confirm it with Y)
					System.out.println("Are you sure you want to exit?");
					System.out.println("Y or N");
					input = reader.readLine();
					if (input.equals("Y"))
					{
						System.exit(0);
					}
				}
				else
				{
					return input;
				}
			}

		} catch (IOException e)
		{
			System.err.println(e.getMessage());
			return "";
		}
	}
}
