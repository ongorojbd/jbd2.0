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

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

import java.util.ArrayList;

public enum DeckCard {

	STRIKE(0, "행운의 검", DeckCardType.ATTACK, DeckCardRarity.COMMON, DeckCardTarget.SINGLE, 1, 100, 0, 0, 0, 0, 0, 0, false, ItemSpriteSheet.WORN_SHORTSWORD),
	GUARD(1, "무당벌레 브로치", DeckCardType.SKILL, DeckCardRarity.COMMON, DeckCardTarget.NONE, 1, 0, 5, 0, 0, 0, 0, 0, false, ItemSpriteSheet.ARMOR_CLOTH),
	BASH(2, "파문 커터", DeckCardType.ATTACK, DeckCardRarity.UNCOMMON, DeckCardTarget.SINGLE, 2, 8, 0, 0, 2, 0, 0, 0, false, ItemSpriteSheet.THROWING_STONE),
	VACCINE_SNAKE(3, "백신 뱀", DeckCardType.ATTACK, DeckCardRarity.COMMON, DeckCardTarget.SINGLE, 1, 9, 0, 1, 0, 0, 0, 0, true, ItemSpriteSheet.SHORTSWORD) {
		@Override
		public int damage(int code) {
			return upgradeLevel(code) > 0 ? 10 : 9;
		}

		@Override
		public int draw(int code) {
			return upgradeLevel(code) > 0 ? 2 : 1;
		}
	},
	STAFF(4, "지팡이", DeckCardType.ATTACK, DeckCardRarity.COMMON, DeckCardTarget.SINGLE, 1, 6, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.QUARTERSTAFF,
			new DeckCardEffects.StaffEffect()),
	RIPPLE_WALL(5, "개구리", DeckCardType.SKILL, DeckCardRarity.UNCOMMON, DeckCardTarget.NONE, 0, 0, 4, 1, 0, 0, 0, 0, true, ItemSpriteSheet.DEWDROP),
	SLIMY(6, "익사", DeckCardType.STATUS, DeckCardRarity.COMMON, DeckCardTarget.NONE, 1, 0, 0, 1, 0, 0, 1, keywords(DeckCardKeyword.EXHAUST), false, ItemSpriteSheet.MOB_HOLDER),
	IGNITE(7, "강화의 DISC", DeckCardType.POWER, DeckCardRarity.UNCOMMON, DeckCardTarget.NONE, 1, 0, 0, 0, 0, 2, 0, 0, true, ItemSpriteSheet.SCROLL_NAUDIZ),

	SHIV(8, "전갈탄", DeckCardType.ATTACK, DeckCardRarity.COMMON, DeckCardTarget.SINGLE, 0, 4, 0, 0, 0, 0, 0, keywords(DeckCardKeyword.EXHAUST), false, ItemSpriteSheet.SPIRIT_ARROW, HeroClass.HUNTRESS, 0) {
		@Override
		public int damage(int code) {
			return damage + upgradeLevel(code) * 2;
		}
	},
	SCORPION_THROW(9, "전갈 투척", DeckCardType.SKILL, DeckCardRarity.COMMON, DeckCardTarget.NONE, 0, 0, 0, 1, 0, 0, 0, 0, false, ItemSpriteSheet.SPIRIT_BOW, HeroClass.HUNTRESS, 1) {
		@Override
		public int draw(int code) {
			return draw + upgradeLevel(code);
		}
	},
	PHANTOM_BLADES(10, "감각 폭주", DeckCardType.POWER, DeckCardRarity.UNCOMMON, DeckCardTarget.NONE, 1, 0, 0, 0, 0, 0, 0, 0, true, Talent.FOLLOWUP_STRIKE, HeroClass.HUNTRESS, 0,
			new DeckCardEffects.PhantomBlades()),
	ACCURACY(11, "생명 추적", DeckCardType.POWER, DeckCardRarity.UNCOMMON, DeckCardTarget.NONE, 1, 0, 0, 0, 0, 0, 0, 0, true, Talent.HEIGHTENED_SENSES, HeroClass.HUNTRESS, 0,
			new DeckCardEffects.Accuracy()),
	KNIFE_TRAP(12, "무한한 가능성", DeckCardType.SKILL, DeckCardRarity.RARE, DeckCardTarget.SINGLE, 2, 0, 0, 0, 0, 0, 0, 0, true, Talent.SWIFT_SPIRIT, HeroClass.HUNTRESS, 0,
			new DeckCardEffects.KnifeTrap()),
	LEADING_STRIKE(13, "자세 교정", DeckCardType.ATTACK, DeckCardRarity.COMMON, DeckCardTarget.SINGLE, 1, 3, 0, 0, 0, 0, 0, 0, true, Talent.SHARED_UPGRADES, HeroClass.HUNTRESS, 2),
	CLOAK_AND_DAGGER(14, "투쟁심", DeckCardType.SKILL, DeckCardRarity.COMMON, DeckCardTarget.NONE, 1, 0, 6, 0, 0, 0, 0, 0, true, Talent.POINT_BLANK, HeroClass.HUNTRESS, 1) {
		@Override
		public int block(int code) {
			return block;
		}

		@Override
		public int shivs(int code) {
			return shivs + upgradeLevel(code);
		}
	},

