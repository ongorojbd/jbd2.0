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
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Daze;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NPC;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.Chasm;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DestOrbSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class Soft extends NPC {

    private int customMinDamage = -1;
    private int customMaxDamage = -1;
    private int attackTargetPos = -1;

    {
        spriteClass = DestOrbSprite.class;

        HP = HT = 80;
        defenseSkill = 999999999;

        EXP = 0;

        alignment = Alignment.ALLY;
        state = HUNTING;

        //before other mobs
        actPriority = MOB_PRIO + 1;

        baseSpeed = 1.5f;

        maxLvl = -9;

        flying = true;

    }

    public void setDamageRange(int min, int max) {
        customMinDamage = min;
        customMaxDamage = max;
    }

    @Override
    public int attackSkill(Char target) {
        return INFINITE_ACCURACY;
    }

    @Override
    public int damageRoll() {
        // Use the same damage as explosion for consistency
        if (customMinDamage >= 0 && customMaxDamage >= 0) {
            return Random.NormalIntRange(customMinDamage, customMaxDamage);
        } else {
            return Random.NormalIntRange(Dungeon.depth, Dungeon.depth*2);
        }
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        // Apply 2 turns of Daze to the enemy
        Buff.prolong(enemy, Daze.class, 2f);
        
        // Save the attack target position to exclude it from explosion damage
        // (since it already received attack damage)
        attackTargetPos = enemy.pos;
        this.die(null);
        return damage;
    }


    @Override
    public void move( int step, boolean travelling) {
        damage(10, this);
        super.move( step, travelling);
    }

    @Override
    public void die( Object cause ) {

        super.die( cause );

        if (cause == Chasm.class) return;

        for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
            int targetPos = pos + PathFinder.NEIGHBOURS8[i];
            // Skip the attack target position to avoid double damage
            if (targetPos == attackTargetPos) {
                continue;
            }
            Char ch = findChar( targetPos );
            if (ch != null && ch.isAlive()) {
                // Do not damage hero
                if (ch == Dungeon.hero) {
                    continue;
                }
                // Use explosion damage source for AntiMagic compatibility
                int damage;
                if (customMinDamage >= 0 && customMaxDamage >= 0) {
                    // Use custom damage range from wand
                    damage = Random.NormalIntRange(customMinDamage, customMaxDamage);
                } else {
                    // Use default depth-based damage
                    damage = Random.NormalIntRange(Dungeon.depth, Dungeon.depth*2);
                }
                damage = Math.max( 0,  damage - (ch.drRoll() + ch.drRoll()) );
                ch.damage( damage, ExplosionDamage.class );
                // Apply 2 turns of Daze to enemies hit by explosion
                Buff.prolong(ch, Daze.class, 2f);
            }
        }
        // Reset attack target position after explosion
        attackTargetPos = -1;

        if (Dungeon.level.heroFOV[pos]) {
            Sample.INSTANCE.play( Assets.Sounds.BLAST );
        }
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(999999999, 999999999);
    }

    // Inner class for explosion damage source (for AntiMagic compatibility)
    public static class ExplosionDamage {
        // This class is used as a damage source identifier for AntiMagic
    }

    private static final String CUSTOM_MIN_DAMAGE = "custom_min_damage";
    private static final String CUSTOM_MAX_DAMAGE = "custom_max_damage";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(CUSTOM_MIN_DAMAGE, customMinDamage);
        bundle.put(CUSTOM_MAX_DAMAGE, customMaxDamage);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        customMinDamage = bundle.getInt(CUSTOM_MIN_DAMAGE);
        customMaxDamage = bundle.getInt(CUSTOM_MAX_DAMAGE);
    }
}
