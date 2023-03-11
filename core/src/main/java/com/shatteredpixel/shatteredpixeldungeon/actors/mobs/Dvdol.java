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

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Dominion;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DvdolSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class Dvdol extends Mob {

    private static final float TIME_TO_ZAP = 2f;

    {
        spriteClass = DvdolSprite.class;

        HP = HT = 195;
        defenseSkill = 20;

        EXP = 0;

        properties.add(Property.BOSS);
        immunities.add(Dominion.class );

    }

    @Override
    public void die( Object cause ) {

        super.die( cause );

        Sample.INSTANCE.play( Assets.Sounds.GHOST);
        yell( Messages.get(this, "defeated") );
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 30, 40 );
    }

    @Override
    public int attackSkill( Char target ) {
        return 40;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 5);
    }

    @Override
    protected boolean canAttack(Char enemy) {
        return new Ballistica(pos, enemy.pos, Ballistica.MAGIC_BOLT).collisionPos == enemy.pos;
    }

    protected boolean doAttack(Char enemy) {

        if (Dungeon.level.adjacent(pos, enemy.pos)) {

            return super.doAttack(enemy);

        } else {

            if (sprite != null && (sprite.visible || enemy.sprite.visible)) {
                sprite.zap(enemy.pos);
                return false;
            } else {
                zap();
                return true;
            }
        }
    }

    //used so resistances can differentiate between melee and magical attacks
    public static class DarkBolt {
    }

    private void zap() {
        spend(TIME_TO_ZAP);

        if (hit(this, enemy, true)) {
            //TODO would be nice for this to work on ghost/statues too
            if (enemy == Dungeon.hero && Random.Int(0) == 0) {
                Buff.affect( enemy, Burning.class ).reignite( enemy, 1f );
                Sample.INSTANCE.play(Assets.Sounds.HIT);
            }

            int dmg = Random.NormalIntRange(15, 20);
            enemy.damage( dmg, new DarkBolt() );

            if (enemy == Dungeon.hero && !enemy.isAlive()) {
                Dungeon.fail(getClass());
            }
        } else {
            enemy.sprite.showStatus(CharSprite.NEUTRAL, enemy.defenseVerb());
        }
    }

    public void onZapComplete() {
        zap();
        next();
    }


}

