package com.shatteredpixel.shatteredpixeldungeon.items.quest;

import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

import java.util.ArrayList;

public class Spw42 extends Item {
    public static final String AC_LIGHT = "LIGHT";

    {
        image = ItemSpriteSheet.ARMOR_JOHNNY;

        stackable = true;
        levelKnown = true;

        defaultAction = AC_LIGHT;
        upgrade(Statistics.spw42);
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
        int current = bonusPercent(speedMultiplier(Statistics.spw42));
        int nextLevel = Math.min(8, Statistics.spw42 + 1);
        int increment = bonusPercent(speedMultiplier(nextLevel)) - bonusPercent(speedMultiplier(Statistics.spw42));
        return Messages.get(this, "desc", current, increment);
    }

    public static float speedMultiplier() {
        return speedMultiplier(Statistics.spw42);
    }

    public static float speedMultiplier(int level) {
        return (float) Math.pow(1.175, Math.max(0, Math.min(8, level)));
    }

    private static int bonusPercent(float multiplier) {
        return Math.round((multiplier - 1f) * 100f);
    }
}
