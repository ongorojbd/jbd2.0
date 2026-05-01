package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

public class AbominationSprite extends MobSprite {

    private int cellToAttack;

    public AbominationSprite() {
        super();

        texture(Assets.Sprites.ABOMINATION);

        TextureFilm frames = new TextureFilm(texture, 19, 16);

        idle = new Animation(4, true);
        idle.frames(frames, 0, 1, 2, 1, 0, 0, 1, 2, 1, 0, 3, 3);

        run = new Animation(8, true);
        run.frames(frames, 4, 5, 6);

        attack = new Animation(8, false);
        attack.frames(frames, 7, 8, 9);

        die = new Animation(12, false);
        die.frames(frames, 10, 11, 12, 13);

        zap = new Animation(12, true);
        zap.frames(frames, 1, 9, 8);

        play(idle);
    }

    @Override
    public void attack( int cell ) {
        if (!Dungeon.level.adjacent( cell, ch.pos )) {

            cellToAttack = cell;
            turnTo( ch.pos , cell );
            play( zap );

        } else {

            super.attack( cell );

        }
    }

    @Override
    public void onComplete( Animation anim ) {
        if (anim == zap) {
            idle();
            Sample.INSTANCE.play( Assets.Sounds.ZAP );
            MagicMissile.boltFromChar(parent, MagicMissile.FIRE, this, cellToAttack, new Callback() {
                @Override
                public void call() {
                    ch.onAttackComplete();
                }
            } );
        } else {
            super.onComplete( anim );
        }
    }
}
