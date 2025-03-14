package com.shatteredpixel.shatteredpixeldungeon.items.spells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Acidic;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Bandit;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Bcom;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Bcomg;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Boytwo;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Genkaku;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Jonny;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mandom;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mih;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Newcmoon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Newgenkaku;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.RipperDemon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Scorpio;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Senior;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Stower;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Tengu;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Vitaminc;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Whsnake;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Whsnake2;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special.SentryRoom;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DoppioDialogSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DvdolSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.TankSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.TenguSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialogueWithPic;
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

        WndDialogueWithPic.dialogue(
                new CharSprite[]{new TankSprite(), new DvdolSprite()},
                new String[]{"죠죠찬", "아톨"},
                new String[]{
                        Messages.get(Maga.class, "n1"),
                        Messages.get(Maga.class, "n2")
                },
                new byte[]{
                        WndDialogueWithPic.IDLE,
                        WndDialogueWithPic.IDLE
                }
        );

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
