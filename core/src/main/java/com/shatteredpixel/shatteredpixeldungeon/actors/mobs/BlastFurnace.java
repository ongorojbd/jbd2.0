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

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Fire;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Dread;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Sleep;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.levels.ForgeBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.BlastFurnaceSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class BlastFurnace extends Mob {

    {
        spriteClass = BlastFurnaceSprite.class;

        HP = HT = 80;
        defenseSkill = 4;

        maxLvl = -2;

        state = PASSIVE;
        alignment = Alignment.NEUTRAL;

        properties.add(Property.INORGANIC);
        properties.add(Property.IMMOVABLE);
        properties.add(Property.MINIBOSS);
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 6);
    }

    @Override
    public void beckon(int cell) {
        //do nothing
    }

    @Override
    public boolean interact(Char c) {
        return true;
    }

    @Override
    public String description() {
        if (alignment == Alignment.NEUTRAL){
            return Messages.get(this, "desc_inactive");
        } else {
            return Messages.get(this, "desc_active");
        }
    }

    @Override
    public boolean reset() {
        return true;
    }

    private float spawnCooldown = 3;

    public boolean spawnRecorded = false;

    @Override
    protected boolean act() {
        alerted = false;



        if (Dungeon.level instanceof ForgeBossLevel) level = (ForgeBossLevel)Dungeon.level;
        if (level.furnaceactive == true) {


            alignment = Alignment.ENEMY;
            ((BlastFurnaceSprite) sprite).activate();


            spawnCooldown--;
            if (spawnCooldown <= 0) {
                ArrayList<Integer> candidates = new ArrayList<>();
                for (int n : PathFinder.NEIGHBOURS8) {
                    if (Dungeon.level.passable[pos + n] && Actor.findChar(pos + n) == null) {
                        candidates.add(pos + n);
                    }
                }

            }
        }
        return super.act();
    }

    ForgeBossLevel level;

    @Override
    public void damage(int dmg, Object src) {
        if (Dungeon.level instanceof ForgeBossLevel) level = (ForgeBossLevel)Dungeon.level;
        if (level.furnaceactive == true) {
            if (dmg >= 20) {
                //takes 20/21/22/23/24/25/26/27/28/29/30 dmg
                // at   20/22/25/29/34/40/47/55/64/74/85 incoming dmg
                dmg = 19 + (int) (Math.sqrt(8 * (dmg - 19) + 1) - 1) / 2;
            }
            spawnCooldown -= dmg;
            super.damage(dmg, src);
        }
    }

    @Override
    public void die(Object cause) {
        GLog.h(Messages.get(this, "on_death"));
        super.die(cause);
    }

    public static final String SPAWN_COOLDOWN = "spawn_cooldown";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(SPAWN_COOLDOWN, spawnCooldown);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        spawnCooldown = bundle.getFloat(SPAWN_COOLDOWN);
    }

    {
        immunities.add( Paralysis.class );
        immunities.add( Amok.class );
        immunities.add( Sleep.class );
        immunities.add( Dread.class );
        immunities.add( Terror.class );
        immunities.add( Vertigo.class );

        immunities.add(Fire.class);
    }
}
