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
		DeckBuilderRun.pendingCardTransform = false;
		DeckBuilderRun.pendingNeutralDiscover = false;
		DeckBuilderRun.pendingCardReward = false;
		DeckBuilderRun.pendingCardRemove = false;
		DeckBuilderRun.pendingCardUpgrade = false;
		DeckBuilderRun.pendingOtherClassCardReward = 0;
		DeckBuilderRun.pendingRareCardChoice = 0;
		DeckBuilderRun.pendingCardRemoveCount = 0;
		DeckBuilderRun.fishingRodProgress = 0;
		DeckBuilderRun.upgradedCardRewardCount = 0;
		DeckBuilderRun.firstTreasureEmpty = false;
		DeckBuilderRun.lastEliteEncounter = -1;
		DeckBuilderRun.tutorialMode = Dungeon.selectedMode == Dungeon.GameMode.DECKBUILDER_TUTORIAL;
		DeckBuilderRun.tutorialStep = 0;
		DeckBuilderRun.tutorialMapMessageShown = false;
		DeckBuilderRun.clearShop();
		DeckBuilderRun.clearTreasure();
		DeckBuilderRun.clearRest();

		if (DeckBuilderRun.tutorialMode) {
			addTutorialDeck();
			DeckBuilderRun.startingRelicChosen = true;
			DeckRunInventory.addPotion(DeckBuilderRun.potions, DeckPotion.STRENGTH, DeckBuilderRun.MAX_POTION_SLOTS);
		} else {
			DeckStartingProfile.addStartingDeck(DeckBuilderRun.deck, heroClass);
			addStartingPotions();

			if (heroClass == HeroClass.WARRIOR) {
				DeckRunInventory.addRelic(DeckBuilderRun.relics, DeckRelic.WAVE_RUSH);
			}
		}
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
			for (int i = 0; i < DeckBuilderRun.startingRelicChoices.length; i++) {
				int id = DeckBuilderRun.startingRelicChoices[i];
				DeckRelic relic = DeckRelic.byId(id);
				DeckRelicType expected = (i == STARTING_RELIC_CHOICE_COUNT - 1)
						? DeckRelicType.PENALTY_STARTER : DeckRelicType.STARTER;
				if (seen.contains(id) || relic.type != expected) {
					valid = false;
					break;
				}
				seen.add(id);
			}
		}
		if (valid) return;

		DeckBuilderRun.startingRelicChoices = new int[STARTING_RELIC_CHOICE_COUNT];
		ArrayList<Integer> starterSeen = new ArrayList<>();
		ArrayList<Integer> penaltySeen = new ArrayList<>();
		for (int i = 0; i < STARTING_RELIC_CHOICE_COUNT; i++) {
			if (i == STARTING_RELIC_CHOICE_COUNT - 1) {
				DeckRelic relic = DeckRelic.randomPenaltyStarterAvailable(penaltySeen);
				int id = relic == null ? DeckRelic.CURSED_PEARL.ordinal() : relic.ordinal();
				penaltySeen.add(id);
				DeckBuilderRun.startingRelicChoices[i] = id;
			} else {
				DeckRelic relic = DeckRelic.randomStarterAvailable(starterSeen);
				int id = relic == null ? DeckRelic.NEW_LEAF.ordinal() : relic.ordinal();
				starterSeen.add(id);
				DeckBuilderRun.startingRelicChoices[i] = id;
			}
		}
	}

	public static void chooseStartingRelic(DeckRelic relic) {
		if (DeckBuilderRun.startingRelicChosen) return;
		DeckRunInventory.addRelic(DeckBuilderRun.relics, relic);
		DeckBuilderRun.startingRelicChosen = true;
		DeckBuilderRun.startingRelicChoices = null;
	}

	private static void addStartingPotions() {
		DeckRunInventory.addPotion(DeckBuilderRun.potions, DeckPotion.DUPLICATOR, DeckBuilderRun.MAX_POTION_SLOTS);
		DeckRunInventory.addPotion(DeckBuilderRun.potions, DeckPotion.DISTILLED_CHAOS, DeckBuilderRun.MAX_POTION_SLOTS);
		DeckRunInventory.addPotion(DeckBuilderRun.potions, DeckPotion.OROBIC_ACID, DeckBuilderRun.MAX_POTION_SLOTS);
	}

	private static void addTutorialDeck() {
		// 튜토리얼 고정 덱: ATTACK(STAFF/BASH) + SKILL(GUARD) + POWER(IGNITE) 각 타입 체험
		DeckRunInventory.addCard(DeckBuilderRun.deck, DeckCard.STAFF);
		DeckRunInventory.addCard(DeckBuilderRun.deck, DeckCard.GUARD);
		DeckRunInventory.addCard(DeckBuilderRun.deck, DeckCard.BASH);
		DeckRunInventory.addCard(DeckBuilderRun.deck, DeckCard.IGNITE);
		DeckRunInventory.addCard(DeckBuilderRun.deck, DeckCard.STAFF);
	}
}
