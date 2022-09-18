package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;

public class MyGdxGame extends ApplicationAdapter {
	private SpriteBatch batch;

	private ShapeRenderer shapeRenderer;
	private MyAtlasAnim tmpA, run, stand, jump;
	private Texture img, png;

	private Music music;
	private Sound sound;

	MyInputProcessor myInputProcessor;
	private float x, y;

	private Rectangle rectangle, window;
	private int dir = 0, step = 1;

	@Override
	public void create () {
		shapeRenderer = new ShapeRenderer();
		rectangle = new Rectangle();
		window = new Rectangle(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
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
		run = new MyAtlasAnim("atlas/unnamed.atlas", "mario_run", 10, Animation.PlayMode.LOOP);
		stand = new MyAtlasAnim("atlas/unnamed.atlas", "mario_stand", 10, Animation.PlayMode.LOOP);
		jump = new MyAtlasAnim("atlas/unnamed.atlas", "mario_jump", 10, Animation.PlayMode.LOOP);

	}

	@Override
	public void render () {
		ScreenUtils.clear(1, 1, 1, 1);

		tmpA = stand;
		dir = 0;

//		long x = Gdx.input.getX() - anim.draw().getRegionWidth()/2;
//		long y = Gdx.graphics.getHeight() - Gdx.input.getY() - anim.draw().getRegionHeight()/8;

		if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) sound.play(1, 1, 1);



		if(myInputProcessor.getOutString().contains("Left")) {
			dir = -1;
			tmpA = run;
		}
		if(myInputProcessor.getOutString().contains("Right")) {
			dir = 1;
			tmpA = run;
		}
		if(myInputProcessor.getOutString().contains("Up")) y++;
		tmpA = jump;
		if(myInputProcessor.getOutString().contains("Down")) y--;
		if(myInputProcessor.getOutString().contains("Space")) {
			x = Gdx.graphics.getWidth()/2;
			y = Gdx.graphics.getHeight()/2;
		}

		if (dir == -1) x-=step;
		if (dir == 1) x+=step;

		tmpA.setTime(Gdx.graphics.getDeltaTime());
		TextureRegion tmp = tmpA.draw();
		if (!tmpA.draw().isFlipX() && dir == -1) tmpA.draw().flip(true, false);
		if (tmpA.draw().isFlipX() && dir == 1) tmpA.draw().flip(true, false);

		rectangle.x = x;
		rectangle.y = y;
		rectangle.width = tmp.getRegionWidth();
		rectangle.height = tmp.getRegionHeight();

		System.out.println(myInputProcessor.getOutString());

		batch.begin();
		batch.draw(tmpA.draw(), x, y);
//		batch.draw(img, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//		batch.draw(png, x, y, png.getHeight()/2, png.getHeight()/2,	png.getWidth(), png.getHeight(), 1, 1, 0, 0, 0,
//		png.getWidth(), png.getHeight(), false, false);
		batch.end();

		shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
		shapeRenderer.setColor(Color.BLACK);
		shapeRenderer.rect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
		shapeRenderer.end();


		window.overlaps(rectangle);
		if (!window.contains(rectangle)) Gdx.graphics.setTitle("Out");
		else Gdx.graphics.setTitle("In");
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		tmpA.dispose();
		music.dispose();
		sound.dispose();

	}
}
