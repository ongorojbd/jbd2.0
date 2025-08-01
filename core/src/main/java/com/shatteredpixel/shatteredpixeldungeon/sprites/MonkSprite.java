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
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.GnollGeomancer;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.watabou.noosa.TextureFilm;
import com.watabou.utils.Random;

public class MonkSprite extends MobSprite {
	
	private Animation kick;

	public MonkSprite() {
		super();

		texture(Assets.Sprites.MONK);

		updateAnims();

		scale.set(0.45f);
	}

	@Override
	public void link( Char ch ) {
		super.link( ch );

		if (ch != null){
			updateAnims();
		}
	}


	private void updateAnims(){

		TextureFilm frames = new TextureFilm( texture, 35, 37 );

		idle = new Animation( 6, true );
		idle.frames( frames, 0, 2, 0, 2 );
		
		run = new Animation( 10, true );
		run.frames( frames, 2, 1, 0, 2, 1, 0 );
		
		attack = new Animation( 14, false );
		attack.frames( frames, 6, 7, 8, 7, 6 );
		
		kick = new Animation( 14, false );
		kick.frames( frames, 3, 4, 5, 4, 3 );
		
		die = new Animation( 15, false );
		die.frames( frames, 9, 10, 11 );

		play( idle );
	}
	
	@Override
	public void attack( int cell ) {
		super.attack( cell );
		if (Random.Float() < 0.5f) {
			play( kick );
		}
	}

	@Override
	public void die() {
		emitter().burst( Speck.factory( Speck.WOOL ), 5 );
		emitter().burst( ShadowParticle.UP, 4 );
		super.die();
	}
	
	@Override
	public void onComplete( Animation anim ) {
		super.onComplete( anim == kick ? attack : anim );
	}
}
