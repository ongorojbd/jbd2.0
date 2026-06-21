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

public class DeckCardPool {

	public static boolean isClassCard(DeckCard card) {
		return card != null && card.deckClass != null;
	}

	public static boolean isNeutralCard(DeckCard card) {
		return card != null && card.deckClass == null;
	}

	public static boolean isStatusOrCurse(DeckCard card) {
		return isStatus(card) || isCurse(card) || isQuest(card);
	}

	public static boolean isStatus(DeckCard card) {
		return card != null && card.type == DeckCardType.STATUS;
	}

	public static boolean isCurse(DeckCard card) {
		return card != null && card.type == DeckCardType.CURSE;
	}

	public static boolean isQuest(DeckCard card) {
		return card != null && card.type == DeckCardType.QUEST;
	}

	public static boolean isRewardCard(DeckCard card, HeroClass heroClass, boolean classOnly, boolean neutralOnly) {
		if (card == null || !card.reward) return false;
		if (DeckStartingProfile.isStartingCard(card, heroClass)) return false;
		boolean classCard = isClassCard(card);
		if (classOnly && card.deckClass != heroClass) return false;
		if (neutralOnly && classCard) return false;
		return classOnly || neutralOnly || !classCard || card.deckClass == heroClass;
	}

	public static DeckCard[] rewardPool() {
		return rewardPool(null, false, false);
	}

	public static DeckCard[] rewardPool(HeroClass heroClass, boolean classOnly, boolean neutralOnly) {
		ArrayList<DeckCard> pool = new ArrayList<>();
		for (DeckCard card : DeckCard.values()) {
			if (isRewardCard(card, heroClass, classOnly, neutralOnly)) {
				pool.add(card);
			}
		}
		return pool.toArray(new DeckCard[0]);
	}

	public static DeckCard rewardFallback(HeroClass heroClass) {
		DeckCard[] pool = rewardPool(heroClass, false, false);
		if (pool.length > 0) return pool[0];
		pool = rewardPool(null, false, false);
		return pool.length > 0 ? pool[0] : DeckCard.STRIKE;
	}
}
