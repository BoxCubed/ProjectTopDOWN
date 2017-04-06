package me.boxcubed.main.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.boxcubed.net.ClientConnection;
import com.boxcubed.net.ClientConnection.ConnectionState;
import com.boxcubed.utils.Assets;
import com.boxcubed.utils.MenuButton;
import com.boxcubed.utils.MenuListener;
import com.boxcubed.utils.ParallaxBackground;
import com.boxcubed.utils.ParallaxLayer;

import me.boxcubed.main.TopDown;

/**
 * @author BoxCubed
 */
public class MenuState implements Screen {
	Stage stage;
    MenuButton clickButton;
    MenuButton multiplayerButton;
    TextField ipField;
    //OLD STUFF v
    TextureAtlas start=TopDown.assets.get(Assets.startATLAS, TextureAtlas.class);;
    Sprite hover=start.createSprite("hover"),click=start.createSprite("click"),normal=start.createSprite("normal");
    SpriteBatch batch=new SpriteBatch();
    //OLD  STUFF ^
    BitmapFont font;
    GlyphLayout startGlyph;
    ParallaxBackground bg;
    ShapeRenderer renderer;
    ClientConnection connection;
    boolean clicked=false;
    public MenuState(GameState loadedInstance) {
    	//Init of debug shape rendrer
        renderer = new ShapeRenderer();
        //init of button
        initButton(loadedInstance);
        
        
        
        //Stage setup
        stage=new Stage();
        ipField=new TextField("localhost:22222", TopDown.assets.get(Assets.menuSKIN, Skin.class));
        Gdx.input.setInputProcessor(stage);
        ipField.setPosition(Gdx.graphics.getWidth()/2-150,Gdx.graphics.getHeight()/2-150);
        ipField.setSize(300, 30);
        stage.addActor(ipField);
        //Background setup
        TextureRegion bgRegion=new TextureRegion(TopDown.assets.get(Assets.scrollMenuIMAGE, Texture.class));
        bg=new ParallaxBackground(new ParallaxLayer[]{
        		new ParallaxLayer(bgRegion, new Vector2(100,100),new Vector2(), new Vector2())
        }, (float)Gdx.graphics.getWidth()/2, (float)Gdx.graphics.getHeight()/2, new Vector2(0, 1));
       
    }

	public void handleInput() {

    }
    Texture backGround;
    float y1, y2;
    int speed; //This is the speed for the scrolling, it is in pixels/second
    int star_SPEED; //A certain speed that the stars should achieve
    public static final int DEFAULT_SPEED = 80;
    public static final int Acceleration = 50;
    public static final int star_SPEED_ACCELERATION = 200;
    float imageScalability;
    boolean isSpeedFixed;
    public void ScrollingBG(){
        backGround = new Texture(Gdx.files.internal("res_menu/background.jpg"));
        y1 = 0;
        y2 = backGround.getHeight();
        speed = 0;
        star_SPEED = DEFAULT_SPEED;
        imageScalability =Gdx.graphics.getWidth()/ backGround.getWidth();
        isSpeedFixed = true;
    }

    public void updateANDrender(float delta, SpriteBatch batch){
        //Adjustment of speed to reach star_SPEED
        if(speed < star_SPEED){
            speed+= delta * star_SPEED_ACCELERATION;
            if(speed > star_SPEED){
                speed = star_SPEED;
            }else if(speed > star_SPEED){
                speed-= delta * star_SPEED_ACCELERATION;
                if(speed < star_SPEED){
                    speed = star_SPEED;
                }
            }
        }

        if(!isSpeedFixed){
            speed += Acceleration * delta;
        }

        y1 -= delta * speed;
        y2 -= delta *speed;
        //if the background reaches the bottom of the game screen (& nt visible) then it puts it back on top of the image that it is under
        if(y1 + backGround.getHeight() <= 0){ // or if(y1 + backGround.getHeight() * imageScaleablity <= 0)
            y1 = y2 + backGround.getHeight() * imageScalability;
        }
        if(y2 + backGround.getHeight() <= 0){// or if(y1 + backGround.getHeight() * imageScaleablity <= 0). The current one works better
            y2 = y1 + backGround.getHeight() * imageScalability;
        }
        batch.draw(backGround, 0, y1, Gdx.graphics.getWidth(), backGround.getHeight() * imageScalability);
        batch.draw(backGround, 0, y2, Gdx.graphics.getWidth(), backGround.getHeight() * imageScalability);
    }

