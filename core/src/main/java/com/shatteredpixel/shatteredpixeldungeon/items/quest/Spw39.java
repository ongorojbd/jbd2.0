package com.shatteredpixel.shatteredpixeldungeon.items.quest;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;

import java.util.ArrayList;

public class Spw39 extends Item {
    public static final String AC_LIGHT = "LIGHT";

    {
        image = ItemSpriteSheet.CLOAK_SCRAP;

        stackable = true;
        levelKnown = true;

        defaultAction = AC_LIGHT;
        upgrade(Statistics.spw39);
        unique = true;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_LIGHT);
        return actions;
    }

    @Override
    public String info() {
        int required = Math.max(1, 10 - Statistics.spw39);
        // Build dynamic description in Korean matching the style
        return "적에게 _" + required + "번째_로 물리 공격을 받을 때마다, 2턴의 투명화를 얻습니다.\n\n"
                + "이 효과를 선택할 때마다 필요한 피격 횟수가 -_1_만큼 더 감소합니다.";
    }
}






