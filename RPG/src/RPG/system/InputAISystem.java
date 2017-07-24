package RPG.system;

import java.util.Set;
import java.util.UUID;

import RPG.component.Components.AIComponent;
import RPG.component.Components.CInput;
import RPG.component.Components.CMovable;
import RPG.entity.EntityManager;

public class InputAISystem implements SubSystem
{
	public EntityManager entitySystem;
	public int timer;
	public InputAISystem(EntityManager em)
	{
		this.entitySystem = em;
	}

	@Override
	public void processOneGameTick(double lastFrameTime)
	{
		Set<UUID> allAI = entitySystem.getAllEntitiesPossessingComponent(AIComponent.class);
		timer++;
		for (UUID e : allAI)
		{
			CInput input = entitySystem.getComponent(e, CInput.class);
			AIComponent inputAI = entitySystem.getComponent(e, AIComponent.class);
		/*	if ( timer % 240 == 0)
			{
				input.intent = CInput.InputType.LEFT;
				timer = 0;
			}
			else if ( timer % 120 == 0)
			{
				input.intent = CInput.InputType.RIGHT;
			}
			else if (timer % 60 == 0)
			{
				input.intent = CInput.InputType.NONE;
			}*/
			
			
		}
	}

	@Override
	public String getSimpleName()
	{
		return null;
	}
	
}
