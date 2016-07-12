package com.greenbatgames.smoosh.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.greenbatgames.smoosh.util.Constants;

/**
 * Created by Quiv on 06-07-2016.
 */
public abstract class PhysicsObject
{
    public static final String TAG = PhysicsObject.class.getSimpleName();

    Vector2 position, lastPosition;
    float width, height;
    boolean atRest;

    protected Body body;

    public PhysicsObject(float x, float y, float width, float height, World world)
    {
        this.position = new Vector2(x, y);
        this.lastPosition = new Vector2(x, y);

        this.width = width;
        this.height = height;

        this.atRest = false;
        this.body = null;

        initPhysics(world);
    }

    protected abstract void initPhysics(World world);
    public abstract void renderShapes(ShapeRenderer renderer);
    public abstract void renderSprites(SpriteBatch batch);

    public void update(float delta)
    {
        // Cling this object's position to the physics body
        this.position.set(
                (this.body.getPosition().x * Constants.PTM) - this.getWidth() / 2.0f,
                (this.body.getPosition().y * Constants.PTM) - this.getHeight() / 2.0f
        );

        // Check position last frame compared to this frame in update, within a variance.
        if (position.dst(lastPosition) < Constants.WOBBLE_ROOM)
            this.atRest = true;
        else
            this.atRest = false;

        // Update our last position for the next frame
        this.lastPosition.set(this.position.x, this.position.y);
    }

    /*
        Getters and Setters
     */

    public void setBody(Body body) { this.body = body; }
    public Body getBody() { return body; }

    public boolean isAtRest() { return atRest; }

    public Vector2 getPosition() { return position; }

    // Width and Height use Body bounds from below
    public float getWidth() { return this.getRight() - this.getLeft(); }
    public float getHeight() { return this.getTop() - this.getBottom(); }

    // Get Bounds based on Body Shape

    public float getBottom()
    {
        Shape shape = this.body.getFixtureList().first().getShape();

        PolygonShape poly = (PolygonShape) shape;
        float lowest = 0f;
        Vector2 vert = new Vector2();

        for (int i = 0; i < poly.getVertexCount(); i++)
        {
            poly.getVertex(i, vert);

            if (i == 0 || vert.y < lowest)
                lowest = vert.y;
        }

        return this.position.y + lowest * Constants.PTM;
    }

    public float getTop()
    {
        Shape shape = this.body.getFixtureList().first().getShape();

        PolygonShape poly = (PolygonShape) shape;
        float highest = 0f;
        Vector2 vert = new Vector2();

        for (int i = 0; i < poly.getVertexCount(); i++)
        {
            poly.getVertex(i, vert);

            if (i == 0 || vert.y > highest)
                highest = vert.y;
        }

        return this.position.y + highest * Constants.PTM;
    }

    public float getLeft()
    {
        Shape shape = this.body.getFixtureList().first().getShape();

        PolygonShape poly = (PolygonShape) shape;
        float leftest = 0f;
        Vector2 vert = new Vector2();

        for (int i = 0; i < poly.getVertexCount(); i++)
        {
            poly.getVertex(i, vert);

            if (i == 0 || vert.x < leftest)
                leftest = vert.x;
        }

        return this.position.y + leftest * Constants.PTM;
    }

    public float getRight()
    {
        Shape shape = this.body.getFixtureList().first().getShape();

        PolygonShape poly = (PolygonShape) shape;
        float rightest = 0f;
        Vector2 vert = new Vector2();

        for (int i = 0; i < poly.getVertexCount(); i++)
        {
            poly.getVertex(i, vert);

            if (i == 0 || vert.x > rightest)
                rightest = vert.x;
        }

        return this.position.y + rightest * Constants.PTM;
    }
}
