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
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Fire;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ShrGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Doom;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Weakness;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Reimi;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.TargetedCell;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BloodParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SmokeParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.ThirdBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.CeremonialCandle;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.CurseInfusion;
import com.shatteredpixel.shatteredpixeldungeon.levels.CavesBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.DestOrbTrap;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.KousakuSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SWATSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;
import com.watabou.utils.Rect;

public class Kawasiri extends Mob {


    {
        spriteClass = KousakuSprite.class;

        HP = HT = 150;

        EXP = 15;
        defenseSkill = 15;
        properties.add(Property.INORGANIC);
        properties.add(Property.BOSS);
        immunities.add(ShrGas.class);

        state = FLEEING;

    }
    int damageTaken = 0;
    public int  Phase = 0;
    int summonCooldown = 7;
    private static final String SUMMON_COOLDOWN = "summoncooldown";
    private static final String SKILL2TIME   = "BurstTime";
    private static int WIDTH = 33;
    private int BurstTime = 0;
    public static int[] pylonPositions = new int[]{ 4 + 13*WIDTH, 28 + 13*WIDTH, 4 + 37*WIDTH, 28 + 37*WIDTH };
    public static int[] kira = new int[]{ 4 + 13*WIDTH };


    @Override
    public void die( Object cause ) {

        super.die( cause );

        GameScene.bossSlain();

        yell( Messages.get(this, "phase4") );

            Dungeon.level.drop( new CeremonialCandle(), pos ).sprite.drop( pos );


        if (Random.Int( 5 ) == 0) {
            Dungeon.level.drop( new ThirdBomb().identify(), pos ).sprite.drop( pos );
        new Flare( 5, 32 ).color( 0xFFFF00, true ).show( hero.sprite, 2f );
        Sample.INSTANCE.play(Assets.Sounds.BADGE);
        GLog.p(Messages.get(Kawasiri.class, "rare"));
        }
        Music.INSTANCE.end();


        for (Mob mob : (Iterable<Mob>)Dungeon.level.mobs.clone()) {
            if (mob instanceof DestOrb || mob instanceof Amblance    ) {
                mob.die( cause );
            }
        }

        Reimi Reimi = new Reimi();
        Reimi.state = Reimi.PASSIVE;
        Reimi.pos = this.pos;
        GameScene.add( Reimi );
        Reimi.beckon(Dungeon.hero.pos);
    }

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put( PHASE, Phase );
        bundle.put(SUMMON_COOLDOWN, summonCooldown);
        bundle.put( SKILL2TIME, BurstTime );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        Phase = bundle.getInt(PHASE);
        summonCooldown = bundle.getInt( SUMMON_COOLDOWN );
        BurstTime = bundle.getInt(SKILL2TIME);
    }

    @Override
    public void damage(int dmg, Object src) {

        if (dmg >= 50){
            //takes 20/21/22/23/24/25/26/27/28/29/30 dmg
            // at   20/22/25/29/34/40/47/55/64/74/85 incoming dmg
            dmg = 50;
        }

        super.damage(dmg, src);

        if (Phase==0 && HP < 149) {
            Phase = 1;
            HP = 149;

            yell(Messages.get(this, "notice"));
        }
        if (Phase==1 && HP < 147) {
            Phase = 2;
            HP = 147;

            yell(Messages.get(this, "phase1"));
        }
        if (Phase==2 && HP < 135) {
            Phase = 3;
            HP = 134;
            if (!BossHealthBar.isAssigned()) {
                BossHealthBar.assignBoss(this);
                for (Char ch : Actor.chars()){
                }
            }
            yell(Messages.get(this, "phase5"));
            state = HUNTING;
            Music.INSTANCE.play(Assets.Music.CAVES_BOSS, true);
        }
        if (Phase==3 && HP < 75) {
            Phase = 4;
            HP = 74;

            yell(Messages.get(this, "phase2"));

        }
        if (Phase==4 && HP < 54) {
            Phase = 5;
            HP = 53;
            int reg = 101 ;
            Dungeon.hero.damage(Dungeon.hero.HP/3, this);
            CellEmitter.center(Dungeon.hero.pos).burst(BlastParticle.FACTORY, 31);
            CellEmitter.center(Dungeon.hero.pos).burst(SmokeParticle.FACTORY, 4);
            GameScene.flash(0x80FFFFFF);
            Buff.affect(this, Barrier.class).setShield(30);
            Camera.main.shake(11, 3f);
            sprite.emitter().burst( Speck.factory( Speck.HEALING ), 15 );
            Sample.INSTANCE.play(Assets.Sounds.TBOMB);
            yell(Messages.get(this, "phase3"));
            if (reg > 0) {
                HP += reg;
                sprite.emitter().burst(Speck.factory(Speck.HEALING), 1);
            }
            for (int i : pylonPositions) {
                ScrollOfTeleportation.appear(this, i);
            }

            Buff.affect(enemy, Blindness.class, 3f);
            Buff.affect(enemy, Cripple.class,   7f);
            Buff.affect(enemy, Weakness.class, 11f);
            Dungeon.level.cleanWalls();
            yell(Messages.get(this, "bom"));
        }
        if (Phase==5 && HP < 52)
           {
            Phase = 6;
            HP = 51;
            yell(Messages.get(this, "phase7"));
            Music.INSTANCE.play(Assets.Music.KOICHI, true);
            Buff.affect(this, Barrier.class).setShield(30);
            state = FLEEING;

               for (int i : kira) {
               Amblance Amblance = new Amblance();
               Amblance.state = Amblance.WANDERING;
               Amblance.pos = i;
               GameScene.add( Amblance, DELAY );
               Amblance.beckon(Dungeon.hero.pos);
               }


        }

        }

    private static final String PHASE   = "Phase";
    private static final float DELAY = 29f;

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 15, 25 );
    }

    @Override
    public int attackSkill( Char target ) {
        return 20;
    }


    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 10);
    }

    @Override
    public void notice() {
        super.notice();
    }

    @Override
    protected boolean act() {
        summonCooldown--;

        if (Phase == 3){
            if (summonCooldown <= 0 && Dungeon.level instanceof CavesBossLevel) {


                new DestOrbTrap().set(target).activate();


                summonCooldown = (7);

                Sample.INSTANCE.play(Assets.Sounds.CHARGEUP);
            }
        }
        if (Phase == 4){
            if (summonCooldown <= 0 && Dungeon.level instanceof CavesBossLevel) {


                new DestOrbTrap().set(target).activate();


                summonCooldown = (7);

                Sample.INSTANCE.play(Assets.Sounds.CHARGEUP);
            }
        }
        if (Phase == 5){
            if (summonCooldown <= 0 && Dungeon.level instanceof CavesBossLevel) {


                new DestOrbTrap().set(target).activate();


                summonCooldown = (7);

                Sample.INSTANCE.play(Assets.Sounds.CHARGEUP);
            }
        }
        if (Phase == 6){
            if (summonCooldown <= 0 && Dungeon.level instanceof CavesBossLevel) {



                for (int i : PathFinder.NEIGHBOURS1) {
                    DestOrb DestOrb = new DestOrb();
                    DestOrb.state = DestOrb.WANDERING;
                    DestOrb.pos = this.pos+i;
                    GameScene.add( DestOrb );
                    DestOrb.beckon(Dungeon.hero.pos);
                }


                summonCooldown = (5);

                Sample.INSTANCE.play(Assets.Sounds.CHARGEUP);
            }
        }

        return super.act();
    }
}
