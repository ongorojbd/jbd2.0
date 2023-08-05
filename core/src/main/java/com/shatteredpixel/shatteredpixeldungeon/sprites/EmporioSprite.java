package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;

public class EmporioSprite extends MobSprite {
    public EmporioSprite(){
        super();

        texture( Assets.Sprites.EMPORIO );

        TextureFilm frames = new TextureFilm( texture, 10, 14 );

        idle = new MovieClip.Animation( 1, true );
        idle.frames( frames, 0, 0, 0, 1, 0, 0, 1, 1 );

        die = new MovieClip.Animation( 1, true );
        die.frames( frames, 0 );

        play( idle );
    }
}
