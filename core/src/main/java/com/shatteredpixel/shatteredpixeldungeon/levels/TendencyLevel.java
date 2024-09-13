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
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Keicho2;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NewShopKeeper;
import com.shatteredpixel.shatteredpixeldungeon.items.Bcomdisc;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Bmap;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Music;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class TendencyLevel extends Level {

    {
        color1 = 0x48763c;
        color2 = 0x59994a;
    }

    @Override
    public void playLevelMusic() {
        Music.INSTANCE.playTracks(
                new String[]{Assets.Music.TENDENCY1},
                new float[]{1},
                false);
    }

    private static int WIDTH = 31;
    private static int HEIGHT = 31;

    public static final int bottomDoor = 7 * 31 + 15;

    @Override
    public String tilesTex() {
        return Assets.Environment.TILES_SEWERS;
    }

    @Override
    public String waterTex() {
        return Assets.Environment.WATER_SEWERS;
    }

    @Override
    protected boolean build() {
        setSize(WIDTH, HEIGHT);

        transitions.add(new LevelTransition(this, 15 + WIDTH*15, LevelTransition.Type.BRANCH_ENTRANCE, Dungeon.depth, 0, LevelTransition.Type.BRANCH_EXIT));

        Keicho2 npc = new Keicho2();
		npc.pos = 15 * width() + 2;
		mobs.add( npc );

        NewShopKeeper newShopKeeper = new NewShopKeeper();
        newShopKeeper.pos = 15 * width() + 28;
        mobs.add( newShopKeeper );

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
    private static final short E = Terrain.ENTRANCE;
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
            W, W, W, W, h, e, e, e, W, W, e, e, e, e, e, e, e, e, e, e, W, W, e, e, e, h, h, W, W, W, W,
            W, W, W, W, h, e, e, e, W, e, e, e, e, e, e, e, e, e, e, e, e, W, e, e, e, e, h, W, W, W, W,
            W, W, W, W, h, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, h, W, W, W, W,
            W, W, W, W, S, e, e, e, e, e, e, e, e, h, e, e, e, h, e, e, e, e, e, e, e, e, S, W, W, W, W,
            W, W, W, W, h, e, e, e, e, e, e, e, h, e, h, h, h, e, h, e, e, e, e, e, e, e, h, W, W, W, W,
            W, W, W, W, h, e, e, e, e, e, e, e, e, h, h, h, h, h, e, e, e, e, e, e, e, e, h, W, W, W, W,
            W, W, s, W, h, e, e, e, e, e, e, e, e, h, h, h, h, h, e, e, e, e, e, e, e, e, h, W, s, W, W,
            W, W, W, W, h, e, e, e, e, e, e, e, e, h, h, h, h, h, e, e, e, e, e, e, e, e, h, W, W, W, W,
            W, W, W, W, h, e, e, e, e, e, e, e, h, e, h, h, h, e, h, e, e, e, e, e, e, e, h, W, W, W, W,
            W, W, W, W, S, e, e, e, e, e, e, e, e, h, e, e, e, h, e, e, e, e, e, e, e, e, S, W, W, W, W,
            W, W, W, W, h, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, h, W, W, W, W,
            W, W, W, W, h, e, e, e, W, e, e, e, e, e, e, e, e, e, e, e, e, W, e, e, e, e, h, W, W, W, W,
            W, W, W, W, h, h, e, e, W, W, e, e, e, e, e, e, e, e, e, e, W, W, e, e, e, h, h, W, W, W, W,
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
        return false;
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