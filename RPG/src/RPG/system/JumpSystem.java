package RPG.system;

import java.util.Set;
import java.util.UUID;

import RPG.component.Components.CAnimation;
import RPG.component.Components.CInput;
import RPG.component.Components.CJump;
import RPG.component.Components.CMovable;
import RPG.component.Components.CWalk;
import RPG.entity.EntityManager;
import RPG.input.Input;

public class JumpSystem implements SubSystem
{
	public EntityManager entitySystem;

	public JumpSystem(EntityManager em)
	{
		this.entitySystem = em;
	}

	@Override
	public void processOneGameTick(double dt)
	{

		Set<UUID> allJump = entitySystem.getAllEntitiesPossessingComponent(CJump.class);

		for (UUID e : allJump)
		{
			CJump jump = entitySystem.getComponent(e, CJump.class);
			jump.jumpVelocity = 700f;
			CMovable mov = entitySystem.getComponent(e, CMovable.class);
			mov.gravity = 20;
			mov.airAcc = 5f;

			CInput input = entitySystem.getComponent(e, CInput.class);
			if (input.intent == CInput.InputType.UP && mov.onGround == true)
			{
				entitySystem.getComponent(e, CWalk.class).active = false;
		//		entitySystem.removeComponent(e, walk);
				mov.onGround = false;
				mov.dir.y = 1;
				mov.vel.y = (float) (-jump.jumpVelocity * dt);
				jump.jumping = true;
			}
			if ( mov.onGround == false)
			{
				CAnimation anim = entitySystem.getComponent(e, CAnimation.class);
				if (mov.dir.x == 1)
					anim.setCurrentAnimation(4);
				else if (mov.dir.x == -1)
					anim.setCurrentAnimation(5);
				anim.getCurrentAnimation().start();
				
				if (input.intent == CInput.InputType.RIGHT)
				{
					if (mov.vel.x < 0)
					{
						float reactivityPercent = 5f;
						mov.vel.x += (reactivityPercent * mov.targetVel.x) * dt;
						System.out.println("Left to right");
					}
					mov.vel.x += mov.airAcc * (mov.targetVel.x - mov.vel.x) * dt;
					mov.dir.x = 1;
					if (mov.vel.x > mov.targetVel.x)
					{
						System.out.println("Full speed");
						mov.vel.x = mov.targetVel.x;
					}
				}
				else if (input.intent == CInput.InputType.LEFT)
				{
					mov.vel.x += -(mov.airAcc * (mov.targetVel.x + mov.vel.x) * dt);
					mov.dir.x = -1;
					if (mov.vel.x < -mov.targetVel.x)
					{
						mov.vel.x = -mov.targetVel.x;
					}
				}
			}
			if (mov.onGround == false && mov.onAir == false)
			{
				if (mov.vel.y > 0 )
				{
					mov.onAir = true;
		//			System.out.println("On air cause jump ended");
				}
				if (Input.upReleased == true)
				{
		//			System.out.println("On air with up released");
					mov.vel.y = 0;
					mov.onAir = true;
				}
				
			}
			
			
			if (mov.onAir == true)
			{
		//		System.out.println("On air");
			}
			if (input.intent == CInput.InputType.DOWN)
			{
				mov.dir.y = -1;
				mov.vel.y += 2;
			}
			else if (input.intent == CInput.InputType.NONE)
			{
			}

		}
	}

	@Override
	public String getSimpleName()
	{
		return null;
	}

}
