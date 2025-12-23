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
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SmokeParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.Chasm;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ShrSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class ShrBomb extends Mob {

    {
        spriteClass = ShrSprite.class;

        HP = HT = 100;
        defenseSkill = 0;

        EXP = 0;

        alignment = Alignment.ALLY;
        state = HUNTING;

        maxLvl = -9;
    }

    @Override
    public int attackSkill(Char target) {
        return INFINITE_ACCURACY;
    }

    @Override
    public int damageRoll() {
        // 1~10의 근접 공격력
        return Random.NormalIntRange(1, 10);
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        // 근접 공격 시 자폭
        this.die(null);
        return damage;
    }

    @Override
    public int drRoll() {
        return super.drRoll() + Random.NormalIntRange(999, 999);
    }

    @Override
    public void die(Object cause) {
        super.die(cause);
        sprite.killAndErase();
        if (cause == Chasm.class) return;

        // 3x3 범위 폭발 (NEIGHBOURS8 + 중심)
        // 중심 위치 (자신의 위치)
        Char centerChar = findChar(pos);
        if (centerChar != null && centerChar.isAlive()) {
            int damage = calculateExplosionDamage();
            damage = Math.max(0, damage - centerChar.drRoll());
            centerChar.damage(damage, Bomb.class);
        }

        // 주변 8방향
        for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
            int targetPos = pos + PathFinder.NEIGHBOURS8[i];
            Char ch = findChar(targetPos);
            if (ch != null && ch.isAlive()) {
                int damage = calculateExplosionDamage();
                damage = Math.max(0, damage - ch.drRoll());
                ch.damage(damage, Bomb.class);
                if (ch == Dungeon.hero && !ch.isAlive()) {
                    Dungeon.fail(getClass());
                }
            }
            
            // 이펙트
            if (Dungeon.level.heroFOV[targetPos]) {
                CellEmitter.center(targetPos).burst(BlastParticle.FACTORY, 15);
                CellEmitter.center(targetPos).burst(SmokeParticle.FACTORY, 4);
            }
        }
        
        // 중심 위치 이펙트
        if (Dungeon.level.heroFOV[pos]) {
            CellEmitter.center(pos).burst(BlastParticle.FACTORY, 15);
            CellEmitter.center(pos).burst(SmokeParticle.FACTORY, 4);
            Sample.INSTANCE.play(Assets.Sounds.BLAST);
        }
    }

    private int calculateExplosionDamage() {
        // ShrapnelBomb 데미지: Random.NormalIntRange( 4 + Dungeon.scalingDepth(), 12 + 3*Dungeon.scalingDepth() )
        // 75% 강하게: 1.75배
        int baseDamage = Random.NormalIntRange(4 + Dungeon.scalingDepth(), 12 + 3 * Dungeon.scalingDepth());
        return Math.round(baseDamage * 1.75f);
    }
}

