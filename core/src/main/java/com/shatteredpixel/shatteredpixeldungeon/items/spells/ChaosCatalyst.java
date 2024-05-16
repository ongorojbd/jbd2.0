package com.shatteredpixel.shatteredpixeldungeon.items.spells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;

public class ChaosCatalyst extends InventorySpell{
    private static ItemSprite.Glowing COL = new ItemSprite.Glowing( 0xE6E6FA );
    {
        image = ItemSpriteSheet.ELIXIR_MIGHT;
        icon = ItemSpriteSheet.Icons.SCROLL_RETRIB;

        unique = true;
        bones = false;
    }

    public ItemSprite.Glowing glowing() {
        return COL;
    }

    @Override
    protected boolean usableOnItem(Item item) {
        return item.isUpgradable();
    }

    @Override
    protected void onItemSelected(Item item) {

        item.level(1);
        Sample.INSTANCE.play(Assets.Sounds.CHARGEUP);
        Dungeon.hero.spendAndNext(1f);
        updateQuickslot();
    }

    @Override
    public int value() {
        //prices of ingredients, divided by output quantity
        return quantity * 25;
    }

    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe{

        {
            inputs =  new Class[]{BossdiscA.class};
            inQuantity = new int[]{1};

            cost = 0;

            output = ChaosCatalyst.class;
            outQuantity = 1;
        }
    }

}
