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

public class HeavywSprite extends MobSprite {

    public HeavywSprite() {
        super();

        texture( Assets.Sprites.HEAVYW );

        TextureFilm frames = new TextureFilm( texture, 16, 16 );

        idle = new Animation( 2, true );
        idle.frames( frames, 16, 16, 16, 17 );

        run = new Animation( 14, true );
        run.frames( frames, 18, 19, 20, 21, 22 );

        attack = new Animation( 11, false );
        attack.frames( frames, 23, 24, 25, 26, 27 );

        die = new Animation( 11, false );
        die.frames( frames, 28, 29, 30, 31 );

        play( idle );
    }
}
