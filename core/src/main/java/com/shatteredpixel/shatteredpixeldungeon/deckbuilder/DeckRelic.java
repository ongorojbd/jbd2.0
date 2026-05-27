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

import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

import java.util.ArrayList;

public enum DeckRelic {

	NEW_LEAF("새로운 잎", "획득 시, 덱의 카드 1장이 무작위로 변환됩니다.", DeckRelicRarity.COMMON, DeckRelicType.STARTER, ItemSpriteSheet.SEED_STARFLOWER) {
		@Override public void onAcquire() {
			if (DeckBuilderRun.deck.isEmpty()) return;
			int idx = Random.Int(DeckBuilderRun.deck.size());
			int code = DeckBuilderRun.deck.get(idx);
			DeckCard current = DeckCard.byCode(code);
			DeckCard[] pool = DeckCard.rewardPool(DeckBuilderRun.heroClass(), false, false);
			ArrayList<DeckCard> filtered = new ArrayList<>();
			for (DeckCard c : pool) { if (c != current) filtered.add(c); }
			if (filtered.isEmpty()) return;
			DeckBuilderRun.deck.set(idx, filtered.get(Random.Int(filtered.size())).code());
		}
	},
	SMALL_CAPSULE("소형 캡슐", "획득 시, 무작위 유물을 1개 얻습니다.", DeckRelicRarity.COMMON, DeckRelicType.STARTER, ItemSpriteSheet.POTION_HOLDER) {
		@Override public void onAcquire() {
			DeckRelic relic = randomAvailable((DeckRelic) null);
			if (relic != null) DeckBuilderRun.addRelic(relic);
		}
	},
	STARTER_NUTRITIOUS_OYSTER("영양만점 굴", "최대 체력이 11 증가합니다.", DeckRelicRarity.COMMON, DeckRelicType.STARTER, ItemSpriteSheet.MEAT_PIE) {
		@Override public void onAcquire() { DeckBuilderRun.playerHT += 11; DeckBuilderRun.playerHP += 11; }
	},
	STONE_HUMIDIFIER("석재 가습기", "휴식 장소에서 휴식을 취할 때마다, 최대 체력이 5 증가합니다.", DeckRelicRarity.COMMON, DeckRelicType.STARTER, ItemSpriteSheet.ARCANE_RESIN),
	LEAD_PAPERWEIGHT("납 문진", "중립 카드 2장 중 1장을 선택해 덱에 추가합니다.", DeckRelicRarity.COMMON, DeckRelicType.STARTER, ItemSpriteSheet.THROWING_STONE) {
		@Override public void onAcquire() { DeckBuilderRun.pendingNeutralDiscover = true; }
	},
	LOST_COFFER("잃어버린 궤짝", "획득 시, 카드 보상을 1번 획득하고, 무작위 포션을 1개 생성합니다.", DeckRelicRarity.UNCOMMON, DeckRelicType.STARTER, ItemSpriteSheet.CRYSTAL_CHEST) {
		@Override public void onAcquire() {
			DeckBuilderRun.pendingCardReward = true;
			DeckPotion[] potions = DeckPotion.values();
			DeckBuilderRun.addPotion(potions[Random.Int(potions.length)]);
		}
	},
	PRECISE_SCISSORS("정밀한 가위", "덱에서 카드를 1장 제거합니다.", DeckRelicRarity.UNCOMMON, DeckRelicType.STARTER, ItemSpriteSheet.GOLDEN_KEY) {
		@Override public void onAcquire() { DeckBuilderRun.pendingCardRemove = true; }
	},
	BOOMING_CONCH("콰광 소라", "강적 전투 시작 시, 카드를 추가로 2장 뽑고 에너지를 1 얻습니다.", DeckRelicRarity.UNCOMMON, DeckRelicType.STARTER, ItemSpriteSheet.BEACON),
	LAVA_ROCK("화산암", "1막의 보스가 보상으로 유물을 2개 제공합니다.", DeckRelicRarity.UNCOMMON, DeckRelicType.STARTER, ItemSpriteSheet.STONE_CLAIRVOYANCE),
	GOLDEN_PEARL("황금 진주", "획득 시, 골드를 150 얻습니다.", DeckRelicRarity.UNCOMMON, DeckRelicType.STARTER, ItemSpriteSheet.GOLD) {
		@Override public void onAcquire() { DeckBuilderRun.gold += 150; }
	},
	STARTER_ARCANE_SCROLL("비전 두루마리", "획득 시, 덱에 무작위 희귀 카드를 1장 추가합니다.", DeckRelicRarity.RARE, DeckRelicType.STARTER, ItemSpriteSheet.SCROLL_CATALYST) {
		@Override public void onAcquire() {
			DeckCard card = randomCard(DeckCardRarity.RARE);
			if (card != null) DeckBuilderRun.addCard(card);
		}
	},
	POMANDER("포맨더", "덱의 카드를 1장 강화합니다.", DeckRelicRarity.RARE, DeckRelicType.STARTER, ItemSpriteSheet.ARTIFACT_TALISMAN) {
		@Override public void onAcquire() { DeckBuilderRun.pendingCardUpgrade = true; }
	},
	NEOWS_TALISMAN("니오우의 호부", "획득 시, STRIKE 1장과 GUARD 1장을 강화합니다.", DeckRelicRarity.COMMON, DeckRelicType.STARTER, ItemSpriteSheet.ANKH) {
		@Override public void onAcquire() {
			upgradeFirstInDeck(DeckCard.STRIKE);
			upgradeFirstInDeck(DeckCard.GUARD);
		}
	},
	PHIAL_HOLSTER("약병 홀스터", "획득 시, 무작위 포션 3개를 획득합니다.", DeckRelicRarity.COMMON, DeckRelicType.STARTER, ItemSpriteSheet.POTION_HOLDER) {
		@Override public void onAcquire() {
			DeckPotion[] potions = DeckPotion.values();
			for (int i = 0; i < 3; i++) DeckBuilderRun.addPotion(potions[Random.Int(potions.length)]);
		}
	},
	KALEIDOSCOPE("만화경", "획득 시, 다른 캐릭터의 카드 보상을 2번 얻습니다.", DeckRelicRarity.UNCOMMON, DeckRelicType.STARTER, ItemSpriteSheet.ARCANE_RESIN) {
		@Override public void onAcquire() { DeckBuilderRun.pendingOtherClassCardReward = 2; }
	},
	FISHING_ROD("낚싯대", "일반 전투 3번마다 덱의 무작위 카드 1장이 강화됩니다.", DeckRelicRarity.UNCOMMON, DeckRelicType.STARTER, ItemSpriteSheet.SPIRIT_BOW),

