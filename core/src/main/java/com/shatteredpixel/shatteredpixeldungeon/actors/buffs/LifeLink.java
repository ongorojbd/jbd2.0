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

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.DwarfKing;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Ghoul;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;

public class LifeLink extends FlavourBuff {

	public int object = 0;

	private static final String OBJECT = "object";

	{
		type = buffType.POSITIVE;
		announced = true;
	}

	public String name() {
		if (target instanceof DwarfKing || target instanceof Ghoul) {
			return Messages.get(this, "name");
		} else {
			return Messages.get(this, "name2");
		}
	}

	public String desc(){
		if (target instanceof DwarfKing || target instanceof Ghoul) {
			return Messages.get(this, "desc", dispTurns());
		} else {
			return Messages.get(this, "desc2", dispTurns());
		}
	}

	@Override
	public void detach() {
		super.detach();
		Char ch = (Char)Actor.findById(object);
		if (!target.isActive() && ch != null){
			for (LifeLink l : ch.buffs(LifeLink.class)){
				if (l.object == target.id()){
					l.detach();
				}
			}
		}
	}

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( OBJECT, object );
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		object = bundle.getInt( OBJECT );
	}

	@Override
	public int icon() {
		return BuffIndicator.HEART;
	}

	@Override
	public void tintIcon(Image icon) {
		icon.hardlight(1, 0, 1);
	}

	@Override
	public float iconFadePercent() {
		int duration = Math.round(6.67f + 3.33f*Dungeon.hero.pointsInTalent(Talent.LIFE_LINK));
		return Math.max(0, (duration - visualcooldown()) / duration);
	}

}
