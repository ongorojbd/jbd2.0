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

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.sprites.KS2Sprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

/**
 * KS2 - 카즈의 정예병 (칼과 방패)
 * 균형잡힌 방어와 공격력을 가진 방어형 유닛
 * 피해를 받을 때 단 한 번 대량의 배리어를 전개함
 */
public class KS2 extends Mob {

	{
		spriteClass = KS2Sprite.class;

		HP = HT = 220;
		defenseSkill = 30;

		EXP = 20;
		maxLvl = 30;

		properties.add(Property.UNDEAD);
		properties.add(Property.DEMONIC);
	}

	private boolean shieldUsed = false;

	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 45, 55 );
	}

	@Override
	public int attackSkill( Char target ) {
		return 40;
	}

	@Override
	public int drRoll() {
		return super.drRoll() + Random.NormalIntRange(0, 20);
	}

	@Override
	public void damage(int dmg, Object src) {
		super.damage(dmg, src);

		// 피해를 받았을 때, 아직 배리어를 사용하지 않았다면 단 한 번 대량의 배리어 전개
		if (!shieldUsed && buff(Barrier.class) == null) {
			Buff.affect(this, Barrier.class).setShield(120);
			sprite.emitter().burst(Speck.factory(Speck.LIGHT), 12);
			Sample.INSTANCE.play(Assets.Sounds.CHARGEUP);
			shieldUsed = true;
		}
	}

	@Override
	public void die( Object cause ) {

		super.die( cause );

		if (Random.Int( 3 ) == 0) {
			Dungeon.level.drop( new Gold().quantity(Random.IntRange(45, 55)), pos ).sprite.drop();
		}

		if (Dungeon.level.heroFOV[pos]) {
			Sample.INSTANCE.play( Assets.Sounds.BONES,  Random.Float(1.2f, 0.9f) );
			Sample.INSTANCE.play(Assets.Sounds.BURNING);
		}

	}
}

