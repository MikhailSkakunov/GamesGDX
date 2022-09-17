package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class MyGdxGame extends ApplicationAdapter {
	private SpriteBatch batch;
	private MyAtlasAnim anim, run, stand;
	private Texture img, png;

	private Music music;
	private Sound sound;

	MyInputProcessor myInputProcessor;
	private float x, y;

	@Override
	public void create () {
		myInputProcessor = new MyInputProcessor();
		Gdx.input.setInputProcessor(myInputProcessor);

		music = Gdx.audio.newMusic(Gdx.files.internal("48bb90af8e1e401.mp3"));
		music.setVolume(0.5f);
//		music.setPan(0, 1);
		music.setLooping(true);
		music.play();

		sound = Gdx.audio.newSound(Gdx.files.internal("Sound_16588.mp3"));


		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		run = new MyAtlasAnim("atlas/marioGame.atlas", "mario_run_r", 10, Animation.PlayMode.LOOP);
		stand = new MyAtlasAnim("atlas/marioGame.atlas", "mario_stand", 10, Animation.PlayMode.LOOP);
		anim = run;
	}

	@Override
	public void render () {
		ScreenUtils.clear(1, 1, 1, 1);

		anim.setTime(Gdx.graphics.getDeltaTime());

//		long x = Gdx.input.getX() - anim.draw().getRegionWidth()/2;
//		long y = Gdx.graphics.getHeight() - Gdx.input.getY() - anim.draw().getRegionHeight()/8;

		if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) sound.play(1, 1, 1);



		if(myInputProcessor.getOutString().contains("Left")) x--;
		if(myInputProcessor.getOutString().contains("Right")) x++;
		if(myInputProcessor.getOutString().contains("Up")) y++;
		if(myInputProcessor.getOutString().contains("Down")) y--;
		if(myInputProcessor.getOutString().contains("Space")) {
			x = Gdx.graphics.getWidth()/2;
			y = Gdx.graphics.getHeight()/2;
		}


		System.out.println(myInputProcessor.getOutString());

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
		anim.dispose();
		music.dispose();
		sound.dispose();
	}
}
