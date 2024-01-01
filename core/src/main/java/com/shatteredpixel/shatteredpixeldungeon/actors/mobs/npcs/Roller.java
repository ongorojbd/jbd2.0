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

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SmokeParticle;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.RollerSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;

public class Roller extends NPC {

    {
        spriteClass = RollerSprite.class;
    }

    public float lifespan;

    private boolean initialized = false;

    @Override
    protected boolean act() {
        if (initialized) {
            HP = 0;

            for (int i : PathFinder.NEIGHBOURS9) {
                if (!Dungeon.level.solid[this.pos + i]) {
                    CellEmitter.center(this.pos+i).burst(BlastParticle.FACTORY, 15);
                    CellEmitter.center(this.pos+i).burst(SmokeParticle.FACTORY, 4);
                }
            }

            Sample.INSTANCE.play(Assets.Sounds.BLAST);

            destroy();
            sprite.die();
            GLog.n(Messages.get(Roller.class, "notice"));

        } else {
            initialized = true;
            spend( lifespan + 1 );
        }
        return true;
    }

    @Override
    public void beckon (int cell) {
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
    public boolean add( Buff buff ) {
        return false;
    }

    @Override
    public int defenseSkill(Char enemy) {
        return INFINITE_EVASION;
    }

    @Override
    public void damage( int dmg, Object src ) {
    }

    @Override
    public boolean interact(Char c) {
        if (c == Dungeon.hero) {
            Dungeon.hero.spendAndNext(1f);
            //sheep summoned by woolly bomb can be dispelled by interacting
            if (lifespan >= 20){
                spend(-cooldown());
            }
        }
        return true;
    }

    @Override
    public void die( Object cause ) {

        for (int i : PathFinder.NEIGHBOURS9) {
            if (!Dungeon.level.solid[this.pos + i]) {
                CellEmitter.center(this.pos+i).burst(BlastParticle.FACTORY, 15);
                CellEmitter.center(this.pos+i).burst(SmokeParticle.FACTORY, 4);
            }
        }

        super.die( cause );

    }


    private static final String LIFESPAN = "lifespan";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(LIFESPAN, lifespan);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        lifespan = bundle.getInt(LIFESPAN);
    }
}