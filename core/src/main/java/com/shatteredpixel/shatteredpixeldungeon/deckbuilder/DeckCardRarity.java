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

public enum DeckCardRarity {

	COMMON("일반", 0xFF252525, 0xFF3E3E3E, 0xFFE8E8E8),
	UNCOMMON("고급", 0xFF252525, 0xFF3E3E3E, 0xFF48C8FF),
	RARE("희귀", 0xFF252525, 0xFF3E3E3E, 0xFFCC66FF);

	public final String label;
	public final int faceColor;
	public final int panelColor;
	public final int labelColor;

	DeckCardRarity(String label, int faceColor, int panelColor, int labelColor) {
		this.label = label;
		this.faceColor = faceColor;
		this.panelColor = panelColor;
		this.labelColor = labelColor;
	}
}
