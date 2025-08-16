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

package com.shatteredpixel.shatteredpixeldungeon.items.scrolls;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.CaesarZeppeli;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SparkParticle;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class ScrollOfSummonCaesar extends Scroll {

    {
        icon = ItemSpriteSheet.Icons.SCROLL_ANTIMAGIC;
    }

    @Override
    public void doRead() {
        
        // Find a suitable position to spawn Caesar
        int spawnPos = -1;
        for (int i : PathFinder.NEIGHBOURS8) {
            int pos = curUser.pos + i;
            if (Dungeon.level.passable[pos] && !Dungeon.level.avoid[pos] && Actor.findChar(pos) == null) {
                spawnPos = pos;
                break;
            }
        }
        
        if (spawnPos == -1) {
            // No space nearby, try to spawn at hero's position
            spawnPos = curUser.pos;
        }
        
        // Spawn Caesar Zeppeli
        CaesarZeppeli caesar = new CaesarZeppeli();
        caesar.pos = spawnPos;
        GameScene.add(caesar);
        Dungeon.level.occupyCell(caesar);
        
        // Visual and audio effects
        caesar.sprite.centerEmitter().start(SparkParticle.FACTORY, 0.05f, 10);
        caesar.sprite.centerEmitter().burst(Speck.factory(Speck.BUBBLE), 10);
        
        // Caesar's entrance message
        caesar.yell(Messages.get(this, "caesar_entrance"));
        GLog.p(Messages.get(this, "summoned"));
        
        identify();
        readAnimation();
    }
    
    @Override
    public int value() {
        return isKnown() ? 50 * quantity : super.value();
    }
}
