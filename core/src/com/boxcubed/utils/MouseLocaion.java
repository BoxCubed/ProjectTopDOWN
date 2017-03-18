package com.boxcubed.utils;

import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;

import me.boxcubed.main.States.GameState;

public class MouseLocaion implements Location<Vector2> {
	float rot=0;
	@Override
	public Vector2 getPosition() {
		return new Vector2(GameState.instance.getMouseCords().x,GameState.instance.getMouseCords().y);
	}

	@Override
	public float getOrientation() {
		return rot;
	}

	@Override
	public void setOrientation(float orientation) {
		rot=orientation;
	}

	@Override
	public float vectorToAngle(Vector2 vector) {
		return (float)Math.atan2(-vector.x, vector.y);
	}

	@Override
	public Vector2 angleToVector(Vector2 outVector, float angle) {
		outVector.x=-(float)Math.sin(angle);
		outVector.y=(float)Math.cos(angle);
		return outVector;
	}

	@Override
	public Location<Vector2> newLocation() {
		return this;
	}

	

}
