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
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Kawasiribuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SmokeParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Jojo2;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.BArray;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;


public class Tbomb extends Bomb {

    // When true, this Tbomb will not damage the hero or allied units.
    public boolean noFriendlyDamage = false;

    {
        image = ItemSpriteSheet.TBOMB;
    }

    @Override
    public void explode(int cell) {
        // match base Bomb behavior, with Tbomb-specific scaling retained
        this.fuse = null;

        Sample.INSTANCE.play(Assets.Sounds.BLAST);

        if (explodesDestructively()) {

            ArrayList<Integer> affectedCells = new ArrayList<>();
            ArrayList<Char> affectedChars = new ArrayList<>();

            if (Dungeon.level.heroFOV[cell]) {
                CellEmitter.center(cell).burst(BlastParticle.FACTORY, 30);
            }

            boolean terrainAffected = false;
            boolean[] explodable = new boolean[Dungeon.level.length()];
            BArray.not(Dungeon.level.solid, explodable);
            BArray.or(Dungeon.level.flamable, explodable, explodable);
            PathFinder.buildDistanceMap(cell, explodable, explosionRange());
            for (int i = 0; i < PathFinder.distance.length; i++) {
                if (PathFinder.distance[i] != Integer.MAX_VALUE) {

                    affectedCells.add(i);

                    Char ch = Actor.findChar(i);
                    if (ch != null) {
                        affectedChars.add(ch);
                    }
                }
            }

            for (int i : affectedCells){
                if (Dungeon.level.heroFOV[i]) {
                    CellEmitter.get(i).burst(SmokeParticle.FACTORY, 4);
                }

                if (Dungeon.level.flamable[i]) {
                    Dungeon.level.destroy(i);
                    GameScene.updateMap(i);
                    terrainAffected = true;
                }

                Heap heap = Dungeon.level.heaps.get(i);
                if (heap != null) {
                    heap.explode();
                }
            }

            for (Char ch : affectedChars) {

                if (!ch.isAlive()) {
                    continue;
                }

                // Skip damaging the hero and allies if flagged for no friendly damage
                if (noFriendlyDamage && (ch instanceof Hero || ch.alignment == Char.Alignment.ALLY)) {
                    continue;
                }

                int base = Random.NormalIntRange(4 + Dungeon.scalingDepth(), 12 + 3 * Dungeon.scalingDepth());

                if (Dungeon.hero.buff(Kawasiribuff.class) != null) {
                    base = base * 3 / 2;
                }

                float scaled = base;
                if (Statistics.spw6 > 0) {
                    scaled *= Math.pow(1.5f, Statistics.spw6);
                }

                int depthBonus = Dungeon.depth / 4;
                int dmg = Math.round(scaled) + depthBonus;

                if (ch instanceof Hero && ((Hero) ch).belongings.getItem(Jojo2.class) != null) {
                    dmg = 0;
                }

                dmg -= ch.drRoll();

                if (dmg > 0) {
                    ch.damage(dmg, this);
                }

                if (ch == Dungeon.hero && !ch.isAlive()) {
                    GLog.n(Messages.get(this, "ondeath"));
                    Dungeon.fail(this);
                }
            }

            if (terrainAffected) {
                Dungeon.observe();
            }
        }
    }


    // use base Bomb description

    @Override
    public int value() {
        return quantity * (10);
    }
}
