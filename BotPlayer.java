import java.util.LinkedList;
import java.util.Queue;

/**
 * Class for the bot player that also contains the logic behind its behaviour
 */
public class BotPlayer extends Player
{
	// the algorithm used for bot movement
	private AStarAlgorithm aStar;

	private Queue<int[]> route = new LinkedList<int[]>();
	// how the bot is represented on the map
	private char mapRepresentation = 'B';
	// x and y coordinates of the human player on the map
	private int positionX;
	private int positionY;
	// bools to indicate whether player is on exit or gold or neither
	private boolean isOnGold;
	private boolean isOnExit;
	// the difficulty of the bot - it can be 1,2,3 or 4
	private int difficulty;
	// a 2d boolean array with the size of the bot map to indicate which points are
	// known(or seen with the look function)
	private boolean[][] knownPoints;
	// the coordinates the bot will head towards
	private int[] destination;
	// contains the number of turns since the player hasn't used the look(works for
	// NORMAL bot only)
	private int lastLook;

	/**
	 * Constructor for bot player
	 * 
	 * @param x          : the x coordinate of the player
	 * @param y          : the y coordinate of the player
	 * @param isOnExit   : whether player is on top of an exit or not
	 * @param isOnGold   : whether player is on top of an exit or not
	 * @param difficulty : the difficulty of the bot
	 * @param map        : the original map which the bot map is build from
	 */
	public BotPlayer(int x, int y, boolean isOnExit, boolean isOnGold, int difficulty, char[][] map)
	{
		// set the simple values
		destination = new int[2];
		positionX = x;
		positionY = y;
		this.difficulty = difficulty;
		this.isOnExit = isOnExit;
		this.isOnGold = isOnGold;
		lastLook = 0;

		char[][] botMap = new char[map.length][map[0].length];
		knownPoints = new boolean[map.length][map[0].length];

		// loop through each of the elements in the original map
		for (int i = 0; i < map.length; i++)
		{
			for (int j = 0; j < map[0].length; j++)
			{
				if (difficulty == 4)
				{
					// if the difficulty is 4 the bot knows the whole map and starts persuing the
					// player at the start
					if (map[i][j] == 'P')
					{
						destination[0] = i;
						destination[1] = j;
						aStar = new AStarAlgorithm(map);
						getRouteFromAStar();

						return;
					}
				}
				else if (i == 0 || j == 0 || i == map.length - 1 || j == map[0].length - 1)
				{
					// if it's the edge of the map the point is wall and is known by the bot
					botMap[i][j] = '#';
					knownPoints[i][j] = true;
				}
				else
				{
					// if it's not the edge the bot assumes it's simple floor tile and it is unknown
					knownPoints[i][j] = false;
					botMap[i][j] = '.';
				}
			}
		}
		aStar = new AStarAlgorithm(botMap);
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

	/**
	 * @return : difficulty of the bot
	 */
	protected int getDifficulty()
	{
		return difficulty;
	}

	/**
	 * Uses AStarAlgorithm to determine the route to the destination of the bot
	 */
	private void getRouteFromAStar()
	{
		// if there is a destination
		if (destination[0] != 0)
		{
			// clear previous route
			route.clear();
			// go through all the coordinates from the A* algorithm and add them to the
			// route queue in this class
			int[][] routeCoordinates;
			routeCoordinates = aStar.pathfind(positionX, positionY, destination[0], destination[1]);
			for (int i = 0; i < routeCoordinates.length; i++)
			{
				route.add(routeCoordinates[i]);
			}
		}
	}

	/**
	 * Set a specific point in the algorithm map(bot map) and calculates the route
	 * if the point is P
	 * 
	 * @param x : the x coordinate of the point
	 * @param y : the y coordinate of the point
	 * @param c : the character the point is set to
	 */
	protected void setBotMapPoint(int x, int y, char c)
	{
		// sets the point in A* algorithm and makes it known
		aStar.setMapPoint(x, y, c);
		knownPoints[x][y] = true;
		if (c == 'P')
		{
			// if the player is seen get the route towards him
			destination[0] = x;
			destination[1] = y;
			getRouteFromAStar();
		}
		else if (c == 'B' && destination[0] == positionX && destination[1] == positionY)
		{
			// if the bot has reached the destination--->reset it
			destination[0] = 0;
			destination[1] = 0;
		}
	}

	/**
	 * Returns the next location or determines it if there is none
	 * 
	 * @return : the coordinates of the next location the bot moves to
	 */
	protected int[] getNextPoint()
	{
		if (route.isEmpty())
		{
			if (difficulty == 1)
			{
				// in case the difficulty is 1 the bot just takes a random coordinate next to
				// him and returns that
				while (true)
				{
					int randX = (int) (Math.random() * 3) - 1;
					int randY = (int) (Math.random() * 3) - 1;
					// the new coordinates have to near the bot(so the increase of either has to be
					// 0 but not both)
					if (randX == 0 ^ randY == 0)
					{
						return new int[]
						{ randX + positionX, randY + positionY };
					}
				}
			}
			if (difficulty == 3 || difficulty == 2)
			{
				// difficulties 2 and 3 explore the map the same way

				/*
				 * the loop is named so that it can be exited with break it goes through each
				 * point in a segment(maximum 8x8) around the bot and checks if the point is
				 * unknown - if it is head towards it
				 */
				beginningLoop: for (int i = Math.max(positionX - 4, 1); i < Math.min(positionX + 4, knownPoints.length - 1); i++)
				{
					for (int j = Math.max(positionY - 4, 1); j < Math.min(positionY + 4, knownPoints[0].length - 1); j++)
					{
						if (!knownPoints[i][j])
						{
							destination[0] = i;
							destination[1] = j;
							getRouteFromAStar();
							break beginningLoop;
						}
					}
				}
				// if the route is still empty after the loop above the bot heads toward a
				// random point on the map
				if (route.isEmpty())
				{
					destination[0] = (int) (Math.random() * (knownPoints.length - 2)) + 1;
					destination[1] = (int) (Math.random() * (knownPoints[0].length - 2)) + 1;
					// set that point to unknown in case it is a wall(in which case the program
					// would crash)
					knownPoints[destination[0]][destination[1]] = false;
					aStar.setMapPoint(destination[0], destination[1], '.');

					getRouteFromAStar();
				}
			}
		}

		// this if determines when the bot will use LOOK(only in case of difficulty 2)
		if (difficulty == 2)
		{
			// if the next point is unknown or the bot hasn't looked in a while
			if (!knownPoints[route.peek()[0]][route.peek()[1]] || lastLook > 3)
			{
				lastLook = 0;
				return null;// returns null which indicates the bot has to use LOOK
			}
		}

		try
		{
			lastLook++;
			return route.poll();
		} finally
		{
			// if the bot difficulty is 3 the bot needs to clear the route often in order
			// not to end up in a wall
			if (difficulty == 3 && lastLook > 1)
			{
				System.out.println("route cleared");
				lastLook = 0;
				route.clear();
				destination[0] = 0;
				destination[1] = 0;
			}
		}
	}

}
