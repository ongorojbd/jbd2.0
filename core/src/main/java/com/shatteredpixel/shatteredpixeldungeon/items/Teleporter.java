package com.shatteredpixel.shatteredpixeldungeon.items;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

import java.util.ArrayList;
//This is from Elemental PD

public class Teleporter extends Item {

    String AC_TELEPORT = "teleport";
    String AC_RETURN = "return";

    {
        defaultAction = AC_TELEPORT;
        image = ItemSpriteSheet.ANKH;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_TELEPORT);
        actions.add(AC_RETURN);
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {

        hero.damage(hero.HP, this);
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

}


