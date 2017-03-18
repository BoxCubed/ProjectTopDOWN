package com.boxcubed.utils;

import java.text.DecimalFormat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

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
		
		format = new DecimalFormat("#.#");
	}
	public void render(SpriteBatch sb){
		String health = "";
		int i;

		for (i = 0; i < GameState.instance.player.getHealth() / GameState.instance.player.getMaxHealth() * 100f; i++) {
			health += "|";
		}
		for (; i < 100; i++)
			health += "-";
								//TODO fix alignment
		font.draw(sb, "FPS/Delta: " + Gdx.graphics.getFramesPerSecond()+"/"+
				  format.format(Gdx.graphics.getRawDeltaTime()*100), -250, textCam.viewportHeight / 2);
		
		font.draw(sb, "Entity Number: " + GameState.instance.entities.size(), -380, textCam.viewportHeight / 2);
		
		font.draw(sb,														//TODO make time work with night 5 sec delay
				"Time: " + /* format.format((PlayerLight.amlight*100)/8) */PlayerLight.amToTime(), -120,
				textCam.viewportHeight / 2);
		
		font.draw(sb, "noZombieMode: " + GameState.instance.noZombie, 0, textCam.viewportHeight / 2);
		
		font.draw(sb, "noTimeMode: " + GameState.instance.noTime, 150, textCam.viewportHeight / 2);
		
		font.draw(sb, "Player Position: " + format.format(GameState.instance.player.getBody().getPosition().x) + ","
				+ format.format(GameState.instance.player.getBody().getPosition().y), -600, textCam.viewportHeight / 2);
		Vector3 mousePos=GameState.instance.getMouseCords(); 
		font.draw(sb, "Mouse Position: "+format.format(mousePos.x)+","+format.format(mousePos.y), -600, textCam.viewportHeight/2-20);
		
		font.draw(sb, health, -200, textCam.viewportHeight / 2 - 50);
	}
	
	public void dispose(){
		font.dispose();
	}
}
