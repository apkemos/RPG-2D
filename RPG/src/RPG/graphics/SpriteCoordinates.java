package RPG.graphics;

public class SpriteCoordinates
{
	public static SpriteCoordinates lookRight = new SpriteCoordinates( 0, 0 , 256 , 256);
//	public static SpriteCoordinates lookLeft = new SpriteCoordinates( 512, 1024 , 512 , 1024);
	public int x,y, width, height;
	
	public SpriteCoordinates(int x, int y, int width, int height)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height ;
	}
}
