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

public class Zombie04Sprite extends MobSprite {

    private int cellToAttack;

    public Zombie04Sprite() {
        super();

        texture( Assets.Sprites.ZOMBIEP );

        TextureFilm frames = new TextureFilm( texture, 12, 15 );

        idle = new Animation( 1, true );
        idle.frames( frames, 63, 63, 63, 64, 63, 63, 64, 64 );

        run = new Animation( 20, true );
        run.frames( frames, 65, 66, 67, 68, 69, 70 );

        attack = new Animation( 15, false );
        attack.frames( frames, 71, 72, 73, 63 );

        zap = attack.clone();

        die = new Animation( 20, false );
        die.frames( frames, 74, 75, 76, 77, 78, 16);

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
