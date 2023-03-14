package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Roots;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.LiquidMetal;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfHaste;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfWealth;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.BossdiscH;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class LSWORD extends MeleeWeapon {
    public static final String AC_ZAP = "ZAP";
    {
        image = ItemSpriteSheet.LSWORD;
        hitSound = Assets.Sounds.HIT_SLASH;
        hitSoundPitch = 1f;
        tier = 5;
    }

    @Override
    public int max(int lvl) {
        return  4*(tier) + 2 +  //18durable_projectiles + 4
                lvl*(tier); }


    @Override
    public int proc(Char attacker, Char defender, int damage) {

        if (Random.Int( 7 ) == 0) {
            for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
                if (Dungeon.level.adjacent(mob.pos, hero.pos) && mob.alignment != Char.Alignment.ALLY) {
                    int dmg = hero.damageRoll();
                    dmg *= 1.3f;
                    CellEmitter.get( mob.pos ).burst( Speck.factory( Speck.STAR), 3 );
                    attacker.sprite.showStatus(CharSprite.NEUTRAL, "[오버드라이브!]");
                    mob.damage(dmg, this);
                }
            }
            CellEmitter.get( curUser.pos ).burst( Speck.factory( Speck.STAR ), 10 );
            Sample.INSTANCE.play(Assets.Sounds.OVERDRIVE);
            hero.sprite.zap(hero.pos);
            Invisibility.dispel();

            if (hero.belongings.getItem(RingOfWealth.class) != null) {
                if (hero.belongings.getItem(RingOfWealth.class).isEquipped(hero)) {
                    {
                        Buff.affect(hero, Barrier.class).setShield(7);
                    }
                }
            }
        }

        if (defender.properties().contains(Char.Property.DEMONIC) || defender.properties().contains(Char.Property.UNDEAD)){
            defender.sprite.emitter().start( ShadowParticle.UP, 0.05f, 10 );
            Sample.INSTANCE.play(Assets.Sounds.BURNING);
            damage *= 1.3333f; //deals more damage to the demons and the undeads
        }
        return super.proc( attacker, defender, damage );
    }

    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe{

        {
            inputs =  new Class[]{WornShortsword.class, WarHammer.class};
            inQuantity = new int[]{1, 1};

            cost = 15;

            output = LSWORD.class;
            outQuantity = 1;
        }
    }

    @Override
    public String desc() {
        String info = Messages.get(this, "desc");
        if (hero.belongings.getItem(RingOfWealth.class) != null) {
            if (hero.belongings.getItem(RingOfWealth.class).isEquipped(hero))
                info += "\n\n" + Messages.get( LSWORD.class, "setbouns");}

        return info;
    }

    @Override
    public String targetingPrompt() {
        return Messages.get(this, "prompt");
    }

    @Override
    protected void duelistAbility(Hero hero, Integer target) {
        Sword.cleaveAbility(hero, target, 1.20f, this);
    }

}
