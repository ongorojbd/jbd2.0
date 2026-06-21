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

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.EnumSet;

public class DeckCardCategoryTable {

	private static final EnumMap<DeckCard, EnumSet<DeckCardCategory>> CATEGORIES = new EnumMap<>(DeckCard.class);

	static {
		register(DeckCard.VACCINE_SNAKE, DeckCardCategory.MELEE_WEAPON);
		register(DeckCard.RIPPLE_WALL, DeckCardCategory.MELEE_WEAPON);
		register(DeckCard.MASSACRE, DeckCardCategory.MELEE_WEAPON);
		register(DeckCard.MAGIC_MISSILE_WAND, DeckCardCategory.WAND);
		register(DeckCard.ENERGY_WAND, DeckCardCategory.WAND);
		register(DeckCard.DRAW_WAND, DeckCardCategory.WAND);
		register(DeckCard.BARRIER_WAND, DeckCardCategory.WAND);
		register(DeckCard.ENHANCEMENT_WAND, DeckCardCategory.WAND);
		register(DeckCard.MAGICIANS_WAND, DeckCardCategory.WAND);
		register(DeckCard.HORUS_WAND, DeckCardCategory.WAND);
		register(DeckCard.HEAVENS_WAND, DeckCardCategory.WAND);
		register(DeckCard.SOFT_WAND, DeckCardCategory.WAND);
		register(DeckCard.GOLD_EXPERIENCE_WAND, DeckCardCategory.WAND);
		register(DeckCard.TUSK2_WAND, DeckCardCategory.WAND);
	}

	public static boolean has(DeckCard card, DeckCardCategory category) {
		EnumSet<DeckCardCategory> categories = CATEGORIES.get(card);
		return categories != null && categories.contains(category);
	}

	public static DeckCardCategory[] categories(DeckCard card) {
		EnumSet<DeckCardCategory> categories = CATEGORIES.get(card);
		return categories == null ? new DeckCardCategory[0] : categories.toArray(new DeckCardCategory[0]);
	}

	public static DeckCard[] cards(DeckCardCategory category) {
		ArrayList<DeckCard> cards = new ArrayList<>();
		for (DeckCard card : DeckCard.values()) {
			if (has(card, category)) cards.add(card);
		}
		return cards.toArray(new DeckCard[0]);
	}

	public static DeckCard[] rewardCards(DeckCardCategory category) {
		ArrayList<DeckCard> cards = new ArrayList<>();
		for (DeckCard card : DeckCard.rewardPool()) {
			if (has(card, category)) cards.add(card);
		}
		return cards.toArray(new DeckCard[0]);
	}

	private static void register(DeckCard card, DeckCardCategory category) {
		EnumSet<DeckCardCategory> categories = CATEGORIES.get(card);
		if (categories == null) {
			categories = EnumSet.noneOf(DeckCardCategory.class);
			CATEGORIES.put(card, categories);
		}
		categories.add(category);
	}
}
