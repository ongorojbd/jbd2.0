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

public interface DeckCardEffect {

	void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result);

	default void apply(DeckCardPlayContext context) {
		apply(context.combat, context.card, context.effectiveCardCode, context.result);
	}

	default String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
		return "";
	}

	default String keywordText(DeckCard card, int cardCode) {
		return "";
	}

	default String upgradePreviewText(DeckCard card, int cardCode, int upgradedCode) {
		return "";
	}

	default boolean conditionMet(DeckBuilderCombat combat, DeckCard card, int cardCode) {
		return false;
	}
}
