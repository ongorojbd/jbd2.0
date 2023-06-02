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
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AscensionChallenge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Fugomob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Yukakomob;
import com.shatteredpixel.shatteredpixeldungeon.levels.CityLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.PrisonLevel;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.BlacksmithSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.FugoSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.RatKingSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.YukakoSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBlacksmith;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndInfoArmorAbility;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndQuest;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Fugo extends NPC {

    {
        spriteClass = FugoSprite.class;

        properties.add(Property.IMMOVABLE);
    }

    private static final String SPAWNED		= "spawned";
    private static boolean spawned;

    @Override
    protected boolean act() {
        if (Dungeon.hero.buff(AscensionChallenge.class) != null){
            die(null);
            return true;
        }
        return super.act();
    }

    @Override
    public void beckon (int cell) {
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
    public boolean add( Buff buff ) {
        return false;
    }

    @Override
    public boolean interact(Char c) {


        sprite.turnTo( pos, c.pos );

        if (c != Dungeon.hero){
            return true;
        }

        {

            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    GameScene.show(new WndOptions(
                            sprite(),
                            Messages.titleCase(name()),
                            Messages.get(Fugo.class, "0"),
                            Messages.get(Fugo.class, "1"),
                            Messages.get(Fugo.class, "2")
                    ){
                        @Override
                        protected void onSelect(int index) {
                            if (index == 0){
                                yell(Messages.get(Fugo.class, "4"));

                                destroy();
                                sprite.killAndErase();
                                die(null);

                                Fugomob Fugomob = new Fugomob();
                                Fugomob.state = Fugomob.HUNTING;
                                Fugomob.pos = pos;
                                GameScene.add( Fugomob );
                                Fugomob.beckon(Dungeon.hero.pos);

                            } else {
                                yell(Messages.get(Fugo.class, "5"));
                            }
                        }
                    });
                }
            });

        }
        return true;
    }

    @Override
    public int defenseSkill( Char enemy ) {
        return INFINITE_EVASION;
    }

    @Override
    public void damage( int dmg, Object src ) {
    }

    public static void spawn(CityLevel level) {

        if (Random.Int( 1 ) == 0) {
            if (Dungeon.depth == 17 && !Dungeon.bossLevel()) {

                Fugo npc = new Fugo();
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

    @Override
    public boolean reset() {
        return true;
    }

}
