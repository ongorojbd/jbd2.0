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

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Roots;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Silence;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.TargetedCell;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SmokeParticle;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.Scorpio2Sprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ScorpioSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.GameMath;
import com.watabou.utils.Random;

public class Manhatan2 extends Mob {

    {
        spriteClass = Scorpio2Sprite.class;

        HP = HT = 50;
        defenseSkill = 4;

        EXP = -1;
        maxLvl = 8;

        viewDistance = 6;

        properties.add(Property.BOSS);
        properties.add(Property.IMMOVABLE);
    }

    public int Phase = 1;
    private int CoolDown = 2;
    private int LastPos = -1;

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( PHASE, Phase );
        bundle.put( CD, CoolDown );
        bundle.put( SKILLPOS, LastPos );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        Phase = bundle.getInt(PHASE);
        CoolDown = bundle.getInt(CD);
        LastPos = bundle.getInt(SKILLPOS);
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(1, 3);
    }

    @Override
    public int attackSkill(Char target) {
        return 10;
    }

    @Override
    protected boolean canAttack(Char enemy) {
        return !Dungeon.level.adjacent(pos, enemy.pos)
                && (super.canAttack(enemy) || new Ballistica(pos, enemy.pos, Ballistica.PROJECTILE).collisionPos == enemy.pos);
    }

    @Override
    public void notice() {
        super.notice();
        if (!BossHealthBar.isAssigned()) {
            BossHealthBar.assignBoss(this);
        }
    }

    @Override
    public void die(Object cause) {

        for (Mob mob : (Iterable<Mob>)Dungeon.level.mobs.clone()) {
            if (mob instanceof Manhatan || mob instanceof Whitesnakeboss) {
                mob.die( cause );
            }
        }

        Buff.affect(Dungeon.hero, Blindness.class, 30f);
        Buff.affect(Dungeon.hero, Silence.class, 30f);
//        Buff.affect(Dungeon.hero, Roots.class, 30f);

        GameScene.bossSlain();
        Music.INSTANCE.end();

        Statistics.spw6 = 5;

        super.die(cause);
    }

    @Override
    protected boolean act() {
        if (CoolDown == 0) {
            if (LastPos == -1) {
                sprite.showStatus(CharSprite.WARNING, "[받아라!]");
                LastPos = Dungeon.hero.pos;
                sprite.parent.addToBack(new TargetedCell(LastPos, 0xFF00FF));

                spend(GameMath.gate(TICK, Dungeon.hero.cooldown(), 3*TICK));
                Dungeon.hero.interrupt();
                return true;
            }
            else  {
                if (LastPos == Dungeon.hero.pos) {
                    Dungeon.hero.damage(damageRoll(), this);
                    CellEmitter.center(hero.pos).burst(SmokeParticle.FACTORY, 3);
                    Camera.main.shake(5, 0.5f);
                    Sample.INSTANCE.play(Assets.Sounds.HIT_STAB);
                    CoolDown = 4;
                    LastPos = -1;
                    spend( TICK );
                    return true;
                }
                else {
                    CellEmitter.center(LastPos).burst(SmokeParticle.FACTORY, 3);
                    Camera.main.shake(5, 0.5f);
                    Sample.INSTANCE.play(Assets.Sounds.HIT_STAB);
                    CoolDown = 4;
                    LastPos = -1;
                }
            }

        }
        else CoolDown--;

        return super.act();
    }

    private static final String PHASE   = "Phase";
    private static final String CD   = "CoolDown";
    private static final String SKILLPOS   = "LastPos";



}
