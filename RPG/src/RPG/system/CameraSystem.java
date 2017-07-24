package RPG.system;

import java.util.Set;
import java.util.UUID;

import RPG.Main;
import RPG.component.Components.CCameraFocus;
import RPG.component.Components.CPosition;
import RPG.entity.EntityManager;
import RPG.graphics.Camera;
import RPG.graphics.SpriteBatcher;
import RPG.math.Matrix4f;
import RPG.math.Vec3f;
import RPG.world.TileMap;
import RPG.world.World;

public class CameraSystem implements SubSystem
{
	public EntityManager entitySystem;

	public CameraSystem(EntityManager em)
	{
		this.entitySystem = em;
	}

	public void processOneGameTick(double lastFrameTime)
	{

		Set<UUID> allCamera = entitySystem.getAllEntitiesPossessingComponent(CCameraFocus.class);
		for (UUID e : allCamera)
		{
			CPosition pos = entitySystem.getComponent(e, CPosition.class);
			CCameraFocus camFocus = entitySystem.getComponent(e, CCameraFocus.class);
			Matrix4f view = SpriteBatcher.batcher.getViewMatrix();
			camFocus.x = Main.getWidth()/2 ; // debugging
			camFocus.y = Main.getHeight()/2 + Main.getHeight()/3;
		//	camFocus.y = 0;
			if (pos.x < Main.getWidth() / 2 )
			{
				camFocus.x  = 0; 
				camFocus.y  -= (int) pos.y;
//				continue;
			}
			else if ( pos.x > (World.level.width - Main.getWidth() / 2))
			{
				camFocus.x  = -World.level.width + Main.getWidth() ; 
				camFocus.y  -= (int) pos.y;
			}
			else
			{
				camFocus.x -= (int) pos.x;
				camFocus.y -= (int) pos.y;
			}
			// view.showElements();
			Camera.cam.x = camFocus.x;
			Camera.cam.y = camFocus.y;
			
	//		view = Matrix4f.multiply(Matrix4f.translate(new Vec3f(camFocus.x, camFocus.y, 0f)), view); // Make resource manager for camera and use object instead of static
	//		SpriteBatcher.batcher.setViewMatrix(view);
	//		SpriteBatcher.batcher.updateUniforms();
		}

	}

	@Override
	public String getSimpleName()
	{
		return null;
	}

}
