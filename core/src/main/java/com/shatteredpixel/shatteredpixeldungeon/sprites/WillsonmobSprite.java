package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SmokeParticle;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class WillsonmobSprite extends MobSprite {

    public WillsonmobSprite() {
        super();

        texture( Assets.Sprites.WILLSONMOB );

        TextureFilm frames = new TextureFilm( texture, 23, 10 );

        idle = new Animation( 15, true );
        idle.frames( frames, 0, 1, 0, 0 );

        run = idle.clone();
        attack = idle.clone();

        die = new Animation( 20, false );
        die.frames( frames, 1 );

        play( idle );
        curFrame = Random.Int( curAnim.frames.length );

        scale.set(1.2f);
    }

    @Override
    public void die() {
        Sample.INSTANCE.play(Assets.Sounds.BLAST);
        emitter().burst(BlastParticle.FACTORY, 20);
        emitter().burst(SmokeParticle.FACTORY, 20);
        super.die();
    }
}