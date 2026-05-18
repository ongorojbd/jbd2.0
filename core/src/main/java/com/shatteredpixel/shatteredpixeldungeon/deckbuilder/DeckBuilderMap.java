/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2025 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package com.shatteredpixel.shatteredpixeldungeon.deckbuilder;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;

public class DeckBuilderMap {

	public static final int NONE = 0;
	public static final int COMBAT = 1;
	public static final int ELITE = 2;
	public static final int SHOP = 3;
	public static final int EVENT = 4;
	public static final int REST = 5;
	public static final int BOSS = 6;
	public static final int TREASURE = 7;

	public static final int GRID_COLUMNS = 7;
	public static final int MAP_FLOORS = 15;
	public static final int FIRST_DEPTH = 2;
	public static final int BOSS_DEPTH = FIRST_DEPTH + MAP_FLOORS;
	public static final int BOSS_COLUMN = GRID_COLUMNS / 2;
	public static final int MAX_DEPTH = BOSS_DEPTH;
	public static final int MAX_NODES = GRID_COLUMNS;
	public static final int MAP_VERSION = 19;

	private static final int PATH_COUNT = 6;
	private static final int MAX_TYPE_ATTEMPTS = 250;

	public static void init() {
		if (validStoredMap()) {
			return;
		}

		Statistics.deckBuilderMapVersion = MAP_VERSION;
		Statistics.deckBuilderMapCounts = new int[MAX_DEPTH + 1];
		Statistics.deckBuilderMapTypes = new int[(MAX_DEPTH + 1) * MAX_NODES];
		Statistics.deckBuilderMapLinks = new int[(MAX_DEPTH + 1) * MAX_NODES];

		for (int depth = FIRST_DEPTH; depth <= MAX_DEPTH; depth++) {
			Statistics.deckBuilderMapCounts[depth] = GRID_COLUMNS;
		}

		generatePaths();
		assignNodeTypes();
		ensureNextChoice();
	}

	public static void ensureReadyForNextChoice() {
		init();
		if (!hasNextChoice()) {
			Statistics.deckBuilderMapVersion = 0;
			init();
		}
		if (!hasNextChoice()) {
			forceFirstFloorFallback();
		}
	}

	public static int[] choicesForDepth(int depth) {
		init();
		int count = rawCount(depth);
		int[] choices = new int[count];
		for (int i = 0; i < choices.length; i++) {
			choices[i] = type(depth, i);
		}
		return choices;
	}

	public static int count(int depth) {
		init();
		return rawCount(depth);
	}

	public static int type(int depth, int node) {
		init();
		if (node < 0 || node >= rawCount(depth)) return NONE;
		return Statistics.deckBuilderMapTypes[index(depth, node)];
	}

	public static int links(int depth, int node) {
		init();
		if (node < 0 || node >= rawCount(depth)) return 0;
		return Statistics.deckBuilderMapLinks[index(depth, node)];
	}

	public static int selectableMask(int depth) {
		init();
		return selectableMaskWithoutInit(depth);
	}

	private static int selectableMaskWithoutInit(int depth) {
		int count = rawCount(depth);
		if (count == 0) return 0;
		if (Dungeon.depth < FIRST_DEPTH
				|| Statistics.deckBuilderMapPath < 0
				|| Statistics.deckBuilderMapPath >= rawCount(Dungeon.depth)) {
			return occupiedMask(depth);
		}
		int mask = linksWithoutInit(Dungeon.depth, Statistics.deckBuilderMapPath);
		return mask & occupiedMask(depth);
	}

	public static boolean selectable(int depth, int node) {
		return node >= 0
				&& node < rawCount(depth)
				&& type(depth, node) != NONE
				&& (selectableMask(depth) & (1 << node)) != 0;
	}

	public static String key(int node) {
		switch (node) {
			case ELITE:
				return "elite";
			case SHOP:
				return "shop";
			case EVENT:
				return "event";
			case REST:
				return "rest";
			case BOSS:
				return "boss";
			case TREASURE:
				return "treasure";
			case COMBAT:
			default:
				return "combat";
		}
	}

