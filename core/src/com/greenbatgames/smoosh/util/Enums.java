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
        IDLE("Idle"),
        WALKING("Walking"),
        WALKING_WITH_PROP("Walking - Prop");

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
