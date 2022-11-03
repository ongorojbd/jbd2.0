/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2022 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.items.rings;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.items.BrokenSeal;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.Honeypot;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.AlchemicalCatalyst;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfExperience;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.ExoticPotion;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTransmutation;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ExoticScroll;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfEnchantment;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.ArcaneCatalyst;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfEnchantment;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.Visual;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;

public class RingOfDrago extends Ring {

    {
        image = ItemSpriteSheet.GRAVE;

        unique = true;
    }

    private float triesToDrop = Float.MIN_VALUE;
    private int dropsToRare = Integer.MIN_VALUE;

    public String statsInfo() {
        if (this.isEquipped(Dungeon.hero)){
            this.identify();
        }
        if (isIdentified()){
            return Messages.get(this, "stats", new DecimalFormat("#.##").format(100f * (Math.pow(1.20f, soloBuffedBonus()) - 1f)));
        } else {
            return Messages.get(this, "typical_stats", new DecimalFormat("#.##").format(20f));
        }
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    private static final String TRIES_TO_DROP = "tries_to_drop";
    private static final String DROPS_TO_RARE = "drops_to_rare";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(TRIES_TO_DROP, triesToDrop);
        bundle.put(DROPS_TO_RARE, dropsToRare);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        triesToDrop = bundle.getFloat(TRIES_TO_DROP);
        dropsToRare = bundle.getInt(DROPS_TO_RARE);
    }

    @Override
    protected RingBuff buff( ) {
        return new Drago();
    }

    public static float dropChanceMultiplier( Char target ){
        return (float)Math.pow(1.20, getBuffedBonus(target, Drago.class));
    }

    public static ArrayList<Item> tryForBonusDrop(Char target, int tries ){
        int bonus = getBuffedBonus(target, Drago.class);

        if (bonus <= 0) return null;

        HashSet<Drago> buffs = target.buffs(Drago.class);
        float triesToDrop = Float.MIN_VALUE;
        int dropsToEquip = Integer.MIN_VALUE;

        //find the largest count (if they aren't synced yet)
        for (Drago w : buffs){
            if (w.triesToDrop() > triesToDrop){
                triesToDrop = w.triesToDrop();
                dropsToEquip = w.dropsToRare();
            }
        }

        //reset (if needed), decrement, and store counts
        if (triesToDrop == Float.MIN_VALUE) {
            triesToDrop = (Dungeon.isChallenged(Challenges.GAMBLER)) ? Random.NormalIntRange(0, 10) : Random.NormalIntRange(0, 25);
            dropsToEquip = (Dungeon.isChallenged(Challenges.GAMBLER)) ? 6 : Random.NormalIntRange(4, 8);
        }

        //now handle reward logic
        ArrayList<Item> drops = new ArrayList<>();
        ScrollOfUpgrade scl = new ScrollOfUpgrade();
        ScrollOfEnchantment enchantment = new ScrollOfEnchantment();

        triesToDrop -= tries;
        while ( triesToDrop <= 0 ){
            if (dropsToEquip <= 0){
                Item i;
                do {
                    i = genEquipmentDrop(bonus - 1);
                } while (Challenges.isItemBlocked(i));
                if (Dungeon.isChallenged(Challenges.GAMBLER)) {
                    if (Dungeon.isChallenged(Challenges.NO_SCROLLS) && Random.Int(2) == 0) {
                        drops.add(enchantment);
                    } else {
                        drops.add(scl);
                    }
                } else {
                    drops.add(i);
                }
                dropsToEquip = (Dungeon.isChallenged(Challenges.GAMBLER)) ? 6 : Random.NormalIntRange(4, 8);
            } else {
                Item i;
                do {
                    i = genConsumableDrop(bonus - 1);
                } while (Challenges.isItemBlocked(i));
                drops.add(i);
                dropsToEquip--;
            }
            triesToDrop += (Dungeon.isChallenged(Challenges.GAMBLER)) ? Random.NormalIntRange(0, 10) : Random.NormalIntRange(0, 25);
        }

        //store values back into rings
        for (Drago w : buffs){
            w.triesToDrop(triesToDrop);
            w.dropsToRare(dropsToEquip);
        }

        return drops;
    }

