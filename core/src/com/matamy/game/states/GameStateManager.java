package com.matamy.game.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Stack;

/**
 * Created by matam on 27/05/2017.
 */

public class GameStateManager {
    private Stack<State> states;

    public GameStateManager(){
        states = new Stack<State>();
    }

    public void push(State state){
        states.push(state);
    }

    public void pop(){
        states.pop().dispose();
    }

    public void set(State state){
        states.pop().dispose();
        states.push(state);
    }

    public void update(float deltaTime){
        states.peek().update(deltaTime);
    }

    public void render(SpriteBatch spriteBatch){
        states.peek().render(spriteBatch);
    }
}
