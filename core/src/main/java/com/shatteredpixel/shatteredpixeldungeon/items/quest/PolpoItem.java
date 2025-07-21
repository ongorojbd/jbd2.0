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

package com.shatteredpixel.shatteredpixeldungeon.items.quest;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Doom;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

import java.util.ArrayList;

public class PolpoItem extends Item {

    {
        image = ItemSpriteSheet.HONEYPOT;

        icon = ItemSpriteSheet.Icons.POTION_LIQFLAME;

        cursed = true;
        cursedKnown = true;

        unique = true;
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return new ItemSprite.Glowing(0x000000, 1f);
    }

    @Override
    public boolean doPickUp(Hero hero, int pos) {
        if (super.doPickUp(hero, pos)) {
            Buff.affect(hero, Doom.class);
            return true;
        }
        return false;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        return new ArrayList<>(); //yup, no dropping this one
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    @Override
    protected void onDetach() {

    }

}