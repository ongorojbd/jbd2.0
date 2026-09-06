/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2026 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.items;

import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

//돌로미테의 이빨
//소지하고 있기만 하면, 치명적인 피해를 입었을 때 REVIVE_CHANCE 확률로
//REVIVE_HP 만큼의 체력을 남기고 즉시 부활한다. 소모되지 않는 영구 패시브.
//실제 부활 처리는 Hero.die(Object) 에서 이 아이템의 소지 여부를 확인해 수행한다.
public class DolomitesTeeth extends Item {

	public static final float REVIVE_CHANCE = 0.8f;
	public static final int REVIVE_HP = 10;

	{
		image = ItemSpriteSheet.TEETH;

		bones = true;
	}

	@Override
	public boolean isUpgradable() {
		return false;
	}

	@Override
	public boolean isIdentified() {
		return true;
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc", (int) (REVIVE_CHANCE * 100), REVIVE_HP);
	}

	@Override
	public int value() {
		return 200 * quantity;
	}
}
