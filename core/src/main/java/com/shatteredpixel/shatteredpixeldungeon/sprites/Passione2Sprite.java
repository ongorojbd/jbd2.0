/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2021 Evan Debenham
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
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

public abstract class Passione2Sprite extends MobSprite {

    private int cellToAttack;

    public Passione2Sprite() {
        super();

        texture( Assets.Sprites.PASSIONE );

        TextureFilm frames = new TextureFilm( texture, 12, 15 );

        int c = texOffset();

        idle = new Animation( 1, true );
        idle.frames( frames, 0+c, 0+c, 0+c, 1+c, 0+c, 0+c, 1+c, 1+c );

        run = new Animation( 15, true );
        run.frames( frames, 2+c, 3+c, 4+c, 5+c, 6+c, 7+c );

        attack = new Animation( 15, false );
        attack.frames( frames, 13+c, 14+c, 15+c, 0+c );

        zap = attack.clone();

        die = new Animation( 20, false );
        die.frames( frames, 8+c, 9+c, 10+c, 11+c, 12+c );


        play( idle );
    }

    protected abstract int texOffset();

    public static class Mi extends Passione2Sprite {
        @Override
        protected int texOffset() {
            return 0;
        }
    }

    @Override
    public void attack( int cell ) {
        if (!Dungeon.level.adjacent( cell, ch.pos )) {

            cellToAttack = cell;
            turnTo( ch.pos , cell );
            play( zap );

        } else {

            super.attack( cell );

        }
    }

    @Override
    public void onComplete( Animation anim ) {
        if (anim == zap) {
            idle();
            Sample.INSTANCE.play( Assets.Sounds.HIT_STAB );
            ((MissileSprite)parent.recycle( MissileSprite.class )).
                    reset( this, cellToAttack, new Passione2Sprite.SoldierShot2(), new Callback() {

                        @Override
                        public void call() {
                            ch.onAttackComplete();
                        }
                    } );
        } else {
            super.onComplete( anim );
        }
    }

    public class SoldierShot2 extends Item {
        {
            image = ItemSpriteSheet.FISHING_SPEAR;
        }
    }

}
