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

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

import java.util.ArrayList;

public enum DeckRelic {

	NEW_LEAF("하트 2", "획득 시, 덱의 카드 1장이 무작위로 변환됩니다.", DeckRelicRarity.COMMON, DeckRelicType.STARTER, ItemSpriteSheet.DECK) {
		@Override public void onAcquire() {
			ArrayList<Integer> transformable = new ArrayList<>();
			for (int i = 0; i < DeckBuilderRun.deck.size(); i++) {
				int code = DeckBuilderRun.deck.get(i);
				DeckCard card = DeckCard.byCode(code);
				if (!DeckCardPool.isQuest(card) && !card.hasKeyword(code, DeckCardKeyword.PERMANENT)) transformable.add(i);
			}
			if (transformable.isEmpty()) return;
			int idx = transformable.get(Random.Int(transformable.size()));
			int code = DeckBuilderRun.deck.get(idx);
			DeckCard current = DeckCard.byCode(code);
			DeckCard[] pool = DeckCard.rewardPool(DeckBuilderRun.heroClass(), false, false);
			ArrayList<DeckCard> filtered = new ArrayList<>();
			for (DeckCard c : pool) { if (c != current) filtered.add(c); }
			if (filtered.isEmpty()) return;
			DeckBuilderRun.deck.set(idx, filtered.get(Random.Int(filtered.size())).code());
		}
	},
	SMALL_CAPSULE("다이아 A", "획득 시, 무작위 유물을 1개 얻습니다.", DeckRelicRarity.COMMON, DeckRelicType.STARTER, ItemSpriteSheet.DECK) {
		@Override public void onAcquire() {
			DeckRelic relic = randomAvailable((DeckRelic) null);
			if (relic != null) DeckBuilderRun.addRelic(relic);
		}
	},
	STONE_HUMIDIFIER("하트 3", "휴식 장소에서 휴식을 취할 때마다, 최대 체력이 5 증가합니다.", DeckRelicRarity.COMMON, DeckRelicType.STARTER, ItemSpriteSheet.DECK),
	LEAD_PAPERWEIGHT("클로버 2", "중립 카드 2장 중 1장을 선택해 덱에 추가합니다.", DeckRelicRarity.COMMON, DeckRelicType.STARTER, ItemSpriteSheet.DECK) {
		@Override public void onAcquire() { DeckBuilderRun.pendingNeutralDiscover = true; }
	},
	LOST_COFFER("다이아 7", "획득 시, 카드 보상을 1번 획득하고, 무작위 포션을 1개 생성합니다.", DeckRelicRarity.COMMON, DeckRelicType.STARTER, ItemSpriteSheet.DECK) {
		@Override public void onAcquire() {
			DeckBuilderRun.pendingCardReward = true;
			DeckPotion[] potions = DeckPotion.values();
			DeckBuilderRun.addPotion(potions[Random.Int(potions.length)]);
		}
	},
	PRECISE_SCISSORS("스페이드 4", "덱에서 카드를 1장 제거합니다.", DeckRelicRarity.COMMON, DeckRelicType.STARTER, ItemSpriteSheet.DECK) {
		@Override public void onAcquire() { DeckBuilderRun.pendingCardRemove = true; }
	},
	BOOMING_CONCH("클로버 5", "강적 전투 시작 시, 카드를 추가로 2장 뽑고 에너지를 1 얻습니다.", DeckRelicRarity.COMMON, DeckRelicType.STARTER, ItemSpriteSheet.DECK),
	LAVA_ROCK("다이아 10", "1막의 보스가 보상으로 유물을 2개 제공합니다.", DeckRelicRarity.COMMON, DeckRelicType.STARTER, ItemSpriteSheet.DECK),
	GOLDEN_PEARL("다이아 9", "획득 시, 골드를 150 얻습니다.", DeckRelicRarity.COMMON, DeckRelicType.STARTER, ItemSpriteSheet.DECK) {
		@Override public void onAcquire() { DeckBuilderRun.gainGold(150); }
	},
	POMANDER("클로버 6", "덱의 카드를 1장 강화합니다.", DeckRelicRarity.COMMON, DeckRelicType.STARTER, ItemSpriteSheet.DECK) {
		@Override public void onAcquire() { DeckBuilderRun.pendingCardUpgrade = true; }
	},
	NEOWS_TALISMAN("하트 J", "획득 시, 기본 무기 1장과 기본 브로치 1장을 강화합니다.", DeckRelicRarity.COMMON, DeckRelicType.STARTER, ItemSpriteSheet.DECK) {
		@Override public void onAcquire() {
			upgradeFirstInDeck(DeckCard.STRIKE);
			upgradeFirstInDeck(DeckCard.GUARD);
		}
	},
	NEOWS_LAMENT("스페이드 2", "획득 시, 덱에 오시리스신을 1장 추가합니다.", DeckRelicRarity.COMMON, DeckRelicType.STARTER, ItemSpriteSheet.DECK) {
		@Override public void onAcquire() {
			DeckBuilderRun.addCard(DeckCard.OSIRIS_GOD);
		}
	},
	PHIAL_HOLSTER("하트 9", "획득 시, 무작위 포션 3개를 획득합니다.", DeckRelicRarity.COMMON, DeckRelicType.STARTER, ItemSpriteSheet.DECK) {
		@Override public void onAcquire() {
			DeckPotion[] potions = DeckPotion.values();
			for (int i = 0; i < 3; i++) DeckBuilderRun.addPotion(potions[Random.Int(potions.length)]);
		}
	},
	KALEIDOSCOPE("다이아 J", "획득 시, 다른 캐릭터 색상 카드를 2번 얻습니다.", DeckRelicRarity.COMMON, DeckRelicType.STARTER, ItemSpriteSheet.DECK) {
		@Override public void onAcquire() { DeckBuilderRun.pendingOtherClassCardReward = 2; }
	},
	FISHING_ROD("클로버 3", "일반 전투 3번마다 덱의 무작위 카드 1장이 강화됩니다.", DeckRelicRarity.COMMON, DeckRelicType.STARTER, ItemSpriteSheet.DECK),

