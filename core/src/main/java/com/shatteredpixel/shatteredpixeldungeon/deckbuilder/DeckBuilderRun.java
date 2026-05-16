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

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class DeckBuilderRun {

	private static final String INITIALIZED = "deckbuilder_initialized";
	private static final String PLAYER_HP = "deckbuilder_player_hp";
	private static final String PLAYER_HT = "deckbuilder_player_ht";
	private static final String DECK = "deckbuilder_deck";
	private static final String MAX_ENERGY = "deckbuilder_max_energy";
	private static final String HAND_SIZE = "deckbuilder_hand_size";
	private static final String MAX_HAND_SIZE = "deckbuilder_max_hand_size";
	private static final String CURRENT_COMBAT = "deckbuilder_current_combat";

	public static final int STARTING_ENERGY = 3;
	public static final int STARTING_HAND_SIZE = 5;
	public static final int DEFAULT_MAX_HAND_SIZE = 10;
	public static final int MAX_ENERGY_CAP = 10;

	public static boolean initialized;
	public static int playerHP;
	public static int playerHT;
	public static int maxEnergy;
	public static int handSize;
	public static int maxHandSize;
	public static ArrayList<Integer> deck = new ArrayList<>();
	public static DeckBuilderCombat currentCombat;

	public static void reset() {
		initialized = false;
		playerHP = 0;
		playerHT = 0;
		maxEnergy = 0;
		handSize = 0;
		maxHandSize = 0;
		deck.clear();
		currentCombat = null;
	}

	public static void initIfNeeded() {
		if (initialized) return;

		initialized = true;
		playerHT = 50;
		playerHP = playerHT;
		maxEnergy = STARTING_ENERGY;
		handSize = STARTING_HAND_SIZE;
		maxHandSize = DEFAULT_MAX_HAND_SIZE;
		deck.clear();
		addCopies(DeckCard.STRIKE, 5);
		addCopies(DeckCard.GUARD, 4);
		addCopies(DeckCard.BASH, 1);
	}

	public static DeckBuilderCombat newCombat(int nodeType) {
		initIfNeeded();
		return new DeckBuilderCombat(nodeType, Math.max(1, Dungeon.depth), deck);
	}

	public static DeckBuilderCombat combatForNode(int nodeType) {
		initIfNeeded();
		if (currentCombat == null
				|| currentCombat.nodeType != nodeType
				|| currentCombat.depth != Math.max(1, Dungeon.depth)
				|| currentCombat.won()
				|| currentCombat.playerDead()) {
			currentCombat = newCombat(nodeType);
		}
		return currentCombat;
	}

	public static void clearCombat() {
		currentCombat = null;
	}

	public static void applyExhaust(ArrayList<Integer> exhausted) {
		for (int code : exhausted) {
			deck.remove(Integer.valueOf(code));
		}
	}

	public static DeckCard[] rewardChoices() {
		DeckCard[] pool = DeckCard.rewardPool();
		DeckCard[] choices = new DeckCard[3];
		for (int i = 0; i < choices.length; i++) {
			DeckCard picked;
			boolean duplicate;
			do {
				picked = pool[Random.Int(pool.length)];
				duplicate = false;
				for (int j = 0; j < i; j++) {
					if (choices[j] == picked) duplicate = true;
				}
			} while (duplicate);
			choices[i] = picked;
		}
		return choices;
	}

	public static void addCard(DeckCard card) {
		initIfNeeded();
		deck.add(card.code());
	}

	public static void storeInBundle(Bundle bundle) {
		bundle.put(INITIALIZED, initialized);
		bundle.put(PLAYER_HP, playerHP);
		bundle.put(PLAYER_HT, playerHT);
		bundle.put(DECK, toArray(deck));
		bundle.put(MAX_ENERGY, maxEnergy);
		bundle.put(HAND_SIZE, handSize);
		bundle.put(MAX_HAND_SIZE, maxHandSize);
		if (currentCombat != null && !currentCombat.playerDead()) {
			Bundle combatBundle = new Bundle();
			currentCombat.storeInBundle(combatBundle);
			bundle.put(CURRENT_COMBAT, combatBundle);
		}
	}

	public static void restoreFromBundle(Bundle bundle) {
		initialized = bundle.getBoolean(INITIALIZED);
		playerHP = bundle.getInt(PLAYER_HP);
		playerHT = bundle.getInt(PLAYER_HT);
		maxEnergy = bundle.contains(MAX_ENERGY) ? bundle.getInt(MAX_ENERGY) : STARTING_ENERGY;
		handSize = bundle.contains(HAND_SIZE) ? bundle.getInt(HAND_SIZE) : STARTING_HAND_SIZE;
		maxHandSize = bundle.contains(MAX_HAND_SIZE) ? bundle.getInt(MAX_HAND_SIZE) : DEFAULT_MAX_HAND_SIZE;
		deck.clear();
		if (bundle.contains(DECK)) {
			for (int id : bundle.getIntArray(DECK)) {
				deck.add(id);
			}
		}
		currentCombat = bundle.contains(CURRENT_COMBAT)
				? DeckBuilderCombat.restoreFromBundle(bundle.getBundle(CURRENT_COMBAT))
				: null;
	}

	private static void addCopies(DeckCard card, int count) {
		for (int i = 0; i < count; i++) {
			deck.add(card.code());
		}
	}

	private static int[] toArray(ArrayList<Integer> list) {
		int[] result = new int[list.size()];
		for (int i = 0; i < list.size(); i++) {
			result[i] = list.get(i);
		}
		return result;
	}
}
