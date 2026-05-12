/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2019 Evan Debenham
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
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.U2;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ChallengeParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.FlameParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.Door;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;

public class FishSprite extends MobSprite {

    public Animation dash;
    
    public FishSprite() {
        super();

        texture( Assets.Sprites.FISH );

        TextureFilm frames = new TextureFilm( texture, 20, 23 );

        idle = new Animation( 8, true );
        idle.frames( frames, 0, 1, 2, 3, 4, 5 );

        run = new Animation( 12, true );
        run.frames( frames, 6, 7, 8, 9 );

        dash = new Animation( 20, true );
        dash.frames( frames, 6, 7, 8, 9 );

        attack = new Animation( 15, false );
        attack.frames( frames, 10, 11, 12, 13, 14, 0);

        die = new Animation( 8, false );
        die.frames( frames, 15, 16, 17, 18, 19 );

        play( idle );

        scale.set(0.75f);
    }

    public void DashATile( int cell ) {

        PixelScene.shake(0.8f, 0.125f);

        //If doing the final step, make sure the golem doesnt overlap with any other actor
        if (((U2)ch).counter == ((U2)ch).route.dist) ((U2)ch).PushAway(cell);


        jump(ch.pos, cell, 0f, 0.125f, new com.watabou.utils.Callback() {
            @Override
            public void call() {
                if (Dungeon.level.map[ch.pos] == Terrain.OPEN_DOOR) {
                    Door.leave(ch.pos);
                }

                CellEmitter.get(ch.pos).burst(ChallengeParticle.FACTORY, 2);

                Char collided = Actor.findChar(cell);
                if (collided != null && !(collided instanceof U2)) {
                    Sample.INSTANCE.play(Assets.Sounds.HIT_STRONG);
                    if (collided.alignment != ch.alignment) {
                        int dmg = ((U2) ch).damageRoll();
                        dmg = Math.max(0, dmg - collided.drRoll());
                        collided.damage(Math.round(dmg * 1.2f), ch);
                        if (!collided.isAlive() && collided == Dungeon.hero) {
                            Dungeon.fail(ch.getClass());
                        }
                    }
                }

                ch.pos = cell;
                Dungeon.level.occupyCell(ch);

                jump(ch.pos, ch.pos, 0f, 0.1f, new com.watabou.utils.Callback() {
                    @Override
                    public void call() {

                        ((U2)ch).DashTile();

                    }
                });
            }
        });

        play( dash );

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