	LEAFY_POULTICE("나뭇잎 습포", "획득 시, STRIKE 1장과 GUARD 1장이 무작위로 변환됩니다. 최대 체력을 12 잃습니다.", DeckRelicRarity.COMMON, DeckRelicType.PENALTY_STARTER, ItemSpriteSheet.ELIXIR_FEATHER) {
		@Override public void onAcquire() {
			transformFirstInDeck(DeckCard.STRIKE);
			transformFirstInDeck(DeckCard.GUARD);
			DeckBuilderRun.playerHT = Math.max(1, DeckBuilderRun.playerHT - 12);
			DeckBuilderRun.playerHP = Math.min(DeckBuilderRun.playerHP, DeckBuilderRun.playerHT);
		}
	},
	STARTER_LARGE_CAPSULE("대형 캡슐", "획득 시, 무작위 유물을 2개 얻습니다. STRIKE 1장과 GUARD 1장을 덱에 추가합니다.", DeckRelicRarity.UNCOMMON, DeckRelicType.PENALTY_STARTER, ItemSpriteSheet.ARTIFACT_HOLDER) {
		@Override public void onAcquire() {
			for (int i = 0; i < 2; i++) {
				DeckRelic relic = randomAvailable((DeckRelic) null);
				if (relic != null) DeckBuilderRun.addRelic(relic);
			}
			DeckBuilderRun.addCard(DeckCard.STRIKE);
			DeckBuilderRun.addCard(DeckCard.GUARD);
		}
	},
	HEFTY_TABLET("묵직한 서판", "획득 시, 덱에 무작위 희귀 카드 3장 중 1장을 선택해 추가하고, 점액투성이를 1장 추가합니다.", DeckRelicRarity.UNCOMMON, DeckRelicType.PENALTY_STARTER, ItemSpriteSheet.SCROLL_HOLDER) {
		@Override public void onAcquire() {
			DeckBuilderRun.addCard(DeckCard.SLIMY);
			DeckBuilderRun.pendingRareCardChoice++;
		}
	},
	PRECARIOUS_SHEARS("불안정한 가위", "획득 시, 덱에서 카드를 2장 제거하고 피해를 16 받습니다.", DeckRelicRarity.RARE, DeckRelicType.PENALTY_STARTER, ItemSpriteSheet.OBLIVION_SHARD) {
		@Override public void onAcquire() {
			DeckBuilderRun.playerHP = Math.max(1, DeckBuilderRun.playerHP - 16);
			DeckBuilderRun.pendingCardRemoveCount += 2;
		}
	},
	SILVER_CRUCIBLE("은 도가니", "처음 3번의 카드 보상이 강화된 상태로 지급됩니다. 처음으로 여는 보물 상자가 비어 있습니다.", DeckRelicRarity.RARE, DeckRelicType.PENALTY_STARTER, ItemSpriteSheet.ARTIFACT_CHALICE3) {
		@Override public void onAcquire() {
			DeckBuilderRun.upgradedCardRewardCount += 3;
			DeckBuilderRun.firstTreasureEmpty = true;
		}
	},
	CURSED_PEARL("저주받은 진주", "점액투성이를 1장 받습니다. 골드를 333 획득합니다.", DeckRelicRarity.COMMON, DeckRelicType.PENALTY_STARTER, ItemSpriteSheet.CROWN) {
		@Override public void onAcquire() {
			DeckBuilderRun.addCard(DeckCard.SLIMY);
			DeckBuilderRun.gold += 333;
		}
	},
	NEOWS_BONES("니오우의 뼈", "획득 시, 무작위 시작 유물 2개를 획득합니다. 덱에 점액투성이를 1장 추가합니다.", DeckRelicRarity.RARE, DeckRelicType.PENALTY_STARTER, ItemSpriteSheet.ARTIFACT_TUSK4) {
		@Override public void onAcquire() {
			ArrayList<Integer> seen = new ArrayList<>();
			for (int i = 0; i < 2; i++) {
				DeckRelic relic = randomStarterAvailable(seen);
				if (relic != null) {
					seen.add(relic.ordinal());
					DeckBuilderRun.addRelic(relic);
				}
			}
			DeckBuilderRun.addCard(DeckCard.SLIMY);
		}
	},

