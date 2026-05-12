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
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Spw;
import com.shatteredpixel.shatteredpixeldungeon.journal.Bestiary;
import com.shatteredpixel.shatteredpixeldungeon.sprites.Bt1Sprite;
import com.watabou.utils.Random;

public class Bt1mob extends Mob {

	private int level = Dungeon.scalingDepth();

	{
		spriteClass = Bt1Sprite.class;
		HP = HT = (1 + level) * 6;
		defenseSkill = 2 + level / 2;
		EXP = 0;

		viewDistance = 6;
	}

	@Override
	public int attackSkill(Char target) {
		if (target != null && alignment == Alignment.NEUTRAL && target.invisible <= 0) {
			return INFINITE_ACCURACY;
		} else {
			return 6 + level;
		}
	}

	@Override
	public int damageRoll() {
		if (alignment == Alignment.NEUTRAL) {
			return Random.NormalIntRange(2 + 2 * level, 2 + 2 * level);
		} else {
			return Random.NormalIntRange(1 + level, 2 + 2 * level);
		}
	}

	@Override
	public int drRoll() {
		return super.drRoll() + Random.NormalIntRange(0, 1 + level / 2);
	}

	@Override
	public int attackProc(Char enemy, int damage) {
		damage = super.attackProc(enemy, damage);
		if (enemy instanceof Mob) {
			((Mob) enemy).aggro(this);
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

    @Override
    public void die(Object cause) {

        super.die(cause);

		Bestiary.setSeen(Bt1.class);

        Dungeon.level.drop(new Spw().identify(), pos).sprite.drop(pos);

    }
}

