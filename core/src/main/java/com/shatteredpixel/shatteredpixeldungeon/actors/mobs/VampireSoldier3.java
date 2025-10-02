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

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.effects.SpellSprite;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.VampireSoldier3Sprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class VampireSoldier3 extends Mob {

	{
		spriteClass = VampireSoldier3Sprite.class;


		HP = HT = 180;
		defenseSkill = 26;

		EXP = 16;
		maxLvl = 27;

		properties.add(Property.UNDEAD);
		properties.add(Property.DEMONIC);
	}

	private int counterCooldown = 0;
	private boolean counterWindup = false;
	private static final String COUNTER_CD = "counter_cooldown";
	private static final String COUNTER_WINDUP = "counter_windup";

	@Override
	public void storeInBundle(com.watabou.utils.Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(COUNTER_CD, counterCooldown);
		bundle.put(COUNTER_WINDUP, counterWindup);
	}

	@Override
	public void restoreFromBundle(com.watabou.utils.Bundle bundle) {
		super.restoreFromBundle(bundle);
		counterCooldown = bundle.getInt(COUNTER_CD);
		counterWindup = bundle.getBoolean(COUNTER_WINDUP);
	}

	@Override
	protected boolean act() {
		if (counterCooldown > 0) counterCooldown--;
		if (state != SLEEPING) {
			// 경고와 동시에 반격 자세 진입
			if (counterCooldown == 1) {
				sprite.showStatus(CharSprite.WARNING, Messages.get(this, "ready"));
				GLog.w(Messages.get(this, "ready2"));
				SpellSprite.show(Dungeon.hero, SpellSprite.VISION, 1, 0f, 0f);
				Dungeon.hero.interrupt();
				Buff.affect(this, CounterStance.class, 1f);
				counterWindup = false;
				counterCooldown = 6;
			} else if (counterCooldown <= 0) {
				// 비정상 타이밍 보정: 그래도 바로 자세 진입
				Buff.affect(this, CounterStance.class, 1f);
				counterCooldown = 6;
			}
		}
		return super.act();
	}

	@Override
	public int defenseProc( Char enemy, int damage ) {
		if (this.buff(CounterStance.class) != null) {

			Sample.INSTANCE.play(Assets.Sounds.HIT_PARRY, 1, Random.Float(0.96f, 1.05f));
			// consume stance and nullify incoming damage
			buff(CounterStance.class).detach();
			int ret = 0;

			// counterattack the attacker
			if (enemy == hero) {
				enemy.damage(Random.NormalIntRange(35, 45), this);
			} else {
				enemy.damage(Random.NormalIntRange(12, 15), this);
			}

			if (enemy == Dungeon.hero && !enemy.isAlive()) {
				Dungeon.fail(getClass());
			}

			return ret;
		}
		return super.defenseProc( enemy, damage );
	}

    // use default defenseVerb
	@Override
	public int defenseSkill(Char enemy) {
		// allow hit to go through so defenseProc can parry & counter
		return super.defenseSkill(enemy);
	}

	public static class CounterStance extends com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff {
		{
			type = buffType.POSITIVE;
		}

		@Override
		public int icon() {
			return BuffIndicator.NONE; // no extra icon; aura only
		}
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 35, 45 );
	}

	@Override
	public int attackSkill( Char target ) {
		return 36;
	}

	@Override
	public int drRoll() {
		return super.drRoll() + Random.NormalIntRange(0, 20);
	}

	@Override
	public int attackProc(Char enemy, int damage) {
		int dealt = super.attackProc(enemy, damage);
		int heal = Math.max(1, Math.round(dealt * 0.25f));
		if (heal > 0 && HP < HT) {
			HP = Math.min(HT, HP + heal);
			if (sprite != null) sprite.showStatusWithIcon(CharSprite.POSITIVE, Integer.toString(heal), FloatingText.HEALING);
		}
		return dealt;
	}

}


