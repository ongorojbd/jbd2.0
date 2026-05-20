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

import java.util.HashMap;
import java.util.Map;

public class DeckEnemyIntent {

	private static final Map<Integer, DeckEnemyIntent> TABLE = new HashMap<>();

	public static final DeckEnemyIntent SLIMY_INJECT = register(new DeckEnemyIntent(
			DeckBuilderCombat.RESULT_SLIMY_INJECT,
			(combat, enemy, remainingBlock) -> {
				combat.discardPile.add(DeckCard.SLIMY.code());
				combat.lastEnemyActions.add(new DeckBuilderCombat.EnemyAction(combat.enemyIndex(enemy), 0, true, "점액투성이", false, DeckCard.SLIMY, 1));
				return new TurnResult(remainingBlock, 0, true);
			},
			enemy -> "예고: 점액투성이"));

	public static final DeckEnemyIntent AGE_DOWN = register(new DeckEnemyIntent(
			DeckBuilderCombat.RESULT_AGE_DOWN,
			(combat, enemy, remainingBlock) -> {
				combat.playerDamageReduction = Math.max(combat.playerDamageReduction, 30);
				combat.lastEnemyActions.add(new DeckBuilderCombat.EnemyAction(combat.enemyIndex(enemy), 0, false, "유아화"));
				return TurnResult.noChange(remainingBlock);
			},
			enemy -> "예고: 유아화"));

	public static final DeckEnemyIntent STRENGTH_7 = register(strengthIntent(DeckBuilderCombat.RESULT_STRENGTH_7, 7, "공격력"));
	public static final DeckEnemyIntent STRENGTH_2 = register(strengthIntent(DeckBuilderCombat.RESULT_STRENGTH_2, 2, "공격력"));

	public static final DeckEnemyIntent ATTACK_6_BLOCK_5 = register(new DeckEnemyIntent(
			DeckBuilderCombat.RESULT_ATTACK_6_BLOCK_5,
			(combat, enemy, remainingBlock) -> {
				AttackResult attack = combat.performEnemyAttack(enemy, 6, remainingBlock, "방어막 +5");
				enemy.block += 5;
				return attack.toTurnResult(false);
			},
			enemy -> "예고: " + damageText(enemy, 6) + " 피해 + 방어 5"));

	public static final DeckEnemyIntent MASSACRE = register(new DeckEnemyIntent(
			DeckBuilderCombat.RESULT_MASSACRE,
			(combat, enemy, remainingBlock) -> {
				AttackSeriesResult result = attackSeries(combat, enemy, remainingBlock, 3, 3, "[대학살!]");
				enemy.thorns = Math.max(0, enemy.thorns - 2);
				return result.toTurnResult(false);
			},
			enemy -> "예고: 3x" + damageText(enemy, 3) + " 피해"));

	public static final DeckEnemyIntent TOWER_NEEDLE = register(new DeckEnemyIntent(
			DeckBuilderCombat.RESULT_TOWER_NEEDLE,
			(combat, enemy, remainingBlock) -> {
				enemy.thorns += 2;
				combat.lastEnemyActions.add(new DeckBuilderCombat.EnemyAction(combat.enemyIndex(enemy), 0, false, "[타워 니들]"));
				return TurnResult.noChange(remainingBlock);
			},
			enemy -> "예고: 반격 +2"));

	public static final DeckEnemyIntent PRESSURIZE = register(strengthIntent(DeckBuilderCombat.RESULT_PRESSURIZE, 4, "힘"));

	public static final DeckEnemyIntent FLAME_TACKLE_BIG = register(flameTackle(
			DeckBuilderCombat.RESULT_FLAME_TACKLE_BIG, 16, 2));

	public static final DeckEnemyIntent LICK_BIG = register(blockReduction(
			DeckBuilderCombat.RESULT_LICK_BIG, 2));

	public static final DeckEnemyIntent FLAME_TACKLE_MEDIUM = register(flameTackle(
			DeckBuilderCombat.RESULT_FLAME_TACKLE_MEDIUM, 8, 1));

	public static final DeckEnemyIntent LICK_MEDIUM = register(blockReduction(
			DeckBuilderCombat.RESULT_LICK_MEDIUM, 1));

