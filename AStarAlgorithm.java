import java.util.PriorityQueue;

/**
 * The algorithm used for bot movement
 */
public class AStarAlgorithm
{
	// The map used for the algorithm(currently it is only the bot map since only
	// bot uses A*)
	private char[][] map;

	// a priority queue of all the points that are "visited" during the algorithm
	private PriorityQueue<Point> visitedPoints = new PriorityQueue<>();

	// a collection of all the points on the map
	private Point[][] points;

	// an array that holds the coordinates of the route
	private int[][] finalRoute;

	/**
	 * Class to represent a point on the map
	 */
	private class Point implements Comparable<Point>
	{
		// x and y coordinate of the point
		int x;
		int y;
		// the points will be compared by their priority in the priority queue
		int priority;
		// the distance from the beginning(to this point)
		int distance;
		/*
		 * string containing the route so far its structure is like this x1-y1 x2-y2...
		 * where x1,y1,x2,y2,.. are the coordinates of the corresponding point
		 */
		String route;
		// bool to indicate whether the point has already been "visited"
		boolean visited;

		/**
		 * Default constructor
		 */
		private Point()
		{
			route = "";
			priority = 0;
			distance = 0;
			visited = false;
		}

		/**
		 * Determines the heuristic between the current and another point
		 * 
		 * @param end : the end point
		 * @return the distance between current and end point
		 */
		private int heuristic(Point end)
		{
			return Math.abs(x - end.x) + Math.abs(y - end.y);
		}

		/**
		 * @return : all point coordinates that are 1 distance away from the
		 *         current(neighbours)
		 */
		private int[][] getNeighbourPoints()
		{
			return new int[][]
			{
					{ x + 1, y },
					{ x, y + 1 },
					{ x - 1, y },
					{ x, y - 1 } };
		}

		/**
		 * Compares 2 points by their priority(in the priority queue)
		 */
		@Override
		public int compareTo(Point o)
		{
			return Integer.compare(this.priority, o.priority);
		}
	}

	/**
	 * Constructor for the class
	 * 
	 * @param map : the map that the algorithm will use
	 */
	public AStarAlgorithm(char[][] map)
	{
		this.map = map;
		points = new Point[map.length][map[0].length];
		for (int i = 0; i < map.length; i++)
		{
			for (int j = 0; j < map[0].length; j++)
			{
				points[i][j] = new Point();
				points[i][j].x = i;
				points[i][j].y = j;
			}
		}
	}

	/**
	 * Method with the logic behind the A* algorithm. Finds the shortest route from
	 * a start to an end point and returns it.
	 * 
	 * @param x1 : x coordinate of the start point
	 * @param y1 : y coordinate of the start point
	 * @param x2 : x coordinate of the end point
	 * @param y2 : y coordinate of the end point
	 * @return : a 2d array of the coordinates of the route
	 */
	protected int[][] pathfind(int x1, int y1, int x2, int y2)
	{
		// handle the attributes of the first point
		points[x1][y1].distance = 0;
		points[x1][y1].visited = true;
		points[x1][y1].route = "";
		visitedPoints.add(points[x1][y1]);
		try
		{
			while (true)
			{
				// go through every point in the priority queue until we reach the end point
				Point curPoint = visitedPoints.poll();
				// check if the end point has been reached
				if (curPoint.x == x2 && curPoint.y == y2)
				{
					// split the route into pieces
					String[] coordinates = curPoint.route.split(" ");
					finalRoute = new int[coordinates.length][2];
					for (int i = 0; i < coordinates.length; i++)
					{
						String[] point = coordinates[i].split("-");
						// store each coordinate in final route
						finalRoute[i][0] = Integer.parseInt(point[0]);
						finalRoute[i][1] = Integer.parseInt(point[1]);
					}
					// reset values for future use
					visitedPoints.clear();
					resetVisitedValue();

					return finalRoute;
				}
				// if the point hasn't been reached yet keep looking

				// go through each of the neighbours of the current point
				for (int[] coordinate : curPoint.getNeighbourPoints())
				{
					Point neighbour = points[coordinate[0]][coordinate[1]];

					// if the neighbour hasn't been "visited" yet and it isn't a wall
					if (!(neighbour.visited || map[coordinate[0]][coordinate[1]] == '#'))
					{
						// handle the attributes of the neighbour and add it to the priority queue
						neighbour.distance = curPoint.distance + 1;
						neighbour.visited = true;
						neighbour.route = curPoint.route + coordinate[0] + "-" + coordinate[1] + " ";
						neighbour.priority = neighbour.distance + neighbour.heuristic(points[x2][y2]);
						visitedPoints.add(neighbour);
					}
				}
			}
		} catch (NullPointerException e)
		{
			return null;
		}

	}

	/**
	 * 
	 * @param positionX : x coordinate of the point in the map
	 * @param positionY : y coordinate of the point in the map
	 * @param c         : the new char representation of that point
	 */
	protected void setMapPoint(int positionX, int positionY, char c)
	{
		map[positionX][positionY] = c;
	}

	/**
	 * Reset the visited value of every point so that they can be used again in the
	 * future
	 */
	private void resetVisitedValue()
	{
		for (int i = 0; i < points.length; i++)
		{
			for (int j = 0; j < points[0].length; j++)
			{
				points[i][j].visited = false;
			}
		}
	}
}
