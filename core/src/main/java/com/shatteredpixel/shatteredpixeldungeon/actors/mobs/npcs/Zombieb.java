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

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Zombiebr;
import com.shatteredpixel.shatteredpixeldungeon.levels.DioLevel;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ZombiebSprite;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndQuest;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;

public class Zombieb extends NPC {

    {
        spriteClass = ZombiebSprite.class;

        properties.add(Property.IMMOVABLE);
    }


    @Override
    public int defenseSkill( Char enemy ) {
        return INFINITE_EVASION;
    }

    @Override
    public void damage( int dmg, Object src ) {
        //do nothing
    }

    @Override
    public boolean add( Buff buff ) {
        return false;
    }

    @Override
    public boolean reset() {
        return true;
    }

    private void tell( String text ) {
        Game.runOnRenderThread(new Callback() {
            @Override
            public void call() {
                GameScene.show( new WndQuest( Zombieb.this, text ));
            }
        });
    }

    @Override
    public boolean interact(Char c) {


        sprite.turnTo( pos, c.pos );

        if (c != hero){
            return true;
        }

        {

            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    Sample.INSTANCE.play( Assets.Sounds.CHARGEUP );
                    GameScene.show(new WndOptions(
                            sprite(),
                            Messages.titleCase(name()),
                            Messages.get(Zombieb.class, "0"),
                            Messages.get(Zombieb.class, "1"),
                            Messages.get(Zombieb.class, "2")
                    ){
                        @Override
                        protected void onSelect(int index) {
                            if (index == 0){
                                Sample.INSTANCE.play(Assets.Sounds.MIMIC);

                                destroy();
                                sprite.killAndErase();
                                die(null);

                                yell( Messages.get(Zombieb.class, "d"));

                                Zombiebr Willa2 = new Zombiebr();
                                Willa2.state = Willa2.HUNTING;
                                Willa2.pos = pos;
                                GameScene.add( Willa2 );
                                Willa2.beckon(hero.pos);

                            } else {
                                yell( Messages.get(Zombieb.class, "r"));
                            }
                        }
                    });
                }
            });

        }
        return true;
    }

    public void flee() {
        destroy();
        sprite.die();
    }

    public static void spawn(DioLevel level) {
        if (Dungeon.depth == 4 && !Dungeon.bossLevel()) {

            Zombieb npc = new Zombieb();
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
