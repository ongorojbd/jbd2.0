package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class KousakuSprite extends MobSprite {

    private int cellToAttack;

    public KousakuSprite() {
        super();

        texture( Assets.Sprites.KOUSAKU);

        TextureFilm frames = new TextureFilm( texture, 12, 15 );

        idle = new Animation( 1, true );
        idle.frames( frames, 0, 0, 0, 1, 0, 0, 1, 1 );

        run = new Animation( 20, true );
        run.frames( frames, 2, 3, 4, 5, 6, 7 );

        attack = new Animation( 15, false );
        attack.frames( frames, 8, 9, 10, 0 );

        zap = attack.clone();

        die = new Animation( 20, false );
        die.frames( frames, 11, 12, 13, 14, 15);

        play( idle );
    }
}