	public static final DeckEnemyIntent WINDUP_PUNCH = register(multiAttack(
			DeckBuilderCombat.RESULT_WINDUP_PUNCH, 3, 2, "감아치기"));

	public static final DeckEnemyIntent LASH = register(multiAttack(
			DeckBuilderCombat.RESULT_LASH, 2, 3, "후려치기"));

	public static final DeckEnemyIntent TACKLE = register(new DeckEnemyIntent(
			DeckBuilderCombat.RESULT_TACKLE,
			(combat, enemy, remainingBlock) -> {
				AttackResult attack = combat.performEnemyAttack(enemy, 9, remainingBlock, "손상 +1");
				combat.playerDamageReduction = Math.max(combat.playerDamageReduction, 25);
				return attack.toTurnResult(false);
			},
			enemy -> "예고: " + damageText(enemy, 9) + " 피해 + 손상"));

	public static final DeckEnemyIntent CHARGE_UP = register(strengthIntent(DeckBuilderCombat.RESULT_CHARGE_UP, 2, "힘"));

	public static final DeckEnemyIntent REPEATER_BLAST = register(new DeckEnemyIntent(
			DeckBuilderCombat.RESULT_REPEATER_BLAST,
			(combat, enemy, remainingBlock) -> {
				AttackResult attack = combat.performEnemyAttack(enemy, 7, remainingBlock, "힘 +2");
				enemy.strength += 2;
				return attack.toTurnResult(false);
			},
			enemy -> "예고: " + damageText(enemy, 7) + " 피해 + 힘"));

	public static final DeckEnemyIntent EXPEL_BLAST = register(multiAttack(
			DeckBuilderCombat.RESULT_EXPEL_BLAST, 2, 5, "방출 폭발"));

	public static final DeckEnemyIntent SUBMERGE = register(new DeckEnemyIntent(
			DeckBuilderCombat.RESULT_SUBMERGE,
			(combat, enemy, remainingBlock) -> {
				enemy.block += 15;
				combat.lastEnemyActions.add(new DeckBuilderCombat.EnemyAction(combat.enemyIndex(enemy), 0, false, "방어도 +15"));
				return TurnResult.noChange(remainingBlock);
			},
			enemy -> "예고: 방어도 +15"));

	public final int id;
	private final IntentAction action;
	private final IntentText text;

	private DeckEnemyIntent(int id, IntentAction action, IntentText text) {
		this.id = id;
		this.action = action;
		this.text = text;
	}

	public static DeckEnemyIntent byId(int id) {
		DeckEnemyIntent intent = TABLE.get(id);
		return intent == null ? attack(id) : intent;
	}

	public TurnResult apply(DeckBuilderCombat combat, DeckCombatEnemy enemy, int remainingBlock) {
		return action.apply(combat, enemy, remainingBlock);
	}

	public String textFor(DeckCombatEnemy enemy) {
		return text.text(enemy);
	}

	public static String text(DeckCombatEnemy enemy) {
		return byId(enemy.intent).textFor(enemy);
	}

	private static DeckEnemyIntent register(DeckEnemyIntent intent) {
		DeckEnemyIntent previous = TABLE.put(intent.id, intent);
		if (previous != null) {
			throw new IllegalStateException("Duplicate deck enemy intent id: " + intent.id);
		}
		return intent;
	}

	private static DeckEnemyIntent attack(int damage) {
		return new DeckEnemyIntent(damage, DeckEnemyIntent::applyAttack, DeckEnemyIntent::attackText);
	}

	private static DeckEnemyIntent strengthIntent(int id, int amount, String label) {
		return new DeckEnemyIntent(
				id,
				(combat, enemy, remainingBlock) -> {
					enemy.strength += amount;
					combat.lastEnemyActions.add(new DeckBuilderCombat.EnemyAction(combat.enemyIndex(enemy), 0, false, label + " +" + amount));
					return TurnResult.noChange(remainingBlock);
				},
				enemy -> "예고: " + label + " +" + amount);
	}

