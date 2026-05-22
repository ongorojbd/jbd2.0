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

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return DeckCardText.damageRulesText(card, cardCode, combat);
		}
	}

	public static class Block implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			result.block += combat.gainBlock(card.block(cardCode));
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return "보호막을 " + card.block(cardCode) + " 얻습니다.";
		}
	}

	public static class Draw implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			combat.draw(card.draw(cardCode));
			result.draw += card.draw(cardCode);
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return "카드를 " + card.draw(cardCode) + "장 뽑습니다.";
		}
	}

	public static class ShuffleIntoDrawPile implements DeckCardEffect {
		private final DeckCard cardToShuffle;
		private final int count;
		private final boolean upgradedCopies;

		public ShuffleIntoDrawPile(DeckCard cardToShuffle, int count) {
			this(cardToShuffle, count, false);
		}

		public ShuffleIntoDrawPile(DeckCard cardToShuffle, int count, boolean upgradedCopies) {
			this.cardToShuffle = cardToShuffle;
			this.count = count;
			this.upgradedCopies = upgradedCopies;
		}

		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			if (cardToShuffle == null || count <= 0) return;
			int code = cardToShuffle.code();
			if (upgradedCopies) code = DeckCardCode.upgrade(code);
			combat.addToDrawPile(code, count, true);
			result.addShuffle(cardToShuffle, count);
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			if (cardToShuffle == null || count <= 0) return "";
			return cardToShuffle.title(cardToShuffle.code()) + " " + count + "장을 뽑을 카드 더미에 섞어 넣습니다.";
		}
	}

	public static class AimShuffleIntoDrawPile implements DeckCardEffect {
		private final DeckCard cardToShuffle;
		private final int count;

		public AimShuffleIntoDrawPile(DeckCard cardToShuffle, int count) {
			this.cardToShuffle = cardToShuffle;
			this.count = count;
		}

		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			// Context-aware apply below handles the actual condition.
		}

		@Override
		public void apply(DeckCardPlayContext context) {
			if (cardToShuffle == null || count <= 0 || context.combat == null) return;
			if (!context.aimActive) return;
			context.combat.addToDrawPile(cardToShuffle.code(), count, true);
			context.result.addShuffle(cardToShuffle, count);
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			if (cardToShuffle == null || count <= 0) return "";
			return cardToShuffle.title(cardToShuffle.code()) + " " + count + "장을 뽑을 카드 더미에 섞어 넣습니다.";
		}
	}

	public static class SpinningNailTraining implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			combat.spinningNailDamageBonus += DeckCardCode.upgradeLevel(cardCode) > 0 ? 2 : 1;
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			int bonus = DeckCardCode.upgradeLevel(cardCode) > 0 ? 2 : 1;
			return DeckCard.ROTATING_NAIL.title(DeckCard.ROTATING_NAIL.code()) + "의 피해량이 +" + bonus + " 증가합니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			int base = DeckCardCode.upgradeLevel(cardCode) > 0 ? 2 : 1;
			int upgraded = DeckCardCode.upgradeLevel(upgradedCode) > 0 ? 2 : 1;
			return base == upgraded ? "" : "회전하는 손톱 피해 증가 +" + base + " > +" + upgraded;
		}
	}

	public static class CopyAndPlayFromDrawPile implements DeckCardEffect {
		private final DeckCard cardToPlay;

		public CopyAndPlayFromDrawPile(DeckCard cardToPlay) {
			this.cardToPlay = cardToPlay;
		}

		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			combat.copyAndPlayFromDrawPile(cardToPlay, result);
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			if (cardToPlay == null) return "";
			return "뽑을 카드 더미에 있는 모든 " + cardToPlay.title(cardToPlay.code()) + "을 복사해서 시전합니다.";
		}
	}

	public static class Vulnerable implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			for (DeckCombatEnemy target : targets(combat, card)) {
				if (!combat.applyEnemyDebuff(target)) continue;
				int vulnerable = card.vulnerable(cardCode);
				target.vulnerable += vulnerable;
				result.addHit(combat.enemyIndex(target), 0, vulnerable);
			}
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return "취약을 " + card.vulnerable(cardCode) + " 부여합니다.";
		}
	}

	public static class AttackDown implements DeckCardEffect {
		private final int base;
		private final int upgraded;

		public AttackDown(int base, int upgraded) {
			this.base = base;
			this.upgraded = upgraded;
		}

		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			int amount = DeckCardCode.upgradeLevel(cardCode) > 0 ? upgraded : base;
			for (DeckCombatEnemy target : targets(combat, card)) {
				if (!combat.applyEnemyDebuff(target)) continue;
				target.attackDown += amount;
			}
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			int amount = DeckCardCode.upgradeLevel(cardCode) > 0 ? upgraded : base;
			return "공격력 저하를 " + amount + " 부여합니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return base == upgraded ? "" : "공격력 저하 " + base + " > " + upgraded;
		}

		@Override
		public String keywordText(DeckCard card, int cardCode) {
			return "공격력 저하: 적의 공격 피해가 25% 감소합니다.";
		}
	}

	public static class Strength implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			int strength = card.strength(cardCode);
			combat.playerStrength += strength;
			result.strength += strength;
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return "공격력을 " + card.strength(cardCode) + " 얻습니다.";
		}
	}

	public static class TurnStrength implements DeckCardEffect {
		private final int amount;

		public TurnStrength(int amount) {
			this.amount = amount;
		}

		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			combat.playerTurnStrength += amount;
			result.strength += amount;
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return "이번 턴에만 공격력을 " + amount + " 얻습니다.";
		}
	}

	public static class AddShivs implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			for (int i = 0; i < card.shivs(cardCode); i++) {
				int shivCode = DeckCard.SHIV.code();
				if (combat.shivRetain) {
					shivCode = DeckCardCode.withKeyword(shivCode, DeckCardKeyword.RETAIN);
				}
				combat.addToHand(shivCode);
			}
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return "전갈탄을 " + card.shivs(cardCode) + "장 손에 가져옵니다.";
		}
	}

	public static class PhantomBlades implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			combat.shivRetain = true;
			combat.firstShivDamageBonus = Math.max(combat.firstShivDamageBonus, DeckCardCode.upgradeLevel(cardCode) > 0 ? 12 : 9);
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			int bonus = DeckCardCode.upgradeLevel(cardCode) > 0 ? 12 : 9;
			return "모든 전갈탄에 " + DeckCardKeyword.RETAIN.label + "을 부여합니다. 매 턴 처음으로 사용하는 전갈탄의 피해량이 +" + bonus + " 증가합니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			int base = DeckCardCode.upgradeLevel(cardCode) > 0 ? 12 : 9;
			int upgraded = DeckCardCode.upgradeLevel(upgradedCode) > 0 ? 12 : 9;
			return base == upgraded ? "" : "첫 전갈탄 피해 증가 +" + base + " > +" + upgraded;
		}
	}

	public static class Accuracy implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			combat.shivDamageBonus += DeckCardCode.upgradeLevel(cardCode) > 0 ? 6 : 4;
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			int bonus = DeckCardCode.upgradeLevel(cardCode) > 0 ? 6 : 4;
			return "전갈탄의 피해량이 +" + bonus + " 증가합니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			int base = DeckCardCode.upgradeLevel(cardCode) > 0 ? 6 : 4;
			int upgraded = DeckCardCode.upgradeLevel(upgradedCode) > 0 ? 6 : 4;
			return base == upgraded ? "" : "전갈탄 피해 증가 +" + base + " > +" + upgraded;
		}
	}

	public static class KnifeTrap implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			ArrayList<Integer> shivs = new ArrayList<>();
			for (int exhausted : combat.exhaustPile) {
				if (DeckCard.byCode(exhausted) == DeckCard.SHIV) {
					shivs.add(DeckCardCode.upgradeLevel(cardCode) > 0 ? DeckCardCode.upgrade(exhausted) : exhausted);
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

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			String text = "버린 카드 더미에 있는 모든 전갈탄을 선택한 적에게 사용합니다.";
			return DeckCardCode.upgradeLevel(cardCode) > 0 ? text + " 강화된 전갈탄으로 사용합니다." : text;
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return DeckCardCode.upgradeLevel(cardCode) == DeckCardCode.upgradeLevel(upgradedCode) ? "" : "전갈탄 시전 > 강화된 전갈탄 시전";
		}
	}

	public static class StaffEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			for (DeckCombatEnemy target : targets(combat, card)) {
				int damage = combat.cardDamage(card, cardCode, target);
				int unblockedDamage = combat.damageEnemy(target, damage, card.type == DeckCardType.ATTACK);
				result.addHit(combat.enemyIndex(target), unblockedDamage, 0);
				if (unblockedDamage > 0) {
					result.block += combat.gainBlock(unblockedDamage);
				}
			}
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return "피해를 " + DeckCardText.damageValue(card, cardCode, combat) + " 줍니다. 막히지 않은 피해만큼 보호막을 얻습니다.";
		}
	}

	public static class MageStaff implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			// Handled in apply(context)
		}

		@Override
		public void apply(DeckCardPlayContext context) {
			if (context.targetWandHandIndex >= 0 && context.targetWandHandIndex < context.combat.hand.size()) {
				int wandCode = context.combat.hand.get(context.targetWandHandIndex);
				if (DeckWandCards.isWand(wandCode)) {
					DeckWandCards.fireWand(context.combat, wandCode, context.result);
				}
			}
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return "손패의 완드 1장을 선택해 [발사]합니다.";
		}

		@Override
		public String keywordText(DeckCard card, int cardCode) {
			return "[발사]: 완드 카드의 효과를 최대 충전량만큼 즉시 발동시킨 뒤 해당 완드를 소멸시킵니다.";
		}
	}

	public static class DrawPileCostReduction implements DeckCardEffect {
		private final DeckCard countedCard;

		public DrawPileCostReduction(DeckCard countedCard) {
			this.countedCard = countedCard;
		}

		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			if (countedCard == null) return "";
			return "뽑을 카드 더미에 있는 " + countedCard.title(countedCard.code()) + "의 수만큼 비용이 감소합니다.";
		}
	}

	public static class ExhaustFromHand implements DeckCardEffect {
		private final int maxCount;

		public ExhaustFromHand(int maxCount) {
			this.maxCount = maxCount;
		}

		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			// Card selection and exhaustion is handled by the battle scene before this card is played.
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			int count = DeckCardCode.upgradeLevel(cardCode) > 0 ? maxCount + 2 : maxCount;
			return "손에 있는 카드를 최대 " + count + "장까지 소멸시킵니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			int base = DeckCardCode.upgradeLevel(cardCode) > 0 ? maxCount + 2 : maxCount;
			int upgraded = DeckCardCode.upgradeLevel(upgradedCode) > 0 ? maxCount + 2 : maxCount;
			return base == upgraded ? "" : "소멸 장 수 " + base + "장 > " + upgraded + "장";
		}
	}

	public static class RetrieveFromDiscard implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			// Card selection is handled by the battle scene after this card is played.
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return "버린 카드 더미에서 카드 1장을 선택해 뽑을 카드 더미의 맨 위에 놓습니다.";
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
