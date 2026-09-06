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

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BloodParticle;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.watabou.noosa.Game;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.noosa.tweeners.ScaleTweener;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

public class MobSprite extends CharSprite {

	private static final float FADE_TIME	= 3f;
	private static final float FALL_TIME	= 1f;

	//optional constant bleeding effect (opt-in per sprite via bleedConstantly())
	private boolean bleedsConstantly = false;
	private Emitter constantBlood;

	//call from a subclass constructor to make the mob drip blood for its whole life
	protected void bleedConstantly(){
		bleedsConstantly = true;
	}

	private void updateConstantBlood(){
		if (!bleedsConstantly){
			return;
		}
		if (constantBlood == null){
			//parent group isn't set at construction time, so attach lazily.
			//use a dedicated emitter (NOT the shared GameScene.emitter() pool): its lifetime is
			//then ours to manage, and killAndErase()-ing it can't corrupt the pool for other fx.
			if (parent != null){
				constantBlood = new Emitter();
				constantBlood.autoKill = false;
				constantBlood.pos(this);
				parent.add(constantBlood);
				constantBlood.pour(BloodParticle.FACTORY, 0.3f);
			}
		} else {
			constantBlood.visible = visible;
		}
	}

	//stops emission and drops any particles still in flight.
	//safe to call from any teardown path - the emitter is private and not pooled.
	private void clearConstantBlood(){
		bleedsConstantly = false;
		if (constantBlood != null){
			constantBlood.on = false;
			constantBlood.killAndErase();
			constantBlood = null;
		}
	}

	@Override
	public void update() {
		sleeping = ch != null && ch.isAlive() && ((Mob)ch).state == ((Mob)ch).SLEEPING;
		super.update();
		updateConstantBlood();
	}

	@Override
	public void die() {
		super.die();
		clearConstantBlood();
	}

	@Override
	public void kill() {
		super.kill();
		clearConstantBlood();
	}

	//catches every remaining teardown path: mob removed without dying (Char.destroy),
	//chasm death that skips sprite.die(), scene shutdown, etc.
	@Override
	public void destroy() {
		clearConstantBlood();
		super.destroy();
	}

	@Override
	public void onComplete( Animation anim ) {

		super.onComplete( anim );

		if (anim == die && parent != null) {
			parent.add( new AlphaTweener( this, 0, FADE_TIME ) {
				@Override
				protected void onComplete() {
					MobSprite.this.killAndErase();
				}
			} );
		}
	}

	public void fall() {

		origin.set( width / 2, height - DungeonTilemap.SIZE / 2 );
		angularSpeed = Random.Int( 2 ) == 0 ? -720 : 720;
		am = 1;

		clearConstantBlood();

		if (emo != null) {
			emo.killAndErase();
			emo = null;
		}

		hideEmo();

		if (health != null){
			health.killAndErase();
		}

		if (parent != null) parent.add( new ScaleTweener( this, new PointF( 0, 0 ), FALL_TIME ) {
				@Override
				protected void onComplete() {
					MobSprite.this.killAndErase();
					parent.erase( this );
				}
				@Override
				protected void updateValues( float progress ) {
					super.updateValues( progress );
					y += 12 * Game.elapsed;
					am = 1 - progress;
				}
			} );
		 else {
			// parent가 null인 경우 즉시 제거
			killAndErase();
		}
	}
}
