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

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.RegrowthBomb;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SoldierSprite;
import com.watabou.utils.Random;

public class Soldier extends Mob {

	{
		spriteClass = SoldierSprite.class;

		HP = HT = 175;
		defenseSkill = 15;

		EXP = 15;
		maxLvl = 30;

		loot = new RegrowthBomb();
		lootChance = 0.15f;
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 30, 43 );
	}

	@Override
	public int attackSkill( Char target ) {
		return 40;
	}

	@Override
	public int drRoll() {
		return Random.NormalIntRange(0, 5);
	}

	@Override
	protected boolean canAttack( Char enemy ) {
		return true;
	}

	@Override
	public float lootChance() {
		return super.lootChance() * ((13f - Dungeon.LimitedDrops.NECRO_HP.count) / 13f);
	}

	@Override
	public Item createLoot(){
		Dungeon.LimitedDrops.NECRO_HP.count++;
		return super.createLoot();
	}


}
