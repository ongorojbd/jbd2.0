/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2026 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Shopkeeper;
import com.shatteredpixel.shatteredpixeldungeon.items.Ankh;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Honeypot;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.Torch;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.items.food.SmallRation;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfIdentify;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRemoveCurse;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Alchemize;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfAugmentation;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special.ShopRoom;
import com.watabou.utils.Random;

import java.util.ArrayList;

/*
    A small hospital-kiosk vending corner: same stock-placement/pricing logic as the regular
    ShopRoom (see placeItems() there), but only ever sells consumables (food/potions/scrolls -
    no weapons, armor or wands, since a vending machine doesn't stock those). Unlike ShopRoom,
    paint() doesn't wall the rect off into its own sealed room - it's meant to read as an open
    corner of whatever room it's placed in (walled shops tucked behind a single door tile were
    too easy to miss entirely), so it just floors the rect and drops the shopkeeper/stock on it.
    Like ImpShopRoom, it isn't built by the level Builder/Painter graph - it's hand-placed into
    a fixed layout via set(left, top, right, bottom) then paint(level), so entrance() is
    overridden to hand back a fixed point instead of reading one out of the (empty) connected
    map (placeItems() uses it purely as the spiral's starting corner, not as a doorway).
*/
public class VendingMachineShopRoom extends ShopRoom {

    @Override
    public int minWidth() {
        return Math.max(6, (int)(Math.sqrt(spacesNeeded())+3));
    }

    @Override
    public int minHeight() {
        return Math.max(6, (int)(Math.sqrt(spacesNeeded())+3));
    }

    @Override
    public int spacesNeeded() {
        if (itemsToSpawn == null) itemsToSpawn = generateVendingItems();
        //+1 for the vendor itself, no sandbag slack needed - this stock never includes hourglass sandbags
        return itemsToSpawn.size() + 1;
    }

    @Override
    public void paint(Level level) {
        //no walls/border here on purpose - see class comment. Plain floor too, rather than
        //ShopRoom's EMPTY_SP: this sits inside a room that's already dressed, and the special
        //shop floor reads as a foreign patch in a custom tileset. The vending machines lined up
        //behind the counter are what mark the area out, not the floor.
        Painter.fill(level, this, Terrain.EMPTY);
        placeShopkeeper(level);
        placeItems(level);
    }

    @Override
    protected void placeShopkeeper(Level level) {
        int pos = level.pointToCell(center());
        Mob keeper = new Shopkeeper();
        keeper.pos = pos;
        level.mobs.add(keeper);
    }

    @Override
    protected void placeItems(Level level) {
        if (itemsToSpawn == null) {
            itemsToSpawn = generateVendingItems();
        }
        super.placeItems(level);
    }

    //not part of the builder graph - hand-placed, so there's no connected door to read a point
    //out of. placeItems() only uses this as the corner the item spiral starts from, since
    //there's no real doorway to speak of - defaults to the middle of the left edge.
    @Override
    public Door entrance() {
        return connected.isEmpty() ? new Door(left, (top+bottom)/2) : super.entrance();
    }

    private static ArrayList<Item> generateVendingItems() {
        ArrayList<Item> items = new ArrayList<>();

        //snack shelf
        items.add(new SmallRation());
        items.add(new SmallRation());
        for (int i = 0; i < 3; i++) {
            items.add(Generator.randomUsingDefaults(Generator.Category.FOOD));
        }

        //drinks cooler
        items.add(new PotionOfHealing());
        items.add(new PotionOfHealing());
        for (int i = 0; i < 4; i++) {
            items.add(Generator.randomUsingDefaults(Generator.Category.POTION));
        }

        //the paperback rack
        items.add(new ScrollOfIdentify());
        items.add(new ScrollOfIdentify());
        items.add(new ScrollOfRemoveCurse());
        items.add(new ScrollOfMagicMapping());
        items.add(Generator.randomUsingDefaults(Generator.Category.SCROLL));

        //sundries - a hospital kiosk stocks an ankh the way a real one stocks a first aid kit
        items.add(new Alchemize().quantity(Random.IntRange(2, 3)));
        items.add(new Torch());
        items.add(new Torch());
        items.add(new Ankh());
        items.add(new StoneOfAugmentation());
        if (Random.Int(2) == 0) {
            items.add(new Honeypot());
        }
        if (Random.Int(2) == 0) {
            items.add(new Bomb());
        }

        Bag bag = ChooseBag(Dungeon.hero.belongings);
        if (bag != null) {
            items.add(bag);
        }

        //use a new generator so shop stock doesn't affect levelgen RNG, same as ShopRoom itself
        Random.pushGenerator(Random.Long());
        Random.shuffle(items);
        Random.popGenerator();

        return items;
    }
}
