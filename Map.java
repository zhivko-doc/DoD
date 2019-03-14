import java.io.*;

/**
 * Reads and contains in memory the map of the game.
 *
 */
public class Map
{
	// Representation of the map
	private char[][] map;

	// Map name
	private String mapName;

	// Gold required for the human player to win
	private int goldRequired;

	/**
	 * Default constructor, creates the default map "Very small Labyrinth of doom".
	 */
	public Map()
	{
		mapName = "Very small Labyrinth of Doom";
		goldRequired = 2;
		map = new char[][]
		{
				{ '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#' },
				{ '#', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '#' },
				{ '#', '.', '.', '.', '.', '.', '.', 'G', '.', '.', '.', '.', '.', '.', '.', '.', '.', 'E', '.', '#' },
				{ '#', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '#' },
				{ '#', '.', '.', 'E', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '#' },
				{ '#', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', 'G', '.', '.', '.', '.', '.', '.', '#' },
				{ '#', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '#' },
				{ '#', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '#' },
				{ '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#' } };
	}

	/**
	 * Constructor that accepts a map to read in from.
	 *
	 * @param fileName : The filename of the map file.
	 * @throws IOException               if there is no file with that name
	 * @throws IndexOutOfBoundsException if the file structure is wrong
	 * @throws NumberFormatException     if the win condition line in the file is
	 *                                   wrong
	 */
	public Map(String fileName) throws IOException, IndexOutOfBoundsException, NumberFormatException
	{
		readMap(fileName);
	}

	/**
	 * @return : Gold required to exit the current map.
	 */
	protected int getGoldRequired()
	{
		return goldRequired;
	}

	/**
	 * @return : The map as stored in memory.
	 */
	protected char[][] getMap()
	{
		return map;
	}

	/**
	 * Get a specific char from the map
	 * 
	 * @param : coordinate x of the char
	 * @param : coordinate y of the char
	 * @return : a specific char
	 */
	protected char getMapPoint(int x, int y)
	{
		return map[x][y];
	}

	/**
	 * @return : The name of the current map.
	 */
	protected String getMapName()
	{
		return mapName;
	}

	/**
	 * 
	 * @param positionX : the x coordinate of the point
	 * @param positionY : the y coordinate of the point
	 * @param c         : the char we want to set that point to
	 */
	protected void setMapPoint(int positionX, int positionY, char c)
	{
		map[positionX][positionY] = c;
	}

	/**
	 * Reads a file with a map
	 * 
	 * @param fileName : The filename of the map file
	 * @throws IOException               if there is no file with that name
	 * @throws IndexOutOfBoundsException if the file structure is wrong
	 * @throws NumberFormatException     if the win condition line in the file is
	 *                                   wrong
	 */
	private void readMap(String fileName) throws IOException, IndexOutOfBoundsException, NumberFormatException
	{
		// get the current directory
		String fileLocation = System.getProperty("user.dir");
		// complete the file location with the name of the file
		fileLocation += "/" + fileName + ".txt";
		BufferedReader fileReader = new BufferedReader(new FileReader(fileLocation));
		// map name begins from index 5 in the first line of the file
		String line = fileReader.readLine();
		mapName = line.substring(5);
		// gold needed to win starts from index 4 of the second line
		line = fileReader.readLine();
		goldRequired = Integer.parseInt(line.substring(4));

		String mapInLine = "";// temporary string that will contain the whole map
		for (int x = 0; true; x++)
		{
			// read the map from the file until it's over
			line = fileReader.readLine();
			if (line == null)
			{
				fileReader.close();
				// determine the size of the map char array
				map = new char[x][mapInLine.length() / x];
				break;
			}
			mapInLine += line;
		}

		// go through each element in mapInLine and assign it to the corresponding spot
		// in the map char array
		for (int i = 0; i < map.length; i++)
		{
			for (int j = 0; j < map[0].length; j++)
			{
				map[i][j] = mapInLine.charAt(i * map[0].length + j);
			}
		}
	}

}
