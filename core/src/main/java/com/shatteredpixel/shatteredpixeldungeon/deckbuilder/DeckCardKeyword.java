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

	EXHAUST(1, "_\uC18C\uBA78_", "\uC0AC\uC6A9\uD558\uBA74 \uC774\uBC88 \uC804\uD22C\uC5D0\uC11C \uC81C\uAC70\uB429\uB2C8\uB2E4."),
	RETAIN(2, "_\uBCF4\uC874_", "\uD134 \uC885\uB8CC \uC2DC \uBC84\uB824\uC9C0\uC9C0 \uC54A\uACE0 \uC190\uC5D0 \uB0A8\uC2B5\uB2C8\uB2E4."),
	CAST_ON_DRAW(4, "_\uBFD1\uC744 \uB54C \uC2DC\uC804_", "\uC774 \uCE74\uB4DC\uB97C \uBFD1\uC73C\uBA74 \uC790\uB3D9\uC73C\uB85C \uC2DC\uC804\uB429\uB2C8\uB2E4."),
	AIM(8, "_\uC870\uC900_", "\uC190\uD328\uC758 \uC815\uC911\uC559\uC5D0\uC11C \uC0AC\uC6A9\uD558\uBA74 \uAC15\uD654 \uD6A8\uACFC\uAC00 \uBC1C\uB3D9\uD569\uB2C8\uB2E4."),
	THROW(16, "_\uD22C\uCC99_", "\uC190\uD328\uC758 \uAC00\uC7A5 \uC67C\uCABD \uB610\uB294 \uAC00\uC7A5 \uC624\uB978\uCABD\uC5D0\uC11C \uC0AC\uC6A9\uD558\uBA74 \uAC15\uD654 \uD6A8\uACFC\uAC00 \uBC1C\uB3D9\uD569\uB2C8\uB2E4."),
	VANGUARD(32, "_\uC120\uBD09_", "\uC804\uD22C \uC2DC\uC791 \uC2DC \uCCAB \uD328\uC5D0 \uB4E4\uC5B4\uC635\uB2C8\uB2E4."),
	TRANSIENT(64, "_\uC77C\uC2DC\uC801_", "\uB0B4 \uD134\uC774 \uB05D\uB0A0 \uB54C \uC190\uC5D0 \uB4E4\uACE0 \uC788\uB2E4\uBA74 \uC18C\uBA78\uD569\uB2C8\uB2E4."),
	ZERO_COST(128, "", "");

	public final int bit;
	public final String label;
	public final String description;

	DeckCardKeyword(int bit, String label, String description) {
		this.bit = bit;
		this.label = label;
		this.description = description;
	}
}
