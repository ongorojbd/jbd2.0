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
import static com.shatteredpixel.shatteredpixeldungeon.Statistics.spw11;
import static com.shatteredpixel.shatteredpixeldungeon.Statistics.wave;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SmokeParticle;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SpwSoldierSprite;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class SpwSoldier extends Mob implements Callback {

    {
        spriteClass = SpwSoldierSprite.class;

        HP = HT = 3;
        defenseSkill = 0;
        EXP = 0;
        maxLvl = -9;
        viewDistance = 5;
        if (spw11 > 4) viewDistance = 9;
        else if (spw11 > 0) viewDistance = 7;
        alignment = Alignment.ALLY;
        intelligentAlly = true;
    }


    @Override
    public int damageRoll() {
        if (enemy != null && !Dungeon.level.adjacent(pos, enemy.pos)) {
            return Char.combatRoll(1, 5);
        } else {
            return Char.combatRoll(0, 1);
        }
    }

    @Override
    public int attackSkill(Char target) {
        return INFINITE_ACCURACY;
    }

    @Override
    public int drRoll() {
        return super.drRoll() + Random.NormalIntRange(0, 2);
    }

    @Override
    public void damage(int dmg, Object src) {

        if (dmg >= 1) {
            //takes 20/21/22/23/24/25/26/27/28/29/30 dmg
            // at   20/22/25/29/34/40/47/55/64/74/85 incoming dmg
            dmg = 1;
        }
        super.damage(dmg, src);
    }


    @Override
    public int attackProc(Char enemy, int damage) {
        damage = super.attackProc(enemy, damage);

        if (enemy instanceof Mob) {
            ((Mob) enemy).aggro(this);
        }

        return damage;
    }

    @Override
    protected boolean canAttack(Char enemy) {
        return new Ballistica(pos, enemy.pos, Ballistica.MAGIC_BOLT).collisionPos == enemy.pos;
    }

    protected boolean doAttack(Char enemy) {

        if (Dungeon.level.adjacent(pos, enemy.pos)
                || new Ballistica(pos, enemy.pos, Ballistica.MAGIC_BOLT).collisionPos != enemy.pos) {
            return super.doAttack(enemy);

        } else {

            if (sprite != null && (sprite.visible || enemy.sprite.visible)) {
                sprite.zap(enemy.pos);
                return false;
            } else {
                zap();
                return true;
            }
        }
    }

    public static class DarkBolt {
    }

    protected void zap() {

        if (spw11 > 6) spend(2f);
        else spend(3f);

        Invisibility.dispel(this);
        Char enemy = this.enemy;
        if (hit(this, enemy, true)) {

            int waveDamage = wave / 5;
            int dmg = Random.NormalIntRange(3 + waveDamage, 5 + waveDamage);

            CellEmitter.center(enemy.pos).burst(BlastParticle.FACTORY, 15);
            CellEmitter.center(enemy.pos).burst(SmokeParticle.FACTORY, 4);

            enemy.damage(dmg, new DarkBolt());

            if (enemy instanceof Mob) {
                ((Mob) enemy).aggro(this);
            }

            if (spw11 > 5) {
                if (Random.Int(4) == 0) {
                    Buff.affect(enemy, Paralysis.class, 1f);
                }
            }

            if (spw11 > 1) {
                if (Random.Int(4) == 0) {
                    Buff.affect(enemy, Burning.class).reignite(enemy, 2f);
                }
            }

            if (!enemy.isAlive() && spw11 > 3) {

                for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
                    if (mob.alignment == Alignment.ALLY && Dungeon.level.distance(this.pos, mob.pos) <= 3) {
                        Buff.affect(mob, Barrier.class).setShield(3);
                    }
                }

                if (Dungeon.level.distance(this.pos, hero.pos) <= 3)
                    Buff.affect(hero, Barrier.class).setShield(3);
            }

        } else {
            enemy.sprite.showStatus(CharSprite.NEUTRAL, enemy.defenseVerb());
        }
    }

    public void onZapComplete() {
        zap();
        next();
    }

    @Override
    public void call() {
        next();
    }

    @Override
    protected boolean getCloser(int target) {

        target = hero.pos;

        return super.getCloser(target);
    }

}
