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
		}
		DeckShop.Offer[] offers = shop.offers();
		if (DeckBuilderRun.hasRelic(DeckRelic.MEMBERSHIP_CARD)) {
			for (int i = 0; i < offers.length; i++) {
				DeckShop.Offer o = offers[i];
				offers[i] = new DeckShop.Offer(o.type, o.id, Math.max(1, o.price / 2), o.sale);
			}
		}
		return offers;
	}

	public static boolean buyOffer(DeckShopState shop, int index) {
		int basePrice = shop.prices[index];
		int actualPrice = DeckBuilderRun.hasRelic(DeckRelic.MEMBERSHIP_CARD) ? Math.max(1, basePrice / 2) : basePrice;
		if (index < 0 || index >= shop.types.length || shop.sold[index] || DeckBuilderRun.gold < actualPrice) return false;
		int type = shop.types[index];
		int id = shop.ids[index];
		if (type == DeckShop.CARD) {
			DeckCard[] cards = DeckCard.values();
			if (id < 0 || id >= cards.length) return false;
			DeckRunInventory.addCard(DeckBuilderRun.deck, cards[id]);
		} else if (type == DeckShop.POTION) {
			DeckPotion potion = DeckPotion.byId(id);
			if (!DeckRunInventory.addPotion(DeckBuilderRun.potions, potion, DeckBuilderRun.MAX_POTION_SLOTS)) return false;
		} else if (type == DeckShop.RELIC) {
			DeckRelic relic = DeckRelic.byId(id);
			if (relic == null || DeckRunInventory.hasRelic(DeckBuilderRun.relics, relic)) return false;
			DeckRunInventory.addRelic(DeckBuilderRun.relics, relic);
		} else {
			return false;
		}
		DeckBuilderRun.gold -= actualPrice;
		shop.sold[index] = true;
		return true;
	}

	public static boolean buyCardRemoval(DeckShopState shop, int deckIndex) {
		int price = DeckShop.removePrice();
		if (shop.removeUsed || DeckBuilderRun.gold < price || !DeckRunInventory.removeCardAt(DeckBuilderRun.deck, deckIndex)) return false;
		DeckBuilderRun.gold -= price;
		shop.removeUsed = true;
		DeckBuilderRun.shopRemoveCount++;
		return true;
	}
}
