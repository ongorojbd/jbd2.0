package com.shatteredpixel.shatteredpixeldungeon.items.quest;

import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

import java.util.ArrayList;

public class Spw37 extends Item {
    public static final String AC_LIGHT = "LIGHT";

    {
        image = ItemSpriteSheet.RING_DIAMOND;
        icon = ItemSpriteSheet.Icons.RING_ARCANA;

        stackable = true;
        levelKnown = true;

        defaultAction = AC_LIGHT;
        upgrade(Statistics.spw37);
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
        int nextLevel = Math.max(1, Statistics.spw37 + 1);
        int current = bonusPercent(enchantMultiplier(nextLevel));
        int increment = bonusPercent(enchantMultiplier(nextLevel + 1)) - bonusPercent(enchantMultiplier(nextLevel));
        return Messages.get(this, "desc", current, increment);
    }

    public static float enchantMultiplier() {
        return enchantMultiplier(Statistics.spw37);
    }

    public static float enchantMultiplier(int level) {
        return (float) Math.pow(1.175, Math.max(0, Math.min(8, level)));
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

    @Override
    public int value() {
        return 20 * quantity;
    }
}
