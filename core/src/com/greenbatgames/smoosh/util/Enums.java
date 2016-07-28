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
        IDLE("idle", true),
        IDLE_WITH_PROP("idle-with-prop", true),

        CROUCHING("crouch", true),
        CROUCHING_WITH_PROP("crouch-with-prop", true),

        CRAWLING("crawl", true),
        CRAWLING_WITH_PROP("crawl-with-prop", true),

        WALKING("walk", true),
        WALKING_WITH_PROP("walk-with-prop", true),

        JUMPING("jump", false),
        JUMPING_WITH_PROP("jump-with-prop", false),

        FALLING("fall", true),
        FALLING_WITH_PROP("fall-with-prop", true),

        RUNNING("run", true),
        RUNNING_WITH_PROP("run-with-prop", true);

        private String label;
        private boolean looped;

        private AnimationState(String label, boolean looped)
        {
            this.label = label;
            this.looped = looped;
        }

        public String getLabel()
        {
            return this.label;
        }

        public boolean isLooped() { return looped; }
    }



    // Particle Effects to be used in Animations
    public enum EffectType
    {
        MATCH_FLAME;
    }
}
