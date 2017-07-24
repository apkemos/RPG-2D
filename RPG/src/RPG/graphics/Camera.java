package RPG.graphics;

import RPG.Main;

public class Camera
{
	public static Camera cam = new Camera(0, 0);
	public int x, y;
	
	public Camera()
	{
	}
	
	public Camera(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
}
