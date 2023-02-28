package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Callback;

public class CmoonSprite extends MobSprite {

    private int cellToAttack;

    private Emitter shadowParticles;

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
            Sample.INSTANCE.play( Assets.Sounds.ATK_SPIRITBOW );
            ((MissileSprite)parent.recycle( MissileSprite.class )).
                    reset( this, cellToAttack, new CmoonSprite.CmoonShot(), new Callback() {

                        @Override
                        public void call() {
                            ch.onAttackComplete();
                        }
                    } );
        } else {
            super.onComplete( anim );
        }
    }

    public class CmoonShot extends Item {
        {
            image = ItemSpriteSheet.SHURIKEN;
        }
    }

    @Override
    public void link(Char ch) {
        super.link(ch);

        shadowParticles = centerEmitter();
        shadowParticles.autoKill = true;
        shadowParticles.pour(MagicMissile.WardParticle.UP, 0.05f);
        shadowParticles.on = true;

    }

    @Override
    public void update() {
        super.update();
        if (shadowParticles != null) {
            shadowParticles.pos(center());
            shadowParticles.visible = visible;
        }
    }

    @Override
    public void die() {
        super.die();
        if (shadowParticles != null) {
            shadowParticles.on = false;
        }


    }
}
