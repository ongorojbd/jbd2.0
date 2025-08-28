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

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Adrenaline;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.VampireSoldier2Sprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;

public class VampireSoldier2 extends Mob {

	{
		spriteClass = VampireSoldier2Sprite.class;

		HP = HT = 130;
		defenseSkill = 15;

		EXP = 12;
		maxLvl = 22;

		properties.add(Property.UNDEAD);
		properties.add(Property.DEMONIC);
	}

	@Override
	protected boolean act() {
		// Continuous empowerment aura: grant Adrenaline and a visible marker every turn
		int radius = 4;
		for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
			boolean eligible = mob != this
					&& mob.alignment == Alignment.ENEMY
					&& Dungeon.level.distance(this.pos, mob.pos) <= radius
					&& !Char.hasProp(mob, Char.Property.BOSS)
					&& !Char.hasProp(mob, Char.Property.MINIBOSS);
			if (eligible) {
				// Use a multi-turn buffer to prevent brief expiry/flicker between actor turns
				Buff.prolong(mob, Adrenaline.class, 3.1f);
				Buff.prolong(mob, EmpoweredByVampire.class, 3.1f);
			}
		}
		return super.act();
	}

	public static class EmpoweredByVampire extends com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff {
		{
			type = buffType.POSITIVE;
		}

		@Override
		public void fx(boolean on) {
			if (on) target.sprite.aura(0xAA0022, 3);
			else target.sprite.clearAura();
		}

		@Override
		public int icon() {
			return BuffIndicator.NONE; // no extra icon; aura only
		}
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 25, 30 );
	}

	@Override
	public int attackSkill( Char target ) {
		return 30;
	}

	@Override
	public int drRoll() {
		return super.drRoll() + Random.NormalIntRange(0, 12);
	}

	@Override
	public int attackProc(Char enemy, int damage) {
		int dealt = super.attackProc(enemy, damage);
		int heal = Math.max(1, Math.round(dealt * 0.25f));
		if (heal > 0 && HP < HT) {
			HP = Math.min(HT, HP + heal);
			if (sprite != null) sprite.showStatusWithIcon(CharSprite.POSITIVE, Integer.toString(heal), FloatingText.HEALING);
		}
		return dealt;
	}

	@Override
	public void die( Object cause ) {

		super.die( cause );

		if (Random.Int( 3 ) == 0) {
			Dungeon.level.drop( new Gold().quantity(Random.IntRange(45, 55)), pos ).sprite.drop();
		}

		if (Dungeon.level.heroFOV[pos]) {
			Sample.INSTANCE.play( Assets.Sounds.BONES,  Random.Float(1.2f, 0.9f) );
			Sample.INSTANCE.play(Assets.Sounds.BURNING);
		}

	}

}


