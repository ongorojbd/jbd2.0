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

import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class DeckBuilderCombat {

	public static final int RESULT_SLIMY_INJECT = -1;
	public static final int RESULT_AGE_DOWN = -2;
	public static final int RESULT_STRENGTH_7 = -3;
	public static final int RESULT_STRENGTH_2 = -4;
	public static final int RESULT_ATTACK_6_BLOCK_5 = -5;
	public static final int RESULT_MASSACRE = -6;
	public static final int RESULT_TOWER_NEEDLE = -7;

	private static final String NODE_TYPE = "node_type";
	private static final String DEPTH = "depth";
	private static final String MAX_ENERGY = "max_energy";
	private static final String HAND_SIZE = "hand_size";
	private static final String MAX_HAND_SIZE = "max_hand_size";
	private static final String TARGET_INDEX = "target_index";
	private static final String ENERGY = "energy";
	private static final String BLOCK = "block";
	private static final String TURN = "turn";
	private static final String PLAYER_STRENGTH = "player_strength";
	private static final String PLAYER_TURN_STRENGTH = "player_turn_strength";
	private static final String DRAW_PILE = "draw_pile";
	private static final String HAND = "hand";
	private static final String DISCARD_PILE = "discard_pile";
	private static final String EXHAUST_PILE = "exhaust_pile";
	private static final String ENEMY_KINDS = "enemy_kinds";
	private static final String ENEMY_HT = "enemy_ht";
	private static final String ENEMY_HP = "enemy_hp";
	private static final String ENEMY_INTENTS = "enemy_intents";
	private static final String ENEMY_VULNERABLE = "enemy_vulnerable";
	private static final String ENEMY_STRENGTH = "enemy_strength";
	private static final String ENEMY_BLOCK = "enemy_block";
	private static final String ENEMY_THORNS = "enemy_thorns";
	private static final String PLAYER_DAMAGE_REDUCTION = "player_damage_reduction";
	private static final String SHIV_DAMAGE_BONUS = "shiv_damage_bonus";
	private static final String FIRST_SHIV_DAMAGE_BONUS = "first_shiv_damage_bonus";
	private static final String SHIV_RETAIN = "shiv_retain";

	public final int nodeType;
	public final int depth;
	public final int maxEnergy;
	public final int handSize;
	public final int maxHandSize;
	public final ArrayList<DeckCombatEnemy> enemies = new ArrayList<>();

	public int targetIndex;
	public int energy;
	public int block;
	public int turn;
	public int playerStrength;
	public int playerTurnStrength;
	public int playerDamageReduction;
	public int shivDamageBonus;
	public int firstShivDamageBonus;
	public boolean firstShivUsed;
	public boolean shivRetain;

	public ArrayList<Integer> drawPile = new ArrayList<>();
	public ArrayList<Integer> hand = new ArrayList<>();
	public ArrayList<Integer> discardPile = new ArrayList<>();
	public ArrayList<Integer> exhaustPile = new ArrayList<>();
	public ArrayList<EnemyAction> lastEnemyActions = new ArrayList<>();

	public DeckBuilderCombat(int nodeType, int depth, ArrayList<Integer> deck) {
		this.nodeType = nodeType;
		this.depth = Math.max(1, depth);
		this.maxEnergy = Math.min(DeckBuilderRun.MAX_ENERGY_CAP, Math.max(1, DeckBuilderRun.maxEnergy));
		this.handSize = Math.max(1, DeckBuilderRun.handSize);
		this.maxHandSize = Math.max(this.handSize, DeckBuilderRun.maxHandSize);
		for (DeckEnemy kind : DeckBuilderRun.rollEncounter(nodeType, this.depth)) {
			enemies.add(new DeckCombatEnemy(kind, this.depth));
		}
		if (enemies.isEmpty()) {
			enemies.add(new DeckCombatEnemy(DeckEnemy.forNode(nodeType), this.depth));
		}
		targetIndex = 0;
		this.turn = 0;
		this.drawPile.addAll(deck);
		shuffle(drawPile);
		startTurn();
	}

	private DeckBuilderCombat(int nodeType, int depth, int maxEnergy, int handSize, int maxHandSize) {
		this.nodeType = nodeType;
		this.depth = Math.max(1, depth);
		this.maxEnergy = Math.min(DeckBuilderRun.MAX_ENERGY_CAP, Math.max(1, maxEnergy));
		this.handSize = Math.max(1, handSize);
		this.maxHandSize = Math.max(this.handSize, maxHandSize);
	}

	public void storeInBundle(Bundle bundle) {
		bundle.put(NODE_TYPE, nodeType);
		bundle.put(DEPTH, depth);
		bundle.put(MAX_ENERGY, maxEnergy);
		bundle.put(HAND_SIZE, handSize);
		bundle.put(MAX_HAND_SIZE, maxHandSize);
		bundle.put(TARGET_INDEX, targetIndex);
		bundle.put(ENERGY, energy);
		bundle.put(BLOCK, block);
		bundle.put(TURN, turn);
		bundle.put(PLAYER_STRENGTH, playerStrength);
		bundle.put(PLAYER_TURN_STRENGTH, playerTurnStrength);
		bundle.put(PLAYER_DAMAGE_REDUCTION, playerDamageReduction);
		bundle.put(SHIV_DAMAGE_BONUS, shivDamageBonus);
		bundle.put(FIRST_SHIV_DAMAGE_BONUS, firstShivDamageBonus);
		bundle.put(SHIV_RETAIN, shivRetain);
		bundle.put(DRAW_PILE, toArray(drawPile));
		bundle.put(HAND, toArray(hand));
		bundle.put(DISCARD_PILE, toArray(discardPile));
		bundle.put(EXHAUST_PILE, toArray(exhaustPile));

		int[] enemyKinds = new int[enemies.size()];
		int[] enemyHt = new int[enemies.size()];
		int[] enemyHp = new int[enemies.size()];
		int[] enemyIntents = new int[enemies.size()];
		int[] enemyVulnerable = new int[enemies.size()];
		int[] enemyStrength = new int[enemies.size()];
		int[] enemyBlock = new int[enemies.size()];
		int[] enemyThorns = new int[enemies.size()];
		for (int i = 0; i < enemies.size(); i++) {
			DeckCombatEnemy enemy = enemies.get(i);
			enemyKinds[i] = enemy.kind.ordinal();
			enemyHt[i] = enemy.ht;
			enemyHp[i] = enemy.hp;
			enemyIntents[i] = enemy.intent;
			enemyVulnerable[i] = enemy.vulnerable;
			enemyStrength[i] = enemy.strength;
			enemyBlock[i] = enemy.block;
			enemyThorns[i] = enemy.thorns;
		}
		bundle.put(ENEMY_KINDS, enemyKinds);
		bundle.put(ENEMY_HT, enemyHt);
		bundle.put(ENEMY_HP, enemyHp);
		bundle.put(ENEMY_INTENTS, enemyIntents);
		bundle.put(ENEMY_VULNERABLE, enemyVulnerable);
		bundle.put(ENEMY_STRENGTH, enemyStrength);
		bundle.put(ENEMY_BLOCK, enemyBlock);
		bundle.put(ENEMY_THORNS, enemyThorns);
	}

	public static DeckBuilderCombat restoreFromBundle(Bundle bundle) {
		if (!bundle.contains(NODE_TYPE) || !bundle.contains(ENEMY_KINDS)) return null;

		DeckBuilderCombat combat = new DeckBuilderCombat(
				bundle.getInt(NODE_TYPE),
				bundle.getInt(DEPTH),
				bundle.contains(MAX_ENERGY) ? bundle.getInt(MAX_ENERGY) : DeckBuilderRun.STARTING_ENERGY,
				bundle.contains(HAND_SIZE) ? bundle.getInt(HAND_SIZE) : DeckBuilderRun.STARTING_HAND_SIZE,
				bundle.contains(MAX_HAND_SIZE) ? bundle.getInt(MAX_HAND_SIZE) : DeckBuilderRun.DEFAULT_MAX_HAND_SIZE);

		combat.targetIndex = bundle.getInt(TARGET_INDEX);
		combat.energy = bundle.getInt(ENERGY);
		combat.block = bundle.getInt(BLOCK);
		combat.turn = bundle.getInt(TURN);
		combat.playerStrength = bundle.getInt(PLAYER_STRENGTH);
		combat.playerTurnStrength = bundle.getInt(PLAYER_TURN_STRENGTH);
		combat.playerDamageReduction = bundle.contains(PLAYER_DAMAGE_REDUCTION) ? bundle.getInt(PLAYER_DAMAGE_REDUCTION) : 0;
		combat.shivDamageBonus = bundle.contains(SHIV_DAMAGE_BONUS) ? bundle.getInt(SHIV_DAMAGE_BONUS) : 0;
		combat.firstShivDamageBonus = bundle.contains(FIRST_SHIV_DAMAGE_BONUS) ? bundle.getInt(FIRST_SHIV_DAMAGE_BONUS) : 0;
		combat.shivRetain = bundle.getBoolean(SHIV_RETAIN);
		restoreList(combat.drawPile, bundle, DRAW_PILE);
		restoreList(combat.hand, bundle, HAND);
		restoreList(combat.discardPile, bundle, DISCARD_PILE);
		restoreList(combat.exhaustPile, bundle, EXHAUST_PILE);

		int[] enemyKinds = bundle.getIntArray(ENEMY_KINDS);
		int[] enemyHt = bundle.contains(ENEMY_HT) ? bundle.getIntArray(ENEMY_HT) : new int[0];
		int[] enemyHp = bundle.contains(ENEMY_HP) ? bundle.getIntArray(ENEMY_HP) : new int[0];
		int[] enemyIntents = bundle.contains(ENEMY_INTENTS) ? bundle.getIntArray(ENEMY_INTENTS) : new int[0];
		int[] enemyVulnerable = bundle.contains(ENEMY_VULNERABLE) ? bundle.getIntArray(ENEMY_VULNERABLE) : new int[0];
		int[] enemyStrength = bundle.contains(ENEMY_STRENGTH) ? bundle.getIntArray(ENEMY_STRENGTH) : new int[0];
		int[] enemyBlock = bundle.contains(ENEMY_BLOCK) ? bundle.getIntArray(ENEMY_BLOCK) : new int[0];
		int[] enemyThorns = bundle.contains(ENEMY_THORNS) ? bundle.getIntArray(ENEMY_THORNS) : new int[0];
		DeckEnemy[] allEnemies = DeckEnemy.values();
		for (int i = 0; i < enemyKinds.length; i++) {
			int kindIndex = enemyKinds[i];
			if (kindIndex < 0 || kindIndex >= allEnemies.length) continue;
			DeckCombatEnemy enemy = new DeckCombatEnemy(allEnemies[kindIndex], combat.depth);
			if (i < enemyHt.length) enemy.ht = enemyHt[i];
			if (i < enemyHp.length) enemy.hp = enemyHp[i];
			if (i < enemyIntents.length) enemy.intent = enemyIntents[i];
			if (i < enemyVulnerable.length) enemy.vulnerable = enemyVulnerable[i];
			if (i < enemyStrength.length) enemy.strength = enemyStrength[i];
			if (i < enemyBlock.length) enemy.block = enemyBlock[i];
			if (i < enemyThorns.length) enemy.thorns = enemyThorns[i];
			combat.enemies.add(enemy);
		}
		if (combat.enemies.isEmpty()) {
			combat.enemies.add(new DeckCombatEnemy(DeckEnemy.forNode(combat.nodeType), combat.depth));
		}
		combat.sanitizeTarget();
		return combat;
	}

	public void startTurn() {
		turn++;
		energy = maxEnergy;
		block = 0;
		playerTurnStrength = 0;
		firstShivUsed = false;
		for (DeckCombatEnemy enemy : enemies) {
			if (enemy.alive()) {
				enemy.intent = enemy.kind.nextIntent(turn, depth, enemyIndex(enemy));
			}
		}
		sanitizeTarget();
		drawTo(handSize);
	}

	public DeckPlayResult play(int handIndex) {
		if (handIndex < 0 || handIndex >= hand.size()) return DeckPlayResult.INVALID;
		int cardCode = hand.get(handIndex);
		DeckCard card = DeckCard.byCode(cardCode);
		if (card.cost(cardCode) > energy) return DeckPlayResult.INVALID;

		energy -= card.cost(cardCode);

		DeckPlayResult.Builder result = new DeckPlayResult.Builder(card);
		for (DeckCardEffect effect : card.effects(cardCode)) {
			effect.apply(this, card, cardCode, result);
		}

		hand.remove(handIndex);
		if (card == DeckCard.SHIV) {
			firstShivUsed = true;
		}
		if (card.type == DeckCardType.POWER) {
			// Powers are removed from the current combat, but not from the run deck.
		} else if (card.hasKeyword(cardCode, DeckCardKeyword.EXHAUST)) {
			exhaustPile.add(cardCode);
			result.exhausted = true;
		} else {
			discardPile.add(cardCode);
		}

		return result.build();
	}

	public int cardDamage(DeckCard card, int cardCode) {
		return cardDamage(card, cardCode, target());
	}

	public int cardDamage(DeckCard card, int cardCode, DeckCombatEnemy target) {
		int handPenaltyTotal = 0;
		for (int code : hand) {
			handPenaltyTotal += DeckCard.byCode(code).handPenalty;
		}
		int strength = card.type == DeckCardType.ATTACK ? playerStrength + playerTurnStrength : 0;
		int damage = Math.max(0, card.damage(cardCode) + strength - handPenaltyTotal);
		if (target != null && target.vulnerable > 0) {
			damage = (damage * 3 + 1) / 2;
		}
		if (playerDamageReduction > 0) {
			damage = damage * Math.max(0, 100 - playerDamageReduction) / 100;
		}
		if (card == DeckCard.SHIV) {
			damage += shivDamageBonus;
			if (!firstShivUsed) {
				damage += firstShivDamageBonus;
			}
		}
		return damage;
	}

	public void addToHand(int cardCode) {
		if (hand.size() < maxHandSize) {
			hand.add(cardCode);
		} else {
			discardPile.add(cardCode);
		}
	}

	public int damageEnemy(DeckCombatEnemy target, int damage, boolean attackCard) {
		if (target == null || damage <= 0) return 0;
		int blocked = Math.min(target.block, damage);
		target.block -= blocked;
		int dealt = Math.max(0, damage - blocked);
		target.hp = Math.max(0, target.hp - dealt);
		if (attackCard && target.thorns > 0) {
			int thornBlocked = Math.min(block, target.thorns);
			block -= thornBlocked;
			int thornDamage = Math.max(0, target.thorns - thornBlocked);
			if (thornDamage > 0) {
				DeckBuilderRun.playerHP = Math.max(0, DeckBuilderRun.playerHP - thornDamage);
			}
		}
		return dealt;
	}

	@SuppressWarnings("SuspiciousIndentation")
    public int endTurn() {
		lastEnemyActions.clear();
		ArrayList<Integer> retained = new ArrayList<>();
		for (int code : hand) {
			DeckCard handCard = DeckCard.byCode(code);
			if (handCard.hasKeyword(code, DeckCardKeyword.RETAIN) || (handCard == DeckCard.SHIV && shivRetain)) {
				retained.add(code);
			} else {
				discardPile.add(code);
			}
		}
		hand.clear();
		hand.addAll(retained);

		boolean injected = false;
		int incoming = 0;
		int remainingBlock = block;
		for (DeckCombatEnemy enemy : enemies) {
			if (!enemy.alive()) continue;
			enemy.block = 0;
			if (enemy.intent == RESULT_SLIMY_INJECT) {
				discardPile.add(DeckCard.SLIMY.code());
				injected = true;
                    lastEnemyActions.add(new EnemyAction(enemyIndex(enemy), 0, true, "\uC810\uC561\uD22C\uC131\uC774"));
			} else if (enemy.intent == RESULT_AGE_DOWN) {
				playerDamageReduction = Math.max(playerDamageReduction, 30);
                    lastEnemyActions.add(new EnemyAction(enemyIndex(enemy), 0, false, "\uC5F0\uB839 \uC800\uD558"));
			} else if (enemy.intent == RESULT_STRENGTH_7 || enemy.intent == RESULT_STRENGTH_2) {
				int strength = enemy.intent == RESULT_STRENGTH_7 ? 7 : 2;
				enemy.strength += strength;
                    lastEnemyActions.add(new EnemyAction(enemyIndex(enemy), 0, false, "\uACF5\uACA9\uB825 +" + strength));
			} else if (enemy.intent == RESULT_TOWER_NEEDLE) {
				enemy.thorns += 2;
                    lastEnemyActions.add(new EnemyAction(enemyIndex(enemy), 0, false, "[\uD0C0\uC6CC \uB2C8\uB4E4]"));
			} else if (enemy.intent == RESULT_MASSACRE) {
				for (int hit = 0; hit < 3; hit++) {
					int enemyDamage = enemyDamage(enemy, 3);
					incoming += enemyDamage;
					int blocked = Math.min(remainingBlock, enemyDamage);
					int damage = Math.max(0, enemyDamage - blocked);
					remainingBlock = Math.max(0, remainingBlock - enemyDamage);
                        lastEnemyActions.add(new EnemyAction(enemyIndex(enemy), damage, false, hit == 0 ? "[\uB300\uD559\uC0B4!]" : null, enemyDamage > 0 && damage == 0));
				}
				enemy.thorns = Math.max(0, enemy.thorns - 2);
			} else if (enemy.intent == RESULT_ATTACK_6_BLOCK_5) {
				int enemyDamage = enemyDamage(enemy, 6);
				incoming += enemyDamage;
				int blocked = Math.min(remainingBlock, enemyDamage);
				int damage = Math.max(0, enemyDamage - blocked);
				remainingBlock = Math.max(0, remainingBlock - enemyDamage);
				enemy.block += 5;
                    lastEnemyActions.add(new EnemyAction(enemyIndex(enemy), damage, false, "\uBC29\uC5B4\uB9C9 +5", enemyDamage > 0 && damage == 0));
			} else {
				int enemyDamage = enemyDamage(enemy, enemy.intent);
				incoming += enemyDamage;
				int blocked = Math.min(remainingBlock, enemyDamage);
				int damage = Math.max(0, enemyDamage - blocked);
				remainingBlock = Math.max(0, remainingBlock - enemyDamage);
                    lastEnemyActions.add(new EnemyAction(enemyIndex(enemy), damage, false, enemy.kind == DeckEnemy.TOWER_OF_GREY && enemy.intent == 7 ? "[\uD600 \uB728\uC5B4\uB0B4\uAE30]" : null, enemyDamage > 0 && damage == 0));
			}
			if (enemy.vulnerable > 0) enemy.vulnerable--;
		}

		int damage = Math.max(0, incoming - block);
		if (damage > 0) {
			DeckBuilderRun.playerHP = Math.max(0, DeckBuilderRun.playerHP - damage);
		}
		if (!playerDead()) {
			startTurn();
		}
		return injected && damage == 0 ? RESULT_SLIMY_INJECT : damage;
	}

	private int enemyDamage(DeckCombatEnemy enemy, int baseDamage) {
		return Math.max(0, baseDamage + enemy.strength);
	}

	public boolean won() {
		for (DeckCombatEnemy enemy : enemies) {
			if (enemy.alive()) return false;
		}
		return true;
	}

	public boolean playerDead() {
		return DeckBuilderRun.playerHP <= 0;
	}

	public DeckCombatEnemy target() {
		sanitizeTarget();
		return targetIndex >= 0 && targetIndex < enemies.size() ? enemies.get(targetIndex) : null;
	}

	public void setTarget(int index) {
		if (index >= 0 && index < enemies.size() && enemies.get(index).alive()) {
			targetIndex = index;
		}
	}

	public ArrayList<DeckCombatEnemy> aliveEnemies() {
		ArrayList<DeckCombatEnemy> result = new ArrayList<>();
		for (DeckCombatEnemy enemy : enemies) {
			if (enemy.alive()) result.add(enemy);
		}
		return result;
	}

	public int enemyIndex(DeckCombatEnemy enemy) {
		return enemies.indexOf(enemy);
	}

	private void sanitizeTarget() {
		if (targetIndex >= 0 && targetIndex < enemies.size() && enemies.get(targetIndex).alive()) {
			return;
		}
		for (int i = 0; i < enemies.size(); i++) {
			if (enemies.get(i).alive()) {
				targetIndex = i;
				return;
			}
		}
		targetIndex = 0;
	}

	private void drawTo(int size) {
		while (hand.size() < size) {
			if (!draw(1)) break;
		}
	}

	public boolean draw(int count) {
		boolean drew = false;
		for (int i = 0; i < count; i++) {
			if (hand.size() >= maxHandSize) return drew;
			if (drawPile.isEmpty()) {
				if (discardPile.isEmpty()) return drew;
				drawPile.addAll(discardPile);
				discardPile.clear();
				shuffle(drawPile);
			}
			hand.add(drawPile.remove(0));
			drew = true;
		}
		return drew;
	}

	private static void shuffle(ArrayList<Integer> cards) {
		for (int i = cards.size() - 1; i > 0; i--) {
			int j = Random.Int(i + 1);
			Integer tmp = cards.get(i);
			cards.set(i, cards.get(j));
			cards.set(j, tmp);
		}
	}

	private static int[] toArray(ArrayList<Integer> list) {
		int[] result = new int[list.size()];
		for (int i = 0; i < list.size(); i++) {
			result[i] = list.get(i);
		}
		return result;
	}

	private static void restoreList(ArrayList<Integer> list, Bundle bundle, String key) {
		list.clear();
		if (!bundle.contains(key)) return;
		for (int id : bundle.getIntArray(key)) {
			list.add(id);
		}
	}

	public static class EnemyAction {
		public final int enemyIndex;
		public final int damage;
		public final boolean slimyInject;
		public final String label;
		public final boolean blocked;

		public EnemyAction(int enemyIndex, int damage, boolean slimyInject) {
			this(enemyIndex, damage, slimyInject, null);
		}

		public EnemyAction(int enemyIndex, int damage, boolean slimyInject, String label) {
			this(enemyIndex, damage, slimyInject, label, false);
		}

		public EnemyAction(int enemyIndex, int damage, boolean slimyInject, String label, boolean blocked) {
			this.enemyIndex = enemyIndex;
			this.damage = damage;
			this.slimyInject = slimyInject;
			this.label = label;
			this.blocked = blocked;
		}
	}
}
