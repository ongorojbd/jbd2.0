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

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Adrenaline;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bless;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.BlobImmunity;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Charm;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Doom;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Haste;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hex;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Light;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.CapeOfThorns;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SWATSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class SWAT extends Mob implements Callback {

	private static final float TIME_TO_ZAP = 1f;

	{
		spriteClass = SWATSprite.class;

		HP = HT = 1000;
		defenseSkill = 25;
		state = PASSIVE;

		EXP = 77;
		maxLvl = 30;

		flying = true;

		properties.add(Property.BOSS);

	}
	int damageTaken = 0;
	public int  Phase = 0;

	@Override
	public void die( Object cause ) {

		super.die( cause );

		if (Random.Int( 4 ) == 0) {
			Dungeon.level.drop( new CapeOfThorns().identify().upgrade(10), pos ).sprite.drop( pos );
			new Flare( 5, 32 ).color( 0xFFFF00, true ).show( hero.sprite, 2f );
			Sample.INSTANCE.play(Assets.Sounds.BADGE);
			GLog.p(Messages.get(Kawasiri.class, "rare"));
		}


		yell( Messages.get(this, "defeated") );
	}

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle(bundle);
		bundle.put( PHASE, Phase );
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		Phase = bundle.getInt(PHASE);
	}

	@Override
	public void damage(int dmg, Object src) {

		if (dmg >= 250){
			//takes 20/21/22/23/24/25/26/27/28/29/30 dmg
			// at   20/22/25/29/34/40/47/55/64/74/85 incoming dmg
			dmg = 250;
		}

		super.damage(dmg, src);

		if (Phase==0 && HP < 999) {
			Phase = 1;
			HP = 999;
			state = HUNTING;
			if (!BossHealthBar.isAssigned()) {
				BossHealthBar.assignBoss(this);
				for (Char ch : Actor.chars()){
				}
			}
			Buff.prolong(this, Bless.class, Bless.DURATION*5000f);
			Buff.prolong(this, BlobImmunity.class, BlobImmunity.DURATION*5000f);
			Buff.prolong(this, Haste.class, Haste.DURATION*5000f);
			Buff.prolong(this, Light.class, Light.DURATION*5000f);
			Buff.prolong(this, Adrenaline.class, Adrenaline.DURATION*5000f);
			Sample.INSTANCE.play( Assets.Sounds.MASTERY );

			new Flare( 5, 32 ).color( 0xFFFF00, true ).show( this.sprite, 3f );
				yell(Messages.get(this, "notice"));

		}
	}

	private static final String PHASE   = "Phase";

	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 51, 57 );
	}

	@Override
	public int attackSkill( Char target ) {
		return 40;
	}

	@Override
	public int drRoll() {
		return Random.NormalIntRange(0, 10);
	}

	@Override
	public void notice() {
		super.notice();
	}

	@Override
	protected boolean canAttack(Char enemy) {
		return new Ballistica(pos, enemy.pos, Ballistica.MAGIC_BOLT).collisionPos == enemy.pos;
	}

	protected boolean doAttack(Char enemy) {

		if (Dungeon.level.adjacent(pos, enemy.pos)) {

			return super.doAttack(enemy);

		} else {

			if (sprite != null && (sprite.visible || enemy.sprite.visible)) {
				sprite.zap(enemy.pos);
				return false;
			} else {
				zap();
				return true;
			}
		}
	}

	//used so resistances can differentiate between melee and magical attacks
	public static class DarkBolt {
	}

	private void zap() {
		spend(TIME_TO_ZAP);

		if (hit(this, enemy, true)) {
			//TODO would be nice for this to work on ghost/statues too
			if (enemy == Dungeon.hero && Random.Int(0) == 0) {
				Buff.prolong(enemy, Charm.class, Charm.DURATION);
				Sample.INSTANCE.play(Assets.Sounds.HIT);
			}

			int dmg = Random.NormalIntRange(25, 30);
			enemy.damage(dmg, new DarkBolt());

			if (enemy == Dungeon.hero && !enemy.isAlive()) {
				Dungeon.fail(getClass());
				GLog.n(Messages.get(this, "bolt_kill"));
			}
		} else {
			enemy.sprite.showStatus(CharSprite.NEUTRAL, enemy.defenseVerb());
		}
	}

	public void onZapComplete() {
		zap();
		next();
	}

	@Override
	public void call() {
		next();
	}

}
