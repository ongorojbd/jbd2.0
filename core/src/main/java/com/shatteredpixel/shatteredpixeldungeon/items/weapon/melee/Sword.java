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
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ArtifactRecharge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Diego;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClothArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.DuelistArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.RogueArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.WarriorArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.UnstableSpellbook;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfArcana;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HeroSprite;
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
				hero.sprite.showStatus(HeroSprite.POSITIVE, Messages.get(Sword.class, "1"));
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
	protected int baseChargeUse(Hero hero, Char target){
		if (hero.buff(Sword.CleaveTracker.class) != null){
			return 0;
		} else {
			return 1;
		}
	}

	@Override
	public String targetingPrompt() {
		return Messages.get(this, "prompt");
	}

	@Override
	protected void duelistAbility(Hero hero, Integer target) {
		//+(4+lvl) damage, roughly +35% base dmg, +40% scaling
		int dmgBoost = augment.damageFactor(4 + buffedLvl());
		Sword.cleaveAbility(hero, target, 1, dmgBoost, this);
	}

	@Override
	public String abilityInfo() {
		int dmgBoost = levelKnown ? 4 + buffedLvl() : 4;
		if (levelKnown){
			return Messages.get(this, "ability_desc", augment.damageFactor(min()+dmgBoost), augment.damageFactor(max()+dmgBoost));
		} else {
			return Messages.get(this, "typical_ability_desc", min(0)+dmgBoost, max(0)+dmgBoost);
		}
	}

	public static void jclass(){

		if (SPDSettings.getSkin() == 1 && hero.belongings.armor() instanceof ClothArmor || SPDSettings.getSkin() == 1 && hero.belongings.armor() instanceof WarriorArmor) {
			Sample.INSTANCE.play(Assets.Sounds.EVOKE);
		} else {
			Sample.INSTANCE.play(Assets.Sounds.OVERDRIVE);
		}
	}

	public static void oclass(){

		if (SPDSettings.getSkin3() == 1 && hero.belongings.armor() instanceof ClothArmor || SPDSettings.getSkin3() == 1 && hero.belongings.armor() instanceof RogueArmor) {
			Sample.INSTANCE.play(Assets.Sounds.HAHAH);
		} else {
			Sample.INSTANCE.play(Assets.Sounds.ORA);
		}
	}

	public static void hclass() {
		if (hero.heroClass == HeroClass.WARRIOR) {
			if (SPDSettings.getSkin() == 1 && hero.belongings.armor() instanceof ClothArmor || SPDSettings.getSkin() == 1 && hero.belongings.armor() instanceof WarriorArmor) {
				Sample.INSTANCE.play(Assets.Sounds.EVOKE);
			} else {
				Sample.INSTANCE.play(Assets.Sounds.JONATHAN2);
			}
		}
	}

	public static void doraclass(){

			if (SPDSettings.getSkin4() == 1 && hero.belongings.armor() instanceof ClothArmor || SPDSettings.getSkin4() == 1 && hero.belongings.armor() instanceof DuelistArmor) {
				Sample.INSTANCE.play(Assets.Sounds.ORA2);
			} else {
				switch (Random.Int( 6 )) {
					case 0:
						Sample.INSTANCE.play( Assets.Sounds.JOSUKE1);
						break;
					case 1:
						Sample.INSTANCE.play( Assets.Sounds.JOSUKE2);
						break;
					case 2:
						Sample.INSTANCE.play( Assets.Sounds.JOSUKE3);
						break;
					case 3:
						Sample.INSTANCE.play( Assets.Sounds.JOSUKE4);
						break;
					case 4:
						Sample.INSTANCE.play( Assets.Sounds.JOSUKE5);
						break;
					case 5:
						Sample.INSTANCE.play( Assets.Sounds.GUITAR);
						break;
				}
			}
	}

	public static void gclass(){

		if (SPDSettings.getSkin4() == 1 && hero.belongings.armor() instanceof ClothArmor || SPDSettings.getSkin4() == 1 && hero.belongings.armor() instanceof DuelistArmor) {
			Sample.INSTANCE.play(Assets.Sounds.ORA3);
		} else {
			Sample.INSTANCE.play(Assets.Sounds.GUITAR);
		}
	}


	public static void cleaveAbility(Hero hero, Integer target, float dmgMulti, int dmgBoost, MeleeWeapon wep){
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
			GLog.w(Messages.get(wep, "ability_target_range"));
			hero.belongings.abilityWeapon = null;
			return;
		}
		hero.belongings.abilityWeapon = null;

		hero.sprite.attack(enemy.pos, new Callback() {
			@Override
			public void call() {
				wep.beforeAbilityUsed(hero, enemy);
				AttackIndicator.target(enemy);
				if (hero.attack(enemy, dmgMulti, dmgBoost, Char.INFINITE_ACCURACY)){
					Sample.INSTANCE.play(Assets.Sounds.HIT_STRONG);
					Sword.doraclass();
				}

				Invisibility.dispel();

				if (!enemy.isAlive()){
					hero.next();
					wep.onAbilityKill(hero, enemy);
					if (hero.buff(CleaveTracker.class) != null) {
						hero.buff(CleaveTracker.class).detach();
					} else {
						Buff.prolong(hero, CleaveTracker.class, 4f); //1 less as attack was instant
					}
				} else {
					hero.spendAndNext(hero.attackDelay());
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
