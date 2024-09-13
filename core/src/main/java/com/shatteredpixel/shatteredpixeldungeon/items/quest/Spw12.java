package com.shatteredpixel.shatteredpixeldungeon.items.quest;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.huntress.SpiritHawk;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.SpwSoldier;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Willamob;
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Spw12 extends Item {
    public static final String AC_LIGHT	= "LIGHT";

    {
        image = ItemSpriteSheet.SUPPLY_RATION;

        icon = ItemSpriteSheet.Icons.RING_WEALTH;

        stackable = true;
        levelKnown = true;

        defaultAction = AC_LIGHT;
        upgrade(Statistics.spw12);
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
                Messages.get(Spw12.class, "desc1"),
                Messages.get(Spw12.class, "desc2"),
                Messages.get(Spw12.class, "desc3"),
                Messages.get(Spw12.class, "desc4"),
                Messages.get(Spw12.class, "desc5"),
                Messages.get(Spw12.class, "desc6"),
                Messages.get(Spw12.class, "desc7")
        };

        int index = Math.min(Statistics.spw12, descriptions.length - 1);
        return descriptions[index];
    }

    public static void Spw12Ability() {
        int value = 50;

        if (Statistics.spw12 > 0 && Statistics.spw12 < 5) value = value + 5 * Statistics.spw12;
        if (Statistics.spw12 == 5)  value = 75;
        if (Statistics.spw12 == 6)  value = 85;
        if (Statistics.spw12 == 7)  value = 100;

        new Gold().quantity(value).doPickUp( Dungeon.hero );

        if(Statistics.spw12 < 7) Statistics.spw12++;
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
