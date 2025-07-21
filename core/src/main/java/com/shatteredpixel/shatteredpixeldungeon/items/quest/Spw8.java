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

public class Spw8 extends Item {
    public static final String AC_LIGHT	= "LIGHT";

    {
        image = ItemSpriteSheet.SUPPLY_RATION;

        icon = ItemSpriteSheet.Icons.POTION_HEALING;

        stackable = true;
        levelKnown = true;

        defaultAction = AC_LIGHT;
        upgrade(Statistics.spw8);
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

    public static void Spw8Ability() {

        hero.HP = hero.HT;

        hero.sprite.emitter().burst(Speck.factory(Speck.HEALING), 4);

        for (Char c : Actor.chars()) {
            if (c.alignment == Char.Alignment.ALLY) {
                c.HP = c.HT;
            }
        }

        GLog.p(Messages.get(Spw.class, "heal"));
        Sample.INSTANCE.play(Assets.Sounds.FF);

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
