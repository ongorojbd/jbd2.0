package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.TargetHealthIndicator;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;

public class PanoramaBuff extends FlavourBuff {

    {
        type = buffType.NEGATIVE;
        announced = true;
    }

    public static final float DURATION = 5f;
    private boolean isDie = true;

    @Override
    public int icon() {
        return BuffIndicator.SACRIFICE;
    }

    @Override
    public void tintIcon(Image icon) {
        icon.hardlight(0.2f, 0.0f, 0.2f);
    }

    @Override
    public boolean act() {
        ((Mob) target).state = ((Mob) target).HUNTING;
        ((Mob) target).beckon(Dungeon.hero.pos);
        return super.act();
    }

    public void Complete() {
        isDie = false;
    }

    @Override
    public void detach() {
        super.detach();
        if(isDie) {
            ((Mob) target).EXP = 0;

            ((Mob) target).destroy();
            ((Mob) target).sprite.killAndErase();
            Dungeon.level.mobs.remove(((Mob) target));
            TargetHealthIndicator.instance.target(null);
        } else target.sprite.showStatus( CharSprite.NEGATIVE, "$%$!!");
    }

    private static final String ISDIE = "left";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(ISDIE, isDie);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        isDie = bundle.getBoolean(ISDIE);
    }
}
