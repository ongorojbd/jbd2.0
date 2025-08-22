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

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.VampireSoldierSprite;
import com.watabou.utils.Random;

public class VampireSoldier extends Mob {

	{
		spriteClass = VampireSoldierSprite.class;

		// 콜로세움 기사와 유사한 튜닝
		HP = HT = 175;
		defenseSkill = 15;
		EXP = 15;
		maxLvl = 30;

	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange(30, 43);
	}

	@Override
	public int attackSkill(Char target) {
		return 40;
	}

	@Override
	public int drRoll() {
		return Random.NormalIntRange(0, 5);
	}

	@Override
	public int attackProc(Char enemy, int damage) {
		int dealt = super.attackProc(enemy, damage);
		int heal = Math.max(1, Math.round(dealt * 0.25f));
		if (heal > 0 && HP < HT) {
			HP = Math.min(HT, HP + heal);
			if (sprite != null) sprite.showStatus(CharSprite.POSITIVE, Integer.toString(heal));
		}
		return dealt;
	}

}


