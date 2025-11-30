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

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.tendencylevel;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.DArby;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Retonio;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Utest;
import com.shatteredpixel.shatteredpixeldungeon.items.trinkets.RatSkull;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Arrays;

public class MobSpawner extends Actor {
    {
        actPriority = BUFF_PRIO; //as if it were a buff.
    }

    @Override
    protected boolean act() {

        if (Dungeon.level.mobCount() < Dungeon.level.mobLimit()) {

            if (Dungeon.level.spawnMob(12)) {
                spend(Dungeon.level.respawnCooldown());
            } else {
                //try again in 1 turn
                spend(TICK);
            }

        } else {
            spend(Dungeon.level.respawnCooldown());
        }

        return true;
    }

    public void resetCooldown() {
        spend(-cooldown());
        spend(Dungeon.level.respawnCooldown());
    }

    public static ArrayList<Class<? extends Mob>> getMobRotation(int depth) {
        ArrayList<Class<? extends Mob>> mobs = standardMobRotation(depth);
        addRareMobs(depth, mobs);
        swapMobAlts(mobs);
        Random.shuffle(mobs);
        return mobs;
    }

    //returns a rotation of standard mobs, unshuffled.
    private static ArrayList<Class<? extends Mob>> standardMobRotation(int depth) {
        // Tendency 모드일 때 모든 층에서 좀비 계열 몹들만 스폰
        if (tendencylevel) {
            return getZombieRotation(depth);
        }

        switch (depth) {

            // Sewers
            case 1:
            default:
                //3x rat, 1x snake
                if (Dungeon.diolevel) {
                    return new ArrayList<>(Arrays.asList(
                            Zombie.class, Zombie.class, Zombie.class,
                            Zombiedog.class));
                } else {
                    return new ArrayList<>(Arrays.asList(
                            Rat.class, Rat.class, Rat.class,
                            Snake.class));
                }
            case 2:
                //2x rat, 1x snake, 2x gnoll
                if (Dungeon.diolevel) {
                    return new ArrayList<>(Arrays.asList(Zombie.class, Zombie.class,
                            Zombiedog.class,
                            Zombied.class, Zombied.class));
                } else {
                    return new ArrayList<>(Arrays.asList(Rat.class, Rat.class,
                            Snake.class,
                            Gnoll.class, Gnoll.class));
                }
            case 3:
                //1x rat, 1x snake, 3x gnoll, 1x swarm, 1x crab
                if (Dungeon.diolevel) {
                    return new ArrayList<>(Arrays.asList(Zombie.class,
                            Zombiedog.class,
                            Zombied.class, Zombied.class, Zombied.class,
                            Zombiez.class,
                            Zombiep.random()));
                } else if (Statistics.spw6 > 0) {
                    return new ArrayList<>(Arrays.asList(Manhatan.class, Manhatan.class,
                            Manhatan.class,
                            Manhatan.class, Manhatan.class));
                } else {
                    return new ArrayList<>(Arrays.asList(Rat.class,
                            Snake.class,
                            Gnoll.class, Gnoll.class, Gnoll.class,
                            Swarm.class,
                            Crab.class));
                }
            case 4:
            case 5:
                //1x gnoll, 1x swarm, 2x crab, 2x slime
                if (Dungeon.diolevel) {
                    return new ArrayList<>(Arrays.asList(Zombied.class,
                            Zombiez.class,
                            Zombiep.random(), Zombiep.random(),
                            Zombiet.class, Zombiet.class));
                } else if (Statistics.spw6 > 0) {
                    return new ArrayList<>(Arrays.asList(Manhatan.class, Manhatan.class,
                            Manhatan.class,
                            Manhatan.class, Manhatan.class));
                } else {
                    return new ArrayList<>(Arrays.asList(Gnoll.class,
                            Swarm.class,
                            Crab.class, Crab.class,
                            Slime.class, Slime.class));
                }

                // Prison
            case 6:
                //3x skeleton, 1x thief, 1x swarm
                return new ArrayList<>(Arrays.asList(Bcom.class, Bcom.class, Bcom.class,
                        Thief.class,
                        Bcomg.class));
            case 7:
                //3x skeleton, 1x thief, 1x DM-100, 1x guard
                return new ArrayList<>(Arrays.asList(Skeleton.class, Skeleton.class, Skeleton.class,
                        Thief.class,
                        DM100.class,
                        Guard.class));
            case 8:
                //2x skeleton, 1x thief, 2x DM-100, 2x guard, 1x necromancer
                return new ArrayList<>(Arrays.asList(Skeleton.class, Skeleton.class,
                        Thief.class,
                        DM100.class, DM100.class,
                        Guard.class, Guard.class,
                        Necromancer.class));
            case 9:
            case 10:
                //1x skeleton, 1x thief, 2x DM-100, 2x guard, 2x necromancer
                return new ArrayList<>(Arrays.asList(Skeleton.class,
                        Thief.class,
                        DM100.class, DM100.class,
                        Guard.class, Guard.class,
                        Necromancer.class, Necromancer.class));

            // Caves
            case 11:
                //3x bat, 1x brute, 1x shaman
                return new ArrayList<>(Arrays.asList(
                        Bat.class, Bat.class, Bat.class,
                        Brute.class,
                        Shaman.random()));
            case 12:
                //2x bat, 2x brute, 1x shaman, 1x spinner
                return new ArrayList<>(Arrays.asList(
                        Bat.class, Bat.class,
                        Brute.class, Brute.class,
                        Shaman.random(),
                        Spinner.class));
            case 13:
                //1x bat, 2x brute, 2x shaman, 2x spinner, 1x DM-200
                return new ArrayList<>(Arrays.asList(
                        Bat.class,
                        Brute.class, Brute.class,
                        Shaman.random(), Shaman.random(),
                        Spinner.class, Spinner.class,
                        DM200.class));
            case 14:
            case 15:
                //1x bat, 1x brute, 2x shaman, 2x spinner, 2x DM-300
                return new ArrayList<>(Arrays.asList(
                        Bat.class,
                        Brute.class,
                        Shaman.random(), Shaman.random(),
                        Spinner.class, Spinner.class,
                        DM200.class, DM200.class));

            // City
            case 16:
                //3x ghoul, 1x elemental, 1x warlock
                return new ArrayList<>(Arrays.asList(
                        Ghoul.class, Ghoul.class, Ghoul.class,
                        Elemental.random(),
                        Warlock.class));
            case 17:
                //1x ghoul, 2x elemental, 1x warlock, 1x monk
                return new ArrayList<>(Arrays.asList(
                        Ghoul.class,
                        Elemental.random(), Elemental.random(),
                        Warlock.class,
                        Monk.class));
            case 18:
                //1x ghoul, 1x elemental, 2x warlock, 2x monk, 1x golem
                return new ArrayList<>(Arrays.asList(
                        Ghoul.class,
                        Elemental.random(),
                        Warlock.class, Warlock.class,
                        Monk.class, Monk.class,
                        Golem.class));
            case 19:
            case 20:
                //1x elemental, 2x warlock, 2x monk, 3x golem
                return new ArrayList<>(Arrays.asList(
                        Elemental.random(),
                        Warlock.class, Warlock.class,
                        Monk.class, Monk.class,
                        Golem.class, Golem.class, Golem.class));

            // Halls
            case 21:
                //2x succubus, 1x evil eye
                return new ArrayList<>(Arrays.asList(
                        Succubus.class, Succubus.class,
                        Eye.class));
            case 22:
                //1x succubus, 1x evil eye
                if (Dungeon.branch == 1) {
                    return new ArrayList<>(Arrays.asList(Zombie2.class,
                            Zombiedog2.class, Zombied2.class,
                            Zombiez2.class, Zombie2p.random(), Zombiet2.class));
                } else {
                    return new ArrayList<>(Arrays.asList(
                            Succubus.class,
                            Eye.class));
                }
            case 23:
                //1x succubus, 2x evil eye, 1x scorpio
                if (Dungeon.branch == 1) {
                    return new ArrayList<>(Arrays.asList(Zombie2.class,
                            Zombiedog2.class, Zombied2.class,
                            Zombiez2.class, Zombie2p.random(), Zombiet2.class));
                } else {
                    return new ArrayList<>(Arrays.asList(
                            Succubus.class,
                            Eye.class, Eye.class,
                            Scorpio.class));
                }
            case 24:
            case 25:
                //1x succubus, 2x evil eye, 3x scorpio,1x acidic
                if (Dungeon.branch == 1) {
                    return new ArrayList<>(Arrays.asList(Zombie2.class,
                            Zombiedog2.class, Zombied2.class,
                            Zombiez2.class, Zombie2p.random(), Zombiet2.class));
                } else {
                    return new ArrayList<>(Arrays.asList(
                            Succubus.class,
                            Eye.class, Eye.class,
                            Scorpio.class, Scorpio.class, Scorpio.class));
                }

                // Labs
            case 26:
                // 3x soldier, 1x researcher
                return new ArrayList<>(Arrays.asList(
                        Soldier.class, Soldier.class, Soldier.class,
                        Researcher.class));
            case 27:
                // 2x Soldier, 2x researcher, 1x supression
                return new ArrayList<>(Arrays.asList(
                        Soldier.class, Soldier.class,
                        Researcher.class, Medic.class,
                        Supression.class));
            case 28:
                // 2x soldier, 1x researcher, 1x supression, 1x tank, 1x medic
                return new ArrayList<>(Arrays.asList(
                        Soldier.class, Soldier.class,
                        Researcher.class,
                        Supression.class,
                        Tank.class,
                        Medic.class));
            case 29:
            case 30:
            case 31:
                // 1x soldier, 2x researcher, 2x supression, 1x tank, 2x medic
                // 1x soldier, 2x researcher, 2x supression, 1x tank, 2x medic, 1x swat
                return new ArrayList<>(Arrays.asList(
                        Soldier.class,
                        Researcher.class, Researcher.class,
                        Supression.class, Supression.class,
                        Tank.class,
                        Medic.class, Medic.class));
        }

    }

