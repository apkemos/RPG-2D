package RPG;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.nio.ByteBuffer;

import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWvidmode;
import org.lwjgl.opengl.GLContext;

import RPG.graphics.Color;
import RPG.graphics.Shader;
import RPG.graphics.SpriteBatcher;
import RPG.input.Input;
import RPG.world.World;
import RPG.math.Matrix4f;


public class Main implements Runnable
{

	private static int width = 1280;
	private static int height = 720;
	private Thread thread;
	private boolean running = false;
	private static long window;
	public static Shader spriteShader;
	private World world; 
	private GLFWKeyCallback keyCallback;
	
	public static void main(String[] args)
	{
		  new Main().start();
	}
	
	private void start()
	{
		running = true;
		thread = new Thread(this, "Game");
		thread.start();
	}

	private void init()
	{
		if (glfwInit() != GL_TRUE) 
		{
			System.err.println("Could not initialize GLFW!");
		}
		
		glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);
		window = glfwCreateWindow(width, height, "RPG", NULL, NULL);
		if (window == NULL)
		{
			System.err.println("Could not create GLFW window!");
		}
		
		ByteBuffer vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		glfwSetWindowPos(window, (GLFWvidmode.width(vidmode) - width) / 2, (GLFWvidmode.height(vidmode) - height) / 2);
		
		glfwSetKeyCallback(window, keyCallback = new Input());
		
		glfwMakeContextCurrent(window);
		glfwShowWindow(window);
		GLContext.createFromCurrent();
		
		glEnable(GL_DEPTH_TEST);
		glDisable(GL_LIGHTING);
		System.out.println("OpenGL: " + glGetString(GL_VERSION));
		spriteShader = new Shader("shaders/bg.vert", "shaders/bg.frag");
		spriteShader.enable();
		SpriteBatcher.batcher = new SpriteBatcher(spriteShader);
		
		
		
		Matrix4f projection = Matrix4f.orthographic(0, 1280.0f, 720.0f, 0.0f, -1.0f, 1.0f);
		SpriteBatcher.batcher.setProjectionMatrix(projection);
		SpriteBatcher.batcher.updateUniforms();
		
	//	Texture texture = new Texture("res/bird.png");
	//	SpriteRenderer.spriteRenderer.DrawSprite(texture, new Vec3f(200.0f, 200.0f, 1.0f), new Vec3f(), 0.0f , new Vec3f() , new Vec3f(1.0f, 1.0f, 1.0f));

		world = new World();
	
		
		
		
	}
	public void run()
	{
		init();
		
		long lastTime = System.nanoTime();
		double delta = 0.0;
		double NS_PER_UPDATE = 1000000000.0 / 60.0;
		long timer = System.currentTimeMillis();
		int updates = 0;
		int frames = 0;
		
		while (running)
		{
			long now = System.nanoTime();
			delta += (now - lastTime ) / NS_PER_UPDATE;
			lastTime = now;
			
			if ( delta >= 1)
			{
				update(1.0f/60);
				updates++;
				delta --;
			}
			
			render();
			frames++;
			
			if (System.currentTimeMillis() - timer > 1000)
			{
				timer +=1000;
			//	System.out.println(updates + " ups, " + frames + " frames");
				updates = 0;
				frames = 0;
			}
			if (glfwWindowShouldClose(window) == GL_TRUE)
			{
				running = false;
			}
		}
		
		glfwDestroyWindow(window);
		glfwTerminate();
		
	}

	
	private void update(double delta)
	{
		glfwPollEvents();
		world.update(delta);
	}
	
	
	private void render()
	{
		glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		world.render();
		int error = glGetError();
		if (error != GL_NO_ERROR)
			System.out.println(error);
		glfwSwapBuffers(window);
	}
	public static int getWidth()
	{
		return width;
	}
	public static int getHeight()
	{
		return height;
	}
	
	public static long getWindow()
	{
		return window;
	}
	
	

}