	LEAFY_POULTICE("스페이드 Q", "획득 시, STRIKE 1장과 GUARD 1장이 무작위로 변환됩니다. 최대 체력을 12 잃습니다.", DeckRelicRarity.COMMON, DeckRelicType.PENALTY_STARTER, ItemSpriteSheet.DECK) {
		@Override public void onAcquire() {
			transformFirstInDeck(DeckCard.STRIKE);
			transformFirstInDeck(DeckCard.GUARD);
			DeckBuilderRun.playerHT = Math.max(1, DeckBuilderRun.playerHT - 12);
			DeckBuilderRun.playerHP = Math.min(DeckBuilderRun.playerHP, DeckBuilderRun.playerHT);
		}
	},
	STARTER_LARGE_CAPSULE("다이아 Q", "획득 시, 무작위 유물을 2개 얻습니다. 기본 무기 1장과 기본 브로치 1장을 덱에 추가합니다.", DeckRelicRarity.COMMON, DeckRelicType.PENALTY_STARTER, ItemSpriteSheet.DECK) {
		@Override public void onAcquire() {
			for (int i = 0; i < 2; i++) {
				DeckRelic relic = randomAvailable((DeckRelic) null);
				if (relic != null) DeckBuilderRun.addRelic(relic);
			}
			DeckBuilderRun.addCard(DeckCard.STRIKE);
			DeckBuilderRun.addCard(DeckCard.GUARD);
		}
	},
	HEFTY_TABLET("스페이드 3", "획득 시, 덱에 무작위 희귀 카드 3장 중 1장을 선택해 추가하고, 점액투성이를 1장 추가합니다.", DeckRelicRarity.COMMON, DeckRelicType.PENALTY_STARTER, ItemSpriteSheet.DECK) {
		@Override public void onAcquire() {
			DeckBuilderRun.addCard(DeckCard.SLIMY);
			DeckBuilderRun.pendingRareCardChoice++;
		}
	},
	PRECARIOUS_SHEARS("스페이드 8", "획득 시, 덱에서 카드를 2장 제거하고 피해를 16 받습니다.", DeckRelicRarity.COMMON, DeckRelicType.PENALTY_STARTER, ItemSpriteSheet.DECK) {
		@Override public void onAcquire() {
			DeckBuilderRun.playerHP = Math.max(1, DeckBuilderRun.playerHP - 16);
			DeckBuilderRun.pendingCardRemoveCount += 2;
		}
	},
	SILVER_CRUCIBLE("다이아 K", "처음 3번의 카드 보상이 강화된 상태로 지급됩니다. 처음으로 여는 보물 상자가 비어 있습니다.", DeckRelicRarity.COMMON, DeckRelicType.PENALTY_STARTER, ItemSpriteSheet.DECK) {
		@Override public void onAcquire() {
			DeckBuilderRun.upgradedCardRewardCount += 3;
			DeckBuilderRun.firstTreasureEmpty = true;
		}
	},
	CURSED_PEARL("스페이드 K", "덱에 영혼 징수를 1장 추가합니다. 골드를 333 획득합니다.", DeckRelicRarity.COMMON, DeckRelicType.PENALTY_STARTER, ItemSpriteSheet.DECK) {
		@Override public void onAcquire() {
			DeckBuilderRun.addCard(DeckCard.SOUL_TOLL);
			DeckBuilderRun.gainGold(333);
		}
	},
	CLUBS_ACE("클로버 A", "앞으로 3번의 전투 동안 적의 체력이 1이 됩니다.", DeckRelicRarity.COMMON, DeckRelicType.PENALTY_STARTER, ItemSpriteSheet.DECK) {
		@Override public void onAcquire() {
			DeckBuilderRun.enemyOneHpBattles += 3;
		}
	},

	HEARTS_ACE("하트 A", "모든 골드를 잃고 희귀 공용 카드 보상을 얻습니다.", DeckRelicRarity.COMMON, DeckRelicType.PENALTY_STARTER, ItemSpriteSheet.DECK) {
		@Override public void onAcquire() {
			DeckBuilderRun.gold = 0;
			DeckBuilderRun.pendingRareNeutralCardChoice++;
		}
	},

	NEOWS_BONES("스페이드 A", "획득 시, 무작위 시작 유물 2개를 획득합니다. 무작위 저주 카드를 1장 덱에 추가합니다.", DeckRelicRarity.COMMON, DeckRelicType.PENALTY_STARTER, ItemSpriteSheet.DECK) {
		@Override public void onAcquire() {
			ArrayList<Integer> seen = new ArrayList<>();
			for (int i = 0; i < 2; i++) {
				DeckRelic relic = randomStarterAvailable(seen);
				if (relic != null) {
					seen.add(relic.ordinal());
					DeckBuilderRun.addRelic(relic);
				}
			}
			ArrayList<DeckCard> curses = new ArrayList<>();
			for (DeckCard card : DeckCard.values()) {
				if (card.type == DeckCardType.CURSE) curses.add(card);
			}
			if (!curses.isEmpty()) DeckBuilderRun.addCard(curses.get(Random.Int(curses.size())));
		}
	},

