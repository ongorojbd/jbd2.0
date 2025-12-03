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

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Adrenaline;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.sprites.KS1Sprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

/**
 * KS1 - 카즈의 정예병 (쌍도끼)
 * 높은 공격력과 출혈 효과를 가진 공격형 유닛
 * 제자리에서 3회 이상 공격하면 아드레날린 30턴 부여
 */
public class KS1 extends Mob {

    {
        spriteClass = KS1Sprite.class;

        HP = HT = 220;
        defenseSkill = 30;

        EXP = 20;
        maxLvl = 30;

        properties.add(Property.UNDEAD);
        properties.add(Property.DEMONIC);
    }

    private int stationaryAttackCount = 0; // 제자리 공격 횟수 (Diego.java 참조)

    private static final String STATIONARY_ATTACK = "stationary_attack";

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(45, 55);
    }

    @Override
    public int attackSkill(Char target) {
        return 40;
    }

    @Override
    public int drRoll() {
        return super.drRoll() + Random.NormalIntRange(0, 20);
    }

    @Override
    public void move(int step, boolean travelling) {
        // 이동하면 제자리 공격 횟수 리셋 (Diego.java 참조)
        stationaryAttackCount = 0;
        super.move(step, travelling);
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        damage = super.attackProc(enemy, damage);

        // 제자리에서 공격하면 카운트 증가
        stationaryAttackCount++;
        
        // 3회 이상 제자리에서 공격하면 아드레날린 30턴 부여
        if (stationaryAttackCount >= 3) {
            Buff.affect(this, Adrenaline.class, 30f);
            stationaryAttackCount = 0; // 리셋
        }

        return damage;
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(STATIONARY_ATTACK, stationaryAttackCount);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        stationaryAttackCount = bundle.getInt(STATIONARY_ATTACK);
    }

    @Override
    public void die(Object cause) {

        super.die(cause);

        if (Dungeon.depth != 54) {
            if (Random.Int(3) == 0) {
                Dungeon.level.drop(new Gold().quantity(Random.IntRange(45, 55)), pos).sprite.drop();
            }
        }
		
        if (Dungeon.level.heroFOV[pos]) {
            Sample.INSTANCE.play(Assets.Sounds.BONES, Random.Float(1.2f, 0.9f));
            Sample.INSTANCE.play(Assets.Sounds.BURNING);
        }

    }
}

