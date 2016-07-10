package com.greenbatgames.smoosh.animation;

import com.greenbatgames.smoosh.entity.Bug;
import com.greenbatgames.smoosh.util.Enums;

/**
 * Created by Quiv on 10-07-2016.
 */
public class EffectFactory
{
    private EffectFactory() {}


    /**
     * Factory method for making AnimationEffects
     *
     * @param type
     * @param bug
     * @return null if the passed type is not handled
     */
    public static final AnimationEffect makeEffect(Enums.EffectType type, Bug bug)
    {
        switch (type)
        {
            case MATCH_FLAME:           return new MatchFlameEffect(bug);
            default:                    return null;
        }
    }
}
