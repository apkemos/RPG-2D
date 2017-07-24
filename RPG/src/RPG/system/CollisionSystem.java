package RPG.system;

import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import RPG.component.Components.AIComponent;
import RPG.component.Components.CCollidable;
import RPG.component.Components.CMovable;
import RPG.component.Components.CPosition;
import RPG.component.Components.ParentComponent;
import RPG.component.Components.PlayerComponent;
import RPG.entity.EntityManager;
import RPG.graphics.TextureRegion;
import RPG.math.Vec2f;
import RPG.world.Tile;
import RPG.world.TileMap;
import RPG.world.World;

public class CollisionSystem implements SubSystem
{

	public EntityManager entitySystem;
	public TextureRegion temp = new TextureRegion("res/bird.png");

	public CollisionSystem(EntityManager em)
	{
		this.entitySystem = em;
	}

	@Override
	public void processOneGameTick(double lastFrameTime)
	{
		
		Set<UUID> allCollision = entitySystem.getAllEntitiesPossessingComponent(CCollidable.class);
		Set<UUID> allMovable = entitySystem.getAllEntitiesPossessingComponent(CMovable.class);
		Set<UUID> allPlayer = entitySystem.getAllEntitiesPossessingComponent(PlayerComponent.class);
		Set<UUID> allMapCollision = EntityManager.getIntersection(allCollision, allPlayer);
		for (UUID e : allMapCollision) // Map collision
		{  
			CCollidable collidable = entitySystem.getComponent(e, CCollidable.class);
			if ( collidable.active == false) continue;
			CMovable movable = entitySystem.getComponent(e, CMovable.class);
			CPosition pos = entitySystem.getComponent(e, CPosition.class);
			resolveMapCollision(e, collidable, movable, pos);
			// System.out.println("Tile coord" + "( " + TileMap.tiles.tileSet[(int)pos.y/64][(int)pos.x/64].x + " , " + TileMap.tiles.tileSet[(int)pos.y/64][(int)pos.x/64].y + " )" );
		}

		Set<UUID> allCollidable = entitySystem.getAllEntitiesPossessingComponent(CCollidable.class);
		Set<UUID> allAI = entitySystem.getAllEntitiesPossessingComponent(AIComponent.class);
		Set<UUID> allParent = entitySystem.getAllEntitiesPossessingComponent(ParentComponent.class);
		Set<UUID> allWeapons = EntityManager.getIntersection(allCollidable, allParent);
		Set<UUID> allNonWeapons = EntityManager.getIntersection(allCollidable, allAI);
		// allParent.retainAll(allCollision);
		int collNum = 0;
		for (UUID weapEntity : allWeapons) // Weapon-entity collision
		{
			CCollidable weapCollidable = entitySystem.getComponent(weapEntity, CCollidable.class);
			if ( weapCollidable.active == false) continue;
			CPosition weapPos = entitySystem.getComponent(weapEntity, CPosition.class);
			if (weapCollidable.active == false)
				continue;
			Vec2f axes1[] = new Vec2f[weapCollidable.points.length];
		//	System.out.println("Weap pos " + weapPos.x + " " + weapPos.y);
			Vec2f[] transformedPoints = Vec2f.rotate(weapCollidable.points, weapPos);
		/*	System.out.println("Transformed Points");
			for (int i =0; i<transformedPoints.length; i+=2)
			{
				System.out.print("" + transformedPoints[i].toString());
				System.out.println("    " + transformedPoints[i+1].toString());
			}*/
			for (int i = 0; i < weapCollidable.points.length; i++)
			{
				int idx = (i + 1 == transformedPoints.length) ? 0 : i + 1;
				Vec2f vert1 = new Vec2f( (transformedPoints[i].x + weapPos.x), (transformedPoints[i].y + weapPos.y));
				Vec2f vert2 = new Vec2f( (transformedPoints[idx].x + weapPos.x), (transformedPoints[idx].y + weapPos.y));
				Vec2f edge = vert2.sub(vert1);
				Vec2f perp = edge.perp();
				axes1[i] = perp.normalize();
			//	System.out.println("Axis[" + i + "]" + " : " + "( " + axes1[i].x + " , " + axes1[i].y + " )");
			}
			for (UUID e : allNonWeapons)
			{
				CCollidable entityCollidable = entitySystem.getComponent(e, CCollidable.class);
				CPosition entityPos = entitySystem.getComponent(e, CPosition.class);
				Vec2f axes2[] = new Vec2f[entityCollidable.points.length];
				collNum++;
				for (int i = 0; i < entityCollidable.points.length; i++)
				{
					int idx = (i + 1 == entityCollidable.points.length) ? 0 : i + 1;
					Vec2f vert1 = new Vec2f( (entityCollidable.points[i].x + entityPos.x), (entityCollidable.points[i].y + entityPos.y));
					Vec2f vert2 = new Vec2f( (entityCollidable.points[idx].x + entityPos.x), (entityCollidable.points[idx].y + entityPos.y));
					Vec2f edge = vert2.sub(vert1);
					Vec2f perp = edge.perp();
					axes2[i] = perp.normalize();
				}
				for (int i = 0; i < axes1.length; i++)
				{
					Vec2f proj1 = projection(transformedPoints, weapPos, axes1[i]);
					Vec2f proj2 = projection(entityCollidable.points, entityPos, axes1[i]);
					System.out.println("Axis1[" + i + "]" + " : " + "( " + axes1[i].x + " , " + axes1[i].y + " )");
					System.out.println("weapPos " + "( " + weapPos.x + " , " + weapPos.y + " )");
					System.out.println("Proj1.min " + proj1.x+ " Proj1.max" + proj1.y);
					System.out.println("Proj2.min " + proj2.x+ " Proj2.max" + proj2.y );
					if ( !overlap(proj1, proj2))
					{// min2-max1
						weapCollidable.collided = false;
						return;
					}
					
			//			System.out.println("Collision!");
				//	System.out.println("Axis[" + i + "]" + " : " + "( " + axes1[i].x + " , " + axes2[i].y + " )");
				}
				for (int i = 0; i < axes2.length; i++)
				{
					Vec2f proj1 = projection(transformedPoints, weapPos, axes2[i]);
					Vec2f proj2 = projection(entityCollidable.points, entityPos, axes2[i]);
					if ( !overlap(proj1, proj2))
					{// min2-max1
						weapCollidable.collided = false;
						return;
					}
				//	System.out.println("b" + i);
					/*System.out.println("Proj1.min " + proj1.x+ " Proj1.max" + proj1.y);
					System.out.println("Proj2.min " + proj2.x+ " Proj2.max" + proj2.y );
					System.out.println("Axis2[" + i + "]" + " : " + "( " + axes1[i].x + " , " + axes2[i].y + " )");*/
				}
				weapCollidable.collided = true;
				if (!weapCollidable.collisionTargets.contains(e))
					weapCollidable.collisionTargets.add(e);

			}
		}

		// System.out.println("Collision num" + collNum);

	}

