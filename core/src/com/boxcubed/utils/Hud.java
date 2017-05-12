package com.boxcubed.utils;

import java.text.DecimalFormat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;

import me.boxcubed.main.TopDown;
import me.boxcubed.main.Objects.Clock;
import me.boxcubed.main.Sprites.Player;
import me.boxcubed.main.States.GameState;

public class Hud {
	public OrthographicCamera textCam;
	Texture healthTexture;
	Texture staminaTexture;
	DecimalFormat format;
	BitmapFont font = new BitmapFont();
	Clock clock;
	GameState gameState=GameState.instance;
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
	public OrthographicCamera getCamera(){
		return textCam;
		
	}
	public void render(ShapeRenderer sr){
		//int i;
		sr.setProjectionMatrix(textCam.combined);
		Gdx.gl.glLineWidth(10);
		gameState.player.inventory.render(sr);
		//gameState.player.inventory.render(sr);
		/*for(i=0;i<gameState.player.inventory.inventory.length;i++){
			InventoryItem item=gameState.player.inventory.inventory[i];
			if(item!=null)
				sr.setColor(Color.GREEN);
			else sr.setColor(Color.RED);
			//TODO square around the selected item
			if(true){
				Vector2 pos=new Vector2(i*55+300 ,textCam.viewportHeight/2-50);
				sr.rect(pos.x ,pos.y,50,50);
			}
		}*/
	}
	public void render(SpriteBatch sb){
		int i;
		Player player=gameState.player;
		

		for (i = 0; i < player.getHealth() / player.getMaxHealth() * 100f; i+=10) {
			sb.draw(healthTexture,i*2,textCam.viewportHeight/2-50,30,30);
		}
		
		for (i = 0; i < player.getStamina() / player.getMaxStamina() * 100f; i+=10) {
			sb.draw(staminaTexture,i*2,textCam.viewportHeight/2-80,12,15);
		}
		/*for(i=0;i<gameState.player.inventory.inventory.length;i++){
			InventoryItem item= gameState.player.inventory.inventory[i];
			if(item!=null)
				sb.draw(item.getTexture(), i*55+300 ,textCam.viewportHeight/2-50,50,50);			
		}*/
	/*	for(i=0;i<gameState.player.inventory.inventory.length;i++){
			InventoryItem item= gameState.player.inventory.inventory[i];
			if(item!=null)
				sb.draw(item.getTexture(), i*55+300 ,textCam.viewportHeight/2-50,50,50);			
		}*/
		
								//TODO fix alignment
		font.draw(sb, "FPS/Delta: " + Gdx.graphics.getFramesPerSecond()+"/"+
				  format.format(Gdx.graphics.getRawDeltaTime()*100), -250, textCam.viewportHeight / 2);
		
		font.draw(sb, "Entity Number: " + gameState.entities.size(), -380, textCam.viewportHeight / 2);
		
		font.draw(sb,														//TODO make time work with night 5 sec delay
				"Time: " + /* format.format((PlayerLight.amlight*100)/8) */clock.amToTime(), -120,
				textCam.viewportHeight / 2);
		
		font.draw(sb, "noZombieMode: " + gameState.noZombie, 0, textCam.viewportHeight / 2);
		
		font.draw(sb, "noTimeMode: " + gameState.noTime, 150, textCam.viewportHeight / 2);
		
		font.draw(sb, "Player Position: " + format.format(player.getBody().getPosition().x) + ","
				+ format.format(player.getBody().getPosition().y), -600, textCam.viewportHeight / 2);
		Vector3 mousePos=gameState.getMouseCords(); 
		font.draw(sb, "Mouse Position: "+format.format(mousePos.x)+","+format.format(mousePos.y), -600, textCam.viewportHeight/2-20);
		gameState.player.inventory.render(sb);
		
	}
	
	public void dispose(){
		font.dispose();
	}
}
