package me.boxcubed.main.States;
/**
 * Created by Tej Sidhu on 9/02/2017.
 */
import java.util.Stack;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
/**
 * 
 * @deprecated no use 
 *
 */
public class GameStateManager {
    private Stack<State> states;//This holds an array of all the states such as the main menu, play and game over state



    public GameStateManager(){

        states= new Stack<State>();

    }

    public void push(State state){

        states.push(state);

    }//This changes the state to the one needed

    public void pop(){

        states.pop().dispose();

    } //This gets rid of the previous state or any of the ones that need to be disposed.

    public void set(State state){ //This sets the new state and gets rid of the old one i4f there was any
    	state.show();
        states.pop().dispose();
        
        states.push(state);

    }

    public void update(float delta){

        states.peek().update(delta);

    }//Updates all the states

    public void render(SpriteBatch batch){

        states.peek().render();

    }//Renders all the states
}
