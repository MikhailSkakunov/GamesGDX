package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img, png;

	MyAnim anim;

	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		png = new Texture("man.jpg");
		anim = new MyAnim("man.jpg", 4, 10, 10, Animation.PlayMode.LOOP);
	}

	@Override
	public void render () {
		ScreenUtils.clear(1, 1, 1, 1);

		anim.setTime(Gdx.graphics.getDeltaTime());

		long x = Gdx.input.getX() - anim.draw().getRegionWidth()/2;
		long y = Gdx.graphics.getHeight() - Gdx.input.getY() - anim.draw().getRegionHeight()/2;

		batch.begin();
		batch.draw(anim.draw(), x, y);
//		batch.draw(img, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//		batch.draw(png, x, y,
//				png.getHeight()/2,
//				png.getHeight()/2,
//				png.getWidth(),
//				png.getHeight(),
//				1, 1, 0, 0, 0,
//				png.getWidth(),
//				png.getHeight(),
//				false, false);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
		anim.dispose();
	}
}
