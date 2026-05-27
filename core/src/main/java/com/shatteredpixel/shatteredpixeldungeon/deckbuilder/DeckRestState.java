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

final class DeckRestState {

	int depth;
	int path;
	boolean used;
	boolean tentUsed;

	DeckRestState() {
		clear();
	}

	void clear() {
		depth = -1;
		path = -1;
		used = false;
		tentUsed = false;
	}

	void initFor(int depth, int path) {
		if (this.depth != depth || this.path != path) {
			this.depth = depth;
			this.path = path;
			used = false;
			tentUsed = false;
		}
	}
}
