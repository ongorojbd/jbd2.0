package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public abstract class SpiderMindSprite extends MobSprite {

    public SpiderMindSprite() {
        super();

        texture( Assets.Sprites.SPIDERMIND );

        TextureFilm frames = new TextureFilm(texture, 16, 16);

        int c = texOffset();

        idle = new Animation(10, true);
        idle.frames(frames, 0+c, 0+c, 0+c, 0+c, 0+c, 1+c, 0+c, 1+c);

        run = new Animation(15, true);
        run.frames(frames, 0+c, 2+c, 3+c, 4+c);

        attack = new Animation(12, false);
        attack.frames(frames, 4+c, 5+c, 6+c, 7+c);

        die = new Animation(12, false);
        die.frames(frames, 8+c, 9+c, 10+c, 11+c);

        play(idle);
    }

    @Override
    public int blood() {
        return 0xFFBFE5B8;
    }

    protected abstract int texOffset();

    public static class Alpha extends SpiderMindSprite {
        @Override
        protected int texOffset() {
            return 0;
        }
    }

    public static class Beta extends SpiderMindSprite {
        @Override
        protected int texOffset() {
            return 16;
        }
    }

}