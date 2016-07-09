package com.greenbatgames.smoosh.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Disposable;
import com.greenbatgames.smoosh.entity.Bug;
import com.greenbatgames.smoosh.entity.bug.Smoosh;

import spine.AnimationState;
import spine.AnimationStateData;
import spine.Skeleton;
import spine.SkeletonData;
import spine.SkeletonJson;
import spine.SkeletonRenderer;
import spine.SkeletonRendererDebug;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by Quiv on 06-07-2016.
 */
public class Assets implements Disposable, AssetErrorListener
{
    public static final Assets instance = new Assets();
    public static final String TAG = instance.getClass().getSimpleName();

    private AssetManager manager;

    private Assets() {}

    public void init()
    {
        manager = new AssetManager();
        manager.setErrorListener(this);
        manager.finishLoading();
    }

    @Override
    public void error(AssetDescriptor asset, Throwable throwable) {
        Gdx.app.error(TAG, "Could not load asset: " + asset.fileName, throwable);
    }

    @Override
    public void dispose() {
        manager.dispose();
    }

    /*
        Spine Animation Asset Classes
     */

    public SpineBugAnimationAsset makeAsset(Bug bug) throws NotImplementedException
    {
        if (bug instanceof Smoosh)      return new SmooshAssets(bug);
        else                            throw new NotImplementedException();
    }

    // Parent class which can be rendered via the Spine runtimes
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
        public abstract AnimationBlend [] getBlends();

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

    /**
     * Bloom is the main character of the game
     */
    private final class SmooshAssets extends SpineBugAnimationAsset
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
}
