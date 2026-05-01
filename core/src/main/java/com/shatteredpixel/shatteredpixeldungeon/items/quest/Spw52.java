package com.shatteredpixel.shatteredpixeldungeon.items.quest;

import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfElements;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

import java.util.ArrayList;

public class Spw52 extends Item {
    public static final String AC_LIGHT = "LIGHT";

    {
        image = ItemSpriteSheet.CEN;

        stackable = true;
        levelKnown = true;

        defaultAction = AC_LIGHT;
        upgrade(Statistics.spw52);
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
        int nextLevel = Math.max(1, Statistics.spw52 + 1);
        int tenacityIncrement = tenacityPercent(nextLevel + 1) - tenacityPercent(nextLevel);
        int elementsIncrement = elementsPercent(nextLevel + 1) - elementsPercent(nextLevel);
        return Messages.get(this, "desc",
                tenacityPercent(nextLevel), elementsPercent(nextLevel),
                tenacityIncrement, elementsIncrement);
    }

    public static float tenacityMultiplier(Char target) {
        return tenacityMultiplier(target, Statistics.spw52);
    }

    public static float tenacityMultiplier(Char target, int level) {
        if (level <= 0 || target.HT <= 0) {
            return 1f;
        }
        return (float) Math.pow(0.85, Math.min(8, level) * ((float) (target.HT - target.HP) / target.HT));
    }

    public static float resistMultiplier(Class effect) {
        if (Statistics.spw52 <= 0) {
            return 1f;
        }
        for (Class c : RingOfElements.RESISTS) {
            if (c.isAssignableFrom(effect)) {
                return (float) Math.pow(0.825, Math.min(8, Statistics.spw52));
            }
        }
        return 1f;
    }

    private static int tenacityPercent(int level) {
        return Math.round(100f * (1f - (float) Math.pow(0.85f, Math.min(8, level))));
    }

    private static int elementsPercent(int level) {
        return Math.round(100f * (1f - (float) Math.pow(0.825f, Math.min(8, level))));
    }
}
