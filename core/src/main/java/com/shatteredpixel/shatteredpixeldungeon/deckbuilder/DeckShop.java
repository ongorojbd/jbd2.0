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
	private static final int REWARD_RELIC_COUNT = 2;
	private static final int SHOP_RELIC_COUNT = 1;

	public static Offer[] generateOffers() {
		return generateOffers(DeckBuilderRun.cardRareOffset, DeckBuilderRun.heroClass());
	}

	public static Offer[] generateOffers(int cardRareOffset, HeroClass heroClass) {
		ArrayList<Offer> offers = new ArrayList<>();
		addClassCardOffers(offers, heroClass);
		addColorlessCardOffers(offers, heroClass);
		ArrayList<Integer> relicOfferIds = new ArrayList<>();
		for (int i = 0; i < REWARD_RELIC_COUNT; i++) {
			DeckRelicRarity rarity = DeckShopBalancePolicy.rollRelicRarity();
			DeckRelic relic = DeckRelic.randomAvailable(rarity, false, relicOfferIds);
			if (relic == null) relic = DeckRelic.randomAvailable(DeckShopBalancePolicy.rollRelicRarity(), false, relicOfferIds);
			if (relic != null) {
				relicOfferIds.add(relic.ordinal());
				offers.add(new Offer(RELIC, relic.ordinal(), DeckShopBalancePolicy.relicPrice(relic.rarity), false));
			}
		}
		for (int i = 0; i < SHOP_RELIC_COUNT; i++) {
			DeckRelic relic = DeckRelic.randomShopAvailable(relicOfferIds);
			if (relic != null) {
				relicOfferIds.add(relic.ordinal());
				offers.add(new Offer(RELIC, relic.ordinal(), DeckShopBalancePolicy.shopRelicPrice(), false));
			}
		}
		for (int i = 0; i < POTION_COUNT; i++) {
			DeckPotion potion = DeckPotionPolicy.randomPotion(DeckShopBalancePolicy.rollPotionRarity());
			offers.add(new Offer(POTION, potion.ordinal(), DeckShopBalancePolicy.potionPrice(potion.rarity), false));
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

	private static void addClassCardOffers(ArrayList<Offer> offers, HeroClass heroClass) {
		int saleIndex = Random.Int(5);
		DeckCardRarity extraPowerRarity = Random.Int(2) == 0 ? DeckCardRarity.COMMON : DeckCardRarity.UNCOMMON;
		DeckCard[] cards = new DeckCard[] {
				randomClassCard(heroClass, DeckCardType.ATTACK, DeckCardRarity.COMMON),
				randomClassCard(heroClass, DeckCardType.ATTACK, DeckCardRarity.RARE),
				randomClassCard(heroClass, DeckCardType.SKILL, DeckCardRarity.COMMON),
				randomClassCard(heroClass, DeckCardType.SKILL, DeckCardRarity.UNCOMMON),
				randomClassCard(heroClass, DeckCardType.POWER, extraPowerRarity)
		};
		for (int i = 0; i < cards.length; i++) {
			offers.add(cardOffer(cards[i], false, i == saleIndex));
		}
	}

	private static void addColorlessCardOffers(ArrayList<Offer> offers, HeroClass heroClass) {
		offers.add(cardOffer(randomColorlessCard(heroClass, DeckCardRarity.UNCOMMON), true, false));
		offers.add(cardOffer(randomColorlessCard(heroClass, DeckCardRarity.RARE), true, false));
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

	private static DeckCard randomClassCard(HeroClass heroClass, DeckCardType type, DeckCardRarity rarity) {
		DeckCard card = randomCard(type, rarity, heroClass, true, false);
		if (card != null) return card;
		card = randomCard(type, rarity, heroClass, false, false);
		if (card != null) return card;
		card = randomCard(null, rarity, heroClass, false, false);
		return card == null ? DeckCard.rewardFallback(heroClass) : card;
	}

	private static DeckCard randomColorlessCard(HeroClass heroClass, DeckCardRarity rarity) {
		DeckCard card = randomCard(null, rarity, heroClass, false, true);
		if (card != null) return card;
		card = randomCard(null, rarity, heroClass, false, false);
		return card == null ? DeckCard.rewardFallback(heroClass) : card;
	}

	private static DeckCard randomCard(DeckCardType type, DeckCardRarity rarity, HeroClass heroClass, boolean classOnly, boolean neutralOnly) {
		ArrayList<DeckCard> pool = new ArrayList<>();
		for (DeckCard card : DeckCard.rewardPool(heroClass, classOnly, neutralOnly)) {
			if (card.rarity == rarity && (type == null || card.type == type)) pool.add(card);
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
