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

import com.watabou.utils.Random;

public enum DeckEnemy {

	GEB_GOD("테스트용 적", 50, 5, true),
	HORUS("호루스신", 44, 9, false),
	CREAM("크림", 60, 11, false),
	SETESH("세트신", 38, 0, false) {
		@Override
		public int nextIntent(DeckCombatEnemy enemy, int turn, int depth, int encounterIndex) {
			if (turn == 1) return DeckBuilderCombat.RESULT_AGE_DOWN;
			return turn % 2 == 0 ? 7 : 13;
		}
	},
	NDOUL("게브신", 55, 0, false) {
		@Override
		public int nextIntent(DeckCombatEnemy enemy, int turn, int depth, int encounterIndex) {
			return turn % 3 == 2 ? DeckBuilderCombat.RESULT_STRENGTH_7 : 4;
		}
	},
	THE_FOOL("더 풀", 42, 0, false) {
		@Override
		public int nextIntent(DeckCombatEnemy enemy, int turn, int depth, int encounterIndex) {
			if (turn % 3 == 1) return 12;
			if (turn % 3 == 2) return DeckBuilderCombat.RESULT_ATTACK_6_BLOCK_5;
			return DeckBuilderCombat.RESULT_STRENGTH_2;
		}
	},
	TOWER_OF_GREY("타워 오브 그레이", 21, 0, false) {
		@Override
		public int hpForDepth(int depth) {
			return 21 + Random.Int(5);
		}

		@Override
		public int nextIntent(DeckCombatEnemy enemy, int turn, int depth, int encounterIndex) {
			if (encounterIndex == 0) {
				if (turn == 1) return DeckBuilderCombat.RESULT_TOWER_NEEDLE;
				int cycle = (turn - 2) % 3;
				if (cycle == 0) return DeckBuilderCombat.RESULT_MASSACRE;
				if (cycle == 1) return 7;
				return DeckBuilderCombat.RESULT_TOWER_NEEDLE;
			} else {
				int cycle = (turn - 1) % 3;
				if (cycle == 0) return 7;
				if (cycle == 1) return DeckBuilderCombat.RESULT_TOWER_NEEDLE;
				return DeckBuilderCombat.RESULT_MASSACRE;
			}
		}
	},

	JUDGEMENT("저지먼트", 56, 0, false) {
		@Override
		public void initialize(DeckCombatEnemy enemy) {
			enemy.platedArmor = 8;
		}

		@Override
		public int nextIntent(DeckCombatEnemy enemy, int turn, int depth, int encounterIndex) {
			return turn % 2 == 1 ? DeckBuilderCombat.RESULT_PRESSURIZE : 10;
		}
	},
	LARGE_SLIME("러버즈", 64, 0, false) {
		@Override
		public int hpForDepth(int depth) {
			return 64 + Random.Int(7);
		}

		@Override
		public int nextIntent(DeckCombatEnemy enemy, int turn, int depth, int encounterIndex) {
			return Random.Int(100) < 30 ? DeckBuilderCombat.RESULT_FLAME_TACKLE_BIG : DeckBuilderCombat.RESULT_LICK_BIG;
		}
	},
	MEDIUM_SLIME("러버즈", 24, 0, false) {
		@Override
		public int nextIntent(DeckCombatEnemy enemy, int turn, int depth, int encounterIndex) {
			return Random.Int(100) < 30 ? DeckBuilderCombat.RESULT_FLAME_TACKLE_MEDIUM : DeckBuilderCombat.RESULT_LICK_MEDIUM;
		}
	},
	CLASH("클래시", 11, 0, false) {
		@Override
		public void initialize(DeckCombatEnemy enemy) {
			enemy.tricky = 1;
		}

		@Override
		public int hpForDepth(int depth) {
			return 11 + Random.Int(7);
		}

		@Override
		public int nextIntent(DeckCombatEnemy enemy, int turn, int depth, int encounterIndex) {
			if (turn == 1) {
				return (encounterIndex == 1) ? DeckBuilderCombat.RESULT_WINDUP_PUNCH : 3;
			}
			int roll = Random.Int(3);
			if (roll == 0) return 3;
			if (roll == 1) return DeckBuilderCombat.RESULT_WINDUP_PUNCH;
			return 10;
		}
	},
	KHNUM("크눔신", 56, 0, false) {
		@Override
		public void initialize(DeckCombatEnemy enemy) {
			enemy.venom = 3;
		}

		@Override
		public int nextIntent(DeckCombatEnemy enemy, int turn, int depth, int encounterIndex) {
			if (turn == 1) return 12;
			int intent;
			do {
				int roll = Random.Int(3);
				intent = roll == 0 ? 12 : roll == 1 ? DeckBuilderCombat.RESULT_LASH : DeckBuilderCombat.RESULT_TACKLE;
			} while (intent == enemy.lastIntent);
			return intent;
		}
	},
	RAMPAGING_BULL("날뛰는 소", 65, 0, false) {
		@Override
		public void initialize(DeckCombatEnemy enemy) {
			enemy.block = 13;
			enemy.artifact = 1;
		}

		@Override
		public int nextIntent(DeckCombatEnemy enemy, int turn, int depth, int encounterIndex) {
			switch ((turn - 1) % 5) {
				case 0:
					return DeckBuilderCombat.RESULT_CHARGE_UP;
				case 1:
				case 2:
					return DeckBuilderCombat.RESULT_REPEATER_BLAST;
				case 3:
					return DeckBuilderCombat.RESULT_EXPEL_BLAST;
				default:
					return DeckBuilderCombat.RESULT_SUBMERGE;
			}
		}
	};

