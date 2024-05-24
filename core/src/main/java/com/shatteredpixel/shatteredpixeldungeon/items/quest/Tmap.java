package com.shatteredpixel.shatteredpixeldungeon.items.quest;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Tendency;
import com.shatteredpixel.shatteredpixeldungeon.effects.SpellSprite;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Mdisc;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SeniorSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;

import java.util.ArrayList;

public class Tmap extends Item {
    public static final String AC_LIGHT = "LIGHT";

    public static final String AC_BOSS = "BOSS";

    {
        image = ItemSpriteSheet.MAP0;

        stackable = true;

        defaultAction = AC_LIGHT;

        unique = true;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_LIGHT);
        actions.add(AC_BOSS);
        return actions;
    }


    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);
        if (action.equals(AC_LIGHT)) {

            GameScene.selectCell(thrower);

        }

        if (action.equals(AC_BOSS)) {
            GameScene.show(
                    new WndOptions(new SeniorSprite(),
                            Messages.get(Mdisc.class, "00"),
                            Messages.get(Mdisc.class, "0"),
                            Messages.get(Mdisc.class, "1"),
                            Messages.get(Mdisc.class, "2"),
                            Messages.get(Mdisc.class, "3"),
                            Messages.get(Mdisc.class, "4"),
                            Messages.get(Mdisc.class, "5"),
                            Messages.get(Mdisc.class, "6"),
                            Messages.get(Mdisc.class, "7")
                    ) {
                        @Override
                        protected void onSelect(int index) {
                            if (index == 0) {
                                Statistics.duwang2 = 1;
                            } else if (index == 1) {
                                Statistics.duwang2 = 2;
                            } else if (index == 2) {
                                for (Char c : Actor.chars()) {
                                    if (c instanceof Tendency) {
                                        ((Tendency) c).a2();
                                    }
                                }
                            } else if (index == 3) {
                                for (Char c : Actor.chars()) {
                                    if (c instanceof Tendency) {
                                        ((Tendency) c).a3();
                                    }
                                }
                            } else if (index == 4) {
                                Sample.INSTANCE.play(Assets.Sounds.TRAP, 1, 1);
                                GLog.p(Messages.get(Mdisc.class, "12"));
                                Music.INSTANCE.play(Assets.Music.HALLS_BOSS, true);
                            } else if (index == 5) {
                                Sample.INSTANCE.play(Assets.Sounds.TRAP, 1, 1);
                                GLog.p(Messages.get(Mdisc.class, "13"));
                                Music.INSTANCE.play(Assets.Music.LABS_BOSS, true);
                            } else {
                                Sample.INSTANCE.play(Assets.Sounds.TRAP, 1, 1);
                                GLog.p(Messages.get(Mdisc.class, "14"));
                                Music.INSTANCE.play(Assets.Music.HEAVENDIO, true);
                            }
                        }

                    }
            );

        }
    }

    protected static CellSelector.Listener thrower = new CellSelector.Listener() {


        @Override
        public void onSelect(Integer target) {
            Tendency ally = getwagon();
            if (target == null) return;

            SpellSprite.show(curUser, SpellSprite.MAP);
            hero.sprite.operate(hero.pos);

            ally.directTocell(target);

        }


        @Override
        public String prompt() {
            return Messages.get(Tendency.class, "prompt");
        }
    };

    private static Tendency getwagon() {
        for (Char ch : Actor.chars()) {
            if (ch instanceof Tendency) {
                return (Tendency) ch;
            }
        }
        return null;
    }


    @Override
    public ItemSprite.Glowing glowing() {
        return new ItemSprite.Glowing(0x9999CC, 1f);
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
        return 50 * quantity;
    }

}
