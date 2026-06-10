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

public class DeckShopTransaction {

	public static DeckShop.Offer[] offersForNode(DeckShopState shop, int depth, int path, int cardRareOffset, HeroClass heroClass) {
		if (shop.missingOffers()) {
			DeckShop.Offer[] offers = DeckShop.generateOffers(cardRareOffset, heroClass);
			shop.replaceOffers(depth, path, offers);
			if (DeckBuilderRun.hasRelic(DeckRelic.LOST_BEEF_SANDWICH)) {
				DeckBuilderRun.playerHP = Math.min(DeckBuilderRun.playerHT, DeckBuilderRun.playerHP + 15);
			}
		}
		DeckShop.Offer[] offers = shop.offers();
		for (int i = 0; i < offers.length; i++) {
			DeckShop.Offer o = offers[i];
			offers[i] = new DeckShop.Offer(o.type, o.id, discountedPrice(o.price), o.sale);
		}
		return offers;
	}

	public static boolean buyOffer(DeckShopState shop, int index) {
		if (index < 0 || index >= shop.types.length) return false;
		int basePrice = shop.prices[index];
		int actualPrice = discountedPrice(basePrice);
		if (shop.sold[index] || DeckBuilderRun.gold < actualPrice) return false;
		int type = shop.types[index];
		int id = shop.ids[index];
		if (type == DeckShop.CARD) {
			DeckCard[] cards = DeckCard.values();
			if (id < 0 || id >= cards.length) return false;
			DeckBuilderRun.addCard(cards[id]);
		} else if (type == DeckShop.POTION) {
			DeckPotion potion = DeckPotion.byId(id);
			if (!DeckRunInventory.addPotion(DeckBuilderRun.potions, potion, DeckBuilderRun.maxPotionSlots())) return false;
		} else if (type == DeckShop.RELIC) {
			DeckRelic relic = DeckRelic.byId(id);
			if (relic == null || DeckRunInventory.hasRelic(DeckBuilderRun.relics, relic)) return false;
			DeckRunInventory.addRelic(DeckBuilderRun.relics, relic);
		} else {
			return false;
		}
		DeckBuilderRun.gold -= actualPrice;
		if (!DeckBuilderRun.hasRelic(DeckRelic.SPW_FOUNDATION_LOST_ITEM)) {
			shop.sold[index] = true;
		}
		return true;
	}

	private static int discountedPrice(int basePrice) {
		int price = basePrice;
		if (DeckBuilderRun.hasRelic(DeckRelic.MEMBERSHIP_CARD)) price = Math.max(1, price / 2);
		if (DeckBuilderRun.hasRelic(DeckRelic.SPW_FOUNDATION_LOST_ITEM)) price = Math.max(1, price * 80 / 100);
		return price;
	}

	public static boolean buyCardRemoval(DeckShopState shop, int deckIndex) {
		int price = DeckShop.removePrice();
		if (shop.removeUsed || DeckBuilderRun.gold < price || !DeckBuilderRun.removeCardAt(deckIndex)) return false;
		DeckBuilderRun.gold -= price;
		shop.removeUsed = true;
		DeckBuilderRun.shopRemoveCount++;
		return true;
	}
}
