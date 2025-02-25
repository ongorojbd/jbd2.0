package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

public class Scorpio2Sprite extends MobSprite {

    private int cellToAttack;

    public Scorpio2Sprite() {
        super();

        texture( Assets.Sprites.SCORPIO3 );

        TextureFilm frames = new TextureFilm( texture, 12, 15 );

        idle = new Animation( 1, true );
        idle.frames( frames, 0, 0, 0, 1, 0, 0, 1, 1 );

        run = new Animation( 15, true );
        run.frames( frames, 2, 3, 4, 5, 6, 7 );

        attack = new Animation( 15, false );
        attack.frames( frames, 8, 9, 10, 0 );

        zap = attack.clone();

        die = new Animation( 20, false );
        die.frames( frames, 11, 12, 13, 14, 15);

        play( idle );
    }

    @Override
    public void attack( int cell ) {
        if (!Dungeon.level.adjacent( cell, ch.pos )) {

            cellToAttack = cell;
            zap(cell);

        } else {

            super.attack( cell );

        }
    }

    @Override
    public void onComplete( Animation anim ) {
        if (anim == zap) {
            idle();
            Sample.INSTANCE.play( Assets.Sounds.HIT_STAB );
            ((MissileSprite)parent.recycle( MissileSprite.class )).
                    reset( this, cellToAttack, new Scorpio2Sprite.ScorpioShot(), new Callback() {
                        @Override
                        public void call() {
                            ch.onAttackComplete();
                        }
                    } );
        } else {
            super.onComplete( anim );
        }
    }

    public class ScorpioShot extends Item {
        {
            image = ItemSpriteSheet.FISHING_SPEAR;
        }
    }

}
