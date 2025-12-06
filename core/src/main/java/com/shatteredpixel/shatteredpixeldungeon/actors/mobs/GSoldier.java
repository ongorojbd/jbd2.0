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
// removed unused static imports

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
// removed unused buff imports
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SmokeParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Tbomb;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GSoldierSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class GSoldier extends Mob {
    private int level = Dungeon.depth / 3;

    {
        spriteClass = GSoldierSprite.class;
        HP = HT = (2 + level) * 8;
        EXP = 0;

        viewDistance = 5;
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
        return super.attackDelay() * 1.5f;
    }

    protected boolean doAttack(Char enemy) {
        if (Dungeon.level.adjacent(pos, enemy.pos) || blastcooldown > 0) {

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
        tbomb.noFriendlyDamage = true;
        tbomb.thrownByGSoldier = true;
        tbomb.explode(enemy.pos);
        Invisibility.dispel(this);
        blastcooldown = 80;
    }

    @Override
    public int attackSkill(Char target) {
        if (target != null && alignment == Alignment.NEUTRAL && target.invisible <= 0) {
            return INFINITE_ACCURACY;
        } else {
            return 8 + level;
        }
    }

    @Override
    public int damageRoll() {
        if (alignment == Alignment.NEUTRAL) {
            return Random.NormalIntRange(2 + 2 * level, 2 + 2 * level);
        } else {
            return Random.NormalIntRange(1 + level, 2 + 2 * level);
        }
    }

    @Override
    public int drRoll() {
        return super.drRoll() + Random.NormalIntRange(0, 1 + level / 2);
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

        if (Random.Int(6) == 0) {
                Ballistica trajectory = new Ballistica(this.pos, enemy.pos, Ballistica.STOP_TARGET);
                trajectory = new Ballistica(trajectory.collisionPos, trajectory.path.get(trajectory.path.size() - 1), Ballistica.PROJECTILE);
                WandOfBlastWave.throwChar(enemy, trajectory, 1, false, true, getClass());
        }

        return damage;
    }

    @Override
    public void die(Object cause) {
        this.yell(Messages.get(this, "die"));
        super.die(cause);
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