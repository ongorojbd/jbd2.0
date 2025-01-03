package com.shatteredpixel.shatteredpixeldungeon.items.bombs;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.GSoldier;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.SpwSoldier;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Tendency;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.BossdiscD;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Fadeleaf;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
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
            if (Dungeon.level.heroFOV[mob.pos] && !(mob instanceof Tendency) && !(mob instanceof GSoldier) && !(mob instanceof SpwSoldier)) {
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

        new Fadeleaf().activate(hero);

        GLog.n(Messages.get(ThirdBomb.class, "return"));

    }

    @Override
    public int value() {
        //prices of ingredients
        return quantity * (20 + 40);
    }

    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe{

        {
            inputs =  new Class[]{BossdiscD.class};
            inQuantity = new int[]{1};

            cost = 0;

            output = ThirdBomb.class;
            outQuantity = 1;
        }
    }

}