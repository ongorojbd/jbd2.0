/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2023 Evan Debenham
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
import com.watabou.noosa.TextureFilm;

public class RollerSprite extends MobSprite {


    public RollerSprite() {
        super();

        texture( Assets.Sprites.ROLLER );
        TextureFilm film = new TextureFilm( texture, 157, 144 );

        idle = new Animation( 25, false );
        idle.frames( film,  0, 1, 2, 3, 4, 5 );

        die = new Animation( 20, false );
        die.frames( film, 6 );

        run = idle.clone();

        attack = idle.clone();

        scale.set(0.2f);

        idle();
    }

    @Override
    public void play(Animation anim) {
        if (isMoving && anim != run){
            synchronized (this){
                isMoving = false;
                notifyAll();
            }
        }
        super.play(anim);
    }

    @Override
    public void onComplete( Animation anim ) {
        super.onComplete( anim );
    }
}
