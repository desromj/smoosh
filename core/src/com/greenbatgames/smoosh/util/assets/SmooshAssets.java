package com.greenbatgames.smoosh.util.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.greenbatgames.smoosh.entity.Bug;
import com.greenbatgames.smoosh.util.AnimationBlend;
import com.greenbatgames.smoosh.util.Enums;

import spine.AnimationState;
import spine.AnimationStateData;
import spine.Skeleton;
import spine.SkeletonData;
import spine.SkeletonJson;
import spine.SkeletonRenderer;
import spine.SkeletonRendererDebug;

/**
 * Created by Quiv on 09-07-2016.
 */
public class SmooshAssets extends SpineBugAnimationAsset
{
    public SmooshAssets(Bug bug) { super(bug); }

    @Override
    public void initSpine()
    {
        skeletonRenderer = new SkeletonRenderer();
        skeletonRenderer.setPremultipliedAlpha(true);       // Alpha blending to reduce outlines

        skeletonRendererDebug = new SkeletonRendererDebug();
        skeletonRendererDebug.setBoundingBoxes(false);
        skeletonRendererDebug.setRegionAttachments(false);

        atlas = new TextureAtlas(Gdx.files.internal("anim/smoosh/smoosh.atlas"));
        SkeletonJson json = new SkeletonJson(atlas);        // load stateless skeleton JSON data
        json.setScale(0.4f);                                // set skeleton scale from Spine

        // Read the JSON data and create the skeleton
        SkeletonData skeletonData = json.readSkeletonData(Gdx.files.internal("anim/smoosh/skeleton.json"));
        skeleton = new Skeleton(skeletonData);

        // init animation
        AnimationStateData stateData = new AnimationStateData(skeletonData);
        animationState = new AnimationState(stateData);

        // Interpolations
        AnimationStateData animationStateData = animationState.getData();

        for (AnimationBlend blend: this.getBlends())
            animationStateData.setMix(blend.getFrom(), blend.getTo(), blend.getDuration());

        // Set default animation for all bugs - idle
        animationState.setAnimation(0, "idle", true);
    }



    /**
     * @return an array of Animation blends for this particular bug. If there are none applicable,
     *      return an empty Array. Default method returns the empty array, and can be overridden
     *      by child classes.
     */
    @Override
    public AnimationBlend[] getBlends() {
        return new AnimationBlend [] {
                AnimationBlend.makeBlend(Enums.AnimationState.IDLE, Enums.AnimationState.WALKING, 0.25f),
                AnimationBlend.makeBlend(Enums.AnimationState.IDLE, Enums.AnimationState.IDLE_WITH_PROP, 0.25f),
                AnimationBlend.makeBlend(Enums.AnimationState.IDLE, Enums.AnimationState.RUNNING, 0.25f),

                AnimationBlend.makeBlend(Enums.AnimationState.IDLE_WITH_PROP, Enums.AnimationState.IDLE, 0.25f),
                AnimationBlend.makeBlend(Enums.AnimationState.IDLE_WITH_PROP, Enums.AnimationState.WALKING_WITH_PROP, 0.25f),
                AnimationBlend.makeBlend(Enums.AnimationState.IDLE_WITH_PROP, Enums.AnimationState.RUNNING_WITH_PROP, 0.25f),

                AnimationBlend.makeBlend(Enums.AnimationState.WALKING, Enums.AnimationState.IDLE, 0.25f),
                AnimationBlend.makeBlend(Enums.AnimationState.WALKING, Enums.AnimationState.RUNNING, 0.75f),
                AnimationBlend.makeBlend(Enums.AnimationState.WALKING, Enums.AnimationState.WALKING_WITH_PROP, 0.25f),

                AnimationBlend.makeBlend(Enums.AnimationState.WALKING_WITH_PROP, Enums.AnimationState.IDLE_WITH_PROP, 0.25f),
                AnimationBlend.makeBlend(Enums.AnimationState.WALKING_WITH_PROP, Enums.AnimationState.WALKING, 0.25f),
                AnimationBlend.makeBlend(Enums.AnimationState.WALKING_WITH_PROP, Enums.AnimationState.RUNNING_WITH_PROP, 0.75f),

                AnimationBlend.makeBlend(Enums.AnimationState.RUNNING, Enums.AnimationState.IDLE, 0.25f),
                AnimationBlend.makeBlend(Enums.AnimationState.RUNNING, Enums.AnimationState.WALKING, 0.75f),
                AnimationBlend.makeBlend(Enums.AnimationState.RUNNING, Enums.AnimationState.RUNNING_WITH_PROP, 0.25f),

                AnimationBlend.makeBlend(Enums.AnimationState.RUNNING_WITH_PROP, Enums.AnimationState.RUNNING, 0.25f),
                AnimationBlend.makeBlend(Enums.AnimationState.RUNNING_WITH_PROP, Enums.AnimationState.WALKING_WITH_PROP, 0.75f),
                AnimationBlend.makeBlend(Enums.AnimationState.RUNNING_WITH_PROP, Enums.AnimationState.IDLE_WITH_PROP, 0.25f)
        };
    }
}