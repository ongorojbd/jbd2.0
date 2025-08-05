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
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.SpwSoldier;
import com.shatteredpixel.shatteredpixeldungeon.effects.Beam;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SmokeParticle;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;

public class SpwSoldierSprite extends MobSprite {


    public SpwSoldierSprite() {
        super();

        texture( Assets.Sprites.SPWSOLDIER );

        TextureFilm frames = new TextureFilm( texture, 15, 15 );

        idle = new Animation( 1, true );
        idle.frames( frames, 0, 0, 1, 2, 1, 0 );

        run = new Animation( 15, true );
        run.frames( frames, 3, 4, 5, 6, 7, 8 );

        attack = new Animation( 15, false );
        attack.frames( frames, 9, 10, 11, 10, 9, 0 );

        zap = attack.clone();

        die = new Animation( 20, false );
        die.frames( frames, 12, 13, 14, 15, 16);

        play( idle );
    }

    @Override
    public void zap( int pos ) {
        idle();
        play(zap);
        turnTo( ch.pos , pos );
        if (Actor.findChar(pos) != null){
            parent.add(new Beam.SPWRay(center(), Actor.findChar(pos).sprite.center()));
        } else {
            parent.add(new Beam.SPWRay(center(), DungeonTilemap.raisedTileCenterToWorld(pos)));
        }
        ((SpwSoldier)ch).onZapComplete();
    }

}
