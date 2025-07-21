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
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ShockingTrap;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HermitCrabSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

public class HermitCrab extends Crab {

    {
        spriteClass = HermitCrabSprite.class;

        HP = HT = 25; //+67% HP
        baseSpeed = 1f; //-50% speed

        loot = null;
        //3x more likely to drop meat, and drops a guaranteed armor
        lootChance = 0.5f;

        properties.add(Property.ELECTRIC);
        immunities.add(Paralysis.class);
    }

    @Override
    public void rollToDropLoot() {
        super.rollToDropLoot();

        if (Dungeon.hero.lvl <= maxLvl + 2) {
            Dungeon.level.drop(Generator.randomArmor(), pos).sprite.drop();
        }
    }

    @Override
    public int drRoll() {
        return super.drRoll() + 2; //2-6 DR total, up from 0-4
    }

    private int trapcooldown = 0;
    private int lastEnemyPos = -1;

    public void setElectricTrap() {
        int trapPos = trapPos();
        if (trapPos != -1) {
            if (Dungeon.level.passable[trapPos] &&
                    Dungeon.level.map[trapPos] != Terrain.EXIT &&
                    Dungeon.level.map[trapPos] != Terrain.ENTRANCE) {

                Level.set(trapPos, Terrain.SECRET_TRAP);

                ShockingTrap electricTrap = new ShockingTrap();
                Dungeon.level.setTrap(electricTrap, trapPos);

                Dungeon.level.discover(trapPos);
                CellEmitter.get(trapPos).burst(Speck.factory(Speck.LIGHT), 3);
            }

            trapcooldown = 20;

            Sample.INSTANCE.play(Assets.Sounds.LIGHTNING);
            Sample.INSTANCE.play(Assets.Sounds.TELEPORT);

            if (Dungeon.level.heroFOV[enemy.pos]) {
                Dungeon.hero.interrupt();
            }
        }
        next();
    }

    @Override
    protected boolean act() {
        trapcooldown--;

        AiState lastState = state;
        boolean result = super.act();

        if (enemy != null && enemySeen) {
            lastEnemyPos = enemy.pos;
        } else {
            lastEnemyPos = Dungeon.hero.pos;
        }

        return result;
    }

    public int trapPos() {
        Char enemy = this.enemy;
        if (enemy == null) return -1;

        Ballistica b;
        // 적이 이동하는 방향에 함정을 설치하거나, 이동하지 않으면 자신과 적 사이에 설치
        if (lastEnemyPos == enemy.pos) {
            b = new Ballistica(enemy.pos, pos, Ballistica.WONT_STOP);
        } else {
            b = new Ballistica(lastEnemyPos, enemy.pos, Ballistica.WONT_STOP);
        }

        int collisionIndex = 0;
        for (int i = 0; i < b.path.size(); i++) {
            if (b.path.get(i) == enemy.pos) {
                collisionIndex = i;
                break;
            }
        }

        // 맵 가장자리에서 경로에 더 이상 셀이 없는 경우
        if (b.path.size() <= collisionIndex + 1) {
            return -1;
        }

        int trapPos = b.path.get(collisionIndex + 1);

        // 벽을 통해 함정을 설치하지 않도록 확인
        int projectilePos = new Ballistica(pos, trapPos, Ballistica.STOP_TARGET | Ballistica.STOP_SOLID).collisionPos;

        if (trapPos != enemy.pos && projectilePos == trapPos && Dungeon.level.passable[trapPos]) {
            return trapPos;
        } else {
            return -1;
        }
    }

    @Override
    public void move(int step, boolean travelling) {
        if (travelling && enemySeen && trapcooldown <= 0 && lastEnemyPos != -1) {
            if (trapPos() != -1) {
                setElectricTrap();
            }
        }
        super.move(step, travelling);
    }

    private static final String TRAPCOOLDOWN = "TRAPCOOLDOWN";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(TRAPCOOLDOWN, trapcooldown);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        trapcooldown = bundle.getInt(TRAPCOOLDOWN);
    }


}