	private boolean overlap(Vec2f proj1, Vec2f proj2)
	{
		float minProj = ( proj2.y > proj1.y) ? proj2.x : proj1.x;
		float maxProj = ( proj2.y > proj1.y) ? proj1.y : proj2.y;
		if ( (minProj - maxProj < 0) )
			return true;
		else
			return false;
		
	}

	private Vec2f projection(Vec2f[] transformedPoints, CPosition pos,  Vec2f axis)
	{
		float min = 1000000000;
		float max = 0;
//		System.out.println("Axis is " + axis.x + " " + axis.y);
		for (int i = 0; i < transformedPoints.length; i++)
		{
			Vec2f vert = new Vec2f( (transformedPoints[i].x + pos.x), (transformedPoints[i].y + pos.y));
			float p = Math.abs(axis.dot(vert));
		//	System.out.println("Dot is " + p);
			if (p < min) 
				min = p;
			else if (p > max)
				max = p;
		}
		return new Vec2f(min, max);
	}

	private void resolveMapCollision(UUID e, CCollidable collidable, CMovable mov, CPosition pos)
	{

		ArrayList<Tile> tilesYUp = getTilesOccupyingYUp(World.level.tileMaps.get(1).tileSet, collidable, mov, pos);

		for (int i = 0; i < tilesYUp.size(); i++)
		{
			Tile tile = tilesYUp.get(i);
			if (tile.isSolid() == true)
			{
				// System.out.println("Collision on Y UP! " + "Tile coord" + "( " + tile.x + " , " + tile.y + " )");
				mov.dir.y = -1; // reverse
				pos.y = (tile.getY() + 1) * TileMap.TILE_SIZE;
				mov.vel.y = 0;
				collidable.collisionYUp = true;
			}
			else
			{
				collidable.collisionYUp = false;
			}

		}
		ArrayList<Tile> tilesYDown = getTilesOccupyingYDown(World.level.tileMaps.get(1).tileSet, collidable, mov, pos);

		for (int i = 0; i < tilesYDown.size(); i++)
		{
			Tile tile = tilesYDown.get(i);
			float temp = mov.vel.x;
			if (tile.isSolid() == true)
			{
				collidable.collisionYBot = true;
				// System.out.println("Collision on Y Down! " + "Tile coord" + "( " + tile.x + " , " + tile.y + " )");
				pos.y = (tile.getY() - 1) * TileMap.TILE_SIZE - 1;
				mov.vel.y = 0;
				mov.vel.x = temp;
				mov.dir.y = 0;

			}
			else
			{
				collidable.collisionYBot = false;
			}
		}

		ArrayList<Tile> tilesRightX = getTilesOccupyingRightX(World.level.tileMaps.get(1).tileSet, collidable, mov, pos);
		for (int i = 0; i < tilesRightX.size(); i++)
		{
			Tile tile = tilesRightX.get(i);
			if (tile.isSolid() == true)
			{
				System.out.println("Collision on XRight! " + "Tile coord" + "( " + tile.x + " , " + tile.y + " )");
				collidable.collisionXRight = true;
				float temp = mov.vel.x;
				mov.vel.x = 0;
				pos.x = (tile.getX() - 1) * TileMap.TILE_SIZE;
				break;
			}
			else
			{
				collidable.collisionXRight = false;
			}
		}

		ArrayList<Tile> tilesLeftX = getTilesOccupyingLeftX(World.level.tileMaps.get(1).tileSet, collidable, mov, pos);
		for (int i = 0; i < tilesLeftX.size(); i++)
		{

			Tile tile = tilesLeftX.get(i);
			if (tile.isSolid() == true)
			{
				System.out.println("Collision on XLeft! " + "Tile coord" + "( " + tile.x + " , " + tile.y + " )");
				collidable.collisionXLeft = true;
				float temp = mov.vel.x;
				mov.vel.x = 0;
				pos.x = (tile.getX() + 1) * TileMap.TILE_SIZE;
				break;
			}
			else
			{
				collidable.collisionXLeft = false;
			}
		}
		/*
		 * SpriteBatcher.batcher.draw( temp , tile.getX() * TileMap.TILE_WIDTH, tile.getY() * TileMap.TILE_HEIGHT, TileMap.TILE_WIDTH, TileMap.TILE_HEIGHT);
		 * 
		 * }
		 * 
		 * SpriteBatcher.batcher.end();
		 */
	}

