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

public class DeckCardPlayContext {

	public final DeckBuilderCombat combat;
	public final DeckCard card;
	public final int cardCode;
	public final int effectiveCardCode;
	public final int handIndex;
	public final boolean castOnDraw;
	public final boolean aimActive;
	public final boolean throwActive;
	public final DeckPlayResult.Builder result;

	public DeckCardPlayContext(DeckBuilderCombat combat, DeckCard card, int cardCode, int effectiveCardCode,
							   int handIndex, boolean castOnDraw, boolean aimActive, boolean throwActive, DeckPlayResult.Builder result) {
		this.combat = combat;
		this.card = card;
		this.cardCode = cardCode;
		this.effectiveCardCode = effectiveCardCode;
		this.handIndex = handIndex;
		this.castOnDraw = castOnDraw;
		this.aimActive = aimActive;
		this.throwActive = throwActive;
		this.result = result;
	}
}
