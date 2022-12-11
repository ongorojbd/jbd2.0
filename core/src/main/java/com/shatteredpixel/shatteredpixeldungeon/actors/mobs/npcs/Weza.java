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

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AscensionChallenge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Bee;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Wezamob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Willamob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Yukakomob;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfIcyTouch;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfMetamorphosis;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Map1;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Sbr1;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.ScrollOfPolymorph;
import com.shatteredpixel.shatteredpixeldungeon.levels.HallsLevel;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.WezaSprite;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class Weza extends NPC {

    {
        spriteClass = WezaSprite.class;

        properties.add(Property.IMMOVABLE);
    }

    private boolean seenBefore = false;

    @Override
    protected boolean act() {
        if (Dungeon.hero.buff(AscensionChallenge.class) != null){
            die(null);
            return true;
        }
        if (!Quest.given && Dungeon.level.heroFOV[pos]) {

            seenBefore = true;
        } else {
            seenBefore = false;
        }

        return super.act();
    }

    @Override
    public int defenseSkill( Char enemy ) {
        return INFINITE_EVASION;
    }

    @Override
    public void damage( int dmg, Object src ) {
    }

    @Override
    public void add( Buff buff ) {
    }

    @Override
    public boolean reset() {
        return true;
    }

    @Override
    public boolean interact(Char c) {

        sprite.turnTo( pos, Dungeon.hero.pos );

        if (c != Dungeon.hero){
            return true;
        }


        //   if (Quest.completed){
        //   }



        if (Quest.given) {

            if (Dungeon.hero.belongings.getItem(ScrollOfPolymorph.class) != null) {
                    Game.runOnRenderThread(new Callback() {
                        @Override
                        public void call() {
                            GameScene.show(new WndOptions(
                                    sprite(),
                                    Messages.titleCase(name()),
                                    Messages.get(Weza.class, "2"),
                                    Messages.get(Weza.class, "3"),
                                    Messages.get(Weza.class, "4")
                                    )
                                           {
                                               @Override
                                               protected void onSelect(int index) {
                                                   if (index == 0){
                                                       Sample.INSTANCE.play( Assets.Sounds.READ );
                                                       yell(Messages.get(Weza.class, "8"));

                                                       Wezamob Wezamob = new Wezamob();
                                                       Wezamob.state = Wezamob.HUNTING;
                                                       Wezamob.pos = pos;
                                                       GameScene.add( Wezamob );
                                                       Wezamob.beckon(Dungeon.hero.pos);

                                                       destroy();
                                                       sprite.killAndErase();
                                                       die(null);

                                                       ScrollOfPolymorph m = Dungeon.hero.belongings.getItem(ScrollOfPolymorph.class);
                                                       m.detachAll(Dungeon.hero.belongings.backpack);
                                                       Quest.completed = true;


                                                   } else if (index == 1) {

                                                       yell(Messages.get(Weza.class, "6"));

                                                   }
                                               }
                                           }
                            );
                        }
                    });
            }
            else  {
                Game.runOnRenderThread(new Callback() {
                    @Override
                    public void call() {
                        GameScene.show(new WndOptions(
                                sprite(),
                                Messages.titleCase(name()),
                                Messages.get(Weza.class, "0")));
                    }
                });



                //sprite.showStatus(CharSprite.POSITIVE, Messages.get(this, "say"));
            }
        } else {
            Quest.given = true;
            Quest.completed = false;

        }

        return true;
    }

    private void tell( String text ) {

    }

    public void flee() {
        destroy();
        sprite.die();
    }

    public static class Quest {

        private static boolean alternative;

        private static boolean spawned;
        private static boolean given;
        private static boolean completed;

        public static Ring reward;

        public static void reset() {
            spawned = false;

            reward = null;
        }

        private static final String NODE		= "demon";

        private static final String ALTERNATIVE	= "alternative";
        private static final String SPAWNED		= "spawned";
        private static final String GIVEN		= "given";
        private static final String COMPLETED	= "completed";
        private static final String REWARD		= "reward";

        public static void storeInBundle( Bundle bundle ) {

            Bundle node = new Bundle();

            node.put( SPAWNED, spawned );

            if (spawned) {
                node.put( ALTERNATIVE, alternative );

                node.put( GIVEN, given );
                node.put( COMPLETED, completed );
                node.put( REWARD, reward );
            }

            bundle.put( NODE, node );
        }

        public static void restoreFromBundle( Bundle bundle ) {

            Bundle node = bundle.getBundle( NODE );

            if (!node.isNull() && (spawned = node.getBoolean( SPAWNED ))) {
                alternative	= node.getBoolean( ALTERNATIVE );

                given = node.getBoolean( GIVEN );
                completed = node.getBoolean( COMPLETED );
                reward = (Ring)node.get( REWARD );
            }
        }

        public static void spawn(HallsLevel level ) {
            if (!spawned && Dungeon.depth > 22 && Random.Int( 24 - Dungeon.depth ) == 0) {

                Weza npc = new Weza();
                do {
                    npc.pos = level.randomRespawnCell( npc );
                } while (
                        npc.pos == -1 ||
                                level.heaps.get( npc.pos ) != null ||
                                level.traps.get( npc.pos) != null ||
                                level.findMob( npc.pos ) != null ||
                                //The imp doesn't move, so he cannot obstruct a passageway
                                !(level.passable[npc.pos + PathFinder.CIRCLE4[0]] && level.passable[npc.pos + PathFinder.CIRCLE4[2]]) ||
                                !(level.passable[npc.pos + PathFinder.CIRCLE4[1]] && level.passable[npc.pos + PathFinder.CIRCLE4[3]]));
                level.mobs.add( npc );

                spawned = true;

                given = false;

            }
        }

        public static void process( Mob mob ) {

        }

        public static void complete() {
            reward = null;
            completed = true;

        }

        public static boolean isCompleted() {
            return completed;
        }
    }
}
