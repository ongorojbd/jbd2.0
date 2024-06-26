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

public class BoytwoSprite extends MobSprite {

    public BoytwoSprite() {
        super();

        texture( Assets.Sprites.BOYTWO);

        TextureFilm frames = new TextureFilm( texture, 13, 18 );

        idle = new Animation( 7, true );
        idle.frames( frames, 0, 1 );

        run = idle.clone();

        attack = new Animation( 11, false );
        attack.frames( frames, 0, 2, 0, 2, 0, 2 );

        die = new Animation( 6, false );
        die.frames( frames, 3, 4, 5 );

        play( idle );
    }
}
