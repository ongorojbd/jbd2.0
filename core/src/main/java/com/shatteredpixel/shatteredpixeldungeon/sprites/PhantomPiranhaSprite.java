/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2023 Evan Debenham
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
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.particles.Emitter;

public class PhantomPiranhaSprite extends MobSprite {

	private Emitter sparkles;

	public PhantomPiranhaSprite() {
		super();

		renderShadow = false;
		perspectiveRaise = 0.2f;

		texture( Assets.Sprites.FF );

		TextureFilm frames = new TextureFilm( texture, 13, 17 );

		idle = new MovieClip.Animation( 8, true );
		idle.frames( frames, 0, 1, 2, 1 );

		run = new MovieClip.Animation( 14, true );
		run.frames( frames, 0, 1, 2, 1 );

		attack = new MovieClip.Animation( 20, false );
		attack.frames( frames, 3, 4, 5, 6, 7, 8, 9, 10);

		die = new MovieClip.Animation( 8, false );
		die.frames( frames,  11, 12, 13 );

		play( idle );
	}

	@Override
	public void link(Char ch) {
		super.link(ch);
		renderShadow = false;

		if (sparkles == null) {
			sparkles = emitter();
			sparkles.pour( Speck.factory( Speck.LIGHT ), 0.5f );
		}
	}

	@Override
	public void update() {
		super.update();

		if (sparkles != null) {
			sparkles.visible = visible;
		}
	}

	@Override
	public void die() {
		super.die();

		if (sparkles != null) {
			sparkles.on = false;
		}
	}

	@Override
	public void kill() {
		super.kill();

		if (sparkles != null) {
			sparkles.on = false;
		}
	}

	@Override
	public void onComplete( MovieClip.Animation anim ) {
		super.onComplete( anim );

		if (anim == attack) {
			GameScene.ripple( ch.pos );
		}
	}
}

