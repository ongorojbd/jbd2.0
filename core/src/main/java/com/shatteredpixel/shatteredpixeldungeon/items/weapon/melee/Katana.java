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
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Chill;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.EnhancedRings;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FrostImbue;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SnowParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfArcana;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfWealth;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class Katana extends MeleeWeapon {

	{
		image = ItemSpriteSheet.KATANA;
		hitSound = Assets.Sounds.HIT_ICE;
		hitSoundPitch = 1.1f;

		tier = 4;
	}

	@Override
	public int max(int lvl) {
		return  4*(tier+1) +    //20 base, down from 25
				lvl*(tier+1);   //scaling unchanged
	}

	private int HealCount = 0;

	@Override
	public int proc(Char attacker, Char defender, int damage) {

		HealCount++;

		Heal(attacker);

		if (hero.belongings.getItem(RingOfWealth.class) != null) {
			if (hero.belongings.getItem(RingOfWealth.class).isEquipped(hero) && (Random.Int(10) == 0)) {
				Buff.affect( hero, FrostImbue.class, 3f );
			}
		}

		return super.proc(attacker, defender, damage);


	}

	private void Heal(Char attacker) {
		if (HealCount >= 10) {

			Sample.INSTANCE.play(Assets.Sounds.SHATTER, 1f);


			for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
				if (Dungeon.level.adjacent(mob.pos, hero.pos) && mob.alignment != Char.Alignment.ALLY) {

					for (int i : PathFinder.NEIGHBOURS8) {
						CellEmitter.get( hero.pos+i ).burst(SnowParticle.FACTORY, 9 );
					}
					Buff.affect( mob, Chill.class, 1f );
					Buff.affect( mob, Paralysis.class, 1f );
				}
			}

			attacker.sprite.showStatus(CharSprite.POSITIVE, "[급속 빙결]");
			HealCount = 0;
		}
	}

	@Override
	public String desc() {
		String info = Messages.get(this, "desc");
		if (hero.belongings.getItem(RingOfWealth.class) != null) {
			if (hero.belongings.getItem(RingOfWealth.class).isEquipped(hero))
				info += "\n\n" + Messages.get( Katana.class, "setbouns");}

		return info;
	}

	@Override
	public int defenseFactor( Char owner ) {
		return 4;	//4 extra defence
	}

	@Override
	public String targetingPrompt() {
		return Messages.get(this, "prompt");
	}

	@Override
	protected void duelistAbility(Hero hero, Integer target) {
		Rapier.lungeAbility(hero, target, 1.67f, 0, this);
	}
}
