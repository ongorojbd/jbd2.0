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
				int dealt = combat.damageEnemy(target, damage, card.type == DeckCardType.ATTACK);
				result.addHit(combat.enemyIndex(target), dealt, 0);
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

	public static class AddShivs implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			for (int i = 0; i < card.shivs(cardCode); i++) {
				int shivCode = DeckCard.SHIV.code();
				if (combat.shivRetain) {
					shivCode = DeckCard.withKeyword(shivCode, DeckCardKeyword.RETAIN);
				}
				combat.addToHand(shivCode);
			}
		}
	}

	public static class PhantomBlades implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			combat.shivRetain = true;
			combat.firstShivDamageBonus = Math.max(combat.firstShivDamageBonus, DeckCard.upgradeLevel(cardCode) > 0 ? 12 : 9);
		}
	}

	public static class Accuracy implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			combat.shivDamageBonus += DeckCard.upgradeLevel(cardCode) > 0 ? 6 : 4;
		}
	}

	public static class KnifeTrap implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			ArrayList<Integer> shivs = new ArrayList<>();
			for (int exhausted : combat.exhaustPile) {
				if (DeckCard.byCode(exhausted) == DeckCard.SHIV) {
					shivs.add(DeckCard.upgradeLevel(cardCode) > 0 ? DeckCard.upgrade(exhausted) : exhausted);
				}
			}
			for (int shivCode : shivs) {
				for (DeckCombatEnemy target : targets(combat, DeckCard.SHIV)) {
					int damage = combat.cardDamage(DeckCard.SHIV, shivCode, target);
					int dealt = combat.damageEnemy(target, damage, true);
					result.addHit(combat.enemyIndex(target), dealt, 0);
				}
				combat.firstShivUsed = true;
			}
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
