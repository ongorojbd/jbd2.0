/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2023 Evan Debenham
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

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Ooze;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.Ratmogrify;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.levels.ArenaBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.sprites.NikuSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.Zombie2Sprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class Niku extends Mob {

    {
        spriteClass = NikuSprite.class;

        HP = HT = 25;
        defenseSkill = 9;

        EXP = 7;
        maxLvl = 10;
        properties.add(Property.UNDEAD);
        properties.add(Property.DEMONIC);

    }

    @Override
    public int attackProc( Char enemy, int damage ) {
        if (Random.Int( 2 ) == 0) {
            Buff.affect( enemy, Ooze.class ).set( Ooze.DURATION );
            enemy.sprite.burst( 0x000000, 5 );
        }

        return super.attackProc( enemy, damage );
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 2, 9 );
    }

    @Override
    public int attackSkill( Char target ) {
        return 12;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 5);
    }

    @Override
    public void die(Object cause) {

        super.die(cause);

        if (!(Dungeon.level instanceof ArenaBossLevel)) {
            if (Random.Int(3) == 0) {
                Dungeon.level.drop(new Gold().quantity(Random.IntRange(45, 55)), pos).sprite.drop();
            }
        }

        if (Dungeon.level.heroFOV[pos]) {
            Sample.INSTANCE.play(Assets.Sounds.BURNING);
        }

    }
}