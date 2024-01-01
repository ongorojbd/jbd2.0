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

package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Btank;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Warlock;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SmokeParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

public class BtankSprite extends MobSprite {

    int cellToAttack;

    public BtankSprite() {
        super();

        texture( Assets.Sprites.BCOPTER );

        TextureFilm frames = new TextureFilm( texture, 40, 35 );

        idle = new Animation( 15, true );
        idle.frames( frames, 0, 1, 2, 0, 1 ,2 );

        run = new Animation( 15, true );
        run.frames( frames, 0, 1, 2, 0, 1 ,2 );

        attack = new Animation( 19, false );
        attack.frames( frames, 0, 2, 0);

        zap = idle.clone();

        die = new Animation( 12, false );
        die.frames( frames, 3);

        scale.set(0.45f);

        play( idle );
    }


    public void zap( int cell ) {

        super.zap( cell );

        MagicMissile.boltFromChar( parent,
                MagicMissile.FIRE,
                this,
                cell,
                new Callback() {
                    @Override
                    public void call() {
                        ((Btank)ch).onZapComplete();
                    }
                } );
        Sample.INSTANCE.play( Assets.Sounds.BLAST, 1f, 0.85f );
    }

    @Override
    public void onComplete( Animation anim ) {
        if (anim == zap) {
            idle();
        }
        super.onComplete( anim );
    }

    @Override
    public void die() {
        emitter().burst(BlastParticle.FACTORY, 8);
        emitter().burst(SmokeParticle.FACTORY, 8);

        super.die();
    }



}