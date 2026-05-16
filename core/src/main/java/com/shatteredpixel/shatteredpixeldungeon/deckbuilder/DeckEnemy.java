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

import com.watabou.utils.Random;

public enum DeckEnemy {

	GEB_GOD("게브신", 45, 5, true),
	HORUS("호루스신", 44, 9, false),
	CREAM("크림", 60, 11, false);

	public final String name;
	public final int baseHP;
	public final int baseIntent;
	private final boolean injectsSlimy;

	DeckEnemy(String name, int baseHP, int baseIntent, boolean injectsSlimy) {
		this.name = name;
		this.baseHP = baseHP;
		this.baseIntent = baseIntent;
		this.injectsSlimy = injectsSlimy;
	}

	public int hpForDepth(int depth) {
		if (this == GEB_GOD) return baseHP;
		return baseHP + Math.max(1, depth) * 3;
	}

	public int nextIntent(int turn, int depth) {
		if (injectsSlimy && turn % 2 == 0) {
			return DeckBuilderCombat.RESULT_SLIMY_INJECT;
		}
		return baseIntent + Math.max(1, depth) / 2 + Random.Int(4);
	}

	public static DeckEnemy forNode(int nodeType) {
		if (nodeType == DeckBuilderMap.ELITE) return HORUS;
		if (nodeType == DeckBuilderMap.BOSS) return CREAM;
		return GEB_GOD;
	}

	public static DeckEnemy[] encounterForNode(int nodeType, int depth) {
		if (nodeType == DeckBuilderMap.BOSS) {
			return new DeckEnemy[]{CREAM};
		}
		if (nodeType == DeckBuilderMap.ELITE) {
			return new DeckEnemy[]{HORUS, GEB_GOD};
		}
		if (depth >= 3 && Random.Int(100) < 45) {
			return new DeckEnemy[]{GEB_GOD, GEB_GOD};
		}
		return new DeckEnemy[]{GEB_GOD};
	}
}
