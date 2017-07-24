package RPG.graphics;

import static org.lwjgl.opengl.GL20.*;

import java.util.HashMap;
import java.util.Map;

import RPG.math.Matrix4f;
import RPG.math.Vec3f;
import RPG.utils.ShaderUtils;

public class Shader
{
	public static final int VERTEX_ATTRIB = 0;
	public static final int TCOORD_ATTRIB = 1;

	private final int ID;
	public boolean enabled = false;
	private Map<String, Integer> locationCache = new HashMap<String, Integer>();

	public Shader( String vertex, String fragment)
	{
		ID = ShaderUtils.load(vertex, fragment);
	}
	
	public int getUniform(String name)
	{
		if (locationCache.containsKey(name))
			return locationCache.get(name);

		int result = glGetUniformLocation(ID, name);
		if (result == -1)
			System.err.println("Could not find uniform variable '" + name + "'!");
		else
			locationCache.put(name, result);
		return result;
	}

	public void setUniform1i(String name, int value)
	{
		if (!enabled)
			enable();
		glUniform1i(getUniform(name), value);
	}

	public void setUniform1f(String name, float value)
	{
		if (!enabled)
			enable();
		glUniform1f(getUniform(name), value);
	}

	public void setUniform2f(String name, float x, float y)
	{
		if (!enabled)
			enable();
		glUniform2f(getUniform(name), x, y);
	}

	public void setUniform3f(String name, Vec3f vector)
	{
		if (!enabled)
			enable();
		glUniform3f(getUniform(name), vector.x, vector.y, vector.z);
	}
	
	public void setUniform4f(String name, Color color)
	{
		if (!enabled)
			enable();
		glUniform4f(getUniform(name), color.r, color.g, color.b, color.a);
	}
	

	public void setUniformMat4f(String name, Matrix4f matrix)
	{
		if (!enabled)
			enable();
		glUniformMatrix4(getUniform(name), false, matrix.toFloatBuffer());
	}

	public void enable()
	{
		glUseProgram(ID);
		enabled = true;
	}
	
	public void disable() {
		glUseProgram(0);
		enabled = false;
	}
}
