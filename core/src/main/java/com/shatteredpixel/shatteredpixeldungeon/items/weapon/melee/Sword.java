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
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ArtifactRecharge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.UnstableSpellbook;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.ui.AttackIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

public class Sword extends MeleeWeapon {

	{
		image = ItemSpriteSheet.SWORD;
		hitSound = Assets.Sounds.HIT_SLASH;
		hitSoundPitch = 1.0f;

		tier = 3;
	}

	@Override
	public int proc(Char attacker, Char defender, int damage) {

		if (Dungeon.hero.belongings.weapon() instanceof Sword){
			if (hero.HP == 2 || hero.HP == 3 || hero.HP == 5 || hero.HP == 7 || hero.HP == 11 || hero.HP == 13 || hero.HP == 17 || hero.HP == 19 || hero.HP == 23 || hero.HP == 29 || hero.HP == 31 || hero.HP == 37 || hero.HP == 41 || hero.HP == 43 || hero.HP == 47 || hero.HP == 53 || hero.HP == 59 || hero.HP == 61 || hero.HP == 67 || hero.HP == 71 || hero.HP == 73 || hero.HP == 79 || hero.HP == 83 || hero.HP == 89 || hero.HP == 97 || hero.HP == 101 || hero.HP == 103 || hero.HP == 107 || hero.HP == 109 || hero.HP == 113 || hero.HP == 127 || hero.HP == 131 || hero.HP == 137 || hero.HP == 139 || hero.HP == 149 || hero.HP == 151 || hero.HP == 157 || hero.HP == 163 || hero.HP == 167 || hero.HP == 173 || hero.HP == 179 || hero.HP == 181 || hero.HP == 191 || hero.HP == 193 || hero.HP == 197 || hero.HP == 199 || hero.HP == 211 || hero.HP == 223 || hero.HP == 227 || hero.HP == 229 || hero.HP == 233 || hero.HP == 239 || hero.HP == 241 || hero.HP == 251 || hero.HP == 257 || hero.HP == 263 || hero.HP == 269 || hero.HP == 271 || hero.HP == 277 || hero.HP == 281 || hero.HP == 283 || hero.HP == 293){
				damage *= 1.9f;
			}
		}

		if (hero.belongings.getItem(UnstableSpellbook.class) != null) {
			if (hero.belongings.getItem(UnstableSpellbook.class).isEquipped(hero) && (Random.Int(10) == 0)) {
				{
					Buff.affect(attacker, ArtifactRecharge.class).prolong( 3 ).ignoreHornOfPlenty = false;
				}
			}
		}

		return super.proc(attacker, defender, damage);
	}

	@Override
	public String desc() {
		String info = Messages.get(this, "desc");
		if (Dungeon.hero.belongings.getItem(UnstableSpellbook.class) != null) {
			if (Dungeon.hero.belongings.getItem(UnstableSpellbook.class).isEquipped(Dungeon.hero))
				info += "\n\n" + Messages.get( Sword.class, "setbouns");}

		return info;
	}

	@Override
	public float abilityChargeUse( Hero hero ) {
		if (hero.buff(Sword.CleaveTracker.class) != null){
			return 0;
		} else {
			return super.abilityChargeUse( hero );
		}
	}

	@Override
	public String targetingPrompt() {
		return Messages.get(this, "prompt");
	}

	@Override
	protected void duelistAbility(Hero hero, Integer target) {
		Sword.cleaveAbility(hero, target, 1.27f, this);
	}

	public static void cleaveAbility(Hero hero, Integer target, float dmgMulti, MeleeWeapon wep){
		if (target == null) {
			return;
		}

		Char enemy = Actor.findChar(target);
		if (enemy == null || enemy == hero || hero.isCharmedBy(enemy) || !Dungeon.level.heroFOV[target]) {
			GLog.w(Messages.get(wep, "ability_no_target"));
			return;
		}

		hero.belongings.abilityWeapon = wep;
		if (!hero.canAttack(enemy)){
			GLog.w(Messages.get(wep, "ability_bad_position"));
			hero.belongings.abilityWeapon = null;
			return;
		}
		hero.belongings.abilityWeapon = null;

		hero.sprite.attack(enemy.pos, new Callback() {
			@Override
			public void call() {
				wep.beforeAbilityUsed(hero);
				AttackIndicator.target(enemy);
				if (hero.attack(enemy, dmgMulti, 0, Char.INFINITE_ACCURACY)){
					Sample.INSTANCE.play(Assets.Sounds.HIT_STRONG);
					Sample.INSTANCE.play(Assets.Sounds.DORA);
				}

				Invisibility.dispel();
				hero.spendAndNext(hero.attackDelay());
				if (!enemy.isAlive()){
					wep.onAbilityKill(hero);
					Buff.prolong(hero, CleaveTracker.class, 5f);
				} else {
					if (hero.buff(CleaveTracker.class) != null) {
						hero.buff(CleaveTracker.class).detach();
					}
				}
				wep.afterAbilityUsed(hero);
			}
		});
	}

	public static class CleaveTracker extends FlavourBuff {

		{
			type = buffType.POSITIVE;
		}

		@Override
		public int icon() {
			return BuffIndicator.DUEL_CLEAVE;
		}

		@Override
		public float iconFadePercent() {
			return Math.max(0, (5 - visualcooldown()) / 5);
		}
	}

}
