package com.boxcubed.utils;

import com.badlogic.gdx.InputProcessor;
/**
 * Extend wht you need to edit, without the mess<br>
 * Brought to you by Java 8, Where we don't hang down on Java 6 like this Autistic Api.
 * @author ryan9
 *
 */
public interface CleanInputProcessor extends InputProcessor{
	@Override
	default boolean keyDown(int keycode) {
		return false;
	}
	@Override
	default boolean keyTyped(char character) {
		return false;
	}@Override
	default boolean keyUp(int keycode) {
		return false;
	}@Override
	default boolean mouseMoved(int screenX, int screenY) {
		return false;
	}@Override
	default boolean scrolled(int amount) {
		return false;
	}@Override
	default boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}@Override
	default boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}@Override
	default boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

}
