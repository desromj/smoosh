package com.greenbatgames.smoosh.util.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.greenbatgames.smoosh.entity.Bug;
import com.greenbatgames.smoosh.util.AnimationBlend;
import com.greenbatgames.smoosh.util.Enums;
import com.greenbatgames.smoosh.util.Utils;

import spine.AnimationState;
import spine.AnimationStateData;
import spine.Event;
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

        // Spine Event Listeners
        animationState.addListener(new AnimationState.AnimationStateAdapter() {
            @Override
            public void event(int trackIndex, Event event) {
                if (event.getData().getName().compareTo("footstep") == 0)
                {
                    Utils.playSound("audio/effects/footstep-grass.wav", 0.25f);
                }
            }
        });

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

                AnimationBlend.makeBlend(Enums.AnimationState.CRAWLING, Enums.AnimationState.CROUCHING, 0.25f),
                AnimationBlend.makeBlend(Enums.AnimationState.CRAWLING, Enums.AnimationState.WALKING, 0.25f),
                AnimationBlend.makeBlend(Enums.AnimationState.CRAWLING, Enums.AnimationState.RUNNING, 0.50f),
                AnimationBlend.makeBlend(Enums.AnimationState.CRAWLING, Enums.AnimationState.CRAWLING_WITH_PROP, 0.25f),
                AnimationBlend.makeBlend(Enums.AnimationState.CRAWLING, Enums.AnimationState.FALLING, 0.25f),


                AnimationBlend.makeBlend(Enums.AnimationState.CRAWLING_WITH_PROP, Enums.AnimationState.CROUCHING_WITH_PROP, 0.25f),
                AnimationBlend.makeBlend(Enums.AnimationState.CRAWLING_WITH_PROP, Enums.AnimationState.WALKING_WITH_PROP, 0.25f),
                AnimationBlend.makeBlend(Enums.AnimationState.CRAWLING_WITH_PROP, Enums.AnimationState.RUNNING_WITH_PROP, 0.50f),
                AnimationBlend.makeBlend(Enums.AnimationState.CRAWLING_WITH_PROP, Enums.AnimationState.CRAWLING, 0.25f),
                AnimationBlend.makeBlend(Enums.AnimationState.CRAWLING_WITH_PROP, Enums.AnimationState.FALLING_WITH_PROP, 0.25f),


                AnimationBlend.makeBlend(Enums.AnimationState.CROUCHING, Enums.AnimationState.IDLE, 0.25f),
                AnimationBlend.makeBlend(Enums.AnimationState.CROUCHING, Enums.AnimationState.CRAWLING, 0.25f),
                AnimationBlend.makeBlend(Enums.AnimationState.CROUCHING, Enums.AnimationState.CROUCHING_WITH_PROP, 0.50f),
                AnimationBlend.makeBlend(Enums.AnimationState.CROUCHING, Enums.AnimationState.FALLING, 0.25f),


                AnimationBlend.makeBlend(Enums.AnimationState.CROUCHING_WITH_PROP, Enums.AnimationState.CROUCHING, 0.50f),
                AnimationBlend.makeBlend(Enums.AnimationState.CROUCHING_WITH_PROP, Enums.AnimationState.CRAWLING_WITH_PROP, 0.50f),
                AnimationBlend.makeBlend(Enums.AnimationState.CROUCHING_WITH_PROP, Enums.AnimationState.IDLE_WITH_PROP, 0.25f),
                AnimationBlend.makeBlend(Enums.AnimationState.CROUCHING_WITH_PROP, Enums.AnimationState.FALLING_WITH_PROP, 0.25f),


                AnimationBlend.makeBlend(Enums.AnimationState.IDLE, Enums.AnimationState.WALKING, 0.25f),
                AnimationBlend.makeBlend(Enums.AnimationState.IDLE, Enums.AnimationState.IDLE_WITH_PROP, 0.50f),
                AnimationBlend.makeBlend(Enums.AnimationState.IDLE, Enums.AnimationState.RUNNING, 0.25f),
                AnimationBlend.makeBlend(Enums.AnimationState.IDLE, Enums.AnimationState.CROUCHING, 0.25f),
                AnimationBlend.makeBlend(Enums.AnimationState.IDLE, Enums.AnimationState.JUMPING, 0.1f),
                AnimationBlend.makeBlend(Enums.AnimationState.IDLE, Enums.AnimationState.FALLING, 0.25f),


                AnimationBlend.makeBlend(Enums.AnimationState.IDLE_WITH_PROP, Enums.AnimationState.IDLE, 0.50f),
                AnimationBlend.makeBlend(Enums.AnimationState.IDLE_WITH_PROP, Enums.AnimationState.WALKING_WITH_PROP, 0.25f),
                AnimationBlend.makeBlend(Enums.AnimationState.IDLE_WITH_PROP, Enums.AnimationState.RUNNING_WITH_PROP, 0.25f),
                AnimationBlend.makeBlend(Enums.AnimationState.IDLE_WITH_PROP, Enums.AnimationState.CROUCHING_WITH_PROP, 0.25f),
                AnimationBlend.makeBlend(Enums.AnimationState.IDLE_WITH_PROP, Enums.AnimationState.JUMPING_WITH_PROP, 0.1f),
                AnimationBlend.makeBlend(Enums.AnimationState.IDLE_WITH_PROP, Enums.AnimationState.FALLING_WITH_PROP, 0.25f),


                AnimationBlend.makeBlend(Enums.AnimationState.WALKING, Enums.AnimationState.IDLE, 0.25f),
                AnimationBlend.makeBlend(Enums.AnimationState.WALKING, Enums.AnimationState.RUNNING, 0.75f),
                AnimationBlend.makeBlend(Enums.AnimationState.WALKING, Enums.AnimationState.CRAWLING, 0.25f),
                AnimationBlend.makeBlend(Enums.AnimationState.WALKING, Enums.AnimationState.WALKING_WITH_PROP, 0.50f),
                AnimationBlend.makeBlend(Enums.AnimationState.WALKING, Enums.AnimationState.JUMPING, 0.1f),
                AnimationBlend.makeBlend(Enums.AnimationState.WALKING, Enums.AnimationState.FALLING, 0.25f),


                AnimationBlend.makeBlend(Enums.AnimationState.WALKING_WITH_PROP, Enums.AnimationState.IDLE_WITH_PROP, 0.25f),
                AnimationBlend.makeBlend(Enums.AnimationState.WALKING_WITH_PROP, Enums.AnimationState.WALKING, 0.50f),
                AnimationBlend.makeBlend(Enums.AnimationState.WALKING_WITH_PROP, Enums.AnimationState.RUNNING_WITH_PROP, 0.75f),
                AnimationBlend.makeBlend(Enums.AnimationState.WALKING_WITH_PROP, Enums.AnimationState.CRAWLING_WITH_PROP, 0.25f),
                AnimationBlend.makeBlend(Enums.AnimationState.WALKING_WITH_PROP, Enums.AnimationState.JUMPING_WITH_PROP, 0.1f),
                AnimationBlend.makeBlend(Enums.AnimationState.WALKING_WITH_PROP, Enums.AnimationState.FALLING_WITH_PROP, 0.25f),


                AnimationBlend.makeBlend(Enums.AnimationState.RUNNING, Enums.AnimationState.IDLE, 0.25f),
                AnimationBlend.makeBlend(Enums.AnimationState.RUNNING, Enums.AnimationState.WALKING, 0.75f),
                AnimationBlend.makeBlend(Enums.AnimationState.RUNNING, Enums.AnimationState.CRAWLING, 0.25f),
                AnimationBlend.makeBlend(Enums.AnimationState.RUNNING, Enums.AnimationState.RUNNING_WITH_PROP, 0.50f),
                AnimationBlend.makeBlend(Enums.AnimationState.RUNNING, Enums.AnimationState.JUMPING, 0.1f),
                AnimationBlend.makeBlend(Enums.AnimationState.RUNNING, Enums.AnimationState.FALLING, 0.25f),


                AnimationBlend.makeBlend(Enums.AnimationState.RUNNING_WITH_PROP, Enums.AnimationState.RUNNING, 0.50f),
                AnimationBlend.makeBlend(Enums.AnimationState.RUNNING_WITH_PROP, Enums.AnimationState.WALKING_WITH_PROP, 0.75f),
                AnimationBlend.makeBlend(Enums.AnimationState.RUNNING_WITH_PROP, Enums.AnimationState.IDLE_WITH_PROP, 0.25f),
                AnimationBlend.makeBlend(Enums.AnimationState.RUNNING_WITH_PROP, Enums.AnimationState.CRAWLING_WITH_PROP, 0.25f),
                AnimationBlend.makeBlend(Enums.AnimationState.RUNNING_WITH_PROP, Enums.AnimationState.JUMPING_WITH_PROP, 0.1f),
                AnimationBlend.makeBlend(Enums.AnimationState.RUNNING_WITH_PROP, Enums.AnimationState.FALLING_WITH_PROP, 0.25f),


                AnimationBlend.makeBlend(Enums.AnimationState.JUMPING, Enums.AnimationState.FALLING, 0.1f),


                AnimationBlend.makeBlend(Enums.AnimationState.JUMPING_WITH_PROP, Enums.AnimationState.FALLING_WITH_PROP, 0.1f),


                AnimationBlend.makeBlend(Enums.AnimationState.FALLING, Enums.AnimationState.LANDING, 0.1f),


                AnimationBlend.makeBlend(Enums.AnimationState.FALLING_WITH_PROP, Enums.AnimationState.LANDING_WITH_PROP, 0.1f),


                AnimationBlend.makeBlend(Enums.AnimationState.LANDING, Enums.AnimationState.IDLE, 0.1f),


                AnimationBlend.makeBlend(Enums.AnimationState.LANDING_WITH_PROP, Enums.AnimationState.IDLE_WITH_PROP, 0.1f)
        };
    }
}
