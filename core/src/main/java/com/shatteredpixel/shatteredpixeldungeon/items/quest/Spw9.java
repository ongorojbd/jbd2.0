package com.shatteredpixel.shatteredpixeldungeon.items.quest;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Tendency;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

import java.util.ArrayList;

public class Spw9 extends Item {
    public static final String AC_LIGHT	= "LIGHT";

    {
        image = ItemSpriteSheet.SUPPLY_RATION;

        icon = ItemSpriteSheet.Icons.POTION_EXP;

        stackable = true;
        levelKnown = true;

        defaultAction = AC_LIGHT;
        upgrade(Statistics.spw9);
        unique = true;
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
                Messages.get(Spw9.class, "desc1"),
                Messages.get(Spw9.class, "desc2"),
                Messages.get(Spw9.class, "desc3"),
                Messages.get(Spw9.class, "desc4"),
                Messages.get(Spw9.class, "desc5"),
                Messages.get(Spw9.class, "desc6"),
                Messages.get(Spw9.class, "desc7")
        };

        int index = Math.min(Statistics.spw9, descriptions.length - 1);
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
