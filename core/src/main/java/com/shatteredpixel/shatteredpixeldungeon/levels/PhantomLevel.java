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
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Keicho3;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NewShopKeeper;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NewShopKeeper2;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Bmap;
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

public class PhantomLevel extends Level {

    {
        color1 = 0x48763c;
        color2 = 0x59994a;
    }

    @Override
    public void playLevelMusic() {
        Music.INSTANCE.playTracks(
                new String[]{Assets.Music.YUUKI},
                new float[]{1},
                false);
    }

    private static int WIDTH = 31;
    private static int HEIGHT = 31;

    public static final int bottomDoor = 7 * 31 + 15;

    @Override
    public String tilesTex() {
        return Assets.Environment.TILES_DIO;
    }

    @Override
    public String waterTex() {
        return Assets.Environment.WATER_SEWERS;
    }

    @Override
    protected boolean build() {
        setSize(WIDTH, HEIGHT);

        transitions.add(new LevelTransition(this, 15 + WIDTH*15, LevelTransition.Type.BRANCH_ENTRANCE, Dungeon.depth, 0, LevelTransition.Type.BRANCH_EXIT));

        Keicho3 npc = new Keicho3();
		npc.pos = 15 * width() + 2;
		mobs.add( npc );

        NewShopKeeper2 NewShopKeeper2 = new NewShopKeeper2();
        NewShopKeeper2.pos = 15 * width() + 28;
        mobs.add( NewShopKeeper2 );

        buildLevel();
        return true;
    }

    private static final short n = -1; //used when a tile shouldn't be changed
    private static final short W = Terrain.WALL;
    private static final short e = Terrain.EMPTY;
    private static final short h = Terrain.EMPTY_SP;
    private static final short s = Terrain.EMPTY;
    private static final short S = Terrain.STATUE_SP;
    private static final short L = Terrain.LOCKED_EXIT;
    private static final short E = Terrain.ENTRANCE_SP;
    private static final short T = Terrain.TRAP;
    private static final short p = Terrain.PEDESTAL;
    private static final short D = Terrain.DOOR;
    private static final short C = Terrain.CRYSTAL_DOOR;
    private static final short B = Terrain.STATUE_SP;
    private static final short A = Terrain.ALCHEMY;
    private static final short r = Terrain.BOOKSHELF;
    private static final short b = Terrain.BARRICADE;
    private static final short H = Terrain.EMPTY_WELL;

    private static final short[] level = {
            W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
            W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
            W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
            W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, L, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
            W, W, W, W, W, W, W, W, h, h, h, h, S, h, h, h, h, h, S, h, h, h, W, W, W, W, W, W, W, W, W,
            W, W, W, W, W, W, W, h, h, e, e, e, e, e, e, e, e, e, e, e, e, h, h, W, W, W, W, W, W, W, W,
            W, W, W, W, W, W, h, h, e, e, e, e, e, e, e, e, e, e, e, e, e, e, h, h, W, W, W, W, W, W, W,
            W, W, W, W, W, h, h, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, h, h, W, W, W, W, W, W,
            W, W, W, W, h, h, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, h, h, W, W, W, W, W,
            W, W, W, W, h, e, e, e, e, W, W, e, e, e, e, e, e, e, e, e, W, W, e, e, e, h, h, W, W, W, W,
            W, W, W, W, h, e, e, e, e, W, e, e, e, e, e, e, e, e, e, e, e, W, e, e, e, e, h, W, W, W, W,
            W, W, W, W, h, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, h, W, W, W, W,
            W, W, W, W, S, e, e, e, e, e, e, e, e, h, e, e, e, h, e, e, e, e, e, e, e, e, S, W, W, W, W,
            W, W, W, W, h, e, e, e, e, e, e, e, h, e, h, h, h, e, h, e, e, e, e, e, e, e, h, W, W, W, W,
            W, W, W, W, h, e, e, e, e, e, e, e, e, h, h, h, h, h, e, e, e, e, e, e, e, e, h, W, W, W, W,
            W, W, s, W, h, e, e, e, e, e, e, e, e, h, h, E, h, h, e, e, e, e, e, e, e, e, h, W, s, W, W,
            W, W, W, W, h, e, e, e, e, e, e, e, e, h, h, h, h, h, e, e, e, e, e, e, e, e, h, W, W, W, W,
            W, W, W, W, h, e, e, e, e, e, e, e, h, e, h, h, h, e, h, e, e, e, e, e, e, e, h, W, W, W, W,
            W, W, W, W, S, e, e, e, e, e, e, e, e, h, e, e, e, h, e, e, e, e, e, e, e, e, S, W, W, W, W,
            W, W, W, W, h, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, h, W, W, W, W,
            W, W, W, W, h, e, e, e, e, W, e, e, e, e, e, e, e, e, e, e, e, W, e, e, e, e, h, W, W, W, W,
            W, W, W, W, h, h, e, e, e, W, W, e, e, e, e, e, e, e, e, e, W, W, e, e, e, h, h, W, W, W, W,
            W, W, W, W, W, h, h, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, h, h, W, W, W, W, W,
            W, W, W, W, W, W, h, h, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, h, h, W, W, W, W, W, W,
            W, W, W, W, W, W, W, h, h, e, e, e, e, e, e, e, e, e, e, e, e, e, h, h, W, W, W, W, W, W, W,
            W, W, W, W, W, W, W, W, h, h, e, e, e, e, e, e, e, e, e, e, e, h, h, W, W, W, W, W, W, W, W,
            W, W, W, W, W, W, W, W, W, h, h, h, S, h, h, h, h, h, S, h, h, h, W, W, W, W, W, W, W, W, W,
            W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
            W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
            W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
            W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W
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
        Random.pushGenerator(Random.Long());
        ArrayList<Item> bonesItems = Bones.get();
        if (bonesItems != null) {
            int pos;
            do {
                pos = randomRespawnCell(null);
            } while (pos == entrance());
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


}