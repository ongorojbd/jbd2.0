package com.shatteredpixel.shatteredpixeldungeon.items.quest;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Act3;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Cmoon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Dvdol;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Genkaku;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Kousaku;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mih;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Newcmoon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Newgenkaku;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Pucci;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.WO;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.BossdiscH;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class UV extends Item {
    public static final String AC_LIGHT = "LIGHT";

    {
        image = ItemSpriteSheet.UV;

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

            for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
                if (mob.alignment != Char.Alignment.ALLY && Dungeon.level.heroFOV[mob.pos]) {
                    if (!mob.properties().contains(Char.Property.BOSS) && !mob.properties().contains(Char.Property.MINIBOSS) && mob.properties().contains(Char.Property.UNDEAD)) {
                        mob.die(this);
                    }
                }
            }

            GLog.p(Messages.get(UV.class, "uv"));
            Sample.INSTANCE.play(Assets.Sounds.RAY, 0.7f, 1.2f);
            Sample.INSTANCE.play(Assets.Sounds.BURNING, 0.7f, 1.2f);
            GameScene.flash(0x9966FF);

            hero.sprite.operate(hero.pos);
            detach(curUser.belongings.backpack);
            updateQuickslot();
            hero.spendAndNext(1f);
        }
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
