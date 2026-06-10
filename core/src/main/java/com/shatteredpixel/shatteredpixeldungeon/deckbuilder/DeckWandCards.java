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

import java.util.ArrayList;
import java.util.EnumMap;

public class DeckWandCards {

	private static final EnumMap<DeckCard, WandProfile> PROFILES = new EnumMap<>(DeckCard.class);

	static {
		register(DeckCard.MAGIC_MISSILE_WAND, 3, 4, TriggerTiming.TURN_START);
	}

	public static boolean isWand(int cardCode) {
		return maxCharge(cardCode) > 0;
	}

	public static int maxCharge(int cardCode) {
		return maxCharge(DeckCard.byCode(cardCode), cardCode);
	}

	public static int maxCharge(DeckCard card) {
		return maxCharge(card, card.code());
	}

	public static int maxCharge(DeckCard card, int cardCode) {
		WandProfile profile = PROFILES.get(card);
		if (profile == null) return 0;
		return DeckCardCode.upgradeLevel(cardCode) > 0 ? profile.upgradedMaxCharge : profile.maxCharge;
	}

	public static void triggerTurnStartWands(DeckBuilderCombat combat) {
		triggerWands(combat, TriggerTiming.TURN_START, combat.lastAutoPlayResults);
	}

	public static void triggerTurnEndWands(DeckBuilderCombat combat) {
		triggerWands(combat, TriggerTiming.TURN_END, combat.lastTurnEndAutoPlayResults);
	}

	public static void fireWand(DeckBuilderCombat combat, int wandCode, DeckPlayResult.Builder result) {
		DeckCard wand = DeckCard.byCode(wandCode);
		for (int i = 0; i < maxCharge(wandCode); i++) {
			applyWandEffects(combat, wand, wandCode, result);
		}
	}

	private static void triggerWands(DeckBuilderCombat combat, TriggerTiming timing, ArrayList<DeckPlayResult> results) {
		ArrayList<Integer> handAtTrigger = new ArrayList<>(combat.hand);
		for (int code : handAtTrigger) {
			triggerWand(combat, code, timing, results);
		}
	}

	private static void triggerWand(DeckBuilderCombat combat, int code, TriggerTiming timing, ArrayList<DeckPlayResult> results) {
		if (!isWand(code)) return;
		WandProfile profile = PROFILES.get(DeckCard.byCode(code));
		if (profile == null || profile.timing != timing) return;
		int handIndex = combat.hand.indexOf(code);
		if (handIndex < 0) return;

		DeckCard wand = DeckCard.byCode(code);
		DeckPlayResult.Builder result = new DeckPlayResult.Builder(wand);
		applyWandEffects(combat, wand, code, result);

		int charge = DeckCardCode.currentCharge(code) - 1;
		if (charge <= 0) {
			combat.hand.remove(handIndex);
			result.draw += combat.exhaustCard(code);
			result.exhausted = true;
		} else {
			combat.hand.set(handIndex, DeckCardCode.withCharge(code, charge));
		}

		results.add(result.build());
	}

	private static void applyWandEffects(DeckBuilderCombat combat, DeckCard wand, int wandCode, DeckPlayResult.Builder result) {
		for (DeckCardEffect effect : wand.effects(wandCode)) {
			effect.apply(new DeckCardPlayContext(combat, wand, wandCode, wandCode, -1, false, false, false, result, -1));
		}
	}

	private static void register(DeckCard card, int maxCharge, int upgradedMaxCharge, TriggerTiming timing) {
		PROFILES.put(card, new WandProfile(maxCharge, upgradedMaxCharge, timing));
	}

	private enum TriggerTiming {
		TURN_START,
		TURN_END
	}

	private static class WandProfile {

		private final int maxCharge;
		private final int upgradedMaxCharge;
		private final TriggerTiming timing;

		private WandProfile(int maxCharge, int upgradedMaxCharge, TriggerTiming timing) {
			this.maxCharge = maxCharge;
			this.upgradedMaxCharge = upgradedMaxCharge;
			this.timing = timing;
		}
	}
}
