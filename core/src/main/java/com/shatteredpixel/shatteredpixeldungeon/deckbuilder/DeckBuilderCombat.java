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
	private static final String ENEMY_HP = "enemy_hp";
	private static final String ENEMY_INTENTS = "enemy_intents";
	private static final String ENEMY_VULNERABLE = "enemy_vulnerable";

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
		for (DeckEnemy kind : DeckEnemy.encounterForNode(nodeType, this.depth)) {
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
		bundle.put(DRAW_PILE, toArray(drawPile));
		bundle.put(HAND, toArray(hand));
		bundle.put(DISCARD_PILE, toArray(discardPile));
		bundle.put(EXHAUST_PILE, toArray(exhaustPile));

		int[] enemyKinds = new int[enemies.size()];
		int[] enemyHp = new int[enemies.size()];
		int[] enemyIntents = new int[enemies.size()];
		int[] enemyVulnerable = new int[enemies.size()];
		for (int i = 0; i < enemies.size(); i++) {
			DeckCombatEnemy enemy = enemies.get(i);
			enemyKinds[i] = enemy.kind.ordinal();
			enemyHp[i] = enemy.hp;
			enemyIntents[i] = enemy.intent;
			enemyVulnerable[i] = enemy.vulnerable;
		}
		bundle.put(ENEMY_KINDS, enemyKinds);
		bundle.put(ENEMY_HP, enemyHp);
		bundle.put(ENEMY_INTENTS, enemyIntents);
		bundle.put(ENEMY_VULNERABLE, enemyVulnerable);
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
		restoreList(combat.drawPile, bundle, DRAW_PILE);
		restoreList(combat.hand, bundle, HAND);
		restoreList(combat.discardPile, bundle, DISCARD_PILE);
		restoreList(combat.exhaustPile, bundle, EXHAUST_PILE);

		int[] enemyKinds = bundle.getIntArray(ENEMY_KINDS);
		int[] enemyHp = bundle.contains(ENEMY_HP) ? bundle.getIntArray(ENEMY_HP) : new int[0];
		int[] enemyIntents = bundle.contains(ENEMY_INTENTS) ? bundle.getIntArray(ENEMY_INTENTS) : new int[0];
		int[] enemyVulnerable = bundle.contains(ENEMY_VULNERABLE) ? bundle.getIntArray(ENEMY_VULNERABLE) : new int[0];
		DeckEnemy[] allEnemies = DeckEnemy.values();
		for (int i = 0; i < enemyKinds.length; i++) {
			int kindIndex = enemyKinds[i];
			if (kindIndex < 0 || kindIndex >= allEnemies.length) continue;
			DeckCombatEnemy enemy = new DeckCombatEnemy(allEnemies[kindIndex], combat.depth);
			if (i < enemyHp.length) enemy.hp = enemyHp[i];
			if (i < enemyIntents.length) enemy.intent = enemyIntents[i];
			if (i < enemyVulnerable.length) enemy.vulnerable = enemyVulnerable[i];
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
		for (DeckCombatEnemy enemy : enemies) {
			if (enemy.alive()) {
				enemy.intent = enemy.kind.nextIntent(turn, depth);
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
		if (card.hasKeyword(cardCode, DeckCardKeyword.EXHAUST)) {
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
		return damage;
	}

	public int endTurn() {
		lastEnemyActions.clear();
		ArrayList<Integer> retained = new ArrayList<>();
		for (int code : hand) {
			if (DeckCard.byCode(code).hasKeyword(code, DeckCardKeyword.RETAIN)) {
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
			if (enemy.intent == RESULT_SLIMY_INJECT) {
				discardPile.add(DeckCard.SLIMY.code());
				injected = true;
				lastEnemyActions.add(new EnemyAction(enemyIndex(enemy), 0, true));
			} else {
				incoming += enemy.intent;
				int blocked = Math.min(remainingBlock, enemy.intent);
				int damage = Math.max(0, enemy.intent - blocked);
				remainingBlock = Math.max(0, remainingBlock - enemy.intent);
				lastEnemyActions.add(new EnemyAction(enemyIndex(enemy), damage, false));
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

		public EnemyAction(int enemyIndex, int damage, boolean slimyInject) {
			this.enemyIndex = enemyIndex;
			this.damage = damage;
			this.slimyInject = slimyInject;
		}
	}
}