	NUTRITIOUS_OYSTER("하트 K", "최대 체력이 11 증가합니다.", DeckRelicRarity.COMMON, DeckRelicType.STARTER, ItemSpriteSheet.DECK) {
		@Override
		public void onAcquire() {
			DeckBuilderRun.playerHT += 11;
			DeckBuilderRun.playerHP += 11;
		}
	},
	BAG_OF_MARBLES("전격탄", "매 전투 시작 시, 모든 적에게 피해 증폭을 1 부여합니다.", DeckRelicRarity.COMMON, DeckRelicType.COMMON, ItemSpriteSheet.SHOCKING_DART),
	VAJRA("축복탄", "매 전투 시작 시, 공격력을 1 얻습니다.", DeckRelicRarity.COMMON, DeckRelicType.COMMON, ItemSpriteSheet.HOLY_DART),
	ANCHOR("빙결탄", "매 전투 시작 시 보호막을 10 얻습니다.", DeckRelicRarity.COMMON, DeckRelicType.COMMON, ItemSpriteSheet.CHILLING_DART),
	STRAWBERRY("창부풍 스파게티", "획득 시, 최대 체력이 7 증가합니다.", DeckRelicRarity.COMMON, DeckRelicType.COMMON, ItemSpriteSheet.RATION) {
		@Override public void onAcquire() { DeckBuilderRun.playerHT += 7; DeckBuilderRun.playerHP += 7; }
	},
	LANTERN("죠스타의 피", "매 전투 시작 시, 추가로 1 에너지를 얻습니다.", DeckRelicRarity.COMMON, DeckRelicType.COMMON, ItemSpriteSheet.SANDBAG),
	GORGET("치유탄", "매 전투 시작 시, 재생을 4 얻습니다.", DeckRelicRarity.COMMON, DeckRelicType.COMMON, ItemSpriteSheet.HEALING_DART),
	BLOCKADE_COMMAND_DISC("봉쇄의 명령 DISC", "전투 시작 시, 방어력 증가를 1 얻습니다.", DeckRelicRarity.COMMON, DeckRelicType.COMMON, ItemSpriteSheet.STONE_FLOCK),
	MOVEMENT_COMMAND_DISC("이동의 명령 DISC", "매 전투마다 처음으로 내 턴에 체력을 잃을 시, 카드를 3장 뽑습니다.", DeckRelicRarity.COMMON, DeckRelicType.COMMON, ItemSpriteSheet.STONE_BLINK),
	FEAR_COMMAND_DISC("두려움의 명령 DISC", "전투 시작 시, 모든 적에게 공격력 저하를 1 부여합니다.", DeckRelicRarity.COMMON, DeckRelicType.COMMON, ItemSpriteSheet.STONE_FEAR),
	ATTACK_COMMAND_DISC("공격의 명령 DISC", "획득 시, 무작위 공격 카드를 2장 강화합니다.", DeckRelicRarity.COMMON, DeckRelicType.COMMON, ItemSpriteSheet.STONE_AGGRESSION) {
		@Override public void onAcquire() {
			upgradeRandomDeckCard(DeckCardType.ATTACK);
			upgradeRandomDeckCard(DeckCardType.ATTACK);
		}
	},
	LOST_BEEF_SANDWICH("로스트 비프 샌드위치", "상점 방에 진입할 때마다, 체력을 15 회복합니다.", DeckRelicRarity.COMMON, DeckRelicType.COMMON, ItemSpriteSheet.STEWED),
	EXPLORATION_COMMAND_DISC("탐사의 명령 DISC", "EVENT 노드에서 더 이상 일반 적 전투가 발생하지 않습니다.", DeckRelicRarity.COMMON, DeckRelicType.COMMON, ItemSpriteSheet.STONE_CLAIRVOYANCE),
	LIFE_BOMB("생명 폭탄", "덱에 카드를 5장 추가할 때마다, 체력을 20 회복합니다.", DeckRelicRarity.COMMON, DeckRelicType.COMMON, ItemSpriteSheet.REGROWTH_BOMB),
	PHANTOM_KEY("환영 열쇠", "휴식 장소에 진입할 때마다, 다음 전투 시작 시 추가로 1 에너지를 얻습니다.", DeckRelicRarity.COMMON, DeckRelicType.COMMON, ItemSpriteSheet.CRYSTAL_KEY),
	DETECTION_COMMAND_DISC("탐지의 명령 DISC", "적이 보상으로 주는 골드가 15 증가합니다.", DeckRelicRarity.COMMON, DeckRelicType.COMMON, ItemSpriteSheet.STONE_DETECT),
	RECHARGE_COMMAND_DISC("재충전의 명령 DISC", "전투 시작 시, 카드를 추가로 2장 뽑습니다.", DeckRelicRarity.COMMON, DeckRelicType.COMMON, ItemSpriteSheet.STONE_SHOCK),
	EQUIVALENT_EXCHANGE_COMMAND_DISC("등가교환의 명령 DISC", "전투 시작 시, 반격을 3 얻습니다.", DeckRelicRarity.COMMON, DeckRelicType.COMMON, ItemSpriteSheet.STONE_AUGMENTATION),
	EXPLOSION_COMMAND_DISC("폭발의 명령 DISC", "전투 시작 시, 모든 적에게 피해를 9 줍니다.", DeckRelicRarity.COMMON, DeckRelicType.COMMON, ItemSpriteSheet.STONE_BLAST),
	FLAME_DETECTOR("불꽃의 탐지기", "획득 시, 무작위 보조 카드를 2장 강화합니다.", DeckRelicRarity.COMMON, DeckRelicType.COMMON, ItemSpriteSheet.TORCH) {
		@Override public void onAcquire() {
			upgradeRandomDeckCard(DeckCardType.SKILL);
			upgradeRandomDeckCard(DeckCardType.SKILL);
		}
	},
	OBSERVATION_COMMAND_DISC("관찰의 명령 DISC", "3턴마다, 카드를 1장 뽑습니다.", DeckRelicRarity.COMMON, DeckRelicType.COMMON, ItemSpriteSheet.STONE_INTUITION),
	POTION_RACK("물약 보관대", "획득 시, 포션 슬롯을 2개 얻습니다.", DeckRelicRarity.COMMON, DeckRelicType.COMMON, ItemSpriteSheet.BANDOLIER),
	LEATHER_POUCH("가죽 주머니", "전투 시작 시, 체력을 2 회복합니다.", DeckRelicRarity.COMMON, DeckRelicType.COMMON, ItemSpriteSheet.VIAL),
	ENERGY_GRANT_COMMAND_DISC("에너지 부여의 명령 DISC", "3턴마다, 1 에너지를 얻습니다.", DeckRelicRarity.COMMON, DeckRelicType.COMMON, ItemSpriteSheet.STONE_ENCHANT),
	SLEEP_COMMAND_DISC("수면의 명령 DISC", "휴식을 취할 때마다 추가로 체력을 15 회복합니다.", DeckRelicRarity.COMMON, DeckRelicType.COMMON, ItemSpriteSheet.STONE_SLEEP),
	FF_DRINK("F.F.의 음료수", "포션을 사용할 때마다 체력을 5 회복합니다.", DeckRelicRarity.COMMON, DeckRelicType.COMMON, ItemSpriteSheet.PHANTOM_MEAT),
	GREMLIN_HORN("소음 폭탄", "적이 죽을 때마다, 1 에너지를 얻고 카드를 1장 뽑습니다.", DeckRelicRarity.UNCOMMON, DeckRelicType.UNCOMMON, ItemSpriteSheet.NOISEMAKER),
	MINIATURE_CANNON("전격 폭탄", "강화된 공격 카드의 피해량이 3 증가합니다.", DeckRelicRarity.UNCOMMON, DeckRelicType.UNCOMMON, ItemSpriteSheet.FLASHBANG),
	PEAR("떡국", "획득 시, 최대 체력이 10 증가합니다.", DeckRelicRarity.UNCOMMON, DeckRelicType.UNCOMMON, ItemSpriteSheet.STEAMED_FISH) {
		@Override public void onAcquire() { DeckBuilderRun.playerHT += 10; DeckBuilderRun.playerHP += 10; }
	},
	PLANISPHERE("흙 뭉치", "조우(EVENT) 방에 진입할 때마다, 체력을 5 회복합니다.", DeckRelicRarity.UNCOMMON, DeckRelicType.UNCOMMON, ItemSpriteSheet.PETAL),
	KUSARIGAMA("화염 폭탄", "한 턴에 공격 카드를 3장 사용할 때마다, 무작위 적에게 피해를 6 줍니다.", DeckRelicRarity.UNCOMMON, DeckRelicType.UNCOMMON, ItemSpriteSheet.FIRE_BOMB),
	TUNING_FORK("정화 폭탄", "보조 카드를 10장 사용할 때마다, 보호막을 7 얻습니다.", DeckRelicRarity.UNCOMMON, DeckRelicType.UNCOMMON, ItemSpriteSheet.HOLY_BOMB),

