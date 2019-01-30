package net.simondaniel.fabio.phisx;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

/**
 * Responsible for converting a integer array consisting of collision information to
 * Box2d Fixtures
 * @author simon
 *
 */
public class Geometry {

	/**
	 * for convinience sets flip y to false;
	 * @param b
	 * @param collision
	 * @param width
	 */
	public static void addGeometryFixtures(Body b, int[] collision, int width) {
		addGeometryFixtures(b, collision, width, false);
	}
	
	/**
	 * adds one Fixture for all connected Parts inide the collision Array to the body
	 * @param body
	 * @param collision 
	 * @param width of collisionArray
	 * @param flip flip Y Axis
	 */
	public static void addGeometryFixtures(Body body, int[] collision, int width, boolean flip) {
		
		int height = collision.length / width;
		
		if(flip) {
			int[] temp = new int[collision.length];
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					temp[x + y*width] = collision[x+ (height -y-1)*width];
				}
			}
			collision = temp;
		}

		ChainShape rect;
	
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.density = 0f;
		fixtureDef.friction = 0f;
		fixtureDef.restitution = 0f;


		ChainShape cs = new ChainShape();
		Vector2[] boundVecs = new Vector2[4];
		boundVecs[0] = new Vector2(0,0);
		boundVecs[1] = new Vector2(width,0);
		boundVecs[2] = new Vector2(width,height);
		boundVecs[3] = new Vector2(0,height);
		cs.createLoop(boundVecs);
		fixtureDef.shape = cs;
		body.createFixture(fixtureDef);
		
		List<GeometryBlock> blocks = new ArrayList<GeometryBlock>();

		for (int i = 0; i < collision.length; i++) {

			int x = i % width;
			int y = i / width;

			if (collision[i] == 1) {

				boolean alreadyInBlock = false;
				for (GeometryBlock b : blocks) {

					if (b.containsPosition(x, y)) {
						alreadyInBlock = true;
						break;
					}
				}

				if (alreadyInBlock)
					continue;

				GeometryBlock b = getBlock(collision, width, x, y);
				blocks.add(b);
				
				List<Vector2> vecList = new ArrayList<Vector2>();
				b.addVertices(vecList);
				Vector2[] vecs = new Vector2[vecList.size()];
				for (int j = 0; j < vecs.length; j++) {
					vecs[j] = vecList.get(j);
				}
			
				cs = new ChainShape();
				cs.createLoop(vecs);
				fixtureDef.shape = cs;
				body.createFixture(fixtureDef);
				
			}
		}
	}
	

	/**
	 * checks if a block with a certain position is already contained in a List
	 * @param alreadyContained list
	 * @param x pos
	 * @param y pos
	 * @return
	 */
	private static GeometryBlock getBlockAt(List<GeometryBlock> alreadyContained, int x, int y) {
		for (GeometryBlock b : alreadyContained) {
			if (b.x == x && b.y == y) {
				return b;
			}
		}
		return null;
	}
	
	/**
	 * creates a block by checking for all connected parts inside the collisionArray recursively (starting point)
	 * @param collision array with collision data
	 * @param w width of the collisionMap
	 * @param x pos
	 * @param y pos
	 * @return block with all connected Parts referenced
	 */
	private static GeometryBlock getBlock(int[] collision, int w, int x, int y) {
		return getBlock(collision, w, x, y, GeometryBlock.CENTER, new ArrayList<GeometryBlock>());
	}

	
	/**
	 * creates a block by checking for all connected parts inside the collisionArray recursively
	 * @param collision array with collision data
	 * @param w width of the collisionMap
	 * @param x pos
	 * @param y pos
	 * @param from from which direction it is checking
	 * @param alreadyContained allready containing blocks
	 * @return block with all connected Partsreferenced
	 */
	private static GeometryBlock getBlock(int[] collision, int w, int x, int y, int from, List<GeometryBlock> alreadyContained) {

		
		if (collision[x + y * w] != 1)
			return null;

		GeometryBlock b = new GeometryBlock(x, y);
		alreadyContained.add(b);
		int height = collision.length / w;

		if (from != GeometryBlock.TOP && y + 1 < height) {
			GeometryBlock tn = getBlockAt(alreadyContained, x, y+1); //ln for top neighbor
			if(tn == null) {
				b.top = getBlock(collision, w, x, y + 1, GeometryBlock.BOT, alreadyContained);
			}
			else {
				b.top = tn;
			}
			if (b.top != null)
				b.top.bot = b;
		}

		if (from != GeometryBlock.BOT && y - 1 >= 0) {
			GeometryBlock bn = getBlockAt(alreadyContained, x, y-1);
			if(bn == null) {
				b.bot = getBlock(collision, w, x, y - 1, GeometryBlock.TOP, alreadyContained);
			}
			else {
				b.bot = bn;
			}
			if (b.bot != null)
				b.bot.top = b;
		}

		if (from != GeometryBlock.LEFT && x - 1 >= 0) {
			GeometryBlock ln = getBlockAt(alreadyContained, x-1, y);
			if(ln == null) {
				b.left = getBlock(collision, w, x - 1, y, GeometryBlock.RIGHT, alreadyContained);
			}
			else {
				b.left = ln;
			}
			if (b.left != null)
				b.left.right = b;
		}

		if (from != GeometryBlock.RIGHT && x + 1 < w) {
			GeometryBlock rn = getBlockAt(alreadyContained, x+1, y);
			if(rn == null) {
				b.right = getBlock(collision, w, x + 1, y, GeometryBlock.LEFT, alreadyContained);
			}
			else {
				b.right = rn;
			}
			if (b.right != null)
				b.right.left = b;
		}

		return b;

	}
}
