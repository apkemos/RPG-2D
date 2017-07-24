package RPG.system;

import java.util.Set;
import java.util.UUID;

import org.lwjgl.glfw.GLFW;

import RPG.component.Components.CInput;
import RPG.component.Components.CInput.InputType;
import RPG.component.Components.PlayerComponent;
import RPG.entity.EntityManager;
import RPG.input.Input;

public class InputSystem implements SubSystem
{
	public EntityManager entitySystem;

	public InputSystem(EntityManager em)
	{
		this.entitySystem = em;
	}

	public void processOneGameTick(double lastFrameTime)
	{
		Set<UUID> allPlayer = entitySystem.getAllEntitiesPossessingComponent(PlayerComponent.class);

		for (UUID e : allPlayer)
		{
			// state.prevState = state.currState;
			CInput input = entitySystem.getComponent(e, CInput.class);
			if ( input.active == false ) continue;
			input.intent = InputType.NONE;
			if (Input.isKeyDown(GLFW.GLFW_KEY_RIGHT) || Input.isKeyDown(GLFW.GLFW_KEY_D))
			{
				input.intent = CInput.InputType.RIGHT;
			}
			if (Input.isKeyDown(GLFW.GLFW_KEY_LEFT) || Input.isKeyDown(GLFW.GLFW_KEY_A))
			{
				input.intent = CInput.InputType.LEFT;
			}
			if (Input.isKeyDown(GLFW.GLFW_KEY_UP) || Input.isKeyDown(GLFW.GLFW_KEY_W) || Input.isKeyDown(GLFW.GLFW_KEY_SPACE))
			{
				input.intent = CInput.InputType.UP;
			}
			if (Input.isKeyDown(GLFW.GLFW_KEY_DOWN) || Input.isKeyDown(GLFW.GLFW_KEY_S))
			{
				input.intent = CInput.InputType.DOWN;
			}
			
			
			if ( Input.qPressed)
			{
				input.intent = InputType.ATTACK;
				Input.qPressed = false;
			}
			//!Input.isKeyDown(GLFW.GLFW_KEY_RIGHT) && !Input.isKeyDown(GLFW.GLFW_KEY_LEFT) && !Input.isKeyDown(GLFW.GLFW_KEY_UP) && !Input.isKeyDown(GLFW.GLFW_KEY_DOWN)
	/*		if ( Input.upReleased )
			{
				input.intent = CInput.InputType.FALL;
			} */
		
			
			

		}
	}

	@Override
	public String getSimpleName()
	{
		return null;
	}

}
