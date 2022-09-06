package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hex;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Roots;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.SoulMark;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vulnerable;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Weakness;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfForce;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfHaste;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfCorruption;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.Kunai;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class AJA extends MeleeWeapon {
    public static final String AC_SP = "SP";

    {
        image = ItemSpriteSheet.AJA;
        hitSound = Assets.Sounds.HIT;
        hitSoundPitch = 1f;

        defaultAction = AC_SP;

        tier = 5;
    }

    @Override
    public int max(int lvl) {
        return  2*(tier) +    //10
                lvl*(tier-3);   // +2
    }

    @Override
    public int proc(Char attacker, Char defender, int damage) {

        for (int count = 0; count<3; count++) {
            if (defender.isAlive()) {
                defender.damage(attacker.damageRoll() - defender.drRoll(), attacker);
                defender.sprite.burst(CharSprite.NEGATIVE, 10);
            }
        }

        if (attacker.buff(JourneyBuff_ice.class) != null) {
            Buff.detach(attacker, JourneyBuff_ice.class);
            Buff.affect(defender, Cripple.class, 2f);
        }
        else if (attacker.buff(JourneyBuff_fire.class) != null) {
            Buff.detach(attacker, JourneyBuff_fire.class);
            Buff.affect(defender, SoulMark.class, 5f);
        }
        else if (attacker.buff(JourneyBuff_heavy.class) != null) {
            Buff.detach(attacker, JourneyBuff_heavy.class);
            Buff.affect(defender, Hex.class, 15f);
        }
        else if (attacker.buff(JourneyBuff_1.class) != null) {
            Buff.detach(attacker, JourneyBuff_1.class);
            Buff.affect(defender, Vulnerable.class, 15f);
        }
        else if (attacker.buff(JourneyBuff_2.class) != null) {
            Buff.detach(attacker, JourneyBuff_2.class);
            Buff.affect(defender, Weakness.class, 15f);
        }

        SPCharge(100);
        updateQuickslot();
        return super.proc(attacker, defender, damage);
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_SP);
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {

        super.execute(hero, action);

        if (action.equals(AC_SP) && this.isEquipped(hero)) {
            GameScene.show(
                    new WndOptions(Messages.get(this, "name"),
                            Messages.get(this, "wnddesc"),
                            Messages.get(this, "special1"),
                            Messages.get(this, "special2"),
                            Messages.get(this, "special3"),
                            Messages.get(this, "special4"),
                            Messages.get(this, "special5")) {

                        @Override
                        protected void onSelect(int index) {
                            if (index == 0) {
                                Buff.affect(hero, JourneyBuff_ice.class);
                                charge = 0;
                                updateQuickslot();
                            }
                            else if (index == 1) {
                                Buff.affect(hero, JourneyBuff_fire.class);
                                charge = 0;
                                updateQuickslot();
                            }
                            else if (index == 2) {
                                Buff.affect(hero, JourneyBuff_heavy.class);
                                charge = 0;
                                updateQuickslot();
                            }
                            else if (index == 3) {
                                Buff.affect(hero, JourneyBuff_1.class);
                                charge = 0;
                                updateQuickslot();
                            }
                            else if (index == 4) {
                                Buff.affect(hero, JourneyBuff_2.class);
                                charge = 0;
                                updateQuickslot();
                            }
                        }
                    });
        }
    }

    @Override
    public String status() {

        //if the artifact isn't IDed, or is cursed, don't display anything
        if (!isIdentified() || cursed) {
            return null;
        }
        //display as percent
        if (chargeCap == 100)
            return Messages.format("%d%%", charge);


        //otherwise, if there's no charge, return null.
        return null;
    }

    public static class JourneyBuff_ice extends Buff{}
    public static class JourneyBuff_fire extends Buff{}
    public static class JourneyBuff_heavy extends Buff{}
    public static class JourneyBuff_1 extends Buff{}
    public static class JourneyBuff_2 extends Buff{}


    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe{

        {
            inputs =  new Class[]{Greataxe.class, Flail.class, Kunai.class};
            inQuantity = new int[]{1, 1, 1};

            cost = 130;

            output = AJA.class;
            outQuantity = 1;
        }
    }

    @Override
    public String desc() {
        String info = Messages.get(this, "desc");
        if (Dungeon.hero.belongings.getItem(RingOfForce.class) != null) {
            if (Dungeon.hero.belongings.getItem(RingOfForce.class).isEquipped(Dungeon.hero))
                info += "\n\n" + Messages.get( AJA.class, "setbouns");}

        return info;
    }




}