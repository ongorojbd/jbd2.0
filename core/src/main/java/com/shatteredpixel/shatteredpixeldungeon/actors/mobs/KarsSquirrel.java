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
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SquirrelSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class KarsSquirrel extends Mob {

    {
        spriteClass = SquirrelSprite.class;

        HP = HT = 100;
        defenseSkill = 12;
        EXP = 0;
        maxLvl = -9;

        baseSpeed = 1.5f; // 빠른 이동 속도

        properties.add(Property.UNDEAD);
        properties.add(Property.DEMONIC);
    }

    private boolean berserkMode = false;

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

    @Override
    protected boolean act() {

        sprite.add(CharSprite.State.KARS);
        if (enemy == null || !enemy.isAlive()) {
            enemy = Dungeon.hero;
        }
        beckon(Dungeon.hero.pos);

        // HP가 절반 이하면 광분 모드 활성화
        if (HP <= HT / 2 && !berserkMode) {
            berserkMode = true;

            // 광분 모드에서 아드레날린 부여 (공격 속도 증가)
            Buff.affect(this, Adrenaline.class, 10f);
        }
        return super.act();
    }

    @Override
    public int attackProc( Char enemy, int damage ) {
        damage = super.attackProc( enemy, damage );
        if (Random.Int( 3 ) == 0) {
            Buff.affect(enemy, Bleeding.class ).set(5);
        }

        return damage;
    }

    @Override
    public void die(Object cause) {

        super.die(cause);

        sprite.remove(CharSprite.State.KARS);

        if (Dungeon.level.heroFOV[pos]) {
            Sample.INSTANCE.play(Assets.Sounds.SP, 1f, 3.3f);
            Sample.INSTANCE.play(Assets.Sounds.BURNING);
        }

    }
}

