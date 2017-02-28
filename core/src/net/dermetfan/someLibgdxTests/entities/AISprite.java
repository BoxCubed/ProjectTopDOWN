package net.dermetfan.someLibgdxTests.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class AISprite extends Sprite {

	private Vector2 velocity = new Vector2();
	private float speed = 500; // we don't need the tolerance variable at all since it behaved perfectly when set to 1 in all cases

	private Array<Vector2> path;
	private int waypoint = 0;

	public AISprite(Sprite sprite, Array<Vector2> path) {
		super(sprite);
		this.path = path;
	}

	@Override
	public void draw(Batch spriteBatch) {
		update(Gdx.graphics.getDeltaTime());
		super.draw(spriteBatch);
	}

	public void update(float delta) {
		float angle = MathUtils.atan2(path.get(waypoint).y - getY(), path.get(waypoint).x - getX());
		velocity.set(MathUtils.cos(angle) * speed, MathUtils.sin(angle) * speed);

		setPosition(getX() + velocity.x * delta, getY() + velocity.y * delta);
		setRotation(angle * MathUtils.radiansToDegrees);

		if(isWaypointReached(delta)) {
			setPosition(path.get(waypoint).x, path.get(waypoint).y);
			if(++waypoint >= path.size)
				waypoint = 0;
		}
	}

	public boolean isWaypointReached(float delta) {
		return Math.abs(path.get(waypoint).x - getX()) <= speed * delta && Math.abs(path.get(waypoint).y - getY()) <= speed * delta; // just removed the tolerance variable
	}

	public Array<Vector2> getPath() {
		return path;
	}

	public int getWaypoint() {
		return waypoint;
	}

}