	ARCANE_SCROLL("조커", "획득 시 덱에 내 캐릭터 색상 희귀 카드 1장을 추가합니다.", DeckRelicRarity.COMMON, DeckRelicType.STARTER, ItemSpriteSheet.DECK) {
		@Override
		public void onAcquire() {
			HeroClass heroClass = DeckBuilderRun.heroClass();
			ArrayList<DeckCard> pool = new ArrayList<>();
			for (DeckCard c : DeckCard.rewardPool(heroClass, true, false)) {
				if (c.rarity == DeckCardRarity.RARE) pool.add(c);
			}
			DeckCard card = pool.isEmpty() ? null : pool.get(Random.Int(pool.size()));
			if (card != null) DeckBuilderRun.addCard(card);
		}
	},
	GYRO_MEMORY_DISC("자이로 체펠리의 기억 DISC", "세 번째 턴 시작 시, 공격력을 1, 방어력 증가를 1 얻습니다.", DeckRelicRarity.UNCOMMON, DeckRelicType.UNCOMMON, ItemSpriteSheet.EXOTIC_KAUNAN),
	MIYAMOTO_MEMORY_DISC("미야모토 테루노스케의 기억 DISC", "내 턴 시작 시, 모든 적에게 피해를 3 줍니다.", DeckRelicRarity.UNCOMMON, DeckRelicType.UNCOMMON, ItemSpriteSheet.EXOTIC_SOWILO),
	ANGER_STONE_MASK("분노의 돌가면", "공격 카드를 10장 사용할 때마다, 1 에너지를 얻습니다.", DeckRelicRarity.UNCOMMON, DeckRelicType.UNCOMMON, ItemSpriteSheet.RING_GARNET),
	STRENGTH_STONE_MASK("힘의 돌가면", "전투 시작 시, 일시적으로 공격력을 5 얻습니다.", DeckRelicRarity.UNCOMMON, DeckRelicType.UNCOMMON, ItemSpriteSheet.RING_RUBY),
	PUCCI_MEMORY_DISC("푸치신부의 기억 DISC", "휴식 장소에 진입 시, 덱에 있는 카드 5장마다 체력을 3 회복합니다.", DeckRelicRarity.UNCOMMON, DeckRelicType.UNCOMMON, ItemSpriteSheet.EXOTIC_LAGUZ),
	PURIFICATION_STONE_MASK("정화의 돌가면", "매 전투마다 처음으로 지속 카드를 사용 시, 보호막을 7 얻습니다.", DeckRelicRarity.UNCOMMON, DeckRelicType.UNCOMMON, ItemSpriteSheet.RING_TOPAZ),
	ENDURANCE_STONE_MASK("인내의 돌가면", "내 턴 종료 시 보호막이 없다면, 보호막을 6 획득합니다.", DeckRelicRarity.UNCOMMON, DeckRelicType.UNCOMMON, ItemSpriteSheet.RING_EMERALD),
	PET_SHOP_MEMORY_DISC("펫 숍의 기억 DISC", "매 전투마다 처음으로 카드를 통해 얻는 보호막이 2배로 증가합니다.", DeckRelicRarity.UNCOMMON, DeckRelicType.UNCOMMON, ItemSpriteSheet.EXOTIC_YNGVI),
	YASUHO_MEMORY_DISC("히로세 야스호의 기억 DISC", "휴식을 취할 때마다, 무작위 포션을 2개 생성합니다.", DeckRelicRarity.UNCOMMON, DeckRelicType.UNCOMMON, ItemSpriteSheet.EXOTIC_GYFU),
	ACCURACY_STONE_MASK("정확성의 돌가면", "한 턴에 공격 카드를 3장 사용할 때마다, 보호막을 4 얻습니다.", DeckRelicRarity.UNCOMMON, DeckRelicType.UNCOMMON, ItemSpriteSheet.RING_ONYX),
	LUCKY_STONE_MASK("행운의 돌가면", "골드를 25% 추가로 획득합니다.", DeckRelicRarity.UNCOMMON, DeckRelicType.UNCOMMON, ItemSpriteSheet.RING_OPAL),
	CHAOS_STONE_MASK("혼돈의 돌가면", "전투 시작 시, 뽑을 카드 더미에 있는 무작위 카드 2장을 남은 전투 동안 강화합니다.", DeckRelicRarity.UNCOMMON, DeckRelicType.UNCOMMON, ItemSpriteSheet.RING_TOURMALINE),
	EVASION_STONE_MASK("회피의 돌가면", "내 턴 동안 공격 카드를 사용하지 않았다면, 보호막을 4 얻습니다.", DeckRelicRarity.UNCOMMON, DeckRelicType.UNCOMMON, ItemSpriteSheet.RING_SAPPHIRE),
	KAWAJIRI_MEMORY_DISC("카와지리 코사쿠의 기억 DISC", "매 턴 종료 시 보호막이 10 이상이라면, 무작위 적에게 피해를 6 줍니다.", DeckRelicRarity.UNCOMMON, DeckRelicType.UNCOMMON, ItemSpriteSheet.EXOTIC_RAIDO),
	CHARGE_STONE_MASK("충전의 돌가면", "두 번째 턴 시작 시, 1 에너지를 얻습니다.", DeckRelicRarity.UNCOMMON, DeckRelicType.UNCOMMON, ItemSpriteSheet.RING_AMETHYST),
	OJIRO_MEMORY_DISC("사사메 오지로의 기억 DISC", "포션을 사용할 때마다, 이번 턴 동안 공격력을 3 얻습니다.", DeckRelicRarity.UNCOMMON, DeckRelicType.UNCOMMON, ItemSpriteSheet.EXOTIC_ISAZ),
	EMPORIO_MEMORY_DISC("엠포리오의 기억 DISC", "보스 전투 시작 시, 체력을 25 회복합니다.", DeckRelicRarity.UNCOMMON, DeckRelicType.UNCOMMON, ItemSpriteSheet.EXOTIC_MANNAZ),
	SNIPING_STONE_MASK("저격의 돌가면", "열 번째로 사용하는 공격 카드의 피해량이 2배로 증가합니다.", DeckRelicRarity.UNCOMMON, DeckRelicType.UNCOMMON, ItemSpriteSheet.RING_QUARTZ),
	VALENTINE_MEMORY_DISC("퍼니 밸런타인의 기억 DISC", "두 번째 턴 시작 시, 보호막을 14 얻습니다.", DeckRelicRarity.UNCOMMON, DeckRelicType.UNCOMMON, ItemSpriteSheet.EXOTIC_NAUDIZ),
	POCOLOCO_MEMORY_DISC("포코로코의 기억 DISC", "덱에 카드를 추가할 때마다, 골드를 15 얻습니다.", DeckRelicRarity.UNCOMMON, DeckRelicType.UNCOMMON, ItemSpriteSheet.EXOTIC_BERKANAN),
	SPEED_STONE_MASK("신속의 돌가면", "카드를 5장 소멸시킬 때마다, 카드를 1장 뽑습니다.", DeckRelicRarity.UNCOMMON, DeckRelicType.UNCOMMON, ItemSpriteSheet.RING_AGATE),
	SUSPICIOUS_TEA("수상한 차", "카드 보상에서 건너뛰기를 선택하면 최대 체력이 2 증가합니다.", DeckRelicRarity.UNCOMMON, DeckRelicType.UNCOMMON, ItemSpriteSheet.CHOC_AMULET),
	STRENGTH_ARM_STONE_MASK("완력의 돌가면", "한 턴에 보조 카드를 3장 사용할 때마다, 모든 적에게 피해를 5 줍니다.", DeckRelicRarity.UNCOMMON, DeckRelicType.UNCOMMON, ItemSpriteSheet.RING_DIAMOND),

