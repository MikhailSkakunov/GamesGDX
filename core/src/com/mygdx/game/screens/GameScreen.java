package com.mygdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.*;
import com.mygdx.game.enums.Actions;
import com.mygdx.game.persons.Bullet;
import com.mygdx.game.persons.Man;

import java.util.ArrayList;
import java.util.List;


public class GameScreen implements Screen {
    Game game;
    private SpriteBatch batch;
    private Texture img;
    private Music music;
    private Sound sound;
    private MyInputProcessor myInputProcessor;
    private OrthographicCamera camera;
    private PhisX phisX;
    private Body body;
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;
    private int[] front, tL;
    private final Man man;
    private final MyAnim coinAnm;
    public static List<Body> bodyToDelete;
    public static List<Bullet> bullets;
    private final Label font;
    private int coins;
    private int bulletsCnt;


    public GameScreen(Game game){
        bulletsCnt = 100;
        coins = 0;

        font = new Label(12);

        bodyToDelete = new ArrayList<>();
        bullets = new ArrayList<>();
        coinAnm = new MyAnim("Full Coinss.png",1,8, 12, Animation.PlayMode.LOOP);
        this.game = game;

        map = new TmxMapLoader().load("map/безымянный.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map);

        front = new int[1];
        front[0] = map.getLayers().getIndex("front");
        tL = new int[1];
        tL[0] = map.getLayers().getIndex("t0");

        TiledMapTileMapObject mo = (TiledMapTileMapObject) map.getLayers().get("damage").getObjects().get("monster1");

        phisX = new PhisX();

        int c = (int) map.getProperties().get("coinsCnt");
        Array<RectangleMapObject> objects = map.getLayers().get("env").getObjects().getByType(RectangleMapObject.class);
        objects.addAll(map.getLayers().get("dyn").getObjects().getByType(RectangleMapObject.class));
        for (int i = 0; i < objects.size; i++) {
            phisX.addObject(objects.get(i));
        }

        objects.clear();
        objects.addAll(map.getLayers().get("damage").getObjects().getByType(RectangleMapObject.class));
        for (int i = 0; i < objects.size; i++) {
            phisX.addDmgObject(objects.get(i));
        }

        body = phisX.addObject((RectangleMapObject) map.getLayers().get("hero").getObjects().get("hero"));
        body.setFixedRotation(true);
        man = new Man(body);

        TiledMapTileMapObject heart = (TiledMapTileMapObject) map.getLayers().get("env").getObjects().get("heart");

        myInputProcessor = new MyInputProcessor();
        Gdx.input.setInputProcessor(myInputProcessor);

        music = Gdx.audio.newMusic(Gdx.files.internal("MC_Hammer_-_U_Cant_Touch_This_b128f0d256.mp3"));
        music.setVolume(0.025f);
        music.setLooping(true);
        music.play();

        sound = Gdx.audio.newSound(Gdx.files.internal("7b999d49fa57974.mp3"));

        batch = new SpriteBatch();
        img = new Texture("badlogic.jpg");

        camera = new OrthographicCamera();
        camera.zoom = 0.35f;
    }

    @Override
    public void show() {}

    @Override
    public void render(final float delta) {
        ScreenUtils.clear(Color.BLACK);

        camera.position.x = body.getPosition().x * phisX.PPM;
        camera.position.y = body.getPosition().y * phisX.PPM;
        camera.update();

        mapRenderer.setView(camera);
        mapRenderer.render(tL);

        man.setTime(delta);
        Vector2 vector = myInputProcessor.getVector();
        Body tBody = man.setFPS(body.getLinearVelocity(), true);
        if (tBody != null & bulletsCnt>0) {
            bulletsCnt--;
            bullets.add(new Bullet(phisX, tBody.getPosition().x, tBody.getPosition().y, man.getDir()));
            vector.set(0, 0);
        } else if (tBody != null) {
            vector.set(0, 0);
            man.setState(Actions.STAND);
        }
        if (MyContactListner.cnt < 1) {
            vector.set(vector.x, 0);
        }
        body.applyForceToCenter(vector, true);

        ArrayList<Bullet> bTmp = new ArrayList<>();
        batch.begin();
        for (Bullet b: bullets) {
            Body tB = b.update(delta);
            if ( tB != null) {
                bodyToDelete.add(tB);
                bTmp.add(b);
            }
        }
        batch.end();
        bullets.removeAll(bTmp);

        Rectangle tmp = man.getRect(camera, man.getFrame());
        ((PolygonShape)body.getFixtureList().get(0).getShape()).setAsBox(tmp.width/2, tmp.height/2);
        ((PolygonShape)body.getFixtureList().get(1).getShape()).setAsBox(
                tmp.width/3,
                tmp.height/10,
                new Vector2(0, -tmp.height/2),0);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        font.draw(batch, "HP:"+ man.getHit(0) + " Монет: " + coins, (int)tmp.x,(int)(tmp.y+tmp.height * PhisX.PPM));
        batch.draw(man.getFrame(), tmp.x,tmp.y, tmp.width * PhisX.PPM, tmp.height * PhisX.PPM);

        Array<Body> bodys = phisX.getBodys("coins");
        coinAnm.setTime(delta);
        TextureRegion tr = coinAnm.draw();
        float dScale = 6;
        for (Body bd: bodys) {
            float cx = bd.getPosition().x * PhisX.PPM - tr.getRegionWidth() / 2 / dScale;
            float cy = bd.getPosition().y * PhisX.PPM - tr.getRegionHeight() / 2 / dScale;
            float cW = tr.getRegionWidth() / PhisX.PPM / dScale;
            float cH = tr.getRegionHeight() / PhisX.PPM / dScale;
            ((PolygonShape)bd.getFixtureList().get(0).getShape()).setAsBox(cW/2, cH/2);
            batch.draw(tr, cx,cy, cW * PhisX.PPM, cH * PhisX.PPM);
        }
        batch.end();

        mapRenderer.render(front);

        for (Body bd: bodyToDelete) {
            if (bd.getUserData() != null && bd.getUserData().equals("coins")) coins++;
            if (bd.getUserData() != null && bd.getUserData().equals("bullet")) ;

            phisX.destroyBody(bd);}
        bodyToDelete.clear();

        phisX.step();
        phisX.debugDraw(camera);

        if (MyContactListner.isDamage) {
            if (man.getHit(1) < 1){
                dispose();
                game.setScreen(new GameOverScreen(game));
            }
        }
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
        this.man.dispose();
        this.font.dispose();
        this.phisX.dispose();
        this.coinAnm.dispose();
    }
}
