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

public class DeckShopBalancePolicy {

	public static int removePrice(int removeCount) {
		return 75 + 25 * removeCount;
	}

	public static int cardPrice(DeckCardRarity rarity, boolean colorless) {
		if (colorless) return colorlessCardPrice(rarity);
		if (rarity == DeckCardRarity.RARE) return Random.IntRange(135, 165);
		if (rarity == DeckCardRarity.UNCOMMON) return Random.IntRange(68, 82);
		return Random.IntRange(45, 55);
	}

	public static int colorlessCardPrice(DeckCardRarity rarity) {
		if (rarity == DeckCardRarity.RARE) return Random.IntRange(162, 198);
		if (rarity == DeckCardRarity.UNCOMMON) return Random.IntRange(81, 99);
		return cardPrice(rarity, false);
	}

	public static int potionPrice(DeckCardRarity rarity) {
		if (rarity == DeckCardRarity.RARE) return potionPrice(DeckPotionRarity.RARE);
		if (rarity == DeckCardRarity.UNCOMMON) return potionPrice(DeckPotionRarity.UNCOMMON);
		return potionPrice(DeckPotionRarity.COMMON);
	}

	public static int potionPrice(DeckPotionRarity rarity) {
		return DeckPotionPolicy.shopPrice(rarity);
	}

	public static int relicPrice(DeckRelicRarity rarity) {
		if (rarity == DeckRelicRarity.RARE) return Random.IntRange(285, 315);
		if (rarity == DeckRelicRarity.UNCOMMON) return Random.IntRange(238, 262);
		return Random.IntRange(143, 157);
	}

	public static int shopRelicPrice() {
		return Random.IntRange(143, 157);
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

	public static DeckPotionRarity rollPotionRarity() {
		return DeckPotionPolicy.rollRarity();
	}

	public static DeckPotionRarity potionRarity(DeckPotion potion) {
		return potion.rarity;
	}

}
