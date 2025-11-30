/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2025 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.actors.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.QuickSlot;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.HorseRiding;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ArmorAbility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.cleric.AscendedForm;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.cleric.PowerOfMany;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.cleric.Trinity;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.duelist.Challenge;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.duelist.ElementalStrike;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.duelist.Feint;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.huntress.NaturesPower;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.huntress.SpectralBlades;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.huntress.SpiritHawk;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.mage.ElementalBlast;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.mage.WarpBeacon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.mage.WildMagic;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.rogue.DeathMark;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.rogue.ShadowClone;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.rogue.SmokeBomb;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.warrior.Endure;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.warrior.HeroicLeap;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.warrior.Shockwave;
import com.shatteredpixel.shatteredpixeldungeon.items.AnnihilationGear;
import com.shatteredpixel.shatteredpixeldungeon.items.BrokenSeal;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.KingsCrown;
import com.shatteredpixel.shatteredpixeldungeon.items.Neotel;
import com.shatteredpixel.shatteredpixeldungeon.items.NitoDismantleHammer;
import com.shatteredpixel.shatteredpixeldungeon.items.TengusMask;
import com.shatteredpixel.shatteredpixeldungeon.items.Waterskin;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClothArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.PlateArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.CloakOfShadows;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.HolyTome;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.VelvetPouch;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Food;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Pasty;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfInvisibility;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLiquidFlame;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfMindVision;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfPurity;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfStrength;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfCleansing;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Diomap;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Drago;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Spw;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Spw11;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.UV;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfAccuracy;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfEnergy;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfMight;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfIdentify;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfLullaby;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMirrorImage;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRage;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRemoveCurse;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfAdvanceguard;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfDisintegration;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfMagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.SpiritBow;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Cudgel;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Dagger;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Gloves;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Rapier;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.WornShortsword;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.ThrowingKnife;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.ThrowingSpike;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.ThrowingStone;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.watabou.utils.DeviceCompat;

public enum HeroClass {

    //영웅 순서 바꾸고 싶으면 여길 바꾸면 된다.
    WARRIOR(HeroSubClass.BERSERKER, HeroSubClass.GLADIATOR),
    MAGE(HeroSubClass.BATTLEMAGE, HeroSubClass.WARLOCK),
    ROGUE(HeroSubClass.ASSASSIN, HeroSubClass.FREERUNNER),
    DUELIST(HeroSubClass.CHAMPION, HeroSubClass.MONK),
    HUNTRESS(HeroSubClass.SNIPER, HeroSubClass.WARDEN),
    CLERIC(HeroSubClass.PRIEST, HeroSubClass.PALADIN);

    private HeroSubClass[] subClasses;

    HeroClass(HeroSubClass... subClasses) {
        this.subClasses = subClasses;
    }

    public void initHero(Hero hero) {
        SPDSettings.quickslotWaterskin(true);

        hero.heroClass = this;
        Talent.initClassTalents(hero);

        Item i = new ClothArmor().identify();
        if (!Challenges.isItemBlocked(i)) hero.belongings.armor = (ClothArmor) i;

        i = new Food();
        if (!Challenges.isItemBlocked(i)) i.collect();

        new VelvetPouch().collect();
        Dungeon.LimitedDrops.VELVET_POUCH.drop();

        Waterskin waterskin = new Waterskin();
        waterskin.collect();

        new ScrollOfIdentify().identify();

        if (DeviceCompat.isDebug()) {
            new Pasty().collect();
            new RingOfMight().identify().upgrade(999).collect();
            new ThrowingSpike().identify().upgrade(9999).collect();
            new RingOfAccuracy().identify().upgrade(999).collect();
            new PotionOfCleansing().identify().collect();
            new PlateArmor().identify().upgrade(999).collect();
            new Neotel().collect();
            new Spw().identify().quantity(100).collect();
            new Bomb().identify().quantity(100).collect();
            new StoneOfAdvanceguard().identify().quantity(100).collect();
            new UV().identify().quantity(100).collect();
            new WandOfDisintegration().identify().upgrade(12).collect();
            new Spw11().identify().quantity(100).collect();
            new PotionOfHealing().identify().quantity(100).collect();
            new TengusMask().collect();
            new KingsCrown().collect();
        }

        switch (this) {
            case WARRIOR:
                initWarrior(hero);
                break;

            case MAGE:
                initMage(hero);
                break;

            case ROGUE:
                initRogue(hero);
                break;

            case HUNTRESS:
                initHuntress(hero);
                break;

            case DUELIST:
                initDuelist(hero);
                break;

            case CLERIC:
                initCleric(hero);
                break;
        }

        if (SPDSettings.quickslotWaterskin()) {
            for (int s = 0; s < QuickSlot.SIZE; s++) {
                if (Dungeon.quickslot.getItem(s) == null) {
                    Dungeon.quickslot.setSlot(s, waterskin);
                    break;
                }
            }
        }

    }

