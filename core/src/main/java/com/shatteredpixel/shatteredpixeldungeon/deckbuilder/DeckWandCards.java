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

import com.watabou.utils.Random;

public class DeckWandCards {

	private static final EnumMap<DeckCard, WandProfile> PROFILES = new EnumMap<>(DeckCard.class);

	static {
		register(DeckCard.MAGIC_MISSILE_WAND, 3, 4, TriggerTiming.TURN_START);
		register(DeckCard.ENERGY_WAND, 2, 3, TriggerTiming.TURN_START);
		register(DeckCard.DRAW_WAND, 2, 3, TriggerTiming.TURN_START);
		register(DeckCard.BARRIER_WAND, 4, 5, TriggerTiming.TURN_START);
		register(DeckCard.ENHANCEMENT_WAND, 3, 3, TriggerTiming.TURN_END);
		register(DeckCard.MAGICIANS_WAND, 3, 4, TriggerTiming.TURN_END);
		register(DeckCard.HORUS_WAND, 2, 3, TriggerTiming.TURN_END);
		register(DeckCard.HEAVENS_WAND, 3, 4, TriggerTiming.TURN_END);
		register(DeckCard.SOFT_WAND, 3, 4, TriggerTiming.TURN_START);
		register(DeckCard.GOLD_EXPERIENCE_WAND, 2, 3, TriggerTiming.TURN_START);
		register(DeckCard.TUSK2_WAND, 3, 4, TriggerTiming.TURN_END);
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
		if (wand == DeckCard.ENHANCEMENT_WAND) {
			int stored = DeckCardCode.auxValue(wandCode) + maxCharge(wandCode);
			damageRandomEnemy(combat, stored * wand.damage(wandCode), result);
			return;
		}
		applyWandEffects(combat, wand, wandCode, result);
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

		int charge = DeckCardCode.currentCharge(code) - 1;
		if (wand == DeckCard.ENHANCEMENT_WAND) {
			int stored = DeckCardCode.auxValue(code) + 1;
			if (charge <= 0) {
				damageRandomEnemy(combat, stored * wand.damage(code), result);
			} else {
				code = DeckCardCode.withAuxValue(code, stored);
			}
		} else {
			applyWandEffects(combat, wand, code, result);
		}
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
		if (applySpecialWandEffect(combat, wand, wandCode, result)) return;
		for (DeckCardEffect effect : wand.effects(wandCode)) {
			effect.apply(new DeckCardPlayContext(combat, wand, wandCode, wandCode, -1, false, false, false, result, -1));
		}
	}

	private static boolean applySpecialWandEffect(DeckBuilderCombat combat, DeckCard wand, int wandCode, DeckPlayResult.Builder result) {
		if (wand == DeckCard.ENERGY_WAND) {
			combat.energy = Math.min(DeckBuilderRun.MAX_ENERGY_CAP, combat.energy + 1);
			return true;
		}
		if (wand == DeckCard.DRAW_WAND) {
			if (combat.draw(1)) result.draw++;
			return true;
		}
		if (wand == DeckCard.BARRIER_WAND) {
			result.block += combat.gainBlock(4);
			damageRandomEnemy(combat, 2, result);
			return true;
		}
		if (wand == DeckCard.MAGICIANS_WAND) {
			for (DeckCombatEnemy enemy : combat.aliveEnemies()) {
				int dealt = combat.damageEnemy(enemy, 5, false);
				result.addAttackHit(combat.enemyIndex(enemy), dealt);
			}
			return true;
		}
		if (wand == DeckCard.HORUS_WAND) {
			DeckCombatEnemy enemy = randomEnemy(combat);
			if (enemy != null) {
				int attackDown = 0;
				if (combat.applyEnemyDebuff(enemy)) {
					enemy.attackDown++;
					attackDown = 1;
				}
				int dealt = combat.damageEnemy(enemy, 2, false);
				result.addHit(combat.enemyIndex(enemy), dealt, 0, attackDown);
			}
			return true;
		}
		if (wand == DeckCard.HEAVENS_WAND) {
			DeckCombatEnemy enemy = randomEnemy(combat);
			if (enemy != null && combat.applyEnemyDebuff(enemy)) enemy.vulnerable++;
			return true;
		}
		if (wand == DeckCard.SOFT_WAND) {
			result.heal += combat.healPlayer(2);
			return true;
		}
		if (wand == DeckCard.GOLD_EXPERIENCE_WAND) {
			combat.addRandomZeroCostExhaustCardsToHand(2);
			return true;
		}
		if (wand == DeckCard.TUSK2_WAND) {
			combat.addToDrawPile(DeckCard.ROTATING_NAIL.code(), 2, true);
			result.addShuffle(DeckCard.ROTATING_NAIL, 2);
			return true;
		}
		return false;
	}

	private static void damageRandomEnemy(DeckBuilderCombat combat, int damage, DeckPlayResult.Builder result) {
		DeckCombatEnemy enemy = randomEnemy(combat);
		if (enemy == null) return;
		int dealt = combat.damageEnemy(enemy, damage, false);
		result.addAttackHit(combat.enemyIndex(enemy), dealt);
	}

	private static DeckCombatEnemy randomEnemy(DeckBuilderCombat combat) {
		ArrayList<DeckCombatEnemy> alive = combat.aliveEnemies();
		return alive.isEmpty() ? null : alive.get(Random.Int(alive.size()));
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
