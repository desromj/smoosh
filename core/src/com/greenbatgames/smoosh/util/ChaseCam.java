package com.greenbatgames.smoosh.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.greenbatgames.smoosh.entity.Bug;

/**
 * Created by Quiv on 06-07-2016.
 */
public class ChaseCam
{
    Bug target;
    Camera camera;
    Boolean following;

    public ChaseCam(Camera camera, Bug target)
    {
        this.target = target;
        this.camera = camera;
        this.following = true;
    }

    public void update(float delta)
    {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
        {
            if (!following)
                this.centreOnTarget();

            following = !following;
        }

        if (following) {
            float
                    xLeeway = Constants.CHASE_CAM_X_LEEWAY / 2.0f,
                    yLeeway = Constants.CHASE_CAM_Y_LEEWAY / 2.0f;

            if (this.camera.position.x > target.getPosition().x
                    && this.camera.position.x - target.getPosition().x > xLeeway)
                this.camera.position.x = target.getPosition().x + xLeeway;

            if (this.camera.position.x < target.getPosition().x
                    && target.getPosition().x - this.camera.position.x > xLeeway)
                this.camera.position.x = target.getPosition().x - xLeeway;

            if (this.camera.position.y > target.getPosition().y
                    && this.camera.position.y - target.getPosition().y > yLeeway)
                this.camera.position.y = target.getPosition().y + yLeeway;

            if (this.camera.position.y < target.getPosition().y
                    && target.getPosition().y - this.camera.position.y > yLeeway)
                this.camera.position.y = target.getPosition().y - yLeeway;

        } else {
            if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                camera.position.x -= delta * Constants.CHASE_CAM_MOVE_SPEED;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                camera.position.x += delta * Constants.CHASE_CAM_MOVE_SPEED;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                camera.position.y += delta * Constants.CHASE_CAM_MOVE_SPEED;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                camera.position.y -= delta * Constants.CHASE_CAM_MOVE_SPEED;
            }
        }
    }

    private void centreOnTarget()
    {
        camera.position.x = target.getPosition().x;
        camera.position.y = target.getPosition().y;
    }
}
