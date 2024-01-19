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

public class GnollSapperSprite extends MobSprite {

	public GnollSapperSprite() {
		super();

		texture(Assets.Sprites.GNOLL_SAPPER );

		TextureFilm frames = new TextureFilm( texture, 15, 15 );

		idle = new Animation( 10, true );
		idle.frames( frames, 0, 2, 3, 2, 0, 0, 0, 4, 5, 4 ,0 );

		run = new Animation( 12, true );
		run.frames( frames, 6, 7, 4, 8 );

		attack = new Animation( 12, false );
		attack.frames( frames, 9, 10, 0 );

		zap = attack.clone();

		die = new Animation( 12, false );
		die.frames( frames, 11, 12, 13, 14, 15 );

		play( idle );
	}

	@Override
	public void onComplete( Animation anim ) {
		if (anim == zap) {
			idle();
		}
		super.onComplete( anim );
	}

}
