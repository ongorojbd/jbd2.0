/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2024 Evan Debenham
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

public class GnollSprite2 extends MobSprite {

    public GnollSprite2() {
        super();

        texture( Assets.Sprites.GNOLL2 );

        TextureFilm frames = new TextureFilm( texture, 45, 45 );

        idle = new Animation( 8, true );
        idle.frames( frames, 0, 1, 2, 1, 0 );

        run = new Animation( 12, true );
        idle.frames( frames, 0, 1, 2, 1, 0 );

        attack = new Animation( 12, false );
        idle.frames( frames, 0, 1, 2, 1, 0 );

        die = new Animation( 12, false );
        idle.frames( frames, 0, 1, 2, 1, 0 );

        scale.set(0.35f);
        play( idle );
    }

    @Override
    public int blood() {
        return 0x000000;
    }
}
