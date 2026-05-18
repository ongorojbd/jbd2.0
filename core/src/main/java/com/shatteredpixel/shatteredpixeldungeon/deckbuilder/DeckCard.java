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

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

import java.util.ArrayList;

public enum DeckCard {

	STRIKE(0, "행운의 검", DeckCardType.ATTACK, DeckCardRarity.COMMON, DeckCardTarget.SINGLE, 1, 6, 0, 0, 0, 0, 0, 0, false, ItemSpriteSheet.WORN_SHORTSWORD),
	GUARD(1, "무당벌레 브로치", DeckCardType.SKILL, DeckCardRarity.COMMON, DeckCardTarget.NONE, 1, 0, 5, 0, 0, 0, 0, 0, false, ItemSpriteSheet.ARMOR_CLOTH),
	BASH(2, "파문 커터", DeckCardType.ATTACK, DeckCardRarity.UNCOMMON, DeckCardTarget.SINGLE, 2, 8, 0, 0, 2, 0, 0, 0, false, ItemSpriteSheet.THROWING_STONE),
	BREATH(3, "파문의 보호막", DeckCardType.SKILL, DeckCardRarity.COMMON, DeckCardTarget.NONE, 1, 0, 3, 1, 0, 0, 0, 0, true, ItemSpriteSheet.SEAL),
	STAR_FINGER(4, "매지션즈 레드의 사격 DISC", DeckCardType.ATTACK, DeckCardRarity.RARE, DeckCardTarget.RANDOM_ENEMY, 2, 13, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.WAND_FIREBOLT),
	RIPPLE_WALL(5, "개구리", DeckCardType.SKILL, DeckCardRarity.UNCOMMON, DeckCardTarget.NONE, 0, 0, 4, 1, 0, 0, 0, 0, true, ItemSpriteSheet.DEWDROP),
	SLIMY(6, "익사", DeckCardType.STATUS, DeckCardRarity.COMMON, DeckCardTarget.NONE, 1, 0, 0, 1, 0, 0, 1, keywords(DeckCardKeyword.EXHAUST), false, ItemSpriteSheet.MOB_HOLDER),
	IGNITE(7, "강화의 DISC", DeckCardType.POWER, DeckCardRarity.UNCOMMON, DeckCardTarget.NONE, 1, 0, 0, 0, 0, 2, 0, 0, true, ItemSpriteSheet.SCROLL_NAUDIZ),

	SHIV(8, "전갈탄", DeckCardType.ATTACK, DeckCardRarity.COMMON, DeckCardTarget.SINGLE, 0, 4, 0, 0, 0, 0, 0, keywords(DeckCardKeyword.EXHAUST), false, ItemSpriteSheet.SPIRIT_ARROW, HeroClass.HUNTRESS, 0),
	SCORPION_THROW(9, "전갈 투척", DeckCardType.SKILL, DeckCardRarity.COMMON, DeckCardTarget.NONE, 0, 0, 0, 1, 0, 0, 0, 0, false, ItemSpriteSheet.SPIRIT_BOW, HeroClass.HUNTRESS, 1),
	PHANTOM_BLADES(10, "감각 폭주", DeckCardType.POWER, DeckCardRarity.UNCOMMON, DeckCardTarget.NONE, 1, 0, 0, 0, 0, 0, 0, 0, true, Talent.FOLLOWUP_STRIKE, HeroClass.HUNTRESS, 0),
	ACCURACY(11, "생명 추적", DeckCardType.POWER, DeckCardRarity.UNCOMMON, DeckCardTarget.NONE, 1, 0, 0, 0, 0, 0, 0, 0, true, Talent.HEIGHTENED_SENSES, HeroClass.HUNTRESS, 0),
	KNIFE_TRAP(12, "무한한 가능성", DeckCardType.SKILL, DeckCardRarity.RARE, DeckCardTarget.SINGLE, 2, 0, 0, 0, 0, 0, 0, 0, true, Talent.SWIFT_SPIRIT, HeroClass.HUNTRESS, 0),
	LEADING_STRIKE(13, "자세 교정", DeckCardType.ATTACK, DeckCardRarity.COMMON, DeckCardTarget.SINGLE, 1, 3, 0, 0, 0, 0, 0, 0, true, Talent.SHARED_UPGRADES, HeroClass.HUNTRESS, 2),
	CLOAK_AND_DAGGER(14, "투쟁심", DeckCardType.SKILL, DeckCardRarity.COMMON, DeckCardTarget.NONE, 1, 0, 6, 0, 0, 0, 0, 0, true, Talent.POINT_BLANK, HeroClass.HUNTRESS, 1),

