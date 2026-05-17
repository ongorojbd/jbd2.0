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

import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public enum DeckPotion {

	HASTE("신속의 물약", "카드를 3장 뽑습니다.", ItemSpriteSheet.POTION_AZURE, ItemSpriteSheet.Icons.POTION_HASTE),
	FIRE("화염 물약", "모든 적에게 피해를 10 줍니다.", ItemSpriteSheet.POTION_BISTRE, ItemSpriteSheet.Icons.POTION_LIQFLAME),
	STRENGTH("힘의 물약", "공격력을 2 얻습니다.", ItemSpriteSheet.POTION_IVORY, ItemSpriteSheet.Icons.POTION_STRENGTH);

	public final String title;
	public final String description;
	public final int image;
	public final int icon;

	DeckPotion(String title, String description, int image, int icon) {
		this.title = title;
		this.description = description;
		this.image = image;
		this.icon = icon;
	}

	public static DeckPotion byId(int id) {
		DeckPotion[] values = values();
		if (id < 0 || id >= values.length) return null;
		return values[id];
	}
}
