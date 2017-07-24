package RPG.math;

public class Vec3f
{
	public float x, y, z;
	
	public Vec3f()
	{
		x = 0.0f;
		y = 0.0f;
		z = 0.0f;
	}

	public Vec3f(float x, float y)
	{
		this(x, y, 0f);
	}
	public Vec3f(float x, float y, float z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vec3f( Vec3f vec)
	{
		this.x = vec.x;
		this.y = vec.y;
		this.z = vec.z;
	}
	
	
	public void set(float x, float y , float z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public void set(Vec3f vec)
	{
		this.x = vec.x;
		this.y = vec.y;
		this.z = vec.z;
	}


	
	
	
}
