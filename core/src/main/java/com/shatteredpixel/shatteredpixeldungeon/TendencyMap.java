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
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon;

public class TendencyMap {

	public static final int NONE = 0;
	public static final int COMBAT = 1;
	public static final int ELITE = 2;
	public static final int SHOP = 3;
	public static final int EVENT = 4;
	public static final int REST = 5;
	public static final int BOSS = 6;
	public static final int TREASURE = 7;

	public static final int MAX_DEPTH = 63;
	public static final int MAX_NODES = 3;
	public static final int MAP_VERSION = 17;

	public static void init() {
		if (Statistics.tendencyMapVersion == MAP_VERSION
				&& Statistics.tendencyMapCounts != null
				&& Statistics.tendencyMapTypes != null
				&& Statistics.tendencyMapLinks != null
				&& Statistics.tendencyMapCounts.length > MAX_DEPTH
				&& Statistics.tendencyMapTypes.length >= (MAX_DEPTH + 1) * MAX_NODES
				&& Statistics.tendencyMapLinks.length >= (MAX_DEPTH + 1) * MAX_NODES
				&& hasNonCombatNodes()
				&& hasRequiredQuotas()) {
			return;
		}

		Statistics.tendencyMapVersion = MAP_VERSION;
		Statistics.tendencyMapCounts = new int[MAX_DEPTH + 1];
		Statistics.tendencyMapTypes = new int[(MAX_DEPTH + 1) * MAX_NODES];
		Statistics.tendencyMapLinks = new int[(MAX_DEPTH + 1) * MAX_NODES];

		for (int depth = 2; depth <= MAX_DEPTH; depth++) {
			int count = nodeCount(depth);
			Statistics.tendencyMapCounts[depth] = count;
			for (int i = 0; i < count; i++) {
				Statistics.tendencyMapTypes[index(depth, i)] = nodeType(depth, i);
			}
		}

		enforceTypeQuotas();

		for (int depth = 2; depth < MAX_DEPTH; depth++) {
			int count = rawCount(depth);
			int nextCount = rawCount(depth + 1);
			for (int i = 0; i < count; i++) {
				int mask;
				if (depth % 9 == 0) {
					mask = (1 << nextCount) - 1;
				} else {
					int center = count == 1 ? randomIndex(depth, i, 0, nextCount) : Math.round(i * (nextCount - 1) / (float)(count - 1));
					mask = 1 << center;
					if (nextCount > 1 && randomIndex(depth, i, 1, 100) < 55) {
						int side = center == 0 ? 1 : center == nextCount - 1 ? center - 1 : center + (randomIndex(depth, i, 2, 2) == 0 ? -1 : 1);
						mask |= 1 << side;
					}
				}
				Statistics.tendencyMapLinks[index(depth, i)] = mask;
			}

			for (int j = 0; j < nextCount; j++) {
				if (!hasInbound(depth, j)) {
					int nearest = count == 1 ? 0 : Math.round(j * (count - 1) / (float)(nextCount - 1));
					Statistics.tendencyMapLinks[index(depth, nearest)] |= 1 << j;
				}
			}
		}
	}

