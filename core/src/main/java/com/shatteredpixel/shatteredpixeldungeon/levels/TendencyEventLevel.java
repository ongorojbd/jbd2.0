/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2025 Evan Debenham
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
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Bt1;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Bt2;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.GSoldier;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.SpwSoldier;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.watabou.noosa.audio.Music;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class TendencyEventLevel extends Level {

    private static final int WIDTH = 29;
    private static final int HEIGHT = 29;

	private static final int CENTER = 14 + 14 * WIDTH;
	private static final int EXIT = 14 + 3 * WIDTH;
	private static final int ALLY = CENTER;

    private static final int[] ENEMY_POSITIONS = {
            14 + 5 * WIDTH,
            22 + 19 * WIDTH,
            14 + 23 * WIDTH,
            6 + 9 * WIDTH
    };

    private static final int[] BLOCKERS = {
            11 + 9 * WIDTH,
            17 + 9 * WIDTH,
            19 + 13 * WIDTH,
            17 + 19 * WIDTH,
            11 + 19 * WIDTH,
            9 + 13 * WIDTH
    };

    private static final int[] COVER = {
            14 + 8 * WIDTH,
            20 + 11 * WIDTH,
            20 + 17 * WIDTH,
            14 + 20 * WIDTH,
            8 + 17 * WIDTH,
            8 + 11 * WIDTH,
            12 + 12 * WIDTH,
            16 + 12 * WIDTH,
            16 + 16 * WIDTH,
            12 + 16 * WIDTH
    };

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

    @Override
    public String tilesTex() {
        return Assets.Environment.TILES_TENDENCY2;
    }

    @Override
    public String waterTex() {
        return Assets.Environment.WATER_SEWERS;
    }

    @Override
    public void create() {
        super.create();
        seal();
    }

    @Override
    protected boolean build() {
        setSize(WIDTH, HEIGHT);

        int radius = 13;
        int radiusSq = radius * radius;
        for (int y = 1; y < HEIGHT - 1; y++) {
            for (int x = 1; x < WIDTH - 1; x++) {
                int dx = x - 14;
                int dy = y - 14;
                if (dx * dx + dy * dy <= radiusSq) {
                    map[x + y * WIDTH] = Terrain.EMPTY;
                }
            }
        }

        for (int pos : BLOCKERS) {
            map[pos] = Terrain.STATUE;
        }
        for (int pos : COVER) {
            map[pos] = Terrain.HIGH_GRASS;
        }

        map[CENTER] = Terrain.ENTRANCE;
        map[EXIT] = Terrain.LOCKED_EXIT;

        transitions.add(new LevelTransition(this, CENTER, LevelTransition.Type.REGULAR_ENTRANCE));
        transitions.add(new LevelTransition(this, EXIT, LevelTransition.Type.REGULAR_EXIT));

        return true;
    }

    @Override
    protected void createMobs() {
        Mob ally;
        int roll = Random.Int(4);
        if (roll == 0) {
            ally = new GSoldier();
            GLog.p("독일 군인: \"이, 이런 괴물들까지 상대해야 한다고는 못 들었다고..!\"");
        } else if (roll == 1) {
            ally = new SpwSoldier();
            GLog.p("SPW재단 특별과학 전투대: \"자외선 조사 장치 가동 확인!\"");
        } else if (roll == 2) {
            ally = new Bt1();
            GLog.p("로긴즈: \"자, 좀 더 기운차게 덤벼봐! 심심하지 않게 말이야.\"");
        } else {
            ally = new Bt2();
            GLog.p("메시나: \"흠, 이 정도라면.. 가볍게 몸풀기 정도로 딱 적당하겠군.\"");
        }
        ally.pos = ALLY;
        ally.state = ally.WANDERING;
        mobs.add(ally);

        for (int pos : ENEMY_POSITIONS) {
            Mob enemy = createMob();
            enemy.pos = pos;
            enemy.state = enemy.HUNTING;
            mobs.add(enemy);
        }
    }

    @Override
    protected void createItems() {
    }

    @Override
    public boolean activateTransition(Hero hero, LevelTransition transition) {
        if (transition.type == LevelTransition.Type.REGULAR_ENTRANCE) return false;
        if (transition.type == LevelTransition.Type.REGULAR_EXIT) {
            if (enemiesAlive()) return false;
            if (locked) unseal();
        }
        return super.activateTransition(hero, transition);
    }

    @Override
    public void occupyCell(Char ch) {
        super.occupyCell(ch);
        if (locked && !enemiesAlive()) {
            unseal();
        }
    }

    @Override
    public void seal() {
        if (!locked) {
            super.seal();
            set(EXIT, Terrain.LOCKED_EXIT, this);
            if (Dungeon.level == this) {
                GameScene.updateMap(EXIT);
            }
        }
    }

    @Override
    public void unseal() {
        if (locked) {
            super.unseal();
            set(EXIT, Terrain.EXIT, this);
            if (Dungeon.level == this) {
                GameScene.updateMap(EXIT);
            }
        }
    }

    @Override
    public int randomRespawnCell(Char ch) {
        int cell;
        do {
            cell = CENTER + PathFinder.NEIGHBOURS8[Random.Int(8)];
        } while (!passable[cell]
                || (Char.hasProp(ch, Char.Property.LARGE) && !openSpace[cell])
                || Actor.findChar(cell) != null);
        return cell;
    }

    private boolean enemiesAlive() {
        for (Mob mob : mobs.toArray(new Mob[0])) {
            if (mob.isAlive() && mob.alignment == Char.Alignment.ENEMY) return true;
        }
        return false;
    }
}
