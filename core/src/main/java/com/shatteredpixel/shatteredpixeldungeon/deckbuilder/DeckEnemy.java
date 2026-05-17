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

	GEB_GOD("테스트용 적", 50, 5, true),
	HORUS("호루스신", 44, 9, false),
	CREAM("크림", 60, 11, false),
	SETESH("세트신", 38, 0, false),
	NDOUL("게브신", 55, 0, false),
	THE_FOOL("더 풀", 42, 0, false),
	TOWER_OF_GREY("타워 오브 그레이", 21, 0, false);

	public static final int ENCOUNTER_SETESH = 0;
	public static final int ENCOUNTER_NDOUL = 1;
	public static final int ENCOUNTER_THE_FOOL = 2;
	public static final int ENCOUNTER_TOWER_OF_GREY = 3;

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
		if (this == SETESH || this == NDOUL || this == THE_FOOL) return baseHP;
		if (this == TOWER_OF_GREY) return 21 + Random.Int(5);
		if (this == GEB_GOD) return baseHP;
		return baseHP + Math.max(1, depth) * 3;
	}

	public int nextIntent(int turn, int depth) {
		return nextIntent(turn, depth, 0);
	}

	public int nextIntent(int turn, int depth, int encounterIndex) {
		if (this == SETESH) {
			if (turn == 1) return DeckBuilderCombat.RESULT_AGE_DOWN;
			return turn % 2 == 0 ? 7 : 13;
		}
		if (this == NDOUL) {
			return turn % 3 == 2 ? DeckBuilderCombat.RESULT_STRENGTH_7 : 4;
		}
		if (this == THE_FOOL) {
			if (turn % 3 == 1) return 12;
			if (turn % 3 == 2) return DeckBuilderCombat.RESULT_ATTACK_6_BLOCK_5;
			return DeckBuilderCombat.RESULT_STRENGTH_2;
		}
		if (this == TOWER_OF_GREY) {
			if (turn == 1) return encounterIndex == 0 ? DeckBuilderCombat.RESULT_TOWER_NEEDLE : 7;
			int cycle = (turn - 2) % 3;
			if (cycle == 0) return DeckBuilderCombat.RESULT_MASSACRE;
			if (cycle == 1) return 7;
			return DeckBuilderCombat.RESULT_TOWER_NEEDLE;
		}
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

	public static DeckEnemy[] openingEncounter(int encounterId) {
		switch (encounterId) {
			case ENCOUNTER_SETESH:
				return new DeckEnemy[]{SETESH};
			case ENCOUNTER_NDOUL:
				return new DeckEnemy[]{NDOUL};
			case ENCOUNTER_THE_FOOL:
				return new DeckEnemy[]{THE_FOOL};
			case ENCOUNTER_TOWER_OF_GREY:
			default:
				return new DeckEnemy[]{TOWER_OF_GREY, TOWER_OF_GREY};
		}
	}

	public static int rollOpeningEncounter(int previousEncounterId) {
		int encounterId;
		do {
			encounterId = Random.Int(4);
		} while (encounterId == previousEncounterId);
		return encounterId;
	}
}
