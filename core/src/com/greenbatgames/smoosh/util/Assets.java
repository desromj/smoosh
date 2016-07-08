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

    public SpineAnimationAsset makeAsset(Bug bug) throws NotImplementedException
    {
        if (bug instanceof Smoosh)      return new SmooshAssets(bug);
        else                            throw new NotImplementedException();
    }

    // Parent class which can be rendered via the Spine runtimes
    public abstract class SpineAnimationAsset
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

        public SpineAnimationAsset(Bug bug)
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
    private final class SmooshAssets extends SpineAnimationAsset
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

            for (AnimationBlend blend: this.bug.getBlends())
                animationStateData.setMix(blend.getFrom(), blend.getTo(), blend.getDuration());

            // Set default animation
            animationState.setAnimation(0, "idle", true);
        }
    }
}
