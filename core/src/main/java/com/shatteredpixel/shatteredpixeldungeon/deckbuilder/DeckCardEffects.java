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
import com.watabou.utils.Random;

import java.util.ArrayList;

public class DeckCardEffects {

	public static class Damage implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			for (DeckCombatEnemy target : targets(combat, card)) {
				int damage = combat.cardDamage(card, cardCode, target);
				int dealt = combat.damageEnemy(target, damage, card.type == DeckCardType.ATTACK);
				result.addAttackHit(combat.enemyIndex(target), dealt);
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
			result.block += combat.gainBlockFromCard(card.block(cardCode));
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return "보호막을 " + DeckCardText.blockValue(card.block(cardCode), combat) + " 얻습니다.";
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
				int vulnerable = combat.enemyDebuffAmount(card.vulnerable(cardCode));
				target.vulnerable += vulnerable;
				result.addHit(combat.enemyIndex(target), 0, vulnerable);
			}
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return "피해 증폭을 " + card.vulnerable(cardCode) + " 부여합니다.";
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
			int amount = combat.enemyDebuffAmount(DeckCardCode.upgradeLevel(cardCode) > 0 ? upgraded : base);
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

	public static class Dexterity implements DeckCardEffect {
		private final int base;
		private final int upgraded;

		public Dexterity(int base, int upgraded) {
			this.base = base;
			this.upgraded = upgraded;
		}

		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			int dexterity = amount(cardCode);
			combat.playerDexterity += dexterity;
			result.dexterity += dexterity;
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return "방어력 증가를 " + amount(cardCode) + " 얻습니다.";
		}

		@Override
		public String keywordText(DeckCard card, int cardCode) {
			return "방어력 증가: 획득 보호막 수치가 직접적으로 표시된 공격/스킬 카드의 사용을 통해 얻는 보호막이 방어력 증가 수치만큼 증감합니다. 전투가 끝날 때까지 유지됩니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			int current = amount(cardCode);
			int next = amount(upgradedCode);
			return current == next ? "" : "방어력 증가 " + current + " > " + next;
		}

		private int amount(int cardCode) {
			return DeckCardCode.upgradeLevel(cardCode) > 0 ? upgraded : base;
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
					result.addAttackHit(combat.enemyIndex(target), dealt);
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
				result.addAttackHit(combat.enemyIndex(target), unblockedDamage);
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

	public static class Discover implements DeckCardEffect {

		private final DeckDiscover spec;

		public Discover(DeckDiscover spec) {
			this.spec = spec;
		}

		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			DeckCard[] choices = spec.rollChoices(combat);
			combat.pendingDiscoverChoices = choices;
			combat.pendingDiscoverZeroCost = spec.zeroCost;
			combat.pendingDiscoverPlayAfterPick = spec.playAfterPick;
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			String text = "무작위 카드를 발견합니다.";
			if (spec.zeroCost) text += " 그 카드의 비용이 0이 됩니다.";
			return text;
		}

		@Override
		public String keywordText(DeckCard card, int cardCode) {
			return "발견: 세 가지 카드 선택지 중 하나를 골라 손으로 가져옵니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "";
		}
	}