	NUTRITIOUS_OYSTER("영양만점 굴", "최대 체력이 11 증가합니다.", DeckRelicRarity.COMMON, DeckRelicType.COMMON, ItemSpriteSheet.MEAT_PIE) {
		@Override
		public void onAcquire() {
			DeckBuilderRun.playerHT += 11;
			DeckBuilderRun.playerHP += 11;
		}
	},
	BAG_OF_MARBLES("구슬 주머니", "매 전투 시작 시, 모든 적에게 피해 증폭을 1 부여합니다.", DeckRelicRarity.COMMON, DeckRelicType.COMMON, ItemSpriteSheet.THROWING_STONE),
	VAJRA("금강저", "매 전투 시작 시, 공격력을 1 얻습니다.", DeckRelicRarity.COMMON, DeckRelicType.COMMON, ItemSpriteSheet.OBLIVION_SHARD),
	ANCHOR("닻", "매 전투 시작 시 보호막을 10 얻습니다.", DeckRelicRarity.COMMON, DeckRelicType.COMMON, ItemSpriteSheet.ARTIFACT_HOURGLASS),
	STRAWBERRY("딸기", "획득 시, 최대 체력이 7 증가합니다.", DeckRelicRarity.COMMON, DeckRelicType.COMMON, ItemSpriteSheet.SEED_STARFLOWER) {
		@Override public void onAcquire() { DeckBuilderRun.playerHT += 7; DeckBuilderRun.playerHP += 7; }
	},
	LANTERN("랜턴", "매 전투 시작 시, 추가로 1 에너지를 얻습니다.", DeckRelicRarity.COMMON, DeckRelicType.COMMON, ItemSpriteSheet.STONE_CLAIRVOYANCE),
	GORGET("목 보호대", "매 전투 시작 시, 재생을 4 얻습니다.", DeckRelicRarity.COMMON, DeckRelicType.COMMON, ItemSpriteSheet.ARMOR_CLOTH),
	GREMLIN_HORN("그렘린 뿔", "적이 죽을 때마다, 1 에너지를 얻고 카드를 1장 뽑습니다.", DeckRelicRarity.UNCOMMON, DeckRelicType.UNCOMMON, ItemSpriteSheet.ARTIFACT_TUSK4),
	MINIATURE_CANNON("미니어처 대포", "강화된 공격 카드의 피해량이 3 증가합니다.", DeckRelicRarity.UNCOMMON, DeckRelicType.UNCOMMON, ItemSpriteSheet.RUNIC_BLADE),
	PEAR("배", "획득 시, 최대 체력이 10 증가합니다.", DeckRelicRarity.UNCOMMON, DeckRelicType.UNCOMMON, ItemSpriteSheet.MEAT_PIE) {
		@Override public void onAcquire() { DeckBuilderRun.playerHT += 10; DeckBuilderRun.playerHP += 10; }
	},
	PLANISPHERE("별자리판", "조우(EVENT) 방에 진입할 때마다, 체력을 5 회복합니다.", DeckRelicRarity.UNCOMMON, DeckRelicType.UNCOMMON, ItemSpriteSheet.STONE_CLAIRVOYANCE),
	KUSARIGAMA("사슬낫", "한 턴에 공격 카드를 3장 사용할 때마다, 무작위 적에게 피해를 6 줍니다.", DeckRelicRarity.UNCOMMON, DeckRelicType.UNCOMMON, ItemSpriteSheet.GOLDEN_KEY),
	TUNING_FORK("소리굽쇠", "보조 카드를 10장 사용할 때마다, 보호막을 7 얻습니다.", DeckRelicRarity.UNCOMMON, DeckRelicType.UNCOMMON, ItemSpriteSheet.OBLIVION_SHARD),

