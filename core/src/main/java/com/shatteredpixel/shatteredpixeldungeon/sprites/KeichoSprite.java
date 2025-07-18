/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2022 Evan Debenham
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

public class KeichoSprite extends MobSprite {

    private Animation prep;

    public KeichoSprite() {
        super();

        texture( Assets.Sprites.KEICHO );

        TextureFilm frames = new TextureFilm( texture, 11, 16 );

        idle = new Animation( 2, true );
        idle.frames( frames, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1);

        run = new Animation( 2, true );
        run.frames( frames, 0, 0, 1);

        prep = new Animation( 3, false );
        prep.frames( frames, 2, 2, 2, 2);

        die = new Animation( 12, false );
        die.frames( frames, 3, 4, 5, 6, 7);

        play( idle );
    }

    public void leapPrep( int cell ){
        turnTo( ch.pos, cell );
        play( prep );
    }

}