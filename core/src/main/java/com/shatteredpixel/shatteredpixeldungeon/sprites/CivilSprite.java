package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Alchemist;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Civil;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;
import com.watabou.utils.Callback;

public class CivilSprite extends MobSprite {

    public CivilSprite() {
        super();

        texture( Assets.Sprites.CIVIL );

        TextureFilm frames = new TextureFilm( texture, 12, 17 );

        idle = new MovieClip.Animation( 1, true );
        idle.frames( frames, 0, 0, 0, 1, 0, 0, 1, 1 );

        run = new MovieClip.Animation( 20, true );
        run.frames( frames, 2, 3, 4, 5, 6, 7 );

        attack = new MovieClip.Animation( 15, false );
        attack.frames( frames, 13, 14, 15, 0 );

        zap = attack.clone();

        die = new MovieClip.Animation( 20, false );
        die.frames( frames, 8, 9, 10, 11, 12);

        play( idle );
    }

    public void zap( int cell ) {

        turnTo( ch.pos , cell );
        play( zap );

        ((MissileSprite)parent.recycle( MissileSprite.class )).
                reset( this, cell, new CivilSprite.ScorpioShot(), new Callback() {
                    @Override
                    public void call() {
                        ((Civil)ch).onZapComplete();
                    }
                } );
    }

    public class ScorpioShot extends Item {
        {
            image = ItemSpriteSheet.POTION_CRIMSON;
        }
    }

    @Override
    public void onComplete( Animation anim ) {
        if (anim == zap) {
            idle();
        }
        super.onComplete( anim );
    }

}