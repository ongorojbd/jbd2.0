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

import com.watabou.utils.Bundle;

public class DeckBuilderRunBundle {

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
	private static final String LAST_ELITE_ENCOUNTER  = "deckbuilder_last_elite_encounter";
	private static final String SHOP_REMOVE_COUNT = "deckbuilder_shop_remove_count";
	private static final String MYSTERY_COMBAT_BONUS    = "deckbuilder_mystery_combat_bonus";
	private static final String MYSTERY_SHOP_BONUS      = "deckbuilder_mystery_shop_bonus";
	private static final String MYSTERY_TREASURE_BONUS  = "deckbuilder_mystery_treasure_bonus";
	private static final String MYSTERY_VISITS          = "deckbuilder_mystery_visits";
	private static final String MYSTERY_PREV_WAS_SHOP   = "deckbuilder_mystery_prev_was_shop";
	private static final String MYSTERY_RESOLVED_DEPTH  = "deckbuilder_mystery_resolved_depth";
	private static final String MYSTERY_RESOLVED_PATH   = "deckbuilder_mystery_resolved_path";
	private static final String MYSTERY_RESOLVED_TYPE   = "deckbuilder_mystery_resolved_type";
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
	private static final String REST_TENT_USED = "deckbuilder_rest_tent_used";
	private static final String REWARD_NODE = "deckbuilder_reward_node";
	private static final String REWARD_DEPTH = "deckbuilder_reward_depth";
	private static final String REWARD_PATH = "deckbuilder_reward_path";
	private static final String REWARD_GOLD = "deckbuilder_reward_gold";
	private static final String REWARD_RELICS = "deckbuilder_reward_relics";
	private static final String REWARD_POTION = "deckbuilder_reward_potion";
	private static final String REWARD_CARDS = "deckbuilder_reward_cards";
	private static final String REWARD_GOLD_CLAIMED = "deckbuilder_reward_gold_claimed";
	private static final String REWARD_RELIC_CLAIMED = "deckbuilder_reward_relic_claimed";
	private static final String REWARD_POTION_CLAIMED = "deckbuilder_reward_potion_claimed";
	private static final String REWARD_CARD_CLAIMED = "deckbuilder_reward_card_claimed";
	private static final String LAST_EVENT_TYPE               = "deckbuilder_last_event_type";
	private static final String PENDING_CARD_TRANSFORM        = "deckbuilder_pending_card_transform";
	private static final String PENDING_NEUTRAL_DISCOVER      = "deckbuilder_pending_neutral_discover";
	private static final String PENDING_CARD_REWARD           = "deckbuilder_pending_card_reward";
	private static final String PENDING_CARD_REWARD_COUNT     = "deckbuilder_pending_card_reward_count";
	private static final String PENDING_CARD_REMOVE           = "deckbuilder_pending_card_remove";
	private static final String PENDING_CARD_UPGRADE          = "deckbuilder_pending_card_upgrade";
	private static final String PENDING_OTHER_CLASS_REWARD    = "deckbuilder_pending_other_class_reward";
	private static final String PENDING_CARD_REMOVE_COUNT     = "deckbuilder_pending_card_remove_count";
	private static final String PENDING_RARE_CARD_CHOICE      = "deckbuilder_pending_rare_card_choice";
	private static final String FISHING_ROD_PROGRESS         = "deckbuilder_fishing_rod_progress";
	private static final String UPGRADED_CARD_REWARD_COUNT   = "deckbuilder_upgraded_card_reward_count";
	private static final String FIRST_TREASURE_EMPTY         = "deckbuilder_first_treasure_empty";
	private static final String LIFE_BOMB_CARDS_ADDED       = "deckbuilder_life_bomb_cards_added";
	private static final String NEXT_COMBAT_BONUS_ENERGY    = "deckbuilder_next_combat_bonus_energy";
	private static final String DIVER_DOWN_USED             = "deckbuilder_diver_down_used";
	private static final String TUTORIAL_MODE                = "deckbuilder_tutorial_mode";
	private static final String TUTORIAL_STEP                = "deckbuilder_tutorial_step";
	private static final String TUTORIAL_MAP_MESSAGE_SHOWN   = "deckbuilder_tutorial_map_message_shown";
	private static final String SELECTED_BOSS               = "deckbuilder_selected_boss";

