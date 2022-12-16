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
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Wezamob;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.ScrollOfPolymorph;
import com.shatteredpixel.shatteredpixeldungeon.levels.HallsLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.PrisonLevel;
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

        return true;
    }

    private void tell( String text ) {

    }

    public void flee() {
        destroy();
        sprite.die();
    }


    public static void spawn(HallsLevel level) {
        if (Dungeon.depth == 23 && !Dungeon.bossLevel()) {

            Weza centinel = new Weza();
            do {
                centinel.pos = level.randomRespawnCell(centinel);
            } while (centinel.pos == -1);
            level.mobs.add(centinel);
        }
    }




}
