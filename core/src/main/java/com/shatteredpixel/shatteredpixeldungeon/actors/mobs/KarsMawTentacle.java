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
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ShrGas;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.TentacleSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class KarsMawTentacle extends Mob {

    {
        spriteClass = TentacleSprite.class;

        HP = HT = 30;
        defenseSkill = 8;
        EXP = 0;
        maxLvl = -9;

        immunities.add( ShrGas.class );
        properties.add(Property.UNDEAD);
        properties.add(Property.DEMONIC);
    }

    @Override
    public boolean act() {
        sprite.add(CharSprite.State.KARS);
        if (enemy == null || !enemy.isAlive()) {
            enemy = Dungeon.hero;
        }
        beckon(Dungeon.hero.pos);

        return super.act();
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 50, 60 );
    }
    @Override
    public int attackSkill( Char target ) {
        return 45;
    }

    @Override
    public int drRoll() {
        return super.drRoll() + Random.NormalIntRange(5, 20);
    }

    // 3칸 이내 시야가 트이면 공격 가능 (촉수 도달 범위)
    @Override
    protected boolean canAttack(Char enemy) {
        return Dungeon.level.distance(pos, enemy.pos) <= 3
                && new Ballistica(pos, enemy.pos, Ballistica.PROJECTILE).collisionPos == enemy.pos;
    }

    @Override
    public void die(Object cause) {

        super.die(cause);

        sprite.remove(CharSprite.State.KARS);

        if (Dungeon.level.heroFOV[pos]) {
            Sample.INSTANCE.play(Assets.Sounds.BURNING);
        }

    }
}
