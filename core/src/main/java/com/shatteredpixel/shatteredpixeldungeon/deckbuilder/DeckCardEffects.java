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
import com.watabou.utils.Random;

import java.util.ArrayList;

public class DeckCardEffects {

	public static class WandTextEffect implements DeckCardEffect {
		private final String text;

		public WandTextEffect(String text) {
			this.text = text;
		}

		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return text;
		}
	}

	public static class EnhancementWandTextEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			int damage = card.damage(cardCode);
			int stored = DeckCardCode.auxValue(cardCode) * damage;
			String text = "내 턴 종료 시, 피해량이 " + damage + " 증가합니다. 해당 완드의 충전량이 0이 되면 누적된 피해량만큼 무작위 적에게 피해를 줍니다.";
			if (stored > 0) text += " (현재 누적 피해량 " + stored + ")";
			return text;
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "피해량 8 > 10";
		}
	}

	public static class CombatBreathingEffect extends Block {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return "턴 종료 시, 현재 연속 타격 수치만큼 보호막 " + card.block(cardCode) + "을 얻습니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "보호막 3 > 5";
		}
	}

	public static class BloodyCloakEffect extends Block {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return "내 턴 시작 시, 체력을 1 잃고 보호막 " + card.block(cardCode) + "을 얻습니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "보호막 6 > 8";
		}
	}

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

	public static class ParticleWallEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
		}

		@Override
		public void apply(DeckCardPlayContext context) {
			int x = Math.max(0, context.xValue);
			for (int i = 0; i < x; i++) {
				context.result.block += context.combat.gainBlockFromCard(context.card.block(context.effectiveCardCode));
			}
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return "보호막을 " + card.block(cardCode) + " X번 얻습니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "보호막 " + card.block(cardCode) + " > " + card.block(upgradedCode);
		}
	}

	public static class IAmInvincibleEffect extends Damage {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			result.block += combat.gainBlockFromCard(card.block(cardCode));
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return "보호막을 " + card.block(cardCode) + " 얻습니다.\n내 턴 종료 시 이 카드가 뽑을 카드 더미 맨 위에 있다면, 사용합니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "보호막 " + card.block(cardCode) + " > " + card.block(upgradedCode);
		}
	}

	public static class SledgehammerEffect extends Damage {
		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return DeckCardText.damageRulesText(card, cardCode, combat) + "\n이 카드는 강화 횟수에 제한이 없습니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "피해 " + card.damage(cardCode) + " > " + card.damage(upgradedCode);
		}
	}

	public static class SpecialShivDamage extends Damage {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			for (DeckCombatEnemy target : targets(combat, card)) {
				int damage = combat.cardDamage(card, cardCode, target);
				int dealt = combat.damageEnemy(target, damage, true);
				int attackDown = 0;
				if (combat.applyEnemyDebuff(target)) {
					attackDown = 1;
					target.attackDown += attackDown;
				}
				result.addHit(combat.enemyIndex(target), dealt, 0, attackDown);
			}
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return DeckCardText.damageRulesText(card, cardCode, combat) + " 공격력 저하를 1 부여합니다.";
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
			return "방어력 증가: 공격/보조 카드로 얻는 보호막이 해당 수치만큼 증가합니다.";
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

	public static class MagicMissileWandEffect extends Damage {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			super.apply(combat, card, cardCode, result);
			combat.playerTurnStrength += 1;
			result.strength += 1;
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return "내 턴이 시작할 때 무작위 적에게 피해를 2 줍니다. 이번 턴에만 공격력을 1 얻습니다.";
		}
	}

	public static class AddShivs implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			for (int i = 0; i < card.shivs(cardCode); i++) {
				combat.addShivToHand(false);
			}
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return "전갈탄을 " + card.shivs(cardCode) + "장 손에 가져옵니다.";
		}
	}

	public static class AddSpecialShivs implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			int count = DeckCardCode.upgradeLevel(cardCode) > 0 ? 3 : 2;
			for (int i = 0; i < count; i++) {
				combat.addSpecialShivToHand();
			}
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			int count = DeckCardCode.upgradeLevel(cardCode) > 0 ? 3 : 2;
			return "피해량이 1 높고, 공격력 저하 1을 부여하는 전갈탄을 " + count + "장 손으로 가져옵니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "전갈탄 2 > 3";
		}
	}

	public static class BladeFanEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			combat.shivAllEnemies = true;
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return "전갈탄이 이제 모든 적을 대상으로 합니다.";
		}
	}

	public static class InfiniteBladesEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			combat.infiniteBladesShivs++;
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return "내 턴 시작 시, 전갈탄을 1장 손으로 가져옵니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "선천성 추가";
		}
	}

	public static class HiddenDaggerEffect implements DeckCardEffect {
		@Override
		public void apply(DeckCardPlayContext context) {
			boolean upgraded = DeckCardCode.upgradeLevel(context.effectiveCardCode) > 0;
			for (int i = 0; i < 2; i++) {
				context.combat.addShivToHand(upgraded);
			}
		}

		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			boolean upgraded = DeckCardCode.upgradeLevel(cardCode) > 0;
			return "카드를 2장 버립니다. " + (upgraded ? "강화된 전갈탄" : "전갈탄") + "을 2장 손으로 가져옵니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "전갈탄 > 강화된 전갈탄";
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
				if (combat.isShivCard(DeckCard.byCode(exhausted))) {
					shivs.add(DeckCardCode.upgradeLevel(cardCode) > 0 ? DeckCardCode.upgrade(exhausted) : exhausted);
				}
			}
			boolean first = true;
			for (int shivCode : shivs) {
				if (!first) result.nextWave();
				first = false;
				DeckCard shivCard = DeckCard.byCode(shivCode);
				for (DeckCombatEnemy target : targets(combat, shivCard)) {
					int damage = combat.cardDamage(shivCard, shivCode, target);
					int dealt = combat.damageEnemy(target, damage, true);
					result.addAttackHit(combat.enemyIndex(target), dealt);
				}
				combat.firstShivUsed = true;
			}
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			String text = "소멸된 카드 더미에 있는 모든 전갈탄을 선택한 적에게 사용합니다.";
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
			return "보호막 12 > 16, 반격 4 > 6";
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
			return "버린 카드 더미에서 무작위 공격 카드 2장을 가져오고 이번 턴에 비용을 0으로 만듭니다. 이 카드의 비용은 _연속 타격_ 수치만큼 감소합니다.";
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
				if (target.vulnerable > 0) damage = target.debuffDoubleTurns > 0 ? damage * 2 : (damage * 3 + 1) / 2;
				if (combat.playerDamageReduction > 0) damage = damage * Math.max(0, 100 - combat.playerDamageReduction) / 100;
				int dealt = combat.damageEnemy(target, damage, true);
				result.addAttackHit(combat.enemyIndex(target), dealt);
			}
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			if (combat == null) return "현재 보호막만큼 피해를 줍니다.";
			int handPenaltyTotal = 0;
			for (int code : combat.hand) handPenaltyTotal += DeckCard.byCode(code).handPenalty;
			int strength = combat.playerStrength + combat.playerTurnStrength;
			DeckCombatEnemy target = combat.target();
			int damage = Math.max(0, combat.block + strength - handPenaltyTotal);
			if (target != null && target.vulnerable > 0) damage = target.debuffDoubleTurns > 0 ? damage * 2 : (damage * 3 + 1) / 2;
			if (combat.playerDamageReduction > 0) damage = damage * Math.max(0, 100 - combat.playerDamageReduction) / 100;
			return "현재 보호막만큼의 피해를 줍니다. (현재 " + damage + ")";
		}
	}

	public static class TarkusGreatswordDamage extends Damage {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			int strength = combat.playerStrength + combat.playerTurnStrength;
			int multiplier = DeckCardCode.upgradeLevel(cardCode) > 0 ? 5 : 3;
			int baseDamage = 14 + strength * (multiplier - 1);
			for (DeckCombatEnemy target : targets(combat, card)) {
				int damage = combat.cardDamageFromBase(card, cardCode, target, baseDamage);
				int dealt = combat.damageEnemy(target, damage, true);
				result.addAttackHit(combat.enemyIndex(target), dealt);
			}
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			int multiplier = DeckCardCode.upgradeLevel(cardCode) > 0 ? 5 : 3;
			if (combat == null) return "피해를 14 줍니다. 공격력의 효과가 " + multiplier + "배로 적용됩니다.";
			int strength = combat.playerStrength + combat.playerTurnStrength;
			return "피해를 " + combat.cardDamageFromBase(card, cardCode, combat.target(), 14 + strength * (multiplier - 1)) + " 줍니다. 공격력의 효과가 " + multiplier + "배로 적용됩니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "공격력 효과 3배 > 5배";
		}
	}

	public static class OddComicBookDamage extends Damage {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			int baseDamage = comicBookBaseDamage();
			for (DeckCombatEnemy target : targets(combat, card)) {
				int damage = combat.cardDamageFromBase(card, cardCode, target, baseDamage);
				int dealt = combat.damageEnemy(target, damage, true);
				result.addAttackHit(combat.enemyIndex(target), dealt);
			}
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			int baseDamage = comicBookBaseDamage();
			if (combat == null) return "피해를 " + baseDamage + " 줍니다. 내 덱의 공격 카드 하나당 피해량이 2 증가합니다.";
			return "피해를 " + combat.cardDamageFromBase(card, cardCode, combat.target(), baseDamage) + " 줍니다. 내 덱의 공격 카드 하나당 피해량이 2 증가합니다.";
		}

		private int comicBookBaseDamage() {
			int attacks = 0;
			for (int code : DeckBuilderRun.deck) {
				if (DeckCard.byCode(code).type == DeckCardType.ATTACK) attacks++;
			}
			return 6 + attacks * 2;
		}
	}

	public static class SoulCutEffect extends Damage {
		@Override
		public void apply(DeckCardPlayContext context) {
			DeckBuilderCombat combat = context.combat;
			for (int i = combat.hand.size() - 1; i >= 0; i--) {
				if (i == context.handIndex) continue;
				int code = combat.hand.get(i);
				if (DeckCard.byCode(code).type != DeckCardType.ATTACK) {
					context.result.draw += combat.exhaustCard(code);
					context.result.exhausted = true;
					combat.hand.remove(i);
				}
			}
			apply(combat, context.card, context.effectiveCardCode, context.result);
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return "손에 있는 공격 카드를 제외한 카드를 모두 소멸시킵니다. " + DeckCardText.damageRulesText(card, cardCode, combat);
		}
	}

	public static class WhirlwindEffect extends Damage {
		@Override
		public void apply(DeckCardPlayContext context) {
			for (int i = 0; i < context.xValue; i++) {
				if (i > 0) context.result.nextWave();
				for (DeckCombatEnemy target : targets(context.combat, context.card)) {
					int damage = context.combat.cardDamage(context.card, context.effectiveCardCode, target);
					int dealt = context.combat.damageEnemy(target, damage, true);
					context.result.addAttackHit(context.combat.enemyIndex(target), dealt);
				}
			}
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return "모든 적에게 피해를 " + DeckCardText.damageValue(card, cardCode, combat) + "만큼 X번 줍니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "피해 5 > 8";
		}
	}

	public static class SkyHighEffect extends Damage {
		@Override
		public void apply(DeckCardPlayContext context) {
			for (int i = 0; i < 3; i++) {
				if (i > 0) context.result.nextWave();
				for (DeckCombatEnemy target : targets(context.combat, context.card)) {
					int damage = context.combat.cardDamage(context.card, context.effectiveCardCode, target);
					int dealt = context.combat.damageEnemy(target, damage, true);
					context.result.addAttackHit(context.combat.enemyIndex(target), dealt);
				}
			}
			int count = DeckCardCode.upgradeLevel(context.effectiveCardCode) > 0 ? 2 : 1;
			ArrayList<Integer> candidates = new ArrayList<>();
			for (int i = 0; i < context.combat.hand.size(); i++) {
				if (i == context.handIndex) continue;
				int code = context.combat.hand.get(i);
				if (DeckCardCode.upgrade(code) != code) {
					candidates.add(i);
				}
			}
			for (int i = 0; i < count && !candidates.isEmpty(); i++) {
				int pick = Random.Int(candidates.size());
				int handIndex = candidates.remove(pick);
				context.combat.hand.set(handIndex, DeckCardCode.upgrade(context.combat.hand.get(handIndex)));
			}
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			int upgrades = DeckCardCode.upgradeLevel(cardCode) > 0 ? 2 : 1;
			return "피해를 " + DeckCardText.damageValue(card, cardCode, combat) + "씩 세 번 줍니다. 손에 있는 무작위 카드 " + upgrades + "장을 강화합니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "피해 3 > 4, 강화 카드 1장 > 2장";
		}
	}

	public static class BarricadeEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			combat.playerBarricade = true;
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return "내 턴 시작 시 보호막이 사라지지 않습니다.";
		}
	}

	public static class EntrenchEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			combat.playerFirstBlockDouble = true;
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return "매 턴 처음으로 카드를 통해 얻는 보호막이 2배가 됩니다.";
		}
	}

	public static class ArmamentsBonus implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			if (DeckCardCode.upgradeLevel(cardCode) > 0) {
				for (int i = 0; i < combat.hand.size(); i++) {
					int handCode = combat.hand.get(i);
					if (DeckCardCode.upgrade(handCode) != handCode) {
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

	public static class KingsPunchText implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			int amount = DeckCardCode.upgradeLevel(cardCode) > 0 ? 6 : 4;
			return "이 카드를 뽑을 때마다, 이번 전투 동안 이 카드의 피해량이 " + amount + " 증가합니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "기본 피해 8 > 10, 피해량 증가 4 > 6";
		}
	}

	public static class FirepowerAmpEffect implements DeckCardEffect {
		@Override
		public void apply(DeckCardPlayContext context) {
			int exhausted = exhaustMatchingHandCards(context, null);
			boolean upgraded = DeckCardCode.upgradeLevel(context.effectiveCardCode) > 0;
			for (int i = 0; i < exhausted; i++) {
				context.combat.addRandomRewardCardToHand(upgraded);
			}
			context.result.exhausted = context.result.exhausted || exhausted > 0;
		}

		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			String text = "손에 있는 모든 카드를 소멸시킵니다. 소멸시킨 카드 1장당 무작위 카드 1장을 손으로 가져옵니다.";
			if (DeckCardCode.upgradeLevel(cardCode) > 0) text += " 가져오는 카드가 강화됩니다.";
			return text;
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "가져오는 카드 강화";
		}
	}

	public static class BurningPactEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			int count = DeckCardCode.upgradeLevel(cardCode) > 0 ? 3 : 2;
			combat.draw(count);
			result.draw += count;
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			int count = DeckCardCode.upgradeLevel(cardCode) > 0 ? 3 : 2;
			return "카드를 1장 소멸시킵니다. 카드를 " + count + "장 뽑습니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "드로우 2 > 3";
		}
	}

	public static class SecondWindEffect implements DeckCardEffect {
		@Override
		public void apply(DeckCardPlayContext context) {
			int blockPerCard = DeckCardCode.upgradeLevel(context.effectiveCardCode) > 0 ? 7 : 5;
			int exhausted = exhaustMatchingHandCards(context, new HandCardPredicate() {
				@Override
				public boolean matches(DeckCard card) {
					return card.type != DeckCardType.ATTACK;
				}
			});
			if (exhausted > 0) {
				context.result.block += context.combat.gainBlock(blockPerCard * exhausted);
				context.result.exhausted = true;
			}
		}

		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			int block = DeckCardCode.upgradeLevel(cardCode) > 0 ? 7 : 5;
			return "손에 있는 공격이 아닌 모든 카드를 소멸시킵니다. 소멸시킨 카드 1장당 보호막을 " + block + " 얻습니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "보호막 5 > 7";
		}
	}

	public static class NumbnessEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			combat.numbnessBlock += DeckCardCode.upgradeLevel(cardCode) > 0 ? 4 : 3;
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			int block = DeckCardCode.upgradeLevel(cardCode) > 0 ? 4 : 3;
			return "카드가 소멸될 때마다, 보호막을 " + block + " 얻습니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "보호막 3 > 4";
		}
	}

	public static class HellfireEffect implements DeckCardEffect {
		@Override
		public void apply(DeckCardPlayContext context) {
			int exhausted = exhaustMatchingHandCards(context, null);
			int baseDamage = DeckCardCode.upgradeLevel(context.effectiveCardCode) > 0 ? 10 : 7;
			for (int i = 0; i < exhausted; i++) {
				if (i > 0) context.result.nextWave();
				for (DeckCombatEnemy target : targets(context.combat, context.card)) {
					int damage = context.combat.cardDamageFromBase(context.card, context.effectiveCardCode, target, baseDamage);
					int dealt = context.combat.damageEnemy(target, damage, true);
					context.result.addAttackHit(context.combat.enemyIndex(target), dealt);
				}
			}
			context.result.exhausted = context.result.exhausted || exhausted > 0;
		}

		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			int damage = DeckCardCode.upgradeLevel(cardCode) > 0 ? 10 : 7;
			return "손에 있는 모든 카드를 소멸시킵니다. 소멸시킨 카드 1장당 피해를 " + damage + " 줍니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "피해 7 > 10";
		}
	}

	public static class DemonEyeEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			int block = DeckCardCode.upgradeLevel(cardCode) > 0 ? 11 : 8;
			int totalBlock = block;
			if (combat.cardsExhaustedThisTurn > 0) totalBlock += block;
			result.block += combat.gainBlockFromCard(totalBlock);
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			int block = DeckCardCode.upgradeLevel(cardCode) > 0 ? 11 : 8;
			return "보호막을 " + DeckCardText.blockValue(block, combat) + " 얻습니다. 이번 턴 동안 소멸시킨 카드가 있다면 보호막을 추가로 " + DeckCardText.blockValue(block, combat) + " 얻습니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "기본 및 추가 보호막 8 > 11";
		}

		@Override
		public boolean conditionMet(DeckBuilderCombat combat, DeckCard card, int cardCode) {
			return combat.cardsExhaustedThisTurn > 0;
		}
	}

	public static class DarkEmbraceEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			combat.darkEmbraceDraw++;
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return "카드가 소멸될 때마다, 카드를 1장 뽑습니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "비용 2 > 1";
		}
	}

	public static class ForgottenRitualEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			result.exhausted = true;
			if (combat.cardsExhaustedThisTurn <= 0) return;
			int amount = DeckCardCode.upgradeLevel(cardCode) > 0 ? 4 : 3;
			combat.gainEnergy(amount);
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			int amount = DeckCardCode.upgradeLevel(cardCode) > 0 ? 4 : 3;
			return "이번 턴에 카드를 소멸시켰다면, " + amount + " 에너지를 얻습니다. 소멸.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "에너지 3 > 4";
		}

		@Override
		public boolean conditionMet(DeckBuilderCombat combat, DeckCard card, int cardCode) {
			return combat.cardsExhaustedThisTurn > 0;
		}
	}

	public static class EndOfPactEffect extends Damage {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			if (combat.exhaustPile.size() < 3) return;
			super.apply(combat, card, cardCode, result);
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return "소멸된 카드 더미에 카드가 3장 이상 있을 때만 사용할 수 있습니다. 모든 적에게 피해를 " + DeckCardText.damageValue(card, cardCode, combat) + " 줍니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "피해 17 > 23";
		}

		@Override
		public boolean conditionMet(DeckBuilderCombat combat, DeckCard card, int cardCode) {
			return combat.exhaustPile.size() >= 3;
		}
	}

	public static class PummelEffect extends Damage {
		@Override
		public void apply(DeckCardPlayContext context) {
			int bonusDamage = exhaustRandomAttackFromHand(context);
			int baseDamage = context.card.damage(context.effectiveCardCode) + bonusDamage;
			for (int i = 0; i < 2; i++) {
				if (i > 0) context.result.nextWave();
				for (DeckCombatEnemy target : targets(context.combat, context.card)) {
					int damage = context.combat.cardDamageFromBase(context.card, context.effectiveCardCode, target, baseDamage);
					int dealt = context.combat.damageEnemy(target, damage, true);
					context.result.addAttackHit(context.combat.enemyIndex(target), dealt);
				}
			}
		}

		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
		}

		private int exhaustRandomAttackFromHand(DeckCardPlayContext context) {
			ArrayList<Integer> candidates = new ArrayList<>();
			for (int i = 0; i < context.combat.hand.size(); i++) {
				if (i == context.handIndex) continue;
				int code = context.combat.hand.get(i);
				DeckCard handCard = DeckCard.byCode(code);
				if (handCard.type == DeckCardType.ATTACK && !handCard.unplayable(code)) {
					candidates.add(i);
				}
			}
			if (candidates.isEmpty()) return 0;
			int idx = candidates.get(Random.Int(candidates.size()));
			int code = context.combat.hand.get(idx);
			DeckCard handCard = DeckCard.byCode(code);
			int bonusDamage = handCard.damage(code);
			context.result.draw += context.combat.exhaustCard(code);
			context.combat.hand.remove(idx);
			if (idx < context.handIndex) {
				context.combat.currentPlayHandIndexShift--;
			}
			context.result.exhausted = true;
			return bonusDamage;
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return "피해를 " + DeckCardText.damageValue(card, cardCode, combat) + "씩 2번 줍니다. 손에 있는 무작위 공격 카드 1장을 소멸시키고, 그 카드의 피해량을 이 카드에 추가합니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "피해 4 > 6";
		}
	}

	public static class AshenStrikeEffect extends Damage {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			for (DeckCombatEnemy target : targets(combat, card)) {
				int damage = combat.cardDamageFromBase(card, cardCode, target, baseDamage(cardCode, combat));
				int dealt = combat.damageEnemy(target, damage, true);
				result.addAttackHit(combat.enemyIndex(target), dealt);
			}
		}

		private int baseDamage(int cardCode, DeckBuilderCombat combat) {
			int perCard = DeckCardCode.upgradeLevel(cardCode) > 0 ? 4 : 3;
			return 6 + combat.exhaustPile.size() * perCard;
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			int perCard = DeckCardCode.upgradeLevel(cardCode) > 0 ? 4 : 3;
			if (combat == null) return "피해를 6 줍니다. 소멸된 카드 더미에 있는 카드 1장당 피해량이 " + perCard + " 증가합니다.";
			int damage = combat.cardDamageFromBase(card, cardCode, combat.target(), baseDamage(cardCode, combat));
			return "피해를 " + damage + " 줍니다. 소멸된 카드 더미에 있는 카드 1장당 피해량이 " + perCard + " 증가합니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "소멸된 카드당 피해 3 > 4";
		}
	}

	public static class ConclusionEffect extends Damage {
		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return "적 전체에게 피해를 " + DeckCardText.damageValue(card, cardCode, combat) + " 줍니다. 턴을 종료합니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "피해 12 > 16";
		}
	}

	public static class DaggerThrowEffect implements DeckCardEffect {
		@Override
		public void apply(DeckCardPlayContext context) {
			for (DeckCombatEnemy target : targets(context.combat, context.card)) {
				int damage = context.combat.cardDamage(context.card, context.effectiveCardCode, target);
				int dealt = context.combat.damageEnemy(target, damage, true);
				context.result.addAttackHit(context.combat.enemyIndex(target), dealt);
			}
			if (context.combat.draw(1)) {
				context.result.draw++;
			}
		}

		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return "카드를 1장 버립니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "피해 9 > 12";
		}
	}

	public static class NemesisEffect extends Damage {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			super.apply(combat, card, cardCode, result);
			combat.nextTurnBonusDraw += 2;
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return "피해를 " + DeckCardText.damageValue(card, cardCode, combat) + " 줍니다. 다음 턴에 카드를 2장 뽑습니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "피해 15 > 20";
		}
	}

	private interface HandCardPredicate {
		boolean matches(DeckCard card);
	}

	private static int exhaustMatchingHandCards(DeckCardPlayContext context, HandCardPredicate predicate) {
		int exhausted = 0;
		for (int i = context.combat.hand.size() - 1; i >= 0; i--) {
			if (i == context.handIndex) continue;
			int code = context.combat.hand.get(i);
			DeckCard card = DeckCard.byCode(code);
			if (predicate != null && !predicate.matches(card)) continue;
			context.result.draw += context.combat.exhaustCard(code);
			context.combat.hand.remove(i);
			if (i < context.handIndex) {
				context.combat.currentPlayHandIndexShift--;
			}
			exhausted++;
		}
		return exhausted;
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
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) { return "공격력 1 > 2"; }
	}

	public static class BloodlettingEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			combat.loseHP(3, result);
			int gain = DeckCardCode.upgradeLevel(cardCode) > 0 ? 3 : 2;
			combat.gainEnergy(gain);
		}
		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			int gain = DeckCardCode.upgradeLevel(cardCode) > 0 ? 3 : 2;
			return "체력을 3 잃습니다. 에너지를 " + gain + " 얻습니다.";
		}
		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) { return "에너지 2 > 3"; }
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
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) { return "피해 6 > 9"; }
	}

	public static class PoisonCoatEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			combat.poisonCoatDamage += DeckCardCode.upgradeLevel(cardCode) > 0 ? 2 : 1;
		}
		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			int amount = DeckCardCode.upgradeLevel(cardCode) > 0 ? 2 : 1;
			return "공격 카드가 막히지 않은 피해를 줄 때마다, 지속 피해를 " + amount + " 부여합니다.";
		}
		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "지속 피해 1 > 2";
		}
	}

	public static class PersistentDamageEffect implements DeckCardEffect {
		private final int base;
		private final int upgraded;

		public PersistentDamageEffect(int base, int upgraded) {
			this.base = base;
			this.upgraded = upgraded;
		}

		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			int amount = DeckCardCode.upgradeLevel(cardCode) > 0 ? upgraded : base;
			for (DeckCombatEnemy target : targets(combat, card)) {
				combat.applyPersistentDamage(target, amount, result);
			}
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			int amount = DeckCardCode.upgradeLevel(cardCode) > 0 ? upgraded : base;
			return "지속 피해를 " + amount + " 부여합니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "지속 피해 " + base + " > " + upgraded;
		}
	}

	public static class OutbreakEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			combat.outbreakDamage += DeckCardCode.upgradeLevel(cardCode) > 0 ? 15 : 11;
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			int damage = DeckCardCode.upgradeLevel(cardCode) > 0 ? 15 : 11;
			return "지속 피해를 3번 부여할 때마다, 모든 적에게 피해를 " + damage + " 줍니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "피해 11 > 15";
		}
	}

	public static class NoxiousGasEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			combat.noxiousGasDamage += DeckCardCode.upgradeLevel(cardCode) > 0 ? 3 : 2;
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			int amount = DeckCardCode.upgradeLevel(cardCode) > 0 ? 3 : 2;
			return "내 턴 시작 시, 모든 적에게 지속 피해를 " + amount + " 부여합니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "지속 피해 2 > 3";
		}
	}

	public static class RisingPoisonEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			DeckCombatEnemy target = combat.target();
			if (target != null && target.persistentDamage > 0) {
				combat.applyPersistentDamage(target, DeckCardCode.upgradeLevel(cardCode) > 0 ? 12 : 9, result);
			}
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			int amount = DeckCardCode.upgradeLevel(cardCode) > 0 ? 12 : 9;
			return "적이 지속 피해를 보유하고 있다면, 지속 피해를 " + amount + " 부여합니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "지속 피해 9 > 12";
		}

		@Override
		public boolean conditionMet(DeckBuilderCombat combat, DeckCard card, int cardCode) {
			DeckCombatEnemy target = combat.target();
			return target != null && target.persistentDamage > 0;
		}
	}

	public static class MirageEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			int total = 0;
			for (DeckCombatEnemy enemy : combat.aliveEnemies()) {
				total += Math.max(0, enemy.persistentDamage);
			}
			if (total > 0) result.block += combat.gainBlockFromCard(total);
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return "모든 적에게 부여된 지속 피해와 동일한 만큼의 보호막을 얻습니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "비용 1 > 0";
		}
	}

	public static class CatalystEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			combat.catalystTriggers += DeckCardCode.upgradeLevel(cardCode) > 0 ? 2 : 1;
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			int amount = DeckCardCode.upgradeLevel(cardCode) > 0 ? 2 : 1;
			return "지속 피해가 " + amount + "번 추가로 발동합니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "추가 발동 1번 > 2번";
		}
	}

	public static class CorrosiveWaveEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			combat.corrosiveWaveDamage += DeckCardCode.upgradeLevel(cardCode) > 0 ? 3 : 2;
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			int amount = DeckCardCode.upgradeLevel(cardCode) > 0 ? 3 : 2;
			return "이번 턴에 카드를 뽑을 때마다, 모든 적에게 지속 피해를 " + amount + " 부여합니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "지속 피해 2 > 3";
		}
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
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) { return "보호막 16 > 20"; }
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
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) { return "적중 2 > 3번"; }
		@Override
		public boolean conditionMet(DeckBuilderCombat combat, DeckCard card, int cardCode) {
			return combat.playerHPLostCountThisTurn > 0;
		}
	}

	public static class BrandEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			applyBrand(combat, cardCode, result, -1);
		}

		@Override
		public void apply(DeckCardPlayContext context) {
			applyBrand(context.combat, context.effectiveCardCode, context.result, context.handIndex);
		}

		private void applyBrand(DeckBuilderCombat combat, int cardCode, DeckPlayResult.Builder result, int handIndex) {
			combat.loseHP(1, result);
			if (combat.hand.size() > 1 && handIndex >= 0 && handIndex < combat.hand.size()) {
				int idx = Random.Int(combat.hand.size() - 1);
				if (idx >= handIndex) idx++;
				result.draw += combat.exhaustCard(combat.hand.get(idx));
				combat.hand.remove(idx);
				result.exhausted = true;
			} else if (!combat.hand.isEmpty()) {
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
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) { return "공격력 1 > 2"; }
	}

	public static class IndomitableEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			int heal = DeckCardCode.upgradeLevel(cardCode) > 0 ? 13 : 10;
			result.heal += combat.healPlayer(heal);
		}
		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			int heal = DeckCardCode.upgradeLevel(cardCode) > 0 ? 13 : 10;
			return "체력을 " + heal + " 회복합니다.";
		}
		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) { return "회복 10 > 13"; }
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
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) { return "피해 5 > 7"; }
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
			return base == upgraded ? "" : "무작위 카드 " + base + "장 > " + upgraded + "장";
		}
	}

	// 미래 예지: 드로우는 auto-Draw 효과가 처리, 이 효과는 손패>드로우 더미 맨 위 플래그만 설정
	public static class ForesightEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			if (!combat.hand.isEmpty()) {
				combat.pendingHandCardToDrawPileTop = true;
			}
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return "손에 있는 카드를 1장 뽑을 카드 더미 맨 위에 놓습니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "소멸 > 소멸 제거";
		}
	}

	// 비상 단추: 보호막 획득 + 2턴간 카드 보호막 비활성화
	public static class PanicButtonEffect implements DeckCardEffect {
		private final int base;
		private final int upgraded;

		public PanicButtonEffect(int base, int upgraded) {
			this.base = base;
			this.upgraded = upgraded;
		}

		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			int amount = DeckCardCode.upgradeLevel(cardCode) > 0 ? upgraded : base;
			result.block += combat.gainBlockFromCard(amount);
			combat.blockFromCardDisabledTurns = 2;
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			int amount = DeckCardCode.upgradeLevel(cardCode) > 0 ? upgraded : base;
			return "보호막을 " + DeckCardText.blockValue(amount, combat) + " 얻습니다. 2턴 동안 카드를 통해 보호막을 얻을 수 없습니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "보호막 " + base + " > " + upgraded;
		}
	}

	// 생산: 에너지 획득
	public static class GainEnergyEffect implements DeckCardEffect {
		private final int base;
		private final int upgraded;

		public GainEnergyEffect(int base, int upgraded) {
			this.base = base;
			this.upgraded = upgraded;
		}

		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			int amount = DeckCardCode.upgradeLevel(cardCode) > 0 ? upgraded : base;
			combat.gainEnergy(amount);
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			int amount = DeckCardCode.upgradeLevel(cardCode) > 0 ? upgraded : base;
			return "에너지를 " + amount + " 얻습니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return base == upgraded ? "" : "에너지 " + base + " > " + upgraded;
		}
	}

	public static class WispEffect extends GainEnergyEffect {
		public WispEffect() {
			super(1, 1);
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "보존 추가";
		}
	}

	public static class DrawThenDiscardEffect implements DeckCardEffect {
		private final int baseDraw;
		private final int upgradedDraw;
		private final int baseDiscard;
		private final int upgradedDiscard;

		public DrawThenDiscardEffect(int baseDraw, int upgradedDraw, int baseDiscard, int upgradedDiscard) {
			this.baseDraw = baseDraw;
			this.upgradedDraw = upgradedDraw;
			this.baseDiscard = baseDiscard;
			this.upgradedDiscard = upgradedDiscard;
		}

		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			int draw = DeckCardCode.upgradeLevel(cardCode) > 0 ? upgradedDraw : baseDraw;
			int discard = DeckCardCode.upgradeLevel(cardCode) > 0 ? upgradedDiscard : baseDiscard;
			int drawn = drawCards(combat, draw);
			result.draw += drawn;
			if (discard > 0 && !combat.hand.isEmpty()) {
				combat.pendingHandDiscardSelectCount += Math.min(discard, Math.max(0, combat.hand.size() - 1));
			}
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			int draw = DeckCardCode.upgradeLevel(cardCode) > 0 ? upgradedDraw : baseDraw;
			int discard = DeckCardCode.upgradeLevel(cardCode) > 0 ? upgradedDiscard : baseDiscard;
			return "카드를 " + draw + "장 뽑습니다. 카드를 " + discard + "장 버립니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			String text = "";
			if (baseDraw != upgradedDraw) text += "드로우 " + baseDraw + "장 > " + upgradedDraw + "장";
			if (baseDiscard != upgradedDiscard) text += (text.length() > 0 ? ", " : "") + "버림 " + baseDiscard + "장 > " + upgradedDiscard + "장";
			return text;
		}
	}

	public static class CalculatedGambleEffect implements DeckCardEffect {
		@Override
		public void apply(DeckCardPlayContext context) {
			int discarded = 0;
			for (int i = context.combat.hand.size() - 1; i >= 0; i--) {
				if (i == context.handIndex) continue;
				int code = DeckCardCode.withoutCostOverride(context.combat.hand.remove(i));
				context.combat.discardPile.add(code);
				if (i < context.handIndex) context.combat.currentPlayHandIndexShift--;
				discarded++;
			}
			context.result.draw += drawCards(context.combat, discarded);
		}

		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return "손에 있는 모든 카드를 버린 뒤, 버린 카드의 수만큼 카드를 뽑습니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "보존 추가";
		}
	}

	public static class EscapePlanEffect implements DeckCardEffect {
		private final int baseBlock;
		private final int upgradedBlock;

		public EscapePlanEffect(int baseBlock, int upgradedBlock) {
			this.baseBlock = baseBlock;
			this.upgradedBlock = upgradedBlock;
		}

		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			int before = combat.hand.size();
			if (combat.draw(1)) {
				result.draw++;
				if (combat.hand.size() > before) {
					int drawn = combat.hand.get(combat.hand.size() - 1);
					if (DeckCard.byCode(drawn).type == DeckCardType.SKILL) {
						int amount = DeckCardCode.upgradeLevel(cardCode) > 0 ? upgradedBlock : baseBlock;
						result.block += combat.gainBlockFromCard(amount);
					}
				}
			}
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			int amount = DeckCardCode.upgradeLevel(cardCode) > 0 ? upgradedBlock : baseBlock;
			return "카드를 1장 뽑습니다. 뽑은 카드가 보조 카드라면, 보호막을 " + DeckCardText.blockValue(amount, combat) + " 얻습니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "보호막 " + baseBlock + " > " + upgradedBlock;
		}
	}

	public static class AdrenalineEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			int energy = DeckCardCode.upgradeLevel(cardCode) > 0 ? 2 : 1;
			combat.gainEnergy(energy);
			result.draw += drawCards(combat, 2);
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			int energy = DeckCardCode.upgradeLevel(cardCode) > 0 ? 2 : 1;
			return "에너지를 " + energy + " 얻습니다. 카드를 2장 뽑습니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "에너지 1 > 2";
		}
	}

	public static class MentalOverflowEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			int energy = DeckCardCode.upgradeLevel(cardCode) > 0 ? 4 : 3;
			combat.gainEnergy(energy);
			result.draw += drawCards(combat, 2);
			combat.mentalOverflowDemise += 3;
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			int energy = DeckCardCode.upgradeLevel(cardCode) > 0 ? 4 : 3;
			return "에너지를 " + energy + " 얻습니다. 카드를 2장 뽑습니다. 내 턴 시작 시, 자신에게 종언을 3 부여합니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "에너지 3 > 4";
		}
	}

	public static class DomainEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			combat.domainCount++;
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return "내 턴 시작 시, 에너지를 1 얻고 카드를 추가로 1장 뽑습니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "비용 3 > 2";
		}
	}

	public static class ToolsOfTheTradeEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			combat.toolsOfTheTradeCount++;
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return "내 턴 시작 시, 카드를 1장 뽑고 카드를 1장 버립니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "비용 1 > 0";
		}
	}

	public static class RestartEffect implements DeckCardEffect {
		@Override
		public void apply(DeckCardPlayContext context) {
			context.combat.shuffleAllCardsIntoDrawPileExceptHandIndex(context.handIndex);
			int count = DeckCardCode.upgradeLevel(context.effectiveCardCode) > 0 ? 6 : 4;
			context.result.draw += drawCards(context.combat, count);
		}

		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			int count = DeckCardCode.upgradeLevel(cardCode) > 0 ? 6 : 4;
			return "모든 카드를 뽑을 카드 더미에 섞어 넣습니다. 카드를 " + count + "장 뽑습니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "드로우 4장 > 6장";
		}
	}

	public static class MachineLearningEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			combat.machineLearningCount++;
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return "내 턴 시작 시, 카드를 추가로 1장 뽑습니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "선봉 추가";
		}
	}

	public static class HandToDrawPileTopEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			if (!combat.hand.isEmpty()) {
				combat.pendingHandCardToDrawPileTop = true;
			}
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return "손에 있는 카드 1장을 뽑을 카드 더미 맨 위에 놓습니다.";
		}
	}

	public static class PaleBlueDotEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			combat.paleBlueDotDraw += DeckCardCode.upgradeLevel(cardCode) > 0 ? 2 : 1;
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			int draw = DeckCardCode.upgradeLevel(cardCode) > 0 ? 2 : 1;
			return "한 턴에 카드를 5장 이상 사용했다면, 다음 턴 시작 시 카드를 추가로 " + draw + "장 뽑습니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "추가 드로우 1장 > 2장";
		}
	}

	public static class DictatorshipEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			combat.dictatorshipCount++;
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return "내 턴 시작 시, 카드를 1장 뽑고 손에 있는 카드 1장을 소멸시킵니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "선봉 추가";
		}
	}

	public static class ExtortionEffect extends Damage {
		@Override
		public void apply(DeckCardPlayContext context) {
			for (DeckCombatEnemy target : targets(context.combat, context.card)) {
				int damage = context.combat.cardDamage(context.card, context.effectiveCardCode, target);
				int dealt = context.combat.damageEnemy(target, damage, true);
				context.result.addAttackHit(context.combat.enemyIndex(target), dealt);
			}
			while (true) {
				int before = context.combat.hand.size();
				int drawn = context.combat.drawOneAndReturnCode();
				if (drawn == -1) break;
				if (context.combat.hand.size() > before) context.result.draw++;
				if (DeckCard.byCode(drawn).type != DeckCardType.ATTACK) break;
			}
		}

		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return DeckCardText.damageRulesText(card, cardCode, combat) + " 공격이 아닌 카드를 뽑을 때까지 카드를 뽑습니다.";
		}
	}

	public static class BattleHypnosisEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			int count = DeckCardCode.upgradeLevel(cardCode) > 0 ? 4 : 3;
			result.draw += drawCards(combat, count);
			combat.drawDisabledThisTurn = true;
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			int count = DeckCardCode.upgradeLevel(cardCode) > 0 ? 4 : 3;
			return "카드를 " + count + "장 뽑습니다. 이번 턴 동안 더 이상 카드를 뽑을 수 없습니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "드로우 3장 > 4장";
		}
	}

	public static class BattleDrumsEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			int energy = DeckCardCode.upgradeLevel(cardCode) > 0 ? 3 : 2;
			return "이 카드가 소멸될 시, 에너지를 " + energy + " 얻습니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "에너지 2 > 3";
		}
	}

	public static class OfferingEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			combat.loseHP(6);
			combat.gainEnergy(2);
			int count = DeckCardCode.upgradeLevel(cardCode) > 0 ? 5 : 3;
			result.draw += drawCards(combat, count);
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			int count = DeckCardCode.upgradeLevel(cardCode) > 0 ? 5 : 3;
			return "체력을 6 잃습니다. 에너지를 2 얻습니다. 카드를 " + count + "장 뽑습니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "드로우 3장 > 5장";
		}
	}

	public static class NextTurnEnergyEffect implements DeckCardEffect {
		private final int base;
		private final int upgraded;

		public NextTurnEnergyEffect(int base, int upgraded) {
			this.base = base;
			this.upgraded = upgraded;
		}

		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			combat.nextTurnEnergy += DeckCardCode.upgradeLevel(cardCode) > 0 ? upgraded : base;
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			int amount = DeckCardCode.upgradeLevel(cardCode) > 0 ? upgraded : base;
			return "다음 턴에 에너지를 " + amount + " 얻습니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return base == upgraded ? "" : "에너지 " + base + " > " + upgraded;
		}
	}

	public static class DeathDanceEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			combat.deathDanceBlock += DeckCardCode.upgradeLevel(cardCode) > 0 ? 6 : 4;
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			int amount = DeckCardCode.upgradeLevel(cardCode) > 0 ? 6 : 4;
			return "비용이 2 이상인 카드를 사용할 때마다, 보호막을 " + amount + " 얻습니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "보호막 4 > 6";
		}
	}

	public static class ShuffleCopyToDiscardEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			combat.discardPile.add(DeckCardCode.withoutCostOverride(cardCode));
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return "이 카드의 복사본을 1장 버린 카드 더미에 섞어 넣습니다.";
		}
	}

	public static class SurvivorRemakeEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			DeckCombatEnemy target = combat.target();
			if (target == null || !target.alive()) return;
			target.block = 0;
			target.artifact = 0;
			target.vulnerable += 2;
			result.addHit(combat.enemyIndex(target), 0, 2);
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return "대상 적의 모든 보호막과 정화의 보호막을 제거합니다. 피해 증폭을 2 부여합니다.";
		}
	}

	public static class BlurEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			combat.preserveBlockNextTurn = true;
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return "다음 턴 시작 시 보호막이 사라지지 않습니다.";
		}
	}

	public static class ShadowStealthEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			combat.shadowBlockDouble = true;
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return "이번 턴 동안 얻는 보호막이 2배가 됩니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "비용 1 > 0";
		}
	}

	public static class AfterimageEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			combat.afterimageBlock++;
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return "카드를 사용할 때마다, 보호막을 1 얻습니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "선봉 추가";
		}
	}

	public static class GeneticAlgorithmEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			combat.pendingGeneticAlgorithmGrowth += DeckCardCode.upgradeLevel(cardCode) > 0 ? 4 : 3;
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			int growth = DeckCardCode.upgradeLevel(cardCode) > 0 ? 4 : 3;
			return "이 카드로 얻는 보호막이 영구적으로 " + growth + " 증가합니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "보호막 증가 3 > 4";
		}
	}

	public static class ReaperScytheEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			combat.pendingReaperScytheGrowth += DeckCardCode.upgradeLevel(cardCode) > 0 ? 5 : 4;
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			int growth = DeckCardCode.upgradeLevel(cardCode) > 0 ? 5 : 4;
			return "이 카드의 피해량이 영구적으로 " + growth + " 증가합니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "피해량 증가 4 > 5";
		}
	}

	public static class DiscardToDrawTopEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			if (!combat.discardPile.isEmpty()) combat.pendingDiscardToDrawPileTop = true;
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return "버린 카드 더미의 카드 1장을 뽑을 카드 더미 맨 위에 놓습니다.";
		}
	}

	public static class HologramEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			if (!combat.discardPile.isEmpty()) combat.pendingDiscardHandSelectCount = 1;
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return "버린 카드 더미에서 카드를 1장 손으로 가져옵니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "소멸 제거";
		}
	}

	public static class AuthorityEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			DeckCard[] pool = DeckCardPool.rewardPool(DeckBuilderRun.heroClass(), false, true);
			ArrayList<DeckCard> commons = new ArrayList<>();
			for (DeckCard c : pool) {
				if (c.rarity == DeckCardRarity.COMMON) commons.add(c);
			}
			if (commons.isEmpty()) return;
			int code = commons.get(Random.Int(commons.size())).code();
			if (DeckCardCode.upgradeLevel(cardCode) > 0) code = DeckCardCode.upgrade(code);
			combat.addToHand(code);
			result.draw++;
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return "무작위 " + (DeckCardCode.upgradeLevel(cardCode) > 0 ? "강화된 " : "") + "공용 카드를 1장 손으로 가져옵니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "보호막 7 > 8, 강화된 공용 카드";
		}
	}

	public static class NextTurnBlockEffect implements DeckCardEffect {
		private final int base;
		private final int upgraded;

		public NextTurnBlockEffect(int base, int upgraded) {
			this.base = base;
			this.upgraded = upgraded;
		}

		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			combat.nextTurnBlock += DeckCardCode.upgradeLevel(cardCode) > 0 ? upgraded : base;
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			int amount = DeckCardCode.upgradeLevel(cardCode) > 0 ? upgraded : base;
			return "다음 턴에 보호막을 " + amount + " 얻습니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return base == upgraded ? "" : "다음 보호막 " + base + " > " + upgraded;
		}
	}

	public static class RageBlockEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			combat.rageBlock += DeckCardCode.upgradeLevel(cardCode) > 0 ? 5 : 3;
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			int amount = DeckCardCode.upgradeLevel(cardCode) > 0 ? 5 : 3;
			return "이번 턴에 공격 카드를 사용할 때마다, 보호막을 " + amount + " 얻습니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "보호막 3 > 5";
		}
	}

	public static class AbsolutePowerEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			combat.absolutePowerDamage += DeckCardCode.upgradeLevel(cardCode) > 0 ? 7 : 5;
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			int amount = DeckCardCode.upgradeLevel(cardCode) > 0 ? 7 : 5;
			return "보호막을 얻을 때마다, 무작위 적에게 피해를 " + amount + " 줍니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "피해 5 > 7";
		}
	}

	public static class UpgradeRandomDiscardEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			int count = DeckCardCode.upgradeLevel(cardCode) > 0 ? 3 : 2;
			for (int i = 0; i < count; i++) {
				ArrayList<Integer> candidates = new ArrayList<>();
				for (int idx = 0; idx < combat.discardPile.size(); idx++) {
					int code = combat.discardPile.get(idx);
					if (DeckCardCode.upgrade(code) != code) candidates.add(idx);
				}
				if (candidates.isEmpty()) return;
				int idx = candidates.get(Random.Int(candidates.size()));
				combat.discardPile.set(idx, DeckCardCode.upgrade(combat.discardPile.get(idx)));
			}
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			int count = DeckCardCode.upgradeLevel(cardCode) > 0 ? 3 : 2;
			return "버린 카드 더미에 있는 무작위 카드를 " + count + "장 강화합니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "피해 10 > 12, 카드 강화 2장 > 3장";
		}
	}

	public static class DecayEffect extends Damage {
		@Override
		public void apply(DeckCardPlayContext context) {
			super.apply(context);
			DeckCombatEnemy target = context.combat.target();
			if (target != null && target.alive() && context.combat.applyEnemyDebuff(target)) {
				target.debuffDoubleTurns += DeckCardCode.upgradeLevel(context.effectiveCardCode) > 0 ? 3 : 2;
			}
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			int turns = DeckCardCode.upgradeLevel(cardCode) > 0 ? 3 : 2;
			return DeckCardText.damageRulesText(card, cardCode, combat) + " 다음 " + turns + "턴 동안 이 적을 대상으로 하는 피해 증폭과 공격력 저하의 효과가 2배가 됩니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "피해 10 > 12, 지속 2턴 > 3턴";
		}
	}

	public static class DeathMarchEffect extends Damage {
		@Override
		public void apply(DeckCardPlayContext context) {
			for (DeckCombatEnemy target : targets(context.combat, context.card)) {
				int damage = context.combat.cardDamage(context.card, context.effectiveCardCode, target);
				int dealt = context.combat.damageEnemy(target, damage, true);
				context.result.addAttackHit(context.combat.enemyIndex(target), dealt);
			}
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			int bonus = DeckCardCode.upgradeLevel(cardCode) > 0 ? 6 : 4;
			return DeckCardText.damageRulesText(card, cardCode, combat) + " 내 턴 동안 뽑은 카드 1장당 피해량이 " + bonus + " 증가합니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "피해 8 > 9, 피해량 증가 4 > 6";
		}
	}

	public static class MiseryEffect extends Damage {
		@Override
		public void apply(DeckCardPlayContext context) {
			super.apply(context);
			DeckCombatEnemy target = context.combat.target();
			if (target == null) return;
			for (DeckCombatEnemy enemy : context.combat.aliveEnemies()) {
				if (enemy == target) continue;
				if (!context.combat.applyEnemyDebuff(enemy)) continue;
				enemy.vulnerable = Math.max(enemy.vulnerable, target.vulnerable);
				enemy.attackDown = Math.max(enemy.attackDown, target.attackDown);
				enemy.blockReduction = Math.max(enemy.blockReduction, target.blockReduction);
				enemy.venom = Math.max(enemy.venom, target.venom);
				enemy.demise = Math.max(enemy.demise, target.demise);
				enemy.persistentDamage = Math.max(enemy.persistentDamage, target.persistentDamage);
				enemy.debuffDoubleTurns = Math.max(enemy.debuffDoubleTurns, target.debuffDoubleTurns);
				enemy.strangleHpLoss = Math.max(enemy.strangleHpLoss, target.strangleHpLoss);
			}
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return DeckCardText.damageRulesText(card, cardCode, combat) + " 대상 적에게 부여된 모든 해로운 효과를 다른 모든 적에게 부여합니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "보존 추가, 피해 7 > 9";
		}
	}

	public static class XMultiHitEffect extends Damage {
		private final int base;
		private final int upgraded;

		public XMultiHitEffect(int base, int upgraded) {
			this.base = base;
			this.upgraded = upgraded;
		}

		@Override
		public void apply(DeckCardPlayContext context) {
			int hits = Math.max(0, context.xValue);
			for (int i = 0; i < hits; i++) {
				if (i > 0) context.result.nextWave();
				for (DeckCombatEnemy target : targets(context.combat, context.card)) {
					int damage = context.combat.cardDamage(context.card, context.effectiveCardCode, target);
					int dealt = context.combat.damageEnemy(target, damage, true);
					context.result.addAttackHit(context.combat.enemyIndex(target), dealt);
				}
			}
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			int damage = DeckCardCode.upgradeLevel(cardCode) > 0 ? upgraded : base;
			return "피해를 " + damage + "만큼 X번 줍니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "피해 " + base + " > " + upgraded;
		}
	}

	public static class HangingEffect extends Damage {
		private final int base;
		private final int upgraded;

		public HangingEffect(int base, int upgraded) {
			this.base = base;
			this.upgraded = upgraded;
		}

		@Override
		public void apply(DeckCardPlayContext context) {
			for (DeckCombatEnemy target : targets(context.combat, context.card)) {
				int damage = context.combat.cardDamage(context.card, context.effectiveCardCode, target);
				if (target.hanged) damage *= 2;
				int dealt = context.combat.damageEnemy(target, damage, true);
				context.result.addAttackHit(context.combat.enemyIndex(target), dealt);
				if (!target.hanged && context.combat.applyEnemyDebuff(target)) {
					target.hanged = true;
				}
			}
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			int damage = DeckCardCode.upgradeLevel(cardCode) > 0 ? upgraded : base;
			return "피해를 " + damage + " 줍니다. 이 카드가 해당 적에게 가하는 피해량이 2배로 증가합니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "피해 " + base + " > " + upgraded;
		}
	}

	public static class GigaDrillBreakEffect extends Damage {
		private final int base;
		private final int upgraded;

		public GigaDrillBreakEffect(int base, int upgraded) {
			this.base = base;
			this.upgraded = upgraded;
		}

		@Override
		public void apply(DeckCardPlayContext context) {
			int x = Math.max(0, context.xValue);
			int hits = x >= 4 ? x * 2 : x;
			for (int i = 0; i < hits; i++) {
				if (i > 0) context.result.nextWave();
				for (DeckCombatEnemy target : targets(context.combat, context.card)) {
					int damage = context.combat.cardDamage(context.card, context.effectiveCardCode, target);
					int dealt = context.combat.damageEnemy(target, damage, true);
					context.result.addAttackHit(context.combat.enemyIndex(target), dealt);
				}
			}
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			int damage = DeckCardCode.upgradeLevel(cardCode) > 0 ? upgraded : base;
			return "피해를 " + damage + "만큼 X번 줍니다. X가 4 이상이라면 X가 2배가 됩니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "피해 " + base + " > " + upgraded;
		}
	}

	public static class DoubleAllEnemiesEffect extends Damage {
		@Override
		public void apply(DeckCardPlayContext context) {
			for (int i = 0; i < 2; i++) {
				if (i > 0) context.result.nextWave();
				for (DeckCombatEnemy target : targets(context.combat, context.card)) {
					int damage = context.combat.cardDamage(context.card, context.effectiveCardCode, target);
					int dealt = context.combat.damageEnemy(target, damage, true);
					context.result.addAttackHit(context.combat.enemyIndex(target), dealt);
				}
			}
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return "모든 적에게 피해를 " + DeckCardText.damageValue(card, cardCode, combat) + "씩 2번 줍니다.";
		}
	}

	public static class FinisherEffect extends Damage {
		@Override
		public void apply(DeckCardPlayContext context) {
			int hits = Math.max(0, context.combat.attackCardsThisTurn);
			for (int i = 0; i < hits; i++) {
				if (i > 0) context.result.nextWave();
				for (DeckCombatEnemy target : targets(context.combat, context.card)) {
					int damage = context.combat.cardDamage(context.card, context.effectiveCardCode, target);
					int dealt = context.combat.damageEnemy(target, damage, true);
					context.result.addAttackHit(context.combat.enemyIndex(target), dealt);
				}
			}
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return "이번 턴에 사용한 공격 카드 1장당 피해를 " + card.damage(cardCode) + " 줍니다.";
		}
	}

	public static class StrangleEffect extends Damage {
		@Override
		public void apply(DeckCardPlayContext context) {
			super.apply(context);
			DeckCombatEnemy target = context.combat.target();
			if (target != null && target.alive() && context.combat.applyEnemyDebuff(target)) {
				target.strangleHpLoss += DeckCardCode.upgradeLevel(context.effectiveCardCode) > 0 ? 3 : 2;
			}
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			int loss = DeckCardCode.upgradeLevel(cardCode) > 0 ? 3 : 2;
			return DeckCardText.damageRulesText(card, cardCode, combat) + " 이번 턴에 카드를 사용할 때마다, 대상 적이 체력을 " + loss + " 잃습니다.";
		}
	}

	public static class FlechettesEffect extends Damage {
		@Override
		public void apply(DeckCardPlayContext context) {
			int skills = 0;
			for (int code : context.combat.hand) {
				if (DeckCard.byCode(code).type == DeckCardType.SKILL) skills++;
			}
			for (int i = 0; i < skills; i++) {
				if (i > 0) context.result.nextWave();
				for (DeckCombatEnemy target : targets(context.combat, context.card)) {
					int damage = context.combat.cardDamage(context.card, context.effectiveCardCode, target);
					int dealt = context.combat.damageEnemy(target, damage, true);
					context.result.addAttackHit(context.combat.enemyIndex(target), dealt);
				}
			}
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return "손에 있는 보조 카드 1장당 피해를 " + card.damage(cardCode) + " 줍니다.";
		}
	}

	public static class MoonBaptismEffect extends Damage {
		@Override
		public void apply(DeckCardPlayContext context) {
			int hits = Math.max(0, context.combat.skillCardsThisTurn);
			for (int i = 0; i < hits; i++) {
				if (i > 0) context.result.nextWave();
				for (DeckCombatEnemy target : targets(context.combat, context.card)) {
					int damage = context.combat.cardDamage(context.card, context.effectiveCardCode, target);
					int dealt = context.combat.damageEnemy(target, damage, true);
					context.result.addAttackHit(context.combat.enemyIndex(target), dealt);
				}
			}
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return "이번 턴에 사용한 보조 카드 1장당 피해를 " + card.damage(cardCode) + " 줍니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "피해 4 > 5";
		}
	}

	public static class MeltingPunchEffect extends Damage {
		@Override
		public void apply(DeckCardPlayContext context) {
			super.apply(context);
			DeckCombatEnemy target = context.combat.target();
			if (target != null && target.alive() && target.vulnerable > 0 && context.combat.applyEnemyDebuff(target)) {
				target.vulnerable *= 2;
			}
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return DeckCardText.damageRulesText(card, cardCode, combat) + " 적이 보유한 피해 증폭이 2배로 증가합니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "피해 10 > 14";
		}
	}

	public static class ExhaustRandomHandCardEffect extends Damage {
		@Override
		public void apply(DeckCardPlayContext context) {
			super.apply(context);
			ArrayList<Integer> candidates = new ArrayList<>();
			for (int i = 0; i < context.combat.hand.size(); i++) {
				if (i != context.handIndex) candidates.add(i);
			}
			if (candidates.isEmpty()) return;
			int idx = candidates.get(Random.Int(candidates.size()));
			context.result.draw += context.combat.exhaustCard(context.combat.hand.remove(idx));
			if (idx < context.handIndex) context.combat.currentPlayHandIndexShift--;
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return DeckCardText.damageRulesText(card, cardCode, combat) + " 무작위 카드를 1장 소멸시킵니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "피해 18 > 24";
		}
	}

	public static class RampageEffect extends Damage {
		@Override
		public void apply(DeckCardPlayContext context) {
			super.apply(context);
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			int amount = DeckCardCode.upgradeLevel(cardCode) > 0 ? 9 : 5;
			return DeckCardText.damageRulesText(card, cardCode, combat) + " 이번 전투 동안 이 카드의 피해량이 " + amount + " 증가합니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "증가하는 피해량 5 > 9";
		}
	}

	public static class DismantleEffect extends Damage {
		@Override
		public void apply(DeckCardPlayContext context) {
			DeckCombatEnemy target = context.combat.target();
			int hits = target != null && target.vulnerable > 0 ? 2 : 1;
			for (int i = 0; i < hits; i++) {
				if (i > 0) context.result.nextWave();
				for (DeckCombatEnemy enemy : targets(context.combat, context.card)) {
					int damage = context.combat.cardDamage(context.card, context.effectiveCardCode, enemy);
					int dealt = context.combat.damageEnemy(enemy, damage, true);
					context.result.addAttackHit(context.combat.enemyIndex(enemy), dealt);
				}
			}
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return DeckCardText.damageRulesText(card, cardCode, combat) + " 대상 적이 피해 증폭 상태라면, 2번 적중합니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "피해 8 > 10";
		}
	}

	public static class ComeAtMeEffect extends Damage {
		@Override
		public void apply(DeckCardPlayContext context) {
			for (int i = 0; i < 2; i++) {
				if (i > 0) context.result.nextWave();
				for (DeckCombatEnemy target : targets(context.combat, context.card)) {
					int damage = context.combat.cardDamage(context.card, context.effectiveCardCode, target);
					int dealt = context.combat.damageEnemy(target, damage, true);
					context.result.addAttackHit(context.combat.enemyIndex(target), dealt);
				}
			}
			int strength = context.card.strength(context.effectiveCardCode);
			context.combat.playerStrength += strength;
			context.result.strength += strength;
			DeckCombatEnemy target = context.combat.target();
			if (target != null && target.alive()) target.strength += 1;
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return "피해를 " + DeckCardText.damageValue(card, cardCode, combat) + "만큼 2번 줍니다. 공격력을 " + card.strength(cardCode) + " 얻습니다. 대상 적이 공격력을 1 얻습니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "피해 5 > 6, 공격력 3 > 4";
		}
	}

	public static class MercilessEffect extends Damage {
		@Override
		public void apply(DeckCardPlayContext context) {
			super.apply(context);
			if (!context.castOnDraw) context.combat.nextAttackZeroCost = true;
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return DeckCardText.damageRulesText(card, cardCode, combat) + " 다음에 사용하는 공격 카드의 비용이 0이 됩니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "피해 14 > 20";
		}
	}

	public static class FeedEffect extends Damage {
		@Override
		public void apply(DeckCardPlayContext context) {
			int killsBefore = context.combat.playKillCount;
			super.apply(context);
			if (context.combat.playKillCount > killsBefore) {
				DeckBuilderRun.playerHT += DeckCardCode.upgradeLevel(context.effectiveCardCode) > 0 ? 4 : 3;
			}
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			int amount = DeckCardCode.upgradeLevel(cardCode) > 0 ? 4 : 3;
			return DeckCardText.damageRulesText(card, cardCode, combat) + " 해당 카드로 적을 처치했다면, 최대 체력이 " + amount + " 증가합니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "피해 10 > 12, 최대 체력 증가 3 > 4";
		}
	}

	public static class HemokinesisEffect extends Damage {
		@Override
		public void apply(DeckCardPlayContext context) {
			super.apply(context);
			DeckCombatEnemy target = context.combat.target();
			if (target != null && target.alive()) {
				target.turnStrengthLoss += DeckCardCode.upgradeLevel(context.effectiveCardCode) > 0 ? 15 : 10;
			}
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			int amount = DeckCardCode.upgradeLevel(cardCode) > 0 ? 15 : 10;
			return DeckCardText.damageRulesText(card, cardCode, combat) + " 이번 턴 동안 적이 공격력을 " + amount + " 잃습니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "피해 15 > 20, 공격력 감소 10 > 15";
		}
	}

	public static class LifeSupportEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			combat.gainEnergy(DeckCardCode.upgradeLevel(cardCode) > 0 ? 6 : 4);
			combat.cardCostIncreaseThisTurn += 1;
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			int amount = DeckCardCode.upgradeLevel(cardCode) > 0 ? 6 : 4;
			return "에너지를 " + amount + " 얻습니다. 이번 턴 동안 카드의 비용이 1 증가합니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "에너지 4 > 6";
		}
	}

	public static class FriendshipEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			int loss = DeckCardCode.upgradeLevel(cardCode) > 0 ? 1 : 2;
			combat.playerStrength -= loss;
			result.strength -= loss;
			combat.friendshipEnergy += 1;
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			int loss = DeckCardCode.upgradeLevel(cardCode) > 0 ? 1 : 2;
			return "공격력을 " + loss + " 잃습니다. 매 턴 시작 시 에너지를 1 얻습니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "공격력 감소 2 > 1";
		}
	}

	public static class EnergizerEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			combat.gainEnergy(combat.energy);
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return "에너지가 2배가 됩니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "비용 1 > 0";
		}
	}

	public static class CaptureEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			if (combat.hand.size() > 1) combat.pendingHandExhaustSelectCount++;
			combat.nextTurnEnergy += DeckCardCode.upgradeLevel(cardCode) > 0 ? 2 : 1;
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			int amount = DeckCardCode.upgradeLevel(cardCode) > 0 ? 2 : 1;
			return "카드를 1장 소멸시킵니다. 다음 턴에, 에너지를 " + amount + " 얻습니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "에너지 1 > 2";
		}
	}

	public static class SubroutineEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			combat.subroutineEnergy += 1;
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return "지속 카드를 사용할 때마다, 1 에너지를 얻습니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "비용 1 > 0";
		}
	}

	public static class OrbitEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			combat.orbitCount += 1;
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return "에너지를 4 소모할 때마다, 에너지를 1 얻습니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "비용 2 > 1";
		}
	}

	public static class ReadyForFightEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			int attacks = 0;
			for (int code : combat.hand) {
				if (DeckCard.byCode(code).type == DeckCardType.ATTACK) attacks++;
			}
			combat.gainEnergy(attacks);
			combat.energyGainDisabledThisTurn = true;
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return "손에 있는 공격 카드 1장당 에너지를 얻습니다. 이번 턴에 추가로 에너지를 얻을 수 없습니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "비용 2 > 1";
		}
	}

	public static class HeartOfFireEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			combat.heartOfFireEnergy += DeckCardCode.upgradeLevel(cardCode) > 0 ? 2 : 1;
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			int amount = DeckCardCode.upgradeLevel(cardCode) > 0 ? 2 : 1;
			return "매 턴 시작 시, 에너지를 " + amount + " 얻습니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "에너지 1 > 2";
		}
	}

	public static class RoyaltyEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			combat.royaltyGold += DeckCardCode.upgradeLevel(cardCode) > 0 ? 40 : 30;
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			int amount = DeckCardCode.upgradeLevel(cardCode) > 0 ? 40 : 30;
			return "전투 종료 시, 골드를 " + amount + " 얻습니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "얻는 골드 30 > 40";
		}
	}

	public static class VoidFormEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			combat.voidFormFreeCards += 2;
			combat.endTurnRequested = true;
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return "내 턴을 종료합니다. 매 턴마다 처음으로 내는 카드 2장을 비용 없이 사용합니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "일시적 제거";
		}
	}

	// 성급함: 손에 공격 카드가 없으면 드로우
	public static class ImpatienceEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			boolean hasAttack = false;
			for (int handCode : combat.hand) {
				if (DeckCard.byCode(handCode).type == DeckCardType.ATTACK) {
					hasAttack = true;
					break;
				}
			}
			if (!hasAttack) {
				int count = DeckCardCode.upgradeLevel(cardCode) > 0 ? 3 : 2;
				combat.draw(count);
				result.draw += count;
			}
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			int count = DeckCardCode.upgradeLevel(cardCode) > 0 ? 3 : 2;
			return "손에 공격 카드가 없다면, 카드를 " + count + "장 뽑습니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "드로우 2장 > 3장";
		}

		@Override
		public boolean conditionMet(DeckBuilderCombat combat, DeckCard card, int cardCode) {
			for (int handCode : combat.hand) {
				if (DeckCard.byCode(handCode).type == DeckCardType.ATTACK) return false;
			}
			return true;
		}
	}

	// 어둠의 족쇄: 대상 적의 이번 턴 공격력을 N 감소
	public static class DarkShacklesEffect implements DeckCardEffect {
		private final int base;
		private final int upgraded;

		public DarkShacklesEffect(int base, int upgraded) {
			this.base = base;
			this.upgraded = upgraded;
		}

		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			int amount = DeckCardCode.upgradeLevel(cardCode) > 0 ? upgraded : base;
			DeckCombatEnemy target = combat.target();
			if (target != null && combat.applyEnemyDebuff(target)) {
				target.turnStrengthLoss += amount;
			}
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			int amount = DeckCardCode.upgradeLevel(cardCode) > 0 ? upgraded : base;
			return "이번 턴에 적의 공격력을 " + amount + " 감소시킵니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "공격력 감소 " + base + " > " + upgraded;
		}

	}

	public static class AllEnemyTurnStrengthLossEffect implements DeckCardEffect {
		private final int base;
		private final int upgraded;

		public AllEnemyTurnStrengthLossEffect(int base, int upgraded) {
			this.base = base;
			this.upgraded = upgraded;
		}

		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			int amount = DeckCardCode.upgradeLevel(cardCode) > 0 ? upgraded : base;
			for (DeckCombatEnemy enemy : combat.aliveEnemies()) {
				if (combat.applyEnemyDebuff(enemy)) {
					enemy.turnStrengthLoss += amount;
				}
			}
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			int amount = DeckCardCode.upgradeLevel(cardCode) > 0 ? upgraded : base;
			return "이번 턴 동안 모든 적이 공격력을 " + amount + " 잃습니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "공격력 감소 " + base + " > " + upgraded;
		}
	}

	// 연장: 다음 턴에 현재 보호막만큼 보호막 획득
	public static class ExtendEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			combat.nextTurnBlock += combat.block;
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			if (combat == null) return "다음 턴에 현재 보호막과 동일한 만큼의 보호막을 얻습니다.";
			return "다음 턴에 보호막을 " + combat.block + " 얻습니다. (현재 보호막 기반)";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "소멸 > 소멸 제거";
		}
	}

	// 초조함: 손에 카드가 없으면 드로우 + 에너지
	public static class AnxietyEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			if (combat.hand.size() <= 1) {
				int drawCount = DeckCardCode.upgradeLevel(cardCode) > 0 ? 3 : 2;
				int energyGain = DeckCardCode.upgradeLevel(cardCode) > 0 ? 3 : 2;
				combat.draw(drawCount);
				result.draw += drawCount;
				combat.gainEnergy(energyGain);
			}
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			int drawCount = DeckCardCode.upgradeLevel(cardCode) > 0 ? 3 : 2;
			int energyGain = DeckCardCode.upgradeLevel(cardCode) > 0 ? 3 : 2;
			return "손에 카드가 없다면, 카드를 " + drawCount + "장 뽑고 에너지를 " + energyGain + " 얻습니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "드로우 2장 > 3장, 에너지 2 > 3";
		}

		@Override
		public boolean conditionMet(DeckBuilderCombat combat, DeckCard card, int cardCode) {
			return combat.hand.size() <= 1;
		}
	}

	// 착수: 다른 직업 공격 카드 발견 (비용 0, 강화 시 강화된 카드)
	public static class OtherClassAttackDiscoverEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			HeroClass heroClass = Dungeon.hero != null ? Dungeon.hero.heroClass : null;
			ArrayList<DeckCard> candidates = new ArrayList<>();
			for (DeckCard c : DeckCard.values()) {
				if (c.type == DeckCardType.ATTACK && DeckCardPool.isOtherClassRewardCard(c, heroClass)) {
					candidates.add(c);
				}
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
			combat.pendingDiscoverZeroCost = true;
			combat.pendingDiscoverTransient = false;
			combat.pendingDiscoverUpgraded = DeckCardCode.upgradeLevel(cardCode) > 0;
			combat.pendingDiscoverPlayAfterPick = false;
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			boolean up = DeckCardCode.upgradeLevel(cardCode) > 0;
			return "다른 직업의 무작위 " + (up ? "강화된 " : "") + "공격 카드 3장 중 1장을 발견합니다. 이번 턴 동안 비용 없이 사용할 수 있습니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "무작위 카드 > 무작위 강화된 카드";
		}

		@Override
		public String keywordText(DeckCard card, int cardCode) {
			return "발견: 세 가지 카드 선택지 중 하나를 골라 손으로 가져옵니다.";
		}
	}

	// 평형: 손패 보존 (보호막은 auto-Block이 처리)
	public static class EquilibriumEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			combat.retainHandTurns++;
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return "이번 턴에 손에 있는 카드를 보존합니다.";
		}
	}

	// 싯딤의 상자: 모든 카드 중 1장 선택해 손패로 (디버그용)
	public static class ShittimBoxEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			combat.pendingAllCardDiscover = true;
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return "모든 카드 중 1장을 선택해 손패로 가져옵니다.";
		}
	}

	// 오렌지 폭탄: 3턴 후 모든 적에게 피해
	// 유물 선택 상자: 모든 유물 중 1개 선택 획득(디버그용)
	public static class RelicSelectionBoxEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			combat.pendingAllRelicDiscover = true;
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return "모든 유물 중 1개를 선택해 획득합니다.";
		}
	}

	// 물약 선택 상자: 모든 물약 중 1개 선택 획득(디버그용)
	public static class PotionSelectionBoxEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			combat.pendingAllPotionDiscover = true;
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return "모든 물약 중 1개를 선택해 획득합니다.";
		}
	}

	public static class OrangeBombEffect implements DeckCardEffect {
		private final int base;
		private final int upgraded;

		public OrangeBombEffect(int base, int upgraded) {
			this.base = base;
			this.upgraded = upgraded;
		}

		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			int damage = DeckCardCode.upgradeLevel(cardCode) > 0 ? upgraded : base;
			combat.orangeBombTimers.add(3);
			combat.orangeBombDamages.add(damage);
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			int damage = DeckCardCode.upgradeLevel(cardCode) > 0 ? upgraded : base;
			return "3턴 후 턴 종료 시, 모든 적에게 피해를 " + damage + " 줍니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return base == upgraded ? "" : "피해 " + base + " > " + upgraded;
		}
	}

	// GUARD 카드에 추가: 고정시키기 활성화 시 추가 보호막
	public static class GuardBonusEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			if (combat.guardBlockBonus > 0) {
				result.block += combat.gainBlockFromCard(combat.guardBlockBonus);
			}
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			if (combat == null || combat.guardBlockBonus <= 0) return "";
			return "보호막을 " + DeckCardText.blockValue(combat.guardBlockBonus, combat) + " 추가로 얻습니다. (고정시키기)";
		}
	}

	// 위풍당당: 턴에 카드 5장 사용마다 모든 적에게 피해 (combat.play()에서 처리됨)
	public static class ImpostingPresenceEffect implements DeckCardEffect {
		private final int base;
		private final int upgraded;

		public ImpostingPresenceEffect(int base, int upgraded) {
			this.base = base;
			this.upgraded = upgraded;
		}

		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			int damage = DeckCardCode.upgradeLevel(cardCode) > 0 ? upgraded : base;
			combat.impostingPresenceDamage += damage;
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			int damage = DeckCardCode.upgradeLevel(cardCode) > 0 ? upgraded : base;
			return "한 턴에 카드를 5장 사용할 때마다, 모든 적에게 피해를 " + damage + " 줍니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return base == upgraded ? "" : "피해 " + base + " > " + upgraded;
		}
	}

	// 구렁이의 형상: 카드 사용 때마다 무작위 적에게 피해
	public static class SnakeFormEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			int damage = DeckCardCode.upgradeLevel(cardCode) > 0 ? 4 : 2;
			combat.snakeFormDamage += damage;
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			int damage = DeckCardCode.upgradeLevel(cardCode) > 0 ? 4 : 2;
			return "카드를 사용할 때마다, 무작위 적에게 피해를 " + damage + " 줍니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "피해 4 > 6";
		}
	}

	// 고정시키기: GUARD 카드 추가 보호막
	public static class AnchorEffect implements DeckCardEffect {
		private final int base;
		private final int upgraded;

		public AnchorEffect(int base, int upgraded) {
			this.base = base;
			this.upgraded = upgraded;
		}

		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			int amount = DeckCardCode.upgradeLevel(cardCode) > 0 ? upgraded : base;
			combat.guardBlockBonus += amount;
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			int amount = DeckCardCode.upgradeLevel(cardCode) > 0 ? upgraded : base;
			return "기본 브로치 카드를 통해 얻는 보호막이 " + amount + " 증가합니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return base == upgraded ? "" : "보호막 " + base + " > " + upgraded;
		}
	}

	// 기량: 공격력 + 방어력 증가
	public static class ProwessEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			int amount = DeckCardCode.upgradeLevel(cardCode) > 0 ? 2 : 1;
			combat.playerStrength += amount;
			result.strength += amount;
			combat.playerDexterity += amount;
			result.dexterity += amount;
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			int amount = DeckCardCode.upgradeLevel(cardCode) > 0 ? 2 : 1;
			return "공격력을 " + amount + " 얻습니다. 방어력 증가를 " + amount + " 얻습니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "공격력 1 > 2, 방어력 증가 1 > 2";
		}

		@Override
		public String keywordText(DeckCard card, int cardCode) {
			return "방어력 증가: 공격/보조 카드로 얻는 보호막이 해당 수치만큼 증가합니다.";
		}
	}

	// 자동화: 카드 10장 뽑을 때마다 에너지 1 (draw()에서 처리됨)
	public static class AutomationEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			combat.automationCount++;
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return "카드를 10장 뽑을 때마다, 에너지를 1 얻습니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "비용 1 > 0";
		}
	}

	// 책략: 덱 리셔플 시 카드 1장 선택 (refillDrawPileFromDiscard에서 처리됨)
	public static class StratagemEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			combat.stratagemCount++;
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return "뽑을 카드 더미를 섞을 때마다, 카드를 1장 선택해 손으로 가져옵니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "비용 1 > 0";
		}
	}

	// 강철의 섬광: damage + draw handled by auto-effects (no special class needed)

	// 만물 절단: auto-Damage hits primary, this effect hits all other enemies for same damage
	public static class CleaveAllEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			DeckCombatEnemy primary = combat.target();
			for (DeckCombatEnemy enemy : combat.aliveEnemies()) {
				if (enemy == primary) continue;
				int dmg = combat.cardDamage(card, cardCode, enemy);
				int dealt = combat.damageEnemy(enemy, dmg, true);
				result.addAttackHit(combat.enemyIndex(enemy), dealt);
			}
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return "가한 피해량만큼 다른 모든 적에게 피해를 줍니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "피해 8 > 11";
		}
	}

	// 고동치는 도끼: 다음 턴 시작 시 손으로 복귀
	public static class PulsingAxeEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			combat.pulsingAxeReturns.add(cardCode);
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return "다음 턴 시작 시, 이 카드를 손으로 다시 가져옵니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "피해 11 > 14";
		}
	}

	// 탐색 타격: 뽑을 카드 더미 상위 3장 중 1장 선택해 손으로
	public static class ScoutStrikeEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			if (!combat.drawPile.isEmpty()) {
				combat.pendingDrawPilePeek = true;
			}
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return "뽑을 카드 더미의 카드 3장 중 1장을 선택해 손으로 가져옵니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "피해 9 > 12";
		}
	}

	public static class RetainHandThisTurnEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			combat.retainHandTurns = Math.max(combat.retainHandTurns, 1);
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return "이번 턴에 손에 있는 카드를 보존합니다.";
		}
	}

	public static class GoldenAxeEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			for (DeckCombatEnemy target : targets(combat, card)) {
				int damage = combat.cardDamageFromBase(card, cardCode, target, Math.max(0, combat.cardsPlayedThisCombat));
				int dealt = combat.damageEnemy(target, damage, true);
				result.addAttackHit(combat.enemyIndex(target), dealt);
			}
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			if (combat == null) return "이번 전투 동안 사용한 카드의 수와 동일한 만큼의 피해를 줍니다.";
			int damage = combat.cardDamageFromBase(card, cardCode, combat.target(), Math.max(0, combat.cardsPlayedThisCombat));
			return "이번 전투 동안 사용한 카드의 수와 동일한 만큼의 피해를 줍니다. (현재 " + damage + ")";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "보존 추가";
		}
	}

	public static class DecentStrategyEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			combat.strategyRetainCount += DeckCardCode.upgradeLevel(cardCode) > 0 ? 2 : 1;
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			int count = DeckCardCode.upgradeLevel(cardCode) > 0 ? 2 : 1;
			return "내 턴 종료 시, 카드를 최대 " + count + "장까지 보존합니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "보존 1장 > 2장";
		}
	}

	public static class SpeedsterEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			combat.speedsterDamage += 2;
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return "내 턴 동안 카드를 뽑을 때마다, 모든 적에게 피해를 2 줍니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "선봉 부여";
		}
	}

	public static class TrackEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			combat.trackAttackDown++;
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return "공격력 저하 상태의 적이 공격 카드로 받는 피해가 2배가 됩니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "비용 2 > 1";
		}
	}

	public static class DemonFormEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			combat.strengthPerTurn += DeckCardCode.upgradeLevel(cardCode) > 0 ? 3 : 2;
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			int amount = DeckCardCode.upgradeLevel(cardCode) > 0 ? 3 : 2;
			return "내 턴 시작 시, 공격력을 " + amount + " 얻습니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "공격력 2 > 3";
		}
	}

	public static class CorruptionEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			combat.corruptionCount++;
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return "보조 카드의 비용이 0이 됩니다. 보조 카드를 사용할 때마다 그 카드를 소멸시킵니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "비용 3 > 2";
		}
	}

	public static class RipAndTearEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			for (DeckCombatEnemy target : targets(combat, card)) {
				int damage = combat.cardDamageFromBase(card, cardCode, target, baseDamage(cardCode) + debuffTypes(target) * bonusDamage(cardCode));
				int dealt = combat.damageEnemy(target, damage, true);
				result.addAttackHit(combat.enemyIndex(target), dealt);
			}
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			int bonus = bonusDamage(cardCode);
			if (combat == null) {
				return "피해를 " + baseDamage(cardCode) + " 줍니다. 적이 보유한 해로운 효과의 종류 하나당 피해량이 " + bonus + " 증가합니다.";
			}
			DeckCombatEnemy target = combat.target();
			int damage = combat.cardDamageFromBase(card, cardCode, target, baseDamage(cardCode) + debuffTypes(target) * bonus);
			return "피해를 " + damage + " 줍니다. 적이 보유한 해로운 효과의 종류 하나당 피해량이 " + bonus + " 증가합니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "피해 15 > 18\n증가 피해 8 > 11";
		}

		private int baseDamage(int cardCode) {
			return DeckCardCode.upgradeLevel(cardCode) > 0 ? 18 : 15;
		}

		private int bonusDamage(int cardCode) {
			return DeckCardCode.upgradeLevel(cardCode) > 0 ? 11 : 8;
		}

		private int debuffTypes(DeckCombatEnemy enemy) {
			if (enemy == null) return 0;
			int count = 0;
			if (enemy.vulnerable > 0) count++;
			if (enemy.attackDown > 0) count++;
			if (enemy.turnStrengthLoss > 0) count++;
			if (enemy.blockReduction > 0) count++;
			if (enemy.venom > 0) count++;
			if (enemy.demise > 0) count++;
			if (enemy.persistentDamage > 0) count++;
			return count;
		}
	}

	public static class GreedHandEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			if (combat.playKillCount > 0) {
				int amount = DeckCardCode.upgradeLevel(cardCode) > 0 ? 25 : 20;
				DeckBuilderRun.gainGold(amount);
				result.gold += amount;
			}
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			int gold = DeckCardCode.upgradeLevel(cardCode) > 0 ? 25 : 20;
			return "해당 카드로 적을 처치했다면, 골드를 " + gold + " 얻습니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "골드 20 > 25";
		}
	}

	public static class JackpotEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			ArrayList<DeckCard> candidates = new ArrayList<>();
			for (DeckCard candidate : DeckCard.rewardPool(DeckBuilderRun.heroClass(), false, false)) {
				if (candidate.cost(candidate.code()) == 0) candidates.add(candidate);
			}
			if (candidates.isEmpty()) return;
			for (int i = 0; i < 3; i++) {
				DeckCard picked = candidates.get(Random.Int(candidates.size()));
				int code = picked.code();
				if (DeckCardCode.upgradeLevel(cardCode) > 0) code = DeckCardCode.upgrade(code);
				combat.addToHand(code);
			}
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			String upgraded = DeckCardCode.upgradeLevel(cardCode) > 0 ? "강화된 " : "";
			return "비용이 0인 무작위 " + upgraded + "카드를 3장 손으로 가져옵니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "무작위 카드 > 무작위 강화된 카드";
		}
	}

	public static class DrawPileTypeSelectEffect implements DeckCardEffect {
		private final DeckCardType type;

		public DrawPileTypeSelectEffect(DeckCardType type) {
			this.type = type;
		}

		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			for (int code : combat.drawPile) {
				if (DeckCard.byCode(code).type == type) {
					combat.pendingDrawPileTypeSelect = type;
					return;
				}
			}
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return "뽑을 카드 더미에서 " + typeName(type) + " 카드 1장을 손으로 가져옵니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "소멸 > 소멸 제거";
		}

		private String typeName(DeckCardType type) {
			if (type == DeckCardType.ATTACK) return "공격";
			if (type == DeckCardType.SKILL) return "보조";
			if (type == DeckCardType.POWER) return "지속";
			return type.label;
		}
	}

	public static class GrandStrategyEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "드로우 3장 > 4장";
		}
	}

	public static class EntrenchedPlanEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			combat.dieOnUnblockedAttack = true;
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return "이번 전투 동안 막히지 않은 공격 피해를 받는다면, 죽습니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "보호막 50 > 75";
		}
	}

	public static class AlchemyEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			DeckBuilderRun.addPotion(DeckPotionPolicy.randomPotion(DeckPotionPolicy.rollRarity()));
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return "무작위 포션을 1개 얻습니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "비용 1 > 0";
		}
	}

	public static class AppointmentEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			for (int i = combat.drawPile.size() - 1; i >= 0; i--) {
				int code = combat.drawPile.get(i);
				if (DeckCard.byCode(code).rarity == DeckCardRarity.RARE) {
					combat.drawPile.remove(i);
					combat.addToHand(code);
				}
			}
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return "뽑을 카드 더미에 있는 모든 희귀 카드를 손으로 가져옵니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "보존 추가";
		}
	}

	public static class ScribbleEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			int before = combat.hand.size();
			int count = Math.max(0, combat.maxHandSize - combat.hand.size() + 1);
			combat.draw(count);
			result.draw += Math.max(0, combat.hand.size() - before);
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return "손이 가득 찰 때까지 카드를 뽑습니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "보존 추가";
		}
	}

	public static class PummelDiscardEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			combat.playRandomAttacksFromDiscard(DeckCardCode.upgradeLevel(cardCode) > 0 ? 4 : 3, result);
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			int count = DeckCardCode.upgradeLevel(cardCode) > 0 ? 4 : 3;
			return "버린 카드 더미에서 무작위 공격 카드 " + count + "장을 사용합니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "공격 카드 3장 > 4장";
		}
	}

	public static class EntropyEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			combat.entropyCount++;
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return "내 턴 시작 시, 손에 있는 카드를 1장 변환시킵니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return DeckCardCode.upgradeLevel(cardCode) > 0 ? "" : "_선봉_ 추가";
		}
	}

	public static class NostalgiaEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			combat.nostalgiaActive = true;
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return "매 턴마다 처음으로 사용하는 공격이나 보조 카드를 뽑을 카드 더미 맨 위에 놓습니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "비용 1 > 0";
		}
	}

	public static class ChaosEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			combat.chaosCount++;
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return "내 턴 시작 시, 뽑을 카드 더미 맨 위의 카드를 사용합니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "비용 2 > 1";
		}
	}

	public static class RollingBoulderEffect implements DeckCardEffect {
		private final int base;
		private final int upgraded;

		public RollingBoulderEffect(int base, int upgraded) {
			this.base = base;
			this.upgraded = upgraded;
		}

		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			combat.rollingBoulderCurrentDamage += DeckCardCode.upgradeLevel(cardCode) > 0 ? upgraded : base;
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			int next = DeckCardCode.upgradeLevel(cardCode) > 0 ? upgraded : base;
			if (combat != null && combat.rollingBoulderCurrentDamage > 0) {
				return "내 턴 시작 시, 모든 적에게 피해를 " + combat.rollingBoulderCurrentDamage + " 줍니다. 매 발동마다 8 증가합니다.";
			}
			return "내 턴 시작 시, 모든 적에게 피해를 " + next + " 줍니다. 매 발동마다 피해가 8 증가합니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return base == upgraded ? "" : "기본 피해 " + base + " > " + upgraded;
		}
	}

	public static class RegenEffect implements DeckCardEffect {
		private final int base;
		private final int upgraded;

		public RegenEffect(int base, int upgraded) {
			this.base = base;
			this.upgraded = upgraded;
		}

		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			combat.playerRegen += DeckCardCode.upgradeLevel(cardCode) > 0 ? upgraded : base;
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			int amount = DeckCardCode.upgradeLevel(cardCode) > 0 ? upgraded : base;
			return "재생을 " + amount + " 얻습니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return base == upgraded ? "" : "재생 " + base + " > " + upgraded;
		}
	}

	public static class OsirisEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			if (!combat.discardPile.isEmpty()) {
				combat.pendingDiscardHandSelectCount = Math.min(2, combat.discardPile.size());
			}
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return "버린 카드 더미에서 카드를 2장 선택하고 손으로 가져옵니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "피해 10 > 14";
		}
	}

	public static class GlideEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			combat.addCreamDarkSpace(2);
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return "크림에게서 멀어집니다. 암흑공간이 2 증가합니다. 해당 카드의 비용이 1 증가합니다.";
		}
	}

	public static class GougeEffect extends Damage {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			super.apply(combat, card, cardCode, result);
			int increase = DeckCardCode.upgradeLevel(cardCode) > 0 ? 3 : 2;
			combat.gougeDamageBonus += increase;
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			int increase = DeckCardCode.upgradeLevel(cardCode) > 0 ? 3 : 2;
			return DeckCardText.damageRulesText(card, cardCode, combat) + " 이번 전투 동안 모든 해당 카드의 피해량이 " + increase + " 증가합니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "피해 3 > 4, 피해량 증가 2 > 3";
		}
	}

	public static class SynthesisEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			combat.nextPowerZeroCost = true;
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return "다음에 사용하는 지속 카드의 비용이 0이 됩니다.";
		}
	}

	public static class HelixPierceEffect extends Damage {
		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			int multiplier = DeckCardCode.upgradeLevel(cardCode) > 0 ? 5 : 3;
			return "이번 턴 동안 소모한 에너지당 피해를 " + multiplier + " 줍니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "피해 3 > 5 (소모 에너지당)";
		}
	}

	public static class PounceEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			combat.nextSkillZeroCost = true;
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return "다음에 사용하는 보조 카드의 비용이 0이 됩니다.";
		}
	}

	public static class PreciseShotEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			return "이번 턴에 보조 카드를 사용할 때마다 비용이 1 감소합니다.";
		}
	}

	public static class KillEffect extends Damage {
		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			int drawn = combat != null ? combat.cardsDrawnThisCombat : 0;
			return "피해를 " + (1 + drawn) + " 줍니다. 이번 전투 동안 뽑은 카드 1장당 피해량이 1 증가합니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "비용 3 > 2";
		}
	}

	public static class RushedExitEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			int block = DeckCardCode.upgradeLevel(cardCode) > 0 ? 9 : 6;
			result.block += combat.gainBlockFromCard(block);
			combat.discardPile.add(DeckCard.DIZZINESS.code());
		}
		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			int block = DeckCardCode.upgradeLevel(cardCode) > 0 ? 9 : 6;
			return "보호막을 " + block + " 얻습니다. 버린 카드 더미에 어지러움을 1장 추가합니다.";
		}
		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "보호막 6 > 9";
		}
	}

	public static class TurboEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			int energy = DeckCardCode.upgradeLevel(cardCode) > 0 ? 2 : 1;
			combat.gainEnergy(energy);
			combat.discardPile.add(DeckCard.HOLLOW.code());
		}
		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			int energy = DeckCardCode.upgradeLevel(cardCode) > 0 ? 2 : 1;
			return energy + " 에너지를 얻습니다. 버린 카드 더미에 공허를 1장 추가합니다.";
		}
		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "에너지 1 > 2";
		}
	}

	public static class OverclockBoostEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			int draw = DeckCardCode.upgradeLevel(cardCode) > 0 ? 3 : 2;
			result.draw += drawCards(combat, draw);
			combat.discardPile.add(DeckCard.BURN.code());
		}
		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			int draw = DeckCardCode.upgradeLevel(cardCode) > 0 ? 3 : 2;
			return "카드를 " + draw + "장 뽑습니다. 버린 카드 더미에 화상을 1장 추가합니다.";
		}
		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "드로우 2 > 3";
		}
	}

	public static class ForcedPushEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			int block = DeckCardCode.upgradeLevel(cardCode) > 0 ? 17 : 13;
			result.block += combat.gainBlockFromCard(block);
			combat.discardPile.add(DeckCard.INJURY.code());
			combat.discardPile.add(DeckCard.INJURY.code());
		}
		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			int block = DeckCardCode.upgradeLevel(cardCode) > 0 ? 17 : 13;
			return "보호막을 " + block + " 얻습니다. 버린 카드 더미에 부상을 2장 추가합니다.";
		}
		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "보호막 13 > 17";
		}
	}

	private static int drawCards(DeckBuilderCombat combat, int count) {
		int drawn = 0;
		for (int i = 0; i < count; i++) {
			int before = combat.hand.size();
			if (!combat.draw(1)) break;
			if (combat.hand.size() > before) drawn++;
		}
		return drawn;
	}

	public static class FleshTrickEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			combat.debuffTriggerDamage += DeckCardCode.upgradeLevel(cardCode) > 0 ? 13 : 9;
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			int dmg = DeckCardCode.upgradeLevel(cardCode) > 0 ? 13 : 9;
			return "적에게 해로운 효과(디버프)를 부여할 때마다, 대상 적이 피해를 " + dmg + " 받습니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "피해 9 > 13";
		}
	}

	public static class DualWieldEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			int copies = DeckCardCode.upgradeLevel(cardCode) > 0 ? 2 : 1;
			boolean hasTarget = false;
			for (int code : combat.hand) {
				DeckCardType t = DeckCard.byCode(code).type;
				if (t == DeckCardType.ATTACK || t == DeckCardType.POWER) {
					hasTarget = true;
					break;
				}
			}
			if (hasTarget) combat.pendingHandCopySelectCopies = copies;
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			int copies = DeckCardCode.upgradeLevel(cardCode) > 0 ? 2 : 1;
			return "내 손의 공격 또는 지속 카드를 선택해서 " + copies + "장 복사합니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "복사량 1장 > 2장";
		}
	}

	public static class AntiaircraftCannonEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			int dmg = DeckCardCode.upgradeLevel(cardCode) > 0 ? 11 : 8;
			int count = 0;
			for (int i = combat.hand.size() - 1; i >= 0; i--) {
				if (DeckCard.byCode(combat.hand.get(i)).type == DeckCardType.STATUS) {
					result.draw += combat.exhaustCard(combat.hand.remove(i));
					count++;
				}
			}
			for (int i = combat.drawPile.size() - 1; i >= 0; i--) {
				if (DeckCard.byCode(combat.drawPile.get(i)).type == DeckCardType.STATUS) {
					result.draw += combat.exhaustCard(combat.drawPile.remove(i));
					count++;
				}
			}
			for (int i = combat.discardPile.size() - 1; i >= 0; i--) {
				if (DeckCard.byCode(combat.discardPile.get(i)).type == DeckCardType.STATUS) {
					result.draw += combat.exhaustCard(combat.discardPile.remove(i));
					count++;
				}
			}
			ArrayList<DeckCombatEnemy> alive = combat.aliveEnemies();
			if (!alive.isEmpty()) {
				for (int i = 0; i < count; i++) {
					DeckCombatEnemy target = alive.get(Random.Int(alive.size()));
					int dealt = combat.damageEnemy(target, dmg, true);
					if (dealt > 0) result.addAttackHit(combat.enemyIndex(target), dealt);
				}
			}
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			int dmg = DeckCardCode.upgradeLevel(cardCode) > 0 ? 11 : 8;
			return "패, 뽑을 카드 더미, 버린 카드 더미의 모든 상태이상 카드를 소멸시킵니다. 소멸시킨 카드 1장당 무작위 적에게 피해를 " + dmg + " 줍니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "피해 8 > 11";
		}
	}

	public static class IronBowgunEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			boolean upgraded = DeckCardCode.upgradeLevel(cardCode) > 0;
			int ammoCode = upgraded ? DeckCardCode.upgrade(DeckCard.BOWGUN_AMMO.code()) : DeckCard.BOWGUN_AMMO.code();
			for (int i = 0; i < combat.hand.size(); i++) {
				int code = combat.hand.get(i);
				if (DeckCard.byCode(code).type == DeckCardType.ATTACK) {
					combat.hand.set(i, ammoCode);
				}
			}
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			boolean upgraded = DeckCardCode.upgradeLevel(cardCode) > 0;
			String target = upgraded ? "강화된 보우건 전용 탄환" : "보우건 전용 탄환";
			return "손에 있는 모든 공격 카드를 " + target + "으로 변환시킵니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "보우건 전용 탄환 > 강화된 보우건 전용 탄환";
		}
	}

	public static class ComposureEffect implements DeckCardEffect {
		@Override
		public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {
			ArrayList<Integer> wands = new ArrayList<>();
			for (int code : combat.drawPile) {
				if (DeckWandCards.isWand(code)) wands.add(code);
			}
			if (!wands.isEmpty()) {
				int picked = wands.get(Random.Int(wands.size()));
				combat.drawPile.remove(Integer.valueOf(picked));
				combat.addToHand(picked);
			}
			combat.draw(DeckCard.COMPOSURE.draw(cardCode));
		}

		@Override
		public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
			int draws = card.draw(cardCode);
			return "뽑을 카드 더미에서 무작위 완드 카드 1장을 손으로 가져옵니다. 카드를 " + draws + "장 뽑습니다.";
		}

		@Override
		public String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
			return "드로우 1 > 2";
		}
	}

	private static ArrayList<DeckCombatEnemy> targets(DeckBuilderCombat combat, DeckCard card) {
		ArrayList<DeckCombatEnemy> targets = new ArrayList<>();
		if ((combat.shivAllEnemies && combat.isShivCard(card)) || card.target == DeckCardTarget.ALL_ENEMIES) {
			targets.addAll(combat.aliveEnemies());
		} else if (card.target == DeckCardTarget.RANDOM_ENEMY) {
			ArrayList<DeckCombatEnemy> alive = combat.aliveEnemies();
			if (!alive.isEmpty()) targets.add(alive.get(Random.Int(alive.size())));
		} else {
			DeckCombatEnemy target = combat.target();
			if (target != null) targets.add(target);
		}
		return targets;
	}
}
