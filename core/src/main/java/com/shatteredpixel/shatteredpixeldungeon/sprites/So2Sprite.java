package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;

public class So2Sprite extends MobSprite {

    public So2Sprite() {
        super();

        texture( Assets.Sprites.SO2 );

        TextureFilm frames = new TextureFilm( texture, 10, 15 );

        idle = new MovieClip.Animation( 1, true );
        idle.frames( frames, 0, 0, 0, 1, 0, 0, 1, 1 );

        play( idle );
    }
}