	public static int[] choicesForDepth(int depth) {
		init();
		int count = rawCount(depth);
		int[] choices = new int[count];
		for (int i = 0; i < count; i++) {
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
		return Statistics.tendencyMapTypes[index(depth, node)];
	}

	public static int links(int depth, int node) {
		init();
		if (node < 0 || node >= rawCount(depth)) return 0;
		return Statistics.tendencyMapLinks[index(depth, node)];
	}

	public static int selectableMask(int depth) {
		init();
		int count = rawCount(depth);
		if (count == 0) return 0;
		if (Dungeon.depth < 2 || Statistics.tendencyMapPath < 0 || Statistics.tendencyMapPath >= rawCount(Dungeon.depth)) {
			return (1 << count) - 1;
		}
		int mask = links(Dungeon.depth, Statistics.tendencyMapPath);
		if (mask == 0) mask = (1 << count) - 1;
		return mask;
	}

	public static boolean selectable(int depth, int node) {
		return (selectableMask(depth) & (1 << node)) != 0;
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

	private static int nodeCount(int depth) {
		if (depth % 9 == 0) return 1;
		return 3;
	}

	private static int nodeType(int depth, int node) {
		if (depth % 9 == 0) return BOSS;
		if (isTreasure(depth, node)) return TREASURE;
		if (isShop(depth, node)) return SHOP;

		switch (randomIndex(depth, node, 11, 9)) {
			case 0:
			case 1:
				return ELITE;
			case 2:
				return restOrCombat(depth);
			case 3:
				return eventOrElite(depth);
			default:
				return COMBAT;
		}
	}

	public static boolean isShop(int depth, int node) {
		if (depth < 2 || depth > MAX_DEPTH || depth % 9 == 0 || depth % 2 != 0 || node < 0 || node >= MAX_NODES) return false;
		return node == shopNode(depth);
	}

	public static int shopNode(int depth) {
		return randomIndex(depth, 0, 31, MAX_NODES);
	}

	public static boolean restAllowed(int depth) {
		return depth % 2 == 0;
	}

	private static int restOrCombat(int depth) {
		return restAllowed(depth) ? REST : COMBAT;
	}

	public static boolean eventAllowed(int depth) {
		int floorInSegment = depth % 9;
		return floorInSegment != 1 && floorInSegment != 8 && floorInSegment != 0;
	}

	private static int eventOrElite(int depth) {
		return eventAllowed(depth) ? EVENT : ELITE;
	}

	public static boolean isTreasure(int depth, int node) {
		if (depth < 2 || depth > MAX_DEPTH || depth % 9 == 0 || node < 0 || node >= MAX_NODES) return false;
		return depth == treasureDepth(depth) && node == treasureNode(depth);
	}

	public static int treasureDepth(int depth) {
		int segmentEnd = ((depth + 8) / 9) * 9;
		segmentEnd = Math.min(MAX_DEPTH, Math.max(9, segmentEnd));
		int first = Math.max(2, segmentEnd - 7);
		int count = 0;
		for (int d = first; d < segmentEnd; d++) {
			if (d % 2 != 0) count++;
		}
		int picked = randomIndex(segmentEnd, 0, 41, Math.max(1, count));
		for (int d = first; d < segmentEnd; d++) {
			if (d % 2 == 0) continue;
			if (picked-- == 0) return d;
		}
		return first;
	}

	public static int treasureNode(int depth) {
		return randomIndex(treasureDepth(depth), 0, 42, MAX_NODES);
	}

	private static boolean hasNonCombatNodes() {
		for (int depth = 2; depth <= MAX_DEPTH; depth++) {
			if (depth % 9 == 0) continue;
			int count = Statistics.tendencyMapCounts[depth];
			for (int node = 0; node < count; node++) {
				int type = Statistics.tendencyMapTypes[index(depth, node)];
				if (type != NONE && type != COMBAT) return true;
			}
		}
		return false;
	}

	private static void enforceTypeQuotas() {
		for (int segmentEnd = 9; segmentEnd <= MAX_DEPTH; segmentEnd += 9) {
			enforceSegmentQuota(segmentEnd, REST, 1 + randomIndex(segmentEnd, REST, 51, 2));
			enforceSegmentQuota(segmentEnd, EVENT, 2 + randomIndex(segmentEnd, EVENT, 52, 3));
		}
	}

	private static boolean hasRequiredQuotas() {
		for (int segmentEnd = 9; segmentEnd <= MAX_DEPTH; segmentEnd += 9) {
			int rests = countTypeInSegment(segmentEnd, REST);
			int events = countTypeInSegment(segmentEnd, EVENT);
			if (rests < 1 || rests > 2 || events < 2 || events > 4) {
				return false;
			}
		}
		return true;
	}

	private static void enforceSegmentQuota(int segmentEnd, int type, int target) {
		while (countTypeInSegment(segmentEnd, type) > target) {
			int[] pos = pickTypeInSegment(segmentEnd, type, 61);
			if (pos == null) break;
			Statistics.tendencyMapTypes[index(pos[0], pos[1])] = COMBAT;
		}

		while (countTypeInSegment(segmentEnd, type) < target) {
			int[] pos = pickConvertibleInSegment(segmentEnd, type, 71);
			if (pos == null) break;
			Statistics.tendencyMapTypes[index(pos[0], pos[1])] = type;
		}
	}

	private static int countTypeInSegment(int segmentEnd, int type) {
		int count = 0;
		for (int depth = segmentStart(segmentEnd); depth < segmentEnd; depth++) {
			for (int node = 0; node < Statistics.tendencyMapCounts[depth]; node++) {
				if (Statistics.tendencyMapTypes[index(depth, node)] == type) count++;
			}
		}
		return count;
	}

	private static int[] pickTypeInSegment(int segmentEnd, int type, int salt) {
		int available = 0;
		for (int depth = segmentStart(segmentEnd); depth < segmentEnd; depth++) {
			for (int node = 0; node < Statistics.tendencyMapCounts[depth]; node++) {
				if (Statistics.tendencyMapTypes[index(depth, node)] == type) available++;
			}
		}
		if (available == 0) return null;

		int picked = randomIndex(segmentEnd, type, salt + available, available);
		for (int depth = segmentStart(segmentEnd); depth < segmentEnd; depth++) {
			for (int node = 0; node < Statistics.tendencyMapCounts[depth]; node++) {
				if (Statistics.tendencyMapTypes[index(depth, node)] != type) continue;
				if (picked-- == 0) return new int[]{depth, node};
			}
		}
		return null;
	}

	private static int[] pickConvertibleInSegment(int segmentEnd, int type, int salt) {
		int available = 0;
		for (int depth = segmentStart(segmentEnd); depth < segmentEnd; depth++) {
			for (int node = 0; node < Statistics.tendencyMapCounts[depth]; node++) {
				if (canConvert(depth, node, type)) available++;
			}
		}
		if (available == 0) return null;

		int picked = randomIndex(segmentEnd, type, salt + available, available);
		for (int depth = segmentStart(segmentEnd); depth < segmentEnd; depth++) {
			for (int node = 0; node < Statistics.tendencyMapCounts[depth]; node++) {
				if (!canConvert(depth, node, type)) continue;
				if (picked-- == 0) return new int[]{depth, node};
			}
		}
		return null;
	}

	private static boolean canConvert(int depth, int node, int targetType) {
		int type = Statistics.tendencyMapTypes[index(depth, node)];
		if (type == BOSS || type == SHOP || type == TREASURE || type == targetType) return false;
		if (targetType == REST && !restAllowed(depth)) return false;
		if (targetType == EVENT && !eventAllowed(depth)) return false;
		return type == COMBAT || type == ELITE;
	}

	private static int segmentStart(int segmentEnd) {
		return segmentEnd == 9 ? 2 : segmentEnd - 8;
	}

	private static boolean hasInbound(int depth, int node) {
		int count = rawCount(depth);
		for (int i = 0; i < count; i++) {
			if ((Statistics.tendencyMapLinks[index(depth, i)] & (1 << node)) != 0) return true;
		}
		return false;
	}

	private static int rawCount(int depth) {
		if (depth < 2 || depth > MAX_DEPTH || Statistics.tendencyMapCounts == null) return 0;
		return Statistics.tendencyMapCounts[depth];
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
