package com.shatteredpixel.shatteredpixeldungeon.items.spells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Roc;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.AlchemicalCatalyst;

import com.shatteredpixel.shatteredpixeldungeon.items.quest.GooBlob;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfAugmentation;

import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;

public class Neoro extends Spell {
    {
        image = ItemSpriteSheet.RO3;
    }


    @Override
    protected void onCast(Hero hero) {

        hero.HP = 30;
        hero.HT = 30;

        Buff.affect(hero, Roc.class);
        GameScene.flash(0x00FFFF);
        hero.sprite.operate(hero.pos);
        Camera.main.shake(31, 5f);
        GLog.p(Messages.get(this, "c"));
        Music.INSTANCE.play(Assets.Music.CAVES_1, true);
        Sample.INSTANCE.play(Assets.Sounds.MASTERY, 0.7f, 0.9f);
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