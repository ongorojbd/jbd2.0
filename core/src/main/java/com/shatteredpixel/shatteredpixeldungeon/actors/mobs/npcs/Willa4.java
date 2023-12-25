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
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bless;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Willa2;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Willamob;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.levels.DioLevel;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.WillaSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndQuest;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class Willa4 extends NPC {

    {
        spriteClass = WillaSprite.class;

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
                GameScene.show( new WndQuest( Willa4.this, text ));
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
                    Sample.INSTANCE.play( Assets.Sounds.WILLA );
                    GameScene.show(new WndOptions(
                            sprite(),
                            Messages.titleCase(name()),
                            Messages.get(Willa4.class, "0"),
                            Messages.get(Willa4.class, "1"),
                            Messages.get(Willa4.class, "2")
                    ){
                        @Override
                        protected void onSelect(int index) {
                            if (index == 0){
                                switch (Random.Int( 2 )) {
                                    case 0:
                                        GLog.p(Messages.get(Willamob.class, "s"));
                                        GLog.n(Messages.get(Willa4.class, "s"));
                                        Sample.INSTANCE.play(Assets.Sounds.LEVELUP);
                                        Buff.affect(hero, Barrier.class).setShield((int) (0.6f * hero.HT + 10));
                                        Buff.affect(hero, Bless.class, 30f);
                                        new Flare( 5, 32 ).color( 0xFFFF33, true ).show( hero.sprite, 3f );
                                        break;
                                    case 1:
                                        Sample.INSTANCE.play(Assets.Sounds.HIT);
                                        hero.damage(hero.HP/2, this);
                                        Camera.main.shake(9, 0.5f);
                                        GLog.n(Messages.get(Willamob.class, "fail"));
                                        break;
                                }
                                destroy();
                                sprite.killAndErase();
                                die(null);

                                Willa2 Willa2 = new Willa2();
                                Willa2.state = Willa2.HUNTING;
                                Willa2.pos = pos;
                                GameScene.add( Willa2 );
                                Willa2.beckon(hero.pos);

                            } else {
                                yell( Messages.get(Willa4.class, "r"));
                                GLog.n(Messages.get(Willa4.class, "s"));
                                destroy();
                                sprite.killAndErase();
                                die(null);

                                Willa2 Willa2 = new Willa2();
                                Willa2.state = Willa2.HUNTING;
                                Willa2.pos = pos;
                                GameScene.add( Willa2 );
                                Willa2.beckon(hero.pos);

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
            if (Dungeon.depth == 3 && !Dungeon.bossLevel()) {

                Willa4 npc = new Willa4();
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
