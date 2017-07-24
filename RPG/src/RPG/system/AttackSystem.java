package RPG.system;

import java.util.Set;
import java.util.UUID;

import RPG.component.Components.AttackComponent;
import RPG.component.Components.CAnimation;
import RPG.component.Components.CCollidable;
import RPG.component.Components.CGraphics;
import RPG.component.Components.CInput;
import RPG.component.Components.CMovable;
import RPG.component.Components.CPosition;
import RPG.component.Components.ParentComponent;
import RPG.component.Components.CInput.InputType;
import RPG.component.Components.CWalk;
import RPG.entity.EntityManager;
import RPG.math.Vec2f;

public class AttackSystem implements SubSystem
{

	public EntityManager entitySystem;
	public int temp;
	public AttackSystem(EntityManager em)
	{
		this.entitySystem = em;
	}

	@Override
	public void processOneGameTick(double dt)
	{
		Set<UUID> allAttacking = entitySystem.getAllEntitiesPossessingComponent(AttackComponent.class);
		for (UUID e : allAttacking)
		{
			CInput input = entitySystem.getComponent(e, CInput.class);
			AttackComponent attack = entitySystem.getComponent(e, AttackComponent.class);
			UUID weaponEntity = attack.weaponID;
			CMovable mov = entitySystem.getComponent(e, CMovable.class);
			
			CPosition weapPos = entitySystem.getComponent(weaponEntity, CPosition.class);
			CWalk walk = entitySystem.getComponent(e, CWalk.class);
			if ( input.intent == InputType.ATTACK && attack.isAttacking == false)
			{	
				attack.isAttacking = true;
				CGraphics weaponGraphics = entitySystem.getComponent(weaponEntity, CGraphics.class);
				entitySystem.getComponent(weaponEntity, CCollidable.class).active = true;
				CAnimation anim = entitySystem.getComponent(e, CAnimation.class);
			/*	if (mov.dir.x == 1)
					anim.setCurrentAnimation(6);
				else if (mov.dir.x == -1)
					anim.setCurrentAnimation(7); */
				anim.getCurrentAnimation().start();
			//	entitySystem.removeComponent(e, entitySystem.getComponent(e, CWalk.class));
	//			walk.active = false;
				
				
			}
			if ( attack.isAttacking == true)
			{
				weapPos.rot += (mov.dir.x)* 50* 60 * (float)dt;
			//	weapPos.rot = 0;
				weapPos.xOrigin = 0;
				weapPos.yOrigin = 64;
				temp ++;
				CCollidable weapCollidable = entitySystem.getComponent(weaponEntity, CCollidable.class);
		//		System.out.println(weapCollidable.collided);
				if (temp == 4)
				{
					input.active = true;
		//			input.intent = InputType.NONE;
					System.out.println("attack released");
					attack.isAttacking = false;
					temp =0 ;
					weapPos.rot = 0;
					entitySystem.getComponent(weaponEntity, CCollidable.class).collided = false;
					entitySystem.getComponent(weaponEntity, CCollidable.class).active = false;
				//	entitySystem.addComponent(e, new CWalk());
	//				walk.active = true;
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
