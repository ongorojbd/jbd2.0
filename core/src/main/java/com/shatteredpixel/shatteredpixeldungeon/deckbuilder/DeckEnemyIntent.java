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

import java.util.HashMap;
import java.util.Map;

public class DeckEnemyIntent {

	private static final Map<Integer, DeckEnemyIntent> TABLE = new HashMap<>();

	public static final DeckEnemyIntent SLIMY_INJECT = register(new DeckEnemyIntent(
			DeckBuilderCombat.RESULT_SLIMY_INJECT,
			(combat, enemy, remainingBlock) -> {
				combat.discardPile.add(DeckCard.SLIMY.code());
				combat.lastEnemyActions.add(new DeckBuilderCombat.EnemyAction(combat.enemyIndex(enemy), 0, true, "점액투성이", false, DeckCard.SLIMY, 1));
				return new TurnResult(remainingBlock, 0, true);
			},
			enemy -> "예고: 점액투성이 1장 섞어 넣음"));

	public static final DeckEnemyIntent AGE_DOWN = register(new DeckEnemyIntent(
			DeckBuilderCombat.RESULT_AGE_DOWN,
			(combat, enemy, remainingBlock) -> {
				if (combat.applyPlayerDebuff()) combat.playerDamageReduction = Math.max(combat.playerDamageReduction, 30);
				combat.lastEnemyActions.add(new DeckBuilderCombat.EnemyAction(combat.enemyIndex(enemy), 0, false, "유아화"));
				return TurnResult.noChange(remainingBlock);
			},
			enemy -> "예고: 유아화 부여"));

	public static final DeckEnemyIntent STRENGTH_7 = register(strengthIntent(DeckBuilderCombat.RESULT_STRENGTH_7, 7, "공격력"));
	public static final DeckEnemyIntent STRENGTH_2 = register(strengthIntent(DeckBuilderCombat.RESULT_STRENGTH_2, 2, "공격력"));

	public static final DeckEnemyIntent ATTACK_6_BLOCK_5 = register(new DeckEnemyIntent(
			DeckBuilderCombat.RESULT_ATTACK_6_BLOCK_5,
			(combat, enemy, remainingBlock) -> {
				AttackResult attack = combat.performEnemyAttack(enemy, 6, remainingBlock, "보호막 +5");
				combat.enemyGainBlock(enemy, 5);
				return attack.toTurnResult(false);
			},
			enemy -> "예고: " + damageText(enemy, 6) + " 피해 + 보호막 5"));

	public static final DeckEnemyIntent MASSACRE = register(new DeckEnemyIntent(
			DeckBuilderCombat.RESULT_MASSACRE,
			(combat, enemy, remainingBlock) -> {
				AttackSeriesResult result = attackSeries(combat, enemy, remainingBlock, 3, 3, "[대학살!]");
				enemy.thorns = Math.max(0, enemy.thorns - 2);
				return result.toTurnResult(false);
			},
			enemy -> "예고: " + damageText(enemy, 3) + "x3 피해"));

	public static final DeckEnemyIntent TOWER_NEEDLE = register(new DeckEnemyIntent(
			DeckBuilderCombat.RESULT_TOWER_NEEDLE,
			(combat, enemy, remainingBlock) -> {
				enemy.thorns += 2;
				combat.lastEnemyActions.add(new DeckBuilderCombat.EnemyAction(combat.enemyIndex(enemy), 0, false, "[타워 니들]"));
				return TurnResult.noChange(remainingBlock);
			},
			enemy -> "예고: 반격 +2"));

	public static final DeckEnemyIntent PRESSURIZE = register(strengthIntent(DeckBuilderCombat.RESULT_PRESSURIZE, 4, "공격력"));

	public static final DeckEnemyIntent TACKLE_BIG = register(simpleAttack(
			DeckBuilderCombat.RESULT_TACKLE_BIG, 16, "태클"));

	public static final DeckEnemyIntent LICK_BIG = register(blockReduction(
			DeckBuilderCombat.RESULT_LICK_BIG, 2));

	public static final DeckEnemyIntent CORROSIVE_SPIT_BIG = register(corrosiveSpit(
			DeckBuilderCombat.RESULT_CORROSIVE_SPIT_BIG, 11));

	public static final DeckEnemyIntent TACKLE_MEDIUM = register(simpleAttack(
			DeckBuilderCombat.RESULT_TACKLE_MEDIUM, 10, "태클"));

