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

public class Lisa3Sprite extends MobSprite {

	public Lisa3Sprite() {
		super();
		
		texture( Assets.Sprites.LISA2 );
		
		TextureFilm frames = new TextureFilm( texture, 12, 15 );

		idle = new Animation( 1, true );
		idle.frames( frames, 0);

		run = new Animation( 1, true );
		run.frames( frames, 2 );

		attack = new Animation( 3, true );
		attack.frames( frames, 2 );

		zap = attack.clone();

		die = new Animation( 5, true );
		die.frames( frames, 2);
		
		play( idle );
	}

}
