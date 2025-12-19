package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Haste;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
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
import com.shatteredpixel.shatteredpixeldungeon.ui.AttackIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
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

    @Override
    public boolean doEquip(Hero hero) {
        identify();
        super.doEquip(hero);
        return true;
    }

    @Override
    public int max(int lvl) {
        return  Math.round(4*(tier) * 1.45f) +                	//29 (45% 상향)
                Math.round(lvl*(tier-1) * 1.45f);              //6*lvl (45% 상향)
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

        if (Dungeon.hero != null && VectorSetBouns()) {
            info += "\n\n" + Messages.get(AJA.class, "setbouns"); }
        return info;
    }

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

    @Override
    public String targetingPrompt() {
        return Messages.get(this, "prompt");
    }

    @Override
    protected void duelistAbility(Hero hero, Integer target) {
        if (hero.HP / (float)hero.HT >= 0.5f){
            GLog.w(Messages.get(this, "ability_cant_use"));
            return;
        }

        if (target == null) {
            return;
        }

        Char enemy = Actor.findChar(target);
        if (enemy == null || enemy == hero || hero.isCharmedBy(enemy) || !Dungeon.level.heroFOV[target]) {
            GLog.w(Messages.get(this, "ability_no_target"));
            return;
        }

        hero.belongings.abilityWeapon = this;
        if (!hero.canAttack(enemy)){
            GLog.w(Messages.get(this, "ability_target_range"));
            hero.belongings.abilityWeapon = null;
            return;
        }
        hero.belongings.abilityWeapon = null;

        hero.sprite.attack(enemy.pos, new Callback() {
            @Override
            public void call() {
                beforeAbilityUsed(hero, enemy);
                AttackIndicator.target(enemy);

                //+(12+(2*lvl)) damage, roughly +50% base damage, +55% scaling
                int dmgBoost = augment.damageFactor(12 + 2*buffedLvl());

                if (hero.attack(enemy, 1, dmgBoost, Char.INFINITE_ACCURACY)){
                    Sample.INSTANCE.play(Assets.Sounds.HIT_STRONG);
                }

                Invisibility.dispel();
                if (!enemy.isAlive()){
                    hero.next();
                    onAbilityKill(hero, enemy);
                } else {
                    hero.spendAndNext(hero.attackDelay());
                }
                afterAbilityUsed(hero);
            }
        });
    }

    @Override
    public String abilityInfo() {
        int dmgBoost = levelKnown ? 12 + 2*buffedLvl() : 12;
        if (levelKnown){
            return Messages.get(this, "ability_desc", augment.damageFactor(min()+dmgBoost), augment.damageFactor(max()+dmgBoost));
        } else {
            return Messages.get(this, "typical_ability_desc", min(0)+dmgBoost, max(0)+dmgBoost);
        }
    }
}