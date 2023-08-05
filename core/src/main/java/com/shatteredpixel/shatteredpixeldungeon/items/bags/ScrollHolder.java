/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2023 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.items.bags;

import com.shatteredpixel.shatteredpixeldungeon.items.ArcaneResin;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.Ram2;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.Scroll;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.AdvancedEvolution;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.BeaconOfReturning;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.ChaosCatalyst;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Highway;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Kinga;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Kingc;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Kingm;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Kings;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Kingt;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Kingw;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Maga;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Map1;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Map2;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Map3;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Mdisc;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Neoro;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Newro;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Rocacaca;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Sbr1;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Sbr2;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Sbr3;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Sbr4;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Sbr5;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Sbr6;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Sbr7;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Sbr8;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Sbr9;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.ScrollOfExtract;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Spell;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Willa;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Willc;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Willg;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Xray;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class ScrollHolder extends Bag {

	{
		image = ItemSpriteSheet.HOLDER;
	}

	@Override
	public boolean canHold( Item item ) {
		if (item instanceof Scroll || item instanceof Spell || item instanceof ArcaneResin){

			if ((item instanceof Kingt || item instanceof Kings || item instanceof Xray || item instanceof Kingc || item instanceof Kingw || item instanceof Kinga || item instanceof Kingm || item instanceof ChaosCatalyst
					|| item instanceof ScrollOfExtract || item instanceof AdvancedEvolution || item instanceof Rocacaca || item instanceof Newro
					|| item instanceof Map1 || item instanceof Map2 || item instanceof Map3 || item instanceof Sbr1 || item instanceof Sbr2 || item instanceof Sbr3 || item instanceof Sbr4 || item instanceof Sbr5 || item instanceof Sbr6
					|| item instanceof Sbr7 || item instanceof Sbr8 || item instanceof Sbr9 || item instanceof Willa || item instanceof Willc || item instanceof Willg || item instanceof Highway || item instanceof Mdisc || item instanceof Maga

			)) {
				return false;
			} else {
				return super.canHold(item);
			}

		} else {
			return false;
		}
	}

	public int capacity(){
		return 19;
	}
	
	@Override
	public void onDetach( ) {
		super.onDetach();
		for (Item item : items) {
			if (item instanceof BeaconOfReturning) {
				((BeaconOfReturning) item).returnDepth = -1;
			}
		}
	}
	
	@Override
	public int value() {
		return 40;
	}

}
