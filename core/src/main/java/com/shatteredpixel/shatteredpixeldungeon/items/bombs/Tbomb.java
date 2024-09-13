/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2024 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.items.bombs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Kawasiribuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SmokeParticle;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.BArray;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Tbomb extends Bomb {

    {
        image = ItemSpriteSheet.TBOMB;
    }

    @Override
    public void explode(int cell) {
        super.explode(cell);

        if (Statistics.spw6 > 4) {
            ArrayList<Char> affected = new ArrayList<>();

            PathFinder.buildDistanceMap(cell, BArray.not(Dungeon.level.solid, null), 2);

            for (int i = 0; i < PathFinder.distance.length; i++) {
                if (PathFinder.distance[i] < Integer.MAX_VALUE) {
                    CellEmitter.get(i).burst(SmokeParticle.FACTORY, 4);
                    CellEmitter.center(i).burst(BlastParticle.FACTORY, 20);
                    Char ch = Actor.findChar(i);
                    if (ch != null) {
                        affected.add(ch);
                    }
                }
            }
            for (Char ch : affected) {

                int dmg = Math.round(Char.combatRoll(6 + Statistics.wave / 5, 12 + Statistics.wave / 5));
                if (Statistics.spw6 > 5) dmg *= 1.5f;
                else if (Statistics.spw6 > 2) dmg *= 1.2f;
                if (ch.pos != cell){
                    dmg = Math.round(dmg*0.67f);
                }
                if (ch instanceof Hero && Statistics.spw6 >= 4){
                    dmg *= 0;
                }
                dmg -= ch.drRoll();

                if (ch instanceof Hero) dmg = 0;

                ch.damage(dmg, this);

            }
        }
    }

    @Override
    public int value() {
        return quantity * (10);
    }
}
