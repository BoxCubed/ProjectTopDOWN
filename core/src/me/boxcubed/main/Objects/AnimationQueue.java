package me.boxcubed.main.Objects;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
/**
 * @deprecated I HATE MY LIFE
 * @author ryan9
 *
 */
public class AnimationQueue {
	public List<AnimationItem> queue;
	public AnimationQueue(){
		queue=new ArrayList<>();
	}
	public void render(SpriteBatch sb){
		
		for(int i=0;i<queue.size();){
			AnimationItem anim=queue.get(i);
			if(anim.anim.isAnimationFinished(anim.watch.getElapsedTimeSecs())){queue.remove(anim);continue;}
			sb.draw(anim.anim.getKeyFrame(anim.watch.getElapsedTimeSecs(), false), anim.vect.x, anim.vect.y);
			i++;
			
			
		}
	}
	public void addAnim(Animation<TextureRegion> anim,Vector2 vect){
		queue.add(new AnimationItem(anim,vect));
	}
private class AnimationItem{
	public StopWatch watch;
	public Animation<TextureRegion> anim;
	public Vector2 vect;
	public AnimationItem(Animation<TextureRegion> anim,Vector2 vect) {
		watch=new StopWatch();
		watch.start();
		this.anim=anim;
		this.vect=vect;
	}
}	
}
