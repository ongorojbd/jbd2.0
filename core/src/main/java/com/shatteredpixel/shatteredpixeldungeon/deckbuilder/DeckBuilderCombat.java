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

import java.util.ArrayList;

import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class DeckBuilderCombat {

	public static final int RESULT_SLIMY_INJECT = -1;
	public static final int RESULT_AGE_DOWN = -2;
	public static final int RESULT_STRENGTH_7 = -3;
	public static final int RESULT_STRENGTH_2 = -4;
	public static final int RESULT_ATTACK_6_BLOCK_5 = -5;
	public static final int RESULT_MASSACRE = -6;
	public static final int RESULT_TOWER_NEEDLE = -7;
	public static final int RESULT_PRESSURIZE = -8;
	public static final int RESULT_TACKLE_BIG = -9;
	public static final int RESULT_LICK_BIG = -10;
	public static final int RESULT_TACKLE_MEDIUM = -11;
	public static final int RESULT_LICK_MEDIUM = -12;
	public static final int RESULT_CORROSIVE_SPIT_BIG = -20;
	public static final int RESULT_CORROSIVE_SPIT_MEDIUM = -21;
	public static final int RESULT_SPLITTING_BITE = -22;
	public static final int RESULT_POISON_FANG = -23;
	public static final int RESULT_BITE = -24;
	public static final int RESULT_TAIL_WHIP = -25;
	public static final int RESULT_DIRTY_FUR = -26;
	public static final int RESULT_CORNER = -27;
	public static final int RESULT_PECK = -28;
	public static final int RESULT_BYRDONIS_BITE = -29;
	public static final int RESULT_LAGAVULIN_SLEEP = -30;
	public static final int RESULT_LAGAVULIN_STUN = -31;
	public static final int RESULT_LAGAVULIN_ATTACK = -32;
	public static final int RESULT_LAGAVULIN_SIPHON = -33;
	public static final int RESULT_HAUNT = -34;
	public static final int RESULT_RAMMING_SPEED = -35;
	public static final int RESULT_SWIPE = -36;
	public static final int RESULT_STOMP = -37;
	public static final int RESULT_CLAW = -38;
	public static final int RESULT_RAMPAGE = -39;
	public static final int RESULT_ROAR = -40;
	public static final int RESULT_VINE_SWIPE = -41;
	public static final int RESULT_GRASPING_VINES = -42;
	public static final int RESULT_CHOMP = -43;
	public static final int RESULT_INCANTATION = -44;
	public static final int RESULT_DARK_STRIKE = -45;
	public static final int RESULT_SEA_KICK = -46;
	public static final int RESULT_SPINNING_KICK = -47;
	public static final int RESULT_BUBBLE_BURP = -48;
	// 시생인
	public static final int RESULT_POWER_DANCE  = -49;
	public static final int RESULT_BOOMERANG    = -50;
	public static final int RESULT_QUICK_SLASH  = -51;
	// 누케사쿠
	public static final int RESULT_ORB_OF_FRAILTY  = -52;
	public static final int RESULT_ORB_OF_WEAKNESS  = -53;
	public static final int RESULT_SOUL_BEAM         = -54;
	public static final int RESULT_DARK_RITUAL       = -55;

	// 시빌 워
	public static final int RESULT_PRICE_CARDS = -56;
	public static final int RESULT_DE_GAS      = -57;
	public static final int RESULT_GAZE        = -58;
	public static final int RESULT_FADE        = -59;
	public static final int RESULT_SCREAM      = -60;
	public static final int RESULT_CREAM_AMBUSH = -61;
	public static final int RESULT_CREAM_MIASMA = -62;
	public static final int RESULT_CREAM_SPIN = -63;
	public static final int RESULT_CREAM_RAGE = -64;
	public static final int RESULT_WINDUP_PUNCH = -13;
	public static final int RESULT_LASH = -14;
	public static final int RESULT_TACKLE = -15;
	public static final int RESULT_CHARGE_UP = -16;
	public static final int RESULT_REPEATER_BLAST = -17;
	public static final int RESULT_EXPEL_BLAST = -18;
	public static final int RESULT_SUBMERGE = -19;

	private static final String NODE_TYPE = "node_type";
	private static final String DEPTH = "depth";
	private static final String MAX_ENERGY = "max_energy";
	private static final String HAND_SIZE = "hand_size";
	private static final String MAX_HAND_SIZE = "max_hand_size";
	private static final String TARGET_INDEX = "target_index";
	private static final String ENERGY = "energy";
	private static final String BLOCK = "block";
	private static final String TURN = "turn";
	private static final String PLAYER_STRENGTH = "player_strength";
	private static final String PLAYER_TURN_STRENGTH = "player_turn_strength";
	private static final String PLAYER_TURN_DEXTERITY = "player_turn_dexterity";
	private static final String DRAW_PILE = "draw_pile";
	private static final String HAND = "hand";
	private static final String DISCARD_PILE = "discard_pile";
	private static final String EXHAUST_PILE = "exhaust_pile";
	private static final String ENEMY_KINDS = "enemy_kinds";
	private static final String ENEMY_HT = "enemy_ht";
	private static final String ENEMY_HP = "enemy_hp";
	private static final String ENEMY_INTENTS = "enemy_intents";
	private static final String ENEMY_VULNERABLE = "enemy_vulnerable";
	private static final String ENEMY_ATTACK_DOWN = "enemy_attack_down";
	private static final String ENEMY_STRENGTH = "enemy_strength";
	private static final String ENEMY_TURN_STRENGTH_LOSS = "enemy_turn_strength_loss";
	private static final String ENEMY_BLOCK = "enemy_block";
	private static final String ENEMY_THORNS = "enemy_thorns";
	private static final String ENEMY_PLATED_ARMOR = "enemy_plated_armor";
	private static final String ENEMY_ARTIFACT = "enemy_artifact";
	private static final String ENEMY_TRICKY = "enemy_tricky";
	private static final String ENEMY_BLOCK_REDUCTION = "enemy_block_reduction";
	private static final String ENEMY_VENOM = "enemy_venom";
	private static final String ENEMY_DEMISE = "enemy_demise";
	private static final String ENEMY_PERSISTENT_DAMAGE = "enemy_persistent_damage";
	private static final String ENEMY_RITUAL = "enemy_ritual";
	private static final String ENEMY_DARK_SPACE = "enemy_dark_space";
	private static final String ENEMY_LAST_INTENT = "enemy_last_intent";
	private static final String ENEMY_SPLIT_USED = "enemy_split_used";
	private static final String ENEMY_BLESSED = "enemy_blessed";
	private static final String ENEMY_DEBUFF_DOUBLE_TURNS = "enemy_debuff_double_turns";
	private static final String ENEMY_STRANGLE_HP_LOSS = "enemy_strangle_hp_loss";
	private static final String PLAYER_BLESSED = "player_blessed";
	private static final String PLAYER_DAMAGE_REDUCTION = "player_damage_reduction";
	private static final String PLAYER_BLOCK_REDUCTION = "player_block_reduction";
	private static final String PLAYER_VULNERABLE = "player_vulnerable";
	private static final String PLAYER_WEAK = "player_weak";
	private static final String PLAYER_DEXTERITY = "player_dexterity";
	private static final String PLAYER_ARTIFACT = "player_artifact";
	private static final String SHIV_DAMAGE_BONUS = "shiv_damage_bonus";
	private static final String FIRST_SHIV_DAMAGE_BONUS = "first_shiv_damage_bonus";
	private static final String SHIV_RETAIN = "shiv_retain";
	private static final String SHIV_ALL_ENEMIES = "shiv_all_enemies";
	private static final String INFINITE_BLADES_SHIVS = "infinite_blades_shivs";
	private static final String SPINNING_NAIL_DAMAGE_BONUS = "spinning_nail_damage_bonus";
	private static final String PLAYER_ENTANGLE = "player_entangle";
	private static final String PENDING_DISCOVER = "pending_discover";
	private static final String PENDING_DISCOVER_MODS = "pending_discover_mods";
	private static final String PLAYER_CONSECUTIVE_STRIKE = "player_consecutive_strike";
	private static final String PLAYER_THORNS = "player_thorns";
	private static final String PLAYER_PERMANENT_THORNS = "player_permanent_thorns";
	private static final String PLAYER_BARRICADE = "player_barricade";
	private static final String PLAYER_FIRST_BLOCK_DOUBLE = "player_first_block_double";
	private static final String FIRST_BLOCK_DOUBLE_USED = "first_block_double_used";
	private static final String PET_SHOP_BLOCK_DOUBLE_USED = "pet_shop_block_double_used";
	private static final String PLAYER_REGEN = "player_regen";
	private static final String ATTACK_CARDS_THIS_TURN = "attack_cards_this_turn";
	private static final String SKILL_CARDS_THIS_TURN = "skill_cards_this_turn";
	private static final String POWER_CARDS_THIS_TURN = "power_cards_this_turn";
	private static final String ATTACK_CARDS_PLAYED_COMBAT = "attack_cards_played_combat";
	private static final String SKILL_CARDS_PLAYED = "skill_cards_played";
	private static final String EXHAUSTED_CARDS_PLAYED_COMBAT = "exhausted_cards_played_combat";
	private static final String OBSIDIAN_TRIGGERED_THIS_TURN = "obsidian_triggered_this_turn";
	private static final String GAMBLE_CHIP_OFFERED = "gamble_chip_offered";
	private static final String SHORT_KEY_USED = "short_key_used";
	private static final String CURRENT_CARD_DEBUFF_DOUBLED = "current_card_debuff_doubled";
	private static final String POTIONLESS_DEXTERITY_ACTIVE = "potionless_dexterity_active";
	private static final String ST_GERMAIN_USED = "st_germain_used";
	private static final String BURNING_STICKS_FIRED = "burning_sticks_fired";
	private static final String CARDS_PLAYED_THIS_TURN = "cards_played_this_turn";
	private static final String CARDS_PLAYED_THIS_COMBAT = "cards_played_this_combat";
	private static final String POWERS_PLAYED = "powers_played";
	private static final String RUPTURE_STRENGTH = "rupture_strength";
	private static final String FIRESEA_DAMAGE = "firesea_damage";
	private static final String POISON_COAT_DAMAGE = "poison_coat_damage";
	private static final String HP_LOST_THIS_TURN = "hp_lost_this_turn";
	private static final String HP_LOST_THIS_COMBAT = "hp_lost_this_combat";
	private static final String BONUS_ENERGY_TURNS = "bonus_energy_turns";
	private static final String BONUS_DRAW_TURNS = "bonus_draw_turns";
	private static final String NEXT_TURN_BONUS_DRAW = "next_turn_bonus_draw";
	private static final String RETAIN_HAND_TURNS = "retain_hand_turns";
	private static final String DUPLICATE_NEXT_CARDS = "duplicate_next_cards";
	private static final String SURGE_ACTIVE = "surge_active";
	private static final String NEXT_ATTACK_DAMAGE_MULTIPLIER = "next_attack_damage_multiplier";
	private static final String INCOMING_DAMAGE_REDUCTION_TURNS = "incoming_damage_reduction_turns";
	private static final String STRENGTH_PER_TURN = "strength_per_turn";
	private static final String NEXT_TURN_BLOCK = "next_turn_block";
	private static final String PREVENT_HP_LOSS_TURNS = "prevent_hp_loss_turns";
	private static final String PENDING_HAND_CARD_TO_DRAW_TOP = "pending_hand_card_to_draw_top";
	private static final String BLOCK_FROM_CARD_DISABLED_TURNS = "block_from_card_disabled_turns";
	private static final String ORANGE_BOMB_TIMERS = "orange_bomb_timers";
	private static final String ORANGE_BOMB_DAMAGES = "orange_bomb_damages";
	private static final String PENDING_ALL_CARD_DISCOVER = "pending_all_card_discover";
	private static final String PENDING_ALL_RELIC_DISCOVER = "pending_all_relic_discover";
	private static final String PENDING_ALL_POTION_DISCOVER = "pending_all_potion_discover";
	private static final String PULSING_AXE_RETURNS = "pulsing_axe_returns";
	private static final String PENDING_DRAW_PILE_PEEK = "pending_draw_pile_peek";
	private static final String PENDING_DRAW_PILE_TYPE_SELECT = "pending_draw_pile_type_select";
	private static final String PENDING_DISCARD_HAND_SELECT_COUNT = "pending_discard_hand_select_count";
	private static final String PENDING_DAGGER_THROW_DISCARD = "pending_dagger_throw_discard";
	private static final String PENDING_HAND_DISCARD_SELECT_COUNT = "pending_hand_discard_select_count";
	private static final String IMPOSTING_PRESENCE_DAMAGE = "imposting_presence_damage";
	private static final String SNAKE_FORM_DAMAGE = "snake_form_damage";
	private static final String GUARD_BLOCK_BONUS = "guard_block_bonus";
	private static final String AUTOMATION_COUNT = "automation_count";
	private static final String AUTOMATION_DRAWS_COUNTER = "automation_draws_counter";
	private static final String STRATAGEM_COUNT = "stratagem_count";
	private static final String PENDING_STRATAGEM_SELECT = "pending_stratagem_select";
	private static final String ENTROPY_COUNT = "entropy_count";
	private static final String NOSTALGIA_ACTIVE = "nostalgia_active";
	private static final String NOSTALGIA_USED_THIS_TURN = "nostalgia_used_this_turn";
	private static final String CHAOS_COUNT = "chaos_count";
	private static final String ROLLING_BOULDER_DAMAGE = "rolling_boulder_damage";
	private static final String DIE_ON_UNBLOCKED_ATTACK = "die_on_unblocked_attack";
	private static final String NUMBNESS_BLOCK = "numbness_block";
	private static final String CARDS_EXHAUSTED_THIS_TURN = "cards_exhausted_this_turn";
	private static final String DARK_EMBRACE_DRAW = "dark_embrace_draw";
	private static final String MENTAL_OVERFLOW_DEMISE = "mental_overflow_demise";
	private static final String DOMAIN_COUNT = "domain_count";
	private static final String TOOLS_OF_THE_TRADE_COUNT = "tools_of_the_trade_count";
	private static final String MACHINE_LEARNING_COUNT = "machine_learning_count";
	private static final String PALE_BLUE_DOT_DRAW = "pale_blue_dot_draw";
	private static final String DICTATORSHIP_COUNT = "dictatorship_count";
	private static final String DRAW_DISABLED_THIS_TURN = "draw_disabled_this_turn";
	private static final String PENDING_HAND_EXHAUST_SELECT_COUNT = "pending_hand_exhaust_select_count";
	private static final String NEXT_TURN_ENERGY = "next_turn_energy";
	private static final String DEATH_DANCE_BLOCK = "death_dance_block";
	private static final String PRESERVE_BLOCK_NEXT_TURN = "preserve_block_next_turn";
	private static final String SHADOW_BLOCK_DOUBLE = "shadow_block_double";
	private static final String AFTERIMAGE_BLOCK = "afterimage_block";
	private static final String RAGE_BLOCK = "rage_block";
	private static final String ABSOLUTE_POWER_DAMAGE = "absolute_power_damage";
	private static final String PENDING_DISCARD_TO_DRAW_TOP = "pending_discard_to_draw_top";
	private static final String CARDS_DRAWN_THIS_TURN = "cards_drawn_this_turn";
	private static final String NEXT_SKILL_ZERO_COST = "next_skill_zero_cost";
	private static final String PRECISE_SHOT_ACTIVE = "precise_shot_active";
	private static final String PRECISE_SHOT_SKILL_DISCOUNT = "precise_shot_skill_discount";
	private static final String CARDS_DRAWN_THIS_COMBAT = "cards_drawn_this_combat";
	private static final String GOUGE_DAMAGE_BONUS = "gouge_damage_bonus";
	private static final String NEXT_POWER_ZERO_COST = "next_power_zero_cost";
	private static final String ENERGY_SPENT_THIS_TURN = "energy_spent_this_turn";
	private static final String NEXT_ATTACK_ZERO_COST = "next_attack_zero_cost";
	private static final String CARD_COST_INCREASE_THIS_TURN = "card_cost_increase_this_turn";
	private static final String ENERGY_GAIN_DISABLED_THIS_TURN = "energy_gain_disabled_this_turn";
	private static final String FRIENDSHIP_ENERGY = "friendship_energy";
	private static final String SUBROUTINE_ENERGY = "subroutine_energy";
	private static final String ORBIT_COUNT = "orbit_count";
	private static final String ORBIT_PROGRESS = "orbit_progress";
	private static final String HEART_OF_FIRE_ENERGY = "heart_of_fire_energy";
	private static final String ROYALTY_GOLD = "royalty_gold";
	private static final String ROYALTY_GOLD_APPLIED = "royalty_gold_applied";
	private static final String VOID_FORM_FREE_CARDS = "void_form_free_cards";
	private static final String VOID_FORM_FREE_CARDS_THIS_TURN = "void_form_free_cards_this_turn";
	private static final String END_TURN_REQUESTED = "end_turn_requested";

	public final int nodeType;
	public final int depth;
	public final int maxEnergy;
	public final int handSize;
	public final int maxHandSize;
	public final ArrayList<DeckCombatEnemy> enemies = new ArrayList<>();

	public int targetIndex;
	public int energy;
	public int block;
	public int turn;
	public int playerStrength;
	public int playerTurnStrength;
	public int playerTurnDexterity;
	public int playerDamageReduction;
	public int playerBlockReduction;
	public int playerVulnerable;
	public int playerWeak;
	public int playerDexterity;
	public int playerArtifact;
	public int playerEntangle;
	public int playerConsecutiveStrike;
	public int playerThorns;
	public int playerPermanentThorns;
	public DeckCard[] pendingDiscoverChoices;
	public boolean pendingDiscoverZeroCost;
	public boolean pendingDiscoverTransient;
	public boolean pendingDiscoverUpgraded;
	public boolean pendingDiscoverPlayAfterPick;
	public int shivDamageBonus;
	public int firstShivDamageBonus;
	public int spinningNailDamageBonus;
	public boolean firstShivUsed;
	public boolean shivRetain;
	public boolean shivAllEnemies;
	public int infiniteBladesShivs;
	public boolean playerBarricade;
	public boolean playerFirstBlockDouble;
	public boolean firstBlockDoubleUsedThisTurn;
	public boolean petShopBlockDoubleUsed;
	public boolean pendingHandCardUpgrade;
	public int playerRegen;
	public int attackCardsThisTurn;
	public int skillCardsThisTurn;
	public int powerCardsThisTurn;
	public int attackCardsPlayedThisCombat;
	public int skillCardsPlayed;
	public int exhaustedCardsPlayedThisCombat;
	public boolean obsidianTriggeredThisTurn;
	public boolean gambleChipOffered;
	public boolean shortKeyUsed;
	public boolean currentCardDebuffDoubled;
	public boolean potionlessDexterityActive;
	public boolean stGermainUsed;
	public int playKillCount;
	public boolean burningSticksFired;
	public int cardsPlayedThisTurn;
	public int cardsPlayedThisCombat;
	public int ruptureStrengthPerLoss;
	public int fireseaDamagePerLoss;
	public int poisonCoatDamage;
	public int playerHPLostCountThisTurn;
	public int playerHPLostCountThisCombat;
	public int bonusEnergyTurns;
	public int bonusDrawTurns;
	public int nextTurnBonusDraw;
	public int retainHandTurns;
	public int duplicateNextCards;
	public boolean surgeActive;
	public int nextAttackDamageMultiplier;
	public int incomingDamageReductionTurns;
	public int strengthPerTurn;
	public int nextTurnBlock;
	public int preventHpLossTurns;
	public int playerBlessed;
	public boolean pendingHandCardToDrawPileTop;
	public int blockFromCardDisabledTurns;
	public boolean pendingAllCardDiscover;
	public boolean pendingAllRelicDiscover;
	public boolean pendingAllPotionDiscover;
	public ArrayList<Integer> pulsingAxeReturns = new ArrayList<>();
	public boolean pendingDrawPilePeek = false;
	public DeckCardType pendingDrawPileTypeSelect = null;
	public int pendingDiscardHandSelectCount = 0;
	public boolean pendingDaggerThrowDiscard = false;
	public int pendingHandDiscardSelectCount = 0;
	public int impostingPresenceDamage = 0;
	public int snakeFormDamage = 0;
	public int guardBlockBonus = 0;
	public int automationCount = 0;
	public int automationDrawsSinceLastTrigger = 0;
	public int stratagemCount = 0;
	public boolean pendingStratagemSelect = false;
	public int entropyCount = 0;
	public int lastEntropyTransformCount = 0;
	public boolean nostalgiaActive = false;
	public boolean nostalgiaUsedThisTurn = false;
	public int chaosCount = 0;
	public int rollingBoulderCurrentDamage = 0;
	public int lastRollingBoulderDamage = 0;
	public boolean dieOnUnblockedAttack = false;
	public boolean dieOnUnblockedAttackTriggered = false;
	public int numbnessBlock = 0;
	public int cardsExhaustedThisTurn = 0;
	public int darkEmbraceDraw = 0;
	public int currentPlayHandIndexShift = 0;
	public boolean forceGameOver = false;
	public int mentalOverflowDemise = 0;
	public int domainCount = 0;
	public int toolsOfTheTradeCount = 0;
	public int machineLearningCount = 0;
	public int paleBlueDotDraw = 0;
	public int dictatorshipCount = 0;
	public boolean drawDisabledThisTurn = false;
	public int pendingHandExhaustSelectCount = 0;
	public int nextTurnEnergy = 0;
	public int deathDanceBlock = 0;
	public boolean preserveBlockNextTurn = false;
	public boolean shadowBlockDouble = false;
	public int afterimageBlock = 0;
	public int rageBlock = 0;
	public int absolutePowerDamage = 0;
	public boolean pendingDiscardToDrawPileTop = false;
	public int pendingGeneticAlgorithmGrowth = 0;
	public int cardsDrawnThisTurn = 0;
	public boolean nextSkillZeroCost = false;
	public boolean preciseShotActive = false;
	public int preciseShotSkillDiscount = 0;
	public int cardsDrawnThisCombat = 0;
	public int gougeDamageBonus = 0;
	public boolean nextPowerZeroCost = false;
	public int energySpentThisTurn = 0;
	public boolean nextAttackZeroCost = false;
	public int cardCostIncreaseThisTurn = 0;
	public boolean energyGainDisabledThisTurn = false;
	public int friendshipEnergy = 0;
	public int subroutineEnergy = 0;
	public int orbitCount = 0;
	public int orbitProgress = 0;
	public int heartOfFireEnergy = 0;
	public int royaltyGold = 0;
	public boolean royaltyGoldApplied = false;
	public int voidFormFreeCards = 0;
	public int voidFormFreeCardsThisTurn = 0;
	public boolean endTurnRequested = false;
	public ArrayList<Integer> orangeBombTimers = new ArrayList<>();
	public ArrayList<Integer> orangeBombDamages = new ArrayList<>();

	public ArrayList<Integer> drawPile = new ArrayList<>();
	public ArrayList<Integer> hand = new ArrayList<>();
	public ArrayList<Integer> discardPile = new ArrayList<>();
	public ArrayList<Integer> exhaustPile = new ArrayList<>();
	public ArrayList<Integer> powersPlayed = new ArrayList<>();
	public ArrayList<DeckPlayResult> lastAutoPlayResults = new ArrayList<>();
	public ArrayList<DeckPlayResult> lastTurnEndAutoPlayResults = new ArrayList<>();
	public ArrayList<EnemyAction> lastEnemyActions = new ArrayList<>();
	public ArrayList<DamageEvent> lastEnemyEndTurnDamageEvents = new ArrayList<>();
	public ArrayList<DamageEvent> lastDamageEvents = new ArrayList<>();
	public int lastTurnEndStatusDamage;
	public int lastTurnEndRegenBlock;
	public int lastTurnEndPoisonDarts;
	public int lastTurnEndCurseDamage;
	public int lastTurnEndRegretDamage;
	public int lastOrangeBombTotalDamage;
	public int lastOrangeBombExplosions;
	public int lastPriceOfSinDamage;
	public int lastCombatBreathingBlock;

	public DeckBuilderCombat(int nodeType, int depth, ArrayList<Integer> deck) {
		this.nodeType = nodeType;
		this.depth = Math.max(1, depth);
		this.maxEnergy = Math.min(DeckBuilderRun.MAX_ENERGY_CAP, Math.max(1, DeckBuilderRun.maxEnergy));
		this.handSize = Math.max(1, DeckBuilderRun.handSize);
		this.maxHandSize = Math.max(this.handSize, DeckBuilderRun.maxHandSize);
		DeckEnemy[] encounter = DeckBuilderRun.rollEncounter(nodeType, this.depth);
		for (DeckEnemy kind : encounter) {
			enemies.add(new DeckCombatEnemy(kind, this.depth));
		}
		if (enemies.isEmpty()) {
			enemies.add(new DeckCombatEnemy(DeckEnemy.forNode(nodeType), this.depth));
		}
		if (DeckBuilderRun.enemyOneHpBattles > 0) {
			for (DeckCombatEnemy enemy : enemies) { enemy.hp = 1; enemy.ht = 1; }
			DeckBuilderRun.enemyOneHpBattles--;
		}
		targetIndex = 0;
		this.turn = 0;
		if (DeckBuilderRun.tutorialMode) {
			// 튜토리얼 고정 덱: ATTACK(STAFF/BASH) + SKILL(GUARD) + POWER(IGNITE) 각 타입 체험
			this.drawPile.add(DeckCard.STAFF.code());   // ATTACK
			this.drawPile.add(DeckCard.GUARD.code());   // SKILL (보호막)
			this.drawPile.add(DeckCard.BASH.code());    // ATTACK
			this.drawPile.add(DeckCard.IGNITE.code());  // POWER (강화의 DISC: +2 공격력)
			this.drawPile.add(DeckCard.STAFF.code());   // ATTACK
		} else {
			this.drawPile.addAll(deck);
			if (DeckBuilderRun.hasRelic(DeckRelic.FORMAGGIO_BOTTLE)) {
				for (int i = 0; i < this.drawPile.size(); i++) {
					DeckCard card = DeckCard.byCode(this.drawPile.get(i));
					if (card == DeckCard.STRIKE || card == DeckCard.GUARD) {
						this.drawPile.set(i, DeckCardCode.withKeyword(this.drawPile.get(i), DeckCardKeyword.TRANSIENT));
					}
				}
			}
			shuffle(drawPile);
			// Move VANGUARD cards to the front so they are drawn in the opening hand
			ArrayList<Integer> vanguardCards = new ArrayList<>();
			for (int i = this.drawPile.size() - 1; i >= 0; i--) {
				int code = this.drawPile.get(i);
				if (DeckCard.byCode(code).hasKeyword(code, DeckCardKeyword.VANGUARD)) {
					vanguardCards.add(0, code);
					this.drawPile.remove(i);
				}
			}
			this.drawPile.addAll(0, vanguardCards);
			if (DeckBuilderRun.hasRelic(DeckRelic.CHAOS_STONE_MASK)) {
				upgradeRandomDrawPileCards(2);
			}
		}
		startTurnState();
	}

	private DeckBuilderCombat(int nodeType, int depth, int maxEnergy, int handSize, int maxHandSize) {
		this.nodeType = nodeType;
		this.depth = Math.max(1, depth);
		this.maxEnergy = Math.min(DeckBuilderRun.MAX_ENERGY_CAP, Math.max(1, maxEnergy));
		this.handSize = Math.max(1, handSize);
		this.maxHandSize = Math.max(this.handSize, maxHandSize);
	}

	public void storeInBundle(Bundle bundle) {
		bundle.put(NODE_TYPE, nodeType);
		bundle.put(DEPTH, depth);
		bundle.put(MAX_ENERGY, maxEnergy);
		bundle.put(HAND_SIZE, handSize);
		bundle.put(MAX_HAND_SIZE, maxHandSize);
		bundle.put(TARGET_INDEX, targetIndex);
		bundle.put(ENERGY, energy);
		bundle.put(BLOCK, block);
		bundle.put(TURN, turn);
		bundle.put(PLAYER_STRENGTH, playerStrength);
		bundle.put(PLAYER_TURN_STRENGTH, playerTurnStrength);
		bundle.put(PLAYER_TURN_DEXTERITY, playerTurnDexterity);
		bundle.put(PLAYER_DAMAGE_REDUCTION, playerDamageReduction);
		bundle.put(PLAYER_BLOCK_REDUCTION, playerBlockReduction);
		bundle.put(PLAYER_VULNERABLE, playerVulnerable);
		bundle.put(PLAYER_WEAK, playerWeak);
		bundle.put(PLAYER_DEXTERITY, playerDexterity);
		bundle.put(PLAYER_ARTIFACT, playerArtifact);
		bundle.put(SHIV_DAMAGE_BONUS, shivDamageBonus);
		bundle.put(FIRST_SHIV_DAMAGE_BONUS, firstShivDamageBonus);
		bundle.put(SHIV_RETAIN, shivRetain);
		bundle.put(SHIV_ALL_ENEMIES, shivAllEnemies);
		bundle.put(INFINITE_BLADES_SHIVS, infiniteBladesShivs);
		bundle.put(SPINNING_NAIL_DAMAGE_BONUS, spinningNailDamageBonus);
		bundle.put(PLAYER_ENTANGLE, playerEntangle);
		bundle.put(PLAYER_CONSECUTIVE_STRIKE, playerConsecutiveStrike);
		bundle.put(PLAYER_THORNS, playerThorns);
		bundle.put(PLAYER_PERMANENT_THORNS, playerPermanentThorns);
		bundle.put(PLAYER_BARRICADE, playerBarricade);
		bundle.put(PLAYER_FIRST_BLOCK_DOUBLE, playerFirstBlockDouble);
		bundle.put(FIRST_BLOCK_DOUBLE_USED, firstBlockDoubleUsedThisTurn);
		bundle.put(PET_SHOP_BLOCK_DOUBLE_USED, petShopBlockDoubleUsed);
		bundle.put(PLAYER_REGEN, playerRegen);
		bundle.put(ATTACK_CARDS_THIS_TURN, attackCardsThisTurn);
		bundle.put(SKILL_CARDS_THIS_TURN, skillCardsThisTurn);
		bundle.put(POWER_CARDS_THIS_TURN, powerCardsThisTurn);
		bundle.put(ATTACK_CARDS_PLAYED_COMBAT, attackCardsPlayedThisCombat);
		bundle.put(SKILL_CARDS_PLAYED, skillCardsPlayed);
		bundle.put(EXHAUSTED_CARDS_PLAYED_COMBAT, exhaustedCardsPlayedThisCombat);
		bundle.put(OBSIDIAN_TRIGGERED_THIS_TURN, obsidianTriggeredThisTurn);
		bundle.put(GAMBLE_CHIP_OFFERED, gambleChipOffered);
		bundle.put(SHORT_KEY_USED, shortKeyUsed);
		bundle.put(CURRENT_CARD_DEBUFF_DOUBLED, currentCardDebuffDoubled);
		bundle.put(POTIONLESS_DEXTERITY_ACTIVE, potionlessDexterityActive);
		bundle.put(ST_GERMAIN_USED, stGermainUsed);
		bundle.put(BURNING_STICKS_FIRED, burningSticksFired);
		bundle.put(CARDS_PLAYED_THIS_TURN, cardsPlayedThisTurn);
		bundle.put(CARDS_PLAYED_THIS_COMBAT, cardsPlayedThisCombat);
		bundle.put(RUPTURE_STRENGTH, ruptureStrengthPerLoss);
		bundle.put(FIRESEA_DAMAGE, fireseaDamagePerLoss);
		bundle.put(POISON_COAT_DAMAGE, poisonCoatDamage);
		bundle.put(HP_LOST_THIS_TURN, playerHPLostCountThisTurn);
		bundle.put(HP_LOST_THIS_COMBAT, playerHPLostCountThisCombat);
		bundle.put(BONUS_ENERGY_TURNS, bonusEnergyTurns);
		bundle.put(BONUS_DRAW_TURNS, bonusDrawTurns);
		bundle.put(NEXT_TURN_BONUS_DRAW, nextTurnBonusDraw);
		bundle.put(RETAIN_HAND_TURNS, retainHandTurns);
		bundle.put(DUPLICATE_NEXT_CARDS, duplicateNextCards);
		bundle.put(SURGE_ACTIVE, surgeActive);
		bundle.put(NEXT_ATTACK_DAMAGE_MULTIPLIER, nextAttackDamageMultiplier);
		bundle.put(INCOMING_DAMAGE_REDUCTION_TURNS, incomingDamageReductionTurns);
		bundle.put(STRENGTH_PER_TURN, strengthPerTurn);
		bundle.put(NEXT_TURN_BLOCK, nextTurnBlock);
		bundle.put(PREVENT_HP_LOSS_TURNS, preventHpLossTurns);
		bundle.put(PENDING_HAND_CARD_TO_DRAW_TOP, pendingHandCardToDrawPileTop);
		bundle.put(BLOCK_FROM_CARD_DISABLED_TURNS, blockFromCardDisabledTurns);
		bundle.put(PENDING_ALL_CARD_DISCOVER, pendingAllCardDiscover);
		bundle.put(PENDING_ALL_RELIC_DISCOVER, pendingAllRelicDiscover);
		bundle.put(PENDING_ALL_POTION_DISCOVER, pendingAllPotionDiscover);
		bundle.put(PENDING_DRAW_PILE_PEEK, pendingDrawPilePeek);
		bundle.put(PENDING_DRAW_PILE_TYPE_SELECT, pendingDrawPileTypeSelect == null ? -1 : pendingDrawPileTypeSelect.ordinal());
		bundle.put(PENDING_DISCARD_HAND_SELECT_COUNT, pendingDiscardHandSelectCount);
		bundle.put(PENDING_DAGGER_THROW_DISCARD, pendingDaggerThrowDiscard);
		bundle.put(PENDING_HAND_DISCARD_SELECT_COUNT, pendingHandDiscardSelectCount);
		if (!pulsingAxeReturns.isEmpty()) {
			bundle.put(PULSING_AXE_RETURNS, toArray(pulsingAxeReturns));
		}
		bundle.put(IMPOSTING_PRESENCE_DAMAGE, impostingPresenceDamage);
		bundle.put(SNAKE_FORM_DAMAGE, snakeFormDamage);
		bundle.put(GUARD_BLOCK_BONUS, guardBlockBonus);
		bundle.put(AUTOMATION_COUNT, automationCount);
		bundle.put(AUTOMATION_DRAWS_COUNTER, automationDrawsSinceLastTrigger);
		bundle.put(STRATAGEM_COUNT, stratagemCount);
		bundle.put(PENDING_STRATAGEM_SELECT, pendingStratagemSelect);
		bundle.put(ENTROPY_COUNT, entropyCount);
		bundle.put(NOSTALGIA_ACTIVE, nostalgiaActive);
		bundle.put(NOSTALGIA_USED_THIS_TURN, nostalgiaUsedThisTurn);
		bundle.put(CHAOS_COUNT, chaosCount);
		bundle.put(ROLLING_BOULDER_DAMAGE, rollingBoulderCurrentDamage);
		bundle.put(NUMBNESS_BLOCK, numbnessBlock);
		bundle.put(CARDS_EXHAUSTED_THIS_TURN, cardsExhaustedThisTurn);
		bundle.put(DARK_EMBRACE_DRAW, darkEmbraceDraw);
		bundle.put(DIE_ON_UNBLOCKED_ATTACK, dieOnUnblockedAttack);
		bundle.put(MENTAL_OVERFLOW_DEMISE, mentalOverflowDemise);
		bundle.put(DOMAIN_COUNT, domainCount);
		bundle.put(TOOLS_OF_THE_TRADE_COUNT, toolsOfTheTradeCount);
		bundle.put(MACHINE_LEARNING_COUNT, machineLearningCount);
		bundle.put(PALE_BLUE_DOT_DRAW, paleBlueDotDraw);
		bundle.put(DICTATORSHIP_COUNT, dictatorshipCount);
		bundle.put(DRAW_DISABLED_THIS_TURN, drawDisabledThisTurn);
		bundle.put(PENDING_HAND_EXHAUST_SELECT_COUNT, pendingHandExhaustSelectCount);
		bundle.put(NEXT_TURN_ENERGY, nextTurnEnergy);
		bundle.put(DEATH_DANCE_BLOCK, deathDanceBlock);
		bundle.put(PRESERVE_BLOCK_NEXT_TURN, preserveBlockNextTurn);
		bundle.put(SHADOW_BLOCK_DOUBLE, shadowBlockDouble);
		bundle.put(AFTERIMAGE_BLOCK, afterimageBlock);
		bundle.put(RAGE_BLOCK, rageBlock);
		bundle.put(ABSOLUTE_POWER_DAMAGE, absolutePowerDamage);
		bundle.put(PENDING_DISCARD_TO_DRAW_TOP, pendingDiscardToDrawPileTop);
		bundle.put(CARDS_DRAWN_THIS_TURN, cardsDrawnThisTurn);
		bundle.put(NEXT_SKILL_ZERO_COST, nextSkillZeroCost);
		bundle.put(PRECISE_SHOT_ACTIVE, preciseShotActive);
		bundle.put(PRECISE_SHOT_SKILL_DISCOUNT, preciseShotSkillDiscount);
		bundle.put(CARDS_DRAWN_THIS_COMBAT, cardsDrawnThisCombat);
		bundle.put(GOUGE_DAMAGE_BONUS, gougeDamageBonus);
		bundle.put(NEXT_POWER_ZERO_COST, nextPowerZeroCost);
		bundle.put(ENERGY_SPENT_THIS_TURN, energySpentThisTurn);
		bundle.put(NEXT_ATTACK_ZERO_COST, nextAttackZeroCost);
		bundle.put(CARD_COST_INCREASE_THIS_TURN, cardCostIncreaseThisTurn);
		bundle.put(ENERGY_GAIN_DISABLED_THIS_TURN, energyGainDisabledThisTurn);
		bundle.put(FRIENDSHIP_ENERGY, friendshipEnergy);
		bundle.put(SUBROUTINE_ENERGY, subroutineEnergy);
		bundle.put(ORBIT_COUNT, orbitCount);
		bundle.put(ORBIT_PROGRESS, orbitProgress);
		bundle.put(HEART_OF_FIRE_ENERGY, heartOfFireEnergy);
		bundle.put(ROYALTY_GOLD, royaltyGold);
		bundle.put(ROYALTY_GOLD_APPLIED, royaltyGoldApplied);
		bundle.put(VOID_FORM_FREE_CARDS, voidFormFreeCards);
		bundle.put(VOID_FORM_FREE_CARDS_THIS_TURN, voidFormFreeCardsThisTurn);
		bundle.put(END_TURN_REQUESTED, endTurnRequested);
		if (!orangeBombTimers.isEmpty()) {
			bundle.put(ORANGE_BOMB_TIMERS, toArray(orangeBombTimers));
			bundle.put(ORANGE_BOMB_DAMAGES, toArray(orangeBombDamages));
		}
		if (pendingDiscoverChoices != null) {
			int[] ids = new int[pendingDiscoverChoices.length];
			for (int i = 0; i < pendingDiscoverChoices.length; i++) ids[i] = pendingDiscoverChoices[i].id;
			bundle.put(PENDING_DISCOVER, ids);
			bundle.put(PENDING_DISCOVER_MODS, (pendingDiscoverZeroCost ? 1 : 0) | (pendingDiscoverPlayAfterPick ? 2 : 0) | (pendingDiscoverTransient ? 4 : 0) | (pendingDiscoverUpgraded ? 8 : 0));
		}
		bundle.put(DRAW_PILE, toArray(drawPile));
		bundle.put(HAND, toArray(hand));
		bundle.put(DISCARD_PILE, toArray(discardPile));
		bundle.put(EXHAUST_PILE, toArray(exhaustPile));
		bundle.put(POWERS_PLAYED, toArray(powersPlayed));

		int[] enemyKinds = new int[enemies.size()];
		int[] enemyHt = new int[enemies.size()];
		int[] enemyHp = new int[enemies.size()];
		int[] enemyIntents = new int[enemies.size()];
		int[] enemyVulnerable = new int[enemies.size()];
		int[] enemyAttackDown = new int[enemies.size()];
		int[] enemyStrength = new int[enemies.size()];
		int[] enemyTurnStrengthLoss = new int[enemies.size()];
		int[] enemyBlock = new int[enemies.size()];
		int[] enemyThorns = new int[enemies.size()];
		int[] enemyPlatedArmor = new int[enemies.size()];
		int[] enemyArtifact = new int[enemies.size()];
		int[] enemyTricky = new int[enemies.size()];
		int[] enemyBlockReduction = new int[enemies.size()];
		int[] enemyVenom = new int[enemies.size()];
		int[] enemyDemise = new int[enemies.size()];
		int[] enemyPersistentDamage = new int[enemies.size()];
		int[] enemyRitual = new int[enemies.size()];
		int[] enemyDarkSpace = new int[enemies.size()];
		int[] enemyLastIntent = new int[enemies.size()];
		boolean[] enemySplitUsed = new boolean[enemies.size()];
		int[] enemyBlessed = new int[enemies.size()];
		int[] enemyDebuffDoubleTurns = new int[enemies.size()];
		int[] enemyStrangleHpLoss = new int[enemies.size()];
		for (int i = 0; i < enemies.size(); i++) {
			DeckCombatEnemy enemy = enemies.get(i);
			enemyKinds[i] = enemy.kind.ordinal();
			enemyHt[i] = enemy.ht;
			enemyHp[i] = enemy.hp;
			enemyIntents[i] = enemy.intent;
			enemyVulnerable[i] = enemy.vulnerable;
			enemyAttackDown[i] = enemy.attackDown;
			enemyStrength[i] = enemy.strength;
			enemyTurnStrengthLoss[i] = enemy.turnStrengthLoss;
			enemyBlock[i] = enemy.block;
			enemyThorns[i] = enemy.thorns;
			enemyPlatedArmor[i] = enemy.platedArmor;
			enemyArtifact[i] = enemy.artifact;
			enemyTricky[i] = enemy.tricky;
			enemyBlockReduction[i] = enemy.blockReduction;
			enemyVenom[i] = enemy.venom;
			enemyDemise[i] = enemy.demise;
			enemyPersistentDamage[i] = enemy.persistentDamage;
			enemyRitual[i] = enemy.ritual;
			enemyDarkSpace[i] = enemy.darkSpace;
			enemyLastIntent[i] = enemy.lastIntent;
			enemySplitUsed[i] = enemy.splitUsed;
			enemyBlessed[i] = enemy.blessed;
			enemyDebuffDoubleTurns[i] = enemy.debuffDoubleTurns;
			enemyStrangleHpLoss[i] = enemy.strangleHpLoss;
		}
		bundle.put(ENEMY_KINDS, enemyKinds);
		bundle.put(ENEMY_HT, enemyHt);
		bundle.put(ENEMY_HP, enemyHp);
		bundle.put(ENEMY_INTENTS, enemyIntents);
		bundle.put(ENEMY_VULNERABLE, enemyVulnerable);
		bundle.put(ENEMY_ATTACK_DOWN, enemyAttackDown);
		bundle.put(ENEMY_STRENGTH, enemyStrength);
		bundle.put(ENEMY_TURN_STRENGTH_LOSS, enemyTurnStrengthLoss);
		bundle.put(ENEMY_BLOCK, enemyBlock);
		bundle.put(ENEMY_THORNS, enemyThorns);
		bundle.put(ENEMY_PLATED_ARMOR, enemyPlatedArmor);
		bundle.put(ENEMY_ARTIFACT, enemyArtifact);
		bundle.put(ENEMY_TRICKY, enemyTricky);
		bundle.put(ENEMY_BLOCK_REDUCTION, enemyBlockReduction);
		bundle.put(ENEMY_VENOM, enemyVenom);
		bundle.put(ENEMY_DEMISE, enemyDemise);
		bundle.put(ENEMY_PERSISTENT_DAMAGE, enemyPersistentDamage);
		bundle.put(ENEMY_RITUAL, enemyRitual);
		bundle.put(ENEMY_DARK_SPACE, enemyDarkSpace);
		bundle.put(ENEMY_LAST_INTENT, enemyLastIntent);
		bundle.put(ENEMY_SPLIT_USED, enemySplitUsed);
		bundle.put(ENEMY_BLESSED, enemyBlessed);
		bundle.put(ENEMY_DEBUFF_DOUBLE_TURNS, enemyDebuffDoubleTurns);
		bundle.put(ENEMY_STRANGLE_HP_LOSS, enemyStrangleHpLoss);
		bundle.put(PLAYER_BLESSED, playerBlessed);
	}

	public static DeckBuilderCombat restoreFromBundle(Bundle bundle) {
		if (!bundle.contains(NODE_TYPE) || !bundle.contains(ENEMY_KINDS)) return null;

		DeckBuilderCombat combat = new DeckBuilderCombat(
				bundle.getInt(NODE_TYPE),
				bundle.getInt(DEPTH),
				bundle.contains(MAX_ENERGY) ? bundle.getInt(MAX_ENERGY) : DeckBuilderRun.STARTING_ENERGY,
				bundle.contains(HAND_SIZE) ? bundle.getInt(HAND_SIZE) : DeckBuilderRun.STARTING_HAND_SIZE,
				bundle.contains(MAX_HAND_SIZE) ? bundle.getInt(MAX_HAND_SIZE) : DeckBuilderRun.DEFAULT_MAX_HAND_SIZE);

		combat.targetIndex = bundle.getInt(TARGET_INDEX);
		combat.energy = bundle.getInt(ENERGY);
		combat.block = bundle.getInt(BLOCK);
		combat.turn = bundle.getInt(TURN);
		combat.playerStrength = bundle.getInt(PLAYER_STRENGTH);
		combat.playerTurnStrength = bundle.getInt(PLAYER_TURN_STRENGTH);
		combat.playerTurnDexterity = bundle.contains(PLAYER_TURN_DEXTERITY) ? bundle.getInt(PLAYER_TURN_DEXTERITY) : 0;
		combat.playerDamageReduction = bundle.contains(PLAYER_DAMAGE_REDUCTION) ? bundle.getInt(PLAYER_DAMAGE_REDUCTION) : 0;
		combat.playerBlockReduction = bundle.contains(PLAYER_BLOCK_REDUCTION) ? bundle.getInt(PLAYER_BLOCK_REDUCTION) : 0;
		combat.playerVulnerable = bundle.contains(PLAYER_VULNERABLE) ? bundle.getInt(PLAYER_VULNERABLE) : 0;
		combat.playerWeak = bundle.contains(PLAYER_WEAK) ? bundle.getInt(PLAYER_WEAK) : 0;
		combat.playerDexterity = bundle.contains(PLAYER_DEXTERITY) ? bundle.getInt(PLAYER_DEXTERITY) : 0;
		combat.playerArtifact = bundle.contains(PLAYER_ARTIFACT) ? bundle.getInt(PLAYER_ARTIFACT) : 0;
		combat.shivDamageBonus = bundle.contains(SHIV_DAMAGE_BONUS) ? bundle.getInt(SHIV_DAMAGE_BONUS) : 0;
		combat.firstShivDamageBonus = bundle.contains(FIRST_SHIV_DAMAGE_BONUS) ? bundle.getInt(FIRST_SHIV_DAMAGE_BONUS) : 0;
		combat.shivRetain = bundle.getBoolean(SHIV_RETAIN);
		combat.shivAllEnemies = bundle.contains(SHIV_ALL_ENEMIES) && bundle.getBoolean(SHIV_ALL_ENEMIES);
		combat.infiniteBladesShivs = bundle.contains(INFINITE_BLADES_SHIVS) ? bundle.getInt(INFINITE_BLADES_SHIVS) : 0;
		combat.spinningNailDamageBonus = bundle.contains(SPINNING_NAIL_DAMAGE_BONUS) ? bundle.getInt(SPINNING_NAIL_DAMAGE_BONUS) : 0;
		combat.playerEntangle = bundle.contains(PLAYER_ENTANGLE) ? bundle.getInt(PLAYER_ENTANGLE) : 0;
		combat.playerConsecutiveStrike = bundle.contains(PLAYER_CONSECUTIVE_STRIKE) ? bundle.getInt(PLAYER_CONSECUTIVE_STRIKE) : 0;
		combat.playerThorns = bundle.contains(PLAYER_THORNS) ? bundle.getInt(PLAYER_THORNS) : 0;
		combat.playerPermanentThorns = bundle.contains(PLAYER_PERMANENT_THORNS) ? bundle.getInt(PLAYER_PERMANENT_THORNS) : 0;
		combat.playerBarricade = bundle.contains(PLAYER_BARRICADE) && bundle.getBoolean(PLAYER_BARRICADE);
		combat.playerFirstBlockDouble = bundle.contains(PLAYER_FIRST_BLOCK_DOUBLE) && bundle.getBoolean(PLAYER_FIRST_BLOCK_DOUBLE);
		combat.firstBlockDoubleUsedThisTurn = bundle.contains(FIRST_BLOCK_DOUBLE_USED) && bundle.getBoolean(FIRST_BLOCK_DOUBLE_USED);
		combat.petShopBlockDoubleUsed = bundle.contains(PET_SHOP_BLOCK_DOUBLE_USED) && bundle.getBoolean(PET_SHOP_BLOCK_DOUBLE_USED);
		combat.playerRegen = bundle.contains(PLAYER_REGEN) ? bundle.getInt(PLAYER_REGEN) : 0;
		combat.attackCardsThisTurn = bundle.contains(ATTACK_CARDS_THIS_TURN) ? bundle.getInt(ATTACK_CARDS_THIS_TURN) : 0;
		combat.skillCardsThisTurn = bundle.contains(SKILL_CARDS_THIS_TURN) ? bundle.getInt(SKILL_CARDS_THIS_TURN) : 0;
		combat.powerCardsThisTurn = bundle.contains(POWER_CARDS_THIS_TURN) ? bundle.getInt(POWER_CARDS_THIS_TURN) : 0;
		combat.attackCardsPlayedThisCombat = bundle.contains(ATTACK_CARDS_PLAYED_COMBAT) ? bundle.getInt(ATTACK_CARDS_PLAYED_COMBAT) : 0;
		combat.skillCardsPlayed = bundle.contains(SKILL_CARDS_PLAYED) ? bundle.getInt(SKILL_CARDS_PLAYED) : 0;
		combat.exhaustedCardsPlayedThisCombat = bundle.contains(EXHAUSTED_CARDS_PLAYED_COMBAT) ? bundle.getInt(EXHAUSTED_CARDS_PLAYED_COMBAT) : 0;
		combat.obsidianTriggeredThisTurn = bundle.contains(OBSIDIAN_TRIGGERED_THIS_TURN) && bundle.getBoolean(OBSIDIAN_TRIGGERED_THIS_TURN);
		combat.gambleChipOffered = bundle.contains(GAMBLE_CHIP_OFFERED) && bundle.getBoolean(GAMBLE_CHIP_OFFERED);
		combat.shortKeyUsed = bundle.contains(SHORT_KEY_USED) && bundle.getBoolean(SHORT_KEY_USED);
		combat.currentCardDebuffDoubled = bundle.contains(CURRENT_CARD_DEBUFF_DOUBLED) && bundle.getBoolean(CURRENT_CARD_DEBUFF_DOUBLED);
		combat.potionlessDexterityActive = bundle.contains(POTIONLESS_DEXTERITY_ACTIVE) && bundle.getBoolean(POTIONLESS_DEXTERITY_ACTIVE);
		combat.stGermainUsed = bundle.contains(ST_GERMAIN_USED) && bundle.getBoolean(ST_GERMAIN_USED);
		combat.burningSticksFired = bundle.contains(BURNING_STICKS_FIRED) && bundle.getBoolean(BURNING_STICKS_FIRED);
		combat.cardsPlayedThisTurn = bundle.contains(CARDS_PLAYED_THIS_TURN) ? bundle.getInt(CARDS_PLAYED_THIS_TURN) : 0;
		combat.cardsPlayedThisCombat = bundle.contains(CARDS_PLAYED_THIS_COMBAT) ? bundle.getInt(CARDS_PLAYED_THIS_COMBAT) : combat.cardsPlayedThisTurn;
		combat.ruptureStrengthPerLoss = bundle.contains(RUPTURE_STRENGTH) ? bundle.getInt(RUPTURE_STRENGTH) : 0;
		combat.fireseaDamagePerLoss = bundle.contains(FIRESEA_DAMAGE) ? bundle.getInt(FIRESEA_DAMAGE) : 0;
		combat.poisonCoatDamage = bundle.contains(POISON_COAT_DAMAGE) ? bundle.getInt(POISON_COAT_DAMAGE) : 0;
		combat.playerHPLostCountThisTurn = bundle.contains(HP_LOST_THIS_TURN) ? bundle.getInt(HP_LOST_THIS_TURN) : 0;
		combat.playerHPLostCountThisCombat = bundle.contains(HP_LOST_THIS_COMBAT) ? bundle.getInt(HP_LOST_THIS_COMBAT) : 0;
		combat.bonusEnergyTurns = bundle.contains(BONUS_ENERGY_TURNS) ? bundle.getInt(BONUS_ENERGY_TURNS) : 0;
		combat.bonusDrawTurns = bundle.contains(BONUS_DRAW_TURNS) ? bundle.getInt(BONUS_DRAW_TURNS) : 0;
		combat.nextTurnBonusDraw = bundle.contains(NEXT_TURN_BONUS_DRAW) ? bundle.getInt(NEXT_TURN_BONUS_DRAW) : 0;
		combat.retainHandTurns = bundle.contains(RETAIN_HAND_TURNS) ? bundle.getInt(RETAIN_HAND_TURNS) : 0;
		combat.duplicateNextCards = bundle.contains(DUPLICATE_NEXT_CARDS) ? bundle.getInt(DUPLICATE_NEXT_CARDS) : 0;
		combat.surgeActive = bundle.contains(SURGE_ACTIVE) && bundle.getBoolean(SURGE_ACTIVE);
		combat.nextAttackDamageMultiplier = bundle.contains(NEXT_ATTACK_DAMAGE_MULTIPLIER) ? bundle.getInt(NEXT_ATTACK_DAMAGE_MULTIPLIER) : 0;
		combat.incomingDamageReductionTurns = bundle.contains(INCOMING_DAMAGE_REDUCTION_TURNS) ? bundle.getInt(INCOMING_DAMAGE_REDUCTION_TURNS) : 0;
		combat.strengthPerTurn = bundle.contains(STRENGTH_PER_TURN) ? bundle.getInt(STRENGTH_PER_TURN) : 0;
		combat.nextTurnBlock = bundle.contains(NEXT_TURN_BLOCK) ? bundle.getInt(NEXT_TURN_BLOCK) : 0;
		combat.preventHpLossTurns = bundle.contains(PREVENT_HP_LOSS_TURNS) ? bundle.getInt(PREVENT_HP_LOSS_TURNS) : 0;
		combat.playerBlessed = bundle.contains(PLAYER_BLESSED) ? bundle.getInt(PLAYER_BLESSED) : 0;
		combat.pendingHandCardToDrawPileTop = bundle.contains(PENDING_HAND_CARD_TO_DRAW_TOP) && bundle.getBoolean(PENDING_HAND_CARD_TO_DRAW_TOP);
		combat.blockFromCardDisabledTurns = bundle.contains(BLOCK_FROM_CARD_DISABLED_TURNS) ? bundle.getInt(BLOCK_FROM_CARD_DISABLED_TURNS) : 0;
		combat.pendingAllCardDiscover = bundle.contains(PENDING_ALL_CARD_DISCOVER) && bundle.getBoolean(PENDING_ALL_CARD_DISCOVER);
		combat.pendingAllRelicDiscover = bundle.contains(PENDING_ALL_RELIC_DISCOVER) && bundle.getBoolean(PENDING_ALL_RELIC_DISCOVER);
		combat.pendingAllPotionDiscover = bundle.contains(PENDING_ALL_POTION_DISCOVER) && bundle.getBoolean(PENDING_ALL_POTION_DISCOVER);
		combat.pendingDrawPilePeek = bundle.contains(PENDING_DRAW_PILE_PEEK) && bundle.getBoolean(PENDING_DRAW_PILE_PEEK);
		if (bundle.contains(PENDING_DRAW_PILE_TYPE_SELECT)) {
			int type = bundle.getInt(PENDING_DRAW_PILE_TYPE_SELECT);
			DeckCardType[] types = DeckCardType.values();
			combat.pendingDrawPileTypeSelect = type >= 0 && type < types.length ? types[type] : null;
		}
		combat.pendingDiscardHandSelectCount = bundle.contains(PENDING_DISCARD_HAND_SELECT_COUNT) ? bundle.getInt(PENDING_DISCARD_HAND_SELECT_COUNT) : 0;
		combat.pendingDaggerThrowDiscard = bundle.contains(PENDING_DAGGER_THROW_DISCARD) && bundle.getBoolean(PENDING_DAGGER_THROW_DISCARD);
		combat.pendingHandDiscardSelectCount = bundle.contains(PENDING_HAND_DISCARD_SELECT_COUNT) ? bundle.getInt(PENDING_HAND_DISCARD_SELECT_COUNT) : (combat.pendingDaggerThrowDiscard ? 1 : 0);
		if (bundle.contains(PULSING_AXE_RETURNS)) {
			restoreList(combat.pulsingAxeReturns, bundle, PULSING_AXE_RETURNS);
		}
		combat.impostingPresenceDamage = bundle.contains(IMPOSTING_PRESENCE_DAMAGE) ? bundle.getInt(IMPOSTING_PRESENCE_DAMAGE) : 0;
		combat.snakeFormDamage = bundle.contains(SNAKE_FORM_DAMAGE) ? bundle.getInt(SNAKE_FORM_DAMAGE) : 0;
		combat.guardBlockBonus = bundle.contains(GUARD_BLOCK_BONUS) ? bundle.getInt(GUARD_BLOCK_BONUS) : 0;
		combat.automationCount = bundle.contains(AUTOMATION_COUNT) ? bundle.getInt(AUTOMATION_COUNT) : 0;
		combat.automationDrawsSinceLastTrigger = bundle.contains(AUTOMATION_DRAWS_COUNTER) ? bundle.getInt(AUTOMATION_DRAWS_COUNTER) : 0;
		combat.stratagemCount = bundle.contains(STRATAGEM_COUNT) ? bundle.getInt(STRATAGEM_COUNT) : 0;
		combat.pendingStratagemSelect = bundle.contains(PENDING_STRATAGEM_SELECT) && bundle.getBoolean(PENDING_STRATAGEM_SELECT);
		combat.entropyCount = bundle.contains(ENTROPY_COUNT) ? bundle.getInt(ENTROPY_COUNT) : 0;
		combat.nostalgiaActive = bundle.contains(NOSTALGIA_ACTIVE) && bundle.getBoolean(NOSTALGIA_ACTIVE);
		combat.nostalgiaUsedThisTurn = bundle.contains(NOSTALGIA_USED_THIS_TURN) && bundle.getBoolean(NOSTALGIA_USED_THIS_TURN);
		combat.chaosCount = bundle.contains(CHAOS_COUNT) ? bundle.getInt(CHAOS_COUNT) : 0;
		combat.rollingBoulderCurrentDamage = bundle.contains(ROLLING_BOULDER_DAMAGE) ? bundle.getInt(ROLLING_BOULDER_DAMAGE) : 0;
		combat.numbnessBlock = bundle.contains(NUMBNESS_BLOCK) ? bundle.getInt(NUMBNESS_BLOCK) : 0;
		combat.cardsExhaustedThisTurn = bundle.contains(CARDS_EXHAUSTED_THIS_TURN) ? bundle.getInt(CARDS_EXHAUSTED_THIS_TURN) : 0;
		combat.darkEmbraceDraw = bundle.contains(DARK_EMBRACE_DRAW) ? bundle.getInt(DARK_EMBRACE_DRAW) : 0;
		combat.dieOnUnblockedAttack = bundle.contains(DIE_ON_UNBLOCKED_ATTACK) && bundle.getBoolean(DIE_ON_UNBLOCKED_ATTACK);
		combat.mentalOverflowDemise = bundle.contains(MENTAL_OVERFLOW_DEMISE) ? bundle.getInt(MENTAL_OVERFLOW_DEMISE) : 0;
		combat.domainCount = bundle.contains(DOMAIN_COUNT) ? bundle.getInt(DOMAIN_COUNT) : 0;
		combat.toolsOfTheTradeCount = bundle.contains(TOOLS_OF_THE_TRADE_COUNT) ? bundle.getInt(TOOLS_OF_THE_TRADE_COUNT) : 0;
		combat.machineLearningCount = bundle.contains(MACHINE_LEARNING_COUNT) ? bundle.getInt(MACHINE_LEARNING_COUNT) : 0;
		combat.paleBlueDotDraw = bundle.contains(PALE_BLUE_DOT_DRAW) ? bundle.getInt(PALE_BLUE_DOT_DRAW) : 0;
		combat.dictatorshipCount = bundle.contains(DICTATORSHIP_COUNT) ? bundle.getInt(DICTATORSHIP_COUNT) : 0;
		combat.drawDisabledThisTurn = bundle.contains(DRAW_DISABLED_THIS_TURN) && bundle.getBoolean(DRAW_DISABLED_THIS_TURN);
		combat.pendingHandExhaustSelectCount = bundle.contains(PENDING_HAND_EXHAUST_SELECT_COUNT) ? bundle.getInt(PENDING_HAND_EXHAUST_SELECT_COUNT) : 0;
		combat.nextTurnEnergy = bundle.contains(NEXT_TURN_ENERGY) ? bundle.getInt(NEXT_TURN_ENERGY) : 0;
		combat.deathDanceBlock = bundle.contains(DEATH_DANCE_BLOCK) ? bundle.getInt(DEATH_DANCE_BLOCK) : 0;
		combat.preserveBlockNextTurn = bundle.contains(PRESERVE_BLOCK_NEXT_TURN) && bundle.getBoolean(PRESERVE_BLOCK_NEXT_TURN);
		combat.shadowBlockDouble = bundle.contains(SHADOW_BLOCK_DOUBLE) && bundle.getBoolean(SHADOW_BLOCK_DOUBLE);
		combat.afterimageBlock = bundle.contains(AFTERIMAGE_BLOCK) ? bundle.getInt(AFTERIMAGE_BLOCK) : 0;
		combat.rageBlock = bundle.contains(RAGE_BLOCK) ? bundle.getInt(RAGE_BLOCK) : 0;
		combat.absolutePowerDamage = bundle.contains(ABSOLUTE_POWER_DAMAGE) ? bundle.getInt(ABSOLUTE_POWER_DAMAGE) : 0;
		combat.pendingDiscardToDrawPileTop = bundle.contains(PENDING_DISCARD_TO_DRAW_TOP) && bundle.getBoolean(PENDING_DISCARD_TO_DRAW_TOP);
		combat.cardsDrawnThisTurn = bundle.contains(CARDS_DRAWN_THIS_TURN) ? bundle.getInt(CARDS_DRAWN_THIS_TURN) : 0;
		combat.nextSkillZeroCost = bundle.contains(NEXT_SKILL_ZERO_COST) && bundle.getBoolean(NEXT_SKILL_ZERO_COST);
		combat.preciseShotActive = bundle.contains(PRECISE_SHOT_ACTIVE) && bundle.getBoolean(PRECISE_SHOT_ACTIVE);
		combat.preciseShotSkillDiscount = bundle.contains(PRECISE_SHOT_SKILL_DISCOUNT) ? bundle.getInt(PRECISE_SHOT_SKILL_DISCOUNT) : 0;
		combat.cardsDrawnThisCombat = bundle.contains(CARDS_DRAWN_THIS_COMBAT) ? bundle.getInt(CARDS_DRAWN_THIS_COMBAT) : 0;
		combat.gougeDamageBonus = bundle.contains(GOUGE_DAMAGE_BONUS) ? bundle.getInt(GOUGE_DAMAGE_BONUS) : 0;
		combat.nextPowerZeroCost = bundle.contains(NEXT_POWER_ZERO_COST) && bundle.getBoolean(NEXT_POWER_ZERO_COST);
		combat.energySpentThisTurn = bundle.contains(ENERGY_SPENT_THIS_TURN) ? bundle.getInt(ENERGY_SPENT_THIS_TURN) : 0;
		combat.nextAttackZeroCost = bundle.contains(NEXT_ATTACK_ZERO_COST) && bundle.getBoolean(NEXT_ATTACK_ZERO_COST);
		combat.cardCostIncreaseThisTurn = bundle.contains(CARD_COST_INCREASE_THIS_TURN) ? bundle.getInt(CARD_COST_INCREASE_THIS_TURN) : 0;
		combat.energyGainDisabledThisTurn = bundle.contains(ENERGY_GAIN_DISABLED_THIS_TURN) && bundle.getBoolean(ENERGY_GAIN_DISABLED_THIS_TURN);
		combat.friendshipEnergy = bundle.contains(FRIENDSHIP_ENERGY) ? bundle.getInt(FRIENDSHIP_ENERGY) : 0;
		combat.subroutineEnergy = bundle.contains(SUBROUTINE_ENERGY) ? bundle.getInt(SUBROUTINE_ENERGY) : 0;
		combat.orbitCount = bundle.contains(ORBIT_COUNT) ? bundle.getInt(ORBIT_COUNT) : 0;
		combat.orbitProgress = bundle.contains(ORBIT_PROGRESS) ? bundle.getInt(ORBIT_PROGRESS) : 0;
		combat.heartOfFireEnergy = bundle.contains(HEART_OF_FIRE_ENERGY) ? bundle.getInt(HEART_OF_FIRE_ENERGY) : 0;
		combat.royaltyGold = bundle.contains(ROYALTY_GOLD) ? bundle.getInt(ROYALTY_GOLD) : 0;
		combat.royaltyGoldApplied = bundle.contains(ROYALTY_GOLD_APPLIED) && bundle.getBoolean(ROYALTY_GOLD_APPLIED);
		combat.voidFormFreeCards = bundle.contains(VOID_FORM_FREE_CARDS) ? bundle.getInt(VOID_FORM_FREE_CARDS) : 0;
		combat.voidFormFreeCardsThisTurn = bundle.contains(VOID_FORM_FREE_CARDS_THIS_TURN) ? bundle.getInt(VOID_FORM_FREE_CARDS_THIS_TURN) : 0;
		combat.endTurnRequested = bundle.contains(END_TURN_REQUESTED) && bundle.getBoolean(END_TURN_REQUESTED);
		if (bundle.contains(ORANGE_BOMB_TIMERS)) {
			restoreList(combat.orangeBombTimers, bundle, ORANGE_BOMB_TIMERS);
			restoreList(combat.orangeBombDamages, bundle, ORANGE_BOMB_DAMAGES);
		}
		if (bundle.contains(PENDING_DISCOVER)) {
			int[] ids = bundle.getIntArray(PENDING_DISCOVER);
			combat.pendingDiscoverChoices = new DeckCard[ids.length];
			for (int i = 0; i < ids.length; i++) combat.pendingDiscoverChoices[i] = DeckCard.byId(ids[i]);
			int mods = bundle.contains(PENDING_DISCOVER_MODS) ? bundle.getInt(PENDING_DISCOVER_MODS) : 0;
			combat.pendingDiscoverZeroCost = (mods & 1) != 0;
			combat.pendingDiscoverPlayAfterPick = (mods & 2) != 0;
			combat.pendingDiscoverTransient = (mods & 4) != 0;
			combat.pendingDiscoverUpgraded = (mods & 8) != 0;
		}
		restoreList(combat.drawPile, bundle, DRAW_PILE);
		restoreList(combat.hand, bundle, HAND);
		restoreList(combat.discardPile, bundle, DISCARD_PILE);
		restoreList(combat.exhaustPile, bundle, EXHAUST_PILE);
		restoreList(combat.powersPlayed, bundle, POWERS_PLAYED);

		int[] enemyKinds = bundle.getIntArray(ENEMY_KINDS);
		int[] enemyHt = bundle.contains(ENEMY_HT) ? bundle.getIntArray(ENEMY_HT) : new int[0];
		int[] enemyHp = bundle.contains(ENEMY_HP) ? bundle.getIntArray(ENEMY_HP) : new int[0];
		int[] enemyIntents = bundle.contains(ENEMY_INTENTS) ? bundle.getIntArray(ENEMY_INTENTS) : new int[0];
		int[] enemyVulnerable = bundle.contains(ENEMY_VULNERABLE) ? bundle.getIntArray(ENEMY_VULNERABLE) : new int[0];
		int[] enemyAttackDown = bundle.contains(ENEMY_ATTACK_DOWN) ? bundle.getIntArray(ENEMY_ATTACK_DOWN) : new int[0];
		int[] enemyStrength = bundle.contains(ENEMY_STRENGTH) ? bundle.getIntArray(ENEMY_STRENGTH) : new int[0];
		int[] enemyTurnStrengthLoss = bundle.contains(ENEMY_TURN_STRENGTH_LOSS) ? bundle.getIntArray(ENEMY_TURN_STRENGTH_LOSS) : new int[0];
		int[] enemyBlock = bundle.contains(ENEMY_BLOCK) ? bundle.getIntArray(ENEMY_BLOCK) : new int[0];
		int[] enemyThorns = bundle.contains(ENEMY_THORNS) ? bundle.getIntArray(ENEMY_THORNS) : new int[0];
		int[] enemyPlatedArmor = bundle.contains(ENEMY_PLATED_ARMOR) ? bundle.getIntArray(ENEMY_PLATED_ARMOR) : new int[0];
		int[] enemyArtifact = bundle.contains(ENEMY_ARTIFACT) ? bundle.getIntArray(ENEMY_ARTIFACT) : new int[0];
		int[] enemyTricky = bundle.contains(ENEMY_TRICKY) ? bundle.getIntArray(ENEMY_TRICKY) : new int[0];
		int[] enemyBlockReduction = bundle.contains(ENEMY_BLOCK_REDUCTION) ? bundle.getIntArray(ENEMY_BLOCK_REDUCTION) : new int[0];
		int[] enemyVenom = bundle.contains(ENEMY_VENOM) ? bundle.getIntArray(ENEMY_VENOM) : new int[0];
		int[] enemyDemise = bundle.contains(ENEMY_DEMISE) ? bundle.getIntArray(ENEMY_DEMISE) : new int[0];
		int[] enemyPersistentDamage = bundle.contains(ENEMY_PERSISTENT_DAMAGE) ? bundle.getIntArray(ENEMY_PERSISTENT_DAMAGE) : new int[0];
		int[] enemyRitual = bundle.contains(ENEMY_RITUAL) ? bundle.getIntArray(ENEMY_RITUAL) : new int[0];
		int[] enemyDarkSpace = bundle.contains(ENEMY_DARK_SPACE) ? bundle.getIntArray(ENEMY_DARK_SPACE) : new int[0];
		int[] enemyLastIntent = bundle.contains(ENEMY_LAST_INTENT) ? bundle.getIntArray(ENEMY_LAST_INTENT) : new int[0];
		boolean[] enemySplitUsed = bundle.contains(ENEMY_SPLIT_USED) ? bundle.getBooleanArray(ENEMY_SPLIT_USED) : new boolean[0];
		int[] enemyBlessed = bundle.contains(ENEMY_BLESSED) ? bundle.getIntArray(ENEMY_BLESSED) : new int[0];
		int[] enemyDebuffDoubleTurns = bundle.contains(ENEMY_DEBUFF_DOUBLE_TURNS) ? bundle.getIntArray(ENEMY_DEBUFF_DOUBLE_TURNS) : new int[0];
		int[] enemyStrangleHpLoss = bundle.contains(ENEMY_STRANGLE_HP_LOSS) ? bundle.getIntArray(ENEMY_STRANGLE_HP_LOSS) : new int[0];
		DeckEnemy[] allEnemies = DeckEnemy.values();
		for (int i = 0; i < enemyKinds.length; i++) {
			int kindIndex = enemyKinds[i];
			if (kindIndex < 0 || kindIndex >= allEnemies.length) continue;
			DeckCombatEnemy enemy = new DeckCombatEnemy(allEnemies[kindIndex], combat.depth);
			if (i < enemyHt.length) enemy.ht = enemyHt[i];
			if (i < enemyHp.length) enemy.hp = enemyHp[i];
			if (i < enemyIntents.length) enemy.intent = enemyIntents[i];
			if (i < enemyVulnerable.length) enemy.vulnerable = enemyVulnerable[i];
			if (i < enemyAttackDown.length) enemy.attackDown = enemyAttackDown[i];
			if (i < enemyStrength.length) enemy.strength = enemyStrength[i];
			if (i < enemyTurnStrengthLoss.length) enemy.turnStrengthLoss = enemyTurnStrengthLoss[i];
			if (i < enemyBlock.length) enemy.block = enemyBlock[i];
			if (i < enemyThorns.length) enemy.thorns = enemyThorns[i];
			if (i < enemyPlatedArmor.length) enemy.platedArmor = enemyPlatedArmor[i];
			if (i < enemyArtifact.length) enemy.artifact = enemyArtifact[i];
			if (i < enemyTricky.length) enemy.tricky = enemyTricky[i];
			if (i < enemyBlockReduction.length) enemy.blockReduction = enemyBlockReduction[i];
			if (i < enemyVenom.length) enemy.venom = enemyVenom[i];
			if (i < enemyDemise.length) enemy.demise = enemyDemise[i];
			if (i < enemyPersistentDamage.length) enemy.persistentDamage = enemyPersistentDamage[i];
			if (i < enemyRitual.length) enemy.ritual = enemyRitual[i];
			if (i < enemyDarkSpace.length) enemy.darkSpace = enemyDarkSpace[i];
			if (i < enemyLastIntent.length) enemy.lastIntent = enemyLastIntent[i];
			if (i < enemySplitUsed.length) enemy.splitUsed = enemySplitUsed[i];
			if (i < enemyBlessed.length) enemy.blessed = enemyBlessed[i];
			if (i < enemyDebuffDoubleTurns.length) enemy.debuffDoubleTurns = enemyDebuffDoubleTurns[i];
			if (i < enemyStrangleHpLoss.length) enemy.strangleHpLoss = enemyStrangleHpLoss[i];
			combat.enemies.add(enemy);
		}
		if (combat.enemies.isEmpty()) {
			combat.enemies.add(new DeckCombatEnemy(DeckEnemy.forNode(combat.nodeType), combat.depth));
		}
		combat.sanitizeTarget();
		return combat;
	}

	public void startTurnState() {
		turn++;
		cardsExhaustedThisTurn = 0;
		cardsDrawnThisTurn = 0;
		drawDisabledThisTurn = false;
		cardCostIncreaseThisTurn = 0;
		energyGainDisabledThisTurn = false;
		voidFormFreeCardsThisTurn = voidFormFreeCards;
		int retainedEnergy = DeckBuilderRun.hasRelic(DeckRelic.CHERRY_DECORATION) ? energy : 0;
		energy = Math.min(DeckBuilderRun.MAX_ENERGY_CAP, maxEnergy + retainedEnergy);
		if (DeckBuilderRun.hasRelic(DeckRelic.SUPER_AJA)) {
			energy = Math.max(0, energy + (turn == 1 ? -1 : 1));
		}
		if (DeckBuilderRun.hasRelic(DeckRelic.DRAGONS_DREAM)) {
			energy = Math.min(DeckBuilderRun.MAX_ENERGY_CAP, energy + 1);
		}
		if (bonusEnergyTurns > 0) {
			energy = Math.min(DeckBuilderRun.MAX_ENERGY_CAP, energy + 1);
			bonusEnergyTurns--;
		}
		if (nextTurnEnergy > 0) {
			gainEnergy(nextTurnEnergy);
			nextTurnEnergy = 0;
		}
		if (domainCount > 0) energy = Math.min(DeckBuilderRun.MAX_ENERGY_CAP, energy + domainCount);
		if (friendshipEnergy > 0) gainEnergy(friendshipEnergy);
		if (heartOfFireEnergy > 0) gainEnergy(heartOfFireEnergy);
		if (mentalOverflowDemise > 0) loseHP(mentalOverflowDemise);
		if (strengthPerTurn > 0) playerStrength += strengthPerTurn;
		if (preserveBlockNextTurn) {
			preserveBlockNextTurn = false;
		} else if (!playerBarricade) {
			block = DeckBuilderRun.hasRelic(DeckRelic.OLD_WORKMAN) ? Math.min(block, 10) : 0;
		}
		int bloodyCloakBlock = activePowerBlock(DeckCard.BLOODY_CLOAK);
		if (bloodyCloakBlock > 0) {
			loseHP(1);
			gainBlock(bloodyCloakBlock);
		}
		if (nextTurnBlock > 0) {
			gainBlock(nextTurnBlock);
			nextTurnBlock = 0;
		}
		if (!pulsingAxeReturns.isEmpty()) {
			for (int code : pulsingAxeReturns) {
				addToHand(code);
			}
			pulsingAxeReturns.clear();
		}
		for (int i = 0; i < infiniteBladesShivs; i++) {
			addShivToHand(false);
		}
		firstBlockDoubleUsedThisTurn = false;
		playerTurnStrength = 0;
		playerTurnDexterity = 0;
		attackCardsThisTurn = 0;
		skillCardsThisTurn = 0;
		powerCardsThisTurn = 0;
		preciseShotActive = false;
		preciseShotSkillDiscount = 0;
		energySpentThisTurn = 0;
		obsidianTriggeredThisTurn = false;
		cardsPlayedThisTurn = 0;
		playerHPLostCountThisTurn = 0;
		firstShivUsed = false;
		nostalgiaUsedThisTurn = false;
		if (playerVulnerable > 0) playerVulnerable--;
		if (playerWeak > 0) playerWeak--;
		if (playerBlessed > 0) playerBlessed--;
		syncPotionlessDexterity();
		lastDamageEvents.clear();
		lastAutoPlayResults.clear();
		playBombardmentsFromExhaustPile();
		for (DeckCombatEnemy enemy : enemies) {
			if (enemy.alive()) {
				enemy.intent = enemy.kind.nextIntent(enemy, turn, depth, enemyIndex(enemy));
			}
		}
		sanitizeTarget();
	}

	public void drawTurnHand() {
		lastEntropyTransformCount = 0;
		lastRollingBoulderDamage = 0;
		if (rollingBoulderCurrentDamage > 0) {
			for (DeckCombatEnemy enemy : aliveEnemies()) {
				lastRollingBoulderDamage += damageEnemy(enemy, rollingBoulderCurrentDamage, false);
			}
			rollingBoulderCurrentDamage += 8;
		}
		draw(handSize);
		if (bonusDrawTurns > 0) {
			draw(1);
			bonusDrawTurns--;
		}
		if (domainCount > 0) draw(domainCount);
		if (toolsOfTheTradeCount > 0) {
			for (int i = 0; i < toolsOfTheTradeCount; i++) {
				if (draw(1)) pendingHandDiscardSelectCount++;
			}
		}
		if (machineLearningCount > 0) draw(machineLearningCount);
		if (dictatorshipCount > 0) {
			for (int i = 0; i < dictatorshipCount; i++) {
				if (draw(1) && !hand.isEmpty()) pendingHandExhaustSelectCount++;
			}
		}
		if (nextTurnBonusDraw > 0) {
			draw(nextTurnBonusDraw);
			nextTurnBonusDraw = 0;
		}
		if (fireseaDamagePerLoss > 0) loseHP(1);
		if (turn == 1) {
			if (nodeType == DeckBuilderMap.ELITE && DeckBuilderRun.hasRelic(DeckRelic.BOOMING_CONCH)) {
				draw(2);
				energy = Math.min(DeckBuilderRun.MAX_ENERGY_CAP, energy + 1);
			}
			if (DeckBuilderRun.hasRelic(DeckRelic.BAG_OF_MARBLES)) {
				for (DeckCombatEnemy enemy : enemies) {
					if (enemy.alive() && applyEnemyDebuff(enemy)) enemy.vulnerable++;
				}
			}
			if (DeckBuilderRun.hasRelic(DeckRelic.BLOCKADE_COMMAND_DISC)) playerDexterity++;
			if (DeckBuilderRun.hasRelic(DeckRelic.FEAR_COMMAND_DISC)) {
				for (DeckCombatEnemy enemy : enemies) {
					if (enemy.alive() && applyEnemyDebuff(enemy)) enemy.attackDown++;
				}
			}
			if (DeckBuilderRun.hasRelic(DeckRelic.RECHARGE_COMMAND_DISC)) draw(2);
			if (DeckBuilderRun.hasRelic(DeckRelic.EQUIVALENT_EXCHANGE_COMMAND_DISC)) {
			playerPermanentThorns += 3;
			playerThorns += 3;
		}
			if (DeckBuilderRun.hasRelic(DeckRelic.EXPLOSION_COMMAND_DISC)) {
				for (DeckCombatEnemy enemy : aliveEnemies()) {
					damageEnemy(enemy, 9, false);
				}
			}
			if (DeckBuilderRun.hasRelic(DeckRelic.LEATHER_POUCH)) {
				DeckBuilderRun.playerHP = Math.min(DeckBuilderRun.playerHT, DeckBuilderRun.playerHP + 2);
			}
			if (nodeType == DeckBuilderMap.BOSS && DeckBuilderRun.hasRelic(DeckRelic.EMPORIO_MEMORY_DISC)) {
				DeckBuilderRun.playerHP = Math.min(DeckBuilderRun.playerHT, DeckBuilderRun.playerHP + 25);
			}
			if (DeckBuilderRun.hasRelic(DeckRelic.STRENGTH_STONE_MASK)) playerTurnStrength += 5;
			if (nodeType == DeckBuilderMap.ELITE && DeckBuilderRun.hasRelic(DeckRelic.POLPO_LIGHTER)) playerStrength += 2;
			if (DeckBuilderRun.hasRelic(DeckRelic.VAJRA)) playerStrength++;
			if (DeckBuilderRun.hasRelic(DeckRelic.ANCHOR)) gainBlock(10);
			if (DeckBuilderRun.hasRelic(DeckRelic.LANTERN)) energy = Math.min(DeckBuilderRun.MAX_ENERGY_CAP, energy + 1);
			if (DeckBuilderRun.nextCombatBonusEnergy > 0) {
				energy = Math.min(DeckBuilderRun.MAX_ENERGY_CAP, energy + DeckBuilderRun.nextCombatBonusEnergy);
				DeckBuilderRun.nextCombatBonusEnergy = 0;
			}
			if (DeckBuilderRun.hasRelic(DeckRelic.GORGET)) playerRegen += 4;
			if (DeckBuilderRun.hasRelic(DeckRelic.TOOLBOX) && pendingDiscoverChoices == null) {
				pendingDiscoverChoices = new DeckDiscover(DeckDiscover.Pool.NEUTRAL_ONLY).rollChoices(this);
				pendingDiscoverZeroCost = false;
				pendingDiscoverTransient = false;
				pendingDiscoverUpgraded = false;
				pendingDiscoverPlayAfterPick = false;
			}
			if (DeckBuilderRun.hasRelic(DeckRelic.HEY_YA)) {
				moveRandomDrawPileCardToHand(true);
			}
			if (DeckBuilderRun.hasRelic(DeckRelic.TOPAZ)) {
				for (int i = 0; i < hand.size(); i++) {
					hand.set(i, DeckCardCode.upgrade(hand.get(i)));
				}
			}
			if (DeckBuilderRun.hasRelic(DeckRelic.DONUT)) playerArtifact++;
			if (DeckBuilderRun.hasRelic(DeckRelic.MODIFIED_DISC)) {
				addRandomTransientCardToHand();
				addRandomTransientCardToHand();
			}
			if (DeckBuilderRun.hasRelic(DeckRelic.DRAGONS_DREAM)) {
				for (DeckCombatEnemy enemy : enemies) {
					if (enemy.alive()) enemy.strength++;
				}
			}
		}
		if (turn == 2 && DeckBuilderRun.hasRelic(DeckRelic.CHARGE_STONE_MASK)) energy = Math.min(DeckBuilderRun.MAX_ENERGY_CAP, energy + 1);
		if (turn == 2 && DeckBuilderRun.hasRelic(DeckRelic.VALENTINE_MEMORY_DISC)) gainBlock(14);
		if (turn == 3 && DeckBuilderRun.hasRelic(DeckRelic.GOO_GOO_DOLLS)) gainBlock(18);
		if (turn == 3 && DeckBuilderRun.hasRelic(DeckRelic.NIGHT_RULER)) energy = Math.min(DeckBuilderRun.MAX_ENERGY_CAP, energy + 1);
		if (turn == 3 && DeckBuilderRun.hasRelic(DeckRelic.GYRO_MEMORY_DISC)) {
			playerStrength++;
			playerDexterity++;
		}
		if (turn > 1 && turn % 3 == 0 && DeckBuilderRun.hasRelic(DeckRelic.OBSERVATION_COMMAND_DISC)) draw(1);
		if (turn > 1 && turn % 3 == 0 && DeckBuilderRun.hasRelic(DeckRelic.ENERGY_GRANT_COMMAND_DISC)) {
			energy = Math.min(DeckBuilderRun.MAX_ENERGY_CAP, energy + 1);
		}
		if (turn > 1 && turn % 3 == 0 && DeckBuilderRun.hasRelic(DeckRelic.SEVERED_WOMAN_HAND)) {
			for (DeckCombatEnemy enemy : enemies) {
				if (enemy.alive() && applyEnemyDebuff(enemy)) enemy.attackDown++;
			}
		}
		if (DeckBuilderRun.hasRelic(DeckRelic.MIYAMOTO_MEMORY_DISC)) {
			for (DeckCombatEnemy enemy : aliveEnemies()) {
				damageEnemy(enemy, 3, false);
			}
		}
		if (entropyCount > 0 && !hand.isEmpty()) {
			DeckCard[] pool = DeckCardPool.rewardPool(DeckBuilderRun.heroClass(), false, false);
			if (pool != null && pool.length > 0) {
				for (int i = 0; i < entropyCount && i < hand.size(); i++) {
					int idx = Random.Int(hand.size());
					hand.set(idx, pool[Random.Int(pool.length)].code());
					lastEntropyTransformCount++;
				}
			}
		}
		if (chaosCount > 0) {
			playTopFromDrawPile(chaosCount);
		}
		DeckWandCards.triggerTurnStartWands(this);
	}

	public void startTurn() {
		startTurnState();
		drawTurnHand();
	}

	public DeckPlayResult play(int handIndex) {
		lastAutoPlayResults.clear();
		lastDamageEvents.clear();
		return play(handIndex, false, -1);
	}

	public DeckPlayResult play(int handIndex, int targetWandIndex) {
		lastAutoPlayResults.clear();
		lastDamageEvents.clear();
		return play(handIndex, false, targetWandIndex);
	}

	private DeckPlayResult play(int handIndex, boolean castOnDraw) {
		return play(handIndex, castOnDraw, -1);
	}

	private DeckPlayResult play(int handIndex, boolean castOnDraw, int targetWandIndex) {
		if (handIndex < 0 || handIndex >= hand.size()) return DeckPlayResult.INVALID;
		currentPlayHandIndexShift = 0;
		int cardCode = hand.get(handIndex);
		DeckCard card = DeckCard.byCode(cardCode);
		int cost = cardCost(cardCode);
		boolean consumesNextAttackZeroCost = !castOnDraw && card.type == DeckCardType.ATTACK && nextAttackZeroCost;
		boolean consumesVoidFormFreeCard = !castOnDraw && voidFormFreeCardsThisTurn > 0;
		boolean xCost = isXCost(cardCode);
		int xValue = xCost ? (consumesVoidFormFreeCard ? 0 : energy + (DeckBuilderRun.hasRelic(DeckRelic.CHEMICAL_X) ? 2 : 0)) : 0;
		int spentEnergy = xCost ? (consumesVoidFormFreeCard ? 0 : energy) : cost;
		if (!castOnDraw && card.unplayable(cardCode)) return DeckPlayResult.INVALID;
		if (!castOnDraw && card == DeckCard.END_OF_PACT && exhaustPile.size() < 3) return DeckPlayResult.INVALID;
		if (!castOnDraw && cost > energy) return DeckPlayResult.INVALID;
		if (!castOnDraw && cardsPlayedThisTurn >= 3 && hasCardActiveInCombat(DeckCard.RULE_COMPLIANCE)) return DeckPlayResult.INVALID;

		if (!castOnDraw) {
			energy = xCost && !consumesVoidFormFreeCard ? 0 : energy - cost;
			energySpentThisTurn += xCost ? xValue : cost;
			triggerOrbitEnergy(spentEnergy);
			cardsPlayedThisTurn++;
			cardsPlayedThisCombat++;
			if (consumesNextAttackZeroCost) nextAttackZeroCost = false;
			if (consumesVoidFormFreeCard) voidFormFreeCardsThisTurn--;
		}

		if (!castOnDraw && card.type == DeckCardType.ATTACK
				&& DeckBuilderRun.hasRelic(DeckRelic.SNIPING_STONE_MASK)
				&& attackCardsPlayedThisCombat + 1 == 10) {
			nextAttackDamageMultiplier = Math.max(nextAttackDamageMultiplier, 2);
		}

		boolean aimActive = card.hasKeyword(cardCode, DeckCardKeyword.AIM) && isCenterHandIndex(handIndex);
		boolean throwActive = card.hasKeyword(cardCode, DeckCardKeyword.THROW) && isEdgeHandIndex(handIndex);
		int effectiveCardCode = card.effectiveCodeForPlay(cardCode, this, handIndex);
		currentCardDebuffDoubled = !castOnDraw
				&& DeckBuilderRun.hasRelic(DeckRelic.SHORT_KEY_NO_2)
				&& !shortKeyUsed
				&& cardHasEnemyDebuff(card, effectiveCardCode);
		if (currentCardDebuffDoubled) shortKeyUsed = true;
		int[] strangleBeforePlay = new int[enemies.size()];
		for (int i = 0; i < enemies.size(); i++) {
			strangleBeforePlay[i] = enemies.get(i).strangleHpLoss;
		}
		playKillCount = 0;
		DeckPlayResult.Builder result = new DeckPlayResult.Builder(card);
		for (DeckCardEffect effect : card.effects(cardCode)) {
			effect.apply(new DeckCardPlayContext(this, card, cardCode, effectiveCardCode, handIndex, castOnDraw, aimActive, throwActive, result, targetWandIndex, xValue));
		}
		if (!castOnDraw && duplicateNextCards > 0) {
			duplicateNextCards--;
			result.nextWave();
			for (DeckCardEffect effect : card.effects(cardCode)) {
				effect.apply(new DeckCardPlayContext(this, card, cardCode, effectiveCardCode, handIndex, true, aimActive, throwActive, result, targetWandIndex, xValue));
			}
		}
		currentCardDebuffDoubled = false;
		if (!castOnDraw && card.type == DeckCardType.ATTACK && nextAttackDamageMultiplier > 1) {
			nextAttackDamageMultiplier = 0;
		}

		if (DeckBuilderRun.hasRelic(DeckRelic.WAVE_RUSH)) {
			if (card.type == DeckCardType.ATTACK) {
				playerConsecutiveStrike++;
			} else {
				playerConsecutiveStrike = 0;
			}
		}

		if (!castOnDraw && afterimageBlock > 0 && card != DeckCard.AFTERIMAGE) {
			result.block += gainBlock(afterimageBlock);
		}
		if (!castOnDraw && deathDanceBlock > 0 && cost >= 2 && card != DeckCard.DEATH_DANCE) {
			result.block += gainBlock(deathDanceBlock);
		}
		if (!castOnDraw && rageBlock > 0 && card.type == DeckCardType.ATTACK) {
			result.block += gainBlock(rageBlock);
		}

		if (!castOnDraw && DeckBuilderRun.hasRelic(DeckRelic.GREMLIN_HORN) && playKillCount > 0) {
			for (int k = 0; k < playKillCount; k++) {
				energy = Math.min(DeckBuilderRun.MAX_ENERGY_CAP, energy + 1);
				if (draw(1)) result.draw++;
			}
		}

		if (!castOnDraw && card.type == DeckCardType.ATTACK) {
			attackCardsThisTurn++;
			attackCardsPlayedThisCombat++;
			triggerObsidianIfReady();
			if (DeckBuilderRun.hasRelic(DeckRelic.ANGER_STONE_MASK) && attackCardsPlayedThisCombat % 10 == 0) {
				energy = Math.min(DeckBuilderRun.MAX_ENERGY_CAP, energy + 1);
			}
			if (DeckBuilderRun.hasRelic(DeckRelic.KUSARIGAMA) && attackCardsThisTurn % 3 == 0) {
				ArrayList<DeckCombatEnemy> alive = aliveEnemies();
				if (!alive.isEmpty()) {
					DeckCombatEnemy kusaTarget = alive.get(Random.Int(alive.size()));
					damageEnemy(kusaTarget, 6, false);
					result.addHit(enemyIndex(kusaTarget), 6, 0);
				}
			}
			if (DeckBuilderRun.hasRelic(DeckRelic.ACCURACY_STONE_MASK) && attackCardsThisTurn % 3 == 0) {
				result.block += gainBlock(4);
			}
			if (DeckBuilderRun.hasRelic(DeckRelic.BLACK_PEARL) && attackCardsThisTurn % 3 == 0) {
				playerDexterity++;
			}
			if (DeckBuilderRun.hasRelic(DeckRelic.CHOCOLATE_DISCO) && attackCardsThisTurn % 3 == 0) {
				playerStrength++;
			}
		}

		if (!castOnDraw && card.type == DeckCardType.SKILL) {
			skillCardsThisTurn++;
			skillCardsPlayed++;
			triggerObsidianIfReady();
			if (DeckBuilderRun.hasRelic(DeckRelic.TUNING_FORK) && skillCardsPlayed % 10 == 0) {
				result.block += gainBlock(7);
			}
			if (DeckBuilderRun.hasRelic(DeckRelic.STRENGTH_ARM_STONE_MASK) && skillCardsPlayed % 3 == 0) {
				for (DeckCombatEnemy enemy : aliveEnemies()) {
					int dealt = damageEnemy(enemy, 5, false);
					if (dealt > 0) result.addHit(enemyIndex(enemy), dealt, 0);
				}
			}
			if (nextSkillZeroCost) nextSkillZeroCost = false;
			if (preciseShotActive) preciseShotSkillDiscount++;
			if (skillCardsThisTurn % 3 == 0) retrieveFirstCardToHand(DeckCard.SO_IT_SHALL_BE);
		}

		if (!castOnDraw && card.type == DeckCardType.POWER) {
			powerCardsThisTurn++;
			triggerObsidianIfReady();
			if (subroutineEnergy > 0) gainEnergy(subroutineEnergy);
			if (nextPowerZeroCost) nextPowerZeroCost = false;
		}

		if (!castOnDraw && card.type == DeckCardType.POWER && DeckBuilderRun.hasRelic(DeckRelic.GAME_PIECE)) {
			if (draw(1)) result.draw++;
		}

		if (!castOnDraw && card.type == DeckCardType.POWER && DeckBuilderRun.hasRelic(DeckRelic.PURIFICATION_STONE_MASK) && powersPlayed.isEmpty()) {
			result.block += gainBlock(7);
		}
		if (!castOnDraw && card.type == DeckCardType.POWER && DeckBuilderRun.hasRelic(DeckRelic.ST_GERMAIN_SANDWICH) && !stGermainUsed) {
			stGermainUsed = true;
			playerStrength++;
			result.block += gainBlock(6);
		}
		if (!castOnDraw && card.type == DeckCardType.POWER && DeckBuilderRun.hasRelic(DeckRelic.OPAL)) {
			DeckBuilderRun.playerHP = Math.min(DeckBuilderRun.playerHT, DeckBuilderRun.playerHP + 2);
		}

		if (targetWandIndex >= 0 && targetWandIndex < hand.size() && targetWandIndex != handIndex) {
			int currentHandIndex = currentHandIndexForPlayedCard(handIndex, cardCode);
			if (currentHandIndex < 0) return result.build();
			int staffCode = DeckCardCode.withoutCostOverride(hand.get(currentHandIndex));
			int wandCode = DeckCardCode.withoutCostOverride(hand.get(targetWandIndex));
			if (currentHandIndex > targetWandIndex) {
				hand.remove(currentHandIndex);
				hand.remove(targetWandIndex);
			} else {
				hand.remove(targetWandIndex);
				hand.remove(currentHandIndex);
			}
			result.draw += exhaustCard(wandCode);
			result.exhausted = true;
			if (card.type == DeckCardType.POWER) {
				// Powers are removed from the current combat, but not from the run deck.
			} else if (card.hasKeyword(staffCode, DeckCardKeyword.EXHAUST)) {
				result.draw += exhaustCard(staffCode);
			} else {
				discardPile.add(staffCode);
			}
		} else {
			int currentHandIndex = currentHandIndexForPlayedCard(handIndex, cardCode);
			if (currentHandIndex < 0) return result.build();
			hand.remove(currentHandIndex);
			if (isShivCard(card)) {
				firstShivUsed = true;
			}
			if (card.type == DeckCardType.POWER) {
				// Powers are removed from the current combat, but not from the run deck.
				if (!castOnDraw) powersPlayed.add(DeckCardCode.withoutCostOverride(cardCode));
			} else if (card.hasKeyword(cardCode, DeckCardKeyword.EXHAUST)) {
				result.draw += exhaustCard(DeckCardCode.withoutCostOverride(cardCode));
				result.exhausted = true;
				if (!castOnDraw && !burningSticksFired && card.type == DeckCardType.SKILL && DeckBuilderRun.hasRelic(DeckRelic.BURNING_STICKS)) {
					burningSticksFired = true;
					addToHand(DeckCardCode.withoutCostOverride(cardCode));
					result.draw++;
				}
			} else {
				int cleanCode = DeckCardCode.withoutCostOverride(cardCode);
				if (card == DeckCard.RAMPAGE) {
					cleanCode = increaseRampageDamage(cleanCode);
				}
				if (card == DeckCard.GLIDE) {
					discardPile.add(DeckCardCode.withCostOverride(cardCode, cost + 1));
				} else if (card == DeckCard.SECRET_PLAN) {
					discardPile.add(DeckCardCode.withCostOverride(cleanCode, Math.max(0, cost - 1)));
				} else if (card == DeckCard.PARTICLE_WALL) {
					addToHand(cleanCode);
				} else if (!castOnDraw && nostalgiaActive && !nostalgiaUsedThisTurn
						&& (card.type == DeckCardType.ATTACK || card.type == DeckCardType.SKILL)) {
					nostalgiaUsedThisTurn = true;
					drawPile.add(0, cleanCode);
				} else {
					discardPile.add(cleanCode);
					if (DeckBuilderRun.hasRelic(DeckRelic.RAZOR_TOOTH) && (card.type == DeckCardType.ATTACK || card.type == DeckCardType.SKILL)) {
						int lastIdx = discardPile.size() - 1;
						discardPile.set(lastIdx, DeckCardCode.upgrade(discardPile.get(lastIdx)));
					}
				}
			}
		}

		if (!castOnDraw && card.type == DeckCardType.POWER && DeckBuilderRun.hasRelic(DeckRelic.VITRIOL_DEVICE) && !hand.isEmpty()) {
			int idx = Random.Int(hand.size());
			hand.set(idx, DeckCardCode.withCostOverride(hand.get(idx), 0));
		}
		if (!castOnDraw && cost >= 2 && DeckBuilderRun.hasRelic(DeckRelic.EMERALD)) {
			result.block += gainBlock(4);
		}
		if (!castOnDraw && cost >= 3 && DeckBuilderRun.hasRelic(DeckRelic.STRANGE_FRAGMENT)) {
			energy = Math.min(DeckBuilderRun.MAX_ENERGY_CAP, energy + 1);
		}
		if (!castOnDraw && DeckBuilderRun.hasRelic(DeckRelic.TAROT_CARD) && hand.isEmpty() && draw(1)) {
			result.draw++;
		}

		if (!castOnDraw && impostingPresenceDamage > 0 && cardsPlayedThisTurn % 5 == 0) {
			for (DeckCombatEnemy enemy : aliveEnemies()) {
				int dealt = damageEnemy(enemy, impostingPresenceDamage, false);
				if (dealt > 0) result.addHit(enemyIndex(enemy), dealt, 0);
			}
		}

		if (!castOnDraw && snakeFormDamage > 0) {
			ArrayList<DeckCombatEnemy> alive = aliveEnemies();
			if (!alive.isEmpty()) {
				DeckCombatEnemy snakeTarget = alive.get(Random.Int(alive.size()));
				int dealt = damageEnemy(snakeTarget, snakeFormDamage, false);
				if (dealt > 0) result.addHit(enemyIndex(snakeTarget), dealt, 0);
			}
		}

		if (!castOnDraw) {
			for (int i = 0; i < enemies.size(); i++) {
				DeckCombatEnemy enemy = enemies.get(i);
				int strangleLoss = i < strangleBeforePlay.length ? strangleBeforePlay[i] : 0;
				if (enemy.alive() && strangleLoss > 0) {
					int dealt = Math.min(strangleLoss, enemy.hp);
					enemy.hp = Math.max(0, enemy.hp - strangleLoss);
					if (dealt > 0) {
						lastDamageEvents.add(DamageEvent.enemy(i, dealt, enemy.hp));
						result.addHit(i, dealt, 0);
					}
					if (!enemy.alive()) playKillCount++;
				}
			}
		}

		return result.build();
	}

	private int currentHandIndexForPlayedCard(int originalHandIndex, int cardCode) {
		int adjustedHandIndex = originalHandIndex + currentPlayHandIndexShift;
		if (adjustedHandIndex >= 0 && adjustedHandIndex < hand.size() && hand.get(adjustedHandIndex) == cardCode) {
			return adjustedHandIndex;
		}
		if (originalHandIndex >= 0 && originalHandIndex < hand.size() && hand.get(originalHandIndex) == cardCode) {
			return originalHandIndex;
		}
		for (int i = hand.size() - 1; i >= 0; i--) {
			if (hand.get(i) == cardCode) return i;
		}
		return -1;
	}

	private void triggerObsidianIfReady() {
		if (obsidianTriggeredThisTurn || !DeckBuilderRun.hasRelic(DeckRelic.OBSIDIAN)) return;
		if (attackCardsThisTurn > 0 && skillCardsThisTurn > 0 && powerCardsThisTurn > 0) {
			obsidianTriggeredThisTurn = true;
			playerStrength++;
			playerDexterity++;
		}
	}

	private boolean cardHasEnemyDebuff(DeckCard card, int cardCode) {
		for (DeckCardEffect effect : card.effects(cardCode)) {
			if (effect instanceof DeckCardEffects.Vulnerable
					|| effect instanceof DeckCardEffects.AttackDown
					|| effect instanceof DeckCardEffects.WeaknessStabBonus) {
				return true;
			}
		}
		return false;
	}

	public int enemyDebuffAmount(int amount) {
		return currentCardDebuffDoubled ? amount * 2 : amount;
	}

	public int exhaustCard(int cardCode) {
		if (DeckCard.byCode(cardCode) == DeckCard.GENETIC_ALGORITHM && pendingGeneticAlgorithmGrowth > 0) {
			int grown = DeckCardCode.withAuxValue(cardCode, DeckCardCode.auxValue(cardCode) + pendingGeneticAlgorithmGrowth);
			for (int i = 0; i < DeckBuilderRun.deck.size(); i++) {
				if (DeckBuilderRun.deck.get(i) == cardCode) {
					DeckBuilderRun.deck.set(i, grown);
					break;
				}
			}
			cardCode = grown;
			pendingGeneticAlgorithmGrowth = 0;
		}
		exhaustPile.add(cardCode);
		cardsExhaustedThisTurn++;
		int draws = 0;
		if (DeckCard.byCode(cardCode) == DeckCard.BATTLE_DRUMS) {
			energy = Math.min(DeckBuilderRun.MAX_ENERGY_CAP, energy + (DeckCardCode.upgradeLevel(cardCode) > 0 ? 3 : 2));
		}
		if (numbnessBlock > 0) {
			gainBlock(numbnessBlock);
		}
		for (int i = 0; i < darkEmbraceDraw; i++) {
			if (draw(1)) draws++;
		}
		if (DeckBuilderRun.hasRelic(DeckRelic.DIO_BONE)) {
			addRandomRewardCardToHand();
		}
		if (!DeckBuilderRun.hasRelic(DeckRelic.SPEED_STONE_MASK)) return draws;
		exhaustedCardsPlayedThisCombat++;
		if (exhaustedCardsPlayedThisCombat % 5 == 0 && draw(1)) draws++;
		return draws;
	}

	private void playAutoDrawnCard(int handIndex) {
		DeckPlayResult result = play(handIndex, true);
		if (result.played) {
			lastAutoPlayResults.add(new DeckPlayResult(
					true, result.card, result.damage, result.block, result.draw,
					result.vulnerable, result.strength, result.dexterity, result.heal, result.exhausted,
					true, result.hits, result.shuffles, result.gold));
		}
	}

	public int cardDamage(DeckCard card, int cardCode) {
		return cardDamage(card, cardCode, target());
	}

	public int cardDamage(DeckCard card, int cardCode, DeckCombatEnemy target) {
		return cardDamageFromBase(card, cardCode, target, card.damage(cardCode));
	}

	public int cardDamageFromBase(DeckCard card, int cardCode, DeckCombatEnemy target, int baseDamage) {
		int handPenaltyTotal = 0;
		for (int code : hand) {
			handPenaltyTotal += DeckCard.byCode(code).handPenalty;
		}
		int strength = card.type == DeckCardType.ATTACK ? playerStrength + playerTurnStrength : 0;
		int base = baseDamage + strength - handPenaltyTotal;
		if (DeckBuilderRun.hasRelic(DeckRelic.MINIATURE_CANNON) && card.type == DeckCardType.ATTACK && DeckCardCode.upgradeLevel(cardCode) > 0) {
			base += 3;
		}
		int damage = Math.max(0, base);
		if (card.type == DeckCardType.ATTACK && nextAttackDamageMultiplier > 1) {
			damage *= nextAttackDamageMultiplier;
		}
		if (target != null && target.vulnerable > 0) {
			damage = target.debuffDoubleTurns > 0 ? damage * 2 : (damage * 3 + 1) / 2;
		}
		if (playerDamageReduction > 0) {
			damage = damage * Math.max(0, 100 - playerDamageReduction) / 100;
		}
		if (isShivCard(card)) {
			damage += shivDamageBonus;
			if (!firstShivUsed) {
				damage += firstShivDamageBonus;
			}
		}
		if (card == DeckCard.ROTATING_NAIL) {
			damage += spinningNailDamageBonus;
		}
		return damage;
	}

	public int cardCost(int cardCode) {
		DeckCard card = DeckCard.byCode(cardCode);
		if (isXCost(cardCode)) return 0;
		if (card.hasKeyword(cardCode, DeckCardKeyword.ZERO_COST)) return 0;
		if (voidFormFreeCardsThisTurn > 0) return 0;
		int overrideCost = DeckCardCode.costOverride(cardCode);
		int cost = overrideCost >= 0 ? overrideCost : card.cost(cardCode);
		if (card == DeckCard.PROUD_STARVER) {
			cost -= countInDrawPile(DeckCard.ROTATING_NAIL);
		}
		if (card == DeckCard.WEAPON_RETRIEVAL) {
			cost -= playerConsecutiveStrike;
		}
		if (card == DeckCard.PRECISE_SHOT) cost -= skillCardsThisTurn;
		if (card.type == DeckCardType.ATTACK && nextAttackZeroCost) return 0;
		if (card.type == DeckCardType.ATTACK && playerEntangle > 0) cost += 1;
		if (card.type == DeckCardType.SKILL && nextSkillZeroCost) return 0;
		if (card.type == DeckCardType.POWER && nextPowerZeroCost) return 0;
		cost += cardCostIncreaseThisTurn;
		return Math.max(0, cost);
	}

	public int gainEnergy(int amount) {
		if (amount <= 0 || energyGainDisabledThisTurn) return 0;
		int before = energy;
		energy = Math.min(DeckBuilderRun.MAX_ENERGY_CAP, energy + amount);
		return energy - before;
	}

	private void triggerOrbitEnergy(int spent) {
		if (orbitCount <= 0 || spent <= 0) return;
		orbitProgress += spent;
		while (orbitProgress >= 4) {
			orbitProgress -= 4;
			gainEnergy(orbitCount);
		}
	}

	public void applyCombatRewardBonuses() {
		if (royaltyGold <= 0 || royaltyGoldApplied || !won()) return;
		DeckCombatRewardState reward = DeckBuilderRun.combatRewardForCurrentNode(nodeType);
		reward.gold += royaltyGold;
		royaltyGoldApplied = true;
	}

	public boolean isXCost(int cardCode) {
		return DeckCard.byCode(cardCode).cost(cardCode) < 0;
	}

	public void addToHand(int cardCode) {
		if (hand.size() < maxHandSize) {
			hand.add(cardCode);
		} else {
			discardPile.add(cardCode);
		}
	}

	public void addShivToHand(boolean upgraded) {
		int shivCode = DeckCard.SHIV.code();
		if (upgraded) shivCode = DeckCardCode.upgrade(shivCode);
		if (shivRetain) shivCode = DeckCardCode.withKeyword(shivCode, DeckCardKeyword.RETAIN);
		addToHand(shivCode);
	}

	public void addSpecialShivToHand() {
		int shivCode = DeckCard.SPECIAL_SHIV.code();
		if (shivRetain) shivCode = DeckCardCode.withKeyword(shivCode, DeckCardKeyword.RETAIN);
		addToHand(shivCode);
	}

	public boolean isShivCard(DeckCard card) {
		return card == DeckCard.SHIV || card == DeckCard.SPECIAL_SHIV;
	}

	private void moveRandomDrawPileCardToHand(boolean zeroCostThisTurn) {
		if (drawPile.isEmpty()) {
			if (discardPile.isEmpty()) return;
			refillDrawPileFromDiscard();
		}
		if (drawPile.isEmpty()) return;
		int idx = Random.Int(drawPile.size());
		int code = drawPile.remove(idx);
		if (zeroCostThisTurn) code = DeckCardCode.withCostOverride(code, 0);
		addToHand(code);
	}

	public void addRandomRewardCardToHand() {
		addRandomRewardCardToHand(false);
	}

	public void addRandomRewardCardToHand(boolean upgraded) {
		DeckCard[] pool = DeckCard.rewardPool(DeckBuilderRun.heroClass(), false, false);
		if (pool.length == 0) return;
		int code = pool[Random.Int(pool.length)].code();
		if (upgraded) code = DeckCardCode.upgrade(code);
		addToHand(code);
	}

	private void addRandomTransientCardToHand() {
		DeckCard[] pool = DeckCard.rewardPool(DeckBuilderRun.heroClass(), false, false);
		if (pool.length == 0) return;
		int code = pool[Random.Int(pool.length)].code();
		code = DeckCardCode.withKeyword(code, DeckCardKeyword.TRANSIENT);
		addToHand(code);
	}

	public void addRandomZeroCostExhaustCardsToHand(int count) {
		ArrayList<DeckCard> pool = new ArrayList<>();
		for (DeckCard card : DeckCard.rewardPool(DeckBuilderRun.heroClass(), false, false)) {
			if (card.cost(card.code()) == 0 && !DeckWandCards.isWand(card.code())) {
				pool.add(card);
			}
		}
		if (pool.isEmpty()) return;
		for (int i = 0; i < count; i++) {
			int code = pool.get(Random.Int(pool.size())).code();
			code = DeckCardCode.withKeyword(code, DeckCardKeyword.EXHAUST);
			addToHand(code);
		}
	}

	private void refillDrawPileFromDiscard() {
		if (discardPile.isEmpty()) return;
		drawPile.addAll(discardPile);
		discardPile.clear();
		shuffle(drawPile);
		if (DeckBuilderRun.hasRelic(DeckRelic.ESCAPE_ROPE)) {
			gainBlock(6);
		}
		if (stratagemCount > 0 && !drawPile.isEmpty()) {
			pendingStratagemSelect = true;
		}
	}

	public void addToDrawPile(int cardCode, int count, boolean shuffle) {
		for (int i = 0; i < count; i++) {
			drawPile.add(cardCode);
		}
		if (shuffle) {
			shuffle(drawPile);
		}
	}

	public void addCreamDarkSpace(int amount) {
		for (DeckCombatEnemy enemy : enemies) {
			if (enemy.kind == DeckEnemy.CREAM && enemy.alive()) {
				enemy.darkSpace = Math.max(0, enemy.darkSpace + amount);
			}
		}
	}

	public int countInDrawPile(DeckCard card) {
		int count = 0;
		for (int code : drawPile) {
			if (DeckCard.byCode(code) == card) count++;
		}
		return count;
	}

	public void copyAndPlayFromDrawPile(DeckCard card, DeckPlayResult.Builder result) {
		if (card == null) return;
		ArrayList<Integer> copies = new ArrayList<>();
		for (int code : drawPile) {
			if (DeckCard.byCode(code) == card) copies.add(code);
		}
		for (int code : copies) {
			DeckCard copyCard = DeckCard.byCode(code);
			int effectiveCode = copyCard.effectiveCodeForPlay(code, this, -1);
			for (DeckCardEffect effect : copyCard.effects(effectiveCode)) {
				effect.apply(new DeckCardPlayContext(this, copyCard, code, effectiveCode, -1, true, false, false, result));
			}
		}
	}

	public void playRandomFromDrawPile(int count) {
		for (int i = 0; i < count; i++) {
			playDrawPileCard(Random.Int(Math.max(1, drawPile.size())));
		}
	}

	public void playTopFromDrawPile(int count) {
		for (int i = 0; i < count; i++) {
			playDrawPileCard(0);
		}
	}

	public void playRandomAttacksFromDiscard(int count, DeckPlayResult.Builder result) {
		boolean first = true;
		for (int i = 0; i < count; i++) {
			ArrayList<Integer> attackIndexes = new ArrayList<>();
			for (int idx = 0; idx < discardPile.size(); idx++) {
				DeckCard card = DeckCard.byCode(discardPile.get(idx));
				if (card.type == DeckCardType.ATTACK && !card.unplayable(discardPile.get(idx))) {
					attackIndexes.add(idx);
				}
			}
			if (attackIndexes.isEmpty()) return;
			int discardIdx = attackIndexes.get(Random.Int(attackIndexes.size()));
			int cardCode = discardPile.remove(discardIdx);
			DeckCard card = DeckCard.byCode(cardCode);
			if (!first) result.nextWave();
			first = false;
			int effectiveCode = card.effectiveCodeForPlay(cardCode, this, -1);
			for (DeckCardEffect effect : card.effects(effectiveCode)) {
				effect.apply(new DeckCardPlayContext(this, card, cardCode, effectiveCode, -1, true, false, false, result));
			}
			if (card.type == DeckCardType.ATTACK && nextAttackDamageMultiplier > 1) {
				nextAttackDamageMultiplier = 0;
			}
			if (card.hasKeyword(cardCode, DeckCardKeyword.EXHAUST)) {
				result.draw += exhaustCard(cardCode);
				result.exhausted = true;
			} else {
				discardPile.add(cardCode);
			}
		}
	}

	private void playDrawPileCard(int index) {
		if (drawPile.isEmpty()) {
			if (!discardPile.isEmpty()) {
				refillDrawPileFromDiscard();
			}
		}
		if (drawPile.isEmpty()) return;
		int idx = Math.max(0, Math.min(index, drawPile.size() - 1));
		int cardCode = drawPile.remove(idx);
		DeckCard card = DeckCard.byCode(cardCode);
		DeckPlayResult.Builder resultBuilder = new DeckPlayResult.Builder(card, true);
		if (!card.unplayable(cardCode) && !DeckCardPool.isStatusOrCurse(card)) {
			int effectiveCode = card.effectiveCodeForPlay(cardCode, this, -1);
			for (DeckCardEffect effect : card.effects(effectiveCode)) {
				effect.apply(new DeckCardPlayContext(this, card, cardCode, effectiveCode, -1, true, false, false, resultBuilder));
			}
			if (card.type == DeckCardType.ATTACK && nextAttackDamageMultiplier > 1) {
				nextAttackDamageMultiplier = 0;
			}
			if (card.type == DeckCardType.POWER) {
				// 랜덤/자동 시전에서는 파워 카드도 버림
				discardPile.add(cardCode);
			} else if (card.hasKeyword(cardCode, DeckCardKeyword.EXHAUST)) {
				resultBuilder.draw += exhaustCard(cardCode);
				resultBuilder.exhausted = true;
			} else {
				discardPile.add(cardCode);
			}
		} else {
			discardPile.add(cardCode);
		}
		lastAutoPlayResults.add(resultBuilder.build());
	}

	private void playBombardmentsFromExhaustPile() {
		ArrayList<Integer> copies = new ArrayList<>();
		for (int code : exhaustPile) {
			if (DeckCard.byCode(code) == DeckCard.BOMBARDMENT) copies.add(code);
		}
		for (int code : copies) {
			int idx = exhaustPile.indexOf(code);
			if (idx < 0) continue;
			int cardCode = exhaustPile.remove(idx);
			DeckCard card = DeckCard.byCode(cardCode);
			DeckPlayResult.Builder resultBuilder = new DeckPlayResult.Builder(card, true);
			if (!card.unplayable(cardCode)) {
				int effectiveCode = card.effectiveCodeForPlay(cardCode, this, -1);
				for (DeckCardEffect effect : card.effects(effectiveCode)) {
					effect.apply(new DeckCardPlayContext(this, card, cardCode, effectiveCode, -1, true, false, false, resultBuilder));
				}
				if (card.type == DeckCardType.ATTACK && nextAttackDamageMultiplier > 1) {
					nextAttackDamageMultiplier = 0;
				}
				resultBuilder.draw += exhaustCard(cardCode);
				resultBuilder.exhausted = true;
			} else {
				exhaustPile.add(cardCode);
			}
			lastAutoPlayResults.add(resultBuilder.build());
		}
	}

	private boolean retrieveFirstCardToHand(DeckCard card) {
		if (moveFirstCardToHand(discardPile, card)) return true;
		if (moveFirstCardToHand(exhaustPile, card)) return true;
		return moveFirstCardToHand(drawPile, card);
	}

	private boolean moveFirstCardToHand(ArrayList<Integer> pile, DeckCard card) {
		for (int i = 0; i < pile.size(); i++) {
			if (DeckCard.byCode(pile.get(i)) == card) {
				addToHand(pile.remove(i));
				return true;
			}
		}
		return false;
	}

	private int increaseRampageDamage(int cardCode) {
		return DeckCardCode.withAuxValue(cardCode, DeckCardCode.auxValue(cardCode) + 1);
	}

	public boolean isCenterHandIndex(int handIndex) {
		int size = hand.size();
		return handIndex >= 0 && handIndex < size && size % 2 == 1 && handIndex == size / 2;
	}

	public boolean isEdgeHandIndex(int handIndex) {
		int size = hand.size();
		return handIndex >= 0 && handIndex < size && (handIndex == 0 || handIndex == size - 1);
	}

	public int damageEnemy(DeckCombatEnemy target, int damage, boolean attackCard) {
		if (target == null || damage <= 0) return 0;
		if (attackCard && playerWeak > 0) {
			damage = Math.max(0, (int)(damage * 0.75f));
		}
		int blocked = Math.min(target.block, damage);
		target.block -= blocked;
		int dealt = Math.max(0, damage - blocked);
		if (dealt > 0 && target.tricky > 0) {
			target.tricky = 0;
			dealt = 1;
		}
		if (dealt > 0 && target.blessed > 0) dealt = 1;
		target.hp = Math.max(0, target.hp - dealt);
		if (dealt > 0) {
			lastDamageEvents.add(DamageEvent.enemy(enemyIndex(target), dealt, target.hp));
		}
		if (attackCard && dealt > 0 && poisonCoatDamage > 0 && target.alive()) {
			if (applyEnemyDebuff(target)) {
				target.persistentDamage += poisonCoatDamage;
			}
		}
		if (!target.alive()) {
			playKillCount++;
			for (DeckCombatEnemy other : enemies) {
				if (other != target && other.alive()) {
					if ((target.kind == DeckEnemy.RAT_JAGGED && other.kind == DeckEnemy.RAT_SMOOTH) ||
						(target.kind == DeckEnemy.RAT_SMOOTH && other.kind == DeckEnemy.RAT_JAGGED)) {
						other.strength += 2;
					}
					// 결전: 누케사쿠 사망 시 시생인 동반 사망
					if (target.kind == DeckEnemy.NUKESAKU && other.kind == DeckEnemy.SICIGIN) {
						other.hp = 0;
						playKillCount++;
					}
				}
			}
		}
		if (target.kind == DeckEnemy.LAGAVULIN && !target.splitUsed && dealt > 0 && target.alive()) {
			target.splitUsed = true;
			target.intent = RESULT_LAGAVULIN_STUN;
		}
		if (dealt > 0 && target.platedArmor > 0) {
			target.platedArmor--;
		}
		if (attackCard && target.thorns > 0) {
			int thornBlocked = Math.min(block, target.thorns);
			block -= thornBlocked;
			int thornDamage = Math.max(0, target.thorns - thornBlocked);
			if (thornDamage > 0) loseHP(thornDamage);
		}
		return dealt;
	}

	private int applyPersistentDamageAtEnemyTurnStart(DeckCombatEnemy enemy) {
		if (enemy == null || !enemy.alive() || enemy.persistentDamage <= 0) return 0;
		int amount = enemy.persistentDamage;
		int dealt = Math.min(amount, enemy.hp);
		enemy.hp = Math.max(0, enemy.hp - amount);
		enemy.persistentDamage = Math.max(0, enemy.persistentDamage - 1);
		if (dealt > 0) {
			lastEnemyEndTurnDamageEvents.add(DamageEvent.enemy(enemyIndex(enemy), dealt, enemy.hp));
		}
		if (!enemy.alive()) playKillCount++;
		return dealt;
	}

	public int gainBlock(int amount) {
		return gainBlock(amount, false);
	}

	public int gainBlockFromCard(int amount) {
		if (blockFromCardDisabledTurns > 0) return 0;
		return gainBlock(amount, true);
	}

	private int gainBlock(int amount, boolean affectedByDexterity) {
		if (amount <= 0) return 0;
		int effectiveAmount = Math.max(0, amount + (affectedByDexterity ? playerDexterity : 0));
		if (effectiveAmount <= 0) return 0;
		if (playerFirstBlockDouble && !firstBlockDoubleUsedThisTurn) {
			effectiveAmount *= 2;
			firstBlockDoubleUsedThisTurn = true;
		}
		if (shadowBlockDouble) {
			effectiveAmount *= 2;
		}
		if (affectedByDexterity && DeckBuilderRun.hasRelic(DeckRelic.PET_SHOP_MEMORY_DISC) && !petShopBlockDoubleUsed) {
			effectiveAmount *= 2;
			petShopBlockDoubleUsed = true;
		}
		int gained = playerBlockReduction > 0 ? effectiveAmount * 3 / 4 : effectiveAmount;
		block += gained;
		if (gained > 0 && absolutePowerDamage > 0) {
			ArrayList<DeckCombatEnemy> alive = aliveEnemies();
			if (!alive.isEmpty()) {
				damageEnemy(alive.get(Random.Int(alive.size())), absolutePowerDamage, false);
			}
		}
		return gained;
	}

	public int healPlayer(int amount) {
		if (amount <= 0) return 0;
		int before = DeckBuilderRun.playerHP;
		DeckBuilderRun.playerHP = Math.min(DeckBuilderRun.playerHT, DeckBuilderRun.playerHP + amount);
		return Math.max(0, DeckBuilderRun.playerHP - before);
	}

	public int orangeBombTimer() {
		int timer = 0;
		for (int turnsLeft : orangeBombTimers) {
			if (turnsLeft <= 0) continue;
			timer = timer <= 0 ? turnsLeft : Math.min(timer, turnsLeft);
		}
		return timer;
	}

	public int enemyGainBlock(DeckCombatEnemy enemy, int amount) {
		if (enemy == null || amount <= 0) return 0;
		int reduction = 25 * Math.max(0, enemy.blockReduction);
		int gained = amount * Math.max(0, 100 - reduction) / 100;
		enemy.block += gained;
		return gained;
	}

	public boolean applyEnemyDebuff(DeckCombatEnemy enemy) {
		if (enemy == null) return false;
		if (enemy.artifact > 0) {
			enemy.artifact--;
			return false;
		}
		return true;
	}

	public boolean applyPlayerDebuff() {
		if (playerArtifact > 0) {
			playerArtifact--;
			return false;
		}
		return true;
	}

	private void syncPotionlessDexterity() {
		boolean shouldHave = DeckBuilderRun.hasRelic(DeckRelic.SPW_FOUNDATION_SUPPLIES) && DeckBuilderRun.potions.isEmpty();
		if (shouldHave && !potionlessDexterityActive) {
			playerDexterity += 2;
			potionlessDexterityActive = true;
		} else if (!shouldHave && potionlessDexterityActive) {
			playerDexterity -= 2;
			potionlessDexterityActive = false;
		}
	}

	@SuppressWarnings("SuspiciousIndentation")
    public int endTurn() {
		lastTurnEndRegenBlock = 0;
		if (playerRegen > 0) {
			lastTurnEndRegenBlock = gainBlock(playerRegen);
		}
		lastEnemyActions.clear();
		lastEnemyEndTurnDamageEvents.clear();
		lastTurnEndAutoPlayResults.clear();
		lastTurnEndStatusDamage = 0;
		lastTurnEndPoisonDarts = 0;
		lastTurnEndCurseDamage = 0;
		lastTurnEndRegretDamage = 0;
		lastOrangeBombTotalDamage = 0;
		lastOrangeBombExplosions = 0;
		lastPriceOfSinDamage = 0;
		lastCombatBreathingBlock = 0;
		for (int code : hand) {
			if (DeckCard.byCode(code) == DeckCard.POISON_DART) {
				lastTurnEndPoisonDarts++;
			}
		}
		if (lastTurnEndPoisonDarts > 0) {
			lastTurnEndStatusDamage = lastTurnEndPoisonDarts * 3;
			int cappedStatus = DeckBuilderRun.hasRelic(DeckRelic.BEATING_REMNANT) ? Math.min(lastTurnEndStatusDamage, 20) : lastTurnEndStatusDamage;
			DeckBuilderRun.playerHP = Math.max(0, DeckBuilderRun.playerHP - preventableHpLoss(cappedStatus));
			if (playerDead()) return lastTurnEndStatusDamage;
		}
		DeckWandCards.triggerTurnEndWands(this);
		int curseDamageTaken = 0, regretDamageTaken = 0, curseBlockReductionGain = 0, curseWeakGain = 0;
		for (int code : hand) {
			DeckCard handCard = DeckCard.byCode(code);
			if (!DeckCardPool.isCurse(handCard)) continue;
			if (handCard == DeckCard.DECAY) {
				curseDamageTaken += 2;
			} else if (handCard == DeckCard.DEBT) {
				DeckBuilderRun.gold = Math.max(0, DeckBuilderRun.gold - 10);
			} else if (handCard == DeckCard.SHAME) {
				curseBlockReductionGain++;
			} else if (handCard == DeckCard.SUSPICION) {
				curseWeakGain++;
			} else if (handCard == DeckCard.REGRET) {
				int regretDamage = hand.size();
				regretDamageTaken += regretDamage;
				curseDamageTaken += regretDamage;
			}
		}
		if (curseDamageTaken > 0) {
			int cappedCurse = DeckBuilderRun.hasRelic(DeckRelic.BEATING_REMNANT)
					? Math.min(curseDamageTaken, Math.max(0, 20 - lastTurnEndStatusDamage))
					: curseDamageTaken;
			lastTurnEndCurseDamage = curseDamageTaken;
			lastTurnEndRegretDamage = regretDamageTaken;
			lastTurnEndStatusDamage += curseDamageTaken;
			DeckBuilderRun.playerHP = Math.max(0, DeckBuilderRun.playerHP - preventableHpLoss(cappedCurse));
			if (playerDead()) return lastTurnEndStatusDamage;
		}
		for (int code : hand) {
			if (DeckCard.byCode(code) == DeckCard.PRICE_OF_SIN) {
				int sinLost = loseHP(6);
				lastPriceOfSinDamage += sinLost;
				if (playerDead()) return lastTurnEndStatusDamage;
			}
		}
		if (surgeActive && !won()) {
			ArrayList<Integer> attacksInHand = new ArrayList<>();
			for (int code : hand) {
				if (DeckCard.byCode(code).type == DeckCardType.ATTACK && !DeckWandCards.isWand(code)) attacksInHand.add(code);
			}
			if (!attacksInHand.isEmpty()) {
				int pickedCode = attacksInHand.get(Random.Int(attacksInHand.size()));
				hand.remove((Integer) pickedCode);
				DeckCard pickedCard = DeckCard.byCode(pickedCode);
				int savedTarget = targetIndex;
				ArrayList<DeckCombatEnemy> alive = aliveEnemies();
				if (!alive.isEmpty()) targetIndex = enemyIndex(alive.get(Random.Int(alive.size())));
				int effectiveCode = pickedCard.effectiveCodeForPlay(pickedCode, this, -1);
				DeckPlayResult.Builder surgeResult = new DeckPlayResult.Builder(pickedCard, true);
				for (DeckCardEffect effect : pickedCard.effects(effectiveCode)) {
					effect.apply(new DeckCardPlayContext(this, pickedCard, pickedCode, effectiveCode, -1, true, false, false, surgeResult));
				}
				targetIndex = savedTarget;
				if (pickedCard.hasKeyword(pickedCode, DeckCardKeyword.EXHAUST)) {
					exhaustCard(pickedCode);
				} else {
					discardPile.add(pickedCode);
				}
				lastTurnEndAutoPlayResults.add(surgeResult.build());
			}
		}
		if (DeckBuilderRun.hasRelic(DeckRelic.FROG_SKIN) && !hand.isEmpty()) {
			gainBlock(hand.size());
		}
		if (DeckBuilderRun.hasRelic(DeckRelic.HIGHWAY_TO_HELL) && hand.isEmpty()) {
			for (DeckCombatEnemy enemy : aliveEnemies()) {
				damageEnemy(enemy, 20, false);
			}
		}
		if (DeckBuilderRun.hasRelic(DeckRelic.THOTH) && attackCardsThisTurn == 0) {
			bonusEnergyTurns++;
		}
		if (DeckBuilderRun.hasRelic(DeckRelic.MAGNET) && cardsPlayedThisTurn <= 3) {
			nextTurnBonusDraw += 3;
		}
		if (paleBlueDotDraw > 0 && cardsPlayedThisTurn >= 5) {
			nextTurnBonusDraw += paleBlueDotDraw;
		}
		if (!drawPile.isEmpty()) {
			int topCode = drawPile.get(0);
			if (DeckCard.byCode(topCode) == DeckCard.I_AM_INVINCIBLE) {
				drawPile.remove(0);
				gainBlockFromCard(DeckCard.I_AM_INVINCIBLE.block(topCode));
				discardPile.add(DeckCardCode.withoutCostOverride(topCode));
			}
		}
		if (DeckBuilderRun.hasRelic(DeckRelic.TOORU_DOLL) && turn == 7) {
			for (DeckCombatEnemy enemy : aliveEnemies()) {
				damageEnemy(enemy, 52, false);
			}
		}
		int combatBreathingBlock = activePowerBlock(DeckCard.COMBAT_BREATHING);
		if (combatBreathingBlock > 0 && playerConsecutiveStrike > 0) {
			lastCombatBreathingBlock = gainBlock(combatBreathingBlock * playerConsecutiveStrike);
		}

		ArrayList<Integer> retained = new ArrayList<>();
		for (int code : hand) {
			int cleanedCode = DeckCardCode.withoutCostOverride(code);
			DeckCard handCard = DeckCard.byCode(code);
			if (handCard.hasKeyword(code, DeckCardKeyword.TRANSIENT)) {
				exhaustCard(cleanedCode);
			} else if (retainHandTurns > 0 || handCard.hasKeyword(code, DeckCardKeyword.RETAIN) || (isShivCard(handCard) && shivRetain)
					|| (turn == 1 && DeckBuilderRun.hasRelic(DeckRelic.RINGING_TRIANGLE))) {
				retained.add(cleanedCode);
			} else {
				discardPile.add(cleanedCode);
			}
		}
		hand.clear();
		hand.addAll(retained);
		if (retainHandTurns > 0) retainHandTurns--;
		if (playerTurnDexterity > 0) {
			playerDexterity -= playerTurnDexterity;
			playerTurnDexterity = 0;
		}
		if (playerBlockReduction > 0) playerBlockReduction--;
		if (playerEntangle > 0) playerEntangle--;
		shadowBlockDouble = false;
		for (int i = 0; i < curseBlockReductionGain; i++) {
			if (applyPlayerDebuff()) playerBlockReduction++;
		}
		for (int i = 0; i < curseWeakGain; i++) {
			if (applyPlayerDebuff()) playerWeak++;
		}
		if (DeckBuilderRun.hasRelic(DeckRelic.KAWAJIRI_MEMORY_DISC) && block >= 10) {
			ArrayList<DeckCombatEnemy> alive = aliveEnemies();
			if (!alive.isEmpty()) {
				damageEnemy(alive.get(Random.Int(alive.size())), 6, false);
			}
		}
		if (DeckBuilderRun.hasRelic(DeckRelic.ENDURANCE_STONE_MASK) && block <= 0) {
			gainBlock(6);
		}
		if (DeckBuilderRun.hasRelic(DeckRelic.EVASION_STONE_MASK) && attackCardsThisTurn == 0) {
			gainBlock(4);
		}
		if (blockFromCardDisabledTurns > 0) blockFromCardDisabledTurns--;

		for (int i = orangeBombTimers.size() - 1; i >= 0; i--) {
			int turnsLeft = orangeBombTimers.get(i) - 1;
			if (turnsLeft <= 0) {
				int bombDamage = orangeBombDamages.get(i);
				lastOrangeBombExplosions++;
				for (DeckCombatEnemy bombTarget : aliveEnemies()) {
					// Orange Bomb is fixed damage: do not route through card damage modifiers.
					int dealt = damageEnemy(bombTarget, bombDamage, false);
					lastOrangeBombTotalDamage += dealt;
				}
				orangeBombTimers.remove(i);
				orangeBombDamages.remove(i);
			} else {
				orangeBombTimers.set(i, turnsLeft);
			}
		}
		if (won()) {
			orangeBombTimers.clear();
			orangeBombDamages.clear();
		}

		for (DeckCombatEnemy enemy : enemies) {
			if (enemy.kind == DeckEnemy.CREAM && enemy.alive() && enemy.darkSpace > 0) {
				enemy.darkSpace--;
				if (enemy.darkSpace <= 0) {
					forceGameOver = true;
					DeckBuilderRun.playerHP = 0;
					return 999;
				}
			}
		}

		boolean injected = false;
		int damageTaken = 0;
		int remainingBlock = block;
		ArrayList<DeckCombatEnemy> splitSpawns = new ArrayList<>();
		for (DeckCombatEnemy enemy : enemies) {
			if (!enemy.alive()) continue;
			enemy.block = 0;
			applyPersistentDamageAtEnemyTurnStart(enemy);
			if (!enemy.alive()) continue;
			if (enemy.kind == DeckEnemy.LARGE_SLIME && !enemy.splitUsed && enemy.hp <= enemy.ht / 2) {
				enemy.splitUsed = true;
				int splitHp = Math.max(1, enemy.hp);
				enemy.hp = 0;
				for (int i = 0; i < 2; i++) {
					DeckCombatEnemy medium = new DeckCombatEnemy(DeckEnemy.MEDIUM_SLIME, depth);
					medium.ht = splitHp;
					medium.hp = splitHp;
					splitSpawns.add(medium);
				}
				lastEnemyActions.add(new EnemyAction(enemyIndex(enemy), 0, false, "분열"));
				continue;
			}
			DeckEnemyIntent.TurnResult intentResult = DeckEnemyIntent.byId(enemy.intent).apply(this, enemy, remainingBlock);
			remainingBlock = intentResult.remainingBlock;
			damageTaken += intentResult.damageTaken;
			injected = injected || intentResult.injected;
			enemy.lastIntent = enemy.intent;
			if (enemy.vulnerable > 0) enemy.vulnerable--;
			if (enemy.attackDown > 0) enemy.attackDown--;
			if (enemy.blessed > 0) enemy.blessed--;
			if (enemy.debuffDoubleTurns > 0) enemy.debuffDoubleTurns--;
			enemy.strangleHpLoss = 0;
			enemy.turnStrengthLoss = 0;
			if (enemy.platedArmor > 0 && enemy.alive()) enemyGainBlock(enemy, enemy.platedArmor);
			if (enemy.kind == DeckEnemy.BYRDONIS && enemy.alive()) enemy.strength += 1;
			if (enemy.ritual > 0 && enemy.alive()) enemy.strength += enemy.ritual;
			if (enemy.demise > 0 && enemy.alive()) {
				int dealt = Math.min(enemy.demise, enemy.hp);
				enemy.hp = Math.max(0, enemy.hp - enemy.demise);
				if (dealt > 0) {
					lastEnemyEndTurnDamageEvents.add(DamageEvent.enemy(enemyIndex(enemy), dealt, enemy.hp));
				}
			}
		}
		enemies.addAll(splitSpawns);
		sanitizeTarget();

		block = remainingBlock;
		playerThorns = playerPermanentThorns;

		if (damageTaken > 0) {
			int cappedEnemy = DeckBuilderRun.hasRelic(DeckRelic.BEATING_REMNANT)
					? Math.max(0, Math.min(damageTaken, 20 - lastTurnEndStatusDamage))
					: damageTaken;
			int hpBeforeEnemy = DeckBuilderRun.playerHP;
			DeckBuilderRun.playerHP = Math.max(0, DeckBuilderRun.playerHP - preventableHpLoss(cappedEnemy));
			if (playerRegen > 0 && DeckBuilderRun.playerHP < hpBeforeEnemy) {
				playerRegen = Math.max(0, playerRegen - 1);
			}
		}
		if (dieOnUnblockedAttackTriggered && !won()) {
			DeckBuilderRun.playerHP = 0;
		}
		if (incomingDamageReductionTurns > 0) incomingDamageReductionTurns--;
		if (preventHpLossTurns > 0) preventHpLossTurns--;
		int totalDamageTaken = lastTurnEndStatusDamage + damageTaken;
		return injected && totalDamageTaken == 0 ? RESULT_SLIMY_INJECT : totalDamageTaken;
	}

	DeckEnemyIntent.AttackResult performEnemyAttack(DeckCombatEnemy enemy, int baseDamage, int remainingBlock, String label) {
		int enemyDamage = enemyDamage(enemy, baseDamage);
		if (playerVulnerable > 0) enemyDamage = (enemyDamage * 3 + 1) / 2;
		if (enemy.attackDown > 0) enemyDamage = enemy.debuffDoubleTurns > 0 ? enemyDamage / 2 : enemyDamage * 3 / 4;
		if (incomingDamageReductionTurns > 0) enemyDamage = enemyDamage * 70 / 100;
		int blocked = Math.min(remainingBlock, enemyDamage);
		int damage = Math.max(0, enemyDamage - blocked);
		if (preventHpLossTurns > 0) damage = 0;
		if (playerBlessed > 0 && damage > 0) damage = 1;
		if (damage > 0 && enemy.venom > 0) {
			enemy.strength += enemy.venom;
			label = appendLabel(label, "공격력 +" + enemy.venom);
		}
		if (damage > 0 && dieOnUnblockedAttack) {
			dieOnUnblockedAttackTriggered = true;
			label = appendLabel(label, "포석");
		}
		int counterDamage = 0;
		if (enemyDamage > 0 && playerThorns > 0) {
			counterDamage = damageEnemy(enemy, playerThorns, false);
		}
		EnemyAction action = new EnemyAction(enemyIndex(enemy), damage, false, label, enemyDamage > 0 && damage == 0);
		action.counterDamage = counterDamage;
		lastEnemyActions.add(action);
		return new DeckEnemyIntent.AttackResult(Math.max(0, remainingBlock - enemyDamage), damage);
	}

	private String appendLabel(String first, String second) {
		if (first == null || first.length() == 0) return second;
		return first + " / " + second;
	}

	private int enemyDamage(DeckCombatEnemy enemy, int baseDamage) {
		return Math.max(0, baseDamage + enemy.strength - enemy.turnStrengthLoss);
	}

	public boolean won() {
		for (DeckCombatEnemy enemy : enemies) {
			if (enemy.alive()) return false;
		}
		return true;
	}

	public boolean playerDead() {
		if (forceGameOver && DeckBuilderRun.playerHP <= 0) return true;
		if (DeckBuilderRun.playerHP <= 0 && DeckBuilderRun.triggerDiverDownRevive()) return false;
		if (DeckBuilderRun.playerHP <= 0 && consumeFairyInABottle()) return false;
		return DeckBuilderRun.playerHP <= 0;
	}

	private boolean consumeFairyInABottle() {
		for (int i = 0; i < DeckBuilderRun.potions.size(); i++) {
			if (DeckPotion.byId(DeckBuilderRun.potions.get(i)) == DeckPotion.FAIRY_IN_A_BOTTLE) {
				DeckBuilderRun.removePotion(i);
				DeckBuilderRun.playerHP = Math.max(1, Math.round(DeckBuilderRun.playerHT * 0.30f));
				return true;
			}
		}
		return false;
	}

	public DeckCombatEnemy target() {
		sanitizeTarget();
		return targetIndex >= 0 && targetIndex < enemies.size() ? enemies.get(targetIndex) : null;
	}

	public void setTarget(int index) {
		if (index >= 0 && index < enemies.size() && enemies.get(index).alive()) {
			targetIndex = index;
		}
	}

	public ArrayList<DeckCombatEnemy> aliveEnemies() {
		ArrayList<DeckCombatEnemy> result = new ArrayList<>();
		for (DeckCombatEnemy enemy : enemies) {
			if (enemy.alive()) result.add(enemy);
		}
		return result;
	}

	public int enemyIndex(DeckCombatEnemy enemy) {
		return enemies.indexOf(enemy);
	}

	private void sanitizeTarget() {
		if (targetIndex >= 0 && targetIndex < enemies.size() && enemies.get(targetIndex).alive()) {
			return;
		}
		for (int i = 0; i < enemies.size(); i++) {
			if (enemies.get(i).alive()) {
				targetIndex = i;
				return;
			}
		}
		targetIndex = 0;
	}

	private void drawTo(int size) {
		while (hand.size() < size) {
			if (!draw(1)) break;
		}
	}

	public boolean draw(int count) {
		boolean drew = false;
		for (int i = 0; i < count; i++) {
			if (drawOneAndReturnCode() < 0) return drew;
			drew = true;
		}
		return drew;
	}

	public int drawOneAndReturnCode() {
		if (drawDisabledThisTurn) return -1;
		if (drawPile.isEmpty()) {
			if (discardPile.isEmpty()) return -1;
			refillDrawPileFromDiscard();
		}
		if (drawPile.isEmpty()) return -1;
		int drawn = drawPile.remove(0);
		drawn = applyDrawnCardTriggers(drawn);
		cardsDrawnThisTurn++;
		cardsDrawnThisCombat++;
		if (automationCount > 0) {
			automationDrawsSinceLastTrigger++;
			if (automationDrawsSinceLastTrigger >= 10) {
				automationDrawsSinceLastTrigger -= 10;
				energy = Math.min(DeckBuilderRun.MAX_ENERGY_CAP, energy + automationCount);
			}
		}
		if (hand.size() >= maxHandSize) {
			discardPile.add(drawn);
			return drawn;
		}
		hand.add(drawn);
		int handIndex = hand.size() - 1;
		if (DeckCard.byCode(drawn).hasKeyword(drawn, DeckCardKeyword.CAST_ON_DRAW)) {
			playAutoDrawnCard(handIndex);
		}
		return drawn;
	}

	private int applyDrawnCardTriggers(int cardCode) {
		DeckCard card = DeckCard.byCode(cardCode);
		if (card == DeckCard.KINGS_PUNCH) {
			return DeckCardCode.withAuxValue(cardCode, DeckCardCode.auxValue(cardCode) + 1);
		}
		if (card == DeckCard.KINGS_KICK) {
			int currentCost = cardCost(cardCode);
			return DeckCardCode.withCostOverride(cardCode, Math.max(0, currentCost - 1));
		}
		return cardCode;
	}

	public void shuffleAllCardsIntoDrawPile() {
		for (int i = 0; i < hand.size(); i++) {
			drawPile.add(DeckCardCode.withoutCostOverride(hand.get(i)));
		}
		hand.clear();
		drawPile.addAll(discardPile);
		discardPile.clear();
		drawPile.addAll(exhaustPile);
		exhaustPile.clear();
		shuffle(drawPile);
		if (DeckBuilderRun.hasRelic(DeckRelic.ESCAPE_ROPE)) {
			gainBlock(6);
		}
	}

	public void shuffleAllCardsIntoDrawPileExceptHandIndex(int handIndex) {
		for (int i = hand.size() - 1; i >= 0; i--) {
			if (i == handIndex) continue;
			drawPile.add(DeckCardCode.withoutCostOverride(hand.remove(i)));
			if (i < handIndex) currentPlayHandIndexShift--;
		}
		drawPile.addAll(discardPile);
		discardPile.clear();
		drawPile.addAll(exhaustPile);
		exhaustPile.clear();
		shuffle(drawPile);
		if (DeckBuilderRun.hasRelic(DeckRelic.ESCAPE_ROPE)) {
			gainBlock(6);
		}
	}

	public void randomizeHandCostsThisTurn() {
		for (int i = 0; i < hand.size(); i++) {
			hand.set(i, DeckCardCode.withCostOverride(hand.get(i), Random.Int(4)));
		}
	}

	private void upgradeRandomDrawPileCards(int count) {
		for (int n = 0; n < count; n++) {
			ArrayList<Integer> upgradable = new ArrayList<>();
			for (int i = 0; i < drawPile.size(); i++) {
				int code = drawPile.get(i);
				if (DeckCardCode.upgrade(code) != code) upgradable.add(i);
			}
			if (upgradable.isEmpty()) return;
			int idx = upgradable.get(Random.Int(upgradable.size()));
			drawPile.set(idx, DeckCardCode.upgrade(drawPile.get(idx)));
		}
	}

	private static void shuffle(ArrayList<Integer> cards) {
		for (int i = cards.size() - 1; i > 0; i--) {
			int j = Random.Int(i + 1);
			Integer tmp = cards.get(i);
			cards.set(i, cards.get(j));
			cards.set(j, tmp);
		}
	}

	private static int[] toArray(ArrayList<Integer> list) {
		int[] result = new int[list.size()];
		for (int i = 0; i < list.size(); i++) {
			result[i] = list.get(i);
		}
		return result;
	}

	private static void restoreList(ArrayList<Integer> list, Bundle bundle, String key) {
		list.clear();
		if (!bundle.contains(key)) return;
		for (int id : bundle.getIntArray(key)) {
			list.add(id);
		}
	}

	public int loseHP(int amount) {
		return loseHP(amount, null);
	}

	public int loseHP(int amount, DeckPlayResult.Builder result) {
		if (amount <= 0) return 0;
		amount = preventableHpLoss(amount);
		if (amount <= 0) return 0;
		int before = DeckBuilderRun.playerHP;
		DeckBuilderRun.playerHP = Math.max(0, DeckBuilderRun.playerHP - amount);
		int actual = before - DeckBuilderRun.playerHP;
		if (actual > 0) {
			lastDamageEvents.add(DamageEvent.player(actual));
			if (playerHPLostCountThisCombat == 0 && DeckBuilderRun.hasRelic(DeckRelic.MOVEMENT_COMMAND_DISC)) {
				draw(3);
			}
			playerHPLostCountThisTurn++;
			playerHPLostCountThisCombat++;
			if (ruptureStrengthPerLoss > 0) {
				playerStrength += ruptureStrengthPerLoss;
				if (result != null) result.strength += ruptureStrengthPerLoss;
			}
			if (fireseaDamagePerLoss > 0) {
				for (DeckCombatEnemy enemy : aliveEnemies()) {
					int dealt = damageEnemy(enemy, fireseaDamagePerLoss, false);
					if (result != null && dealt > 0) result.addAttackHit(enemyIndex(enemy), dealt);
				}
			}
		}
		return actual;
	}

	private int preventableHpLoss(int amount) {
		if (preventHpLossTurns > 0) return 0;
		if (playerBlessed > 0 && amount > 0) return 1;
		if (DeckBuilderRun.hasRelic(DeckRelic.THE_HUSTLE)) return Math.max(0, amount - 1);
		return amount;
	}

	private boolean hasCardActiveInCombat(DeckCard card) {
		for (int code : drawPile) { if (DeckCard.byCode(code) == card) return true; }
		for (int code : hand) { if (DeckCard.byCode(code) == card) return true; }
		for (int code : discardPile) { if (DeckCard.byCode(code) == card) return true; }
		return false;
	}

	private int activePowerBlock(DeckCard card) {
		int block = 0;
		for (int code : powersPlayed) {
			if (DeckCard.byCode(code) == card) {
				block += card.block(code);
			}
		}
		return block;
	}

	public static class EnemyAction {
		public final int enemyIndex;
		public final int damage;
		public final boolean slimyInject;
		public final String label;
		public final boolean blocked;
		public DeckCard shuffledCard;
		public int shuffledCount;
		public int counterDamage;

		public EnemyAction(int enemyIndex, int damage, boolean slimyInject) {
			this(enemyIndex, damage, slimyInject, null);
		}

		public EnemyAction(int enemyIndex, int damage, boolean slimyInject, String label) {
			this(enemyIndex, damage, slimyInject, label, false);
		}

		public EnemyAction(int enemyIndex, int damage, boolean slimyInject, String label, boolean blocked) {
			this(enemyIndex, damage, slimyInject, label, blocked, null, 0);
		}

		public EnemyAction(int enemyIndex, int damage, boolean slimyInject, String label, boolean blocked, DeckCard shuffledCard, int shuffledCount) {
			this.enemyIndex = enemyIndex;
			this.damage = damage;
			this.slimyInject = slimyInject;
			this.label = label;
			this.blocked = blocked;
			this.shuffledCard = shuffledCard;
			this.shuffledCount = shuffledCount;
		}

		public void setShuffle(DeckCard shuffledCard, int shuffledCount) {
			this.shuffledCard = shuffledCard;
			this.shuffledCount = shuffledCount;
		}
	}

	public static class DamageEvent {
		public static final int PLAYER = -1;

		public final int enemyIndex;
		public final int damage;
		public final int enemyHpAfter;

		private DamageEvent(int enemyIndex, int damage, int enemyHpAfter) {
			this.enemyIndex = enemyIndex;
			this.damage = damage;
			this.enemyHpAfter = enemyHpAfter;
		}

		public static DamageEvent player(int damage) {
			return new DamageEvent(PLAYER, damage, -1);
		}

		public static DamageEvent enemy(int enemyIndex, int damage) {
			return new DamageEvent(enemyIndex, damage, -1);
		}

		public static DamageEvent enemy(int enemyIndex, int damage, int enemyHpAfter) {
			return new DamageEvent(enemyIndex, damage, enemyHpAfter);
		}

		public boolean playerTarget() {
			return enemyIndex == PLAYER;
		}
	}
}
