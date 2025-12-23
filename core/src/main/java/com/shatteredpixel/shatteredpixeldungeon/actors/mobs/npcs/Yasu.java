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

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AscensionChallenge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.Lighter;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Kinga;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Kingc;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Kingm;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Kings;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Kingt;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Kingw;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Xray;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfAdvanceguard;
import com.shatteredpixel.shatteredpixeldungeon.levels.CavesLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.YasuSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndQuest;
import com.watabou.noosa.Game;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class Yasu extends NPC {

    {
        spriteClass = YasuSprite.class;

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
                GameScene.show( new WndQuest( Yasu.this, text ));
            }
        });
    }

    @Override
    public boolean interact(Char c) {
        sprite.turnTo( pos, Dungeon.hero.pos );

        if (c != Dungeon.hero){
            return true;
        }

        if (Statistics.duwang2 == 0) {

        tell( Messages.get(this, "1") );

        Statistics.duwang2 = 1;
        Statistics.duwang3 = 0;
        } else {
            if (Statistics.duwang3 >= 3) {
                yell( Messages.get(this, "2") );

                Item pick = Random.oneOf(
                        new Kingt().quantity(1),
                        new StoneOfAdvanceguard().quantity(1),
                        new Xray().quantity(1),
                        new Kings().quantity(1),
                        new Kingm().quantity(1),
                        new Kingw().quantity(1),
                        new Kingc().quantity(1),
                        new Kinga().quantity(1)

                );

                if (pick.doPickUp( Dungeon.hero )) {
                    GLog.i( Messages.capitalize(Messages.get(Dungeon.hero, "you_now_have", pick.name()) ));
                } else {
                    Dungeon.level.drop( pick, Dungeon.hero.pos ).sprite.drop();
                }
                destroy();
                sprite.killAndErase();
                die(null);
            } else {
                tell( Messages.get(this, "3") );
            }
        }
        return true;
    }

    public void flee() {
        destroy();
        sprite.die();
    }

    public static void spawn(CavesLevel level) {
        // 독립적인 시드 오프셋을 사용
        Random.pushGenerator(Dungeon.seedCurDepth() + 999995L);
        boolean shouldSpawn = Random.Int( 3 ) == 0;
        Random.popGenerator();
        
        if (shouldSpawn) {
            if (Dungeon.depth == 13 && !Dungeon.bossLevel()) {

                Yasu npc = new Yasu();
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


}
