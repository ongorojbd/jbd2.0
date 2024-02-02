package com.shatteredpixel.shatteredpixeldungeon.items.quest;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hex;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.SoulMark;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Diobrando;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;

import java.util.ArrayList;

public class Lpsword extends Item {
    public static final String AC_LIGHT	= "LIGHT";

    {
        image = ItemSpriteSheet.LSWORD;

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
            if(Statistics.duwang3 != 5) {
                GLog.p(Messages.get(Lpsword.class, "w"));
            } else {
                for (Mob mob : (Iterable<Mob>) Dungeon.level.mobs.clone()) {
                    if (mob instanceof Diobrando) {
                        Buff.affect(mob, Paralysis.class, 5f);
                        Buff.affect(mob, SoulMark.class, 5f);
                        Buff.affect(mob, Hex.class, 5f);
                    }
                }
                GLog.p(Messages.get(Lpsword.class, "d"));
                GLog.n(Messages.get(Lpsword.class, "h"));



                new Flare(6, 32).color(0xFFFFCC, true).show(hero.sprite, 1f);
                Sample.INSTANCE.play(Assets.Sounds.HIT_SLASH, 0.7f, 0.7f);
                Sample.INSTANCE.play(Assets.Sounds.DIO6);
                GameScene.flash(0xFFFFCC);
                Camera.main.shake(3, 0.7f);
                GLog.n(Messages.get(Lpsword.class, "s"));
                hero.sprite.operate(hero.pos);
                detach(curUser.belongings.backpack);
                updateQuickslot();
            }


        }
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return new ItemSprite.Glowing(0x00FFFF, 1f);
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