	DIVER_DOWN("다이버 다운", "체력이 0이 될 때, 최대 체력의 50%만큼 체력을 회복합니다. 한 번만 발동합니다.", DeckRelicRarity.RARE, DeckRelicType.RARE, ItemSpriteSheet.ANKH),
	GAMBLE_CHIP("겜블 칩", "전투 시작 시, 원하는 만큼 카드를 버리고 버린 만큼 카드를 뽑습니다.", DeckRelicRarity.RARE, DeckRelicType.RARE, ItemSpriteSheet.TOKEN),
	STRAWBERRY_SHORTCAKE("딸기 생크림 케이크", "획득 시, 최대 체력이 14 상승합니다.", DeckRelicRarity.RARE, DeckRelicType.RARE, ItemSpriteSheet.MEAT_PIE) {
		@Override public void onAcquire() { DeckBuilderRun.playerHT += 14; DeckBuilderRun.playerHP += 14; }
	},
	FROG_SKIN("개구리 가죽", "내 턴 종료 시, 손에 있는 카드 1장당 보호막을 1 얻습니다.", DeckRelicRarity.RARE, DeckRelicType.RARE, ItemSpriteSheet.MOSSY_CLUMP),
	TAROT_CARD("타로 카드", "내 턴 동안 손에 카드가 없다면, 카드를 1장 뽑습니다.", DeckRelicRarity.RARE, DeckRelicType.RARE, ItemSpriteSheet.WONDROUS_RESIN),
	OBSIDIAN("흑요석", "매 턴마다 공격, 보조, 지속 카드를 처음으로 모두 사용 시, 공격력, 방어력 증가를 1 얻습니다.", DeckRelicRarity.RARE, DeckRelicType.RARE, ItemSpriteSheet.SUMMON_ELE),
	VITRIOL_DEVICE("염청 장치", "지속 카드를 사용할 때마다, 손에 있는 무작위 카드 1장을 이번 턴 동안 비용 없이 사용할 수 있습니다.", DeckRelicRarity.RARE, DeckRelicType.RARE, ItemSpriteSheet.EYE_OF_NEWT),
	SPW_FOUNDATION_LOST_ITEM("SPW재단의 유실물", "상점에서 판매하는 카드, 유물, 포션이 품절되지 않으며, 판매 금액이 20% 감소합니다.", DeckRelicRarity.RARE, DeckRelicType.RARE, ItemSpriteSheet.BONES),
	THOTH("토트신", "내 턴 동안 공격 카드를 사용하지 않았다면, 다음 턴에 추가 에너지를 얻습니다.", DeckRelicRarity.RARE, DeckRelicType.RARE, ItemSpriteSheet.RAT_SKULL),
	SAINT_TORSO("성인의 동체부", "휴식 장소에 탐색 선택지가 추가됩니다. 탐색을 선택하면 무작위 유물을 획득할 수 있습니다.", DeckRelicRarity.RARE, DeckRelicType.RARE, ItemSpriteSheet.PARCHMENT_SCRAP),
	NIGHT_RULER("밤의 지배자", "세 번째 턴 시작 시, 추가 에너지를 얻습니다.", DeckRelicRarity.RARE, DeckRelicType.RARE, ItemSpriteSheet.SUNDIAL),
	TOORU_DOLL("토오루의 인형", "일곱번째 턴 종료 시, 모든 적에게 피해를 52 줍니다.", DeckRelicRarity.RARE, DeckRelicType.RARE, ItemSpriteSheet.MIMIC_TOOTH),
	GOO_GOO_DOLLS("구구 돌즈", "세 번째 턴 시작 시, 보호막을 18 얻습니다.", DeckRelicRarity.RARE, DeckRelicType.RARE, ItemSpriteSheet.FERRET_TUFT),
	SHORT_KEY_NO_2("쇼트 키 No. 2", "매 전투마다 처음으로 적에게 해로운 효과를 부여하는 카드를 사용 시, 효과가 2배가 됩니다.", DeckRelicRarity.RARE, DeckRelicType.RARE, ItemSpriteSheet.CHAOTIC_CENSER),
	BLACK_PEARL("검은 진주", "한 턴에 공격 카드를 3장 사용할 때마다, 방어력 증가를 1 얻습니다.", DeckRelicRarity.RARE, DeckRelicType.RARE, ItemSpriteSheet.RECLAIM_TRAP),
	CHERRY_DECORATION("체리 장식", "턴 종료 시 남은 에너지가 사라지지 않습니다.", DeckRelicRarity.RARE, DeckRelicType.RARE, ItemSpriteSheet.SALT_CUBE),
	SCAN("스캔", "지속 카드를 덱에 추가할 때마다, 그 카드를 강화합니다.", DeckRelicRarity.RARE, DeckRelicType.RARE, ItemSpriteSheet.PETRIFIED_SEED),
	EMERALD("에메랄드", "비용이 2 이상인 카드를 사용할 때마다, 보호막을 4 얻습니다.", DeckRelicRarity.RARE, DeckRelicType.RARE, ItemSpriteSheet.WILD_ENERGY),
	LAVA_STONE("용암 암석", "보조 카드를 덱에 추가할 때마다, 그 카드를 강화합니다.", DeckRelicRarity.RARE, DeckRelicType.RARE, ItemSpriteSheet.EXOTIC_CRYSTALS),
	HEY_YA("헤이 야!", "전투 시작 시, 무작위 카드를 1장 손으로 가져옵니다. 이번 턴 동안 그 카드를 비용 없이 사용할 수 있습니다.", DeckRelicRarity.RARE, DeckRelicType.RARE, ItemSpriteSheet.CLOVER),
	THE_HUSTLE("THE 허슬", "체력을 잃을 때마다, 1만큼 덜 잃습니다.", DeckRelicRarity.RARE, DeckRelicType.RARE, ItemSpriteSheet.BLOOD_VIAL),
	OLD_WORKMAN("낡은 워크맨", "턴 사이에 보호막이 최대 10까지 유지됩니다.", DeckRelicRarity.RARE, DeckRelicType.RARE, ItemSpriteSheet.OBLIVION_SHARD),
	CHOCOLATE_DISCO("초콜릿 디스코", "한 턴에 공격 카드를 3장 사용할 때마다, 공격력을 1 얻습니다.", DeckRelicRarity.RARE, DeckRelicType.RARE, ItemSpriteSheet.TRAP_MECHANISM),
	TOPAZ("토파즈", "전투 시작 시 손에 있는 모든 카드가 강화됩니다.", DeckRelicRarity.RARE, DeckRelicType.RARE, ItemSpriteSheet.MAGIC_INFUSE),
	AUTUMN_LEAVES("어텀 리브스", "강적 처치 보상에 희귀 카드가 항상 등장합니다.", DeckRelicRarity.RARE, DeckRelicType.RARE, ItemSpriteSheet.SPYGLASS),
	RUBY("루비", "전투 보상에 포션이 항상 등장합니다.", DeckRelicRarity.RARE, DeckRelicType.RARE, ItemSpriteSheet.RECYCLE),
	MAGNET("자철석", "내 턴 동안 카드를 3장 이하로 사용했다면, 다음 턴 시작 시 카드를 추가로 3장 뽑습니다.", DeckRelicRarity.RARE, DeckRelicType.RARE, ItemSpriteSheet.TELE_GRAB),
	OPAL("자수정", "지속 카드를 사용할 때마다 체력을 2 회복합니다.", DeckRelicRarity.RARE, DeckRelicType.RARE, ItemSpriteSheet.RETURN_BEACON),
	DIO_BONE("DIO의 뼈", "카드가 소멸될 때마다 무작위 카드를 1장 손으로 가져옵니다.", DeckRelicRarity.RARE, DeckRelicType.RARE, ItemSpriteSheet.BEACON),
	BLACK_STAR("송편", "강적이 보상으로 주는 유물이 1개 증가합니다.", DeckRelicRarity.RARE, DeckRelicType.RARE, ItemSpriteSheet.SHATTERED_CAKE),
	GAME_PIECE("사파이어", "파워 카드를 사용할 때마다, 카드를 1장 뽑습니다.", DeckRelicRarity.RARE, DeckRelicType.RARE, ItemSpriteSheet.PHASE_SHIFT),
	MEAT_ON_THE_BONE("허브", "매 전투 종료 시 남은 체력이 50% 이하라면, 체력을 12 회복합니다.", DeckRelicRarity.RARE, DeckRelicType.RARE, ItemSpriteSheet.ORE),
	BEATING_REMNANT("20th 센츄리 보이", "내가 한 턴에 잃는 체력이 20을 넘을 수 없습니다.", DeckRelicRarity.RARE, DeckRelicType.RARE, ItemSpriteSheet.CEN),
	RAZOR_TOOTH("스피넬", "공격이나 보조 카드를 사용할 때마다, 그 카드를 남은 전투 동안 강화합니다.", DeckRelicRarity.RARE, DeckRelicType.RARE, ItemSpriteSheet.CURSE_INFUSE),
	OLD_COIN("금화", "획득 시, 골드를 300 얻습니다.", DeckRelicRarity.RARE, DeckRelicType.RARE, ItemSpriteSheet.GOLD) {
		@Override public void onAcquire() { DeckBuilderRun.gainGold(300); }
	},
	MOLTEN_EGG("에코즈의 알", "공격 카드를 덱에 추가할 때마다, 그 카드를 강화합니다.", DeckRelicRarity.RARE, DeckRelicType.RARE, ItemSpriteSheet.BLANDFRUIT),

