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

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Belongings;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.TendencyShopkeeper;
import com.shatteredpixel.shatteredpixeldungeon.items.Dewdrop;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.Ram2;
import com.shatteredpixel.shatteredpixeldungeon.items.Stylus;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.ChaliceOfBlood;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.EtherealChains;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.SandalsOfNature;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.WoundsofWar;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.MagicalHolster;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.PotionBandolier;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.ScrollHolder;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.VelvetPouch;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Food;
import com.shatteredpixel.shatteredpixeldungeon.items.food.FrozenCarpaccio;
import com.shatteredpixel.shatteredpixeldungeon.items.food.MeatPie;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Pasty;
import com.shatteredpixel.shatteredpixeldungeon.items.food.PhantomMeat;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfExperience;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfStrength;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfAquaticRejuvenation;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfArcaneArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfDragonsBlood;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfHoneyedHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfIcyTouch;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfToxicEssence;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfWeaponEnhance;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfWeaponUpgrade;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfCleansing;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfCorrosiveGas;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfDivineInspiration;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfDragonsBreath;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfEarthenArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfMagicalSight;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfShielding;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfShroudingFog;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfSnapFreeze;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfStamina;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfStormClouds;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Spw;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Spw10;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Spw11;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.UV;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRemoveCurse;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfAntiMagic;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfChallenge;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfDivination;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfDread;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfForesight;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfMetamorphosis;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfMysticalEnergy;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfPrismaticImage;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfPsionicBlast;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfSirensSong;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.AdvancedEvolution;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.CurseInfusion;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Highway;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Kinga;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Kingc;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Kingm;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Kings;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Kingt;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Kingw;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.MagicalInfusion;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.PhaseShift;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.ScrollOfExtract;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.TelekineticGrab;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.UnstableSpell;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.WildEnergy;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfAdvanceguard;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfAugmentation;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfEnchantment;
import com.shatteredpixel.shatteredpixeldungeon.items.trinkets.ChaoticCenser;
import com.shatteredpixel.shatteredpixeldungeon.items.trinkets.EyeOfNewt;
import com.shatteredpixel.shatteredpixeldungeon.items.trinkets.FerretTuft;
import com.shatteredpixel.shatteredpixeldungeon.items.trinkets.PetrifiedSeed;
import com.shatteredpixel.shatteredpixeldungeon.items.trinkets.ThirteenLeafClover;
import com.shatteredpixel.shatteredpixeldungeon.items.trinkets.VialOfBlood;
import com.shatteredpixel.shatteredpixeldungeon.items.trinkets.WondrousResin;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.HealingDart;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.TippedDart;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashMap;

public class TendencyShopRoom extends SpecialRoom {

    protected ArrayList<Item> itemsToSpawn;

    @Override
    public int minWidth() {
        return Math.max(6, (int) Math.ceil(Math.sqrt(spacesNeeded())));
    }

    @Override
    public int minHeight() {
        return Math.max(6, (int) Math.ceil(Math.sqrt(spacesNeeded())));
    }

    public int spacesNeeded() {
        if (itemsToSpawn == null) {
            itemsToSpawn = generateItemsGauntlet();
        }
        //sandbags spawn based on current level of an hourglass the player may be holding
        // so, to avoid rare cases of min sizes differing based on that, we ignore all sandbags
        // and then add 4 items in all cases, which is max number of sandbags that can be in the shop
        int spacesNeeded = itemsToSpawn.size();
        for (Item i : itemsToSpawn) {
            if (i instanceof TimekeepersHourglass.sandBag) {
                spacesNeeded--;
            }
        }
        spacesNeeded += 2;

        //we also add 1 more space, for the shopkeeper
        spacesNeeded++;
        return spacesNeeded;
    }

    public void paint(Level level) {

        Painter.fill(level, this, Terrain.WALL);
        Painter.fill(level, this, 1, Terrain.EMPTY_SP);

        placeShopkeeper(level);

        placeItems(level);

        for (Door door : connected.values()) {
            door.set(Door.Type.REGULAR);
        }

    }

