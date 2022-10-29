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

package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Vitaminc;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

public class VitamincSprite extends MobSprite {

    public VitamincSprite() {
        super();

        texture( Assets.Sprites.VITAMINC );

        TextureFilm frames = new TextureFilm( texture, 21, 19 );

        idle = new MovieClip.Animation( 3, true );
        idle.frames( frames, 0, 1 );

        run = idle.clone();

        attack = new MovieClip.Animation( 12, false );
        attack.frames( frames, 2, 3, 4 );

        zap = attack.clone();

        die = new MovieClip.Animation( 15, false );
        die.frames( frames, 5, 6, 7);

        play( idle );
    }

    public void zap( int cell ) {

        turnTo( ch.pos , cell );
        play( zap );

        MagicMissile.boltFromChar( parent,
                MagicMissile.RAINBOW,
                this,
                cell,
                new Callback() {
                    @Override
                    public void call() {
                        ((Vitaminc)ch).onZapComplete();
                    }
                } );
        Sample.INSTANCE.play( Assets.Sounds.ZAP );
    }

    @Override
    public void onComplete( MovieClip.Animation anim ) {
        if (anim == zap) {
            idle();
        }
        super.onComplete( anim );
    }
}