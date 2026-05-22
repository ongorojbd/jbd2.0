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

import com.shatteredpixel.shatteredpixeldungeon.Challenges;
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

	JUDGEMENT("저지먼트", 65, 0, false) {
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
	},
	LARGE_SLIME("러버즈", 65, 0, false) {
		@Override
		public int hpForDepth(int depth) {
			return (Challenges.activeChallenges() >= 7 ? 68 : 65) + Random.Int(5);
		}

		@Override
		public int nextIntent(DeckCombatEnemy enemy, int turn, int depth, int encounterIndex) {
			int r = Random.Int(100);
			if (r < 30) return DeckBuilderCombat.RESULT_CORROSIVE_SPIT_BIG;
			if (r < 70) return DeckBuilderCombat.RESULT_TACKLE_BIG;
			return DeckBuilderCombat.RESULT_LICK_BIG;
		}
	},
	MEDIUM_SLIME("러버즈", 28, 0, false) {
		@Override
		public int hpForDepth(int depth) {
			return 28 + Random.Int(5);
		}

		@Override
		public int nextIntent(DeckCombatEnemy enemy, int turn, int depth, int encounterIndex) {
			int r = Random.Int(100);
			if (r < 30) return DeckBuilderCombat.RESULT_CORROSIVE_SPIT_MEDIUM;
			if (r < 70) return DeckBuilderCombat.RESULT_TACKLE_MEDIUM;
			return DeckBuilderCombat.RESULT_LICK_MEDIUM;
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
	RAMPAGING_BULL("날뛰는 소", 56, 0, false) {
		@Override
		public void initialize(DeckCombatEnemy enemy) {
			enemy.platedArmor = 8;
		}

		@Override
		public int nextIntent(DeckCombatEnemy enemy, int turn, int depth, int encounterIndex) {
			return turn % 2 == 1 ? DeckBuilderCombat.RESULT_PRESSURIZE : 10;
		}
	},

	LAGAVULIN("라가불린", 109, 0, false) {
		@Override
		public int hpForDepth(int depth) {
			return 109 + Random.Int(3);
		}

		@Override
		public int nextIntent(DeckCombatEnemy enemy, int turn, int depth, int encounterIndex) {
			if (!enemy.splitUsed) {
				if (turn > 3) {
					enemy.splitUsed = true;
				} else {
					return DeckBuilderCombat.RESULT_LAGAVULIN_SLEEP;
				}
			}
			int idx = enemy.venom % 3;
			enemy.venom++;
			if (idx == 2) return DeckBuilderCombat.RESULT_LAGAVULIN_SIPHON;
			return DeckBuilderCombat.RESULT_LAGAVULIN_ATTACK;
		}
	},

	BYRDONIS("섀도니스", 81, 0, false) {
		@Override
		public int hpForDepth(int depth) {
			return 81 + Random.Int(4);
		}

		@Override
		public int nextIntent(DeckCombatEnemy enemy, int turn, int depth, int encounterIndex) {
			return (turn % 2 == 1) ? DeckBuilderCombat.RESULT_BYRDONIS_BITE : DeckBuilderCombat.RESULT_PECK;
		}
	},

	RAT_JAGGED("래트(깔쭉이)", 48, 0, false) {
		@Override
		public int hpForDepth(int depth) {
			return 48 + Random.Int(5);
		}

		@Override
		public int nextIntent(DeckCombatEnemy enemy, int turn, int depth, int encounterIndex) {
			switch ((turn - 1) % 3) {
				case 0: return DeckBuilderCombat.RESULT_SPLITTING_BITE;
				case 1: return DeckBuilderCombat.RESULT_POISON_FANG;
				default: return DeckBuilderCombat.RESULT_BITE;
			}
		}
	},

	RAT_SMOOTH("래트(안깔쭉이)", 42, 0, false) {
		@Override
		public int hpForDepth(int depth) {
			return 42 + Random.Int(5);
		}

		@Override
		public int nextIntent(DeckCombatEnemy enemy, int turn, int depth, int encounterIndex) {
			if (turn == 1) return DeckBuilderCombat.RESULT_DIRTY_FUR;
			return (turn % 2 == 0) ? DeckBuilderCombat.RESULT_TAIL_WHIP : DeckBuilderCombat.RESULT_CORNER;
		}
	},

	HAUNTED_SHIP("유령선", 63, 0, false) {
		@Override
		public int hpForDepth(int depth) {
			return 63 + Random.Int(5);
		}

		@Override
		public int nextIntent(DeckCombatEnemy enemy, int turn, int depth, int encounterIndex) {
			if (turn == 1) return DeckBuilderCombat.RESULT_HAUNT;
			if (turn == 2) return DeckBuilderCombat.RESULT_RAMMING_SPEED;
			int[] options = {DeckBuilderCombat.RESULT_RAMMING_SPEED, DeckBuilderCombat.RESULT_SWIPE, DeckBuilderCombat.RESULT_STOMP};
			int intent;
			do {
				intent = options[Random.Int(3)];
			} while (intent == enemy.lastIntent);
			return intent;
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
	public static final int ENCOUNTER_HAUNTED_SHIP = 10;

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
		if (this == GEB_GOD || this == JUDGEMENT || this == KHNUM || this == RAMPAGING_BULL) return baseHP;
		return baseHP + Math.max(1, depth) * 3;
	}

	public int nextIntent(DeckCombatEnemy enemy, int turn, int depth, int encounterIndex) {
		if (injectsSlimy && turn % 2 == 0) {
			return DeckBuilderCombat.RESULT_SLIMY_INJECT;
		}
		return baseIntent + Math.max(1, depth) / 2 + Random.Int(4);
	}

	public static DeckEnemy forNode(int nodeType) {
		if (nodeType == DeckBuilderMap.ELITE) return BYRDONIS;
		if (nodeType == DeckBuilderMap.BOSS) return CREAM;
		return GEB_GOD;
	}

	public static DeckEnemy[] encounterForNode(int nodeType, int depth, int previousEncounterId) {
		if (nodeType == DeckBuilderMap.BOSS) {
			return new DeckEnemy[]{CREAM};
		}
		if (nodeType == DeckBuilderMap.ELITE) {
			int r = Random.Int(3);
			if (r == 0) return new DeckEnemy[]{BYRDONIS};
			if (r == 1) return new DeckEnemy[]{RAT_JAGGED, RAT_SMOOTH};
			return new DeckEnemy[]{LAGAVULIN};
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
			case ENCOUNTER_HAUNTED_SHIP:
				return new DeckEnemy[]{HAUNTED_SHIP};
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
			encounterId = ENCOUNTER_GEB_SETESH + Random.Int(7);
		} while (encounterId == previousEncounterId);
		return encounterId;
	}
}