    /**
     * Tendency 모드일 때 사용할 좀비 계열 몹들의 로테이션을 반환
     */
    private static ArrayList<Class<? extends Mob>> getZombieRotation(int depth) {
        switch (depth) {
            // Sewers (1-5층)
            case 1:
            default:
                return new ArrayList<>(Arrays.asList(
                        ZombieTwo.class, ZombieTwo.class));
            case 2:
                return new ArrayList<>(Arrays.asList(
                        ZombieTwo.class, ZombieTwo.class, ZombieTwo.class, ZombieThree.class,
                        ZombieThree.class));
            case 3:
                return new ArrayList<>(Arrays.asList(
                        ZombieTwo.class, ZombieTwo.class, Zombie.class, ZombieThree.class,
                        ZombieBrute.class));
            case 4:
                return new ArrayList<>(Arrays.asList(
                        Zombie.class, ZombieThree.class, ZombieThree.class, ZombieBrute.class,
                        ZombieBrute.class));
            case 5:
                return new ArrayList<>(Arrays.asList(
                        Zombie.class, ZombieThree.class, ZombieThree.class, ZombieBrute.class,
                        ZombieBrute2.class));
            case 6:
                return new ArrayList<>(Arrays.asList(
                        Zombie.class, ZombieThree.class, ZombieBrute.class, ZombieBrute2.class,
                        ZombieBrute2.class));
            case 7:
                return new ArrayList<>(Arrays.asList(
                        ZombieThree.class, ZombieThree.class,  ZombieBrute.class, ZombieBrute.class,
                        ZombieBrute2.class, ZombieBrute2.class));
            case 8:
            case 9:
                return new ArrayList<>(Arrays.asList(
                        Zombie.class, ZombieBrute.class, ZombieBrute.class, ZombieBrute2.class,
                        ZombieBrute2.class));
            case 10:
                return new ArrayList<>(Arrays.asList(
                        ZombieSoldier.class));
            case 11:
                return new ArrayList<>(Arrays.asList(
                        Niku.class));

            case 12:
                return new ArrayList<>(Arrays.asList(
                        ZombieSoldier.class, Niku.class));
            case 13:
                return new ArrayList<>(Arrays.asList(
                        ZombieSoldier.class, ZombieSoldier.class,
                        Niku.class, Niku.class, Niku.class,
                        Abomination.class));
            case 14:
                return new ArrayList<>(Arrays.asList(
                        ZombieSoldier.class, ZombieSoldier.class,
                        Niku.class, Niku.class, Abomination.class,
                        Abomination.class));
            case 15:
                return new ArrayList<>(Arrays.asList(
                        ZombieSoldier.class, ZombieSoldier.class,
                        Niku.class, Niku.class, Abomination.class,
                        VampireTest.class));
            case 16:
                return new ArrayList<>(Arrays.asList(
                        ZombieSoldier.class, ZombieSoldier.class,
                        Niku.class, VampireTest.class, Abomination.class,
                        VampireTest.class));
            case 17:
                return new ArrayList<>(Arrays.asList(
                        ZombieSoldier.class, VampireTest.class,
                        Niku.class, VampireTest.class, Abomination.class,
                        VampireTest.class));
            case 18:
                return new ArrayList<>(Arrays.asList(
                        ZombieSoldier.class, VampireTest.class,
                        Niku.class, VampireTest.class, VampireTest.class,
                        VampireTest.class));
            case 19:
                return new ArrayList<>(Arrays.asList(
                        Vampire.class));

            case 20:
                return new ArrayList<>(Arrays.asList(
                        Vampire.class, Vampire.class, ZombieBrute3.class));

            case 21:
                return new ArrayList<>(Arrays.asList(
                        Vampire.class, ZombieBrute3.class));
            case 22:
                return new ArrayList<>(Arrays.asList(
                        ZombieBrute3.class, ZombieBrute3.class,
                        Vampire.class, Vampire.class, Vampire.class,
                        Vampire2.class));

            case 23:
                return new ArrayList<>(Arrays.asList(
                        ZombieBrute3.class, ZombieBrute3.class,
                        Vampire2.class, Vampire.class, Vampire2.class,
                        Vampire2.class));

            case 24:
                return new ArrayList<>(Arrays.asList(
                        ZombieBrute3.class, ZombieBrute3.class,
                        Vampire.class, Vampire.class, Vampire2.class,
                        ZombieTank.class));

            case 25:
                return new ArrayList<>(Arrays.asList(
                        ZombieBrute3.class, ZombieBrute3.class,
                        Vampire.class, ZombieTank.class, Vampire2.class,
                        ZombieTank.class));

            case 26:
                return new ArrayList<>(Arrays.asList(
                        ZombieBrute3.class, Vampire.class,
                        Vampire2.class, ZombieTank.class, ZombieTank.class,
                        ZombieTank.class));
            case 27:
                return new ArrayList<>(Arrays.asList(
                        Zombie2.class, Zombie2.class, Zombie2.class,
                        Zombiedog2.class, Zombied2.class));

            case 28:
                return new ArrayList<>(Arrays.asList(
                        VampireSoldier.class));

            case 29:
                return new ArrayList<>(Arrays.asList(
                        VampireSoldier.class, VampireSoldier.class, VampireSoldier.class, VampireSoldier2.class));

            case 30:
                return new ArrayList<>(Arrays.asList(
                        VampireSoldier.class, VampireSoldier.class, VampireSoldier2.class));
            case 31:
                return new ArrayList<>(Arrays.asList(
                        VampireSoldier2.class, VampireSoldier2.class,
                        VampireSoldier.class, VampireSoldier.class,
                        Abomination2.class));

            case 32:
                return new ArrayList<>(Arrays.asList(
                        VampireSoldier2.class, VampireSoldier2.class,
                        Abomination2.class, VampireSoldier.class, Abomination2.class,
                        Abomination2.class));

            case 33:
                return new ArrayList<>(Arrays.asList(
                        VampireSoldier2.class, VampireSoldier2.class,
                        VampireSoldier.class, VampireSoldier.class, Abomination2.class,
                        VampireSoldier3.class));

            case 34:
                return new ArrayList<>(Arrays.asList(
                        VampireSoldier2.class, VampireSoldier2.class,
                        VampireSoldier.class, VampireSoldier3.class, Abomination2.class,
                        VampireSoldier3.class));
            case 35:
                return new ArrayList<>(Arrays.asList(
                        VampireSoldier2.class, VampireSoldier2.class,
                        VampireSoldier3.class, VampireSoldier3.class, Abomination2.class,
                        VampireSoldier3.class));
            case 37:
                return new ArrayList<>(Arrays.asList(Wired.class));
            case 38:
                return new ArrayList<>(Arrays.asList(VampireHorse.class));
            case 39:
                return new ArrayList<>(Arrays.asList(Wired.class, VampireHorse.class));
            case 40:
                return new ArrayList<>(Arrays.asList(
                        Wired.class, Wired.class,
                        VampireHorse.class, VampireHorse.class,
                        VampireSoldierNew.class));
            case 41:
                return new ArrayList<>(Arrays.asList(
                        Wired.class, Wired.class,
                        VampireSoldierNew.class, VampireHorse.class, VampireHorse.class,
                        VampireSoldierNew.class));
            case 42:
                return new ArrayList<>(Arrays.asList(
                        Wired.class, VampireHorse.class,
                        VampireSoldierNew.class, VampireSoldierNew.class, VampireHorse.class,
                        VampireChariot.class));
            case 43:
                return new ArrayList<>(Arrays.asList(
                        VampireSoldierNew.class, VampireSoldierNew.class, VampireSoldierNew.class,
                        VampireChariot.class, VampireChariot.class));
            case 44:
                return new ArrayList<>(Arrays.asList(VampireChariot.class));
            case 45:
                return new ArrayList<>(Arrays.asList(VampireChariot.class));
            case 46:
                return new ArrayList<>(Arrays.asList(KS1.class, KS2.class));
            case 47:
                return new ArrayList<>(Arrays.asList(KS1.class, KS2.class));
            case 48:
                return new ArrayList<>(Arrays.asList(KS1.class, KS2.class, KS1.class, KS2.class, KS3.class));
            case 49:
                return new ArrayList<>(Arrays.asList(KS1.class, KS2.class, KS3.class));
            case 50:
                return new ArrayList<>(Arrays.asList(KS1.class, KS2.class, KS3.class, KS4.class));
            case 51:
                return new ArrayList<>(Arrays.asList(KS1.class, KS2.class, KS4.class, KS4.class));
            case 52:
                return new ArrayList<>(Arrays.asList(KS3.class, KS4.class));
            case 53:
                return new ArrayList<>(Arrays.asList(KS4.class));
            case 54:
                return new ArrayList<>(Arrays.asList(KS4.class));
        }
    }

