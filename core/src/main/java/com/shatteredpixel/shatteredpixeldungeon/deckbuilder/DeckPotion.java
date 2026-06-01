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

	// 일반

	HASTE("신속의 물약", "카드를 3장 뽑습니다.", DeckPotionRarity.COMMON, ItemSpriteSheet.POTION_CRIMSON, ItemSpriteSheet.Icons.POTION_HASTE),
	FIRE("독성 물약", "적에게 피해를 20 줍니다.", DeckPotionRarity.COMMON, ItemSpriteSheet.POTION_AMBER, ItemSpriteSheet.Icons.POTION_LIQFLAME),
	STRENGTH("힘의 물약", "공격력을 2 얻습니다.", DeckPotionRarity.COMMON, ItemSpriteSheet.POTION_GOLDEN, ItemSpriteSheet.Icons.POTION_STRENGTH),

	ATTACK("투시 물약", "무작위 공격 카드 3장 중 1장을 선택해 손으로 가져옵니다. 이번 턴 동안 그 카드를 비용 없이 사용할 수 있습니다.", DeckPotionRarity.COMMON, ItemSpriteSheet.POTION_JADE, ItemSpriteSheet.Icons.POTION_STRENGTH),
	FLEX("마비 물약", "공격력을 5 얻습니다. 내 턴 종료 시 공격력을 5 잃습니다.", DeckPotionRarity.COMMON, ItemSpriteSheet.POTION_TURQUOISE, ItemSpriteSheet.Icons.POTION_MASTERY),
	COLORLESS("수리 물약", "무작위 공용 카드 3장 중 1장을 선택해 손으로 가져옵니다. 이번 턴 동안 그 카드를 비용 없이 사용할 수 있습니다.", DeckPotionRarity.COMMON, ItemSpriteSheet.LIQUID_METAL, ItemSpriteSheet.Icons.POTION_MINDVIS),
	DEXTERITY("정화 물약", "방어력 증가를 2 얻습니다.", DeckPotionRarity.COMMON, ItemSpriteSheet.POTION_INDIGO, ItemSpriteSheet.Icons.POTION_HASTE),
	BLOCK("투명화 물약", "보호막을 12 얻습니다.", DeckPotionRarity.COMMON, ItemSpriteSheet.POTION_MAGENTA, ItemSpriteSheet.Icons.POTION_SHIELDING),
	SPEED("부유 물약", "방어력 증가를 5 얻습니다. 내 턴 종료 시 방어력 증가를 5 잃습니다.", DeckPotionRarity.COMMON, ItemSpriteSheet.POTION_BISTRE, ItemSpriteSheet.Icons.POTION_HASTE),
	SKILL("LOCACACA 6251", "무작위 보조 카드 3장 중 1장을 선택해 손으로 가져옵니다. 이번 턴 동안 그 카드를 비용 없이 사용할 수 있습니다.", DeckPotionRarity.COMMON, ItemSpriteSheet.RO3, ItemSpriteSheet.Icons.POTION_MINDVIS),
	WEAK("서리 물약", "공격력 저하를 3 부여합니다.", DeckPotionRarity.COMMON, ItemSpriteSheet.POTION_IVORY, ItemSpriteSheet.Icons.POTION_PARAGAS),
	ENERGY("경험의 물약", "2 에너지를 얻습니다.", DeckPotionRarity.COMMON, ItemSpriteSheet.POTION_AZURE, ItemSpriteSheet.Icons.POTION_HASTE),
	VULNERABLE("폭포수 용액", "피해 증폭을 3 부여합니다.", DeckPotionRarity.COMMON, ItemSpriteSheet.BREW_AQUA, ItemSpriteSheet.Icons.POTION_LIQFLAME),
	POWER("치유 물약", "무작위 지속 카드 3장 중 1장을 선택해 손으로 가져옵니다. 이번 턴 동안 그 카드를 비용 없이 사용할 수 있습니다.", DeckPotionRarity.COMMON, ItemSpriteSheet.POTION_CHARCOAL, ItemSpriteSheet.Icons.POTION_MINDVIS),
	EXPLOSIVE_AMPHULE("화염 물약", "모든 적에게 피해를 10 줍니다.", DeckPotionRarity.COMMON, ItemSpriteSheet.POTION_SILVER, ItemSpriteSheet.Icons.POTION_DRGBREATH),

	// 특별

	FORTIFIER("보호막의 물약", "현재 보호막의 양을 3배로 만듭니다.", DeckPotionRarity.UNCOMMON, ItemSpriteSheet.EXOTIC_CRIMSON, ItemSpriteSheet.Icons.POTION_SHIELDING),
	TOUCH_OF_INSANITY("숙련의 물약", "손에서 카드를 1장 선택합니다. 이번 전투 동안 그 카드를 비용 없이 사용할 수 있습니다.", DeckPotionRarity.UNCOMMON, ItemSpriteSheet.EXOTIC_AMBER, ItemSpriteSheet.Icons.POTION_MASTERY),
	RADIANT_TINCTURE("죠스타의 물약", "1의 에너지를 얻습니다. 다음 3턴 동안 추가로 1의 에너지를 얻습니다.", DeckPotionRarity.UNCOMMON, ItemSpriteSheet.EXOTIC_GOLDEN, ItemSpriteSheet.Icons.POTION_DIVINE),
	CLARITY_EXTRACT("지구력의 물약", "카드를 1장 뽑습니다. 다음 3턴 시작 시 카드를 추가로 1장 뽑습니다.", DeckPotionRarity.UNCOMMON, ItemSpriteSheet.EXOTIC_JADE, ItemSpriteSheet.Icons.POTION_MAGISIGHT),
	CURE_ALL("천리안의 물약", "1의 에너지를 얻고 카드를 2장 뽑습니다.", DeckPotionRarity.UNCOMMON, ItemSpriteSheet.EXOTIC_TURQUOISE, ItemSpriteSheet.Icons.POTION_CLEANSE),
	HEART_OF_IRON("청정의 물약", "재생을 7 얻습니다.", DeckPotionRarity.UNCOMMON, ItemSpriteSheet.EXOTIC_AZURE, ItemSpriteSheet.Icons.POTION_EARTHARMR),
	GAMBLERS_BREW("연막 물약", "손의 카드를 원하는 만큼 버리고, 버린 만큼 카드를 뽑습니다.", DeckPotionRarity.UNCOMMON, ItemSpriteSheet.EXOTIC_INDIGO, ItemSpriteSheet.Icons.POTION_MINDVIS),
	FYSH_OIL("바위 인간의 물약", "공격력을 1 얻고 방어력 증가를 1 얻습니다.", DeckPotionRarity.UNCOMMON, ItemSpriteSheet.EXOTIC_MAGENTA, ItemSpriteSheet.Icons.POTION_STRENGTH),
	DUPLICATOR("불길의 물약", "이번 턴에 사용하는 다음 카드가 1번 추가로 사용됩니다.", DeckPotionRarity.UNCOMMON, ItemSpriteSheet.EXOTIC_BISTRE, ItemSpriteSheet.Icons.POTION_MASTERY),
	BINDING("부식 물약", "모든 적에게 공격력 저하를 1, 방어력 저하를 1 부여합니다.", DeckPotionRarity.UNCOMMON, ItemSpriteSheet.EXOTIC_CHARCOAL, ItemSpriteSheet.Icons.POTION_PARAGAS),
	STABLE_SERUM("순간 빙결 물약", "손에 있는 카드를 2턴 동안 보존합니다.", DeckPotionRarity.UNCOMMON, ItemSpriteSheet.EXOTIC_SILVER, ItemSpriteSheet.Icons.POTION_STAMINA),
	LIQUID_BRONZE("폭우 물약", "반격을 3 얻습니다.", DeckPotionRarity.UNCOMMON, ItemSpriteSheet.EXOTIC_IVORY, ItemSpriteSheet.Icons.POTION_EARTHARMR),
	FORGE_BLESSING("눈보라 용액", "손에 있는 모든 카드를 남은 전투 동안 강화합니다.", DeckPotionRarity.UNCOMMON, ItemSpriteSheet.BREW_BLIZZARD, ItemSpriteSheet.Icons.POTION_MASTERY),
	REGEN("F.F.의 용액", "체력을 8 회복합니다.", DeckPotionRarity.UNCOMMON, ItemSpriteSheet.ELIXIR_AQUA, ItemSpriteSheet.Icons.POTION_HEALING),
	POWDERED_DEMISE("괴염왕 용액", "적의 턴 종료 시 적이 체력을 9 잃습니다.", DeckPotionRarity.UNCOMMON, ItemSpriteSheet.ELIXIR_DRAGON, ItemSpriteSheet.Icons.POTION_TOXICGAS),

	// 희귀

	GIGANTIFICATION("복수의 물약", "다음에 사용하는 공격 카드의 피해량이 3배로 증가합니다.", DeckPotionRarity.RARE, ItemSpriteSheet.P1, ItemSpriteSheet.Icons.POTION_MASTERY),
	FRUIT_JUICE("시생인의 용액", "최대 체력을 5 얻습니다.", DeckPotionRarity.RARE, ItemSpriteSheet.ELIXIR_MIGHT, ItemSpriteSheet.Icons.POTION_HEALING),
	BEETLE_JUICE("스탠드 저항 용액", "다음 4턴 동안 적이 가하는 공격의 피해량이 30% 감소합니다.", DeckPotionRarity.RARE, ItemSpriteSheet.ELIXIR_ARCANE, ItemSpriteSheet.Icons.POTION_EARTHARMR),
	MAZALETHS_GIFT("폴포의 용액", "턴이 시작할 때마다 공격력을 1 얻습니다.", DeckPotionRarity.RARE, ItemSpriteSheet.ELIXIR_HONEY, ItemSpriteSheet.Icons.POTION_DIVINE),
	BOTTLED_POTENTIAL("전격 용액", "모든 카드를 뽑을 카드 더미에 섞어넣고 카드를 5장 뽑습니다.", DeckPotionRarity.RARE, ItemSpriteSheet.BREW_SHOCKING, ItemSpriteSheet.Icons.POTION_MAGISIGHT),
	SHIP_IN_A_BOTTLE("부유 용액", "보호막을 10 얻습니다. 다음 턴에 보호막을 10 얻습니다.", DeckPotionRarity.RARE, ItemSpriteSheet.ELIXIR_FEATHER, ItemSpriteSheet.Icons.POTION_SHIELDING),
	FAIRY_IN_A_BOTTLE("변형된 용액", "사망 시 이 물약이 버려지며, 최대 체력의 30%만큼 체력을 회복합니다.", DeckPotionRarity.RARE, ItemSpriteSheet.BREW_UNSTABLE, ItemSpriteSheet.Icons.POTION_DIVINE),
	SHACKLING("기화냉동 용액", "이번 턴 동안 모든 적이 공격력을 7 잃습니다.", DeckPotionRarity.RARE, ItemSpriteSheet.ELIXIR_ICY, ItemSpriteSheet.Icons.POTION_PARAGAS),
	SNECKO_OIL("광기의 피", "카드를 7장 뽑고, 이번 턴 동안 손에 있는 카드의 비용을 무작위로 변경합니다.", DeckPotionRarity.RARE, ItemSpriteSheet.ELIXIR_MIGHT, ItemSpriteSheet.Icons.POTION_MINDVIS),
	LIQUID_MEMORIES("산성 용액", "버린 카드 더미에서 카드를 1장 선택해 손으로 가져옵니다. 이번 턴 동안 비용이 0이 됩니다.", DeckPotionRarity.RARE, ItemSpriteSheet.BREW_CAUSTIC, ItemSpriteSheet.Icons.POTION_MINDVIS),
	ENTROPIC_BREW("물약 보관대", "비어있는 모든 포션 슬롯을 무작위 포션으로 채웁니다.", DeckPotionRarity.RARE, ItemSpriteSheet.POTION_HOLDER, ItemSpriteSheet.Icons.POTION_STRMCLOUD),
	PRECOGNITION_DROPLET("용암 용액", "뽑을 카드 더미에서 카드를 1장 선택해 손으로 가져옵니다.", DeckPotionRarity.RARE, ItemSpriteSheet.BREW_INFERNAL, ItemSpriteSheet.Icons.POTION_MAGISIGHT),
	OROBIC_ACID("눈보라 용액", "무작위 공격, 보조, 지속 카드를 손으로 가져옵니다. 이번 턴 동안 비용 없이 사용할 수 있습니다.", DeckPotionRarity.RARE, ItemSpriteSheet.BREW_BLIZZARD, ItemSpriteSheet.Icons.POTION_CORROGAS),
	DISTILLED_CHAOS("디스토션 용액", "뽑을 카드 더미 위에서부터 3장의 카드를 사용합니다.", DeckPotionRarity.RARE, ItemSpriteSheet.ELIXIR_TOXIC, ItemSpriteSheet.Icons.POTION_DRGBREATH),
	LUCKY_TONIC("환각의 물약", "다음 턴에 체력을 잃지 않습니다.", DeckPotionRarity.RARE, ItemSpriteSheet.P2, ItemSpriteSheet.Icons.POTION_STAMINA);

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
