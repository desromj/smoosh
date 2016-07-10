package com.greenbatgames.smoosh.util;

/**
 * Created by Quiv on 06-07-2016.
 */
public class Enums
{
    private Enums() {}

    // Animation States for Bugs
    public enum AnimationState
    {
        IDLE("idle"),
        IDLE_WITH_PROP("idle-with-prop"),

        CROUCHING("crouch"),
        CROUCHING_WITH_PROP("crouch-with-prop"),

        WALKING("walk"),
        WALKING_WITH_PROP("walk-with-prop"),

        RUNNING("run"),
        RUNNING_WITH_PROP("run-with-prop");

        private String label;

        private AnimationState(String label)
        {
            this.label = label;
        }

        public String getLabel()
        {
            return this.label;
        }
    }



    // Particle Effects to be used in Animations
    public enum EffectType
    {
        MATCH_FLAME;
    }
}
