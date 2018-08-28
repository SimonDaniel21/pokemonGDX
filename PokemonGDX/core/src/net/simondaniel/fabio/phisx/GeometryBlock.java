package net.simondaniel.fabio.phisx;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;

/**
 * class representing one Block in a connected group of Blocks
 * holds information about neighbors in a list like way
 * @author simon
 *
 */
public class GeometryBlock {
	
	public static final float BLOCK_SIZE = 1; // not in use, should stay at 1, no reason to change that

	int collisionValue;

	static final int CENTER = 0, TOP = 1, BOT = 2, LEFT = 3, RIGHT = 4;

	/**
	 * erstellt einen Block mit BlockPosition(Grid position)
	 * @param x pos.x
	 * @param y pos.y
	 */
	public GeometryBlock(int x, int y) {
		this.x = x;
		this.y = y;
	}

	int x, y; // position inside Tilegrid
	GeometryBlock top, bot, left, right; // neighbors
	
	/**
	 * checks if any of neighbors have the given position (recursively) (start point)
	 * @param x
	 * @param y
	 * @return weather one single Block has the position
	 */
	public boolean containsPosition(int x, int y) {
		return containsPosition(x, y, new ArrayList<GeometryBlock>());
	}

	/**
	 * checks if any of neighbors have the given position (recursively)
	 * @param x
	 * @param y
	 * @param alreadyVisited a list of already checked Blocks
	 * @return
	 */
	private boolean containsPosition(int x, int y, List<GeometryBlock> alreadyVisited) {

		
		alreadyVisited.add(this);
		boolean t = false, b = false, l = false, r = false;

		if (this.x == x && this.y == y)
			return true;

		if (top != null && !alreadyVisited.contains(top))
			t = top.containsPosition(x, y, alreadyVisited);

		if (bot != null && !alreadyVisited.contains(bot))
			b = bot.containsPosition(x, y, alreadyVisited);

		if (left != null && !alreadyVisited.contains(left))
			l = left.containsPosition(x, y, alreadyVisited);

		if (right != null && !alreadyVisited.contains(right))
			r = right.containsPosition(x, y, alreadyVisited);

		return t || b || l || r;
	}
	
	/**
	 * adds corner Points of the whole block to a Vector2 List
	 * @param v
	 */
	public void addVertices(List<Vector2> v) {
		if(bot == null && left == null) {
			drawBottomLine(v, this);
		}
	}
	
	/**
	 * starts to outline a Block and add its cornenr points to the List v.
	 * outling starts by going to the left (drawBottomLine) and ends by
	 * coming from the top(drawLeftLine) to the startNode
	 * @param v
	 * @param start startNode
	 */
	private void drawBottomLine(List<Vector2> v, GeometryBlock start) {
		if(bot != null) {
			v.add(new Vector2(x, y));
			bot.drawLeftLine(v, start);
		}
		else if(right == null) {
			v.add(new Vector2(x+1, y));
			drawRightLine(v, start);
		}
		else {
			right.drawBottomLine(v, start);
		}
	}
	
	private void drawLeftLine(List<Vector2> v, GeometryBlock start) {
		if(left != null) {
			v.add(new Vector2(x, y+1));
			left.drawTopLine(v, start);
		}
		else if(bot == null) {
			v.add(new Vector2(x, y));
			if(this != start)	// END POINT
				drawBottomLine(v, start);
		}
		else {
			bot.drawLeftLine(v, start);
		}
	}
	
	private void drawRightLine(List<Vector2> v, GeometryBlock start) {
		if(right != null) {
			v.add(new Vector2(x+1, y));
			right.drawBottomLine(v, start);
		}
		else if(top == null) {
			v.add(new Vector2(x+1, y +1));
			drawTopLine(v, start);
		}
		else {
			top.drawRightLine(v, start);
		}
	}
	
	private void drawTopLine(List<Vector2> v, GeometryBlock start) {
		if(top != null) {
			v.add(new Vector2(x+1, y+1));
			top.drawRightLine(v, start);
		}
		else if(left == null) {
			v.add(new Vector2(x, y+1));
			drawLeftLine(v, start);
		}
		else {
			left.drawTopLine(v, start);
		}
	}
}
