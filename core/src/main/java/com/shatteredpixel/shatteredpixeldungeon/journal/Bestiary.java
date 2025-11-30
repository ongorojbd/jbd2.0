/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2024 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.journal;

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.cleric.PowerOfMany;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.huntress.SpiritHawk;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.rogue.ShadowClone;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.rogue.SmokeBomb;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.*;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Annasui;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Bdth;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Blacksmith;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.DArby;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Dannynpc;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Fugo;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Ghost;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Imp;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.MirrorImage;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Polpo;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.PrismaticImage;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.RatKing;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Retonio;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Rohan;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Rohan2;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Rohan3;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Sheep;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Shopkeeper;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.So1;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Speedwagon2;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.TendencyShopkeeper;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.TuskBestiary2;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.TuskBestiary4;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Wandmaker;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Weather;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Willson;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Yasu;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Yukako;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.DriedRose;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.CorpseDust;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfLivingEarth;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfRegrowth;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfWarding;
import com.shatteredpixel.shatteredpixeldungeon.levels.TempleLastLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special.SentryRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.AlarmTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.BlazingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.BurningTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ChillingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ConfusionTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.CorrosionTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.CreamTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.CursingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.DisarmingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.DisintegrationTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.DistortionTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.DoobieTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ExplosiveTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.FancakeTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.FlashingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.FlockTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.FrostTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GatewayTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GeyserTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GnollRockfallTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GrimTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GrippingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GuardianTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.MachineTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.OozeTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.PitfallTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.PoisonDartTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.RockfallTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ShockingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.StormTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.SummoningTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.TeleportationTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.TenguDartTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ToxicTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.WarpingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.WeakeningTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.WiredTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.WornDartTrap;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.BlandfruitBush;
import com.shatteredpixel.shatteredpixeldungeon.plants.Blindweed;
import com.shatteredpixel.shatteredpixeldungeon.plants.Earthroot;
import com.shatteredpixel.shatteredpixeldungeon.plants.Fadeleaf;
import com.shatteredpixel.shatteredpixeldungeon.plants.Firebloom;
import com.shatteredpixel.shatteredpixeldungeon.plants.Icecap;
import com.shatteredpixel.shatteredpixeldungeon.plants.Mageroyal;
import com.shatteredpixel.shatteredpixeldungeon.plants.Rotberry;
import com.shatteredpixel.shatteredpixeldungeon.plants.Sorrowmoss;
import com.shatteredpixel.shatteredpixeldungeon.plants.Starflower;
import com.shatteredpixel.shatteredpixeldungeon.plants.Stormvine;
import com.shatteredpixel.shatteredpixeldungeon.plants.Sungrass;
import com.shatteredpixel.shatteredpixeldungeon.plants.Swiftthistle;
import com.watabou.utils.Bundle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;

//contains all the game's various entities, mostly enemies, NPCS, and allies, but also traps and plants
public enum Bestiary {

    REGIONAL,
    BOSSES,
    UNIVERSAL,
    RARE,
    CASTLE,
    TENDENCY,
    QUEST,
    NEUTRAL,
    ALLY,
    TRAP,
    PLANT;

    //tracks whether an entity has been encountered
    private final LinkedHashMap<Class<?>, Boolean> seen = new LinkedHashMap<>();
    //tracks enemy kills, trap activations, plant tramples, or just sets to 1 for seen on allies
    private final LinkedHashMap<Class<?>, Integer> encounterCount = new LinkedHashMap<>();

    //should only be used when initializing
    private void addEntities(Class<?>... classes) {
        for (Class<?> cls : classes) {
            seen.put(cls, false);
            encounterCount.put(cls, 0);
        }
    }

    public Collection<Class<?>> entities() {
        return seen.keySet();
    }

    public String title() {
        return Messages.get(this, name() + ".title");
    }

    public int totalEntities() {
        return seen.size();
    }

    public int totalSeen() {
        int seenTotal = 0;
        for (boolean entitySeen : seen.values()) {
            if (entitySeen) seenTotal++;
        }
        return seenTotal;
    }

