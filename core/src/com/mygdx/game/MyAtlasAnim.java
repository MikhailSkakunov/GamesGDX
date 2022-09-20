package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class MyAtlasAnim {
    TextureAtlas atlas;
    Animation<TextureAtlas.AtlasRegion> animation;
    private float time;
    private boolean loop;

    private Sound sound;

    private float d;

    public MyAtlasAnim(String atlas, String name, float fps, boolean playMode, String sound) {
        if (playMode) loop = true;
        this.sound = Gdx.audio.newSound(Gdx.files.internal(sound));
        this.sound.play();
        time = 0;
        this.atlas = new TextureAtlas(atlas);
        animation = new Animation<>(1/fps, this.atlas.findRegion(name));
        animation.setPlayMode(Animation.PlayMode.NORMAL);
        d = animation.getAnimationDuration()/2;

    }

    public TextureRegion draw() {
        return  animation.getKeyFrame(time);
    }

    public void  setTime(float dt) {
        time += dt;
        if (time > d && time < animation.getAnimationDuration()) {
            sound.play();
            d *= 2;
        } else if (time >= animation.getAnimationDuration() && loop) {
            time = 0;
            d = animation.getAnimationDuration() / 2;
            sound.play();
        }
    }

    public void dispose() {
        this.atlas.dispose();
        sound.dispose();
    }
}