	public static final DeckEnemyIntent LICK_MEDIUM = register(blockReduction(
			DeckBuilderCombat.RESULT_LICK_MEDIUM, 1));

	public static final DeckEnemyIntent CORROSIVE_SPIT_MEDIUM = register(corrosiveSpit(
			DeckBuilderCombat.RESULT_CORROSIVE_SPIT_MEDIUM, 7));

	public static final DeckEnemyIntent SPLITTING_BITE = register(multiAttack(
			DeckBuilderCombat.RESULT_SPLITTING_BITE, 2, 4, "갈라물기"));

	public static final DeckEnemyIntent POISON_FANG = register(new DeckEnemyIntent(
			DeckBuilderCombat.RESULT_POISON_FANG,
			(combat, enemy, remainingBlock) -> {
				AttackResult attack = combat.performEnemyAttack(enemy, 6, remainingBlock, "독니 찌르기");
				combat.discardPile.add(DeckCard.POISON_DART.code());
				combat.lastEnemyActions.get(combat.lastEnemyActions.size() - 1).setShuffle(DeckCard.POISON_DART, 1);
				return attack.toTurnResult(true);
			},
			enemy -> "예고: " + damageText(enemy, 6) + " 피해 + 독침 1장 섞어 넣음"));

	public static final DeckEnemyIntent BITE = register(simpleAttack(
			DeckBuilderCombat.RESULT_BITE, 8, "물어뜯기"));

	public static final DeckEnemyIntent TAIL_WHIP = register(simpleAttack(
			DeckBuilderCombat.RESULT_TAIL_WHIP, 9, "꼬리 후려치기"));

	public static final DeckEnemyIntent DIRTY_FUR = register(new DeckEnemyIntent(
			DeckBuilderCombat.RESULT_DIRTY_FUR,
			(combat, enemy, remainingBlock) -> {
				combat.discardPile.add(DeckCard.POISON_DART.code());
				combat.discardPile.add(DeckCard.POISON_DART.code());
				combat.lastEnemyActions.add(new DeckBuilderCombat.EnemyAction(combat.enemyIndex(enemy), 0, false, "더러운 털뭉치", false, DeckCard.POISON_DART, 2));
				return TurnResult.noChange(remainingBlock);
			},
			enemy -> "예고: 독침 2장 섞어 넣음"));

	public static final DeckEnemyIntent CORNER = register(new DeckEnemyIntent(
			DeckBuilderCombat.RESULT_CORNER,
			(combat, enemy, remainingBlock) -> {
				AttackResult attack = combat.performEnemyAttack(enemy, 5, remainingBlock, "구석 몰기");
				if (combat.applyPlayerDebuff()) combat.playerWeak += 1;
				return attack.toTurnResult(false);
			},
			enemy -> "예고: " + damageText(enemy, 5) + " 피해 + 공격력 저하 1 부여"));

	public static final DeckEnemyIntent LAGAVULIN_SLEEP = register(new DeckEnemyIntent(
			DeckBuilderCombat.RESULT_LAGAVULIN_SLEEP,
			(combat, enemy, remainingBlock) -> {
				combat.enemyGainBlock(enemy, 8);
				combat.lastEnemyActions.add(new DeckBuilderCombat.EnemyAction(combat.enemyIndex(enemy), 0, false, "[금속화] 보호막 +8"));
				return TurnResult.noChange(remainingBlock);
			},
			enemy -> "예고: 젠틀리 위프스 (보호막 +8)"));

	public static final DeckEnemyIntent LAGAVULIN_STUN = register(new DeckEnemyIntent(
			DeckBuilderCombat.RESULT_LAGAVULIN_STUN,
			(combat, enemy, remainingBlock) -> {
				combat.lastEnemyActions.add(new DeckBuilderCombat.EnemyAction(combat.enemyIndex(enemy), 0, false, "[기절]"));
				return TurnResult.noChange(remainingBlock);
			},
			enemy -> "예고: 타겟 고정"));

	public static final DeckEnemyIntent LAGAVULIN_ATTACK = register(simpleAttack(
			DeckBuilderCombat.RESULT_LAGAVULIN_ATTACK, 18, "공격"));