    //used for visuals
    // 1/2/3 used for low/mid/high tier consumables
    // 3 used for +0-1 equips, 4 used for +2 or higher equips
    private static int latestDropTier = 0;

    public static void showFlareForBonusDrop( Visual vis ){
        if (vis == null || vis.parent == null) return;
        switch (latestDropTier){
            default:
                break; //do nothing
            case 1:
                new Flare(6, 20).color(0x00FF00, true).show(vis, 3f);
                break;
            case 2:
                new Flare(6, 24).color(0x00AAFF, true).show(vis, 3.33f);
                break;
            case 3:
                new Flare(6, 28).color(0xAA00FF, true).show(vis, 3.67f);
                break;
            case 4:
                new Flare(6, 32).color(0xFFAA00, true).show(vis, 4f);
                break;
        }
        latestDropTier = 0;
    }

    public static Item genConsumableDrop(int level) {
        float roll = Random.Float();
        //60% chance - 4% per level. Starting from +15: 0%
        if (roll < (0.6f - 0.04f * level)) {
            latestDropTier = 1;
            return genLowValueConsumable();
            //30% chance + 2% per level. Starting from +15: 60%-2%*(lvl-15)
        } else if (roll < (0.9f - 0.02f * level)) {
            latestDropTier = 2;
            return genMidValueConsumable();
            //10% chance + 2% per level. Starting from +15: 40%+2%*(lvl-15)
        } else {
            latestDropTier = 3;
            return genHighValueConsumable();
        }
    }

    private static Item genLowValueConsumable(){
        if (Dungeon.isChallenged(Challenges.GAMBLER)) {
            switch (Random.Int(4)){
                case 0: default:
                    return Generator.randomUsingDefaults(Generator.Category.SEED);
                case 1:
                    return Generator.randomUsingDefaults(Generator.Category.STONE);
                case 2:
                    return Generator.randomUsingDefaults(Generator.Category.POTION);
                case 3:
                    if (Dungeon.isChallenged(Challenges.NO_SCROLLS)) {
                        if (Random.Int(16) == 0) {
                            return new ScrollOfUpgrade();
                        } else {
                            return Generator.randomUsingDefaults(Generator.Category.SCROLL);
                        }
                    } else {
                        if (Random.Int(8) == 0) {
                            return new ScrollOfUpgrade();
                        } else {
                            return Generator.randomUsingDefaults(Generator.Category.SCROLL);
                        }
                    }
            }
        } else {
            switch (Random.Int(4)){
                case 0: default:
                    Item i = new Gold().random();
                    return i.quantity(i.quantity()/2);
                case 1:
                    return Generator.randomUsingDefaults(Generator.Category.STONE);
                case 2:
                    return Generator.randomUsingDefaults(Generator.Category.POTION);
                case 3:
                    return Generator.randomUsingDefaults(Generator.Category.SCROLL);
            }
        }
    }

    private static Item genMidValueConsumable(){
        switch (Random.Int(6)){
            case 0: default:
                Item i = genLowValueConsumable();
                return i.quantity(i.quantity()*2);
            case 1:
                i = Generator.randomUsingDefaults(Generator.Category.POTION);
                return Reflection.newInstance(ExoticPotion.regToExo.get(i.getClass()));
            case 2:
                i = Generator.randomUsingDefaults(Generator.Category.SCROLL);
                if (Dungeon.isChallenged(Challenges.GAMBLER)) {
                    if (Dungeon.isChallenged(Challenges.NO_SCROLLS)) {
                        if (Random.Int(8) == 0) {
                            return new ScrollOfUpgrade();
                        } else {
                            return Reflection.newInstance(ExoticScroll.regToExo.get(i.getClass()));
                        }
                    } else {
                        if (Random.Int(4) == 0) {
                            return new ScrollOfUpgrade();
                        } else {
                            return Reflection.newInstance(ExoticScroll.regToExo.get(i.getClass()));
                        }
                    }
                } else {
                    return Reflection.newInstance(ExoticScroll.regToExo.get(i.getClass()));
                }

            case 3:
                return Random.Int(2) == 0 ? new ArcaneCatalyst() : new AlchemicalCatalyst();
            case 4:
                return new Bomb();
            case 5:
                return new Honeypot();
        }
    }

