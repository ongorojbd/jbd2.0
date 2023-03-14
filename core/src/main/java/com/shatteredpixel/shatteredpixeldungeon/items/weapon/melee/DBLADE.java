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
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Combo;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.ChaliceOfBlood;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfFuror;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.Door;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.AttackIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
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
                    attacker.sprite.showStatus(CharSprite.NEUTRAL, "[치명타 공격!]");
                }
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
        if (Dungeon.hero.belongings.getItem(RingOfFuror.class) != null) {
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
        if (target == null){
            return;
        }

        Char enemy = Actor.findChar(target);
        //duelist can lunge out of her FOV, but this wastes the ability instead of cancelling if there is no target
        if (Dungeon.level.heroFOV[target]) {
            if (enemy == null || enemy == hero || hero.isCharmedBy(enemy)) {
                GLog.w(Messages.get(this, "ability_no_target"));
                return;
            }
        }

        if (hero.rooted || Dungeon.level.distance(hero.pos, target) < 2
                || Dungeon.level.distance(hero.pos, target)-1 > reachFactor(hero)){
            GLog.w(Messages.get(this, "ability_bad_position"));
            return;
        }

        int lungeCell = -1;
        for (int i : PathFinder.NEIGHBOURS8){
            if (Dungeon.level.distance(hero.pos+i, target) <= reachFactor(hero)
                    && Actor.findChar(hero.pos+i) == null
                    && (Dungeon.level.passable[hero.pos+i] || (Dungeon.level.avoid[hero.pos+i] && hero.flying))){
                if (lungeCell == -1 || Dungeon.level.trueDistance(hero.pos + i, target) < Dungeon.level.trueDistance(lungeCell, target)){
                    lungeCell = hero.pos + i;
                }
            }
        }

        if (lungeCell == -1){
            GLog.w(Messages.get(this, "ability_bad_position"));
            return;
        }

        final int dest = lungeCell;
        hero.busy();
        Sample.INSTANCE.play(Assets.Sounds.MISS);
        Sample.INSTANCE.play(Assets.Sounds.DORA);
        hero.sprite.jump(hero.pos, dest, 0, 0.1f, new Callback() {
            @Override
            public void call() {
                if (Dungeon.level.map[hero.pos] == Terrain.OPEN_DOOR) {
                    Door.leave( hero.pos );
                }
                hero.pos = dest;
                Dungeon.level.occupyCell(hero);

                if (enemy != null) {
                    hero.sprite.attack(enemy.pos, new Callback() {
                        @Override
                        public void call() {
                            //+3+lvl damage, equivalent to +67% damage, but more consistent
                            beforeAbilityUsed(hero);
                            AttackIndicator.target(enemy);
                            if (hero.attack(enemy, 1f, augment.damageFactor(3 + level()), Char.INFINITE_ACCURACY)) {
                                Sample.INSTANCE.play(Assets.Sounds.HIT_STRONG);
                                if (!enemy.isAlive()) {
                                    onAbilityKill(hero);
                                }
                            }
                            Invisibility.dispel();
                            hero.spendAndNext(hero.attackDelay());
                            afterAbilityUsed(hero);
                        }
                    });
                } else {
                    beforeAbilityUsed(hero);
                    GLog.w(Messages.get(Rapier.class, "ability_no_target"));
                    hero.spendAndNext(hero.speed());
                    afterAbilityUsed(hero);
                }
            }
        });
    }

}