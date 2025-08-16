package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

public class TrialOfPillars extends Buff {

    {
        type = buffType.NEGATIVE;
        announced = true;
        revivePersists = true;
    }

    @Override
    public void fx(boolean on) {
        if (on) target.sprite.add(CharSprite.State.DARKENED);
        else if (target.invisible == 0) target.sprite.remove(CharSprite.State.DARKENED);
    }

    private int floorsLeft = 5;
    private int lastDepth;

    @Override
    public boolean attachTo(Char target) {
        boolean ok = super.attachTo(target);
        if (ok) {
            lastDepth = Dungeon.depth;
        }
        return ok;
    }

    @Override
    public boolean act() {
        // decrement only when descending to a new, deeper depth
        if (Dungeon.depth > lastDepth) {
            floorsLeft = Math.max(0, floorsLeft - 1);
            lastDepth = Dungeon.depth;
            if (floorsLeft == 0) {
                completeTrial();
                detach();
                return true;
            }
        }
        spend(TICK);
        return true;
    }

    private void completeTrial() {
        if (target instanceof Hero) {
            // grant spw1/spw2/spw3 one each, also apply initial buffs if they were 0
            if (Statistics.spw1 == 0) Buff.affect(target, EnhancedWeapon.class);
            if (Statistics.spw2 == 0) Buff.affect(target, EnhancedWand.class);
            if (Statistics.spw3 == 0) Buff.affect(target, EnhancedArmor.class);
            new Flare(6, 32).color(0xFF6600, true).show(hero.sprite, 3f);
            hero.sprite.remove(CharSprite.State.DARKENED);
            Sample.INSTANCE.play(Assets.Sounds.BADGE);
            Statistics.spw1++;
            Statistics.spw2++;
            Statistics.spw3++;
        }
        GLog.p(Messages.get(TrialOfPillars.class, "complete"));
    }

    public int getFloorsLeft() {
        return floorsLeft;
    }

    @Override
    public int icon() {
        return BuffIndicator.CORRUPT;
    }

    @Override
    public String name() {
        return Messages.get(this, "name");
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc", floorsLeft);
    }

    private static final String FLOORS = "floors";
    private static final String LAST_DEPTH = "last_depth";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(FLOORS, floorsLeft);
        bundle.put(LAST_DEPTH, lastDepth);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        floorsLeft = bundle.getInt(FLOORS);
        lastDepth = bundle.getInt(LAST_DEPTH);
    }
}
