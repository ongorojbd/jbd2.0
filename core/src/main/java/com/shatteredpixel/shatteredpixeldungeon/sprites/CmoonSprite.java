package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class CmoonSprite extends MobSprite {

    private int cellToAttack;

    public CmoonSprite() {
        super();

        texture( Assets.Sprites.KOUSAKU);

        TextureFilm frames = new TextureFilm( texture, 12, 15 );

        idle = new Animation( 1, true );
        idle.frames( frames, 21, 21, 21, 22, 21, 21, 21, 22 );

        run = new Animation( 20, true );
        run.frames( frames, 23, 24, 25, 26, 27, 28 );

        attack = new Animation( 15, false );
        attack.frames( frames, 29, 30, 31, 21 );

        zap = attack.clone();

        die = new Animation( 20, false );
        die.frames( frames, 32, 33, 34, 35, 36);

        play( idle );
    }
}
