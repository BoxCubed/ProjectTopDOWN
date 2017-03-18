package me.boxcubed.main.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.boxcubed.utils.CleanInputProcessor;
import com.boxcubed.utils.MenuButton;
import com.boxcubed.utils.MenuListener;

import me.boxcubed.main.TopDown;

/**
 * Created by Tej Sidhu on 23/02/2017.
 */
public class MenuState implements State, CleanInputProcessor {
 //LOL//
	Stage stage;
    Texture button;
    MenuButton clickButton;
    private static final int buttonXNY =200;
    TextureAtlas start=new TextureAtlas("assets/button/start.atlas");
    Sprite hover,click,normal;
   
    public MenuState() {
     /*this.gsm=gsm;*/
        Gdx.input.setInputProcessor(stage);
        Gdx.input.setInputProcessor(this);
        hover=start.createSprite("hover");
        click=start.createSprite("click");
        normal=start.createSprite("normal");
        
        button = new Texture("assets/img/icon.png");
        clickButton=new MenuButton(normal, Gdx.graphics.getWidth()/2-start.createSprite("normal").getWidth()/2, Gdx.graphics.getHeight()/2, new MenuListener() {
			
			@Override
			public void rightclicked(MenuButton m) {
				// TODO Auto-generated method stub
				System.out.println("RClicked!");
				
			}
			
			@Override
			public void notChosen(MenuButton m) {
				// TODO Auto-generated method stub
				m.setImage(normal);
				
				
			}
			
			@Override
			public void clicked(MenuButton m) {
				// TODO Auto-generated method stub
				m.setImage(click);
				
				TopDown.instance.setScreen(new GameState());
				
			}
			
			@Override
			public void chosen(MenuButton m) {
				// TODO Auto-generated method stub
				m.setImage(hover);
				
			}
		});
    }

    @Override
	public void handleInput() {

    }

    @Override
    public void update(float delta) {
    	clickButton.update(delta);

    }

    @Override
    public void render(SpriteBatch batch) {
    	
        batch.begin();
        //batch.draw(button, Gdx.graphics.getWidth()/2 -buttonXNY/2, Gdx.graphics.getHeight()/2 - buttonXNY/2, buttonXNY, buttonXNY);
        clickButton.render(batch);
        batch.end();
    }

    @Override
    public void show() {

    }

    

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        this.dispose();
    }
  
  
   
}
