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
import com.watabou.utils.Random;

import java.util.ArrayList;

public enum DeckRelic {

	STARTER_LUCKY_COIN("행운의 동전", "기능 없는 시작 유물입니다.", DeckRelicRarity.COMMON, DeckRelicType.STARTER, ItemSpriteSheet.GOLD),
	STARTER_OLD_COMPASS("낡은 나침반", "기능 없는 시작 유물입니다.", DeckRelicRarity.COMMON, DeckRelicType.STARTER, ItemSpriteSheet.BEACON),
	STARTER_TINY_STAR("작은 별", "기능 없는 시작 유물입니다.", DeckRelicRarity.COMMON, DeckRelicType.STARTER, ItemSpriteSheet.SEED_STARFLOWER),

	NUTRITIOUS_OYSTER("영양만점 굴", "최대 체력이 11 증가합니다.", DeckRelicRarity.COMMON, DeckRelicType.COMMON, ItemSpriteSheet.MEAT_PIE) {
		@Override
		public void onAcquire() {
			DeckBuilderRun.playerHT += 11;
			DeckBuilderRun.playerHP += 11;
		}
	},
	COMMON_STONE("매끄러운 돌", "기능 없는 일반 유물입니다.", DeckRelicRarity.COMMON, DeckRelicType.COMMON, ItemSpriteSheet.THROWING_STONE),
	COMMON_BROOCH("낡은 브로치", "기능 없는 일반 유물입니다.", DeckRelicRarity.COMMON, DeckRelicType.COMMON, ItemSpriteSheet.ARTIFACT_TALISMAN),
	COMMON_CHARM("빛바랜 부적", "기능 없는 일반 유물입니다.", DeckRelicRarity.COMMON, DeckRelicType.COMMON, ItemSpriteSheet.ANKH),

	ARCANE_SCROLL("비전 두루마리", "획득 시 덱에 무작위 희귀 카드 1장을 추가합니다.", DeckRelicRarity.UNCOMMON, DeckRelicType.UNCOMMON, ItemSpriteSheet.SCROLL_CATALYST) {
		@Override
		public void onAcquire() {
			DeckCard card = randomCard(DeckCardRarity.RARE);
			if (card != null) {
				DeckBuilderRun.addCard(card);
			}
		}
	},
	UNCOMMON_LENS("균열 렌즈", "기능 없는 고급 유물입니다.", DeckRelicRarity.UNCOMMON, DeckRelicType.UNCOMMON, ItemSpriteSheet.ARTIFACT_HOURGLASS),
	UNCOMMON_FEATHER("가벼운 깃털", "기능 없는 고급 유물입니다.", DeckRelicRarity.UNCOMMON, DeckRelicType.UNCOMMON, ItemSpriteSheet.ELIXIR_FEATHER),
	UNCOMMON_ORB("푸른 구슬", "기능 없는 고급 유물입니다.", DeckRelicRarity.UNCOMMON, DeckRelicType.UNCOMMON, ItemSpriteSheet.ARCANE_RESIN),

	LARGE_CAPSULE("대형 캡슐", "획득 시 무작위 유물 2개를 얻습니다. 타격 1장과 수비 1장을 덱에 추가합니다.", DeckRelicRarity.RARE, DeckRelicType.RARE, ItemSpriteSheet.POTION_HOLDER) {
		@Override
		public void onAcquire() {
			DeckBuilderRun.addCard(DeckCard.STRIKE);
			DeckBuilderRun.addCard(DeckCard.GUARD);
			for (int i = 0; i < 2; i++) {
				DeckRelic relic = randomAvailable(this);
				if (relic != null) {
					DeckBuilderRun.addRelic(relic);
				}
			}
		}
	},
	BLACK_STAR("검은 별", "승리할 때 유물을 하나 더 선택합니다.", DeckRelicRarity.RARE, DeckRelicType.RARE, ItemSpriteSheet.ARTIFACT_TUSK4),
	RARE_MASK("황금 가면", "기능 없는 희귀 유물입니다.", DeckRelicRarity.RARE, DeckRelicType.RARE, ItemSpriteSheet.MASK),
	RARE_CHALICE("붉은 성배", "기능 없는 희귀 유물입니다.", DeckRelicRarity.RARE, DeckRelicType.RARE, ItemSpriteSheet.ARTIFACT_CHALICE3),
	RARE_BLADE("잠든 검", "기능 없는 희귀 유물입니다.", DeckRelicRarity.RARE, DeckRelicType.RARE, ItemSpriteSheet.RUNIC_BLADE),

