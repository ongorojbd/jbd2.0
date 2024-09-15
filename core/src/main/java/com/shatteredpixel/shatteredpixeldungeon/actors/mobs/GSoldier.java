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
import static com.shatteredpixel.shatteredpixeldungeon.Statistics.spw10;
import static com.shatteredpixel.shatteredpixeldungeon.Statistics.wave;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vulnerable;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SmokeParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Tbomb;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GSoldierSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class GSoldier extends Mob {

    {
        spriteClass = GSoldierSprite.class;

        HP = HT = 3;
        defenseSkill = 0;
        EXP = 0;
        maxLvl = -9;
        viewDistance = 5;
        if (spw10 > 4) viewDistance = 9;
        else if (spw10 > 0) viewDistance = 7;
        alignment = Alignment.ALLY;
        intelligentAlly = true;
    }

    private int blastcooldown = 3;
    private static final String BLAST_CD = "blastcooldown";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(BLAST_CD, blastcooldown);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        blastcooldown = bundle.getInt(BLAST_CD);
    }

    @Override
    protected boolean act() {

        if (blastcooldown > 0) blastcooldown--;

        return super.act();
    }

    @Override
    public float attackDelay() {
        if (spw10 > 6) return super.attackDelay() * 1.5f;
        else return super.attackDelay() * 2f;
    }

    protected boolean doAttack(Char enemy) {
        if (Dungeon.level.adjacent(pos, enemy.pos) || blastcooldown > 0 || spw10 < 4) {

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

    public void onZapComplete() {
        zap();
        next();
    }

    protected void zap() {
        spend(1f);
        Tbomb tbomb = new Tbomb();
        tbomb.explode(enemy.pos);
        Invisibility.dispel(this);
        blastcooldown = 30;
    }

    @Override
    public int attackSkill(Char target) {
        return hero.lvl + 12;
    }

    @Override
    public int damageRoll() {
        int waveDamage = wave / 5;
        if (spw10 > 1) waveDamage += 2;
        if (enemy != null && !Dungeon.level.adjacent(pos, enemy.pos)) {
            return Random.NormalIntRange(1 + waveDamage, 5 + waveDamage);
        } else {
            return Random.NormalIntRange(waveDamage, 1 + waveDamage);
        }
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
        Sample.INSTANCE.play(Assets.Sounds.HIT_STAB, 1f, 0.8f);
        CellEmitter.get(enemy.pos).burst(SmokeParticle.FACTORY, 2);
        CellEmitter.center(enemy.pos).burst(BlastParticle.FACTORY, 2);

        if (enemy instanceof Mob) {
            ((Mob) enemy).aggro(this);
        }

        if (spw10 > 5) {
            if (Random.Int(4) == 0) {
                Buff.affect(enemy, Vulnerable.class, 2f);
            }
        }

        if (spw10 > 2) {
            if (Random.Int(3) == 0) {
                Ballistica trajectory = new Ballistica(this.pos, enemy.pos, Ballistica.STOP_TARGET);
                trajectory = new Ballistica(trajectory.collisionPos, trajectory.path.get(trajectory.path.size() - 1), Ballistica.PROJECTILE);
                WandOfBlastWave.throwChar(enemy, trajectory, 1, false, true, getClass());
            }
        }

        return damage;
    }

    @Override
    protected boolean canAttack(Char enemy) {
        return true;
    }

    @Override
    protected boolean getCloser(int target) {

        target = hero.pos;

        return super.getCloser(target);
    }

}