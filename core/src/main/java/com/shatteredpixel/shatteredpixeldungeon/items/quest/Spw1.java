package com.shatteredpixel.shatteredpixeldungeon.items.quest;

import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.SpwItem;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

import java.util.ArrayList;

public class Spw1 extends Item {
    public static final String AC_LIGHT	= "LIGHT";

    {
        image = ItemSpriteSheet.SUPPLY_RATION;

        stackable = true;
        levelKnown = true;

        defaultAction = AC_LIGHT;

        unique = true;
        upgrade(Statistics.spw1);
        icon = ItemSpriteSheet.Icons.RING_FORCE;

    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add( AC_LIGHT );
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute( hero, action );
        if (action.equals( AC_LIGHT )) {

        }
    }

    @Override
    public String desc() {
        String[] descriptions = {
                Messages.get(this, "desc"),
                Messages.get(Spw1.class, "desc1"),
                Messages.get(Spw1.class, "desc2"),
                Messages.get(Spw1.class, "desc3"),
                Messages.get(Spw1.class, "desc4"),
                Messages.get(Spw1.class, "desc5"),
                Messages.get(Spw1.class, "desc6"),
                Messages.get(Spw1.class, "desc7")
        };

        int index = Math.min(Statistics.spw1, descriptions.length - 1);
        return descriptions[index];
    }

    @Override
    public boolean isUpgradable() {
        return true;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

}
