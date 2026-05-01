package com.shatteredpixel.shatteredpixeldungeon.items.quest;

import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

import java.util.ArrayList;

public class Spw45 extends Item {
    public static final String AC_LIGHT = "LIGHT";

    {
        image = ItemSpriteSheet.ORE;

        stackable = true;
        levelKnown = true;

        defaultAction = AC_LIGHT;
        upgrade(Statistics.spw45);
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
        int nextLevel = Math.max(1, Statistics.spw45 + 1);
        int current = chaliceLevel(nextLevel);
        int increment = chaliceLevel(nextLevel + 1) - chaliceLevel(nextLevel);
        return Messages.get(this, "desc", current, increment);
    }

    public static int chaliceLevel() {
        return Statistics.spw45 <= 0 ? -1 : chaliceLevel(Statistics.spw45);
    }

    public static int chaliceLevel(int level) {
        return Math.max(1, Math.min(10, Math.round(Math.max(1, Math.min(8, level)) * 10f / 8f)));
    }

    public static int maxHealthPenalty() {
        return Statistics.spw45 * 2;
    }
}
