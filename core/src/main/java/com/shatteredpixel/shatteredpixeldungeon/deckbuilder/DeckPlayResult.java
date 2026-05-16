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

import java.util.ArrayList;

public class DeckPlayResult {

	public static final DeckPlayResult INVALID = new DeckPlayResult(false, null, 0, 0, 0, 0, 0, false, new ArrayList<Hit>());

	public final boolean played;
	public final DeckCard card;
	public final int damage;
	public final int block;
	public final int draw;
	public final int vulnerable;
	public final int strength;
	public final boolean exhausted;
	public final ArrayList<Hit> hits;

	public DeckPlayResult(boolean played, DeckCard card, int damage, int block, int draw, int vulnerable, int strength, boolean exhausted, ArrayList<Hit> hits) {
		this.played = played;
		this.card = card;
		this.damage = damage;
		this.block = block;
		this.draw = draw;
		this.vulnerable = vulnerable;
		this.strength = strength;
		this.exhausted = exhausted;
		this.hits = hits;
	}

	public static class Hit {
		public final int enemyIndex;
		public final int damage;
		public final int vulnerable;

		public Hit(int enemyIndex, int damage, int vulnerable) {
			this.enemyIndex = enemyIndex;
			this.damage = damage;
			this.vulnerable = vulnerable;
		}
	}

	public static class Builder {
		public final DeckCard card;
		public int damage;
		public int block;
		public int draw;
		public int vulnerable;
		public int strength;
		public boolean exhausted;
		public ArrayList<Hit> hits = new ArrayList<>();

		public Builder(DeckCard card) {
			this.card = card;
		}

		public void addHit(int enemyIndex, int damage, int vulnerable) {
			hits.add(new Hit(enemyIndex, damage, vulnerable));
			this.damage += damage;
			this.vulnerable += vulnerable;
		}

		public DeckPlayResult build() {
			return new DeckPlayResult(true, card, damage, block, draw, vulnerable, strength, exhausted, hits);
		}
	}
}