	public static final int ENCOUNTER_SETESH = 0;
	public static final int ENCOUNTER_NDOUL = 1;
	public static final int ENCOUNTER_THE_FOOL = 2;
	public static final int ENCOUNTER_TOWER_OF_GREY = 3;

	public static final int ENCOUNTER_GEB_SETESH = 4;
	public static final int ENCOUNTER_JUDGEMENT = 5;
	public static final int ENCOUNTER_LARGE_SLIME = 6;
	public static final int ENCOUNTER_CLASHES = 7;
	public static final int ENCOUNTER_KHNUM = 8;
	public static final int ENCOUNTER_RAMPAGING_BULL = 9;

	public final String name;
	public final int baseHP;
	public final int baseIntent;
	private final boolean injectsSlimy;

	DeckEnemy(String name, int baseHP, int baseIntent, boolean injectsSlimy) {
		this.name = name;
		this.baseHP = baseHP;
		this.baseIntent = baseIntent;
		this.injectsSlimy = injectsSlimy;
	}

	public void initialize(DeckCombatEnemy enemy) {
	}

	public int hpForDepth(int depth) {
		if (this == SETESH || this == NDOUL || this == THE_FOOL) return baseHP;
		if (this == GEB_GOD || this == JUDGEMENT || this == MEDIUM_SLIME || this == KHNUM || this == RAMPAGING_BULL) return baseHP;
		return baseHP + Math.max(1, depth) * 3;
	}

	public int nextIntent(DeckCombatEnemy enemy, int turn, int depth, int encounterIndex) {
		if (injectsSlimy && turn % 2 == 0) {
			return DeckBuilderCombat.RESULT_SLIMY_INJECT;
		}
		return baseIntent + Math.max(1, depth) / 2 + Random.Int(4);
	}

	public static DeckEnemy forNode(int nodeType) {
		if (nodeType == DeckBuilderMap.ELITE) return HORUS;
		if (nodeType == DeckBuilderMap.BOSS) return CREAM;
		return GEB_GOD;
	}

	public static DeckEnemy[] encounterForNode(int nodeType, int depth, int previousEncounterId) {
		if (nodeType == DeckBuilderMap.BOSS) {
			return new DeckEnemy[]{CREAM};
		}
		if (nodeType == DeckBuilderMap.ELITE) {
			return new DeckEnemy[]{HORUS, GEB_GOD};
		}
		int encounter = rollAct1Encounter(previousEncounterId);
		return normalEncounter(encounter);
	}

	public static DeckEnemy[] openingEncounter(int encounterId) {
		switch (encounterId) {
			case ENCOUNTER_SETESH:
				return new DeckEnemy[]{SETESH};
			case ENCOUNTER_NDOUL:
				return new DeckEnemy[]{NDOUL};
			case ENCOUNTER_THE_FOOL:
				return new DeckEnemy[]{THE_FOOL};
			case ENCOUNTER_TOWER_OF_GREY:
			default:
				return new DeckEnemy[]{TOWER_OF_GREY, TOWER_OF_GREY};
		}
	}

	public static DeckEnemy[] normalEncounter(int encounterId) {
		switch (encounterId) {
			case ENCOUNTER_GEB_SETESH:
				return new DeckEnemy[]{NDOUL, SETESH};
			case ENCOUNTER_JUDGEMENT:
				return new DeckEnemy[]{JUDGEMENT};
			case ENCOUNTER_LARGE_SLIME:
				return new DeckEnemy[]{LARGE_SLIME};
			case ENCOUNTER_CLASHES:
				return new DeckEnemy[]{CLASH, CLASH, CLASH};
			case ENCOUNTER_KHNUM:
				return new DeckEnemy[]{KHNUM};
			case ENCOUNTER_RAMPAGING_BULL:
			default:
				return new DeckEnemy[]{RAMPAGING_BULL};
		}
	}

	public static int rollOpeningEncounter(int previousEncounterId) {
		int encounterId;
		do {
			encounterId = Random.Int(4);
		} while (encounterId == previousEncounterId);
		return encounterId;
	}

	public static int rollAct1Encounter(int previousEncounterId) {
		int encounterId;
		do {
			encounterId = ENCOUNTER_GEB_SETESH + Random.Int(6);
		} while (encounterId == previousEncounterId);
		return encounterId;
	}
}
