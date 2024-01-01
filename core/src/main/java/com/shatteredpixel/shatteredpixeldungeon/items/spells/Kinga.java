package com.shatteredpixel.shatteredpixeldungeon.items.spells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicalSleep;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Fadeleaf;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.watabou.noosa.audio.Sample;

public class Kinga extends Spell {

    {
        image = ItemSpriteSheet.KINGA;
    }


    @Override
    protected void onCast(Hero hero) {

        if (Dungeon.bossLevel()){
            GLog.w( Messages.get(ScrollOfTeleportation.class, "no_tele") );
            return;
        }

        for (Mob mob : Dungeon.level.mobs.toArray( new Mob[0] )) {
            if (mob.alignment != Char.Alignment.ALLY && Dungeon.level.heroFOV[mob.pos]) {
                new Fadeleaf().activate(mob);
                Buff.affect(mob, Paralysis.class, 30f);
            }
        }

        identify();
        GLog.p( Messages.get(this, "w") );

        curUser.sprite.centerEmitter().start( Speck.factory( Speck.LIGHT ), 0.3f, 3 );
        Sample.INSTANCE.play(Assets.Sounds.RAY);
        hero.sprite.operate(hero.pos);
        detach( curUser.belongings.backpack );
        updateQuickslot();
        hero.spendAndNext( 1f );
    }


    @Override
    public int value() {
        return 10 * quantity;
    }
}
