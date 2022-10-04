package com.mygdx.game;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.persons.Man;

public class MyInputProcessor implements InputProcessor {

    private final Vector2 outForce;

    public MyInputProcessor() {
        outForce = new Vector2();
    }

    public Vector2 getVector() {
        return outForce;
    }

    @Override
    public boolean keyDown(int keycode) {
        String outString = Input.Keys.toString(keycode);
        switch (outString)
        {
            case "Left": outForce.add(-0.0075f, 0); break;
            case "Right": outForce.add(0.0075f, 0); break;
            case "Up":outForce.add(0, 0.15f); break;
            case "Space": Man.isFire = true; break;
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        String outString = Input.Keys.toString(keycode);
        switch (outString) {
            case "Left": outForce.set(0, outForce.y); break;
            case "Right": outForce.set(0, outForce.y); break;
            case "Down": outForce.set(outForce.x, 0); break;
            case "Space": outForce.set(outForce.x, 0); break;
        }
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
