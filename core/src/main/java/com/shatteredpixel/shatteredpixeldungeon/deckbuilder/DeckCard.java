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

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.GoldenMimic;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Lisalisa;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Zombied;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special.SentryRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.BlazingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.BurningTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ChillingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ConfusionTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.CreamTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.DisintegrationTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.DoobieTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.FlockTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.FrostTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GatewayTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GrippingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GuardianTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.PoisonDartTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ShockingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.StormTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.SummoningTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.TeleportationTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.WarpingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.WeakeningTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.WornDartTrap;
import com.shatteredpixel.shatteredpixeldungeon.sprites.Abomination2Sprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.AbominationSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.Act1Sprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.Act2Sprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.Act3Sprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.AtomSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.BanditSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.BatSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.BcomgSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.Bt1Sprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.Bt2Sprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CatSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CausticSlimeSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CivilSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CrabSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CrystalGuardianSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CrystalSpireSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CrystalWispSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DannySprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DvdolSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ElementalSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.EmporioSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.FishSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.FugoSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GSoldierSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GhostSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GnollExileSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GnollGeomancerSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GnollGuardSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GnollSapperSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GnollTricksterSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HeavywSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HermitCrabSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ImpSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.JotaroSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.LarvaSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.LisaSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MimicSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.NikuSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.Passione2Sprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.PassioneSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.PiranhaSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.RetonioSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.RohanSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.Scorpio2Sprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SheepSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ShopkeeperSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SkeletonSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SlimeSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SnakeSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.So2Sprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.Speedwagon2Sprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SquirrelSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.StowerSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SwarmSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.TormentedSpiritSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.TsujiAyaSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.VampireHorseSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.VampireSoldier2Sprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.VampireSoldier3Sprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.VampireSoldierSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.VitamincSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.VtestSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.WillaSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.WillcSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.WillsonSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.WiredSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.YasuSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.YukakoSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.Zombie3Sprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ZombieBrute2Sprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ZombieBrute3Sprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ZombieBruteSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ZombiedSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ZombietSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ZombiezSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;

import java.util.ArrayList;

public enum DeckCard {

	STRIKE(0, "행운의 검", DeckCardType.ATTACK, DeckCardRarity.COMMON, DeckCardTarget.SINGLE, 1, 6, 0, 0, 0, 0, 0, 0, false, ItemSpriteSheet.WORN_SHORTSWORD),
	GUARD(1, "무당벌레 브로치", DeckCardType.SKILL, DeckCardRarity.COMMON, DeckCardTarget.NONE, 1, 0, 5, 0, 0, 0, 0, 0, false, ItemSpriteSheet.ARMOR_CLOTH,
			new DeckCardEffects.GuardBonusEffect()),
	BASH(2, "파문 커터", DeckCardType.ATTACK, DeckCardRarity.COMMON, DeckCardTarget.SINGLE, 2, 8, 0, 0, 2, 0, 0, 0, false, ItemSpriteSheet.THROWING_STONE),
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
	SLIMY(6, "뇌조직 파괴", DeckCardType.STATUS, DeckCardRarity.COMMON, DeckCardTarget.NONE, 1, 0, 0, 0, 0, 0, 0, keywords(DeckCardKeyword.EXHAUST), false, Talent.RATSISTANCE),
	IGNITE(7, "로스트 비프 샌드위치", DeckCardType.POWER, DeckCardRarity.UNCOMMON, DeckCardTarget.NONE, 1, 0, 0, 0, 0, 2, 0, 0, true, ItemSpriteSheet.STEWED),