	private static void generatePaths() {
		int[] starts = new int[PATH_COUNT];
		starts[0] = randomIndex(0, 0, 0, GRID_COLUMNS);
		starts[1] = randomIndex(0, 1, 0, GRID_COLUMNS);
		if (starts[1] == starts[0]) {
			starts[1] = (starts[0] + 1 + randomIndex(0, 1, 1, GRID_COLUMNS - 1)) % GRID_COLUMNS;
		}
		for (int path = 2; path < PATH_COUNT; path++) {
			starts[path] = randomIndex(0, path, 0, GRID_COLUMNS);
		}

		for (int path = 0; path < PATH_COUNT; path++) {
			int column = starts[path];
			for (int row = 0; row < MAP_FLOORS; row++) {
				int depth = depthForRow(row);
				Statistics.deckBuilderMapTypes[index(depth, column)] = COMBAT;

				if (row >= MAP_FLOORS - 1) continue;
				int linkIndex = index(depth, column);
				int existingLink = row == MAP_FLOORS - 2 ? Statistics.deckBuilderMapLinks[linkIndex] : 0;
				int nextColumn = existingLink == 0 ? nextColumn(row, column, path) : firstLinkedColumn(existingLink);
				Statistics.deckBuilderMapLinks[linkIndex] |= 1 << nextColumn;
				column = nextColumn;
			}
		}

		Statistics.deckBuilderMapTypes[index(BOSS_DEPTH, BOSS_COLUMN)] = BOSS;
		for (int column = 0; column < GRID_COLUMNS; column++) {
			if (Statistics.deckBuilderMapTypes[index(depthForFloor(15), column)] != NONE) {
				Statistics.deckBuilderMapLinks[index(depthForFloor(15), column)] = 1 << BOSS_COLUMN;
			}
		}
	}

	private static int firstLinkedColumn(int linkMask) {
		for (int column = 0; column < GRID_COLUMNS; column++) {
			if ((linkMask & (1 << column)) != 0) return column;
		}
		return 0;
	}

	private static int nextColumn(int row, int column, int path) {
		int min = Math.max(0, column - 1);
		int max = Math.min(GRID_COLUMNS - 1, column + 1);
		return min + randomIndex(row, column, path + 11, max - min + 1);
	}

	private static void assignNodeTypes() {
		for (int attempt = 0; attempt < MAX_TYPE_ATTEMPTS; attempt++) {
			assignNodeTypes(attempt);
			if (validNodeTypes()) return;
		}
		repairNodeTypes();
	}

	private static void repairNodeTypes() {
		for (int depth = FIRST_DEPTH; depth <= MAX_DEPTH; depth++) {
			int fixed = fixedType(depth);
			for (int column = 0; column < GRID_COLUMNS; column++) {
				int idx = index(depth, column);
				if (Statistics.deckBuilderMapTypes[idx] == NONE) continue;
				if (fixed != NONE) {
					Statistics.deckBuilderMapTypes[idx] = fixed;
				} else if ((Statistics.deckBuilderMapTypes[idx] == ELITE || Statistics.deckBuilderMapTypes[idx] == REST)
						&& mapFloor(depth) < 6) {
					Statistics.deckBuilderMapTypes[idx] = COMBAT;
				}
			}
		}

		for (int depth = FIRST_DEPTH; depth < MAX_DEPTH; depth++) {
			for (int column = 0; column < GRID_COLUMNS; column++) {
				int idx = index(depth, column);
				int type = Statistics.deckBuilderMapTypes[idx];
				if (type == NONE) continue;
				int links = Statistics.deckBuilderMapLinks[idx];
				if ((links & occupiedMask(depth + 1)) == 0) {
					int next = firstOccupiedColumn(depth + 1);
					if (next >= 0) Statistics.deckBuilderMapLinks[idx] = 1 << next;
				}
				if (restrictedRepeatType(type)) {
					for (int next = 0; next < GRID_COLUMNS; next++) {
						if ((Statistics.deckBuilderMapLinks[idx] & (1 << next)) != 0
								&& typeWithoutInit(depth + 1, next) == type
								&& fixedType(depth) == NONE) {
							Statistics.deckBuilderMapTypes[idx] = COMBAT;
							break;
						}
					}
				}
			}
		}

		Statistics.deckBuilderMapTypes[index(BOSS_DEPTH, BOSS_COLUMN)] = BOSS;
		for (int column = 0; column < GRID_COLUMNS; column++) {
			if (column != BOSS_COLUMN) Statistics.deckBuilderMapTypes[index(BOSS_DEPTH, column)] = NONE;
		}
	}

