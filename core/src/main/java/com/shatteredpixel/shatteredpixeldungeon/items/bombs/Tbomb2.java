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

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SmokeParticle;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.ShadowCaster;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Point;

import java.util.ArrayList;

public class Tbomb2 extends Bomb {

    {
        image = ItemSpriteSheet.TBOMB2;
    }

    @Override
    public boolean explodesDestructively() {
        return false;
    }

    @Override
    public void explode(int cell) {
        super.explode(cell);

        boolean[] FOV = new boolean[Dungeon.level.length()];
        Point c = Dungeon.level.cellToPoint(cell);
        ShadowCaster.castShadow(c.x, c.y, Dungeon.level.width(), FOV, Dungeon.level.losBlocking, 5);

        ArrayList<Char> affected = new ArrayList<>();

        for (int i = 0; i < FOV.length; i++) {
            if (FOV[i]) {
                if (Dungeon.level.heroFOV[i] && !Dungeon.level.solid[i]) {
                    CellEmitter.get(i).burst(SmokeParticle.FACTORY, 4);
                    CellEmitter.center(i).burst( BlastParticle.FACTORY, 20 );
                }
                Char ch = Actor.findChar(i);
                if (ch != null){
                    affected.add(ch);
                }
            }
        }

        for (Char ch : affected){

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

            if (ch == Dungeon.hero && !ch.isAlive()) {
                Dungeon.fail(this);
            }
        }
    }

    @Override
    public int value() {
        return quantity * 30;
    }
}