    private static Item genHighValueConsumable(){
        if (Dungeon.isChallenged(Challenges.GAMBLER)) {
            if (Dungeon.isChallenged(Challenges.NO_SCROLLS)) {
                switch (Random.Int(10)){
                    case 0: case 1: default:
                        Item i = genMidValueConsumable();
                        if (i instanceof Bomb){
                            return new Bomb.DoubleBomb();
                        } else {
                            return i.quantity(i.quantity()*2);
                        }
                    case 2: case 3:
                        return new StoneOfEnchantment();
                    case 4: case 5:
                        return new PotionOfExperience();
                    case 6: case 7:
                        return new ScrollOfTransmutation();
                    case 8:
                        return new ScrollOfUpgrade();
                    case 9:
                        return new ScrollOfEnchantment();
                }
            } else {
                switch (Random.Int(5)){
                    case 0: default:
                        Item i = genMidValueConsumable();
                        if (i instanceof Bomb){
                            return new Bomb.DoubleBomb();
                        } else {
                            return i.quantity(i.quantity()*2);
                        }
                    case 1:
                        return new StoneOfEnchantment();
                    case 2:
                        return new PotionOfExperience();
                    case 3:
                        return new ScrollOfTransmutation();
                    case 4:
                        return new ScrollOfUpgrade();
                }
            }
        } else {
            switch (Random.Int(4)){
                case 0: default:
                    Item i = genMidValueConsumable();
                    if (i instanceof Bomb){
                        return new Bomb.DoubleBomb();
                    } else {
                        return i.quantity(i.quantity()*2);
                    }
                case 1:
                    return new StoneOfEnchantment();
                case 2:
                    return new PotionOfExperience();
                case 3:
                    return new ScrollOfTransmutation();
            }
        }
    }

    private static Item genEquipmentDrop( int level ){
        Item result;
        //each upgrade increases depth used for calculating drops by 1
        int floorset = (Dungeon.depth + level)/5;
        switch (Random.Int(5)){
            default: case 0: case 1:
                Weapon w = Generator.randomWeapon(floorset);
                if (!w.hasGoodEnchant() && Random.Int(10) < level)      w.enchant();
                else if (w.hasCurseEnchant())                           w.enchant(null);
                result = w;
                break;
            case 2:
                Armor a = Generator.randomArmor(floorset);
                if (!a.hasGoodGlyph() && Random.Int(10) < level)        a.inscribe();
                else if (a.hasCurseGlyph())                             a.inscribe(null);
                result = a;
                break;
            case 3:
                result = Generator.random(Generator.Category.RING);
                break;
            case 4:
                if (Dungeon.isChallenged(Challenges.GAMBLER)) {
                    result = Generator.random(Generator.Category.RING);
                } else {
                    result = Generator.random(Generator.Category.ARTIFACT);
                }
                break;
        }

        //minimum level is 1/2/3/4/5/6 when ring level is 1/3/6/10/15/21
        if (result.isUpgradable()){
            int minLevel = (int)Math.floor((Math.sqrt(8*level + 1)-1)/2f);
            if (result.level() < minLevel){
                result.level(minLevel);
            }
        }
        result.cursed = false;
        result.cursedKnown = true;
        if (result.level() >= 2 || Dungeon.isChallenged(Challenges.GAMBLER)) {
            latestDropTier = 4;
        } else {
            latestDropTier = 3;
        }
        return result;
    }

    public class Drago extends RingBuff {

        private void triesToDrop( float val ){
            triesToDrop = val;
        }

        private float triesToDrop(){
            return triesToDrop;
        }

        private void dropsToRare( int val ) {
            dropsToRare = val;
        }

        private int dropsToRare(){
            return dropsToRare;
        }

    }
}
