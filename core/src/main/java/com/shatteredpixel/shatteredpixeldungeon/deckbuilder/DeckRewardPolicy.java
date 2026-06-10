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
import com.watabou.utils.Random;

import java.util.ArrayList;

public class DeckRewardPolicy {

	public static CardReward rollCardChoices(int nodeType, int cardRareOffset, HeroClass heroClass) {
		DeckCard[] choices = new DeckCard[4];
		boolean rareSeen = false;
		int commonSeen = 0;
		for (int i = 0; i < choices.length; i++) {
			DeckCardRarity rarity = rollCardRarity(nodeType, cardRareOffset);
			DeckCard card = randomCard(rarity, heroClass, classSlot(i));
			int guard = 0;
			while (duplicate(choices, i, card) && guard++ < 20) {
				card = randomCard(rarity, heroClass, classSlot(i));
			}
			choices[i] = card == null ? DeckCard.rewardFallback(heroClass) : card;
			if (choices[i].rarity == DeckCardRarity.RARE) rareSeen = true;
			if (choices[i].rarity == DeckCardRarity.COMMON) commonSeen++;
		}
		if (nodeType == DeckBuilderMap.ELITE && DeckBuilderRun.hasRelic(DeckRelic.AUTUMN_LEAVES) && !rareSeen) {
			int idx = Random.Int(choices.length);
			DeckCard card = randomCard(DeckCardRarity.RARE, heroClass, classSlot(idx));
			int guard = 0;
			while (duplicate(choices, choices.length, card) && guard++ < 20) {
				card = randomCard(DeckCardRarity.RARE, heroClass, classSlot(idx));
			}
			choices[idx] = card == null ? DeckCard.rewardFallback(heroClass) : card;
			rareSeen = choices[idx].rarity == DeckCardRarity.RARE;
		}
		return new CardReward(choices, rareSeen ? -5 : cardRareOffset + commonSeen);
	}

	public static DeckCard[] commonChoices(HeroClass heroClass, int count) {
		DeckCard[] choices = new DeckCard[count];
		for (int i = 0; i < choices.length; i++) {
			choices[i] = randomCard(DeckCardRarity.COMMON, heroClass, -1);
		}
		return choices;
	}

	public static int rollGold(int nodeType) {
		if (nodeType == DeckBuilderMap.BOSS) return 95 + Random.Int(11);
		if (nodeType == DeckBuilderMap.ELITE) return 25 + Random.Int(11);
		return 10 + Random.Int(11);
	}

	public static DeckRelic[] rollRelics(int nodeType, boolean blackStar) {
		int count;
		if (nodeType == DeckBuilderMap.ELITE) {
			count = blackStar ? 2 : 1;
		} else if (nodeType == DeckBuilderMap.BOSS && DeckBuilderRun.hasRelic(DeckRelic.LAVA_ROCK)) {
			count = 2;
		} else {
			return new DeckRelic[0];
		}
		ArrayList<DeckRelic> result = new ArrayList<>();
		for (int i = 0; i < count; i++) {
			DeckRelic relic = DeckRelic.randomAvailable(rollRelicRarity());
			if (relic != null && !result.contains(relic)) result.add(relic);
		}
		return result.toArray(new DeckRelic[0]);
	}

	public static PotionReward rollPotion(int nodeType, int potionDropChance) {
		DeckPotionPolicy.PotionReward reward = DeckPotionPolicy.rollRewardPotion(nodeType, potionDropChance);
		return new PotionReward(reward.potion, reward.nextPotionDropChance);
	}

	public static int rollTreasureChest() {
		int roll = Random.Int(100);
		if (roll < 50) return 0;
		if (roll < 83) return 1;
		return 2;
	}

	public static DeckRelicRarity rollTreasureRarity(int chest) {
		int roll = Random.Int(100);
		if (chest == 0) return roll < 75 ? DeckRelicRarity.COMMON : DeckRelicRarity.UNCOMMON;
		if (chest == 1) {
			if (roll < 35) return DeckRelicRarity.COMMON;
			if (roll < 85) return DeckRelicRarity.UNCOMMON;
			return DeckRelicRarity.RARE;
		}
		return roll < 75 ? DeckRelicRarity.UNCOMMON : DeckRelicRarity.RARE;
	}

	public static DeckRelicRarity rollRelicRarity() {
		int roll = Random.Int(100);
		if (roll < 50) return DeckRelicRarity.COMMON;
		if (roll < 83) return DeckRelicRarity.UNCOMMON;
		return DeckRelicRarity.RARE;
	}

	public static DeckCard randomCard(DeckCardRarity rarity, HeroClass heroClass) {
		return randomCard(rarity, heroClass, -1);
	}

	private static int classSlot(int index) {
		if (index == 0) return 100;
		if (index == 1) return 60;
		if (index == 2) return 20;
		return 0;
	}

	private static DeckCardRarity rollCardRarity(int nodeType, int cardRareOffset) {
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

	private static DeckCard randomCard(DeckCardRarity rarity, HeroClass heroClass, int classChance) {
		boolean classOnly = classChance >= 100;
		boolean neutralOnly = classChance == 0;
		boolean classPool = classOnly || (!neutralOnly && classChance > 0 && Random.Int(100) < classChance);
		DeckCard[] pool = DeckCard.rewardPool(heroClass, classPool, !classPool);
		ArrayList<DeckCard> rarityPool = new ArrayList<>();
		for (DeckCard card : pool) {
			if (card.rarity == rarity) rarityPool.add(card);
		}
		if (rarityPool.isEmpty() && classPool && !classOnly) {
			return randomCard(rarity, heroClass, 0);
		}
		if (rarityPool.isEmpty() && !classPool && !neutralOnly) {
			return randomCard(rarity, heroClass, 100);
		}
		if (rarityPool.isEmpty()) return pool.length == 0 ? DeckCard.rewardFallback(heroClass) : pool[Random.Int(pool.length)];
		return rarityPool.get(Random.Int(rarityPool.size()));
	}

	public static class CardReward {
		public final DeckCard[] choices;
		public final int nextCardRareOffset;

		public CardReward(DeckCard[] choices, int nextCardRareOffset) {
			this.choices = choices;
			this.nextCardRareOffset = nextCardRareOffset;
		}
	}

	public static class PotionReward {
		public final DeckPotion potion;
		public final int nextPotionDropChance;

		public PotionReward(DeckPotion potion, int nextPotionDropChance) {
			this.potion = potion;
			this.nextPotionDropChance = nextPotionDropChance;
		}
	}
}
