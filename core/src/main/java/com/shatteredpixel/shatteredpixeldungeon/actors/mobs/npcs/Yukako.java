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
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Yukakomob;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.PrisonLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
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
                    Sample.INSTANCE.play(Assets.Sounds.YUKAK);
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
                                Sample.INSTANCE.play(Assets.Sounds.YUKAK);
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
                                Sample.INSTANCE.play(Assets.Sounds.YUKAK);
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
                                Sample.INSTANCE.play(Assets.Sounds.YUKAK);
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

    public static void spawn(PrisonLevel level) {
        // 독립적인 시드 오프셋을 사용
        Random.pushGenerator(Dungeon.seedCurDepth() + 999996L);
        boolean shouldSpawn = Random.Int( 2 ) == 0;
        Random.popGenerator();
        
        if (shouldSpawn) {
        if (Dungeon.depth >= 9 && !Dungeon.bossLevel()) {

            Yukako npc = new Yukako();
            do {
                npc.pos = level.randomRespawnCell( npc );
            } while (
                    npc.pos == -1 ||
                            level.heaps.get( npc.pos ) != null ||
                            level.traps.get( npc.pos) != null ||
                            level.findMob( npc.pos ) != null ||
                            level.map[npc.pos] == Terrain.GRASS ||
                            level.map[npc.pos] == Terrain.HIGH_GRASS ||
                            level.map[npc.pos] == Terrain.FURROWED_GRASS ||
                            //The imp doesn't move, so he cannot obstruct a passageway
                            !(level.passable[npc.pos + PathFinder.CIRCLE4[0]] && level.passable[npc.pos + PathFinder.CIRCLE4[2]]) ||
                            !(level.passable[npc.pos + PathFinder.CIRCLE4[1]] && level.passable[npc.pos + PathFinder.CIRCLE4[3]]));
            level.mobs.add( npc );
            
            // 풀 타일을 EMPTY로 변경하여 겹침 방지 (do-while에서 이미 제외했지만 안전을 위해)
            if (level.map[npc.pos] == Terrain.GRASS ||
                    level.map[npc.pos] == Terrain.HIGH_GRASS ||
                    level.map[npc.pos] == Terrain.FURROWED_GRASS) {
                Level.set(npc.pos, Terrain.EMPTY, level);
            } else if (level.map[npc.pos] != Terrain.EMPTY_DECO) {
                Level.set(npc.pos, Terrain.EMPTY, level);
            }
        }
        }
    }

    @Override
    public boolean reset() {
        return true;
    }

}
