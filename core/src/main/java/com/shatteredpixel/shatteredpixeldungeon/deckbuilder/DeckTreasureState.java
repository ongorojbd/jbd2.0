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

final class DeckTreasureState {

	int depth;
	int path;
	int chest;
	int relic;
	boolean claimed;

	DeckTreasureState() {
		clear();
	}

	void clear() {
		depth = -1;
		path = -1;
		chest = 0;
		relic = -1;
		claimed = false;
	}

	boolean needsRoll(int depth, int path) {
		return this.depth != depth || this.path != path || relic < 0;
	}
}
