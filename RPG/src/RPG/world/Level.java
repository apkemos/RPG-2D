package RPG.world;

import java.util.List;

public class Level
{
	public int width;
	public int  height;
	public List<TileMap> tileMaps;
	
	public Level(int lvlWidth, int lvlHeight, List<TileMap> tileMaps )
	{
		this.width = lvlWidth;
		this.height = lvlHeight;
		this.tileMaps = tileMaps;
	}
}
