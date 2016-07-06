package com.greenbatgames.smoosh.util;

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
