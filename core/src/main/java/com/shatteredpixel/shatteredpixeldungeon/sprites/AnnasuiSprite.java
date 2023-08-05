package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;

public class AnnasuiSprite extends MobSprite {

    public AnnasuiSprite() {
        super();

        texture( Assets.Sprites.ANNASUI );

        TextureFilm frames = new TextureFilm( texture, 11, 16 );

        idle = new MovieClip.Animation( 1, true );
        idle.frames( frames, 0, 0, 0, 1, 0, 0, 1, 1 );

        play( idle );
    }
}
