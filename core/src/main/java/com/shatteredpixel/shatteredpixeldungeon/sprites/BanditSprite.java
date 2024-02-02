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
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.watabou.noosa.TextureFilm;

public class BanditSprite extends MobSprite {
	
	public BanditSprite() {
		super();
		
		texture( Assets.Sprites.THIEF );
		TextureFilm film = new TextureFilm( texture, 13, 17 );
		
		idle = new Animation( 10, true );
		idle.frames( film, 0, 1, 2, 3, 2, 1, 0);
		
		run = new Animation( 12, true );
		run.frames( film, 4, 14, 5, 4, 14, 5 );
		
		die = new Animation( 10, false );
		die.frames( film, 10, 11, 12, 13 );
		
		attack = new Animation( 12, false );
		attack.frames( film, 6, 7, 8, 9 );

		scale.set(0.8f);
		
		idle();
	}

	@Override
	public void die() {
		super.die();
		if (Dungeon.level.heroFOV[ch.pos]) {
			emitter().burst( Speck.factory( Speck.COIN ), 30 );
		}
	}
}
