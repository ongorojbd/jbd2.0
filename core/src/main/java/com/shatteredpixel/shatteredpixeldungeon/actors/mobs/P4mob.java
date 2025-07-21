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
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AscensionChallenge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Degrade;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicalSight;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Polpo;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SmokeParticle;
import com.shatteredpixel.shatteredpixeldungeon.journal.Bestiary;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.PassioneSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.BArray;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

//FIXME the AI for these things is becoming a complete mess, should refactor
public class P4mob extends Mob implements Callback {
    private static final float TIME_TO_ZAP = 1f;

    private int level = Dungeon.depth;

    {
        spriteClass = PassioneSprite.Na.class;
        HP = HT = (1 + level) * 6;
        EXP = 0;

        state = WANDERING;
        alignment = Alignment.ALLY;
        intelligentAlly = true;
    }

    private static final String LEVEL = "level";
    private static final String ALIGMNENT = "alignment";

    @Override
    protected boolean canAttack(Char enemy) {
        if (Dungeon.level.adjacent(pos, enemy.pos)) {
            return true;
        }

        if (Dungeon.level.distance(pos, enemy.pos) <= 3) {
            boolean[] passable = BArray.not(Dungeon.level.solid, null);

            for (Char ch : Actor.chars()) {
                passable[ch.pos] = ch == this;
            }

            PathFinder.buildDistanceMap(enemy.pos, passable, 3);

            if (PathFinder.distance[pos] <= 3) {
                return true;
            }
        }

        return super.canAttack(enemy)
                || new Ballistica(pos, enemy.pos, Ballistica.MAGIC_BOLT).collisionPos == enemy.pos;
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
        spend(TIME_TO_ZAP);

        Invisibility.dispel(this);
        Char enemy = this.enemy;
        if (hit(this, enemy, true)) {

            Sample.INSTANCE.play(Assets.Sounds.HIT);
            int dmg = Random.NormalIntRange(1 + level, 2 * level);

            CellEmitter.center(enemy.pos).burst(BlastParticle.FACTORY, 15);
            CellEmitter.center(enemy.pos).burst(SmokeParticle.FACTORY, 4);

            Buff.affect(enemy, Burning.class).reignite(enemy, 1f);

            if (alignment != Alignment.ENEMY) {
                Buff.affect(hero, MagicalSight.class, 1f);
            }

            dmg = Math.round(dmg * AscensionChallenge.statModifier(this));
            enemy.damage(dmg, new P4mob.DarkBolt());

            if (enemy instanceof Mob) {
                ((Mob) enemy).aggro(this);
            }

            if (enemy == hero && !enemy.isAlive()) {
                Badges.validateDeathFromEnemyMagic();
                Dungeon.fail(this);
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
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(LEVEL, level);
        bundle.put(ALIGMNENT, alignment);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        level = bundle.getInt(LEVEL);
        if (bundle.contains(ALIGMNENT)) alignment = bundle.getEnum(ALIGMNENT, Alignment.class);
    }

    @Override
    protected boolean act() {
        if (Dungeon.level.heroFOV[pos]) {
            Bestiary.setSeen(getClass());
        }

        if (alignment == Alignment.ENEMY && Dungeon.hero.buff(Invisibility.class) != null) {
            GLog.n(Messages.get(this, "burst"));
            Buff.detach(Dungeon.hero, Invisibility.class);
        }

        return super.act();
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

        if (enemy instanceof Mob) {
            ((Mob) enemy).aggro(this);
        }
        return damage;
    }

    @Override
    public void die(Object cause) {
        this.yell(Messages.get(this, "die"));
        super.die(cause);
    }

    @Override
    protected Char chooseEnemy() {

        return super.chooseEnemy();
    }

    @Override
    protected boolean getCloser(int target) {

        target = hero.pos;

        return super.getCloser(target);
    }

}