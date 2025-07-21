/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2024 Evan Debenham
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

import com.watabou.utils.Bundle;
import com.watabou.utils.SparseArray;
import java.util.Arrays;
import java.util.HashSet;

public class Statistics {

    public static int goldCollected;
    public static int deepestFloor;
    public static int highestAscent;
    public static int enemiesSlain;
    public static int foodEaten;
    public static int itemsCrafted;
    public static int piranhasKilled;
    public static int hazardAssistedKills;
    public static int yorihimes;
    public static int ankhsUsed;
    //tracks every item type 'seen' this run (i.e. would be added to catalogs)
    public static HashSet<Class> itemTypesDiscovered = new HashSet<>();

    //These are used for score calculation
    // some are built incrementally, most are assigned when full score is calculated
    public static int progressScore;
    public static int heldItemValue;
    public static int treasureScore;
    public static SparseArray<Float> floorsExplored = new SparseArray<>();
    public static int exploreScore;
    public static int[] bossScores = new int[5];
    public static int totalBossScore;
    public static int[] questScores = new int[5];
    public static int totalQuestScore;
    public static float winMultiplier;
    public static float chalMultiplier;
    public static int totalScore;

    //used for hero unlock badges
    public static int upgradesUsed;
    public static int sneakAttacks;
    public static int thrownAttacks;

    public static int spawnersAlive;

    public static float duration;

    public static int timeReset;
    public static int neoroca;

    public static int bcom;
    public static int duwang;
    public static int duwang2;
    public static int duwang3;
    public static int spw1;
    public static int spw2;
    public static int spw3;
    public static int spw4;
    public static int spw5;
    public static int spw6;
    public static int spw7;
    public static int spw8;
    public static int spw9;
    public static int spw10;
    public static int spw11;
    public static int spw12;
    public static int spw13;
    public static int spw14;
    public static int spw15;
    public static int cizah;
    public static int diocount;
    public static int zombiecount;
    public static int polpocount;

    public static int wave;

    public static int manga;

    public static boolean qualifiedForNoKilling = false;
    public static boolean completedWithNoKilling = false;
    public static boolean qualifiedForBossRemainsBadge = false;
    public static boolean qualifiedForBossChallengeBadge = false;

    public static boolean amuletObtained = false;
    public static boolean gameWon = false;
    public static boolean ascended = false;
    public static boolean polpoQuest = false;
    public static boolean diokilled = false;

    public static void reset() {

        goldCollected = 0;
        deepestFloor = 0;
        highestAscent = 0;
        enemiesSlain = 0;
        foodEaten = 0;
        itemsCrafted = 0;
        piranhasKilled = 0;
        hazardAssistedKills = 0;
        yorihimes = 0;
        ankhsUsed = 0;
        itemTypesDiscovered.clear();

        progressScore = 0;
        heldItemValue = 0;
        treasureScore = 0;
        floorsExplored = new SparseArray<>();
        exploreScore = 0;
        bossScores = new int[5];
        totalBossScore = 0;
        questScores = new int[5];
        totalQuestScore = 0;
        winMultiplier = 1;
        chalMultiplier = 1;
        totalScore = 0;

        upgradesUsed = 0;
        sneakAttacks = 0;
        thrownAttacks = 0;

        spawnersAlive = 0;

        timeReset = 0;
        neoroca = 0;
        bcom = 0;
        duwang = 0;
        duwang2 = 0;
        duwang3 = 0;
        spw1 = 0;
        spw2 = 0;
        spw3 = 0;
        spw4 = 0;
        spw5 = 0;
        spw6 = 0;
        spw7 = 0;
        spw8 = 0;
        spw9 = 0;
        spw10 = 0;
        spw11 = 0;
        spw12 = 0;
        spw13 = 0;
        spw14= 0;
        spw15= 0;
        cizah= 0;
        diocount = 0;
        zombiecount = 0;
        polpocount = 0;
        wave = 1;
        manga = 0;

        duration = 0;

        qualifiedForNoKilling = false;
        qualifiedForBossRemainsBadge = false;
        qualifiedForBossChallengeBadge = false;

        amuletObtained = false;
        gameWon = false;
        ascended = false;
        diokilled = false;
        polpoQuest = false;

    }

