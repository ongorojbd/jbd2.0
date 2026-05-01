package com.shatteredpixel.shatteredpixeldungeon.items.quest;

import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;

import java.util.ArrayList;

public class Spw46 extends Item {
    public static final String AC_LIGHT = "LIGHT";

    {
        image = ItemSpriteSheet.ARMOR_WARRIOR;

        stackable = true;
        levelKnown = true;

        defaultAction = AC_LIGHT;
        upgrade(Statistics.spw46);
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
        return Messages.get(this, "desc", cooldown(Math.max(1, Statistics.spw46 + 1)));
    }

    public static void updateRipples(Hero hero) {
        if (Statistics.spw46 <= 0
                || hero.buff(FinalRippleReady.class) != null
                || hero.buff(FinalRippleCooldown.class) != null) {
            return;
        }
        if (hero.HP <= Math.max(1, Math.floor(hero.HT * 0.20f))) {
            Buff.affect(hero, FinalRippleReady.class);
        }
    }

    public static int proc(Hero hero, Char enemy, int damage) {
        FinalRippleReady ready = hero.buff(FinalRippleReady.class);
        if (Statistics.spw46 <= 0 || ready == null) {
            return damage;
        }

        ready.detach();
        int heal = healAmount(hero);
        hero.HP = Math.min(hero.HT, hero.HP + heal);
        if (hero.sprite != null) {
            hero.sprite.showStatusWithIcon(CharSprite.POSITIVE, Integer.toString(heal), FloatingText.HEALING);
            hero.sprite.emitter().burst(Speck.factory(Speck.HEALING), 3);
        }
        Buff.prolong(enemy, Paralysis.class, 2f);
        Buff.affect(hero, FinalRippleCooldown.class, cooldown());
        return damage + bonusDamage(hero);
    }

    public static int cooldown() {
        return cooldown(Statistics.spw46);
    }

    public static int cooldown(int level) {
        return Math.max(20, 100 - 10 * Math.max(1, Math.min(8, level)));
    }

    public static int healAmount(Hero hero) {
        return Math.max(1, Math.round(hero.HT * 0.45f));
    }

    public static int bonusDamage(Hero hero) {
        return Math.max(1, Math.round(hero.HT * 0.30f));
    }

    public static class FinalRippleReady extends Buff {
        {
            type = buffType.POSITIVE;
        }

        @Override
        public int icon() {
            return BuffIndicator.WEAPON;
        }

        @Override
        public void tintIcon(Image icon) {
            icon.hardlight(1f, 0.85f, 0.25f);
        }
    }

    public static class FinalRippleCooldown extends FlavourBuff {
        {
            type = buffType.NEUTRAL;
        }

        @Override
        public int icon() {
            return BuffIndicator.TIME;
        }

        @Override
        public void tintIcon(Image icon) {
            icon.hardlight(0.65f, 0.85f, 1f);
        }
    }
}
