package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.ArcaneResin;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Diodiary;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Greenbaby;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Jotarodisc;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfAugmentation;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfEnchantment;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

public class FlameKatana extends MeleeWeapon {
    {
        image = ItemSpriteSheet.BEACON;
        hitSound = Assets.Sounds.HIT_CRUSH;
        hitSoundPitch = 1f;

        unique = true;
        tier = 2;
    }

    private int killpoint = 0;

    @Override
    public int max(int lvl) {
        return  4*(tier+1) +    //12 base, down from 15
                lvl*(tier+1);   //scaling unchanged
    }


    public void GetKillPoint() {
        killpoint++;
        FlameKatana pick = Dungeon.hero.belongings.getItem(  FlameKatana.class );
        if (killpoint == 36) {
            Sample.INSTANCE.play(Assets.Sounds.HAHAH);
            GLog.n( Messages.get(this, "rev") );
            GameScene.flash(0xFFFF00);
            Greenbaby n = new Greenbaby();

            n.cursedKnown = cursedKnown;
            n.cursed = cursed;

            pick.doUnequip( Dungeon.hero, false );
            pick.detach( Dungeon.hero.belongings.backpack );

            if (n.doPickUp( Dungeon.hero )) {
                GLog.h( Messages.capitalize(Messages.get(Dungeon.hero, "you_now_have", n.name()) ));
            } else {
                Dungeon.level.drop(n, Dungeon.hero.pos ).sprite.drop();
            }

            Dungeon.hero.sprite.emitter().burst(Speck.factory(Speck.RED_LIGHT),12);
        }
    }

    @Override
    public String status() { return killpoint + "/" + 36;}


    private static final String KILL = "killpoint";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(KILL, killpoint);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        killpoint = bundle.getInt(KILL);
    }

    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe{

        {
            inputs =  new Class[]{Jotarodisc.class, StoneOfAugmentation.class};
            inQuantity = new int[]{1, 1};

            cost = 30;

            output = FlameKatana.class;
            outQuantity = 1;
        }
    }

    @Override
    public float abilityChargeUse(Hero hero, Char target) {
        return 2*super.abilityChargeUse(hero, target);
    }

    @Override
    protected void duelistAbility(Hero hero, Integer target) {
        Dagger.sneakAbility(hero, 5, this);
    }
}