	CAULDRON("미감정 물약", "획득 시, 무작위 포션을 3개 생성합니다.", DeckRelicRarity.COMMON, DeckRelicType.SHOP, ItemSpriteSheet.POTION_HOLDER) {
		@Override public void onAcquire() {
			DeckPotion[] potions = DeckPotion.values();
			for (int i = 0; i < 3; i++) DeckBuilderRun.addPotion(potions[Random.Int(potions.length)]);
		}
	},
	TOOLBOX("소프트&웨트의 방울", "매 전투 시작 시, 무작위 중립 카드 3장 중 1장을 선택해 손으로 가져옵니다.", DeckRelicRarity.UNCOMMON, DeckRelicType.SHOP, ItemSpriteSheet.STYLUS),
	RINGING_TRIANGLE("황금 열쇠", "매 전투마다 첫 턴에 손에 있는 카드를 보존합니다.", DeckRelicRarity.UNCOMMON, DeckRelicType.SHOP, ItemSpriteSheet.GOLDEN_KEY),
	DOLLYS_MIRROR("에니그마의 종이", "획득 시, 덱에 있는 무작위 카드의 복사본을 1장 얻습니다.", DeckRelicRarity.UNCOMMON, DeckRelicType.SHOP, ItemSpriteSheet.HOLSTER) {
		@Override public void onAcquire() {
			if (!DeckBuilderRun.deck.isEmpty()) {
				DeckBuilderRun.deck.add(DeckBuilderRun.deck.get(Random.Int(DeckBuilderRun.deck.size())));
			}
		}
	},
	LEES_WAFFLE("마르게리타 피자", "획득 시, 최대 체력이 7 상승하고 모든 체력을 회복합니다.", DeckRelicRarity.COMMON, DeckRelicType.SHOP, ItemSpriteSheet.PASTY) {
		@Override public void onAcquire() { DeckBuilderRun.playerHT += 7; DeckBuilderRun.playerHP = DeckBuilderRun.playerHT; }
	},
	MEMBERSHIP_CARD("크리스마스 선물 세트", "모든 상품이 50% 할인됩니다.", DeckRelicRarity.RARE, DeckRelicType.SHOP, ItemSpriteSheet.CANDY_CANE),
	MINIATURE_TENT("특수 열쇠", "휴식 장소에서 선택지를 둘다 선택할 수 있습니다.", DeckRelicRarity.RARE, DeckRelicType.SHOP, ItemSpriteSheet.WORN_KEY),
	BURNING_STICKS("직화구이 고기", "매 전투마다 처음으로 보조 카드를 소멸시킬 시, 그 카드의 복사본을 1장 손으로 가져옵니다.", DeckRelicRarity.UNCOMMON, DeckRelicType.SHOP, ItemSpriteSheet.STEAK),
	SPW_FOUNDATION_SUPPLIES("SPW재단의 보급품", "보유한 포션이 없는 동안, 2의 방어력 증가를 얻습니다.", DeckRelicRarity.RARE, DeckRelicType.SHOP, ItemSpriteSheet.OVERPRICED),
	HIGHWAY_TO_HELL("하이웨이 투 헬", "내 턴 종료 시 손에 카드가 없다면, 모든 적에게 피해를 20 줍니다.", DeckRelicRarity.RARE, DeckRelicType.SHOP, ItemSpriteSheet.HIGHWAY),
	SUPER_AJA("슈퍼 에이자", "첫 턴 시작 시, 1 에너지를 잃습니다. 다른 모든 턴 시작 시, 1 에너지를 얻습니다.", DeckRelicRarity.RARE, DeckRelicType.SHOP, ItemSpriteSheet.ALCHEMIZE),
	MAGIC_LAMP("마법의 램프", "골드를 획득할 때마다, 최대 체력이 1 증가합니다.", DeckRelicRarity.RARE, DeckRelicType.SHOP, ItemSpriteSheet.RAM),
	POLPO_LIGHTER("폴포의 라이터", "엘리트 전투 시작 시, 공격력을 2 얻습니다.", DeckRelicRarity.RARE, DeckRelicType.SHOP, ItemSpriteSheet.HONEYPOT),
	DANGEROUS_OBJECT_UNIDENTIFIED("무언가가 담긴 자루", "전투 동안 피해를 입지 않았다면, 매 전투 종료 시 모든 카드 보상이 강화됩니다.", DeckRelicRarity.RARE, DeckRelicType.SHOP, ItemSpriteSheet.WATERSKIN),
	FORMAGGIO_BOTTLE("포르마조의 병", "STRIKE, GUARD가 일시적을 얻습니다.", DeckRelicRarity.RARE, DeckRelicType.SHOP, ItemSpriteSheet.FORMAGGIO),
	ESCAPE_ROPE("탈출용 로프", "뽑을 카드 더미를 섞을 때마다, 보호막을 6 얻습니다.", DeckRelicRarity.RARE, DeckRelicType.SHOP, ItemSpriteSheet.ESCAPE),
	SPW_FOUNDATION_SUPPLY_BOX("SPW 재단의 보급 상자", "획득 시, 카드 보상을 5번 얻습니다.", DeckRelicRarity.RARE, DeckRelicType.SHOP, ItemSpriteSheet.SUPPLY_RATION) {
		@Override public void onAcquire() { DeckBuilderRun.pendingCardRewardCount += 5; }
	},
	SEVERED_WOMAN_HAND("잘린 여자 손", "3턴마다 적 전체에게 공격력 저하를 1 부여합니다.", DeckRelicRarity.RARE, DeckRelicType.SHOP, ItemSpriteSheet.CANDLE),
	RAW_MEAT_YUKHOE("생고기 육회", "덱에서 카드를 제거할 때마다 체력을 15 회복합니다.", DeckRelicRarity.RARE, DeckRelicType.SHOP, ItemSpriteSheet.CARPACCIO),
	BLACK_WILL("칠흑의 의지", "엘리트를 처치할 때마다 체력을 7 얻고 골드를 35 얻습니다.", DeckRelicRarity.RARE, DeckRelicType.SHOP, ItemSpriteSheet.BLOB),
	ST_GERMAIN_SANDWICH("생 제르맹 샌드위치", "매 전투 시 처음으로 지속 카드를 사용하면 공격력을 1 얻고 보호막을 6 얻습니다.", DeckRelicRarity.RARE, DeckRelicType.SHOP, ItemSpriteSheet.PUMPKIN_PIE),
	DONUT("도넛", "전투 시작 시, 정화의 보호막을 1 얻습니다.", DeckRelicRarity.RARE, DeckRelicType.SHOP, ItemSpriteSheet.EASTER_EGG),
	MODIFIED_DISC("변형된 DISC", "전투 시작 시, 무작위 일시적 카드 2장을 손으로 가져옵니다.", DeckRelicRarity.RARE, DeckRelicType.SHOP, ItemSpriteSheet.UNSTABLE_SPELL),
	STRANGE_FRAGMENT("기묘한 파편", "에너지가 3 이상인 카드를 사용할 때마다, 1 에너지를 얻습니다.", DeckRelicRarity.RARE, DeckRelicType.SHOP, ItemSpriteSheet.SHARD),
	DRAGONS_DREAM("드래곤즈 드림", "매 턴 시작 시 에너지를 1 추가로 얻습니다. 전투 시작 시 모든 적이 공격력을 1 추가로 얻습니다.", DeckRelicRarity.RARE, DeckRelicType.SHOP, ItemSpriteSheet.GRAVE),

