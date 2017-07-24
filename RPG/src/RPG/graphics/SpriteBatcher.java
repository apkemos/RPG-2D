package RPG.graphics;

import static org.lwjgl.opengl.GL11.*;

import java.util.Arrays;
import java.util.List;

import RPG.math.Matrix4f;

public class SpriteBatcher
{
	public static SpriteBatcher batcher;
	private Shader shader;
	private VertexArray quadVA;
	private Texture texture;
	public float[] vertices, texCoords;
	public Color color;
	private boolean drawing;
	public static int renderCalls = 0;
	private int idx;
	private int maxIndex;

	public static final String ATTR_COLOR = "Color";
	public static final String ATTR_POSITION = "Position";
	public static final String ATTR_TEXCOORD = "TexCoord";
	public static final List<VertexAttrib> ATTRIBUTES = Arrays.asList(new VertexAttrib(0, ATTR_POSITION, 3), new VertexAttrib(1, ATTR_COLOR, 4), new VertexAttrib(2, ATTR_TEXCOORD, 2));

	protected Matrix4f projMatrix = new Matrix4f();
	protected Matrix4f viewMatrix = Matrix4f.identity();
	private Matrix4f projViewMatrix = new Matrix4f(); // only for re-using Matrix4f objects

	public SpriteBatcher(Shader shader)
	{
		drawing = false;
		this.shader = shader;
		this.color = Color.WHITE;
		this.quadVA = new VertexArray(2000 * 8, ATTRIBUTES);
		maxIndex = 1000 * 8;
	}

	public void setShader(Shader program)
	{
		setShader(program, false);
	}

	public void setShader(Shader shader, boolean updateUniforms)
	{
		if (shader == null)
			throw new NullPointerException("shader cannot be null; use getDefaultShader instead");
		if (drawing) // if we are already drawing, flush the batch before switching shaders
			flush();
		this.shader = shader; // now switch the shader
		if (updateUniforms) // send uniform data to shader
		{
		}// updateUniforms();
		else if (drawing) // if we don't want to update, then just start the program if we are drawing
			shader.enable();
	}

	public Shader getShader()
	{
		return shader;
	}

	public void setViewMatrix(Matrix4f view)
	{
		this.viewMatrix = view;
	}

	public Matrix4f getViewMatrix()
	{
		return viewMatrix;
	}

	public void setProjectionMatrix(Matrix4f view)
	{
		this.projMatrix = view;
	}

	public Matrix4f getProjectionMatrix()
	{
		return projMatrix;
	}

	public Matrix4f getCombinedMatrix()
	{
		Matrix4f.multiply(viewMatrix, projViewMatrix);
		return projViewMatrix;
	}

	public void updateUniforms()
	{
		updateUniforms(shader);
	}

	public void updateUniforms(Shader shader)
	{
		// projViewMatrix = getCombinedMatrix();

		// bind the program before sending uniforms
		shader.enable();

		// we can now utilize ShaderProgram's hash map which may be better than
		// glGetUniformLocation

		// Store the the multiplied matrix in the "projViewMatrix"-uniform:
		shader.setUniformMat4f("view", viewMatrix);
		shader.setUniformMat4f("projection", projMatrix);
		// upload texcoord 0
		shader.setUniform1i("ourTex0", 0);

	}

	public void drawRegion(Texture tex, float srcX, float srcY, float srcWidth, float srcHeight, float dstX, float dstY)
	{
		drawRegion(tex, srcX, srcY, srcWidth, srcHeight, dstX, dstY, srcWidth, srcHeight);
	}

	public void drawRegion(Texture tex, float srcX, float srcY, float srcWidth, float srcHeight, float dstX, float dstY, float dstWidth, float dstHeight)
	{
		float u = srcX / tex.getWidth();
		float v = srcY / tex.getHeight();
		float u2 = (srcX + srcWidth) / tex.getWidth();
		float v2 = (srcY + srcHeight) / tex.getHeight();
		draw(tex, dstX, dstY, dstWidth, dstHeight, u, v, u2, v2);
	}

	public void draw(ITexture tex, float x, float y, float z)
	{
		draw(tex, x, y,z, tex.getWidth(), tex.getHeight());
	}

	public void draw(ITexture tex, float x, float y, float z, float width, float height)
	{
		draw(tex, x, y, z, width, height, tex.getU(), tex.getV(), tex.getU2(), tex.getV2());
	}

	public void draw(ITexture tex, float x, float y, float z, float width, float height, float u, float v, float u2, float v2)
	{
		draw(tex, x, y, 0f, width, height, x, y, 0f, u, v, u2, v2);
	}

	public void draw(ITexture tex, float x, float y, float z, float originX, float originY, float rotationRadians)
	{
		draw(tex, x, y, z, tex.getWidth(), tex.getHeight(), originX, originY, rotationRadians);
	}

	public void draw(ITexture sprite, float x, float y, float z, float width, float height, float originX, float originY, float rotationRadians)
	{
		draw(sprite, x, y, z, width, height, originX, originY, rotationRadians, sprite.getU(), sprite.getV(), sprite.getU2(), sprite.getV2());
	}

