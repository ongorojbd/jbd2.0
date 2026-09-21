package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Abomination2;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

public class SlotSprite extends MobSprite {

    public SlotSprite() {
        super();

        texture(Assets.Sprites.SLOT);

        TextureFilm frames = new TextureFilm(texture, 18, 27);

        idle = new Animation(4, true);
        idle.frames(frames, 0);

        run = new Animation(8, true);
        run.frames(frames, 0);

        attack = new Animation(8, false);
        attack.frames(frames, 0);

        die = new Animation(12, false);
        die.frames(frames, 0);

        zap = new Animation(12, true);
        zap.frames(frames, 0);

        play(idle);

        scale.set(0.75f);
    }

}
