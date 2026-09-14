/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2026 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

//ported from Tower Pixel Dungeon's DrillBigSprite (drill_big.png), reused as-is for the HospitalLevel ambulance.
//Full original size (260x174/frame) - TPD keeps it full size too and instead special-cases
//DrillSprite/DrillBigSprite in GameScene.sortMobSprites() so it always draws behind everything
//else. AmbulanceSprite gets the same special-case treatment in jbd2.0's GameScene.
public class AmbulanceSprite extends MobSprite {

    public AmbulanceSprite() {
        super();
        perspectiveRaise = -4.9f;

        shadowHeight = 0.9f;
        shadowWidth     = 1.0f;
        shadowOffset    = 0.25f;

        texture( Assets.Sprites.AMBULANCE );

        TextureFilm frames = new TextureFilm( texture, 260, 174 );

        idle = new Animation( 12, true );
        idle.frames( frames, 0, 1 );

        run = attack = idle.clone();

        die = new Animation( 15, true );
        die.frames( frames, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0 );

        flipHorizontal = false;

        play( idle );
    }

    @Override
    public void turnTo(int from, int to) {
        flipHorizontal = false;
    }
}