    protected void placeShopkeeper(Level level) {

        int pos = level.pointToCell(center());

        Mob shopkeeper = new TendencyShopkeeper();
        shopkeeper.pos = pos;
        level.mobs.add(shopkeeper);

    }

    protected void placeItems(Level level) {

        if (itemsToSpawn == null) {
            itemsToSpawn = generateItemsGauntlet();
        }

        Point entryInset = new Point(entrance());
        if (entryInset.y == top) {
            entryInset.y++;
        } else if (entryInset.y == bottom) {
            entryInset.y--;
        } else if (entryInset.x == left) {
            entryInset.x++;
        } else {
            entryInset.x--;
        }

        Point curItemPlace = entryInset.clone();

        int inset = 1;

        for (Item item : itemsToSpawn.toArray(new Item[0])) {

            //place items in a clockwise pattern
            if (curItemPlace.x == left + inset && curItemPlace.y != top + inset) {
                curItemPlace.y--;
            } else if (curItemPlace.y == top + inset && curItemPlace.x != right - inset) {
                curItemPlace.x++;
            } else if (curItemPlace.x == right - inset && curItemPlace.y != bottom - inset) {
                curItemPlace.y++;
            } else {
                curItemPlace.x--;
            }

            //once we get to the inset from the entrance again, move another cell inward and loop
            if (curItemPlace.equals(entryInset)) {

                if (entryInset.y == top + inset) {
                    entryInset.y++;
                } else if (entryInset.y == bottom - inset) {
                    entryInset.y--;
                }
                if (entryInset.x == left + inset) {
                    entryInset.x++;
                } else if (entryInset.x == right - inset) {
                    entryInset.x--;
                }
                inset++;

                if (inset > (Math.min(width(), height()) - 3) / 2) {
                    break; //out of space!
                }

                curItemPlace = entryInset.clone();

                //make sure to step forward again
                if (curItemPlace.x == left + inset && curItemPlace.y != top + inset) {
                    curItemPlace.y--;
                } else if (curItemPlace.y == top + inset && curItemPlace.x != right - inset) {
                    curItemPlace.x++;
                } else if (curItemPlace.x == right - inset && curItemPlace.y != bottom - inset) {
                    curItemPlace.y++;
                } else {
                    curItemPlace.x--;
                }
            }

            int cell = level.pointToCell(curItemPlace);
            //prevents high grass from being trampled, potentially dropping dew/seeds onto shop items
            if (level.map[cell] == Terrain.HIGH_GRASS) {
                Level.set(cell, Terrain.GRASS, level);
                GameScene.updateMap(cell);
            }
            level.drop(item, cell).type = Heap.Type.FOR_SALE;
            itemsToSpawn.remove(item);
        }

        //we didn't have enough space to place everything neatly, so now just fill in anything left
        if (!itemsToSpawn.isEmpty()) {
            for (Point p : getPoints()) {
                int cell = level.pointToCell(p);
                if ((level.map[cell] == Terrain.EMPTY_SP || level.map[cell] == Terrain.EMPTY)
                        && level.heaps.get(cell) == null && level.findMob(cell) == null) {
                    level.drop(itemsToSpawn.remove(0), level.pointToCell(p)).type = Heap.Type.FOR_SALE;
                }
                if (itemsToSpawn.isEmpty()) {
                    break;
                }
            }
        }

        if (!itemsToSpawn.isEmpty()) {
            ShatteredPixelDungeon.reportException(new RuntimeException("failed to place all items in a shop!"));
        }

    }

