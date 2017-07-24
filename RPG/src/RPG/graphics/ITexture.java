package RPG.graphics;

public interface ITexture
{
	public Texture getTexture();

	public int getWidth();

	public int getHeight();

	public float getU();

	public float getV();

	public float getU2();

	public float getV2();
	
	public void set(int x, int y , int width, int height);
}
