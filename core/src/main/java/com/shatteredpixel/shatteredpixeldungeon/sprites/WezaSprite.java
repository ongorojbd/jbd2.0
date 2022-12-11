package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.DM100;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Wezamob;
import com.shatteredpixel.shatteredpixeldungeon.effects.Lightning;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PointF;

public class WezaSprite extends MobSprite {

    public WezaSprite() {
        super();

        texture( Assets.Sprites.WEZA );

        TextureFilm frames = new TextureFilm( texture, 12, 15 );

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

    public void zap( int pos ) {

        Char enemy = Actor.findChar(pos);

        //shoot lightning from eye, not sprite center.
        PointF origin = center();
        if (flipHorizontal){
            origin.y -= 6*scale.y;
            origin.x -= 1*scale.x;
        } else {
            origin.y -= 8*scale.y;
            origin.x += 1*scale.x;
        }
        if (enemy != null) {
            parent.add(new Lightning(origin, enemy.sprite.destinationCenter(), (Wezamob) ch));
        } else {
            parent.add(new Lightning(origin, pos, (Wezamob) ch));
        }
        Sample.INSTANCE.play( Assets.Sounds.LIGHTNING );

        super.zap( ch.pos );
        flash();
    }

    @Override
    public void onComplete( Animation anim ) {
        if (anim == zap) {
            idle();
        }
        super.onComplete( anim );
    }



}