	public ArrayList<Tile> getTilesOccupyingRightX(Tile[][] tiles, CCollidable collidable, CMovable mov, CPosition pos)
	{
		ArrayList<Tile> occupiedTiles = new ArrayList<Tile>();

		for (int i = 0; i < 2; i++) // RIGHT
		{
			int xCoord = (int) (collidable.points[i + 2].x + pos.x + mov.vel.x);
			int yCoord = (int) (collidable.points[i + 2].y + pos.y + mov.vel.y);
			occupiedTiles.add(tiles[yCoord / 64][xCoord / 64]);
		}
		return occupiedTiles;
	}

	public ArrayList<Tile> getTilesOccupyingLeftX(Tile[][] tiles, CCollidable collidable, CMovable mov, CPosition pos)
	{
		ArrayList<Tile> occupiedTiles = new ArrayList<Tile>();
		for (int i = 0; i < 2; i++) // LEFT
		{
			int offSet = (mov.vel.x > 0) ? 0 : -1; // If no movement, continue detecting collision like right case, else leave stucking.
			int xCoord = (int) (collidable.points[i + 6].x + offSet + pos.x + mov.vel.x);
			int yCoord = (int) (collidable.points[i + 6].y + offSet + pos.y + mov.vel.y);
			// System.out.println("Collision on XLeft0 xCoord! " + " Player pos.x" + pos.x );
			// System.out.println("Collision on XLeft0 yCoord! " + " Player pos.y" + pos.y );
			occupiedTiles.add(tiles[yCoord / 64][xCoord / 64]);
			// System.out.println("Tile.x " + tiles[yCoord / 64][ xCoord/64].x + " Tile.y " + tiles[yCoord / 64][ xCoord/64].y + " solid : " + tiles[yCoord / 64][ xCoord/64].isSolid() );
		}
		return occupiedTiles;

	}

	public ArrayList<Tile> getTilesOccupyingYUp(Tile[][] tiles, CCollidable collidable, CMovable mov, CPosition pos)
	{
		ArrayList<Tile> occupiedTiles = new ArrayList<Tile>();

		for (int i = 0; i < 2; i++) // TOP
		{
			int xCoord = (int) (collidable.points[i].x + pos.x + mov.vel.x);
			int yCoord = (int) (collidable.points[i].y + pos.y + mov.vel.y);
			occupiedTiles.add(tiles[yCoord / 64][xCoord / 64]);
		}
		return occupiedTiles;
	}

	public ArrayList<Tile> getTilesOccupyingYDown(Tile[][] tiles, CCollidable collidable, CMovable mov, CPosition pos)
	{
		ArrayList<Tile> occupiedTiles = new ArrayList<Tile>();

		for (int i = 0; i < 2; i++) // BOT
		{
			int xCoord = (int) (collidable.points[i + 4].x + pos.x + mov.vel.x);
			int yCoord = (int) (collidable.points[i + 4].y + pos.y + mov.vel.y);
			occupiedTiles.add(tiles[yCoord / 64][xCoord / 64]);
		}

		return occupiedTiles;
	}

	@Override
	public String getSimpleName()
	{
		return null;
	}
}