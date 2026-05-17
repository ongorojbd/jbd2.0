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

	NUTRITIOUS_OYSTER("영양만점 굴", "최대 체력이 11 증가합니다.", DeckRelicRarity.COMMON) {
		@Override
		public void onAcquire() {
			DeckBuilderRun.playerHT += 11;
			DeckBuilderRun.playerHP += 11;
		}
	},

	ARCANE_SCROLL("비전 두루마리", "획득 시, 덱에 무작위 희귀 카드를 1장 추가합니다.", DeckRelicRarity.UNCOMMON) {
		@Override
		public void onAcquire() {
			DeckCard card = randomCard(DeckCardRarity.RARE);
			if (card != null) {
				DeckBuilderRun.addCard(card);
			}
		}
	},

	LARGE_CAPSULE("대형 캡슐", "획득 시, 무작위 유물을 2개 얻습니다. 타격 1장과 수비 1장을 덱에 추가합니다.", DeckRelicRarity.RARE) {
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

	BLACK_STAR("검은 별", "엘리트가 유물을 하나 더 드롭합니다.", DeckRelicRarity.RARE);

	public final String title;
	public final String description;
	public final DeckRelicRarity rarity;
	public final int icon;

	DeckRelic(String title, String description, DeckRelicRarity rarity) {
		this.title = title;
		this.description = description;
		this.rarity = rarity;
		this.icon = ItemSpriteSheet.SOMETHING;
	}

	public void onAcquire() {
	}

	public static DeckRelic byId(int id) {
		DeckRelic[] values = values();
		if (id < 0 || id >= values.length) return NUTRITIOUS_OYSTER;
		return values[id];
	}

	public static DeckRelic randomAvailable(DeckRelic excluded) {
		ArrayList<DeckRelic> pool = new ArrayList<>();
		for (DeckRelic relic : values()) {
			if (relic != excluded && !DeckBuilderRun.hasRelic(relic)) {
				pool.add(relic);
			}
		}
		return pool.isEmpty() ? null : pool.get(Random.Int(pool.size()));
	}

	public static DeckRelic randomAvailable(DeckRelicRarity rarity) {
		ArrayList<DeckRelic> pool = new ArrayList<>();
		for (DeckRelic relic : values()) {
			if (relic.rarity == rarity && !DeckBuilderRun.hasRelic(relic)) {
				pool.add(relic);
			}
		}
		if (pool.isEmpty()) return randomAvailable((DeckRelic)null);
		return pool.get(Random.Int(pool.size()));
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