	WAVE_RUSH("파문질주", "공격 카드를 사용할 때마다 연속 타격을 1 얻습니다. 연속 타격은 공격 이외의 카드를 사용하면 소멸합니다. 연속 타격은 공격 카드의 피해를 수치만큼 증가시킵니다. 매 전투 종료 시, 체력을 6 회복합니다.", DeckRelicRarity.COMMON, DeckRelicType.CLASS_STARTER, ItemSpriteSheet.SEAL_SHARD),

	CHEMICAL_X("폭약 더미", "비용이 X인 카드를 사용할 때, 그 카드의 효과가 X+2 에너지를 사용한 효과로 발동됩니다.", DeckRelicRarity.RARE, DeckRelicType.SHOP, ItemSpriteSheet.TBOMB2);

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
		if (this == NUTRITIOUS_OYSTER || this == ARCANE_SCROLL) return false;
		return type == DeckRelicType.COMMON || type == DeckRelicType.UNCOMMON || type == DeckRelicType.RARE;
	}

	public String rarityLabel() {
		return type == DeckRelicType.SHOP ? "특별" : rarity.label;
	}

	public String titleWithRarity() {
		if (type == DeckRelicType.STARTER || type == DeckRelicType.PENALTY_STARTER || type == DeckRelicType.CLASS_STARTER) {
			return title;
		}
		return title + "(" + rarityLabel() + ")";
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
		return randomAvailable(rarity, shopOnly, null);
	}

	public static DeckRelic randomAvailable(DeckRelicRarity rarity, boolean shopOnly, ArrayList<Integer> excludedIds) {
		ArrayList<DeckRelic> pool = new ArrayList<>();
		for (DeckRelic relic : values()) {
			boolean typeAllowed = shopOnly ? relic.type == DeckRelicType.SHOP : relic.rewardPool();
			if (typeAllowed
					&& relic.rarity == rarity
					&& !DeckBuilderRun.hasRelic(relic)
					&& (excludedIds == null || !excludedIds.contains(relic.ordinal()))) {
				pool.add(relic);
			}
		}
		if (pool.isEmpty() && !shopOnly) {
			for (DeckRelic relic : values()) {
				if (relic.rewardPool()
						&& !DeckBuilderRun.hasRelic(relic)
						&& (excludedIds == null || !excludedIds.contains(relic.ordinal()))) {
					pool.add(relic);
				}
			}
		}
		return pool.isEmpty() ? null : pool.get(Random.Int(pool.size()));
	}

	public static DeckRelic randomShopAvailable(ArrayList<Integer> excludedIds) {
		ArrayList<DeckRelic> pool = new ArrayList<>();
		for (DeckRelic relic : values()) {
			if (relic.type == DeckRelicType.SHOP
					&& !DeckBuilderRun.hasRelic(relic)
					&& (excludedIds == null || !excludedIds.contains(relic.ordinal()))) {
				pool.add(relic);
			}
		}
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
		for (DeckCard card : DeckCard.rewardPool(DeckBuilderRun.heroClass(), false, false)) {
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

	static void upgradeRandomDeckCard(DeckCardType type) {
		ArrayList<Integer> upgradable = new ArrayList<>();
		for (int i = 0; i < DeckBuilderRun.deck.size(); i++) {
			int code = DeckBuilderRun.deck.get(i);
			DeckCard card = DeckCard.byCode(code);
			if ((type == null || card.type == type) && DeckCardCode.upgrade(code) != code) {
				upgradable.add(i);
			}
		}
		if (upgradable.isEmpty()) return;
		int idx = upgradable.get(Random.Int(upgradable.size()));
		DeckBuilderRun.deck.set(idx, DeckCardCode.upgrade(DeckBuilderRun.deck.get(idx)));
	}

	static void transformFirstInDeck(DeckCard target) {
		for (int i = 0; i < DeckBuilderRun.deck.size(); i++) {
			if (DeckCard.byCode(DeckBuilderRun.deck.get(i)) == target) {
				int code = DeckBuilderRun.deck.get(i);
				if (target.hasKeyword(code, DeckCardKeyword.PERMANENT)) return;
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
