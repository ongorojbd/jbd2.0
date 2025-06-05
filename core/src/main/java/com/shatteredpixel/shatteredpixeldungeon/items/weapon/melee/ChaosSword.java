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

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.AlchemistsToolkit;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfCleansing;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.BossdiscB;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.CursedWand;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

public class ChaosSword extends MeleeWeapon {

    {
        image = ItemSpriteSheet.MASK;
        hitSound = Assets.Sounds.HIT_SLASH;
        hitSoundPitch = 1f;

        tier = 3;
    }

    @Override
    public boolean doEquip(Hero hero) {
        identify();
        super.doEquip(hero);
        return true;
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return new ItemSprite.Glowing(0x000000, 1f);
    }

    @Override
    public int proc(Char attacker, Char defender, int damage) {

        if (hero.belongings.getItem(AlchemistsToolkit.class) != null) {
            if (hero.belongings.getItem(AlchemistsToolkit.class).isEquipped(hero)) {
                if (Random.Int(10) == 0) {
                    Buff.affect(hero, PotionOfCleansing.Cleanse.class, 5f);
                }
            }
        }

        return super.proc( attacker, defender, damage );
    }

    @Override
    public int max(int lvl) {
        return  Math.round(7.5f*(tier+1)) +    //40 base, up from 30
                lvl*Math.round(1.7f*(tier+2)); //+8 per level, up from +6
    }

    @Override
    public String desc() {
        String info = Messages.get(this, "desc");
        if (Dungeon.hero != null && Dungeon.hero.belongings.getItem(AlchemistsToolkit.class) != null) {
            if (Dungeon.hero.belongings.getItem(AlchemistsToolkit.class).isEquipped(Dungeon.hero))
                info += "\n\n" + Messages.get( ChaosSword.class, "setbouns");}

        return info;
    }

    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe{

        {
            inputs =  new Class[]{BossdiscB.class};
            inQuantity = new int[]{1};

            cost = 0;

            output = ChaosSword.class;
            outQuantity = 1;
        }
    }

    @Override
    public String targetingPrompt() {
        return Messages.get(this, "prompt");
    }

    @Override
    protected void duelistAbility(Hero hero, Integer target) {
        //+(4+1.5*lvl) damage, roughly +55% base dmg, +60% scaling
        int dmgBoost = augment.damageFactor(5 + Math.round(1.5f*buffedLvl()));
        Mace.heavyBlowAbility(hero, target, 1, dmgBoost, this);
    }

    @Override
    public String abilityInfo() {
        int dmgBoost = levelKnown ? 5 + Math.round(1.5f*buffedLvl()) : 5;
        if (levelKnown){
            return Messages.get(this, "ability_desc", augment.damageFactor(min()+dmgBoost), augment.damageFactor(max()+dmgBoost));
        } else {
            return Messages.get(this, "typical_ability_desc", min(0)+dmgBoost, max(0)+dmgBoost);
        }
    }

}
