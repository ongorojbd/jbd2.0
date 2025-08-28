package com.shatteredpixel.shatteredpixeldungeon.items.quest;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;

import java.util.ArrayList;

public class Spw40 extends Item {
    public static final String AC_LIGHT = "LIGHT";

    {
        image = ItemSpriteSheet.PETRIFIED_SEED;

        stackable = true;
        levelKnown = true;

        defaultAction = AC_LIGHT;
        upgrade(Statistics.spw40);
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
        int chance = Math.min(100, 8 + 8 * Statistics.spw40);
        return "적을 처치할 때마다, _" + chance + "_% 확률로 무작위 명령 DISC를 획득할 수 있습니다.\n\n"
                + "이 효과를 선택할 때마다 확률이 +_8_%만큼 더 증가합니다.";
    }
}






