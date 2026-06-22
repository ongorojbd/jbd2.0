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

	public static final DeckPlayResult INVALID = new DeckPlayResult(false, null, 0, 0, 0, 0, 0, 0, 0, false, false, new ArrayList<Hit>(), new ArrayList<Shuffle>());

	public final boolean played;
	public final DeckCard card;
	public final int damage;
	public final int block;
	public final int draw;
	public final int vulnerable;
	public final int strength;
	public final int dexterity;
	public final int heal;
	public final boolean exhausted;
	public final boolean isRandomPlay;
	public final ArrayList<Hit> hits;
	public final ArrayList<Shuffle> shuffles;

	public DeckPlayResult(boolean played, DeckCard card, int damage, int block, int draw, int vulnerable, int strength, int dexterity, int heal, boolean exhausted, boolean isRandomPlay, ArrayList<Hit> hits, ArrayList<Shuffle> shuffles) {
		this.played = played;
		this.card = card;
		this.damage = damage;
		this.block = block;
		this.draw = draw;
		this.vulnerable = vulnerable;
		this.strength = strength;
		this.dexterity = dexterity;
		this.heal = heal;
		this.exhausted = exhausted;
		this.isRandomPlay = isRandomPlay;
		this.hits = hits;
		this.shuffles = shuffles;
	}

	public static class Hit {
		public final int enemyIndex;
		public final int damage;
		public final int vulnerable;
		public final int wave;
		public final boolean isAttack;

		public Hit(int enemyIndex, int damage, int vulnerable, int wave, boolean isAttack) {
			this.enemyIndex = enemyIndex;
			this.damage = damage;
			this.vulnerable = vulnerable;
			this.wave = wave;
			this.isAttack = isAttack;
		}
	}

	public static class Shuffle {
		public final DeckCard card;
		public final int count;

		public Shuffle(DeckCard card, int count) {
			this.card = card;
			this.count = count;
		}
	}

	public static class Builder {
		public final DeckCard card;
		public int damage;
		public int block;
		public int draw;
		public int vulnerable;
		public int strength;
		public int dexterity;
		public int heal;
		public boolean exhausted;
		public boolean isRandomPlay;
		public ArrayList<Hit> hits = new ArrayList<>();
		public ArrayList<Shuffle> shuffles = new ArrayList<>();
		private int currentWave = 0;

		public Builder(DeckCard card) {
			this(card, false);
		}

		public Builder(DeckCard card, boolean isRandomPlay) {
			this.card = card;
			this.isRandomPlay = isRandomPlay;
		}

		public void nextWave() { currentWave++; }

		public void addHit(int enemyIndex, int damage, int vulnerable) {
			hits.add(new Hit(enemyIndex, damage, vulnerable, currentWave, false));
			this.damage += damage;
			this.vulnerable += vulnerable;
		}

		public void addAttackHit(int enemyIndex, int damage) {
			hits.add(new Hit(enemyIndex, damage, 0, currentWave, true));
			this.damage += damage;
		}

		public void addShuffle(DeckCard card, int count) {
			if (card != null && count > 0) {
				shuffles.add(new Shuffle(card, count));
			}
		}

		public DeckPlayResult build() {
			return new DeckPlayResult(true, card, damage, block, draw, vulnerable, strength, dexterity, heal, exhausted, isRandomPlay, hits, shuffles);
		}
	}
}
