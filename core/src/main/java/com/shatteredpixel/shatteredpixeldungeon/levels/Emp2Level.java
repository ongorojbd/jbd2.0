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

package com.shatteredpixel.shatteredpixeldungeon.levels;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.level;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Bones;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Beast;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Annasui;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Blacksmith;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Emporio;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Emporio3;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Jolyne;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Pian;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Weza;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Yukako;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Amulet;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.CavesPainter;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.CaveRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.EmptyRoom;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredpixeldungeon.tiles.CustomTilemap;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.Image;
import com.watabou.noosa.Tilemap;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class Emp2Level extends Level {

    {
        color1 = 0x534f3e;
        color2 = 0xb9d661;
    }

    private static final String BOSS = "boss";

    @Override
    public void playLevelMusic() {
        if (locked){
            Music.INSTANCE.play(Assets.Music.LABS_BOSS, true);
            //if exit isn't unlocked
        } else if (map[exit()] != Terrain.EXIT){
            Music.INSTANCE.play(Assets.Music.LABS_BOSS, true);
        } else {
            Music.INSTANCE.playTracks(
                    new String[]{Assets.Music.LABS_BOSS},
                    new float[]{1},
                    false);
        }
    }

    @Override
    public String tilesTex() {
        return Assets.Environment.TILES_EMPO;
    }

    @Override
    public String waterTex() {
        return Assets.Environment.WATER_CAVES;
    }

    @Override
    protected boolean build() {

        boss = null;

        setSize(14, 14);

        EmptyRoom c = new EmptyRoom();
        c.set(1, 1, 13, 13);
        c.paint(this);


        int entrance = 11 * width() + 7;

        transitions.add(new LevelTransition(this,
                entrance,
                LevelTransition.Type.BRANCH_ENTRANCE,
                Dungeon.depth,
                0,
                LevelTransition.Type.BRANCH_EXIT));

        map[entrance] = Terrain.ENTRANCE;


        Pian npc2 = new Pian();
        npc2.pos = 7 * width() + 7;
        mobs.add( npc2 );

        Emporio3 npc5 = new Emporio3();
        npc5.pos = 11 * width() + 11;
        mobs.add( npc5 );

        return true;
    }

    @Override
    public Mob createMob() {
        return null;
    }

    @Override
    public void unseal() {
        super.unseal();

        int entrance = entrance();
        set(entrance, Terrain.ENTRANCE);
        GameScene.updateMap(entrance);

        Dungeon.observe();

        Game.runOnRenderThread(new Callback() {
            @Override
            public void call() {
                Music.INSTANCE.end();
            }
        });
    }

    @Override
    protected void createMobs() {
    }

    public Actor addRespawner() {
        return null;
    }

    @Override
    protected void createItems() {
        Item item = Bones.get();
        if (item != null) {
            drop( item, entrance()-width() ).setHauntedIfCursed().type = Heap.Type.REMAINS;
        }
    }

    @Override
    public int randomRespawnCell( Char ch ) {
        return entrance()-width();
    }

    @Override
    public String tileName( int tile ) {
        switch (tile) {
            case Terrain.GRASS:
                return Messages.get(CavesLevel.class, "grass_name");
            case Terrain.HIGH_GRASS:
                return Messages.get(CavesLevel.class, "high_grass_name");
            case Terrain.WATER:
                return Messages.get(CavesLevel.class, "water_name");
            case Terrain.STATUE:
                //city statues are used
                return Messages.get(CityLevel.class, "statue_name");
            default:
                return super.tileName( tile );
        }
    }

    @Override
    public String tileDesc( int tile ) {
        switch (tile) {
            case Terrain.WATER:
                return super.tileDesc( tile ) + "\n\n" + Messages.get(CavesBossLevel.class, "water_desc");
            case Terrain.ENTRANCE:
                return Messages.get(CavesLevel.class, "entrance_desc");
            case Terrain.EXIT:
                //city exit is used
                return Messages.get(CityLevel.class, "exit_desc");
            case Terrain.HIGH_GRASS:
                return Messages.get(CavesLevel.class, "high_grass_desc");
            case Terrain.WALL_DECO:
                return Messages.get(CavesLevel.class, "wall_deco_desc");
            case Terrain.BOOKSHELF:
                return Messages.get(CavesLevel.class, "bookshelf_desc");
            //city statues are used
            case Terrain.STATUE:
                return Messages.get(CityLevel.class, "statue_desc");
            default:
                return super.tileDesc( tile );
        }
    }

    @Override
    public Group addVisuals() {
        super.addVisuals();
        CavesLevel.addCavesVisuals(this, visuals);
        return visuals;
    }

    private Mob boss;
    private static boolean killed = false;

    @Override
    public void seal() {
        if (!locked) {
            super.seal();

            int entrance = entrance();
            set(entrance, Terrain.EMPTY);

            Heap heap = Dungeon.level.heaps.get(entrance);
            if (heap != null) {
                int n;
                do {
                    n = entrance + PathFinder.NEIGHBOURS8[Random.Int(8)];
                } while (!Dungeon.level.passable[n]);
                Dungeon.level.drop(heap.pickUp(), n).sprite.drop(entrance);
            }

            Char ch = Actor.findChar(entrance);
            if (ch != null) {
                int n;
                do {
                    n = entrance + PathFinder.NEIGHBOURS8[Random.Int(8)];
                } while (!Dungeon.level.passable[n]);
                ch.pos = n;
                ch.sprite.place(n);
            }

            GameScene.updateMap(entrance);
            Dungeon.observe();


            boss = new Beast();
            boss.state = boss.WANDERING;
            do {
                boss.pos = 5 * width() + 5;
            } while (!openSpace[boss.pos] || map[boss.pos] == Terrain.EMPTY_SP || Actor.findChar(boss.pos) != null);
            GameScene.add(boss);

        }
    }

    @Override
    public boolean activateTransition(Hero hero, LevelTransition transition) {
        if (transition.type == LevelTransition.Type.BRANCH_ENTRANCE
                && Dungeon.level instanceof Emp2Level){

            if( Statistics.duwang == 34){
                InterlevelScene.mode = InterlevelScene.Mode.RETURN;
                InterlevelScene.returnDepth = Math.max(1, (Dungeon.depth -1));
                InterlevelScene.returnBranch = 0;
                InterlevelScene.returnPos = -2;
                Game.switchScene(InterlevelScene.class);
                return false;
            } else {
                InterlevelScene.mode = InterlevelScene.Mode.RETURN;
                InterlevelScene.returnDepth = Math.max(1, (Dungeon.depth + 1));
                InterlevelScene.returnBranch = 1;
                InterlevelScene.returnPos = -2;
                Game.switchScene(InterlevelScene.class);
                return false;
            }
        } else {
            return super.activateTransition(hero, transition);
        }
    }


    @Override
    public void storeInBundle( Bundle bundle ) {

        super.storeInBundle( bundle );

        bundle.put( BOSS, boss );

    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);

        boss = (Mob)bundle.get( BOSS );

    }

    public static class BorderDarken extends CustomTilemap{

        {
            texture = Assets.Environment.CAVES_QUEST;
        }

        @Override
        public Tilemap create() {
            Tilemap v = super.create();
            int[] data = new int[tileW*tileH];
            for (int i = 0; i < data.length; i++){
                if (i < tileW){
                    data[i] = 2;
                } else if (i % tileW == 0 || i % tileW == tileW-1){
                    data[i] = 1;
                } else if (i + 2*tileW > data.length) {
                    data[i] = 3;
                } else {
                    data[i] = -1;
                }
            }
            v.map( data, tileW );
            return v;
        }

        @Override
        public Image image(int tileX, int tileY) {
            return null;
        }
    }
}
