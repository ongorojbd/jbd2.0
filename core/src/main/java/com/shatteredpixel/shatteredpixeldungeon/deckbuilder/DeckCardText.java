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

public class DeckCardText {

	public static String detailTitle(DeckCard card, int cardCode) {
		return card.title(cardCode) + "(" + typeLabel(card.type) + ", " + rarityLabel(card.rarity) + "): 비용 " + card.cost(cardCode);
	}

	public static String rulesText(DeckCard card, int cardCode) {
		return rulesText(card, cardCode, null);
	}

	public static String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
		if (card == DeckCard.STAFF) {
			return "피해를 " + damageValue(card, cardCode, combat) + " 줍니다. 막히지 않은 피해만큼 방어도를 얻습니다.";
		}
		if (card == DeckCard.ROTATING_NAIL) {
			return "무작위 적에게 피해를 " + damageValue(card, cardCode, combat) + " 주고 카드를 1장 뽑습니다.";
		}
		if (card == DeckCard.TUSK_EQUIPMENT_DISC) {
			return "회전하는 손톱 2장을 뽑을 카드 더미에 섞어 넣고 피해를 " + damageValue(card, cardCode, combat) + " 줍니다. [조준] 강화됩니다.";
		}
		if (card == DeckCard.TRACKING_BULLET_HOLE) {
			return "모든 적에게 피해를 " + damageValue(card, cardCode, combat) + " 줍니다. 회전하는 손톱 3장을 뽑을 카드 더미에 섞어 넣습니다.";
		}
		if (card == DeckCard.SPIN_TRAINING) {
			int bonus = DeckCard.upgradeLevel(cardCode) > 0 ? 2 : 1;
			return "회전하는 손톱의 피해량이 +" + bonus + " 증가합니다. 카드를 1장 뽑습니다. [조준] 회전하는 손톱 3장을 뽑을 카드 더미에 섞어 넣습니다.";
		}
		if (card == DeckCard.PROUD_STARVER) {
			return "카드를 " + card.draw(cardCode) + "장 뽑습니다. 뽑을 카드 더미에 있는 회전하는 손톱의 수만큼 비용이 감소합니다.";
		}
		if (card == DeckCard.LESSON_FIVE) {
			return "뽑을 카드 더미에 있는 모든 회전하는 손톱을 복사해서 시전합니다.";
		}
		if (card == DeckCard.SCORPION_THROW) {
			return "전갈탄을 1장 손에 가져옵니다. 카드를 " + card.draw(cardCode) + "장 뽑습니다.";
		}
		if (card == DeckCard.PHANTOM_BLADES) {
			int bonus = DeckCard.upgradeLevel(cardCode) > 0 ? 12 : 9;
			return "모든 전갈탄에 " + DeckCardKeyword.RETAIN.label + "을 부여합니다. 매 턴 처음으로 사용하는 전갈탄의 피해량이 +" + bonus + " 증가합니다.";
		}
		if (card == DeckCard.ACCURACY) {
			int bonus = DeckCard.upgradeLevel(cardCode) > 0 ? 6 : 4;
			return "전갈탄의 피해량이 +" + bonus + " 증가합니다.";
		}
		if (card == DeckCard.KNIFE_TRAP) {
			String base = "버린 카드 더미에 있는 모든 전갈탄을 선택한 적에게 사용합니다.";
			return DeckCard.upgradeLevel(cardCode) > 0 ? base + " 강화된 전갈탄으로 사용합니다." : base;
		}
		if (card == DeckCard.LEADING_STRIKE) {
			return "피해를 " + damageValue(card, cardCode, combat) + " 줍니다. 전갈탄을 " + card.shivs(cardCode) + "장 손에 가져옵니다.";
		}
		if (card == DeckCard.CLOAK_AND_DAGGER) {
			return "방어도를 " + card.block(cardCode) + " 얻습니다. 전갈탄을 " + card.shivs(cardCode) + "장 손에 가져옵니다.";
		}

