package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;

public class AbominationSprite extends MobSprite {

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

        zap = new Animation(4, true);
        zap.frames(frames, 1, 9, 8);

        play(idle);
    }
}
