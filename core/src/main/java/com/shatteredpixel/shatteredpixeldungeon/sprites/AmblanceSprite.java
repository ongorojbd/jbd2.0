package com.shatteredpixel.shatteredpixeldungeon.sprites;

        import com.shatteredpixel.shatteredpixeldungeon.Assets;
        import com.watabou.noosa.TextureFilm;
        import com.watabou.utils.Random;

public class AmblanceSprite extends MobSprite {

    public AmblanceSprite() {
        super();

        texture( Assets.Sprites.JOJO );

        TextureFilm frames = new TextureFilm( texture, 26, 21 );

        idle = new Animation( 8, true );
        idle.frames( frames, 0, 1, 2, 3 );

        run = idle.clone();
        attack = idle.clone();

        die = new Animation( 20, false );
        die.frames( frames, 0 );

        play( idle );
        curFrame = Random.Int( curAnim.frames.length );
    }
}