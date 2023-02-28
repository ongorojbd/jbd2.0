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
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AscensionChallenge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.levels.PrisonLevel;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.PolpoSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ResearcherSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.RohanSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class Rohan extends NPC {

    {
        spriteClass = RohanSprite.class;

        properties.add(Property.IMMOVABLE);
    }

    private boolean seenBefore = false;

    @Override
    protected boolean act() {
        if (Dungeon.hero.buff(AscensionChallenge.class) != null){
            die(null);
            return true;
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

        if (Statistics.duwang > 2) {
            yell( Messages.get(Rohan.class, "bi") );
            destroy();
            sprite.killAndErase();
            die(null);
        }

        if (Statistics.duwang < 3) {
                Game.runOnRenderThread(new Callback() {
                    @Override
                    public void call() {

                        GameScene.show(new WndOptions(
                                sprite(),
                                Messages.titleCase(name()),
                                Messages.get(Rohan.class, "i"),
                                Messages.get(Rohan.class, "1"),
                                Messages.get(Rohan.class, "2")
                        ){
                            @Override
                            protected void onSelect(int index) {
                                if (index == 0){
                                    Game.runOnRenderThread(new Callback() {
                                        @Override
                                        public void call() {
                                            GameScene.show(new WndOptions(
                                                    sprite(),
                                                    Messages.titleCase(name()),
                                                    Messages.get(Rohan.class, "4"),
                                                    Messages.get(Rohan.class, "5"),
                                                    Messages.get(Rohan.class, "6"),
                                                    Messages.get(Rohan.class, "7"),
                                                    Messages.get(Rohan.class, "8"),
                                                    Messages.get(Rohan.class, "9")
                                            ){
                                                @Override
                                                protected void onSelect(int index) {
                                                    if (index == 0){
                                                        if (Dungeon.gold > 49) {
                                                            Statistics.duwang += 1;
                                                            Sample.INSTANCE.play(Assets.Sounds.GOLD);
                                                            switch (Random.Int(2)){
                                                                case 0:
                                                                    Dungeon.gold += 50;
                                                                    GLog.p( Messages.get(Rohan.class, "w") );
                                                                    yell(Messages.get(Rohan.class, "lose"));
                                                                    break;
                                                                case 1:
                                                                    Dungeon.gold -= 50;
                                                                    GLog.h( Messages.get(Rohan.class, "l") );
                                                                    yell(Messages.get(Rohan.class, "win"));
                                                                    break;
                                                            }


                                                        }
                                                        else
                                                        yell(Messages.get(Rohan.class, "no"));
                                                    }
                                                    else if (index == 1) {
                                                        if (Dungeon.gold > 99) {

                                                            switch (Random.Int(2)){
                                                                case 0:
                                                                    Dungeon.gold += 100;
                                                                    Statistics.duwang += 1;
                                                                    Sample.INSTANCE.play(Assets.Sounds.GOLD);
                                                                    GLog.p( Messages.get(Rohan.class, "w") );
                                                                    yell(Messages.get(Rohan.class, "lose"));
                                                                    break;
                                                                case 1:
                                                                    Dungeon.gold -= 100;
                                                                    Statistics.duwang += 1;
                                                                    Sample.INSTANCE.play(Assets.Sounds.MIMIC);
                                                                    GLog.h( Messages.get(Rohan.class, "l") );
                                                                    yell(Messages.get(Rohan.class, "win"));
                                                                    break;
                                                            }

                                                        }
                                                        else
                                                            yell(Messages.get(Rohan.class, "no"));
                                                    }
                                                    else if (index == 2) {
                                                        if (Dungeon.gold > 249) {
                                                            Statistics.duwang += 1;
                                                            Sample.INSTANCE.play(Assets.Sounds.GOLD);
                                                            switch (Random.Int(2)){
                                                                case 0:
                                                                    Dungeon.gold += 250;
                                                                    GLog.p( Messages.get(Rohan.class, "w") );
                                                                    yell(Messages.get(Rohan.class, "lose"));
                                                                    break;
                                                                case 1:
                                                                    Dungeon.gold -= 250;
                                                                    GLog.h( Messages.get(Rohan.class, "l") );
                                                                    yell(Messages.get(Rohan.class, "win"));
                                                                    break;
                                                            }

                                                        }
                                                        else
                                                            yell(Messages.get(Rohan.class, "no"));
                                                    }
                                                    else if (index == 3) {
                                                        if (Dungeon.gold > 499) {
                                                            Statistics.duwang += 1;
                                                            Sample.INSTANCE.play(Assets.Sounds.GOLD);
                                                            switch (Random.Int(2)){
                                                                case 0:
                                                                    Dungeon.gold += 500;
                                                                    GLog.p( Messages.get(Rohan.class, "w") );
                                                                    yell(Messages.get(Rohan.class, "lose"));
                                                                    break;
                                                                case 1:
                                                                    Dungeon.gold -= 500;
                                                                    GLog.h( Messages.get(Rohan.class, "l") );
                                                                    yell(Messages.get(Rohan.class, "win"));
                                                                    break;
                                                            }


                                                        }
                                                        else
                                                            yell(Messages.get(Rohan.class, "no"));
                                                    }
                                                    else if (index == 4) {
                                                        if (Dungeon.gold > 999) {
                                                            Statistics.duwang += 1;
                                                            Sample.INSTANCE.play(Assets.Sounds.GOLD);
                                                            switch (Random.Int(2)){
                                                                case 0:
                                                                    Dungeon.gold += 1000;
                                                                    GLog.p( Messages.get(Rohan.class, "w") );
                                                                    yell(Messages.get(Rohan.class, "lose"));
                                                                    break;
                                                                case 1:
                                                                    Dungeon.gold -= 1000;
                                                                    GLog.h( Messages.get(Rohan.class, "l") );
                                                                    yell(Messages.get(Rohan.class, "win"));
                                                                    break;
                                                            }


                                                        }
                                                        else
                                                            yell(Messages.get(Rohan.class, "no"));
                                                    }
                                                }
                                            });
                                        }
                                    });
                                } else if (index == 1) {

                                    yell(Messages.get(Rohan.class, "play"));

                                }
                            }
                        });
                    }
                }  );}



        return true;
    }

    public void flee() {
        destroy();
        sprite.die();
    }

    public static void spawn(PrisonLevel level) {
        if (Dungeon.depth == 6 && !Dungeon.bossLevel()) {

            Rohan npc = new Rohan();
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
        }
    }

}




