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

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.sprites.Bt1Sprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.HashSet;

public class Bt1 extends Mob {

	private int level = Dungeon.depth / 3;

	private HashSet<Integer> paralyzedTargets = new HashSet<>();

	{
		spriteClass = Bt1Sprite.class;
		HP = HT = (2 + level) * 7; // slightly tougher than GSoldier
		EXP = 0;

		viewDistance = 6;
		alignment = Alignment.ALLY;
		intelligentAlly = true;
	}

	@Override
	public int attackSkill(Char target) {
		if (target != null && alignment == Alignment.NEUTRAL && target.invisible <= 0) {
			return INFINITE_ACCURACY;
		} else {
			return 9 + level;
		}
	}

	@Override
	public int damageRoll() {
		if (alignment == Alignment.NEUTRAL) {
			return Math.round((2 + 2 * level) * 0.9f);
		} else {
			return Random.NormalIntRange(Math.round((1 + level) * 0.9f), Math.round((2 + 2 * level) * 0.9f));
		}
	}

	@Override
	public int drRoll() {
		return super.drRoll() + Random.NormalIntRange(0, 2 + level / 2);
	}

	@Override
	public int attackProc(Char enemy, int damage) {
		damage = super.attackProc(enemy, damage);
		if (enemy instanceof Mob) {
			((Mob) enemy).aggro(this);
		}
		if (!paralyzedTargets.contains(enemy.id())) {
			paralyzedTargets.add(enemy.id());
			Buff.affect(enemy, Paralysis.class, 2f);
		}
		return damage;
	}

	@Override
	protected boolean canAttack(Char enemy) {
		return Dungeon.level.adjacent(pos, enemy.pos);
	}

	@Override
	protected boolean getCloser(int target) {
		target = hero.pos;
		return super.getCloser(target);
	}

	private static final String PARALYZED_TARGETS = "paralyzed_targets";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		int[] ids = new int[paralyzedTargets.size()];
		int i = 0;
		for (int id : paralyzedTargets) ids[i++] = id;
		bundle.put(PARALYZED_TARGETS, ids);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		paralyzedTargets = new HashSet<>();
		if (bundle.contains(PARALYZED_TARGETS)) {
			for (int id : bundle.getIntArray(PARALYZED_TARGETS)) {
				paralyzedTargets.add(id);
			}
		}
	}
}

