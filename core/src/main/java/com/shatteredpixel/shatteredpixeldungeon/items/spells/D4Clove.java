package com.shatteredpixel.shatteredpixeldungeon.items.spells;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.D4C;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class D4Clove extends Spell {
    {
        image = ItemSpriteSheet.RO3;
    }


    @Override
    protected void onCast(Hero hero) {

        Buff.affect(hero, D4C.class);
        GameScene.flash(0x00FFFF);
        hero.sprite.operate(hero.pos);
        detach( curUser.belongings.backpack );
        updateQuickslot();
        hero.spendAndNext( 1f );
    }

    @Override
    public int value() {
        return 15 * quantity;}
}