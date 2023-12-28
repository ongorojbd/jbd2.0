/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2022 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class MihSprite extends MobSprite {

    private Animation stab;
    private Animation prep;
    private Animation leap;

    private int cellToAttack;
    private Emitter shadowParticles;

    private boolean alt = Random.Int(2) == 0;

    public MihSprite() {
        super();

        texture(Assets.Sprites.RIPPER);

        TextureFilm frames = new TextureFilm(texture, 16, 17);

        idle = new Animation(4, true);
        idle.frames(frames, 1, 0, 1, 2);

        run = new Animation(15, true);
        run.frames(frames, 3, 4, 5, 6, 7, 8);

        attack = new Animation(12, false);
        attack.frames(frames, 0, 9, 10, 9);

        stab = new Animation(12, false);
        stab.frames(frames, 0, 9, 11, 9);

        prep = new Animation(1, true);
        prep.frames(frames, 9);

        leap = new Animation(1, true);
        leap.frames(frames, 12);

        die = new Animation(15, false);
        die.frames(frames, 1, 13, 14, 15, 16);

        scale.set(0.85f);

        play(idle);
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
    public void attack(int cell) {
        if (!Dungeon.level.adjacent(cell, ch.pos)) {

            cellToAttack = cell;
            turnTo(ch.pos, cell);
            play(zap);

        } else {

            super.attack(cell);

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
