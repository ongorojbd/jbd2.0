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

import com.shatteredpixel.shatteredpixeldungeon.deckbuilder.DeckCardType;

import java.util.ArrayList;

public class DeckBuilderRun {

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
	public static int lastEliteEncounter; // -1: 없음
	public static int selectedBoss = -1; // -1: 미결정, 0: 크림, 1: 시빌 워, 2: 누케사쿠
	public static int shopRemoveCount;
	// 미지 노드 천장(pity) 시스템
	public static int mysteryCombatBonus = 0;
	public static int mysteryShopBonus = 0;
	public static int mysteryTreasureBonus = 0;
	public static int mysteryVisitsThisAct = 0;
	public static boolean mysteryPrevWasShop = false;
	public static int mysteryResolvedDepth = -1;
	public static int mysteryResolvedPath = -1;
	public static int mysteryResolvedType = DeckBuilderMap.NONE;
	public static DeckBuilderCombat currentCombat;

	// Pending relic events set by onAcquire(), resolved in DeckRelicChoiceScene before going to map
	public static boolean pendingCardTransform;
	public static boolean pendingNeutralDiscover;
	public static boolean pendingCardReward;
	public static boolean pendingCardRemove;
	public static int pendingCardRemoveCount;
	public static boolean pendingCardUpgrade;
	public static int pendingOtherClassCardReward;
	public static int pendingRareCardChoice;
	// Fishing Rod: counts normal combats toward next random card upgrade
	public static int fishingRodProgress;
	// Silver Crucible: upgrade card picked from reward; first treasure empty
	public static int upgradedCardRewardCount;
	public static boolean firstTreasureEmpty;
	public static boolean tutorialMode;
	public static int tutorialStep;
	public static boolean tutorialMapMessageShown;