    protected static ArrayList<Item> generateItemsGauntlet() {
        ArrayList<Item> itemsToSpawn = new ArrayList<>();

        Class<?>[] specialPotions = {
                // 엘릭서들
                ElixirOfArcaneArmor.class,      // 스탠드 저항 용액
                ElixirOfAquaticRejuvenation.class, // F.F.의 용액
                ElixirOfDragonsBlood.class,     // 괴염왕 용액
                ElixirOfHoneyedHealing.class,   // 폴포의 용액
                ElixirOfIcyTouch.class,         // 기화냉동 용액
                ElixirOfToxicEssence.class,     // 디스토션 용액
                ElixirOfWeaponEnhance.class,    // 복수의 물약
                ElixirOfWeaponUpgrade.class,    // 환각의 물약

                // 특수 물약들
                PotionOfCleansing.class,        // 청정의 물약
                PotionOfCorrosiveGas.class,     // 부식 물약
                PotionOfDragonsBreath.class,    // 불길의 물약
                PotionOfEarthenArmor.class,     // 바위 인간의 물약
                PotionOfDivineInspiration.class, // 죠스타의 물약
                PotionOfMagicalSight.class,     // 천리안의 물약
                PotionOfShielding.class,        // 보호막의 물약
                PotionOfShroudingFog.class,     // 연막 물약
                PotionOfSnapFreeze.class,       // 순간 빙결 물약
                PotionOfStamina.class,          // 지구력의 물약
                PotionOfStormClouds.class       // 폭우 물약
        };
        try {
            Class<?> selectedPotion = Random.element(specialPotions);
            Item pot = (Item) selectedPotion.newInstance();
            itemsToSpawn.add(pot.identify());
        } catch (Exception e) {

        }

        switch (Random.Int(2)) {
            case 0:
                itemsToSpawn.add(Generator.random(Generator.Category.SCROLL).identify());
                break;
            case 1:
                Class<?>[] memoryDiscs = {
                        UnstableSpell.class,            // 변형된 DISC
                        ScrollOfSirensSong.class,       // 사사메 오지로의 기억 DISC
                        ScrollOfAntiMagic.class,        // 푸치신부의 기억 DISC
                        ScrollOfChallenge.class,        // 엠포리오의 기억 DISC
                        ScrollOfDivination.class,       // 히로세 야스호의 기억 DISC
                        ScrollOfDread.class,            // 미야모토 테루노스케의 기억 DISC
                        ScrollOfForesight.class,        // 도피오의 기억 DISC
                        ScrollOfMetamorphosis.class,    // 츠지 아야의 기억 DISC
                        ScrollOfMysticalEnergy.class,   // 포코로코의 기억 DISC
                        ScrollOfPrismaticImage.class,   // 퍼니 발렌타인의 기억 DISC
                        ScrollOfPsionicBlast.class      // 카와지리 코사쿠의 기억 DISC
                };
                try {
                    Class<?> selectedDisc = Random.element(memoryDiscs);
                    Item disc = (Item) selectedDisc.newInstance();
                    itemsToSpawn.add(disc.identify());
                } catch (Exception e) {

                }
                break;
        }

        itemsToSpawn.add(Generator.random(Generator.Category.STONE));

        // 음식
        switch (Random.Int(6)) {
            case 0:
                itemsToSpawn.add(new Ram2());
                break;
            case 1:
                itemsToSpawn.add(new FrozenCarpaccio());
                break;
            case 2:
                itemsToSpawn.add(new Food());
                break;
            case 3:
                itemsToSpawn.add(new Pasty());
                break;
            case 4:
                itemsToSpawn.add(new MeatPie());
                break;
            case 5:
                itemsToSpawn.add(new PhantomMeat());
                break;
        }

        // 추가 소모품 (랜덤)
        if (Random.Int(2) == 0) {
            switch (Random.Int(2)) {
                case 0:
                    itemsToSpawn.add(new StoneOfEnchantment());
                    break;
                case 1:
                    itemsToSpawn.add(new Stylus());
                    break;
            }
        }

        switch (Random.Int(7)) {
            case 0:
                itemsToSpawn.add(new StoneOfAugmentation());
                break;
            case 1:
                itemsToSpawn.add(new Highway());
                break;
            case 2:
                itemsToSpawn.add(new PhaseShift().quantity(3));
                break;
            case 3:
                itemsToSpawn.add(new TelekineticGrab().quantity(8));
                break;
            case 4:
                itemsToSpawn.add(new WildEnergy().quantity(2));
                break;
            case 5:
                itemsToSpawn.add(new HealingDart().quantity(1));
                break;
            case 6:
                itemsToSpawn.add(new ScrollOfExtract().quantity(1));
                break;
        }

        switch (Random.Int(4)) {
            case 0:
                Item t2;
                t2 = Generator.randomUsingDefaults(Generator.Category.WEP_T2);
                t2.identify();
                itemsToSpawn.add(t2);
                break;
            case 1:
                Item t3;
                t3 = Generator.randomUsingDefaults(Generator.Category.WEP_T3);
                t3.identify();
                itemsToSpawn.add(t3);
                break;
            case 2:
                Item t4;
                t4 = Generator.randomUsingDefaults(Generator.Category.WEP_T4);
                t4.identify();
                itemsToSpawn.add(t4);
                break;
            case 3:
                Item t5;
                t5 = Generator.randomUsingDefaults(Generator.Category.WEP_T5);
                t5.identify();
                itemsToSpawn.add(t5);
                break;
        }

        if (Random.Int(3) == 0) {
            switch (Random.Int(2)) {
                case 0:
                    itemsToSpawn.add(new Spw10());
                    break;
                case 1:
                    itemsToSpawn.add(new Spw11());
                    break;
            }
        }

        if (Random.Int(3) == 0) {
            itemsToSpawn.add(new Bomb.DoubleTBomb());
        }

        // 다트 (항상 포함)
        itemsToSpawn.add(TippedDart.randomTipped(2));

        // 깊이별 특별 아이템
        if (Dungeon.depth % 2 == 0) itemsToSpawn.add(new PotionOfHealing().identify());
        if (Dungeon.depth % 2 == 0) itemsToSpawn.add(Generator.randomMissile());
        if (Dungeon.depth % 6 == 0) itemsToSpawn.add(new MagicalInfusion().identify());
        if (Dungeon.depth % 4 == 0) itemsToSpawn.add(new ScrollOfRemoveCurse().identify());
        if (Dungeon.depth % 4 == 0) itemsToSpawn.add(new PotionOfStrength().identify());

        // 희귀 아이템 (하나만 선택)
        Item rare;
        switch (Random.Int(5)) {
            case 0:
                rare = Generator.randomUsingDefaults(Generator.Category.WAND);
                break;
            case 1:
                switch (Random.Int(12)) {
                    case 0:
                        rare = new EtherealChains().identify();
                        break;
                    case 1:
                        rare = new ChaliceOfBlood().identify();
                        break;
                    case 2:
                        rare = new WoundsofWar().identify();
                        break;
                    case 3:
                        rare = new CurseInfusion();
                        break;
                    case 4:
                        rare = new SandalsOfNature().identify();
                        break;
                    case 5:
                        rare = new PetrifiedSeed().upgrade(3).identify();
                        ;
                        break;
                    case 6:
                        rare = new ThirteenLeafClover().upgrade(3).identify();
                        ;
                        break;
                    case 7:
                        rare = new WondrousResin().upgrade(3).identify();
                        ;
                        break;
                    case 8:
                        rare = new EyeOfNewt().upgrade(3).identify();
                        ;
                        break;
                    case 9:
                        rare = new VialOfBlood().upgrade(3).identify();
                        ;
                        break;
                    case 10:
                        rare = new ChaoticCenser().upgrade(3).identify();
                        ;
                        break;
                    case 11:
                        rare = new FerretTuft().upgrade(3).identify();
                        ;
                        break;
                    default:
                        rare = new Dewdrop();
                        break;
                }
                break;
            case 2:
                rare = Generator.randomArmor();
                break;
            case 3:
                rare = Generator.random(Generator.Category.RING);
                break;
            default:
                rare = new Dewdrop();
        }

        switch (Random.Int(8)) {
            case 0:
                itemsToSpawn.add(new Kingt());
                break;
            case 1:
                itemsToSpawn.add(new StoneOfAdvanceguard());
                break;
            case 3:
                itemsToSpawn.add(new Kinga());
                break;
            case 4:
                itemsToSpawn.add(new Kings());
                break;
            case 5:
                itemsToSpawn.add(new Kingm());
                break;
            case 6:
                itemsToSpawn.add(new Kingw());
                break;
            case 7:
                itemsToSpawn.add(new Kingc());
                break;
        }

        if (Random.Int(8) == 0) {
            itemsToSpawn.add(new UV());
        }

        if (Random.Int(12) == 0) {
            itemsToSpawn.add(new Spw());
        }

        if (Random.Int(12) == 0) {
            itemsToSpawn.add(new AdvancedEvolution());
        }

        rare.identify();
        itemsToSpawn.add(rare);

        // 폭탄 (항상 포함)
        itemsToSpawn.add(new Bomb.DoubleTBomb());

        // 직업별 특화 아이템 (50% 확률)
        if (Random.Int(2) == 0) {
            Item additionalRare;
            switch (hero.heroClass) {
                case WARRIOR:
                case ROGUE:
                    additionalRare = Generator.randomWeapon();
                    break;
                case MAGE:
                    additionalRare = Generator.random(Generator.Category.WAND);
                    break;
                case HUNTRESS:
                    additionalRare = Generator.randomMissile();
                    break;
                default:
                    additionalRare = new Dewdrop();
            }
            additionalRare.identify();
            itemsToSpawn.add(additionalRare);
        }

        // 가방 (특정 깊이에서만)
        if (Dungeon.depth % 10 == 0) {
            Bag bag = ChooseBag(hero.belongings);
            if (bag != null) {
                itemsToSpawn.add(bag);
            }
        }

        // TimekeepersHourglass 관련 아이템
        TimekeepersHourglass hourglass = hero.belongings.getItem(TimekeepersHourglass.class);
        if (hourglass != null && hourglass.isIdentified() && !hourglass.cursed) {
            int bags = 0;
            switch (Dungeon.depth) {
                case 8:
                    bags = (int) Math.ceil((5 - hourglass.sandBags) * 0.20f);
                    break;
                case 16:
                    bags = (int) Math.ceil((5 - hourglass.sandBags) * 0.25f);
                    break;
                case 24:
                    bags = (int) Math.ceil((5 - hourglass.sandBags) * 0.50f);
                    break;
                case 32:
                    bags = (int) Math.ceil((5 - hourglass.sandBags) * 0.80f);
                    break;
            }

            for (int k = 1; k <= bags; k++) {
                itemsToSpawn.add(new TimekeepersHourglass.sandBag());
                hourglass.sandBags++;
            }
        }

        Random.pushGenerator(Random.Long());
        Random.shuffle(itemsToSpawn);
        Random.popGenerator();

        return itemsToSpawn;
    }

