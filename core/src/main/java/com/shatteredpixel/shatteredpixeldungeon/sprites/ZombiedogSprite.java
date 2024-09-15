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
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.FlameParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.HamonPartice;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.watabou.noosa.TextureFilm;
import com.watabou.utils.Callback;

public class ZombiedogSprite extends MobSprite {

    private int cellToAttack;

    public ZombiedogSprite() {
        super();

        texture( Assets.Sprites.ZOMBIEDOG );

        TextureFilm frames = new TextureFilm( texture, 18, 16 );

        idle = new Animation( 2, true );
        idle.frames( frames, 0, 0, 0, 0, 0, 1, 1);

        run = new Animation( 12, true );
        run.frames( frames, 0, 2, 3, 4, 5, 6 );

        attack = new Animation( 12, false );
        attack.frames( frames,  7, 8, 9, 0 );

        die = new Animation( 20, false );
        die.frames( frames, 10, 11, 12, 13, 14, 15);

        play( idle );
    }

    @Override
    public void play( Animation anim ) {
        if (anim == die) {
            emitter().burst( FlameParticle.FACTORY, 20);
            emitter().burst( ShadowParticle.UP, 12 );
        }
        super.play( anim );
    }

}