	private static DeckEnemyIntent flameTackle(int id, int damage, int slimyCount) {
		return new DeckEnemyIntent(
				id,
				(combat, enemy, remainingBlock) -> {
					AttackResult attack = combat.performEnemyAttack(enemy, damage, remainingBlock, "점액투성이 +" + slimyCount);
					for (int i = 0; i < slimyCount; i++) {
						combat.discardPile.add(DeckCard.SLIMY.code());
					}
					combat.lastEnemyActions.get(combat.lastEnemyActions.size() - 1).setShuffle(DeckCard.SLIMY, slimyCount);
					return attack.toTurnResult(true);
				},
				enemy -> "예고: " + damageText(enemy, damage) + " 피해 + 점액 " + slimyCount);
	}

	private static DeckEnemyIntent blockReduction(int id, int amount) {
		return new DeckEnemyIntent(
				id,
				(combat, enemy, remainingBlock) -> {
					combat.playerBlockReduction += amount;
					combat.lastEnemyActions.add(new DeckBuilderCombat.EnemyAction(combat.enemyIndex(enemy), 0, false, "방어력 저하 +" + amount));
					return TurnResult.noChange(remainingBlock);
				},
				enemy -> "예고: 방어력 저하 " + amount);
	}

	private static DeckEnemyIntent multiAttack(int id, int hits, int baseDamage, String firstLabel) {
		return new DeckEnemyIntent(
				id,
				(combat, enemy, remainingBlock) -> attackSeries(combat, enemy, remainingBlock, hits, baseDamage, firstLabel).toTurnResult(false),
				enemy -> "예고: " + hits + "x" + damageText(enemy, baseDamage) + " 피해");
	}

	private static TurnResult applyAttack(DeckBuilderCombat combat, DeckCombatEnemy enemy, int remainingBlock) {
		String label = enemy.kind == DeckEnemy.TOWER_OF_GREY && enemy.intent == 7 ? "[혀 뜯어내기]" : null;
		return combat.performEnemyAttack(enemy, enemy.intent, remainingBlock, label).toTurnResult(false);
	}

	private static String attackText(DeckCombatEnemy enemy) {
		if (enemy.intent > 0 && enemy.strength != 0) {
			return "예고: " + damageText(enemy, enemy.intent) + " 피해";
		}
		return "예고: " + enemy.intent + " 피해";
	}

	private static int damageText(DeckCombatEnemy enemy, int baseDamage) {
		return Math.max(0, baseDamage + enemy.strength);
	}

	private static AttackSeriesResult attackSeries(DeckBuilderCombat combat, DeckCombatEnemy enemy, int remainingBlock, int hits, int baseDamage, String firstLabel) {
		int damageTaken = 0;
		for (int hit = 0; hit < hits; hit++) {
			AttackResult attack = combat.performEnemyAttack(enemy, baseDamage, remainingBlock, hit == 0 ? firstLabel : null);
			remainingBlock = attack.remainingBlock;
			damageTaken += attack.damageTaken;
		}
		return new AttackSeriesResult(remainingBlock, damageTaken);
	}

	private interface IntentAction {
		TurnResult apply(DeckBuilderCombat combat, DeckCombatEnemy enemy, int remainingBlock);
	}

	private interface IntentText {
		String text(DeckCombatEnemy enemy);
	}

	public static class TurnResult {
		public final int remainingBlock;
		public final int damageTaken;
		public final boolean injected;

		public TurnResult(int remainingBlock, int damageTaken, boolean injected) {
			this.remainingBlock = remainingBlock;
			this.damageTaken = damageTaken;
			this.injected = injected;
		}

		public static TurnResult noChange(int remainingBlock) {
			return new TurnResult(remainingBlock, 0, false);
		}
	}

	public static class AttackResult {
		public final int remainingBlock;
		public final int damageTaken;

		public AttackResult(int remainingBlock, int damageTaken) {
			this.remainingBlock = remainingBlock;
			this.damageTaken = damageTaken;
		}

		public TurnResult toTurnResult(boolean injected) {
			return new TurnResult(remainingBlock, damageTaken, injected);
		}
	}

	private static class AttackSeriesResult {
		private final int remainingBlock;
		private final int damageTaken;

		private AttackSeriesResult(int remainingBlock, int damageTaken) {
			this.remainingBlock = remainingBlock;
			this.damageTaken = damageTaken;
		}

		private TurnResult toTurnResult(boolean injected) {
			return new TurnResult(remainingBlock, damageTaken, injected);
		}
	}
}
