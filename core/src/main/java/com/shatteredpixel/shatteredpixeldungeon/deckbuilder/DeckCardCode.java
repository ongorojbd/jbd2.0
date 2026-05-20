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

public final class DeckCardCode {

	private static final int ID_MASK = 0xFF;
	private static final int UPGRADE_SHIFT = 8;
	private static final int UPGRADE_MASK = 0xF;
	private static final int KEYWORD_SHIFT = 12;
	private static final int CHARGE_SHIFT = 24;
	private static final int CHARGE_MASK = 0xF;

	private DeckCardCode() {
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
		DeckCard card = DeckCard.byCode(code);
		int upgrade = Math.min(card.maxUpgradeLevel(), upgradeLevel(code) + 1);
		return withUpgradeLevel(code, upgrade);
	}

	public static int withUpgradeLevel(int code, int upgrade) {
		return (code & ~(UPGRADE_MASK << UPGRADE_SHIFT)) | (upgrade << UPGRADE_SHIFT);
	}

	public static int maxCharge(int code) {
		DeckCard card = DeckCard.byCode(code);
		return DeckWandCards.maxCharge(card, code);
	}

	public static int currentCharge(int code) {
		int charge = (code >>> CHARGE_SHIFT) & CHARGE_MASK;
		if (charge == 0) {
			return maxCharge(code);
		}
		return charge;
	}

	public static int withCharge(int code, int charge) {
		return (code & ~(CHARGE_MASK << CHARGE_SHIFT)) | (charge << CHARGE_SHIFT);
	}
}
