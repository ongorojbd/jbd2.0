package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Haste;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.SoulMark;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BloodParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.ChaliceOfBlood;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.HornOfPlenty;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.MasterThievesArmband;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.UnstableSpellbook;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfAccuracy;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfEvasion;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfForce;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.Kunai;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.GameMath;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class AJA extends MeleeWeapon {

    private int HealCount = 0;
    private int mode = 0;

    {
        image = ItemSpriteSheet.AJA;

        tier = 5;
        ACC = 1.4f;
        DLY = 1f;
    }


    // 0, 1, 2

    @Override
    public int max(int lvl) {
        return  4*(tier) +                	//20 + 4
                lvl*(tier-1);
    }

    private void Heal(Char attacker) {
        if (HealCount >= 10) {
            mode++;
            Sample.INSTANCE.play(Assets.Sounds.CHARGEUP, 1f);
                if (mode > 2) mode = 0;

                switch (mode) {
                    default:
                    case 0:
                        ACC = 1f;
                        DLY = 0.75f;
                        hitSound = Assets.Sounds.HIT;
                        hitSoundPitch = 3.3f;
                        break;
                    case 1:
                        ACC = 1.5f;
                        DLY = 1f;
                        hitSound = Assets.Sounds.HIT_STRONG;
                        hitSoundPitch = 3.3f;
                        break;
                    case 2:
                        ACC = 0.5f;
                        DLY = 0.5f;
                        hitSound = Assets.Sounds.SP;
                        hitSoundPitch = 3.3f;
                        break;
                }

            attacker.sprite.showStatus(CharSprite.POSITIVE, "[신체 변형!]");
            HealCount = 0;
        }
    }

    @Override
    public int proc(Char attacker, Char defender, int damage) {
        HealCount++;
        Heal(attacker);

        if (mode == 0) {
        int extratarget = 0;
        if (attacker instanceof Hero) {
            for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
                if (Dungeon.level.adjacent(mob.pos, defender.pos) && mob.alignment != Char.Alignment.ALLY) {
                    int dmg = Dungeon.hero.damageRoll() - Math.max(defender.drRoll(), defender.drRoll());
                    mob.damage(dmg, this);
                    extratarget++;

                }
            }
        }

        float bounsdmg = Math.min(1.5f, 1f+(extratarget*0.1f));

        damage = Math.round(damage * bounsdmg);

        }

        if (mode == 1) {
            if (defender.buff(Poison.class) == null)
                Buff.affect(defender, Poison.class).set(3f);
            else Buff.affect(defender, Poison.class).extend(3f);
        }


        if (mode == 2) {
            defender.damage(attacker.damageRoll(), attacker);
        }



            if (Dungeon.hero.belongings.getItem(RingOfAccuracy.class) != null) {
                if (Dungeon.hero.belongings.getItem(RingOfAccuracy.class).isEquipped(Dungeon.hero)) {
                    if (Random.Int(12 + buffedLvl()) > 10) {
                        if (defender.buff(SoulMark.class) == null)
                            Buff.affect(defender, SoulMark.class, 1f);
                    }
                }
            }


        return super.proc(attacker, defender, damage);
    }

    @Override
    public String desc() {
        String info;
        if (mode == 2) info = Messages.get(this, "desc_mode2");
        else if (mode == 1) info = Messages.get(this, "desc_mode1");
        else info = Messages.get(this, "desc");

        if (VectorSetBouns()) {
            info += "\n\n" + Messages.get(AJA.class, "setbouns"); }
        return info;
    }

    @Override
    public String status() {
        return HealCount + "/" + 10;}

    public static boolean VectorSetBouns() {
        if (!(Dungeon.hero.belongings.weapon instanceof AJA)) return false;

        if (Dungeon.hero.belongings.getItem(RingOfAccuracy.class) != null) {
            if (Dungeon.hero.belongings.getItem(RingOfAccuracy.class).isEquipped(Dungeon.hero))
                return true;
        }
        return false;
    }

    private static final String SWICH = "mode";
    private static final String DLYSAVE = "DLY";
    private static final String ACCSAVE = "ACC";
    private static final String HEALPOINT = "HealCount";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(SWICH, mode);
        bundle.put(ACCSAVE, ACC);
        bundle.put(DLYSAVE, DLY);
        bundle.put(HEALPOINT, HealCount);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        mode = bundle.getInt(SWICH);
        ACC = bundle.getFloat(ACCSAVE);
        DLY = bundle.getFloat(DLYSAVE);
        HealCount = bundle.getInt(HEALPOINT);
    }



    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe{

        {
            inputs =  new Class[]{Greataxe.class, Flail.class, Kunai.class};
            inQuantity = new int[]{1, 1, 1};

            cost = 15;

            output = AJA.class;
            outQuantity = 1;
        }
    }
}