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

package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Bcopter;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Elemental;
import com.shatteredpixel.shatteredpixeldungeon.effects.Beam;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ElmoParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.FlameParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.RainbowParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SmokeParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SparkParticle;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Callback;

public class BcopterSprite extends MobSprite {

    protected int boltType;

    public BcopterSprite() {
        super();

        texture(Assets.Sprites.BTANK);

        TextureFilm frames = new TextureFilm(texture, 28, 20);

        idle = new Animation(6, true);
        idle.frames(frames, 0, 0, 0, 2, 0, 0, 0, 2);

        run = new Animation(15, true);
        run.frames(frames, 0, 1, 2, 0, 1, 2);

        attack = new Animation(19, false);
        attack.frames(frames, 0, 2, 0);

        die = new Animation(12, false);
        die.frames(frames, 3);

        scale.set(0.7f);

        play(idle);
    }

    public void zap( int cell ) {
        super.zap( cell );


        {
            boltType = MagicMissile.FIRE;
        }

        MagicMissile.boltFromChar( parent,
                boltType,
                this,
                cell,
                new Callback() {
                    @Override
                    public void call() {
                        ((Bcopter)ch).onZapComplete();
                    }
                } );
        Sample.INSTANCE.play( Assets.Sounds.BLAST, 1f, 0.85f );
    }

    @Override
    public void die() {
        emitter().burst(BlastParticle.FACTORY, 8);
        emitter().burst(SmokeParticle.FACTORY, 8);

        super.die();
    }

    @Override
    public void onComplete( Animation anim ) {
        if (anim == zap) {
            idle();
        }
        super.onComplete( anim );
    }




}
