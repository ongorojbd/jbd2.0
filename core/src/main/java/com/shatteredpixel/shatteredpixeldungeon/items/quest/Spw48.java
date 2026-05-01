package com.shatteredpixel.shatteredpixeldungeon.items.quest;

import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Food;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.Scroll;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Spell;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.Runestone;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Spw48 extends Item {
    public static final String AC_LIGHT = "LIGHT";

    {
        image = ItemSpriteSheet.RACE;

        stackable = true;
        levelKnown = true;

        defaultAction = AC_LIGHT;
        upgrade(Statistics.spw48);
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
        int nextLevel = Math.max(1, Statistics.spw48 + 1);
        return Messages.get(this, "desc", chance(nextLevel), chance(nextLevel + 1));
    }

    public static boolean preserves(Item item) {
        return Statistics.spw48 > 0
                && isConsumable(item)
                && Random.Int(100) < chance(Statistics.spw48);
    }

    public static int chance(int level) {
        return Math.max(1, Math.min(8, level)) * 8;
    }

    private static boolean isConsumable(Item item) {
        return item instanceof Potion
                || item instanceof Scroll
                || item instanceof Runestone
                || item instanceof Food
                || item instanceof Spell;
    }
}
