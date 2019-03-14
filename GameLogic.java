import java.io.*;

/**
 * Contains the main logic part of the game, as it processes.
 *
 */
public class GameLogic
{
	/*
	 * The game logic class contains a map,bot and human player object and performs
	 * operation with/on them
	 */
	private Map map;
	private HumanPlayer humanPlayer;
	private BotPlayer botPlayer;
	// bool to indicate whose turn it is(true for player, false for bot)
	private boolean playerTurn = true;

	/**
	 * Default constructor
	 */
	public GameLogic()
	{
		// create a default human player to use for input
		humanPlayer = new HumanPlayer();
	}

	/**
	 * Spawns the human player on a random tile(that isn't wall or gold) and updates
	 * the map.
	 */
	protected void spawnHumanPlayer()
	{
		// random coordinates of the player
		int randomX = (int) (Math.random() * (map.getMap().length - 1)) + 1;
		int randomY = (int) (Math.random() * (map.getMap()[0].length - 1)) + 1;
		/*
		 * loops until the coordinates are valid the random function is good enough so
		 * that it will cover all possibilities before long
		 */
		while (true)
		{
			// if the tile is a wall or gold keep looping
			if (map.getMap()[randomX][randomY] == '#' || map.getMap()[randomX][randomY] == 'G')
			{
				randomX = (int) (Math.random() * (map.getMap().length - 1)) + 1;
				randomY = (int) (Math.random() * (map.getMap()[0].length - 1)) + 1;
			}
			else
			{
				// if the coordinates are valid
				// check whether player is on exit tile
				boolean isOnExit = false;
				if (map.getMap()[randomX][randomY] == 'E')
				{
					isOnExit = true;
				}
				// create a player object with the new coordinates
				humanPlayer = new HumanPlayer(randomX, randomY, isOnExit);
				map.setMapPoint(randomX, randomY, 'P');
				return;
			}
		}
	}

	/**
	 * Spawns the player on a random tile(that isn't wall or gold) and updates the
	 * map.
	 *
	 * @param : the difficulty of the bot
	 * 
	 */
	protected void spawnBot(int difficulty)
	{
		// random coordinates of the bot
		int randomX = (int) (Math.random() * (map.getMap().length - 1)) + 1;
		int randomY = (int) (Math.random() * (map.getMap()[0].length - 1)) + 1;
		// loops until the coordinates are valid
		while (true)
		{
			// if the tile is a wall or human player keep looping
			if (map.getMapPoint(randomX, randomY) == '#' || map.getMapPoint(randomX, randomY) == 'P')
			{
				randomX = (int) (Math.random() * (map.getMap().length - 1)) + 1;
				randomY = (int) (Math.random() * (map.getMap()[0].length - 1)) + 1;
			}
			else
			{
				// check whether bot in os gold or exit
				boolean isOnExit = false;
				boolean isOnGold = false;
				if (map.getMapPoint(randomX, randomY) == 'E')
				{
					isOnExit = true;
				}
				else if (map.getMapPoint(randomX, randomY) == 'G')
				{
					isOnGold = true;
				}
				// spawn bot with the new attributes
				botPlayer = new BotPlayer(randomX, randomY, isOnExit, isOnGold, difficulty, map.getMap());
				map.setMapPoint(randomX, randomY, 'B');
				return;
			}
		}
	}

	/**
	 * Processes the human player input and call the corresponding method
	 * 
	 * @param command : the input of the human player
	 * @return : : Protocol if success or fail.
	 */
	protected String processCommand(String command)
	{
		/*
		 * Processes the command if it follows the protocol strictly - execute the
		 * corresponding method if it doesn't the answer is invalid and player loses a
		 * turn
		 */
		switch (command)
		{
		case "MOVE N":
			return move(humanPlayer.getPositionX() - 1, humanPlayer.getPositionY());
		case "MOVE S":
			return move(humanPlayer.getPositionX() + 1, humanPlayer.getPositionY());
		case "MOVE E":
			return move(humanPlayer.getPositionX(), humanPlayer.getPositionY() + 1);
		case "MOVE W":
			return move(humanPlayer.getPositionX(), humanPlayer.getPositionY() - 1);
		case "HELLO":
			return hello();
		case "GOLD":
			return gold();
		case "PICKUP":
			return pickup();
		case "LOOK":
			look(humanPlayer.getPositionX(), humanPlayer.getPositionY());
			return "";
		case "QUIT":
			quitGame();
		default:
			return "Invalid";
		}
	}

