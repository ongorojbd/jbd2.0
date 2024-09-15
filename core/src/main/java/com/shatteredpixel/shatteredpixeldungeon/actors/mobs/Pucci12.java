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
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blizzard;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.CorrosiveGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Dominion;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Electricity;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Fire;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Freezing;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Inferno;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ParalyticGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Dread;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Roots;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Sleep;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Sbr8;
import com.shatteredpixel.shatteredpixeldungeon.levels.CityBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.PucciSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Music;
import com.watabou.utils.Bundle;
import com.watabou.utils.Point;
import com.watabou.utils.Random;
import com.watabou.utils.Rect;

public class Pucci12 extends Mob {
    private static final String[] LINE_KEYS = {"2", "6", "8"};

    {
        spriteClass = PucciSprite.class;

        immunities.add( Paralysis.class );
        immunities.add( Roots.class );
        immunities.add( Dread.class );
        immunities.add( Terror.class );
        immunities.add( Sleep.class );
        immunities.add( Vertigo.class );
        immunities.add( Blizzard.class );
        immunities.add( CorrosiveGas.class );
        immunities.add( Electricity.class );
        immunities.add( Fire.class );
        immunities.add( Freezing.class );
        immunities.add( Inferno.class );
        immunities.add( ParalyticGas.class );
        immunities.add( ToxicGas.class );
        properties.add(Property.BOSS);
        maxLvl = -9;
        HP = HT = 500;
        defenseSkill = 22;
        EXP = 0;
        state = WANDERING;
        immunities.add(Dominion.class );

    }

    private static final Rect arena = new Rect(1, 25, 14, 38);
    private static final int[] pedestals = new int[4];
    private static int WIDTH = 15;
    static {
        Point c = arena.center();
        pedestals[0] = c.x-3 + (c.y-3) * WIDTH;
        pedestals[1] = c.x+3 + (c.y-3) * WIDTH;
        pedestals[2] = c.x+3 + (c.y+3) * WIDTH;
        pedestals[3] = c.x-3 + (c.y+3) * WIDTH;
    }

    int summonCooldown = 6;
    int amCooldown = 290;
    private static final String SUMMON_COOLDOWN = "summoncooldown";
    private static final String AM_COOLDOWN = "amcooldown";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put(SUMMON_COOLDOWN, summonCooldown);
        bundle.put(AM_COOLDOWN, amCooldown);
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        summonCooldown = bundle.getInt( SUMMON_COOLDOWN );
        amCooldown = bundle.getInt( AM_COOLDOWN );
    }


    @Override
    public void notice() {
        super.notice();
        if (!BossHealthBar.isAssigned()) {
            BossHealthBar.assignBoss(this);
        }
    }

    @Override
    protected boolean act() {
        if (!BossHealthBar.isAssigned()) {
            BossHealthBar.assignBoss(this);
        }

        Music.INSTANCE.play(Assets.Music.CIV, true);

        summonCooldown--;
        amCooldown--;



        if (summonCooldown <= 0 && Dungeon.level instanceof CityBossLevel) {


                Val12 Val12 = new Val12();
                Val12.state = Val12.HUNTING;
                Val12.pos = pedestals[0];
                GameScene.add( Val12 );

                Val12 Val13 = new Val12();
                Val13.state = Val13.HUNTING;
                Val13.pos = pedestals[1];
                GameScene.add( Val13 );

                Val12 Val14 = new Val12();
                Val14.state = Val14.HUNTING;
                Val14.pos = pedestals[2];
                GameScene.add( Val14 );

                Val12 Val15 = new Val12();
                Val15.state = Val15.HUNTING;
                Val15.pos = pedestals[3];
                GameScene.add( Val15 );

                    switch (Random.Int(4)){
                        case 0:
                            ScrollOfTeleportation.appear(this, pedestals[0]);
                            break;
                        case 1:
                            ScrollOfTeleportation.appear(this, pedestals[1]);
                            break;
                        case 2:
                            ScrollOfTeleportation.appear(this, pedestals[2]);
                            break;
                        case 3:
                            ScrollOfTeleportation.appear(this, pedestals[3]);
                            break;
                    }


                summonCooldown = (10);

            yell(Messages.get(this, Random.element( LINE_KEYS )));

            }



        if (amCooldown <= 0 && Dungeon.level instanceof CityBossLevel) {

            damage(999, this);

            amCooldown = (290);

        }
        return super.act();
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 25, 30 );
    }

    @Override
    public int attackSkill( Char target ) {
        return 26;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 10);
    }

    @Override
    public int attackProc( Char enemy, int damage ) {

        damage = super.attackProc( enemy, damage );

        return damage;
    }

    @Override
    public void damage(int dmg, Object src) {

        if (dmg >= 75){
            //takes 20/21/22/23/24/25/26/27/28/29/30 dmg
            // at   20/22/25/29/34/40/47/55/64/74/85 incoming dmg
            dmg = 75;
        }

        super.damage(dmg, src);
    }

    @Override
    public void die(Object cause) {
        super.die(cause);

        GameScene.bossSlain();
        Music.INSTANCE.end();
        yell(Messages.get(this, "defeated"));

        for (Mob mob : (Iterable<Mob>)Dungeon.level.mobs.clone()) {
            if (mob instanceof Val12 ) {
                mob.die( cause );
            }
        }

        Item pick = new Sbr8();
        if (pick.doPickUp( Dungeon.hero )) {
            GLog.p( Messages.capitalize(Messages.get(Dungeon.hero, "you_now_have", pick.name()) ));
        } else {
            Dungeon.level.drop( pick, Dungeon.hero.pos ).sprite.drop();
        }

    }

}