package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.PointF;

public class CrazydiamondSprite extends MobSprite {

    private static final int FRAME_WIDTH	= 10;
    private static final int FRAME_HEIGHT	= 15;

    public CrazydiamondSprite() {
        super();

        texture( Assets.Sprites.CRAZYDIAMOND );

        TextureFilm frames = new TextureFilm( texture, 10, 15 );

        idle = new MovieClip.Animation( 2, true );
        idle.frames( frames, 0, 0, 0, 0 );

        run = new MovieClip.Animation( 12, true );
        run.frames( frames, 0, 0, 0, 0, 0 );

        attack = new MovieClip.Animation( 15, false );
        attack.frames( frames, 0, 0, 0, 0, 0 );

        die = new MovieClip.Animation( 10, false );
        die.frames( frames, 0, 0, 0, 0 );

        play( idle );
    }

    @Override
    public void resetColor() {
        super.resetColor();
        alpha(0.6f);
    }

    @Override
    public void bloodBurstA(PointF from, int damage) {
        //do nothing
    }

    @Override
    public void die() {
        //don't interrupt current animation to start fading
        //this ensures the fake attack animation plays
        if (parent != null) {
            parent.add( new AlphaTweener( this, 0, 3f ) {
                @Override
                protected void onComplete() {
                    CrazydiamondSprite.this.killAndErase();
                }
            } );
        }
    }

    @Override
    public int blood() {
        return 0x99FFFF;
    }

}
