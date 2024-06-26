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

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;

import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;

public class Cudgel extends MeleeWeapon {

    {
        image = ItemSpriteSheet.CUDGEL;
        hitSound = Assets.Sounds.HIT;
        hitSoundPitch = 1.1f;

        tier = 1;

        bones = false;
    }

    @Override
    protected int baseChargeUse(Hero hero, Char target){
        if (hero.buff(Sword.CleaveTracker.class) != null){
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public String targetingPrompt() {
        return Messages.get(this, "prompt");
    }

    @Override
    protected void duelistAbility(Hero hero, Integer target) {
        //+(4+lvl) damage, roughly +35% base dmg, +40% scaling
        int dmgBoost = augment.damageFactor(4 + buffedLvl());
        Sword.cleaveAbility(hero, target, 1, dmgBoost, this);
    }
}