    public Badges.Badge masteryBadge() {
        switch (this) {
            case WARRIOR:
                return Badges.Badge.MASTERY_WARRIOR;
            case MAGE:
                return Badges.Badge.MASTERY_MAGE;
            case ROGUE:
                return Badges.Badge.MASTERY_ROGUE;
            case HUNTRESS:
                return Badges.Badge.MASTERY_HUNTRESS;
            case DUELIST:
                return Badges.Badge.MASTERY_DUELIST;
            case CLERIC:
                return Badges.Badge.MASTERY_CLERIC;
        }
        return null;
    }

    private static void initWarrior(Hero hero) {
        (hero.belongings.weapon = new WornShortsword()).identify();
        ThrowingStone stones = new ThrowingStone();
        stones.identify().collect();
        Dungeon.quickslot.setSlot(0, stones);

        if (hero.belongings.armor != null) {
            hero.belongings.armor.affixSeal(new BrokenSeal());
            Catalog.setSeen(BrokenSeal.class); //as it's not added to the inventory
        }

        if (SPDSettings.getDio() >= 1) {
            NitoDismantleHammer hamm = new NitoDismantleHammer();
            Diomap map = new Diomap();
            hamm.collect();
            map.collect();
            Dungeon.quickslot.setSlot(1, hamm);
            Dungeon.quickslot.setSlot(2, map);
        }

        new PotionOfHealing().identify();
        new ScrollOfRage().identify();
    }

    private static void initMage(Hero hero) {
        MagesStaff staff;

        staff = new MagesStaff(new WandOfMagicMissile());

        (hero.belongings.weapon = staff).identify();
        hero.belongings.weapon.activate(hero);

        if (SPDSettings.getDio() >= 1) {
            NitoDismantleHammer hamm = new NitoDismantleHammer();
            Diomap map = new Diomap();
            hamm.collect();
            map.collect();
            Dungeon.quickslot.setSlot(1, hamm);
            Dungeon.quickslot.setSlot(2, map);
        }

        Dungeon.quickslot.setSlot(0, staff);

        new ScrollOfUpgrade().identify();
        new PotionOfLiquidFlame().identify();
    }

    private static void initRogue(Hero hero) {
        (hero.belongings.weapon = new Dagger()).identify();

        CloakOfShadows cloak = new CloakOfShadows();
        (hero.belongings.artifact = cloak).identify();
        hero.belongings.artifact.activate(hero);

        ThrowingKnife knives = new ThrowingKnife();
        knives.identify().collect();

        Dungeon.quickslot.setSlot(0, cloak);
        Dungeon.quickslot.setSlot(1, knives);

        if (SPDSettings.getDio() >= 1) {
            NitoDismantleHammer hamm = new NitoDismantleHammer();
            Diomap map = new Diomap();
            hamm.collect();
            map.collect();
            Dungeon.quickslot.setSlot(2, hamm);
            Dungeon.quickslot.setSlot(3, map);
        }

        new ScrollOfMagicMapping().identify();
        new PotionOfInvisibility().identify();
    }