	ROTATING_NAIL(15, "회전하는 손톱", DeckCardType.ATTACK, DeckCardRarity.COMMON, DeckCardTarget.RANDOM_ENEMY, 1, 3, 0, 1, 0, 0, 0, keywords(DeckCardKeyword.CAST_ON_DRAW, DeckCardKeyword.EXHAUST), false, Talent.J43, HeroClass.JOHNNY, 0),
	TUSK_EQUIPMENT_DISC(16, "터스크의 장비 DISC", DeckCardType.ATTACK, DeckCardRarity.COMMON, DeckCardTarget.SINGLE, 1, 4, 0, 0, 0, 0, 0, keywords(DeckCardKeyword.AIM), false, ItemSpriteSheet.ARTIFACT_TUSK1, HeroClass.JOHNNY, 0),
	TRACKING_BULLET_HOLE(17, "추적하는 탄흔", DeckCardType.ATTACK, DeckCardRarity.UNCOMMON, DeckCardTarget.ALL_ENEMIES, 2, 12, 0, 0, 0, 0, 0, 0, true, Talent.J22, HeroClass.JOHNNY, 0),
	SPIN_TRAINING(18, "회전 훈련", DeckCardType.POWER, DeckCardRarity.UNCOMMON, DeckCardTarget.NONE, 1, 0, 0, 1, 0, 0, 0, keywords(DeckCardKeyword.AIM), true, Talent.J14, HeroClass.JOHNNY, 0),
	PROUD_STARVER(19, "긍지있게 굶주린 자", DeckCardType.SKILL, DeckCardRarity.UNCOMMON, DeckCardTarget.NONE, 4, 0, 0, 5, 0, 0, 0, 0, true, Talent.J21, HeroClass.JOHNNY, 0),
	LESSON_FIVE(20, "Lesson 5는 이걸 위해..", DeckCardType.SKILL, DeckCardRarity.RARE, DeckCardTarget.RANDOM_ENEMY, 2, 0, 0, 0, 0, 0, 0, 0, true, Talent.J36, HeroClass.JOHNNY, 0),
	STRIKE7(26, "손톱탄", DeckCardType.ATTACK, DeckCardRarity.COMMON, DeckCardTarget.SINGLE, 1, 50, 0, 0, 0, 0, 0, 0, false, ItemSpriteSheet.PICKAXE);


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
	public final HeroClass deckClass;
	public final int shivs;
	public final Talent talentIcon;

	DeckCard(int id, String title, DeckCardType type, DeckCardRarity rarity, DeckCardTarget target, int cost, int damage, int block, int draw, int vulnerable,
			 int strength, int handPenalty, int baseKeywords, boolean reward, int icon) {
		this(id, title, type, rarity, target, cost, damage, block, draw, vulnerable, strength, handPenalty, baseKeywords, reward, icon, null, 0, null);
	}

	DeckCard(int id, String title, DeckCardType type, DeckCardRarity rarity, DeckCardTarget target, int cost, int damage, int block, int draw, int vulnerable,
			 int strength, int handPenalty, int baseKeywords, boolean reward, Talent talentIcon) {
		this(id, title, type, rarity, target, cost, damage, block, draw, vulnerable, strength, handPenalty, baseKeywords, reward, 0, null, 0, talentIcon);
	}

	DeckCard(int id, String title, DeckCardType type, DeckCardRarity rarity, DeckCardTarget target, int cost, int damage, int block, int draw, int vulnerable,
			 int strength, int handPenalty, int baseKeywords, boolean reward, int icon, HeroClass deckClass, int shivs) {
		this(id, title, type, rarity, target, cost, damage, block, draw, vulnerable, strength, handPenalty, baseKeywords, reward, icon, deckClass, shivs, null);
	}

	DeckCard(int id, String title, DeckCardType type, DeckCardRarity rarity, DeckCardTarget target, int cost, int damage, int block, int draw, int vulnerable,
			 int strength, int handPenalty, int baseKeywords, boolean reward, Talent talentIcon, HeroClass deckClass, int shivs) {
		this(id, title, type, rarity, target, cost, damage, block, draw, vulnerable, strength, handPenalty, baseKeywords, reward, 0, deckClass, shivs, talentIcon);
	}

	DeckCard(int id, String title, DeckCardType type, DeckCardRarity rarity, DeckCardTarget target, int cost, int damage, int block, int draw, int vulnerable,
			 int strength, int handPenalty, int baseKeywords, boolean reward, int icon, HeroClass deckClass, int shivs, Talent talentIcon) {
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
		this.deckClass = deckClass;
		this.shivs = shivs;
		this.talentIcon = talentIcon;
	}

	public int code() {
		return id;
	}

	public String title(int code) {
		return upgradeLevel(code) > 0 ? "+" + title : title;
	}

	public int cost(int code) {
		if (this == LESSON_FIVE && upgradeLevel(code) > 0) return 1;
		return cost;
	}

