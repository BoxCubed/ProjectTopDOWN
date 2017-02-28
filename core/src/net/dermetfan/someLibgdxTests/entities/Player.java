package net.dermetfan.someLibgdxTests.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;

public class Player extends Sprite implements InputProcessor {

	/** the movement velocity */
	private Vector2 velocity = new Vector2();

	private float speed = 60 * 2, gravity = 60 * 1.8f;

	private boolean canJump;

	private TiledMapTileLayer collisionLayer;

	public Player(Sprite sprite, TiledMapTileLayer collisionLayer) {
		super(sprite);
		this.collisionLayer = collisionLayer;
	}

	@Override
	public void draw(Batch spriteBatch) {
		update(Gdx.graphics.getDeltaTime());
		super.draw(spriteBatch);
	}

	public void update(float delta) {
		// apply gravity
		velocity.y -= gravity * delta;

		// clamp velocity
		if(velocity.y > speed)
			velocity.y = speed;
		else if(velocity.y < -speed)
			velocity.y = -speed;

		// save old position
		float oldX = getX(), oldY = getY(), tileWidth = collisionLayer.getTileWidth(), tileHeight = collisionLayer.getTileHeight();
		boolean collisionX = false, collisionY = false;

		// move on x
		setX(getX() + velocity.x * delta);

		if(velocity.x < 0) { // going left
			// top left
			collisionX = collisionLayer.getCell((int) (getX() / tileWidth), (int) ((getY() + getHeight()) / tileHeight)).getTile().getProperties().containsKey("blocked");

			// middle left
			if(!collisionX)
				collisionX = collisionLayer.getCell((int) (getX() / tileWidth), (int) ((getY() + getHeight() / 2) / tileHeight)).getTile().getProperties().containsKey("blocked");

			// bottom left
			if(!collisionX)
				collisionX = collisionLayer.getCell((int) (getX() / tileWidth), (int) (getY() / tileHeight)).getTile().getProperties().containsKey("blocked");
		} else if(velocity.x > 0) { // going right
			// top right
			collisionX = collisionLayer.getCell((int) ((getX() + getWidth()) / tileWidth), (int) ((getY() + getHeight()) / tileHeight)).getTile().getProperties().containsKey("blocked");

			// middle right
			if(!collisionX)
				collisionX = collisionLayer.getCell((int) ((getX() + getWidth()) / tileWidth), (int) ((getY() + getHeight() / 2) / tileHeight)).getTile().getProperties().containsKey("blocked");

			// bottom right
			if(!collisionX)
				collisionX = collisionLayer.getCell((int) ((getX() + getWidth()) / tileWidth), (int) (getY() / tileHeight)).getTile().getProperties().containsKey("blocked");
		}

		// react to x collision
		if(collisionX) {
			setX(oldX);
			velocity.x = 0;
		}

		// move on y
		setY(getY() + velocity.y * delta);

		if(velocity.y < 0) { // going down
			// bottom left
			collisionY = collisionLayer.getCell((int) (getX() / tileWidth), (int) (getY() / tileHeight)).getTile().getProperties().containsKey("blocked");

			// bottom middle
			if(!collisionY)
				collisionY = collisionLayer.getCell((int) ((getX() + getWidth() / 2) / tileWidth), (int) (getY() / tileHeight)).getTile().getProperties().containsKey("blocked");

			// bottom right
			if(!collisionY)
				collisionY = collisionLayer.getCell((int) ((getX() + getWidth()) / tileWidth), (int) (getY() / tileHeight)).getTile().getProperties().containsKey("blocked");

			canJump = collisionY;
		} else if(velocity.y > 0) { // going up
			// top left
			collisionY = collisionLayer.getCell((int) (getX() / tileWidth), (int) ((getY() + getHeight()) / tileHeight)).getTile().getProperties().containsKey("blocked");

			// top middle
			if(!collisionY)
				collisionY = collisionLayer.getCell((int) ((getX() + getWidth() / 2) / tileWidth), (int) ((getY() + getHeight()) / tileHeight)).getTile().getProperties().containsKey("blocked");

			// top right
			if(!collisionY)
				collisionY = collisionLayer.getCell((int) ((getX() + getWidth()) / tileWidth), (int) ((getY() + getHeight()) / tileHeight)).getTile().getProperties().containsKey("blocked");
		}

		// react to y collision
		if(collisionY) {
			setY(oldY);
			velocity.y = 0;
		}
	}

	public Vector2 getVelocity() {
		return velocity;
	}

	public void setVelocity(Vector2 velocity) {
		this.velocity = velocity;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public float getGravity() {
		return gravity;
	}

	public void setGravity(float gravity) {
		this.gravity = gravity;
	}

	public TiledMapTileLayer getCollisionLayer() {
		return collisionLayer;
	}

	public void setCollisionLayer(TiledMapTileLayer collisionLayer) {
		this.collisionLayer = collisionLayer;
	}

	@Override
	public boolean keyDown(int keycode) {
		switch(keycode) {
		case Keys.W:
			if(canJump) {
				velocity.y = speed;
				canJump = false;
			}
			break;
		case Keys.A:
			velocity.x = -speed;
			break;
		case Keys.D:
			velocity.x = speed;
		}
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		switch(keycode) {
		case Keys.A:
		case Keys.D:
			velocity.x = 0;
		}
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

}
