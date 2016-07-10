package com.greenbatgames.smoosh.entity.bug;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.greenbatgames.smoosh.entity.Bug;
import com.greenbatgames.smoosh.util.Constants;
import com.greenbatgames.smoosh.util.Enums;
import com.greenbatgames.smoosh.util.Utils;

import spine.AnimationState;

/**
 * Created by Quiv on 06-07-2016.
 */
public class Smoosh extends Bug
{
    Vector2 spawnPosition;
    boolean carryingProp;



    public Smoosh(Vector2 spawn, World world) { this(spawn.x, spawn.y, world); }

    public Smoosh(float x, float y, World world) {
        super(
                x,
                y,
                Constants.SMOOSH_RADIUS * 2.0f,
                Constants.SMOOSH_RADIUS * 8.0f,
                world,
                false);
        this.spawnPosition = new Vector2();
        init();
    }



    public void init()
    {
        this.body.setLinearVelocity(0f, 0f);

        this.getPosition().set(this.spawnPosition.x, this.spawnPosition.y);
        this.getLastPosition().set(this.spawnPosition.x, this.spawnPosition.y);

        // TODO: Change this to fall/jump when complete
        this.animationState = Enums.AnimationState.IDLE;
        this.grounded = false;
        this.disableCollisionFor = 0.0f;
        this.carryingProp = false;
    }



    @Override
    public void update(float delta)
    {
        // Double the playback speed of running animations
        float playbackSpeed = 1.0f;

        for (AnimationState.TrackEntry te = this.asset.animationState.getCurrent(0); te != null; te = te.getNext()) {
            if (te.getAnimation().getName().startsWith("run"))
                playbackSpeed = 2.0f;
        }

        this.asset.animationState.getCurrent(0).setTimeScale(playbackSpeed);

        // Update parent method
        super.update(delta);

        // Switch the prop carrying method on/off with the E key
        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            this.carryingProp = !this.carryingProp;

            if (this.carryingProp)
                Utils.playSound("audio/effects/match-strike.wav", 0.4f);

            this.refreshAnimationState();
        }
    }



    @Override
    protected void move()
    {
        // Check left/right movement keys for X velocity
        boolean running = (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT));

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            if (running) {
                this.body.setLinearVelocity(
                        Constants.SMOOSH_RUN_SPEED / Constants.PTM,
                        this.body.getLinearVelocity().y
                );
            } else {
                this.body.setLinearVelocity(
                        Constants.SMOOSH_WALK_SPEED / Constants.PTM,
                        this.body.getLinearVelocity().y
                );
            }

            this.facingRight = true;

        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            if (running) {
                this.body.setLinearVelocity(
                        -Constants.SMOOSH_RUN_SPEED / Constants.PTM,
                        this.body.getLinearVelocity().y
                );
            } else {
                this.body.setLinearVelocity(
                        -Constants.SMOOSH_WALK_SPEED / Constants.PTM,
                        this.body.getLinearVelocity().y
                );
            }

            this.facingRight = false;

        } else {
            if (!this.grounded)
            {
                this.body.setLinearVelocity(
                        this.body.getLinearVelocity().x * Constants.SMOOSH_HORIZONTAL_FALL_DAMPEN,
                        this.body.getLinearVelocity().y
                );
            } else {
                this.body.setLinearVelocity(
                        this.body.getLinearVelocity().x * Constants.SMOOSH_HORIZONTAL_WALK_DAMPEN,
                        this.body.getLinearVelocity().y
                );
            }
        }
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
        // Then get the necessary animation state
        if (this.grounded)
        {
            if (Math.abs(this.getBody().getLinearVelocity().x * Constants.PTM) > Constants.SMOOSH_WALK_SPEED_THRESHOLD) {
                if (this.carryingProp)
                    return Enums.AnimationState.RUNNING_WITH_PROP;
                else
                    return Enums.AnimationState.RUNNING;
            } else if (Math.abs(this.getBody().getLinearVelocity().x * Constants.PTM) > Constants.SMOOSH_IDLE_SPEED_THRESHOLD) {
                if (this.carryingProp)
                    return Enums.AnimationState.WALKING_WITH_PROP;
                else
                    return Enums.AnimationState.WALKING;
            } else {
                if (this.carryingProp)
                    return Enums.AnimationState.IDLE_WITH_PROP;
                else
                    return Enums.AnimationState.IDLE;
            }
        }
        else
        {
            // TODO: Should be falling
            return Enums.AnimationState.IDLE;
        }
    }



    @Override
    public void renderShapes(ShapeRenderer renderer) {}
}