	ROTATING_NAIL(15, "회전하는 손톱", DeckCardType.ATTACK, DeckCardRarity.COMMON, DeckCardTarget.RANDOM_ENEMY, 1, 3, 0, 1, 0, 0, 0, keywords(DeckCardKeyword.CAST_ON_DRAW, DeckCardKeyword.EXHAUST), false, Talent.J43, HeroClass.JOHNNY, 0),
	TUSK_EQUIPMENT_DISC(16, "터스크의 장비 DISC", DeckCardType.ATTACK, DeckCardRarity.COMMON, DeckCardTarget.SINGLE, 1, 4, 0, 0, 0, 0, 0, keywords(DeckCardKeyword.AIM), false, ItemSpriteSheet.ARTIFACT_TUSK1, HeroClass.JOHNNY, 0,
			new DeckCardEffects.ShuffleIntoDrawPile(ROTATING_NAIL, 5)) {
		@Override
		public int damage(int code) {
			return damage + upgradeLevel(code) * 4;
		}

		@Override
		public int maxUpgradeLevel() {
			return 3;
		}
	},
	TRACKING_BULLET_HOLE(17, "추적하는 탄흔", DeckCardType.ATTACK, DeckCardRarity.UNCOMMON, DeckCardTarget.ALL_ENEMIES, 2, 12, 0, 0, 0, 0, 0, 0, true, Talent.J22, HeroClass.JOHNNY, 0,
			new DeckCardEffects.ShuffleIntoDrawPile(ROTATING_NAIL, 3)),
	SPIN_TRAINING(18, "회전 훈련", DeckCardType.POWER, DeckCardRarity.UNCOMMON, DeckCardTarget.NONE, 1, 0, 0, 1, 0, 0, 0, keywords(DeckCardKeyword.AIM), true, Talent.J14, HeroClass.JOHNNY, 0,
			new DeckCardEffects.SpinningNailTraining(),
			new DeckCardEffects.AimShuffleIntoDrawPile(ROTATING_NAIL, 3)),
	PROUD_STARVER(19, "긍지있게 굶주린 자", DeckCardType.SKILL, DeckCardRarity.UNCOMMON, DeckCardTarget.NONE, 4, 0, 0, 5, 0, 0, 0, 0, true, Talent.J21, HeroClass.JOHNNY, 0,
			new DeckCardEffects.DrawPileCostReduction(ROTATING_NAIL)) {
		@Override
		public int draw(int code) {
			return upgradeLevel(code) > 0 ? 6 : draw;
		}
	},
	LESSON_FIVE(20, "Lesson 5는 이걸 위해..", DeckCardType.SKILL, DeckCardRarity.RARE, DeckCardTarget.RANDOM_ENEMY, 2, 0, 0, 0, 0, 0, 0, 0, true, Talent.J36, HeroClass.JOHNNY, 0,
			new DeckCardEffects.CopyAndPlayFromDrawPile(ROTATING_NAIL)) {
		@Override
		public int cost(int code) {
			return upgradeLevel(code) > 0 ? 1 : cost;
		}
	},

	MAGIC_MISSILE_WAND(21, "마탄의 마법 막대", DeckCardType.ATTACK, DeckCardRarity.COMMON, DeckCardTarget.RANDOM_ENEMY, 1, 2, 0, 0, 0, 0, 0, keywords(DeckCardKeyword.RETAIN), true, ItemSpriteSheet.WAND_MAGIC_MISSILE,
			new DeckCardEffects.TurnStrength(1)) {
		@Override
		public int damage(int code) {
			return 2;
		}
	},
	MAGE_STAFF(22, "마법사의 지팡이", DeckCardType.ATTACK, DeckCardRarity.COMMON, DeckCardTarget.SINGLE, 1, 4, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.MAGES_STAFF,
			new DeckCardEffects.MageStaff()),

	DRAMATIC_ENTRANCE(23, "극적인 입장", DeckCardType.ATTACK, DeckCardRarity.UNCOMMON, DeckCardTarget.ALL_ENEMIES, 0, 11, 0, 0, 0, 0, 0, keywords(DeckCardKeyword.VANGUARD, DeckCardKeyword.EXHAUST), true, ItemSpriteSheet.SCROLL_NAUDIZ) {
		@Override
		public int damage(int code) {
			return upgradeLevel(code) > 0 ? 15 : 11;
		}
	},

	PURE(24, "순수", DeckCardType.SKILL, DeckCardRarity.COMMON, DeckCardTarget.NONE, 0, 0, 0, 0, 0, 0, 0, keywords(DeckCardKeyword.RETAIN, DeckCardKeyword.EXHAUST), true, ItemSpriteSheet.DEWDROP,
			new DeckCardEffects.ExhaustFromHand(3)),

