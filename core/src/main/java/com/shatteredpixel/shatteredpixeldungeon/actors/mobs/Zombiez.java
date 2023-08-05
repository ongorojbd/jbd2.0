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

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GnollSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ZombieSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ZombiedSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ZombiezSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class Zombiez extends Mob {

    {
        spriteClass = ZombiezSprite.class;

        HP = HT = 16;
        defenseSkill = 2;

        EXP = 3;
        maxLvl = 9;

        properties.add(Property.UNDEAD);
        properties.add(Property.DEMONIC);

        loot = new PotionOfHealing();
        lootChance = 0.1667f; //by default, see lootChance()
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 2, 5 );
    }

    @Override
    protected boolean canAttack( Char enemy ) {
        return true;
    }

    @Override
    public int attackSkill( Char target ) {
        return 10;
    }

    @Override
    public int drRoll() {
        return super.drRoll() + Random.NormalIntRange(1, 2);
    }

    @Override
    public int attackProc( Char enemy, int damage ) {
        damage = super.attackProc( enemy, damage );
        if (damage > 0 && Random.Int( 2 ) == 0) {
            Buff.affect( enemy, Bleeding.class ).set( damage );
        }

        return damage;
    }

    @Override
    public void die( Object cause ) {

        super.die( cause );

        if (Dungeon.level.heroFOV[pos]) {
            Sample.INSTANCE.play( Assets.Sounds.BONES,  Random.Float(1.2f, 0.9f) );
            Sample.INSTANCE.play(Assets.Sounds.BURNING);
        }
    }

    @Override
    public float lootChance(){
        return super.lootChance() * (5f - Dungeon.LimitedDrops.SWARM_HP.count) / 5f;
    }

    @Override
    public Item createLoot(){
        Dungeon.LimitedDrops.SWARM_HP.count++;
        return super.createLoot();
    }
}