	public static final DeckEnemyIntent LAGAVULIN_SIPHON = register(new DeckEnemyIntent(
			DeckBuilderCombat.RESULT_LAGAVULIN_SIPHON,
			(combat, enemy, remainingBlock) -> {
				if (combat.applyPlayerDebuff()) {
					combat.playerStrength -= 1;
					combat.playerDexterity -= 1;
				}
				combat.lastEnemyActions.add(new DeckBuilderCombat.EnemyAction(combat.enemyIndex(enemy), 0, false, "영혼 흡수 (공격력 -1, 민첩 -1)"));
				return TurnResult.noChange(remainingBlock);
			},
			enemy -> "예고: 영혼 흡수"));

	public static final DeckEnemyIntent PECK = register(multiAttack(
			DeckBuilderCombat.RESULT_PECK, 3, 3, "쪼기"));

	public static final DeckEnemyIntent BYRDONIS_BITE = register(simpleAttack(
			DeckBuilderCombat.RESULT_BYRDONIS_BITE, 17, "물기"));

	public static final DeckEnemyIntent HAUNT = register(new DeckEnemyIntent(
			DeckBuilderCombat.RESULT_HAUNT,
			(combat, enemy, remainingBlock) -> {
				for (int i = 0; i < 3; i++) combat.discardPile.add(DeckCard.BARNACLE.code());
				if (combat.applyPlayerDebuff()) combat.playerWeak += 3;
				combat.lastEnemyActions.add(new DeckBuilderCombat.EnemyAction(
						combat.enemyIndex(enemy), 0, false, "출몰 (공격력 저하 +3)", false, DeckCard.BARNACLE, 3));
				return new TurnResult(remainingBlock, 0, true);
			},
			enemy -> "예고: 따개비 3장 섞어 넣음 + 공격력 저하 3 부여"));

	public static final DeckEnemyIntent RAMMING_SPEED = register(new DeckEnemyIntent(
			DeckBuilderCombat.RESULT_RAMMING_SPEED,
			(combat, enemy, remainingBlock) -> {
				AttackResult attack = combat.performEnemyAttack(enemy, 10, remainingBlock, "전속력");
				if (combat.applyPlayerDebuff()) combat.playerWeak += 1;
				return attack.toTurnResult(false);
			},
			enemy -> "예고: " + damageText(enemy, 10) + " 피해 + 공격력 저하 1 부여"));

	public static final DeckEnemyIntent SWIPE = register(simpleAttack(
			DeckBuilderCombat.RESULT_SWIPE, 13, "밀쳐내기"));

	public static final DeckEnemyIntent STOMP = register(multiAttack(
			DeckBuilderCombat.RESULT_STOMP, 4, 3, "발구르기"));

	public static final DeckEnemyIntent CLAW = register(multiAttack(
			DeckBuilderCombat.RESULT_CLAW, 4, 2, "발톱"));

	public static final DeckEnemyIntent RAMPAGE = register(simpleAttack(
			DeckBuilderCombat.RESULT_RAMPAGE, 14, "날뛰기"));

	public static final DeckEnemyIntent INCANTATION = register(new DeckEnemyIntent(
			DeckBuilderCombat.RESULT_INCANTATION,
			(combat, enemy, remainingBlock) -> {
				enemy.ritual += 2;
				combat.lastEnemyActions.add(new DeckBuilderCombat.EnemyAction(
						combat.enemyIndex(enemy), 0, false, "주문 (광물화 +2)"));
				return TurnResult.noChange(remainingBlock);
			},
			enemy -> "예고: 광물화 +2"));

	public static final DeckEnemyIntent DARK_STRIKE = register(simpleAttack(
			DeckBuilderCombat.RESULT_DARK_STRIKE, 9, "어둠의 타격"));

	public static final DeckEnemyIntent SEA_KICK = register(simpleAttack(
			DeckBuilderCombat.RESULT_SEA_KICK, 11, "바다 차기"));

	public static final DeckEnemyIntent SPINNING_KICK = register(multiAttack(
			DeckBuilderCombat.RESULT_SPINNING_KICK, 2, 4, "돌려차기"));

	public static final DeckEnemyIntent BUBBLE_BURP = register(new DeckEnemyIntent(
			DeckBuilderCombat.RESULT_BUBBLE_BURP,
			(combat, enemy, remainingBlock) -> {
				combat.enemyGainBlock(enemy, 7);
				enemy.strength += 1;
				combat.lastEnemyActions.add(new DeckBuilderCombat.EnemyAction(
						combat.enemyIndex(enemy), 0, false, "거품 뿜기 (보호막 +7, 공격력 +1)"));
				return TurnResult.noChange(remainingBlock);
			},
			enemy -> "예고: 보호막 +7, 공격력 +1"));

