package me.boxcubed.main.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.boxcubed.utils.MenuButton;
import com.boxcubed.utils.MenuListener;

import me.boxcubed.main.TopDown;

/**
 * @author BoxCubed
 */
public class MenuState implements State {
	Stage stage;
    Texture button;
    MenuButton clickButton;
    TextureAtlas start=new TextureAtlas("assets/button/start.atlas");
    Sprite hover=start.createSprite("hover"),click=start.createSprite("click"),normal=start.createSprite("normal");
    SpriteBatch batch=new SpriteBatch();
    //OLD  STUFF ^
    BitmapFont font;
    FreeTypeFontGenerator generator;
    FreeTypeFontParameter parameter;
    GlyphLayout startGlyph;
    public MenuState(GameState loadedInstance) {
        generator = new FreeTypeFontGenerator(Gdx.files.internal("assets/font.ttf"));
        parameter = new FreeTypeFontParameter();
        parameter.size = 48;
        font = generator.generateFont(parameter);
        font.setColor(Color.GREEN);
        startGlyph = new GlyphLayout();
        startGlyph.setText(font, "Start");
     /*this.gsm=gsm;*/
        Gdx.input.setInputProcessor(stage);
        
        button = new Texture("assets/img/icon.png");
        clickButton=new MenuButton(startGlyph,font,  Gdx.graphics.getWidth()/2 - startGlyph.width/2,  Gdx.graphics.getHeight()/2 - startGlyph.height/2, 
        		new MenuListener() {
			
			@Override
			public void rightclicked(MenuButton m) {
				System.out.println("RClicked!");
				
			}
			
			@Override
			public void notChosen(MenuButton m) {
				
				
			}
			
			@Override
			public void clicked(MenuButton m) {
				
				TopDown.instance.setScreen(loadedInstance);
				
			}
			
			@Override
			public void chosen(MenuButton m) {
				
			}
		});
        
        font.setColor(Color.BLUE);
        clickButton.setGlyphNotChosen(new GlyphLayout(font, "DONT TOUCH ME"));
        clickButton.setCollisionLock(true);
        clickButton.getRect().width=clickButton.f.width;
        clickButton.getRect().height=clickButton.f.height;
    }

    @Override
	public void handleInput() {

    }

    @Override
    public void update(float delta) {
    	clickButton.update(delta);
    	

    }

    @Override
    public void render() {
    	
    }

    @Override
    public void render(float delta) {
        //Dont ask me why. It looked cool
        //Lorenz attractor
        clickButton.update(delta);


        float dt = 0.01f; //time basically.
        float dx = (a *(y-x))*dt;
        float dy = (x *(b-z) - y)*dt;
        float dz = (x*y - c*z)*dt;
        x = x + dx;
        y = y + dy;
        z = z + dz;
        batch.begin();
        //renderer.translate(20, 12, 2);
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
        batch.dispose();
        generator.dispose();
    }
  
  
   
}
