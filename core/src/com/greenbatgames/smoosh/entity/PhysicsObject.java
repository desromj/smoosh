package com.greenbatgames.smoosh.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.greenbatgames.smoosh.util.Constants;

/**
 * Created by Quiv on 06-07-2016.
 */
public abstract class PhysicsObject
{
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

        initPhysics(world);
    }

    protected abstract void initPhysics(World world);
    public abstract void renderShapes(ShapeRenderer renderer);
    public abstract void renderSprites(SpriteBatch batch);

    public void update(float delta)
    {
        // Cling this object's position to the physics body
        this.position.set(
                (this.body.getPosition().x * Constants.PTM) - this.width / 2.0f,
                (this.body.getPosition().y * Constants.PTM) - this.height / 2.0f
        );

        // Check position last frame compared to this frame in update, within a variance.
        if (position.dst(lastPosition) < Constants.WOBBLE_ROOM)
            this.atRest = true;
        else
            this.atRest = false;

        // Update our last position for the next frame
        this.lastPosition.set(this.position.x, this.position.y);
    }

    public boolean isAtRest() { return atRest; }
    public Vector2 getPosition() {
        return position;
    }
    public float getWidth() {
        return width;
    }
    public float getHeight() {
        return height;
    }
    public Body getBody() {
        return body;
    }

    public float getBottom() { return this.position.y - this.height / 2.0f; }
    public float getTop() { return this.position.y + this.height / 2.0f; }
    public float getLeft() { return this.position.x - this.width / 2.0f; }
    public float getRight() { return this.position.x + this.width / 2.0f; }
}
