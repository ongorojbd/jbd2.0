/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2026 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.levels.HospitalLevel;
import com.shatteredpixel.shatteredpixeldungeon.sprites.AmbulanceSprite;

/**
 * The moving vehicle used by HospitalLevel. Indestructible background prop, not the protect
 * objective itself (see Patient for that) - modeled on Tower Pixel Dungeon's DrillBig.
 * All wave/movement decisions are driven by HospitalLevel via onAmbulanceTurn(), this class
 * just carries the body and sprite along.
 */
public class Ambulance extends Mob {

    {
        //all of these match DrillBig's own field block
        spriteClass = AmbulanceSprite.class;
        state = WANDERING;
        HP = HT = 8055847;
        defenseSkill = 0;
        alignment = Alignment.NEUTRAL;
        viewDistance = 10;
        properties.add(Property.IMMOVABLE);
        EXP = 2;
        maxLvl = 10;
    }

    @Override
    public boolean canInteract(Char c) {
        //same as DrillBig. Swapping places with the hero used to move the ambulance's anchor
        //cell, which shifted the Patient and the shove box relative to the sprite. Riding along
        //is handled by HospitalLevel.stepForward()'s box instead.
        return false;
    }

    @Override
    public void damage(int dmg, Object src) {
        //indestructible
    }

    @Override
    protected Char chooseEnemy() {
        return null;
    }

    @Override
    public boolean attack(Char enemy, float dmgMulti, float dmgBonus, float accMulti) {
        return false;
    }

    @Override
    public boolean isImmune(Class effect) {
        return true;
    }

    @Override
    public int defenseProc(Char enemy, int damage) {
        return 0;
    }

    @Override
    protected boolean act() {
        if (Dungeon.level instanceof HospitalLevel) {
            ((HospitalLevel) Dungeon.level).onAmbulanceTurn(this);
        }
        spend(TICK);
        return true;
    }
}
