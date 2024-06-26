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

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Dread;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.SoulMark;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.SandalsOfNature;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfSharpshooting;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfTenacity;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

public class Crossbow extends MeleeWeapon {
	
	{
		image = ItemSpriteSheet.CROSSBOW;
		hitSound = Assets.Sounds.HIT;
		hitSoundPitch = 1f;
		
		//check Dart.class for additional properties
		
		tier = 4;
	}

	@Override
	public boolean doUnequip(Hero hero, boolean collect, boolean single) {
		if (super.doUnequip(hero, collect, single)){
			if (hero.buff(ChargedShot.class) != null &&
					!(hero.belongings.weapon() instanceof Crossbow)
					&& !(hero.belongings.secondWep() instanceof Crossbow)){
				//clear charged shot if no crossbow is equipped
				hero.buff(ChargedShot.class).detach();
			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	public int proc(Char attacker, Char defender, int damage) {

		if (Dungeon.hero.belongings.getItem(RingOfSharpshooting.class) != null) {
			if (hero.belongings.getItem(RingOfSharpshooting.class).isEquipped(hero) && (Random.Int(20) == 0)) {
				Buff.affect(defender, SoulMark.class, 3f);
			}
		}
		return super.proc(attacker, defender, damage);
	}

	@Override
	public String desc() {
		String info = Messages.get(this, "desc");
		if (Dungeon.hero.belongings.getItem(RingOfSharpshooting.class) != null) {
			if (Dungeon.hero.belongings.getItem(RingOfSharpshooting.class).isEquipped(Dungeon.hero))
				info += "\n\n" + Messages.get( Crossbow.class, "setbouns");}

		return info;
	}
	
	@Override
	public int max(int lvl) {
		return  4*(tier+1) +    //20 base, down from 25
				lvl*(tier);     //+4 per level, down from +5
	}

	@Override
	protected void duelistAbility(Hero hero, Integer target) {
		if (hero.buff(ChargedShot.class) != null){
			GLog.w(Messages.get(this, "ability_cant_use"));
			return;
		}

		beforeAbilityUsed(hero, null);
		Buff.affect(hero, ChargedShot.class);
		Sword.doraclass();
		Sample.INSTANCE.play(Assets.Sounds.HIT_STRONG);
		hero.sprite.operate(hero.pos);
		hero.next();
		afterAbilityUsed(hero);
	}

	@Override
	public String abilityInfo() {
		if (levelKnown){
			return Messages.get(this, "ability_desc", 3+buffedLvl());
		} else {
			return Messages.get(this, "typical_ability_desc", 3);
		}
	}

	public static class ChargedShot extends Buff{

		{
			announced = true;
			type = buffType.POSITIVE;
		}

		@Override
		public int icon() {
			return BuffIndicator.DUEL_XBOW;
		}

	}
}