	public static void store(Bundle bundle) {
		bundle.put(INITIALIZED, DeckBuilderRun.initialized);
		bundle.put(PLAYER_HP, DeckBuilderRun.playerHP);
		bundle.put(PLAYER_HT, DeckBuilderRun.playerHT);
		bundle.put(DECK, DeckRunInventory.toArray(DeckBuilderRun.deck));
		bundle.put(MAX_ENERGY, DeckBuilderRun.maxEnergy);
		bundle.put(HAND_SIZE, DeckBuilderRun.handSize);
		bundle.put(MAX_HAND_SIZE, DeckBuilderRun.maxHandSize);
		bundle.put(RELICS, DeckRunInventory.toArray(DeckBuilderRun.relics));
		bundle.put(GOLD, DeckBuilderRun.gold);
		bundle.put(POTIONS, DeckRunInventory.toArray(DeckBuilderRun.potions));
		bundle.put(CARD_RARE_OFFSET, DeckBuilderRun.cardRareOffset);
		bundle.put(POTION_DROP_CHANCE, DeckBuilderRun.potionDropChance);
		bundle.put(STARTING_RELIC_CHOSEN, DeckBuilderRun.startingRelicChosen);
		if (DeckBuilderRun.startingRelicChoices != null) bundle.put(STARTING_RELIC_CHOICES, DeckBuilderRun.startingRelicChoices);
		bundle.put(ACT1_NORMAL_FIGHTS, DeckBuilderRun.act1NormalFights);
		bundle.put(LAST_NORMAL_ENCOUNTER, DeckBuilderRun.lastNormalEncounter);
		bundle.put(LAST_ELITE_ENCOUNTER,  DeckBuilderRun.lastEliteEncounter);
		bundle.put(SHOP_REMOVE_COUNT, DeckBuilderRun.shopRemoveCount);
		bundle.put(MYSTERY_COMBAT_BONUS,   DeckBuilderRun.mysteryCombatBonus);
		bundle.put(MYSTERY_SHOP_BONUS,     DeckBuilderRun.mysteryShopBonus);
		bundle.put(MYSTERY_TREASURE_BONUS, DeckBuilderRun.mysteryTreasureBonus);
		bundle.put(MYSTERY_VISITS,         DeckBuilderRun.mysteryVisitsThisAct);
		bundle.put(MYSTERY_PREV_WAS_SHOP,  DeckBuilderRun.mysteryPrevWasShop);
		bundle.put(MYSTERY_RESOLVED_DEPTH, DeckBuilderRun.mysteryResolvedDepth);
		bundle.put(MYSTERY_RESOLVED_PATH,  DeckBuilderRun.mysteryResolvedPath);
		bundle.put(MYSTERY_RESOLVED_TYPE,  DeckBuilderRun.mysteryResolvedType);
		bundle.put(LAST_EVENT_TYPE,            DeckBuilderRun.lastEventType);
		bundle.put(PENDING_CARD_TRANSFORM,     DeckBuilderRun.pendingCardTransform);
		bundle.put(PENDING_NEUTRAL_DISCOVER,   DeckBuilderRun.pendingNeutralDiscover);
		bundle.put(PENDING_CARD_REWARD,        DeckBuilderRun.pendingCardReward);
		bundle.put(PENDING_CARD_REWARD_COUNT,  DeckBuilderRun.pendingCardRewardCount);
		bundle.put(PENDING_CARD_REMOVE,        DeckBuilderRun.pendingCardRemove);
		bundle.put(PENDING_CARD_UPGRADE,       DeckBuilderRun.pendingCardUpgrade);
		bundle.put(PENDING_OTHER_CLASS_REWARD, DeckBuilderRun.pendingOtherClassCardReward);
		bundle.put(PENDING_CARD_REMOVE_COUNT,  DeckBuilderRun.pendingCardRemoveCount);
		bundle.put(PENDING_RARE_CARD_CHOICE,   DeckBuilderRun.pendingRareCardChoice);
		bundle.put(FISHING_ROD_PROGRESS,       DeckBuilderRun.fishingRodProgress);
		bundle.put(UPGRADED_CARD_REWARD_COUNT, DeckBuilderRun.upgradedCardRewardCount);
		bundle.put(FIRST_TREASURE_EMPTY,       DeckBuilderRun.firstTreasureEmpty);
		bundle.put(LIFE_BOMB_CARDS_ADDED,      DeckBuilderRun.lifeBombCardsAdded);
		bundle.put(NEXT_COMBAT_BONUS_ENERGY,   DeckBuilderRun.nextCombatBonusEnergy);
		bundle.put(DIVER_DOWN_USED,            DeckBuilderRun.diverDownUsed);
		bundle.put(TUTORIAL_MODE,              DeckBuilderRun.tutorialMode);
		bundle.put(TUTORIAL_STEP,              DeckBuilderRun.tutorialStep);
		bundle.put(TUTORIAL_MAP_MESSAGE_SHOWN, DeckBuilderRun.tutorialMapMessageShown);
		bundle.put(SELECTED_BOSS,              DeckBuilderRun.selectedBoss);
		storeShop(bundle);
		storeTreasure(bundle);
		storeRest(bundle);
		storeReward(bundle);
		if (DeckBuilderRun.currentCombat != null && !DeckBuilderRun.currentCombat.playerDead()) {
			Bundle combatBundle = new Bundle();
			DeckBuilderRun.currentCombat.storeInBundle(combatBundle);
			bundle.put(CURRENT_COMBAT, combatBundle);
		}
	}

