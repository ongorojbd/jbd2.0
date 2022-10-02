package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
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
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfWealth;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
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

        defaultAction = AC_ZAP;
    }

    @Override
    public int max(int lvl) {
        return  4*(tier) + 2 +  //18durable_projectiles + 4
                lvl*(tier); }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_ZAP);
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);
        if (action.equals(AC_ZAP)) {
            if (!isEquipped(hero)) return;

            if (!cursed) {
                if (charge >= 17) {
                    for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
                        if (Dungeon.level.adjacent(mob.pos, hero.pos) && mob.alignment != Char.Alignment.ALLY) {
                            int dmg = hero.damageRoll();
                            dmg *= 1.3f;
                            CellEmitter.get( mob.pos ).burst( Speck.factory( Speck.STAR), 3 );
                            mob.damage(dmg, this);
                        }
                    }
                    CellEmitter.get( curUser.pos ).burst( Speck.factory( Speck.STAR ), 10 );
                    charge -= 17;
                    Sample.INSTANCE.play(Assets.Sounds.OVERDRIVE);
                    hero.sprite.zap(hero.pos);
                    Invisibility.dispel();
                    updateQuickslot();
                    hero.spendAndNext(1f);
                }
            }
            else {
                Buff.affect(hero, Roots.class, 5f);
                cursedKnown = true;
                charge -= 17;
                Invisibility.dispel();
                updateQuickslot();
                hero.spendAndNext(1f);
            }
        }
    }

    @Override
    public String status() {
        if (chargeCap == 100)
            return Messages.format("%d%%", charge);
        return null;
    }

    @Override
    public int proc(Char attacker, Char defender, int damage) {
        float procChance = (buffedLvl()+1f)/(buffedLvl()+3f);
        if (Random.Float() < procChance) {
            Buff.affect(defender, Blindness.class, 2+buffedLvl());
        }
        if (defender.properties().contains(Char.Property.DEMONIC) || defender.properties().contains(Char.Property.UNDEAD)){
            defender.sprite.emitter().start( ShadowParticle.UP, 0.05f, 10+buffedLvl() );
            Sample.INSTANCE.play(Assets.Sounds.BURNING);

            damage *= 1.3333f; //deals more damage to the demons and the undeads
        }
        return super.proc( attacker, defender, damage );
    }

    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe{

        {
            inputs =  new Class[]{WornShortsword.class};
            inQuantity = new int[]{1};

            cost = 150;

            output = LSWORD.class;
            outQuantity = 1;
        }
    }

    @Override
    public String desc() {
        String info = Messages.get(this, "desc");
        if (Dungeon.hero.belongings.getItem(RingOfWealth.class) != null) {
            if (Dungeon.hero.belongings.getItem(RingOfWealth.class).isEquipped(Dungeon.hero))
                info += "\n\n" + Messages.get( LSWORD.class, "setbouns");}

        return info;
    }

}
