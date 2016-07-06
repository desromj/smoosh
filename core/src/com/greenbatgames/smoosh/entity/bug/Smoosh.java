package com.greenbatgames.smoosh.entity.bug;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.greenbatgames.smoosh.entity.Bug;
import com.greenbatgames.smoosh.util.Constants;
import com.greenbatgames.smoosh.util.Enums;

/**
 * Created by Quiv on 06-07-2016.
 */
public class Smoosh extends Bug
{
    public Smoosh(float x, float y, float width, float height, World world, boolean grounded) {
        super(x, y, width, height, world, grounded);
    }

    @Override
    protected void initPhysics(World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(
                this.getPosition().x / Constants.PTM,
                (this.getPosition().y + Constants.SMOOSH_RADIUS) / Constants.PTM
        );
        bodyDef.fixedRotation = true;

        this.body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.set(Constants.SMOOSH_VERTICIES);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = Constants.SMOOSH_DENSITY;
        fixtureDef.restitution = 0f;
        fixtureDef.friction = 0f;

        this.body.createFixture(fixtureDef);
        this.body.setUserData(this);

        shape.dispose();
    }

    /**
     * Logic to determine what the next animation state should be set
     * to, based on velocity and boolean flags grounded and flapping
     *
     * @return The Animation State this object should be in, based off its movement boolean flags
     */
    @Override
    protected Enums.AnimationState nextAnimationState()
    {
        if (this.grounded)
        {
            if (Math.abs(this.getBody().getLinearVelocity().x * Constants.PTM) > Constants.BUG_IDLE_SPEED_THRESHOLD)
                return Enums.AnimationState.WALKING;
            else
                return Enums.AnimationState.IDLE;
        }
        else
        {
            // TODO: Should be falling
            return Enums.AnimationState.IDLE;
        }
    }

    @Override
    public void renderShapes(ShapeRenderer renderer) {

    }

    @Override
    public void renderSprites(SpriteBatch batch) {

    }
}
