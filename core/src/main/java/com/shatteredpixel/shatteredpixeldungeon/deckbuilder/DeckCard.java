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

import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

import java.util.ArrayList;

public enum DeckCard {

	STRIKE(0, "행운의 검", DeckCardType.ATTACK, DeckCardRarity.COMMON, DeckCardTarget.SINGLE, 1, 6, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.WORN_SHORTSWORD),
	GUARD(1, "무당벌레 브로치", DeckCardType.SKILL, DeckCardRarity.COMMON, DeckCardTarget.NONE, 1, 0, 5, 0, 0, 0, 0, 0, true, ItemSpriteSheet.ARMOR_CLOTH),
	BASH(2, "파문 커터", DeckCardType.ATTACK, DeckCardRarity.UNCOMMON, DeckCardTarget.SINGLE, 2, 8, 0, 0, 2, 0, 0, 0, true, ItemSpriteSheet.THROWING_STONE),
	BREATH(3, "파문의 보호막", DeckCardType.SKILL, DeckCardRarity.COMMON, DeckCardTarget.NONE, 1, 0, 3, 1, 0, 0, 0, 0, true, ItemSpriteSheet.SEAL),
	STAR_FINGER(4, "매지션즈 레드의 사격 DISC", DeckCardType.ATTACK, DeckCardRarity.RARE, DeckCardTarget.RANDOM_ENEMY, 2, 13, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.WAND_FIREBOLT),
	RIPPLE_WALL(5, "개구리", DeckCardType.SKILL, DeckCardRarity.UNCOMMON, DeckCardTarget.ALL_ENEMIES, 2, 5, 12, 0, 0, 0, 0, 0, true, ItemSpriteSheet.DEWDROP),
	SLIMY(6, "익사", DeckCardType.STATUS, DeckCardRarity.COMMON, DeckCardTarget.NONE, 1, 0, 0, 1, 0, 0, 1, keywords(DeckCardKeyword.EXHAUST), false, ItemSpriteSheet.MOB_HOLDER),
	IGNITE(7, "강화의 DISC", DeckCardType.POWER, DeckCardRarity.UNCOMMON, DeckCardTarget.NONE, 1, 0, 0, 0, 0, 2, 0, 0, true, ItemSpriteSheet.SCROLL_NAUDIZ);

	private static final int ID_MASK = 0xFF;
	private static final int UPGRADE_SHIFT = 8;
	private static final int UPGRADE_MASK = 0xF;
	private static final int KEYWORD_SHIFT = 12;

	public final int id;
	public final String title;
	public final DeckCardType type;
	public final DeckCardRarity rarity;
	public final DeckCardTarget target;
	public final int cost;
	public final int damage;
	public final int block;
	public final int draw;
	public final int vulnerable;
	public final int strength;
	public final int handPenalty;
	public final int baseKeywords;
	public final boolean reward;
	public final int icon;

	DeckCard(int id, String title, DeckCardType type, DeckCardRarity rarity, DeckCardTarget target, int cost, int damage, int block, int draw, int vulnerable,
			 int strength, int handPenalty, int baseKeywords, boolean reward, int icon) {
		this.id = id;
		this.title = title;
		this.type = type;
		this.rarity = rarity;
		this.target = target;
		this.cost = cost;
		this.damage = damage;
		this.block = block;
		this.draw = draw;
		this.vulnerable = vulnerable;
		this.strength = strength;
		this.handPenalty = handPenalty;
		this.baseKeywords = baseKeywords;
		this.reward = reward;
		this.icon = icon;
	}

	public int code() {
		return id;
	}

	public String title(int code) {
		return upgradeLevel(code) > 0 ? "+" + title : title;
	}

	public int cost(int code) {
		return cost;
	}

	public int damage(int code) {
		return damage > 0 ? damage + upgradeLevel(code) * 3 : 0;
	}

	public int block(int code) {
		return block > 0 ? block + upgradeLevel(code) * 3 : 0;
	}

