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
 */

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.QuickSlot;
import com.shatteredpixel.shatteredpixeldungeon.Rankings;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.KindOfWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.services.rankings.DailyRankingEntry;
import com.shatteredpixel.shatteredpixeldungeon.services.rankings.Ranking;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DailyChampionSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.ActionIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Game;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class DailyChampion extends ArmoredStatue {

	{
		properties.add(Property.MINIBOSS);
		spriteClass = DailyChampionSprite.class;
	}

	private String championName;
	private int sourceRank;
	private int sourceScore;
	private String sourceDate;
	private HeroClass heroClass = HeroClass.WARRIOR;
	private int armorTier = 0;
	private int brawlPower = 0;

	public DailyChampion() {
		super();
	}

	public static DailyChampion createFromTopRanking() {
		if (Dungeon.dailyReplay) {
			return null;
		}

		if (!Ranking.rankingsAvailable()) {
			return null;
		}

		ArrayList<DailyRankingEntry> rankings = Ranking.rankings();
		if (rankings.isEmpty()) {
			return null;
		}

		return createFromRankingEntry(rankings.get(0), 0);
	}

	public static DailyChampion createFromRankingEntry(DailyRankingEntry entry, int powerBonus) {
		if (Dungeon.dailyReplay || entry == null) {
			return null;
		}

		Rankings.Record record = Rankings.createRecordFromDailyEntry(entry);
		if (record == null || record.gameData == null || !record.gameData.contains(Rankings.HERO)) {
			return null;
		}

		try {
			Item[] quickslot = saveQuickslot();
			ActionIndicator.Action action = ActionIndicator.action;
			Hero rankedHero;
			boolean wasSuppressed = GLog.suppressed;
			try {
				GLog.suppressed = true;
				rankedHero = (Hero)record.gameData.get(Rankings.HERO);
			} finally {
				GLog.suppressed = wasSuppressed;
				restoreQuickslot(quickslot);
				restoreAction(action);
			}
			if (rankedHero == null || rankedHero.belongings == null) {
				return null;
			}

			KindOfWeapon rankedWeapon = rankedHero.belongings.weapon();
			Armor rankedArmor = rankedHero.belongings.armor();
			if (!(rankedWeapon instanceof MeleeWeapon)) {
				return null;
			}

			DailyChampion champion = new DailyChampion();
			champion.sourceRank = entry.rank;
			champion.sourceScore = entry.score;
			champion.sourceDate = entry.date;
			champion.championName = entry.playerName == null || entry.playerName.isEmpty()
					? Messages.get(DailyChampion.class, "fallback_name")
					: entry.playerName;
			champion.heroClass = parseHeroClass(entry.heroClass, rankedHero.heroClass);
			champion.armorTier = Math.max(0, Math.min(6, entry.armorTier));

			champion.weapon = (Weapon)((Weapon)rankedWeapon).duplicate();
			champion.weapon.cursed = false;
			champion.weapon.identify(false);

			if (rankedArmor != null && !rankedArmor.unique) {
				champion.armor = (Armor)rankedArmor.duplicate();
			}
			if (champion.armor == null) {
				champion.armor = com.shatteredpixel.shatteredpixeldungeon.items.Generator.randomArmor();
			}
			champion.armor.cursed = false;
			champion.armor.identify(false);

			champion.brawlPower = Math.max(0, powerBonus);
			int power = champion.brawlPower;
			champion.HT = 200 + 200 * power;
			champion.HP = champion.HT;
			champion.defenseSkill = 30 + 10 * power;
			champion.EXP = 0;
			champion.maxLvl = 0;

			return champion;
		} catch (Exception e) {
			Game.reportException(e);
			return null;
		}
	}

	private static HeroClass parseHeroClass(String heroClassName, HeroClass fallback) {
		if (heroClassName != null && !heroClassName.isEmpty()) {
			try {
				return HeroClass.valueOf(heroClassName);
			} catch (IllegalArgumentException ignored) {
				//fall through
			}
		}
		return fallback == null ? HeroClass.WARRIOR : fallback;
	}

	private static Item[] saveQuickslot() {
		Item[] saved = new Item[QuickSlot.SIZE];
		for (int i = 0; i < QuickSlot.SIZE; i++) {
			saved[i] = Dungeon.quickslot.getItem(i);
		}
		return saved;
	}

	private static void restoreQuickslot(Item[] saved) {
		Dungeon.quickslot.reset();
		for (int i = 0; i < saved.length; i++) {
			if (saved[i] != null) {
				Dungeon.quickslot.setSlot(i, saved[i]);
			}
		}
		Item.updateQuickslot();
	}

	private static void restoreAction(ActionIndicator.Action action) {
		if (action == null) {
			ActionIndicator.clearAction();
		} else {
			ActionIndicator.setAction(action);
		}
	}

	private static final String CHAMPION_NAME = "champion_name";
	private static final String SOURCE_RANK = "source_rank";
	private static final String SOURCE_SCORE = "source_score";
	private static final String SOURCE_DATE = "source_date";
	private static final String HERO_CLASS = "hero_class";
	private static final String ARMOR_TIER = "armor_tier";
	private static final String BRAWL_POWER = "brawl_power";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(CHAMPION_NAME, championName);
		bundle.put(SOURCE_RANK, sourceRank);
		bundle.put(SOURCE_SCORE, sourceScore);
		bundle.put(SOURCE_DATE, sourceDate);
		bundle.put(HERO_CLASS, heroClass);
		bundle.put(ARMOR_TIER, armorTier);
		bundle.put(BRAWL_POWER, brawlPower);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		championName = bundle.getString(CHAMPION_NAME);
		sourceRank = bundle.getInt(SOURCE_RANK);
		sourceScore = bundle.getInt(SOURCE_SCORE);
		sourceDate = bundle.getString(SOURCE_DATE);
		heroClass = bundle.contains(HERO_CLASS) ? bundle.getEnum(HERO_CLASS, HeroClass.class) : HeroClass.WARRIOR;
		armorTier = bundle.getInt(ARMOR_TIER);
		brawlPower = bundle.getInt(BRAWL_POWER);
	}

	public HeroClass heroClass() {
		return heroClass == null ? HeroClass.WARRIOR : heroClass;
	}

	public int armorTier() {
		return armorTier;
	}

	public int sourceScore() {
		return sourceScore;
	}

	@Override
	public CharSprite sprite() {
		return new DailyChampionSprite(heroClass(), armorTier());
	}

	@Override
	public String name() {
		if (championName != null && !championName.isEmpty()) {
			return championName;
		}
		return super.name();
	}

	@Override
	public String description() {
		String desc = Messages.get(this, "desc", name(), sourceDateText(), sourceScore);
		if (weapon != null && armor != null) {
			desc += "\n\n" + Messages.get(this, "desc_arm_wep", weapon.name(), armor.name());
		}
		return desc;
	}

	public String sourceDateText() {
		if (sourceDate != null && sourceDate.length() >= 10) {
			try {
				int month = Integer.parseInt(sourceDate.substring(5, 7));
				int day = Integer.parseInt(sourceDate.substring(8, 10));
				return Messages.get(this, "date", month, day);
			} catch (Exception ignored) {
				//fall through
			}
		}
		return Messages.get(this, "unknown_date");
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange(50 + brawlPower * 10, 60 + brawlPower * 10);
	}

	@Override
	public int attackSkill(Char target) {
		return 45 + brawlPower * 10;
	}

	@Override
	public int drRoll() {
		return super.drRoll() + Random.NormalIntRange(5 + brawlPower * 5, 20 + brawlPower * 5);
	}

	@Override
	public void die(Object cause) {
		if (Dungeon.level instanceof com.shatteredpixel.shatteredpixeldungeon.levels.ParallelBrawlLevel) {
			((com.shatteredpixel.shatteredpixeldungeon.levels.ParallelBrawlLevel)Dungeon.level).onChampionDefeated(pos);
		} else if (Dungeon.level != null && Dungeon.level.locked) {
			Dungeon.level.unseal();
		}
		destroy();
		sprite.die();
	}

	@Override
	public String info() {
		return description();
	}
}