    protected static Bag ChooseBag(Belongings pack) {

        //generate a hashmap of all valid bags.
        HashMap<Bag, Integer> bags = new HashMap<>();
        if (!Dungeon.LimitedDrops.VELVET_POUCH.dropped()) bags.put(new VelvetPouch(), 1);
        if (!Dungeon.LimitedDrops.SCROLL_HOLDER.dropped()) bags.put(new ScrollHolder(), 0);
        if (!Dungeon.LimitedDrops.POTION_BANDOLIER.dropped()) bags.put(new PotionBandolier(), 0);
        if (!Dungeon.LimitedDrops.MAGICAL_HOLSTER.dropped()) bags.put(new MagicalHolster(), 0);

        if (bags.isEmpty()) return null;

        //count up items in the main bag
        for (Item item : pack.backpack.items) {
            for (Bag bag : bags.keySet()) {
                if (bag.canHold(item)) {
                    bags.put(bag, bags.get(bag) + 1);
                }
            }
        }

        //find which bag will result in most inventory savings, drop that.
        Bag bestBag = null;
        for (Bag bag : bags.keySet()) {
            if (bestBag == null) {
                bestBag = bag;
            } else if (bags.get(bag) > bags.get(bestBag)) {
                bestBag = bag;
            }
        }

        if (bestBag instanceof VelvetPouch) {
            Dungeon.LimitedDrops.VELVET_POUCH.drop();
        } else if (bestBag instanceof ScrollHolder) {
            Dungeon.LimitedDrops.SCROLL_HOLDER.drop();
        } else if (bestBag instanceof PotionBandolier) {
            Dungeon.LimitedDrops.POTION_BANDOLIER.drop();
        } else if (bestBag instanceof MagicalHolster) {
            Dungeon.LimitedDrops.MAGICAL_HOLSTER.drop();
        }

        return bestBag;

    }

}
