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

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Ooze;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.GooBlob;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CreamSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GooSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class Cream extends Mob {

    {
        spriteClass = CreamSprite.class;

        HP = HT = 15;
        defenseSkill = 10;
        viewDistance = 99;

        EXP = 0;

        state = HUNTING;

        maxLvl = -9;

        flying = true;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 0, Dungeon.depth*2 );
    }

    @Override
    public int attackSkill( Char target ) {
        return 999;
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        Buff.affect(enemy, Ooze.class).set( Ooze.DURATION );
        Buff.prolong( enemy, Blindness.class, Blindness.DURATION );
        Buff.prolong( enemy, Cripple.class, Cripple.DURATION );
        this.die(null);
        return damage;
    }

    @Override
    public void die(Object cause) {

        Sample.INSTANCE.play( Assets.Sounds.BLAST );

        super.die(cause);
    }

}