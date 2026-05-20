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

public class DeckRunInventory {

	public static void applyExhaust(ArrayList<Integer> deck, ArrayList<Integer> exhausted) {
		for (int code : exhausted) {
			deck.remove(Integer.valueOf(code));
		}
	}

	public static void addCard(ArrayList<Integer> deck, DeckCard card) {
		if (card != null) deck.add(card.code());
	}

	public static boolean upgradeCardAt(ArrayList<Integer> deck, int index) {
		if (index < 0 || index >= deck.size()) return false;
		int cardCode = deck.get(index);
		int upgraded = DeckCardCode.upgrade(cardCode);
		if (upgraded == cardCode) return false;
		deck.set(index, upgraded);
		return true;
	}

	public static boolean removeCardAt(ArrayList<Integer> deck, int index) {
		if (index < 0 || index >= deck.size()) return false;
		deck.remove(index);
		return true;
	}

	public static void addRelic(ArrayList<Integer> relics, DeckRelic relic) {
		if (relic == null || hasRelic(relics, relic)) return;
		relics.add(relic.ordinal());
		relic.onAcquire();
	}

	public static boolean hasRelic(ArrayList<Integer> relics, DeckRelic relic) {
		return relic != null && relics.contains(relic.ordinal());
	}

	public static DeckPotion potionAt(ArrayList<Integer> potions, int slot) {
		if (slot < 0 || slot >= potions.size()) return null;
		return DeckPotion.byId(potions.get(slot));
	}

	public static boolean addPotion(ArrayList<Integer> potions, DeckPotion potion, int maxSlots) {
		if (potion == null || potions.size() >= maxSlots) return false;
		potions.add(potion.ordinal());
		return true;
	}

	public static void removePotion(ArrayList<Integer> potions, int slot) {
		if (slot >= 0 && slot < potions.size()) {
			potions.remove(slot);
		}
	}

	public static void addCopies(ArrayList<Integer> deck, DeckCard card, int count) {
		for (int i = 0; i < count; i++) {
			addCard(deck, card);
		}
	}

	public static String relicListText(ArrayList<Integer> relics) {
		if (relics.isEmpty()) return "획득한 유물이 없습니다.";
		String text = "";
		for (int id : relics) {
			DeckRelic relic = DeckRelic.byId(id);
			text += (text.length() > 0 ? "\n\n" : "") + relic.title + "\n" + relic.description;
		}
		return text;
	}

	static int[] toArray(ArrayList<Integer> list) {
		int[] result = new int[list.size()];
		for (int i = 0; i < list.size(); i++) {
			result[i] = list.get(i);
		}
		return result;
	}
}
