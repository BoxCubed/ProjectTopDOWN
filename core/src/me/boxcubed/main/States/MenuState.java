package me.boxcubed.main.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.boxcubed.net.NetworkManager;
import com.boxcubed.net.NetworkManager.ConnectionState;
import com.boxcubed.utils.Assets;
import com.boxcubed.utils.BoxoUtil;
import com.boxcubed.utils.MenuButton;
import com.boxcubed.utils.MenuListener;
import com.boxcubed.utils.ParallaxBackground;
import com.boxcubed.utils.ParallaxLayer;
import com.boxcubed.utils.easinglib.Bounce;

import me.boxcubed.main.TopDown;

/**
 * @author BoxCubed
 */
public class MenuState implements Screen {
	private Stage stage;
    private MenuButton clickButton;
    private MenuButton multiplayerButton;
    private TextField ipField,nameField;
    //OLD STUFF v
    /*TextureAtlas start=TopDown.assets.get(Assets.startATLAS, TextureAtlas.class);;
    Sprite hover=start.createSprite("hover"),click=start.createSprite("click"),normal=start.createSprite("normal");*/
  //OLD  STUFF ^
    SpriteBatch batch;
    
    BitmapFont font;
    GameState loadedInstance;
    GlyphLayout startGlyph,multiGlyph;
    ParallaxBackground bg;
    NetworkManager connection;
    boolean clicked=false;
    float elapsedTime=0;
    //TODO Thread safe startup
    private boolean init=false;
    public MenuState(GameState loadedInstance) {
       
    	this.loadedInstance=loadedInstance;
    }

	private void init(GameState loadedInstance) {
		//Init of debug shape rendrer
		  batch=new SpriteBatch();
        //init of button
		
        initButton(loadedInstance);
      
        
        
        //Stage setup
        stage=new Stage();
        ipField=new TextField("localhost:22222", TopDown.assets.get(Assets.menuSKIN, Skin.class));
        nameField=new TextField("BoxCubed", TopDown.assets.get(Assets.menuSKIN, Skin.class));
        BoxoUtil.addInputProcessor(stage);
        //ip field
        ipField.setPosition(Gdx.graphics.getWidth()/2-150,Gdx.graphics.getHeight()/2-150);
        ipField.setSize(300, 30);
        stage.addActor(ipField);
        //name field
        nameField.setPosition(Gdx.graphics.getWidth()/2-150,Gdx.graphics.getHeight()/2-200);
        stage.addActor(nameField);
        nameField.setSize(300, 30);
        
        //Background setup
        TextureRegion bgRegion=new TextureRegion(TopDown.assets.get(Assets.scrollMenuIMAGE, Texture.class));
        bg=new ParallaxBackground(new ParallaxLayer[]{
        		new ParallaxLayer(bgRegion, new Vector2(0,100),new Vector2(), new Vector2())
        }, (float)Gdx.graphics.getWidth()/2, (float)Gdx.graphics.getHeight()/2, new Vector2(0, 1));
		init=true;
	}

	public void handleInput() {

    }

    public void update(float delta) {
    	if(!init)init(loadedInstance);
    	
    	stage.act();
    	elapsedTime+=delta;
    	clickButton.update(delta);
    	multiplayerButton.update(delta);
    	if(elapsedTime>2 )return;
    	clickButton.setY(Bounce.easeOut(elapsedTime, 0, Gdx.graphics.getHeight()/2, 2));
    	multiplayerButton.setY(Bounce.easeOut(elapsedTime, 0, Gdx.graphics.getHeight()/2-100, 2));
    	
    	

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
				loadedInstance.newPlayer(0);
				
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
        multiGlyph=new GlyphLayout(font, "Start Multiplayer");
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
						/*if(loadedInstance.player.connection!=null&&loadedInstance.player.connection.state.equals(ConnectionState.CONNECTED)&&
								loadedInst){
							TopDown.instance.setScreen(loadedInstance);
							return;
						}*/
						
						if(connection==null){
							connection=new NetworkManager(loadedInstance.player,ipField.getText());
							connection.name=nameField.getText();
							
						}else if(connection.state.equals(ConnectionState.CONNECTING))
							return;
						else if(connection.state.equals(ConnectionState.CONNECTED)){
							loadedInstance.player.setConnection(connection, 1);
							TopDown.instance.setScreen(loadedInstance);}
							
					
						else if(connection.state.equals(ConnectionState.INVALIDIP)||connection.state.equals(ConnectionState.DISCONNECTED)){
							connection=new NetworkManager(loadedInstance.player,ipField.getText());
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
    	//TODO CAMERA

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
    	if(init)
    	BoxoUtil.remInputProcessor(stage);
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
  
  
   
}
