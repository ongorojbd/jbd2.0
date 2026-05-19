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
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
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
	private static final String SHOP_REMOVE_COUNT = "deckbuilder_shop_remove_count";
	private static final String SHOP_DEPTH = "deckbuilder_shop_depth";
	private static final String SHOP_PATH = "deckbuilder_shop_path";
	private static final String SHOP_TYPES = "deckbuilder_shop_types";
	private static final String SHOP_IDS = "deckbuilder_shop_ids";
	private static final String SHOP_PRICES = "deckbuilder_shop_prices";
	private static final String SHOP_SOLD = "deckbuilder_shop_sold";
	private static final String SHOP_SALES = "deckbuilder_shop_sales";
	private static final String SHOP_REMOVE_USED = "deckbuilder_shop_remove_used";
	private static final String TREASURE_DEPTH = "deckbuilder_treasure_depth";
	private static final String TREASURE_PATH = "deckbuilder_treasure_path";
	private static final String TREASURE_CHEST = "deckbuilder_treasure_chest";
	private static final String TREASURE_RELIC = "deckbuilder_treasure_relic";
	private static final String TREASURE_CLAIMED = "deckbuilder_treasure_claimed";
	private static final String REST_DEPTH = "deckbuilder_rest_depth";
	private static final String REST_PATH = "deckbuilder_rest_path";
	private static final String REST_USED = "deckbuilder_rest_used";

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
	public static int shopRemoveCount;
	public static int shopDepth;
	public static int shopPath;
	public static int[] shopTypes;
	public static int[] shopIds;
	public static int[] shopPrices;
	public static boolean[] shopSold;
	public static boolean[] shopSales;
	public static boolean shopRemoveUsed;
	public static int treasureDepth;
	public static int treasurePath;
	public static int treasureChest;
	public static int treasureRelic;
	public static boolean treasureClaimed;
	public static int restDepth;
	public static int restPath;
	public static boolean restUsed;
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
		shopRemoveCount = 0;
		clearShop();
		clearTreasure();
		clearRest();
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
		shopRemoveCount = 0;
		clearShop();
		clearTreasure();
		clearRest();

		if (Dungeon.hero != null && Dungeon.hero.heroClass == HeroClass.WARRIOR) {
			addCopies(DeckCard.STRIKE, 5);
			addCopies(DeckCard.GUARD, 4);
			addCopies(DeckCard.BASH, 1);
			addCopies(DeckCard.VACCINE_SNAKE, 1);
			addCopies(DeckCard.STAFF, 1);
		}

		if (Dungeon.hero != null && Dungeon.hero.heroClass == HeroClass.HUNTRESS) {
			addCopies(DeckCard.STRIKE, 5);
			addCopies(DeckCard.GUARD, 4);
			addCopies(DeckCard.SCORPION_THROW, 1);
			addCopies(DeckCard.PHANTOM_BLADES, 1);
		}

		if (Dungeon.hero != null && Dungeon.hero.heroClass == HeroClass.JOHNNY) {
			addCopies(DeckCard.STRIKE, 5);
			addCopies(DeckCard.GUARD, 4);
			addCopies(DeckCard.TUSK_EQUIPMENT_DISC, 1);
		}

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
		if (nodeType == DeckBuilderMap.COMBAT) {
			int encounter = DeckEnemy.rollAct1Encounter(lastNormalEncounter);
			lastNormalEncounter = encounter;
			act1NormalFights++;
			return DeckEnemy.normalEncounter(encounter);
		}
		return DeckEnemy.encounterForNode(nodeType, depth, lastNormalEncounter);
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

	public static void clearShop() {
		shopDepth = -1;
		shopPath = -1;
		shopTypes = null;
		shopIds = null;
		shopPrices = null;
		shopSold = null;
		shopSales = null;
		shopRemoveUsed = false;
	}

	public static DeckRelic treasureRelicForCurrentNode() {
		initIfNeeded();
		int depth = Math.max(1, Dungeon.depth);
		int path = Statistics.deckBuilderMapPath;
		if (treasureDepth != depth || treasurePath != path || treasureRelic < 0) {
			treasureDepth = depth;
			treasurePath = path;
			treasureChest = rollTreasureChest();
			DeckRelic relic = DeckRelic.randomAvailable(rollTreasureRarity(treasureChest));
			treasureRelic = relic == null ? -1 : relic.ordinal();
			treasureClaimed = false;
		}
		return treasureRelic < 0 ? null : DeckRelic.byId(treasureRelic);
	}

	public static int treasureChestForCurrentNode() {
		treasureRelicForCurrentNode();
		return treasureChest;
	}

	public static boolean claimTreasureRelic() {
		DeckRelic relic = treasureRelicForCurrentNode();
		if (treasureClaimed || relic == null) return false;
		addRelic(relic);
		treasureClaimed = true;
		return true;
	}

	public static void clearTreasure() {
		treasureDepth = -1;
		treasurePath = -1;
		treasureChest = 0;
		treasureRelic = -1;
		treasureClaimed = false;
	}

	public static void initRestForCurrentNode() {
		initIfNeeded();
		int depth = Math.max(1, Dungeon.depth);
		int path = Statistics.deckBuilderMapPath;
		if (restDepth != depth || restPath != path) {
			restDepth = depth;
			restPath = path;
			restUsed = false;
		}
	}

	public static boolean restUsedForCurrentNode() {
		initRestForCurrentNode();
		return restUsed;
	}

	public static int restHealAmount() {
		initIfNeeded();
		return Math.max(0, playerHT * 30 / 100);
	}

	public static boolean canRestAtRestSite() {
		initRestForCurrentNode();
		return !restUsed && restHealAmount() > 0;
	}

	public static boolean canSmithAtRestSite() {
		initRestForCurrentNode();
		if (restUsed) return false;
		for (int code : deck) {
			if (DeckCard.upgrade(code) != code) return true;
		}
		return false;
	}

	public static boolean restAtRestSite() {
		if (!canRestAtRestSite()) return false;
		playerHP = Math.min(playerHT, playerHP + restHealAmount());
		restUsed = true;
		return true;
	}

	public static boolean smithAtRestSite(int index) {
		if (!canSmithAtRestSite() || !upgradeCardAt(index)) return false;
		restUsed = true;
		return true;
	}

	public static void clearRest() {
		restDepth = -1;
		restPath = -1;
		restUsed = false;
	}

	private static int rollTreasureChest() {
		int roll = Random.Int(100);
		if (roll < 50) return 0;
		if (roll < 83) return 1;
		return 2;
	}

	private static DeckRelicRarity rollTreasureRarity(int chest) {
		int roll = Random.Int(100);
		if (chest == 0) return roll < 75 ? DeckRelicRarity.COMMON : DeckRelicRarity.UNCOMMON;
		if (chest == 1) {
			if (roll < 35) return DeckRelicRarity.COMMON;
			if (roll < 85) return DeckRelicRarity.UNCOMMON;
			return DeckRelicRarity.RARE;
		}
		return roll < 75 ? DeckRelicRarity.UNCOMMON : DeckRelicRarity.RARE;
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
		DeckCard[] choices = new DeckCard[4];
		boolean rareSeen = false;
		int commonSeen = 0;
		for (int i = 0; i < choices.length; i++) {
			DeckCardRarity rarity = rollCardRarity(nodeType);
			DeckCard card = randomCard(rarity, classSlot(i));
			int guard = 0;
			while (duplicate(choices, i, card) && guard++ < 20) {
				card = randomCard(rarity, classSlot(i));
			}
			choices[i] = card == null ? DeckCard.STRIKE : card;
			if (choices[i].rarity == DeckCardRarity.RARE) rareSeen = true;
			if (choices[i].rarity == DeckCardRarity.COMMON) commonSeen++;
		}
		cardRareOffset = rareSeen ? -5 : cardRareOffset + commonSeen;
		return choices;
	}

	private static int classSlot(int index) {
		if (index == 0) return 100;
		if (index == 1) return 60;
		if (index == 2) return 20;
		return 0;
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
		return randomCard(rarity, -1);
	}

	private static DeckCard randomCard(DeckCardRarity rarity, int classChance) {
		boolean classOnly = classChance >= 100;
		boolean neutralOnly = classChance == 0;
		boolean classPool = classOnly || (!neutralOnly && classChance > 0 && Random.Int(100) < classChance);
		DeckCard[] pool = DeckCard.rewardPool(Dungeon.hero == null ? null : Dungeon.hero.heroClass, classPool, !classPool);
		ArrayList<DeckCard> rarityPool = new ArrayList<>();
		for (DeckCard card : pool) {
			if (card.rarity == rarity) rarityPool.add(card);
		}
		if (rarityPool.isEmpty() && classPool && !classOnly) {
			return randomCard(rarity, 0);
		}
		if (rarityPool.isEmpty() && !classPool && !neutralOnly) {
			return randomCard(rarity, 100);
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

	public static boolean upgradeCardAt(int index) {
		initIfNeeded();
		if (index < 0 || index >= deck.size()) return false;
		int cardCode = deck.get(index);
		int upgraded = DeckCard.upgrade(cardCode);
		if (upgraded == cardCode) return false;
		deck.set(index, upgraded);
		return true;
	}

	public static boolean removeCardAt(int index) {
		initIfNeeded();
		if (index < 0 || index >= deck.size()) return false;
		deck.remove(index);
		return true;
	}

	public static DeckShop.Offer[] shopOffersForCurrentNode() {
		initIfNeeded();
		int depth = Math.max(1, Dungeon.depth);
		int path = Statistics.deckBuilderMapPath;
		boolean missingShop = shopTypes == null || shopIds == null || shopPrices == null || shopSold == null
				|| shopTypes.length != shopIds.length || shopTypes.length != shopPrices.length || shopTypes.length != shopSold.length;
		if (missingShop) {
			DeckShop.Offer[] offers = DeckShop.generateOffers();
			shopDepth = depth;
			shopPath = path;
			shopTypes = new int[offers.length];
			shopIds = new int[offers.length];
			shopPrices = new int[offers.length];
			shopSold = new boolean[offers.length];
			shopSales = new boolean[offers.length];
			shopRemoveUsed = false;
			for (int i = 0; i < offers.length; i++) {
				shopTypes[i] = offers[i].type;
				shopIds[i] = offers[i].id;
				shopPrices[i] = offers[i].price;
				shopSales[i] = offers[i].sale;
			}
		}
		DeckShop.Offer[] offers = new DeckShop.Offer[shopTypes.length];
		for (int i = 0; i < offers.length; i++) {
			offers[i] = new DeckShop.Offer(shopTypes[i], shopIds[i], shopPrices[i], shopSales != null && i < shopSales.length && shopSales[i]);
		}
		return offers;
	}

	public static boolean buyShopOffer(int index) {
		shopOffersForCurrentNode();
		if (index < 0 || index >= shopTypes.length || shopSold[index] || gold < shopPrices[index]) return false;
		int type = shopTypes[index];
		int id = shopIds[index];
		if (type == DeckShop.CARD) {
			DeckCard[] cards = DeckCard.values();
			if (id < 0 || id >= cards.length) return false;
			addCard(cards[id]);
		} else if (type == DeckShop.POTION) {
			DeckPotion potion = DeckPotion.byId(id);
			if (potion == null || !addPotion(potion)) return false;
		} else if (type == DeckShop.RELIC) {
			DeckRelic relic = DeckRelic.byId(id);
			if (relic == null || hasRelic(relic)) return false;
			addRelic(relic);
		} else {
			return false;
		}
		gold -= shopPrices[index];
		shopSold[index] = true;
		return true;
	}

	public static boolean buyCardRemoval(int deckIndex) {
		initIfNeeded();
		int price = DeckShop.removePrice();
		if (shopRemoveUsed || gold < price || !removeCardAt(deckIndex)) return false;
		gold -= price;
		shopRemoveUsed = true;
		shopRemoveCount++;
		return true;
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
		int count = 3;
		boolean valid = startingRelicChoices != null && startingRelicChoices.length == count;
		ArrayList<Integer> seen = new ArrayList<>();
		if (valid) {
			for (int id : startingRelicChoices) {
				DeckRelic relic = DeckRelic.byId(id);
				if (seen.contains(id) || relic.type != DeckRelicType.STARTER) {
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
			DeckRelic relic = DeckRelic.randomStarterAvailable(seen);
			int id = relic == null ? DeckRelic.STARTER_LUCKY_COIN.ordinal() : relic.ordinal();
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
		bundle.put(SHOP_REMOVE_COUNT, shopRemoveCount);
		bundle.put(SHOP_DEPTH, shopDepth);
		bundle.put(SHOP_PATH, shopPath);
		if (shopTypes != null) bundle.put(SHOP_TYPES, shopTypes);
		if (shopIds != null) bundle.put(SHOP_IDS, shopIds);
		if (shopPrices != null) bundle.put(SHOP_PRICES, shopPrices);
		if (shopSold != null) bundle.put(SHOP_SOLD, shopSold);
		if (shopSales != null) bundle.put(SHOP_SALES, shopSales);
		bundle.put(SHOP_REMOVE_USED, shopRemoveUsed);
		bundle.put(TREASURE_DEPTH, treasureDepth);
		bundle.put(TREASURE_PATH, treasurePath);
		bundle.put(TREASURE_CHEST, treasureChest);
		bundle.put(TREASURE_RELIC, treasureRelic);
		bundle.put(TREASURE_CLAIMED, treasureClaimed);
		bundle.put(REST_DEPTH, restDepth);
		bundle.put(REST_PATH, restPath);
		bundle.put(REST_USED, restUsed);
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
		shopRemoveCount = bundle.contains(SHOP_REMOVE_COUNT) ? bundle.getInt(SHOP_REMOVE_COUNT) : 0;
		shopDepth = bundle.contains(SHOP_DEPTH) ? bundle.getInt(SHOP_DEPTH) : -1;
		shopPath = bundle.contains(SHOP_PATH) ? bundle.getInt(SHOP_PATH) : -1;
		shopTypes = bundle.contains(SHOP_TYPES) ? bundle.getIntArray(SHOP_TYPES) : null;
		shopIds = bundle.contains(SHOP_IDS) ? bundle.getIntArray(SHOP_IDS) : null;
		shopPrices = bundle.contains(SHOP_PRICES) ? bundle.getIntArray(SHOP_PRICES) : null;
		shopSold = bundle.contains(SHOP_SOLD) ? bundle.getBooleanArray(SHOP_SOLD) : null;
		shopSales = bundle.contains(SHOP_SALES) ? bundle.getBooleanArray(SHOP_SALES) : null;
		shopRemoveUsed = bundle.getBoolean(SHOP_REMOVE_USED);
		treasureDepth = bundle.contains(TREASURE_DEPTH) ? bundle.getInt(TREASURE_DEPTH) : -1;
		treasurePath = bundle.contains(TREASURE_PATH) ? bundle.getInt(TREASURE_PATH) : -1;
		treasureChest = bundle.contains(TREASURE_CHEST) ? bundle.getInt(TREASURE_CHEST) : 0;
		treasureRelic = bundle.contains(TREASURE_RELIC) ? bundle.getInt(TREASURE_RELIC) : -1;
		treasureClaimed = bundle.getBoolean(TREASURE_CLAIMED);
		restDepth = bundle.contains(REST_DEPTH) ? bundle.getInt(REST_DEPTH) : -1;
		restPath = bundle.contains(REST_PATH) ? bundle.getInt(REST_PATH) : -1;
		restUsed = bundle.getBoolean(REST_USED);
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
