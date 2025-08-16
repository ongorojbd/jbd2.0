/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2025 Evan Debenham
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
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SparkParticle;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

public class CaesarZeppeliSprite extends MobSprite {

    public CaesarZeppeliSprite() {
        super();

        texture(Assets.Sprites.CAESAR);

        TextureFilm frames = new TextureFilm(texture, 12, 15);

        idle = new Animation(2, true);
        idle.frames(frames, 0, 0, 0, 1, 0, 0, 1, 1, 2, 1);

        run = new Animation(12, true);
        run.frames(frames, 3, 4, 5, 6, 7, 8);

        attack = new Animation(15, false);
        attack.frames(frames, 9, 10, 11, 0);

        zap = new Animation(10, false);
        zap.frames(frames, 12, 13, 14, 13, 12);

        die = new Animation(10, false);
        die.frames(frames, 15, 16, 17, 18, 19);

        play(idle);
    }

    @Override
    public void zap(int cell) {
        super.zap(cell);

        // Bubble effect for Caesar's attacks
        MagicMissile.boltFromChar(parent,
                MagicMissile.FROST,
                this,
                cell,
                new Callback() {
                    @Override
                    public void call() {
                        ((CaesarZeppeliSprite)CaesarZeppeliSprite.this).onZapComplete();
                    }
                });
        Sample.INSTANCE.play(Assets.Sounds.ZAP);
    }

    @Override
    public void attack(int cell) {
        super.attack(cell);
        
        // Spark effect for Hamon attacks
        if (parent != null) {
            emitter().burst(SparkParticle.FACTORY, 5);
        }
    }
    
    public void onZapComplete() {
        idle();
    }

    @Override
    public void onComplete(Animation anim) {
        if (anim == zap) {
            onZapComplete();
        }
        super.onComplete(anim);
    }

    @Override
    public int blood() {
        return 0xFF44D5EE;  // Light blue for Caesar's bubble theme
    }
}
