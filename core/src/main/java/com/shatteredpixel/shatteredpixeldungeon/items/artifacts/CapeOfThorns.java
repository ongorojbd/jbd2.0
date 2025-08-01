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

package com.shatteredpixel.shatteredpixeldungeon.items.artifacts;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.BossdiscG;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;


public class CapeOfThorns extends Artifact {

	{
		image = ItemSpriteSheet.ARTIFACT_CAPE;

		levelCap = 1;

		charge = 0;
		chargeCap = 100;
		cooldown = 0;

		defaultAction = "NONE"; //so it can be quickslotted
	}

	@Override
	protected ArtifactBuff passiveBuff() {
		return new Thorns();
	}

	@Override
	public void charge(Hero target, float amount) {
		if (cooldown == 0) {
			charge += Math.round(4*amount);
			updateQuickslot();
		}
		if (charge >= chargeCap){
			target.buff(Thorns.class).proc(0, null, null);
		}
	}

	@Override
	public String desc() {
		String desc = Messages.get(this, "desc");
		if (isEquipped( Dungeon.hero )) {
			desc += "\n\n";
			if (cooldown == 0)
				desc += Messages.get(this, "desc_inactive");
			else
				desc += Messages.get(this, "desc_active");
		}

		return desc;
	}

	public class Thorns extends ArtifactBuff{

		@Override
		public boolean act(){
			if (cooldown > 0) {
				cooldown--;
				if (cooldown == 0) {
					GLog.w( Messages.get(this, "inert") );
				}
				updateQuickslot();
			}
			spend(TICK);
			return true;
		}

		public int proc(int damage, Char attacker, Char defender){
			if (!cursed) {
				if (cooldown == 0){
					charge += damage;
					if (charge >= chargeCap){
						charge = 0;
						cooldown = 5;
						GLog.p( Messages.get(this, "radiating") );
						new Flare( 5, 32 ).color( 0xFFFF00, true ).show( hero.sprite, 2f );
						Sample.INSTANCE.play( Assets.Sounds.HIT_PARRY, 2, 0.66f );
					}
				}


			if (cooldown != 0){
				int deflected = Random.NormalIntRange(0, damage);
				damage -= deflected;

					if (attacker != null && Dungeon.level.adjacent(attacker.pos, defender.pos)) {
						attacker.damage(deflected, this);
					}

					exp+= deflected;

				}
				updateQuickslot();
				return damage;
			} else {
				return damage;
			}
		}

		@Override
		public String toString() {
			return Messages.get(this, "name");
		}

		@Override
		public String desc() {
			return Messages.get(this, "desc", dispTurns(cooldown));
		}

		@Override
		public int icon() {
			if (cooldown == 0)
				return BuffIndicator.NONE;
			else
				return BuffIndicator.THORNS;
		}

		@Override
		public void detach(){
			cooldown = 0;
			charge = 0;
			super.detach();
		}

	}

	public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe{

		{
			inputs =  new Class[]{BossdiscG.class};
			inQuantity = new int[]{1};

			cost = 0;

			output = CapeOfThorns.class;
			outQuantity = 1;
		}
	}





}