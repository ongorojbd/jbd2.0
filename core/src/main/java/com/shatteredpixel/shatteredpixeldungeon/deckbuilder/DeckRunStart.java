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

public class DeckRunStart {

	private static final int STARTING_RELIC_CHOICE_COUNT = 3;

	public static void initializeNewRun(HeroClass heroClass) {
		DeckBuilderRun.initialized = true;
		DeckBuilderRun.playerHT = 50;
		DeckBuilderRun.playerHP = DeckBuilderRun.playerHT;
		DeckBuilderRun.maxEnergy = DeckBuilderRun.STARTING_ENERGY;
		DeckBuilderRun.handSize = DeckBuilderRun.STARTING_HAND_SIZE;
		DeckBuilderRun.maxHandSize = DeckBuilderRun.DEFAULT_MAX_HAND_SIZE;
		DeckBuilderRun.deck.clear();
		DeckBuilderRun.relics.clear();
		DeckBuilderRun.potions.clear();
		DeckBuilderRun.gold = 99;
		DeckBuilderRun.cardRareOffset = -5;
		DeckBuilderRun.resetPotionDropChance();
		DeckBuilderRun.startingRelicChosen = false;
		DeckBuilderRun.startingRelicChoices = null;
		DeckBuilderRun.act1NormalFights = 0;
		DeckBuilderRun.lastNormalEncounter = -1;
		DeckBuilderRun.shopRemoveCount = 0;
		DeckBuilderRun.clearShop();
		DeckBuilderRun.clearTreasure();
		DeckBuilderRun.clearRest();

		DeckStartingProfile.addStartingDeck(DeckBuilderRun.deck, heroClass);
		addStartingPotions();
	}

	public static DeckRelic[] startingRelicChoices() {
		sanitizeStartingRelicChoices();
		DeckRelic[] choices = new DeckRelic[DeckBuilderRun.startingRelicChoices.length];
		for (int i = 0; i < choices.length; i++) {
			choices[i] = DeckRelic.byId(DeckBuilderRun.startingRelicChoices[i]);
		}
		return choices;
	}

	public static void sanitizeStartingRelicChoices() {
		if (DeckBuilderRun.startingRelicChosen) return;
		boolean valid = DeckBuilderRun.startingRelicChoices != null
				&& DeckBuilderRun.startingRelicChoices.length == STARTING_RELIC_CHOICE_COUNT;
		ArrayList<Integer> seen = new ArrayList<>();
		if (valid) {
			for (int id : DeckBuilderRun.startingRelicChoices) {
				DeckRelic relic = DeckRelic.byId(id);
				if (seen.contains(id) || relic.type != DeckRelicType.STARTER) {
					valid = false;
					break;
				}
				seen.add(id);
			}
		}
		if (valid) return;

		DeckBuilderRun.startingRelicChoices = new int[STARTING_RELIC_CHOICE_COUNT];
		seen.clear();
		for (int i = 0; i < DeckBuilderRun.startingRelicChoices.length; i++) {
			DeckRelic relic = DeckRelic.randomStarterAvailable(seen);
			int id = relic == null ? DeckRelic.STARTER_LUCKY_COIN.ordinal() : relic.ordinal();
			seen.add(id);
			DeckBuilderRun.startingRelicChoices[i] = id;
		}
	}

	public static void chooseStartingRelic(DeckRelic relic) {
		if (DeckBuilderRun.startingRelicChosen) return;
		DeckRunInventory.addRelic(DeckBuilderRun.relics, relic);
		DeckBuilderRun.startingRelicChosen = true;
		DeckBuilderRun.startingRelicChoices = null;
	}

	private static void addStartingPotions() {
		DeckRunInventory.addPotion(DeckBuilderRun.potions, DeckPotion.HASTE, DeckBuilderRun.MAX_POTION_SLOTS);
		DeckRunInventory.addPotion(DeckBuilderRun.potions, DeckPotion.FIRE, DeckBuilderRun.MAX_POTION_SLOTS);
		DeckRunInventory.addPotion(DeckBuilderRun.potions, DeckPotion.GAMBLERS_BREW, DeckBuilderRun.MAX_POTION_SLOTS);
	}
}