	HEADBUTT(25, "박치기", DeckCardType.ATTACK, DeckCardRarity.COMMON, DeckCardTarget.SINGLE, 1, 9, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.THROWING_STONE,
			new DeckCardEffects.RetrieveFromDiscard()) {
		@Override
		public int damage(int code) {
			return upgradeLevel(code) > 0 ? 12 : 9;
		}
	},

	POISON_DART(26, "독침", DeckCardType.STATUS, DeckCardRarity.COMMON, DeckCardTarget.NONE, 1, 0, 0, 0, 0, 0, 0, 0, false, ItemSpriteSheet.SPIRIT_ARROW),

	MASSACRE(27, "대학살", DeckCardType.ATTACK, DeckCardRarity.UNCOMMON, DeckCardTarget.SINGLE, 2, 20, 0, 0, 0, 0, 0, keywords(DeckCardKeyword.TRANSIENT), true, ItemSpriteSheet.GREATAXE) {
		@Override
		public int damage(int code) {
			return upgradeLevel(code) > 0 ? 28 : 20;
		}
	},

	SHOCKWAVE(28, "충격파", DeckCardType.SKILL, DeckCardRarity.UNCOMMON, DeckCardTarget.ALL_ENEMIES, 2, 0, 0, 0, 3, 0, 0, keywords(DeckCardKeyword.EXHAUST), true, ItemSpriteSheet.HOLSTER,
			new DeckCardEffects.AttackDown(3, 5)) {
		@Override
		public int vulnerable(int code) {
			return upgradeLevel(code) > 0 ? 5 : 3;
		}
	},

	BARNACLE(29, "따개비", DeckCardType.STATUS, DeckCardRarity.COMMON, DeckCardTarget.NONE, 1, 0, 0, 0, 0, 0, 0, keywords(DeckCardKeyword.TRANSIENT), false, ItemSpriteSheet.DEWDROP) {
		@Override
		public boolean unplayable(int code) { return true; }
	},

	FOUNDATION_BOX(30, "재단의 상자", DeckCardType.SKILL, DeckCardRarity.UNCOMMON, DeckCardTarget.NONE, 0, 0, 0, 0, 0, 0, 0, keywords(DeckCardKeyword.EXHAUST), true, ItemSpriteSheet.WONDROUS_RESIN,
			new DeckCardEffects.Discover(new DeckDiscover(DeckDiscover.Pool.ALL, true))) {
		@Override
		public boolean hasKeyword(int code, DeckCardKeyword keyword) {
			if (keyword == DeckCardKeyword.EXHAUST && upgradeLevel(code) > 0) return false;
			return super.hasKeyword(code, keyword);
		}
	},

	WEAKNESS_STAB(31, "약점 찌르기", DeckCardType.ATTACK, DeckCardRarity.COMMON, DeckCardTarget.SINGLE, 1, 8, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.THROWING_STONE, HeroClass.WARRIOR, 0,
			new DeckCardEffects.Damage(), new DeckCardEffects.WeaknessStabBonus()) {
		@Override public int damage(int code) { return upgradeLevel(code) > 0 ? 13 : 8; }
	},

	BARRAGE(32, "연격 난사", DeckCardType.ATTACK, DeckCardRarity.COMMON, DeckCardTarget.ALL_ENEMIES, 1, 10, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.HOLSTER, HeroClass.WARRIOR, 0,
			new DeckCardEffects.Damage(), new DeckCardEffects.BarrageBonus()) {
		@Override public int damage(int code) { return upgradeLevel(code) > 0 ? 12 : 10; }
	},

	FLOW_SLASH(33, "흐름 베기", DeckCardType.ATTACK, DeckCardRarity.COMMON, DeckCardTarget.SINGLE, 1, 10, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.SHORTSWORD, HeroClass.WARRIOR, 0,
			new DeckCardEffects.Damage(), new DeckCardEffects.FlowSlashBonus()),

	ACCEL_STAB(34, "가속 찌르기", DeckCardType.ATTACK, DeckCardRarity.COMMON, DeckCardTarget.SINGLE, 0, 4, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.WORN_SHORTSWORD, HeroClass.WARRIOR, 0,
			new DeckCardEffects.Damage(), new DeckCardEffects.AccelStabBonus()) {
		@Override public int damage(int code) { return upgradeLevel(code) > 0 ? 7 : 4; }
	},

	HYPERVENTILATE(35, "과호흡", DeckCardType.ATTACK, DeckCardRarity.UNCOMMON, DeckCardTarget.SINGLE, 2, 12, 0, 0, 0, 0, 0, keywords(DeckCardKeyword.EXHAUST), true, ItemSpriteSheet.GREATAXE, HeroClass.WARRIOR, 0,
			new DeckCardEffects.Damage(), new DeckCardEffects.HyperventilateBonus()) {
		@Override public int damage(int code) { return upgradeLevel(code) > 0 ? 16 : 12; }
	},

