package com.shatteredpixel.shatteredpixeldungeon.items.quest;

import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

import java.util.ArrayList;

public class Spw41 extends Item {
    public static final String AC_LIGHT = "LIGHT";

    {
        image = ItemSpriteSheet.SANDBAG;

        stackable = true;
        levelKnown = true;

        defaultAction = AC_LIGHT;
        upgrade(Statistics.spw41);
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
        return Messages.get(this, "desc", healAmount(Math.max(1, Statistics.spw41 + 1)));
    }

    public static int healAmount(int level) {
        return Math.max(1, Math.min(8, level)) * 2 - 1;
    }

    public static void healAllies() {
        if (Statistics.spw41 <= 0) {
            return;
        }

        int heal = healAmount(Statistics.spw41);
        for (Char ch : Actor.chars()) {
            if (ch.alignment == Char.Alignment.ALLY && ch.HP > 0 && ch.HP < ch.HT) {
                ch.HP = Math.min(ch.HP + heal, ch.HT);
                if (ch.sprite != null) {
                    ch.sprite.showStatusWithIcon(CharSprite.POSITIVE, Integer.toString(heal), FloatingText.HEALING);
                }
            }
        }
    }
}
