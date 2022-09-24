package com.mygdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.MyAtlasAnim;
import com.mygdx.game.MyInputProcessor;
import com.mygdx.game.PhisX;
import com.mygdx.game.enums.Actions;

import java.util.HashMap;

public class GameScreen implements Screen {

    Game game;
    private SpriteBatch batch;
    private Texture img;
    private HashMap<Actions, MyAtlasAnim> manAssetss;
    private Music music;
    private Sound sound;
    private MyInputProcessor myInputProcessor;
    private OrthographicCamera camera;
    private PhisX phisX;
    private Body body;
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;
    private Actions actions;
    private  int[] front, tL;

    public GameScreen(Game game) {
        this.game = game;
        map = new TmxMapLoader().load("map/безымянный.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map);

        front = new int[1];
        front[0] = map.getLayers().getIndex("front");
        tL = new int[1];
        tL[0] = map.getLayers().getIndex("t0");

        phisX = new PhisX();

        Array<RectangleMapObject> objects = map.getLayers().get("env").getObjects().getByType(RectangleMapObject.class);
        objects.addAll(map.getLayers().get("dyn").getObjects().getByType(RectangleMapObject.class));
        for (int i = 0; i < objects.size; i++) {
            phisX.addObject(objects.get(i));
        }

        body = phisX.addObject((RectangleMapObject) map.getLayers().get("hero").getObjects().get("hero"));
        body.setFixedRotation(true);

        myInputProcessor = new MyInputProcessor();
        Gdx.input.setInputProcessor(myInputProcessor);

        music = Gdx.audio.newMusic(Gdx.files.internal("48bb90af8e1e401.mp3"));
        music.setVolume(0.25f);
        music.setLooping(true);
        music.play();

        sound = Gdx.audio.newSound(Gdx.files.internal("Sound_16588.mp3"));


        batch = new SpriteBatch();
        img = new Texture("badlogic.jpg");

        manAssetss = new HashMap<>();
        manAssetss.put(Actions.STAND, new MyAtlasAnim("atlas/unnamed.atlas", "mario_stand", 7, false, "assets_single_on_dirty_stone_step_flip_flop_007_30443.mp3"));
        manAssetss.put(Actions.RUN, new MyAtlasAnim("atlas/unnamed.atlas", "mario_run", 7, true, "assets_single_on_dirty_stone_step_flip_flop_007_30443.mp3"));
        manAssetss.put(Actions.JUMP, new MyAtlasAnim("atlas/unnamed.atlas", "mario_jump", 7, true, "assets_single_on_dirty_stone_step_flip_flop_007_30443.mp3"));
        actions = Actions.STAND;

        camera = new OrthographicCamera();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);

        camera.position.x = body.getPosition().x * phisX.PPM;
        camera.position.y = body.getPosition().y * phisX.PPM;
        camera.zoom = 1;
        camera.update();

        mapRenderer.setView(camera);
        mapRenderer.render(tL);

        manAssetss.get(actions).setTime(Gdx.graphics.getDeltaTime());
        body.applyForceToCenter(myInputProcessor.getVector(), true);

        if (body.getLinearVelocity().len() < 0.6f) actions = Actions.STAND;
        else if (Math.abs(body.getLinearVelocity().x) > 0.6f)  {
            actions = Actions.RUN;
        }

        manAssetss.get(actions).setTime(Gdx.graphics.getDeltaTime());
        if (!manAssetss.get(actions).draw().isFlipX() & body.getLinearVelocity().x < 0.6f) {
            manAssetss.get(actions).draw().flip(true, false);
        }
        if (manAssetss.get(actions).draw().isFlipX() & body.getLinearVelocity().x > 0.6f) {
            manAssetss.get(actions).draw().flip(true, false);
        }

        float x = body.getPosition().x * phisX.PPM - 2.5f/camera.zoom;
        float y = body.getPosition().y * phisX.PPM - 2.5f/camera.zoom;

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(manAssetss.get(actions).draw(), x, y);
        batch.end();

        mapRenderer.render(front);

        Gdx.graphics.setTitle(String.valueOf(body.getLinearVelocity()));
        phisX.step();
        phisX.debugDraw(camera);
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportHeight = height;
        camera.viewportWidth = width;
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
        batch.dispose();
        img.dispose();
        music.dispose();
        sound.dispose();
        map.dispose();
        mapRenderer.dispose();
    }
}
