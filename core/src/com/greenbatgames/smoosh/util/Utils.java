package com.greenbatgames.smoosh.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.greenbatgames.smoosh.entity.Bug;
import com.greenbatgames.smoosh.screen.GameScreen;

/**
 * Created by Quiv on 06-07-2016.
 */
public class Utils
{
    private Utils() {}



    /**
     * @param verts
     * @return The maximum height difference in an array of Vector2 objects
     */
    public static float getMaxHeight(Vector2 [] verts)
    {
        float highest = 0f, lowest = 0f;

        for (int i = 0; i < verts.length; i++)
        {
            if (i == 0)
            {
                highest = verts[i].y;
                lowest = verts[i].y;
                continue;
            }

            if (verts[i].y > highest)
                highest = verts[i].y;

            if (verts[i].y < lowest)
                lowest = verts[i].y;
        }

        return highest - lowest;
    }



    /**
     * @param verts
     * @return The maximum height difference in an array of Vector2 objects
     */
    public static float getMaxWidth(Vector2 [] verts)
    {
        float highest = 0f, lowest = 0f;

        for (int i = 0; i < verts.length; i++)
        {
            if (i == 0)
            {
                highest = verts[i].x;
                lowest = verts[i].x;
                continue;
            }

            if (verts[i].x > highest)
                highest = verts[i].x;

            if (verts[i].x < lowest)
                lowest = verts[i].x;
        }

        return highest - lowest;
    }



    /**
     * Creates a ParticleEffect object given the path, imagePath, and scale.
     *
     * @param filePath Gdx.files.internal path to the file defining the particle effect
     * @param imagesPath Gdx.files.internal path to the images the filePath file uses
     * @param scale Scale to apply to the ParticleEffect
     * @return The created and started ParticleEffect
     */
    public static ParticleEffect makeParticleEffect(
            String filePath,
            String imagesPath,
            float scale
    )
    {
        ParticleEffect effect = new ParticleEffect();
        effect.load(Gdx.files.internal(filePath), Gdx.files.internal(imagesPath));
        effect.scaleEffect(scale);

        return effect;
    }



    /**
     * @param filename The filename of the internal-path sound effect to play, including extension
     * @param volume Ratio of volume to play the effect at
     * @return The Sound object created and currently being played, in case it needs to be edited further
     */
    public static Sound playSound(String filename, float volume)
    {
        Sound sound = Gdx.audio.newSound(Gdx.files.internal(filename));
        sound.play(volume * Constants.VOLUME_EFFECTS);
        return sound;
    }



    /**
     * @param filename The filename of the internal-path sound effect to play, including extension
     * @param volume Ratio of volume to play the effect at
     * @return The Sound object created and currently being played, in case it needs to be edited further
     */
    public static Sound playMusic(String filename, float volume)
    {
        // TODO: Change this to actually play music instead of sound effects
        Sound sound = Gdx.audio.newSound(Gdx.files.internal(filename));
        sound.play(volume * Constants.VOLUME_MUSIC);
        return sound;
    }



    public static boolean almostEqualTo(float first, float second, float variance)
    {
        return Math.abs(first - second) < variance;
    }



    public static float getMassRatio(Body first, Body second, boolean getFirst)
    {
        float firstRatio = first.getMass() / (first.getMass() + second.getMass());
        float secondRatio = second.getMass() / (first.getMass() + second.getMass());

        if (getFirst)
            return firstRatio;
        return secondRatio;
    }



    public static boolean contactHasPlayer(Contact contact)
    {
        Object
                a = contact.getFixtureA().getBody().getUserData(),
                b = contact.getFixtureB().getBody().getUserData();

        if ((a == GameScreen.instance.getPlayer()) || (b == GameScreen.instance.getPlayer()))
            return true;
        return false;
    }



    public static boolean contactHasBug(Contact contact)
    {
        Object
                a = contact.getFixtureA().getBody().getUserData(),
                b = contact.getFixtureB().getBody().getUserData();

        if ((a instanceof Bug) || (b instanceof Bug))
            return true;
        return false;
    }



    public static Bug getPlayerContact(Contact contact) throws NullPointerException
    {
        Object
                a = contact.getFixtureA().getBody().getUserData(),
                b = contact.getFixtureB().getBody().getUserData();

        if (a == GameScreen.instance.getPlayer())
            return (Bug) a;
        else if (b == GameScreen.instance.getPlayer())
            return (Bug) b;
        else
            throw new NullPointerException();
    }



    public static Bug getBugContact(Contact contact) throws NullPointerException
    {
        Object
                a = contact.getFixtureA().getBody().getUserData(),
                b = contact.getFixtureB().getBody().getUserData();

        if (a instanceof Bug)
            return (Bug) a;
        else if (b instanceof Bug)
            return (Bug) b;
        else
            throw new NullPointerException();
    }



    public static Object getNonPlayerContact(Contact contact) throws NullPointerException
    {
        Object
                a = contact.getFixtureA().getBody().getUserData(),
                b = contact.getFixtureB().getBody().getUserData();

        if (a == GameScreen.instance.getPlayer())
            return b;
        else if (b == GameScreen.instance.getPlayer())
            return a;
        else
            throw new NullPointerException();
    }



    /**
     * @param contact
     * @return The non-bug object involved in this collision. If both fixtures are bugs,
     *          returns the second bug involved
     * @throws NullPointerException
     */
    public static Object getNonBugContact(Contact contact) throws NullPointerException
    {
        Object
                a = contact.getFixtureA().getBody().getUserData(),
                b = contact.getFixtureB().getBody().getUserData();

        if (a instanceof Bug)
            return b;
        else if (b instanceof Bug)
            return a;
        else
            throw new NullPointerException();
    }



    /**
     * Uses the user data from a contact to return the Fixture of the correct Fixture
     * in the collision - either A or B.
     *
     * User data is the user data set on the Fixture's Body, not on the Fixture itself
     *
     * @param contact
     * @param object
     * @return
     */
    public static Fixture getObjectFixture(Contact contact, Object object) throws NullPointerException
    {
        if (object == contact.getFixtureA().getBody().getUserData())
            return contact.getFixtureA();
        else if (object == contact.getFixtureB().getBody().getUserData())
            return contact.getFixtureB();
        else
            throw new NullPointerException();
    }
}