	THORN_STANCE(36, "가시 자세", DeckCardType.SKILL, DeckCardRarity.UNCOMMON, DeckCardTarget.NONE, 2, 0, 12, 0, 0, 0, 0, 0, true, ItemSpriteSheet.ARMOR_CLOTH, HeroClass.WARRIOR, 0,
			new DeckCardEffects.Block(), new DeckCardEffects.ThornStanceBonus()) {
		@Override public int block(int code) { return upgradeLevel(code) > 0 ? 16 : 12; }
	},

	WEAPON_RETRIEVAL(37, "무기 회수", DeckCardType.ATTACK, DeckCardRarity.UNCOMMON, DeckCardTarget.SINGLE, 4, 5, 0, 0, 0, 0, 0, keywords(DeckCardKeyword.EXHAUST), true, ItemSpriteSheet.SHORTSWORD, HeroClass.WARRIOR, 0,
			new DeckCardEffects.Damage(), new DeckCardEffects.WeaponRetrievalBonus()) {
		@Override public int cost(int code) { return upgradeLevel(code) > 0 ? 3 : 4; }
	},

	WEAPON_DISCOVER(38, "무기 발견", DeckCardType.ATTACK, DeckCardRarity.UNCOMMON, DeckCardTarget.SINGLE, 0, 4, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.THROWING_STONE, HeroClass.WARRIOR, 0,
			new DeckCardEffects.Damage(), new DeckCardEffects.WeaponDiscoverBonus()),

	ONSLAUGHT(39, "난무", DeckCardType.ATTACK, DeckCardRarity.RARE, DeckCardTarget.SINGLE, 3, 12, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.GREATAXE, HeroClass.WARRIOR, 0,
			new DeckCardEffects.Damage(), new DeckCardEffects.OnslaughtBonus()) {
		@Override public int damage(int code) { return upgradeLevel(code) > 0 ? 15 : 12; }
	},

	BODY_SLAM(40, "몸통 박치기", DeckCardType.ATTACK, DeckCardRarity.COMMON, DeckCardTarget.SINGLE, 1, 0, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.GREATAXE, HeroClass.WARRIOR, 0,
			new DeckCardEffects.BodySlamDamage()) {
		@Override public int cost(int code) { return upgradeLevel(code) > 0 ? 0 : 1; }
	},

	BARRICADE(41, "바리케이드", DeckCardType.POWER, DeckCardRarity.RARE, DeckCardTarget.NONE, 3, 0, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.HOLSTER, HeroClass.WARRIOR, 0,
			new DeckCardEffects.BarricadeEffect()) {
		@Override public int cost(int code) { return upgradeLevel(code) > 0 ? 2 : 3; }
	},

	ENTRENCH(42, "요지부동", DeckCardType.POWER, DeckCardRarity.RARE, DeckCardTarget.NONE, 2, 0, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.ARMOR_CLOTH, HeroClass.WARRIOR, 0,
			new DeckCardEffects.EntrenchEffect()) {
		@Override public int cost(int code) { return upgradeLevel(code) > 0 ? 1 : 2; }
	},

	ARMAMENTS(43, "전투장비", DeckCardType.SKILL, DeckCardRarity.COMMON, DeckCardTarget.NONE, 1, 0, 5, 0, 0, 0, 0, 0, true, ItemSpriteSheet.SHORTSWORD, HeroClass.WARRIOR, 0,
			new DeckCardEffects.Block(), new DeckCardEffects.ArmamentsBonus()) {
		@Override public int block(int code) { return 5; }
	},

	RULE_COMPLIANCE(44, "규칙 준수", DeckCardType.CURSE, DeckCardRarity.COMMON, DeckCardTarget.NONE, 0, 0, 0, 0, 0, 0, 0, 0, false, ItemSpriteSheet.SCROLL_ISAZ,
			new DeckCardEffects.TextOnly("한 턴에 카드를 최대 3장까지만 사용할 수 있습니다.")),

	STRUGGLE(45, "몸부림", DeckCardType.CURSE, DeckCardRarity.COMMON, DeckCardTarget.NONE, 0, 0, 0, 0, 0, 0, 0, keywords(DeckCardKeyword.VANGUARD), false, ItemSpriteSheet.ARTIFACT_CHAINS),

	DECAY(46, "부패", DeckCardType.CURSE, DeckCardRarity.COMMON, DeckCardTarget.NONE, 0, 0, 0, 0, 0, 0, 0, 0, false, ItemSpriteSheet.WONDROUS_RESIN,
			new DeckCardEffects.TextOnly("내 턴 종료 시 이 카드가 손에 있다면, 피해를 2 받습니다.")),

	DEBT(47, "빚", DeckCardType.CURSE, DeckCardRarity.COMMON, DeckCardTarget.NONE, 0, 0, 0, 0, 0, 0, 0, 0, false, ItemSpriteSheet.GOLD,
			new DeckCardEffects.TextOnly("내 턴 종료 시 이 카드가 손에 있다면, 골드를 10 잃습니다.")),

	WOUND(48, "상처", DeckCardType.CURSE, DeckCardRarity.COMMON, DeckCardTarget.NONE, 0, 0, 0, 0, 0, 0, 0, 0, false, ItemSpriteSheet.TORN_PAGE),

