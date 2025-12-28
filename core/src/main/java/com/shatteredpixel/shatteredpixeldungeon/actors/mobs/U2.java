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
import com.shatteredpixel.shatteredpixeldungeon.sprites.FishSprite;
import com.watabou.utils.Random;

public class U2 extends Mob {

	{
		spriteClass = FishSprite.class;

		baseSpeed = 2f;

		EXP = 3;
		maxLvl = 10;

		SLEEPING = new Sleeping();
		WANDERING = new Wandering();
		HUNTING = new Hunting();

		state = SLEEPING;
	}

	private int consecutiveAttacks = 0;

	public U2() {
		super();

		HP = HT = 15 + Dungeon.depth * 3;
		defenseSkill = 8 + Dungeon.depth * 2;
	}

	@Override
	protected boolean act() {
		// 물 밖에서도 생존 가능 (dieOnLand 제거)
		return super.act();
	}

	@Override
	public int damageRoll() {
		int baseDamage = Random.NormalIntRange(Dungeon.depth + 2, 4 + Dungeon.depth * 2);
		// 물에 있으면 데미지 증가
		if (Dungeon.level.water[pos]) {
			baseDamage = Math.round(baseDamage * 1.5f);
		}
		return baseDamage;
	}

	@Override
	public int attackSkill(Char target) {
		int skill = 12 + Dungeon.depth * 2;
		// 물에 있으면 명중률 증가
		if (Dungeon.level.water[pos]) {
			skill += 5;
		}
		return skill;
	}

	@Override
	public int drRoll() {
		return super.drRoll() + Random.NormalIntRange(0, Dungeon.depth);
	}

	@Override
	public int attackProc(Char enemy, int damage) {
		damage = super.attackProc(enemy, damage);
		// 연속 공격 가능 (30% 확률)
		if (Random.Float() < 0.3f && enemy.isAlive() && consecutiveAttacks < 1) {
			consecutiveAttacks++;
			spend(-0.5f); // 다음 공격을 빠르게
			attack(enemy);
			consecutiveAttacks = 0;
		} else {
			consecutiveAttacks = 0;
		}
		return damage;
	}
}