	private static boolean hasNextChoice() {
		int depth = Dungeon.depth < FIRST_DEPTH ? FIRST_DEPTH : Math.min(MAX_DEPTH, Dungeon.depth + 1);
		int mask = selectableMaskWithoutInit(depth);
		return mask != 0;
	}

	private static void ensureNextChoice() {
		if (hasNextChoice()) return;
		forceFirstFloorFallback();
	}

	private static void forceFirstFloorFallback() {
		int first = FIRST_DEPTH;
		for (int column = 0; column < GRID_COLUMNS; column++) {
			Statistics.deckBuilderMapCounts[first] = GRID_COLUMNS;
			if (Statistics.deckBuilderMapTypes[index(first, column)] == NONE) {
				Statistics.deckBuilderMapTypes[index(first, column)] = COMBAT;
			}
		}
	}

	private static int firstOccupiedColumn(int depth) {
		for (int column = 0; column < GRID_COLUMNS; column++) {
			if (typeWithoutInit(depth, column) != NONE) return column;
		}
		return -1;
	}

	private static void assignNodeTypes(int attempt) {
		for (int row = 0; row < MAP_FLOORS; row++) {
			int depth = depthForRow(row);
			for (int column = 0; column < GRID_COLUMNS; column++) {
				int idx = index(depth, column);
				if (Statistics.deckBuilderMapTypes[idx] == NONE) continue;
				int fixed = fixedType(depth);
				Statistics.deckBuilderMapTypes[idx] = fixed == NONE ? rollType(depth, column, attempt) : fixed;
			}
		}
	}

	private static int fixedType(int depth) {
		int floor = mapFloor(depth);
		if (floor == 1) return COMBAT;
		if (floor == 9) return TREASURE;
		if (floor == 15) return REST;
		if (depth == BOSS_DEPTH) return BOSS;
		return NONE;
	}

	private static int rollType(int depth, int column, int attempt) {
		for (int reroll = 0; reroll < 12; reroll++) {
			int roll = randomIndex(depth, column, attempt * 31 + reroll, 100);
			int type;
			if (roll < 45) type = COMBAT;
			else if (roll < 67) type = EVENT;
			else if (roll < 83) type = ELITE;
			else if (roll < 95) type = REST;
			else type = SHOP;

			if ((type == ELITE || type == REST) && mapFloor(depth) < 6) continue;
			return type;
		}
		return COMBAT;
	}

	private static boolean validNodeTypes() {
		for (int depth = FIRST_DEPTH; depth <= MAX_DEPTH; depth++) {
			for (int node = 0; node < GRID_COLUMNS; node++) {
				int type = typeWithoutInit(depth, node);
				if (type == NONE) continue;
				if (fixedType(depth) != NONE && type != fixedType(depth)) return false;
				if ((type == ELITE || type == REST) && mapFloor(depth) < 6) return false;

				int linkMask = linksWithoutInit(depth, node);
				if (depth < depthForFloor(15)
						&& depth + 1 != depthForFloor(9)
						&& duplicatedNextChoiceType(depth + 1, linkMask)) return false;
				if (linkedToSameRestrictedType(depth, node, type, linkMask)) return false;
			}
		}
		return true;
	}

	private static boolean duplicatedNextChoiceType(int nextDepth, int linkMask) {
		if (nextDepth > MAX_DEPTH) return false;
		int seen = 0;
		for (int next = 0; next < GRID_COLUMNS; next++) {
			if ((linkMask & (1 << next)) == 0) continue;
			int type = typeWithoutInit(nextDepth, next);
			if (type == NONE) continue;
			if ((seen & (1 << type)) != 0) return true;
			seen |= 1 << type;
		}
		return false;
	}

	private static boolean linkedToSameRestrictedType(int depth, int node, int type, int linkMask) {
		if (!restrictedRepeatType(type) || depth >= MAX_DEPTH) return false;
		for (int next = 0; next < GRID_COLUMNS; next++) {
			if ((linkMask & (1 << next)) != 0 && typeWithoutInit(depth + 1, next) == type) {
				return true;
			}
		}
		return false;
	}

