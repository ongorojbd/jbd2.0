/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2025 Evan Debenham
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
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;

public class EnhancedThrownWeapon extends Buff {

    {
        type = buffType.POSITIVE;
        announced = true;
    }

    public static final float DURATION = 1_000_000;
    
    private int enhancementLevel = -1; // -1이면 Statistics.spw1 사용, 그 외에는 해당 값 사용

    @Override
    public int icon() {
        return BuffIndicator.UPGRADE;
    }

    @Override
    public String desc() {
        int level = getEnhancementLevel();
        return Messages.get(this, "desc", level);
    }

    @Override
    public void tintIcon(Image icon) {
        icon.hardlight(0.8f, 0.6f, 0.2f); // 노란색 (투척무기 강화)
    }

    @Override
    public String toString() {
        return Messages.get(this, "name");
    }
    
    public int getEnhancementLevel() {
        if (Dungeon.tendencylevel) {
            return Statistics.spw1;
        }
        if (enhancementLevel >= 0) {
            return enhancementLevel;
        }
        return Statistics.spw1;
    }
    
    public void setEnhancementLevel(int level) {
        enhancementLevel = level;
    }
    
    private static final String ENHANCEMENT_LEVEL = "enhancement_level";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(ENHANCEMENT_LEVEL, enhancementLevel);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        enhancementLevel = bundle.getInt(ENHANCEMENT_LEVEL);
    }

}

