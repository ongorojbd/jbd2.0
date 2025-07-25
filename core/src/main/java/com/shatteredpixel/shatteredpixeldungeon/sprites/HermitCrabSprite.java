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

package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class HermitCrabSprite extends MobSprite {

	public HermitCrabSprite() {
		super();

		texture( Assets.Sprites.HERMIT );

		TextureFilm frames = new TextureFilm( texture, 12, 16 );

		idle = new Animation( 1, true );
		idle.frames( frames, 0, 0, 0, 1, 0, 0, 1, 1 );

		run = new Animation( 15, true );
		run.frames( frames, 4, 5, 6, 7, 8, 9 );

		attack = new Animation( 12, false );
		attack.frames( frames, 10, 11, 12, 0 );

		zap = new Animation( 12, true );
		attack.frames( frames, 2, 3, 0 );

		die = new Animation( 20, false );
		die.frames( frames, 13, 14, 15, 16, 17 );

		play( idle );
	}

}
