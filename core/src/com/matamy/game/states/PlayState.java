package com.matamy.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.matamy.game.FlappyDemo;
import com.matamy.game.sprites.Bird;
import com.matamy.game.sprites.Tube;

/**
 * Created by matam on 27/05/2017.
 */

public class PlayState extends State {
    private static final int TUBE_SPACING = 125;
    private static final int TUBE_COUNT = 4;
    private static final int GROUND_Y_OFFSET = -30;

    private Bird bird;
    private Texture background;
    private Texture ground;
    private Vector2 groundPos1,groundPos2;


    private Array<Tube> tubes;

    public PlayState(GameStateManager gameStateManager) {
        super(gameStateManager);
        bird = new Bird(50, 300);
        camera.setToOrtho(false, FlappyDemo.WIDTH/2, FlappyDemo.HEIGHT/2);
        background = new Texture("bg.png");
        ground = new Texture("ground.png");
        groundPos1 = new Vector2(camera.position.x - camera.viewportWidth/2, GROUND_Y_OFFSET);
        groundPos2 = new Vector2((camera.position.x - camera.viewportWidth/2)+ ground.getWidth(),GROUND_Y_OFFSET);

        tubes = new Array<Tube>();
        for(int i = 1; i <= TUBE_COUNT; i++){
            tubes.add(new Tube(i * (TUBE_SPACING + Tube.TUBE_WIDTH)));
        }
    }

    @Override
    public void handleInput() {
        if(Gdx.input.justTouched()){
            bird.jump();
        }
    }

    @Override
    public void update(float deltaTime) {
        handleInput();
        updateGround();
        bird.update(deltaTime);
        camera.position.x = bird.getPosition().x + 80;

        //Moving tube that are off screen to the beginning of the screen (end/left to begin/right)
        for(Tube tube : tubes){
            if(camera.position.x - (camera.viewportWidth/2) > tube.getPosTopTube().x + tube.getTopTube().getWidth()){
                tube.reposition(tube.getPosTopTube().x + (Tube.TUBE_WIDTH + TUBE_SPACING)* TUBE_COUNT);
            }

            if(tube.collides(bird.getBounds())){
                gameStateManager.set(new PlayState(gameStateManager));
                break;
            }
        }
        if(bird.getPosition().y <= ground.getHeight() + GROUND_Y_OFFSET)
            gameStateManager.set(new PlayState(gameStateManager));
        //anytime we apply changes to our camera we have to update it
        camera.update();
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        //Drawing the background where the camera is
        spriteBatch.draw(background, camera.position.x - (camera.viewportWidth/2),0);

        spriteBatch.draw(bird.getTexture(),bird.getPosition().x,bird.getPosition().y);
        for(Tube tube: tubes){
            spriteBatch.draw(tube.getTopTube(), tube.getPosTopTube().x, tube.getPosTopTube().y);
            spriteBatch.draw(tube.getBottomTube(), tube.getPosBotTube().x, tube.getPosBotTube().y);
        }
        spriteBatch.draw(ground, groundPos1.x,groundPos1.y);
        spriteBatch.draw(ground, groundPos2.x,groundPos2.y);
        spriteBatch.end();
    }

    @Override
    public void dispose() {
        background.dispose();
        bird.dispose();
        ground.dispose();
        for(Tube tube : tubes){
            tube.dispose();
        }
    }

    private void updateGround(){
        if(camera.position.x - (camera.viewportWidth/2) > groundPos1.x + ground.getWidth())
            groundPos1.add(ground.getWidth()*2, 0);
        if(camera.position.x - (camera.viewportWidth/2) > groundPos2.x + ground.getWidth())
            groundPos2.add(ground.getWidth()*2, 0);
    }
}
