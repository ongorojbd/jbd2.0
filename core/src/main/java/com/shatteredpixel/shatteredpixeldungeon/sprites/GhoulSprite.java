/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2024 Evan Debenham
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
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.watabou.noosa.TextureFilm;

public abstract class GhoulSprite extends MobSprite {

    private Animation crumple;

    public GhoulSprite() {
        super();

        texture(Assets.Sprites.GHOUL);

        TextureFilm frames = new TextureFilm(texture, 12, 15);

        int c = texOffset();

        idle = new Animation(2, true);
        idle.frames(frames, 0 + c, 0 + c, 0 + c, 1 + c);

        run = new Animation(12, true);
        run.frames(frames, 2 + c, 3 + c, 4 + c, 5 + c, 6 + c, 7 + c);

        attack = new Animation(12, false);
        attack.frames(frames, 0 + c, 8 + c, 9 + c);

        crumple = new Animation(15, false);
        crumple.frames(frames, 0 + c, 10 + c, 11 + c, 12 + c);

        die = new Animation(15, false);
        die.frames(frames, 0 + c, 10 + c, 11 + c, 12 + c, 13 + c);

        play(idle);
    }

    protected abstract int texOffset();

    public static class Blue extends GhoulSprite {
        @Override
        protected int texOffset() {
            return 0;
        }
    }

    public static class Green extends GhoulSprite {
        @Override
        protected int texOffset() {
            return 14;
        }
    }

    public static class Red extends GhoulSprite {
        @Override
        protected int texOffset() { return 28; }
    }

    public void crumple() {
        hideEmo();
        play(crumple);
    }

    @Override
    public void die() {
        if (curAnim == crumple) {
            //causes the sprite to not rise then fall again when dieing.
            die.frames[0] = die.frames[1] = die.frames[2] = die.frames[3];
        }

        emitter().burst( Speck.factory( Speck.WOOL ), 5 );
        super.die();
    }
}