	ARCANE_SCROLL("비전 두루마리", "획득 시 덱에 무작위 희귀 카드 1장을 추가합니다.", DeckRelicRarity.UNCOMMON, DeckRelicType.UNCOMMON, ItemSpriteSheet.SCROLL_CATALYST) {
		@Override
		public void onAcquire() {
			DeckCard card = randomCard(DeckCardRarity.RARE);
			if (card != null) {
				DeckBuilderRun.addCard(card);
			}
		}
	},
	HORN_CLEAT("소뿔모양 걸이", "두 번째 턴 시작 시, 보호막을 14 얻습니다.", DeckRelicRarity.UNCOMMON, DeckRelicType.UNCOMMON, ItemSpriteSheet.ARTIFACT_HOURGLASS),

	LARGE_CAPSULE("대형 캡슐", "획득 시 무작위 유물 2개를 얻습니다. 타격 1장과 수비 1장을 덱에 추가합니다.", DeckRelicRarity.RARE, DeckRelicType.RARE, ItemSpriteSheet.POTION_HOLDER) {
		@Override
		public void onAcquire() {
			DeckBuilderRun.addCard(randomCard(DeckCardRarity.COMMON, DeckCardType.ATTACK));
			DeckBuilderRun.addCard(randomCard(DeckCardRarity.COMMON, DeckCardType.SKILL));
			for (int i = 0; i < 2; i++) {
				DeckRelic relic = randomAvailable(this);
				if (relic != null) {
					DeckBuilderRun.addRelic(relic);
				}
			}
		}
	},
	BLACK_STAR("검은 별", "승리할 때 유물을 하나 더 선택합니다.", DeckRelicRarity.RARE, DeckRelicType.RARE, ItemSpriteSheet.ARTIFACT_TUSK4),
	GAME_PIECE("게임용 말", "파워 카드를 사용할 때마다, 카드를 1장 뽑습니다.", DeckRelicRarity.RARE, DeckRelicType.RARE, ItemSpriteSheet.ANKH),
	MEAT_ON_THE_BONE("고깃덩어리", "매 전투 종료 시 남은 체력이 50% 이하라면, 체력을 12 회복합니다.", DeckRelicRarity.RARE, DeckRelicType.RARE, ItemSpriteSheet.ARTIFACT_CHALICE3),
	BEATING_REMNANT("고동치는 잔여물", "내가 한 턴에 잃는 체력이 20을 넘을 수 없습니다.", DeckRelicRarity.RARE, DeckRelicType.RARE, ItemSpriteSheet.ARCANE_RESIN),
	RAZOR_TOOTH("날카로운 이빨", "공격이나 보조 카드를 사용할 때마다, 그 카드를 남은 전투 동안 강화합니다.", DeckRelicRarity.RARE, DeckRelicType.RARE, ItemSpriteSheet.RUNIC_BLADE),
	OLD_COIN("낡은 동전", "획득 시, 골드를 300 얻습니다.", DeckRelicRarity.RARE, DeckRelicType.RARE, ItemSpriteSheet.GOLD) {
		@Override public void onAcquire() { DeckBuilderRun.gold += 300; }
	},
	MOLTEN_EGG("녹아내린 알", "공격 카드를 덱에 추가할 때마다, 그 카드를 강화합니다.", DeckRelicRarity.RARE, DeckRelicType.RARE, ItemSpriteSheet.POTION_HOLDER),

