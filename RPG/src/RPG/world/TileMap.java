package RPG.world;

import static org.lwjgl.opengl.GL11.GL_REPEAT;
import static org.lwjgl.opengl.GL14.GL_MIRRORED_REPEAT;
import RPG.Main;
import RPG.graphics.Camera;
import RPG.graphics.SpriteBatcher;
import RPG.graphics.TextureRegion;
import RPG.math.Matrix4f;
import RPG.math.Vec2f;
import RPG.math.Vec3f;

public class TileMap
{

	public int mapWidth, mapHeight;
	public static int TILE_WIDTH = 64;
	public static int TILE_HEIGHT = 64;
	public static int TILE_SIZE = 64;
	// public static TileMap tileMap;
	public Tile[][] tileSet;
	public int tileType[][];
	public TextureRegion tileSheet;
	int rows, cols;
	int level;
	float factor;
	float depth;

	public TileMap(TextureRegion tex, int mapWidth, int mapHeight, int level, float factor, float depth)
	{

		this.tileSheet = tex;
		this.level = level;
		this.factor = factor;
		this.mapWidth = mapWidth;
		this.mapHeight = mapHeight;
		this.depth = depth;
		this.rows = mapHeight / TILE_HEIGHT;
		this.cols = mapWidth / TILE_WIDTH;
		System.out.println("Rows " + rows + "Cols " + cols);
		tileSet = new Tile[rows][cols];
		tileType = new int[rows][cols];
	

	}

	public void loadTileMap()
	{

		for (int y = 0; y < rows; y++)
			for (int x = 0; x < cols; x++)
			{
				tileType[y][x] = 0;
				if (x == 0 || x == (cols - 1))
				{
					tileType[y][x] = 1;
				}
				if (y == 0 || y == rows - 1)
				{
					tileType[y][x] = 2;
				}
			}

		tileType[9][6] = 0;
		tileType[9][7] = 0;
		tileType[6][7] = 0;

		for (int y = 0; y < rows; y++)
			for (int x = 0; x < cols; x++)
			{
				if (tileType[y][x] == 0)
					tileSet[y][x] = new Tile(x, y, 0, 0, 60, 98, false, "Air");
			else if (tileType[y][x] == 1)
					tileSet[y][x] = new Tile(x, y, 2*64, 2*64, 64, 64, true, "Wall");
				else if (tileType[y][x] == 2)
					tileSet[y][x] = new Tile(x, y, 0, 0, 64, 64, true, "Ground");
			}
		/*
		 * for (int y = 0; y < rows; y++) { System.out.println(""); for (int x = 0; x < cols; x++) { System.out.print( "( " + tileSet[y][x].x + ", " + tileSet[y][x].y + ")"); } }
		 */

	}

	public void drawMap()
	{
		// tileSheet.getTexture().bind();
		SpriteBatcher.batcher.begin();
		// SpriteBatcher.batcher.setViewMatrix( Matrix4f.identity());
		// SpriteBatcher.batcher.updateUniforms();
		int width = Main.getWidth();
		int height = Main.getHeight();
		for (int y = 0; y < rows; y++)
			for (int x = 0; x < cols; x++)
			{
				if (tileType[y][x] == 0)
					continue;
				Tile tile = tileSet[y][x];
				SpriteBatcher.batcher.draw(tileSheet, tile.getX() * TILE_WIDTH, tile.getY() * TILE_HEIGHT, depth, TILE_WIDTH, TILE_HEIGHT, 0, 0, 0f, tile.texU, tile.texV, tile.texWidth, tile.texHeight); // last 2 arguments must be spritesheet width and height
				// SpriteBatcher.batcher.draw(tileSheet, tile.getX() * TILE_WIDTH, tile.getY() * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT, 0, 0, 0f, 0f, 0f, 0.25f, 0.25f ); //last 2 arguments must be spritesheet width and height
			}

		SpriteBatcher.batcher.end();
		// tileSheet.getTexture().unbind();
	}
/*	for (int y = Camera.cam.y; y < viewHeight; y+= TILE_HEIGHT)
		for (int x = Camera.cam.x; x < viewWidth; x+= TILE_WIDTH)
		{
			if (tileType[y/TILE_HEIGHT][x/TILE_WIDTH] == 0)
				continue;*/
	public void updateViewMatrix()
	{
		//System.out.println("Cam.x" + Camera.cam.x + "Cam.y" + Camera.cam.y );
		Matrix4f view = Matrix4f.identity();
		view = Matrix4f.multiply(Matrix4f.translate(new Vec3f(Camera.cam.x * factor, Camera.cam.y * factor, 0f)), view);
		SpriteBatcher.batcher.setViewMatrix(view);
		SpriteBatcher.batcher.updateUniforms();
	}

	public void loadMap()
	{
		tileSheet.getTexture().setWrap(GL_REPEAT, GL_MIRRORED_REPEAT);
		for (int y = 0; y < rows; y++)
			for (int x = 0; x < cols; x++)
			{
				tileType[y][x] = 1;
				tileSet[y][x] = new Tile(x, y, x * TILE_WIDTH, y * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT, false, "Background");
			}
	}

}
