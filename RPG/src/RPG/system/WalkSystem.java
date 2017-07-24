package RPG.system;

import java.util.Set;
import java.util.UUID;

import RPG.component.Components.CAnimation;
import RPG.component.Components.CInput;
import RPG.component.Components.CMovable;
import RPG.component.Components.CWalk;
import RPG.entity.EntityManager;
import RPG.math.Vec2f;

public class WalkSystem implements SubSystem
{
	public EntityManager entitySystem;

	public WalkSystem(EntityManager em)
	{
		this.entitySystem = em;
	}

	@Override
	public void processOneGameTick(double dt)
	{
		Set<UUID> allWalking = entitySystem.getAllEntitiesPossessingComponent(CWalk.class);
		for (UUID e : allWalking) // allmovable + allFSM
		{
			CWalk walk = entitySystem.getComponent(e, CWalk.class);
			if ( !walk.active) continue;
			CMovable mov = entitySystem.getComponent(e, CMovable.class);
			mov.targetVel = new Vec2f(7, 7);
			mov.acc = 10f;
//			mov.gravity = 0f;
			CInput input = entitySystem.getComponent(e, CInput.class);
			CAnimation anim = entitySystem.getComponent(e, CAnimation.class);
			if (input.intent == CInput.InputType.RIGHT)
			{
				if (mov.vel.x < 0)
				{
					float reactivityPercent = 5f;
					mov.vel.x += (reactivityPercent * mov.targetVel.x) * dt;
				}
				mov.vel.x += mov.acc * (mov.targetVel.x - mov.vel.x) * dt;
				mov.dir.x = 1;

				if (mov.vel.x > mov.targetVel.x)
				{
					System.out.println("Full speed");
					mov.vel.x = mov.targetVel.x;
				}
				anim.setCurrentAnimation(0);
				anim.getCurrentAnimation().start();
			}
			else if (input.intent == CInput.InputType.LEFT)
			{

				if (mov.vel.x > 0)
				{
					float reactivityPercent = 5f;
					mov.vel.x += -(reactivityPercent * mov.targetVel.x) * dt;
				}
				// mov.vel.x += -(mov.acc * mov.targetVel.x + (1 - mov.acc) * Math.abs(mov.vel.x))*dt ;
				mov.vel.x += -(mov.acc * (mov.targetVel.x + mov.vel.x)) * dt;
				mov.dir.x = -1;

				if (mov.vel.x < -mov.targetVel.x)
				{
					mov.vel.x = -mov.targetVel.x;
				}
				anim.setCurrentAnimation(1);
				anim.getCurrentAnimation().start();
			}

			else if (input.intent == CInput.InputType.NONE)
			{
				if (mov.dir.x == 1)
					anim.setCurrentAnimation(2);
				else if (mov.dir.x == -1)
					anim.setCurrentAnimation(3);
				
				anim.getCurrentAnimation().start();
				mov.vel.x = 0;

			}

			// }
		}	
	}

	@Override
	public String getSimpleName()
	{
		return null;
	}

}
