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
	private static final String RELICS = "deckbuilder_relics";
	private static final String GOLD = "deckbuilder_gold";
	private static final String POTIONS = "deckbuilder_potions";
	private static final String CARD_RARE_OFFSET = "deckbuilder_card_rare_offset";
	private static final String POTION_DROP_CHANCE = "deckbuilder_potion_drop_chance";
	private static final String STARTING_RELIC_CHOSEN = "deckbuilder_starting_relic_chosen";
	private static final String STARTING_RELIC_CHOICES = "deckbuilder_starting_relic_choices";
	private static final String ACT1_NORMAL_FIGHTS = "deckbuilder_act1_normal_fights";
	private static final String LAST_NORMAL_ENCOUNTER = "deckbuilder_last_normal_encounter";

	public static final int STARTING_ENERGY = 3;
	public static final int STARTING_HAND_SIZE = 5;
	public static final int DEFAULT_MAX_HAND_SIZE = 10;
	public static final int MAX_ENERGY_CAP = 10;
	public static final int MAX_POTION_SLOTS = 3;

	public static boolean initialized;
	public static int playerHP;
	public static int playerHT;
	public static int maxEnergy;
	public static int handSize;
	public static int maxHandSize;
	public static ArrayList<Integer> deck = new ArrayList<>();
	public static ArrayList<Integer> relics = new ArrayList<>();
	public static ArrayList<Integer> potions = new ArrayList<>();
	public static int gold;
	public static int cardRareOffset;
	public static int potionDropChance;
	public static boolean startingRelicChosen;
	public static int[] startingRelicChoices;
	public static int act1NormalFights;
	public static int lastNormalEncounter;
	public static DeckBuilderCombat currentCombat;

	public static void reset() {
		initialized = false;
		playerHP = 0;
		playerHT = 0;
		maxEnergy = 0;
		handSize = 0;
		maxHandSize = 0;
		deck.clear();
		relics.clear();
		potions.clear();
		gold = 0;
		cardRareOffset = -5;
		potionDropChance = 40;
		startingRelicChosen = false;
		startingRelicChoices = null;
		act1NormalFights = 0;
		lastNormalEncounter = -1;
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
		relics.clear();
		potions.clear();
		gold = 99;
		cardRareOffset = -5;
		potionDropChance = 40;
		startingRelicChosen = false;
		startingRelicChoices = null;
		act1NormalFights = 0;
		lastNormalEncounter = -1;
		addCopies(DeckCard.STRIKE, 5);
		addCopies(DeckCard.GUARD, 4);
		addCopies(DeckCard.BASH, 1);
		addPotion(DeckPotion.HASTE);
		addPotion(DeckPotion.FIRE);
		addPotion(DeckPotion.STRENGTH);
	}

	public static DeckBuilderCombat newCombat(int nodeType) {
		initIfNeeded();
		return new DeckBuilderCombat(nodeType, Math.max(1, Dungeon.depth), deck);
	}

	public static DeckEnemy[] rollEncounter(int nodeType, int depth) {
		initIfNeeded();
		if (nodeType == DeckBuilderMap.COMBAT && act1NormalFights < 3) {
			int encounter = DeckEnemy.rollOpeningEncounter(lastNormalEncounter);
			lastNormalEncounter = encounter;
			act1NormalFights++;
			return DeckEnemy.openingEncounter(encounter);
		}
		return DeckEnemy.encounterForNode(nodeType, depth);
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
		DeckCard[] choices = new DeckCard[3];
		for (int i = 0; i < choices.length; i++) {
			choices[i] = randomCard(DeckCardRarity.COMMON);
		}
		return choices;
	}

	public static DeckCard[] rewardChoicesForNode(int nodeType) {
		initIfNeeded();
		DeckCard[] choices = new DeckCard[3];
		boolean rareSeen = false;
		int commonSeen = 0;
		for (int i = 0; i < choices.length; i++) {
			DeckCardRarity rarity = rollCardRarity(nodeType);
			DeckCard card = randomCard(rarity);
			int guard = 0;
			while (duplicate(choices, i, card) && guard++ < 20) {
				card = randomCard(rarity);
			}
			choices[i] = card == null ? DeckCard.STRIKE : card;
			if (choices[i].rarity == DeckCardRarity.RARE) rareSeen = true;
			if (choices[i].rarity == DeckCardRarity.COMMON) commonSeen++;
		}
		cardRareOffset = rareSeen ? -5 : cardRareOffset + commonSeen;
		return choices;
	}

	private static DeckCardRarity rollCardRarity(int nodeType) {
		int baseRare = nodeType == DeckBuilderMap.ELITE ? 10 : 3;
		int uncommon = nodeType == DeckBuilderMap.ELITE ? 40 : 37;
		int rare = baseRare + cardRareOffset;
		int roll = Random.Int(100);
		if (roll < rare) return DeckCardRarity.RARE;
		if (roll < rare + uncommon) return DeckCardRarity.UNCOMMON;
		return DeckCardRarity.COMMON;
	}

	private static boolean duplicate(DeckCard[] cards, int end, DeckCard card) {
		for (int i = 0; i < end; i++) {
			if (cards[i] == card) return true;
		}
		return false;
	}

	public static DeckCard randomCard(DeckCardRarity rarity) {
		DeckCard[] pool = DeckCard.rewardPool();
		ArrayList<DeckCard> rarityPool = new ArrayList<>();
		for (DeckCard card : pool) {
			if (card.rarity == rarity) rarityPool.add(card);
		}
		if (rarityPool.isEmpty()) return pool.length == 0 ? DeckCard.STRIKE : pool[Random.Int(pool.length)];
		return rarityPool.get(Random.Int(rarityPool.size()));
	}

	public static int rollRewardGold(int nodeType) {
		if (nodeType == DeckBuilderMap.BOSS) return 95 + Random.Int(11);
		if (nodeType == DeckBuilderMap.ELITE) return 25 + Random.Int(11);
		return 10 + Random.Int(11);
	}

	public static DeckRelic[] rollRewardRelics(int nodeType) {
		if (nodeType != DeckBuilderMap.ELITE) return new DeckRelic[0];
		int count = hasRelic(DeckRelic.BLACK_STAR) ? 2 : 1;
		ArrayList<DeckRelic> result = new ArrayList<>();
		for (int i = 0; i < count; i++) {
			DeckRelic relic = DeckRelic.randomAvailable(rollRelicRarity());
			if (relic != null && !result.contains(relic)) result.add(relic);
		}
		return result.toArray(new DeckRelic[0]);
	}

	private static DeckRelicRarity rollRelicRarity() {
		int roll = Random.Int(100);
		if (roll < 50) return DeckRelicRarity.COMMON;
		if (roll < 83) return DeckRelicRarity.UNCOMMON;
		return DeckRelicRarity.RARE;
	}

	public static DeckPotion rollRewardPotion(int nodeType) {
		int chance;
		boolean variable = nodeType == DeckBuilderMap.COMBAT;
		if (nodeType == DeckBuilderMap.ELITE) {
			chance = 40;
		} else if (nodeType == DeckBuilderMap.COMBAT) {
			chance = potionDropChance;
		} else {
			return null;
		}
		boolean dropped = Random.Int(100) < chance;
		if (variable) potionDropChance = Math.max(0, Math.min(100, potionDropChance + (dropped ? -10 : 10)));
		if (!dropped) return null;
		DeckPotion[] values = DeckPotion.values();
		return values[Random.Int(values.length)];
	}

	public static void addCard(DeckCard card) {
		initIfNeeded();
		deck.add(card.code());
	}

	public static void addRelic(DeckRelic relic) {
		initIfNeeded();
		if (relic == null || hasRelic(relic)) return;
		relics.add(relic.ordinal());
		relic.onAcquire();
	}

	public static boolean hasRelic(DeckRelic relic) {
		return relic != null && relics.contains(relic.ordinal());
	}

	public static DeckPotion potionAt(int slot) {
		initIfNeeded();
		if (slot < 0 || slot >= potions.size()) return null;
		return DeckPotion.byId(potions.get(slot));
	}

	public static boolean addPotion(DeckPotion potion) {
		initIfNeeded();
		if (potion == null || potions.size() >= MAX_POTION_SLOTS) return false;
		potions.add(potion.ordinal());
		return true;
	}

	public static void removePotion(int slot) {
		initIfNeeded();
		if (slot >= 0 && slot < potions.size()) {
			potions.remove(slot);
		}
	}

	public static DeckRelic[] startingRelicChoices() {
		initIfNeeded();
		sanitizeStartingRelicChoices();
		DeckRelic[] choices = new DeckRelic[startingRelicChoices.length];
		for (int i = 0; i < choices.length; i++) {
			choices[i] = DeckRelic.byId(startingRelicChoices[i]);
		}
		return choices;
	}

	private static void sanitizeStartingRelicChoices() {
		if (startingRelicChosen) return;
		DeckRelic[] all = DeckRelic.values();
		int count = Math.min(3, all.length);
		boolean valid = startingRelicChoices != null && startingRelicChoices.length == count;
		ArrayList<Integer> seen = new ArrayList<>();
		if (valid) {
			for (int id : startingRelicChoices) {
				if (id < 0 || id >= all.length || seen.contains(id)) {
					valid = false;
					break;
				}
				seen.add(id);
			}
		}
		if (valid) return;

		startingRelicChoices = new int[count];
		seen.clear();
		for (int i = 0; i < startingRelicChoices.length; i++) {
			int id;
			do {
				id = Random.Int(all.length);
			} while (seen.contains(id));
			seen.add(id);
			startingRelicChoices[i] = id;
		}
	}

	public static void chooseStartingRelic(DeckRelic relic) {
		if (startingRelicChosen) return;
		addRelic(relic);
		startingRelicChosen = true;
		startingRelicChoices = null;
	}

	public static String relicListText() {
		initIfNeeded();
		if (relics.isEmpty()) return "획득한 유물이 없습니다.";
		String text = "";
		for (int id : relics) {
			DeckRelic relic = DeckRelic.byId(id);
			text += (text.length() > 0 ? "\n\n" : "") + relic.title + "\n" + relic.description;
		}
		return text;
	}

	public static void storeInBundle(Bundle bundle) {
		bundle.put(INITIALIZED, initialized);
		bundle.put(PLAYER_HP, playerHP);
		bundle.put(PLAYER_HT, playerHT);
		bundle.put(DECK, toArray(deck));
		bundle.put(MAX_ENERGY, maxEnergy);
		bundle.put(HAND_SIZE, handSize);
		bundle.put(MAX_HAND_SIZE, maxHandSize);
		bundle.put(RELICS, toArray(relics));
		bundle.put(GOLD, gold);
		bundle.put(POTIONS, toArray(potions));
		bundle.put(CARD_RARE_OFFSET, cardRareOffset);
		bundle.put(POTION_DROP_CHANCE, potionDropChance);
		bundle.put(STARTING_RELIC_CHOSEN, startingRelicChosen);
		if (startingRelicChoices != null) bundle.put(STARTING_RELIC_CHOICES, startingRelicChoices);
		bundle.put(ACT1_NORMAL_FIGHTS, act1NormalFights);
		bundle.put(LAST_NORMAL_ENCOUNTER, lastNormalEncounter);
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
		gold = bundle.contains(GOLD) ? bundle.getInt(GOLD) : 0;
		cardRareOffset = bundle.contains(CARD_RARE_OFFSET) ? bundle.getInt(CARD_RARE_OFFSET) : -5;
		potionDropChance = bundle.contains(POTION_DROP_CHANCE) ? bundle.getInt(POTION_DROP_CHANCE) : 40;
		startingRelicChosen = bundle.getBoolean(STARTING_RELIC_CHOSEN);
		startingRelicChoices = bundle.contains(STARTING_RELIC_CHOICES) ? bundle.getIntArray(STARTING_RELIC_CHOICES) : null;
		act1NormalFights = bundle.contains(ACT1_NORMAL_FIGHTS) ? bundle.getInt(ACT1_NORMAL_FIGHTS) : 0;
		lastNormalEncounter = bundle.contains(LAST_NORMAL_ENCOUNTER) ? bundle.getInt(LAST_NORMAL_ENCOUNTER) : -1;
		sanitizeStartingRelicChoices();
		deck.clear();
		if (bundle.contains(DECK)) {
			for (int id : bundle.getIntArray(DECK)) {
				deck.add(id);
			}
		}
		relics.clear();
		if (bundle.contains(RELICS)) {
			for (int id : bundle.getIntArray(RELICS)) {
				relics.add(id);
			}
		}
		potions.clear();
		if (bundle.contains(POTIONS)) {
			for (int id : bundle.getIntArray(POTIONS)) {
				if (potions.size() < MAX_POTION_SLOTS && DeckPotion.byId(id) != null) {
					potions.add(id);
				}
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