    private static void initHuntress(Hero hero) {

        (hero.belongings.weapon = new Gloves()).identify();
        SpiritBow bow = new SpiritBow();
        bow.identify().collect();

        if (SPDSettings.getDio() >= 1) {
            NitoDismantleHammer hamm = new NitoDismantleHammer();
            Diomap map = new Diomap();
            hamm.collect();
            map.collect();
            Dungeon.quickslot.setSlot(1, hamm);
            Dungeon.quickslot.setSlot(2, map);
        }

        Dungeon.quickslot.setSlot(0, bow);

        new PotionOfMindVision().identify();
        new ScrollOfLullaby().identify();
    }

    private static void initDuelist(Hero hero) {

        (hero.belongings.weapon = new Rapier()).identify();
        hero.belongings.weapon.activate(hero);

        ThrowingSpike spikes = new ThrowingSpike();
        spikes.quantity(2).identify().collect(); //set quantity is 3, but Duelist starts with 2

        Dungeon.quickslot.setSlot(0, hero.belongings.weapon);
        Dungeon.quickslot.setSlot(1, spikes);

        if (SPDSettings.getDio() >= 1) {
            NitoDismantleHammer hamm = new NitoDismantleHammer();
            Diomap map = new Diomap();
            hamm.collect();
            map.collect();
            Dungeon.quickslot.setSlot(2, hamm);
            Dungeon.quickslot.setSlot(3, map);
        }

        new PotionOfStrength().identify();
        new ScrollOfMirrorImage().identify();
    }

    private static void initCleric(Hero hero) {

        (hero.belongings.weapon = new Cudgel()).identify();
        hero.belongings.weapon.activate(hero);

        HolyTome tome = new HolyTome();
        (hero.belongings.artifact = tome).identify();
        hero.belongings.artifact.activate(hero);

        Dungeon.quickslot.setSlot(0, tome);

        if (SPDSettings.getDio() >= 1) {
            NitoDismantleHammer hamm = new NitoDismantleHammer();
            Diomap map = new Diomap();
            hamm.collect();
            map.collect();
            Dungeon.quickslot.setSlot(2, hamm);
            Dungeon.quickslot.setSlot(3, map);
        }

        new PotionOfPurity().identify();
        new ScrollOfRemoveCurse().identify();
    }

    private static void initJohnny(Hero hero) {

        (hero.belongings.weapon = new Cudgel()).identify();
        hero.belongings.weapon.activate(hero);

        Buff.affect(hero, HorseRiding.class).set();

        AnnihilationGear annihilationGear = new AnnihilationGear();
        annihilationGear.identify().collect();
        Dungeon.quickslot.setSlot(0, annihilationGear);

        if (SPDSettings.getDio() >= 1) {
            NitoDismantleHammer hamm = new NitoDismantleHammer();
            Diomap map = new Diomap();
            hamm.collect();
            map.collect();
            Dungeon.quickslot.setSlot(2, hamm);
            Dungeon.quickslot.setSlot(3, map);
        }

        new PotionOfPurity().identify();
        new ScrollOfRemoveCurse().identify();
    }

    public String title() {
        return Messages.get(HeroClass.class, name());
    }

    public String desc() {
        return Messages.get(HeroClass.class, name() + "_desc");
    }

    public String shortDesc() {
        return Messages.get(HeroClass.class, name() + "_desc_short");
    }

    public HeroSubClass[] subClasses() {
        return subClasses;
    }

    //레퀴엠 능력 순서 바꾸고 싶으면 여길 바꾸면 된다.

