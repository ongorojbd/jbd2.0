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

package com.shatteredpixel.shatteredpixeldungeon.scenes;

import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.Button;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.watabou.noosa.ColorBlock;

class DeckRewardRow extends Button {

	private final int iconImage;
	private ItemSprite icon;
	private final String label;
	private ColorBlock bg;
	RenderedTextBlock text;
	boolean claimed;

	DeckRewardRow(int iconImage, String label) {
		this.iconImage = iconImage;
		this.label = label;
	}

	@Override
	protected void createChildren() {
		super.createChildren();
		bg = new ColorBlock(1, 1, 0xFF255C5F);
		bg.am = 0.92f;
		add(bg);
		text = PixelScene.renderTextBlock("", 6);
		text.hardlight(0xFFD8D1BD);
		add(text);
	}

	@Override
	protected void layout() {
		super.layout();
		bg.x = x;
		bg.y = y;
		bg.size(width, height);
		bg.am = claimed ? 0.45f : 0.92f;
		float textX = x + 10;
		if (iconImage >= 0) {
			if (icon == null) {
				icon = new ItemSprite(iconImage, null);
				add(icon);
			}
			icon.x = x + 7;
			icon.y = y + (height - icon.height()) / 2f;
			textX = x + 31;
		}
		text.text(claimed ? "획득 완료" : label);
		text.hardlight(claimed ? 0xFF9A9A9A : 0xFFD8D1BD);
		text.maxWidth((int)(width - (textX - x) - 7));
		text.setPos(textX, y + (height - text.height()) / 2f);
	}
}
