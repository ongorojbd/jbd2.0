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

import static com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Zombie.spwPrize;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.CursedBlow;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Spw;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.CursedWand;
import com.shatteredpixel.shatteredpixeldungeon.levels.TendencyLevel;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GnollSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ZombieSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ZombiedogSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class Zombiedog extends Mob {

    {
        spriteClass = ZombiedogSprite.class;

        HP = HT = 8;
        defenseSkill = 2;
        baseSpeed = 1.5f;
        EXP = 2;

        maxLvl = 5;

        if (!(Dungeon.level instanceof TendencyLevel)) {
            loot = Generator.Category.SEED;
            lootChance = 0.25f;
        }

        properties.add(Property.UNDEAD);
        properties.add(Property.DEMONIC);
    }

    @Override
    public int attackProc( Char enemy, int damage ) {
        damage = super.attackProc( enemy, damage );

        if (Random.Int( 2 ) == 0) {
            Ballistica aim = new Ballistica(pos, enemy.pos, Ballistica.STOP_TARGET);
            CursedWand.randomValidEffect(null, this, aim, false).effect(null, this, aim, false);
        }

        return damage;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 1, 4 );
    }

    @Override
    public int attackSkill( Char target ) {
        return 8;
    }

    @Override
    public int drRoll() {
        return super.drRoll() + Random.NormalIntRange(0, 1);
    }

    @Override
    public void die( Object cause ) {

        super.die( cause );

        if(Dungeon.level instanceof TendencyLevel) {
            Statistics.duwang3++;

            if (Statistics.duwang3 == Statistics.duwang2) {
                spwPrize(pos);
            }
        }

        if (Dungeon.level.heroFOV[pos]) {
            Sample.INSTANCE.play( Assets.Sounds.BONES,  Random.Float(1.2f, 0.9f) );
            Sample.INSTANCE.play(Assets.Sounds.BURNING);
        }

    }
}