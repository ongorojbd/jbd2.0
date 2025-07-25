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
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Belongings;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Shopkeeper;
import com.shatteredpixel.shatteredpixeldungeon.items.Amulet;
import com.shatteredpixel.shatteredpixeldungeon.items.Ankh;
import com.shatteredpixel.shatteredpixeldungeon.items.Dewdrop;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Honeypot;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.Ram2;
import com.shatteredpixel.shatteredpixeldungeon.items.Stylus;
import com.shatteredpixel.shatteredpixeldungeon.items.Torch;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.LeatherArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.MailArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.PlateArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ScaleArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.MagicalHolster;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.PotionBandolier;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.ScrollHolder;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.VelvetPouch;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Tbomb;
import com.shatteredpixel.shatteredpixeldungeon.items.food.SmallRation;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfStrength;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Araki;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Cen;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Drago;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfIdentify;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRemoveCurse;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Alchemize;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Highway;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Kinga;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Kingc;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Kingm;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Kings;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Kingt;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Kingw;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Maga;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Mdisc;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Neoro;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Ram;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Sbr1;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Sbr2;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Sbr3;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Willa;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Willc;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Willg;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Xray;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfAdvanceguard;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfAugmentation;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.TippedDart;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashMap;

public class ShopRoom extends SpecialRoom {

    protected ArrayList<Item> itemsToSpawn;

	@Override
	public int minWidth() {
		if (SPDSettings.getTendency() > 0) {
			// Tendency 모드에서는 더 작은 크기 사용
			return Math.max(5, (int) Math.ceil(Math.sqrt(spacesNeeded() * 0.5)));
		}
		return Math.max(5, (int) Math.ceil(Math.sqrt(spacesNeeded())));
	}

	@Override
	public int minHeight() {
		if (SPDSettings.getTendency() > 0) {
			// Tendency 모드에서는 더 작은 크기 사용
			return Math.max(5, (int) Math.ceil(Math.sqrt(spacesNeeded() * 0.5)));
		}
		return Math.max(5, (int) Math.ceil(Math.sqrt(spacesNeeded())));
	}


