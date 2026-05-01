package com.shatteredpixel.shatteredpixeldungeon.items.quest;

import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

import java.util.ArrayList;

public class Spw51 extends Item {
    public static final String AC_LIGHT = "LIGHT";

    {
        image = ItemSpriteSheet.WILD_ENERGY;

        stackable = true;
        levelKnown = true;

        defaultAction = AC_LIGHT;
        upgrade(Statistics.spw51);
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
        int nextLevel = Math.max(1, Statistics.spw51 + 1);
        int current = bonusPercent(chargeMultiplier(nextLevel));
        int increment = bonusPercent(chargeMultiplier(nextLevel + 1)) - bonusPercent(chargeMultiplier(nextLevel));
        return Messages.get(this, "desc", current, increment);
    }

    public static float chargeMultiplier() {
        return chargeMultiplier(Statistics.spw51);
    }

    public static float chargeMultiplier(int level) {
        return (float) Math.pow(1.175, Math.max(0, Math.min(8, level)));
    }

    private static int bonusPercent(float multiplier) {
        return Math.round((multiplier - 1f) * 100f);
    }
}