	CAULDRON("가마솥", "획득 시, 무작위 포션을 3개 생성합니다.", DeckRelicRarity.COMMON, DeckRelicType.SHOP, ItemSpriteSheet.ARTIFACT_CHALICE3) {
		@Override public void onAcquire() {
			DeckPotion[] potions = DeckPotion.values();
			for (int i = 0; i < 3; i++) DeckBuilderRun.addPotion(potions[Random.Int(potions.length)]);
		}
	},
	TOOLBOX("공구함", "매 전투 시작 시, 무작위 중립 카드 3장 중 1장을 선택해 손으로 가져옵니다.", DeckRelicRarity.UNCOMMON, DeckRelicType.SHOP, ItemSpriteSheet.ARTIFACT_HOLDER),
	RINGING_TRIANGLE("공명하는 트라이앵글", "매 전투마다 첫 턴에 손에 있는 카드를 보존합니다.", DeckRelicRarity.UNCOMMON, DeckRelicType.SHOP, ItemSpriteSheet.ARTIFACT_TALISMAN),
	DOLLYS_MIRROR("돌리의 거울", "획득 시, 덱에 있는 무작위 카드의 복사본을 1장 얻습니다.", DeckRelicRarity.UNCOMMON, DeckRelicType.SHOP, ItemSpriteSheet.ARTIFACT_HOURGLASS) {
		@Override public void onAcquire() {
			if (!DeckBuilderRun.deck.isEmpty()) {
				DeckBuilderRun.deck.add(DeckBuilderRun.deck.get(Random.Int(DeckBuilderRun.deck.size())));
			}
		}
	},
	LEES_WAFFLE("리의 와플", "획득 시, 최대 체력이 7 상승하고 모든 체력을 회복합니다.", DeckRelicRarity.COMMON, DeckRelicType.SHOP, ItemSpriteSheet.MEAT_PIE) {
		@Override public void onAcquire() { DeckBuilderRun.playerHT += 7; DeckBuilderRun.playerHP = DeckBuilderRun.playerHT; }
	},
	MEMBERSHIP_CARD("멤버십 카드", "모든 상품이 50% 할인됩니다.", DeckRelicRarity.RARE, DeckRelicType.SHOP, ItemSpriteSheet.GOLDEN_KEY),
	MINIATURE_TENT("미니어처 텐트", "휴식 장소에서 선택지를 둘다 선택할 수 있습니다.", DeckRelicRarity.RARE, DeckRelicType.SHOP, ItemSpriteSheet.CRYSTAL_CHEST),
	BURNING_STICKS("불타는 나뭇가지", "매 전투마다 처음으로 보조 카드를 소멸시킬 시, 그 카드의 복사본을 1장 손으로 가져옵니다.", DeckRelicRarity.UNCOMMON, DeckRelicType.SHOP, ItemSpriteSheet.ELIXIR_FEATHER),

	WAVE_RUSH("파문질주", "공격 카드를 사용할 때마다 연속 타격을 1 얻습니다. 연속 타격은 공격 이외의 카드를 사용하면 소멸합니다. 연속 타격은 공격 카드의 피해를 수치만큼 증가시킵니다.", DeckRelicRarity.COMMON, DeckRelicType.CLASS_STARTER, ItemSpriteSheet.SPIRIT_BOW);

	public final String title;
	public final String description;
	public final DeckRelicRarity rarity;
	public final DeckRelicType type;
	public final int icon;

	DeckRelic(String title, String description, DeckRelicRarity rarity, DeckRelicType type, int icon) {
		this.title = title;
		this.description = description;
		this.rarity = rarity;
		this.type = type;
		this.icon = icon;
	}

	public void onAcquire() {
	}

