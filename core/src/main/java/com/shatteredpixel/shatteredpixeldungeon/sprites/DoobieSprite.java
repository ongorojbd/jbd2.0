package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Abomination2;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.DoobieWah;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

public class DoobieSprite extends MobSprite {

    public DoobieSprite() {
        super();

        texture(Assets.Sprites.DOOBIE);

        TextureFilm frames = new TextureFilm(texture, 16, 16);

        idle = new Animation(10, true);
        idle.frames(frames, 0, 1, 2);

        run = new Animation(12, true);
        run.frames(frames, 0, 1, 2);

        attack = new Animation(15, false);
        attack.frames(frames, 3, 4, 5, 6, 7);

        die = new Animation(15, false);
        die.frames(frames, 8, 9, 10, 11, 12);

        play(idle);

        scale.set(DoobieWah.BASE_SCALE);

    }

    //re-read every frame rather than set on damage: the boss grows as its health drops, and
    //this way nothing that resets the sprite can leave it at the wrong size
    @Override
    public void update() {
        super.update();

        if (ch instanceof DoobieWah) {
            scale.set(((DoobieWah) ch).sizeScale());
        }
    }

}
