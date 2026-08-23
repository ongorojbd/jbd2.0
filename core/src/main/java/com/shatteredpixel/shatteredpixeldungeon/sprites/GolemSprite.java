/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2026 Evan Debenham
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
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Golem;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ElmoParticle;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Callback;

public class GolemSprite extends MobSprite {

	private Emitter teleParticles;
	
	public GolemSprite() {
		super();
		
		texture( Assets.Sprites.GOLEM );

		scale.set(0.5f);
		
		TextureFilm frames = new TextureFilm( texture, 40, 40 );
		
		idle = new Animation( 6, true );
		idle.frames( frames, 0, 1, 2 );
		
		run = new Animation( 12, true );
		run.frames( frames, 3, 4, 5, 6, 7 );
		
		attack = new Animation( 10, false );
		attack.frames( frames, 8, 9, 10, 0 );

		zap = attack.clone();
		
		die = new Animation( 15, false );
		die.frames( frames, 11);
		
		play( idle );
	}

	@Override
	public void link(Char ch) {
		super.link(ch);

	}

	@Override
	public void update() {
		super.update();

	}

	@Override
	public void kill() {
		super.kill();

	}

	@Override
	public int blood() {
		return 0xFF80706c;
	}

	public void zap( int cell ) {

		super.zap( cell );

		MagicMissile.boltFromChar( parent,
				MagicMissile.ELMO,
				this,
				cell,
				new Callback() {
					@Override
					public void call() {
						((Golem)ch).onZapComplete();
					}
				} );
		Sample.INSTANCE.play( Assets.Sounds.ZAP );
	}

	private boolean died = false;

	@Override
	public void onComplete( Animation anim ) {
		if (anim == die && !died) {
			died = true;
		}
		if (anim == zap) {
			idle();
		}
		super.onComplete( anim );
	}

	@Override
	public void die() {
		emitter().burst( Speck.factory( Speck.WOOL ), 5 );
		super.die();
	}
}
