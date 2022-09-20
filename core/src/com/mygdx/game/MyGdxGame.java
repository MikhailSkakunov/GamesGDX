package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

public class MyGdxGame extends ApplicationAdapter {
	private SpriteBatch batch;

	private ShapeRenderer shapeRenderer;
	private MyAtlasAnim tmpA, run, stand, jump;
	private Texture img, png;

	private Music music;
	private Sound sound;

	private MyInputProcessor myInputProcessor;

	private OrthographicCamera camera;
	private float x, y;

	private int dir = 0, step = 1;
	private Rectangle rectangle, window;

	private PhisX phisX;

	private Body body;
	private TiledMap map;
	private OrthogonalTiledMapRenderer mapRenderer;

	@Override
	public void create () {
		map = new TmxMapLoader().load("map/безымянный.tmx");
		mapRenderer = new OrthogonalTiledMapRenderer(map);

		phisX = new PhisX();

		BodyDef def = new BodyDef();
		FixtureDef fDef = new FixtureDef();
		PolygonShape shape = new PolygonShape();

//		def.gravityScale = 1;
		def.type = BodyDef.BodyType.StaticBody;
//		def.position.set(0, 0);

//		shape.setAsBox(250, 10);
		fDef.shape = shape;
		fDef.density = 1;
		fDef.friction = 0;
		fDef.restitution = 1;

		MapLayer env = map.getLayers().get("env");
		Array<RectangleMapObject> rect = env.getObjects().getByType(RectangleMapObject.class);
		for (int i = 0; i < rect.size; i++) {
			float x = rect.get(i).getRectangle().x;
			float y = rect.get(i).getRectangle().y;
			float w = rect.get(i).getRectangle().width/2;
			float h = rect.get(i).getRectangle().height/2;
			def.position.set(x, y);
			shape.setAsBox(w, h);
			phisX.world.createBody(def).createFixture(fDef).setUserData("Kubik");
		}


		def.type = BodyDef.BodyType.DynamicBody;
		def.gravityScale = 4;
		env = map.getLayers().get("dyn");
		rect = env.getObjects().getByType(RectangleMapObject.class);
		for (int i = 0; i < rect.size; i++) {
			float x = rect.get(i).getRectangle().x;
			float y = rect.get(i).getRectangle().y;
			float w = rect.get(i).getRectangle().width/2;
			float h = rect.get(i).getRectangle().height/2;
			def.position.set(x, y);
			shape.setAsBox(w, h);
			fDef.shape = shape;
			fDef.density = 1;
			fDef.friction = 0;
			fDef.restitution = 1;
			phisX.world.createBody(def).createFixture(fDef).setUserData("Kubik");
		}

		env = map.getLayers().get("hero");
		RectangleMapObject hero = (RectangleMapObject) env.getObjects().get("hero");
			float x = hero.getRectangle().x;
			float y = hero.getRectangle().y;
			float w = hero.getRectangle().width/2;
			float h = hero.getRectangle().height/2;
			def.position.set(x, y);
			shape.setAsBox(w, h);
			fDef.shape = shape;
			fDef.density = 1;
			fDef.friction = 0;
			fDef.restitution = 1;
		body = phisX.world.createBody(def);
		body.createFixture(fDef).setUserData("Kubik");


		shape.dispose();

		shapeRenderer = new ShapeRenderer();
		rectangle = new Rectangle();
		window = new Rectangle(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		myInputProcessor = new MyInputProcessor();
		Gdx.input.setInputProcessor(myInputProcessor);

		music = Gdx.audio.newMusic(Gdx.files.internal("48bb90af8e1e401.mp3"));
		music.setVolume(0.25f);
		music.setLooping(true);
		music.play();

		sound = Gdx.audio.newSound(Gdx.files.internal("Sound_16588.mp3"));


		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		run = new MyAtlasAnim("atlas/unnamed.atlas", "mario_run", 10, true, "assets_single_on_dirty_stone_step_flip_flop_007_30443.mp3");
		stand = new MyAtlasAnim("atlas/unnamed.atlas", "mario_stand", 10, false, "assets_single_on_dirty_stone_step_flip_flop_007_30443.mp3");
		jump = new MyAtlasAnim("atlas/unnamed.atlas", "mario_jump", 10, true, "assets_single_on_dirty_stone_step_flip_flop_007_30443.mp3");

		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

	}

	@Override
	public void render () {
		ScreenUtils.clear(Color.BLACK);

		camera.position.x = body.getPosition().x;
		camera.position.y = body.getPosition().y;
		camera.zoom = 1;
		camera.update();

		mapRenderer.setView(camera);
		mapRenderer.render();

		tmpA = stand;
		dir = 0;

		if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) sound.play(1, 1, 1);



		if(myInputProcessor.getOutString().contains("Left")) {
			dir = -1;
			tmpA = run;
			body.applyForceToCenter(new Vector2(-10000, 0f), true);

		}
		if(myInputProcessor.getOutString().contains("Right")) {
			dir = 1;
			tmpA = run;
			body.applyForceToCenter(new Vector2(10000, 0f), true);

		}
		if(myInputProcessor.getOutString().contains("Up")) { y++;
		tmpA = jump;}
		if(myInputProcessor.getOutString().contains("Down")) { y--; }
		if(myInputProcessor.getOutString().contains("Space")) {
//			x = Gdx.graphics.getWidth()/2;
//			y = Gdx.graphics.getHeight()/2;
			body.applyForceToCenter(new Vector2(0, 100000f), true);
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

		float x = body.getPosition().x - 2.5f;
		float y = body.getPosition().y - 2.5f;

		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(tmpA.draw(), body.getPosition().x, body.getPosition().y);
		batch.end();

		shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
		shapeRenderer.setColor(Color.BLACK);
		shapeRenderer.rect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
		shapeRenderer.end();


		window.overlaps(rectangle);
		if (!window.contains(rectangle)) Gdx.graphics.setTitle("Out");
		else Gdx.graphics.setTitle("In");

		phisX.step();
		phisX.debugDraw(camera);
	}

	@Override
	public void resize(int width, int height) {
		camera.viewportHeight = height;
		camera.viewportWidth = width;
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		tmpA.dispose();
		img.dispose();
		stand.dispose();
		music.dispose();
		sound.dispose();
		map.dispose();
		mapRenderer.dispose();

	}
}
