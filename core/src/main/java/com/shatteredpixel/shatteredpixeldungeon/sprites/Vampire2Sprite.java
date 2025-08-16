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
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.FlameParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.watabou.noosa.TextureFilm;

public class Vampire2Sprite extends MobSprite {

    private Animation cast;
    private Animation summoning;
    private Animation blink;

    public Vampire2Sprite() {
        super();

        texture( Assets.Sprites.VAMPIRE2 );

        TextureFilm frames = new TextureFilm( texture, 17, 16 );

        idle = new Animation( 1, true );
        idle.frames( frames, 0, 0, 0, 1, 0, 0, 1, 1 );

        run = new Animation( 15, true );
        run.frames( frames, 2, 3, 4, 5, 6, 7 );

        attack = new Animation( 10, false );
        attack.frames( frames, 8, 9, 10 );

        die = new Animation( 20, false );
        die.frames( frames,  15, 16, 17, 18, 19 );

        cast = new Animation( 10, false );
        cast.frames( frames, 11, 12, 13, 14 );

        blink = new Animation( 10, false );
        blink.frames( frames, 11, 12, 13);

        summoning = new Animation( 10, true );
        summoning.frames( frames,  13, 14 );

        play( idle );

    }

    @Override
    public void play( Animation anim ) {
        if (anim == die) {
            emitter().burst( FlameParticle.FACTORY, 20);
            emitter().burst( ShadowParticle.UP, 12 );
        }
        super.play( anim );
    }


    public void summon(int from, int to ) {
        turnTo( from , to );
        play(summoning);
    }
    public void blink( int from, int to ) {
        place( to );
        play(blink);
        turnTo( from , to );
        isMoving = true;
        ch.onMotionComplete();
    }

}
