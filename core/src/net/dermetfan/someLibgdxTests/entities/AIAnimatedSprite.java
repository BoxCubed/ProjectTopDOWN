package net.dermetfan.someLibgdxTests.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import net.dermetfan.gdx.graphics.g2d.AnimatedSprite;

public class AIAnimatedSprite extends AnimatedSprite {

	private Vector2 velocity = new Vector2();
	private float speed = 500; // we don't need the tolerance variable at all since it behaved perfectly when set to 1 in all cases

	private Array<Vector2> path;
	private int waypoint = 0;

	public AIAnimatedSprite(Animation animation, Array<Vector2> path) {
		super(animation);
		this.path = path;
	}

	@Override
	public void update(float delta) {
		super.update(delta);
		float angle = (float) Math.atan2(path.get(waypoint).y - getY(), path.get(waypoint).x - getX());
		velocity.set((float) Math.cos(angle) * speed, (float) Math.sin(angle) * speed);

		setPosition(getX() + velocity.x * delta, getY() + velocity.y * delta);
		setRotation(angle * MathUtils.radiansToDegrees);

		if(isWaypointReached()) {
			setPosition(path.get(waypoint).x, path.get(waypoint).y);
			if(++waypoint >= path.size)
				waypoint = 0;
		}
	}

	public boolean isWaypointReached() {
		return Math.abs(path.get(waypoint).x - getX()) <= speed * Gdx.graphics.getDeltaTime() && Math.abs(path.get(waypoint).y - getY()) <= speed * Gdx.graphics.getDeltaTime(); // just removed the tolerance variable
	}

	public Array<Vector2> getPath() {
		return path;
	}

	public int getWaypoint() {
		return waypoint;
	}

}
