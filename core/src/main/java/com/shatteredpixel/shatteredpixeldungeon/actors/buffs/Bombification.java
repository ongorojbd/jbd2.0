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

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;

//킬러 퀸의 사격 DISC로 부여되는 폭탄화 상태.
//기폭 전까지 무기한 지속되며, 한 번에 오직 하나의 대상만 이 상태가 될 수 있다.
public class Bombification extends Buff {

	{
		type = buffType.NEGATIVE;
		announced = true;
	}

	@Override
	public int icon() {
		return BuffIndicator.FIRE;
	}

	@Override
	public void tintIcon(Image icon) {
		icon.hardlight(1f, 0.2f, 0.2f);
	}

	@Override
	public void fx(boolean on) {
		if (on) {
			target.sprite.add(CharSprite.State.MARKED);
		} else if (target.sprite != null) {
			target.sprite.remove(CharSprite.State.MARKED);
		}
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc");
	}
}