		String text = "";
		if (card.damage(cardCode) > 0) text += damageRulesText(card, cardCode, combat);
		if (card.block(cardCode) > 0) text += appendSentence(text, "방어도를 " + card.block(cardCode) + " 얻습니다.");
		if (card.draw(cardCode) > 0) text += appendSentence(text, "카드를 " + card.draw(cardCode) + "장 뽑습니다.");
		if (card.vulnerable(cardCode) > 0) text += appendSentence(text, "취약을 " + card.vulnerable(cardCode) + " 부여합니다.");
		if (card.strength(cardCode) > 0) text += appendSentence(text, "힘을 " + card.strength(cardCode) + " 얻습니다.");
		if (card.handPenalty > 0) text += appendSentence(text, "손패에 있으면 공격 카드 피해가 " + card.handPenalty + " 감소합니다.");
		if (card.hasKeyword(cardCode, DeckCardKeyword.EXHAUST)) text += appendSentence(text, DeckCardKeyword.EXHAUST.label);
		if (card.hasKeyword(cardCode, DeckCardKeyword.RETAIN)) text += appendSentence(text, DeckCardKeyword.RETAIN.label);
		return text.length() > 0 ? text : "별도의 즉시 효과가 없습니다.";
	}

	public static String keywordText(DeckCard card, int cardCode) {
		String text = "";
		if (card.vulnerable(cardCode) > 0) text += "취약: 받는 공격 피해가 50% 증가합니다.";
		if (card.strength(cardCode) > 0) text += appendLine(text, "힘: 공격 카드의 피해가 증가합니다.");
		for (DeckCardKeyword keyword : DeckCardKeyword.values()) {
			if (card.hasKeyword(cardCode, keyword)) {
				text += appendLine(text, keyword.label + ": " + keyword.description);
			}
		}
		if (card.handPenalty > 0) text += appendLine(text, "방해: 손패에 있으면 공격 카드 피해가 감소합니다.");
		return text;
	}

	public static String upgradePreviewText(int cardCode) {
		int upgraded = DeckCard.upgrade(cardCode);
		DeckCard card = DeckCard.byCode(cardCode);
		if (upgraded == cardCode) return "더 이상 강화할 수 없습니다.";
		if (card == DeckCard.SPIN_TRAINING) return "회전하는 손톱 피해 증가 +1 > +2";
		if (card == DeckCard.LESSON_FIVE) return "비용 2 > 1";
		if (card == DeckCard.PHANTOM_BLADES) return "첫 전갈탄 피해 증가 +9 > +12";
		if (card == DeckCard.ACCURACY) return "전갈탄 피해 증가 +4 > +6";
		if (card == DeckCard.KNIFE_TRAP) return "전갈탄 시전 > 강화된 전갈탄 시전";

		String text = "";
		if (card.cost(cardCode) != card.cost(upgraded)) text += appendLine(text, "비용 " + card.cost(cardCode) + " > " + card.cost(upgraded));
		if (card.damage(cardCode) != card.damage(upgraded)) text += appendLine(text, "피해 " + card.damage(cardCode) + " > " + card.damage(upgraded));
		if (card.block(cardCode) != card.block(upgraded)) text += appendLine(text, "방어 " + card.block(cardCode) + " > " + card.block(upgraded));
		if (card.draw(cardCode) != card.draw(upgraded)) text += appendLine(text, "드로우 " + card.draw(cardCode) + " > " + card.draw(upgraded));
		if (card.vulnerable(cardCode) != card.vulnerable(upgraded)) text += appendLine(text, "취약 " + card.vulnerable(cardCode) + " > " + card.vulnerable(upgraded));
		if (card.strength(cardCode) != card.strength(upgraded)) text += appendLine(text, "힘 " + card.strength(cardCode) + " > " + card.strength(upgraded));
		if (card.shivs(cardCode) != card.shivs(upgraded)) text += appendLine(text, "전갈탄 " + card.shivs(cardCode) + " > " + card.shivs(upgraded));
		return text.length() > 0 ? text : "강화 효과가 아직 정의되지 않았습니다.";
	}

	private static String damageRulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
		if (card.target == DeckCardTarget.ALL_ENEMIES) return "모든 적에게 피해를 " + damageValue(card, cardCode, combat) + " 줍니다.";
		if (card.target == DeckCardTarget.RANDOM_ENEMY) return "무작위 적에게 피해를 " + damageValue(card, cardCode, combat) + " 줍니다.";
		return "피해를 " + damageValue(card, cardCode, combat) + " 줍니다.";
	}

	private static String damageValue(DeckCard card, int cardCode, DeckBuilderCombat combat) {
		if (combat == null) return String.valueOf(card.damage(cardCode));
		if (card.target == DeckCardTarget.ALL_ENEMIES || card.target == DeckCardTarget.RANDOM_ENEMY) {
			int min = Integer.MAX_VALUE;
			int max = Integer.MIN_VALUE;
			for (DeckCombatEnemy enemy : combat.aliveEnemies()) {
				int damage = combat.cardDamage(card, cardCode, enemy);
				min = Math.min(min, damage);
				max = Math.max(max, damage);
			}
			if (min == Integer.MAX_VALUE) min = max = combat.cardDamage(card, cardCode);
			return min == max ? String.valueOf(min) : min + "-" + max;
		}
		return String.valueOf(combat.cardDamage(card, cardCode));
	}

	private static String typeLabel(DeckCardType type) {
		switch (type) {
			case ATTACK: return "공격";
			case SKILL: return "보조";
			case POWER: return "지속";
			case STATUS: return "상태이상";
			case CURSE: return "저주";
			default: return type.label;
		}
	}

	private static String rarityLabel(DeckCardRarity rarity) {
		switch (rarity) {
			case COMMON: return "일반";
			case UNCOMMON: return "특별";
			case RARE: return "희귀";
			default: return rarity.label;
		}
	}

	private static String appendSentence(String text, String value) {
		return (text.length() > 0 ? " " : "") + value;
	}

	private static String appendLine(String text, String value) {
		return (text.length() > 0 ? "\n" : "") + value;
	}
}
