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
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AllyBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AnkhInvulnerability;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bless;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Light;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Roots;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ShovelDigCoolDown6;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.SoulMark;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.DirectableAlly;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.EnergyParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfAggression;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.WillcSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.BArray;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class Tendency extends DirectableAlly {

    {
        spriteClass = WillcSprite.class;

        HP = HT = 30;
        defenseSkill = (Dungeon.hero.lvl + 4);
        viewDistance = 7;
        alignment = Alignment.ALLY;
        intelligentAlly = true;
        immunities.add(AllyBuff.class);
    }

    private int sph = 1;

    public void updateTendencyHT(boolean boostHP) {
        int curHT = HT;

        HT = 30 + 5 * (hero.lvl - 1);

        if (boostHP) {
            HP += Math.max(HT - curHT, 0);
        }

        HP = Math.min(HP, HT);
    }

    @Override
    public String description() {
        String desc = super.description();

        desc += "\n" + Messages.get(this, "p1", this.HP, this.HT);

        return desc;
    }

    @Override
    public void damage(int dmg, Object src) {
        if (dmg >= 50) {
            dmg = 50;
        }

        super.damage(dmg, src);
    }

    @Override
    public int drRoll() {
        return super.drRoll() + hero.drRoll();
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        damage = super.attackProc(enemy, damage);

        if (this.buff(Light.class) != null) Buff.affect(enemy, Paralysis.class, 1f);

        return damage;
    }


    @Override
    public void defendPos(int cell) {
        Sample.INSTANCE.play(Assets.Sounds.CE3);
        yell(Messages.get(this, "direct_defend"));

        if (Statistics.spw9 > 2) {
            if (Dungeon.level.passable[cell] && !(Dungeon.level.pit[cell])
                    && Dungeon.level.openSpace[cell] && Actor.findChar(cell) == null
                    && Dungeon.hero.buff(ShovelDigCoolDown6.class) == null) {
                ScrollOfTeleportation.appear(this, cell);
                super.defendPos(cell);
                Buff.affect(Dungeon.hero, ShovelDigCoolDown6.class, 20f);
            }
        }

        super.defendPos(cell);
    }

    @Override
    public void followHero() {
        Sample.INSTANCE.play(Assets.Sounds.TENDENCY2);
        yell(Messages.get(this, "direct_attack"));
        super.followHero();
    }

    @Override
    public void targetChar(Char ch) {
        Sample.INSTANCE.play(Assets.Sounds.CE4);
        yell(Messages.get(this, "direct_follow"));
        super.targetChar(ch);
    }

    @Override
    public int attackSkill(Char target) {
        return Dungeon.hero.lvl + 9;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(1 + hero.damageRoll() / 2, 3 + hero.damageRoll() / 2);
    }

    @Override
    protected boolean act() {
        int oldPos = pos;
        boolean result = super.act();
        //partially simulates how the hero switches to idle animation
        if ((pos == target || oldPos == pos) && sprite.looping()) {
            sprite.idle();
        }

        Dungeon.level.updateFieldOfView(this, fieldOfView);
        GameScene.updateFog(pos, viewDistance + (int) Math.ceil(speed()));

        return result;
    }

    public void sayHeroKilled() {
        yell(Messages.get(this, "z"));
        Sample.INSTANCE.play(Assets.Sounds.TENDENCY2);
        GLog.newLine();
    }

    public void a2() {
        for (int i : PathFinder.NEIGHBOURS8) {
            int cell = this.pos + i;
            Char ch = Actor.findChar(cell);
            if (ch != null && ch.alignment == Char.Alignment.ENEMY) {
                //trace a ballistica to our target (which will also extend past them
                Ballistica trajectory = new Ballistica(this.pos, ch.pos, Ballistica.STOP_TARGET);
                //trim it to just be the part that goes past them
                trajectory = new Ballistica(trajectory.collisionPos, trajectory.path.get(trajectory.path.size() - 1), Ballistica.PROJECTILE);
                //knock them back along that ballistica
                WandOfBlastWave.throwChar(ch, trajectory, 3, false, true, this);

            }
            if (Dungeon.level.map[cell] == Terrain.DOOR) {
                Level.set(cell, Terrain.OPEN_DOOR);
                Dungeon.observe();
            }
        }
        Sample.INSTANCE.play(Assets.Sounds.BLAST);
        WandOfBlastWave.BlastWave.blast(this.pos);
    }

    public void a3() {
        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
            CellEmitter.get(this.pos + 1).burst(EnergyParticle.FACTORY, 5);
            if (mob.pos == this.pos + 1) {
                mob.damage(5 * Dungeon.depth, this);
                Buff.affect(mob, SoulMark.class, 5f);
            }
        }
        for (Mob mob2 : Dungeon.level.mobs.toArray(new Mob[0])) {
            CellEmitter.get(this.pos - 1).burst(EnergyParticle.FACTORY, 5);
            if (mob2.pos == this.pos - 1) {
                mob2.damage(5 * Dungeon.depth, this);
                Buff.affect(mob2, SoulMark.class, 5f);
            }
        }
        for (Mob mob3 : Dungeon.level.mobs.toArray(new Mob[0])) {
            CellEmitter.get(this.pos + 2).burst(EnergyParticle.FACTORY, 5);
            if (mob3.pos == this.pos + 2) {
                mob3.damage(5 * Dungeon.depth, this);
                Buff.affect(mob3, SoulMark.class, 5f);
            }
        }
        for (Mob mob4 : Dungeon.level.mobs.toArray(new Mob[0])) {
            CellEmitter.get(this.pos - 2).burst(EnergyParticle.FACTORY, 5);
            if (mob4.pos == this.pos - 2) {
                mob4.damage(5 * Dungeon.depth, this);
                Buff.affect(mob4, SoulMark.class, 5f);
            }
        }
    }

    public static void summon() {
        Tendency jojo = new Tendency();
        jojo.state = jojo.WANDERING;
        jojo.pos = Dungeon.hero.pos;
        GameScene.add(jojo);
        jojo.beckon(Dungeon.hero.pos);
    }

    public void heal(double recoveryFactor) {
        HP = (int) Math.min(this.HP + this.HT * recoveryFactor, this.HT);
        sprite.emitter().burst(Speck.factory(Speck.HEALING), 4);
    }

    public boolean a0() {
        int oldPos = pos;
        int newPos = hero.pos;

        PathFinder.buildDistanceMap(hero.pos, BArray.or(Dungeon.level.passable, Dungeon.level.avoid, null));
        if (PathFinder.distance[pos] == Integer.MAX_VALUE) {
            return true;
        }
        yell(Messages.get(this, "a0"));
        Sample.INSTANCE.play(Assets.Sounds.TENDENCY2);

        pos = newPos;
        hero.pos = oldPos;
        ScrollOfTeleportation.appear(this, newPos);
        ScrollOfTeleportation.appear(hero, oldPos);

        Buff.affect(hero, AnkhInvulnerability.class, 1f);
        Buff.affect(this, AnkhInvulnerability.class, 1f);

        Dungeon.observe();
        GameScene.updateFog();

        return true;
    }

    public boolean barrier() {
        yell(Messages.get(this, "a0"));
        Sample.INSTANCE.play(Assets.Sounds.TENDENCY1);
        new Flare(6, 32).color(0x99FFFF, true).show(hero.sprite, 2f);
        new Flare(6, 32).color(0x99FFFF, true).show(this.sprite, 2f);
        Buff.affect(this, Barrier.class).setShield(this.HP / 4);
        Buff.affect(hero, Barrier.class).setShield(hero.HP / 4);
        Buff.affect(hero, Bless.class, 15f);
        Buff.affect(hero, ShovelDigCoolDown6.class, 40f);
        return true;
    }

    public boolean cat() {
        yell(Messages.get(this, "a0"));
        Buff.affect(this, AnkhInvulnerability.class, 5f);
        Buff.affect(this, StoneOfAggression.Aggression.class, 5f);
        Buff.affect(hero, ShovelDigCoolDown6.class, 40f);
        return true;
    }

    @Override
    public boolean isInvulnerable(Class effect) {
        return super.isInvulnerable(effect) || buff(AnkhInvulnerability.class) != null;
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
    }

    @Override
    public void die(Object cause) {
        super.die(cause);

        Statistics.cizah = 1;

        Sample.INSTANCE.play(Assets.Sounds.TENDENCY3);

        yell(Messages.get(this, "death"));
    }
}