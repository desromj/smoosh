package com.greenbatgames.smoosh.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.greenbatgames.smoosh.animation.AnimationEffect;
import com.greenbatgames.smoosh.animation.EffectFactory;
import com.greenbatgames.smoosh.util.assets.Assets;
import com.greenbatgames.smoosh.util.Constants;
import com.greenbatgames.smoosh.util.Enums;
import com.greenbatgames.smoosh.util.assets.SpineBugAnimationAsset;

import spine.AnimationState;
import spine.Skeleton;

/**
 * Created by Quiv on 06-07-2016.
 */
public abstract class Bug extends PhysicsObject
{
    private SpineBugAnimationAsset asset;

    protected Enums.AnimationState animationState, previousState;
    protected boolean grounded, jumped;
    private boolean animationChanged;
    protected boolean facingRight;
    protected float disableCollisionFor;
    protected Array<AnimationEffect> particles;

    public Bug(float x, float y, float width, float height, World world, boolean grounded) {
        super(x, y, width, height, world);
        this.grounded = grounded;
        this.jumped = true;
        this.animationChanged = false;
        this.facingRight = true;
        this.disableCollisionFor = 0.0f;
        this.asset = Assets.instance.makeAsset(this);
        this.animationState = Enums.AnimationState.IDLE;
        this.previousState = Enums.AnimationState.IDLE;
        this.particles = new Array<AnimationEffect>();
    }



    protected abstract Enums.AnimationState nextAnimationState();
    protected abstract void move();



    protected void addParticleEffect(Enums.EffectType type)
    {
        this.particles.add(EffectFactory.makeEffect(type, this));
    }



    protected void turnEffectOn(Enums.EffectType type)
    {
        for (AnimationEffect effect: particles)
            if (effect.getType() == type)
                effect.turnOn();
    }



    protected void turnEffectOff(Enums.EffectType type)
    {
        for (AnimationEffect effect: particles)
            if (effect.getType() == type)
                effect.turnOff();
    }



    public void update(float delta)
    {
        // First cling position to the physics body
        this.position.set(
                this.body.getPosition().x * Constants.PTM,
                this.body.getPosition().y * Constants.PTM
        );

        this.disableCollisionFor -= delta;

        // Idle/Walking/Running movement controls
        this.move();

        // Ensure our dynamic bodies are always awake and ready to be interacted with
        this.body.setAwake(true);

        // Set what the next animation state should be
        this.animationState = nextAnimationState();

        if (this.animationState != this.previousState)
            this.animationChanged = true;

        this.previousState = this.animationState;

        // Set the last position at the end of the update loop
        this.lastPosition.set(this.position.x, this.position.y);

        // Cling the skeleton to the position of the body
        this.asset.skeleton.setPosition(
                this.getPosition().x,
                this.getPosition().y - this.getHeight() / 2.0f);

        // Update particle effects
        for (AnimationEffect effect: particles)
            effect.update(delta);
    }



    @Override
    public void renderSprites(SpriteBatch batch)
    {
        // Render the bug first
        if (this.animationChanged) {
            asset.skeleton.setToSetupPose();
            asset.setAnimation(0, this.animationState.getLabel(), true);
            this.animationChanged = false;
        }

        this.asset.skeleton.setFlipX(!this.facingRight);
        asset.render(batch);

        // Render particle effects
        for (AnimationEffect effect: particles)
            effect.draw(batch);
    }



    public void land()
    {
        this.grounded = true;
        this.jumped = false;
    }



    public void jump()
    {
        if (this.jumped)
            return;

        this.jumped = true;

        if (this.grounded && Gdx.input.isKeyPressed(Input.Keys.DOWN))
        {
            this.disableCollisionFor = Constants.DISABLE_COLLISION_FOR_PLATFORM;
            return;
        }

        this.body.applyForceToCenter(0f, Constants.SMOOSH_JUMP_IMPULSE, true);
    }



    /*
        Getters and Setters
     */
    public Vector2 getPosition() { return this.position; }
    public Vector2 getLastPosition() { return this.lastPosition; }

    public final AnimationState getAnimationState() { return this.asset.animationState; }
    public final Skeleton getSkeleton() { return this.asset.skeleton; }

    public boolean collisionDisabled() { return this.disableCollisionFor > 0f; }
    public final void refreshAnimationState() { this.animationChanged = true; }
}
