package com.boxcubed.utils;

import java.text.DecimalFormat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

import me.boxcubed.main.TopDown;
import me.boxcubed.main.Objects.Clock;
import me.boxcubed.main.States.GameState;

public class Hud {
	public OrthographicCamera textCam;
	Texture healthTexture;
	Texture staminaTexture;
	DecimalFormat format;
	BitmapFont font = new BitmapFont();
	Clock clock;
	public Hud(Clock clock){
		this.clock=clock;
		textCam = new OrthographicCamera(1280, 900);
		textCam.update();
		
		healthTexture = TopDown.assets.get(Assets.healthIMAGE, Texture.class);
		staminaTexture = TopDown.assets.get(Assets.staminaIMAGE,Texture.class);
	}
	public void update(){
		textCam.update();
		
		format = new DecimalFormat("#.#");
	}
	public void render(SpriteBatch sb){
		int i;

		for (i = 0; i < GameState.instance.player.getHealth() / GameState.instance.player.getMaxHealth() * 100f; i+=10) {
			sb.draw(healthTexture,i*2,textCam.viewportHeight/2-50,30,30);
		}
		
		for (i = 0; i < GameState.instance.player.getStamina() / GameState.instance.player.getMaxStamina() * 100f; i+=10) {
			sb.draw(staminaTexture,i*2,textCam.viewportHeight/2-80,12,15);
		}
								//TODO fix alignment
		font.draw(sb, "FPS/Delta: " + Gdx.graphics.getFramesPerSecond()+"/"+
				  format.format(Gdx.graphics.getRawDeltaTime()*100), -250, textCam.viewportHeight / 2);
		
		font.draw(sb, "Entity Number: " + GameState.instance.entities.size(), -380, textCam.viewportHeight / 2);
		
		font.draw(sb,														//TODO make time work with night 5 sec delay
				"Time: " + /* format.format((PlayerLight.amlight*100)/8) */clock.amToTime(), -120,
				textCam.viewportHeight / 2);
		
		font.draw(sb, "noZombieMode: " + GameState.instance.noZombie, 0, textCam.viewportHeight / 2);
		
		font.draw(sb, "noTimeMode: " + GameState.instance.noTime, 150, textCam.viewportHeight / 2);
		
		font.draw(sb, "Player Position: " + format.format(GameState.instance.player.getBody().getPosition().x) + ","
				+ format.format(GameState.instance.player.getBody().getPosition().y), -600, textCam.viewportHeight / 2);
		Vector3 mousePos=GameState.instance.getMouseCords(); 
		font.draw(sb, "Mouse Position: "+format.format(mousePos.x)+","+format.format(mousePos.y), -600, textCam.viewportHeight/2-20);
		
	}
	
	public void dispose(){
		font.dispose();
	}
}
