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
		return card.title(cardCode) + "(" + typeLabel(card.type) + ", " + rarityLabel(card.rarity) + "): 비용 " + costText(card, cardCode);
	}

	public static String rulesText(DeckCard card, int cardCode) {
		return rulesText(card, cardCode, null);
	}

	public static String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
		if (card == DeckCard.POISON_DART) {
			return "사용불가. 내 턴 종료 시 이 카드가 손에 있다면, 피해를 3 받습니다.";
		}
		if (card == DeckCard.BARNACLE) {
			return "사용불가. " + DeckCardKeyword.TRANSIENT.label + ".";
		}
		if (card == DeckCard.DIZZINESS) {
			return "사용불가. " + DeckCardKeyword.TRANSIENT.label + ".";
		}
		if (card == DeckCard.HOLLOW) {
			return "사용불가. " + DeckCardKeyword.TRANSIENT.label + ". 이 카드를 뽑을 때마다, 에너지를 1 잃습니다.";
		}
		if (card == DeckCard.BURN) {
			return "사용불가. 내 턴 종료 시 이 카드가 손에 있다면, 피해를 2 받습니다.";
		}
		if (card == DeckCard.INJURY) {
			return "사용불가.";
		}
		String text = "";
		for (DeckCardEffect effect : card.effects(cardCode)) {
			text += appendSentence(text, effect.rulesText(card, cardCode, combat));
		}
		if (card.handPenalty > 0) text += appendSentence(text, "손패에 있으면 공격 카드 피해가 " + card.handPenalty + " 감소합니다.");
		for (DeckCardKeyword keyword : DeckCardKeyword.values()) {
			if (keyword.label.isEmpty()) continue;
			if (card.hasKeyword(cardCode, keyword)) text += appendSentence(text, keyword.label + ".");
		}
		if (DeckCardCode.maxCharge(cardCode) > 0) {
			text += appendSentence(text, "_충전: " + DeckCardCode.currentCharge(cardCode) + "/" + DeckCardCode.maxCharge(cardCode) + "_.");
		}
		String result = text.length() > 0 ? text : "별도의 즉시 효과가 없습니다.";
		if (!result.endsWith(".")) result += ".";
		return result;
	}

	public static String keywordText(DeckCard card, int cardCode) {
		String text = "";
		if (card.vulnerable(cardCode) > 0) text += "피해 증폭: 받는 공격 피해가 50% 증가합니다.";
		if (card.strength(cardCode) > 0) text += appendLine(text, "공격력: 공격 카드의 피해가 증가합니다.");
		for (DeckCardKeyword keyword : DeckCardKeyword.values()) {
			if (keyword.label.isEmpty()) continue;
			if (card.hasKeyword(cardCode, keyword)) {
				text += appendLine(text, keyword.label.replace("_", "") + ": " + keyword.description);
			}
		}
		if (DeckCardCode.maxCharge(cardCode) > 0) {
			text += appendLine(text, "충전: 직접 사용할 수 없으며, 정해진 시점마다 충전을 1 잃습니다. 충전이 0이 되면 소멸합니다.");
		}
		for (DeckCardEffect effect : card.effects(cardCode)) {
			text += appendLine(text, effect.keywordText(card, cardCode));
		}
		if (card.handPenalty > 0) text += appendLine(text, "방해: 손패에 있으면 공격 카드 피해가 감소합니다.");
		return text;
	}

	public static String rulesAndKeywordText(DeckCard card, int cardCode) {
		return rulesAndKeywordText(card, cardCode, null);
	}

	public static String rulesAndKeywordText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
		String keywords = keywordText(card, cardCode);
		return rulesText(card, cardCode, combat) + (keywords.length() > 0 ? "\n\n" + keywords : "");
	}

	public static String upgradePreviewText(int cardCode) {
		int upgraded = DeckCardCode.upgrade(cardCode);
		DeckCard card = DeckCard.byCode(cardCode);
		if (upgraded == cardCode) return "더 이상 강화할 수 없습니다.";

		// Collect upgrade text only from custom effects (skip auto-added base stat classes)
		String effectText = "";
		for (DeckCardEffect effect : card.effects(cardCode)) {
			if (isBaseStatEffect(effect)) continue;
			effectText += appendLine(effectText, effect.upgradePreviewText(card, cardCode, upgraded));
		}

		String text = "";
		if (card.cost(cardCode) != card.cost(upgraded) && !effectText.contains("비용"))
			text += appendLine(text, "비용 " + costText(card, cardCode) + " > " + costText(card, upgraded));

		if (effectText.isEmpty()) {
			// No custom effect text — fall back to auto stat comparison
			if (card.damage(cardCode) != card.damage(upgraded)) text += appendLine(text, "피해 " + card.damage(cardCode) + " > " + card.damage(upgraded));
			if (card.block(cardCode) != card.block(upgraded)) text += appendLine(text, "보호막 " + card.block(cardCode) + " > " + card.block(upgraded));
			if (card.draw(cardCode) != card.draw(upgraded)) text += appendLine(text, "드로우 " + card.draw(cardCode) + " > " + card.draw(upgraded));
			if (card.vulnerable(cardCode) != card.vulnerable(upgraded)) text += appendLine(text, "피해 증폭 " + card.vulnerable(cardCode) + " > " + card.vulnerable(upgraded));
		}

		if (card.strength(cardCode) != card.strength(upgraded) && !effectText.contains("공격력")) text += appendLine(text, "공격력 " + card.strength(cardCode) + " > " + card.strength(upgraded));
		if (card.shivs(cardCode) != card.shivs(upgraded) && !effectText.contains("전갈탄")) text += appendLine(text, "전갈탄 " + card.shivs(cardCode) + " > " + card.shivs(upgraded));
		if (DeckCardCode.maxCharge(cardCode) != DeckCardCode.maxCharge(upgraded)) text += appendLine(text, "충전 " + DeckCardCode.maxCharge(cardCode) + "/" + DeckCardCode.maxCharge(cardCode) + " > " + DeckCardCode.maxCharge(upgraded) + "/" + DeckCardCode.maxCharge(upgraded));
		text += appendLine(text, effectText);

		return text.length() > 0 ? text : "강화 효과가 아직 정의되지 않았습니다.";
	}

	private static boolean isBaseStatEffect(DeckCardEffect effect) {
		Class<?> c = effect.getClass();
		return c == DeckCardEffects.Damage.class
			|| c == DeckCardEffects.Block.class
			|| c == DeckCardEffects.Draw.class
			|| c == DeckCardEffects.Vulnerable.class
			|| c == DeckCardEffects.Strength.class
			|| c == DeckCardEffects.AddShivs.class;
	}

	static int blockValue(int baseBlock, DeckBuilderCombat combat) {
		if (combat == null) return baseBlock;
		return Math.max(0, baseBlock + combat.playerDexterity);
	}

	static String damageRulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
		if (card.target == DeckCardTarget.ALL_ENEMIES) return "모든 적에게 피해를 " + damageValue(card, cardCode, combat) + " 줍니다.";
		if (card.target == DeckCardTarget.RANDOM_ENEMY) return "무작위 적에게 피해를 " + damageValue(card, cardCode, combat) + " 줍니다.";
		return "피해를 " + damageValue(card, cardCode, combat) + " 줍니다.";
	}

	static String damageValue(DeckCard card, int cardCode, DeckBuilderCombat combat) {
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
			case QUEST: return "퀘스트";
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

	static String appendSentence(String text, String value) {
		if (value == null || value.length() == 0) return "";
		return (text.length() > 0 ? " " : "") + value;
	}

	static String appendLine(String text, String value) {
		if (value == null || value.length() == 0) return "";
		return (text.length() > 0 ? "\n" : "") + value;
	}

	static String costText(DeckCard card, int cardCode) {
		return card.cost(cardCode) < 0 ? "X" : String.valueOf(card.cost(cardCode));
	}
}
