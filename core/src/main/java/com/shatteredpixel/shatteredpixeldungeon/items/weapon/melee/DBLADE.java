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

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Combo;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.ChaliceOfBlood;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfFuror;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

public class DBLADE extends MeleeWeapon {

    {
        image = ItemSpriteSheet.DBLADE;
        hitSound = Assets.Sounds.HIT_SLASH;
        hitSoundPitch = 0.8f;

        tier = 5;
        DLY = 0.8f;//1.25x speed
    }

    private boolean doubleattack = true;


    @Override
    public int max(int lvl) {
        return  3*(tier+1) - 1 +    //17 + 4. 공식상 2회 타격
                lvl*(tier-1);   //scaling unchanged
    }

    @Override
    public int proc(Char attacker, Char defender, int damage) {
        if (doubleattack) {
            doubleattack = false;
            if (!attacker.attack(defender)) {
                doubleattack = true; }
            else {
                defender.sprite.bloodBurstA( defender.sprite.center(), 4 );
                defender.sprite.flash();
            }
        }
        else doubleattack = true;

        if (attacker instanceof Hero) {
            if (Dungeon.hero.belongings.getItem(RingOfFuror.class) != null) {
                if (((Hero) attacker).belongings.getItem(RingOfFuror.class).isEquipped(Dungeon.hero)) {
                    if (Random.Int(20) < 1)
                        damage *= 1.5f;
                }
            }
        }
        return super.proc(attacker, defender, damage);
    }

    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe{

        {
            inputs =  new Class[]{Longsword.class, BattleAxe.class};
            inQuantity = new int[]{1, 1};

            cost = 30;

            output = DBLADE.class;
            outQuantity = 1;
        }
    }

    @Override
    public String desc() {
        String info = Messages.get(this, "desc");
        if (Dungeon.hero.belongings.getItem(RingOfFuror.class) != null) {
            if (Dungeon.hero.belongings.getItem(RingOfFuror.class).isEquipped(Dungeon.hero))
                info += "\n\n" + Messages.get( DBLADE.class, "setbouns");}

        return info;
    }

}