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

public class DannySprite extends MobSprite {

	public DannySprite() {
		super();
		
		texture( Assets.Sprites.DANNY );
		
		TextureFilm frames = new TextureFilm( texture, 32, 16 );
		
		idle = new Animation( 12, true );
		idle.frames( frames, 0, 1, 2, 3, 4, 5, 6, 7 );
		
		run = new Animation( 12, true );
		run.frames( frames, 8, 9, 10, 11, 12, 13, 14, 15 );
		
		attack = new Animation( 12, false );
		attack.frames( frames, 16, 17, 18, 19, 20 );
		
		die = new Animation( 12, false );
		die.frames( frames, 0, 21, 22, 23, 24, 25 );
		
		play( idle );

		scale.set(0.75f);
	}

}
