package com.shatteredpixel.shatteredpixeldungeon.items.spells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.Transmuting;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.EquipableItem;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.Recipe;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClothArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.food.MysteryMeat;
import com.shatteredpixel.shatteredpixeldungeon.items.food.StewedMeat;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTransmutation;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfUpgrade;

import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfAugmentation;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.AJA;

import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.AssassinsBlade;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.BattleAxe;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.ChaosSword;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Crossbow;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.DBLADE;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Dagger;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Dirk;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Flail;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.FlameKatana;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Gauntlet;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Glaive;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Gloves;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Greataxe;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Greatshield;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Greatsword;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.HandAxe;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.HeavyMachinegun;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.KSG;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.LSWORD;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Longsword;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MISTA;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Mace;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.PINK;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Quarterstaff;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.RoundShield;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.RunicBlade;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Sai;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Scimitar;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Shortsword;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.SnowHunter;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Spear;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Spheaven;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Sword;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.WarHammer;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Whip;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.WornShortsword;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Random;

public class Rocacaca extends InventorySpell {
    {
        image = ItemSpriteSheet.RO1;

        stackable = true;
    }

    @Override
    protected boolean usableOnItem(Item item) {
        return item instanceof Glaive
                || item instanceof Greatshield
                || item instanceof Gauntlet
                || item instanceof Greataxe
                || item instanceof WarHammer
                || item instanceof Greatsword

                || item instanceof Crossbow
                || item instanceof AssassinsBlade
                || item instanceof RunicBlade
                || item instanceof Flail
                || item instanceof BattleAxe
                || item instanceof Longsword

                || item instanceof Sword
                || item instanceof Mace
                || item instanceof Scimitar
                || item instanceof RoundShield
                || item instanceof Sai
                || item instanceof Whip

                || item instanceof Shortsword
                || item instanceof HandAxe
                || item instanceof Spear
                || item instanceof Quarterstaff
                || item instanceof Dirk

                || item instanceof WornShortsword
                || item instanceof Gloves
                || item instanceof Dagger
                || item instanceof MagesStaff

                || item instanceof KSG
                || item instanceof HeavyMachinegun
                || item instanceof ChaosSword
                || item instanceof FlameKatana

                || item instanceof AJA
                || item instanceof PINK
                || item instanceof LSWORD
                || item instanceof DBLADE
                || item instanceof MISTA
                || item instanceof SnowHunter
                || item instanceof Spheaven

                || item instanceof Potion;
    }

    @Override
    protected void onItemSelected(Item item) {
        Item result;

        int ItemLevel = item.level();
        result = changeAvant(item, ItemLevel);

        if (result == null) {
            //This shouldn't ever trigger
            GLog.n(Messages.get(ScrollOfTransmutation.class, "nothing"));
            curItem.collect(curUser.belongings.backpack);
        }
        else {
            if (item.isEquipped(Dungeon.hero)) {
                item.cursed = false; //to allow it to be unequipped
                ((EquipableItem) item).doUnequip(Dungeon.hero, false);
                ((EquipableItem) result).doEquip(Dungeon.hero);
            } else {
                item.detach(Dungeon.hero.belongings.backpack);
                if (!result.collect()) {
                    Dungeon.level.drop(result, curUser.pos).sprite.drop();
                }
            }
            if (result.isIdentified()) {
                Catalog.setSeen(result.getClass());
            }
            Transmuting.show(curUser, item, result);
            curUser.sprite.emitter().start(Speck.factory(Speck.CHANGE), 0.2f, 10);
            GLog.p(Messages.get(ScrollOfTransmutation.class, "morph"));
        }
    }


    private Item changeAvant(Item item, int Level) {
        if (item.isEquipped(Dungeon.hero)) return null;
        if (item instanceof Spell) { // 아이템이 돌일 경우

                   switch (Random.Int(6)) {
                       case 0:
                           item = new LSWORD();
                           break;
                       case 1:
                           item = new MISTA();
                           break;
                       case 2:
                           item = new DBLADE();
                           break;
                       case 3:
                           item = new PINK();
                           break;
                       case 4:
                           item = new AJA();
                           break;
                       case 5:
                           item = new SnowHunter();
                           break;
                   }

        }

        else {
            item = null;
        }

        return item;
    }

    @Override
    public int value() {
        //prices of ingredients, divided by output quantity
        return Math.round(quantity * ((30 + 100) / 3f));
    }



    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe{
        {
            inputs =  new Class[]{ClothArmor.class};
            inQuantity = new int[]{1};

            cost = 30;

            output = Rocacaca.class;
            outQuantity = 1;
        }
    }


}