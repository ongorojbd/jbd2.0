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

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DestOrbSprite;
import com.watabou.utils.Random;

public class DestOrb extends Mob {

    {
        spriteClass = DestOrbSprite.class;

        HP = HT = 10;
        defenseSkill = 10;
        viewDistance = 99;

        EXP = 0;

        state = HUNTING;

        baseSpeed = 1.2f;

        maxLvl = -9;

        flying = true;

    }

    @Override
    public int damageRoll() {
        if (Statistics.johnnyquest) {
            return Random.NormalIntRange( 25, 35 );
        } else {
            return Random.NormalIntRange( 20, 25 );
        }

    }

    @Override
    public int attackSkill( Char target ) {
        return 999;
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        this.die(null);
        return damage;
    }

    @Override
    public void damage(int dmg, Object src) {

        if (dmg >= 10){
            //takes 20/21/22/23/24/25/26/27/28/29/30 dmg
            // at   20/22/25/29/34/40/47/55/64/74/85 incoming dmg
            dmg = 10;
        }

        super.damage(dmg, src);
    }
}