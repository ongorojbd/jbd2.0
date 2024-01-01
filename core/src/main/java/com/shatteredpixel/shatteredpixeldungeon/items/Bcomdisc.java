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

package com.shatteredpixel.shatteredpixeldungeon.items;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ShovelDigCoolDown5;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.huntress.SpiritHawk;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Bcomg;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Bcomsoldier;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Bcomsolg2;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Bcopter;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Btank;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.SummonElemental;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Bcomdisc extends Item {

    private static String AC_USE = "USE";

    {
        image = ItemSpriteSheet.BCOM;
        defaultAction = AC_USE;
        unique = true;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add( AC_USE );
        return actions;
    }

    @Override
    public void execute( final Hero hero, String action ) {

        ArrayList<Integer> spawnPoints = new ArrayList<>();

        super.execute( hero, action );

        if (action.equals( AC_USE )) {
            if (hero.buff(ShovelDigCoolDown5.class) != null){
                GLog.w(Messages.get(this, "not_ready"));
            }
            else {

                for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
                    int p = hero.pos + PathFinder.NEIGHBOURS8[i];
                    if (Actor.findChar( p ) == null && Dungeon.level.passable[p]) {
                        spawnPoints.add( p );
                    }
                }

                if (!spawnPoints.isEmpty()){
                    GLog.p(Messages.get(this, "go"));
                    new Flare(6, 32).color(0x00FF00, true).show(hero.sprite, 3f);
                    Sample.INSTANCE.play( Assets.Sounds.K1);
                    Buff.affect(hero, ShovelDigCoolDown5.class, ShovelDigCoolDown5.DURATION);
                    Dungeon.hero.sprite.emitter().burst(Speck.factory(Speck.RED_LIGHT),12);

                    Bcomsolg2 elemental = new Bcomsolg2();
                    GameScene.add( elemental );
                    Buff.affect(elemental, SummonElemental.InvisAlly.class);
                    ScrollOfTeleportation.appear( elemental, Random.element(spawnPoints) );

                    Bcomsoldier q = new Bcomsoldier();
                    GameScene.add( q );
                    Buff.affect(q, SummonElemental.InvisAlly.class);
                    ScrollOfTeleportation.appear( q, Random.element(spawnPoints) );

                    Btank e = new Btank();
                    GameScene.add( e );
                    Buff.affect(e, SummonElemental.InvisAlly.class);
                    ScrollOfTeleportation.appear( e, Random.element(spawnPoints) );

                    Bcopter r= new Bcopter();
                    GameScene.add( r );
                    Buff.affect(r, SummonElemental.InvisAlly.class);
                    ScrollOfTeleportation.appear( r, Random.element(spawnPoints) );

                } else {
                    GLog.w(Messages.get(SpiritHawk.class, "no_space"));
                }

                hero.sprite.operate(hero.pos);
            }
        }
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    @Override
    public int value() {
        return 30 * quantity;
    }


}