	public static final DeckEnemyIntent VINE_SWIPE = register(multiAttack(
			DeckBuilderCombat.RESULT_VINE_SWIPE, 6, 2, "밀쳐내기"));

	public static final DeckEnemyIntent GRASPING_VINES = register(new DeckEnemyIntent(
			DeckBuilderCombat.RESULT_GRASPING_VINES,
			(combat, enemy, remainingBlock) -> {
				AttackResult attack = combat.performEnemyAttack(enemy, 8, remainingBlock, "휘감는 덩굴");
				if (combat.applyPlayerDebuff()) combat.playerEntangle += 1;
				return attack.toTurnResult(false);
			},
			enemy -> "예고: " + damageText(enemy, 8) + " 피해 + 뒤얽힘 1 부여"));

	public static final DeckEnemyIntent CHOMP = register(simpleAttack(
			DeckBuilderCombat.RESULT_CHOMP, 16, "물어뜯기"));

	public static final DeckEnemyIntent ROAR = register(new DeckEnemyIntent(
			DeckBuilderCombat.RESULT_ROAR,
			(combat, enemy, remainingBlock) -> {
				boolean applied = combat.applyPlayerDebuff();
				if (applied) combat.playerVulnerable += 3;
				enemy.splitUsed = true;
				combat.lastEnemyActions.add(new DeckBuilderCombat.EnemyAction(
						combat.enemyIndex(enemy), 0, false, applied ? "포효 (피해 증폭 +3)" : "포효 (피해 증폭 무효)"));
				return TurnResult.noChange(remainingBlock);
			},
			enemy -> "예고: 피해 증폭 +3"));

	public static final DeckEnemyIntent WINDUP_PUNCH = register(multiAttack(
			DeckBuilderCombat.RESULT_WINDUP_PUNCH, 3, 2, "감아치기"));

	public static final DeckEnemyIntent LASH = register(multiAttack(
			DeckBuilderCombat.RESULT_LASH, 2, 3, "후려치기"));

	public static final DeckEnemyIntent TACKLE = register(new DeckEnemyIntent(
			DeckBuilderCombat.RESULT_TACKLE,
			(combat, enemy, remainingBlock) -> {
				AttackResult attack = combat.performEnemyAttack(enemy, 9, remainingBlock, "방어력 저하 +1");
				if (combat.applyPlayerDebuff()) combat.playerDamageReduction = Math.max(combat.playerDamageReduction, 25);
				return attack.toTurnResult(false);
			},
			enemy -> "예고: " + damageText(enemy, 9) + " 피해 + 유아화 부여"));

	public static final DeckEnemyIntent CHARGE_UP = register(strengthIntent(DeckBuilderCombat.RESULT_CHARGE_UP, 2, "공격력"));

	public static final DeckEnemyIntent REPEATER_BLAST = register(new DeckEnemyIntent(
			DeckBuilderCombat.RESULT_REPEATER_BLAST,
			(combat, enemy, remainingBlock) -> {
				AttackResult attack = combat.performEnemyAttack(enemy, 7, remainingBlock, "공격력 +2");
				enemy.strength += 2;
				return attack.toTurnResult(false);
			},
			enemy -> "예고: " + damageText(enemy, 7) + " 피해 + 공격력 +2"));

	public static final DeckEnemyIntent EXPEL_BLAST = register(multiAttack(
			DeckBuilderCombat.RESULT_EXPEL_BLAST, 2, 5, "방출 폭발"));

	public static final DeckEnemyIntent SUBMERGE = register(new DeckEnemyIntent(
			DeckBuilderCombat.RESULT_SUBMERGE,
			(combat, enemy, remainingBlock) -> {
				combat.enemyGainBlock(enemy, 15);
				combat.lastEnemyActions.add(new DeckBuilderCombat.EnemyAction(combat.enemyIndex(enemy), 0, false, "보호막 +15"));
				return TurnResult.noChange(remainingBlock);
			},
			enemy -> "예고: 보호막 +15"));

