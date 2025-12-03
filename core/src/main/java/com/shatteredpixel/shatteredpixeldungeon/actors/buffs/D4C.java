/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2020 Evan Debenham
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

import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.utils.Bundle;

public class D4C extends Buff {

    {
        type = buffType.POSITIVE;
        announced = true;
    }

    public static final float DURATION = 1_000_000;
    
    private float left = DURATION;

    @Override
    public void fx(boolean on) {
        if (on) target.sprite.aura( 0xFF9900, 4 );
        else target.sprite.clearAura();
    }

    @Override
    public int icon() {
        return BuffIndicator.RAGE;
    }

    @Override
    public String toString() {
        return Messages.get(this, "name");
    }
    
    public void oneTurn() {
        left = 1f;
    }
    
    @Override
    public boolean act() {
        if (left < DURATION) {
            // 시간 제한이 있는 경우 (1턴 버프)
            left -= 1f;
            if (left <= 0) {
                detach();
                return true;
            }
        }
        return super.act();
    }
    
    @Override
    public String desc() {
        if (left < DURATION) {
            return Messages.get(this, "desc", dispTurns(Math.max(0, left)));
        }
        return Messages.get(this, "desc");
    }
    
    private static final String LEFT = "left";
    
    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(LEFT, left);
    }
    
    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        if (bundle.contains(LEFT)) {
            left = bundle.getFloat(LEFT);
        } else {
            left = DURATION;
        }
    }

}