	private static boolean restrictedRepeatType(int type) {
		return type == REST || type == SHOP || type == ELITE;
	}

	private static boolean validStoredMap() {
		return Statistics.deckBuilderMapVersion == MAP_VERSION
				&& Statistics.deckBuilderMapCounts != null
				&& Statistics.deckBuilderMapTypes != null
				&& Statistics.deckBuilderMapLinks != null
				&& Statistics.deckBuilderMapCounts.length > MAX_DEPTH
				&& Statistics.deckBuilderMapTypes.length >= (MAX_DEPTH + 1) * MAX_NODES
				&& Statistics.deckBuilderMapLinks.length >= (MAX_DEPTH + 1) * MAX_NODES
				&& validPathLayout()
				&& hasNextChoice();
	}

	private static boolean validPathLayout() {
		int starts = 0;
		for (int node = 0; node < GRID_COLUMNS; node++) {
			if (typeWithoutInit(FIRST_DEPTH, node) != NONE) starts++;
		}
		if (starts < 2) return false;

		if (typeWithoutInit(BOSS_DEPTH, BOSS_COLUMN) != BOSS) return false;
		for (int node = 0; node < GRID_COLUMNS; node++) {
			if (node != BOSS_COLUMN && typeWithoutInit(BOSS_DEPTH, node) != NONE) return false;
		}

		for (int depth = FIRST_DEPTH; depth <= MAX_DEPTH; depth++) {
			if (rawCount(depth) != GRID_COLUMNS) return false;
			int occupied = occupiedMask(depth);
			if (occupied == 0) return false;
			for (int node = 0; node < GRID_COLUMNS; node++) {
				int type = typeWithoutInit(depth, node);
				int links = linksWithoutInit(depth, node);
				if (type == NONE && links != 0) return false;
				if (depth < MAX_DEPTH && type != NONE && (links & occupiedMask(depth + 1)) == 0) return false;
			}
		}
		return true;
	}

	private static int occupiedMask(int depth) {
		int mask = 0;
		if (depth < FIRST_DEPTH || depth > MAX_DEPTH || Statistics.deckBuilderMapTypes == null) return mask;
		for (int node = 0; node < GRID_COLUMNS; node++) {
			if (typeWithoutInit(depth, node) != NONE) mask |= 1 << node;
		}
		return mask;
	}

	private static int rawCount(int depth) {
		if (depth < FIRST_DEPTH || depth > MAX_DEPTH || Statistics.deckBuilderMapCounts == null) return 0;
		return Statistics.deckBuilderMapCounts[depth];
	}

	private static int typeWithoutInit(int depth, int node) {
		if (Statistics.deckBuilderMapTypes == null || depth < FIRST_DEPTH || depth > MAX_DEPTH || node < 0 || node >= GRID_COLUMNS) return NONE;
		return Statistics.deckBuilderMapTypes[index(depth, node)];
	}

	private static int linksWithoutInit(int depth, int node) {
		if (Statistics.deckBuilderMapLinks == null || depth < FIRST_DEPTH || depth > MAX_DEPTH || node < 0 || node >= GRID_COLUMNS) return 0;
		return Statistics.deckBuilderMapLinks[index(depth, node)];
	}

	private static int depthForRow(int row) {
		return FIRST_DEPTH + row;
	}

	private static int depthForFloor(int floor) {
		return FIRST_DEPTH + floor - 1;
	}

	private static int mapFloor(int depth) {
		return depth - FIRST_DEPTH + 1;
	}

	private static int index(int depth, int node) {
		return depth * MAX_NODES + node;
	}

	private static int randomIndex(int depth, int choice, int attempt, int length) {
		long hash = Dungeon.seed;
		hash ^= depth * 0x9E3779B97F4A7C15L;
		hash ^= choice * 0xBF58476D1CE4E5B9L;
		hash ^= attempt * 0x94D049BB133111EBL;
		hash ^= (hash >>> 30);
		hash *= 0xBF58476D1CE4E5B9L;
		hash ^= (hash >>> 27);
		hash *= 0x94D049BB133111EBL;
		hash ^= (hash >>> 31);
		return (int)Math.floorMod(hash, length);
	}
}
