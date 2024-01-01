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
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Preparation;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Bcom;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Bcomsolg;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BloodParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.ChaliceOfBlood;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.CloakOfShadows;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.HornOfPlenty;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TalismanOfForesight;
import com.shatteredpixel.shatteredpixeldungeon.items.food.MeatPie;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfAccuracy;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfRegrowth;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class MISTA extends MeleeWeapon {
    {
        image = ItemSpriteSheet.MISTA;
        hitSound = Assets.Sounds.HIT_STAB;
        hitSoundPitch = 0.9f;

        tier = 5;
        ACC = 1.15f; //24% boost to accuracy
        RCH = 3;    //lots of extra reach
    }

    private int HealCount = 0;

    @Override
    public int max(int lvl) {
        return 3 * (tier + 1) +    //15+3
                lvl * (tier - 1);
    }

    private int starpower = 0;
    private int starpowercap = 6;

    private void Heal(Char attacker) {
        if (HealCount >= 6) {
            starpower++;
            HealCount = 0;
        }
    }

    @Override
    public int proc(Char attacker, Char defender, int damage) {

        HealCount++;

        Heal(attacker);

        if (starpower >= 1 && attacker instanceof Hero) {
            for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
                if (mob.alignment != Char.Alignment.ALLY && Dungeon.level.heroFOV[mob.pos]) {
                    int dmg = attacker.damageRoll() - defender.drRoll();

                    if (Dungeon.hero.belongings.getItem(HornOfPlenty.class) != null) {
                        if (Dungeon.hero.belongings.getItem(HornOfPlenty.class).isEquipped(Dungeon.hero)) {
                            dmg = Math.round(dmg * 1.35f);
                        }
                    } else if(defender instanceof Bcomsolg) {
                        dmg = Math.round(dmg * 0.3f);
                    }  else {
                        dmg = Math.round(dmg * 1.2f);
                    }

                    mob.damage(dmg, attacker);
                }
            }

            GameScene.flash(0x80FFFFFF);
            Camera.main.shake(2, 1.5f);
            Sample.INSTANCE.play(Assets.Sounds.SP, 1.1f, 1f);
            attacker.sprite.showStatus(CharSprite.POSITIVE, "[섹스 피스톨즈!]");
        }

        starpower = 0;
        updateQuickslot();
        return super.proc(attacker, defender, damage);
    }

    @Override
    public String desc() {
        String info = Messages.get(this, "desc");
        if (Dungeon.hero.belongings.getItem(HornOfPlenty.class) != null) {
            if (Dungeon.hero.belongings.getItem(HornOfPlenty.class).isEquipped(Dungeon.hero))
                info += "\n\n" + Messages.get(MISTA.class, "setbouns");
        }

        return info;
    }

    private static final String POWER = "starpower";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(POWER, starpower);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        if (starpowercap > 0) starpower = Math.min(starpowercap, bundle.getInt(POWER));
        else starpower = bundle.getInt(POWER);
    }

    @Override
    protected void duelistAbility(Hero hero, Integer target) {

        ArrayList<Char> targets = new ArrayList<>();
        Char closest = null;

        hero.belongings.abilityWeapon = this;
        for (Char ch : Actor.chars()) {
            if (ch.alignment == Char.Alignment.ENEMY
                    && !hero.isCharmedBy(ch)
                    && Dungeon.level.heroFOV[ch.pos]
                    && hero.canAttack(ch)) {
                targets.add(ch);
                if (closest == null || Dungeon.level.trueDistance(hero.pos, closest.pos) > Dungeon.level.trueDistance(hero.pos, ch.pos)) {
                    closest = ch;
                }
            }
        }
        hero.belongings.abilityWeapon = null;

        if (targets.isEmpty()) {
            GLog.w(Messages.get(this, "ability_no_target"));
            return;
        }

        throwSound();
        Char finalClosest = closest;
        hero.sprite.attack(hero.pos, new Callback() {
            @Override
            public void call() {
                beforeAbilityUsed(hero, finalClosest);
                for (Char ch : targets) {
                    hero.attack(ch, 1, 0, ch == finalClosest ? Char.INFINITE_ACCURACY : 1);
                    if (!ch.isAlive()) {
                        onAbilityKill(hero, ch);
                    }
                }
                Invisibility.dispel();
                Sword.doraclass();

                hero.spendAndNext(hero.attackDelay());
                afterAbilityUsed(hero);
            }
        });
    }

    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe {

        {
            inputs = new Class[]{Whip.class, WandOfRegrowth.class};
            inQuantity = new int[]{1, 1};

            cost = 15;

            output = MISTA.class;
            outQuantity = 1;
        }
    }

}
