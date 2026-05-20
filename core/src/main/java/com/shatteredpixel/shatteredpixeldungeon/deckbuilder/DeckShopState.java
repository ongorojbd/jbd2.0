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

final class DeckShopState {

	int depth;
	int path;
	int[] types;
	int[] ids;
	int[] prices;
	boolean[] sold;
	boolean[] sales;
	boolean removeUsed;

	DeckShopState() {
		clear();
	}

	void clear() {
		depth = -1;
		path = -1;
		types = null;
		ids = null;
		prices = null;
		sold = null;
		sales = null;
		removeUsed = false;
	}

	boolean matches(int depth, int path) {
		return this.depth == depth && this.path == path;
	}

	boolean missingOffers() {
		return types == null || ids == null || prices == null || sold == null
				|| types.length != ids.length || types.length != prices.length || types.length != sold.length;
	}

	void replaceOffers(int depth, int path, DeckShop.Offer[] offers) {
		this.depth = depth;
		this.path = path;
		types = new int[offers.length];
		ids = new int[offers.length];
		prices = new int[offers.length];
		sold = new boolean[offers.length];
		sales = new boolean[offers.length];
		removeUsed = false;
		for (int i = 0; i < offers.length; i++) {
			types[i] = offers[i].type;
			ids[i] = offers[i].id;
			prices[i] = offers[i].price;
			sales[i] = offers[i].sale;
		}
	}

	DeckShop.Offer[] offers() {
		DeckShop.Offer[] result = new DeckShop.Offer[types.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = new DeckShop.Offer(types[i], ids[i], prices[i], sales != null && i < sales.length && sales[i]);
		}
		return result;
	}

	boolean sold(int index) {
		return sold != null && index >= 0 && index < sold.length && sold[index];
	}
}
