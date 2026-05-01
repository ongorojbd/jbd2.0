package com.shatteredpixel.shatteredpixeldungeon.items.quest;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.BlobImmunity;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FrostImbue;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Spw50 extends Item {
    public static final String AC_LIGHT = "LIGHT";

    {
        image = ItemSpriteSheet.TOKEN;

        stackable = true;
        levelKnown = true;

        defaultAction = AC_LIGHT;
        upgrade(Statistics.spw50);
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
        int currentLevel = Math.max(1, Statistics.spw50);
        int nextLevel = Math.max(1, Math.min(8, Statistics.spw50 + 1));
        return Messages.get(this, "desc",
                bonusPercent(currentLevel));
    }

    public static void rollForFloor(Hero hero) {
        if (Statistics.spw50 <= 0) {
            return;
        }
        FateDiceBuff buff = hero.buff(FateDiceBuff.class);
        if (buff == null || buff.depth != Dungeon.depth) {
            int roll = Random.IntRange(1, 6);
            FateDiceBuff dice = Buff.affect(hero, FateDiceBuff.class);
            dice.set(roll, Dungeon.depth);
            if (hero.sprite != null) {
                new Flare(6, 32).color(dice.flareColor(), true).show(hero.sprite, 2f);
            }
            Sample.INSTANCE.play(Assets.Sounds.SHEEP, 1f, 3.2f);
            GLog.p(Messages.get(Spw50.class, "rolled", roll));
        }
    }

    public static float damageMultiplier(Hero hero) {
        FateDiceBuff buff = hero.buff(FateDiceBuff.class);
        if (buff == null) return 1f;
        return buff.hasDamageBoost() ? 1f + bonus() : 1f;
    }

    public static float speedMultiplier(Hero hero) {
        FateDiceBuff buff = hero.buff(FateDiceBuff.class);
        if (buff == null) return 1f;
        return buff.hasSpeedBoost() ? 1f + bonus() : 1f;
    }

    public static float attackSpeedMultiplier(Hero hero) {
        FateDiceBuff buff = hero.buff(FateDiceBuff.class);
        if (buff == null) return 1f;
        return buff.hasAttackSpeedBoost() ? 1f + bonus() : 1f;
    }

    public static float evasionMultiplier(Hero hero) {
        FateDiceBuff buff = hero.buff(FateDiceBuff.class);
        if (buff == null) return 1f;
        return buff.hasEvasionBoost() ? 1f + bonus() : 1f;
    }

    public static float damageTakenMultiplier(Hero hero) {
        FateDiceBuff buff = hero.buff(FateDiceBuff.class);
        if (buff == null || !buff.hasDamageReduction()) return 1f;
        return 1f - damageReductionPercent(Statistics.spw50) / 100f;
    }

    private static float bonus() {
        return bonusPercent(Statistics.spw50) / 100f;
    }

    private static int bonusPercent(int level) {
        return 12 + Math.max(1, Math.min(8, level)) * 6;
    }

    private static int damageReductionPercent(int level) {
        return bonusPercent(level);
    }

    public static class FateDiceBuff extends Buff {
        public int roll;
        public int depth;
        private boolean appliedFrost;
        private boolean appliedBlobImmunity;

        {
            type = buffType.POSITIVE;
        }

        public void set(int roll, int depth) {
            this.roll = roll;
            this.depth = depth;
            this.appliedFrost = false;
            this.appliedBlobImmunity = false;
            applyFloorBuffs();
        }

        @Override
        public int icon() {
            return BuffIndicator.BLESS;
        }

        @Override
        public void tintIcon(Image icon) {
            icon.hardlight(1f, 0.95f, 0.45f);
        }

        @Override
        public String desc() {
            return Messages.get(this, "desc", rollName(), effectSummary());
        }

        @Override
        public boolean act() {
            if (depth != Dungeon.depth) {
                if (appliedFrost) {
                    Buff.detach(target, FrostImbue.class);
                }
                if (appliedBlobImmunity) {
                    Buff.detach(target, BlobImmunity.class);
                }
                detach();
                return true;
            }
            applyFloorBuffs();
            spend(TICK);
            return true;
        }

        public boolean hasEvasionBoost() {
            return roll == 1 || roll == 6;
        }

        public boolean hasDamageReduction() {
            return roll == 2 || roll == 6;
        }

        public boolean hasSpeedBoost() {
            return roll == 3 || roll == 6;
        }

        public boolean hasAttackSpeedBoost() {
            return roll == 4 || roll == 6;
        }

        public boolean hasDamageBoost() {
            return roll == 5 || roll == 6;
        }

        private void applyFloorBuffs() {
            if (target == null || depth != Dungeon.depth) {
                return;
            }
            if (roll == 4 || roll == 6) {
                Buff.prolong(target, BlobImmunity.class, 3f);
                appliedBlobImmunity = true;
            }
            if (roll == 5 || roll == 6) {
                Buff.prolong(target, FrostImbue.class, 3f);
                appliedFrost = true;
            }
        }

        private int flareColor() {
            switch (roll) {
                case 1:
                    return 0x66FF66;
                case 2:
                    return 0x6699FF;
                case 3:
                    return 0xFFCC33;
                case 4:
                    return 0x66FFFF;
                case 5:
                    return 0xFF6666;
                case 6:
                    return 0xFF9900;
                default:
                    return 0xFFFFFF;
            }
        }

        private String rollName() {
            switch (roll) {
                case 1:
                    return Messages.get(Spw50.class, "roll_1");
                case 2:
                    return Messages.get(Spw50.class, "roll_2");
                case 3:
                    return Messages.get(Spw50.class, "roll_3");
                case 4:
                    return Messages.get(Spw50.class, "roll_4");
                case 5:
                    return Messages.get(Spw50.class, "roll_5");
                case 6:
                    return Messages.get(Spw50.class, "roll_6");
                default:
                    return Integer.toString(roll);
            }
        }

        private String effectSummary() {
            int bonus = bonusPercent(Statistics.spw50);
            int reduction = damageReductionPercent(Statistics.spw50);
            switch (roll) {
                case 1:
                    return Messages.get(Spw50.class, "effect_1", bonus);
                case 2:
                    return Messages.get(Spw50.class, "effect_2", reduction);
                case 3:
                    return Messages.get(Spw50.class, "effect_3", bonus);
                case 4:
                    return Messages.get(Spw50.class, "effect_4", bonus);
                case 5:
                    return Messages.get(Spw50.class, "effect_5", bonus);
                case 6:
                    return Messages.get(Spw50.class, "effect_6", bonus, reduction);
                default:
                    return "";
            }
        }

        private static final String ROLL = "roll";
        private static final String DEPTH = "depth";
        private static final String APPLIED_FROST = "applied_frost";
        private static final String APPLIED_BLOB_IMMUNITY = "applied_blob_immunity";

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put(ROLL, roll);
            bundle.put(DEPTH, depth);
            bundle.put(APPLIED_FROST, appliedFrost);
            bundle.put(APPLIED_BLOB_IMMUNITY, appliedBlobImmunity);
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            roll = bundle.getInt(ROLL);
            depth = bundle.getInt(DEPTH);
            appliedFrost = bundle.getBoolean(APPLIED_FROST);
            appliedBlobImmunity = bundle.getBoolean(APPLIED_BLOB_IMMUNITY);
        }
    }
}
