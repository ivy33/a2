package a2;

public class Entity {
	final static int SQUARE = 1;
	final static int CIRCLE = 2;
	final static int STRING = 3;
	final static int IMAGE = 4;

	private Object ent; // entity to paint (may be null)
	private int type; // type of entity
	private int x; // x coordinate for painting
	private int y; // y coordinate for painting

	Entity(Object entArg, int typeArg, int xArg, int yArg)
	{
		ent = entArg;
		type = typeArg;
		x = xArg;
		y = yArg;
	}

	public Object getEntity()
	{
		return ent;
	}

	public int getType()
	{
		return type;
	}

	public int getX()
	{
		return x;
	}

	public int getY()
	{
		return y;
	}

}