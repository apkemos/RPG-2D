package RPG.world;

import RPG.graphics.TextureRegion;
import RPG.math.Vec2f;

public class Tile
{
	public int x, y, texU, texV;
	public int texWidth, texHeight;
	public boolean solid;
	public String type;
	

	public Tile(int x, int y, int texU, int texV,  int texWidth, int texHeight , boolean solid, String type)
	{
		this.x = x;
		this.y = y;
		this.texU = texU;
		this.texV = texV;
		this.texWidth = texWidth;
		this.texHeight = texHeight;
		this.solid = solid;
		this.type = type;
	}

	public int getX()
	{
		return x;
	}

	public int getY()
	{
		return y;
	}
	
	public boolean isSolid()
	{
		return this.solid;
	}

	public String getType()
	{
		return type;
	}


	

}
