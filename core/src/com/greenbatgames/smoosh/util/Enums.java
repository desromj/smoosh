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
        IDLE("idle", true, true),
        IDLE_WITH_PROP("idle-with-prop", true, true),

        CROUCHING("crouch", true, true),
        CROUCHING_WITH_PROP("crouch-with-prop", true, true),

        CRAWLING("crawl", true, true),
        CRAWLING_WITH_PROP("crawl-with-prop", true, true),

        WALKING("walk", true, true),
        WALKING_WITH_PROP("walk-with-prop", true, true),

        JUMPING("jump", false, true),
        JUMPING_WITH_PROP("jump-with-prop", false, true),

        FALLING("fall", true, true),
        FALLING_WITH_PROP("fall-with-prop", true, true),

        LANDING("land", false, false),
        LANDING_WITH_PROP("land-with-prop", false, false),

        RUNNING("run", true, true),
        RUNNING_WITH_PROP("run-with-prop", true, true);

        private String label;
        private boolean looped, allowControl;

        private AnimationState(String label, boolean looped, boolean allowControl)
        {
            this.label = label;
            this.looped = looped;
            this.allowControl = allowControl;
        }

        public String getLabel()
        {
            return this.label;
        }

        public boolean isLooped() { return looped; }

        public boolean allowControl() { return this.allowControl; }
    }



    // Particle Effects to be used in Animations
    public enum EffectType
    {
        MATCH_FLAME;
    }
}
