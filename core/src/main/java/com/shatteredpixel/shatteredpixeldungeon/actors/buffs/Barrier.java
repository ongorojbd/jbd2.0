/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2024 Evan Debenham
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

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SmokeParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Jojo7;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Jojo8;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Jojo9;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Blocking;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.BArray;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;

public class Barrier extends ShieldBuff {
	
	{
		type = buffType.POSITIVE;
	}

	float partialLostShield;

	@Override
	public void incShield(int amt) {
		super.incShield(amt);
		partialLostShield = 0;
	}

	@Override
	public void setShield(int shield) {
		super.setShield(shield);
		if (shielding() == shield) partialLostShield = 0;
	}

	@Override
	public boolean act() {

		partialLostShield += Math.min(1f, shielding()/20f);

		if (partialLostShield >= 1f) {
			absorbDamage(1);
			partialLostShield = 0;
		}
		
		if (shielding() <= 0){
			detach();
		}
		
		spend( TICK );
		
		return true;
	}
	
	@Override
	public void fx(boolean on) {
		if (on) {
			target.sprite.add(CharSprite.State.SHIELDED);
		} else if (target.buff(Blocking.BlockBuff.class) == null) {
			target.sprite.remove(CharSprite.State.SHIELDED);
		}
	}
	
	@Override
	public int icon() {
		return BuffIndicator.ARMOR;
	}
	
	@Override
	public void tintIcon(Image icon) {
		icon.hardlight(0.5f, 1f, 2f);
	}

	@Override
	public String iconTextDisplay() {
		return Integer.toString(shielding());
	}
	
	@Override
	public String desc() {
		return Messages.get(this, "desc", shielding());
	}

	private static final String PARTIAL_LOST_SHIELD = "partial_lost_shield";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(PARTIAL_LOST_SHIELD, partialLostShield);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		partialLostShield = bundle.getFloat(PARTIAL_LOST_SHIELD);
	}

	@Override
	public void detach() {
		if (hero.belongings.getItem(Jojo7.class) != null && hero.belongings.getItem(Jojo8.class) != null && hero.belongings.getItem(Jojo9.class) != null) {

		if(target==hero){
		int damage = target.damageRoll();
		damage *= 1.5f;

		PathFinder.buildDistanceMap(target.pos, BArray.not(Dungeon.level.solid, null), 2);
		for (int cell = 0; cell < PathFinder.distance.length; cell++) {
			if (PathFinder.distance[cell] < Integer.MAX_VALUE) {
				CellEmitter.get( cell ).burst(Speck.factory(Speck.WOOL), 5);
				CellEmitter.get(cell).burst(BlastParticle.FACTORY, 15);
				CellEmitter.get(cell).burst(SmokeParticle.FACTORY, 4);
				Char ch = Actor.findChar(cell);
				if (ch != null&& !(ch instanceof Hero)) {
					ch.damage(damage, target);
				}}}

		Sample.INSTANCE.play( Assets.Sounds.BLAST, 3f, 1.35f );
		Camera.main.shake(1, 0.17f);

		}
		}

		super.detach();
	}

}
