/**
 * Superclass for player containing the shared attributes between bot and human
 */
public class Player
{
	// all attributes that both BotPlayer and HumanPlayer have
	private char mapRepresentation;
	private int positionX;
	private int positionY;
	private boolean isOnGold;
	private boolean isOnExit;

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
}