    private static final String GOLD = "score";
    private static final String DEEPEST = "maxDepth";
    private static final String HIGHEST = "maxAscent";
    private static final String SLAIN = "enemiesSlain";
    private static final String FOOD = "foodEaten";
    private static final String ALCHEMY = "potionsCooked";
    private static final String PIRANHAS = "priranhas";
    private static final String HAZARD_ASSISTS = "hazard_assists";
    private static final String YORIHIMES = "yorihimes";
    private static final String ANKHS = "ankhsUsed";

    private static final String TIMERESET = "timeReset";
    private static final String NEOROCA = "neoroca";
    private static final String BCOM = "bcom";
    private static final String DUWANG = "duwang";
    private static final String DUWANG2 = "duwang2";
    private static final String DUWANG3 = "duwang3";
    private static final String SPW1 = "spw1";
    private static final String SPW2 = "spw2";
    private static final String SPW3 = "spw3";
    private static final String SPW4 = "spw4";
    private static final String SPW5 = "spw5";
    private static final String SPW6 = "spw6";
    private static final String SPW7 = "spw7";
    private static final String SPW8 = "spw8";
    private static final String SPW9 = "spw9";
    private static final String SPW10 = "spw10";
    private static final String SPW11 = "spw11";
    private static final String SPW12 = "spw12";
    private static final String SPW13 = "spw13";
    private static final String SPW14 = "spw14";
    private static final String SPW15 = "spw15";
    private static final String CIZAH = "cizah";
    private static final String DIOCOUNT = "diocount";
    private static final String ZOMBIECOUNT = "zombiecount";
    private static final String POLPOCOUNT = "polpocount";
    private static final String WAVE = "wave";
    private static final String MANGA = "manga";
    private static final String PROG_SCORE = "prog_score";
    private static final String ITEM_VAL = "item_val";
    private static final String TRES_SCORE = "tres_score";
    private static final String FLR_EXPL = "flr_expl_";
    private static final String EXPL_SCORE = "expl_score";
    private static final String BOSS_SCORES = "boss_scores";
    private static final String TOT_BOSS = "tot_boss";
    private static final String QUEST_SCORES = "quest_scores";
    private static final String TOT_QUEST = "tot_quest";
    private static final String WIN_MULT = "win_mult";
    private static final String CHAL_MULT = "chal_mult";
    private static final String TOTAL_SCORE = "total_score";

    private static final String UPGRADES = "upgradesUsed";
    private static final String SNEAKS = "sneakAttacks";
    private static final String THROWN = "thrownAssists";
    private static final String ITEM_TYPES_DISCOVERED = "item_types_discovered";
    private static final String SPAWNERS = "spawnersAlive";

    private static final String DURATION = "duration";

    private static final String NO_KILLING_QUALIFIED = "qualifiedForNoKilling";

    private static final String BOSS_REMAINS_QUALIFIED = "qualifiedForBossRemainsBadge";
    private static final String BOSS_CHALLENGE_QUALIFIED = "qualifiedForBossChallengeBadge";

    private static final String AMULET = "amuletObtained";
    private static final String WON = "won";
    private static final String ASCENDED = "ascended";
    private static final String DIOKILLED = "diokilled";
    private static final String POLPOQUEST = "polpoQuest";

