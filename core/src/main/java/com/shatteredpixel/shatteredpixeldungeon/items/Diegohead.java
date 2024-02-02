package com.shatteredpixel.shatteredpixeldungeon.items;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Diego21;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;

import java.util.ArrayList;

public class Diegohead extends Item {
    private static String AC_USE = "USE";
    {
        image = ItemSpriteSheet.WATERSKIN;
        stackable = true;
        defaultAction = AC_USE;
    }

    @Override
    public ArrayList<String> actions(Hero hero ) {
        ArrayList<String> actions = super.actions( hero );
        actions.add( AC_USE );
        return actions;
    }

    @Override
    public void execute( final Hero hero, String action ) {

        super.execute( hero, action );

        if (action.equals( AC_USE )) {

            if (Dungeon.depth == 25) {
            for (Mob mob : (Iterable<Mob>)Dungeon.level.mobs.clone()) {
                if (mob instanceof Diego21) {
                    mob.sprite.die();
                    mob.die(this);
                }
            }
            detach(Dungeon.hero.belongings.backpack);
            }
            else
            {
                Sample.INSTANCE.play(Assets.Sounds.ITEM);
            }
        }

    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    @Override
    public int value() {
        return 30 * quantity;
    }

}
