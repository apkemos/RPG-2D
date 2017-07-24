package RPG.system;

import java.util.Set;
import java.util.UUID;

import RPG.component.Components.CGraphics;
import RPG.component.Components.HealthComponent;
import RPG.entity.EntityManager;
import RPG.graphics.TextureRegion;

public class HealthSystem implements SubSystem
{
	public EntityManager entitySystem;
	public TextureRegion deadTex = new TextureRegion("res/blue.jpg");
	public TextureRegion aliveTex = new TextureRegion("res/red.jpg");
	public int timer = 0;
	public HealthSystem(EntityManager em)
	{
		this.entitySystem = em;
	}

	@Override
	public void processOneGameTick(double lastFrameTime)
	{
		Set<UUID> allHealth = entitySystem.getAllEntitiesPossessingComponent(HealthComponent.class);
		for ( UUID e: allHealth)
		{
			HealthComponent healthComp = entitySystem.getComponent(e, HealthComponent.class);
			if ( healthComp.health < 0f)
			{
				CGraphics graphics = entitySystem.getComponent(e, CGraphics.class);
				graphics.image = deadTex;
				timer++;
			}
			if ( timer >= 120)
			{
				CGraphics graphics = entitySystem.getComponent(e, CGraphics.class);
				graphics.image = aliveTex;
				healthComp.health = 20f;
				timer = 0;
			}
		}
	}

	@Override
	public String getSimpleName()
	{
		return null;
	}
	
}
