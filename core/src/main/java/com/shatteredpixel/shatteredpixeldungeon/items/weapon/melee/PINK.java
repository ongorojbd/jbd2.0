package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Corruption;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.ChaliceOfBlood;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.shatteredpixeldungeon.items.food.MeatPie;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.AlchemicalCatalyst;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfStrength;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfMight;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfHaste;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfCorruption;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.BArray;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class PINK extends MeleeWeapon {
    public static final String AC_ZAP = "ZAP";
    {
        image = ItemSpriteSheet.PINK;
        hitSound = Assets.Sounds.HIT_CRUSH;
        hitSoundPitch = 1.03f;

        tier = 5;
        defaultAction = AC_ZAP;
        ACC = 1.20f; //20% boost to accuracy
    }

    @Override
    public int max(int lvl) {
        return  4*(tier+1)+
                lvl*(tier);
    }

    @Override
    public int proc(Char attacker, Char defender, int damage) {
        SPCharge(2);
        return super.proc(attacker, defender, damage);
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_ZAP);
        return actions;
    }


    @Override
    public void execute(Hero hero, String action) {

        super.execute(hero, action);

        if (action.equals(AC_ZAP) && isEquipped(hero)) {
            if (charge >= chargeCap) {
                MindBrack(hero);
                Sample.INSTANCE.play(Assets.Sounds.CHARMS);
                charge = 0;

                updateQuickslot();
                curUser.spendAndNext(1f);
            }
        }
    }

    private void MindBrack(Hero hero) {
        PathFinder.buildDistanceMap(hero.pos, BArray.not(Dungeon.level.solid, null), 2);
        for (int cell = 0; cell < PathFinder.distance.length; cell++) {
            if (PathFinder.distance[cell] < Integer.MAX_VALUE) {
                CellEmitter.get( cell ).burst( Speck.factory( Speck.MASK ), 10 );
                Char ch = Actor.findChar(cell);
                if (ch != null&& !(ch instanceof Hero) && ch instanceof Mob && ch.alignment != Char.Alignment.ALLY) {
                    if (!ch.isImmune(Corruption.class)) {
                        boolean chance = true;

                        if (chance)Buff.affect(ch, Corruption.class);

                        boolean droppingLoot = ch.alignment != Char.Alignment.ALLY;

                        if (ch.buff(Corruption.class) != null){
                            if (droppingLoot) ((Mob)ch).rollToDropLoot();
                            Statistics.enemiesSlain++;
                            Badges.validateMonstersSlain();
                            Statistics.qualifiedForNoKilling = false;
                            if (((Mob)ch).EXP > 0 && curUser.lvl <= ((Mob)ch).maxLvl) {
                                curUser.sprite.showStatus(CharSprite.POSITIVE, Messages.get(((Mob)ch), "exp", ((Mob)ch).EXP));
                                curUser.earnExp(((Mob)ch).EXP, ((Mob)ch).getClass());
                            } else {
                                curUser.earnExp(0, ((Mob)ch).getClass());
                            }
                        }
                    }

                }}}
    }


    @Override
    public String status() {
        if (chargeCap == 100)
            return Messages.format("%d%%", charge);
        return null;
    }

    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe{

        {
            inputs =  new Class[]{WarHammer.class, WandOfCorruption.class};
            inQuantity = new int[]{1, 1};

            cost = 3;

            output = PINK.class;
            outQuantity = 1;
        }
    }

    @Override
    public String desc() {
        String info = Messages.get(this, "desc");
        if (Dungeon.hero.belongings.getItem(RingOfHaste.class) != null) {
            if (Dungeon.hero.belongings.getItem(RingOfHaste.class).isEquipped(Dungeon.hero))
                info += "\n\n" + Messages.get( PINK.class, "setbouns");}

        return info;
    }

}