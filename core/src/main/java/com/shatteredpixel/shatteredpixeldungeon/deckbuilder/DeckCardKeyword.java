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

public enum DeckCardKeyword {

	EXHAUST(1, "[소멸]", "사용하면 이번 전투에서 제거됩니다."),
	RETAIN(2, "[보존]", "턴 종료 시 버려지지 않고 손에 남습니다.");

	public final int bit;
	public final String label;
	public final String description;

	DeckCardKeyword(int bit, String label, String description) {
		this.bit = bit;
		this.label = label;
		this.description = description;
	}
}
