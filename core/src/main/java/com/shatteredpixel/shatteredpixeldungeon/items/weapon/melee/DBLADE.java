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
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfFuror;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
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

    @Override
    public boolean doEquip(Hero hero) {
        identify();
        super.doEquip(hero);
        return true;
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
                    if (Random.Int(20) == 0) {
                        damage *= 1.5f;
                    attacker.sprite.showStatus(CharSprite.NEUTRAL, "[치명타 공격!]");
                }}
            }
        }
        return super.proc(attacker, defender, damage);
    }

    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe{

        {
            inputs =  new Class[]{Longsword.class, BattleAxe.class};
            inQuantity = new int[]{1, 1};

            cost = 15;

            output = DBLADE.class;
            outQuantity = 1;
        }
    }

    @Override
    public String desc() {
        String info = Messages.get(this, "desc");
        if (Dungeon.hero != null && Dungeon.hero.belongings.getItem(RingOfFuror.class) != null) {
            if (Dungeon.hero.belongings.getItem(RingOfFuror.class).isEquipped(Dungeon.hero))
                info += "\n\n" + Messages.get( DBLADE.class, "setbouns");}

        return info;
    }

    @Override
    public String targetingPrompt() {
        return Messages.get(this, "prompt");
    }

    @Override
    protected void duelistAbility(Hero hero, Integer target) {
        //+(6+1.5*lvl) damage, roughly +50% damage
        int dmgBoost = augment.damageFactor(6 + Math.round(1.5f*buffedLvl()));
        Rapier.lungeAbility(hero, target, 1, dmgBoost, this);
    }

    @Override
    public String abilityInfo() {
        int dmgBoost = levelKnown ? 6 + Math.round(1.5f*buffedLvl()) : 6;
        if (levelKnown){
            return Messages.get(this, "ability_desc", augment.damageFactor(min()+dmgBoost), augment.damageFactor(max()+dmgBoost));
        } else {
            return Messages.get(this, "typical_ability_desc", min(0)+dmgBoost, max(0)+dmgBoost);
        }
    }
}