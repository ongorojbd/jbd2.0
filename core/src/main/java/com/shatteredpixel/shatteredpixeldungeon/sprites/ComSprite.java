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

public class ComSprite extends MobSprite {

    private int cellToAttack;

    private Emitter shadowParticles;

    public ComSprite() {
        super();

        texture( Assets.Sprites.COM);

        TextureFilm frames = new TextureFilm( texture, 15, 13 );

        idle = new Animation( 14, true );
        idle.frames( frames, 0, 1, 2, 3, 4, 3, 2, 1 );

        play( idle );
    }
}
