package com.shatteredpixel.shatteredpixeldungeon.items.quest;

import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Spw49 extends Item {
    public static final String AC_LIGHT = "LIGHT";

    {
        image = ItemSpriteSheet.DBLADE;

        stackable = true;
        levelKnown = true;

        defaultAction = AC_LIGHT;
        upgrade(Statistics.spw49);
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
        int nextLevel = Math.max(1, Statistics.spw49 + 1);
        return Messages.get(this, "desc", chance(nextLevel), chance(nextLevel + 1));
    }

    public static int proc(Hero attacker, int damage) {
        if (Statistics.spw49 <= 0 || Random.Int(100) >= chance(Statistics.spw49)) {
            return damage;
        }
        if (attacker.sprite != null) {
            attacker.sprite.showStatus(CharSprite.NEUTRAL, "[치명타 공격!]");
        }
        return Math.round(damage * 2f);
    }

    public static int chance(int level) {
        return Math.max(1, Math.min(8, level)) * 8;
    }
}
