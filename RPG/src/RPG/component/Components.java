package RPG.component;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.UUID;

import RPG.graphics.Animation;
import RPG.graphics.TextureRegion;
import RPG.math.Vec2f;

public class Components
{
	
	public static class CInput implements Component
	{
		public boolean active = true;
		public enum InputType {
			NONE, RIGHT, LEFT, UP, DOWN, FALL, ATTACK
		}

		public InputType intent;

		public CInput()
		{
		}
		
		public CInput(InputType t)
		{
			this.intent = t;
		}
	}
	public static class PlayerComponent implements Component
	{
		public boolean active = true;
		public PlayerComponent()
		{

		}
	}
	
	public static class AIComponent implements Component
	{
		public boolean active = true;
		public AIComponent()
		{

		}
	}

	public static class CPosition implements Component
	{
		public boolean active = true;
		public float x, y,z;
		public float xOrigin , yOrigin;
		public float rot;
		public int width, height;
		public CPosition()
		{
			x = 0f;
			y = 0f;
			rot = 0f;
		}

		public CPosition(float x, float y, float z, int width, int height)
		{
			this.x = x;
			this.y = y;
			this.z = z;
			this.width = width;
			this.height= height;
		}
	}
	
	public static class ParentComponent implements Component
	{
		public boolean active = true;
		public UUID parentID;
		public float xOffset, yOffset;
		
		public ParentComponent(UUID e, float xOffset , float yOffset)
		{
			this.parentID = e;
			this.xOffset = xOffset;
			this.yOffset = yOffset;
		}
		
	}
	
	public static class AttackComponent implements Component
	{
		public boolean active = true;
		public UUID weaponID;
		public boolean isAttacking;
		
		public AttackComponent(UUID e)
		{
			this.weaponID = e;
			this.isAttacking = false;
		}
		
	}
	

	public static class CGraphics implements Component
	{
		public boolean active = true;
		public TextureRegion image;
		public float width, height;

		public CGraphics(TextureRegion tex, float width, float height)
		{
			this.width = width;
			this.height = height;
			image = tex;
		}

	}

	public static class CMovable implements Component
	{

		public boolean active = true;
		public Vec2f vel;
		public Vec2f targetVel;
		public float acc;
		public float airAcc;
		public float threshold;
		public Vec2f dir;
		public float gravity;
		public boolean onGround ;
		public boolean onAir = false;

		public CMovable( Vec2f v, Vec2f targetV, float acc, float airAcc, float threshold, Vec2f dir, float gravity, boolean onGround)
		{
			this.vel = v;
			this.targetVel = targetV;
			this.acc = acc;
			this.airAcc = acc;
			this.threshold = threshold;
			this.dir = dir;
			this.gravity = gravity;
			this.onGround = onGround;
			
		}

	}

	public static class CState implements Component
	{
		public boolean active = true;
		public enum State {
			IDLE, RUN_LEFT, RUN_RIGHT, JUMP, DUCK
		};

		public State prevState;
		public State currState;

		public CState(State state)
		{
			this.currState = state;
		}
	}

	public static class CAnimation implements Component
	{
		public boolean active = true;
		public Animation[] animations;

		private int currentAnimation;

		public Animation getCurrentAnimation()
		{
			return animations[currentAnimation];
		}
		
		public int getCurrentAnimationIndex()
		{
			return currentAnimation;
		}
		
		public Animation getAnimation(int index)
		{
			return animations[index];
		} 
		public void setCurrentAnimation(int currentAnimation)
		{
			this.currentAnimation = currentAnimation;
		}

		public CAnimation(Animation[] spriteSheets)
		{
			this.animations = spriteSheets;
		}
	}

	public static class CFSM implements Component
	{
		// public MetaEntity statesEntity;
		public enum State {
			WALK_RIGHT, WALK_LEFT, JUMP, IDLE, ON_GROUND

		};

		public EnumSet<State> states = EnumSet.noneOf(State.class);

		public CFSM(State... statesArg)
		{
			for (State st : statesArg)
			{
				this.states.add(st);
			}
			// this.statesEntity = e;
		}
	}

	public static class CWalk implements Component
	{
		public boolean active = true;
		public enum WalkType {
			RIGHT, LEFT
		};

		public WalkType walking;
		public int dir;
		
		public CWalk()
		{
		}
		public CWalk(WalkType walkType)
		{
			this.walking = walkType;
			if (walking == WalkType.RIGHT)
			{
				dir = 1;
			}
			else if (walking == WalkType.LEFT)
			{
				dir = -1;
			}
		}
	}

	public static class CJump implements Component
	{
		public boolean active = true;
		public float jumpVelocity;
		public float gravity;
		public boolean jumping;

		public CJump(float jumpV, float gravity, boolean jumping)
		{
			this.jumpVelocity = jumpV;
			this.gravity = gravity;
			this.jumping = jumping;
		}
		
		public boolean isJumping()
		{
			return jumping;
		}
	}

	public static class CCollidable implements Component
	{
		public boolean active = true;
		public Vec2f[] points;
		public List<UUID> collisionTargets;
		public List<UUID> hittedTargets;
		public boolean collisionXLeft = false;
		public boolean collisionXRight = false;
		public boolean collisionYBot = false;
		public boolean collisionYUp = false;
		public boolean collided = false;
		public CCollidable(int verticesNum, int width, int height, boolean active)
		{
			collisionTargets = new ArrayList<UUID>();
			hittedTargets = new ArrayList<UUID>();
			
			points = new Vec2f[verticesNum];
			if ( verticesNum == 8)
			{
				points[0] = new Vec2f( 20, 0); //TOP LEFT
				points[1] = new Vec2f( 40, 0); //TOP RIGHT
				points[2] = new Vec2f( 64, 20); //TOP MIDDLE RIGHT
				points[3] = new Vec2f( 64, 40); //BOT MIDDLE RIGHT
				points[4] = new Vec2f( 40, 64); //BOT RIGHT
				points[5] = new Vec2f( 20, 64); //BOT LEFT
				points[6] = new Vec2f( 0, 60); //BOT MIDDLE LEFT
				points[7] = new Vec2f( 0, 30); //TOP MIDDLE LEFT
			}
			else if (verticesNum == 4)
			{
				points[0] =  new Vec2f( 0, 0); //TOP LEFT
				points[1] =  new Vec2f( width, 0); //TOP RIGHT
				points[2] =  new Vec2f( width, height); //BOT RIGHT
				points[3] =  new Vec2f( 0, height); //BOT LEFT
			}
			this.active = active;
		}

	}
	
	public static class CCameraFocus implements Component
	{
		public boolean active = true;
		public int x,y;
		public CCameraFocus(int x, int y)
		{
			this.x = x;
			this.y = y;
		}
		
	}
	
	public static class HealthComponent implements Component
	{
		public float health;
		public HealthComponent(float health)
		{
			this.health = health;
		}
		
	}
	
	public static class StatsComponent implements Component{
		public float atkDamage;
		
		public StatsComponent( float atkDamage)
		{
			this.atkDamage = atkDamage;
		}
	}
	
	
}
