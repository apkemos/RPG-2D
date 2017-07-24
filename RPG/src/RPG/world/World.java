package RPG.world;

import java.util.LinkedList;
import java.util.List;

import RPG.Main;
import RPG.component.Components.AIComponent;
import RPG.component.Components.AttackComponent;
import RPG.component.Components.CAnimation;
import RPG.component.Components.CCameraFocus;
import RPG.component.Components.CCollidable;
import RPG.component.Components.CGraphics;
import RPG.component.Components.CInput;
import RPG.component.Components.CJump;
import RPG.component.Components.CMovable;
import RPG.component.Components.CPosition;
import RPG.component.Components.CWalk;
import RPG.component.Components.HealthComponent;
import RPG.component.Components.ParentComponent;
import RPG.component.Components.PlayerComponent;
import RPG.component.Components.StatsComponent;
import RPG.entity.EntityManager;
import RPG.entity.MetaEntity;
import RPG.graphics.Animation;
import RPG.graphics.SpriteBatcher;
import RPG.graphics.Texture;
import RPG.graphics.TextureRegion;
import RPG.math.Vec2f;
import RPG.system.AnimateSystem;
import RPG.system.AttackSystem;
import RPG.system.CameraSystem;
import RPG.system.CollisionSystem;
import RPG.system.FSMSystem;
import RPG.system.HealthSystem;
import RPG.system.InputAISystem;
import RPG.system.InputSystem;
import RPG.system.JumpSystem;
import RPG.system.MovementSystem;
import RPG.system.ParentSystem;
import RPG.system.RenderingSystem;
import RPG.system.WalkSystem;

public class World
{
	public EntityManager entityManager;
	public SpriteBatcher batcher;
	public InputSystem inputSys;
	public ParentSystem parentSys;
	private JumpSystem jumpSys;
	private WalkSystem walkSys;
	public CollisionSystem collisionSys;
	public MovementSystem moveSys;
	public AnimateSystem animateSys;
	public CameraSystem cameraSys;
	public RenderingSystem renderSys;
	public FSMSystem fsmSys;
	private InputAISystem inputAISys;
	private AttackSystem attackSys;
	public List<TileMap> tileMaps;
	private HealthSystem healthSys;
	public static Level level;
	public World()
	{
		entityManager = new EntityManager(); 
		MetaEntity.defaultEntityManager = entityManager;
	//	MetaEntity level = new MetaEntity("Level", new CGraphics(new Texture("res/background.png"), 800, 600, 0), new CPosition(0f, 0f));
	//	glActiveTexture(GL_TEXTURE1);
	//	assert(Constants.NORMAL_TEXTURE + GL_TEXTURE0 == GL_TEXTURE1);
		TextureRegion tileSheet = new TextureRegion(new Texture("res/tileSet.png"));
		TextureRegion background = new TextureRegion( new Texture("res/background.jpg"));
	//	Main.spriteShader.setUniform1i("ourTexture1", 1);
		
		createHero();
		createAI();
		
		
		tileMaps = new LinkedList<TileMap>();
		tileMaps.add( new TileMap(background, 2*1280, 720, 1, 1f, 0.0f));
		tileMaps.get(0).loadMap();
		tileMaps.add(new TileMap(tileSheet, 2*1280, 720, 0, 1f, 0.01f)) ;
		tileMaps.get(1).loadTileMap();
		level = new Level(2*1280, 720, tileMaps);
		
		//tileMaps.add(new MapLayer(0, "Background", background, 1.0f ));
		
	//	State.initStates();
		
		
		inputSys = new InputSystem(entityManager);
		inputAISys =  new InputAISystem(entityManager);
		attackSys = new AttackSystem(entityManager);
		parentSys = new ParentSystem(entityManager);
		jumpSys = new JumpSystem(entityManager);
		walkSys = new WalkSystem(entityManager);
		collisionSys = new CollisionSystem(entityManager);
		moveSys = new MovementSystem(entityManager);
		animateSys = new AnimateSystem(entityManager);
		cameraSys = new CameraSystem(entityManager);
		renderSys = new RenderingSystem(entityManager);
		fsmSys = new FSMSystem(entityManager);
		healthSys = new HealthSystem(entityManager);
		
		
	}
		
	
	private void createHero()
	{
		TextureRegion heroSprite = new TextureRegion(new Texture("res/idle.png"), 0, 0, 64 , 64);
		MetaEntity hero = new MetaEntity("Hero", new CInput(CInput.InputType.NONE), new CGraphics(heroSprite ,64, 64), new CPosition(4*TileMap.TILE_SIZE, 8*TileMap.TILE_SIZE, 0.5f, 64 , 64 ) ) ;
		hero.add( new PlayerComponent());
		hero.add( new CMovable(new Vec2f(0,0), new Vec2f(30,30), 0.5f, 0.2f,  0.1f , new Vec2f(1,0), 30f , false) );
		hero.add (new CWalk(CWalk.WalkType.RIGHT));
		hero.add ( new CJump (1000f, 20f, false));
		Animation[] spriteSheet = loadFrames();
		hero.add( new CAnimation(spriteSheet) );
		hero.add ( new CCollidable(8 , 64, 64, true));
		hero.add( new CCameraFocus(Main.getWidth()/2, Main.getHeight()/2));
		
		TextureRegion weaponSprite = new TextureRegion(new Texture("res/blue.jpg"), 0, 0, 64 , 64);
		MetaEntity weapon = new MetaEntity("Weapon");
		weapon.add(new CGraphics(weaponSprite, 15, 64));
		weapon.add( new CPosition(0, 0, 0.7f, 15, 64));
		weapon.add ( new ParentComponent( hero.entity, 32 , -32));
		weapon.add ( new CCollidable(4, 15, 64, false));
		weapon.add ( new StatsComponent(10.0f));
		entityManager.setEntityName(weapon.entity, "Weapon");
		hero.add(new AttackComponent(weapon.entity));
		entityManager.setEntityName(hero.entity, "Hero");
	}


