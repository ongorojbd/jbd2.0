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

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ArmorAbility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.duelist.Challenge;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.duelist.ElementalStrike;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.duelist.Feint;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.DuelistArmor;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

public class Rapier2 extends MeleeWeapon {

	private static final int REQUIEM_FEINT = 0;
	private static final int REQUIEM_CHALLENGE = 1;
	private static final int REQUIEM_ELEMENTAL_STRIKE = 2;

	private int pendingRequiemAbility = -1;

	{
		image = ItemSpriteSheet.RAPIER2;
		hitSound = Assets.Sounds.HIT_CRUSH;
		hitSoundPitch = 1.3f;

		tier = 1;

		bones = false;
	}

	@Override
	public int max(int lvl) {
		return  4*(tier+1) +
				lvl*(tier+1);
	}

	@Override
	public int defenseFactor( com.shatteredpixel.shatteredpixeldungeon.actors.Char owner ) {
		return 1;
	}

	@Override
	public String targetingPrompt() {
		switch (pendingRequiemAbility()) {
			case REQUIEM_FEINT:
				return new Feint().targetingPrompt();
			case REQUIEM_CHALLENGE:
				return new Challenge().targetingPrompt();
			case REQUIEM_ELEMENTAL_STRIKE:
				return new ElementalStrike().targetingPrompt();
			default:
				return Messages.get(this, "prompt");
		}
	}

	@Override
	protected void duelistAbility(Hero hero, Integer target) {
		if (target == null) {
			return;
		}

		Charger charger = Buff.affect(hero, Charger.class);
		int charges = charger.charges;
		float partialCharge = charger.partialCharge;

		beforeAbilityUsed(hero, null);

		ArmorAbility ability;
		switch (pendingRequiemAbility()) {
			case REQUIEM_FEINT:
				ability = new Feint();
				break;
			case REQUIEM_CHALLENGE:
				ability = new Challenge();
				break;
			case REQUIEM_ELEMENTAL_STRIKE:
			default:
				ability = new ElementalStrike();
				break;
		}

		DuelistArmor dummyArmor = new DuelistArmor();
		dummyArmor.charge = 1000f;
		ability.activateForItem(dummyArmor, hero, target);

		if (dummyArmor.charge < 1000f) {
			pendingRequiemAbility = -1;
			afterAbilityUsed(hero);
		} else {
			charger.charges = charges;
			charger.partialCharge = partialCharge;
			hero.belongings.abilityWeapon = null;
			updateQuickslot();
		}
	}

	@Override
	public String abilityInfo() {
		return Messages.get(this, "ability_desc");
	}

	public String upgradeAbilityStat(int level){
		return "";
	}

	private int pendingRequiemAbility() {
		if (pendingRequiemAbility == -1) {
			pendingRequiemAbility = Random.Int(3);
		}
		return pendingRequiemAbility;
	}
}
