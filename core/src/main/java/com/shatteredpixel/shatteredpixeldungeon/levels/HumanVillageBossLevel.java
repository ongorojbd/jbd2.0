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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon.levels;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Bones;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Goo;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Rat;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Annasui;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Emporio2;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class HumanVillageBossLevel extends Level {

    {
        viewDistance = 8;

        color1 = 0x48763c;
        color2 = 0x59994a;
    }

    @Override
    public void playLevelMusic() {
        Music.INSTANCE.playTracks(
                new String[]{Assets.Music.CITY_BOSS},
                new float[]{1},
                false);
    }

    private static int WIDTH = 23;
    private static int HEIGHT = 22;

    private static boolean isCompleted = false;

    @Override
    public String tilesTex() {
        return Assets.Environment.TILES_PRISON;
    }

    @Override
    public String waterTex() {
        return Assets.Environment.WATER_CITY;
    }

    @Override
    protected boolean build() {
        setSize(WIDTH, HEIGHT);

        transitions.add(new LevelTransition(this, 425, LevelTransition.Type.REGULAR_EXIT));
        transitions.add(new LevelTransition(this, 34, LevelTransition.Type.REGULAR_ENTRANCE));

        transitions.add(new LevelTransition(this,
                entrance,
                LevelTransition.Type.BRANCH_ENTRANCE,
                Dungeon.depth,
                0,
                LevelTransition.Type.BRANCH_EXIT));

        map[entrance] = Terrain.ENTRANCE;

        buildLevel();

        Emporio2 npc = new Emporio2();
        npc.pos = 6 * width() + 11;
        mobs.add( npc );

        return true;
    }

    private static final short n = -1;
    private static final short W = Terrain.WALL;
    private static final short E = Terrain.ENTRANCE;
    private static final short d = Terrain.EMPTY;
    private static final short x = Terrain.WATER;
    private static final short L = Terrain.LOCKED_EXIT;

    private static final short D = Terrain.LOCKED_DOOR;

    private static short[] level = {
            W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
            W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
            W, W, W, W, W, W, x, x, x, W, x, x, x, W, x, x, x, W, W, W, W, W, W,
            W, W, W, W, W, W, x, x, x, W, x, x, x, W, x, x, x, W, W, W, W, W, W,
            W, W, W, W, W, W, d, d, d, W, d, d, d, W, d, d, d, W, W, W, W, W, W,
            W, W, W, W, W, W, W, D, W, W, W, D, W, W, W, D, W, W, W, W, W, W, W,
            W, W, d, d, d, W, d, d, d, d, d, d, d, d, d, d, d, W, x, d, d, W, W,
            W, W, d, d, x, D, d, d, d, d, d, d, d, d, d, d, d, D, x, d, d, W, W,
            W, W, d, d, x, W, d, d, d, d, d, d, d, d, d, d, d, W, x, d, d, W, W,
            W, W, W, W, W, W, d, d, d, d, x, x, x, d, d, d, d, W, W, W, W, W, W,
            W, W, W, W, W, W, d, d, d, d, x, x, x, d, d, d, d, W, W, W, W, W, W,
            W, W, d, d, x, W, d, d, d, d, x, x, x, d, d, d, d, W, x, d, d, W, W,
            W, W, d, d, x, D, d, d, d, d, x, x, x, d, d, d, d, D, x, d, d, W, W,
            W, W, d, d, x, W, d, d, d, d, d, d, d, d, d, d, d, W, x, d, d, W, W,
            W, W, W, W, W, W, d, d, d, d, d, d, d, d, d, d, d, W, W, W, W, W, W,
            W, W, W, W, W, W, d, d, d, d, d, d, d, d, d, d, d, W, W, W, W, W, W,
            W, W, d, d, x, W, d, d, d, d, d, d, d, d, d, d, d, W, x, d, d, W, W,
            W, W, d, d, x, D, d, d, d, d, d, d, d, d, d, d, d, D, x, d, d, W, W,
            W, W, d, d, x, W, x, x, x, x, x, x, x, x, x, x, x, W, x, d, d, W, W,
            W, W, W, W, W, W, x, x, x, x, x, x, x, x, x, x, x, W, W, W, W, W, W,
            W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
            W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W
    };

    private void buildLevel(){
        int pos = 0 + 0*width();

        short[] levelTiles = level;
        for (int i = 0; i < levelTiles.length; i++){
            if (levelTiles[i] != n) map[pos] = levelTiles[i];

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
            for (Item i : bonesItems) {
                drop(i, pos).setHauntedIfCursed().type = Heap.Type.REMAINS;
            }
        }
        Random.popGenerator();
    }

    @Override
    public int randomRespawnCell( Char ch ) {
        int cell;
        do {
            cell = entrance() + PathFinder.NEIGHBOURS8[Random.Int(8)];
        } while (!passable[cell]
                || Actor.findChar(cell) != null);
        return cell;
    }

    @Override
    public void seal() {
        super.seal();

        set( 425, Terrain.WATER );
        GameScene.updateMap( 425 );
    }

    @Override
    public void unseal() {
        super.unseal();

        set( 425, Terrain.EMPTY );
        GameScene.updateMap( 425 );

        isCompleted = true;

        Dungeon.observe();
    }

    private static final String ISCOMPLETED = "iscompleted";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put(ISCOMPLETED, isCompleted);
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        isCompleted = bundle.getBoolean( ISCOMPLETED );
    }
}