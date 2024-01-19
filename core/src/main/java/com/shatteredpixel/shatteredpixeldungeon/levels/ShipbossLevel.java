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

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Bones;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Diego;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Diobrando;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Diobrando2;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Rebel;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Blacksmith;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Pickaxe;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.StartScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.BlacksmithSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndTitledMessage;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;
import com.watabou.utils.Rect;

import java.util.ArrayList;


public class ShipbossLevel extends Level {

    {
        color1 = 0x48763c;
        color2 = 0x59994a;
    }

    @Override
    public void playLevelMusic() {
        if (locked){
            if(Statistics.duwang3 == 999){
                Music.INSTANCE.play(Assets.Music.HEAVENDIO, true);
            } else Music.INSTANCE.play(Assets.Music.SEWERS_BOSS, true);
            //if top door isn't unlocked
        } else if (map[exit()] != Terrain.EXIT){
            Music.INSTANCE.end();
        } else {
            Music.INSTANCE.playTracks(
                    new String[]{Assets.Music.HEAVENDIO},
                    new float[]{1},
                    false);
        }
    }


    private static int WIDTH = 17;
    private static int HEIGHT = 35;

    private static final int bottomDoor = 8 + (16) * 17; //냅둬야함
    private static boolean isCompleted = false;
    private static final Rect arena = new Rect(0, 0, 17, 13);

    @Override
    public String tilesTex() {
        return Assets.Environment.TILES_SHIP;
    }

    @Override
    public String waterTex() {
        return Assets.Environment.WATER_SEWERS;
    }


    @Override
    protected boolean build() {
        setSize(WIDTH, HEIGHT);

        transitions.add(new LevelTransition(this, 8 + (21)*17, LevelTransition.Type.REGULAR_ENTRANCE));

        buildLevel();

        return true;
    }

    private static final short n = -1;
    private static final short W = Terrain.WALL;
    private static final short J = Terrain.STATUE;
    private static final short D = Terrain.DOOR;
    private static final short e = Terrain.EMPTY;
    private static final short w = Terrain.WATER;
    private static final short E = Terrain.ENTRANCE;
    private static final short B = Terrain.BOOKSHELF;
    private static final short C = Terrain.STATUE_SP;
    private static final short b = Terrain.BARRICADE;

    private static final short G = Terrain.STATUE;
    private static final short m = Terrain.EMBERS;
    private static final short i = Terrain.EMPTY_SP;

    private static final short P = Terrain.CHASM;
    private static final short S = Terrain.PEDESTAL;


    private static short[] level = {
            W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
            W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
            W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
            W, W, i, i, i, i, i, i, i, i, i, i, i, i, i, W, W,
            W, W, i, e, e, e, e, b, b, b, e, e, e, e, i, W, W,
            W, W, i, e, e, e, e, b, b, b, e, e, e, e, i, W, W,
            W, W, i, e, e, e, e, b, b, b, e, e, e, e, i, W, W,
            W, W, i, e, e, e, e, e, e, e, e, e, e, e, i, W, W,
            W, W, i, b, b, b, e, i, i, i, e, b, b, b, i, W, W,
            W, W, i, b, b, b, e, i, i, i, e, b, b, b, i, W, W,
            W, W, i, b, b, b, e, i, i, i, e, b, b, b, i, W, W,
            W, W, i, e, e, e, e, e, e, e, e, e, e, e, i, W, W,
            W, W, i, e, e, e, e, b, b, b, e, e, e, e, i, W, W,
            W, W, i, e, e, e, e, b, b, b, e, e, e, e, i, W, W,
            W, W, i, e, e, e, e, b, b, b, e, e, e, e, i, W, W,
            W, W, i, i, i, i, i, i, i, i, i, i, i, i, i, W, W,
            W, W, W, W, W, W, W, W, D, W, W, W, W, W, W, W, W,
            W, W, W, W, W, G, G, G, e, G, G, G, W, W, W, W, W,
            W, W, W, W, W, e, e, m, m, e, m, m, W, W, W, W, W,
            W, W, W, W, W, m, e, e, m, m, e, e, W, W, W, W, W,
            W, W, W, W, W, m, e, i, i, i, e, e, W, W, W, W, W,
            W, W, W, W, W, m, e, i, E, i, e, m, W, W, W, W, W,
            W, W, W, W, W, e, e, i, i, i, m, m, W, W, W, W, W,
            W, W, W, W, W, e, m, m, m, e, e, m, W, W, W, W, W,
            W, W, W, W, W, e, e, m, m, e, e, e, W, W, W, W, W,
            W, W, W, W, W, m, e, e, e, e, m, m, W, W, W, W, W,
            W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
            W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
            W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
            W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
            W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
            W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
            W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
            W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
            W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W

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
    public void occupyCell(Char ch) {



        if (map[bottomDoor] != Terrain.LOCKED_DOOR && ch.pos < bottomDoor && ch == Dungeon.hero) {
            seal();
        }

        super.occupyCell(ch);
    }


    @Override
    public boolean activateTransition(Hero hero, LevelTransition transition) {
        if (transition.type == LevelTransition.Type.REGULAR_ENTRANCE) {

            if (!Blacksmith.Quest.given()) {
                GLog.w(Messages.get(Blacksmith.class, "entrance_blocked2"));
            }
            return false;

        } else {
            return super.activateTransition(hero, transition);
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
                || (Char.hasProp(ch, Char.Property.LARGE) && !openSpace[cell])
                || Actor.findChar(cell) != null);
        return cell;
    }


    @Override
    public void seal() {
        super.seal();

        //moves intelligent allies with the hero, preferring closer pos to entrance door
        int doorPos = bottomDoor;
        Mob.holdAllies(this, doorPos);
        Mob.restoreAllies(this, Dungeon.hero.pos, doorPos);

        Diobrando2 boss = new Diobrando2();
        boss.state = boss.WANDERING;
        boss.pos = pointToCell(arena.center());
        GameScene.add( boss );
        boss.beckon(Dungeon.hero.pos);

        if (heroFOV[boss.pos]) {
            boss.notice();
            boss.sprite.alpha( 0 );
            boss.sprite.parent.add( new AlphaTweener( boss.sprite, 1, 0.1f ) );
        }

        set(bottomDoor, Terrain.LOCKED_DOOR);
        GameScene.updateMap(bottomDoor);
        Dungeon.observe();

        Game.runOnRenderThread(new Callback() {
            @Override
            public void call() {
                Music.INSTANCE.play(Assets.Music.SEWERS_BOSS, true);
            }
        });
    }

    @Override
    public void unseal() {
        super.unseal();

        set(bottomDoor, Terrain.DOOR);
        GameScene.updateMap(bottomDoor);

        isCompleted = true;

        Dungeon.observe();

        Game.runOnRenderThread(new Callback() {
            @Override
            public void call() {
                Music.INSTANCE.end();
            }
        });
    }

    @Override
    public String tileName( int tile ) {
        switch (tile) {
            case Terrain.WATER:
                return Messages.get(SewerLevel.class, "water_name");
            default:
                return super.tileName( tile );
        }
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

    @Override
    public String tileDesc(int tile) {
        switch (tile) {
            case Terrain.EMPTY_DECO:
                return Messages.get(SewerLevel.class, "empty_deco_desc");
            case Terrain.BOOKSHELF:
                return Messages.get(SewerLevel.class, "bookshelf_desc");
            default:
                return super.tileDesc( tile );
        }
    }
}