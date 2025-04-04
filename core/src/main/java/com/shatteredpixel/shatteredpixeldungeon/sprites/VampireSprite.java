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
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.FlameParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.watabou.noosa.TextureFilm;

public abstract class VampireSprite extends MobSprite {


    public VampireSprite() {
        super();

        texture( Assets.Sprites.VAMPIRE );

        TextureFilm frames = new TextureFilm( texture, 12, 16 );

        int c = texOffset();

        idle = new Animation( 1, true );
        idle.frames( frames, 0 + c, 0 + c, 0 + c, 1 + c, 0 + c, 0 + c, 1 + c, 1 + c );

        run = new Animation( 15, true );
        run.frames( frames, 2 + c, 3 + c, 4 + c, 5 + c, 6 + c, 7 + c );

        attack = new Animation( 15, false );
        attack.frames( frames, 8 + c, 9 + c, 10 + c, 0 + c );

        zap = attack.clone();

        die = new Animation( 20, false );
        die.frames( frames, 11 + c, 12 + c, 13 + c, 14 + c, 15 + c, 16 + c );

        play( idle );
    }

    protected abstract int texOffset();

    public static class Blue extends VampireSprite {
        @Override
        protected int texOffset() {
            return 0;
        }
    }

    public static class Green extends VampireSprite {
        @Override
        protected int texOffset() {
            return 17;
        }
    }

    public static class Red extends VampireSprite {
        @Override
        protected int texOffset() { return 34; }
    }
    public static class Yellow extends VampireSprite {
        @Override
        protected int texOffset() { return 51; }
    }


    @Override
    public void play( Animation anim ) {
        if (anim == die) {
            emitter().burst( FlameParticle.FACTORY, 20);
            emitter().burst( ShadowParticle.UP, 12 );
        }
        super.play( anim );
    }

}
