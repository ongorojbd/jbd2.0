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

public enum DeckCardTarget {

	NONE("대상 없음"),
	SINGLE("단일 대상"),
	ALL_ENEMIES("모든 적"),
	RANDOM_ENEMY("무작위 적");

	public final String label;

	DeckCardTarget(String label) {
		this.label = label;
	}
}