	SHOP_LEDGER("상인의 장부", "기능 없는 상점 유물입니다.", DeckRelicRarity.COMMON, DeckRelicType.SHOP, ItemSpriteSheet.SCROLL_HOLDER),
	SHOP_PRICE_TAG("낡은 가격표", "기능 없는 상점 유물입니다.", DeckRelicRarity.UNCOMMON, DeckRelicType.SHOP, ItemSpriteSheet.GOLDEN_KEY),
	SHOP_GLASS_CASE("유리 진열장", "기능 없는 상점 유물입니다.", DeckRelicRarity.RARE, DeckRelicType.SHOP, ItemSpriteSheet.CRYSTAL_CHEST),

	ANCIENT_TABLET("고대 석판", "기능 없는 고대 유물입니다.", DeckRelicRarity.COMMON, DeckRelicType.ANCIENT, ItemSpriteSheet.STONE_CLAIRVOYANCE),
	ANCIENT_CORE("고대 핵", "기능 없는 고대 유물입니다.", DeckRelicRarity.UNCOMMON, DeckRelicType.ANCIENT, ItemSpriteSheet.ARTIFACT_HOLDER),
	ANCIENT_CROWN("낡은 왕관", "기능 없는 고대 유물입니다.", DeckRelicRarity.RARE, DeckRelicType.ANCIENT, ItemSpriteSheet.CROWN);

	public final String title;
	public final String description;
	public final DeckRelicRarity rarity;
	public final DeckRelicType type;
	public final int icon;

	DeckRelic(String title, String description, DeckRelicRarity rarity, DeckRelicType type, int icon) {
		this.title = title;
		this.description = description;
		this.rarity = rarity;
		this.type = type;
		this.icon = icon;
	}

	public void onAcquire() {
	}

	public boolean rewardPool() {
		return type == DeckRelicType.COMMON || type == DeckRelicType.UNCOMMON || type == DeckRelicType.RARE;
	}

	public static DeckRelic byId(int id) {
		DeckRelic[] values = values();
		if (id < 0 || id >= values.length) return NUTRITIOUS_OYSTER;
		return values[id];
	}

	public static DeckRelic randomAvailable(DeckRelic excluded) {
		ArrayList<DeckRelic> pool = new ArrayList<>();
		for (DeckRelic relic : values()) {
			if (relic.rewardPool() && relic != excluded && !DeckBuilderRun.hasRelic(relic)) {
				pool.add(relic);
			}
		}
		return pool.isEmpty() ? null : pool.get(Random.Int(pool.size()));
	}

	public static DeckRelic randomAvailable(DeckRelicRarity rarity) {
		return randomAvailable(rarity, false);
	}

	public static DeckRelic randomAvailable(DeckRelicRarity rarity, boolean shopOnly) {
		ArrayList<DeckRelic> pool = new ArrayList<>();
		for (DeckRelic relic : values()) {
			boolean typeAllowed = shopOnly ? relic.type == DeckRelicType.SHOP : relic.rewardPool();
			if (typeAllowed && relic.rarity == rarity && !DeckBuilderRun.hasRelic(relic)) {
				pool.add(relic);
			}
		}
		if (pool.isEmpty() && !shopOnly) return randomAvailable((DeckRelic)null);
		return pool.isEmpty() ? null : pool.get(Random.Int(pool.size()));
	}

	public static DeckRelic randomStarterAvailable(ArrayList<Integer> excludedIds) {
		ArrayList<DeckRelic> pool = new ArrayList<>();
		for (DeckRelic relic : values()) {
			if (relic.type == DeckRelicType.STARTER
					&& !DeckBuilderRun.hasRelic(relic)
					&& (excludedIds == null || !excludedIds.contains(relic.ordinal()))) {
				pool.add(relic);
			}
		}
		return pool.isEmpty() ? null : pool.get(Random.Int(pool.size()));
	}

	private static DeckCard randomCard(DeckCardRarity rarity) {
		ArrayList<DeckCard> pool = new ArrayList<>();
		for (DeckCard card : DeckCard.rewardPool()) {
			if (card.rarity == rarity) {
				pool.add(card);
			}
		}
		return pool.isEmpty() ? null : pool.get(Random.Int(pool.size()));
	}
}