	CLUMSINESS(49, "서투름", DeckCardType.CURSE, DeckCardRarity.COMMON, DeckCardTarget.NONE, 0, 0, 0, 0, 0, 0, 0, keywords(DeckCardKeyword.TRANSIENT), false, ItemSpriteSheet.DART),

	SLEEP_DEPRIVATION(50, "수면 부족", DeckCardType.CURSE, DeckCardRarity.COMMON, DeckCardTarget.NONE, 0, 0, 0, 0, 0, 0, 0, keywords(DeckCardKeyword.RETAIN), false, ItemSpriteSheet.HONEYPOT),

	SHAME(51, "수치", DeckCardType.CURSE, DeckCardRarity.COMMON, DeckCardTarget.NONE, 0, 0, 0, 0, 0, 0, 0, 0, false, ItemSpriteSheet.SCROLL_RAIDO,
			new DeckCardEffects.TextOnly("내 턴 종료 시 이 카드가 손에 있다면, 방어력 저하를 1 얻습니다.")),

	SUSPICION(52, "의심", DeckCardType.CURSE, DeckCardRarity.COMMON, DeckCardTarget.NONE, 0, 0, 0, 0, 0, 0, 0, 0, false, ItemSpriteSheet.ROT_DART,
			new DeckCardEffects.TextOnly("내 턴 종료 시 이 카드가 손에 있다면, 공격력 저하를 1 얻습니다.")),

	GUILT(53, "죄책감", DeckCardType.CURSE, DeckCardRarity.COMMON, DeckCardTarget.NONE, 0, 0, 0, 0, 0, 0, 0, 0, false, ItemSpriteSheet.SCROLL_BERKANAN) {
		@Override public int maxUpgradeLevel() { return 5; }
		@Override public String title(int code) {
			int remaining = 5 - upgradeLevel(code);
			return "죄책감 (" + remaining + "전 후 제거)";
		}
	},

	REGRET(54, "후회", DeckCardType.CURSE, DeckCardRarity.COMMON, DeckCardTarget.NONE, 0, 0, 0, 0, 0, 0, 0, 0, false, ItemSpriteSheet.SCROLL_MANNAZ,
			new DeckCardEffects.TextOnly("내 턴 종료 시 이 카드가 손에 있다면, 손에 있는 카드 1장당 체력을 1 잃습니다.")),

	SPORE_INVASION(55, "포자 잠식", DeckCardType.CURSE, DeckCardRarity.COMMON, DeckCardTarget.NONE, 1, 0, 0, 0, 0, 0, 0, keywords(DeckCardKeyword.EXHAUST), false, ItemSpriteSheet.PETRIFIED_SEED),

	RUPTURE(56, "파열", DeckCardType.POWER, DeckCardRarity.UNCOMMON, DeckCardTarget.NONE, 1, 0, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.BLOOD_VIAL, HeroClass.WARRIOR, 0,
			new DeckCardEffects.RuptureEffect()),

	BLOODLETTING(57, "사혈", DeckCardType.SKILL, DeckCardRarity.COMMON, DeckCardTarget.NONE, 0, 0, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.POTION_CRIMSON, HeroClass.WARRIOR, 0,
			new DeckCardEffects.BloodlettingEffect()),

	FIRESEA(58, "불바다", DeckCardType.POWER, DeckCardRarity.UNCOMMON, DeckCardTarget.NONE, 1, 0, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.FIRE_BOMB, HeroClass.WARRIOR, 0,
			new DeckCardEffects.FireseaEffect()),

	BLOOD_WALL(59, "피의 벽", DeckCardType.SKILL, DeckCardRarity.COMMON, DeckCardTarget.NONE, 2, 0, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.ARMOR_PLATE, HeroClass.WARRIOR, 0,
			new DeckCardEffects.BloodWallEffect()),

	MALICE(60, "악의", DeckCardType.ATTACK, DeckCardRarity.UNCOMMON, DeckCardTarget.SINGLE, 0, 5, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.DAGGER, HeroClass.WARRIOR, 0,
			new DeckCardEffects.MaliceBonus()) {
		@Override public int damage(int code) { return 5; }
	},

	BLOODFLOW(61, "혈류", DeckCardType.ATTACK, DeckCardRarity.UNCOMMON, DeckCardTarget.SINGLE, 1, 15, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.SWORD, HeroClass.WARRIOR, 0,
			new DeckCardEffects.BloodflowEffect(), new DeckCardEffects.Damage()) {
		@Override public int damage(int code) { return upgradeLevel(code) > 0 ? 20 : 15; }
	},

	BRAND(62, "낙인", DeckCardType.SKILL, DeckCardRarity.RARE, DeckCardTarget.NONE, 0, 0, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.SCROLL_TIWAZ, HeroClass.WARRIOR, 0,
			new DeckCardEffects.BrandEffect()),

