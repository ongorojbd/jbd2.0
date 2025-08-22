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

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.CursedWand;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.sprites.Abomination2Sprite;
import com.watabou.utils.Random;

public class Abomination2 extends Mob {

	{
		spriteClass = Abomination2Sprite.class;

		// 강화된 변종 설정
		HP = HT = 80;
		defenseSkill = 14;
		EXP = 10;
		maxLvl = 20;

		properties.add(Property.UNDEAD);
		properties.add(Property.DEMONIC);
	}

	private static final float TIME_TO_ZAP = 2f;

	@Override
	protected boolean canAttack(Char enemy) {
		return new Ballistica(pos, enemy.pos, Ballistica.MAGIC_BOLT).collisionPos == enemy.pos;
	}

	@Override
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

	// 원거리 마법 피해 타입 구분용
	public static class CursedBolt {}

	private void zap() {
		spend(TIME_TO_ZAP);

		if (hit(this, enemy, true)) {
			int dmg = Random.NormalIntRange(8, 16);
			enemy.damage(dmg, new CursedBolt());

			// 명중 시 무작위 저주 완드 효과 발동(희귀도 고정: 커먼 풀만 사용)
			Ballistica aim = new Ballistica(pos, enemy.pos, Ballistica.STOP_TARGET);
			CursedWand.randomValidCommonEffect(null, this, aim, false).effect(null, this, aim, false);
			if (enemy == Dungeon.hero && !enemy.isAlive()) {
				Dungeon.fail(getClass());
			}
		} else {
			enemy.sprite.showStatus(com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite.NEUTRAL, enemy.defenseVerb());
		}
	}

	public void onZapComplete() {
		zap();
		next();
	}

	// 공격 시에는 별도 저주 시전 없음(고유 패턴과 차별화)

	@Override
	public int damageRoll() {
		return Random.NormalIntRange(6, 14);
	}

	@Override
	public int attackSkill(Char target) {
		return 14;
	}

	@Override
	public int drRoll() {
		return super.drRoll() + Random.NormalIntRange(0, 5);
	}
}