    public int spacesNeeded() {
        if (itemsToSpawn == null) {
            if (SPDSettings.getTendency() > 0) { // 전투조류
                itemsToSpawn = generateItemsGauntlet();
            } else itemsToSpawn = generateItems();
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
        // spacesNeeded += 4;

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

        Mob shopkeeper = new Shopkeeper();
        shopkeeper.pos = pos;
        level.mobs.add(shopkeeper);

    }

    protected void placeItems(Level level) {

        if (itemsToSpawn == null) {
			if (SPDSettings.getTendency() > 0) { // 전투조류
				itemsToSpawn = generateItemsGauntlet();
			} else {
				itemsToSpawn = generateItems();
			}
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

        for (int i = 0; i < 2; i++) {
            itemsToSpawn.add(Generator.random(Generator.Category.POTION).identify());
            itemsToSpawn.add(Generator.random(Generator.Category.SCROLL).identify());
            itemsToSpawn.add(Generator.random(Generator.Category.STONE));
        }

        itemsToSpawn.add(TippedDart.randomTipped(2));

        if (Dungeon.depth % 2 == 0) itemsToSpawn.add(new ScrollOfUpgrade().identify());
        if (Dungeon.depth % 3 == 0) itemsToSpawn.add(new PotionOfStrength().identify());
        if (Dungeon.depth % 2 == 0) itemsToSpawn.add(Generator.randomMissile());

        Item rare;
        switch (Random.Int(5)) {
            case 0:
                rare = Generator.randomUsingDefaults(Generator.Category.WAND);
                break;
            case 1:
                rare = Generator.randomUsingDefaults(Generator.Category.ARTIFACT);
                break;
            case 2:
                rare = Generator.randomWeapon();
                break;
            case 3:
                rare = Generator.randomArmor();
                break;
            default:
                rare = new Dewdrop();
        }
        rare.identify();
        itemsToSpawn.add(rare);
        itemsToSpawn.add(new Bomb.DoubleTBomb());
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

        if (Dungeon.depth % 6 == 0) itemsToSpawn.add(ChooseBag(hero.belongings));

        TimekeepersHourglass hourglass = hero.belongings.getItem(TimekeepersHourglass.class);
        if (hourglass != null && hourglass.isIdentified() && !hourglass.cursed) {
            int bags = 0;
            //creates the given float percent of the remaining bags to be dropped.
            //this way players who get the hourglass late can still max it, usually.
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

    protected static ArrayList<Item> generateItems() {

        ArrayList<Item> itemsToSpawn = new ArrayList<>();

        MeleeWeapon w;
        switch (Dungeon.depth) {
            case 6:
            default:
                w = (MeleeWeapon) Generator.random(Generator.wepTiers[1]);
                itemsToSpawn.add(Generator.random(Generator.misTiers[1]).quantity(2).identify(false));
                itemsToSpawn.add(new LeatherArmor().identify(false));
                break;

            case 11:
                w = (MeleeWeapon) Generator.random(Generator.wepTiers[2]);
                itemsToSpawn.add(Generator.random(Generator.misTiers[2]).quantity(2).identify(false));
                itemsToSpawn.add(new MailArmor().identify(false));
                break;

            case 16:
                w = (MeleeWeapon) Generator.random(Generator.wepTiers[3]);
                itemsToSpawn.add(Generator.random(Generator.misTiers[3]).quantity(2).identify(false));
                itemsToSpawn.add(new ScaleArmor().identify(false));
                break;

            case 20:
            case 21:
                w = (MeleeWeapon) Generator.random(Generator.wepTiers[4]);
                itemsToSpawn.add(Generator.random(Generator.misTiers[4]).quantity(2).identify(false));
                itemsToSpawn.add(new PlateArmor().identify(false));
                itemsToSpawn.add(new Torch());
                itemsToSpawn.add(new Torch());
                itemsToSpawn.add(new Torch());
                break;
        }
        w.enchant(null);
        w.cursed = false;
        w.level(0);
        w.identify(false);
        itemsToSpawn.add(w);

        itemsToSpawn.add(TippedDart.randomTipped(2));

        itemsToSpawn.add(new Alchemize().quantity(Random.IntRange(2, 3)));

        Bag bag = ChooseBag(hero.belongings);
        if (bag != null) {
            itemsToSpawn.add(bag);
        }

        itemsToSpawn.add(new PotionOfHealing());
        itemsToSpawn.add(Generator.randomUsingDefaults(Generator.Category.POTION));
        itemsToSpawn.add(Generator.randomUsingDefaults(Generator.Category.POTION));

        itemsToSpawn.add(new ScrollOfIdentify());
        itemsToSpawn.add(new ScrollOfRemoveCurse());
        itemsToSpawn.add(new ScrollOfMagicMapping());

        for (int i = 0; i < 2; i++)
            itemsToSpawn.add(Random.Int(2) == 0 ?
                    Generator.randomUsingDefaults(Generator.Category.POTION) :
                    Generator.randomUsingDefaults(Generator.Category.SCROLL));


        itemsToSpawn.add(new SmallRation());
        switch (Random.Int(2)) {
            case 0:
                itemsToSpawn.add(new Ram2());
                break;
            case 1:
                itemsToSpawn.add(new SmallRation());
                break;
        }


        switch (Random.Int(6)) {
            case 0:
                itemsToSpawn.add(new Highway());
                break;
            case 1:
                itemsToSpawn.add(new Bomb());
                break;
            case 2:
                itemsToSpawn.add(new Bomb.DoubleBomb());
                break;
            case 3:
                itemsToSpawn.add(new Honeypot());
                break;
            case 4:
                itemsToSpawn.add(new Drago());
                break;
            case 5:
                itemsToSpawn.add(new Neoro());
                break;
        }

        switch (Random.Int(8)) {
            case 0:
                itemsToSpawn.add(new Kingt());
                break;
            case 1:
                itemsToSpawn.add(new StoneOfAdvanceguard());
                break;
            case 2:
                itemsToSpawn.add(new Xray());
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


        if (Random.Int(5) == 0) {
            switch (Random.Int(3)) {
                case 0:
                    itemsToSpawn.add(new Willa());
                    break;
                case 1:
                    itemsToSpawn.add(new Willc());
                    break;
                case 2:
                    itemsToSpawn.add(new Willg());
                    break;
            }
        }

        if (Random.Int(20) == 0) {
            switch (Random.Int(3)) {
                case 0:
                    itemsToSpawn.add(new Sbr1());
                    break;
                case 1:
                    itemsToSpawn.add(new Sbr2());
                    break;
                case 2:
                    itemsToSpawn.add(new Sbr3());
                    break;
            }
        }

        if (Random.Int(17) == 0) {
            itemsToSpawn.add(new Maga());
        }

        if (Random.Int(20) == 0) {
            itemsToSpawn.add(new Cen());
        }

        if (Random.Int(40) == 0) {
            itemsToSpawn.add(new Mdisc());
        }

        if (Random.Int(50) == 0) {
            itemsToSpawn.add(new Araki());
        }

        itemsToSpawn.add(new Ankh());
        itemsToSpawn.add(new StoneOfAugmentation());

        TimekeepersHourglass hourglass = hero.belongings.getItem(TimekeepersHourglass.class);
        if (hourglass != null && hourglass.isIdentified() && !hourglass.cursed) {
            int bags = 0;
            //creates the given float percent of the remaining bags to be dropped.
            //this way players who get the hourglass late can still max it, usually.
            switch (Dungeon.depth) {
                case 6:
                    bags = (int) Math.ceil((5 - hourglass.sandBags) * 0.20f);
                    break;
                case 11:
                    bags = (int) Math.ceil((5 - hourglass.sandBags) * 0.25f);
                    break;
                case 16:
                    bags = (int) Math.ceil((5 - hourglass.sandBags) * 0.50f);
                    break;
                case 20:
                case 21:
                    bags = (int) Math.ceil((5 - hourglass.sandBags) * 0.80f);
                    break;
            }

            for (int i = 1; i <= bags; i++) {
                itemsToSpawn.add(new TimekeepersHourglass.sandBag());
                hourglass.sandBags++;
            }
        }

        Item rare;
        switch (Random.Int(10)) {
            case 0:
                rare = Generator.random(Generator.Category.WAND);
                rare.level(0);
                break;
            case 1:
                rare = Generator.random(Generator.Category.RING);
                rare.level(0);
                break;
            case 2:
                rare = Generator.random(Generator.Category.ARTIFACT);
                break;
            case 3:
                rare = new Kingt();
                break;
            default:
                rare = new Stylus();
        }
        rare.cursed = false;
        rare.cursedKnown = true;
        itemsToSpawn.add(rare);

        //use a new generator here to prevent items in shop stock affecting levelgen RNG (e.g. sandbags)
        //we can use a random long for the seed as it will be the same long every time
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
