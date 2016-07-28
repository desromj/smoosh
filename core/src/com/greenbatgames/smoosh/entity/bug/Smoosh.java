package com.greenbatgames.smoosh.entity.bug;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.TimeUtils;
import com.greenbatgames.smoosh.entity.Bug;
import com.greenbatgames.smoosh.screen.GameScreen;
import com.greenbatgames.smoosh.util.Constants;
import com.greenbatgames.smoosh.util.Enums;
import com.greenbatgames.smoosh.util.Utils;

import spine.Animation;
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

        // Load all particle effects
        this.addParticleEffect(Enums.EffectType.MATCH_FLAME);
    }



    private float getAnimationPlaybackSpeed(Animation anim)
    {
        // Running and jumping animation played back at 2x speed
        if (anim.getName().startsWith("run") || anim.getName().startsWith("jump"))
            return 2.0f;

        if (anim.getName().startsWith("land"))
            return 4.0f;

        return 1.0f;
    }



    @Override
    public void update(float delta)
    {
        // Edit playback speed as necessary
        float playbackSpeed = getAnimationPlaybackSpeed(this.getAnimationState().getCurrent(0).getAnimation());
        this.getAnimationState().getCurrent(0).setTimeScale(playbackSpeed);

        // Update parent method
        super.update(delta);

        // Switch the prop carrying method on/off with the E key
        if (this.animationState.allowControl() && Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            this.carryingProp = !this.carryingProp;

            if (this.carryingProp) {
                this.turnEffectOn(Enums.EffectType.MATCH_FLAME);
                Utils.playSound("audio/effects/match-strike.wav", 0.4f);
            } else {
                this.turnEffectOff(Enums.EffectType.MATCH_FLAME);
            }

            this.refreshAnimationState();
        }

        // Check for crouching
        if (this.animationState.allowControl() && this.grounded && Gdx.input.isKeyJustPressed(Input.Keys.CONTROL_LEFT)) {
            if (this.crouched)
                this.unCrouch();
            else
                this.crouch();
        }
    }



    @Override
    protected void move()
    {
        // Prevent movement if our animation doesn't allow it
        if (!this.animationState.allowControl()) {
            this.body.setLinearVelocity(
                    0f,
                    0f
            );
            return;
        }

        // Check left/right movement keys for X velocity
        boolean running = (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT));

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            if (this.crouched) {
                this.body.setLinearVelocity(
                        Constants.SMOOSH_CROUCH_SPEED / Constants.PTM,
                        this.body.getLinearVelocity().y
                );
            } else if (running) {
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
            if (this.crouched) {
                this.body.setLinearVelocity(
                        -Constants.SMOOSH_CROUCH_SPEED / Constants.PTM,
                        this.body.getLinearVelocity().y
                );
            } else if (running) {
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
                this.getPosition().y / Constants.PTM
        );
        bodyDef.fixedRotation = true;

        this.body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.set(Constants.SMOOSH_VERTICIES_NORMAL);

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
        Animation anim = this.getAnimationState().getCurrent(0).getAnimation();

        // Check animation states for if we're -grounded-...
        if (this.grounded)
        {
            // Check if we've landed first
            if (anim.getName().startsWith("land") && this.getAnimationState().getCurrent(0).isComplete())
                this.landing = false;

            // Check if we're locked into landing
            if (this.landing)
            {
                if (anim.getName().startsWith("land"))
                {
                    if (this.carryingProp)
                        return Enums.AnimationState.LANDING_WITH_PROP;
                    else
                        return Enums.AnimationState.LANDING;
                }

                // TODO: Verify this works as expected
                return Enums.AnimationState.LANDING;
            }
            // Otherwise, look for crouching
            else if (this.crouched)
            {
                if (Math.abs(this.getBody().getLinearVelocity().x * Constants.PTM) > Constants.SMOOSH_IDLE_SPEED_THRESHOLD)
                {
                    if (this.carryingProp)
                        return Enums.AnimationState.CRAWLING_WITH_PROP;
                    else
                        return Enums.AnimationState.CRAWLING;
                }
                else
                {
                    if (this.carryingProp)
                        return Enums.AnimationState.CROUCHING_WITH_PROP;
                    else
                        return Enums.AnimationState.CROUCHING;
                }
            }
            // Otherwise, check speed for running
            else if (Math.abs(this.getBody().getLinearVelocity().x * Constants.PTM) > Constants.SMOOSH_WALK_SPEED_THRESHOLD)
            {
                if (this.carryingProp)
                    return Enums.AnimationState.RUNNING_WITH_PROP;
                else
                    return Enums.AnimationState.RUNNING;
            }
            // Otherwise, check speed for walking
            else if (Math.abs(this.getBody().getLinearVelocity().x * Constants.PTM) > Constants.SMOOSH_IDLE_SPEED_THRESHOLD)
            {
                if (this.carryingProp)
                    return Enums.AnimationState.WALKING_WITH_PROP;
                else
                    return Enums.AnimationState.WALKING;
            }
            // Otherwise, we're idle
            else
            {
                if (this.carryingProp)
                    return Enums.AnimationState.IDLE_WITH_PROP;
                else
                    return Enums.AnimationState.IDLE;
            }
        }
        // If we are -airborne-...
        else
        {
            // Check if we're already falling
            if (anim.getName().startsWith("fall"))
            {
                if (this.carryingProp)
                    return Enums.AnimationState.FALLING_WITH_PROP;
                else
                    return Enums.AnimationState.FALLING;
            }

            // Then check if we should be transitioning from the jumping animation
            if (anim.getName().startsWith("jump"))
            {
                if (this.getAnimationState().getCurrent(0).isComplete())
                {
                    if (this.carryingProp)
                        return Enums.AnimationState.FALLING_WITH_PROP;
                    else
                        return Enums.AnimationState.FALLING;
                }
            }

            // Finally, just jump if the other two don't apply
            if (this.carryingProp)
                return Enums.AnimationState.JUMPING_WITH_PROP;
            else
                return Enums.AnimationState.JUMPING;
        }
    }



    @Override
    protected void setCrouchCollision(boolean crouching)
    {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        bodyDef.fixedRotation = true;

        PolygonShape shape = new PolygonShape();

        if (this.isCrouched()) {
            shape.set(Constants.SMOOSH_VERTICIES_CROUCHED);
            bodyDef.position.set(
                    this.body.getPosition().x,
                    (this.body.getPosition().y -
                            (Utils.getMaxHeight(Constants.SMOOSH_VERTICIES_NORMAL)
                                    - Utils.getMaxHeight(Constants.SMOOSH_VERTICIES_CROUCHED))
                                    / 2.0f)
            );
        } else {
            shape.set(Constants.SMOOSH_VERTICIES_NORMAL);
            bodyDef.position.set(
                    this.body.getPosition().x,
                    (this.body.getPosition().y -
                            (Utils.getMaxHeight(Constants.SMOOSH_VERTICIES_CROUCHED)
                                    - Utils.getMaxHeight(Constants.SMOOSH_VERTICIES_NORMAL))
                                    / 2.0f)
            );
        }

        FixtureDef fixtureDef = new FixtureDef();

        fixtureDef.shape = shape;
        fixtureDef.density = Constants.SMOOSH_DENSITY;
        fixtureDef.restitution = 0f;
        fixtureDef.friction = 0f;

        GameScreen.instance.queueBodyToCreate(bodyDef, fixtureDef, this);
        GameScreen.instance.queueBodyToDestroy(this.body);
    }


    @Override
    public void renderShapes(ShapeRenderer renderer) {}
}