    public static void storeInBundle(Bundle bundle) {
        bundle.put(GOLD, goldCollected);
        bundle.put(DEEPEST, deepestFloor);
        bundle.put(HIGHEST, highestAscent);
        bundle.put(SLAIN, enemiesSlain);
        bundle.put(FOOD, foodEaten);
        bundle.put(ALCHEMY, itemsCrafted);
        bundle.put(PIRANHAS, piranhasKilled);
        bundle.put(HAZARD_ASSISTS, hazardAssistedKills);
        bundle.put(YORIHIMES, yorihimes);
        bundle.put(ANKHS, ankhsUsed);
        bundle.put(ITEM_TYPES_DISCOVERED, itemTypesDiscovered.toArray(new Class<?>[0]));

        bundle.put(TIMERESET, timeReset);
        bundle.put(NEOROCA, neoroca);
        bundle.put(BCOM, bcom);
        bundle.put(DUWANG, duwang);
        bundle.put(DUWANG2, duwang2);
        bundle.put(DUWANG3, duwang3);
        bundle.put(SPW1, spw1);
        bundle.put(SPW2, spw2);
        bundle.put(SPW3, spw3);
        bundle.put(SPW4, spw4);
        bundle.put(SPW5, spw5);
        bundle.put(SPW6, spw6);
        bundle.put(SPW7, spw7);
        bundle.put(SPW8, spw8);
        bundle.put(SPW9, spw9);
        bundle.put(SPW10, spw10);
        bundle.put(SPW11, spw11);
        bundle.put(SPW12, spw12);
        bundle.put(SPW13, spw13);
        bundle.put(SPW14, spw14);
        bundle.put(SPW15, spw15);
        bundle.put(CIZAH, cizah);
        bundle.put(DIOCOUNT, diocount);
        bundle.put(ZOMBIECOUNT, zombiecount);
        bundle.put(POLPOCOUNT, polpocount);
        bundle.put(WAVE, wave);
        bundle.put(MANGA, manga);
        bundle.put(PROG_SCORE, progressScore);
        bundle.put(ITEM_VAL, heldItemValue);
        bundle.put(TRES_SCORE, treasureScore);
        for (int i = 1; i < 31; i++) {
            if (floorsExplored.containsKey(i)) {
                bundle.put(FLR_EXPL + i, floorsExplored.get(i));
            }
        }
        bundle.put(EXPL_SCORE, exploreScore);
        bundle.put(BOSS_SCORES, bossScores);
        bundle.put(TOT_BOSS, totalBossScore);
        bundle.put(QUEST_SCORES, questScores);
        bundle.put(TOT_QUEST, totalQuestScore);
        bundle.put(WIN_MULT, winMultiplier);
        bundle.put(CHAL_MULT, chalMultiplier);
        bundle.put(TOTAL_SCORE, totalScore);

        bundle.put(UPGRADES, upgradesUsed);
        bundle.put(SNEAKS, sneakAttacks);
        bundle.put(THROWN, thrownAttacks);

        bundle.put(SPAWNERS, spawnersAlive);

        bundle.put(DURATION, duration);

        bundle.put(NO_KILLING_QUALIFIED, qualifiedForNoKilling);
        bundle.put(BOSS_REMAINS_QUALIFIED, qualifiedForBossRemainsBadge);
        bundle.put(BOSS_CHALLENGE_QUALIFIED, qualifiedForBossChallengeBadge);

        bundle.put(AMULET, amuletObtained);
        bundle.put(WON, gameWon);
        bundle.put(ASCENDED, ascended);
        bundle.put(DIOKILLED, diokilled);
        bundle.put(POLPOQUEST, polpoQuest);
    }

