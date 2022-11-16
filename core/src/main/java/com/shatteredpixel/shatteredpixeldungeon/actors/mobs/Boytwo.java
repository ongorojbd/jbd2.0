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
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.BlobImmunity;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Corruption;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Degrade;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MindVision;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NPC;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfCleansing;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.Chasm;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GeyserTrap;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.BoytwoSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DestOrbSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GnollSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class Boytwo extends Mob {

    {
        spriteClass = BoytwoSprite.class;

        HP = HT = 3;

        EXP = 0;
        properties.add(Property.IMMOVABLE);

        maxLvl = -9;
        HUNTING = new Mob.Hunting();

    }

    private int spell = 0; // 1 def, 2 knokback, 3 atk

    @Override
    protected boolean getCloser(int target) {
        return true;
    }

    @Override
    protected boolean getFurther(int target) {
        return true;
    }

    @Override
    public int attackSkill(Char target) {
        return 0;
    }

    @Override
    public int defenseProc(Char enemy, int damage) {

            {
                spell = Random.Int(1, 4);
                switch (spell) {
                    case 1:
                        sprite.showStatus(CharSprite.POSITIVE, Messages.get(this, "G1"));
                        yell(Messages.get(this, "e1"));
                        break;
                    case 2:
                        sprite.showStatus(CharSprite.POSITIVE, Messages.get(this, "G1"));
                        yell(Messages.get(this, "e2"));
                        damage(1, this);
                        break;
                    case 3:
                        sprite.showStatus(CharSprite.POSITIVE, Messages.get(this, "G1"));
                        yell(Messages.get(this, "e3"));
                        Dungeon.hero.damage(Dungeon.hero.HP/3, this);
                        break;
                }

            }


        return super.defenseProc(enemy, damage);
    }


    @Override
    public void die( Object cause ) {

        super.die( cause );

        yell(Messages.get(this, "d2"));

        if (Dungeon.level.heroFOV[pos]) {
            Sample.INSTANCE.play( Assets.Sounds.BLAST );
        }

    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(999,999);
    }
}