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

import java.util.ArrayList;
import java.util.EnumMap;

public class DeckStartingProfile {

	private static final EnumMap<HeroClass, DeckStartingProfile> PROFILES = new EnumMap<>(HeroClass.class);

	// 카드 테스트

	static {
		register(HeroClass.WARRIOR,
				card(DeckCard.STRIKE, 5),
				card(DeckCard.GUARD, 4),
				card(DeckCard.WEAKNESS_STAB, 1),
				card(DeckCard.BARRAGE, 1),
				card(DeckCard.FLOW_SLASH, 1),
				card(DeckCard.ACCEL_STAB, 1),
				card(DeckCard.HYPERVENTILATE, 1),
				card(DeckCard.BARRICADE, 1),
				card(DeckCard.BASH, 1));

		register(HeroClass.MAGE,
				card(DeckCard.STRIKE, 5),
				card(DeckCard.GUARD, 4),
				card(DeckCard.MAGE_STAFF, 1));

		register(HeroClass.HUNTRESS,
				card(DeckCard.STRIKE, 5),
				card(DeckCard.GUARD, 4),
				card(DeckCard.FOUNDATION_BOX, 1));

		register(HeroClass.JOHNNY,
				card(DeckCard.STRIKE, 5),
				card(DeckCard.GUARD, 4),
				card(DeckCard.TUSK_EQUIPMENT_DISC, 1));
	}

	private final CardEntry[] deck;

	private DeckStartingProfile(CardEntry[] deck) {
		this.deck = deck;
	}

	public static DeckStartingProfile forHero(HeroClass heroClass) {
		return heroClass == null ? null : PROFILES.get(heroClass);
	}

	public static void addStartingDeck(ArrayList<Integer> deck, HeroClass heroClass) {
		DeckStartingProfile profile = forHero(heroClass);
		if (profile == null) return;
		profile.addDeckTo(deck);
	}

	public static boolean isStartingCard(DeckCard card) {
		if (card == null) return false;
		for (DeckStartingProfile profile : PROFILES.values()) {
			if (profile.contains(card)) return true;
		}
		return false;
	}

	public static boolean isStartingCard(DeckCard card, HeroClass heroClass) {
		if (card == null) return false;
		if (heroClass != null) {
			DeckStartingProfile profile = forHero(heroClass);
			return profile != null && profile.contains(card);
		}
		return isStartingCard(card);
	}

	private void addDeckTo(ArrayList<Integer> deck) {
		for (CardEntry entry : this.deck) {
			DeckRunInventory.addCopies(deck, entry.card, entry.count);
		}
	}

	private boolean contains(DeckCard card) {
		for (CardEntry entry : deck) {
			if (entry.card == card) return true;
		}
		return false;
	}

	private static void register(HeroClass heroClass, CardEntry... deck) {
		PROFILES.put(heroClass, new DeckStartingProfile(deck));
	}

	private static CardEntry card(DeckCard card, int count) {
		return new CardEntry(card, count);
	}

	private static class CardEntry {

		private final DeckCard card;
		private final int count;

		private CardEntry(DeckCard card, int count) {
			this.card = card;
			this.count = count;
		}
	}
}
