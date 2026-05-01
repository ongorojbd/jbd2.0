package com.shatteredpixel.shatteredpixeldungeon.items.quest;

import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

import java.util.ArrayList;

public class Spw43 extends Item {
    public static final String AC_LIGHT = "LIGHT";

    {
        image = ItemSpriteSheet.BATTLE_AXE;

        stackable = true;
        levelKnown = true;

        defaultAction = AC_LIGHT;
        upgrade(Statistics.spw43);
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
        int nextLevel = Math.max(1, Statistics.spw43 + 1);
        int accCurrent  = bonusPercent(accuracyMultiplier(nextLevel));
        int evaCurrent  = bonusPercent(evasionMultiplier(nextLevel));
        int accIncrement = bonusPercent(accuracyMultiplier(nextLevel + 1)) - accCurrent;
        int evaIncrement = bonusPercent(evasionMultiplier(nextLevel + 1)) - evaCurrent;
        return Messages.get(this, "desc", accCurrent, evaCurrent, accIncrement, evaIncrement);
    }

    public static float accuracyMultiplier() {
        return accuracyMultiplier(Statistics.spw43);
    }

    public static float accuracyMultiplier(int level) {
        return (float) Math.pow(1.3f, Math.max(0, Math.min(8, level)));
    }

    public static float evasionMultiplier() {
        return evasionMultiplier(Statistics.spw43);
    }

    public static float evasionMultiplier(int level) {
        return (float) Math.pow(1.125, Math.max(0, Math.min(8, level)));
    }

    private static int bonusPercent(float multiplier) {
        return Math.round((multiplier - 1f) * 100f);
    }
}
