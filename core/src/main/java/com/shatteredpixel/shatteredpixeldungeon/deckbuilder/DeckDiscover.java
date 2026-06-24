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
import com.watabou.utils.Random;

import java.util.ArrayList;

/**
 * 발견(Discover) 시스템: 3가지 카드 선택지 중 하나를 손으로 가져옵니다.
 *
 * Pool 종류:
 *   ALL          - reward=true인 모든 카드 (직업 카드 포함)
 *   MY_CLASS     - 내 직업 + 중립 카드
 *   CLASS_ONLY   - 내 직업 카드만
 *   NEUTRAL_ONLY - 중립 카드만
 *   OTHER_CLASS  - 타 직업 + 중립 카드
 *   FROM_DECK    - 현재 전투 덱에 있는 카드
 */
public class DeckDiscover {

    public enum Pool {
        ALL,
        MY_CLASS,
        CLASS_ONLY,
        NEUTRAL_ONLY,
        OTHER_CLASS,
        FROM_DECK,
        ATTACK,
        SKILL,
        POWER
    }

    public final Pool pool;
    public final int count;
    public final boolean zeroCost;
    public final boolean playAfterPick;

    public DeckDiscover(Pool pool) {
        this(pool, 3, false, false);
    }

    public DeckDiscover(Pool pool, boolean zeroCost) {
        this(pool, 3, zeroCost, false);
    }

    public DeckDiscover(Pool pool, int count, boolean zeroCost, boolean playAfterPick) {
        this.pool = pool;
        this.count = count;
        this.zeroCost = zeroCost;
        this.playAfterPick = playAfterPick;
    }

    public DeckCard[] rollChoices(DeckBuilderCombat combat) {
        DeckCard[] candidates = buildCandidates(combat);

        if (candidates.length == 0) {
            candidates = DeckCard.rewardPool();
        }

        ArrayList<DeckCard> shuffled = new ArrayList<>();
        for (DeckCard c : candidates) shuffled.add(c);
        for (int i = shuffled.size() - 1; i > 0; i--) {
            int j = Random.Int(i + 1);
            DeckCard tmp = shuffled.get(i);
            shuffled.set(i, shuffled.get(j));
            shuffled.set(j, tmp);
        }

        int actual = Math.min(count, shuffled.size());
        DeckCard[] choices = new DeckCard[actual];
        for (int i = 0; i < actual; i++) choices[i] = shuffled.get(i);
        return choices;
    }

    private DeckCard[] buildCandidates(DeckBuilderCombat combat) {
        com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass heroClass =
                Dungeon.hero != null ? Dungeon.hero.heroClass : null;

        switch (pool) {
            case MY_CLASS:
                return DeckCard.rewardPool(heroClass, false, false);
            case CLASS_ONLY:
                return DeckCard.rewardPool(heroClass, true, false);
            case NEUTRAL_ONLY:
                return DeckCard.rewardPool(null, false, true);
			case OTHER_CLASS:
				ArrayList<DeckCard> other = new ArrayList<>();
				for (DeckCard c : DeckCard.values()) {
					if (DeckCardPool.isOtherClassRewardCard(c, heroClass)) other.add(c);
				}
				return other.toArray(new DeckCard[0]);
            case FROM_DECK:
                if (combat != null) {
                    ArrayList<DeckCard> seen = new ArrayList<>();
                    ArrayList<Integer> allCodes = new ArrayList<>();
                    allCodes.addAll(combat.drawPile);
                    allCodes.addAll(combat.hand);
                    allCodes.addAll(combat.discardPile);
                    for (int code : allCodes) {
                        DeckCard c = DeckCard.byCode(code);
                        if (!seen.contains(c)) seen.add(c);
                    }
                    return seen.toArray(new DeckCard[0]);
                }
                return DeckCard.rewardPool();
            case ATTACK:
                return filterByType(DeckCard.rewardPool(heroClass, false, false), DeckCardType.ATTACK);
            case SKILL:
                return filterByType(DeckCard.rewardPool(heroClass, false, false), DeckCardType.SKILL);
            case POWER:
                return filterByType(DeckCard.rewardPool(heroClass, false, false), DeckCardType.POWER);
            case ALL:
            default:
                return DeckCard.rewardPool();
        }
    }

    private DeckCard[] filterByType(DeckCard[] source, DeckCardType type) {
        ArrayList<DeckCard> filtered = new ArrayList<>();
        for (DeckCard card : source) {
            if (card.type == type) filtered.add(card);
        }
        return filtered.toArray(new DeckCard[0]);
    }
}
