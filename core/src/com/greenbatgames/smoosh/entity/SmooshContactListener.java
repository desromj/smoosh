package com.greenbatgames.smoosh.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.greenbatgames.smoosh.util.Constants;
import com.greenbatgames.smoosh.util.Utils;

/**
 * Created by Quiv on 06-07-2016.
 */
public class SmooshContactListener implements ContactListener
{
    @Override
    public void beginContact(Contact contact) {

        // Bug-Specific collision (Player and NPCs)
        if (Utils.contactHasBug(contact)) {

            Bug bug = Utils.getBugContact(contact);
            Object other = Utils.getNonBugContact(contact);

            // Collision logic for Bug landing on other physics objects
            if (other instanceof PhysicsObject)
            {
                PhysicsObject physical = (PhysicsObject) other;

                /*
                    A collision has already happened, we just need to check:
                        - Player's position is above the other object's
                        - Player's x position is within the other's x + width
                  */
                Vector2 bugPos = Utils.getObjectFixture(contact, bug).getBody().getPosition();
                Vector2 otherPos = Utils.getObjectFixture(contact, physical).getBody().getPosition();

                boolean landed = false;

                if (bugPos.y > otherPos.y)
                {
                    if ((bugPos.x * Constants.PTM + Constants.PLATFORM_EDGE_LEEWAY > otherPos.x * Constants.PTM - physical.getWidth() / 2.0f)
                            && (bugPos.x * Constants.PTM - Constants.PLATFORM_EDGE_LEEWAY < otherPos.x * Constants.PTM + physical.getWidth() / 2.0f))
                    {
                        landed = true;
                    }
                }

                if (landed) {
                    if (bug.isCrouched())
                        bug.crouch();
                    else
                        bug.land();
                }
            }
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

        if (Utils.contactHasBug(contact)) {

            Bug bug = Utils.getBugContact(contact);
            Object other = Utils.getNonBugContact(contact);

            // Collision logic for one-way platforms
            if (other instanceof Platform) {

                Platform platform = (Platform) other;

                if (bug.collisionDisabled() && platform.isOneWay()) {
                    contact.setEnabled(false);
                } else if (platform.isOneWay()) {
                    if (bug.getBottom() <= platform.top - Constants.PLATFORM_COLLISION_LEEWAY) {
                        contact.setEnabled(false);
                    }
                }
            }
        }
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