	INDOMITABLE(63, "불굴", DeckCardType.SKILL, DeckCardRarity.RARE, DeckCardTarget.NONE, 2, 0, 0, 0, 0, 0, 0, keywords(DeckCardKeyword.EXHAUST), true, ItemSpriteSheet.POTION_GOLDEN, HeroClass.WARRIOR, 0,
			new DeckCardEffects.IndomitableEffect()),

	REND(64, "갈가리 찢기", DeckCardType.ATTACK, DeckCardRarity.RARE, DeckCardTarget.SINGLE, 2, 5, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.GREATAXE, HeroClass.WARRIOR, 0,
			new DeckCardEffects.RendBonus()) {
		@Override public int damage(int code) { return upgradeLevel(code) > 0 ? 7 : 5; }
	},

	FOOTWORK(65, "발놀림", DeckCardType.POWER, DeckCardRarity.UNCOMMON, DeckCardTarget.NONE, 1, 0, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.ARMOR_CLOTH,
			new DeckCardEffects.Dexterity(2, 3)),

	SURGE(66, "쇄도", DeckCardType.POWER, DeckCardRarity.UNCOMMON, DeckCardTarget.NONE, 2, 0, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.SCROLL_GYFU,
			new DeckCardEffects.SurgeEffect()) {
		@Override
		public int cost(int code) { return upgradeLevel(code) > 0 ? 1 : 2; }
	},

	CALAMITY(67, "재난", DeckCardType.SKILL, DeckCardRarity.UNCOMMON, DeckCardTarget.NONE, 2, 0, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.WAND_LIGHTNING,
			new DeckCardEffects.PlayRandomFromDrawPile(2, 3)) {
		@Override
		public int cost(int code) { return upgradeLevel(code) > 0 ? 1 : 2; }
	};

	public final int id;
	public final String title;
	public final DeckCardType type;
	public final DeckCardRarity rarity;
	public final DeckCardTarget target;
	public final int cost;
	public final int damage;
	public final int block;
	public final int draw;
	public final int vulnerable;
	public final int strength;
	public final int handPenalty;
	public final int baseKeywords;
	public final boolean reward;
	public final int icon;
	public final HeroClass deckClass;
	public final int shivs;
	public final Talent talentIcon;
	private final DeckCardEffect[] specialEffects;

	DeckCard(int id, String title, DeckCardType type, DeckCardRarity rarity, DeckCardTarget target, int cost, int damage, int block, int draw, int vulnerable,
			 int strength, int handPenalty, int baseKeywords, boolean reward, int icon) {
		this(id, title, type, rarity, target, cost, damage, block, draw, vulnerable, strength, handPenalty, baseKeywords, reward, icon, null, 0, (Talent)null);
	}

	DeckCard(int id, String title, DeckCardType type, DeckCardRarity rarity, DeckCardTarget target, int cost, int damage, int block, int draw, int vulnerable,
			 int strength, int handPenalty, int baseKeywords, boolean reward, int icon, DeckCardEffect... specialEffects) {
		this(id, title, type, rarity, target, cost, damage, block, draw, vulnerable, strength, handPenalty, baseKeywords, reward, icon, null, 0, null, specialEffects);
	}

	DeckCard(int id, String title, DeckCardType type, DeckCardRarity rarity, DeckCardTarget target, int cost, int damage, int block, int draw, int vulnerable,
			 int strength, int handPenalty, int baseKeywords, boolean reward, Talent talentIcon) {
		this(id, title, type, rarity, target, cost, damage, block, draw, vulnerable, strength, handPenalty, baseKeywords, reward, 0, null, 0, talentIcon);
	}

	DeckCard(int id, String title, DeckCardType type, DeckCardRarity rarity, DeckCardTarget target, int cost, int damage, int block, int draw, int vulnerable,
			 int strength, int handPenalty, int baseKeywords, boolean reward, int icon, HeroClass deckClass, int shivs) {
		this(id, title, type, rarity, target, cost, damage, block, draw, vulnerable, strength, handPenalty, baseKeywords, reward, icon, deckClass, shivs, (Talent)null);
	}

	DeckCard(int id, String title, DeckCardType type, DeckCardRarity rarity, DeckCardTarget target, int cost, int damage, int block, int draw, int vulnerable,
			 int strength, int handPenalty, int baseKeywords, boolean reward, int icon, HeroClass deckClass, int shivs, DeckCardEffect... specialEffects) {
		this(id, title, type, rarity, target, cost, damage, block, draw, vulnerable, strength, handPenalty, baseKeywords, reward, icon, deckClass, shivs, null, specialEffects);
	}

	DeckCard(int id, String title, DeckCardType type, DeckCardRarity rarity, DeckCardTarget target, int cost, int damage, int block, int draw, int vulnerable,
			 int strength, int handPenalty, int baseKeywords, boolean reward, Talent talentIcon, HeroClass deckClass, int shivs) {
		this(id, title, type, rarity, target, cost, damage, block, draw, vulnerable, strength, handPenalty, baseKeywords, reward, 0, deckClass, shivs, talentIcon);
	}

