package com.greenbatgames.smoosh.screen;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.greenbatgames.smoosh.entity.Bug;
import com.greenbatgames.smoosh.entity.Platform;
import com.greenbatgames.smoosh.util.ChaseCam;

/**
 * Created by Quiv on 06-07-2016.
 */
public class GameScreen extends ScreenAdapter implements InputProcessor
{
    public static final GameScreen instance = new GameScreen();

    Viewport viewport;
    ShapeRenderer renderer;
    SpriteBatch batch;
    ChaseCam chaseCam;

    Array<Platform> platforms;
    Bug player;

    World world;
    Box2DDebugRenderer debugRenderer;
    Matrix4 debugMatrix;

    private GameScreen() { init(); }

    public void init()
    {

    }




    /*
        Getters and Setters
     */
    public Bug getPlayer() { return this.player; }

    /*
        Input Overrides
     */

    @Override
    public boolean keyDown(int keycode) {
        return false;
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
