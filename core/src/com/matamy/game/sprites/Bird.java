package com.matamy.game.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by matam on 27/05/2017.
 */

public class Bird {
    private static final int GRAVITY = -15;
    private static final int MOVEMENT = 100;
    private Vector3 position;
    private Vector3 velocity;
    private Rectangle bounds;
    private Animation birdAnimation;
    private Texture texture;
    private Sound flap;

    public Bird(int x, int y){
        position = new Vector3(x,y,0);
        velocity = new Vector3(0,0,0);
        texture = new Texture("birdanimation.png");
        birdAnimation = new Animation(new TextureRegion(texture), 3, 0.5f);
        bounds = new Rectangle(x,y, texture.getWidth()/3, texture.getHeight());
        flap = Gdx.audio.newSound(Gdx.files.internal("sfx_wing.ogg"));
    }

    public void update(float deltaTime){
        birdAnimation.update(deltaTime);
        if(position.y > 0)
            velocity.add(0, GRAVITY, 0);

        velocity.scl(deltaTime);
        position.add(MOVEMENT * deltaTime, velocity.y, 0);

        if(position.y < 0)
            position.y = 0;

        //reverse what we did earlier so we can add it again on the next frame
        velocity.scl(1/deltaTime);

        bounds.setPosition(position.x,position.y);
    }

    public Vector3 getPosition() {
        return position;
    }

    public TextureRegion getTexture(){return birdAnimation.getFrame();}

    public void jump(){
        velocity.y = 250;
        flap.play(0.2f);
    }

    public Rectangle getBounds(){
        return bounds;
    }
    public void dispose(){
        texture.dispose();
        flap.dispose();
    }
}