	public void draw(ITexture tex, float x, float y, float z,float width, float height, float originX, float originY, float rotationRadians, int x1, int y1, int texWidth, int texHeight)
	{
		float u = (float) x1 / tex.getWidth();
		float v = (float) y1 / tex.getHeight();
		float u2 = (float) (x1 + texWidth) / tex.getWidth();
		float v2 = (float) (y1 + texHeight) / tex.getHeight();
		draw(tex, x, y, z, width, height, originX, originY, rotationRadians, u, v, u2, v2);
	}

	public void draw(ITexture tex, float x, float y, float z, float width, float height, float originX, float originY, float angle, float u, float v, float u2, float v2)
	{
		checkFlush(tex);
		final float r = color.r;
		final float g = color.g;
		final float b = color.b;
		final float a = color.a;

		float x1, y1, x2, y2, x3, y3, x4, y4;

		if (angle != 0)
		{
			float scaleX = 1.0f;// width/tex.getWidth();
			float scaleY = 1.0f;// height/tex.getHeight();
			float cx = originX * scaleX;
			float cy = originY * scaleY;

			float p1x = -cx;
			float p1y = -cy;
			float p2x = width - cx;
			float p2y = -cy;
			float p3x = width - cx;
			float p3y = height - cy;
			float p4x = -cx;
			float p4y = height - cy;

			float rotationRadians = (float) Math.toRadians(angle);
			final float cos = (float) Math.cos(rotationRadians);
			final float sin = (float) Math.sin(rotationRadians);

			x1 = x + (cos * p1x - sin * p1y) + cx; // TOP LEFT
			y1 = y + (sin * p1x + cos * p1y) + cy;
			x2 = x + (cos * p2x - sin * p2y) + cx; // TOP RIGHT
			y2 = y + (sin * p2x + cos * p2y) + cy;
			x3 = x + (cos * p3x - sin * p3y) + cx; // BOTTOM RIGHT
			y3 = y + (sin * p3x + cos * p3y) + cy;
			x4 = x + (cos * p4x - sin * p4y) + cx; // BOTTOM LEFT
			y4 = y + (sin * p4x + cos * p4y) + cy;
		}
		else
		{
			x1 = x;
			y1 = y;

			x2 = x + width;
			y2 = y;

			x3 = x + width;
			y3 = y + height;

			x4 = x;
			y4 = y + height;
		}

		// top left, top right, bottom left
		vertex(x1, y1, z, r, g, b, a, u, v);
		vertex(x2, y2, z, r, g, b, a, u2, v);
		vertex(x4, y4, z, r, g, b, a, u, v2);

		// top right, bottom right, bottom left
		vertex(x2, y2, z, r, g, b, a, u2, v);
		vertex(x3, y3, z, r, g, b, a, u2, v2);
		vertex(x4, y4, z, r, g, b, a, u, v2);
	}

	private VertexArray vertex(float x, float y, float z, float r, float g, float b, float a, float u, float v)
	{
		quadVA.put(x).put(y).put(z).put(r).put(g).put(b).put(a).put(u).put(v);
		idx++;
		return quadVA;
	}

	public void begin()
	{
		if (drawing)
			throw new IllegalStateException("must not be drawing before calling begin()");
		drawing = true;
		shader.enable();
		idx = 0;
		renderCalls = 0;
		texture = null;
	}

	public void end()
	{
		if (!drawing)
			throw new IllegalStateException("must be drawing before calling end()");
		drawing = false;
		flush();
	}

	public void flush()
	{
		if (idx > 0)
		{
			quadVA.flip();
			render();
			idx = 0;
			quadVA.clear();
		}
	}

	protected void checkFlush(ITexture sprite)
	{
		if (sprite == null || sprite.getTexture() == null)
			throw new NullPointerException("null texture");

		// we need to bind a different texture/type. this is
		// for convenience; ideally the user should order
		// their rendering wisely to minimize texture binds
		if (sprite.getTexture() != this.texture || idx >= maxIndex)
		{
			// apply the last texture
			flush();
			this.texture = sprite.getTexture();
		}
	}

	public void setColor(Color col)
	{
		color.set(col);
	}

	private void render()
	{
		if (texture != null)
			texture.bind();
		quadVA.bind();
		quadVA.draw(GL_TRIANGLES, 0, idx);
		quadVA.unbind();
		renderCalls++;
	}

}

/*
 * public void DrawSprite(TextureRegion sprite, Vec3f pos, Vec3f size, float rotate, Color color) { texture = sprite.texture; float u, v, u2, v2; u = sprite.getU(); v = sprite.getV(); u2 = sprite.getU2(); v2 = sprite.getV2(); // texCoords = new float[] { u, v2, u2, v, u, v, u, v2, u2, v2, u2, v, };
 * 
 * 
 * if (shader.enabled == false) shader.enable();
 * 
 * Matrix4f translation = Matrix4f.translate(pos); Matrix4f rotation = Matrix4f.rotate(rotate); Matrix4f scale = Matrix4f.scale(size); Matrix4f model = Matrix4f.identity();
 * 
 * model = multiply(model, translation);
 * 
 * model = multiply(model, Matrix4f.translate(new Vec3f(0.5f * size.x, 0.5f * size.y, 0.0f))); model = multiply(model, rotation); model = multiply(model, Matrix4f.translate(new Vec3f(-0.5f * size.x, -0.5f * size.y, 0.0f)));
 * 
 * model = multiply(model, scale);
 */

