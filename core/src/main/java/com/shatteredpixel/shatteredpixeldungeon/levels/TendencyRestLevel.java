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
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.WaterOfHealth;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.WellWater;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.watabou.noosa.audio.Music;

public class TendencyRestLevel extends Level {

    private static final int WIDTH = 13;
    private static final int HEIGHT = 13;

    private static final int EXIT = 6 + 2 * WIDTH;
    private static final int WELL = 6 + 6 * WIDTH;
    private static final int ENTRANCE = 6 + 10 * WIDTH;

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
    protected boolean build() {
        setSize(WIDTH, HEIGHT);

        for (int y = 2; y <= 10; y++) {
            for (int x = 2; x <= 10; x++) {
                map[x + y * WIDTH] = Terrain.EMPTY;
            }
        }

        map[ENTRANCE] = Terrain.ENTRANCE;
        map[EXIT] = Terrain.EXIT;
        map[WELL] = Terrain.WELL;

        transitions.add(new LevelTransition(this, ENTRANCE, LevelTransition.Type.REGULAR_ENTRANCE));
        transitions.add(new LevelTransition(this, EXIT, LevelTransition.Type.REGULAR_EXIT));

        WellWater.seed(WELL, 1, WaterOfHealth.class, this);

        return true;
    }

    @Override
    protected void createMobs() {
    }

    @Override
    protected void createItems() {
    }

    @Override
    public boolean activateTransition(Hero hero, LevelTransition transition) {
        if (transition.type == LevelTransition.Type.REGULAR_ENTRANCE) return false;
        return super.activateTransition(hero, transition);
    }
}
