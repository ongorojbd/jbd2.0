package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.ArcaneResin;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Diodiary;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Greenbaby;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Jotarodisc;
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

public class Polpol extends MeleeWeapon {
    {
        image = ItemSpriteSheet.SHATTPOT;
        hitSound = Assets.Sounds.HIT;
        hitSoundPitch = 1f;
        identify();
        unique = true;
        tier = 2;
    }

    private int killpoint = 0;

    public void GetKillPoint() {
        killpoint++;
        Polpol pick = Dungeon.hero.belongings.getItem( Polpol.class );
        if (killpoint == 7) {
            Sample.INSTANCE.play(Assets.Sounds.BEE);
            GLog.n( Messages.get(this, "rev") );
            GameScene.flash(0xFFFF00);
            Polpoc n = new Polpoc();
            int lvl = this.level();
            n.enchantment = enchantment;
            n.curseInfusionBonus = curseInfusionBonus;
            n.levelKnown = levelKnown;
            n.cursedKnown = cursedKnown;
            n.cursed = cursed;
            n.augment = augment;
            n.level(lvl);

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




    // public void GetKillPoint() {
    //killpoint++;

    //if (killpoint == 36) {
    // int lvl = this.level();

    // WarHammer n = new WarHammer();

    //n.enchantment = enchantment;
    //n.curseInfusionBonus = curseInfusionBonus;
    //n.levelKnown = levelKnown;
    // n.cursedKnown = cursedKnown;
    //n.cursed = cursed;
    //n.augment = augment;
    // n.level(lvl);

    // Dungeon.hero.belongings.weapon = n;

    //  Dungeon.hero.sprite.emitter().burst(Speck.factory(Speck.RED_LIGHT),12);
    //  }
    //  }





    @Override
    public String status() {
        if (this.isIdentified()) return killpoint + "/" + 7;
        else return null;}


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


}