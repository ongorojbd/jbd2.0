package com.shatteredpixel.shatteredpixeldungeon.items.spells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicalSleep;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Acidic;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Bandit;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Bcom;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Bcomg;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Boytwo;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Cmoon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Crab;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.DemonSpawner;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Dvdol;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Genkaku;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Gnoll;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Jonny;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Kousaku;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mandom;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mih;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Newcmoon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Newgenkaku;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Pucci;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Rat;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.RipperDemon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Scorpio;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Senior;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Stower;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Vitaminc;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.WO;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Whsnake;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Whsnake2;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special.SentryRoom;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Fadeleaf;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.watabou.noosa.audio.Sample;

public class Maga extends Spell {

    {
        image = ItemSpriteSheet.ALCH_PAGE;
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return new ItemSprite.Glowing(0xCC0000, 1f);
    }

    @Override
    protected void onCast(Hero hero) {

        for (Mob mob : (Iterable<Mob>)Dungeon.level.mobs.clone()) {
            if (mob instanceof Bandit || mob instanceof Mandom || mob instanceof Senior || mob instanceof RipperDemon || mob instanceof Vitaminc || mob instanceof Scorpio || mob instanceof Acidic || mob instanceof Newgenkaku || mob instanceof Newcmoon || mob instanceof Mih || mob instanceof Jonny ||  mob instanceof Genkaku  ||  mob instanceof Whsnake ||  mob instanceof Whsnake2 ||  mob instanceof SentryRoom.Sentry ||  mob instanceof Bcom ||  mob instanceof Bcomg ||  mob instanceof Boytwo ||  mob instanceof Stower) {
                mob.destroy();
                mob.sprite.killAndErase();
                Dungeon.level.mobs.remove(mob);
            }
        }

        identify();
        GLog.p( Messages.get(this, "w") );

        curUser.sprite.centerEmitter().start( Speck.factory( Speck.LIGHT ), 0.3f, 3 );
        Sample.INSTANCE.play(Assets.Sounds.PUFF);
        hero.sprite.operate(hero.pos);
        detach( curUser.belongings.backpack );
        updateQuickslot();
        hero.spendAndNext( 1f );
    }


    @Override
    public int value() {
        return 35 * quantity;
    }
}
