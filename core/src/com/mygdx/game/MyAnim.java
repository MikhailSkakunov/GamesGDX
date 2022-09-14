package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class MyAnim {

    Texture png;
    Animation<TextureRegion> animation;
    private float time;

    public MyAnim(String name, int row, int col, float fps, Animation.PlayMode playMode) {
        time = 0;
        png = new Texture(name);
        TextureRegion reg1 = new TextureRegion(png);
        TextureRegion[][] regions = reg1.split(png.getWidth()/col, png.getHeight()/row);
        TextureRegion[] tmp = new TextureRegion[regions.length * regions[0].length];
        int count = 0;
        for (int i = 0; i < regions.length; i++) {
            for (int j = 0; j < regions[0].length; j++) {
                tmp[count++] = regions[i][j];
            }
        }
        animation = new Animation<>(1/fps, tmp);
        animation.setPlayMode(playMode);

    }

    public TextureRegion draw() {
        return  animation.getKeyFrame(time);
    }

    public void  setTime(float dt) {
        time += dt;
    }

    public void dispose() {
        this.png.dispose();
    }
}
