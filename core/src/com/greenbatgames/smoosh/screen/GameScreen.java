package com.greenbatgames.smoosh.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.greenbatgames.smoosh.entity.Bug;
import com.greenbatgames.smoosh.entity.PhysicsObject;
import com.greenbatgames.smoosh.entity.Platform;
import com.greenbatgames.smoosh.entity.SmooshContactListener;
import com.greenbatgames.smoosh.entity.bug.Smoosh;
import com.greenbatgames.smoosh.util.ChaseCam;
import com.greenbatgames.smoosh.util.Constants;

import java.util.Map;

/**
 * Created by Quiv on 06-07-2016.
 */
public class GameScreen extends ScreenAdapter implements InputProcessor
{
    public static final GameScreen instance = new GameScreen();
    public static final String TAG = GameScreen.class.getSimpleName();

    Viewport viewport;
    ShapeRenderer renderer;
    SpriteBatch batch;
    ChaseCam chaseCam;

    Array<Platform> platforms;
    Bug player;
    Vector2 spawnPoint;

    World world;
    Array<Body> bodiesToRemove;
    Array<BodyDef> bodiesToAdd;
    Array<FixtureDef> fixturesToAdd;
    Array<PhysicsObject> userDataToAdd;
    Box2DDebugRenderer debugRenderer;
    Matrix4 debugMatrix;

    private GameScreen() { init(); }

    public void init()
    {
        // Init physics world: with gravity
        world = new World(new Vector2(0, Constants.GRAVITY), true);
        bodiesToAdd = new Array<BodyDef>();
        fixturesToAdd = new Array<FixtureDef>();
        userDataToAdd = new Array<PhysicsObject>();
        bodiesToRemove = new Array<Body>();
        debugRenderer = new Box2DDebugRenderer();

        // Base inits
        this.platforms = new Array<Platform>();
        this.viewport = new ExtendViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
        this.renderer = new ShapeRenderer();
        this.batch = new SpriteBatch();
        this.spawnPoint = new Vector2(80.0f, 240.0f);

        // World dependent objects
        this.player = new Smoosh(spawnPoint, world);
        this.chaseCam = new ChaseCam(viewport.getCamera(), this.player);

        // Init Platforms
        platforms.add(new Platform(20.0f, 20.0f, 1600.0f, 80.0f, world, false));
        platforms.add(new Platform(800.0f, 420.0f, 540.0f, 25.0f, world, true));

        // Finalize
        Gdx.input.setInputProcessor(this);

        world.setContactListener(new SmooshContactListener());
    }



    public void render(float delta)
    {
        // Update the physics engine with all the bodies
        world.step(
                Constants.PHYSICS_STEP_FREQ,
                Constants.PHYSICS_VEL_ITERATIONS,
                Constants.PHYSICS_POS_ITERATIONS
        );

        // Edit Physics bodies outside of world.step
        runQueuedPhysicsChanges();

        /*
            Game object updates
         */

        player.update(delta);
        chaseCam.update(delta);

        /*
            Rendering logic
         */

        // Set projection matricies
        viewport.apply();
        renderer.setProjectionMatrix(viewport.getCamera().combined);
        batch.setProjectionMatrix(viewport.getCamera().combined);

        // Scale the debug Matrix to box2d sizes
        debugMatrix = viewport.getCamera().combined.cpy().scale(
                Constants.PTM,
                Constants.PTM,
                0
        );

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Render Shapes
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        doShapeRender(renderer);
        renderer.end();

        // Render Sprites
        batch.begin();
        doSpriteRender(batch);
        batch.end();

        // Render the debug physics engine settings
        debugRenderer.render(world, debugMatrix);
    }



    private void runQueuedPhysicsChanges()
    {
        // Add
        for (int i = 0; i < this.bodiesToAdd.size; i++)
        {
            Body body = world.createBody(this.bodiesToAdd.get(i));
            body.createFixture(fixturesToAdd.get(i));
            body.setUserData(userDataToAdd.get(i));

            userDataToAdd.get(i).setBody(body);
        }

        if (this.bodiesToAdd.size > 0) {
            this.bodiesToAdd.clear();
            this.fixturesToAdd.clear();
            this.userDataToAdd.clear();
        }

        // Remove
        for (int i = 0; i < this.bodiesToRemove.size; i++)
        {
            world.destroyBody(this.bodiesToRemove.get(i));
        }
        this.bodiesToRemove.clear();
    }



    public void doShapeRender(ShapeRenderer renderer)
    {
        // Render game background
        renderer.setColor(Constants.BG_COLOR);
        renderer.rect(0, 0, Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);

        // Render platforms
        for (Platform platform: platforms)
            platform.renderShapes(renderer);
    }



    public void doSpriteRender(SpriteBatch batch)
    {
        // Render player character
        player.renderSprites(batch);
    }

    /*
        ScreenAdapter Overrides
     */

    @Override
    public void resize(int width, int height)
    {
        viewport.update(width, height, true);
    }

    /*
        Getters and Setters
     */
    public Bug getPlayer() { return this.player; }
    public Viewport getViewport()
    {
        return viewport;
    }

    public void queueBodyToDestroy(Body body)
    {
        this.bodiesToRemove.add(body);
    }

    public void queueBodyToCreate(BodyDef bodyDef, FixtureDef fixtureDef, PhysicsObject newUserData)
    {
        this.bodiesToAdd.add(bodyDef);
        this.fixturesToAdd.add(fixtureDef);
        this.userDataToAdd.add(newUserData);
    }

    /*
        Input Overrides
     */

    @Override
    public boolean keyDown(int keycode)
    {
        if (keycode == Input.Keys.Z)
            player.jump();

        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
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
    public boolean scrolled(int amount) {
        return false;
    }
}
