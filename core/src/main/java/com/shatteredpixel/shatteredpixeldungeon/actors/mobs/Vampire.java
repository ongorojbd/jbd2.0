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
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.CursedBlow;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.TargetedCell;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BloodParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.VSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.GameMath;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class Vampire extends Mob {

    {
        spriteClass = VSprite.class;

        // Stronger baseline than VampireTest
        HP = HT = 80;
        defenseSkill = 12;
        baseSpeed = 1.2f;

        EXP = 7;
        maxLvl = 15;

        properties.add(Property.UNDEAD);
        properties.add(Property.DEMONIC);

        HUNTING = new Vampire.Hunting();

    }

    private float leapCooldown = 0;
    private int lastEnemyPos = -1;
    private int leapPos = -1;

    @Override
    protected boolean act() {

        // Passive regeneration when wounded (more vampiric resilience)
        if (HP < HT * 2 / 3) {
            HP += 1;
            if (Dungeon.level.heroFOV[pos]) {
                sprite.emitter().burst(Speck.factory(Speck.HEALING), 1);
                CellEmitter.center(this.pos).burst(BloodParticle.BURST, 2);
            }
        }

        // Adrenaline-style surge at low HP
//        if (HP <= HT / 3 && !adrenalineTriggered) {
//            Buff.affect(this, Adrenaline.class, 10f);
//            adrenalineTriggered = true;
//            if (Dungeon.level.heroFOV[pos]) {
//                CellEmitter.center(this.pos).burst(BloodParticle.BURST, 10);
//            }
//        }

        if (leapCooldown > 0) leapCooldown--;
        return super.act();
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 10, 16 );
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        damage = super.attackProc(enemy, damage);

        if (Random.Int( 3 ) == 0) {
            if(enemy instanceof Hero || enemy instanceof CaesarZeppeli) {
                Buff.affect(enemy, CursedBlow.class, 3f);
                sprite.showStatus(CharSprite.WARNING, Messages.get(this, "frenzy"));
            }

        }

        // 33% chance to inflict bleeding on hit (stronger than VampireTest)
        if (Random.Int(3) == 0) {
            Buff.affect(enemy, Bleeding.class).set(0.5f * damage);
            if (Dungeon.level.heroFOV[pos]) {
                CellEmitter.center(this.pos).burst(BloodParticle.BURST, 3);
            }
        }

        // Lifesteal: heal for a portion of damage (increased if target is bleeding)
        float leechRatio = enemy.buff(Bleeding.class) != null ? 0.4f : 0.2f;
        int heal = Math.min(Math.round(damage * leechRatio), HT - HP);
        if (heal > 0) {
            HP += heal;
            sprite.showStatusWithIcon(CharSprite.POSITIVE, Integer.toString(heal), FloatingText.HEALING);
        }

        return damage;
    }

    @Override
    public int attackSkill( Char target ) {
        return 16;
    }

    @Override
    public int drRoll() {
        return super.drRoll() + Random.NormalIntRange(0, 5);
    }

    @Override
    public void die( Object cause ) {

        super.die( cause );

        if (Random.Int( 3 ) == 0) {
            Dungeon.level.drop( new Gold().quantity(Random.IntRange(45, 55)), pos ).sprite.drop();
        }

        if (Dungeon.level.heroFOV[pos]) {
            Sample.INSTANCE.play( Assets.Sounds.BONES,  Random.Float(1.2f, 0.9f) );
            Sample.INSTANCE.play(Assets.Sounds.BURNING);
        }

    }

    public class Hunting extends Mob.Hunting {

        @Override
        public boolean act( boolean enemyInFOV, boolean justAlerted ) {

            if (leapPos != -1){
                leapCooldown = Random.NormalIntRange(3, 6);

                // do the leap following VampireTest pattern
                Ballistica b = new Ballistica(pos, leapPos, Ballistica.STOP_TARGET | Ballistica.STOP_SOLID);

                if (rooted || b.collisionPos != leapPos){
                    leapPos = -1;
                    return true;
                }

                final Char leapVictim = Actor.findChar(leapPos);
                final int endPos;

                if (leapVictim != null){
                    int bouncepos = -1;
                    for (int i : PathFinder.NEIGHBOURS8){
                        if ((bouncepos == -1 || Dungeon.level.trueDistance(pos, leapPos+i) < Dungeon.level.trueDistance(pos, bouncepos))
                                && Actor.findChar(leapPos+i) == null && Dungeon.level.passable[leapPos+i]){
                            bouncepos = leapPos+i;
                        }
                    }
                    if (bouncepos == -1) {
                        leapPos = -1;
                        return true;
                    } else {
                        endPos = bouncepos;
                    }
                } else {
                    endPos = leapPos;
                }

                sprite.visible = Dungeon.level.heroFOV[pos] || Dungeon.level.heroFOV[leapPos] || Dungeon.level.heroFOV[endPos];
                sprite.jump(pos, leapPos, new com.watabou.utils.Callback() {
                    @Override
                    public void call() {
                        if (leapVictim != null && alignment != leapVictim.alignment){
                            Buff.affect(leapVictim, Bleeding.class).set(0.8f*damageRoll());
                            leapVictim.sprite.flash();
                            Sample.INSTANCE.play(Assets.Sounds.HIT);
                        }

                        if (endPos != leapPos){
                            Actor.addDelayed(new Pushing(Vampire.this, leapPos, endPos), -1);
                        }

                        pos = endPos;
                        leapPos = -1;
                        sprite.idle();
                        Dungeon.level.occupyCell(Vampire.this);
                        next();
                    }
                });
                return false;
            }

            enemySeen = enemyInFOV;
            if (enemyInFOV && !isCharmedBy( enemy ) && canAttack( enemy )) {

                return doAttack( enemy );

            } else {

                if (enemyInFOV) {
                    target = enemy.pos;
                } else if (enemy == null) {
                    state = WANDERING;
                    target = Dungeon.level.randomDestination( Vampire.this );
                    return true;
                }

                if (leapCooldown <= 0 && enemyInFOV && !rooted
                        && Dungeon.level.distance(pos, enemy.pos) >= 3) {

                    int targetPos = enemy.pos;
                    if (lastEnemyPos != enemy.pos){
                        // keep logic structure consistent with VampireTest but simplify (we aim directly at enemy)
                        targetPos = enemy.pos;
                    }

                    Ballistica b = new Ballistica(pos, targetPos, Ballistica.STOP_TARGET | Ballistica.STOP_SOLID);
                    if (b.collisionPos != targetPos && targetPos != enemy.pos){
                        targetPos = enemy.pos;
                        b = new Ballistica(pos, targetPos, Ballistica.STOP_TARGET | Ballistica.STOP_SOLID);
                    }
                    if (b.collisionPos == targetPos){
                        leapPos = targetPos;
                        spend(GameMath.gate(TICK, enemy.cooldown(), 3*TICK));
                        if (Dungeon.level.heroFOV[pos] || Dungeon.level.heroFOV[leapPos]){
                            sprite.parent.addToBack(new TargetedCell(leapPos, 0xFF00FF));
                            Dungeon.hero.interrupt();
                        }
                        return true;
                    }
                }

                int oldPos = pos;
                if (target != -1 && getCloser( target )) {

                    spend( 1 / speed() );
                    return moveSprite( oldPos,  pos );

                } else {
                    spend( TICK );
                    if (!enemyInFOV) {
                        sprite.showLost();
                        state = WANDERING;
                        target = Dungeon.level.randomDestination( Vampire.this );
                    }
                    return true;
                }
            }
        }

    }
}