    //has a chance to add a rarely spawned mobs to the rotation
    public static void addRareMobs(int depth, ArrayList<Class<? extends Mob>> rotation) {
        if (Statistics.diocount == 0 && !(tendencylevel)) {
            switch (depth) {

                // Sewers
                default:
                    return;
                case 4:
                    if (Random.Float() < 0.025f) rotation.add(Thief.class);
                    return;

                // Prison

                case 7:
                    if (Random.Float() < 0.035f) rotation.add(Stower.class);
                    return;
                case 8:
                    if (Random.Float() < 0.025f) rotation.add(Stower.class);
                    return;
                case 9:
                    if (Random.Float() < 0.025f) rotation.add(Bat.class);
                    return;

                // Caves

                case 14:
                    if (Random.Float() < 0.04f) rotation.add(Boytwo.class);
                    if (Random.Float() < 0.025f) rotation.add(Ghoul.class);
                    return;

                // City

                case 17:
                    if (Random.Float() < 0.04f) rotation.add(Boytwo.class);
                    if (Random.Float() < 0.025f) rotation.add(Mandom.class);
                    return;
                case 19:
                    if (Random.Float() < 0.04f) rotation.add(Boytwo.class);
                    if (Random.Float() < 0.025f) rotation.add(Succubus.class);
                    if (Random.Float() < 0.02f) rotation.add(Retonio.class);
                    return;
                case 21:
                    if (Random.Float() < 0.029f) rotation.add(Mandom.class);
                    if (Random.Float() < 0.02f) rotation.add(Retonio.class);
                    return;
                case 23:
                    if (Random.Float() < 0.03f) rotation.add(Cat.class);
                    if (Random.Float() < 0.031f) rotation.add(Mandom.class);
                    return;
                case 24:
                    if (Random.Float() < 0.03f) rotation.add(Cat.class);
                    return;
                case 27:
                    if (Random.Float() < 0.03f) rotation.add(Cat.class);
                    if (Random.Float() < 0.04f) rotation.add(Retonio.class);
                    return;
                case 28:
                    if (Random.Float() < 0.03f) rotation.add(Cat.class);
                    return;
            }
        }
    }

    //switches out regular mobs for their alt versions when appropriate
    private static void swapMobAlts(ArrayList<Class<? extends Mob>> rotation) {
        float altChance = 1 / 50f * RatSkull.exoticChanceMultiplier();
        for (int i = 0; i < rotation.size(); i++) {
            if (Random.Float() < altChance) {
                Class<? extends Mob> cl = rotation.get(i);
                if (cl == Rat.class) cl = Albino.class;
                else if (cl == Gnoll.class) cl = GnollExile.class;
                else if (cl == Crab.class) cl = HermitCrab.class;
                else if (cl == Slime.class) cl = CausticSlime.class;
                else if (cl == Thief.class) cl = Bandit.class;
                else if (cl == Necromancer.class) cl = SpectralNecromancer.class;
                else if (cl == Brute.class) cl = ArmoredBrute.class;
                else if (cl == DM200.class) cl = DM201.class;
                else if (cl == Monk.class) cl = Senior.class;
                else if (cl == Scorpio.class) cl = Acidic.class;
                else if (cl == Soldier.class) cl = Teq.class;
                rotation.set(i, cl);
            }
        }
    }
}