	/**
	 * Returns the gold required to win.
	 *
	 * @return : Gold required to win.
	 */
	protected String hello()
	{
		return "Gold to win: " + map.getGoldRequired();
	}

	/**
	 * Returns the gold currently owned by the player.
	 *
	 * @return : Gold currently owned.
	 */
	protected String gold()
	{
		return "Gold owned: " + humanPlayer.getGold();
	}

	/**
	 * Processes the player's pickup command, updating the map and the player's gold
	 * amount.
	 *
	 * @return If the player successfully picked-up gold or failed.
	 */
	protected String pickup()
	{
		if (humanPlayer.isOnGold())
		{
			humanPlayer.increaseGold();
			humanPlayer.setIsOnGold(false);
			return "SUCCESS. Gold owned: " + humanPlayer.getGold();
		}
		else
		{
			return "FAIL. Gold owned: " + humanPlayer.getGold();
		}
	}

	/**
	 * Displays whether the human player wins or not and quits the game, shutting
	 * down the application.
	 */
	protected void quitGame()
	{
		// if the 2 conditions are met(human player is on exit and has enough gold) he
		// wins
		if (humanPlayer.isOnExit() && humanPlayer.getGold() >= map.getGoldRequired())
		{
			System.out.println("WIN. CONGRATULATION YOU ARE THE BEST!");
			System.exit(0);
		}
		else
		{
			System.out.println("LOSE");
			System.exit(0);
		}
	}

	/**
	 * Prints the 5x5 segment of the map around the player or updates the bot map
	 * 
	 * @param x : the x coordinate of player(bot or human)
	 * @param y : the y coordinate of player(bot or human)
	 */
	protected void look(int x, int y)
	{
		// determine the boundaries(if player is on the edge the boundaries will be 4x4
		// and not 5x5)
		int beginX = Math.max(x - 2, 0);
		int endX = Math.min(x + 2, map.getMap().length - 1);
		int beginY = Math.max(y - 2, 0);
		int endY = Math.min(y + 2, map.getMap()[0].length - 1);

		// loop through each of the elements in the segment around the player
		for (int i = beginX; i <= endX; i++)
		{
			for (int j = beginY; j <= endY; j++)
			{
				/*
				 * If it's the player's turn print it if it's the bot's turn update the bot
				 * map(in the algorithm)
				 */
				if (playerTurn)
				{
					System.out.print(map.getMapPoint(i, j));
				}
				else
				{
					botPlayer.setBotMapPoint(i, j, map.getMapPoint(i, j));
				}
			}
			if (playerTurn)
			{
				System.out.println();
			}
		}
	}

	/**
	 * Checks whether the move is valid
	 * 
	 * @param newX : the new x coordinate of the player
	 * @param newY : the new y coordinate of the player
	 * @return : Protocol if success or fail.
	 */
	protected String move(int newX, int newY)
	{
		// if the human player has been caught he loses and game quits
		if (map.getMapPoint(newX, newY) == 'B' || map.getMapPoint(newX, newY) == 'P')
		{
			System.out.println("LOSE. YOU HAVE BEEN CAUGHT.");
			System.exit(0);
		}
		// if the new location is a wall the move fails
		if (map.getMapPoint(newX, newY) == '#')
		{
			return "FAIL";
		}
		else
		{
			// move the corresponding player depending on whose turn it is
			if (playerTurn)
			{
				movePlayer(newX, newY, humanPlayer);
			}
			else
			{
				movePlayer(newX, newY, botPlayer);
			}
			return "SUCCESS";
		}
	}

