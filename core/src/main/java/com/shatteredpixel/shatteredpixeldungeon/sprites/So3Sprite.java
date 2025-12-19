package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;

public class So3Sprite extends MobSprite {

    public So3Sprite() {
        super();

        texture( Assets.Sprites.SO1 ); // SO3 스프라이트가 없으므로 SO1 사용

        TextureFilm frames = new TextureFilm( texture, 10, 15 );

        idle = new MovieClip.Animation( 1, true );
        idle.frames( frames, 0, 0, 0, 1, 0, 0, 1, 1 );

        play( idle );
    }
}

