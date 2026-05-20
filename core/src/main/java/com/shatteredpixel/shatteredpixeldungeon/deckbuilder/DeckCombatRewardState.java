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

public class DeckCombatRewardState {

	public int node;
	public int depth;
	public int path;
	public int gold;
	public int[] relics;
	public int potion;
	public int[] cards;
	public boolean goldClaimed;
	public boolean[] relicClaimed;
	public boolean potionClaimed;
	public boolean cardClaimed;

	public DeckCombatRewardState() {
		clear();
	}

	public void clear() {
		node = DeckBuilderMap.NONE;
		depth = -1;
		path = -1;
		gold = 0;
		relics = null;
		potion = -1;
		cards = null;
		goldClaimed = false;
		relicClaimed = null;
		potionClaimed = false;
		cardClaimed = false;
	}

	public boolean matches(int node, int depth, int path) {
		return this.node == node && this.depth == depth && this.path == path && cards != null;
	}

	public DeckRelic relicAt(int index) {
		if (relics == null || index < 0 || index >= relics.length) return null;
		return DeckRelic.byId(relics[index]);
	}

	public DeckPotion potion() {
		return DeckPotion.byId(potion);
	}

	public DeckCard[] cardChoices() {
		if (cards == null) return new DeckCard[0];
		DeckCard[] result = new DeckCard[cards.length];
		for (int i = 0; i < cards.length; i++) {
			result[i] = DeckCard.byId(cards[i]);
		}
		return result;
	}
}
