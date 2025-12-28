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
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Adrenaline;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SquirrelSprite;
import com.watabou.utils.Random;

public class U1 extends Mob {

	{
		spriteClass = SquirrelSprite.class;

		HP = HT = 15;
		defenseSkill = 5;

		EXP = 3;
		maxLvl = 10;

		baseSpeed = 1.5f; // 빠른 이동 속도
	}

	private boolean berserkMode = false;
	private int consecutiveAttacks = 0;

	@Override
	public int damageRoll() {
		int baseDamage = Random.NormalIntRange(2, 6);
		// 광분 모드: HP가 낮을수록 데미지 증가
		if (berserkMode) {
			float hpRatio = 1f - (HP / (float) HT);
			baseDamage = Math.round(baseDamage * (1f + hpRatio * 1.5f)); // 최대 2.5배
		}
		return baseDamage;
	}

	@Override
	public int attackSkill(Char target) {
		int skill = 10;
		// 광분 모드: 명중률 증가
		if (berserkMode) {
			skill += 5;
		}
		return skill;
	}

	@Override
	public int drRoll() {
		return super.drRoll() + Random.NormalIntRange(0, 2);
	}

	@Override
	protected boolean act() {
		// HP가 절반 이하면 광분 모드 활성화
		if (HP <= HT / 2 && !berserkMode) {
			berserkMode = true;
			if (Dungeon.level.heroFOV[pos]) {
				sprite.showStatus(CharSprite.NEGATIVE, Messages.get(this, "berserk"));
			}
			// 광분 모드에서 아드레날린 부여 (공격 속도 증가)
			Buff.affect(this, Adrenaline.class, 10f);
		}
		return super.act();
	}

	@Override
	public int attackProc(Char enemy, int damage) {
		damage = super.attackProc(enemy, damage);
		
		// 광분 모드에서 연속 공격 가능 (50% 확률)
		if (berserkMode && Random.Float() < 0.5f && enemy.isAlive() && consecutiveAttacks < 2) {
			consecutiveAttacks++;
			spend(-0.3f); // 다음 공격을 빠르게
			attack(enemy);
			consecutiveAttacks = 0;
		} else {
			consecutiveAttacks = 0;
		}
		
		return damage;
	}

	@Override
	public float attackDelay() {
		float delay = super.attackDelay();
		// 광분 모드: 공격 속도 증가
		if (berserkMode) {
			delay *= 0.6f; // 40% 빠른 공격
		}
		return delay;
	}
}

