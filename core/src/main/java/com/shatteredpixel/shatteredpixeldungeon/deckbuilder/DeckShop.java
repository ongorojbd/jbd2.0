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
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class DeckShop {

	public static final int CARD = 0;
	public static final int POTION = 1;
	public static final int RELIC = 2;
	public static final int REMOVE = 3;

	private static final int CARD_COUNT = 7;
	private static final int POTION_COUNT = 3;
	private static final int RELIC_COUNT = 3;

	public static Offer[] generateOffers() {
		ArrayList<Offer> offers = new ArrayList<>();
		int saleIndex = Random.Int(5);
		for (int i = 0; i < 5; i++) {
			DeckCardRarity rarity = rollShopCardRarity();
			DeckCard card = randomCard(rarity, Dungeon.hero == null ? null : Dungeon.hero.heroClass, true, false);
			if (card != null) offers.add(cardOffer(card, false, i == saleIndex));
		}
		for (int i = 0; i < 2; i++) {
			DeckCardRarity rarity = i == 0 ? DeckCardRarity.UNCOMMON : DeckCardRarity.RARE;
			DeckCard card = randomCard(rarity, Dungeon.hero == null ? null : Dungeon.hero.heroClass, false, true);
			if (card != null) offers.add(cardOffer(card, true, false));
		}
		for (int i = 0; i < RELIC_COUNT; i++) {
			boolean shopRelic = i == RELIC_COUNT - 1;
			DeckRelicRarity rarity = rollRelicRarity();
			DeckRelic relic = DeckRelic.randomAvailable(rarity, shopRelic);
			if (relic == null && shopRelic) relic = DeckRelic.randomAvailable(rollRelicRarity(), true);
			if (relic == null) relic = DeckRelic.randomAvailable(rarity);
			if (relic != null) offers.add(new Offer(RELIC, relic.ordinal(), relicPrice(relic.rarity), false));
		}
		for (int i = 0; i < POTION_COUNT; i++) {
			DeckPotion potion = randomPotion(rollPotionRarity());
			offers.add(new Offer(POTION, potion.ordinal(), potionPrice(potionRarity(potion)), false));
		}
		return offers.toArray(new Offer[0]);
	}

	public static int removePrice() {
		return 75 + 25 * DeckBuilderRun.shopRemoveCount;
	}

	private static Offer cardOffer(DeckCard card, boolean colorless, boolean sale) {
		int price = cardPrice(card.rarity, colorless);
		if (sale) price = Math.max(1, price / 2);
		return new Offer(CARD, card.ordinal(), price, sale);
	}

	private static int cardPrice(DeckCardRarity rarity, boolean colorless) {
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

	private static int potionPrice(DeckCardRarity rarity) {
		if (rarity == DeckCardRarity.RARE) return ascensionAdjusted(rollAround(100, 5));
		if (rarity == DeckCardRarity.UNCOMMON) return ascensionAdjusted(rollAround(75, 4));
		return ascensionAdjusted(rollAround(50, 3));
	}

	private static int relicPrice(DeckRelicRarity rarity) {
		if (rarity == DeckRelicRarity.RARE) return ascensionAdjusted(rollAround(300, 15));
		if (rarity == DeckRelicRarity.UNCOMMON) return ascensionAdjusted(rollAround(250, 13));
		return ascensionAdjusted(rollAround(150, 8));
	}

	private static int rollAround(int base, int variance) {
		return base - variance + Random.Int(variance * 2 + 1);
	}

	private static int ascensionAdjusted(int price) {
		return Challenges.activeChallenges() >= 16 ? Math.round(price * 1.1f) : price;
	}

	private static DeckCardRarity rollShopCardRarity() {
		int rare = Math.max(0, Math.min(100, 9 + DeckBuilderRun.cardRareOffset));
		int uncommon = 37;
		int roll = Random.Int(100);
		if (roll < rare) return DeckCardRarity.RARE;
		if (roll < rare + uncommon) return DeckCardRarity.UNCOMMON;
		return DeckCardRarity.COMMON;
	}

	private static DeckRelicRarity rollRelicRarity() {
		int roll = Random.Int(100);
		if (roll < 50) return DeckRelicRarity.COMMON;
		if (roll < 83) return DeckRelicRarity.UNCOMMON;
		return DeckRelicRarity.RARE;
	}

	private static DeckCardRarity rollPotionRarity() {
		int roll = Random.Int(100);
		if (roll < 65) return DeckCardRarity.COMMON;
		if (roll < 90) return DeckCardRarity.UNCOMMON;
		return DeckCardRarity.RARE;
	}

	private static DeckCardRarity potionRarity(DeckPotion potion) {
		if (potion == DeckPotion.STRENGTH) return DeckCardRarity.RARE;
		if (potion == DeckPotion.FIRE) return DeckCardRarity.UNCOMMON;
		return DeckCardRarity.COMMON;
	}

	private static DeckPotion randomPotion(DeckCardRarity rarity) {
		ArrayList<DeckPotion> pool = new ArrayList<>();
		for (DeckPotion potion : DeckPotion.values()) {
			if (potionRarity(potion) == rarity) pool.add(potion);
		}
		return pool.isEmpty() ? DeckPotion.values()[Random.Int(DeckPotion.values().length)] : pool.get(Random.Int(pool.size()));
	}

	private static DeckCard randomCard(DeckCardRarity rarity, HeroClass heroClass, boolean classOnly, boolean neutralOnly) {
		ArrayList<DeckCard> pool = new ArrayList<>();
		for (DeckCard card : DeckCard.rewardPool(heroClass, classOnly, neutralOnly)) {
			if (card.rarity == rarity) pool.add(card);
		}
		if (pool.isEmpty() && classOnly) {
			for (DeckCard card : DeckCard.rewardPool(heroClass, false, false)) {
				if (card.rarity == rarity) pool.add(card);
			}
		}
		if (pool.isEmpty()) {
			for (DeckCard card : DeckCard.rewardPool()) {
				if (card.rarity == rarity) pool.add(card);
			}
		}
		return pool.isEmpty() ? null : pool.get(Random.Int(pool.size()));
	}

	public static class Offer {
		public final int type;
		public final int id;
		public final int price;
		public final boolean sale;

		public Offer(int type, int id, int price, boolean sale) {
			this.type = type;
			this.id = id;
			this.price = price;
			this.sale = sale;
		}
	}
}
