package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Abomination2;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.ZombieBrute3;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

public class Abomination2Sprite extends MobSprite {

    public Abomination2Sprite() {
        super();

        texture(Assets.Sprites.ABOMINATION2);

        TextureFilm frames = new TextureFilm(texture, 19, 16);

        idle = new Animation(4, true);
        idle.frames(frames, 0, 1, 2, 1, 0, 0, 1, 2, 1, 0, 3, 3);

        run = new Animation(8, true);
        run.frames(frames, 4, 5, 6);

        attack = new Animation(8, false);
        attack.frames(frames, 7, 8, 9);

        die = new Animation(12, false);
        die.frames(frames, 10, 11, 12, 13);

        zap = new Animation(4, true);
        zap.frames(frames, 1, 9, 8);

        play(idle);
    }

    public void zap( int cell ) {

        super.zap( cell );

        MagicMissile.boltFromChar( parent,
                MagicMissile.SHADOW,
                this,
                cell,
                new Callback() {
                    @Override
                    public void call() {
                        ((Abomination2)ch).onZapComplete();
                    }
                } );
        Sample.INSTANCE.play( Assets.Sounds.PUFF );
    }

    @Override
    public void onComplete( Animation anim ) {
        if (anim == zap) {
            idle();
        }
        super.onComplete( anim );
    }

}