    static {

        REGIONAL.addEntities(Rat.class, Snake.class, Gnoll.class, Swarm.class, Crab.class, Slime.class,
                Bcom.class, Bcomg.class, Skeleton.class, Thief.class, DM100.class, Guard.class, Necromancer.class,
                Bat.class, Brute.class, Shaman.BlueShaman.class, Shaman.RedShaman.class, Shaman.PurpleShaman.class, Spinner.class, DM200.class,
                Ghoul.class, Elemental.FireElemental.class, Elemental.FrostElemental.class, Elemental.ShockElemental.class, Warlock.class, Monk.class, Golem.class,
                RipperDemon.class, DemonSpawner.class, Succubus.class, Eye.class, Scorpio.class, Willsonmob.class, Supression.class, Medic.class, Tank.class, Researcher.class, Soldier.class);

        BOSSES.addEntities(Goo.class,
                Tengu.class,
                DM300.class, Pylon.class, Kawasiri.class,
                DwarfKing.class,
                YogDzewa.Larva.class, YogFist.BrightFist.class, YogDzewa.class, Beast.class, SWAT.class, WO.class, Rebel.class);

        UNIVERSAL.addEntities(Wraith.class, Piranha.class, Mimic.class, GoldenMimic.class, Statue.class, GuardianTrap.Guardian.class, Enemytonio.class, SentryRoom.Sentry.class, EbonyMimic.class);

        RARE.addEntities(Albino.class, GnollExile.class, HermitCrab.class, CausticSlime.class,
                Bandit.class, SpectralNecromancer.class, Marilin.class,
                ArmoredBrute.class, DM201.class, Kars.class,
                Elemental.ChaosElemental.class, Senior.class, Enrico.class,
                Acidic.class, Greenbaby.class, Teq.class,
                TormentedSpirit.class, PhantomPiranha.class, CrystalMimic.class, ArmoredStatue.class, Boytwo.class, Stower.class, Mandom.class, Cat.class);

        CASTLE.addEntities(Zombie.class, Zombiedog.class, Zombied.class, Zombiez.class, Zombiep.Zombiep1.class, Zombiep.Zombiep2.class, Zombiep.Zombiep3.class, Zombiep.Zombiep4.class, Zombiet.class, Zombiebr.class, Diobrando.class);

        TENDENCY.addEntities(ZombieTwo.class, ZombieThree.class, ZombieBrute.class, ZombieBrute2.class, Tboss.class, ZombieSoldier.class, Niku.class, Abomination.class, VampireTest.class, Santana.class,
                Vampire.class, ZombieBrute3.class, Vampire2.class, ZombieTank.class, WamuuFirst.class, VampireSoldier.class, VampireSoldier2.class, Abomination2.class, VampireSoldier3.class, Esidisi.class, TendencyShopkeeper.class, GSoldier.class, SpwSoldier.class, Sturo.class, TendencyTank.class, CaesarZeppeli.class,
                Wired.class, VampireHorse.class, VampireSoldierNew.class, VampireChariot.class, Wamuu.class, KS1.class, KS2.class, KS3.class, KS4.class, KarsLight.class);

        QUEST.addEntities(FetidRat.class, GnollTrickster.class, GreatCrab.class, Manhatan2.class,
                Elemental.NewbornFireElemental.class, RotLasher.class, RotHeart.class,
                CrystalWisp.class, CrystalGuardian.class, CrystalSpire.class, GnollGeomancer.class, GnollSapper.class, GnollGuard.class,
                Civil.class, Bmore.class, Diego.class, Pucci12.class, Diego21.class, Diego12.class, ZombieFour.class, ZombietBoss.class,
                TempleLastLevel.TempleBrute.class, TempleLastLevel.TempleGuard.class, Keichomob.class);

        NEUTRAL.addEntities(Ghost.class, RatKing.class, Shopkeeper.class, Wandmaker.class, Blacksmith.class, Imp.class, DArby.class, Willson.class, Sheep.class, Bee.class, Amblance.class, Heavyw.class, Rohan.class, Yukako.class, Retonio.class, Yasu.class, Weather.class, Bdth.class, Annasui.class, So1.class, Polpo.class, Dannynpc.class);

        ALLY.addEntities(MirrorImage.class, PrismaticImage.class,
                DriedRose.GhostHero.class, Willamob.class, Willcmob.class, Willgmob.class,
                TuskBestiary2.class, WandOfWarding.Ward.class, TuskBestiary4.class, WandOfWarding.Ward.WardSentry.class, WandOfLivingEarth.EarthGuardian.class,
                ShadowClone.ShadowAlly.class, SmokeBomb.NinjaLog.class, SpiritHawk.HawkAlly.class, PowerOfMany.LightAlly.class, P1mob.class, P2mob.class, P3mob.class, P4mob.class, P5mob.class, Act1.class, Act2.class, Act3.class, SpeedWagon.class, jojo.class, Jotaro.class);

        TRAP.addEntities(WornDartTrap.class, CreamTrap.class, PoisonDartTrap.class, DisintegrationTrap.class, GatewayTrap.class,
                ChillingTrap.class, BurningTrap.class, ShockingTrap.class, AlarmTrap.class, GrippingTrap.class, TeleportationTrap.class, OozeTrap.class,
                FrostTrap.class, BlazingTrap.class, StormTrap.class, GuardianTrap.class, FlashingTrap.class, WarpingTrap.class,
                ConfusionTrap.class, ToxicTrap.class, CorrosionTrap.class,
                FlockTrap.class, SummoningTrap.class, WeakeningTrap.class, CursingTrap.class,
                GeyserTrap.class, ExplosiveTrap.class, RockfallTrap.class, PitfallTrap.class,
                DistortionTrap.class, DisarmingTrap.class, GrimTrap.class, WiredTrap.class, FancakeTrap.class, DoobieTrap.class, MachineTrap.class);

        PLANT.addEntities(Rotberry.class, Sungrass.class, Fadeleaf.class, Icecap.class,
                Firebloom.class, Sorrowmoss.class, Swiftthistle.class, Blindweed.class,
                Stormvine.class, Earthroot.class, Mageroyal.class, Starflower.class,
                BlandfruitBush.class,
                WandOfRegrowth.Dewcatcher.class, WandOfRegrowth.Seedpod.class, WandOfRegrowth.Lotus.class);

    }

