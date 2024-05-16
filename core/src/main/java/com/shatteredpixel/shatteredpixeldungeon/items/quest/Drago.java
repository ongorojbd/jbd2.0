package com.shatteredpixel.shatteredpixeldungeon.items.quest;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Drago extends Item {
    public static final String AC_LIGHT	= "LIGHT";

    {
        image = ItemSpriteSheet.GRAVE;

        stackable = true;

        defaultAction = AC_LIGHT;

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
            switch (Random.Int(2)){

                case 0:
                    GLog.p(Messages.get(Drago.class, "1"));
                    Camera.main.shake(2, 0.5f);
                    for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
                        if (mob.alignment != Char.Alignment.ALLY && Dungeon.level.heroFOV[mob.pos]) {
                            mob.damage(curUser.lvl * 18, curUser);
                        }
                    }
                    break;
                case 1:
                    GLog.p(Messages.get(Drago.class, "2"));
                    hero.damage(hero.HT/2, this);
                    if (!hero.isAlive()) {
                        Dungeon.fail(getClass());
                    }
                    break;
            }

            GameScene.flash(0x339900);

            curUser.sprite.centerEmitter().start(Speck.factory(Speck.LIGHT), 0.3f, 3);
            Sample.INSTANCE.play(Assets.Sounds.BEACON);

            hero.sprite.operate(hero.pos);
            detach( curUser.belongings.backpack );
            updateQuickslot();
            hero.spendAndNext( 1f );
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
        return 2 * quantity;
    }
}