	// 시생인
	public static final DeckEnemyIntent POWER_DANCE = register(strengthIntent(
			DeckBuilderCombat.RESULT_POWER_DANCE, 2, "공격력"));

	public static final DeckEnemyIntent BOOMERANG = register(multiAttack(
			DeckBuilderCombat.RESULT_BOOMERANG, 2, 2, "부메랑"));

	public static final DeckEnemyIntent QUICK_SLASH = register(simpleAttack(
			DeckBuilderCombat.RESULT_QUICK_SLASH, 5, "빠른 참격"));

	// 누케사쿠
	public static final DeckEnemyIntent ORB_OF_FRAILTY = register(new DeckEnemyIntent(
			DeckBuilderCombat.RESULT_ORB_OF_FRAILTY,
			(combat, enemy, remainingBlock) -> {
				AttackResult attack = combat.performEnemyAttack(enemy, 8, remainingBlock, "쇠약의 구체");
				if (combat.applyPlayerDebuff()) combat.playerBlockReduction += 1;
				return attack.toTurnResult(false);
			},
			enemy -> "예고: " + damageText(enemy, 8) + " 피해 + 방어력 저하 1 부여"));

	public static final DeckEnemyIntent ORB_OF_WEAKNESS = register(new DeckEnemyIntent(
			DeckBuilderCombat.RESULT_ORB_OF_WEAKNESS,
			(combat, enemy, remainingBlock) -> {
				AttackResult attack = combat.performEnemyAttack(enemy, 8, remainingBlock, "나약함의 구체");
				if (combat.applyPlayerDebuff()) combat.playerWeak += 1;
				return attack.toTurnResult(false);
			},
			enemy -> "예고: " + damageText(enemy, 8) + " 피해 + 공격력 저하 1 부여"));

	public static final DeckEnemyIntent SOUL_BEAM = register(multiAttack(
			DeckBuilderCombat.RESULT_SOUL_BEAM, 3, 3, "영혼 광선"));

	public static final DeckEnemyIntent DARK_RITUAL = register(strengthIntent(
			DeckBuilderCombat.RESULT_DARK_RITUAL, 2, "공격력"));

	// 시빌 워
	public static final DeckEnemyIntent PRICE_CARDS = register(new DeckEnemyIntent(
			DeckBuilderCombat.RESULT_PRICE_CARDS,
			(combat, enemy, remainingBlock) -> {
				int priceCode = DeckCard.PRICE_OF_SIN.code();
				combat.drawPile.add(priceCode);
				combat.discardPile.add(priceCode);
				combat.lastEnemyActions.add(new DeckBuilderCombat.EnemyAction(combat.enemyIndex(enemy), 0, false, "죄의 대가", false, DeckCard.PRICE_OF_SIN, 2));
				return TurnResult.noChange(remainingBlock);
			},
			enemy -> "예고: 죄의 대가 2장 섞어 넣음"));

	public static final DeckEnemyIntent DE_GAS = register(simpleAttack(
			DeckBuilderCombat.RESULT_DE_GAS, 16, "가스 방출"));

	public static final DeckEnemyIntent GAZE = register(new DeckEnemyIntent(
			DeckBuilderCombat.RESULT_GAZE,
			(combat, enemy, remainingBlock) -> {
				AttackResult attack = combat.performEnemyAttack(enemy, 7, remainingBlock, "시선");
				combat.discardPile.add(DeckCard.PRICE_OF_SIN.code());
				combat.lastEnemyActions.get(combat.lastEnemyActions.size() - 1).setShuffle(DeckCard.PRICE_OF_SIN, 1);
				return attack.toTurnResult(true);
			},
			enemy -> "예고: " + damageText(enemy, 7) + " 피해 + 죄의 대가 1장 버린 카드 더미에 추가"));

	public static final DeckEnemyIntent FADE = register(new DeckEnemyIntent(
			DeckBuilderCombat.RESULT_FADE,
			(combat, enemy, remainingBlock) -> {
				enemy.blessed += 2;
				combat.lastEnemyActions.add(new DeckBuilderCombat.EnemyAction(combat.enemyIndex(enemy), 0, false, "희미함 (축복 +2)"));
				return TurnResult.noChange(remainingBlock);
			},
			enemy -> "예고: 축복 +2 획득"));

