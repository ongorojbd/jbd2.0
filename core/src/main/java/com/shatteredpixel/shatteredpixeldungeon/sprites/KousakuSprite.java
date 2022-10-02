package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.DM300;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Splash;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SmokeParticle;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;

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


    @Override
    public void onComplete( Animation anim ) {

        super.onComplete( anim );

        if (anim == attack) {
            Sample.INSTANCE.play(Assets.Sounds.BLAST);
            emitter().burst( BlastParticle.FACTORY, 55 );
            emitter().burst(SmokeParticle.FACTORY, 4);
        }

        if (anim == die) {
            Sample.INSTANCE.play(Assets.Sounds.BLAST);
            emitter().burst( BlastParticle.FACTORY, 100 );
            emitter().burst(SmokeParticle.FACTORY, 4);
            killAndErase();
        }
    }

    @Override
    public void die() {
        Splash.at( center(), blood(), 100 );
        killAndErase();
    }
}
