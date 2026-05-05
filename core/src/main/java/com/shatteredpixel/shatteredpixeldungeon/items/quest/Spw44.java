package com.shatteredpixel.shatteredpixeldungeon.items.quest;

import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

import java.util.ArrayList;

public class Spw44 extends Item {
    public static final String AC_LIGHT = "LIGHT";

    {
        image = ItemSpriteSheet.GAUNTLETS;
        icon = ItemSpriteSheet.Icons.RING_FUROR;

        stackable = true;
        levelKnown = true;

        defaultAction = AC_LIGHT;
        upgrade(Statistics.spw44);
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
        int current = bonusPercent(attackSpeedMultiplier(Statistics.spw44));
        int nextLevel = Math.min(8, Statistics.spw44 + 1);
        int increment = bonusPercent(attackSpeedMultiplier(nextLevel)) - current;
        return Messages.get(this, "desc", current, increment);
    }

    public static float attackSpeedMultiplier() {
        return attackSpeedMultiplier(Statistics.spw44);
    }

    public static float attackSpeedMultiplier(int level) {
        return (float) Math.pow(1.09051, Math.max(0, Math.min(8, level)));
    }

    private static int bonusPercent(float multiplier) {
        return Math.round((multiplier - 1f) * 100f);
    }
}