    public void set_Speeed(int star_SPEED){
        this.star_SPEED = star_SPEED;
    }
    public void set_SpeedFixed(boolean isSpeedFixed){
        this.isSpeedFixed = isSpeedFixed;
    }
    public void update(float delta) {
    	stage.act();
    	clickButton.update(delta);
    	multiplayerButton.update(delta);
    	

    }

    @Override
    public void show() {
    	

    }

    @Override
    public void render(float delta) {
       
    	update(delta);

        bg.render(delta);
        batch.begin();
        
        //renderer.translate(20, 12, 2);
        clickButton.render(batch);
        multiplayerButton.render(batch);

        batch.end();
        stage.draw();
    }

    private void initButton(GameState loadedInstance){
    	font = TopDown.assets.get(Assets.menuFONT, BitmapFont.class);
        //setting color for chosen
        font.setColor(Color.GREEN);
        //making glyph fir button
        startGlyph = new GlyphLayout();
        startGlyph.setText(font, "Start Singleplayer");
        //Making button with listener
        clickButton=new MenuButton(startGlyph,font,  Gdx.graphics.getWidth()/2 - startGlyph.width/2,  Gdx.graphics.getHeight()/2, 
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
				loadedInstance.player.state=0;
				if(loadedInstance.player.connection!=null){
					loadedInstance.player.connection.stop=true;
					try {
						loadedInstance.player.connection.join();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					loadedInstance.player.connection=null;
					
					}
				
                TopDown.instance.setScreen(loadedInstance);
			}
			@Override
			public void chosen(MenuButton m) {
				
			}
		});
        
        //making glyph for when button is not selected
        font.setColor(Color.RED);
        //assigning it
        clickButton.setGlyphChosen(new GlyphLayout(font, "Start Singleplayer"));
        //locking collision so the box won't change with font
        clickButton.setCollisionLock(true);
        //assigning the dimensions for collision since we locked it
        clickButton.getRect().width=clickButton.f.width;
        clickButton.getRect().height=clickButton.f.height;
        font.setColor(Color.GREEN);
        GlyphLayout multiGlyph=new GlyphLayout(font, "Start Multiplayer");
    	multiplayerButton=new MenuButton(multiGlyph, font, Gdx.graphics.getWidth()/2-multiGlyph.width/2, Gdx.graphics.getHeight()/2-100, 
    			new MenuListener() {
					
					@Override
					public void rightclicked(MenuButton m) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void notChosen(MenuButton m) {
						font.setColor(Color.GREEN);
						if(clicked&&connection!=null)
						multiGlyph.setText(font, connection.state.toString().toLowerCase());
						else 
							multiGlyph.setText(font, "Start Multiplayer");
						
					}
					
					@Override
					public void clicked(MenuButton m) {
						clicked=true;
						if(loadedInstance.player.connection!=null&&loadedInstance.player.connection.state.equals(ConnectionState.CONNECTED)){
							loadedInstance.player.setConnection(connection, 1);
							TopDown.instance.setScreen(loadedInstance);
							return;
						}
						
						if(connection==null){
							connection=new ClientConnection(loadedInstance.player,ipField.getText());
							
						}else if(connection.state.equals(ConnectionState.CONNECTING))
							return;
						else if(connection.state.equals(ConnectionState.CONNECTED)){
							loadedInstance.player.setConnection(connection, 1);
							TopDown.instance.setScreen(loadedInstance);}
							
					
						else if(connection.state.equals(ConnectionState.INVALIDIP)||connection.state.equals(ConnectionState.DISCONNECTED)){
							connection=new ClientConnection(loadedInstance.player,ipField.getText());
						}
						
					}
					
					@Override
					public void chosen(MenuButton m) {
						font.setColor(Color.RED);
						if(clicked&&connection!=null)
						multiGlyph.setText(font, connection.state.toString().toLowerCase());
						else 
							multiGlyph.setText(font, "Start Multiplayer");
							
						
					}
				});
    	
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
        generator.dispose();
    }
  
  
   
}
