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

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.EnhancedRings;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.SandalsOfNature;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfElements;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

public class RunicBlade extends MeleeWeapon {

	{
		image = ItemSpriteSheet.RUNIC_BLADE;
		hitSound = Assets.Sounds.HIT_CRUSH;
		hitSoundPitch = 0.8f;

		tier = 4;
	}

	//Essentially it's a tier 4 weapon, with tier 3 base max damage, and tier 5 scaling.
	//equal to tier 4 in damage at +5

	@Override
	public int max(int lvl) {
		return  5*(tier) +                	//20 base, down from 25
				Math.round(lvl*(tier+2));	//+6 per level, up from +5
	}

	@Override
	public int proc(Char attacker, Char defender, int damage) {


		if (hero.belongings.getItem(RingOfElements.class) != null) {
			if (hero.belongings.getItem(RingOfElements.class).isEquipped(hero)) {
				{
					Buff.affect( hero, EnhancedRings.class, 3f );
				}
			}
		}

		return super.proc(attacker, defender, damage);
	}

	@Override
	public String desc() {
		String info = Messages.get(this, "desc");
		if (hero.belongings.getItem(RingOfElements.class) != null) {
			if (hero.belongings.getItem(RingOfElements.class).isEquipped(hero))
				info += "\n\n" + Messages.get( RunicBlade.class, "setbouns");}

		return info;
	}



}