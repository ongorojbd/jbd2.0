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
import com.shatteredpixel.shatteredpixeldungeon.deckbuilder.DeckBuilderRun;

import javax.swing.plaf.basic.BasicRadioButtonMenuItemUI;

public enum DeckEnemy {

	TUTORIAL_DUMMY("로버트 E.O. 스피드왜건", 24, 0, false) {
		@Override
		public int nextIntent(DeckCombatEnemy enemy, int turn, int depth, int encounterIndex) {
			if (turn == 1) return 3;  // 1턴: 공격 없음 (ATTACK 튜토리얼)
			if (turn == 2) return 6;  // 2턴: 공격 (SKILL 방어 튜토리얼)
			return 4;                 // 3턴~: 약한 공격 (자유 플레이)
		}
	},

	GEB_GOD("로버트 E.O. 스피드왜건", 40, 5, true),
	HORUS("호루스신", 44, 9, false),
	CREAM("크림", 226, 0, false) {
		@Override
		public int hpForDepth(int depth) {
			return 226;
		}

		@Override
		public int nextIntent(DeckCombatEnemy enemy, int turn, int depth, int encounterIndex) {
			if (turn == 1) return DeckBuilderCombat.RESULT_CREAM_AMBUSH;
			if (turn <= 4) {
				switch (turn) {
					case 2: return DeckBuilderCombat.RESULT_CREAM_MIASMA;
					case 3: return DeckBuilderCombat.RESULT_CREAM_SPIN;
					default: return DeckBuilderCombat.RESULT_CREAM_RAGE;
				}
			}
			switch ((turn - 5) % 4) {
				case 0:
				case 1:  return DeckBuilderCombat.RESULT_CREAM_MIASMA;
				case 2:  return DeckBuilderCombat.RESULT_CREAM_SPIN;
				default: return DeckBuilderCombat.RESULT_CREAM_RAGE;
			}
		}
	},
	CIVIL_WAR("시빌 워", 211, 0, false) {
		@Override
		public int hpForDepth(int depth) {
			return 211;
		}

		@Override
		public int nextIntent(DeckCombatEnemy enemy, int turn, int depth, int encounterIndex) {
			switch ((turn - 1) % 5) {
				case 0:  return DeckBuilderCombat.RESULT_PRICE_CARDS;
				case 1:  return DeckBuilderCombat.RESULT_DE_GAS;
				case 2:  return DeckBuilderCombat.RESULT_GAZE;
				case 3:  return DeckBuilderCombat.RESULT_FADE;
				default: return DeckBuilderCombat.RESULT_SCREAM;
			}
		}
	},
	SICIGIN("시생인", 58, 0, false) {
		@Override
		public int hpForDepth(int depth) {
			return 58 + Random.Int(2);
		}

		@Override
		public int nextIntent(DeckCombatEnemy enemy, int turn, int depth, int encounterIndex) {
			if (encounterIndex == 0) {
				// 앞쪽: 힘의 춤 > 빠른 참격 > 부메랑 > 반복
				switch ((turn - 1) % 3) {
					case 0:  return DeckBuilderCombat.RESULT_POWER_DANCE;
					case 1:  return DeckBuilderCombat.RESULT_QUICK_SLASH;
					default: return DeckBuilderCombat.RESULT_BOOMERANG;
				}
			} else {
				// 뒤쪽: 빠른 참격 > 부메랑 > 힘의 춤 > 반복
				switch ((turn - 1) % 3) {
					case 0:  return DeckBuilderCombat.RESULT_QUICK_SLASH;
					case 1:  return DeckBuilderCombat.RESULT_BOOMERANG;
					default: return DeckBuilderCombat.RESULT_POWER_DANCE;
				}
			}
		}
	},
	NUKESAKU("누케사쿠", 190, 0, false) {
		@Override
		public int hpForDepth(int depth) {
			return 190;
		}

		@Override
		public int nextIntent(DeckCombatEnemy enemy, int turn, int depth, int encounterIndex) {
			switch ((turn - 1) % 4) {
				case 0:  return DeckBuilderCombat.RESULT_ORB_OF_FRAILTY;
				case 1:  return DeckBuilderCombat.RESULT_ORB_OF_WEAKNESS;
				case 2:  return DeckBuilderCombat.RESULT_SOUL_BEAM;
				default: return DeckBuilderCombat.RESULT_DARK_RITUAL;
			}
		}
	},
	SETESH("세트신", 38, 0, false) {
		@Override
		public int hpForDepth(int depth) {
			return 38 + Random.Int(3);
		}
		@Override
		public int nextIntent(DeckCombatEnemy enemy, int turn, int depth, int encounterIndex) {
			if (turn == 1) return DeckBuilderCombat.RESULT_AGE_DOWN;
			return turn % 2 == 0 ? 7 : 13;
		}
	},
	NDOUL("게브신", 55, 0, false) {
		@Override
		public int hpForDepth(int depth) {
			return 55 + Random.Int(3);
		}
		@Override
		public int nextIntent(DeckCombatEnemy enemy, int turn, int depth, int encounterIndex) {
			return turn % 3 == 2 ? DeckBuilderCombat.RESULT_STRENGTH_7 : 4;
		}
	},
	THE_FOOL("더 풀", 42, 0, false) {
		@Override
		public int hpForDepth(int depth) {
			return 42 + Random.Int(5);
		}

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

	JUDGEMENT("오아시스", 65, 0, false) {
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
			return 65 + Random.Int(5);
		}

		@Override
		public int nextIntent(DeckCombatEnemy enemy, int turn, int depth, int encounterIndex) {
			return slimeIntent(
					DeckBuilderCombat.RESULT_CORROSIVE_SPIT_BIG,
					DeckBuilderCombat.RESULT_TACKLE_BIG,
					DeckBuilderCombat.RESULT_LICK_BIG);
		}
	},
	MEDIUM_SLIME("러버즈", 28, 0, false) {
		@Override
		public int hpForDepth(int depth) {
			return 28 + Random.Int(5);
		}

		@Override
		public int nextIntent(DeckCombatEnemy enemy, int turn, int depth, int encounterIndex) {
			return slimeIntent(
					DeckBuilderCombat.RESULT_CORROSIVE_SPIT_MEDIUM,
					DeckBuilderCombat.RESULT_TACKLE_MEDIUM,
					DeckBuilderCombat.RESULT_LICK_MEDIUM);
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
			return nonRepeatingIntent(KHNUM_PATTERN, enemy.lastIntent);
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

	LAGAVULIN("화이트 앨범", 109, 0, false) {
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

	BYRDONIS("요요마", 81, 0, false) {
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

	HAUNTED_SHIP("다크 블루 문", 63, 0, false) {
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
	},

	CALCIFIED_FANATIC("하이 프리스티스", 38, 0, false) {
		@Override
		public int hpForDepth(int depth) {
			return 38 + Random.Int(4);
		}

		@Override
		public int nextIntent(DeckCombatEnemy enemy, int turn, int depth, int encounterIndex) {
			return turn == 1 ? DeckBuilderCombat.RESULT_INCANTATION : DeckBuilderCombat.RESULT_DARK_STRIKE;
		}
	},

	ROGUE_SEAWEED("블랙 사바스", 44, 0, false) {
		@Override
		public int hpForDepth(int depth) {
			return 44 + Random.Int(3);
		}

		@Override
		public int nextIntent(DeckCombatEnemy enemy, int turn, int depth, int encounterIndex) {
			switch ((turn - 1) % 3) {
				case 0: return DeckBuilderCombat.RESULT_SEA_KICK;
				case 1: return DeckBuilderCombat.RESULT_SPINNING_KICK;
				default: return DeckBuilderCombat.RESULT_BUBBLE_BURP;
			}
		}
	},

	STAGGERING_VINE("머라이어", 61, 0, false) {
		@Override
		public int hpForDepth(int depth) {
			return 61 + Random.Int(5);
		}

		@Override
		public int nextIntent(DeckCombatEnemy enemy, int turn, int depth, int encounterIndex) {
			switch ((turn - 1) % 3) {
				case 0: return DeckBuilderCombat.RESULT_VINE_SWIPE;
				case 1: return DeckBuilderCombat.RESULT_GRASPING_VINES;
				default: return DeckBuilderCombat.RESULT_CHOMP;
			}
		}
	},

	ANGLERFISH("호루스신", 72, 0, false) {
		@Override
		public int hpForDepth(int depth) {
			return 72 + Random.Int(5);
		}

		@Override
		public int nextIntent(DeckCombatEnemy enemy, int turn, int depth, int encounterIndex) {
			if (turn == 1) return DeckBuilderCombat.RESULT_CLAW;
			int[] options = enemy.splitUsed
					? new int[]{DeckBuilderCombat.RESULT_CLAW, DeckBuilderCombat.RESULT_RAMPAGE}
					: new int[]{DeckBuilderCombat.RESULT_CLAW, DeckBuilderCombat.RESULT_RAMPAGE, DeckBuilderCombat.RESULT_ROAR};
			int intent;
			do {
				intent = options[Random.Int(options.length)];
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
	public static final int ENCOUNTER_ANGLERFISH = 11;
	public static final int ENCOUNTER_STAGGERING_VINE = 12;
	public static final int ENCOUNTER_CALCIFIED_PAIR = 13;

	public final String name;
	public final int baseHP;
	public final int baseIntent;
	private final boolean injectsSlimy;
	private static final int[] KHNUM_PATTERN = new int[]{
			12,
			DeckBuilderCombat.RESULT_LASH,
			DeckBuilderCombat.RESULT_TACKLE
	};

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

	private static int slimeIntent(int corrosiveSpit, int tackle, int lick) {
		int roll = Random.Int(100);
		if (roll < 30) return corrosiveSpit;
		if (roll < 70) return tackle;
		return lick;
	}

	private static int nonRepeatingIntent(int[] pattern, int lastIntent) {
		int intent;
		do {
			intent = pattern[Random.Int(pattern.length)];
		} while (intent == lastIntent);
		return intent;
	}

	public static DeckEnemy forNode(int nodeType) {
		if (nodeType == DeckBuilderMap.ELITE) return BYRDONIS;
		if (nodeType == DeckBuilderMap.BOSS) {
			if (DeckBuilderRun.selectedBoss < 0) DeckBuilderRun.selectedBoss = Random.Int(3);
			return bossBySelection();
		}
		return GEB_GOD;
	}

	/** 현재 선택된 보스 반환 (0=크림, 1=시빌 워, 2=누케사쿠) */
	public static DeckEnemy bossBySelection() {
		switch (DeckBuilderRun.selectedBoss) {
			case 1:  return CIVIL_WAR;
			case 2:  return NUKESAKU;
			default: return CREAM;
		}
	}

	public static DeckEnemy[] encounterForNode(int nodeType, int depth, int previousEncounterId) {
		if (nodeType == DeckBuilderMap.BOSS) {
			if (DeckBuilderRun.selectedBoss < 0) DeckBuilderRun.selectedBoss = Random.Int(3);
			if (DeckBuilderRun.selectedBoss == 2) {
				return new DeckEnemy[]{SICIGIN, NUKESAKU, SICIGIN};
			}
			return new DeckEnemy[]{bossBySelection()};
		}
		if (nodeType == DeckBuilderMap.ELITE) {
			int last = DeckBuilderRun.lastEliteEncounter;
			int r;
			if (last < 0) {
				r = Random.Int(3);
			} else {
				// 직전 강적을 제외한 2종 중 랜덤 선택
				r = Random.Int(2);
				if (r >= last) r++;
			}
			DeckBuilderRun.lastEliteEncounter = r;
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
			case ENCOUNTER_ANGLERFISH:
				return new DeckEnemy[]{ANGLERFISH};
			case ENCOUNTER_STAGGERING_VINE:
				return new DeckEnemy[]{STAGGERING_VINE};
			case ENCOUNTER_CALCIFIED_PAIR:
				return new DeckEnemy[]{CALCIFIED_FANATIC, ROGUE_SEAWEED};
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
			encounterId = ENCOUNTER_GEB_SETESH + Random.Int(10);
		} while (encounterId == previousEncounterId);
		return encounterId;
	}
}
