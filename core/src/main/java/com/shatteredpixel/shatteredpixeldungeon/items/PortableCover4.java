package com.shatteredpixel.shatteredpixeldungeon.items;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ShovelDigCoolDown;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ShovelDigCoolDown4;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.PurpleParticle;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class PortableCover4 extends Item {
    private static String AC_USE = "USE";
    {
        image = ItemSpriteSheet.SPOH;
        stackable = true;
        defaultAction = AC_USE;
        unique = true;
    }

    @Override
    public ArrayList<String> actions(Hero hero ) {
        ArrayList<String> actions = super.actions( hero );
        actions.add( AC_USE );
        return actions;
    }

    @Override
    public void execute( final Hero hero, String action ) {

        super.execute( hero, action );

        if (action.equals( AC_USE )) {
            if (hero.buff(ShovelDigCoolDown4.class) != null){
                GLog.w(Messages.get(this, "not_ready"));
            }
            else {
                Buff.append(curUser, PortableCover4.CoverBuff.class, 3f);
                Dungeon.hero.sprite.emitter().burst(Speck.factory(Speck.MASK),20);
                new Flare( 5, 32 ).color( 0x9999FF, true ).show( hero.sprite, 1f );
                hero.sprite.operate(hero.pos);

                Sample.INSTANCE.play( Assets.Sounds.ORA);
                Buff.affect(hero, ShovelDigCoolDown4.class, 20f);
            }}
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
        return 30 * quantity;
    }

    public static class CoverBuff extends FlavourBuff {
    }


}