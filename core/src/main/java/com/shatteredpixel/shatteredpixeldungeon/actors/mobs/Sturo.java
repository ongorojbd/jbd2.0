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

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;
import static com.shatteredpixel.shatteredpixeldungeon.Statistics.wave;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SmokeParticle;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SturoSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Sturo extends Mob {

    {
        spriteClass = SturoSprite.class;

        HP = HT = Dungeon.hero.HT;
        defenseSkill = 0;
        EXP = 0;
        maxLvl = -9;
        viewDistance = 10;
        alignment = Alignment.ALLY;
        intelligentAlly = true;
    }

    private int charge = 0;
    private static final String SKILLCD = "charge";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(SKILLCD, charge);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        charge = bundle.getInt(SKILLCD);
    }

    @Override
    public float attackDelay() {
        return super.attackDelay() * 0.3f;
    }

    @Override
    public int attackSkill(Char target) {
        return Dungeon.hero.lvl + 12;
    }

    @Override
    public int damageRoll() {
        int waveDamage = wave / 5;
        return Char.combatRoll(waveDamage, 1 + waveDamage);
    }

    @Override
    public int drRoll() {
        return super.drRoll() + hero.drRoll();
    }

    @Override
    public void damage(int dmg, Object src) {

        if (dmg >= 15) {
            //takes 20/21/22/23/24/25/26/27/28/29/30 dmg
            // at   20/22/25/29/34/40/47/55/64/74/85 incoming dmg
            dmg = 15;
        }
        super.damage(dmg, src);
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        damage = super.attackProc(enemy, damage);
        Sample.INSTANCE.play(Assets.Sounds.HIT_STAB, 1f, 0.8f);
        CellEmitter.get(enemy.pos).burst(SmokeParticle.FACTORY, 2);
        CellEmitter.center(enemy.pos).burst(BlastParticle.FACTORY, 2);

        if (charge >= 12) {
            Sample.INSTANCE.play(Assets.Sounds.HEI);
            charge = 0;
        } else {
            charge++;
        }

        return damage;
    }

    @Override
    protected boolean canAttack(Char enemy) {
        return true;
    }

    @Override
    protected boolean getCloser(int target) {

        target = Dungeon.hero.pos;

        return super.getCloser(target);
    }

}