	private void createAI()
	{
		TextureRegion enemyAISprite = new TextureRegion(new Texture("res/red.jpg"), 0, 0, 64 , 64);
		MetaEntity enemyAI = new MetaEntity("AI");
		enemyAI.add( new AIComponent());
		enemyAI.add (new CInput( CInput.InputType.NONE));
		enemyAI.add(new CGraphics(enemyAISprite ,64, 64));
		enemyAI.add( new CPosition(7*TileMap.TILE_SIZE, 9*TileMap.TILE_SIZE, 0.4f, 64, 64 ) );
		enemyAI.add( new HealthComponent(10f));
	//	enemyAI.add ( new CMovable( new Vec2f(0,0), new Vec2f(10,10),0.5f, 0.2f, 0.1f, new Vec2f(-1,0), 10f	, false));
		/*enemyAI.add (new CWalk(CWalk.WalkType.RIGHT));
		enemyAI.add ( new CJump (1000f, 20f, false) );
		Animation[] spriteSheet = loadFrames();*/
	//	enemyAI.add( new CAnimation(spriteSheet) );
		enemyAI.add ( new CCollidable(4 , 64, 64, true));
		entityManager.setEntityName(enemyAI.entity, "AI");
	}


	private Animation[] loadFrames()
	{
		TextureRegion framesRunLeft[] = new TextureRegion[10];
		TextureRegion framesRunRight[] = new TextureRegion[10];
		TextureRegion framesIdleLeft[] = new TextureRegion[10];
		TextureRegion framesIdleRight[] = new TextureRegion[10];
		TextureRegion framesJumpRight[] = new TextureRegion[10];
		TextureRegion framesJumpLeft[] = new TextureRegion[10];
	//	TextureRegion framesAttackRight[] = new TextureRegion[7];
	//	TextureRegion framesAttackLeft[] = new TextureRegion[7];
		
		Texture texRun = new Texture("res/run.png");
		Texture texIdle = new Texture("res/idle.png");
		Texture texJump = new Texture("res/Jump.png");
		Texture texAttack = new Texture("res/Attack.png");
		
		for (int i=0; i<5; i++)
		{
			framesRunRight[i] = new TextureRegion(texRun, i*367 , 0 , 345, 495);
			framesRunLeft[i] = new TextureRegion(texRun, i*367 , 0 , 345, 495);
			framesRunLeft[i].flip(true, false);
			framesIdleRight[i] = new TextureRegion(texIdle, i*312, 0 , 320, 510);
			framesIdleLeft[i] = new TextureRegion(texIdle, i*312, 0 , 320, 510);
			framesIdleLeft[i].flip(true, false);
			framesJumpRight[i] = new TextureRegion(texJump, i*320, 0 , 320, 510);
			framesJumpLeft[i] = new TextureRegion(texJump, i*320, 0 , 320, 510);
			framesJumpLeft[i].flip(true, false);
			
		}
		
/*		for (int i=0; i<3; i++)
		{
				framesAttackRight[i] = new TextureRegion(texAttack, i*680, 0 , 550, 500);
				framesAttackLeft[i] = new TextureRegion(texAttack, i*680, 0 , 550, 500);
				framesAttackLeft[i].flip(true, false);
		}
		for (int i=0; i<4; i++)
		{
				framesAttackRight[i+3] = new TextureRegion(texAttack, i*512, 600 , 512, 500);
				framesAttackLeft[i+3] = new TextureRegion(texAttack, i*512, 600 , 512, 500);
				framesAttackLeft[i+3].flip(true, false);
		}
		for (int i=0; i<3; i++)
		{
				framesAttackRight[i+7] = new TextureRegion(texAttack, i*680, 2*600, 550, 500);
				framesAttackLeft[i+7] = new TextureRegion(texAttack, i*680, 2*600 , 550, 500);
				framesAttackLeft[i+7].flip(true, false);
		}*/
			
			
		for (int i=0; i<5; i++)
		{
			framesRunRight[i+5] = new TextureRegion(texRun, i*367 , 516 , 345, 495);
			framesRunLeft[i+5] = new TextureRegion(texRun, i*367 , 516 , 345, 495);
			framesRunLeft[i+5].flip(true, false);
			framesIdleRight[i+5] = new TextureRegion(texIdle, i*312, 510 , 320, 510);
			framesIdleLeft[i+5] = new TextureRegion(texIdle, i*312, 510 , 320, 510);
			framesIdleLeft[i+5].flip(true, false);
			
			framesJumpRight[i+5] = new TextureRegion(texJump, i*320, 516 , 320, 510);
			framesJumpLeft[i+5] = new TextureRegion(texJump, i*320, 516 , 320, 510);
			framesJumpLeft[i+5].flip(true, false);
		}
		
		
		Animation[] spriteSheet = new Animation[6];
		spriteSheet[0] = new Animation( framesRunRight , 5);
		spriteSheet[1] = new Animation( framesRunLeft, 5);
		spriteSheet[2] = new Animation( framesIdleRight, 5);
		spriteSheet[3] = new Animation( framesIdleLeft, 5);
		spriteSheet[4] = new Animation( framesJumpRight, 5);
		spriteSheet[5] = new Animation( framesJumpLeft, 5);
	//	spriteSheet[6] = new Animation( framesAttackRight, 5);
	//	spriteSheet[7] = new Animation( framesAttackLeft, 5);
		
		return spriteSheet;
	}

	public void update(double delta)
	{

		inputSys.processOneGameTick(1);
		inputAISys.processOneGameTick(1);
		attackSys.processOneGameTick(delta);
		walkSys.processOneGameTick(delta);
		jumpSys.processOneGameTick(delta);
		collisionSys.processOneGameTick(1);
		moveSys.processOneGameTick(delta);
		parentSys.processOneGameTick(1);
		healthSys.processOneGameTick(1);
		cameraSys.processOneGameTick(1);
		animateSys.processOneGameTick(1);
	//	fsmSys.processOneGameTick(1);
	}
	
	public void render()
	{
		tileMaps.get(0).factor = 0.2f;
		for (TileMap tileMap : tileMaps )
		{
			tileMap.updateViewMatrix();
			tileMap.drawMap();
		}
		renderSys.processOneGameTick(1);
	
		
	}

}
