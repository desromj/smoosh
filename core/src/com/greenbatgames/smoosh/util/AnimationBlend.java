package com.greenbatgames.smoosh.util;

/**
 * Created by Quiv on 08-07-2016.
 */
public class AnimationBlend
{
    private String from, to;
    private float duration;

    private AnimationBlend() {}

    public static AnimationBlend makeBlend(Enums.AnimationState from, Enums.AnimationState to, float duration)
    {
        AnimationBlend blend = new AnimationBlend();

        blend.from = from.getLabel();
        blend.to = to.getLabel();
        blend.duration = duration;

        return blend;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public float getDuration() {
        return duration;
    }
}
