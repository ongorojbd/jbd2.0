package com.shatteredpixel.shatteredpixeldungeon.items.quest;

import static com.shatteredpixel.shatteredpixeldungeon.Statistics.spw24;

import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

import java.util.ArrayList;

public class Spw24 extends Item {
    public static final String AC_LIGHT	= "LIGHT";

    {
        image = ItemSpriteSheet.RING_DIAMOND;

        icon = ItemSpriteSheet.Icons.RING_WEALTH;

        stackable = true;
        levelKnown = true;

        defaultAction = AC_LIGHT;
        upgrade(Math.min(Statistics.spw24, 10));
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
        // 기본값 10% + spw24당 15%, 최대 100%
        int chance = Math.min(100, 15 + (spw24 * 15));
        String result = "보상을 받을 때마다, SPW 재단의 보급 상자를 _" + chance + "%_ 확률로 1개 더 획득할 수 있습니다.\n\n이 효과를 선택할 때마다 확률이 +_15%_만큼 더 증가합니다.";
        return result;
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