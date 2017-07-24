package RPG.system;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import RPG.component.Components.AttackComponent;
import RPG.component.Components.CCollidable;
import RPG.component.Components.CPosition;
import RPG.component.Components.HealthComponent;
import RPG.component.Components.ParentComponent;
import RPG.component.Components.StatsComponent;
import RPG.entity.EntityManager;

public class ParentSystem implements SubSystem
{
	public EntityManager entitySystem;

	public ParentSystem(EntityManager em)
	{
		entitySystem = em;
	}

	@Override
	public void processOneGameTick(double dt)
	{
		Set<UUID> allParent = entitySystem.getAllEntitiesPossessingComponent(ParentComponent.class);
		Set<UUID> allCollidable = entitySystem.getAllEntitiesPossessingComponent(CCollidable.class);
		
		for (UUID weap : allParent) // allmovable + allFSM
		{
			CPosition childPos = entitySystem.getComponent(weap, CPosition.class);
			CCollidable weapCollidable = entitySystem.getComponent(weap, CCollidable.class);
			ParentComponent weapon = entitySystem.getComponent(weap, ParentComponent.class);
			AttackComponent attackComp = entitySystem.getComponent(weapon.parentID, AttackComponent.class);
			CPosition parentPos = entitySystem.getComponent(weapon.parentID, CPosition.class);
		//	CMovable parentMov = entitySystem.getComponent(parent.parentID, CMovable.class);
			
			childPos.x = parentPos.x + weapon.xOffset ;
			childPos.y = parentPos.y + weapon.yOffset;
			
			if ( attackComp.isAttacking == true) //Change to colliding==true ?
			{
			//	System.out.println("Collision!");
				StatsComponent weapStats = entitySystem.getComponent(weap, StatsComponent.class);
				for ( UUID target: weapCollidable.collisionTargets)
				{
					if ( weapCollidable.hittedTargets.contains(target)) 
					{	
					continue; }
					else
						weapCollidable.hittedTargets.add(target);
					HealthComponent healthComp = entitySystem.getComponent(target, HealthComponent.class);
					healthComp.health -= weapStats.atkDamage;
				}
			}
			else
			{
				for(int i=0; i< weapCollidable.hittedTargets.size(); i++)
				{
					weapCollidable.hittedTargets.remove(i);
				}
				for(int i=0; i< weapCollidable.collisionTargets.size(); i++)
				{
					weapCollidable.collisionTargets.remove(i);
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