	DeckCard(int id, String title, DeckCardType type, DeckCardRarity rarity, DeckCardTarget target, int cost, int damage, int block, int draw, int vulnerable,
			 int strength, int handPenalty, int baseKeywords, boolean reward, Talent talentIcon, HeroClass deckClass, int shivs, DeckCardEffect... specialEffects) {
		this(id, title, type, rarity, target, cost, damage, block, draw, vulnerable, strength, handPenalty, baseKeywords, reward, 0, deckClass, shivs, talentIcon, specialEffects);
	}

	DeckCard(int id, String title, DeckCardType type, DeckCardRarity rarity, DeckCardTarget target, int cost, int damage, int block, int draw, int vulnerable,
			 int strength, int handPenalty, int baseKeywords, boolean reward, int icon, HeroClass deckClass, int shivs, Talent talentIcon) {
		this(id, title, type, rarity, target, cost, damage, block, draw, vulnerable, strength, handPenalty, baseKeywords, reward, icon, deckClass, shivs, talentIcon, new DeckCardEffect[0]);
	}

	DeckCard(int id, String title, DeckCardType type, DeckCardRarity rarity, DeckCardTarget target, int cost, int damage, int block, int draw, int vulnerable,
			 int strength, int handPenalty, int baseKeywords, boolean reward, int icon, HeroClass deckClass, int shivs, Talent talentIcon, DeckCardEffect... specialEffects) {
		this.id = id;
		this.title = title;
		this.type = type;
		this.rarity = rarity;
		this.target = target;
		this.cost = cost;
		this.damage = damage;
		this.block = block;
		this.draw = draw;
		this.vulnerable = vulnerable;
		this.strength = strength;
		this.handPenalty = handPenalty;
		this.baseKeywords = baseKeywords;
		this.reward = reward;
		this.icon = icon;
		this.deckClass = deckClass;
		this.shivs = shivs;
		this.talentIcon = talentIcon;
		this.specialEffects = specialEffects == null ? new DeckCardEffect[0] : specialEffects;
	}

	public int code() {
		return id;
	}

	public String title(int code) {
		String name = title;
		if (this == STRIKE) {
			HeroClass cl = Dungeon.hero != null ? Dungeon.hero.heroClass : null;
			if (cl != null) {
				switch (cl) {
					case WARRIOR: name = "행운의 검"; break;
					case MAGE: name = "적석 타격"; break;
					case ROGUE: name = "스타 플라티나의 주먹"; break;
					case DUELIST: name = "크레이지 다이아몬드의 주먹"; break;
					case HUNTRESS: name = "골드 익스피리언스의 주먹"; break;
					case CLERIC: name = "스톤 프리의 주먹"; break;
					case JOHNNY: name = "손톱탄"; break;
				}
			}
		}
		return upgradeLevel(code) > 0 ? name + " +" + upgradeLevel(code) : name;
	}

	public int icon() {
		if (this == STRIKE) {
			HeroClass cl = Dungeon.hero != null ? Dungeon.hero.heroClass : null;
			if (cl != null) {
				switch (cl) {
					case WARRIOR: return ItemSpriteSheet.WORN_SHORTSWORD;
					case MAGE: return ItemSpriteSheet.TENS;
					case ROGUE: return ItemSpriteSheet.DAGGER;
					case DUELIST: return ItemSpriteSheet.RAPIER;
					case HUNTRESS: return ItemSpriteSheet.GLOVES;
					case CLERIC: return ItemSpriteSheet.CUDGEL;
					case JOHNNY: return ItemSpriteSheet.PICKAXE;
				}
			}
		}
		return icon;
	}

	public int cost(int code) {
		return cost;
	}

	public int damage(int code) {
		return damage > 0 ? damage + upgradeLevel(code) * 3 : 0;
	}

	public int block(int code) {
		return block > 0 ? block + upgradeLevel(code) * 3 : 0;
	}

	public int draw(int code) {
		return draw;
	}

	public int vulnerable(int code) {
		return vulnerable;
	}

	public int strength(int code) {
		return strength > 0 ? strength + upgradeLevel(code) : 0;
	}

	public int shivs(int code) {
		return shivs;
	}

	public int maxUpgradeLevel() {
		return 1;
	}

	public int effectiveCodeForPlay(int code, DeckBuilderCombat combat, int handIndex) {
		if (combat == null) return code;
		if (this == TUSK_EQUIPMENT_DISC && hasKeyword(code, DeckCardKeyword.AIM) && combat.isCenterHandIndex(handIndex)) {
			return upgradedForPlay(code);
		}
		if (hasKeyword(code, DeckCardKeyword.THROW) && combat.isEdgeHandIndex(handIndex)) {
			return upgradedForPlay(code);
		}
		return code;
	}

	public int upgradedForPlay(int code) {
		int upgrade = Math.min(maxUpgradeLevel(), upgradeLevel(code) + 1);
		return DeckCardCode.withUpgradeLevel(code, upgrade);
	}

	public boolean hasKeyword(int code, DeckCardKeyword keyword) {
		return (keywords(code) & keyword.bit) != 0;
	}

	public int keywords(int code) {
		return baseKeywords | keywordBits(code);
	}

