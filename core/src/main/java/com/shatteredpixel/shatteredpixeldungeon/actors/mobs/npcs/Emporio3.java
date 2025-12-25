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

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.GravityChaosTracker;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Beast;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.EmporioSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Game;
import com.watabou.utils.Bundle;

public class Emporio3 extends NPC {

    {
        spriteClass = EmporioSprite.class;

        properties.add(Property.IMMOVABLE);
    }

    // GravityChaosTracker 쿨다운
    private int gravityCD = 9999;
    private boolean beastDied = false;  // Beast가 죽었는지 확인하는 플래그

    private static final String GRAVITY_CD = "gravity_cd";
    private static final String BEAST_DIED = "beast_died";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(GRAVITY_CD, gravityCD);
        bundle.put(BEAST_DIED, beastDied);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        gravityCD = bundle.getInt(GRAVITY_CD);
        beastDied = bundle.getBoolean(BEAST_DIED);
    }

    @Override
    protected boolean act() {
        // Beast가 살아있는지 확인
        boolean beastAlive = false;
        if (Dungeon.level != null) {
            for (Mob mob : Dungeon.level.mobs) {
                if (mob instanceof Beast && mob.isAlive()) {
                    beastAlive = true;
                    break;
                }
            }
        }

        // Beast가 죽었는지 확인 (beastAlive가 false이고 이전에 살아있었다면 죽은 것)
        if (!beastAlive && !beastDied && Statistics.johnnyquest) {
            beastDied = true;
            gravityCD = 6;
        }

        // Beast가 살아있으면 GravityChaosTracker를 사용하지 않음
        if (beastAlive) {
            return super.act();
        }

        if (Statistics.johnnyquest) {
            // Beast가 죽은 후에만 GravityChaosTracker 사용
            if (beastDied) {
                // 쿨다운 감소
                if (gravityCD > 0) gravityCD--;

                if (gravityCD <= 0 && Dungeon.hero != null) {
                    Buff.affect(hero, LockedFloor.class);
                    InterlevelScene.mode = InterlevelScene.Mode.RETURN;
                    InterlevelScene.returnDepth = 5;
                    InterlevelScene.returnBranch = 1;
                    InterlevelScene.returnPos = -2;
                    Game.switchScene(InterlevelScene.class);
                }
            }
        }

        return super.act();
    }

    @Override
    public void beckon(int cell) {
        //do nothing
    }

    @Override
    protected boolean getCloser(int target) {
        return true;
    }

    @Override
    protected boolean getFurther(int target) {
        return true;
    }

    @Override
    public boolean add(Buff buff) {
        return false;
    }

    @Override
    public boolean interact(Char c) {

        sprite.turnTo(pos, c.pos);

        if (c != Dungeon.hero) {
            return true;
        }

        if (Statistics.duwang == 34) {

        } else {
            GLog.p(Messages.get(Emporio.class, "q"));
        }


        return true;
    }

    @Override
    public int defenseSkill(Char enemy) {
        return INFINITE_EVASION;
    }

    @Override
    public void damage(int dmg, Object src) {
    }

    @Override
    public boolean reset() {
        return true;
    }

}
