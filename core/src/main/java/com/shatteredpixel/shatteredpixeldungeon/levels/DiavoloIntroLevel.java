/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2021 Evan Debenham
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
 * along with this program.  If noe, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon.levels;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Bones;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Bcomsoldier;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Bcopter;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Btank;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Annasui;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Weza;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Yasu;
import com.shatteredpixel.shatteredpixeldungeon.items.Bcomdisc;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Bmap;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Kinga;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Kingc;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Kingm;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Kings;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Kingt;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Kingw;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Xray;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfAdvanceguard;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Music;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class DiavoloIntroLevel extends Level {

    {
        color1 = 0x48763c;
        color2 = 0x59994a;
    }

    @Override
    public void playLevelMusic() {
        Music.INSTANCE.playTracks(
                new String[]{Assets.Music.PRISON_TENSE},
                new float[]{1},
                false);
    }

    private static int WIDTH = 31;
    private static int HEIGHT = 31;

    public static final int bottomDoor = 7 * 31 + 15;

    @Override
    public String tilesTex() {
        return Assets.Environment.TILES_TENDENCY;
    }

    @Override
    public String waterTex() {
        return Assets.Environment.WATER_PRISON;
    }

    @Override
    protected boolean build() {
        setSize(WIDTH, HEIGHT);

        transitions.add(new LevelTransition(this, 15 + WIDTH*15, LevelTransition.Type.BRANCH_ENTRANCE, Dungeon.depth, 0, LevelTransition.Type.BRANCH_EXIT));

        Yasu npc = new Yasu();
        npc.pos = 15 * width() + 10;
        mobs.add( npc );

        Weza npc2 = new Weza();
        npc2.pos = 11 * width() + 13;
        mobs.add( npc2 );

        Annasui npc3 = new Annasui();
        npc3.pos = 12 * width() + 14;
        mobs.add( npc3 );


        buildLevel();
        return true;
    }

    private static final short n = -1; //used when a tile shouldn't be changed
    private static final short W = Terrain.WALL;
    private static final short w = Terrain.WATER;
    private static final short e = Terrain.EMPTY;
    private static final short E = Terrain.ENTRANCE;
    private static final short c = Terrain.CHASM;
    private static final short p = Terrain.PEDESTAL;
    private static final short D = Terrain.DOOR;
    private static final short C = Terrain.CRYSTAL_DOOR;
    private static final short s = Terrain.STATUE;
    private static final short A = Terrain.ALCHEMY;
    private static final short r = Terrain.BOOKSHELF;
    private static final short b = Terrain.BARRICADE;
    private static final short H = Terrain.EMPTY_WELL;

    private static final short[] level = {
            e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e,
            e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e,
            e, e, p, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, p, e, e,
            e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e,
            e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e,
            e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e,
            e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e,
            e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e,
            e, e, e, e, e, e, w, w, w, w, e, e, e, e, e, e, e, e, e, e, e, W, c, c, c, c, e, e, e, e, e,
            e, e, e, e, e, e, w, w, w, w, e, e, e, e, e, e, e, e, e, e, e, W, c, c, c, c, e, e, e, e, e,
            e, e, e, e, e, e, w, w, w, w, s, e, e, e, e, e, e, e, e, e, e, W, c, c, c, c, e, e, e, e, e,
            e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W, c, c, c, c, e, e, e, e, e,
            e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W, c, c, c, c, e, e, e, e, e,
            e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W, c, c, c, c, e, e, e, e, e,
            e, e, e, e, e, e, w, w, w, w, s, e, e, e, e, e, e, e, e, e, e, W, c, c, c, c, e, e, e, e, e,
            e, e, e, e, e, e, w, w, w, w, e, e, e, e, e, e, e, e, e, e, e, W, c, c, c, c, e, e, e, e, e,
            e, e, e, e, e, e, w, w, w, w, e, e, e, e, e, e, e, e, e, e, e, W, c, c, c, c, e, e, e, e, e,
            e, e, e, e, e, e, w, w, w, w, e, e, e, e, e, e, e, e, e, e, e, W, c, c, c, c, e, e, e, e, e,
            e, e, e, e, e, e, w, w, w, w, e, e, e, e, e, e, e, e, e, e, e, W, c, c, c, c, e, e, e, e, e,
            e, e, e, e, e, e, w, w, w, w, e, e, e, e, e, e, e, e, e, e, e, W, c, c, c, c, e, e, e, e, e,
            e, e, e, e, e, e, w, w, w, w, e, e, e, e, e, e, e, e, e, e, e, W, c, c, c, c, e, e, e, e, e,
            e, e, e, e, e, e, w, w, w, w, e, e, e, e, e, e, e, e, e, e, e, W, c, c, c, c, e, e, e, e, e,
            e, e, e, e, e, e, w, w, w, w, e, e, e, e, e, e, e, e, e, e, e, W, c, c, c, c, e, e, e, e, e,
            e, e, e, e, e, e, w, w, w, w, e, e, e, e, e, e, e, e, e, e, e, W, c, c, c, c, e, e, e, e, e,
            e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W, c, c, c, c, e, e, e, e, e,
            e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e,
            e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e,
            e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e,
            e, e, p, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, p, e, e,
            e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e,
            e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W
    };

    private void buildLevel(){
        int pos = 0 + 0*width();
        short[] levelTiles = level;
        ArrayList<Integer> spawnerPos = new ArrayList<>();

        for (short levelTile : levelTiles) {
            if (levelTile != n) map[pos] = levelTile;


            pos++;
        }

    }

    @Override
    protected void createMobs() {
    }

    @Override
    protected void createItems() {
        Item prize = Random.oneOf(
                new Kingt().quantity(1),
                new StoneOfAdvanceguard().quantity(1),
                new Xray().quantity(1),
                new Kings().quantity(1),
                new Kingm().quantity(1),
                new Kingw().quantity(1),
                new Kingc().quantity(1),
                new Kinga().quantity(1)

        );

        drop( new Bcomdisc(), 15+WIDTH*2).type = Heap.Type.CHEST;

        Random.pushGenerator(Random.Long());
        ArrayList<Item> bonesItems = Bones.get();
        if (bonesItems != null) {
            int pos;
            do {
                pos = randomRespawnCell(null);
            } while (pos == entrance());
            for (Item i : bonesItems) {
                drop(i, pos).setHauntedIfCursed().type = Heap.Type.REMAINS;
            }
        }
        Random.popGenerator();
    }

    public boolean activateTransition(Hero hero, LevelTransition transition) {
        if (transition.type == LevelTransition.Type.BRANCH_ENTRANCE) {

            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    GameScene.show(new WndOptions(new ItemSprite(ItemSpriteSheet.MAP0),
                            Messages.titleCase(Messages.get(Bmap.class, "name")),
                            Messages.get(Bmap.class, "1"),
                            Messages.get(Bmap.class, "yes"),
                            Messages.get(Bmap.class, "no")){
                        @Override
                        protected void onSelect(int index) {
                            if (index == 0){
                                InterlevelScene.mode = InterlevelScene.Mode.RETURN;
                                InterlevelScene.returnDepth = 22;
                                InterlevelScene.returnBranch = 0;
                                InterlevelScene.returnPos = -1;
                                Game.switchScene(InterlevelScene.class);
                            }
                        }
                    } );
                }
            });
            return false;

        } else {
            return super.activateTransition(hero, transition);
        }
    }

    @Override
    public int randomRespawnCell( Char ch ) {
        int cell;
        do {
            cell = entrance() + PathFinder.NEIGHBOURS8[Random.Int(8)];
        } while (!passable[cell]
                || (Char.hasProp(ch, Char.Property.LARGE) && !openSpace[cell])
                || Actor.findChar(cell) != null);
        return cell;
    }

    public static class TempleBrute extends Bcopter {
        {
            properties.add(Property.BOSS_MINION);
            state = HUNTING;
        }
    }

    public static class TemplePurpleShaman extends Bcomsoldier {
        {
            properties.add(Property.BOSS_MINION);
            state = HUNTING;
        }
    }

    public static class TempleGuard extends Btank {
        {
            properties.add(Property.BOSS_MINION);
            state = HUNTING;
        }
    }
}