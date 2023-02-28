/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2022 Evan Debenham
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
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.DriedRose;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.BcomTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.DisarmingTrap;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.BcomSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GnollSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class Bcom extends Mob {

    {
        spriteClass = BcomSprite.class;

        HP = HT = 25;
        defenseSkill = 9;

        EXP = 5;
        maxLvl = 10;

        loot = Generator.Category.WEAPON;
        lootChance = 0.1667f; //by default, see lootChance()

        properties.add(Property.UNDEAD);
        properties.add(Property.INORGANIC);
    }

    private boolean seenBefore = false;

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 2, 9 );
    }


    @Override
    public int attackSkill( Char target ) {
        return 12;
    }

    @Override
    protected boolean canAttack(Char enemy) {
        return this.fieldOfView[enemy.pos] && Dungeon.level.distance(this.pos, enemy.pos) <= 2;
    }

    @Override
    public int attackProc( Char enemy, int damage ) {
        Sample.INSTANCE.play(Assets.Sounds.HIT_STAB, 1f, 0.8f);
        return damage;
    }

    @Override
    public float lootChance() {
        //each drop makes future drops 1/2 as likely
        // so loot chance looks like: 1/6, 1/12, 1/24, 1/48, etc.
        return super.lootChance() * (float)Math.pow(1/2f, Dungeon.LimitedDrops.SKELE_WEP.count);
    }

    @Override
    public Item createLoot() {
        Dungeon.LimitedDrops.SKELE_WEP.count++;
        return super.createLoot();
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 5);
    }

}