	public int draw(int code) {
		return draw;
	}

	public int vulnerable(int code) {
		return vulnerable;
	}

	public int strength(int code) {
		return strength > 0 ? strength + upgradeLevel(code) : 0;
	}

	public boolean hasKeyword(int code, DeckCardKeyword keyword) {
		return (keywords(code) & keyword.bit) != 0;
	}

	public int keywords(int code) {
		return baseKeywords | keywordBits(code);
	}

	public String rulesText(int code) {
		String text = "";
		if (damage(code) > 0) text += target == DeckCardTarget.ALL_ENEMIES ? "모든 적에게 피해 " + damage(code) : "피해 " + damage(code);
		if (block(code) > 0) text += (text.length() > 0 ? " / " : "") + "방어 " + block(code);
		if (draw(code) > 0) text += (text.length() > 0 ? " / " : "") + draw(code) + "장 뽑기";
		if (vulnerable(code) > 0) text += (text.length() > 0 ? " / " : "") + "취약 " + vulnerable(code);
		if (strength(code) > 0) text += (text.length() > 0 ? " / " : "") + "공격력을 " + strength(code) + " 얻습니다.";
		if (target == DeckCardTarget.RANDOM_ENEMY) text += (text.length() > 0 ? " / " : "") + "무작위 대상";
		if (handPenalty > 0) text += (text.length() > 0 ? " / " : "") + "손패: 공격 -" + handPenalty;
		if (hasKeyword(code, DeckCardKeyword.EXHAUST)) text += (text.length() > 0 ? " / " : "") + DeckCardKeyword.EXHAUST.label;
		if (hasKeyword(code, DeckCardKeyword.RETAIN)) text += (text.length() > 0 ? " / " : "") + DeckCardKeyword.RETAIN.label;
		return text;
	}

	public DeckCardEffect[] effects(int code) {
		ArrayList<DeckCardEffect> effects = new ArrayList<>();
		if (damage(code) > 0) effects.add(new DeckCardEffects.Damage());
		if (block(code) > 0) effects.add(new DeckCardEffects.Block());
		if (vulnerable(code) > 0) effects.add(new DeckCardEffects.Vulnerable());
		if (draw(code) > 0) effects.add(new DeckCardEffects.Draw());
		if (strength(code) > 0) effects.add(new DeckCardEffects.Strength());
		return effects.toArray(new DeckCardEffect[0]);
	}

	public static int id(int code) {
		return code & ID_MASK;
	}

	public static int upgradeLevel(int code) {
		return (code >>> UPGRADE_SHIFT) & UPGRADE_MASK;
	}

	public static int keywordBits(int code) {
		return code >>> KEYWORD_SHIFT;
	}

	public static int withKeyword(int code, DeckCardKeyword keyword) {
		return code | (keyword.bit << KEYWORD_SHIFT);
	}

	public static int withoutKeyword(int code, DeckCardKeyword keyword) {
		return code & ~(keyword.bit << KEYWORD_SHIFT);
	}

	public static int upgrade(int code) {
		int upgrade = Math.min(UPGRADE_MASK, upgradeLevel(code) + 1);
		return (code & ~(UPGRADE_MASK << UPGRADE_SHIFT)) | (upgrade << UPGRADE_SHIFT);
	}

	public static DeckCard byCode(int code) {
		return byId(id(code));
	}

	public static DeckCard byId(int id) {
		for (DeckCard card : values()) {
			if (card.id == id) return card;
		}
		return STRIKE;
	}

	public static DeckCard[] rewardPool() {
		ArrayList<DeckCard> pool = new ArrayList<>();
		for (DeckCard card : values()) {
			if (card.reward) pool.add(card);
		}
		return pool.toArray(new DeckCard[0]);
	}

	private static int keywords(DeckCardKeyword... keywords) {
		int bits = 0;
		for (DeckCardKeyword keyword : keywords) {
			bits |= keyword.bit;
		}
		return bits;
	}
}
