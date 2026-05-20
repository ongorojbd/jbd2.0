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

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class DeckShop {

	public static final int CARD = 0;
	public static final int POTION = 1;
	public static final int RELIC = 2;
	public static final int REMOVE = 3;

	private static final int POTION_COUNT = 3;
	private static final int RELIC_COUNT = 3;

	public static Offer[] generateOffers() {
		return generateOffers(DeckBuilderRun.cardRareOffset, DeckBuilderRun.heroClass());
	}

	public static Offer[] generateOffers(int cardRareOffset, HeroClass heroClass) {
		ArrayList<Offer> offers = new ArrayList<>();
		int saleIndex = Random.Int(5);
		for (int i = 0; i < 5; i++) {
			DeckCardRarity rarity = DeckShopBalancePolicy.rollShopCardRarity(cardRareOffset);
			DeckCard card = randomCard(rarity, heroClass, true, false);
			if (card != null) offers.add(cardOffer(card, false, i == saleIndex));
		}
		for (int i = 0; i < 2; i++) {
			DeckCardRarity rarity = i == 0 ? DeckCardRarity.UNCOMMON : DeckCardRarity.RARE;
			DeckCard card = randomCard(rarity, heroClass, false, true);
			if (card != null) offers.add(cardOffer(card, true, false));
		}
		for (int i = 0; i < RELIC_COUNT; i++) {
			boolean shopRelic = i == RELIC_COUNT - 1;
			DeckRelicRarity rarity = DeckShopBalancePolicy.rollRelicRarity();
			DeckRelic relic = DeckRelic.randomAvailable(rarity, shopRelic);
			if (relic == null && shopRelic) relic = DeckRelic.randomAvailable(DeckShopBalancePolicy.rollRelicRarity(), true);
			if (relic == null) relic = DeckRelic.randomAvailable(rarity);
			if (relic != null) offers.add(new Offer(RELIC, relic.ordinal(), DeckShopBalancePolicy.relicPrice(relic.rarity), false));
		}
		for (int i = 0; i < POTION_COUNT; i++) {
			DeckPotion potion = randomPotion(DeckShopBalancePolicy.rollPotionRarity());
			offers.add(new Offer(POTION, potion.ordinal(), DeckShopBalancePolicy.potionPrice(DeckShopBalancePolicy.potionRarity(potion)), false));
		}
		return offers.toArray(new Offer[0]);
	}

	public static int removePrice() {
		return DeckShopBalancePolicy.removePrice(DeckBuilderRun.shopRemoveCount);
	}

	private static Offer cardOffer(DeckCard card, boolean colorless, boolean sale) {
		int price = DeckShopBalancePolicy.cardPrice(card.rarity, colorless);
		if (sale) price = Math.max(1, price / 2);
		return new Offer(CARD, card.ordinal(), price, sale);
	}

	private static DeckPotion randomPotion(DeckCardRarity rarity) {
		ArrayList<DeckPotion> pool = new ArrayList<>();
		for (DeckPotion potion : DeckPotion.values()) {
			if (DeckShopBalancePolicy.potionRarity(potion) == rarity) pool.add(potion);
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
