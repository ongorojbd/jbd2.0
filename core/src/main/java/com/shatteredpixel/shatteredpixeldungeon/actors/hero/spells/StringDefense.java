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

package com.shatteredpixel.shatteredpixeldungeon.actors.hero.spells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Combo;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.HolyTome;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Sword;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.watabou.noosa.audio.Sample;

public class StringDefense extends ClericSpell {

	public static final StringDefense INSTANCE = new StringDefense();

	@Override
	public int icon() {
		return HeroIcon.COMBO;
	}

	@Override
	public float chargeUse(Hero hero) {
		return 5 - hero.pointsInTalent(Talent.JOLYNE_NEW1);
	}

	@Override
	public boolean canCast(Hero hero) {
		return hero.hasTalent(Talent.JOLYNE_NEW1);
	}

	@Override
	public void onCast(HolyTome tome, Hero hero) {
		Buff.affect(hero, Combo.ParryTracker.class, Actor.TICK);
		Sword.stonefreeclass();
		Sample.INSTANCE.play(Assets.Sounds.HIT_PARRY);
		hero.sprite.operate(hero.pos);
		hero.spendAndNext(Actor.TICK);
		hero.busy();
		onSpellCast(tome, hero);
	}
}