    public ArmorAbility[] armorAbilities() {
        switch (this) {
            case WARRIOR:
            default:
                return new ArmorAbility[]{new HeroicLeap(), new Shockwave(), new Endure()};
            case MAGE:
                return new ArmorAbility[]{new ElementalBlast(), new WildMagic(), new WarpBeacon()};
            case ROGUE:
                return new ArmorAbility[]{new SmokeBomb(), new DeathMark(), new ShadowClone()};
            case HUNTRESS:
                return new ArmorAbility[]{new SpectralBlades(), new NaturesPower(), new SpiritHawk()};
            case DUELIST:
                return new ArmorAbility[]{new Feint(), new Challenge(), new ElementalStrike()};
            case CLERIC:
                return new ArmorAbility[]{new AscendedForm(), new Trinity(), new PowerOfMany()};
        }
    }

    public String spritesheet() {
        switch (this) {
            case WARRIOR:
            default:
                if (SPDSettings.getSkin() == 1) {
                    return Assets.Sprites.WARRIOR2;
                } else return Assets.Sprites.WARRIOR;
            case MAGE:
                if (SPDSettings.getSkin2() == 1) {
                    return Assets.Sprites.MAGE2;
                } else return Assets.Sprites.MAGE;
            case ROGUE:
                if (SPDSettings.getSkin3() == 1) {
                    return Assets.Sprites.ROGUE2;
                } else return Assets.Sprites.ROGUE;
            case DUELIST:
                if (SPDSettings.getSkin4() == 1) {
                    return Assets.Sprites.DUELIST2;
                } else return Assets.Sprites.DUELIST;
            case HUNTRESS:
                if (SPDSettings.getSkin5() == 1) {
                    return Assets.Sprites.HUNTRESS2;
                } else return Assets.Sprites.HUNTRESS;
            case CLERIC:
                if (SPDSettings.getSkin6() == 1) {
                    return Assets.Sprites.CLERIC2;
                } else return Assets.Sprites.CLERIC;
        }
    }

    public String splashArt() {
        switch (this) {
            case WARRIOR:
            default:
                if (SPDSettings.getDio() >= 1) {
                    return Assets.Splashes.BRANDO;
                } else return Assets.Splashes.WARRIOR;

            case MAGE:

                if (SPDSettings.getDio() >= 1) {
                    return Assets.Splashes.BRANDO;
                } else return Assets.Splashes.MAGE;

            case ROGUE:

                if (SPDSettings.getDio() >= 1) {
                    return Assets.Splashes.BRANDO;
                } else return Assets.Splashes.ROGUE;

            case HUNTRESS:

                if (SPDSettings.getDio() >= 1) {
                    return Assets.Splashes.BRANDO;
                } else return Assets.Splashes.HUNTRESS;

            case DUELIST:

                if (SPDSettings.getDio() >= 1) {
                    return Assets.Splashes.BRANDO;
                } else return Assets.Splashes.DUELIST;

            case CLERIC:

                if (SPDSettings.getDio() >= 1) {
                    return Assets.Splashes.BRANDO;
                } else return Assets.Splashes.CLERIC;
        }
    }

    public boolean isUnlocked() {
        //always unlock on debug builds
        if (DeviceCompat.isDebug()) return true;

        switch (this) {
            case WARRIOR:
            default:
                return true;
            case MAGE:
                return Badges.isUnlocked(Badges.Badge.UNLOCK_MAGE);
            case ROGUE:
                return Badges.isUnlocked(Badges.Badge.UNLOCK_ROGUE);
            case HUNTRESS:
                return Badges.isUnlocked(Badges.Badge.UNLOCK_HUNTRESS);
            case DUELIST:
                return Badges.isUnlocked(Badges.Badge.UNLOCK_DUELIST);
            case CLERIC:
                return Badges.isUnlocked(Badges.Badge.UNLOCK_CLERIC);
        }
    }

    public String unlockMsg() {
        return shortDesc() + "\n\n" + Messages.get(HeroClass.class, name() + "_unlock");
    }

}