	public static class WeaknessStabBonus implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			if (combat.playerConsecutiveStrike < 2) return;
			combat.playerConsecutiveStrike -= 2;
			for (DeckCombatEnemy target : targets(combat, card)) {
				if (!combat.applyEnemyDebuff(target)) continue;
				int amount = combat.enemyDebuffAmount(2);
				target.attackDown += amount;
				target.vulnerable += amount;
				result.addHit(combat.enemyIndex(target), 0, amount);
			}
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return "_연속 타격_을 2 소모하고 공격력 저하 2와 피해 증폭 2를 부여합니다.";
		}

		@Override
		public boolean conditionMet(DeckBuilderCombat combat, DeckCard card, int cardCode) {
			return combat.playerConsecutiveStrike >= 2;
		}
	}

	public static class BarrageBonus implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			if (combat.playerConsecutiveStrike < 3) return;
			combat.playerConsecutiveStrike -= 3;
			result.nextWave();
			for (DeckCombatEnemy target : targets(combat, card)) {
				int bonusDmg = combat.cardDamage(card, cardCode, target);
				int dealt = combat.damageEnemy(target, bonusDmg, true);
				result.addAttackHit(combat.enemyIndex(target), dealt);
			}
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return "_연속 타격_을 3 소모하고 피해를 " + DeckCardText.damageValue(card, cardCode, combat) + " 추가로 줍니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "추가 피해 10 > 12";
		}

		@Override
		public boolean conditionMet(DeckBuilderCombat combat, DeckCard card, int cardCode) {
			return combat.playerConsecutiveStrike >= 3;
		}
	}

	public static class FlowSlashBonus implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			if (combat.playerConsecutiveStrike < 2) return;
			result.block += combat.gainBlockFromCard(10);
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return "_연속 타격_이 2 이상이면 보호막을 " + DeckCardText.blockValue(10, combat) + " 얻습니다.";
		}

		@Override
		public boolean conditionMet(DeckBuilderCombat combat, DeckCard card, int cardCode) {
			return combat.playerConsecutiveStrike >= 2;
		}
	}

	public static class AccelStabBonus implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			int amount = DeckCardCode.upgradeLevel(cardCode) > 0 ? 3 : 2;
			combat.playerTurnStrength += amount;
			result.strength += amount;
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			int amount = DeckCardCode.upgradeLevel(cardCode) > 0 ? 3 : 2;
			return "이번 턴에 공격력을 " + amount + " 얻습니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "공격력 2 > 3";
		}
	}

	public static class HyperventilateBonus implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			if (combat.playerConsecutiveStrike < 3) return;
			combat.playerConsecutiveStrike -= 3;
			combat.energy = Math.min(10, combat.energy + 3);
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return "_연속 타격_을 3 소모하고 에너지를 3 얻습니다.";
		}

		@Override
		public boolean conditionMet(DeckBuilderCombat combat, DeckCard card, int cardCode) {
			return combat.playerConsecutiveStrike >= 3;
		}
	}

	public static class ThornStanceBonus implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			combat.playerThorns += DeckCard.upgradeLevel(cardCode) > 0 ? 6 : 4;
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			int thorns = DeckCard.upgradeLevel(cardCode) > 0 ? 6 : 4;
			return "이번 턴에 반격 " + thorns+ "를 얻습니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "보호막 12 → 16, 반격 4 → 6";
		}
	}

	public static class WeaponRetrievalBonus implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			ArrayList<Integer> attacks = new ArrayList<>();
			for (int code : combat.discardPile) {
				if (DeckCard.byCode(code).type == DeckCardType.ATTACK) attacks.add(code);
			}
			for (int i = attacks.size() - 1; i > 0; i--) {
				int j = Random.Int(i + 1);
				int tmp = attacks.get(i);
				attacks.set(i, attacks.get(j));
				attacks.set(j, tmp);
			}
			int count = Math.min(2, attacks.size());
			for (int i = 0; i < count; i++) {
				int picked = attacks.get(i);
				combat.discardPile.remove(Integer.valueOf(picked));
				combat.addToHand(DeckCardCode.withKeyword(picked, DeckCardKeyword.ZERO_COST));
			}
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return "버린 카드 더미에서 무작위 공격 카드 2장을 손으로 가져옵니다. 그 카드들의 비용이 이번 턴에 0이 됩니다. 이 카드의 비용은 _연속 타격_ 수치만큼 감소합니다.";
		}

		@Override
		public boolean conditionMet(DeckBuilderCombat combat, DeckCard card, int cardCode) {
			for (int code : combat.discardPile) {
				if (DeckCard.byCode(code).type == DeckCardType.ATTACK) return true;
			}
			return false;
		}
	}

	public static class WeaponDiscoverBonus implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass heroClass =
					Dungeon.hero != null ? Dungeon.hero.heroClass : null;
			ArrayList<DeckCard> candidates = new ArrayList<>();
			for (DeckCard c : DeckCard.rewardPool(heroClass, true, false)) {
				if (c.type == DeckCardType.ATTACK && c != DeckCard.WEAPON_DISCOVER) candidates.add(c);
			}
			if (candidates.isEmpty()) {
				for (DeckCard c : DeckCard.rewardPool(heroClass, false, false)) {
					if (c.type == DeckCardType.ATTACK) candidates.add(c);
				}
			}
			for (int i = candidates.size() - 1; i > 0; i--) {
				int j = Random.Int(i + 1);
				DeckCard tmp = candidates.get(i);
				candidates.set(i, candidates.get(j));
				candidates.set(j, tmp);
			}
			int actual = Math.min(3, candidates.size());
			DeckCard[] choices = new DeckCard[actual];
			for (int i = 0; i < actual; i++) choices[i] = candidates.get(i);
			combat.pendingDiscoverChoices = choices;
			combat.pendingDiscoverZeroCost = false;
			combat.pendingDiscoverTransient = true;
			combat.pendingDiscoverUpgraded = DeckCardCode.upgradeLevel(cardCode) > 0;
			combat.pendingDiscoverPlayAfterPick = false;
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			boolean upgraded = DeckCardCode.upgradeLevel(cardCode) > 0;
			return (upgraded ? "강화된 " : "") + "내 영웅의 전용 공격 카드 1장을 발견합니다. 그 카드에 _일시적_을 부여합니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "강화된 카드를 발견합니다.";
		}
	}

	public static class OnslaughtBonus implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			int repeats = combat.playerConsecutiveStrike;
			for (int i = 0; i < repeats; i++) {
				result.nextWave();
				for (DeckCombatEnemy target : targets(combat, card)) {
					int dmg = combat.cardDamage(card, cardCode, target);
					int dealt = combat.damageEnemy(target, dmg, true);
					result.addAttackHit(combat.enemyIndex(target), dealt);
				}
			}
			combat.playerConsecutiveStrike = 0;
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return "_연속 타격_ 수치만큼 반복합니다. 이후 _연속 타격_을 모두 잃습니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "피해 6 > 8";
		}

		@Override
		public boolean conditionMet(DeckBuilderCombat combat, DeckCard card, int cardCode) {
			return combat.playerConsecutiveStrike > 0;
		}
	}

	public static class BodySlamDamage implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			int handPenaltyTotal = 0;
			for (int code : combat.hand) handPenaltyTotal += DeckCard.byCode(code).handPenalty;
			int strength = combat.playerStrength + combat.playerTurnStrength;
			for (DeckCombatEnemy target : targets(combat, card)) {
				int damage = Math.max(0, combat.block + strength - handPenaltyTotal);
				if (target.vulnerable > 0) damage = (damage * 3 + 1) / 2;
				if (combat.playerDamageReduction > 0) damage = damage * Math.max(0, 100 - combat.playerDamageReduction) / 100;
				int dealt = combat.damageEnemy(target, damage, true);
				result.addAttackHit(combat.enemyIndex(target), dealt);
			}
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			if (combat == null) return "현재 방어도만큼 피해를 줍니다.";
			int handPenaltyTotal = 0;
			for (int code : combat.hand) handPenaltyTotal += DeckCard.byCode(code).handPenalty;
			int strength = combat.playerStrength + combat.playerTurnStrength;
			DeckCombatEnemy target = combat.target();
			int damage = Math.max(0, combat.block + strength - handPenaltyTotal);
			if (target != null && target.vulnerable > 0) damage = (damage * 3 + 1) / 2;
			if (combat.playerDamageReduction > 0) damage = damage * Math.max(0, 100 - combat.playerDamageReduction) / 100;
			return "피해를 " + damage + " 줍니다. (현재 방어도 기반)";
		}
	}

	public static class BarricadeEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			combat.playerBarricade = true;
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return "내 턴 시작 시 방어도가 사라지지 않습니다.";
		}
	}

	public static class EntrenchEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			combat.playerFirstBlockDouble = true;
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return "매 턴 처음으로 카드를 통해 얻는 방어도가 2배가 됩니다.";
		}
	}

	public static class ArmamentsBonus implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			if (DeckCardCode.upgradeLevel(cardCode) > 0) {
				for (int i = 0; i < combat.hand.size(); i++) {
					int handCode = combat.hand.get(i);
					DeckCard handCard = DeckCard.byCode(handCode);
					if (DeckCardCode.upgradeLevel(handCode) < handCard.maxUpgradeLevel()) {
						combat.hand.set(i, DeckCardCode.upgrade(handCode));
					}
				}
			} else {
				combat.pendingHandCardUpgrade = true;
			}
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return DeckCardCode.upgradeLevel(cardCode) > 0
					? "손에 있는 모든 카드를 1회 강화합니다."
					: "손에 있는 카드 1장을 강화합니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return DeckCardCode.upgradeLevel(cardCode) > 0 ? "" : "카드 1장 강화 > 손에 있는 모든 카드 강화";
		}
	}

	public static class TextOnly implements DeckCardEffect {
		private final String text;
		public TextOnly(String text) { this.text = text; }
		@Override public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {}
		@Override public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) { return text; }
	}

	public static class RuptureEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			combat.ruptureStrengthPerLoss = DeckCardCode.upgradeLevel(cardCode) > 0 ? 2 : 1;
		}
		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			int s = DeckCardCode.upgradeLevel(cardCode) > 0 ? 2 : 1;
			return "내 턴 동안 체력을 잃을 때마다, 공격력을 " + s + " 얻습니다.";
		}
		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) { return "공격력 1 → 2"; }
	}

	public static class BloodlettingEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			combat.loseHP(3, result);
			int gain = DeckCardCode.upgradeLevel(cardCode) > 0 ? 3 : 2;
			combat.energy = Math.min(DeckBuilderRun.MAX_ENERGY_CAP, combat.energy + gain);
		}
		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			int gain = DeckCardCode.upgradeLevel(cardCode) > 0 ? 3 : 2;
			return "체력을 3 잃습니다. 에너지를 " + gain + " 얻습니다.";
		}
		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) { return "에너지 2 → 3"; }
	}

	public static class FireseaEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			combat.fireseaDamagePerLoss = DeckCardCode.upgradeLevel(cardCode) > 0 ? 9 : 6;
		}
		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			int dmg = DeckCardCode.upgradeLevel(cardCode) > 0 ? 9 : 6;
			return "매 턴 시작 시 체력을 1 잃습니다. 내 턴 동안 체력을 잃을 때마다, 모든 적에게 피해를 " + dmg + " 줍니다.";
		}
		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) { return "피해 6 → 9"; }
	}

	public static class BloodWallEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			combat.loseHP(2, result);
			int block = DeckCardCode.upgradeLevel(cardCode) > 0 ? 20 : 16;
			result.block += combat.gainBlockFromCard(block);
		}
		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			int block = DeckCardCode.upgradeLevel(cardCode) > 0 ? 20 : 16;
			return "체력을 2 잃습니다. 보호막을 " + DeckCardText.blockValue(block, combat) + " 얻습니다.";
		}
		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) { return "보호막 16 → 20"; }
	}

	public static class BloodflowEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			combat.loseHP(2, result);
		}
		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) { return "체력을 2 잃습니다."; }
	}

	public static class MaliceBonus implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			if (combat.playerHPLostCountThisTurn <= 0) return;
			int extra = DeckCardCode.upgradeLevel(cardCode) > 0 ? 2 : 1;
			for (int i = 0; i < extra; i++) {
				result.nextWave();
				for (DeckCombatEnemy target : targets(combat, card)) {
					int dealt = combat.damageEnemy(target, combat.cardDamage(card, cardCode, target), true);
					result.addAttackHit(combat.enemyIndex(target), dealt);
				}
			}
		}
		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			int hits = DeckCardCode.upgradeLevel(cardCode) > 0 ? 3 : 2;
			return "이번 턴 체력을 잃었다면, " + hits + "번 적중합니다.";
		}
		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) { return "적중 2 → 3번"; }
		@Override
		public boolean conditionMet(DeckBuilderCombat combat, DeckCard card, int cardCode) {
			return combat.playerHPLostCountThisTurn > 0;
		}
	}

	public static class BrandEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			combat.loseHP(1, result);
			if (!combat.hand.isEmpty()) {
				int idx = Random.Int(combat.hand.size());
				result.draw += combat.exhaustCard(combat.hand.get(idx));
				combat.hand.remove(idx);
				result.exhausted = true;
			}
			int s = DeckCardCode.upgradeLevel(cardCode) > 0 ? 2 : 1;
			combat.playerStrength += s;
			result.strength += s;
		}
		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			int s = DeckCardCode.upgradeLevel(cardCode) > 0 ? 2 : 1;
			return "체력을 1 잃습니다. 손의 카드 1장을 무작위로 소멸시킵니다. 공격력을 " + s + " 얻습니다.";
		}
		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) { return "공격력 1 → 2"; }
	}

	public static class IndomitableEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			int heal = DeckCardCode.upgradeLevel(cardCode) > 0 ? 13 : 10;
			DeckBuilderRun.playerHP = Math.min(DeckBuilderRun.playerHT, DeckBuilderRun.playerHP + heal);
		}
		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			int heal = DeckCardCode.upgradeLevel(cardCode) > 0 ? 13 : 10;
			return "체력을 " + heal + " 회복합니다.";
		}
		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) { return "회복 10 → 13"; }
	}

	public static class RendBonus implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			int repeats = combat.playerHPLostCountThisCombat;
			for (int i = 0; i < repeats; i++) {
				result.nextWave();
				for (DeckCombatEnemy target : targets(combat, card)) {
					int dealt = combat.damageEnemy(target, combat.cardDamage(card, cardCode, target), true);
					result.addAttackHit(combat.enemyIndex(target), dealt);
				}
			}
		}
		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			if (combat == null) return "이번 전투 동안 체력을 잃은 횟수만큼 반복합니다.";
			return "이번 전투 동안 체력을 잃은 횟수만큼 반복합니다. (현재 " + combat.playerHPLostCountThisCombat + "회)";
		}
		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) { return "피해 5 → 7"; }
		@Override
		public boolean conditionMet(DeckBuilderCombat combat, DeckCard card, int cardCode) {
			return combat.playerHPLostCountThisCombat > 0;
		}
	}

	public static class SurgeEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			combat.surgeActive = true;
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return "내 턴 종료 시, 손에 있는 무작위 공격 카드 1장이 무작위 적에게 사용됩니다.";
		}
	}

	public static class PlayRandomFromDrawPile implements DeckCardEffect {
		private final int base;
		private final int upgraded;

		public PlayRandomFromDrawPile(int base, int upgraded) {
			this.base = base;
			this.upgraded = upgraded;
		}

		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			int count = DeckCardCode.upgradeLevel(cardCode) > 0 ? upgraded : base;
			combat.playRandomFromDrawPile(count);
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			int count = DeckCardCode.upgradeLevel(cardCode) > 0 ? upgraded : base;
			return "뽑을 카드 더미에서 무작위 카드를 " + count + "장 사용합니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return base == upgraded ? "" : "무작위 카드 " + base + "장 → " + upgraded + "장";
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
