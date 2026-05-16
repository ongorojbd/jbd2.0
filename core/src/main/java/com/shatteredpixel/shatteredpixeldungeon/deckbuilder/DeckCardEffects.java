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

import com.watabou.utils.Random;

import java.util.ArrayList;

public class DeckCardEffects {

	public static class Damage implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			for (DeckCombatEnemy target : targets(combat, card)) {
				int damage = combat.cardDamage(card, cardCode, target);
				target.hp = Math.max(0, target.hp - damage);
				result.addHit(combat.enemyIndex(target), damage, 0);
			}
		}
	}

	public static class Block implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			combat.block += card.block(cardCode);
			result.block += card.block(cardCode);
		}
	}

	public static class Draw implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			combat.draw(card.draw(cardCode));
			result.draw += card.draw(cardCode);
		}
	}

	public static class Vulnerable implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			for (DeckCombatEnemy target : targets(combat, card)) {
				int vulnerable = card.vulnerable(cardCode);
				target.vulnerable += vulnerable;
				result.addHit(combat.enemyIndex(target), 0, vulnerable);
			}
		}
	}

	public static class Strength implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			int strength = card.strength(cardCode);
			combat.playerStrength += strength;
			result.strength += strength;
		}
	}

	private static ArrayList<DeckCombatEnemy> targets(DeckBuilderCombat combat, DeckCard card) {
		ArrayList<DeckCombatEnemy> targets = new ArrayList<>();
		if (card.target == DeckCardTarget.ALL_ENEMIES) {
			targets.addAll(combat.aliveEnemies());
		} else if (card.target == DeckCardTarget.RANDOM_ENEMY) {
			ArrayList<DeckCombatEnemy> alive = combat.aliveEnemies();
			if (!alive.isEmpty()) targets.add(alive.get(Random.Int(alive.size())));
		} else {
			DeckCombatEnemy target = combat.target();
			if (target != null && target.alive()) targets.add(target);
		}
		return targets;
	}
}