    public static void restoreFromBundle(Bundle bundle) {
        goldCollected = bundle.getInt(GOLD);
        deepestFloor = bundle.getInt(DEEPEST);
        highestAscent = bundle.getInt(HIGHEST);
        enemiesSlain = bundle.getInt(SLAIN);
        foodEaten = bundle.getInt(FOOD);
        itemsCrafted = bundle.getInt(ALCHEMY);
        piranhasKilled = bundle.getInt(PIRANHAS);
        hazardAssistedKills = bundle.getInt( HAZARD_ASSISTS );
        yorihimes = bundle.getInt(YORIHIMES);
        ankhsUsed = bundle.getInt(ANKHS);
        if (bundle.contains( ITEM_TYPES_DISCOVERED )) {
            itemTypesDiscovered = new HashSet<>(Arrays.asList(bundle.getClassArray(ITEM_TYPES_DISCOVERED)));
        } else {
            itemTypesDiscovered.clear();
        }

        timeReset = bundle.getInt(TIMERESET);
        neoroca = bundle.getInt(NEOROCA);
        bcom = bundle.getInt(BCOM);
        duwang = bundle.getInt(DUWANG);
        duwang2 = bundle.getInt(DUWANG2);
        duwang3 = bundle.getInt(DUWANG3);
        spw1 = bundle.getInt(SPW1);
        spw2 = bundle.getInt(SPW2);
        spw3 = bundle.getInt(SPW3);
        spw4 = bundle.getInt(SPW4);
        spw5 = bundle.getInt(SPW5);
        spw6 = bundle.getInt(SPW6);
        spw7 = bundle.getInt(SPW7);
        spw8 = bundle.getInt(SPW8);
        spw9 = bundle.getInt(SPW9);
        spw10 = bundle.getInt(SPW10);
        spw11 = bundle.getInt(SPW11);
        spw12 = bundle.getInt(SPW12);
        spw13 = bundle.getInt(SPW13);
        spw14 = bundle.getInt(SPW14);
        spw15 = bundle.getInt(SPW15);
        cizah = bundle.getInt(CIZAH);
        diocount = bundle.getInt(DIOCOUNT);
        zombiecount = bundle.getInt(ZOMBIECOUNT);
        polpocount = bundle.getInt(POLPOCOUNT);
        wave = bundle.getInt(WAVE);
        manga = bundle.getInt(MANGA);
        progressScore = bundle.getInt(PROG_SCORE);
        heldItemValue = bundle.getInt(ITEM_VAL);
        treasureScore = bundle.getInt(TRES_SCORE);
        floorsExplored.clear();
        for (int i = 1; i < 31; i++) {
            if (bundle.contains(FLR_EXPL + i)) {
                //we have this check to reduce an error with bad conversion specifically in v3.1-BETA-1.0
                if (!Dungeon.bossLevel(i) && i <= deepestFloor){
                    floorsExplored.put(i, bundle.getFloat( FLR_EXPL+i ));
                }
                //pre-3.1 saves. The bundle key does have an underscore and is a boolean
            } else if (bundle.contains( "flr_expl"+i )){
                floorsExplored.put(i, bundle.getBoolean( "flr_expl"+i ) ? 1f : 0f);
            }
        }
        exploreScore = bundle.getInt(EXPL_SCORE);
        if (bundle.contains(BOSS_SCORES)) bossScores = bundle.getIntArray(BOSS_SCORES);
        else bossScores = new int[5];
        totalBossScore = bundle.getInt(TOT_BOSS);
        if (bundle.contains(QUEST_SCORES)) questScores = bundle.getIntArray(QUEST_SCORES);
        else questScores = new int[5];
        totalQuestScore = bundle.getInt(TOT_QUEST);
        winMultiplier = bundle.getFloat(WIN_MULT);
        chalMultiplier = bundle.getFloat(CHAL_MULT);
        totalScore = bundle.getInt(TOTAL_SCORE);

        upgradesUsed = bundle.getInt(UPGRADES);
        sneakAttacks = bundle.getInt(SNEAKS);
        thrownAttacks = bundle.getInt(THROWN);

        spawnersAlive = bundle.getInt(SPAWNERS);

        duration = bundle.getFloat(DURATION);

        qualifiedForNoKilling = bundle.getBoolean(NO_KILLING_QUALIFIED);
        qualifiedForBossRemainsBadge = bundle.getBoolean( BOSS_REMAINS_QUALIFIED );
        qualifiedForBossChallengeBadge = bundle.getBoolean(BOSS_CHALLENGE_QUALIFIED);

        amuletObtained = bundle.getBoolean(AMULET);
        gameWon = bundle.getBoolean(WON);
        ascended = bundle.getBoolean(ASCENDED);
        diokilled = bundle.getBoolean(DIOKILLED);
        polpoQuest = bundle.getBoolean(POLPOQUEST);
    }

    public static void preview(GamesInProgress.Info info, Bundle bundle) {
        info.goldCollected = bundle.getInt(GOLD);
        info.maxDepth = bundle.getInt(DEEPEST);
    }

}
