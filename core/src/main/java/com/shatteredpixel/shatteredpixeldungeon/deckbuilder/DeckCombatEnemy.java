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

public class DeckCombatEnemy {

	public final DeckEnemy kind;
	public final String name;
	public int ht;

	public int hp;
	public int intent;
	public int vulnerable;
	public int strength;
	public int block;
	public int thorns;
	public int platedArmor;
	public int artifact;
	public int tricky;
	public int blockReduction;
	public int venom;
	public int lastIntent;
	public boolean splitUsed;

	public DeckCombatEnemy(DeckEnemy kind, int depth) {
		this.kind = kind;
		this.name = kind.name;
		this.ht = kind.hpForDepth(depth);
		this.hp = ht;
		kind.initialize(this);
	}

	public boolean alive() {
		return hp > 0;
	}
}
