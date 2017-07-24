package RPG.graphics;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import java.nio.FloatBuffer;
import java.util.List;

import RPG.utils.BufferUtils;

public class VertexArray
{

	protected VertexAttrib[] attributes;

	private int totalNumComponents;
	private int stride;
	private FloatBuffer buffer;
	private int vertCount;

	/**
	 * 
	 * @param vertCount
	 *            the number of VERTICES; e.g. 3 verts to make a triangle, regardless of number of attributes
	 * @param attributes
	 *            a list of attributes per vertex
	 */
	public VertexArray(int vertCount, VertexAttrib... attributes)
	{
		this.attributes = attributes;
		for (VertexAttrib a : attributes)
			totalNumComponents += a.numComponents;
		this.vertCount = vertCount;

		// our buffer which holds our data
		this.buffer = BufferUtils.createFloatBuffer(vertCount * totalNumComponents);
	}

	public VertexArray(int vertCount, List<VertexAttrib> attributes)
	{
		this(vertCount, attributes.toArray(new VertexAttrib[attributes.size()]));
	}

	public VertexArray flip()
	{
		buffer.flip();
		return this;
	}

	public VertexArray clear()
	{
		buffer.clear();
		return this;
	}

	public VertexArray put(float[] verts, int offset, int length)
	{
		buffer.put(verts, offset, length);
		return this;
	}

	public VertexArray put(float f)
	{
		buffer.put(f);
		return this;
	}

	public FloatBuffer buffer()
	{
		return buffer;
	}

	public int getTotalNumComponents()
	{
		return totalNumComponents;
	}

	public int getVertexCount()
	{
		return vertCount;
	}

	public void bind()
	{
		int offset = 0;
		// 4 bytes per float
		int stride = totalNumComponents * 4;

		for (int i = 0; i < attributes.length; i++)
		{
			VertexAttrib a = attributes[i];
			buffer.position(offset);
			glVertexAttribPointer(a.location, a.numComponents, false, stride, buffer);
			glEnableVertexAttribArray(a.location);
			offset += a.numComponents;
		}
	}

	public void draw(int geom, int first, int count)
	{
		glDrawArrays(geom, first, count);
	}

	public void unbind()
	{
		for (int i = 0; i < attributes.length; i++)
		{
			VertexAttrib a = attributes[i];
			glDisableVertexAttribArray(a.location);
		}
	}
}
