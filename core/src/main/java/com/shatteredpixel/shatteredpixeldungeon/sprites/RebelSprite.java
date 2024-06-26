/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2021 Evan Debenham
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
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Eye;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.EnergyParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SacrificialParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShaftParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SmokeParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class RebelSprite extends MobSprite {

	private int cellToAttack;
	private Emitter chargeParticles;

	public RebelSprite() {
		super();
		
		texture( Assets.Sprites.REBEL );
		
		TextureFilm frames = new TextureFilm( texture, 16, 16 );

		idle = new Animation( 10, true );
		idle.frames( frames, 0, 0, 1, 1, 2, 1, 1, 0 );

		run = new Animation( 15, true );
		run.frames( frames, 4, 5, 6, 7, 8, 9 );

		attack = new Animation( 15, false );
		attack.frames( frames, 10, 11, 12, 0 );
		
		zap = attack.clone();

		die = new Animation( 10, false );
		die.frames( frames, 0, 13, 14, 15 );
		
		play( idle );
	}

	@Override
	public void link(Char ch) {
		super.link(ch);

		chargeParticles = centerEmitter();
		chargeParticles.autoKill = true;
		chargeParticles.pour(MagicMissile.MagicParticle.ATTRACTING, 0.05f);
		chargeParticles.on = true;

	}

	@Override
	public void update() {
		super.update();
		if (chargeParticles != null){
			chargeParticles.pos( center() );
			chargeParticles.visible = visible;
		}
	}

	@Override
	public void attack( int cell ) {
		if (!Dungeon.level.adjacent( cell, ch.pos )) {

			cellToAttack = cell;
			turnTo( ch.pos , cell );
			play( zap );

		} else {
			
			super.attack( cell );
			
		}
	}
	
	@Override
	public void onComplete( Animation anim ) {
		if (anim == zap) {
			idle();
			Sample.INSTANCE.play( Assets.Sounds.RAY, 2, Random.Float(0.33f, 0.66f) );
			((MissileSprite)parent.recycle( MissileSprite.class )).
			reset( this, cellToAttack, new RebelShot(), new Callback() {

				@Override
				public void call() {
					ch.onAttackComplete();
				}
			} );
		} else {
			super.onComplete( anim );
		}
	}

	@Override
	public void die() {
		super.die();
		if (chargeParticles != null){
			chargeParticles.on = false;
		}
	}

	public class RebelShot extends Item {
		{
			image = ItemSpriteSheet.THROWING_KNIFE;
		}
	}
}
