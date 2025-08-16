/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2023 Evan Debenham
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
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.TankZombieSprite;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.watabou.noosa.Camera;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Bundle;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class ZombieTank extends Mob {

    {
        spriteClass = TankZombieSprite.class;

        HP = HT = 120;
        defenseSkill = 12;

        EXP = 9;
        maxLvl = 17;
        properties.add(Property.UNDEAD);
        properties.add(Property.DEMONIC);
    }

    private int smashCount = 0;

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 15, 20 );
    }

    @Override
    public int attackSkill( Char target ) {
        return 16;
    }

    @Override
    public int drRoll() {
        return super.drRoll() + Random.NormalIntRange(0, 10);
    }

    @Override
    public boolean attack( Char enemy, float dmgMulti, float dmgBonus, float accMulti ) {
        if (smashCount < 2) smashCount++;
        else {
            // 3번째 공격 시 주변 8칸 광역 피해
            for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
                int p = this.pos + PathFinder.NEIGHBOURS8[i];
                CellEmitter.get(p).start(Speck.factory(Speck.ROCK), 0.07f, 10);
                Char ch = Actor.findChar(p);
                if (ch != null && ch.isAlive()) {
                    int damage = Math.max(10, 30);
                    damage -= ch.drRoll();
                    ch.damage(Math.max(damage, 0), this);
                }
            }
            sprite.showStatus(CharSprite.WARNING, Messages.get(this, "ab"));
            Camera.main.shake(3, 0.5f);
            Sample.INSTANCE.play(Assets.Sounds.BLAST);
            smashCount = 0;
        }
        return super.attack(enemy, dmgMulti, dmgBonus, accMulti);
    }

    @Override
    public void die( Object cause ) {

        super.die( cause );

        if (Random.Int( 3 ) == 0) {
            Dungeon.level.drop( new Gold().quantity(Random.IntRange(45, 55)), pos ).sprite.drop();
        }

        if (Dungeon.level.heroFOV[pos]) {
            Sample.INSTANCE.play( Assets.Sounds.BONES,  Random.Float(1.2f, 0.9f) );
            Sample.INSTANCE.play(Assets.Sounds.BURNING);
        }

    }

    private static final String SMASH = "SMASH";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(SMASH, smashCount);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        smashCount = bundle.getInt(SMASH);
    }
}