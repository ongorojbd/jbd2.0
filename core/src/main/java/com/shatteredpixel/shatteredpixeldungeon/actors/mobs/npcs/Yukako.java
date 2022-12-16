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
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.Ratmogrify;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Jonny;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Yukakomob;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.BrokenSeal;
import com.shatteredpixel.shatteredpixeldungeon.items.EquipableItem;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.DarkGold;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Pickaxe;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.journal.Notes;
import com.shatteredpixel.shatteredpixeldungeon.levels.CityLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.PrisonLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.SewerLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.BlacksmithRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.DogTrap;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.BlacksmithSprite;
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

public class Yukako extends NPC {

    {
        spriteClass = YukakoSprite.class;

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
                            Messages.get(Yukako.class, "0"),
                            Messages.get(Yukako.class, "1"),
                            Messages.get(Yukako.class, "2"),
                            Messages.get(Yukako.class, "3")
                    ){
                        @Override
                        protected void onSelect(int index) {
                            if (index == 0){
                                Sample.INSTANCE.play(Assets.Sounds.MIMIC);
                                yell(Messages.get(Yukako.class, "4"));

                                destroy();
                                sprite.killAndErase();
                                die(null);

                                Yukakomob Yukakomob = new Yukakomob();
                                Yukakomob.state = Yukakomob.HUNTING;
                                Yukakomob.pos = pos;
                                GameScene.add( Yukakomob );
                                Yukakomob.beckon(Dungeon.hero.pos);

                            } else if (index == 1) {
                                Sample.INSTANCE.play(Assets.Sounds.MIMIC);
                                yell(Messages.get(Yukako.class, "5"));

                                destroy();
                                sprite.killAndErase();
                                die(null);

                                Yukakomob Yukakomob = new Yukakomob();
                                Yukakomob.state = Yukakomob.HUNTING;
                                Yukakomob.pos = pos;
                                GameScene.add( Yukakomob );
                                Yukakomob.beckon(Dungeon.hero.pos);

                            } else {
                                Sample.INSTANCE.play(Assets.Sounds.MIMIC);
                                yell(Messages.get(Yukako.class, "6"));

                                destroy();
                                sprite.killAndErase();
                                die(null);

                                Yukakomob Yukakomob = new Yukakomob();
                                Yukakomob.state = Yukakomob.HUNTING;
                                Yukakomob.pos = pos;
                                GameScene.add( Yukakomob );
                                Yukakomob.beckon(Dungeon.hero.pos);
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

    @Override
    public void add( Buff buff ) {
    }


    public static void spawn(PrisonLevel level) {
        if (Dungeon.depth >= 9 && !Dungeon.bossLevel()) {

            Yukako npc = new Yukako();
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

    @Override
    public boolean reset() {
        return true;
    }

}