	SHIV(8, "전갈탄", DeckCardType.ATTACK, DeckCardRarity.COMMON, DeckCardTarget.SINGLE, 0, 4, 0, 0, 0, 0, 0, keywords(DeckCardKeyword.EXHAUST), false, ItemSpriteSheet.SPIRIT_ARROW, HeroClass.HUNTRESS, 0) {
		@Override
		public int damage(int code) {
			return damage + upgradeLevel(code) * 2;
		}
	},
	SPECIAL_SHIV(151, "전갈탄", DeckCardType.ATTACK, DeckCardRarity.COMMON, DeckCardTarget.SINGLE, 0, 5, 0, 0, 0, 0, 0, keywords(DeckCardKeyword.EXHAUST), false, ItemSpriteSheet.SPIRIT_ARROW, HeroClass.HUNTRESS, 0,
			new DeckCardEffects.SpecialShivDamage()) {
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
	LIFE_UNDERSTANDING(152, "생명 이해", DeckCardType.SKILL, DeckCardRarity.RARE, DeckCardTarget.NONE, 1, 0, 0, 0, 0, 0, 0, 0, true, Talent.SEER_SHOT, HeroClass.HUNTRESS, 0,
			new DeckCardEffects.AddSpecialShivs()),
	BLADE_FAN(153, "화살의 선택", DeckCardType.POWER, DeckCardRarity.RARE, DeckCardTarget.NONE, 2, 0, 0, 0, 0, 0, 0, 0, true, Talent.J55, HeroClass.HUNTRESS, 4,
			new DeckCardEffects.BladeFanEffect()) {
		@Override public int shivs(int code) { return upgradeLevel(code) > 0 ? 5 : 4; }
	},
	BLADE_DANCE(154, "생명 창조", DeckCardType.SKILL, DeckCardRarity.COMMON, DeckCardTarget.NONE, 1, 0, 0, 0, 0, 0, 0, keywords(DeckCardKeyword.EXHAUST), true, Talent.LIQUID_NATURE, HeroClass.HUNTRESS, 3) {
		@Override public int shivs(int code) { return upgradeLevel(code) > 0 ? 4 : 3; }
	},
	INFINITE_BLADES(155, "생명의 에너지", DeckCardType.POWER, DeckCardRarity.COMMON, DeckCardTarget.NONE, 1, 0, 0, 0, 0, 0, 0, 0, true, Talent.NATURES_AID, HeroClass.HUNTRESS, 0,
			new DeckCardEffects.InfiniteBladesEffect()) {
		@Override public boolean hasKeyword(int code, DeckCardKeyword keyword) {
			if (keyword == DeckCardKeyword.VANGUARD && upgradeLevel(code) > 0) return true;
			return super.hasKeyword(code, keyword);
		}
	},
	SECRET_PLAN(156, "전선 뱀", DeckCardType.SKILL, DeckCardRarity.COMMON, DeckCardTarget.NONE, 2, 0, 0, 0, 0, 0, 0, 0, true, Talent.NATURES_BOUNTY, HeroClass.HUNTRESS, 3) {
		@Override public int shivs(int code) { return upgradeLevel(code) > 0 ? 4 : 3; }
	},
	HIDDEN_DAGGER(157, "생명 순환", DeckCardType.SKILL, DeckCardRarity.UNCOMMON, DeckCardTarget.NONE, 0, 0, 0, 0, 0, 0, 0, 0, true, Talent.BARKSKIN, HeroClass.HUNTRESS, 0,
			new DeckCardEffects.HiddenDaggerEffect()),

	SNAKE_FORM(158, "귀도 미스타", DeckCardType.POWER, DeckCardRarity.UNCOMMON, DeckCardTarget.NONE, 2, 0, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.ELIXIR_DRAGON, HeroClass.HUNTRESS, 0, Passione2Sprite.Mi.class,
			new DeckCardEffects.SnakeFormEffect()),

	MENTAL_OVERFLOW(159, "완전생물의 피라냐", DeckCardType.POWER, DeckCardRarity.RARE, DeckCardTarget.NONE, 0, 0, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.SCROLL_RAIDO, null, 0, FishSprite.class,
			new DeckCardEffects.MentalOverflowEffect()),

	DOMAIN(160, "어웨이킹 III 리브스", DeckCardType.POWER, DeckCardRarity.RARE, DeckCardTarget.NONE, 3, 0, 0, 0, 0, 0, 0, keywords(DeckCardKeyword.TRANSIENT), true, ItemSpriteSheet.KINGA,
			new DeckCardEffects.DomainEffect()) {
		@Override public int cost(int code) { return upgradeLevel(code) > 0 ? 2 : 3; }
	},

	PREPARED(161, "정화풀의 씨앗", DeckCardType.SKILL, DeckCardRarity.COMMON, DeckCardTarget.NONE, 0, 0, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.SEED_MAGEROYAL,
			new DeckCardEffects.DrawThenDiscardEffect(1, 2, 1, 2)),

	BACKFLIP(162, "고양이", DeckCardType.SKILL, DeckCardRarity.COMMON, DeckCardTarget.NONE, 1, 0, 5, 2, 0, 0, 0, 0, true, ItemSpriteSheet.ELIXIR_FEATHER, null, 0, CatSprite.class) {
		@Override public int block(int code) { return upgradeLevel(code) > 0 ? 8 : 5; }
	},

	CALCULATED_GAMBLE(163, "어웨이킹 III 리브스의 함정", DeckCardType.SKILL, DeckCardRarity.UNCOMMON, DeckCardTarget.NONE, 0, 0, 0, 0, 0, 0, 0, keywords(DeckCardKeyword.EXHAUST), true, GatewayTrap.class,
			new DeckCardEffects.CalculatedGambleEffect()) {
		@Override public boolean hasKeyword(int code, DeckCardKeyword keyword) {
			if (keyword == DeckCardKeyword.RETAIN && upgradeLevel(code) > 0) return true;
			return super.hasKeyword(code, keyword);
		}
	},

	ESCAPE_PLAN(164, "으깨진 알", DeckCardType.SKILL, DeckCardRarity.UNCOMMON, DeckCardTarget.NONE, 0, 0, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.BLAND_CHUNKS,
			new DeckCardEffects.EscapePlanEffect(3, 5)),

	ACROBATICS(165, "에니그마의 함정", DeckCardType.SKILL, DeckCardRarity.UNCOMMON, DeckCardTarget.NONE, 1, 0, 0, 0, 0, 0, 0, 0, true, SummoningTrap.class,
			new DeckCardEffects.DrawThenDiscardEffect(3, 4, 1, 1)),

	ADRENALINE(166, "시간풀의 씨앗", DeckCardType.SKILL, DeckCardRarity.RARE, DeckCardTarget.NONE, 0, 0, 0, 0, 0, 0, 0, keywords(DeckCardKeyword.EXHAUST), true, ItemSpriteSheet.SEED_SWIFTTHISTLE,
			new DeckCardEffects.AdrenalineEffect()),

	TOOLS_OF_THE_TRADE(167, "스무스 오퍼레이터즈의 장비 DISC", DeckCardType.POWER, DeckCardRarity.RARE, DeckCardTarget.NONE, 1, 0, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.ARTIFACT_KEY,
			new DeckCardEffects.ToolsOfTheTradeEffect()) {
		@Override public int cost(int code) { return upgradeLevel(code) > 0 ? 0 : 1; }
	},

	SWEEPING_BEAM(168, "수상한 아이스크림", DeckCardType.ATTACK, DeckCardRarity.COMMON, DeckCardTarget.ALL_ENEMIES, 1, 6, 0, 1, 0, 0, 0, 0, true, ItemSpriteSheet.HAND_AXE) {
		@Override public int damage(int code) { return upgradeLevel(code) > 0 ? 9 : 6; }
	},

	REBOOT(169, "너구리풀의 씨앗", DeckCardType.SKILL, DeckCardRarity.RARE, DeckCardTarget.NONE, 0, 0, 0, 0, 0, 0, 0, keywords(DeckCardKeyword.EXHAUST), true, ItemSpriteSheet.SEED_FADELEAF,
			new DeckCardEffects.RestartEffect()),

	MACHINE_LEARNING(170, "킹 너싱", DeckCardType.POWER, DeckCardRarity.RARE, DeckCardTarget.NONE, 1, 0, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.KINGN,
			new DeckCardEffects.MachineLearningEffect()) {
		@Override public boolean hasKeyword(int code, DeckCardKeyword keyword) {
			if (keyword == DeckCardKeyword.VANGUARD && upgradeLevel(code) > 0) return true;
			return super.hasKeyword(code, keyword);
		}
	},

	PHOTON_SLASH(171, "작살", DeckCardType.ATTACK, DeckCardRarity.COMMON, DeckCardTarget.SINGLE, 1, 10, 0, 1, 0, 0, 0, 0, true, ItemSpriteSheet.DIRK,
			new DeckCardEffects.HandToDrawPileTopEffect()) {
		@Override public int damage(int code) { return upgradeLevel(code) > 0 ? 13 : 10; }
		@Override public int draw(int code) { return upgradeLevel(code) > 0 ? 2 : 1; }
	},

	FAINT_LIGHT(172, "태너 색스의 함정", DeckCardType.SKILL, DeckCardRarity.UNCOMMON, DeckCardTarget.NONE, 1, 0, 0, 3, 0, 0, 0, 0, true, WarpingTrap.class,
			new DeckCardEffects.HandToDrawPileTopEffect()) {
		@Override public int draw(int code) { return upgradeLevel(code) > 0 ? 4 : 3; }
	},

	PROPHECY(173, "와이어드", DeckCardType.SKILL, DeckCardRarity.UNCOMMON, DeckCardTarget.NONE, 2, 0, 0, 6, 0, 0, 0, 0, true, ItemSpriteSheet.SCROLL_GYFU, null, 0, CrystalWispSprite.Blue.class) {
		@Override public int draw(int code) { return upgradeLevel(code) > 0 ? 9 : 6; }
	},

	PALE_BLUE_DOT(174, "툼 오브 더 붐 · 3", DeckCardType.POWER, DeckCardRarity.UNCOMMON, DeckCardTarget.NONE, 1, 0, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.SCROLL_GYFU, null, 0, GnollGuardSprite.class,
			new DeckCardEffects.PaleBlueDotEffect()),

	DICTATORSHIP(175, "벽의 눈", DeckCardType.POWER, DeckCardRarity.RARE, DeckCardTarget.NONE, 1, 0, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.WALL,
			new DeckCardEffects.DictatorshipEffect()) {
		@Override public boolean hasKeyword(int code, DeckCardKeyword keyword) {
			if (keyword == DeckCardKeyword.VANGUARD && upgradeLevel(code) > 0) return true;
			return super.hasKeyword(code, keyword);
		}
	},

	DEFLECT(176, "흡혈귀 하급 병사", DeckCardType.SKILL, DeckCardRarity.COMMON, DeckCardTarget.NONE, 1, 0, 8, 1, 0, 0, 0, 0, true, ItemSpriteSheet.SCROLL_GYFU, null, 0, VampireSoldierSprite.class) {
		@Override public int block(int code) { return upgradeLevel(code) > 0 ? 11 : 8; }
	},

	EXTORTION(177, "흡혈귀 광전사", DeckCardType.ATTACK, DeckCardRarity.UNCOMMON, DeckCardTarget.SINGLE, 1, 6, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.SCROLL_GYFU, null, 0, VampireSoldier2Sprite.class,
			new DeckCardEffects.ExtortionEffect()) {
		@Override public int damage(int code) { return upgradeLevel(code) > 0 ? 9 : 6; }
	},

	BATTLE_HYPNOSIS(178, "바스테트 여신의 함정", DeckCardType.SKILL, DeckCardRarity.UNCOMMON, DeckCardTarget.NONE, 0, 0, 0, 0, 0, 0, 0, 0, true, ShockingTrap.class,
			new DeckCardEffects.BattleHypnosisEffect()),

	BATTLE_DRUMS(179, "롤링 스톤즈", DeckCardType.SKILL, DeckCardRarity.UNCOMMON, DeckCardTarget.NONE, 1, 0, 0, 2, 0, 0, 0, 0, true, ItemSpriteSheet.SCROLL_GYFU, null, 0, SheepSprite.class,
			new DeckCardEffects.BattleDrumsEffect()),

	OFFERING(180, "너트 킹 콜", DeckCardType.SKILL, DeckCardRarity.RARE, DeckCardTarget.NONE, 0, 0, 0, 0, 0, 0, 0, keywords(DeckCardKeyword.EXHAUST), true, ItemSpriteSheet.KINGC,
			new DeckCardEffects.OfferingEffect()),

	DEFIANCE(181, "증오하는 육편", DeckCardType.SKILL, DeckCardRarity.COMMON, DeckCardTarget.SINGLE, 1, 0, 6, 0, 0, 0, 0, keywords(DeckCardKeyword.TRANSIENT), true, ItemSpriteSheet.SCROLL_GYFU, null,0, NikuSprite.class,
			new DeckCardEffects.AttackDown(1, 1)) {
		@Override public int block(int code) { return upgradeLevel(code) > 0 ? 9 : 6; }
	},

	DELAY(182, "토니오 트루사르디", DeckCardType.SKILL, DeckCardRarity.UNCOMMON, DeckCardTarget.NONE, 2, 0, 11, 0, 0, 0, 0, 0, true, ItemSpriteSheet.SCROLL_GYFU, null, 0, RetonioSprite.class,
			new DeckCardEffects.NextTurnEnergyEffect(1, 2)) {
		@Override public int block(int code) { return upgradeLevel(code) > 0 ? 13 : 11; }
	},

	DEATH_DANCE(183, "신 로카카카", DeckCardType.POWER, DeckCardRarity.UNCOMMON, DeckCardTarget.NONE, 1, 0, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.RO2,
			new DeckCardEffects.DeathDanceEffect()),

	IMMORTALITY(184, "회복풀의 씨앗", DeckCardType.SKILL, DeckCardRarity.RARE, DeckCardTarget.NONE, 0, 0, 7, 0, 0, 0, 0, 0, true, ItemSpriteSheet.SEED_SUNGRASS,
			new DeckCardEffects.ShuffleCopyToDiscardEffect()) {
		@Override public int block(int code) { return upgradeLevel(code) > 0 ? 9 : 7; }
	},

	SURVIVOR(185, "비타민 C", DeckCardType.SKILL, DeckCardRarity.RARE, DeckCardTarget.SINGLE, 0, 0, 0, 0, 0, 0, 0, keywords(DeckCardKeyword.EXHAUST), true, ItemSpriteSheet.SCROLL_GYFU, null, 0, VitamincSprite.class,
			new DeckCardEffects.SurvivorRemakeEffect()),

	DASH(186, "바람의 프로텍터", DeckCardType.ATTACK, DeckCardRarity.UNCOMMON, DeckCardTarget.SINGLE, 2, 10, 10, 0, 0, 0, 0, 0, true, ItemSpriteSheet.ROUND_SHIELD) {
		@Override public int damage(int code) { return upgradeLevel(code) > 0 ? 13 : 10; }
		@Override public int block(int code) { return upgradeLevel(code) > 0 ? 13 : 10; }
	},

	BLUR(187, "에코즈 ACT.1", DeckCardType.SKILL, DeckCardRarity.UNCOMMON, DeckCardTarget.NONE, 1, 0, 5, 0, 0, 0, 0, 0, true, ItemSpriteSheet.SCROLL_GYFU, null, 0, Act1Sprite.class,
			new DeckCardEffects.BlurEffect()) {
		@Override public int block(int code) { return upgradeLevel(code) > 0 ? 8 : 5; }
	},

	LEG_SWEEP(188, "에코즈 ACT.2", DeckCardType.SKILL, DeckCardRarity.UNCOMMON, DeckCardTarget.SINGLE, 2, 0, 11, 0, 0, 0, 0, 0, true, ItemSpriteSheet.SCROLL_GYFU, null, 0, Act2Sprite.class,
			new DeckCardEffects.AttackDown(2, 3)) {
		@Override public int block(int code) { return upgradeLevel(code) > 0 ? 14 : 11; }
	},

	SHADOW_STEALTH(189, "슈퍼 플라이", DeckCardType.SKILL, DeckCardRarity.RARE, DeckCardTarget.NONE, 1, 0, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.SCROLL_GYFU, null, 0, StowerSprite.class,
			new DeckCardEffects.ShadowStealthEffect()) {
		@Override public int cost(int code) { return upgradeLevel(code) > 0 ? 0 : 1; }
	},

	AFTERIMAGE(190, "파시오네 뱃지", DeckCardType.POWER, DeckCardRarity.RARE, DeckCardTarget.NONE, 2, 0, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.ARMOR_HUNTRESS, HeroClass.HUNTRESS, 0,
			new DeckCardEffects.AfterimageEffect()) {
		@Override public boolean hasKeyword(int code, DeckCardKeyword keyword) {
			if (keyword == DeckCardKeyword.VANGUARD && upgradeLevel(code) > 0) return true;
			return super.hasKeyword(code, keyword);
		}
	},

	RECHARGE_BATTERY(191, "굶주린 시생인", DeckCardType.SKILL, DeckCardRarity.COMMON, DeckCardTarget.NONE, 1, 0, 7, 0, 0, 0, 0, 0, true, ItemSpriteSheet.SCROLL_GYFU, null, 0, ZombieBruteSprite.class,
			new DeckCardEffects.NextTurnEnergyEffect(1, 1)) {
		@Override public int block(int code) { return upgradeLevel(code) > 0 ? 10 : 7; }
	},

	HOLOGRAM(192, "언더 월드의 함정", DeckCardType.SKILL, DeckCardRarity.COMMON, DeckCardTarget.NONE, 1, 0, 3, 0, 0, 0, 0, keywords(DeckCardKeyword.EXHAUST), true, GuardianTrap.class,
			new DeckCardEffects.HologramEffect()) {
		@Override public int block(int code) { return upgradeLevel(code) > 0 ? 5 : 3; }
		@Override public boolean hasKeyword(int code, DeckCardKeyword keyword) {
			if (keyword == DeckCardKeyword.EXHAUST && upgradeLevel(code) > 0) return false;
			return super.hasKeyword(code, keyword);
		}
	},

	BOOT_SEQUENCE(193, "갑옷풀의 씨앗", DeckCardType.SKILL, DeckCardRarity.UNCOMMON, DeckCardTarget.NONE, 0, 0, 10, 0, 0, 0, 0, keywords(DeckCardKeyword.VANGUARD, DeckCardKeyword.EXHAUST), true, ItemSpriteSheet.SEED_EARTHROOT) {
		@Override public int block(int code) { return upgradeLevel(code) > 0 ? 13 : 10; }
	},

	GENETIC_ALGORITHM(194, "달팽이", DeckCardType.SKILL, DeckCardRarity.RARE, DeckCardTarget.NONE, 1, 0, 1, 0, 0, 0, 0, keywords(DeckCardKeyword.EXHAUST), true, ItemSpriteSheet.SCROLL_GYFU, null, 0, HeavywSprite.class,
			new DeckCardEffects.GeneticAlgorithmEffect()) {
		@Override public int block(int code) { return (upgradeLevel(code) > 0 ? 1 : 1) + DeckCardCode.auxValue(code); }
	},

	COSMIC_INDIFFERENCE(195, "점핑 잭 플래시의 함정", DeckCardType.SKILL, DeckCardRarity.COMMON, DeckCardTarget.NONE, 1, 0, 6, 0, 0, 0, 0, 0, true, ConfusionTrap.class,
			new DeckCardEffects.DiscardToDrawTopEffect()) {
		@Override public int block(int code) { return upgradeLevel(code) > 0 ? 9 : 6; }
	},

	LIGHT_STREAM(196, "오아시스", DeckCardType.SKILL, DeckCardRarity.COMMON, DeckCardTarget.NONE, 2, 0, 11, 0, 0, 0, 0, 0, true, ItemSpriteSheet.SCROLL_GYFU, null, 0, CausticSlimeSprite.class,
			new DeckCardEffects.NextTurnBlockEffect(5, 7)) {
		@Override public int block(int code) { return upgradeLevel(code) > 0 ? 13 : 11; }
	},

	PARTICLE_WALL(197, "롤링 스톤즈의 함정", DeckCardType.SKILL, DeckCardRarity.UNCOMMON, DeckCardTarget.NONE, -1, 0, 0, 0, 0, 0, 0, 0, true, FlockTrap.class,
			new DeckCardEffects.ParticleWallEffect()) {
		@Override public int block(int code) { return upgradeLevel(code) > 0 ? 9 : 6; }
	},

	AUTHORITY_EXERCISE(198, "스기모토 레이미", DeckCardType.SKILL, DeckCardRarity.UNCOMMON, DeckCardTarget.NONE, 1, 0, 7, 0, 0, 0, 0, 0, true, ItemSpriteSheet.SCROLL_GYFU, null, 0, ShopkeeperSprite.class,
			new DeckCardEffects.AuthorityEffect()) {
		@Override public int block(int code) { return upgradeLevel(code) > 0 ? 8 : 7; }
	},

	I_AM_INVINCIBLE(199, "잡지 넣은 가쿠란", DeckCardType.SKILL, DeckCardRarity.RARE, DeckCardTarget.NONE, 1, 0, 10, 0, 0, 0, 0, 0, true, ItemSpriteSheet.CLOAK_SCRAP,
			new DeckCardEffects.IAmInvincibleEffect()) {
		@Override public int block(int code) { return upgradeLevel(code) > 0 ? 13 : 10; }
	},

	RAGE(200, "빙결풀의 씨앗", DeckCardType.SKILL, DeckCardRarity.UNCOMMON, DeckCardTarget.NONE, 0, 0, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.SEED_ICECAP,
			new DeckCardEffects.RageBlockEffect()),

	TAUNT(201, "본 디스 웨이의 함정", DeckCardType.SKILL, DeckCardRarity.UNCOMMON, DeckCardTarget.SINGLE, 1, 0, 7, 0, 1, 0, 0, 0, true, FrostTrap.class) {
		@Override public int block(int code) { return upgradeLevel(code) > 0 ? 8 : 7; }
		@Override public int vulnerable(int code) { return upgradeLevel(code) > 0 ? 2 : 1; }
	},

	INVINCIBLE(202, "금빛 무당벌레 브로치", DeckCardType.SKILL, DeckCardRarity.RARE, DeckCardTarget.NONE, 2, 0, 30, 0, 0, 0, 0, keywords(DeckCardKeyword.EXHAUST), true, ItemSpriteSheet.ARMOR_SCALE) {
		@Override public int block(int code) { return upgradeLevel(code) > 0 ? 40 : 30; }
	},

	ABSOLUTE_POWER(203, "킹 크림슨의 함정", DeckCardType.POWER, DeckCardRarity.RARE, DeckCardTarget.NONE, 2, 0, 0, 0, 0, 0, 0, 0, true, PoisonDartTrap.class,
			new DeckCardEffects.AbsolutePowerEffect()),

	ENERGY_DRAIN(204, "독일 군인", DeckCardType.ATTACK, DeckCardRarity.COMMON, DeckCardTarget.SINGLE, 1, 10, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.SCROLL_GYFU, null, 0, GSoldierSprite.class,
			new DeckCardEffects.UpgradeRandomDiscardEffect()) {
		@Override public int damage(int code) { return upgradeLevel(code) > 0 ? 12 : 10; }
	},

	GRAVE_EXPLOSION(205, "투기장 시생인", DeckCardType.ATTACK, DeckCardRarity.COMMON, DeckCardTarget.SINGLE, 1, 4, 0, 0, 0, 0, 0, keywords(DeckCardKeyword.EXHAUST), true, ItemSpriteSheet.SCROLL_GYFU, null, 0, Zombie3Sprite.class,
			new DeckCardEffects.HologramEffect()) {
		@Override public int damage(int code) { return upgradeLevel(code) > 0 ? 6 : 4; }
		@Override public boolean hasKeyword(int code, DeckCardKeyword keyword) {
			if (keyword == DeckCardKeyword.EXHAUST && upgradeLevel(code) > 0) return false;
			return super.hasKeyword(code, keyword);
		}
	},

	DECAY_ATTACK(206, "죽음의 낫", DeckCardType.ATTACK, DeckCardRarity.UNCOMMON, DeckCardTarget.SINGLE, 1, 10, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.SCIMITAR,
			new DeckCardEffects.DecayEffect()) {
		@Override public int damage(int code) { return upgradeLevel(code) > 0 ? 12 : 10; }
	},

	DEATH_MARCH(207, "미스타의 권총", DeckCardType.ATTACK, DeckCardRarity.UNCOMMON, DeckCardTarget.SINGLE, 1, 8, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.WHIP,
			new DeckCardEffects.DeathMarchEffect()) {
		@Override public int damage(int code) { return (upgradeLevel(code) > 0 ? 9 : 8) + (upgradeLevel(code) > 0 ? 6 : 4) * (DeckBuilderRun.currentCombat == null ? 0 : DeckBuilderRun.currentCombat.cardsDrawnThisTurn); }
	},

	MISERY(208, "핑크 다크의 소년", DeckCardType.ATTACK, DeckCardRarity.RARE, DeckCardTarget.SINGLE, 0, 7, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.PINK,
			new DeckCardEffects.MiseryEffect()) {
		@Override public int damage(int code) { return upgradeLevel(code) > 0 ? 9 : 7; }
		@Override public boolean hasKeyword(int code, DeckCardKeyword keyword) {
			if (keyword == DeckCardKeyword.RETAIN && upgradeLevel(code) > 0) return true;
			return super.hasKeyword(code, keyword);
		}
	},

	EXTERMINATE(209, "샷건", DeckCardType.ATTACK, DeckCardRarity.RARE, DeckCardTarget.SINGLE, -1, 11, 0, 0, 0, 0, 0, keywords(DeckCardKeyword.RETAIN), true, ItemSpriteSheet.SHOTGUN,
			new DeckCardEffects.XMultiHitEffect(11, 14)) {
		@Override public int damage(int code) { return upgradeLevel(code) > 0 ? 14 : 11; }
	},

	NEUTRALIZE(210, "실명풀의 씨앗", DeckCardType.ATTACK, DeckCardRarity.COMMON, DeckCardTarget.SINGLE, 0, 3, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.SEED_BLINDWEED,
			new DeckCardEffects.AttackDown(1, 2)) {
		@Override public int damage(int code) { return upgradeLevel(code) > 0 ? 4 : 3; }
	},

	DAGGER_SPRAY(211, "머라이어", DeckCardType.ATTACK, DeckCardRarity.COMMON, DeckCardTarget.ALL_ENEMIES, 1, 4, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.SCROLL_GYFU, null, 0, HermitCrabSprite.class,
			new DeckCardEffects.DoubleAllEnemiesEffect()) {
		@Override public int damage(int code) { return upgradeLevel(code) > 0 ? 6 : 4; }
	},

	SUCKER_PUNCH(212, "푸치신부의 십자가", DeckCardType.ATTACK, DeckCardRarity.COMMON, DeckCardTarget.SINGLE, 1, 8, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.SWORD,
			new DeckCardEffects.AttackDown(1, 2),
			new DeckCardEffects.TextOnly("현재 체력이 소수면 비용이 0이 됩니다.")) {
		@Override public int damage(int code) { return upgradeLevel(code) > 0 ? 10 : 8; }
		@Override public int cost(int code) { return isPrime(DeckBuilderRun.playerHP) ? 0 : 1; }
		@Override public boolean conditionMet(int code, DeckBuilderCombat combat) {
			return combat != null && isPrime(DeckBuilderRun.playerHP);
		}
	},

	SKEWER(213, "렉킹 볼", DeckCardType.ATTACK, DeckCardRarity.UNCOMMON, DeckCardTarget.SINGLE, -1, 8, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.THROWING_CLUB,
			new DeckCardEffects.XMultiHitEffect(8, 11)) {
		@Override public int damage(int code) { return upgradeLevel(code) > 0 ? 11 : 8; }
	},

	BETRAYAL(214, "잭 더 리퍼", DeckCardType.ATTACK, DeckCardRarity.UNCOMMON, DeckCardTarget.SINGLE, 0, 11, 0, 0, 0, 0, 0, keywords(DeckCardKeyword.VANGUARD, DeckCardKeyword.EXHAUST), true, ItemSpriteSheet.SCROLL_GYFU, null, 0, ZombiezSprite.class) {
		@Override public int damage(int code) { return upgradeLevel(code) > 0 ? 15 : 11; }
	},

	PRECISE_CUT(215, "완전생물의 다람쥐", DeckCardType.ATTACK, DeckCardRarity.UNCOMMON, DeckCardTarget.SINGLE, 0, 13, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.KUNAI,
			new DeckCardEffects.PreciseCutEffect()) {
		@Override public int damage(int code) {
			int base = upgradeLevel(code) > 0 ? 16 : 13;
			return Math.max(0, base - Math.max(0, (DeckBuilderRun.currentCombat == null ? 1 : DeckBuilderRun.currentCombat.hand.size()) - 1) * 2);
		}
	},

	FINISHER(216, "툼 오브 더 붐 · 2", DeckCardType.ATTACK, DeckCardRarity.UNCOMMON, DeckCardTarget.SINGLE, 1, 6, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.SCROLL_GYFU, null, 0, GnollSapperSprite.class,
			new DeckCardEffects.FinisherEffect()) {
		@Override public int damage(int code) { return upgradeLevel(code) > 0 ? 8 : 6; }
	},

	STRANGLE(217, "끝이 없는 끝", DeckCardType.ATTACK, DeckCardRarity.UNCOMMON, DeckCardTarget.SINGLE, 1, 6, 0, 0, 0, 0, 0, 0, true, Talent.WILD_MOMENTUM, HeroClass.HUNTRESS, 0,
			new DeckCardEffects.StrangleEffect()) {
		@Override public int damage(int code) { return upgradeLevel(code) > 0 ? 10 : 8; }
	},

	FLECHETTES(218, "두비 와!의 함정", DeckCardType.ATTACK, DeckCardRarity.UNCOMMON, DeckCardTarget.SINGLE, 1, 5, 0, 0, 0, 0, 0, 0, true, DoobieTrap.class,
			new DeckCardEffects.FlechettesEffect()) {
		@Override public int damage(int code) { return upgradeLevel(code) > 0 ? 7 : 5; }
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

	MAGIC_MISSILE_WAND(21, "엠퍼러의 사격 DISC", DeckCardType.SKILL, DeckCardRarity.COMMON, DeckCardTarget.RANDOM_ENEMY, 1, 2, 0, 0, 0, 0, 0, keywords(DeckCardKeyword.RETAIN), true, ItemSpriteSheet.WAND_MAGIC_MISSILE,
			new DeckCardEffects.MagicMissileWandEffect()) {
		@Override
		public int damage(int code) {
			return 2;
		}
	},
	MAGE_STAFF(22, "에이자의 적석", DeckCardType.ATTACK, DeckCardRarity.COMMON, DeckCardTarget.SINGLE, 1, 4, 0, 0, 0, 0, 0, 0, false, ItemSpriteSheet.MAGES_STAFF,
			new DeckCardEffects.MageStaff()),

	DRAMATIC_ENTRANCE(23, "날뛰는 소", DeckCardType.ATTACK, DeckCardRarity.UNCOMMON, DeckCardTarget.ALL_ENEMIES, 0, 11, 0, 0, 0, 0, 0, keywords(DeckCardKeyword.VANGUARD, DeckCardKeyword.EXHAUST), true, ItemSpriteSheet.SCROLL_NAUDIZ, null, 0, GnollExileSprite.class) {
		@Override
		public int damage(int code) {
			return upgradeLevel(code) > 0 ? 15 : 11;
		}
	},

	PURE(24, "더 핸드의 함정", DeckCardType.SKILL, DeckCardRarity.COMMON, DeckCardTarget.NONE, 0, 0, 0, 0, 0, 0, 0, keywords(DeckCardKeyword.RETAIN, DeckCardKeyword.EXHAUST), true, DisintegrationTrap.class,
			new DeckCardEffects.ExhaustFromHand(3)),

	HEADBUTT(25, "낙서 함정", DeckCardType.ATTACK, DeckCardRarity.COMMON, DeckCardTarget.SINGLE, 1, 9, 0, 0, 0, 0, 0, 0, true, CreamTrap.class,
			new DeckCardEffects.RetrieveFromDiscard()) {
		@Override
		public int damage(int code) {
			return upgradeLevel(code) > 0 ? 12 : 9;
		}
	},

	POISON_DART(26, "독침", DeckCardType.STATUS, DeckCardRarity.COMMON, DeckCardTarget.NONE, 1, 0, 0, 0, 0, 0, 0, 0, false, ItemSpriteSheet.SPIRIT_ARROW, null, 0, GnollTricksterSprite.class),

	MASSACRE(27, "아톰 하트 파더", DeckCardType.ATTACK, DeckCardRarity.UNCOMMON, DeckCardTarget.SINGLE, 2, 20, 0, 0, 0, 0, 0, keywords(DeckCardKeyword.TRANSIENT), true, ItemSpriteSheet.GREATAXE, null, 0, AtomSprite.class) {
		@Override
		public int damage(int code) {
			return upgradeLevel(code) > 0 ? 28 : 20;
		}
	},

	SHOCKWAVE(28, "자외선 조사장치", DeckCardType.SKILL, DeckCardRarity.UNCOMMON, DeckCardTarget.ALL_ENEMIES, 2, 0, 0, 0, 3, 0, 0, keywords(DeckCardKeyword.EXHAUST), true, ItemSpriteSheet.UV,
			new DeckCardEffects.ShockwaveEffect(3, 5)) {
		@Override
		public int vulnerable(int code) {
			return upgradeLevel(code) > 0 ? 5 : 3;
		}
	},

	BARNACLE(29, "따개비", DeckCardType.STATUS, DeckCardRarity.COMMON, DeckCardTarget.NONE, 1, 0, 0, 0, 0, 0, 0, keywords(DeckCardKeyword.TRANSIENT), false, ItemSpriteSheet.DEWDROP, null, 0, SlimeSprite.class) {
		@Override
		public boolean unplayable(int code) { return true; }
	},

	FOUNDATION_BOX(30, "SPW 재단의 보급 상자", DeckCardType.SKILL, DeckCardRarity.UNCOMMON, DeckCardTarget.NONE, 0, 0, 0, 0, 0, 0, 0, keywords(DeckCardKeyword.EXHAUST), true, ItemSpriteSheet.SUPPLY_RATION,
			new DeckCardEffects.FoundationBoxEffect(new DeckDiscover(DeckDiscover.Pool.ALL, true))) {
		@Override
		public boolean hasKeyword(int code, DeckCardKeyword keyword) {
			if (keyword == DeckCardKeyword.EXHAUST && upgradeLevel(code) > 0) return false;
			return super.hasKeyword(code, keyword);
		}
	},

	WEAKNESS_STAB(31, "파문 전도", DeckCardType.ATTACK, DeckCardRarity.COMMON, DeckCardTarget.SINGLE, 1, 8, 0, 0, 0, 0, 0, 0, true, Talent.ENRAGED_CATALYST, HeroClass.WARRIOR, 0,
			new DeckCardEffects.Damage(), new DeckCardEffects.WeaknessStabBonus()) {
		@Override public int damage(int code) { return upgradeLevel(code) > 0 ? 13 : 8; }
	},

	BARRAGE(32, "불붙은 장갑", DeckCardType.ATTACK, DeckCardRarity.COMMON, DeckCardTarget.ALL_ENEMIES, 1, 10, 0, 0, 0, 0, 0, 0, true, Talent.ENHANCED_COMBO, HeroClass.WARRIOR, 0,
			new DeckCardEffects.Damage(), new DeckCardEffects.BarrageBonus()) {
		@Override public int damage(int code) { return upgradeLevel(code) > 0 ? 12 : 10; }
	},

	FLOW_SLASH(33, "파문의 회복력", DeckCardType.ATTACK, DeckCardRarity.COMMON, DeckCardTarget.SINGLE, 1, 10, 0, 0, 0, 0, 0, 0, true, Talent.LETHAL_DEFENSE, HeroClass.WARRIOR, 0,
			new DeckCardEffects.Damage(), new DeckCardEffects.FlowSlashBonus()),

	ACCEL_STAB(34, "줌 펀치", DeckCardType.ATTACK, DeckCardRarity.COMMON, DeckCardTarget.SINGLE, 0, 4, 0, 0, 0, 0, 0, 0, true, Talent.SHOCK_FORCE, HeroClass.WARRIOR, 0,
			new DeckCardEffects.Damage(), new DeckCardEffects.AccelStabBonus()) {
		@Override public int damage(int code) { return upgradeLevel(code) > 0 ? 7 : 4; }
	},

	HYPERVENTILATE(35, "심선맥질주", DeckCardType.ATTACK, DeckCardRarity.UNCOMMON, DeckCardTarget.SINGLE, 2, 12, 0, 0, 0, 0, 0, keywords(DeckCardKeyword.EXHAUST), true, Talent.RUNIC_TRANSFERENCE, HeroClass.WARRIOR, 0,
			new DeckCardEffects.Damage(), new DeckCardEffects.HyperventilateBonus()) {
		@Override public int damage(int code) { return upgradeLevel(code) > 0 ? 16 : 12; }
	},

	THORN_STANCE(36, "흔들림 없는 용기", DeckCardType.SKILL, DeckCardRarity.UNCOMMON, DeckCardTarget.NONE, 2, 0, 12, 0, 0, 0, 0, 0, true, Talent.HOLD_FAST, HeroClass.WARRIOR, 0,
			new DeckCardEffects.Block(), new DeckCardEffects.ThornStanceBonus()) {
		@Override public int block(int code) { return upgradeLevel(code) > 0 ? 16 : 12; }
	},

	WEAPON_RETRIEVAL(37, "넘치는 힘", DeckCardType.ATTACK, DeckCardRarity.UNCOMMON, DeckCardTarget.SINGLE, 4, 5, 0, 0, 0, 0, 0, keywords(DeckCardKeyword.EXHAUST), true, Talent.IRON_STOMACH, HeroClass.WARRIOR, 0,
			new DeckCardEffects.Damage(), new DeckCardEffects.WeaponRetrievalBonus()) {
		@Override public int cost(int code) { return upgradeLevel(code) > 0 ? 3 : 4; }
	},

	WEAPON_DISCOVER(38, "스피드왜건의 도움", DeckCardType.ATTACK, DeckCardRarity.UNCOMMON, DeckCardTarget.SINGLE, 0, 4, 0, 0, 0, 0, 0, 0, true, Talent.VETERANS_INTUITION, HeroClass.WARRIOR, 0,
			new DeckCardEffects.Damage(), new DeckCardEffects.WeaponDiscoverBonus()),

	ONSLAUGHT(39, "행운과 용기의 검", DeckCardType.ATTACK, DeckCardRarity.RARE, DeckCardTarget.SINGLE, 1, 7, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.LSWORD, HeroClass.WARRIOR, 0,
			new DeckCardEffects.Damage(), new DeckCardEffects.OnslaughtBonus()) {
		@Override public int damage(int code) { return upgradeLevel(code) > 0 ? 10 : 7; }
	},

	BODY_SLAM(40, "목숨을 건 반격", DeckCardType.ATTACK, DeckCardRarity.COMMON, DeckCardTarget.SINGLE, 1, 0, 0, 0, 0, 0, 0, 0, true, Talent.IMPROVISED_PROJECTILES, HeroClass.WARRIOR, 0,
			new DeckCardEffects.BodySlamDamage()) {
		@Override public int cost(int code) { return upgradeLevel(code) > 0 ? 0 : 1; }
	},

	BARRICADE(41, "최후의 에너지", DeckCardType.POWER, DeckCardRarity.RARE, DeckCardTarget.NONE, 3, 0, 0, 0, 0, 0, 0, 0, true, Talent.DOUBLE_JUMP, HeroClass.WARRIOR, 0,
			new DeckCardEffects.BarricadeEffect()) {
		@Override public int cost(int code) { return upgradeLevel(code) > 0 ? 2 : 3; }
	},

	ENTRENCH(42, "파문의 흐름", DeckCardType.POWER, DeckCardRarity.RARE, DeckCardTarget.NONE, 2, 0, 0, 0, 0, 0, 0, 0, true, Talent.JONATHAN_NEW1, HeroClass.WARRIOR, 0,
			new DeckCardEffects.EntrenchEffect()) {
		@Override public int cost(int code) { return upgradeLevel(code) > 0 ? 1 : 2; }
	},

	ARMAMENTS(43, "대니", DeckCardType.SKILL, DeckCardRarity.COMMON, DeckCardTarget.NONE, 1, 0, 5, 0, 0, 0, 0, 0, true, ItemSpriteSheet.SHORTSWORD, HeroClass.WARRIOR, 0, DannySprite.class,
			new DeckCardEffects.Block(), new DeckCardEffects.ArmamentsBonus()) {
		@Override public int block(int code) { return 5; }
	},

	RULE_COMPLIANCE(44, "혼잡한 저주", DeckCardType.CURSE, DeckCardRarity.COMMON, DeckCardTarget.NONE, 0, 0, 0, 0, 0, 0, 0, 0, false, Talent.CUR,
			new DeckCardEffects.TextOnly("한 턴에 카드를 최대 3장까지만 사용할 수 있습니다.")),

	STRUGGLE(45, "변위의 저주", DeckCardType.CURSE, DeckCardRarity.COMMON, DeckCardTarget.NONE, 0, 0, 0, 0, 0, 0, 0, keywords(DeckCardKeyword.VANGUARD), false, Talent.CUR),

	DECAY(46, "폭발의 저주", DeckCardType.CURSE, DeckCardRarity.COMMON, DeckCardTarget.NONE, 0, 0, 0, 0, 0, 0, 0, 0, false, Talent.CUR,
			new DeckCardEffects.TextOnly("내 턴 종료 시 이 카드가 손에 있다면, 피해를 2 받습니다.")),

	DEBT(47, "거추장스러운 저주", DeckCardType.CURSE, DeckCardRarity.COMMON, DeckCardTarget.NONE, 0, 0, 0, 0, 0, 0, 0, 0, false, Talent.CUR,
			new DeckCardEffects.TextOnly("내 턴 종료 시 이 카드가 손에 있다면, 골드를 10 잃습니다.")),

	WOUND(48, "짜증나는 저주", DeckCardType.CURSE, DeckCardRarity.COMMON, DeckCardTarget.NONE, 0, 0, 0, 0, 0, 0, 0, 0, false, Talent.CUR,
			new DeckCardEffects.TextOnly("사용불가.")),

	CLUMSINESS(49, "희생의 저주", DeckCardType.CURSE, DeckCardRarity.COMMON, DeckCardTarget.NONE, 0, 0, 0, 0, 0, 0, 0, keywords(DeckCardKeyword.TRANSIENT), false, Talent.CUR),

	SLEEP_DEPRIVATION(50, "친화적인 저주", DeckCardType.CURSE, DeckCardRarity.COMMON, DeckCardTarget.NONE, 0, 0, 0, 0, 0, 0, 0, keywords(DeckCardKeyword.RETAIN), false, Talent.CUR),

	SHAME(51, "악취의 저주", DeckCardType.CURSE, DeckCardRarity.COMMON, DeckCardTarget.NONE, 0, 0, 0, 0, 0, 0, 0, 0, false, Talent.CUR,
			new DeckCardEffects.TextOnly("내 턴 종료 시 이 카드가 손에 있다면, 방어력 저하를 1 얻습니다.")),

	SUSPICION(52, "눈부신 저주", DeckCardType.CURSE, DeckCardRarity.COMMON, DeckCardTarget.NONE, 0, 0, 0, 0, 0, 0, 0, 0, false, Talent.CUR,
			new DeckCardEffects.TextOnly("내 턴 종료 시 이 카드가 손에 있다면, 공격력 저하를 1 얻습니다.")),

	GUILT(53, "허기의 저주", DeckCardType.CURSE, DeckCardRarity.COMMON, DeckCardTarget.NONE, 0, 0, 0, 0, 0, 0, 0, 0, false, Talent.CUR,
			new DeckCardEffect() {
				@Override public void apply(DeckBuilderCombat combat, DeckCard card, int cardCode, DeckPlayResult.Builder result) {}
				@Override public String rulesText(DeckCard card, int cardCode, DeckBuilderCombat combat) {
					int remaining = 5 - DeckCard.upgradeLevel(cardCode);
					return "사용불가. (" + remaining + "의 전투 후 제거)";
				}
			}) {
		@Override public int maxUpgradeLevel() { return 5; }
	},

	REGRET(54, "부식의 저주", DeckCardType.CURSE, DeckCardRarity.COMMON, DeckCardTarget.NONE, 0, 0, 0, 0, 0, 0, 0, 0, false, Talent.CUR,
			new DeckCardEffects.TextOnly("내 턴 종료 시 이 카드가 손에 있다면, 손에 있는 카드 1장당 체력을 1 잃습니다.")),

	SPORE_INVASION(55, "과성장의 저주", DeckCardType.CURSE, DeckCardRarity.COMMON, DeckCardTarget.NONE, 1, 0, 0, 0, 0, 0, 0, keywords(DeckCardKeyword.EXHAUST), false, Talent.CUR),

	RUPTURE(56, "파문의 호흡", DeckCardType.POWER, DeckCardRarity.UNCOMMON, DeckCardTarget.NONE, 1, 0, 0, 0, 0, 0, 0, 0, true, Talent.IRON_WILL, HeroClass.WARRIOR, 0,
			new DeckCardEffects.RuptureEffect()),

	BLOODLETTING(57, "기적의 에너지", DeckCardType.SKILL, DeckCardRarity.COMMON, DeckCardTarget.NONE, 0, 0, 0, 0, 0, 0, 0, 0, true, Talent.STRONGMAN, HeroClass.WARRIOR, 0,
			new DeckCardEffects.BloodlettingEffect()),

	FIRESEA(58, "최후의 파문", DeckCardType.POWER, DeckCardRarity.UNCOMMON, DeckCardTarget.NONE, 1, 0, 0, 0, 0, 0, 0, 0, true, Talent.DEATHLESS_FURY, HeroClass.WARRIOR, 0,
			new DeckCardEffects.FireseaEffect()),

	BLOOD_WALL(59, "파문의 양극", DeckCardType.SKILL, DeckCardRarity.COMMON, DeckCardTarget.NONE, 2, 0, 0, 0, 0, 0, 0, 0, true, Talent.LIQUID_WILLPOWER, HeroClass.WARRIOR, 0,
			new DeckCardEffects.BloodWallEffect()),

	MALICE(60, "더 패션", DeckCardType.ATTACK, DeckCardRarity.UNCOMMON, DeckCardTarget.SINGLE, 0, 5, 0, 0, 0, 0, 0, 0, true, Talent.PROVOKED_ANGER, HeroClass.WARRIOR, 0,
			new DeckCardEffects.MaliceBonus()) {
		@Override public int damage(int code) { return 5; }
	},

	BLOODFLOW(61, "정신적인 폭발력", DeckCardType.ATTACK, DeckCardRarity.UNCOMMON, DeckCardTarget.SINGLE, 1, 15, 0, 0, 0, 0, 0, 0, true, Talent.CLEAVE, HeroClass.WARRIOR, 0,
			new DeckCardEffects.BloodflowEffect(), new DeckCardEffects.Damage()) {
		@Override public int damage(int code) { return upgradeLevel(code) > 0 ? 20 : 15; }
	},

	BRAND(62, "그 피의 운명", DeckCardType.SKILL, DeckCardRarity.UNCOMMON, DeckCardTarget.NONE, 0, 0, 0, 0, 0, 0, 0, 0, true, Talent.EVEN_THE_ODDS, HeroClass.WARRIOR, 0,
			new DeckCardEffects.BrandEffect()),

	INDOMITABLE(63, "원기 회복", DeckCardType.SKILL, DeckCardRarity.RARE, DeckCardTarget.NONE, 2, 0, 0, 0, 0, 0, 0, keywords(DeckCardKeyword.EXHAUST), true, Talent.HEARTY_MEAL, HeroClass.WARRIOR, 0,
			new DeckCardEffects.IndomitableEffect()),

	REND(64, "오버드라이브", DeckCardType.ATTACK, DeckCardRarity.RARE, DeckCardTarget.SINGLE, 2, 5, 0, 0, 0, 0, 0, 0, true, Talent.LETHAL_MOMENTUM, HeroClass.WARRIOR, 0,
			new DeckCardEffects.RendBonus()) {
		@Override public int damage(int code) { return upgradeLevel(code) > 0 ? 7 : 5; }
	},

	FOOTWORK(65, "빵", DeckCardType.POWER, DeckCardRarity.UNCOMMON, DeckCardTarget.NONE, 1, 0, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.BERRY,
			new DeckCardEffects.Dexterity(2, 3)),

	SURGE(66, "화이트 스네이크의 장비 DISC", DeckCardType.POWER, DeckCardRarity.UNCOMMON, DeckCardTarget.NONE, 2, 0, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.ARTIFACT_SPELLBOOK,
			new DeckCardEffects.SurgeEffect()) {
		@Override
		public int cost(int code) { return upgradeLevel(code) > 0 ? 1 : 2; }
	},

	CALAMITY(67, "윌슨 필립스 상원의원", DeckCardType.SKILL, DeckCardRarity.UNCOMMON, DeckCardTarget.NONE, 2, 0, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.WAND_LIGHTNING, null, 0, WillsonSprite.class,
			new DeckCardEffects.PlayRandomFromDrawPile(2, 3)) {
		@Override
		public int cost(int code) { return upgradeLevel(code) > 0 ? 1 : 2; }
	},

	PRICE_OF_SIN(68, "죄의 대가", DeckCardType.STATUS, DeckCardRarity.COMMON, DeckCardTarget.NONE, 1, 0, 0, 0, 0, 0, 0, 0, false, ItemSpriteSheet.UV, null, 0, CivilSprite.class,
			new DeckCardEffects.TextOnly("내 턴 종료 시 이 카드가 손에 있다면, 체력을 6 잃습니다.")),

	FORESIGHT(69, "염청 장치", DeckCardType.SKILL, DeckCardRarity.UNCOMMON, DeckCardTarget.NONE, 0, 0, 0, 2, 0, 0, 0, keywords(DeckCardKeyword.EXHAUST), true, ItemSpriteSheet.EYE_OF_NEWT,
			new DeckCardEffects.ForesightEffect()) {
		@Override
		public boolean hasKeyword(int code, DeckCardKeyword keyword) {
			if (keyword == DeckCardKeyword.EXHAUST && upgradeLevel(code) > 0) return false;
			return super.hasKeyword(code, keyword);
		}
	},

	PANIC_BUTTON(70, "은빛 무당벌레 브로치", DeckCardType.SKILL, DeckCardRarity.UNCOMMON, DeckCardTarget.NONE, 0, 0, 0, 0, 0, 0, 0, keywords(DeckCardKeyword.EXHAUST), true, ItemSpriteSheet.ARMOR_MAIL,
			new DeckCardEffects.PanicButtonEffect(30, 40)),

	PRODUCTION(71, "가죽 주머니", DeckCardType.SKILL, DeckCardRarity.UNCOMMON, DeckCardTarget.NONE, 0, 0, 0, 0, 0, 0, 0, keywords(DeckCardKeyword.EXHAUST), true, ItemSpriteSheet.VIAL,
			new DeckCardEffects.GainEnergyEffect(2, 3)),

	IMPATIENCE(72, "마젠트 마젠트", DeckCardType.SKILL, DeckCardRarity.UNCOMMON, DeckCardTarget.NONE, 0, 0, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.DART, null, 0, CrystalSpireSprite.Blue.class,
			new DeckCardEffects.ImpatienceEffect()),

	DARK_SHACKLES(73, "에코즈 ACT.3", DeckCardType.SKILL, DeckCardRarity.UNCOMMON, DeckCardTarget.SINGLE, 0, 0, 0, 0, 0, 0, 0, keywords(DeckCardKeyword.EXHAUST), true, ItemSpriteSheet.ARTIFACT_CHAINS, null, 0, Act3Sprite.class,
			new DeckCardEffects.DarkShacklesEffect(9, 15)),

	EXTEND(74, "베이비페이스", DeckCardType.SKILL, DeckCardRarity.UNCOMMON, DeckCardTarget.NONE, 0, 0, 0, 0, 0, 0, 0, keywords(DeckCardKeyword.EXHAUST), true, ItemSpriteSheet.DEWDROP, null, 0, MimicSprite.Golden.class,
			new DeckCardEffects.ExtendEffect()) {
		@Override
		public boolean hasKeyword(int code, DeckCardKeyword keyword) {
			if (keyword == DeckCardKeyword.EXHAUST && upgradeLevel(code) > 0) return false;
			return super.hasKeyword(code, keyword);
		}
	},

	ANXIETY(75, "존갈리 A", DeckCardType.SKILL, DeckCardRarity.UNCOMMON, DeckCardTarget.NONE, 0, 0, 0, 0, 0, 0, 0, keywords(DeckCardKeyword.RETAIN), true, ItemSpriteSheet.SCROLL_RAIDO, null, 0, Scorpio2Sprite.class,
			new DeckCardEffects.AnxietyEffect()),

	ENDEAVOR(76, "츠지 아야", DeckCardType.SKILL, DeckCardRarity.UNCOMMON, DeckCardTarget.NONE, 1, 0, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.SCROLL_NAUDIZ, null, 0, TsujiAyaSprite.class,
			new DeckCardEffects.OtherClassAttackDiscoverEffect()),

	EQUILIBRIUM(77, "구리빛 무당벌레 브로치", DeckCardType.SKILL, DeckCardRarity.UNCOMMON, DeckCardTarget.NONE, 2, 0, 13, 0, 0, 0, 0, 0, true, ItemSpriteSheet.ARMOR_LEATHER,
			new DeckCardEffects.EquilibriumEffect()),

	ORANGE_BOMB(78, "오렌지 폭탄", DeckCardType.SKILL, DeckCardRarity.UNCOMMON, DeckCardTarget.NONE, 2, 0, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.BOMB,
			new DeckCardEffects.OrangeBombEffect(40, 50)),

	ABDUL_QUEST(79, "압둘의 퀘스트", DeckCardType.QUEST, DeckCardRarity.COMMON, DeckCardTarget.NONE, 0, 0, 0, 0, 0, 0, 0, 0, false, ItemSpriteSheet.TORN_PAGE, null, 0, GhostSprite.class,
			new DeckCardEffects.TextOnly("사용불가. _강적을 처치_하면 보상으로 '무함마드 압둘' 카드를 획득합니다.")),

	MUHAMMAD_AVDOL(80, "무함마드 압둘", DeckCardType.ATTACK, DeckCardRarity.RARE, DeckCardTarget.SINGLE, 0, 14, 0, 0, 0, 0, 0, 0, false, ItemSpriteSheet.SCROLL_LAGUZ, null, 0, DvdolSprite.class) {
		@Override public int damage(int code) { return upgradeLevel(code) > 0 ? 18 : 14; }
	},

	SHITTIM_BOX(81, "싯딤의 상자", DeckCardType.SKILL, DeckCardRarity.UNCOMMON, DeckCardTarget.NONE, 0, 0, 0, 0, 0, 0, 0, 0, false, ItemSpriteSheet.DECK,
			new DeckCardEffects.ShittimBoxEffect()),

	FLASH_OF_STEEL(82, "클래시", DeckCardType.ATTACK, DeckCardRarity.UNCOMMON, DeckCardTarget.SINGLE, 0, 5, 0, 1, 0, 0, 0, 0, true, ItemSpriteSheet.RAPIER, null, 0, PiranhaSprite.class),

	CLEAVE_ALL(83, "러버즈", DeckCardType.ATTACK, DeckCardRarity.UNCOMMON, DeckCardTarget.SINGLE, 0, 8, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.GREATAXE, null, 0, SwarmSprite.class,
			new DeckCardEffects.CleaveAllEffect()),

	PULSING_AXE(84, "노토리어스 B.I.G", DeckCardType.ATTACK, DeckCardRarity.UNCOMMON, DeckCardTarget.SINGLE, 1, 11, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.BOOMERANG,
			new DeckCardEffects.PulsingAxeEffect()),

	SCOUT_STRIKE(85, "JAIL HOUSE LOCK의 함정", DeckCardType.ATTACK, DeckCardRarity.UNCOMMON, DeckCardTarget.SINGLE, 1, 9, 0, 0, 0, 0, 0, 0, true, TeleportationTrap.class,
			new DeckCardEffects.ScoutStrikeEffect()),

	IMPOSTING_PRESENCE(86, "와이어드 벡", DeckCardType.POWER, DeckCardRarity.UNCOMMON, DeckCardTarget.NONE, 0, 0, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.SCROLL_RAIDO, null, 0, WiredSprite.class,
			new DeckCardEffects.ImpostingPresenceEffect(10, 14)),

	ANCHOR(87, "소프트&웨트의 방울", DeckCardType.POWER, DeckCardRarity.UNCOMMON, DeckCardTarget.NONE, 1, 0, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.STYLUS,
			new DeckCardEffects.AnchorEffect(5, 7)),

	PROWESS(88, "더 풀", DeckCardType.POWER, DeckCardRarity.UNCOMMON, DeckCardTarget.NONE, 1, 0, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.SCROLL_NAUDIZ, null, 0, SnakeSprite.class,
			new DeckCardEffects.ProwessEffect()),

	AUTOMATION(89, "행드맨", DeckCardType.POWER, DeckCardRarity.UNCOMMON, DeckCardTarget.NONE, 1, 0, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.WAND_MAGIC_MISSILE, null, 0, SkeletonSprite.class,
			new DeckCardEffects.AutomationEffect()) {
		@Override public int cost(int code) { return upgradeLevel(code) > 0 ? 0 : 1; }
	},

	STRATAGEM(90, "허밋 퍼플의 장비 DISC", DeckCardType.POWER, DeckCardRarity.UNCOMMON, DeckCardTarget.NONE, 1, 0, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.ARTIFACT_TALISMAN,
			new DeckCardEffects.StratagemEffect()) {
		@Override public int cost(int code) { return upgradeLevel(code) > 0 ? 0 : 1; }
	},

	FOCUSED_FIRE(91, "호루스신", DeckCardType.ATTACK, DeckCardRarity.RARE, DeckCardTarget.SINGLE, 1, 12, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.HOLSTER, null, 0, CrabSprite.class,
			new DeckCardEffects.Damage(), new DeckCardEffects.RetainHandThisTurnEffect()) {
		@Override public int damage(int code) { return upgradeLevel(code) > 0 ? 16 : 12; }
	},

	GOLDEN_AXE(92, "리사리사", DeckCardType.ATTACK, DeckCardRarity.RARE, DeckCardTarget.SINGLE, 1, 0, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.GREATAXE, null, 0, LisaSprite.class,
			new DeckCardEffects.GoldenAxeEffect()) {
		@Override public boolean hasKeyword(int code, DeckCardKeyword keyword) {
			if (keyword == DeckCardKeyword.RETAIN && upgradeLevel(code) > 0) return true;
			return super.hasKeyword(code, keyword);
		}
	},

	RIP_AND_TEAR(93, "원더 오브 U", DeckCardType.ATTACK, DeckCardRarity.RARE, DeckCardTarget.SINGLE, 2, 0, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.GREATAXE, null, 0, SentryRoom.SentrySprite.class,
			new DeckCardEffects.RipAndTearEffect()),

	GREED_HAND(94, "추심인 마릴린 맨슨", DeckCardType.ATTACK, DeckCardRarity.RARE, DeckCardTarget.SINGLE, 2, 20, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.GOLD, null, 0, BanditSprite.class,
			new DeckCardEffects.Damage(), new DeckCardEffects.GreedHandEffect()) {
		@Override public int damage(int code) { return upgradeLevel(code) > 0 ? 25 : 20; }
	},

	JACKPOT(95, "배드 컴퍼니의 DISC", DeckCardType.ATTACK, DeckCardRarity.RARE, DeckCardTarget.SINGLE, 3, 25, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.BCOM,
			new DeckCardEffects.Damage(), new DeckCardEffects.JackpotEffect()) {
		@Override public int damage(int code) { return upgradeLevel(code) > 0 ? 30 : 25; }
	},

	SECRET_TECHNIQUE(96, "로긴즈", DeckCardType.SKILL, DeckCardRarity.RARE, DeckCardTarget.NONE, 0, 0, 0, 0, 0, 0, 0, keywords(DeckCardKeyword.EXHAUST), true, ItemSpriteSheet.SCROLL_MANNAZ, null, 0, Bt1Sprite.class,
			new DeckCardEffects.DrawPileTypeSelectEffect(DeckCardType.SKILL)) {
		@Override public boolean hasKeyword(int code, DeckCardKeyword keyword) {
			if (keyword == DeckCardKeyword.EXHAUST && upgradeLevel(code) > 0) return false;
			return super.hasKeyword(code, keyword);
		}
	},

	SECRET_WEAPON(97, "메시나", DeckCardType.SKILL, DeckCardRarity.RARE, DeckCardTarget.NONE, 0, 0, 0, 0, 0, 0, 0, keywords(DeckCardKeyword.EXHAUST), true, ItemSpriteSheet.SCROLL_TIWAZ, null, 0, Bt2Sprite.class,
			new DeckCardEffects.DrawPileTypeSelectEffect(DeckCardType.ATTACK)) {
		@Override public boolean hasKeyword(int code, DeckCardKeyword keyword) {
			if (keyword == DeckCardKeyword.EXHAUST && upgradeLevel(code) > 0) return false;
			return super.hasKeyword(code, keyword);
		}
	},

	GRAND_STRATEGY(98, "실험용 흡혈귀", DeckCardType.SKILL, DeckCardRarity.RARE, DeckCardTarget.NONE, 0, 0, 0, 3, 0, 0, 0, keywords(DeckCardKeyword.EXHAUST), true, ItemSpriteSheet.SCROLL_GYFU, null, 0, VtestSprite.class,
			new DeckCardEffects.GrandStrategyEffect()) {
		@Override public int draw(int code) { return upgradeLevel(code) > 0 ? 4 : 3; }
	},

	ENTRENCHED_PLAN(99, "다이아몬드 무당벌레 브로치", DeckCardType.SKILL, DeckCardRarity.RARE, DeckCardTarget.NONE, 0, 0, 50, 0, 0, 0, 0, 0, true, ItemSpriteSheet.ARMOR_PLATE,
			new DeckCardEffects.EntrenchedPlanEffect()) {
		@Override public int block(int code) { return upgradeLevel(code) > 0 ? 75 : 50; }
	},

	ALCHEMY(100, "미스터 프레지던트의 장비 DISC", DeckCardType.SKILL, DeckCardRarity.RARE, DeckCardTarget.NONE, 1, 0, 0, 0, 0, 0, 0, keywords(DeckCardKeyword.EXHAUST), true, ItemSpriteSheet.ARTIFACT_TOOLKIT,
			new DeckCardEffects.AlchemyEffect()) {
		@Override public int cost(int code) { return upgradeLevel(code) > 0 ? 0 : 1; }
	},

	APPOINTMENT(101, "전투조류 스피드왜건", DeckCardType.SKILL, DeckCardRarity.RARE, DeckCardTarget.NONE, 1, 0, 0, 0, 0, 0, 0, keywords(DeckCardKeyword.EXHAUST), true, ItemSpriteSheet.SCROLL_HOLDER, null, 0, Speedwagon2Sprite.class,
			new DeckCardEffects.AppointmentEffect()) {
		@Override public boolean hasKeyword(int code, DeckCardKeyword keyword) {
			if (keyword == DeckCardKeyword.RETAIN && upgradeLevel(code) > 0) return true;
			return super.hasKeyword(code, keyword);
		}
	},

	SCRIBBLE(102, "탈출용 로프", DeckCardType.SKILL, DeckCardRarity.RARE, DeckCardTarget.NONE, 1, 0, 0, 0, 0, 0, 0, keywords(DeckCardKeyword.EXHAUST), true, ItemSpriteSheet.ESCAPE,
			new DeckCardEffects.ScribbleEffect()) {
		@Override public boolean hasKeyword(int code, DeckCardKeyword keyword) {
			if (keyword == DeckCardKeyword.RETAIN && upgradeLevel(code) > 0) return true;
			return super.hasKeyword(code, keyword);
		}
	},

	PUMMEL(103, "스톤 오션 죠타로", DeckCardType.SKILL, DeckCardRarity.RARE, DeckCardTarget.NONE, 3, 0, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.GREATAXE, null, 0, JotaroSprite.class,
			new DeckCardEffects.PummelDiscardEffect()) {
	},

	ENTROPY(104, "실버 채리엇 레퀴엠의 장비 DISC", DeckCardType.POWER, DeckCardRarity.UNCOMMON, DeckCardTarget.NONE, 1, 0, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.SKULL,
			new DeckCardEffects.EntropyEffect()) {
		@Override public boolean hasKeyword(int code, DeckCardKeyword keyword) {
			if (keyword == DeckCardKeyword.VANGUARD && upgradeLevel(code) > 0) return true;
			return super.hasKeyword(code, keyword);
		}
	},

	NOSTALGIA(105, "하이어로펜트 그린의 장비 DISC", DeckCardType.POWER, DeckCardRarity.UNCOMMON, DeckCardTarget.NONE, 1, 0, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.ARTIFACT_CHAINS,
			new DeckCardEffects.NostalgiaEffect()) {
		@Override public int cost(int code) { return upgradeLevel(code) > 0 ? 0 : 1; }
	},

	CHAOS(106, "더 월드의 장비 DISC", DeckCardType.POWER, DeckCardRarity.UNCOMMON, DeckCardTarget.NONE, 2, 0, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.ARTIFACT_HOURGLASS,
			new DeckCardEffects.ChaosEffect()) {
		@Override public int cost(int code) { return upgradeLevel(code) > 0 ? 1 : 2; }
	},

	ROLLING_BOULDER(107, "폭약 더미", DeckCardType.POWER, DeckCardRarity.UNCOMMON, DeckCardTarget.NONE, 3, 0, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.TBOMB2,
			new DeckCardEffects.RollingBoulderEffect(5, 10)),

	ETERNAL_ARMOR(108, "하이웨이 스타의 장비 DISC", DeckCardType.POWER, DeckCardRarity.RARE, DeckCardTarget.NONE, 3, 0, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.ARTIFACT_CHALICE3,
			new DeckCardEffects.RegenEffect(9, 12)),

	OSIRIS_GOD(109, "오시리스신", DeckCardType.ATTACK, DeckCardRarity.RARE, DeckCardTarget.SINGLE, 1, 10, 0, 0, 0, 0, 0, keywords(DeckCardKeyword.EXHAUST), false, ItemSpriteSheet.SCROLL_TIWAZ, null, 0, ImpSprite.class,
			new DeckCardEffects.Damage(), new DeckCardEffects.OsirisEffect()) {
		@Override public int damage(int code) { return upgradeLevel(code) > 0 ? 14 : 10; }
	},

	GLIDE(110, "동료의 희생", DeckCardType.STATUS, DeckCardRarity.COMMON, DeckCardTarget.NONE, 1, 0, 0, 0, 0, 0, 0, 0, false, ItemSpriteSheet.ELIXIR_FEATHER, null, 0, GhostSprite.class,
			new DeckCardEffects.GlideEffect()) {
	},

	ENERGY_WAND(111, "레드 핫 칠리 페퍼의 사격 DISC", DeckCardType.SKILL, DeckCardRarity.RARE, DeckCardTarget.NONE, 1, 0, 0, 0, 0, 0, 0, keywords(DeckCardKeyword.RETAIN), true, ItemSpriteSheet.WAND_LIGHTNING,
			new DeckCardEffects.WandTextEffect("내 턴 시작 시, 에너지를 1 얻습니다.")),

	DRAW_WAND(112, "실버 채리엇의 사격 DISC", DeckCardType.SKILL, DeckCardRarity.UNCOMMON, DeckCardTarget.NONE, 1, 0, 0, 0, 0, 0, 0, keywords(DeckCardKeyword.RETAIN), true, ItemSpriteSheet.WAND_PRISMATIC_LIGHT,
			new DeckCardEffects.WandTextEffect("내 턴 시작 시, 카드를 1장 뽑습니다.")),

	BARRIER_WAND(113, "서피스의 사격 DISC", DeckCardType.SKILL, DeckCardRarity.UNCOMMON, DeckCardTarget.RANDOM_ENEMY, 1, 0, 0, 0, 0, 0, 0, keywords(DeckCardKeyword.RETAIN), true, ItemSpriteSheet.WAND_LIVING_EARTH,
			new DeckCardEffects.WandTextEffect("내 턴 시작 시, 보호막을 3 얻고 무작위 적에게 피해를 2 줍니다.")),

	ENHANCEMENT_WAND(114, "터스크 ACT.1의 사격 DISC", DeckCardType.SKILL, DeckCardRarity.UNCOMMON, DeckCardTarget.RANDOM_ENEMY, 1, 0, 0, 0, 0, 0, 0, keywords(DeckCardKeyword.RETAIN), true, ItemSpriteSheet.WAND_DISINTEGRATION,
			new DeckCardEffects.EnhancementWandTextEffect()) {
		@Override public int damage(int code) { return upgradeLevel(code) > 0 ? 10 : 8; }
	},

	MAGICIANS_WAND(115, "매지션즈 레드의 사격 DISC", DeckCardType.SKILL, DeckCardRarity.UNCOMMON, DeckCardTarget.ALL_ENEMIES, 1, 0, 0, 0, 0, 0, 0, keywords(DeckCardKeyword.RETAIN), true, ItemSpriteSheet.WAND_FIREBOLT,
			new DeckCardEffects.WandTextEffect("내 턴 종료 시, 모든 적에게 피해를 5 줍니다.")),

	HORUS_WAND(116, "호루스신의 사격 DISC", DeckCardType.SKILL, DeckCardRarity.COMMON, DeckCardTarget.RANDOM_ENEMY, 1, 0, 0, 0, 0, 0, 0, keywords(DeckCardKeyword.RETAIN), true, ItemSpriteSheet.WAND_FROST,
			new DeckCardEffects.WandTextEffect("내 턴 종료 시, 무작위 적에게 공격력 저하를 1 부여하고 피해를 2 줍니다.")),

	HEAVENS_WAND(117, "헤븐즈 도어의 사격 DISC", DeckCardType.SKILL, DeckCardRarity.UNCOMMON, DeckCardTarget.RANDOM_ENEMY, 1, 0, 0, 0, 0, 0, 0, keywords(DeckCardKeyword.RETAIN), true, ItemSpriteSheet.WAND_CORRUPTION,
			new DeckCardEffects.WandTextEffect("내 턴 종료 시, 무작위 적에게 피해 증폭을 1 부여합니다.")),

	SOFT_WAND(118, "소프트&웨트의 사격 DISC", DeckCardType.SKILL, DeckCardRarity.UNCOMMON, DeckCardTarget.NONE, 1, 0, 0, 0, 0, 0, 0, keywords(DeckCardKeyword.RETAIN), true, ItemSpriteSheet.WAND_TRANSFUSION,
			new DeckCardEffects.WandTextEffect("내 턴 시작 시, 체력을 2 회복합니다.")),

	GOLD_EXPERIENCE_WAND(119, "골드 익스피리언스의 사격 DISC", DeckCardType.SKILL, DeckCardRarity.RARE, DeckCardTarget.NONE, 1, 0, 0, 0, 0, 0, 0, keywords(DeckCardKeyword.RETAIN), true, ItemSpriteSheet.WAND_REGROWTH,
			new DeckCardEffects.WandTextEffect("내 턴 시작 시, 비용이 0인 무작위 카드 2장에 소멸을 부여하고 손으로 가져옵니다.")),

	TUSK2_WAND(120, "터스크 ACT.2~4의 사격 DISC", DeckCardType.SKILL, DeckCardRarity.UNCOMMON, DeckCardTarget.NONE, 1, 0, 0, 0, 0, 0, 0, keywords(DeckCardKeyword.RETAIN), true, ItemSpriteSheet.WAND_WARDING,
			new DeckCardEffects.WandTextEffect("내 턴 종료 시, 회전하는 손톱 2장을 뽑을 카드 더미에 섞어 넣습니다.")),

	COMBAT_BREATHING(121, "피묻은 돌가면", DeckCardType.POWER, DeckCardRarity.COMMON, DeckCardTarget.NONE, 0, 0, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.SMASK, HeroClass.WARRIOR, 0,
			new DeckCardEffects.CombatBreathingEffect()) {
		@Override public int block(int code) { return upgradeLevel(code) > 0 ? 5 : 3; }
	},

	BLOODY_CLOAK(122, "용기의 보호막", DeckCardType.POWER, DeckCardRarity.UNCOMMON, DeckCardTarget.NONE, 1, 0, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.ARMOR_WARRIOR, HeroClass.WARRIOR, 0,
			new DeckCardEffects.BloodyCloakEffect()) {
		@Override public int block(int code) { return upgradeLevel(code) > 0 ? 8 : 6; }
	},

	SLEDGEHAMMER(123, "슬레지해머", DeckCardType.ATTACK, DeckCardRarity.UNCOMMON, DeckCardTarget.SINGLE, 2, 12, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.RUNIC_BLADE,
			new DeckCardEffects.SledgehammerEffect()) {
		@Override public int damage(int code) {
			int n = upgradeLevel(code);
			return 12 + n * (n + 7) / 2;
		}
		@Override public int maxUpgradeLevel() { return 15; }
	},

	TARKUS_GREATSWORD(124, "타커스", DeckCardType.ATTACK, DeckCardRarity.COMMON, DeckCardTarget.SINGLE, 2, 0, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.GREATAXE, null, 0, ZombietSprite.class,
			new DeckCardEffects.TarkusGreatswordDamage()),

	ODD_COMIC_BOOK(125, "배드 컴퍼니 그린 베레", DeckCardType.ATTACK, DeckCardRarity.COMMON, DeckCardTarget.SINGLE, 2, 0, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.WAR_HAMMER, null, 0, BcomgSprite.class,
			new DeckCardEffects.OddComicBookDamage()),

	CLUBBING(126, "타커스의 대검", DeckCardType.ATTACK, DeckCardRarity.COMMON, DeckCardTarget.SINGLE, 3, 32, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.GREATSWORD) {
		@Override public int damage(int code) { return upgradeLevel(code) > 0 ? 42 : 32; }
	},

	SOUL_CUT(127, "부패한 흡혈귀", DeckCardType.ATTACK, DeckCardRarity.UNCOMMON, DeckCardTarget.SINGLE, 2, 16, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.SCROLL_TIWAZ, null, 0, ZombieBrute3Sprite.class,
			new DeckCardEffects.SoulCutEffect()) {
		@Override public int damage(int code) { return upgradeLevel(code) > 0 ? 22 : 16; }
	},

	WHIRLWIND(128, "DISC가 심어진 독개구리", DeckCardType.ATTACK, DeckCardRarity.UNCOMMON, DeckCardTarget.ALL_ENEMIES, -1, 5, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.FORCE_CUBE,
			new DeckCardEffects.WhirlwindEffect()) {
		@Override public int damage(int code) { return upgradeLevel(code) > 0 ? 8 : 5; }
	},

	SKY_HIGH(129, "스카이 하이", DeckCardType.ATTACK, DeckCardRarity.UNCOMMON, DeckCardTarget.SINGLE, 1, 3, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.SAI,
			new DeckCardEffects.SkyHighEffect()) {
		@Override public int damage(int code) { return upgradeLevel(code) > 0 ? 4 : 3; }
	},

	RELIC_SELECTION_BOX(130, "아이템 선택 상자", DeckCardType.SKILL, DeckCardRarity.UNCOMMON, DeckCardTarget.NONE, 0, 0, 0, 0, 0, 0, 0, 0, false, ItemSpriteSheet.CRYSTAL_CHEST,
			new DeckCardEffects.RelicSelectionBoxEffect()),

	POTION_SELECTION_BOX(131, "물약 선택 상자", DeckCardType.SKILL, DeckCardRarity.UNCOMMON, DeckCardTarget.NONE, 0, 0, 0, 0, 0, 0, 0, 0, false, ItemSpriteSheet.POTION_HOLDER,
			new DeckCardEffects.PotionSelectionBoxEffect()),

	END_OF_PACT(132, "툼 오브 더 붐 · 1", DeckCardType.ATTACK, DeckCardRarity.RARE, DeckCardTarget.ALL_ENEMIES, 0, 17, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.SCROLL_TIWAZ, null, 0, GnollGeomancerSprite.class,
			new DeckCardEffects.EndOfPactEffect()) {
		@Override public int damage(int code) { return upgradeLevel(code) > 0 ? 23 : 17; }
	},

	BURNING_PACT(133, "광산 폭약", DeckCardType.SKILL, DeckCardRarity.COMMON, DeckCardTarget.NONE, 1, 0, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.TBOMB,
			new DeckCardEffects.BurningPactEffect()),

	FIREPOWER_AMP(134, "스페이스 트러킹", DeckCardType.SKILL, DeckCardRarity.RARE, DeckCardTarget.NONE, 1, 0, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.KINGT,
			new DeckCardEffects.FirepowerAmpEffect()),

	SECOND_WIND(135, "로카카카", DeckCardType.SKILL, DeckCardRarity.COMMON, DeckCardTarget.NONE, 1, 0, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.RO1,
			new DeckCardEffects.SecondWindEffect()),

	NUMBNESS(136, "펄 잼의 장비 DISC", DeckCardType.POWER, DeckCardRarity.UNCOMMON, DeckCardTarget.NONE, 1, 0, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.ARTIFACT_HORN4,
			new DeckCardEffects.NumbnessEffect()),

	HELLFIRE(137, "G.E.R의 주먹", DeckCardType.ATTACK, DeckCardRarity.RARE, DeckCardTarget.SINGLE, 2, 0, 0, 0, 0, 0, 0, keywords(DeckCardKeyword.EXHAUST), true, ItemSpriteSheet.GAUNTLETS,
			new DeckCardEffects.HellfireEffect()),

	DEMON_EYE(138, "소대", DeckCardType.SKILL, DeckCardRarity.COMMON, DeckCardTarget.NONE, 1, 0, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.RING_RUBY, null, 0, Abomination2Sprite.class,
			new DeckCardEffects.DemonEyeEffect()),

	DARK_EMBRACE(139, "맨 인 더 미러의 장비 DISC", DeckCardType.POWER, DeckCardRarity.UNCOMMON, DeckCardTarget.NONE, 2, 0, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.ARTIFACT_BEACON,
			new DeckCardEffects.DarkEmbraceEffect()) {
		@Override public int cost(int code) { return upgradeLevel(code) > 0 ? 1 : 2; }
	},

	FORGOTTEN_RITUAL(140, "히로세 야스호", DeckCardType.SKILL, DeckCardRarity.UNCOMMON, DeckCardTarget.NONE, 1, 0, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.SCROLL_NAUDIZ, null, 0, YasuSprite.class,
			new DeckCardEffects.ForgottenRitualEffect()),

	MULTI_HIT(141, "쌍검", DeckCardType.ATTACK, DeckCardRarity.RARE, DeckCardTarget.SINGLE, 1, 0, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.DBLADE,
			new DeckCardEffects.PummelEffect()) {
		@Override public int damage(int code) { return upgradeLevel(code) > 0 ? 6 : 4; }
	},

	SOUL_TOLL(142, "영혼 징수", DeckCardType.CURSE, DeckCardRarity.COMMON, DeckCardTarget.NONE, 0, 0, 0, 0, 0, 0, 0, keywords(DeckCardKeyword.PERMANENT), false, ItemSpriteSheet.TOKEN,
			new DeckCardEffects.TextOnly("사용불가.")) {
		@Override public boolean unplayable(int code) { return true; }
	},

	ASHEN_STRIKE(143, "야마기시 유카코", DeckCardType.ATTACK, DeckCardRarity.COMMON, DeckCardTarget.SINGLE, 1, 0, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.DAGGER, null, 0, YukakoSprite.class,
			new DeckCardEffects.AshenStrikeEffect()) {
		@Override public int damage(int code) { return 6; }
	},

	HARVEST(144, "기묘한 만화책", DeckCardType.ATTACK, DeckCardRarity.COMMON, DeckCardTarget.SINGLE, 3, 27, 0, 0, 0, 0, 0, keywords(DeckCardKeyword.RETAIN), true, ItemSpriteSheet.WAR_HAMMER) {
		@Override public int damage(int code) { return upgradeLevel(code) > 0 ? 33 : 27; }
	},

	CONCLUSION(145, "선", DeckCardType.ATTACK, DeckCardRarity.COMMON, DeckCardTarget.ALL_ENEMIES, 1, 12, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.SCROLL_TIWAZ, null, 0, ElementalSprite.Fire.class,
			new DeckCardEffects.ConclusionEffect()) {
		@Override public int damage(int code) { return upgradeLevel(code) > 0 ? 16 : 12; }
	},

	BURIAL(146, "휘채활도", DeckCardType.ATTACK, DeckCardRarity.UNCOMMON, DeckCardTarget.SINGLE, 4, 52, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.GREATAXE) {
		@Override public int damage(int code) { return upgradeLevel(code) > 0 ? 63 : 52; }
	},

	BLASPHEMY(147, "바나나 권총", DeckCardType.ATTACK, DeckCardRarity.COMMON, DeckCardTarget.SINGLE, 1, 13, 0, 0, 0, 0, 0, keywords(DeckCardKeyword.TRANSIENT), true, ItemSpriteSheet.WHIP) {
		@Override public int damage(int code) { return upgradeLevel(code) > 0 ? 17 : 13; }
	},

	DAGGER_THROW(148, "괴인 두비", DeckCardType.ATTACK, DeckCardRarity.COMMON, DeckCardTarget.SINGLE, 1, 9, 0, 1, 0, 0, 0, 0, true, ItemSpriteSheet.DAGGER, null, 0, ZombiedSprite.class,
			new DeckCardEffects.DaggerThrowEffect()) {
		@Override public int damage(int code) { return upgradeLevel(code) > 0 ? 12 : 9; }
	},

	NEMESIS(149, "아누비스신", DeckCardType.ATTACK, DeckCardRarity.UNCOMMON, DeckCardTarget.SINGLE, 2, 15, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.LONGSWORD,
			new DeckCardEffects.NemesisEffect()) {
		@Override public int damage(int code) { return upgradeLevel(code) > 0 ? 20 : 15; }
	},

	POISON_COAT(150, "이어지는 황금의 정신", DeckCardType.POWER, DeckCardRarity.UNCOMMON, DeckCardTarget.NONE, 2, 0, 0, 0, 0, 0, 0, 0, true, Talent.SPIRIT_BLADES, HeroClass.HUNTRESS, 0,
			new DeckCardEffects.PoisonCoatEffect()),

	POUNCE(219, "실버 채리엇의 레이피어", DeckCardType.ATTACK, DeckCardRarity.UNCOMMON, DeckCardTarget.SINGLE, 2, 14, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.BATTLE_AXE,
			new DeckCardEffects.PounceEffect()) {
		@Override public int damage(int code) { return upgradeLevel(code) > 0 ? 20 : 14; }
	},

	PRECISE_SHOT(220, "스네이크 머플러", DeckCardType.ATTACK, DeckCardRarity.UNCOMMON, DeckCardTarget.SINGLE, 3, 15, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.WAR_SCYTHE,
			new DeckCardEffects.PreciseShotEffect()) {
		@Override public int damage(int code) { return upgradeLevel(code) > 0 ? 19 : 15; }
	},

	GRAND_FINALE(221, "에이자의 돌가면", DeckCardType.ATTACK, DeckCardRarity.RARE, DeckCardTarget.ALL_ENEMIES, 0, 60, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.AJA,
			new DeckCardEffects.TextOnly("남은 카드가 없을 때만 사용할 수 있습니다.")) {
		@Override public int damage(int code) { return upgradeLevel(code) > 0 ? 75 : 60; }
		@Override public boolean unplayable(int code) {
			return DeckBuilderRun.currentCombat != null && !DeckBuilderRun.currentCombat.drawPile.isEmpty();
		}
	},

	ASSASSINATION(222, "캘리포니아 킹 베드", DeckCardType.ATTACK, DeckCardRarity.RARE, DeckCardTarget.SINGLE, 0, 10, 0, 0, 1, 0, 0, keywords(DeckCardKeyword.VANGUARD, DeckCardKeyword.EXHAUST), true, ItemSpriteSheet.KINGB) {
		@Override public int damage(int code) { return upgradeLevel(code) > 0 ? 13 : 10; }
		@Override public int vulnerable(int code) { return upgradeLevel(code) > 0 ? 2 : 1; }
	},

	KILL(223, "섹스 피스톨즈", DeckCardType.ATTACK, DeckCardRarity.RARE, DeckCardTarget.SINGLE, 3, 1, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.MISTA,
			new DeckCardEffects.KillEffect()) {
		@Override public int damage(int code) { return 1 + (DeckBuilderRun.currentCombat == null ? 0 : DeckBuilderRun.currentCombat.cardsDrawnThisCombat); }
		@Override public int cost(int code) { return upgradeLevel(code) > 0 ? 2 : 3; }
	},

	LASER_POINTER(224, "맹독풀의 씨앗", DeckCardType.ATTACK, DeckCardRarity.COMMON, DeckCardTarget.SINGLE, 0, 3, 0, 0, 1, 0, 0, 0, true, ItemSpriteSheet.SEED_SORROWMOSS) {
		@Override public int damage(int code) { return upgradeLevel(code) > 0 ? 4 : 3; }
		@Override public int vulnerable(int code) { return upgradeLevel(code) > 0 ? 2 : 1; }
	},

	GOUGE(225, "육신의 싹", DeckCardType.ATTACK, DeckCardRarity.COMMON, DeckCardTarget.SINGLE, 0, 3, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.SCROLL_GYFU, null, 0, LarvaSprite.class,
			new DeckCardEffects.GougeEffect()) {
		@Override public int damage(int code) { return (upgradeLevel(code) > 0 ? 4 : 3) + (DeckBuilderRun.currentCombat == null ? 0 : DeckBuilderRun.currentCombat.gougeDamageBonus); }
	},

	SYNTHESIS(226, "고드름", DeckCardType.ATTACK, DeckCardRarity.UNCOMMON, DeckCardTarget.SINGLE, 2, 14, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.KATANA,
			new DeckCardEffects.SynthesisEffect()) {
		@Override public int damage(int code) { return upgradeLevel(code) > 0 ? 20 : 14; }
	},

	HELIX_PIERCE(227, "스트레이 캣의 씨앗", DeckCardType.ATTACK, DeckCardRarity.RARE, DeckCardTarget.SINGLE, 0, 0, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.SEED_ROTBERRY,
			new DeckCardEffects.HelixPierceEffect()) {
		@Override public int damage(int code) { return (upgradeLevel(code) > 0 ? 5 : 3) * (DeckBuilderRun.currentCombat == null ? 0 : DeckBuilderRun.currentCombat.energySpentThisTurn); }
	},

	MOON_BAPTISM(228, "이기", DeckCardType.ATTACK, DeckCardRarity.UNCOMMON, DeckCardTarget.SINGLE, 0, 4, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.JAVELIN,
			new DeckCardEffects.MoonBaptismEffect()) {
		@Override public int damage(int code) { return upgradeLevel(code) > 0 ? 5 : 4; }
	},

	KINGS_PUNCH(229, "크래커 볼리", DeckCardType.ATTACK, DeckCardRarity.UNCOMMON, DeckCardTarget.SINGLE, 1, 8, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.BOLAS,
			new DeckCardEffects.KingsPunchText()) {
		@Override public int damage(int code) { return (upgradeLevel(code) > 0 ? 10 : 8) + DeckCardCode.auxValue(code) * (upgradeLevel(code) > 0 ? 6 : 4); }
	},

	DOMINANCE(230, "킹 크림슨의 주먹", DeckCardType.ATTACK, DeckCardRarity.UNCOMMON, DeckCardTarget.SINGLE, 2, 15, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.SHURIKEN,
			new DeckCardEffects.NextTurnEnergyEffect(2, 3)) {
		@Override public int damage(int code) { return upgradeLevel(code) > 0 ? 18 : 15; }
	},

	KINGS_KICK(231, "완전생물의 촉수", DeckCardType.ATTACK, DeckCardRarity.UNCOMMON, DeckCardTarget.SINGLE, 4, 27, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.FLAIL,
			new DeckCardEffects.TextOnly("이 카드를 뽑을 때마다, 이 카드의 비용이 1 감소합니다.")) {
		@Override public int damage(int code) { return upgradeLevel(code) > 0 ? 35 : 27; }
	},

	SO_IT_SHALL_BE(232, "완전생물의 다람쥐", DeckCardType.ATTACK, DeckCardRarity.RARE, DeckCardTarget.SINGLE, 0, 6, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.SCROLL_GYFU, null, 0, SquirrelSprite.class,
			new DeckCardEffects.TextOnly("한 턴에 보조 카드를 3장 사용할 때마다, 이 카드를 손으로 가져옵니다.")) {
		@Override public int damage(int code) { return upgradeLevel(code) > 0 ? 9 : 6; }
	},

	BOMBARDMENT(233, "워킹 하트", DeckCardType.ATTACK, DeckCardRarity.RARE, DeckCardTarget.SINGLE, 3, 18, 0, 0, 0, 0, 0, keywords(DeckCardKeyword.EXHAUST), true, ItemSpriteSheet.KINGW,
			new DeckCardEffects.TextOnly("내 턴 시작 시, 소멸된 카드 더미에서 사용됩니다.")) {
		@Override public int damage(int code) { return upgradeLevel(code) > 0 ? 24 : 18; }
	},

	ANGER(234, "화염풀의 씨앗", DeckCardType.ATTACK, DeckCardRarity.COMMON, DeckCardTarget.SINGLE, 0, 6, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.SEED_FIREBLOOM,
			new DeckCardEffects.ShuffleCopyToDiscardEffect()) {
		@Override public int damage(int code) { return upgradeLevel(code) > 0 ? 8 : 6; }
	},

	MELTING_PUNCH(235, "DIO의 나이프", DeckCardType.ATTACK, DeckCardRarity.COMMON, DeckCardTarget.SINGLE, 1, 10, 0, 0, 0, 0, 0, keywords(DeckCardKeyword.EXHAUST), true, ItemSpriteSheet.TOMAHAWK,
			new DeckCardEffects.MeltingPunchEffect()) {
		@Override public int damage(int code) { return upgradeLevel(code) > 0 ? 14 : 10; }
	},

	THUNDER(236, "베어링 탄환", DeckCardType.ATTACK, DeckCardRarity.COMMON, DeckCardTarget.ALL_ENEMIES, 1, 4, 0, 0, 1, 0, 0, 0, true, ItemSpriteSheet.FISHING_SPEAR) {
		@Override public int damage(int code) { return upgradeLevel(code) > 0 ? 7 : 4; }
	},

	EMBER(237, "토니오의 식칼", DeckCardType.ATTACK, DeckCardRarity.COMMON, DeckCardTarget.SINGLE, 2, 18, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.SICKLE,
			new DeckCardEffects.ExhaustRandomHandCardEffect()) {
		@Override public int damage(int code) { return upgradeLevel(code) > 0 ? 24 : 18; }
	},

	RAMPAGE(238, "11명의 남자", DeckCardType.ATTACK, DeckCardRarity.UNCOMMON, DeckCardTarget.SINGLE, 1, 9, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.SCROLL_GYFU, null, 0, CrystalGuardianSprite.Blue.class,
			new DeckCardEffects.RampageEffect()) {
		@Override public int damage(int code) { return 9 + DeckCardCode.auxValue(code) * (upgradeLevel(code) > 0 ? 9 : 5); }
	},

	DISMANTLE(239, "광란의 시생인", DeckCardType.ATTACK, DeckCardRarity.UNCOMMON, DeckCardTarget.SINGLE, 1, 8, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.SCROLL_GYFU, null, 0, ZombieBrute2Sprite.class,
			new DeckCardEffects.DismantleEffect()) {
		@Override public int damage(int code) { return upgradeLevel(code) > 0 ? 10 : 8; }
	},

	COME_AT_ME(240, "서바이버", DeckCardType.ATTACK, DeckCardRarity.UNCOMMON, DeckCardTarget.SINGLE, 2, 5, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.DUST,
			new DeckCardEffects.ComeAtMeEffect()) {
		@Override public int damage(int code) { return upgradeLevel(code) > 0 ? 6 : 5; }
		@Override public int strength(int code) { return upgradeLevel(code) > 0 ? 4 : 3; }
	},

	MERCILESS(241, "잭 더 리퍼의 나이프", DeckCardType.ATTACK, DeckCardRarity.UNCOMMON, DeckCardTarget.SINGLE, 2, 14, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.ASSASSINS_BLADE,
			new DeckCardEffects.MercilessEffect()) {
		@Override public int damage(int code) { return upgradeLevel(code) > 0 ? 20 : 14; }
	},

	UPPERCUT(242, "철구", DeckCardType.ATTACK, DeckCardRarity.UNCOMMON, DeckCardTarget.SINGLE, 2, 12, 0, 0, 1, 0, 0, 0, true, ItemSpriteSheet.THROWING_SPEAR,
			new DeckCardEffects.AttackDown(1, 2)) {
		@Override public int vulnerable(int code) { return upgradeLevel(code) > 0 ? 2 : 1; }
	},

	TRAMPLE(243, "톰슨 기관단총", DeckCardType.ATTACK, DeckCardRarity.UNCOMMON, DeckCardTarget.ALL_ENEMIES, 3, 12, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.GLAIVE,
			new DeckCardEffects.TextOnly("이번 턴 동안 사용한 공격 카드 1장당 비용이 1 감소합니다.")) {
		@Override public int damage(int code) { return upgradeLevel(code) > 0 ? 15 : 12; }
		@Override public int cost(int code) { return Math.max(0, 3 - (DeckBuilderRun.currentCombat == null ? 0 : DeckBuilderRun.currentCombat.attackCardsThisTurn)); }
	},

	FEED(244, "크림 스타터", DeckCardType.ATTACK, DeckCardRarity.RARE, DeckCardTarget.SINGLE, 1, 10, 0, 0, 0, 0, 0, keywords(DeckCardKeyword.EXHAUST), true, ItemSpriteSheet.MACE,
			new DeckCardEffects.FeedEffect()) {
		@Override public int damage(int code) { return upgradeLevel(code) > 0 ? 12 : 10; }
	},

	HEMOKINESIS(245, "교통표지판", DeckCardType.ATTACK, DeckCardRarity.RARE, DeckCardTarget.SINGLE, 3, 15, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.GREATSHIELD,
			new DeckCardEffects.HemokinesisEffect()) {
		@Override public int damage(int code) { return upgradeLevel(code) > 0 ? 20 : 15; }
	},

	WISP(246, "에코즈의 알", DeckCardType.SKILL, DeckCardRarity.COMMON, DeckCardTarget.NONE, 0, 0, 0, 0, 0, 0, 0, keywords(DeckCardKeyword.EXHAUST), true, ItemSpriteSheet.BLANDFRUIT,
			new DeckCardEffects.WispEffect()) {
		@Override public boolean hasKeyword(int code, DeckCardKeyword keyword) {
			return keyword == DeckCardKeyword.RETAIN && upgradeLevel(code) > 0 || super.hasKeyword(code, keyword);
		}
	},

	LIFE_SUPPORT(247, "마법의 램프", DeckCardType.SKILL, DeckCardRarity.UNCOMMON, DeckCardTarget.NONE, 1, 0, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.RAM,
			new DeckCardEffects.LifeSupportEffect()),

	FRIENDSHIP(248, "융합된 인간", DeckCardType.POWER, DeckCardRarity.UNCOMMON, DeckCardTarget.NONE, 1, 0, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.SCROLL_GYFU, null, 0, AbominationSprite.class,
			new DeckCardEffects.FriendshipEffect()),

	ENERGIZER(249, "레오네 아바키오", DeckCardType.SKILL, DeckCardRarity.UNCOMMON, DeckCardTarget.NONE, 1, 0, 0, 0, 0, 0, 0, keywords(DeckCardKeyword.EXHAUST), true, ItemSpriteSheet.SCROLL_GYFU, null, 0, PassioneSprite.Ab.class,
			new DeckCardEffects.EnergizerEffect()) {
		@Override public int cost(int code) { return upgradeLevel(code) > 0 ? 0 : 1; }
	},

	CAPTURE(250, "크로스 파이어 허리케인의 함정", DeckCardType.SKILL, DeckCardRarity.UNCOMMON, DeckCardTarget.NONE, 1, 0, 0, 0, 0, 0, 0, 0, true, BurningTrap.class,
			new DeckCardEffects.CaptureEffect()),

	SUBROUTINE(251, "엠포리오 아르니뇨", DeckCardType.POWER, DeckCardRarity.UNCOMMON, DeckCardTarget.NONE, 1, 0, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.SCROLL_GYFU, null, 0, EmporioSprite.class,
			new DeckCardEffects.SubroutineEffect()) {
		@Override public int cost(int code) { return upgradeLevel(code) > 0 ? 0 : 1; }
	},

	OVERCLOCK(252, "백금풀의 씨앗", DeckCardType.SKILL, DeckCardRarity.RARE, DeckCardTarget.NONE, 0, 0, 0, 0, 0, 0, 0, keywords(DeckCardKeyword.EXHAUST), true, ItemSpriteSheet.SEED_STARFLOWER,
			new DeckCardEffects.GainEnergyEffect(4, 6)),

	ORBIT(253, "흡혈마", DeckCardType.POWER, DeckCardRarity.UNCOMMON, DeckCardTarget.NONE, 2, 0, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.SCROLL_GYFU, null, 0, VampireHorseSprite.class,
			new DeckCardEffects.OrbitEffect()) {
		@Override public int cost(int code) { return upgradeLevel(code) > 0 ? 1 : 2; }
	},

	READY_FOR_FIGHT(254, "흡혈귀 병사", DeckCardType.SKILL, DeckCardRarity.UNCOMMON, DeckCardTarget.NONE, 2, 0, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.SCROLL_GYFU, null, 0, VampireSoldier3Sprite.class,
			new DeckCardEffects.ReadyForFightEffect()) {
		@Override public int cost(int code) { return upgradeLevel(code) > 0 ? 1 : 2; }
	},

	HEART_OF_FIRE(255, "브루노 부차라티", DeckCardType.POWER, DeckCardRarity.RARE, DeckCardTarget.NONE, 2, 0, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.SCROLL_GYFU, null, 0, PassioneSprite.Bu.class,
			new DeckCardEffects.HeartOfFireEffect()),

	ROYALTY(256, "하베스트의 장비 DISC", DeckCardType.POWER, DeckCardRarity.RARE, DeckCardTarget.NONE, 1, 0, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.ARTIFACT_ARMBAND,
			new DeckCardEffects.RoyaltyEffect()),

	VOID_FORM(257, "G.E.R의 장비 DISC", DeckCardType.POWER, DeckCardRarity.RARE, DeckCardTarget.NONE, 3, 0, 0, 0, 0, 0, 0, keywords(DeckCardKeyword.TRANSIENT), true, ItemSpriteSheet.ARTIFACT_CAPE,
			new DeckCardEffects.VoidFormEffect()) {
		@Override public boolean hasKeyword(int code, DeckCardKeyword keyword) {
			if (keyword == DeckCardKeyword.TRANSIENT && upgradeLevel(code) > 0) return false;
			return super.hasKeyword(code, keyword);
		}
	},

	STONE_ARMOR(258, "하이웨이 스타", DeckCardType.POWER, DeckCardRarity.UNCOMMON, DeckCardTarget.NONE, 1, 0, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.SCROLL_GYFU, null, 0, BatSprite.class,
			new DeckCardEffects.RegenEffect(4, 6)),

	DECENT_STRATEGY(259, "젠틀리 위프스의 함정", DeckCardType.POWER, DeckCardRarity.COMMON, DeckCardTarget.NONE, 1, 0, 0, 0, 0, 0, 0, 0, true, ChillingTrap.class,
			new DeckCardEffects.DecentStrategyEffect()),

	SPEEDSTER(260, "나란차 길가", DeckCardType.POWER, DeckCardRarity.COMMON, DeckCardTarget.NONE, 2, 0, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.SCROLL_GYFU, null, 0, PassioneSprite.Na.class,
			new DeckCardEffects.SpeedsterEffect()) {
		@Override public boolean hasKeyword(int code, DeckCardKeyword keyword) {
			if (keyword == DeckCardKeyword.VANGUARD && upgradeLevel(code) > 0) return true;
			return super.hasKeyword(code, keyword);
		}
	},

	TRACK(261, "키시베 로한", DeckCardType.POWER, DeckCardRarity.RARE, DeckCardTarget.NONE, 2, 0, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.SCROLL_GYFU, null, 0, RohanSprite.class,
			new DeckCardEffects.TrackEffect()) {
		@Override public int cost(int code) { return DeckCardCode.upgradeLevel(code) > 0 ? 1 : 2; }
	},

	DEMON_FORM(262, "저지먼트의 장비 DISC", DeckCardType.POWER, DeckCardRarity.RARE, DeckCardTarget.NONE, 3, 0, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.ARTIFACT_ROSE1,
			new DeckCardEffects.DemonFormEffect()),

	CORRUPTION(263, "스피드 킹", DeckCardType.POWER, DeckCardRarity.RARE, DeckCardTarget.NONE, 3, 0, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.KINGS,
			new DeckCardEffects.CorruptionEffect()) {
		@Override public int cost(int code) { return DeckCardCode.upgradeLevel(code) > 0 ? 2 : 3; }
	},

	DIZZINESS(264, "혼란", DeckCardType.STATUS, DeckCardRarity.COMMON, DeckCardTarget.NONE, 1, 0, 0, 0, 0, 0, 0, keywords(DeckCardKeyword.TRANSIENT), false, BuffIndicator.VERTIGO) {
		@Override public boolean unplayable(int code) { return true; }
		@Override public int buffIconInt() { return BuffIndicator.VERTIGO; }
	},

	HOLLOW(265, "마비", DeckCardType.STATUS, DeckCardRarity.COMMON, DeckCardTarget.NONE, 1, 0, 0, 0, 0, 0, 0, keywords(DeckCardKeyword.TRANSIENT), false, BuffIndicator.PARALYSIS) {
		@Override public boolean unplayable(int code) { return true; }
		@Override public int buffIconInt() { return BuffIndicator.PARALYSIS; }
	},

	BURN(266, "발화", DeckCardType.STATUS, DeckCardRarity.COMMON, DeckCardTarget.NONE, 1, 0, 0, 0, 0, 0, 0, 0, false, BuffIndicator.FIRE) {
		@Override public boolean unplayable(int code) { return true; }
		@Override public int buffIconInt() { return BuffIndicator.FIRE; }
	},

	INJURY(267, "출혈", DeckCardType.STATUS, DeckCardRarity.COMMON, DeckCardTarget.NONE, 1, 0, 0, 0, 0, 0, 0, 0, false, BuffIndicator.BLEEDING) {
		@Override public boolean unplayable(int code) { return true; }
		@Override public int buffIconInt() { return BuffIndicator.BLEEDING; }
	},

	RUSHED_EXIT(268, "혼란풀의 씨앗", DeckCardType.SKILL, DeckCardRarity.COMMON, DeckCardTarget.NONE, 0, 0, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.SEED_STORMVINE,
			new DeckCardEffects.RushedExitEffect()),

	TURBO(269, "크래프트 워크의 함정", DeckCardType.SKILL, DeckCardRarity.COMMON, DeckCardTarget.NONE, 0, 0, 0, 0, 0, 0, 0, 0, true, StormTrap.class,
			new DeckCardEffects.TurboEffect()),

	OVERCLOCK_BOOST(270, "크로스 파이어 허리케인 스페셜의 함정", DeckCardType.SKILL, DeckCardRarity.UNCOMMON, DeckCardTarget.NONE, 0, 0, 0, 0, 0, 0, 0, 0, true, BlazingTrap.class,
			new DeckCardEffects.OverclockBoostEffect()),

	FORCED_PUSH(271, "메탈리카의 함정", DeckCardType.SKILL, DeckCardRarity.UNCOMMON, DeckCardTarget.NONE, 1, 0, 0, 0, 0, 0, 0, 0, true, GrippingTrap.class,
			new DeckCardEffects.ForcedPushEffect()),

	REAPER_SCYTHE(272, "시저 체펠리", DeckCardType.ATTACK, DeckCardRarity.RARE, DeckCardTarget.SINGLE, 2, 13, 0, 0, 0, 0, 0, keywords(DeckCardKeyword.EXHAUST), true, ItemSpriteSheet.SCROLL_GYFU, null, 0, WillcSprite.class,
			new DeckCardEffects.ReaperScytheEffect()) {
		@Override public int damage(int code) { return 13 + DeckCardCode.auxValue(code); }
	},
	COMPOSURE(273, "에니그마의 종이", DeckCardType.SKILL, DeckCardRarity.UNCOMMON, DeckCardTarget.NONE, 1, 0, 0, 1, 0, 0, 0, 0, true, ItemSpriteSheet.HOLSTER,
			new DeckCardEffects.ComposureEffect()) {
		@Override public int draw(int code) { return upgradeLevel(code) > 0 ? 2 : 1; }
	},
	BOWGUN_AMMO(274, "보우건 전용 탄환", DeckCardType.ATTACK, DeckCardRarity.COMMON, DeckCardTarget.SINGLE, 1, 16, 0, 0, 0, 0, 0, 0, false, ItemSpriteSheet.DART,
			new DeckCardEffects.Damage()) {
		@Override public int damage(int code) { return upgradeLevel(code) > 0 ? 20 : 16; }
	},
	IRON_BOWGUN(275, "아이언 보우건", DeckCardType.SKILL, DeckCardRarity.RARE, DeckCardTarget.NONE, 0, 0, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.CROSSBOW,
			new DeckCardEffects.IronBowgunEffect()),
	FLESH_TRICK(276, "녹색 아기의 장비 DISC", DeckCardType.POWER, DeckCardRarity.UNCOMMON, DeckCardTarget.NONE, 2, 0, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.ARTIFACT_GREAVES,
			new DeckCardEffects.FleshTrickEffect()),
	DUAL_WIELD(277, "에르메스 코스텔로", DeckCardType.POWER, DeckCardRarity.UNCOMMON, DeckCardTarget.NONE, 1, 0, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.SCROLL_GYFU, null, 0, So2Sprite.class,
			new DeckCardEffects.DualWieldEffect()),
	ANTIAIRCRAFT_CANNON(278, "스타 플라티나 더 월드의 주먹", DeckCardType.ATTACK, DeckCardRarity.RARE, DeckCardTarget.NONE, 2, 0, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.SPWORLD,
			new DeckCardEffects.AntiaircraftCannonEffect()),
	GIGA_DRILL_BREAK(279, "중기관포", DeckCardType.ATTACK, DeckCardRarity.RARE, DeckCardTarget.SINGLE, -1, 8, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.STURO,
			new DeckCardEffects.GigaDrillBreakEffect(8, 10)) {
		@Override public int damage(int code) { return upgradeLevel(code) > 0 ? 10 : 8; }
	},
	HANGING(280, "볼 브레이커", DeckCardType.ATTACK, DeckCardRarity.RARE, DeckCardTarget.SINGLE, 1, 10, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.BB,
			new DeckCardEffects.HangingEffect(10, 13)) {
		@Override public int damage(int code) { return upgradeLevel(code) > 0 ? 13 : 10; }
	},

	POISON_STAB(281, "맹독 뱀", DeckCardType.ATTACK, DeckCardRarity.COMMON, DeckCardTarget.SINGLE, 1, 6, 0, 0, 0, 0, 0, 0, true, Talent.SHARED_ENCHANTMENT, HeroClass.HUNTRESS, 0,
			new DeckCardEffects.Damage(), new DeckCardEffects.PersistentDamageEffect(3, 4)) {
		@Override public int damage(int code) { return upgradeLevel(code) > 0 ? 8 : 6; }
	},

	DEADLY_POISON(282, "생명 융합", DeckCardType.SKILL, DeckCardRarity.COMMON, DeckCardTarget.SINGLE, 1, 0, 0, 0, 0, 0, 0, 0, true, Talent.DURABLE_TIPS, HeroClass.HUNTRESS, 0,
			new DeckCardEffects.PersistentDamageEffect(5, 7)),

	SNAKE_BITE(283, "생명의 의지", DeckCardType.SKILL, DeckCardRarity.COMMON, DeckCardTarget.SINGLE, 2, 0, 0, 0, 0, 0, 0, keywords(DeckCardKeyword.RETAIN), true, Talent.REJUVENATING_STEPS, HeroClass.HUNTRESS, 0,
			new DeckCardEffects.PersistentDamageEffect(7, 10)),

	OUTBREAK(284, "냉철함", DeckCardType.POWER, DeckCardRarity.UNCOMMON, DeckCardTarget.NONE, 1, 0, 0, 0, 0, 0, 0, 0, true, Talent.SURVIVALISTS_INTUITION, HeroClass.HUNTRESS, 0,
			new DeckCardEffects.OutbreakEffect()),

	NOXIOUS_GAS(285, "생명력 주입", DeckCardType.POWER, DeckCardRarity.UNCOMMON, DeckCardTarget.NONE, 1, 0, 0, 0, 0, 0, 0, 0, true, Talent.DURABLE_PROJECTILES, HeroClass.HUNTRESS, 0,
			new DeckCardEffects.NoxiousGasEffect()),

	RISING_POISON(286, "한계 초월", DeckCardType.SKILL, DeckCardRarity.UNCOMMON, DeckCardTarget.SINGLE, 1, 0, 0, 0, 0, 0, 0, 0, true, Talent.FARSIGHT, HeroClass.HUNTRESS, 0,
			new DeckCardEffects.RisingPoisonEffect()),

	MIRAGE(287, "네아폴리스의 식사법", DeckCardType.SKILL, DeckCardRarity.UNCOMMON, DeckCardTarget.NONE, 1, 0, 0, 0, 0, 0, 0, keywords(DeckCardKeyword.EXHAUST), true, Talent.INVIGORATING_MEAL, HeroClass.HUNTRESS, 0,
			new DeckCardEffects.MirageEffect()) {
		@Override public int cost(int code) { return upgradeLevel(code) > 0 ? 0 : 1; }
	},

	CATALYST(288, "판나코타 푸고", DeckCardType.POWER, DeckCardRarity.RARE, DeckCardTarget.NONE, 1, 0, 0, 0, 0, 0, 0, 0, true, ItemSpriteSheet.SCROLL_GYFU, HeroClass.HUNTRESS, 0, FugoSprite.class,
			new DeckCardEffects.CatalystEffect()),

	CORROSIVE_WAVE(289, "끝이 없는 끝", DeckCardType.SKILL, DeckCardRarity.RARE, DeckCardTarget.NONE, 1, 0, 0, 0, 0, 0, 0, 0, true, Talent.WILD_MOMENTUM, HeroClass.HUNTRESS, 0,
			new DeckCardEffects.CorrosiveWaveEffect()),

	PIERCING_WAIL(290, "리틀 피트의 함정", DeckCardType.SKILL, DeckCardRarity.COMMON, DeckCardTarget.ALL_ENEMIES, 1, 0, 0, 0, 0, 0, 0, keywords(DeckCardKeyword.EXHAUST), true, WeakeningTrap.class,
			new DeckCardEffects.AllEnemyTurnStrengthLossEffect(6, 8)),

	UPGRADE_TEXT_VIEWER(291, "강화 텍스트 뷰어", DeckCardType.SKILL, DeckCardRarity.COMMON, DeckCardTarget.NONE, 0, 0, 0, 0, 0, 0, 0, 0, false, ItemSpriteSheet.SCROLL_HOLDER,
			new DeckCardEffects.UpgradeTextViewerEffect()),

	PREDICTION(292, "파문의 보호막", DeckCardType.SKILL, DeckCardRarity.COMMON, DeckCardTarget.NONE, 0, 0, 0, 0, 0, 0, 0, 0, false, ItemSpriteSheet.SEAL, null, 0,
			new DeckCardEffects.TurnDexterity(2, 3))
	;

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
	public final Class<? extends Trap> trapIcon;
	public final CharSpriteInfo charSprite;
	public Class<? extends CharSprite> spriteClass = null;
	private final DeckCardEffect[] specialEffects;

	public static class CharSpriteInfo {
		public final String tex;
		public final int w, h, frame;
		public final float scale;

		public CharSpriteInfo(String tex, int w, int h) {
			this(tex, w, h, 0, 1.6f);
		}

		public CharSpriteInfo(String tex, int w, int h, int frame, float scale) {
			this.tex = tex; this.w = w; this.h = h; this.frame = frame; this.scale = scale;
		}

		public static final CharSpriteInfo RAT   = new CharSpriteInfo(Assets.Sprites.RAT,  16, 15);
		public static final CharSpriteInfo CIVIL  = new CharSpriteInfo(Assets.Sprites.CIVIL, 12, 17, 0, 1.1f);
	}

	DeckCard(int id, String title, DeckCardType type, DeckCardRarity rarity, DeckCardTarget target, int cost, int damage, int block, int draw, int vulnerable,
			 int strength, int handPenalty, int baseKeywords, boolean reward, int icon) {
		this(id, title, type, rarity, target, cost, damage, block, draw, vulnerable, strength, handPenalty, baseKeywords, reward, icon, null, null, 0, (Talent)null, null);
	}

	DeckCard(int id, String title, DeckCardType type, DeckCardRarity rarity, DeckCardTarget target, int cost, int damage, int block, int draw, int vulnerable,
			 int strength, int handPenalty, int baseKeywords, boolean reward, int icon, DeckCardEffect... specialEffects) {
		this(id, title, type, rarity, target, cost, damage, block, draw, vulnerable, strength, handPenalty, baseKeywords, reward, icon, null, null, 0, null, null, specialEffects);
	}

	DeckCard(int id, String title, DeckCardType type, DeckCardRarity rarity, DeckCardTarget target, int cost, int damage, int block, int draw, int vulnerable,
			 int strength, int handPenalty, int baseKeywords, boolean reward, Talent talentIcon) {
		this(id, title, type, rarity, target, cost, damage, block, draw, vulnerable, strength, handPenalty, baseKeywords, reward, 0, null, null, 0, talentIcon, null);
	}

	DeckCard(int id, String title, DeckCardType type, DeckCardRarity rarity, DeckCardTarget target, int cost, int damage, int block, int draw, int vulnerable,
			 int strength, int handPenalty, int baseKeywords, boolean reward, Talent talentIcon, DeckCardEffect... specialEffects) {
		this(id, title, type, rarity, target, cost, damage, block, draw, vulnerable, strength, handPenalty, baseKeywords, reward, 0, null, null, 0, talentIcon, null, specialEffects);
	}

	DeckCard(int id, String title, DeckCardType type, DeckCardRarity rarity, DeckCardTarget target, int cost, int damage, int block, int draw, int vulnerable,
			 int strength, int handPenalty, int baseKeywords, boolean reward, Talent talentIcon, CharSpriteInfo charSprite) {
		this(id, title, type, rarity, target, cost, damage, block, draw, vulnerable, strength, handPenalty, baseKeywords, reward, 0, null, null, 0, talentIcon, charSprite);
	}

	DeckCard(int id, String title, DeckCardType type, DeckCardRarity rarity, DeckCardTarget target, int cost, int damage, int block, int draw, int vulnerable,
			 int strength, int handPenalty, int baseKeywords, boolean reward, Talent talentIcon, CharSpriteInfo charSprite, DeckCardEffect... specialEffects) {
		this(id, title, type, rarity, target, cost, damage, block, draw, vulnerable, strength, handPenalty, baseKeywords, reward, 0, null, null, 0, talentIcon, charSprite, specialEffects);
	}

	DeckCard(int id, String title, DeckCardType type, DeckCardRarity rarity, DeckCardTarget target, int cost, int damage, int block, int draw, int vulnerable,
			 int strength, int handPenalty, int baseKeywords, boolean reward, int icon, HeroClass deckClass, int shivs) {
		this(id, title, type, rarity, target, cost, damage, block, draw, vulnerable, strength, handPenalty, baseKeywords, reward, icon, null, deckClass, shivs, (Talent)null, null);
	}

	DeckCard(int id, String title, DeckCardType type, DeckCardRarity rarity, DeckCardTarget target, int cost, int damage, int block, int draw, int vulnerable,
			 int strength, int handPenalty, int baseKeywords, boolean reward, int icon, HeroClass deckClass, int shivs, DeckCardEffect... specialEffects) {
		this(id, title, type, rarity, target, cost, damage, block, draw, vulnerable, strength, handPenalty, baseKeywords, reward, icon, null, deckClass, shivs, null, null, specialEffects);
	}

	DeckCard(int id, String title, DeckCardType type, DeckCardRarity rarity, DeckCardTarget target, int cost, int damage, int block, int draw, int vulnerable,
			 int strength, int handPenalty, int baseKeywords, boolean reward, int icon, HeroClass deckClass, int shivs, Class<? extends CharSprite> spriteClass, DeckCardEffect... specialEffects) {
		this(id, title, type, rarity, target, cost, damage, block, draw, vulnerable, strength, handPenalty, baseKeywords, reward, icon, null, deckClass, shivs, null, null, specialEffects);
		this.spriteClass = spriteClass;
	}

	DeckCard(int id, String title, DeckCardType type, DeckCardRarity rarity, DeckCardTarget target, int cost, int damage, int block, int draw, int vulnerable,
			 int strength, int handPenalty, int baseKeywords, boolean reward, Talent talentIcon, HeroClass deckClass, int shivs) {
		this(id, title, type, rarity, target, cost, damage, block, draw, vulnerable, strength, handPenalty, baseKeywords, reward, 0, null, deckClass, shivs, talentIcon, null);
	}

	DeckCard(int id, String title, DeckCardType type, DeckCardRarity rarity, DeckCardTarget target, int cost, int damage, int block, int draw, int vulnerable,
			 int strength, int handPenalty, int baseKeywords, boolean reward, Talent talentIcon, HeroClass deckClass, int shivs, DeckCardEffect... specialEffects) {
		this(id, title, type, rarity, target, cost, damage, block, draw, vulnerable, strength, handPenalty, baseKeywords, reward, 0, null, deckClass, shivs, talentIcon, null, specialEffects);
	}

	DeckCard(int id, String title, DeckCardType type, DeckCardRarity rarity, DeckCardTarget target, int cost, int damage, int block, int draw, int vulnerable,
			 int strength, int handPenalty, int baseKeywords, boolean reward, int icon, HeroClass deckClass, int shivs, Talent talentIcon) {
		this(id, title, type, rarity, target, cost, damage, block, draw, vulnerable, strength, handPenalty, baseKeywords, reward, icon, null, deckClass, shivs, talentIcon, null, new DeckCardEffect[0]);
	}

	// charSprite overloads
	DeckCard(int id, String title, DeckCardType type, DeckCardRarity rarity, DeckCardTarget target, int cost, int damage, int block, int draw, int vulnerable,
			 int strength, int handPenalty, int baseKeywords, boolean reward, CharSpriteInfo charSprite) {
		this(id, title, type, rarity, target, cost, damage, block, draw, vulnerable, strength, handPenalty, baseKeywords, reward, 0, null, null, 0, null, charSprite);
	}

	DeckCard(int id, String title, DeckCardType type, DeckCardRarity rarity, DeckCardTarget target, int cost, int damage, int block, int draw, int vulnerable,
			 int strength, int handPenalty, int baseKeywords, boolean reward, CharSpriteInfo charSprite, DeckCardEffect... specialEffects) {
		this(id, title, type, rarity, target, cost, damage, block, draw, vulnerable, strength, handPenalty, baseKeywords, reward, 0, null, null, 0, null, charSprite, specialEffects);
	}

	DeckCard(int id, String title, DeckCardType type, DeckCardRarity rarity, DeckCardTarget target, int cost, int damage, int block, int draw, int vulnerable,
			 int strength, int handPenalty, int baseKeywords, boolean reward, int icon, CharSpriteInfo charSprite, DeckCardEffect... specialEffects) {
		this(id, title, type, rarity, target, cost, damage, block, draw, vulnerable, strength, handPenalty, baseKeywords, reward, icon, null, null, 0, null, charSprite, specialEffects);
	}

	// trapIcon overloads
	DeckCard(int id, String title, DeckCardType type, DeckCardRarity rarity, DeckCardTarget target, int cost, int damage, int block, int draw, int vulnerable,
			 int strength, int handPenalty, int baseKeywords, boolean reward, Class<? extends Trap> trapIcon) {
		this(id, title, type, rarity, target, cost, damage, block, draw, vulnerable, strength, handPenalty, baseKeywords, reward, 0, trapIcon, null, 0, (Talent)null, null);
	}

	DeckCard(int id, String title, DeckCardType type, DeckCardRarity rarity, DeckCardTarget target, int cost, int damage, int block, int draw, int vulnerable,
			 int strength, int handPenalty, int baseKeywords, boolean reward, Class<? extends Trap> trapIcon, DeckCardEffect... specialEffects) {
		this(id, title, type, rarity, target, cost, damage, block, draw, vulnerable, strength, handPenalty, baseKeywords, reward, 0, trapIcon, null, 0, null, null, specialEffects);
	}

	DeckCard(int id, String title, DeckCardType type, DeckCardRarity rarity, DeckCardTarget target, int cost, int damage, int block, int draw, int vulnerable,
			 int strength, int handPenalty, int baseKeywords, boolean reward, Class<? extends Trap> trapIcon, HeroClass deckClass, int shivs) {
		this(id, title, type, rarity, target, cost, damage, block, draw, vulnerable, strength, handPenalty, baseKeywords, reward, 0, trapIcon, deckClass, shivs, (Talent)null, null);
	}

	// master constructor
	DeckCard(int id, String title, DeckCardType type, DeckCardRarity rarity, DeckCardTarget target, int cost, int damage, int block, int draw, int vulnerable,
			 int strength, int handPenalty, int baseKeywords, boolean reward, int icon, Class<? extends Trap> trapIcon, HeroClass deckClass, int shivs, Talent talentIcon, CharSpriteInfo charSprite, DeckCardEffect... specialEffects) {
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
		this.trapIcon = trapIcon;
		this.deckClass = deckClass;
		this.shivs = shivs;
		this.talentIcon = talentIcon;
		this.charSprite = charSprite;
		this.specialEffects = specialEffects == null ? new DeckCardEffect[0] : specialEffects;
	}

	public int buffIconInt() { return -1; }

	public int code() {
		return DeckCardCode.baseCode(id);
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
		if (DeckCardPool.isStatus(this) || DeckCardPool.isCurse(this)) {
			return code;
		}
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
			else if (e instanceof DeckCardEffects.Block || e instanceof DeckCardEffects.ParticleWallEffect) hasBlock = true;
			else if (e instanceof DeckCardEffects.Vulnerable) hasVulnerable = true;
			else if (e instanceof DeckCardEffects.Draw) hasDraw = true;
			else if (e instanceof DeckCardEffects.Strength) hasStrength = true;
			else if (e instanceof DeckCardEffects.AddShivs) hasShivs = true;
			if (e instanceof DeckCardEffects.ComeAtMeEffect) {
				hasDamage = true;
				hasStrength = true;
			}
		}
		if (damage(code) > 0 && this != STAFF && !DeckWandCards.isWand(code) && !hasDamage) effects.add(new DeckCardEffects.Damage());
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
		if (type == DeckCardType.QUEST) return true;
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

	public static boolean isPrime(int n) {
		if (n < 2) return false;
		if (n == 2) return true;
		if (n % 2 == 0) return false;
		for (int i = 3; i * i <= n; i += 2) {
			if (n % i == 0) return false;
		}
		return true;
	}

	private static int keywords(DeckCardKeyword... keywords) {
		int bits = 0;
		for (DeckCardKeyword keyword : keywords) {
			bits |= keyword.bit;
		}
		return bits;
	}
}