	public static void restore(Bundle bundle) {
		DeckBuilderRun.initialized = bundle.getBoolean(INITIALIZED);
		DeckBuilderRun.playerHP = bundle.getInt(PLAYER_HP);
		DeckBuilderRun.playerHT = bundle.getInt(PLAYER_HT);
		DeckBuilderRun.maxEnergy = bundle.contains(MAX_ENERGY) ? bundle.getInt(MAX_ENERGY) : DeckBuilderRun.STARTING_ENERGY;
		DeckBuilderRun.handSize = bundle.contains(HAND_SIZE) ? bundle.getInt(HAND_SIZE) : DeckBuilderRun.STARTING_HAND_SIZE;
		DeckBuilderRun.maxHandSize = bundle.contains(MAX_HAND_SIZE) ? bundle.getInt(MAX_HAND_SIZE) : DeckBuilderRun.DEFAULT_MAX_HAND_SIZE;
		DeckBuilderRun.gold = bundle.contains(GOLD) ? bundle.getInt(GOLD) : 0;
		DeckBuilderRun.cardRareOffset = bundle.contains(CARD_RARE_OFFSET) ? bundle.getInt(CARD_RARE_OFFSET) : -5;
		DeckBuilderRun.potionDropChance = bundle.contains(POTION_DROP_CHANCE) ? bundle.getInt(POTION_DROP_CHANCE) : DeckPotionPolicy.STARTING_DROP_CHANCE;
		DeckBuilderRun.startingRelicChosen = bundle.getBoolean(STARTING_RELIC_CHOSEN);
		DeckBuilderRun.startingRelicChoices = bundle.contains(STARTING_RELIC_CHOICES) ? bundle.getIntArray(STARTING_RELIC_CHOICES) : null;
		DeckBuilderRun.act1NormalFights = bundle.contains(ACT1_NORMAL_FIGHTS) ? bundle.getInt(ACT1_NORMAL_FIGHTS) : 0;
		DeckBuilderRun.lastNormalEncounter = bundle.contains(LAST_NORMAL_ENCOUNTER) ? bundle.getInt(LAST_NORMAL_ENCOUNTER) : -1;
		DeckBuilderRun.lastEliteEncounter  = bundle.contains(LAST_ELITE_ENCOUNTER)  ? bundle.getInt(LAST_ELITE_ENCOUNTER)  : -1;
		DeckBuilderRun.shopRemoveCount        = bundle.contains(SHOP_REMOVE_COUNT)       ? bundle.getInt(SHOP_REMOVE_COUNT)       : 0;
		DeckBuilderRun.mysteryCombatBonus      = bundle.contains(MYSTERY_COMBAT_BONUS)    ? bundle.getInt(MYSTERY_COMBAT_BONUS)    : 0;
		DeckBuilderRun.mysteryShopBonus        = bundle.contains(MYSTERY_SHOP_BONUS)      ? bundle.getInt(MYSTERY_SHOP_BONUS)      : 0;
		DeckBuilderRun.mysteryTreasureBonus    = bundle.contains(MYSTERY_TREASURE_BONUS)  ? bundle.getInt(MYSTERY_TREASURE_BONUS)  : 0;
		DeckBuilderRun.mysteryVisitsThisAct    = bundle.contains(MYSTERY_VISITS)          ? bundle.getInt(MYSTERY_VISITS)          : 0;
		DeckBuilderRun.mysteryPrevWasShop      = bundle.getBoolean(MYSTERY_PREV_WAS_SHOP);
		DeckBuilderRun.mysteryResolvedDepth    = bundle.contains(MYSTERY_RESOLVED_DEPTH)  ? bundle.getInt(MYSTERY_RESOLVED_DEPTH)  : -1;
		DeckBuilderRun.mysteryResolvedPath     = bundle.contains(MYSTERY_RESOLVED_PATH)   ? bundle.getInt(MYSTERY_RESOLVED_PATH)   : -1;
		DeckBuilderRun.mysteryResolvedType     = bundle.contains(MYSTERY_RESOLVED_TYPE)   ? bundle.getInt(MYSTERY_RESOLVED_TYPE)   : DeckBuilderMap.NONE;
		DeckBuilderRun.lastEventType               = bundle.contains(LAST_EVENT_TYPE) ? bundle.getInt(LAST_EVENT_TYPE) : -1;
		DeckBuilderRun.pendingCardTransform        = bundle.getBoolean(PENDING_CARD_TRANSFORM);
		DeckBuilderRun.pendingNeutralDiscover      = bundle.getBoolean(PENDING_NEUTRAL_DISCOVER);
		DeckBuilderRun.pendingCardReward           = bundle.getBoolean(PENDING_CARD_REWARD);
		DeckBuilderRun.pendingCardRewardCount      = bundle.contains(PENDING_CARD_REWARD_COUNT) ? bundle.getInt(PENDING_CARD_REWARD_COUNT) : 0;
		DeckBuilderRun.pendingCardRemove           = bundle.getBoolean(PENDING_CARD_REMOVE);
		DeckBuilderRun.pendingCardUpgrade          = bundle.getBoolean(PENDING_CARD_UPGRADE);
		DeckBuilderRun.pendingOtherClassCardReward = bundle.contains(PENDING_OTHER_CLASS_REWARD) ? bundle.getInt(PENDING_OTHER_CLASS_REWARD) : 0;
		DeckBuilderRun.pendingCardRemoveCount      = bundle.contains(PENDING_CARD_REMOVE_COUNT)  ? bundle.getInt(PENDING_CARD_REMOVE_COUNT)  : 0;
		DeckBuilderRun.pendingRareCardChoice       = bundle.contains(PENDING_RARE_CARD_CHOICE)   ? bundle.getInt(PENDING_RARE_CARD_CHOICE)   : 0;
		DeckBuilderRun.fishingRodProgress          = bundle.contains(FISHING_ROD_PROGRESS)       ? bundle.getInt(FISHING_ROD_PROGRESS)       : 0;
		DeckBuilderRun.upgradedCardRewardCount     = bundle.contains(UPGRADED_CARD_REWARD_COUNT) ? bundle.getInt(UPGRADED_CARD_REWARD_COUNT) : 0;
		DeckBuilderRun.firstTreasureEmpty          = bundle.getBoolean(FIRST_TREASURE_EMPTY);
		DeckBuilderRun.lifeBombCardsAdded          = bundle.contains(LIFE_BOMB_CARDS_ADDED) ? bundle.getInt(LIFE_BOMB_CARDS_ADDED) : 0;
		DeckBuilderRun.nextCombatBonusEnergy       = bundle.contains(NEXT_COMBAT_BONUS_ENERGY) ? bundle.getInt(NEXT_COMBAT_BONUS_ENERGY) : 0;
		DeckBuilderRun.diverDownUsed               = bundle.contains(DIVER_DOWN_USED) && bundle.getBoolean(DIVER_DOWN_USED);
		DeckBuilderRun.tutorialMode                = bundle.getBoolean(TUTORIAL_MODE);
		DeckBuilderRun.tutorialStep                = bundle.contains(TUTORIAL_STEP) ? bundle.getInt(TUTORIAL_STEP) : 0;
		DeckBuilderRun.tutorialMapMessageShown     = bundle.getBoolean(TUTORIAL_MAP_MESSAGE_SHOWN);
		DeckBuilderRun.selectedBoss                = bundle.contains(SELECTED_BOSS) ? bundle.getInt(SELECTED_BOSS) : -1;
		restoreShop(bundle);
		restoreTreasure(bundle);
		restoreRest(bundle);
		restoreReward(bundle);
		DeckBuilderRun.sanitizeStartingRelicChoices();
		restoreDeck(bundle);
		restoreRelics(bundle);
		restorePotions(bundle);
		DeckBuilderRun.currentCombat = bundle.contains(CURRENT_COMBAT)
				? DeckBuilderCombat.restoreFromBundle(bundle.getBundle(CURRENT_COMBAT))
				: null;
	}

