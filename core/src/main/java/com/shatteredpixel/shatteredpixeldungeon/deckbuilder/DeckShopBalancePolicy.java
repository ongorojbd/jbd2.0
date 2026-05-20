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

import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.watabou.utils.Random;

public class DeckShopBalancePolicy {

	public static int removePrice(int removeCount) {
		return 75 + 25 * removeCount;
	}

	public static int cardPrice(DeckCardRarity rarity, boolean colorless) {
		int base;
		int variance;
		if (rarity == DeckCardRarity.RARE) {
			base = 150;
			variance = 15;
		} else if (rarity == DeckCardRarity.UNCOMMON) {
			base = 75;
			variance = 8;
		} else {
			base = 50;
			variance = 5;
		}
		int price = rollAround(base, variance);
		if (colorless) price = Math.round(price * 1.2f);
		return ascensionAdjusted(price);
	}

	public static int potionPrice(DeckCardRarity rarity) {
		if (rarity == DeckCardRarity.RARE) return ascensionAdjusted(rollAround(100, 5));
		if (rarity == DeckCardRarity.UNCOMMON) return ascensionAdjusted(rollAround(75, 4));
		return ascensionAdjusted(rollAround(50, 3));
	}

	public static int relicPrice(DeckRelicRarity rarity) {
		if (rarity == DeckRelicRarity.RARE) return ascensionAdjusted(rollAround(300, 15));
		if (rarity == DeckRelicRarity.UNCOMMON) return ascensionAdjusted(rollAround(250, 13));
		return ascensionAdjusted(rollAround(150, 8));
	}

	public static DeckCardRarity rollShopCardRarity(int cardRareOffset) {
		int rare = Math.max(0, Math.min(100, 9 + cardRareOffset));
		int uncommon = 37;
		int roll = Random.Int(100);
		if (roll < rare) return DeckCardRarity.RARE;
		if (roll < rare + uncommon) return DeckCardRarity.UNCOMMON;
		return DeckCardRarity.COMMON;
	}

	public static DeckRelicRarity rollRelicRarity() {
		return DeckRewardPolicy.rollRelicRarity();
	}

	public static DeckCardRarity rollPotionRarity() {
		int roll = Random.Int(100);
		if (roll < 65) return DeckCardRarity.COMMON;
		if (roll < 90) return DeckCardRarity.UNCOMMON;
		return DeckCardRarity.RARE;
	}

	public static DeckCardRarity potionRarity(DeckPotion potion) {
		if (potion == DeckPotion.STRENGTH) return DeckCardRarity.RARE;
		if (potion == DeckPotion.FIRE) return DeckCardRarity.UNCOMMON;
		return DeckCardRarity.COMMON;
	}

	private static int rollAround(int base, int variance) {
		return base - variance + Random.Int(variance * 2 + 1);
	}

	private static int ascensionAdjusted(int price) {
		return Challenges.activeChallenges() >= 16 ? Math.round(price * 1.1f) : price;
	}
}