	public int damage(int code) {
		if (this == SHIV) return damage + upgradeLevel(code) * 2;
		if (this == TUSK_EQUIPMENT_DISC) return damage + upgradeLevel(code) * 4;
		if (this == TRACKING_BULLET_HOLE) return upgradeLevel(code) > 0 ? 15 : damage;
		return damage > 0 ? damage + upgradeLevel(code) * 3 : 0;
	}

	public int block(int code) {
		if (this == CLOAK_AND_DAGGER) return block;
		return block > 0 ? block + upgradeLevel(code) * 3 : 0;
	}

	public int draw(int code) {
		if (this == PROUD_STARVER) return upgradeLevel(code) > 0 ? 6 : draw;
		return this == SCORPION_THROW ? draw + upgradeLevel(code) : draw;
	}

	public int vulnerable(int code) {
		return vulnerable;
	}

	public int strength(int code) {
		return strength > 0 ? strength + upgradeLevel(code) : 0;
	}

	public int shivs(int code) {
		return this == CLOAK_AND_DAGGER ? shivs + upgradeLevel(code) : shivs;
	}

	public int maxUpgradeLevel() {
		if (this == TUSK_EQUIPMENT_DISC) return 3;
		return 1;
	}

	public int effectiveCodeForPlay(int code, DeckBuilderCombat combat, int handIndex) {
		if (combat == null) return code;
		if (this == TUSK_EQUIPMENT_DISC && hasKeyword(code, DeckCardKeyword.AIM) && combat.isCenterHandIndex(handIndex)) {
			return upgradedForPlay(code);
		}
		if (hasKeyword(code, DeckCardKeyword.THROW) && combat.isEdgeHandIndex(handIndex)) {
			return upgradedForPlay(code);
		}
		return code;
	}

	public int upgradedForPlay(int code) {
		int upgrade = Math.min(maxUpgradeLevel(), upgradeLevel(code) + 1);
		return (code & ~(UPGRADE_MASK << UPGRADE_SHIFT)) | (upgrade << UPGRADE_SHIFT);
	}

	public boolean hasKeyword(int code, DeckCardKeyword keyword) {
		return (keywords(code) & keyword.bit) != 0;
	}

	public int keywords(int code) {
		return baseKeywords | keywordBits(code);
	}

	public DeckCardEffect[] effects(int code) {
		ArrayList<DeckCardEffect> effects = new ArrayList<>();
		if (damage(code) > 0) effects.add(new DeckCardEffects.Damage());
		if (block(code) > 0) effects.add(new DeckCardEffects.Block());
		if (vulnerable(code) > 0) effects.add(new DeckCardEffects.Vulnerable());
		if (draw(code) > 0) effects.add(new DeckCardEffects.Draw());
		if (strength(code) > 0) effects.add(new DeckCardEffects.Strength());
		if (shivs(code) > 0) effects.add(new DeckCardEffects.AddShivs());
		if (this == PHANTOM_BLADES) effects.add(new DeckCardEffects.PhantomBlades());
		if (this == ACCURACY) effects.add(new DeckCardEffects.Accuracy());
		if (this == KNIFE_TRAP) effects.add(new DeckCardEffects.KnifeTrap());
		if (this == TUSK_EQUIPMENT_DISC) effects.add(new DeckCardEffects.ShuffleIntoDrawPile(ROTATING_NAIL, 2));
		if (this == TRACKING_BULLET_HOLE) effects.add(new DeckCardEffects.ShuffleIntoDrawPile(ROTATING_NAIL, 3));
		if (this == SPIN_TRAINING) {
			effects.add(new DeckCardEffects.SpinningNailTraining());
			effects.add(new DeckCardEffects.AimShuffleIntoDrawPile(ROTATING_NAIL, 3));
		}
		if (this == LESSON_FIVE) effects.add(new DeckCardEffects.CopyAndPlayFromDrawPile(ROTATING_NAIL));
		return effects.toArray(new DeckCardEffect[0]);
	}

	public int classFaceColor() {
		if (deckClass == HeroClass.JOHNNY) return 0xFF4A213F;
		return 0xFF1A3D6B;
	}

	public int classPanelColor() {
		if (deckClass == HeroClass.JOHNNY) return 0xFF8D3F7F;
		return 0xFF2D5F9D;
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
		DeckCard card = byCode(code);
		int upgrade = Math.min(card.maxUpgradeLevel(), upgradeLevel(code) + 1);
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
		return rewardPool(null, false, false);
	}

	public static DeckCard[] rewardPool(HeroClass heroClass, boolean classOnly, boolean neutralOnly) {
		ArrayList<DeckCard> pool = new ArrayList<>();
		for (DeckCard card : values()) {
			if (!card.reward) continue;
			boolean classCard = card.deckClass != null;
			if (classOnly && card.deckClass != heroClass) continue;
			if (neutralOnly && classCard) continue;
			if (!classOnly && !neutralOnly && classCard && card.deckClass != heroClass) continue;
			pool.add(card);
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
