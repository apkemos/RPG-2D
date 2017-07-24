package RPG.system;

import java.util.Set;
import java.util.UUID;

import RPG.component.Components.CAnimation;
import RPG.component.Components.CGraphics;
import RPG.component.Components.CInput;
import RPG.component.Components.CMovable;
import RPG.component.Components.CState;
import RPG.component.Components.CInput.InputType;
import RPG.component.Components.CState.State;
import RPG.entity.EntityManager;
import RPG.graphics.Animation;
import RPG.graphics.SpriteCoordinates;
import RPG.input.Input;

public class AnimateSystem implements SubSystem
{

	public EntityManager entitySystem;

	public AnimateSystem(EntityManager em)
	{
		entitySystem = em;
	}

	@Override
	public void processOneGameTick(double lastFrameTime)
	{

		Set<UUID> allAnim = entitySystem.getAllEntitiesPossessingComponent(CAnimation.class);

		for (UUID e : allAnim)
		{
			CGraphics sprite = entitySystem.getComponent(e, CGraphics.class);
			CAnimation animationComponent = entitySystem.getComponent(e, CAnimation.class);
			CMovable mov = entitySystem.getComponent(e, CMovable.class);
			
	//		 System.out.println("Anim index" + animationComponent.getCurrentAnimationIndex());
			for (int i = 0; i < animationComponent.animations.length; i++)
			{
				if (i != animationComponent.getCurrentAnimationIndex())
				{
					Animation otherAnim = animationComponent.getAnimation(i);
					otherAnim.reset();
				}
			}
		//	 System.out.println("Frame " + animationComponent.getCurrentAnimation().currentFrame);
			Animation currAnimation = animationComponent.getCurrentAnimation();
			currAnimation.update();
			sprite.image = currAnimation.getSprite(); // Can use optimization, change sprite with sprites cords
		//	sprite.active = true;
		}
	}

	@Override
	public String getSimpleName()
	{
		return null;
	}

}
