/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2021 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;

public class Wedding2 extends FlavourBuff implements Hero.Doom {

    public static final float DURATION = 1599f;

    {
        type = buffType.NEUTRAL;
        announced = true;
    }

    private float left;

    private static final String LEFT = "left";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(LEFT, left);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        left = bundle.getFloat(LEFT);
        // safety for older saves where left might be 0
        if (left <= 0) left = DURATION;
    }

    @Override
    public boolean attachTo(Char target) {
        if (super.attachTo(target)) {
            if (left <= 0) left = DURATION;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean act() {
        spend(TICK);

        if (target.isAlive()) {
            left -= TICK;

            // If time runs out: game over regardless of Wamuu state
            if (left <= 0) {
                if (target == Dungeon.hero) Dungeon.hero.die(this);
                else detach();
            }
        } else {
            detach();
        }

        return true;
    }

    @Override
    public int icon() {
        return BuffIndicator.POISON;
    }

    @Override
    public void tintIcon(Image icon) {
        icon.hardlight(1, 0, 0);
    }

    @Override
    public String iconTextDisplay() {
        return Integer.toString((int) Math.max(0, left));
    }

    @Override
    public float iconFadePercent() {
        return Math.max(0, (DURATION - visualcooldown()) / DURATION);
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc", dispTurns());
    }

    // Ensure description/dispTurns() and fade use our custom remaining time
    @Override
    public float cooldown() {
        return left;
    }

    @Override
    public void onDeath() {
        // mark the run as failed to ensure proper ranking handling
        Dungeon.fail(this);
    }
}