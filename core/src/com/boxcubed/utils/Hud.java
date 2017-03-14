package com.boxcubed.utils;

import java.text.DecimalFormat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import me.boxcubed.main.Sprites.PlayerLight;
import me.boxcubed.main.States.GameState;

public class Hud {
	public OrthographicCamera textCam;
	DecimalFormat format;
	BitmapFont font = new BitmapFont();
	public Hud(){
		textCam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		textCam.update();
	}
	public void update(){
		textCam.update();
		
		format = new DecimalFormat("#.##");
	}
	public void render(SpriteBatch sb,float delta){
		String health = "";
		int i;

		for (i = 0; i < GameState.instance.player.getHealth() / GameState.instance.player.getMaxHealth() * 100f; i++) {
			health += "|";
		}
		for (; i < 100; i++)
			health += "-";
		
		font.draw(sb, "FPS: " + Gdx.graphics.getFramesPerSecond(), -250, textCam.viewportHeight / 2);
		font.draw(sb, "Entity Number: " + GameState.instance.entities.size(), -380, textCam.viewportHeight / 2);
		font.draw(sb,
				"Time: " + /* format.format((PlayerLight.amlight*100)/8) */PlayerLight.amToTime(), -120,
				textCam.viewportHeight / 2);
		font.draw(sb, "noZombieMode: " + GameState.instance.noZombie, 0, textCam.viewportHeight / 2);
		font.draw(sb, "noTimeMode: " + GameState.instance.noTime, 150, textCam.viewportHeight / 2);
		font.draw(sb, "Player Position: " + format.format(GameState.instance.player.getBody().getPosition().x) + ","
				+ format.format(GameState.instance.player.getBody().getPosition().y), -600, textCam.viewportHeight / 2);
		font.draw(sb, health, -200, textCam.viewportHeight / 2 - 50);
	}
	
	public void dispose(){
		font.dispose();
	}
}
