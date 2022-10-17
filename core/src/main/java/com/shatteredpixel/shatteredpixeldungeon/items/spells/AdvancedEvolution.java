/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2021 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon.items.spells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.Transmuting;
import com.shatteredpixel.shatteredpixeldungeon.items.EquipableItem;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.LiquidMetal;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.AlchemicalCatalyst;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfWeaponEnhance;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Sleepcmoon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.SpiritBow;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
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
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Spear;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Sword;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.WarHammer;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Whip;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.WornShortsword;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class AdvancedEvolution extends InventorySpell {

    {
        image = ItemSpriteSheet.BEACON;

        unique = true;
        bones = false;
    }

    private static final ItemSprite.Glowing WHITE = new ItemSprite.Glowing( 0xFFFFFF );

    @Override
    public ItemSprite.Glowing glowing() {
        return WHITE;
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
                || item instanceof Sleepcmoon

                || item instanceof AJA
                || item instanceof PINK
                || item instanceof LSWORD
                || item instanceof DBLADE
                || item instanceof MISTA;
    }

    @Override
    protected void onItemSelected(Item item) {

        Item result = changeItem(item);

        if (result == null){
            //This shouldn't ever trigger
            GLog.n( Messages.get(this, "nothing") );
            curItem.collect( curUser.belongings.backpack );
        } else {
            if (item.isEquipped(Dungeon.hero)){
                item.cursed = false; //to allow it to be unequipped
                ((EquipableItem)item).doUnequip(Dungeon.hero, false);
                ((EquipableItem)result).doEquip(Dungeon.hero);
            } else {
                item.detach(Dungeon.hero.belongings.backpack);
                if (!result.collect()){
                    Dungeon.level.drop(result, curUser.pos).sprite.drop();
                }
            }
            if (result.isIdentified()){
                Catalog.setSeen(result.getClass());
            }
            Transmuting.show(curUser, item, result);
            curUser.sprite.emitter().start(Speck.factory(Speck.MASK), 0.2f, 10);
            GLog.p( Messages.get(this, "evolve") );
            Sample.INSTANCE.play( Assets.Sounds.HAHAH );
        }

    }

    public static Item changeItem( Item item ){
        if (item instanceof MeleeWeapon
                || item instanceof SpiritBow
                || item instanceof MissileWeapon ) {
            return changeWeapon((Weapon) item);
        } else {
            return null;
        }
    }

    private static Weapon changeWeapon( Weapon w ) {

        //Normal Weapon Success Rate: 90%
        //Gun Success Rate: 70%
        //Gus Success Rate for Gunner: 100%

        Weapon n;
        {




            { //w instanceof Whip
                switch (Random.Int(5)) {
                    case 0:
                    default:
                        n = new LSWORD();
                        break;
                    case 1:
                        n = new MISTA();
                        break;
                    case 2:
                        n = new DBLADE();
                        break;
                    case 3:
                        n = new PINK();
                        break;
                    case 4:
                        n = new AJA();
                        break;
                }  {

            }
            }
            int level = w.level();
            if (w.curseInfusionBonus) level--;
            if (level > 0) {
                n.upgrade( level );
            } else if (level < 0) {
                n.degrade( -level );
            }

            n.enchantment = w.enchantment;
            n.curseInfusionBonus = w.curseInfusionBonus;
            n.masteryPotionBonus = w.masteryPotionBonus;
            n.levelKnown = w.levelKnown;
            n.cursedKnown = w.cursedKnown;
            n.cursed = w.cursed;
            n.augment = w.augment;

            return n;

        }
    }

    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe{

        {
            inputs =  new Class[]{BossdiscF.class, LiquidMetal.class};
            inQuantity = new int[]{1, 1};

            cost = 3;

            output = AdvancedEvolution.class;
            outQuantity = 1;
        }
    }

}