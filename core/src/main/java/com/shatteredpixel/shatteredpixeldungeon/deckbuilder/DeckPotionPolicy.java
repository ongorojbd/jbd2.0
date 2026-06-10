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

package com.shatteredpixel.shatteredpixeldungeon.deckbuilder;

import com.watabou.utils.Random;

import java.util.ArrayList;

public class DeckPotionPolicy {

	public static final int STARTING_DROP_CHANCE = 40;
	private static final int DROP_CHANCE_STEP = 10;

	public static PotionReward rollRewardPotion(int nodeType, int potionDropChance) {
		if (!hasPotionReward(nodeType)) {
			return new PotionReward(null, potionDropChance);
		}
		boolean dropped = Random.Int(100) < potionDropChance;
		int nextChance = nextDropChance(potionDropChance, dropped);
		return new PotionReward(dropped ? randomPotion(rollRarity()) : null, nextChance);
	}

	public static boolean hasPotionReward(int nodeType) {
		return nodeType == DeckBuilderMap.COMBAT
				|| nodeType == DeckBuilderMap.ELITE
				|| nodeType == DeckBuilderMap.BOSS
				|| nodeType == DeckBuilderMap.EVENT;
	}

	public static DeckPotionRarity rollRarity() {
		int roll = Random.Int(100);
		if (roll < 65) return DeckPotionRarity.COMMON;
		if (roll < 90) return DeckPotionRarity.UNCOMMON;
		return DeckPotionRarity.RARE;
	}

	public static DeckPotion randomPotion(DeckPotionRarity rarity) {
		ArrayList<DeckPotion> pool = new ArrayList<>();
		for (DeckPotion potion : DeckPotion.values()) {
			if (potion.rarity == rarity) pool.add(potion);
		}
		return pool.isEmpty() ? DeckPotion.values()[Random.Int(DeckPotion.values().length)] : pool.get(Random.Int(pool.size()));
	}

	public static int shopPrice(DeckPotionRarity rarity) {
		if (rarity == DeckPotionRarity.RARE) return Random.IntRange(95, 105);
		if (rarity == DeckPotionRarity.UNCOMMON) return Random.IntRange(72, 78);
		return Random.IntRange(48, 52);
	}

	public static int nextDropChance(int currentChance, boolean dropped) {
		int next = currentChance + (dropped ? -DROP_CHANCE_STEP : DROP_CHANCE_STEP);
		return Math.max(0, Math.min(100, next));
	}

	public static class PotionReward {
		public final DeckPotion potion;
		public final int nextPotionDropChance;

		public PotionReward(DeckPotion potion, int nextPotionDropChance) {
			this.potion = potion;
			this.nextPotionDropChance = nextPotionDropChance;
		}
	}
}
