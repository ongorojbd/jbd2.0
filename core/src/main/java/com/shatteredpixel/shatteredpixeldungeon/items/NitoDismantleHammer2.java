package com.shatteredpixel.shatteredpixeldungeon.items;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Jojo1;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Jojo2;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Jojo3;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Jojo4;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Jojo5;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Jojo6;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Jojo7;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Jojo8;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Jojo9;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class NitoDismantleHammer2 extends Item {

    public static final String AC_DISMANTLE2	= "DISMANTLE2";

    {
        image = ItemSpriteSheet.DEMON;

        defaultAction = AC_DISMANTLE2;

        stackable = true;
        unique = true;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_DISMANTLE2);
        return actions;
    }

    @Override
    public void execute(final Hero hero, String action) {
        super.execute(hero, action);

        if (action.equals(AC_DISMANTLE2))  {
            GameScene.show(new WndOptions(new ItemSprite(curItem),
                    Messages.titleCase(name()),
                    Messages.get(NitoDismantleHammer.class, "think"),
                    Messages.get(NitoDismantleHammer.class, "yes"),
                    Messages.get(NitoDismantleHammer.class, "no")) {
                @Override
                protected void onSelect(int index) {
                    switch (index) {
                        case 0:
                            if (Dungeon.gold > 999) {
                                Dungeon.gold -= 1000;

                                GLog.n(  "- 1000골드!");
                                Sample.INSTANCE.play(Assets.Sounds.GOLD);
                                CellEmitter.get(curUser.pos).burst(Speck.factory(Speck.COIN), 12);

                                if (Statistics.manga < 3) {
                                    if (Random.Int( 33 ) == 0) {
                                        Statistics.manga += 1;
                                        new Flare(6, 32).color(0xFFAA00, true).show(hero.sprite, 4f);
                                        Sample.INSTANCE.play(Assets.Sounds.MASTERY, 0.7f, 1.2f);
                                        GameScene.flash(0xFFCC00);
                                        switch (Random.Int(9)){

                                            case 0:
                                                Item a = new Jojo1();
                                                Dungeon.level.drop(a, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                                                GLog.h(Messages.get(NitoDismantleHammer.class, "1"));
                                                break;
                                            case 1:
                                                Item b = new Jojo2();
                                                Dungeon.level.drop(b, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                                                GLog.h(Messages.get(NitoDismantleHammer.class, "2"));
                                                break;
                                            case 2:
                                                Item c = new Jojo3();
                                                Dungeon.level.drop(c, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                                                GLog.h(Messages.get(NitoDismantleHammer.class, "3"));
                                                break;
                                            case 3:
                                                Item d = new Jojo4();
                                                Dungeon.level.drop(d, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                                                GLog.h(Messages.get(NitoDismantleHammer.class, "4"));
                                                break;
                                            case 4:
                                                Item e = new Jojo5();
                                                Dungeon.level.drop(e, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                                                GLog.h(Messages.get(NitoDismantleHammer.class, "5"));
                                                break;
                                            case 5:
                                                Item f = new Jojo6();
                                                Dungeon.level.drop(f, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                                                GLog.h(Messages.get(NitoDismantleHammer.class, "6"));
                                                break;
                                            case 6:
                                                Item g = new Jojo7();
                                                Dungeon.level.drop(g, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                                                GLog.h(Messages.get(NitoDismantleHammer.class, "7"));
                                                break;
                                            case 7:
                                                Item h = new Jojo8();
                                                Dungeon.level.drop(h, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                                                GLog.h(Messages.get(NitoDismantleHammer.class, "8"));
                                                break;
                                            case 8:
                                                Item i = new Jojo9();
                                                Dungeon.level.drop(i, Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
                                                GLog.h(Messages.get(NitoDismantleHammer.class, "9"));
                                                break;
                                        }}}



                            } else{
                                GLog.n( Messages.get(NitoDismantleHammer2.class, "rev") );
                            }
                            break;
                        case 1:

                    }
                }
            });
        } }


    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }


}