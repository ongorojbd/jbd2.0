package com.shatteredpixel.shatteredpixeldungeon.items.quest;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.SpellSprite;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;

import java.util.ArrayList;

public class Bmap extends Item {
    public static final String AC_LIGHT = "LIGHT";

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
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);
        if (action.equals(AC_LIGHT)) {
            if (Dungeon.depth == 17) {
                if (Statistics.zombiecount == 0) {
                    GameScene.show(
                            new WndOptions(new ItemSprite(this),
                                    Messages.titleCase(name()),
                                    Messages.get(this, "prick_warn"),
                                    Messages.get(this, "yes"),
                                    Messages.get(this, "no")) {
                                @Override
                                protected void onSelect(int index) {
                                    if (index == 0) {
                                        InterlevelScene.mode = InterlevelScene.Mode.RETURN;
                                        InterlevelScene.returnDepth = Math.max(0, (Dungeon.depth));
                                        InterlevelScene.returnBranch = 1;
                                        InterlevelScene.returnPos = -2;
                                        Game.switchScene(InterlevelScene.class);

                                        detach(curUser.belongings.backpack);
                                        updateQuickslot();
                                    } else {

                                    }
                                }
                            }
                    );
                } else {
                    GLog.h(Messages.get(Bmap.class, "6"));
                    SpellSprite.show(curUser, SpellSprite.MAP);
                    Sample.INSTANCE.play(Assets.Sounds.READ);
                }
            } else {
                GLog.h(Messages.get(Bmap.class, "4"));
                SpellSprite.show(curUser, SpellSprite.MAP);
                Sample.INSTANCE.play(Assets.Sounds.READ);
            }
        }
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return new ItemSprite.Glowing(0xFFCC33, 1f);
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

}
