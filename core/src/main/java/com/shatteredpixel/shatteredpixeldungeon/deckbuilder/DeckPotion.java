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

public enum DeckPotion {

	HASTE("신속의 물약", "카드를 3장 뽑습니다.", DeckPotionRarity.COMMON, ItemSpriteSheet.POTION_AZURE, ItemSpriteSheet.Icons.POTION_HASTE),
	FIRE("화염 포션", "적에게 피해를 20 줍니다.", DeckPotionRarity.COMMON, ItemSpriteSheet.POTION_BISTRE, ItemSpriteSheet.Icons.POTION_LIQFLAME),
	STRENGTH("공격력의 물약", "공격력을 2 얻습니다.", DeckPotionRarity.COMMON, ItemSpriteSheet.POTION_IVORY, ItemSpriteSheet.Icons.POTION_STRENGTH),
	GAMBLERS_BREW("도박꾼의 영액", "손의 카드를 원하는 만큼 버리고, 버린 만큼 카드를 뽑습니다.", DeckPotionRarity.UNCOMMON, ItemSpriteSheet.POTION_AMBER, ItemSpriteSheet.Icons.POTION_MINDVIS),
	ATTACK("공격 포션", "무작위 공격 카드 3장 중 1장을 선택해 손으로 가져옵니다. 이번 턴 동안 그 카드를 비용 없이 사용합니다.", DeckPotionRarity.COMMON, ItemSpriteSheet.POTION_CRIMSON, ItemSpriteSheet.Icons.POTION_STRENGTH),
	FLEX("몸풀기 포션", "공격력을 5 얻습니다. 내 턴 종료 시 공격력을 5 잃습니다.", DeckPotionRarity.COMMON, ItemSpriteSheet.POTION_AMBER, ItemSpriteSheet.Icons.POTION_MASTERY),
	COLORLESS("무색 포션", "무작위 공용 카드 3장 중 1장을 선택해 손으로 가져옵니다. 이번 턴 동안 그 카드를 비용 없이 사용합니다.", DeckPotionRarity.COMMON, ItemSpriteSheet.POTION_IVORY, ItemSpriteSheet.Icons.POTION_MINDVIS),
	DEXTERITY("민첩 포션", "방어력 증가를 2 얻습니다.", DeckPotionRarity.COMMON, ItemSpriteSheet.POTION_AZURE, ItemSpriteSheet.Icons.POTION_HASTE),
	BLOCK("방어도 포션", "보호막을 12 얻습니다.", DeckPotionRarity.COMMON, ItemSpriteSheet.POTION_INDIGO, ItemSpriteSheet.Icons.POTION_SHIELDING),
	SPEED("속도 포션", "방어력 증가를 5 얻습니다. 내 턴 종료 시 방어력 증가를 5 잃습니다.", DeckPotionRarity.COMMON, ItemSpriteSheet.POTION_AZURE, ItemSpriteSheet.Icons.POTION_HASTE),
	SKILL("스킬 포션", "무작위 보조 카드 3장 중 1장을 선택해 손으로 가져옵니다. 이번 턴 동안 그 카드를 비용 없이 사용합니다.", DeckPotionRarity.COMMON, ItemSpriteSheet.POTION_TURQUOISE, ItemSpriteSheet.Icons.POTION_MINDVIS),
	WEAK("약화 포션", "공격력 저하를 3 부여합니다.", DeckPotionRarity.COMMON, ItemSpriteSheet.POTION_BISTRE, ItemSpriteSheet.Icons.POTION_PARAGAS),
	ENERGY("에너지 포션", "2의 에너지를 얻습니다.", DeckPotionRarity.COMMON, ItemSpriteSheet.POTION_GOLDEN, ItemSpriteSheet.Icons.POTION_HASTE),
	VULNERABLE("취약 포션", "피해 증폭을 3 부여합니다.", DeckPotionRarity.COMMON, ItemSpriteSheet.POTION_MAGENTA, ItemSpriteSheet.Icons.POTION_LIQFLAME),
	POWER("파워 포션", "무작위 지속 카드 3장 중 1장을 선택해 손으로 가져옵니다. 이번 턴 동안 그 카드를 비용 없이 사용합니다.", DeckPotionRarity.COMMON, ItemSpriteSheet.POTION_INDIGO, ItemSpriteSheet.Icons.POTION_MINDVIS),
	FORTIFIER("강장제", "현재 보호막을 3배로 만듭니다.", DeckPotionRarity.UNCOMMON, ItemSpriteSheet.POTION_GOLDEN, ItemSpriteSheet.Icons.POTION_SHIELDING),
	TOUCH_OF_INSANITY("광기의 손길", "손에서 카드를 1장 선택합니다. 이번 전투 동안 그 카드를 비용 없이 사용합니다.", DeckPotionRarity.UNCOMMON, ItemSpriteSheet.POTION_MAGENTA, ItemSpriteSheet.Icons.POTION_MASTERY),
	RADIANT_TINCTURE("광휘의 팅크", "1의 에너지를 얻습니다. 다음 3턴 동안 추가로 1의 에너지를 얻습니다.", DeckPotionRarity.UNCOMMON, ItemSpriteSheet.POTION_GOLDEN, ItemSpriteSheet.Icons.POTION_DIVINE),
	CLARITY_EXTRACT("명확성 추출물", "카드를 1장 뽑습니다. 다음 3턴 시작 시 카드를 추가로 1장 뽑습니다.", DeckPotionRarity.UNCOMMON, ItemSpriteSheet.POTION_TURQUOISE, ItemSpriteSheet.Icons.POTION_MAGISIGHT),
	CURE_ALL("묘약", "1의 에너지를 얻고 카드를 2장 뽑습니다.", DeckPotionRarity.UNCOMMON, ItemSpriteSheet.POTION_SILVER, ItemSpriteSheet.Icons.POTION_CLEANSE),
	HEART_OF_IRON("무쇠의 심장", "재생을 7 얻습니다.", DeckPotionRarity.UNCOMMON, ItemSpriteSheet.POTION_CHARCOAL, ItemSpriteSheet.Icons.POTION_EARTHARMR),
	FYSH_OIL("물교기 기름", "공격력을 1 얻고 방어력 증가를 1 얻습니다.", DeckPotionRarity.UNCOMMON, ItemSpriteSheet.POTION_AMBER, ItemSpriteSheet.Icons.POTION_STRENGTH),
	DUPLICATOR("복제액", "이번 턴에 사용하는 다음 카드가 1번 추가로 사용됩니다.", DeckPotionRarity.UNCOMMON, ItemSpriteSheet.POTION_IVORY, ItemSpriteSheet.Icons.POTION_MASTERY),
	BINDING("속박의 포션", "모든 적에게 공격력 저하를 1, 방어력 저하를 1 부여합니다.", DeckPotionRarity.UNCOMMON, ItemSpriteSheet.POTION_INDIGO, ItemSpriteSheet.Icons.POTION_PARAGAS),
	STABLE_SERUM("안정된 혈청", "손에 있는 카드를 2턴 동안 보존합니다.", DeckPotionRarity.UNCOMMON, ItemSpriteSheet.POTION_JADE, ItemSpriteSheet.Icons.POTION_STAMINA),
	LIQUID_BRONZE("액상 청동", "반격을 3 얻습니다.", DeckPotionRarity.UNCOMMON, ItemSpriteSheet.POTION_BISTRE, ItemSpriteSheet.Icons.POTION_EARTHARMR),
	FORGE_BLESSING("재련의 축복", "손에 있는 모든 카드를 남은 전투 동안 강화합니다.", DeckPotionRarity.UNCOMMON, ItemSpriteSheet.POTION_CRIMSON, ItemSpriteSheet.Icons.POTION_MASTERY),
	REGEN("재생 포션", "체력을 8 회복합니다.", DeckPotionRarity.UNCOMMON, ItemSpriteSheet.POTION_JADE, ItemSpriteSheet.Icons.POTION_HEALING),
	POWDERED_DEMISE("종언의 가루", "적의 턴 종료 시 적이 체력을 9 잃습니다.", DeckPotionRarity.UNCOMMON, ItemSpriteSheet.POTION_CHARCOAL, ItemSpriteSheet.Icons.POTION_TOXICGAS);

	public final String title;
	public final String description;
	public final DeckPotionRarity rarity;
	public final int image;
	public final int icon;

	DeckPotion(String title, String description, DeckPotionRarity rarity, int image, int icon) {
		this.title = title;
		this.description = description;
		this.rarity = rarity;
		this.image = image;
		this.icon = icon;
	}

	public static DeckPotion byId(int id) {
		DeckPotion[] values = values();
		if (id < 0 || id >= values.length) return null;
		return values[id];
	}
}