	public DeckCardEffect[] effects(int code) {
		ArrayList<DeckCardEffect> effects = new ArrayList<>();
		boolean hasDamage = false, hasBlock = false, hasVulnerable = false,
				hasDraw = false, hasStrength = false, hasShivs = false;
		for (DeckCardEffect e : specialEffects) {
			if (e instanceof DeckCardEffects.Damage) hasDamage = true;
			else if (e instanceof DeckCardEffects.Block) hasBlock = true;
			else if (e instanceof DeckCardEffects.Vulnerable) hasVulnerable = true;
			else if (e instanceof DeckCardEffects.Draw) hasDraw = true;
			else if (e instanceof DeckCardEffects.Strength) hasStrength = true;
			else if (e instanceof DeckCardEffects.AddShivs) hasShivs = true;
		}
		if (damage(code) > 0 && this != STAFF && !hasDamage) effects.add(new DeckCardEffects.Damage());
		if (block(code) > 0 && !hasBlock) effects.add(new DeckCardEffects.Block());
		if (vulnerable(code) > 0 && !hasVulnerable) effects.add(new DeckCardEffects.Vulnerable());
		if (draw(code) > 0 && !hasDraw) effects.add(new DeckCardEffects.Draw());
		if (strength(code) > 0 && !hasStrength) effects.add(new DeckCardEffects.Strength());
		if (shivs(code) > 0 && !hasShivs) effects.add(new DeckCardEffects.AddShivs());
		for (DeckCardEffect effect : specialEffects) {
			effects.add(effect);
		}
		return effects.toArray(new DeckCardEffect[0]);
	}

	public boolean hasCategory(DeckCardCategory category) {
		return DeckCardCategoryTable.has(this, category);
	}

	public DeckCardCategory[] categories() {
		return DeckCardCategoryTable.categories(this);
	}

	public boolean unplayable(int code) {
		if (type == DeckCardType.CURSE && this != SPORE_INVASION) return true;
		return this == POISON_DART || DeckWandCards.isWand(code);
	}

	public int classFaceColor() {
		if (deckClass == HeroClass.JOHNNY) return 0xFF4A213F;
		if (deckClass == HeroClass.WARRIOR) return 0xFF2D1010;
		return 0xFF1A3D6B;
	}

	public int classPanelColor() {
		if (deckClass == HeroClass.JOHNNY) return 0xFF8D3F7F;
		if (deckClass == HeroClass.WARRIOR) return 0xFF5C2020;
		return 0xFF2D5F9D;
	}

	public int maxCharge() {
		return DeckWandCards.maxCharge(this);
	}

	public static int maxCharge(int code) {
		return DeckCardCode.maxCharge(code);
	}

	public static int currentCharge(int code) {
		return DeckCardCode.currentCharge(code);
	}

	public static int withCharge(int code, int charge) {
		return DeckCardCode.withCharge(code, charge);
	}

	public static int id(int code) {
		return DeckCardCode.id(code);
	}

	public static int upgradeLevel(int code) {
		return DeckCardCode.upgradeLevel(code);
	}

	public static int keywordBits(int code) {
		return DeckCardCode.keywordBits(code);
	}

	public static int withKeyword(int code, DeckCardKeyword keyword) {
		return DeckCardCode.withKeyword(code, keyword);
	}

	public static int withoutKeyword(int code, DeckCardKeyword keyword) {
		return DeckCardCode.withoutKeyword(code, keyword);
	}

	public static int upgrade(int code) {
		return DeckCardCode.upgrade(code);
	}

	public boolean conditionMet(int code, DeckBuilderCombat combat) {
		if (combat == null) return false;
		for (DeckCardEffect effect : effects(code)) {
			if (effect.conditionMet(combat, this, code)) return true;
		}
		return false;
	}

	public static DeckCard byCode(int code) {
		return byId(id(code));
	}

	public static boolean hasCategory(int code, DeckCardCategory category) {
		return byCode(code).hasCategory(category);
	}

	public static DeckCard[] cardsInCategory(DeckCardCategory category) {
		return DeckCardCategoryTable.cards(category);
	}

	public static DeckCard[] rewardCardsInCategory(DeckCardCategory category) {
		return DeckCardCategoryTable.rewardCards(category);
	}

	public static DeckCard byId(int id) {
		for (DeckCard card : values()) {
			if (card.id == id) return card;
		}
		return STRIKE;
	}

	public static DeckCard[] rewardPool() {
		return DeckCardPool.rewardPool();
	}

	public static DeckCard[] rewardPool(HeroClass heroClass, boolean classOnly, boolean neutralOnly) {
		return DeckCardPool.rewardPool(heroClass, classOnly, neutralOnly);
	}

	public static DeckCard rewardFallback(HeroClass heroClass) {
		return DeckCardPool.rewardFallback(heroClass);
	}

	private static int keywords(DeckCardKeyword... keywords) {
		int bits = 0;
		for (DeckCardKeyword keyword : keywords) {
			bits |= keyword.bit;
		}
		return bits;
	}
}