	public static final DeckEnemyIntent SCREAM = register(new DeckEnemyIntent(
			DeckBuilderCombat.RESULT_SCREAM,
			(combat, enemy, remainingBlock) -> {
				boolean willApply = combat.playerArtifact <= 0;
				AttackResult attack = combat.performEnemyAttack(enemy, 11, remainingBlock, willApply ? "절규 (피해 증폭 +3)" : "절규 (피해 증폭 무효)");
				if (combat.applyPlayerDebuff()) combat.playerVulnerable += 3;
				return attack.toTurnResult(false);
			},
			enemy -> "예고: " + damageText(enemy, 11) + " 피해 + 피해 증폭 +3"));

	// 크림
	public static final DeckEnemyIntent CREAM_AMBUSH = register(new DeckEnemyIntent(
			DeckBuilderCombat.RESULT_CREAM_AMBUSH,
			(combat, enemy, remainingBlock) -> {
				enemy.darkSpace += 5;
				for (int i = 0; i < 2; i++) {
					combat.drawPile.add(DeckCard.GLIDE.code());
					combat.discardPile.add(DeckCard.GLIDE.code());
				}
				combat.lastEnemyActions.add(new DeckBuilderCombat.EnemyAction(combat.enemyIndex(enemy), 0, false, "급습 (암흑공간 +5)", false, DeckCard.GLIDE, 4));
				return TurnResult.noChange(remainingBlock);
			},
			enemy -> "예고: 암흑공간 +5, 활공 4장 섞어 넣음"));

	public static final DeckEnemyIntent CREAM_MIASMA = register(multiAttack(
			DeckBuilderCombat.RESULT_CREAM_MIASMA, 2, 6, "아공간의 독기"));

	public static final DeckEnemyIntent CREAM_SPIN = register(simpleAttack(
			DeckBuilderCombat.RESULT_CREAM_SPIN, 22, "무차별 회전"));

	public static final DeckEnemyIntent CREAM_RAGE = register(strengthIntent(
			DeckBuilderCombat.RESULT_CREAM_RAGE, 3, "공격력"));

	public final int id;
	private final IntentAction action;
	private final IntentText text;

	private DeckEnemyIntent(int id, IntentAction action, IntentText text) {
		this.id = id;
		this.action = action;
		this.text = text;
	}

	public static DeckEnemyIntent byId(int id) {
		DeckEnemyIntent intent = TABLE.get(id);
		return intent == null ? attack(id) : intent;
	}

	public TurnResult apply(DeckBuilderCombat combat, DeckCombatEnemy enemy, int remainingBlock) {
		return action.apply(combat, enemy, remainingBlock);
	}

	public String textFor(DeckCombatEnemy enemy) {
		return text.text(enemy);
	}

	public static String text(DeckCombatEnemy enemy) {
		return byId(enemy.intent).textFor(enemy);
	}

	private static DeckEnemyIntent register(DeckEnemyIntent intent) {
		DeckEnemyIntent previous = TABLE.put(intent.id, intent);
		if (previous != null) {
			throw new IllegalStateException("Duplicate deck enemy intent id: " + intent.id);
		}
		return intent;
	}

	private static DeckEnemyIntent attack(int damage) {
		return new DeckEnemyIntent(damage, DeckEnemyIntent::applyAttack, DeckEnemyIntent::attackText);
	}

	private static DeckEnemyIntent strengthIntent(int id, int amount, String label) {
		return new DeckEnemyIntent(
				id,
				(combat, enemy, remainingBlock) -> {
					enemy.strength += amount;
					combat.lastEnemyActions.add(new DeckBuilderCombat.EnemyAction(combat.enemyIndex(enemy), 0, false, label + " +" + amount));
					return TurnResult.noChange(remainingBlock);
				},
				enemy -> "예고: " + label + " +" + amount);
	}

	private static DeckEnemyIntent simpleAttack(int id, int damage, String label) {
		return new DeckEnemyIntent(
				id,
				(combat, enemy, remainingBlock) -> {
					AttackResult attack = combat.performEnemyAttack(enemy, damage, remainingBlock, label);
					return attack.toTurnResult(false);
				},
				enemy -> "예고: " + damageText(enemy, damage) + " 피해");
	}