	private static void storeShop(Bundle bundle) {
		bundle.put(SHOP_DEPTH, DeckBuilderRun.shop.depth);
		bundle.put(SHOP_PATH, DeckBuilderRun.shop.path);
		if (DeckBuilderRun.shop.types != null) bundle.put(SHOP_TYPES, DeckBuilderRun.shop.types);
		if (DeckBuilderRun.shop.ids != null) bundle.put(SHOP_IDS, DeckBuilderRun.shop.ids);
		if (DeckBuilderRun.shop.prices != null) bundle.put(SHOP_PRICES, DeckBuilderRun.shop.prices);
		if (DeckBuilderRun.shop.sold != null) bundle.put(SHOP_SOLD, DeckBuilderRun.shop.sold);
		if (DeckBuilderRun.shop.sales != null) bundle.put(SHOP_SALES, DeckBuilderRun.shop.sales);
		bundle.put(SHOP_REMOVE_USED, DeckBuilderRun.shop.removeUsed);
	}

	private static void restoreShop(Bundle bundle) {
		DeckBuilderRun.shop.depth = bundle.contains(SHOP_DEPTH) ? bundle.getInt(SHOP_DEPTH) : -1;
		DeckBuilderRun.shop.path = bundle.contains(SHOP_PATH) ? bundle.getInt(SHOP_PATH) : -1;
		DeckBuilderRun.shop.types = bundle.contains(SHOP_TYPES) ? bundle.getIntArray(SHOP_TYPES) : null;
		DeckBuilderRun.shop.ids = bundle.contains(SHOP_IDS) ? bundle.getIntArray(SHOP_IDS) : null;
		DeckBuilderRun.shop.prices = bundle.contains(SHOP_PRICES) ? bundle.getIntArray(SHOP_PRICES) : null;
		DeckBuilderRun.shop.sold = bundle.contains(SHOP_SOLD) ? bundle.getBooleanArray(SHOP_SOLD) : null;
		DeckBuilderRun.shop.sales = bundle.contains(SHOP_SALES) ? bundle.getBooleanArray(SHOP_SALES) : null;
		DeckBuilderRun.shop.removeUsed = bundle.getBoolean(SHOP_REMOVE_USED);
	}