    //some mobs and traps have different internal classes in some cases, so need to convert here
    private static final HashMap<Class<?>, Class<?>> classConversions = new HashMap<>();

    static {

        classConversions.put(CorpseDust.DustWraith.class, Wraith.class);
        classConversions.put(Rohan2.class, Rohan.class);
        classConversions.put(Rohan3.class, Rohan.class);
        classConversions.put(DestOrb.class, RotLasher.class);

        classConversions.put(Zombie2.class, Zombie.class);
        classConversions.put(Zombiedog2.class, Zombiedog.class);
        classConversions.put(Zombied2.class, Zombied.class);
        classConversions.put(Zombiez2.class, Zombiez.class);
        classConversions.put(Zombie2p.Zombiep1.class, Zombiep.Zombiep1.class);
        classConversions.put(Zombie2p.Zombiep2.class, Zombiep.Zombiep2.class);
        classConversions.put(Zombie2p.Zombiep3.class, Zombiep.Zombiep3.class);
        classConversions.put(Zombie2p.Zombiep4.class, Zombiep.Zombiep4.class);
        classConversions.put(Zombiet2.class, Zombiet.class);
        classConversions.put(Dio2brando.class, Diobrando.class);
        classConversions.put(Diego2.class, Diego.class);
        classConversions.put(Speedwagon2.class, SpeedWagon.class);

        classConversions.put(Necromancer.NecroSkeleton.class, Skeleton.class);

        classConversions.put(TenguDartTrap.class, PoisonDartTrap.class);
        classConversions.put(GnollRockfallTrap.class, RockfallTrap.class);

        classConversions.put(DwarfKing.DKGhoul.class, Ghoul.class);
        classConversions.put(DwarfKing.DKWarlock.class, Warlock.class);
        classConversions.put(DwarfKing.DKMonk.class, Monk.class);
        classConversions.put(DwarfKing.DKGolem.class, Golem.class);

        classConversions.put(YogFist.BurningFist.class, YogFist.BrightFist.class);
        classConversions.put(YogFist.SoiledFist.class, YogFist.BrightFist.class);
        classConversions.put(YogFist.RottingFist.class, YogFist.BrightFist.class);
        classConversions.put(YogFist.RustedFist.class, YogFist.BrightFist.class);
        classConversions.put(YogFist.DarkFist.class, YogFist.BrightFist.class);

        classConversions.put(YogDzewa.YogRipper.class, RipperDemon.class);
        classConversions.put(YogDzewa.YogEye.class, Eye.class);
        classConversions.put(YogDzewa.YogScorpio.class, Scorpio.class);
    }

