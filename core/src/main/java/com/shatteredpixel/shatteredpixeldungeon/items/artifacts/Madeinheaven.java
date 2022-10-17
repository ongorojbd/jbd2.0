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

package com.shatteredpixel.shatteredpixeldungeon.items.artifacts;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Triplespeed;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public class Madeinheaven extends Artifact {

    {
        image = ItemSpriteSheet.AMULET;

        levelCap = 1;

        identify();
    }

    @Override
    public ArrayList<String> actions( Hero hero ) {
        ArrayList<String> actions = super.actions( hero );


        return actions;
    }

    @Override
    public String status() {
        if (this.isIdentified()) return "MIH";
        else return null;}



    @Override
    public Item upgrade() {
        if (level() >= 6)
            image = ItemSpriteSheet.ARTIFACT_CHALICE3;
        else if (level() >= 2)
            image = ItemSpriteSheet.ARTIFACT_CHALICE2;
        return super.upgrade();
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        if (level() >= 7) image = ItemSpriteSheet.ARTIFACT_CHALICE3;
        else if (level() >= 3) image = ItemSpriteSheet.ARTIFACT_CHALICE2;
    }

    @Override
    protected ArtifactBuff passiveBuff() {
        return new Madeinheaven.chaliceRegen();
    }

    @Override
    public void charge(Hero target, float amount) {


        //effectively 1HP at lvl 0-5, 2HP lvl 6-8, 3HP lvl 9, and 5HP lvl 10.
        target.HP = Math.min( 155, 2344);
    }


    @Override
    public String desc() {
        String desc = super.desc();

        return desc;
    }

    public class chaliceRegen extends ArtifactBuff {

        @Override
        public boolean act(){


            Buff.prolong(hero, Triplespeed.class, 3);
            spend(TICK);
            return true;
        }

    }

}

