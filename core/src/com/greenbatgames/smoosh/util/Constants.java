package com.greenbatgames.smoosh.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Quiv on 06-07-2016.
 */
public class Constants
{
    private Constants() {}

    /*
        User-Configurable Options
     */
    public static float VOLUME_EFFECTS = 1.0f;
    public static float VOLUME_MUSIC = 1.0f;


    /*
        World Aspect Ratio
     */
    public static final float WORLD_WIDTH = 800f;
    public static final float WORLD_HEIGHT = WORLD_WIDTH * 3f / 4f;

    public static final Color BG_COLOR = Color.SKY;

    /*
        Physics World Values
     */

    public static final float GRAVITY = -9.81f;

    // PIXELS_TO_METRES: We are working with bugs as main characters: 1000 pixels = 1 metre
    public static final float PTM = 100f;
    public static final float WOBBLE_ROOM = WORLD_WIDTH / 16000f;

    public static final float DISABLE_COLLISION_FOR_PLATFORM = 0.25f;

    public static final float PHYSICS_STEP_FREQ = 1f / 60f;
    public static final int PHYSICS_VEL_ITERATIONS = 6;
    public static final int PHYSICS_POS_ITERATIONS = 6;

    /*
        Camera Controls
     */
    public static final float CHASE_CAM_MOVE_SPEED = WORLD_WIDTH * 2.0f;
    public static final float CHASE_CAM_X_LEEWAY = WORLD_WIDTH / 4f;
    public static final float CHASE_CAM_Y_LEEWAY = WORLD_WIDTH / 10f;

    /*
        Platform Values
     */
    public static final float PLATFORM_EDGE_LEEWAY = WORLD_WIDTH / 60.0f;
    public static final float PLATFORM_COLLISION_LEEWAY = WORLD_WIDTH / 960.0f;

    /*
        Bug Values
     */

    // Smoosh

    // Density values, in kg/m^3 (human average is 985, water is 1020, wood about 650, but varies)
    public static final float SMOOSH_DENSITY = 400.0f;
    public static final float SMOOSH_JUMP_IMPULSE = 120.0f * SMOOSH_DENSITY;
    public static final float SMOOSH_RADIUS = WORLD_WIDTH / 36.0f;

    public static final float SMOOSH_CROUCH_SPEED = 120.0f;
    public static final float SMOOSH_WALK_SPEED = 160.0f;
    public static final float SMOOSH_RUN_SPEED = 400.0f;

    public static final float SMOOSH_HORIZONTAL_WALK_DAMPEN = 0.80f;
    public static final float SMOOSH_HORIZONTAL_FALL_DAMPEN = 0.90f;

    public static final float SMOOSH_VERTEX_X_SCALE = SMOOSH_RADIUS;
    public static final float SMOOSH_VERTEX_Y_SCALE = SMOOSH_RADIUS * 2.0f;

    public static final Vector2[] SMOOSH_VERTICIES_NORMAL = new Vector2[] {
            new Vector2(0.90f * SMOOSH_VERTEX_X_SCALE / PTM, 0.67f * SMOOSH_VERTEX_Y_SCALE / PTM),
            new Vector2(0.33f * SMOOSH_VERTEX_X_SCALE / PTM, 1.00f * SMOOSH_VERTEX_Y_SCALE / PTM),
            new Vector2(-0.33f * SMOOSH_VERTEX_X_SCALE / PTM, 1.00f * SMOOSH_VERTEX_Y_SCALE / PTM),
            new Vector2(-0.90f * SMOOSH_VERTEX_X_SCALE / PTM, 0.67f * SMOOSH_VERTEX_Y_SCALE / PTM),
            new Vector2(-0.90f * SMOOSH_VERTEX_X_SCALE / PTM, -1.67f * SMOOSH_VERTEX_Y_SCALE / PTM),
            new Vector2(-0.33f * SMOOSH_VERTEX_X_SCALE / PTM, -2.00f * SMOOSH_VERTEX_Y_SCALE / PTM),
            new Vector2(0.33f * SMOOSH_VERTEX_X_SCALE / PTM, -2.00f * SMOOSH_VERTEX_Y_SCALE / PTM),
            new Vector2(0.90f * SMOOSH_VERTEX_X_SCALE / PTM, -1.67f * SMOOSH_VERTEX_Y_SCALE / PTM)
    };

    public static final Vector2[] SMOOSH_VERTICIES_CROUCHED = new Vector2[] {
            new Vector2(1.67f * SMOOSH_VERTEX_X_SCALE / PTM, -1.00f * SMOOSH_VERTEX_Y_SCALE / PTM),
            new Vector2(1.00f * SMOOSH_VERTEX_X_SCALE / PTM, -0.67f * SMOOSH_VERTEX_Y_SCALE / PTM),
            new Vector2(-1.00f * SMOOSH_VERTEX_X_SCALE / PTM, -0.67f * SMOOSH_VERTEX_Y_SCALE / PTM),
            new Vector2(-1.67f * SMOOSH_VERTEX_X_SCALE / PTM, -1.00f * SMOOSH_VERTEX_Y_SCALE / PTM),
            new Vector2(-1.67f * SMOOSH_VERTEX_X_SCALE / PTM, -1.67f * SMOOSH_VERTEX_Y_SCALE / PTM),
            new Vector2(-1.00f * SMOOSH_VERTEX_X_SCALE / PTM, -2.00f * SMOOSH_VERTEX_Y_SCALE / PTM),
            new Vector2(1.00f * SMOOSH_VERTEX_X_SCALE / PTM, -2.00f * SMOOSH_VERTEX_Y_SCALE / PTM),
            new Vector2(1.67f * SMOOSH_VERTEX_X_SCALE / PTM, -1.67f * SMOOSH_VERTEX_Y_SCALE / PTM)
    };

    /*
        Animation Helper Values
     */
    public static final float SMOOSH_IDLE_SPEED_THRESHOLD = 25f;
    public static final float SMOOSH_WALK_SPEED_THRESHOLD = SMOOSH_WALK_SPEED * 2f;

    /*
        Platform Game Object Values
     */
    public static final Color PLATFORM_COLOR = Color.BLUE;

}
