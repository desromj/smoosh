package com.greenbatgames.smoosh.animation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.greenbatgames.smoosh.entity.Bug;
import com.greenbatgames.smoosh.util.Enums;
import com.greenbatgames.smoosh.util.Utils;

import spine.Bone;

/**
 * Created by Quiv on 10-07-2016.
 */
public class MatchFlameEffect extends AnimationEffect
{
    public MatchFlameEffect(Bug bug)
    {
        super(bug);

    }



    @Override
    public Enums.EffectType getType() {
        return Enums.EffectType.MATCH_FLAME;
    }



    @Override
    protected void clingTransform(ParticleEffect effect) {

        Bone bone = this.parent.getSkeleton().findBone("prop");

        effect.setPosition(
            this.parent.getPosition().x + bone.getX(),
            this.parent.getPosition().y + bone.getY()
        );
    }



    @Override
    protected void populateEffects() {
        this.effects.add(Utils.makeParticleEffect(
                "particles/data/pe-match",
                "particles/images",
                1.0f
        ));
    }
}
