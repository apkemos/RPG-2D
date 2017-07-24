package RPG.system;

import java.util.Set;
import java.util.UUID;

import RPG.component.Components.CAnimation;
import RPG.component.Components.CFSM;
import RPG.component.Components.CJump;
import RPG.component.Components.CMovable;
import RPG.component.Components.CWalk;
import RPG.component.Components.CWalk.WalkType;
import RPG.entity.EntityManager;

public class FSMSystem implements SubSystem
{
	public EntityManager entitySystem;

	// public static MetaEntity moveRight = new MetaEntity("MoveRight", new CWalk(WalkType.RIGHT));

	public FSMSystem(EntityManager em)
	{
		this.entitySystem = em;
	}

	public void processOneGameTick(double lastFrameTime)
	{
		Set<UUID> allFSM = entitySystem.getAllEntitiesPossessingComponent(CFSM.class);

		for (UUID e : allFSM)
		{
			CFSM fsm = entitySystem.getComponent(e, CFSM.class);
			CAnimation anim = entitySystem.getComponent(e, CAnimation.class);

			if (fsm.states.contains(CFSM.State.WALK_RIGHT))
			{
			/*	if (entitySystem.hasComponent(e, CWalk.class))
				{
					CWalk walk = entitySystem.getComponent(e, CWalk.class);
					walk.dir = 1;
					CMovable mov = entitySystem.getComponent(e, CMovable.class);
					mov.dir = 1;
					// System.out.println("Right");
				}
				else
					entitySystem.addComponent(e, new CWalk(WalkType.RIGHT));

				anim.setCurrentAnimation(1);
				anim.getCurrentAnimation().start();*/
			}
			else if (fsm.states.contains(CFSM.State.WALK_LEFT))
			{

		/*		if (entitySystem.hasComponent(e, CWalk.class))
				{
					CWalk walk = entitySystem.getComponent(e, CWalk.class);
					walk.dir = -1;
					CMovable mov = entitySystem.getComponent(e, CMovable.class);
					mov.dir = -1;
				}
				else
				{

					entitySystem.addComponent(e, new CWalk(WalkType.LEFT));
				} */

				anim.setCurrentAnimation(0);
				anim.getCurrentAnimation().start();
			}
			else if (fsm.states.contains(CFSM.State.IDLE))
			{
				/*if (entitySystem.hasComponent(e, CWalk.class))
				{
					CWalk walk = entitySystem.getComponent(e, CWalk.class);
					walk.dir = 0;
					entitySystem.removeComponent(e, walk);
				}*/
				Set<UUID> allAnim = entitySystem.getAllEntitiesPossessingComponent(CAnimation.class);

				for (UUID eAnim : allAnim)
				{
					CAnimation cAnim = entitySystem.getComponent(eAnim, CAnimation.class);
					cAnim.getCurrentAnimation().reset();
				}
				// anim.setCurrentAnimation(2);
			/*	CMovable mov = entitySystem.getComponent(e, CMovable.class);
				mov.vel.x = 0;*/
			}

			if (fsm.states.contains(CFSM.State.JUMP)  )
			{
				if (!entitySystem.hasComponent(e, CJump.class) && fsm.states.contains(CFSM.State.ON_GROUND) )
				{
			/*		CJump jump = new CJump(-40f, 9.8f, true);
					entitySystem.addComponent(e, jump);
					CMovable mov = entitySystem.getComponent(e, CMovable.class);
					mov.vel.y = jump.jumpVelocity;
					fsm.states.remove(CFSM.State.ON_GROUND); //remove acc */
				}
				else
				{
					
				}
			}

		}
	}

	@Override
	public String getSimpleName()
	{
		return null;
	}

}
