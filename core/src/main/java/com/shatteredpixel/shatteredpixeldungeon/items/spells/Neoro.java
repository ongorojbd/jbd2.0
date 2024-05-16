package com.shatteredpixel.shatteredpixeldungeon.items.spells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Roc;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfAugmentation;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class Neoro extends Spell {
    {
        image = ItemSpriteSheet.RO3;
    }


    @Override
    protected void onCast(Hero hero) {
        Buff.affect(hero, Roc.class, 50f);
        Sample.INSTANCE.play(Assets.Sounds.DRINK);
        Sample.INSTANCE.play(Assets.Sounds.CHARMS);
        new Flare(6, 32).color(0x00CCFF, true).show(hero.sprite, 3f);
        hero.sprite.operate(hero.pos);
        GLog.p(Messages.get(this, "c"));
        detach( curUser.belongings.backpack );
        updateQuickslot();
        hero.spendAndNext( 1f );
    }

    @Override
    public int value() {
        return 15 * quantity;}



    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe{

        {
            inputs =  new Class[]{Ogroc.class, StoneOfAugmentation.class};
            inQuantity = new int[]{1, 1};

            cost = 0;

            output = Neoro.class;
            outQuantity = 1;
        }
    }


}