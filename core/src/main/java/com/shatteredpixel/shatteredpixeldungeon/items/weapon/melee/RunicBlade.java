/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2022 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.CloakOfShadows;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.UnstableSpellbook;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class RunicBlade extends MeleeWeapon {

	{
		image = ItemSpriteSheet.RUNIC_BLADE;
		hitSound = Assets.Sounds.FF;
		hitSoundPitch = 1f;

		tier = 4;
		RCH = 2;    //extra reach
	}

	@Override
	public int max(int lvl) {
		return  4*(tier) + 2 +  // 22 + 4
				lvl*(tier);
	}


	@Override
	public int proc(Char attacker, Char defender, int damage) {
		float dmgbouns = 1f;
		if (Dungeon.level.map[attacker.pos] == Terrain.WATER) {
			dmgbouns += 0.2f;
		}
		if (Dungeon.level.map[defender.pos] == Terrain.WATER) {
			dmgbouns += 0.2f;
		}

		damage *= dmgbouns;
		return super.proc(attacker, defender, damage);
	}

	@Override
	public String desc() {
		String info = Messages.get(this, "desc");
		if (Dungeon.hero.belongings.getItem(UnstableSpellbook.class) != null) {
			if (Dungeon.hero.belongings.getItem(UnstableSpellbook.class).isEquipped(Dungeon.hero))
				info += "\n\n" + Messages.get( RunicBlade.class, "setbouns");}

		return info;
	}
}
