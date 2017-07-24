package RPG.system;

import java.util.Set;
import java.util.UUID;

import RPG.Main;
import RPG.component.Components.CGraphics;
import RPG.component.Components.CPosition;
import RPG.entity.EntityManager;
import RPG.graphics.Camera;
import RPG.graphics.SpriteBatcher;
import RPG.math.Matrix4f;
import RPG.math.Vec3f;


public class RenderingSystem implements SubSystem
{
	protected EntityManager entitySystem;
	
	public RenderingSystem( EntityManager em)
	{
		entitySystem = em;
	}
	public void processOneGameTick(double lastFrameTime)
	{
		Set<UUID> allGraphics = entitySystem.getAllEntitiesPossessingComponent(CGraphics.class);
		
		Matrix4f view = Matrix4f.identity();
		view = Matrix4f.multiply(Matrix4f.translate(new Vec3f(Camera.cam.x, Camera.cam.y, 0f)), view);
		SpriteBatcher.batcher.setViewMatrix(view);
		SpriteBatcher.batcher.updateUniforms();
		SpriteBatcher.batcher.begin();
		for( UUID e : allGraphics)
		{
			CGraphics graphic = entitySystem.getComponent(e, CGraphics.class);
			if (graphic.active == false) continue;
			CPosition pos = entitySystem.getComponent(e, CPosition.class);
		//	System.out.println("Pos.x "+ pos.x + " Pos.y " + pos.y);
			SpriteBatcher.batcher.draw(graphic.image, pos.x, pos.y, pos.z, graphic.width, graphic.height, pos.xOrigin, pos.yOrigin, pos.rot);
		}
		
		SpriteBatcher.batcher.end();
		
	}

	@Override
	public String getSimpleName()
	{
		return "Rendering System";
	}
	
	
}
