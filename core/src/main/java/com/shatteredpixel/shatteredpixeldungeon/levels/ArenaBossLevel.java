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

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Bones;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Esidisi;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.GSoldier2;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Santana;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Tboss;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Wamuu;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.WamuuFirst;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.FlameParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndMessage;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.Halo;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class ArenaBossLevel extends Level {

    {
        color1 = 0x48763c;
        color2 = 0x59994a;
    }

    @Override
    public void playLevelMusic() {
        if (locked) {
            Music.INSTANCE.play(Assets.Music.TENDENCY2, true);
            //if top door isn't unlocked
        } else if (map[exit()] != Terrain.EXIT) {
            Music.INSTANCE.end();
        } else {
            Music.INSTANCE.playTracks(
                    new String[]{Assets.Music.TENDENCY2},
                    new float[]{1},
                    false);
        }
    }

    private static int WIDTH = 31;
    private static int HEIGHT = 31;

    public static final int bottomDoor = 7 * 31 + 15;

    @Override
    public String tilesTex() {
        return Assets.Environment.TILES_TENDENCY2;
    }

    @Override
    public String waterTex() {
        return Assets.Environment.WATER_SEWERS;
    }

    @Override
    protected boolean build() {
        setSize(WIDTH, HEIGHT);

        // 위층으로 가는 일반적인 연결 추가
        transitions.add(new LevelTransition(this, 15 + WIDTH * 15, LevelTransition.Type.REGULAR_ENTRANCE));

        buildLevel();
        return true;
    }

    private static final short n = -1; //used when a tile shouldn't be changed
    private static final short W = Terrain.WALL;
    private static final short w = Terrain.EMPTY_DECO;
    private static final short e = Terrain.EMPTY;
    private static final short h = Terrain.EMPTY_SP;
    private static final short t = Terrain.PEDESTAL;
    private static final short S = Terrain.STATUE_SP;
    private static final short L = Terrain.LOCKED_EXIT;
    private static final short E = Terrain.ENTRANCE_SP;
    private static final short T = Terrain.TRAP;
    private static final short p = Terrain.PEDESTAL;
    private static final short d = Terrain.WALL_DECO;
    private static final short C = Terrain.CRYSTAL_DOOR;
    private static final short B = Terrain.STATUE_SP;
    private static final short A = Terrain.ALCHEMY;
    private static final short r = Terrain.BOOKSHELF;
    private static final short b = Terrain.BARRICADE;
    private static final short H = Terrain.EMPTY_WELL;

    private boolean bossSpawned = false;

    private static final String BOSS_SPAWNED = "boss_spawned";

    private static final short[] level = {
            W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
            W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
            W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
            W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, L, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
            W, W, W, W, W, W, W, W, h, h, h, h, S, h, h, h, h, h, S, h, h, h, h, W, W, W, W, W, W, W, W,
            W, W, W, W, W, W, W, h, h, e, e, e, e, e, e, e, e, e, e, e, e, e, h, h, W, W, W, W, W, W, W,
            W, W, W, W, W, W, h, h, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, h, h, W, W, W, W, W, W,
            W, W, W, W, W, h, h, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, h, h, W, W, W, W, W,
            W, W, W, W, h, h, e, e, e, e, e, e, w, e, e, e, e, e, w, e, e, e, e, e, e, h, h, W, W, W, W,
            W, W, W, W, h, e, e, e, e, d, d, e, e, e, e, e, e, e, e, e, d, d, e, e, e, e, h, W, W, W, W,
            W, W, W, W, h, e, e, e, e, d, e, e, e, e, e, e, e, e, e, e, e, d, e, e, e, e, h, W, W, W, W,
            W, W, W, W, h, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, h, W, W, W, W,
            W, W, W, W, S, e, e, e, w, e, e, e, e, h, e, e, e, h, e, e, e, e, w, e, e, e, S, W, W, W, W,
            W, W, W, W, h, e, e, e, e, e, e, e, h, e, h, h, h, e, h, e, e, e, e, e, e, e, h, W, W, W, W,
            W, W, W, W, h, e, e, e, e, e, e, e, e, h, h, h, h, h, e, e, e, e, e, e, e, e, h, W, W, W, W,
            W, W, W, W, h, e, e, e, e, e, e, e, e, h, h, E, h, h, e, e, e, e, e, e, e, e, h, W, W, W, W,
            W, W, W, W, h, e, e, e, e, e, e, e, e, h, h, h, h, h, e, e, e, e, e, e, e, e, h, W, W, W, W,
            W, W, W, W, h, e, e, e, e, e, e, e, h, e, h, h, h, e, h, e, e, e, e, e, e, e, h, W, W, W, W,
            W, W, W, W, S, e, e, e, w, e, e, e, e, h, e, e, e, h, e, e, e, e, w, e, e, e, S, W, W, W, W,
            W, W, W, W, h, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, h, W, W, W, W,
            W, W, W, W, h, e, e, e, e, d, e, e, e, e, e, e, e, e, e, e, e, d, e, e, e, e, h, W, W, W, W,
            W, W, W, W, h, h, e, e, e, d, d, e, e, e, e, e, e, e, e, e, d, d, e, e, e, h, h, W, W, W, W,
            W, W, W, W, W, h, h, e, e, e, e, e, w, e, e, e, e, e, w, e, e, e, e, e, h, h, W, W, W, W, W,
            W, W, W, W, W, W, h, h, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, h, h, W, W, W, W, W, W,
            W, W, W, W, W, W, W, h, h, e, e, e, e, e, e, e, e, e, e, e, e, e, h, h, W, W, W, W, W, W, W,
            W, W, W, W, W, W, W, W, h, h, e, e, e, e, e, e, e, e, e, e, e, h, h, W, W, W, W, W, W, W, W,
            W, W, W, W, W, W, W, W, W, h, h, h, S, h, h, h, h, h, S, h, h, h, W, W, W, W, W, W, W, W, W,
            W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
            W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
            W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
            W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W
    };

    private void buildLevel() {
        int pos = 0 + 0 * width();
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

    @Override
    public int randomRespawnCell(Char ch) {
        int cell;
        do {
            cell = entrance() + PathFinder.NEIGHBOURS8[Random.Int(8)];
        } while (!passable[cell]
                || (Char.hasProp(ch, Char.Property.LARGE) && !openSpace[cell])
                || Actor.findChar(cell) != null);
        return cell;
    }

    @Override
    public String tileName(int tile) {
        switch (tile) {
            case Terrain.LOCKED_EXIT:
                return Messages.get(LabsLevel.class, "locked_exit_name");
            case Terrain.UNLOCKED_EXIT:
                return Messages.get(LabsLevel.class, "unlocked_exit_name");
            default:
                return super.tileName(tile);
        }
    }

    @Override
    public String tileDesc(int tile) {
        switch (tile) {
            case Terrain.LOCKED_EXIT:
                return Messages.get(LabsLevel.class, "locked_exit_desc");
            case Terrain.UNLOCKED_EXIT:
                return Messages.get(LabsLevel.class, "unlocked_exit_desc");
            default:
                return super.tileDesc(tile);
        }
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(BOSS_SPAWNED, bossSpawned);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        bossSpawned = bundle.getBoolean(BOSS_SPAWNED);
    }

    @Override
    public void occupyCell(Char ch) {
        super.occupyCell(ch);

        if (ch == hero && !locked && !bossSpawned) {
            seal();
        }
    }

    @Override
    public void seal() {
        super.seal();

        bossSpawned = true;

        int entrance = entrance();
        set(entrance, Terrain.EMPTY_SP);
        GameScene.updateMap(entrance);

        Dungeon.observe();

        if (Dungeon.depth == 9) {
            Wamuu boss = new Wamuu();
            boss.pos = 15 + WIDTH * 10;
            boss.state = boss.WANDERING;
            GameScene.add(boss);
            boss.beckon(Dungeon.hero.pos);

            if (heroFOV[boss.pos]) {
                boss.notice();
                boss.sprite.alpha(0);
                boss.sprite.parent.add(new AlphaTweener(boss.sprite, 1, 0.1f));
            }
        } else if (Dungeon.depth == 18) {
            int[] spawnPositions = {22 + 15 * 31, 15 + 22 * 31, 10 + 20 * 31, 18 + 18 * 31, 12 + 17 * 31, 20 + 19 * 31};

            for (int i = 0; i < 6; i++) {
                Mob spawn = new GSoldier2();
                spawn.pos = spawnPositions[i];
                spawn.state = spawn.HUNTING;
                GameScene.add(spawn);
            }

            Santana boss = new Santana();
            boss.pos = 15 + WIDTH * 10;
            boss.state = boss.WANDERING;
            GameScene.add(boss);
            boss.beckon(Dungeon.hero.pos);

            if (heroFOV[boss.pos]) {
                boss.notice();
                boss.sprite.alpha(0);
                boss.sprite.parent.add(new AlphaTweener(boss.sprite, 1, 0.1f));
            }
        } else if (Dungeon.depth == 27) {
            WamuuFirst boss = new WamuuFirst();
            boss.pos = 15 + WIDTH * 10;
            boss.state = boss.WANDERING;
            GameScene.add(boss);
            boss.beckon(Dungeon.hero.pos);

            if (heroFOV[boss.pos]) {
                boss.notice();
                boss.sprite.alpha(0);
                boss.sprite.parent.add(new AlphaTweener(boss.sprite, 1, 0.1f));
            }
        } else if (Dungeon.depth == 36) {
            Esidisi boss = new Esidisi();
            boss.pos = 15 + WIDTH * 10;
            boss.state = boss.WANDERING;
            GameScene.add(boss);
            boss.beckon(Dungeon.hero.pos);

            if (heroFOV[boss.pos]) {
                boss.notice();
                boss.sprite.alpha(0);
                boss.sprite.parent.add(new AlphaTweener(boss.sprite, 1, 0.1f));
            }
        }

        GameScene.updateMap(bottomDoor);
        Dungeon.observe();

        Game.runOnRenderThread(new Callback() {
            @Override
            public void call() {
                Music.INSTANCE.play(Assets.Music.TENDENCY2, true);
            }
        });

    }

    @Override
    public void unseal() {
        super.unseal();
        set(entrance(), Terrain.ENTRANCE);
        GameScene.updateMap(entrance());

        // LOCKED_EXIT를 UNLOCKED_EXIT로 변경하고 LevelTransition 추가
        for (int i = 0; i < length(); i++) {
            if (map[i] == Terrain.LOCKED_EXIT) {
                set(i, Terrain.UNLOCKED_EXIT);
                GameScene.updateMap(i);

                // LevelTransition 추가
                LevelTransition exit = new LevelTransition(this, i, LevelTransition.Type.REGULAR_EXIT);
                transitions.add(exit);
            }
        }

        Dungeon.observe();
    }

    @Override
    public Group addVisuals() {
        super.addVisuals();
        addArenaVisuals(this, visuals);
        return visuals;
    }

    public static void addArenaVisuals(Level level, Group group) {
        for (int i = 0; i < level.length(); i++) {
            if (level.map[i] == Terrain.EMPTY_DECO) {
                group.add(new Torch(i));
            }
        }
    }

    @Override
    public boolean activateTransition(Hero hero, LevelTransition transition) {

        if (transition.type == LevelTransition.Type.REGULAR_ENTRANCE) {
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    GameScene.show(new WndMessage(Messages.get(hero, "tendency2")));
                }
            });
            return false;
        } else {
            return super.activateTransition(hero, transition);
        }
    }

    public static class Torch extends Emitter {

        private int pos;

        public Torch(int pos) {
            super();

            this.pos = pos;

            PointF p = DungeonTilemap.tileCenterToWorld(pos);
            pos(p.x - 1, p.y + 2, 2, 0);

            pour(FlameParticle.FACTORY, 0.15f);

            add(new Halo(12, 0xFFFFCC, 0.4f).point(p.x, p.y + 1));
        }

        @Override
        public void update() {
            if (visible = (pos < Dungeon.level.heroFOV.length && Dungeon.level.heroFOV[pos])) {
                super.update();
            }
        }
    }

}