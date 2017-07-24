package RPG.input;


import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;

import RPG.component.Components.CInput;



public class Input extends GLFWKeyCallback
{ 
	public static boolean[] keys = new boolean[65536];
	public static boolean upReleased, qPressed;
	public void invoke(long window, int key, int scancode, int action, int mods)
	{
		if ( key ==-1)
		{
			System.out.println("Key is -1");
			return;
		}
		keys[key] = ( (action == GLFW.GLFW_PRESS ) || (action == GLFW.GLFW_REPEAT)) ? true : false;
		if ( key == GLFW.GLFW_KEY_UP)
		{
			upReleased = (action == GLFW.GLFW_RELEASE) ? true : false;
		}
		if ( key == GLFW.GLFW_KEY_Q)
		{
			qPressed = (action == GLFW.GLFW_PRESS) ? true : false;
		}
		
		
	}
	
	public static boolean isKeyDown(int keycode) {
		return keys[keycode];
	}
	
	

	

	
	
}
