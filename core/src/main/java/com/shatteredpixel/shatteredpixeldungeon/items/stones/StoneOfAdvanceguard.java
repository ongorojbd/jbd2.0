package com.shatteredpixel.shatteredpixeldungeon.items.stones;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Speed;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Diobrando;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.SpeedWagon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Annasui;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Bdth;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Com;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Emporio;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Emporio2;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Emporio3;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Pian;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Weather;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.items.Ankh;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.GooBlob;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfDivination;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.TargetHealthIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class StoneOfAdvanceguard extends Runestone {
    {
        image = ItemSpriteSheet.KINGB;
    }

    @Override
    protected void activate(int cell) {
        Char mob = Actor.findChar(cell);
        if (mob != null) {
            if (mob.properties().contains(Char.Property.BOSS) || mob.properties().contains(Char.Property.MINIBOSS) || mob instanceof Pian || mob instanceof Emporio || mob instanceof Com || mob instanceof Weather || mob instanceof Annasui || mob instanceof Bdth || mob instanceof SpeedWagon || mob instanceof Emporio2 || mob instanceof Emporio3 || mob instanceof Hero) {
                GLog.h(Messages.get(this, "fail"));
                Dungeon.level.drop(new StoneOfAdvanceguard(), cell).sprite.drop();
            } else {
                mob.destroy();
                mob.sprite.killAndErase();
                TargetHealthIndicator.instance.target(null);
                GameScene.flash(0x660099);
                Sample.INSTANCE.play(Assets.Sounds.PUFF, 1f);
                Dungeon.level.drop(new ScrollOfDivination(), cell).sprite.drop();
                GLog.p(Messages.get(this, "hit"));
            }
        } else {
            Dungeon.level.drop(new StoneOfAdvanceguard(), cell).sprite.drop();
        }
    }

    @Override
    public int value() {
        return 20 * quantity;
    }
}
