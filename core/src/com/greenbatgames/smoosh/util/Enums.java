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
        WALKING("walk"),
        WALKING_WITH_PROP("walk-with-prop");

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
}
