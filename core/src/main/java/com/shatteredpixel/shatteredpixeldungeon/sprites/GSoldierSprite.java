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
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Civil;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.GSoldier;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

public class GSoldierSprite extends MobSprite {
    private int cellToAttack;
    private Animation adjacent;

    public GSoldierSprite() {
        super();

        texture(Assets.Sprites.GSOLDIER);

        TextureFilm frames = new TextureFilm(texture, 16, 15);

        idle = new Animation(1, true);
        idle.frames(frames, 0, 0, 0, 1, 0, 0, 1, 1);

        run = new Animation(15, true);
        run.frames(frames, 5, 6, 7, 8, 9, 10);

        attack = new Animation(15, false);
        attack.frames(frames, 2, 3, 4, 0);

        zap = attack.clone();
        adjacent = attack.clone();

        die = new Animation(20, false);
        die.frames(frames, 11, 12, 13, 14, 15);

        play(idle);
    }

    @Override
    public void attack(int cell) {
        if (!Dungeon.level.adjacent(cell, ch.pos)) {

            cellToAttack = cell;
            turnTo(ch.pos, cell);
            play(adjacent);

        } else {

            super.attack(cell);

        }
    }

    public void zap(int cell) {

        turnTo(ch.pos, cell);
        play(zap);

        ((MissileSprite) parent.recycle(MissileSprite.class)).
                reset(this, cell, new GSoldierSprite.ScorpioShot(), new Callback() {
                    @Override
                    public void call() {
                        ((GSoldier) ch).onZapComplete();
                    }
                });
    }

    @Override
    public void onComplete(Animation anim) {
        if (anim == adjacent) {
            idle();
            Sample.INSTANCE.play(Assets.Sounds.MISS);
            ((MissileSprite) parent.recycle(MissileSprite.class)).
                    reset(this, cellToAttack, new GSoldierSprite.WillcShot(), new Callback() {

                        @Override
                        public void call() {
                            ch.onAttackComplete();
                        }
                    });
        } else if (anim == zap) {
            idle();
        } else {
            super.onComplete(anim);
        }
    }

    public class WillcShot extends Item {
        {
            image = ItemSpriteSheet.FISHING_SPEAR;
        }
    }

    public class ScorpioShot extends Item {
        {
            image = ItemSpriteSheet.TBOMB;
        }
    }


}
