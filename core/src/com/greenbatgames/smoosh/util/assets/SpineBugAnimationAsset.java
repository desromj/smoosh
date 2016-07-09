package com.greenbatgames.smoosh.util.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.greenbatgames.smoosh.entity.Bug;
import com.greenbatgames.smoosh.util.AnimationBlend;

import spine.AnimationState;
import spine.Skeleton;
import spine.SkeletonRenderer;
import spine.SkeletonRendererDebug;

/**
 * Created by Quiv on 09-07-2016.
 */
public abstract class SpineBugAnimationAsset
{
    protected SkeletonRenderer skeletonRenderer;
    protected SkeletonRendererDebug skeletonRendererDebug;
    protected TextureAtlas atlas;

    // Skeleton and animationState are public since they will need to be updated via game logic
    public AnimationState animationState;
    public Skeleton skeleton;

    protected Bug bug;

    // All subclasses must initialize all required Spine classes above
    public abstract void initSpine();
    public abstract AnimationBlend[] getBlends();



    public SpineBugAnimationAsset(Bug bug)
    {
        this.bug = bug;
        initSpine();
    }



    public void render(SpriteBatch batch)
    {
        animationState.update(Gdx.graphics.getDeltaTime());
        animationState.apply(skeleton);
        skeleton.updateWorldTransform();

        skeletonRenderer.draw(batch, skeleton);
        // skeletonRendererDebug.draw(skeleton);
    }



    public void setAnimation(int trackIndex, String animationName, boolean loop)
    {
        animationState.setAnimation(trackIndex, animationName, loop);
    }
}