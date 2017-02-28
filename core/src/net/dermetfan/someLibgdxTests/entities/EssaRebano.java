package net.dermetfan.someLibgdxTests.entities;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class EssaRebano extends Sprite implements InputProcessor {

	/** the movement velocity */
	private Vector2 velocity = new Vector2();

	private float speed = 60 * 2, gravity = 60 * 3, boundingTolerance = 1;

	private boolean canJump;

	private TiledMapTileLayer collisionLayer;

	private String blockedKey = "blocked", diggableKey = "diggable", beforeDiggingKey = "beforeDigging", afterDiggingKey = "afterDigging", diggingCellKey = "diggingCell", isDiggingKey = "isDigging", diggingSinceKey = "diggingSince", digTimeKey = "digTime", collisionLayerName = "background";

	private AnimatedTiledMapTile digging;

	public EssaRebano(Sprite sprite, TiledMap map) {
		super(sprite);
		collisionLayer = (TiledMapTileLayer) map.getLayers().get(collisionLayerName);

		Array<StaticTiledMapTile> frameTiles = new Array<StaticTiledMapTile>(2);
		// ESSA REBANO: find tile that should be set after digging
		StaticTiledMapTile afterDiggingTile = null;

		// get the frame tiles
		Iterator<TiledMapTile> tiles = map.getTileSets().getTileSet("tiles").iterator();
		while(tiles.hasNext()) {
			TiledMapTile tile = tiles.next();
			if(tile.getProperties().containsKey("animation") && tile.getProperties().get("animation", String.class).equals("digging"))
				frameTiles.add((StaticTiledMapTile) tile);
			else if(tile.getProperties().containsKey("afterDigging")) // ESSA REBANO: found it, set it
				afterDiggingTile = (StaticTiledMapTile) tile;
		}

		// ESSA REBANO: create the digging animated tile
		digging = new AnimatedTiledMapTile(1 / 3f, frameTiles);
		// ESSA REBANO: put all things needed for digging in the digging tile properties
		digging.getProperties().put(afterDiggingKey, afterDiggingTile); // ESSA REBANO: set the tile that should be set after digging
		digging.getProperties().put(blockedKey, null); // ESSA REBANO: the tile should be blocked while digging
		digging.getProperties().put(isDiggingKey, false); // ESSA REBANO: set that we're currently not digging
		digging.getProperties().put(digTimeKey, 1000); // ESSA REBANO: set the time that digging takes (in milliseconds)
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

		//Collision
		//Save old position
		float oldX = getX(), oldY = getY(), tileWidth = collisionLayer.getTileWidth(), tileHeight = collisionLayer.getTileHeight();
		boolean collisionX = false, collisionY = false;
		Cell tmpCell;

		// move on x
		setX(getX() + velocity.x * delta);

		if(velocity.x < 0) { // going left
			// top left
			if((tmpCell = collisionLayer.getCell((int) ((getX() + boundingTolerance) / tileWidth), (int) ((getY() + getHeight() - boundingTolerance) / tileHeight))).getTile() != null)
				collisionX = isCellBlocked(tmpCell);

			// middle left
			if(!collisionX && (tmpCell = collisionLayer.getCell((int) ((getX() + boundingTolerance) / tileWidth), (int) ((getY() + getHeight() / 2) / tileHeight))).getTile() != null)
				collisionX = isCellBlocked(tmpCell);

			// bottom left
			if(!collisionX && (tmpCell = collisionLayer.getCell((int) ((getX() + boundingTolerance) / tileWidth), (int) ((getY() + boundingTolerance) / tileHeight))).getTile() != null)
				collisionX = isCellBlocked(tmpCell);
		} else if(velocity.x > 0) { // going right
			// top right
			if((tmpCell = collisionLayer.getCell((int) ((getX() - boundingTolerance + getWidth()) / tileWidth), (int) ((getY() - boundingTolerance + getHeight()) / tileHeight))).getTile() != null)
				collisionX = isCellBlocked(tmpCell);

			// middle right
			if(!collisionX && (tmpCell = collisionLayer.getCell((int) ((getX() - boundingTolerance + getWidth()) / tileWidth), (int) ((getY() + getHeight() / 2) / tileHeight))).getTile() != null)
				collisionX = isCellBlocked(tmpCell);

			// bottom right
			if(!collisionX && (tmpCell = collisionLayer.getCell((int) ((getX() - boundingTolerance + getWidth()) / tileWidth), (int) ((getY() + boundingTolerance) / tileHeight))).getTile() != null)
				collisionX = isCellBlocked(tmpCell);
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
			if((tmpCell = collisionLayer.getCell((int) ((getX() + boundingTolerance) / tileWidth), (int) ((getY() + boundingTolerance) / tileHeight))).getTile() != null)
				collisionY = isCellBlocked(tmpCell);

			// bottom middle
			if(!collisionY && (tmpCell = collisionLayer.getCell((int) ((getX() + getWidth() / 2) / tileWidth), (int) ((getY() + boundingTolerance) / tileHeight))).getTile() != null)
				collisionY = isCellBlocked(tmpCell);

			// bottom right
			if(!collisionY && (tmpCell = collisionLayer.getCell((int) ((getX() - boundingTolerance + getWidth()) / tileWidth), (int) ((getY() + boundingTolerance) / tileHeight))).getTile() != null)
				collisionY = isCellBlocked(tmpCell);

			canJump = collisionY;
		} else if(velocity.y > 0) { // going up
			// top left
			if((tmpCell = collisionLayer.getCell((int) ((getX() + boundingTolerance) / tileWidth), (int) ((getY() - boundingTolerance + getHeight()) / tileHeight))).getTile() != null)
				collisionY = isCellBlocked(tmpCell);

			// top middle
			if(!collisionY && (tmpCell = collisionLayer.getCell((int) ((getX() + getWidth() / 2) / tileWidth), (int) ((getY() - boundingTolerance + getHeight()) / tileHeight))).getTile() != null)
				collisionY = isCellBlocked(tmpCell);

			// top right
			if(!collisionY && (tmpCell = collisionLayer.getCell((int) ((getX() - boundingTolerance + getWidth()) / tileWidth), (int) ((getY() - boundingTolerance + getHeight()) / tileHeight))).getTile() != null)
				collisionY = isCellBlocked(tmpCell);
		}

		// react to y collision
		if(collisionY) {
			setY(oldY);
			velocity.y = 0;
		}

		// ESSA REBANO: update the digging behaviour
		if(digging.getProperties().get(isDiggingKey, Boolean.class)) // ESSA REBANO: if we're digging
			// ESSA REBANO: set tmpCell to the cell under the character and see if it is NOT the same as the cell that we currently dig
			if((tmpCell = collisionLayer.getCell((int) ((getX() + getWidth() / 2) / collisionLayer.getTileWidth()), (int) (getY() / collisionLayer.getTileHeight()))) != digging.getProperties().get(diggingCellKey, Cell.class)) {
				// ESSA REBANO: if we're here, the cell under the character is not the cell that we began to dig in anymore (the character may have moved while digging)
				digging.getProperties().get(diggingCellKey, Cell.class).setTile(digging.getProperties().get(beforeDiggingKey, StaticTiledMapTile.class)); // ESSA REBANO: therefore, set the tile of the cell that we dug in back to what it previously was
				digging.getProperties().put(isDiggingKey, false); // ESSA REBANO: save that we're not digging anymore
			} else if(TimeUtils.millis() - digging.getProperties().get(diggingSinceKey, Long.class) > digging.getProperties().get(digTimeKey, Integer.class)) {
				// ESSA REBANO: if we're here, digging is complete
				digging.getProperties().put(isDiggingKey, false); // therefore, set digging to false
				digging.getProperties().get(diggingCellKey, Cell.class).setTile(digging.getProperties().get(afterDiggingKey, StaticTiledMapTile.class)); // ESSA REBANO: and put the afterDigging tile in the cell that we dug
			}
	}

	private boolean isCellBlocked(Cell cell) {
		return cell.getTile().getProperties().containsKey(blockedKey);
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
			break;
		case Keys.DOWN: // ESSA REBANO: start digging
			Cell cell = collisionLayer.getCell((int) ((getX() + getWidth() / 2) / collisionLayer.getTileWidth()), (int) (getY() / collisionLayer.getTileHeight())); // ESSA REBANO: get the cell under the player
			if(cell.getTile() != null && cell.getTile().getProperties().containsKey(diggableKey)) { // ESSA REBANO: if the cell contains a tile which is diggable
				digging.getProperties().put(diggingCellKey, cell); // ESSA REBANO: store this cell somewhere
				digging.getProperties().put(beforeDiggingKey, cell.getTile()); // ESSA REBANO: save the tile that is in the cell
				cell.setTile(digging); // ESSA REBANO: put the digging animation tile in the cell
				digging.getProperties().put(isDiggingKey, true); // ESSA REBANO: set digging to true
				digging.getProperties().put(diggingSinceKey, TimeUtils.millis()); // ESSA REBANO: save the time we began to dig to decide if we're done later
			}
		}
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		switch(keycode) {
		case Keys.A:
			if(velocity.x < 0)
				velocity.x = 0;
			break;
		case Keys.D:
			if(velocity.x > 0)
				velocity.x = 0;
			break;
		case Keys.DOWN:
			// ESSA REBANO: stop digging
			Cell cell = collisionLayer.getCell((int) ((getX() + getWidth() / 2) / collisionLayer.getTileWidth()), (int) (getY() / collisionLayer.getTileHeight())); // ESSA REBANO: get the cell under the player
			if(cell.getTile() == digging) { // ESSA REBANO: if the tile that the character is standing on is the digging animation tile 
				digging.getProperties().put(isDiggingKey, false); // ESSA REBANO: set digging to false
				cell.setTile(digging.getProperties().get(beforeDiggingKey, StaticTiledMapTile.class)); // ESSA REBANO: change the tile back to what it was before digging
			}
		}
		return true;
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

	public float getBoundingTolerance() {
		return boundingTolerance;
	}

	public void setBoundingTolerance(float boundingTolerance) {
		this.boundingTolerance = boundingTolerance;
	}

	public String getBlockedKey() {
		return blockedKey;
	}

	public void setBlockedKey(String blockedKey) {
		this.blockedKey = blockedKey;
	}

	public String getDiggableKey() {
		return diggableKey;
	}

	public void setDiggableKey(String diggableKey) {
		this.diggableKey = diggableKey;
	}

	public String getCollisionLayerName() {
		return collisionLayerName;
	}

	public void setCollisionLayerName(String collisionLayerName) {
		this.collisionLayerName = collisionLayerName;
	}

	public AnimatedTiledMapTile getDigging() {
		return digging;
	}

	public void setDigging(AnimatedTiledMapTile digging) {
		this.digging = digging;
	}

	public boolean isCanJump() {
		return canJump;
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