    public static boolean isSeen(Class<?> cls) {
        for (Bestiary cat : values()) {
            if (cat.seen.containsKey(cls)) {
                return cat.seen.get(cls);
            }
        }
        return false;
    }

    public static void setSeen(Class<?> cls) {
        if (classConversions.containsKey(cls)) {
            cls = classConversions.get(cls);
        }
        for (Bestiary cat : values()) {
            if (cat.seen.containsKey(cls) && !cat.seen.get(cls)) {
                cat.seen.put(cls, true);
                Journal.saveNeeded = true;
            }
        }
        Badges.validateCatalogBadges();
    }

    public static int encounterCount(Class<?> cls) {
        for (Bestiary cat : values()) {
            if (cat.encounterCount.containsKey(cls)) {
                return cat.encounterCount.get(cls);
            }
        }
        return 0;
    }

    //used primarily when bosses are killed and need to clean up their minions
    public static boolean skipCountingEncounters = false;

    public static void countEncounter(Class<?> cls) {
        countEncounters(cls, 1);
    }

    public static void countEncounters(Class<?> cls, int encounters) {
        if (skipCountingEncounters) {
            return;
        }
        if (classConversions.containsKey(cls)) {
            cls = classConversions.get(cls);
        }
        for (Bestiary cat : values()) {
            if (cat.encounterCount.containsKey(cls) && cat.encounterCount.get(cls) != Integer.MAX_VALUE) {
                cat.encounterCount.put(cls, cat.encounterCount.get(cls) + encounters);
                if (cat.encounterCount.get(cls) < -1_000_000_000) { //to catch cases of overflow
                    cat.encounterCount.put(cls, Integer.MAX_VALUE);
                }
                Journal.saveNeeded = true;
            }
        }
    }

    private static final String BESTIARY_CLASSES = "bestiary_classes";
    private static final String BESTIARY_SEEN = "bestiary_seen";
    private static final String BESTIARY_ENCOUNTERS = "bestiary_encounters";

    public static void store(Bundle bundle) {

        ArrayList<Class<?>> classes = new ArrayList<>();
        ArrayList<Boolean> seen = new ArrayList<>();
        ArrayList<Integer> encounters = new ArrayList<>();

        for (Bestiary cat : values()) {
            for (Class<?> entity : cat.entities()) {
                if (cat.seen.get(entity) || cat.encounterCount.get(entity) > 0) {
                    classes.add(entity);
                    seen.add(cat.seen.get(entity));
                    encounters.add(cat.encounterCount.get(entity));
                }
            }
        }

        Class<?>[] storeCls = new Class[classes.size()];
        boolean[] storeSeen = new boolean[seen.size()];
        int[] storeEncounters = new int[encounters.size()];

        for (int i = 0; i < storeCls.length; i++) {
            storeCls[i] = classes.get(i);
            storeSeen[i] = seen.get(i);
            storeEncounters[i] = encounters.get(i);
        }

        bundle.put(BESTIARY_CLASSES, storeCls);
        bundle.put(BESTIARY_SEEN, storeSeen);
        bundle.put(BESTIARY_ENCOUNTERS, storeEncounters);

    }

    public static void restore(Bundle bundle) {

        if (bundle.contains(BESTIARY_CLASSES)
                && bundle.contains(BESTIARY_SEEN)
                && bundle.contains(BESTIARY_ENCOUNTERS)) {
            Class<?>[] classes = bundle.getClassArray(BESTIARY_CLASSES);
            boolean[] seen = bundle.getBooleanArray(BESTIARY_SEEN);
            int[] encounters = bundle.getIntArray(BESTIARY_ENCOUNTERS);

            for (int i = 0; i < classes.length; i++) {
                for (Bestiary cat : values()) {
                    if (cat.seen.containsKey(classes[i])) {
                        cat.seen.put(classes[i], seen[i]);
                        cat.encounterCount.put(classes[i], encounters[i]);
                    }
                }
            }
        }

    }

}