	public boolean rewardPool() {
		return type == DeckRelicType.COMMON || type == DeckRelicType.UNCOMMON || type == DeckRelicType.RARE;
	}

	public static DeckRelic byId(int id) {
		DeckRelic[] values = values();
		if (id < 0 || id >= values.length) return NUTRITIOUS_OYSTER;
		return values[id];
	}

	public static DeckRelic randomAvailable(DeckRelic excluded) {
		ArrayList<DeckRelic> pool = new ArrayList<>();
		for (DeckRelic relic : values()) {
			if (relic.rewardPool() && relic != excluded && !DeckBuilderRun.hasRelic(relic)) {
				pool.add(relic);
			}
		}
		return pool.isEmpty() ? null : pool.get(Random.Int(pool.size()));
	}

	public static DeckRelic randomAvailable(DeckRelicRarity rarity) {
		return randomAvailable(rarity, false);
	}

	public static DeckRelic randomAvailable(DeckRelicRarity rarity, boolean shopOnly) {
		ArrayList<DeckRelic> pool = new ArrayList<>();
		for (DeckRelic relic : values()) {
			boolean typeAllowed = shopOnly ? relic.type == DeckRelicType.SHOP : relic.rewardPool();
			if (typeAllowed && relic.rarity == rarity && !DeckBuilderRun.hasRelic(relic)) {
				pool.add(relic);
			}
		}
		if (pool.isEmpty() && !shopOnly) return randomAvailable((DeckRelic)null);
		return pool.isEmpty() ? null : pool.get(Random.Int(pool.size()));
	}

	public static DeckRelic randomStarterAvailable(ArrayList<Integer> excludedIds) {
		ArrayList<DeckRelic> pool = new ArrayList<>();
		for (DeckRelic relic : values()) {
			if (relic.type == DeckRelicType.STARTER
					&& !DeckBuilderRun.hasRelic(relic)
					&& (excludedIds == null || !excludedIds.contains(relic.ordinal()))) {
				pool.add(relic);
			}
		}
		return pool.isEmpty() ? null : pool.get(Random.Int(pool.size()));
	}

	private static DeckCard randomCard(DeckCardRarity rarity) {
		return randomCard(rarity, null);
	}

	private static DeckCard randomCard(DeckCardRarity rarity, DeckCardType type) {
		ArrayList<DeckCard> pool = new ArrayList<>();
		for (DeckCard card : DeckCard.rewardPool()) {
			if (card.rarity == rarity && (type == null || card.type == type)) {
				pool.add(card);
			}
		}
		return pool.isEmpty() ? DeckCard.rewardFallback(DeckBuilderRun.heroClass()) : pool.get(Random.Int(pool.size()));
	}

	static void upgradeFirstInDeck(DeckCard target) {
		for (int i = 0; i < DeckBuilderRun.deck.size(); i++) {
			int code = DeckBuilderRun.deck.get(i);
			if (DeckCard.byCode(code) == target) {
				int upgraded = DeckCardCode.upgrade(code);
				if (upgraded != code) {
					DeckBuilderRun.deck.set(i, upgraded);
					return;
				}
			}
		}
	}

	static void transformFirstInDeck(DeckCard target) {
		for (int i = 0; i < DeckBuilderRun.deck.size(); i++) {
			if (DeckCard.byCode(DeckBuilderRun.deck.get(i)) == target) {
				DeckCard current = target;
				DeckCard[] pool = DeckCard.rewardPool(DeckBuilderRun.heroClass(), false, false);
				ArrayList<DeckCard> filtered = new ArrayList<>();
				for (DeckCard c : pool) { if (c != current) filtered.add(c); }
				if (!filtered.isEmpty()) {
					DeckBuilderRun.deck.set(i, filtered.get(Random.Int(filtered.size())).code());
				}
				return;
			}
		}
	}

	public static DeckRelic randomPenaltyStarterAvailable(ArrayList<Integer> excludedIds) {
		ArrayList<DeckRelic> pool = new ArrayList<>();
		for (DeckRelic relic : values()) {
			if (relic.type == DeckRelicType.PENALTY_STARTER
					&& !DeckBuilderRun.hasRelic(relic)
					&& (excludedIds == null || !excludedIds.contains(relic.ordinal()))) {
				pool.add(relic);
			}
		}
		return pool.isEmpty() ? null : pool.get(Random.Int(pool.size()));
	}
}
