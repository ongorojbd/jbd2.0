package com.shatteredpixel.shatteredpixeldungeon.items.quest;

import static com.shatteredpixel.shatteredpixeldungeon.Statistics.spw36;

import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

import java.util.ArrayList;

public class Spw36 extends Item {
    public static final String AC_LIGHT = "LIGHT";

    {
        image = ItemSpriteSheet.BREW_UNSTABLE;
        stackable = true;
        levelKnown = true;
        defaultAction = AC_LIGHT;
        upgrade(Statistics.spw36);
        unique = true;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_LIGHT);
        return actions;
    }

    @Override
    public String desc() {
        int chance = 20 + spw36 * 20;
        return "보상을 받을 때마다, 무작위 강화 물약을 _" +chance + "%_ 확률로 획득할 수 있습니다.\n\n이 효과를 선택할 때마다 확률이 +_20%_만큼 더 증가합니다.";
    }
}