	private static void storeTreasure(Bundle bundle) {
		bundle.put(TREASURE_DEPTH, DeckBuilderRun.treasure.depth);
		bundle.put(TREASURE_PATH, DeckBuilderRun.treasure.path);
		bundle.put(TREASURE_CHEST, DeckBuilderRun.treasure.chest);
		bundle.put(TREASURE_RELIC, DeckBuilderRun.treasure.relic);
		bundle.put(TREASURE_CLAIMED, DeckBuilderRun.treasure.claimed);
	}

	private static void restoreTreasure(Bundle bundle) {
		DeckBuilderRun.treasure.depth = bundle.contains(TREASURE_DEPTH) ? bundle.getInt(TREASURE_DEPTH) : -1;
		DeckBuilderRun.treasure.path = bundle.contains(TREASURE_PATH) ? bundle.getInt(TREASURE_PATH) : -1;
		DeckBuilderRun.treasure.chest = bundle.contains(TREASURE_CHEST) ? bundle.getInt(TREASURE_CHEST) : 0;
		DeckBuilderRun.treasure.relic = bundle.contains(TREASURE_RELIC) ? bundle.getInt(TREASURE_RELIC) : -1;
		DeckBuilderRun.treasure.claimed = bundle.getBoolean(TREASURE_CLAIMED);
	}

	private static void storeRest(Bundle bundle) {
		bundle.put(REST_DEPTH, DeckBuilderRun.rest.depth);
		bundle.put(REST_PATH, DeckBuilderRun.rest.path);
		bundle.put(REST_USED, DeckBuilderRun.rest.used);
		bundle.put(REST_TENT_USED, DeckBuilderRun.rest.tentUsed);
	}

	private static void restoreRest(Bundle bundle) {
		DeckBuilderRun.rest.depth = bundle.contains(REST_DEPTH) ? bundle.getInt(REST_DEPTH) : -1;
		DeckBuilderRun.rest.path = bundle.contains(REST_PATH) ? bundle.getInt(REST_PATH) : -1;
		DeckBuilderRun.rest.used = bundle.getBoolean(REST_USED);
		DeckBuilderRun.rest.tentUsed = bundle.contains(REST_TENT_USED) && bundle.getBoolean(REST_TENT_USED);
	}

