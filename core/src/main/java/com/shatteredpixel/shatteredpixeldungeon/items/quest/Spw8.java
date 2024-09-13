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

    @Override
    public String desc() {
        String[] descriptions = {
                Messages.get(this, "desc"),
                Messages.get(Spw8.class, "desc1"),
                Messages.get(Spw8.class, "desc2"),
                Messages.get(Spw8.class, "desc3"),
                Messages.get(Spw8.class, "desc4"),
                Messages.get(Spw8.class, "desc5"),
                Messages.get(Spw8.class, "desc6"),
                Messages.get(Spw8.class, "desc7")
        };

        int index = Math.min(Statistics.spw8, descriptions.length - 1);
        return descriptions[index];
    }

    public static void Spw8Ability() {

        double recoveryFactor = 0.25;

        recoveryFactor += Statistics.spw8 * 0.05;

        hero.HP = (int) Math.min(hero.HP + hero.HT * recoveryFactor, hero.HT);

        hero.sprite.emitter().burst(Speck.factory(Speck.HEALING), 4);
        for (Char c : Actor.chars()) {
            if (c instanceof Tendency) {
                ((Tendency) c).heal(recoveryFactor);
            }
        }
        GLog.p(Messages.get(Spw.class, "heal"));
        Sample.INSTANCE.play(Assets.Sounds.FF);
        if(Statistics.spw8 < 7) Statistics.spw8++;

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
