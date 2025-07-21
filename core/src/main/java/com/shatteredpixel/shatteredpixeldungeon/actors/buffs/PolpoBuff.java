package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;

public class PolpoBuff extends Buff {
    {
        type = Buff.buffType.POSITIVE;
        announced = true;
    }

    public static final float DURATION = 1_000_000;

    @Override
    public int icon() {
        return BuffIndicator.TRINITY_FORM;
    }

    @Override
    public void tintIcon(Image icon) {
        icon.hardlight(1.2f, 1.2f, 0.2f);
    }

    @Override
    public String toString() {
        return Messages.get(this, "name");
    }
}
