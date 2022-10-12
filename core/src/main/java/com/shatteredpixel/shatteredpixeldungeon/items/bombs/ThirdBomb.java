package com.shatteredpixel.shatteredpixeldungeon.items.bombs;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Rebel;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;

public class ThirdBomb extends Bomb {

    {
        image = ItemSpriteSheet.KIT;

        unique = true;
    }

    @Override
    public void explode(int cell) {
        super.explode(cell);

        GameScene.flash( 0x80FFFFFF );

        Sample.INSTANCE.play( Assets.Sounds.TBOMB );

        int targets = 0;
        for (Mob mob : Dungeon.level.mobs.toArray( new Mob[0] )) {
            if (Dungeon.level.heroFOV[mob.pos]) {
                targets ++;
                mob.damage(Math.round(mob.HT/2f + mob.HP/2f), this);

            }
        }
        PotionOfHealing.cure(hero);
        PotionOfHealing.heal(hero);
        identify();


        if (!Dungeon.interfloorTeleportAllowed()) {

            GLog.w( Messages.get(ScrollOfTeleportation.class, "no_tele") );
            return;

        }
        InterlevelScene.mode = InterlevelScene.Mode.RETURN;
        InterlevelScene.returnDepth = Math.max(1, (Dungeon.depth - 1 - (Dungeon.depth-2)%5));
        InterlevelScene.returnBranch = 0;
        InterlevelScene.returnPos = -1;
        Game.switchScene( InterlevelScene.class );

        GLog.n(Messages.get(ThirdBomb.class, "return"));

    }

    @Override
    public int value() {
        //prices of ingredients
        return quantity * (20 + 40);
    }
}