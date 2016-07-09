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
import com.greenbatgames.smoosh.util.assets.SmooshAssets;
import com.greenbatgames.smoosh.util.assets.SpineBugAnimationAsset;

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
}