	private static DeckEnemyIntent corrosiveSpit(int id, int damage) {
		return new DeckEnemyIntent(
				id,
				(combat, enemy, remainingBlock) -> {
					AttackResult attack = combat.performEnemyAttack(enemy, damage, remainingBlock, "부식의 침");
					combat.discardPile.add(DeckCard.SLIMY.code());
					combat.lastEnemyActions.get(combat.lastEnemyActions.size() - 1).setShuffle(DeckCard.SLIMY, 1);
					return attack.toTurnResult(true);
				},
				enemy -> "예고: " + damageText(enemy, damage) + " 피해 + 점액투성이 1장 섞어 넣음");
	}

	private static DeckEnemyIntent blockReduction(int id, int amount) {
		return new DeckEnemyIntent(
				id,
				(combat, enemy, remainingBlock) -> {
					if (combat.applyPlayerDebuff()) combat.playerBlockReduction += amount;
					combat.lastEnemyActions.add(new DeckBuilderCombat.EnemyAction(combat.enemyIndex(enemy), 0, false, "방어력 저하 +" + amount));
					return TurnResult.noChange(remainingBlock);
				},
				enemy -> "예고: 방어력 저하 부여 (" + amount + "턴)");
	}

	private static DeckEnemyIntent multiAttack(int id, int hits, int baseDamage, String firstLabel) {
		return new DeckEnemyIntent(
				id,
				(combat, enemy, remainingBlock) -> attackSeries(combat, enemy, remainingBlock, hits, baseDamage, firstLabel).toTurnResult(false),
				enemy -> "예고: " + damageText(enemy, baseDamage) + "x" + hits + " 피해");
	}

	private static TurnResult applyAttack(DeckBuilderCombat combat, DeckCombatEnemy enemy, int remainingBlock) {
		String label = enemy.kind == DeckEnemy.TOWER_OF_GREY && enemy.intent == 7 ? "[혀 뜯어내기]" : null;
		return combat.performEnemyAttack(enemy, enemy.intent, remainingBlock, label).toTurnResult(false);
	}

	private static String attackText(DeckCombatEnemy enemy) {
		if (enemy.intent > 0 && (enemy.strength != 0 || enemy.attackDown > 0)) {
			return "예고: " + damageText(enemy, enemy.intent) + " 피해";
		}
		return "예고: " + enemy.intent + " 피해";
	}

	private static int damageText(DeckCombatEnemy enemy, int baseDamage) {
		int damage = Math.max(0, baseDamage + enemy.strength);
		if (enemy.attackDown > 0) damage = damage * 3 / 4;
		return damage;
	}

	private static AttackSeriesResult attackSeries(DeckBuilderCombat combat, DeckCombatEnemy enemy, int remainingBlock, int hits, int baseDamage, String firstLabel) {
		int damageTaken = 0;
		for (int hit = 0; hit < hits; hit++) {
			AttackResult attack = combat.performEnemyAttack(enemy, baseDamage, remainingBlock, hit == 0 ? firstLabel : null);
			remainingBlock = attack.remainingBlock;
			damageTaken += attack.damageTaken;
		}
		return new AttackSeriesResult(remainingBlock, damageTaken);
	}

	private interface IntentAction {
		TurnResult apply(DeckBuilderCombat combat, DeckCombatEnemy enemy, int remainingBlock);
	}

	private interface IntentText {
		String text(DeckCombatEnemy enemy);
	}

	public static class TurnResult {
		public final int remainingBlock;
		public final int damageTaken;
		public final boolean injected;

		public TurnResult(int remainingBlock, int damageTaken, boolean injected) {
			this.remainingBlock = remainingBlock;
			this.damageTaken = damageTaken;
			this.injected = injected;
		}

		public static TurnResult noChange(int remainingBlock) {
			return new TurnResult(remainingBlock, 0, false);
		}
	}

	public static class AttackResult {
		public final int remainingBlock;
		public final int damageTaken;

		public AttackResult(int remainingBlock, int damageTaken) {
			this.remainingBlock = remainingBlock;
			this.damageTaken = damageTaken;
		}

		public TurnResult toTurnResult(boolean injected) {
			return new TurnResult(remainingBlock, damageTaken, injected);
		}
	}

	private static class AttackSeriesResult {
		private final int remainingBlock;
		private final int damageTaken;

		private AttackSeriesResult(int remainingBlock, int damageTaken) {
			this.remainingBlock = remainingBlock;
			this.damageTaken = damageTaken;
		}

		private TurnResult toTurnResult(boolean injected) {
			return new TurnResult(remainingBlock, damageTaken, injected);
		}
	}
}