	static final DeckShopState shop = new DeckShopState();
	static final DeckTreasureState treasure = new DeckTreasureState();
	static final DeckRestState rest = new DeckRestState();
	static final DeckCombatRewardState reward = new DeckCombatRewardState();

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
		potionDropChance = DeckPotionPolicy.STARTING_DROP_CHANCE;
		startingRelicChosen = false;
		startingRelicChoices = null;
		act1NormalFights = 0;
		lastNormalEncounter = -1;
		lastEliteEncounter = -1;
		selectedBoss = -1;
		shopRemoveCount = 0;
		mysteryCombatBonus = 0;
		mysteryShopBonus = 0;
		mysteryTreasureBonus = 0;
		mysteryVisitsThisAct = 0;
		mysteryPrevWasShop = false;
		mysteryResolvedDepth = -1;
		mysteryResolvedPath = -1;
		mysteryResolvedType = DeckBuilderMap.NONE;
		pendingCardTransform = false;
		pendingNeutralDiscover = false;
		pendingCardReward = false;
		pendingCardRemove = false;
		pendingCardRemoveCount = 0;
		pendingCardUpgrade = false;
		pendingOtherClassCardReward = 0;
		pendingRareCardChoice = 0;
		fishingRodProgress = 0;
		upgradedCardRewardCount = 0;
		firstTreasureEmpty = false;
		tutorialMode = false;
		tutorialStep = 0;
		tutorialMapMessageShown = false;
		clearShop();
		clearTreasure();
		clearRest();
		clearCombatReward();
		currentCombat = null;
	}

	public static void initIfNeeded() {
		if (initialized) return;

		DeckRunStart.initializeNewRun(heroClass());
	}

	public static DeckBuilderCombat newCombat(int nodeType) {
		initIfNeeded();
		return new DeckBuilderCombat(nodeType, Math.max(1, Dungeon.depth), deck);
	}

	public static DeckEnemy[] rollEncounter(int nodeType, int depth) {
		initIfNeeded();
		if (tutorialMode) {
			return new DeckEnemy[]{DeckEnemy.TUTORIAL_DUMMY};
		}
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
				|| currentCombat.playerDead()) {
			currentCombat = newCombat(nodeType);
		}
		return currentCombat;
	}

	public static void clearCombat() {
		currentCombat = null;
		clearCombatReward();
	}

	public static DeckCombatRewardState combatRewardForCurrentNode(int nodeType) {
		initIfNeeded();
		int depth = Math.max(1, Dungeon.depth);
		int path = Statistics.deckBuilderMapPath;
		if (!reward.matches(nodeType, depth, path)) {
			rollCombatReward(nodeType, depth, path);
		}
		return reward;
	}

	public static void clearCombatReward() {
		reward.clear();
	}

	private static void upgradeRandomDeckCard() {
		ArrayList<Integer> upgradable = new ArrayList<>();
		for (int i = 0; i < deck.size(); i++) {
			int code = deck.get(i);
			if (DeckCardCode.upgrade(code) != code) upgradable.add(i);
		}
		if (upgradable.isEmpty()) return;
		int idx = upgradable.get(Random.Int(upgradable.size()));
		deck.set(idx, DeckCardCode.upgrade(deck.get(idx)));
	}

	private static void rollCombatReward(int nodeType, int depth, int path) {
		if (tutorialMode) {
			reward.node = nodeType;
			reward.depth = depth;
			reward.path = path;
			reward.gold = 0;
			reward.relics = new int[0];
			reward.relicClaimed = new boolean[0];
			reward.potion = -1;
			reward.cards = new int[]{
					DeckCard.BASH.ordinal(),
					DeckCard.RIPPLE_WALL.ordinal(),
					DeckCard.IGNITE.ordinal()
			};
			reward.goldClaimed = true;
			reward.potionClaimed = true;
			reward.cardClaimed = false;
			return;
		}
		if (nodeType == DeckBuilderMap.COMBAT && hasRelic(DeckRelic.FISHING_ROD)) {
			fishingRodProgress++;
			if (fishingRodProgress >= 3) {
				fishingRodProgress = 0;
				upgradeRandomDeckCard();
			}
		}
		reward.node = nodeType;
		reward.depth = depth;
		reward.path = path;
		reward.gold = DeckRewardPolicy.rollGold(nodeType);
		DeckRelic[] relics = DeckRewardPolicy.rollRelics(nodeType, hasRelic(DeckRelic.BLACK_STAR));
		reward.relics = new int[relics.length];
		reward.relicClaimed = new boolean[relics.length];
		for (int i = 0; i < relics.length; i++) {
			reward.relics[i] = relics[i].ordinal();
		}
		DeckRewardPolicy.PotionReward potionReward = DeckRewardPolicy.rollPotion(nodeType, potionDropChance);
		potionDropChance = potionReward.nextPotionDropChance;
		reward.potion = potionReward.potion == null ? -1 : potionReward.potion.ordinal();
		DeckRewardPolicy.CardReward cardReward = DeckRewardPolicy.rollCardChoices(nodeType, cardRareOffset, heroClass());
		cardRareOffset = cardReward.nextCardRareOffset;
		reward.cards = new int[cardReward.choices.length];
		for (int i = 0; i < cardReward.choices.length; i++) {
			reward.cards[i] = cardReward.choices[i].ordinal();
		}
		reward.goldClaimed = false;
		reward.potionClaimed = false;
		reward.cardClaimed = false;
	}

	public static void clearShop() {
		shop.clear();
	}

	public static boolean shopMatches(int depth, int path) {
		return shop.matches(depth, path);
	}

	public static boolean shopOfferSold(int index) {
		return shop.sold(index);
	}

	public static boolean shopRemoveUsed() {
		return shop.removeUsed;
	}

	public static DeckRelic treasureRelicForCurrentNode() {
		initIfNeeded();
		int depth = Math.max(1, Dungeon.depth);
		int path = Statistics.deckBuilderMapPath;
		if (treasure.needsRoll(depth, path)) {
			treasure.depth = depth;
			treasure.path = path;
			treasure.chest = DeckRewardPolicy.rollTreasureChest();
			if (firstTreasureEmpty) {
				firstTreasureEmpty = false;
				treasure.relic = -1;
			} else {
				DeckRelic relic = DeckRelic.randomAvailable(DeckRewardPolicy.rollTreasureRarity(treasure.chest));
				treasure.relic = relic == null ? -1 : relic.ordinal();
			}
			treasure.claimed = false;
		}
		return treasure.relic < 0 ? null : DeckRelic.byId(treasure.relic);
	}

	public static void consumeUpgradedCardReward() {
		if (upgradedCardRewardCount <= 0 || deck.isEmpty()) return;
		upgradedCardRewardCount--;
		int lastIdx = deck.size() - 1;
		deck.set(lastIdx, DeckCardCode.upgrade(deck.get(lastIdx)));
	}

	public static int treasureChestForCurrentNode() {
		treasureRelicForCurrentNode();
		return treasure.chest;
	}

	public static boolean claimTreasureRelic() {
		DeckRelic relic = treasureRelicForCurrentNode();
		if (treasure.claimed || relic == null) return false;
		addRelic(relic);
		treasure.claimed = true;
		return true;
	}

	public static void clearTreasure() {
		treasure.clear();
	}

	public static void initRestForCurrentNode() {
		initIfNeeded();
		int depth = Math.max(1, Dungeon.depth);
		int path = Statistics.deckBuilderMapPath;
		rest.initFor(depth, path);
	}

	public static boolean restUsedForCurrentNode() {
		initRestForCurrentNode();
		return rest.used;
	}

	public static int restHealAmount() {
		initIfNeeded();
		return Math.max(0, playerHT * 30 / 100);
	}

	public static boolean canRestAtRestSite() {
		initRestForCurrentNode();
		if (restHealAmount() <= 0) return false;
		if (!rest.used) return true;
		return hasRelic(DeckRelic.MINIATURE_TENT) && !rest.tentUsed;
	}

	public static boolean canSmithAtRestSite() {
		initRestForCurrentNode();
		boolean canUpgrade = false;
		for (int code : deck) {
			DeckCard card = DeckCard.byCode(code);
			if (DeckCardPool.isStatusOrCurse(card)) continue;
			if (DeckCardCode.upgrade(code) != code) { canUpgrade = true; break; }
		}
		if (!canUpgrade) return false;
		if (!rest.used) return true;
		return hasRelic(DeckRelic.MINIATURE_TENT) && !rest.tentUsed;
	}

	public static boolean restAtRestSite() {
		if (!canRestAtRestSite()) return false;
		playerHP = Math.min(playerHT, playerHP + restHealAmount());
		if (hasRelic(DeckRelic.STONE_HUMIDIFIER)) {
			playerHT += 5;
			playerHP = Math.min(playerHT, playerHP + 5);
		}
		if (rest.used) rest.tentUsed = true; else rest.used = true;
		return true;
	}

	public static boolean smithAtRestSite(int index) {
		if (!canSmithAtRestSite() || !upgradeCardAt(index)) return false;
		if (rest.used) rest.tentUsed = true; else rest.used = true;
		return true;
	}

	public static boolean restTentActionsDone() {
		initRestForCurrentNode();
		return rest.used && rest.tentUsed;
	}

	public static void clearRest() {
		rest.clear();
	}

	public static void applyExhaust(ArrayList<Integer> exhausted) {
		DeckRunInventory.applyExhaust(deck, exhausted);
	}

	public static DeckCard[] rewardChoices() {
		return DeckRewardPolicy.commonChoices(heroClass(), 3);
	}

	public static DeckCard[] rewardChoicesForNode(int nodeType) {
		initIfNeeded();
		DeckRewardPolicy.CardReward reward = DeckRewardPolicy.rollCardChoices(nodeType, cardRareOffset, heroClass());
		cardRareOffset = reward.nextCardRareOffset;
		return reward.choices;
	}

	public static DeckCard randomCard(DeckCardRarity rarity) {
		return DeckRewardPolicy.randomCard(rarity, heroClass());
	}

	public static int rollRewardGold(int nodeType) {
		return DeckRewardPolicy.rollGold(nodeType);
	}

	public static DeckRelic[] rollRewardRelics(int nodeType) {
		return DeckRewardPolicy.rollRelics(nodeType, hasRelic(DeckRelic.BLACK_STAR));
	}

	public static DeckPotion rollRewardPotion(int nodeType) {
		DeckRewardPolicy.PotionReward reward = DeckRewardPolicy.rollPotion(nodeType, potionDropChance);
		potionDropChance = reward.nextPotionDropChance;
		return reward.potion;
	}

	public static void resetPotionDropChance() {
		potionDropChance = DeckPotionPolicy.STARTING_DROP_CHANCE;
	}

	public static void addCard(DeckCard card) {
		initIfNeeded();
		DeckRunInventory.addCard(deck, card);
		if (hasRelic(DeckRelic.MOLTEN_EGG) && card != null && card.type == DeckCardType.ATTACK && !deck.isEmpty()) {
			int lastIdx = deck.size() - 1;
			deck.set(lastIdx, DeckCardCode.upgrade(deck.get(lastIdx)));
		}
	}

	public static boolean upgradeCardAt(int index) {
		initIfNeeded();
		return DeckRunInventory.upgradeCardAt(deck, index);
	}

	public static boolean removeCardAt(int index) {
		initIfNeeded();
		return DeckRunInventory.removeCardAt(deck, index);
	}

	public static DeckShop.Offer[] shopOffersForCurrentNode() {
		initIfNeeded();
		int depth = Math.max(1, Dungeon.depth);
		int path = Statistics.deckBuilderMapPath;
		return DeckShopTransaction.offersForNode(shop, depth, path, cardRareOffset, heroClass());
	}

	public static boolean buyShopOffer(int index) {
		shopOffersForCurrentNode();
		return DeckShopTransaction.buyOffer(shop, index);
	}

	public static boolean buyCardRemoval(int deckIndex) {
		initIfNeeded();
		return DeckShopTransaction.buyCardRemoval(shop, deckIndex);
	}

	public static void addRelic(DeckRelic relic) {
		initIfNeeded();
		DeckRunInventory.addRelic(relics, relic);
	}

	public static boolean hasRelic(DeckRelic relic) {
		return DeckRunInventory.hasRelic(relics, relic);
	}

	public static DeckPotion potionAt(int slot) {
		initIfNeeded();
		return DeckRunInventory.potionAt(potions, slot);
	}

	public static boolean addPotion(DeckPotion potion) {
		initIfNeeded();
		return DeckRunInventory.addPotion(potions, potion, MAX_POTION_SLOTS);
	}

	public static void removePotion(int slot) {
		initIfNeeded();
		DeckRunInventory.removePotion(potions, slot);
	}

	public static DeckRelic[] startingRelicChoices() {
		initIfNeeded();
		return DeckRunStart.startingRelicChoices();
	}

	static void sanitizeStartingRelicChoices() {
		DeckRunStart.sanitizeStartingRelicChoices();
	}

	public static void chooseStartingRelic(DeckRelic relic) {
		DeckRunStart.chooseStartingRelic(relic);
	}

	public static void prepareNextAct() {
		startingRelicChosen = false;
		startingRelicChoices = null;
		act1NormalFights = 0;
		lastNormalEncounter = -1;
		lastEliteEncounter = -1;
		selectedBoss = -1;
		mysteryCombatBonus = 0;
		mysteryShopBonus = 0;
		mysteryTreasureBonus = 0;
		mysteryVisitsThisAct = 0;
		mysteryPrevWasShop = false;
		mysteryResolvedDepth = -1;
		mysteryResolvedPath = -1;
		mysteryResolvedType = DeckBuilderMap.NONE;
		clearShop();
		clearTreasure();
		clearRest();
		clearCombatReward();
	}

	public static String relicListText() {
		initIfNeeded();
		return DeckRunInventory.relicListText(relics);
	}

	/**
	 * 미지 노드 진입 시 호출 — 천장 시스템으로 인카운터 타입을 결정합니다.
	 * 같은 depth+path면 캐시된 결과를 반환해 세이브/로드에도 일관성을 유지합니다.
	 */
	public static int resolveMysteryEncounter(int depth, int path) {
		initIfNeeded();
		if (mysteryResolvedDepth == depth && mysteryResolvedPath == path
				&& mysteryResolvedType != DeckBuilderMap.NONE) {
			return mysteryResolvedType;
		}

		int combatP = 10 + mysteryCombatBonus;
		int shopP   = mysteryPrevWasShop ? 0 : (3 + mysteryShopBonus);
		int treasureP = 2 + mysteryTreasureBonus;

		// 6층 이후: 이번 막에서 이미 방문한 미지 수 × 2% 추가
		int floor = DeckBuilderMap.mapFloor(depth);
		if (floor >= 6) {
			combatP += mysteryVisitsThisAct * 2;
		}

		// 비(非)이벤트 합이 100% 초과 시 보물→상점 순으로 삭감
		int nonEvent = combatP + shopP + treasureP;
		if (nonEvent > 100) {
			int excess = nonEvent - 100;
			int tCut = Math.min(excess, Math.max(0, treasureP));
			treasureP -= tCut;
			excess -= tCut;
			if (excess > 0) shopP = Math.max(0, shopP - excess);
		}

		int roll = Random.Int(100);
		int type;
		if      (roll < combatP)                          type = DeckBuilderMap.COMBAT;
		else if (roll < combatP + shopP)                  type = DeckBuilderMap.SHOP;
		else if (roll < combatP + shopP + treasureP)      type = DeckBuilderMap.TREASURE;
		else                                               type = DeckBuilderMap.EVENT;

		// 천장 업데이트: 발생한 타입은 리셋, 나머지는 기본값만큼 증가
		mysteryVisitsThisAct++;
		switch (type) {
			case DeckBuilderMap.EVENT:
				mysteryCombatBonus   += 10;
				mysteryShopBonus     += 3;
				mysteryTreasureBonus += 2;
				break;
			case DeckBuilderMap.COMBAT:
				mysteryCombatBonus   = 0;
				mysteryShopBonus     += 3;
				mysteryTreasureBonus += 2;
				break;
			case DeckBuilderMap.SHOP:
				mysteryShopBonus     = 0;
				mysteryCombatBonus   += 10;
				mysteryTreasureBonus += 2;
				break;
			case DeckBuilderMap.TREASURE:
				mysteryTreasureBonus = 0;
				mysteryCombatBonus   += 10;
				mysteryShopBonus     += 3;
				break;
		}
		mysteryPrevWasShop = (type == DeckBuilderMap.SHOP);

		mysteryResolvedDepth = depth;
		mysteryResolvedPath  = path;
		mysteryResolvedType  = type;
		return type;
	}

	/** 미지가 아닌 일반 노드 진입 시 호출 — 상점 직후 억제 추적용 */
	public static void notifyNodeEntered(int type) {
		mysteryPrevWasShop = (type == DeckBuilderMap.SHOP);
	}

	public static void storeInBundle(Bundle bundle) {
		DeckBuilderRunBundle.store(bundle);
	}

	public static void restoreFromBundle(Bundle bundle) {
		DeckBuilderRunBundle.restore(bundle);
	}

	public static HeroClass heroClass() {
		return Dungeon.hero == null ? null : Dungeon.hero.heroClass;
	}

}
