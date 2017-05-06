package com.boxcubed.utils;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

/**
 * A simple Menu Button 
 * Works with LibGDX and requires update and render methods to be called
 * every loop
 * @author ryan9
 * 
 *
 */
public class MenuButton{
	float x,y;
	boolean chosen,lock=false;
	private int mx,my;//mouse x and y
	Sprite i,ci;
	Rectangle r;
	public GlyphLayout f,cf;
	public BitmapFont font,chosenFont;
	Color col=Color.WHITE,ccol=Color.WHITE;
	Camera cam;
	boolean useImage(){
		if(i==null)return false;
				return true;
	}
	MenuListener l=null;
	public MenuButton(Sprite i,float x,float y,MenuListener l){
		this.i=i;
		this.l=l;
		this.x=x;
		this.y=y;
		r=new Rectangle(x,y,i.getWidth(),i.getHeight());
		
		
		
	}
	public MenuButton(Sprite i,Sprite ci,float x,float y,MenuListener l){
		this.i=i;
		this.l=l;
		this.x=x;
		this.y=y;
		this.ci=ci;
		r=new Rectangle(x, y, i.getWidth(), i.getHeight());
		
		
	}
public 	MenuButton(GlyphLayout i,BitmapFont f,float x,float y,MenuListener l){
		this(i,i,f,f,x,y,l);
		
		
	}
	public MenuButton(GlyphLayout f,GlyphLayout cf,BitmapFont c,BitmapFont cc,float x,float y,MenuListener l){
		this.f=f;
		font=c;
		chosenFont=cc;
		this.x=x;
		this.y=y;
		this.cf=cf;
		this.l=l;
		r=new Rectangle(x, y, 5, 10);
		
	}
	
	public void setCam(Camera cam){
		this.cam=cam;
	}
	/**
	 * Call when updating
	 * @param gc
	 * provide delta
	 */
	
	boolean debug=true;
	ShapeRenderer sr=new ShapeRenderer();
	public void update(float delta){
		if(!isLocked())
		setCollisionBounds();
		Input in=Gdx.input;
		
		mx=in.getX();
		//my=in.getY()*-1+Gdx.graphics.getHeight();
		my=in.getY();
		Vector3 mouse=cam.unproject(new Vector3(mx, my, 0));
		sr.begin(ShapeType.Line);
		sr.setColor(Color.GREEN);
		sr.rect(x, y, r.getWidth(), r.getHeight());
		sr.rect(mx-5, my-5, 10, 10);
		sr.end();
		
		//System.out.println(mx+","+my);
		
		if(r.contains(mouse.x, mouse.y)){
			chosen=true;
		l.chosen(this);}
		
		else {chosen=false;
		l.notChosen(this);}
		
		if(chosen&&in.isButtonPressed(0))
			l.clicked(this);
		if(chosen&&in.isButtonPressed(1))
			l.rightclicked(this);
		
		
		
		
	}
	/**
	 * Call when rendering
	 * @param g
	 * provide  Sprite Batch
	 */
	public void render(SpriteBatch batch){
		if(useImage()){
			
			
			if(!chosen || (chosen&&ci==null))
				batch.draw(i, x, y);
			else if(ci!=null)
				batch.draw(ci, x, y);
			else throw new NullPointerException("Could not draw Button Image");
			
		}else{
			if(chosen){
				chosenFont.setColor(ccol);
				chosenFont.draw(batch, cf, x, y+cf.height);	
			
			}
			else {
				font.setColor(col);
				font.draw(batch, f, x, y+f.height);}
			
			
		}
		
		
		
		
		
	}
	public void setX(float x){this.x=x;}
	public void setY(float y){this.y=y;}
	public void setGlyphChosen(GlyphLayout s){this.cf=s;}
	public void setGlyphNotChosen(GlyphLayout s){this.f=s;}
	/**
	 * Should be used to detect collisions without the use of the mouse
	 * @return
	 * the rectangle the menu button is enclosed in
	 */
	public Rectangle getRect(){return r;}
	public void setColor(Color c){this.col=c; }
	public void setChosenColor(Color c){this.ccol=c;}
	public boolean isChosen(){return chosen;}
	public void addListener(MenuListener l){this.l=l;}
	/**
	 *Sets an image to the button
	 *Note: 
	 * If object was created with a string, setting this will stop rendering a string and render an image instead
	 * to change back just set this to null. rendering both isn't possible...yet
	 * 
	 * 
	 */
	public void setImage(Sprite i){this.i=i;}
	public void setChosenImage(Sprite i){ci=i;}
	public void setChosen(boolean c){chosen=c;}
	public float getY(){return y;}
	public float getX(){return x;}
	/**
	 * Allows to lock the collision box so it is not moved with the image/text
	 * Should be used if a custom one is to be made with {@code getRect()}
	 */
	public  void setCollisionLock(boolean lock){this.lock=lock;}
	public boolean isLocked(){return lock;}
	public MenuButton getInstance(){return this;}
	/**
	 * Uses a linear equation in order to space the button evenly with the others. Only edits the Y value
	 * @param number 
	 * The order of the button from top down. (Assuming you are using an array so 0 should be the first)
	 * @param spacing the amount to space the button from the previous not neccesaraly in pixels
	 * @param startPos the y position of the first button. Should be universal throughout your list.
	 */
	public void orderButton(int number,int spacing, int startPos){
		setY(number*spacing+startPos);
		setCollisionBounds();
		
	}
	private void setCollisionBounds(){
		if(useImage())
		r.set(x, y, i.getWidth(), i.getHeight());
		else if(!isChosen()) r.set(x, y, f.width, f.height);
		else r.set(x, y, cf.width,cf.height);
		}


}
