package com.shatteredpixel.shatteredpixeldungeon.items.quest;

import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

import java.util.ArrayList;

public class Spw4 extends Item {
    public static final String AC_LIGHT = "LIGHT";

    {
        image = ItemSpriteSheet.RING_AGATE;
        icon = ItemSpriteSheet.Icons.RING_SHARPSHOOT;

        stackable = true;
        levelKnown = true;

        defaultAction = AC_LIGHT;
        upgrade(Statistics.spw4);
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
        int nextLevel = Math.max(1, Statistics.spw4 + 1);
        int currentDurability = bonusPercent(durabilityMultiplier(nextLevel));
        int durabilityIncrement = bonusPercent(durabilityMultiplier(nextLevel + 1)) - bonusPercent(durabilityMultiplier(nextLevel));
        return Messages.get(this, "desc", nextLevel, currentDurability, durabilityIncrement);
    }

    public static int levelDamageBonus() {
        return Math.max(0, Math.min(8, Statistics.spw4));
    }

    public static float durabilityMultiplier() {
        return durabilityMultiplier(Statistics.spw4);
    }

    public static float durabilityMultiplier(int level) {
        return (float) Math.pow(1.2, Math.max(0, Math.min(8, level)));
    }

    private static int bonusPercent(float multiplier) {
        return Math.round((multiplier - 1f) * 100f);
    }

    @Override
    public boolean isUpgradable() {
        return true;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }
}
