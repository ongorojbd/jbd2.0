package com.shatteredpixel.shatteredpixeldungeon.items.quest;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Kinga;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Kingc;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Kingm;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Kings;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Kingt;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Kingw;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Xray;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfAdvanceguard;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Spw21 extends Item {
    public static final String AC_LIGHT	= "LIGHT";

    {
        image = ItemSpriteSheet.RO1;

        stackable = true;
        levelKnown = true;

        defaultAction = AC_LIGHT;
        //upgrade(Statistics.spw21);
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

    public static void Spw21Ability() {

        List<Class<? extends Item>> itemClasses = Arrays.asList(
                Kingt.class,
                StoneOfAdvanceguard.class,
                Xray.class,
                Kings.class,
                Kingm.class,
                Kingw.class,
                Kingc.class,
                Kinga.class
        );

        ArrayList<Integer> indices = new ArrayList<Integer>();
        for (int i = 0; i < itemClasses.size(); i++) {
            indices.add(i);
        }
        Collections.shuffle(indices);

        for (int i = 0; i < 2; i++) {
            try {
                Item item = itemClasses.get(indices.get(i)).newInstance();
                if (item.doPickUp(Dungeon.hero)) {
                    GLog.p(Messages.capitalize(Messages.get(Dungeon.hero, "you_now_have", item.name())));
                } else {
                    Dungeon.level.drop(item, Dungeon.hero.pos).sprite.drop();
                }
            } catch (Exception e) {
                e.printStackTrace(); // or GLog.w("아이템 생성 실패");
            }
        }


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
