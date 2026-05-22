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

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;

public class EnhancedWand extends Buff {

    {
        type = buffType.POSITIVE;
        announced = true;
        revivePersists = true;
    }

    public static final float DURATION = 1_000_000;
    
    private int enhancementLevel = -1; // -1이면 Statistics.spw2 사용, 그 외에는 해당 값 사용
    private int permanentEnhancementLevel = 0;
    private int temporaryEnhancementLevel = 0;
    private float temporaryDuration = -1; // -1이면 영구, 그 외에는 임시 버프
    private float initialDuration = -1; // 초기 duration 저장 (iconFadePercent 계산용)

    @Override
    public int icon() {
        return BuffIndicator.UPGRADE;
    }

    @Override
    public String desc() {
        int level = getEnhancementLevel();
        if (temporaryDuration > 0) {
            return Messages.get(this, "desc", level) + "\n\n" + Messages.get(this, "remaining", (int)temporaryDuration);
        }
        return Messages.get(this, "desc", level);
    }
    
    @Override
    public float iconFadePercent() {
        if (temporaryDuration > 0 && initialDuration > 0) {
            return Math.max(0, (initialDuration - temporaryDuration) / initialDuration);
        }
        return 0;
    }
    
    public int getEnhancementLevel() {
        return Statistics.spw2 + permanentEnhancementLevel + temporaryEnhancementLevel;
    }
    
    public void setEnhancementLevel(int level) {
        enhancementLevel = level;
        permanentEnhancementLevel = level;
    }

    public void setTemporaryEnhancement(int level, float duration) {
        temporaryEnhancementLevel = level;
        temporaryDuration = duration;
        initialDuration = duration;
    }
    
    public void setTemporaryDuration(float duration) {
        temporaryEnhancementLevel = Math.max(0, enhancementLevel);
        temporaryDuration = duration;
        initialDuration = duration;
    }

    public void setPermanent() {
        temporaryDuration = -1;
        initialDuration = -1;
        temporaryEnhancementLevel = 0;
        permanentEnhancementLevel = Math.max(permanentEnhancementLevel, Math.max(0, enhancementLevel));
    }
    
    public boolean isPermanent() {
        return temporaryDuration < 0 && temporaryEnhancementLevel <= 0;
    }

    private boolean hasPersistentEnhancement() {
        return Statistics.spw2 + permanentEnhancementLevel > 0;
    }

    private void clearTemporaryEnhancement() {
        temporaryEnhancementLevel = 0;
        temporaryDuration = -1;
        initialDuration = -1;
    }
    
    @Override
    public boolean act() {
        if (temporaryDuration > 0) {
            temporaryDuration--;
            if (temporaryDuration <= 0) {
                clearTemporaryEnhancement();
                if (!hasPersistentEnhancement()) {
                    detach();
                }
            }
        }
        spend(TICK);
        return true;
    }

    @Override
    public void detach() {
        if (hasPersistentEnhancement()) {
            clearTemporaryEnhancement();
        } else {
            super.detach();
        }
    }

    @Override
    public void tintIcon(Image icon) {
        icon.hardlight(1, 0.5f, 0.8f); // 핑크색 (사격 DISC 강화)
    }

    @Override
    public String toString() {
        return Messages.get(this, "name");
    }
    
    private static final String ENHANCEMENT_LEVEL = "enhancement_level";
    private static final String PERMANENT_ENHANCEMENT_LEVEL = "permanent_enhancement_level";
    private static final String TEMPORARY_ENHANCEMENT_LEVEL = "temporary_enhancement_level";
    private static final String TEMPORARY_DURATION = "temporary_duration";
    private static final String INITIAL_DURATION = "initial_duration";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(ENHANCEMENT_LEVEL, enhancementLevel);
        bundle.put(PERMANENT_ENHANCEMENT_LEVEL, permanentEnhancementLevel);
        bundle.put(TEMPORARY_ENHANCEMENT_LEVEL, temporaryEnhancementLevel);
        bundle.put(TEMPORARY_DURATION, temporaryDuration);
        bundle.put(INITIAL_DURATION, initialDuration);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        if (bundle.contains(ENHANCEMENT_LEVEL)) {
            enhancementLevel = bundle.getInt(ENHANCEMENT_LEVEL);
        } else {
            enhancementLevel = -1; // 기존 세이브 파일 호환성: 키가 없으면 -1 (Statistics.spw2 사용)
        }
        if (bundle.contains(PERMANENT_ENHANCEMENT_LEVEL)) {
            permanentEnhancementLevel = bundle.getInt(PERMANENT_ENHANCEMENT_LEVEL);
        } else if (enhancementLevel >= 0 && !bundle.contains(TEMPORARY_DURATION)) {
            permanentEnhancementLevel = enhancementLevel;
        }
        if (bundle.contains(TEMPORARY_ENHANCEMENT_LEVEL)) {
            temporaryEnhancementLevel = bundle.getInt(TEMPORARY_ENHANCEMENT_LEVEL);
        }
        if (bundle.contains(TEMPORARY_DURATION)) {
            temporaryDuration = bundle.getFloat(TEMPORARY_DURATION);
            if (!bundle.contains(TEMPORARY_ENHANCEMENT_LEVEL) && temporaryDuration > 0 && enhancementLevel >= 0) {
                temporaryEnhancementLevel = enhancementLevel;
            }
        } else {
            temporaryDuration = -1;
        }
        if (!bundle.contains(PERMANENT_ENHANCEMENT_LEVEL) && enhancementLevel >= 0 && temporaryDuration <= 0) {
            permanentEnhancementLevel = enhancementLevel;
        }
        if (bundle.contains(INITIAL_DURATION)) {
            initialDuration = bundle.getFloat(INITIAL_DURATION);
        } else {
            initialDuration = -1;
        }
    }

}
