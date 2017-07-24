package RPG.math;

import RPG.component.Components.CPosition;

public class Vec2f
{
	public float x, y;
	
	public Vec2f()
	{
		x = 0.0f;
		y = 0.0f;
	}

	public Vec2f(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
	
	
	public Vec2f( Vec2f vec)
	{
		this.x = vec.x;
		this.y = vec.y;
	}

	public Vec2f sub(Vec2f vec)
	{
		this.x -= vec.x;
		this.y -= vec.y;
		return this;
	}

	public Vec2f perp()
	{
		return new Vec2f( -y , x);
	}

	public Vec2f normalize()
	{
		float length = (float) Math.sqrt( x*x + y*y);
		this.x /= length;
		this.y /= length;
		return this;
	}

	public float dot(Vec2f vec)
	{
		return x *vec.x + y * vec.y;
	}
	
	public static Vec2f[] rotate( Vec2f[] points, CPosition pos  )
	{
		float scaleX = 1.0f;
		float scaleY = 1.0f;
		float cx = pos.xOrigin * scaleX;
		float cy = pos.yOrigin * scaleY;
		Vec2f[] transform = new Vec2f[points.length];
		for (int i = 0; i < transform.length; i++)
			{
				transform[i] = new Vec2f();
			}
		float p1x = -cx;
		float p1y = -cy;
		float p2x = pos.width  - cx;
		float p2y = -cy;
		float p3x = pos.width - cx; //Change to collidable points[1].x etc etc
		float p3y = pos.height - cy;
		float p4x = -cx;
		float p4y = pos.height - cy;
		
		float rotationRadians =  (float) Math.toRadians(pos.rot);
		final float cos = (float) Math.cos(rotationRadians);
		final float sin = (float) Math.sin(rotationRadians);
		transform[0].x = points[0].x + (cos * p1x - sin * p1y) + cx; // TOP LEFT
		transform[0].y = points[0].y + (sin * p1x + cos * p1y) + cy;
		transform[1].x = points[0].x + (cos * p2x - sin * p2y) + cx; // TOP RIGHT
		transform[1].y = points[0].y + (sin * p2x + cos * p2y) + cy;
		transform[2].x = points[0].x + (cos * p3x - sin * p3y) + cx; // BOTTOM RIGHT
		transform[2].y = points[0].y + (sin * p3x + cos * p3y) + cy;
		transform[3].x = points[0].x + (cos * p4x - sin * p4y) + cx; // BOTTOM LEFT
		transform[3].y = points[0].y + (sin * p4x + cos * p4y) + cy;
		return transform;
	}
	
	public String toString()
	{
		return "("+ x + "," + y +  ")" ;
		
	}
}