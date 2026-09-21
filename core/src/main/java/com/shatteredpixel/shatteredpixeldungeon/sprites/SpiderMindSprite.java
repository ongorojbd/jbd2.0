package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.EarthParticle;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.particles.Emitter;

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

    //Beta burrows (see actors.mobs.Beta). The sheet has no submerge frames, so while it is
    //underground the body is hidden and only a trail of earth particles shows where it is.
    public static class Beta extends SpiderMindSprite {

        private Emitter particles;
        private boolean submerged = false;

        @Override
        protected int texOffset() {
            return 16;
        }

        public void setSubmerge() {
            submerged = true;
            if (particles != null) particles.on = true;
        }

        public void setEmerge() {
            submerged = false;
            alpha(1f);
            if (particles != null) {
                particles.on = false;
                particles.revive();
            }
        }

        @Override
        public void link(Char ch) {
            super.link(ch);

            if (particles == null) {
                particles = emitter();
                particles.pour(EarthParticle.FACTORY, 0.06f);
                particles.on = false;
                particles.revive();
            }

            if (ch instanceof com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Beta
                    && ((com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Beta) ch).digging) {
                setSubmerge();
            }
        }

        @Override
        public void update() {
            super.update();

            //re-applied every frame: flashes and other tints reset the colour (alpha included)
            if (submerged) alpha(0f);
            if (particles != null) particles.visible = visible;
        }

        @Override
        public void die() {
            super.die();
            if (particles != null) particles.on = false;
        }

        @Override
        public void kill() {
            super.kill();
            if (particles != null) particles.killAndErase();
        }
    }

}