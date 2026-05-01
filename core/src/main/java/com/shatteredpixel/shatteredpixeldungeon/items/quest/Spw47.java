package com.shatteredpixel.shatteredpixeldungeon.items.quest;

import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public class Spw47 extends Item {
    public static final String AC_LIGHT = "LIGHT";

    {
        image = ItemSpriteSheet.RUNIC_BLADE;

        stackable = true;
        levelKnown = true;

        defaultAction = AC_LIGHT;
        upgrade(Statistics.spw47);
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
        return Messages.get(this, "desc",
                Math.round((damageMultiplier(Math.max(1, Statistics.spw47 + 1)) - 1f) * 100f),
                shieldAmount());
    }

    public static void prepare(Hero hero) {
        if (Statistics.spw47 > 0) {
            Buff.affect(hero, FocusedShotTracker.class).pos = hero.pos;
        }
    }

    public static int proc(Hero hero, int damage) {
        FocusedShotTracker tracker = hero.buff(FocusedShotTracker.class);
        if (Statistics.spw47 <= 0 || tracker == null) {
            return damage;
        }

        tracker.detach();
        int shield = shieldAmount();
        Buff.affect(hero, Barrier.class).setShield(shield);
        if (hero.sprite != null) {
            hero.sprite.showStatusWithIcon(CharSprite.POSITIVE, Integer.toString(shield), FloatingText.SHIELDING);
        }
        return Math.round(damage * damageMultiplier());
    }

    public static float damageMultiplier() {
        return damageMultiplier(Statistics.spw47);
    }

    public static float damageMultiplier(int level) {
        return 1f + Math.max(1, Math.min(8, level)) / 4f;
    }

    public static int shieldAmount() {
        return 5 * Math.max(1, Math.min(8, Statistics.spw47));
    }

    public static class FocusedShotTracker extends Buff {
        public int pos;

        {
            type = buffType.POSITIVE;
        }

        @Override
        public int icon() {
            return BuffIndicator.WEAPON;
        }

        @Override
        public void tintIcon(Image icon) {
            icon.hardlight(0.2f, 0.9f, 1f);
        }

        @Override
        public String desc() {
            return Messages.get(this, "desc", Math.round((damageMultiplier() - 1f) * 100f));
        }

        @Override
        public boolean act() {
            if (pos != target.pos) {
                detach();
            } else {
                spend(TICK);
            }
            return true;
        }

        private static final String POS = "pos";

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put(POS, pos);
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            pos = bundle.getInt(POS);
        }
    }
}
