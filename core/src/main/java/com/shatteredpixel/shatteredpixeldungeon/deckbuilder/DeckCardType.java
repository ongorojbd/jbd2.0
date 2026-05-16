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

public enum DeckCardType {

	ATTACK("공격", 0xFFE05245, 0xFFE8E8E8),
	SKILL("보조", 0xFF5B8FD9, 0xFFE8E8E8),
	POWER("지속", 0xFFE2C64D, 0xFFE8E8E8),
	STATUS("상태이상", 0xFF8A4A4A, 0xFFFF5A5A),
	CURSE("저주", 0xFF9C5DB8, 0xFFFF5A5A);

	public final String label;
	public final int borderColor;
	public final int labelColor;

	DeckCardType(String label, int borderColor, int labelColor) {
		this.label = label;
		this.borderColor = borderColor;
		this.labelColor = labelColor;
	}
}
