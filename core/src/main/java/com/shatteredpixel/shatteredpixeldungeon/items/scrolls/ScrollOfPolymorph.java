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

package com.shatteredpixel.shatteredpixeldungeon.items.scrolls;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Belongings;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.FetidRat;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Heavyw;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Sheep;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.RainbowParticle;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.TimeManiTrap;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.TargetHealthIndicator;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class ScrollOfPolymorph extends Scroll {

    {
        image = ItemSpriteSheet.EXOTIC_BERKANAN;

        unique = true;

        identify();
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return new ItemSprite.Glowing(0x990000, 1f);
    }

    @Override
    public void doRead() {

        Sample.INSTANCE.play( Assets.Sounds.READ );

        for (Mob mob : Dungeon.level.mobs.toArray( new Mob[0] )) {
            if (mob.alignment != Char.Alignment.ALLY && Dungeon.level.heroFOV[mob.pos]) {
                if (!mob.properties().contains(Char.Property.BOSS)
                        && !mob.properties().contains(Char.Property.MINIBOSS)){
                    Heavyw Heavyw = new Heavyw();
                    Heavyw.pos = mob.pos;

                    //awards half exp for each sheep-ified mob
                    //50% chance to round up, 50% to round down
                    if (mob.EXP % 2 == 1) mob.EXP += Random.Int(2);
                    mob.EXP /= 2;

                    mob.destroy();
                    mob.sprite.killAndErase();
                    Dungeon.level.mobs.remove(mob);
                    TargetHealthIndicator.instance.target(null);
                    GameScene.add(Heavyw);
                    CellEmitter.get(Heavyw.pos).burst( RainbowParticle.BURST, 12 );
                    curUser.sprite.emitter().burst( RainbowParticle.BURST, 12 );
                    Sample.INSTANCE.play(Assets.Sounds.PUFF);
                    Sample.INSTANCE.play(Assets.Sounds.SHEEP);
                }
            }
        }
        identify();

        readAnimation();

    }

}
