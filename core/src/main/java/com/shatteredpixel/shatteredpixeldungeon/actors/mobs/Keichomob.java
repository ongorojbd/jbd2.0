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
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.level;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blizzard;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.CorrosiveGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Electricity;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Fire;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Freezing;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Inferno;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ParalyticGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ShrGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.StenchGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.StormCloud;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Chill;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LostInventory;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicalSleep;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Sleep;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Keicho;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.AntiMagic;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Araki;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Kinga;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Kingc;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Kingm;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Kings;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Kingt;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Kingw;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Xray;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfAdvanceguard;
import com.shatteredpixel.shatteredpixeldungeon.levels.TempleLastLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.KeichoSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Keichomob extends Mob {

    private static final String[] LINE_KEYS = {"1", "2", "3", "4", "5", "6", "7"};

    {
        spriteClass = KeichoSprite.class;

        HP = HT = 25;
        defenseSkill = 0;

        EXP = 0;

        state = PASSIVE;

        properties.add(Property.IMMOVABLE);
        properties.add(Property.MINIBOSS);

        immunities.add( Blizzard.class );
        immunities.add( CorrosiveGas.class );
        immunities.add( ShrGas.class );
        immunities.add( Electricity.class );
        immunities.add( Fire.class );
        immunities.add( Freezing.class );
        immunities.add( Inferno.class );
        immunities.add( ParalyticGas.class );
        immunities.add( StenchGas.class );
        immunities.add( StormCloud.class );
        immunities.add( Paralysis.class );
        immunities.add( Amok.class );
        immunities.add( Sleep.class );
        immunities.add( Terror.class );
        immunities.add( Vertigo.class );
        immunities.add( ToxicGas.class );
        immunities.add( Chill.class );
        immunities.add( MagicalSleep.class );
    }

    private static final String SPAWN_COOLDOWN = "spawnCooldown";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(SPAWN_COOLDOWN, spawnCooldown);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        spawnCooldown = bundle.getFloat(SPAWN_COOLDOWN);
    }

    private float spawnCooldown = 5;

    @Override
    public int drRoll() {
        return super.drRoll() + Random.NormalIntRange(0, 15);
    }

    @Override
    public void beckon(int cell) {
        //do nothing
    }

    @Override
    protected boolean getCloser(int target) {
        return true;
    }

    @Override
    protected boolean getFurther(int target) {
        return true;
    }

    @Override
    public boolean reset() {
        return true;
    }

    @Override
    protected boolean act() {

        BossHealthBar.assignBoss(this);

        if (fieldOfView == null || fieldOfView.length != level.length()) {
            fieldOfView = new boolean[level.length()];
        }
        level.updateFieldOfView(this, fieldOfView);

        if (properties().contains(Property.IMMOVABLE)) {
            throwItems();
        }

        if (spawnCooldown > 20) {
            spawnCooldown = 20;
        }

        if (hero != null) {
            if (fieldOfView[hero.pos]
                    && level.map[hero.pos] == Terrain.EMPTY_SP
                    && hero.buff(LostInventory.class) == null) {
                spawnCooldown--;
            } else {
                sprite.idle();
            }
            spawnCooldown--;

            if (spawnCooldown <= 0) {

                //we don't want spawners to store multiple brutes
                if (spawnCooldown < -20) {
                    spawnCooldown = -20;
                }

                ArrayList<Integer> candidates = new ArrayList<>();
                for (int n : PathFinder.NEIGHBOURS8) {
                    if (level.passable[pos + n] && Actor.findChar(pos + n) == null) {
                        candidates.add(pos + n);
                    }
                }

                if (!candidates.isEmpty()) {
                    Mob spawn;
                    switch (Random.Int(3)) {
                        case 0: default:
                            spawn = new TempleLastLevel.TempleGuard();
                            break;
                        case 1:
                            spawn = new TempleLastLevel.TempleBrute();
                            break;
                        case 2:
                            spawn = new TempleLastLevel.TemplePurpleShaman();
                            break;
                    }

                    spawn.pos = 8+15*31;
                    spawn.state = spawn.HUNTING;

                    GameScene.add(spawn);
                    level.occupyCell(spawn);

                    Mob spawn2;
                    switch (Random.Int(3)) {
                        case 0: default:
                            spawn2 = new TempleLastLevel.TempleGuard();
                            break;
                        case 1:
                            spawn2 = new TempleLastLevel.TempleBrute();
                            break;
                        case 2:
                            spawn2 = new TempleLastLevel.TemplePurpleShaman();
                            break;
                    }

                    spawn2.pos = 22+15*31;
                    spawn2.state = spawn2.HUNTING;

                    GameScene.add(spawn2);
                    level.occupyCell(spawn2);


                    Mob spawn3;
                    switch (Random.Int(3)) {
                        case 0: default:
                            spawn3 = new TempleLastLevel.TempleGuard();
                            break;
                        case 1:
                            spawn3 = new TempleLastLevel.TempleBrute();
                            break;
                        case 2:
                            spawn3 = new TempleLastLevel.TemplePurpleShaman();
                            break;
                    }

                    spawn3.pos = 15+22*31;
                    spawn3.state = spawn3.HUNTING;

                    GameScene.add(spawn3);
                    level.occupyCell(spawn3);

                    ((KeichoSprite)sprite).leapPrep(hero.pos);

                    switch (Random.Int( 3 )) {
                        case 0:
                            Sample.INSTANCE.play( Assets.Sounds.K1 );
                            break;
                        case 1:
                            Sample.INSTANCE.play( Assets.Sounds.K2 );
                            break;
                        case 2:
                            Sample.INSTANCE.play( Assets.Sounds.K3 );
                            break;
                    }

                    yell( Messages.get(Keicho.class, Random.element( LINE_KEYS )));
                    spawnCooldown += 20;

                }
            }
            alerted = false;
        }
        return super.act();
    }

    @Override
    public void die(Object cause) {
        super.die(cause);

        for (Mob mob : (Iterable<Mob>)Dungeon.level.mobs.clone()) {
            if (mob instanceof Bcomsoldier || mob instanceof Bcomsolg || mob instanceof Bcopter || mob instanceof Btank) {
                mob.die( cause );
            }
        }

        Item prize = Random.oneOf(
                new Kingt().quantity(1),
                new StoneOfAdvanceguard().quantity(1),
                new Xray().quantity(1),
                new Kings().quantity(1),
                new Kingm().quantity(1),
                new Kingw().quantity(1),
                new Kingc().quantity(1),
                new Kinga().quantity(1)

        );

        Dungeon.level.drop( prize, pos ).sprite.drop( pos );
        Dungeon.level.drop( new Araki(), pos ).sprite.drop( pos );


        yell( Messages.get(this, "defeated") );
    }

}
