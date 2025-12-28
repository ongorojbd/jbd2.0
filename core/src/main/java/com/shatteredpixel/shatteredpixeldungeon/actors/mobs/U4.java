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
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.sprites.TentacleSprite;
import com.watabou.utils.Random;

public class U4 extends Mob {

	{
		spriteClass = TentacleSprite.class;

		HP = HT = 20;
		defenseSkill = 6;

		EXP = 4;
		maxLvl = 10;
	}

	private int abilityCooldown = 0;

	@Override
	public int damageRoll() {
		return Random.NormalIntRange(3, 8);
	}

	@Override
	public int attackSkill(Char target) {
		return 14;
	}

	@Override
	public int drRoll() {
		return super.drRoll() + Random.NormalIntRange(0, 3);
	}

	@Override
	protected boolean act() {
		abilityCooldown--;
		return super.act();
	}

	@Override
	public int attackProc(Char enemy, int damage) {
		damage = super.attackProc(enemy, damage);
		// 관통 공격 능력 (쿨다운 3턴, 원거리 공격 시)
		if (abilityCooldown <= 0 && enemy != null && !Dungeon.level.adjacent(pos, enemy.pos)) {
			Ballistica attack = new Ballistica(pos, enemy.pos, Ballistica.PROJECTILE);
			for (int i = 1; i < attack.dist; i++) {
				int cell = attack.path.get(i);
				Char ch = Actor.findChar(cell);
				if (ch != null && ch != enemy && ch.alignment != alignment) {
					// 경로상의 다른 적에게도 데미지
					int dmg = damageRoll();
					dmg = Math.round(dmg * 0.5f); // 50% 데미지
					ch.damage(dmg, this);
					if (ch.sprite != null) {
						ch.sprite.flash();
					}
				}
			}
			abilityCooldown = 3;
		}
		return damage;
	}
}

