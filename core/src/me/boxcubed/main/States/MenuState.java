package me.boxcubed.main.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.boxcubed.utils.Assets;

import me.boxcubed.main.TopDown;

/**
 * @author BoxCubed
 */
public class MenuState implements Screen {
    private Stage stage;
    private Skin skin;
    BitmapFont title_font;
    GlyphLayout title_layout;
    SpriteBatch batch;
    FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.absolute("fonts/menuFont.ttf"));
    FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
    public MenuState(GameState loadedInstance) {
        title_layout = new GlyphLayout();
        parameter.size = 80;
        parameter.color = Color.GREEN;
        parameter.shadowOffsetX = 10;
        parameter.shadowOffsetY = -10;
        title_font = generator.generateFont(parameter);



        title_layout.setText(title_font, "Top Down");

        batch = new SpriteBatch();

        skin = new Skin(Gdx.files.internal("uiskin.json"));
        stage = new Stage(new ScreenViewport());

        final TextButton button = new TextButton("Single Player", skin, "default");
        button.setWidth(300);
        button.setHeight(100);
        button.setX(Gdx.graphics.getWidth()/2 - button.getWidth()/2);
        button.setY(Gdx.graphics.getHeight()/2);
        button.setColor(Color.CORAL);

        button.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                TopDown.instance.setScreen(loadedInstance);
            }
        });
        stage.addActor(button);

        final TextButton button1 = new TextButton("Multi-Player", skin, "default");
        button1.setWidth(300);
        button1.setHeight(100);
        button1.setX(Gdx.graphics.getWidth()/2 - button.getWidth()/2);
        button1.setY(Gdx.graphics.getHeight()/3);
        button1.setColor(Color.CORAL);

        button1.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                TopDown.instance.setScreen(loadedInstance);
            }
        });
        stage.addActor(button1);

        Gdx.input.setInputProcessor(stage);
        ScrollingBG();
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

    	

    }

    @Override
    public void show() {
    	

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.1f, .12f, .16f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        updateANDrender(delta, batch);
        title_font.draw(batch, "Project TopDOWN", Gdx.graphics.getWidth()/4, Gdx.graphics.getHeight() - 100);
        batch.end();
        stage.act(delta);
        stage.draw();
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