	/**
	 * Moves the player(bot or human) to a new location
	 * 
	 * @param newX   : the new x coordinate of the player
	 * @param newY   : the new y coordinate of the player
	 * @param player : the player that makes the move
	 */
	private void movePlayer(int newX, int newY, Player player)
	{
		// set the current player location to what it has to be
		if (player.isOnExit())
		{
			map.setMapPoint(player.getPositionX(), player.getPositionY(), 'E');
			player.setIsOnExit(false);
		}
		else if (player.isOnGold())
		{
			map.setMapPoint(player.getPositionX(), player.getPositionY(), 'G');
			player.setIsOnGold(false);
		}
		else
		{
			map.setMapPoint(player.getPositionX(), player.getPositionY(), '.');
		}

		// determine whether the the new location is gold or exit and update the player
		// attributes
		if (map.getMapPoint(newX, newY) == 'G')
		{
			player.setIsOnGold(true);
		}
		if (map.getMapPoint(newX, newY) == 'E')
		{
			player.setIsOnExit(true);
		}
		// set the new location to the player representation
		map.setMapPoint(newX, newY, player.getMapRepresentation());
		// if the difficulty is 4 the bot sees every human player move and is updated
		// for it
		if (botPlayer.getDifficulty() == 4 && playerTurn)
		{
			botPlayer.setBotMapPoint(newX, newY, 'P');
		}
		if (!playerTurn)
		{
			botPlayer.setBotMapPoint(newX, newY, 'B');
		}
		// update the new position of player
		player.setPositionX(newX);
		player.setPositionY(newY);
	}

	/**
	 * Executes the bot turn
	 */
	protected void botTurn()
	{
		int newCoordinates[];// temporary variable for the new coordinates of the bot
		if (botPlayer.getDifficulty() == 3)
		{
			// if the bot difficulty is 3 - it looks each turn and then moves
			look(botPlayer.getPositionX(), botPlayer.getPositionY());
			newCoordinates = botPlayer.getNextPoint();
			move(newCoordinates[0], newCoordinates[1]);
		}
		else
		{
			newCoordinates = botPlayer.getNextPoint();
			if (botPlayer.getDifficulty() == 2)
			{
				/*
				 * if the difficulty is 2 the bot looks either before it moves to unknown tile
				 * or if it hasn't looked in 3 turns. The logic behind this is in getNextPoint()
				 * if the bot doesn't look it moves instead
				 */
				if (newCoordinates == null)
				{
					look(botPlayer.getPositionX(), botPlayer.getPositionY());
				}
				else
				{
					move(newCoordinates[0], newCoordinates[1]);
				}
			}
			else
			{
				// if the difficulty is 1 or 4 they move every turn without looking
				move(newCoordinates[0], newCoordinates[1]);
			}
		}
	}

	/**
	 * Main method in the code. The program starts from here
	 */
	public static void main(String[] args)
	{
		// First create a simple menu for the game
		System.out.println("Welcome to Dungeons of Doom\n");
		System.out.println("Please select a map. You can choose from these:");
		System.out.println("Bridges of Doom\nChristmas Cane\nMaze of Doom\nMerry Christmas\nTiny Map\nTest Map");
		System.out.println("or you can type the name of a custom map you have created.");

		GameLogic logic = new GameLogic();

		while (true)
		{
			// keep trying to take input for map file name until it is correct
			try
			{
				String line = logic.humanPlayer.getInputFromConsole();
				logic.map = new Map(line);
				break;
			} catch (IOException e)
			{
				// if there is no such file name in the directory
				System.err.println("Wrong file name. Try again:");
			} catch (IndexOutOfBoundsException e)
			{
				// if the file structure is wrong
				System.err.println("Wrong file structure. Try again:");
			} catch (NumberFormatException e)
			{
				// if there is something wrong with the second line in the file
				System.err.println("Wrong gold win condition. Try again:");
			}
		}

		logic.spawnHumanPlayer();
		System.out.println("Please select bot difficulty: it can be EASY, NORMAL, HARD or NIGTHMARE");
		// take input for bot difficulty until it is correct
		// the loop is named so that the code can exit that and not only the switch
		// statement
		difficultyLoop: while (true)
		{
			String difficulty = logic.humanPlayer.getInputFromConsole();
			switch (difficulty)
			{
			case "EASY":
				logic.spawnBot(1);
				break difficultyLoop;
			case "NORMAL":
				logic.spawnBot(2);
				break difficultyLoop;
			case "HARD":
				logic.spawnBot(3);
				break difficultyLoop;
			case "NIGHTMARE":
				logic.spawnBot(4);
				break difficultyLoop;
			default:
				System.out.println("Unrecognised difficulty. Please try again:");
			}
		}

		System.out.println();
		System.out.println(logic.map.getMapName());
		// loop that continues until the game is over
		while (true)
		{
			// execute the human player turn
			System.out.println(logic.processCommand(logic.humanPlayer.getInputFromConsole()));
			logic.playerTurn = false;
			// execute bot player turn
			logic.botTurn();
			logic.playerTurn = true;
		}
	}
}
