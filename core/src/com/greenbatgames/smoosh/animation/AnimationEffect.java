package com.greenbatgames.smoosh.animation;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.greenbatgames.smoosh.entity.Bug;
import com.greenbatgames.smoosh.util.Enums;

/**
 * Created by Quiv on 10-07-2016.
 */
public abstract class AnimationEffect
{
    protected Bug parent;

    private boolean on;

    protected Vector2 position, rotation;
    protected Array<ParticleEffect> effects;

    protected AnimationEffect(Bug bug)
    {
        this.parent = bug;

        this.position = new Vector2();
        this.rotation = new Vector2();
        this.on = false;
        this.effects = new Array<ParticleEffect>();
    }



    public void update(float delta)
    {
        for (int i = 0; i < effects.size; i++)
        {
            ParticleEffect effect = effects.get(i);
            effect.update(delta);

            if (effect.isComplete())
                effects.removeIndex(i);
            else
                this.clingTransform(effect);

        }
    }



    public void draw(SpriteBatch batch)
    {
        if (this.isOn())
            for (ParticleEffect effect: effects)
                effect.draw(batch);
    }



    protected abstract void clingTransform(ParticleEffect effect);
    protected abstract void populateEffects();

    public abstract Enums.EffectType getType();



    public boolean isOn()
    {
        return this.on;
    }



    public void turnOn()
    {
        this.on = true;

        this.populateEffects();
        for (ParticleEffect effect: effects)
            effect.start();
    }



    public void turnOff()
    {
        this.on = false;

        for (int i = 0; i < effects.size; i++)
            effects.removeIndex(i);
    }
}
