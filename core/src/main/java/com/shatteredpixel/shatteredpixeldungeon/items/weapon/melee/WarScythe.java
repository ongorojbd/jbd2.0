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

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.SandalsOfNature;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfEvasion;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class WarScythe extends MeleeWeapon {

	{
		image = ItemSpriteSheet.WAR_SCYTHE;
		hitSound = Assets.Sounds.HAMON;
		hitSoundPitch = 0.9f;

		tier = 5;
		ACC = 0.8f; //20% penalty to accuracy
	}

	@Override
	public int max(int lvl) {
		return  Math.round(6.67f*(tier+1)) +    //40 base, up from 30
				lvl*(tier+1);                   //scaling unchanged
	}

	@Override
	public int proc(Char attacker, Char defender, int damage) {

		if (hero.belongings.getItem(RingOfEvasion.class) != null) {
			if (hero.belongings.getItem(RingOfEvasion.class).isEquipped(hero) && (Random.Int(20) == 0)) {
				{
					Sample.INSTANCE.play( Assets.Sounds.LIGHTNING );
					attacker.sprite.showStatus(CharSprite.POSITIVE, "[파문을 일점집중!]");
					Buff.affect( defender, Paralysis.class, 1f );
				}
			}
		}

		return super.proc(attacker, defender, damage);
	}

	@Override
	public String desc() {
		String info = Messages.get(this, "desc");
		if (hero.belongings.getItem(RingOfEvasion.class) != null) {
			if (hero.belongings.getItem(RingOfEvasion.class).isEquipped(hero))
				info += "\n\n" + Messages.get( WarScythe.class, "setbouns");}

		return info;
	}

	protected int baseChargeUse(Hero hero, Char target){
		return 2;
	}

	@Override
	public String targetingPrompt() {
		return Messages.get(this, "prompt");
	}

	@Override
	protected void duelistAbility(Hero hero, Integer target) {
		Sickle.harvestAbility(hero, target, 0.8f, this);
	}

}