	private static void storeReward(Bundle bundle) {
		bundle.put(REWARD_NODE, DeckBuilderRun.reward.node);
		bundle.put(REWARD_DEPTH, DeckBuilderRun.reward.depth);
		bundle.put(REWARD_PATH, DeckBuilderRun.reward.path);
		bundle.put(REWARD_GOLD, DeckBuilderRun.reward.gold);
		if (DeckBuilderRun.reward.relics != null) bundle.put(REWARD_RELICS, DeckBuilderRun.reward.relics);
		bundle.put(REWARD_POTION, DeckBuilderRun.reward.potion);
		if (DeckBuilderRun.reward.cards != null) bundle.put(REWARD_CARDS, DeckBuilderRun.reward.cards);
		bundle.put(REWARD_GOLD_CLAIMED, DeckBuilderRun.reward.goldClaimed);
		if (DeckBuilderRun.reward.relicClaimed != null) bundle.put(REWARD_RELIC_CLAIMED, DeckBuilderRun.reward.relicClaimed);
		bundle.put(REWARD_POTION_CLAIMED, DeckBuilderRun.reward.potionClaimed);
		bundle.put(REWARD_CARD_CLAIMED, DeckBuilderRun.reward.cardClaimed);
	}

	private static void restoreReward(Bundle bundle) {
		DeckBuilderRun.reward.node = bundle.contains(REWARD_NODE) ? bundle.getInt(REWARD_NODE) : DeckBuilderMap.NONE;
		DeckBuilderRun.reward.depth = bundle.contains(REWARD_DEPTH) ? bundle.getInt(REWARD_DEPTH) : -1;
		DeckBuilderRun.reward.path = bundle.contains(REWARD_PATH) ? bundle.getInt(REWARD_PATH) : -1;
		DeckBuilderRun.reward.gold = bundle.contains(REWARD_GOLD) ? bundle.getInt(REWARD_GOLD) : 0;
		DeckBuilderRun.reward.relics = bundle.contains(REWARD_RELICS) ? bundle.getIntArray(REWARD_RELICS) : null;
		DeckBuilderRun.reward.potion = bundle.contains(REWARD_POTION) ? bundle.getInt(REWARD_POTION) : -1;
		DeckBuilderRun.reward.cards = bundle.contains(REWARD_CARDS) ? bundle.getIntArray(REWARD_CARDS) : null;
		DeckBuilderRun.reward.goldClaimed = bundle.getBoolean(REWARD_GOLD_CLAIMED);
		DeckBuilderRun.reward.relicClaimed = bundle.contains(REWARD_RELIC_CLAIMED) ? bundle.getBooleanArray(REWARD_RELIC_CLAIMED) : null;
		DeckBuilderRun.reward.potionClaimed = bundle.getBoolean(REWARD_POTION_CLAIMED);
		DeckBuilderRun.reward.cardClaimed = bundle.getBoolean(REWARD_CARD_CLAIMED);
		if (DeckBuilderRun.reward.relics != null
				&& (DeckBuilderRun.reward.relicClaimed == null || DeckBuilderRun.reward.relicClaimed.length != DeckBuilderRun.reward.relics.length)) {
			DeckBuilderRun.reward.relicClaimed = new boolean[DeckBuilderRun.reward.relics.length];
		}
	}

	private static void restoreDeck(Bundle bundle) {
		DeckBuilderRun.deck.clear();
		if (bundle.contains(DECK)) {
			for (int id : bundle.getIntArray(DECK)) {
				DeckBuilderRun.deck.add(id);
			}
		}
	}

	private static void restoreRelics(Bundle bundle) {
		DeckBuilderRun.relics.clear();
		if (bundle.contains(RELICS)) {
			for (int id : bundle.getIntArray(RELICS)) {
				DeckBuilderRun.relics.add(id);
			}
		}
	}

	private static void restorePotions(Bundle bundle) {
		DeckBuilderRun.potions.clear();
		if (bundle.contains(POTIONS)) {
			for (int id : bundle.getIntArray(POTIONS)) {
				if (DeckBuilderRun.potions.size() < DeckBuilderRun.maxPotionSlots() && DeckPotion.byId(id) != null) {
					DeckBuilderRun.potions.add(id);
				}
			}
		}
	}
}
