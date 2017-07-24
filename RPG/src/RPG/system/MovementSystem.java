package RPG.system;

import java.util.Set;
import java.util.UUID;

import RPG.component.Components.CCollidable;
import RPG.component.Components.CWalk.WalkType;
import RPG.component.Components.CFSM;
import RPG.component.Components.CGraphics;
import RPG.component.Components.CMovable;
import RPG.component.Components.CPosition;
import RPG.component.Components.CWalk;
import RPG.entity.EntityManager;

public class MovementSystem implements SubSystem
{
	public EntityManager entitySystem;

	public MovementSystem(EntityManager em)
	{
		entitySystem = em;
	}

	public void processOneGameTick(double dt)
	{
		Set<UUID> allFSM = entitySystem.getAllEntitiesPossessingComponent(CFSM.class);
		Set<UUID> allMovable = entitySystem.getAllEntitiesPossessingComponent(CMovable.class);

		for (UUID e : allMovable) // allmovable + allFSM
		{
			CPosition pos = entitySystem.getComponent(e, CPosition.class);
			CMovable mov = entitySystem.getComponent(e, CMovable.class);
			CCollidable col = entitySystem.getComponent(e, CCollidable.class);
			
			if (col.collisionYBot == true)
			{
				mov.onGround = true;
				mov.onAir = false;
				
			}
			if ( mov.onGround == true)
			{
				mov.vel.y = 0;
				entitySystem.getComponent(e, CWalk.class).active = true;
			}
			else
			{
				mov.vel.y += (float) mov.gravity * dt;
			}
	
				pos.x += mov.vel.x;
				pos.y += mov.vel.y;
			
		
		//	System.out.println("Move dir.y " + mov.dir.y);
		}

	}

	@Override
	public String getSimpleName()
	{
		return null;
	}
}
