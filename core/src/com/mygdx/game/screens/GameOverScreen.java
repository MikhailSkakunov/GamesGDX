package com.mygdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Label;

public class GameOverScreen implements Screen {
    Game game;
    Texture fon;
    SpriteBatch batch;
    Label label;


    public GameOverScreen(Game game){
        this.game = game;
        fon = new Texture("fon.jpg");
        batch = new SpriteBatch();
        label = new Label(65);

//        Actor actor;
//        actor.addAction(new );
//        actor.setTouchable(Touchable.enabled);
//        Group group;
//        Stage stage;

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        batch.begin();

        batch.draw(fon, 0,0,Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        label.draw(batch, "ГамеОвер", 0, Gdx.graphics.getHeight()/2);
        batch.end();

        if (Gdx.input.isTouched()) {
            dispose();
            game.setScreen(new GameScreen(game));
        }
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
        this.fon.dispose();
        this.batch.dispose();
        this.label.dispose();
    }
}
