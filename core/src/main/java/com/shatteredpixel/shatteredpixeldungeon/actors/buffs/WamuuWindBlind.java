/*
 * Visual-only blindness for Wamuu: shows Blindness icon without affecting vision/logic
 */
package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;

public class WamuuWindBlind extends FlavourBuff {

    {
        type = buffType.NEUTRAL;
        announced = false;
    }

    @Override
    public int icon() {
        return BuffIndicator.BLINDNESS;
    }

    @Override
    public float iconFadePercent() {
        return 0f;
    }
}


