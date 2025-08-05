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
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
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

        ArrayList<Char> affected = new ArrayList<>();

        PathFinder.buildDistanceMap(cell, BArray.not(Dungeon.level.solid, null), 1);

        // 범위 내의 모든 캐릭터를 찾아서 affected 리스트에 추가
        for (int i = 0; i < PathFinder.distance.length; i++) {
            if (PathFinder.distance[i] < Integer.MAX_VALUE) {
                Char ch = Actor.findChar(i);
                if (ch != null) {
                    affected.add(ch);
                }
            }
        }

        for (Char ch : affected) {
            int base = Random.NormalIntRange(6, 12);
            // spw6가 0인 경우도 대비 (1.5^0 = 1)
            float dmg = base * (float) Math.pow(1.5, Math.max(0, Statistics.spw6));
            
            // 깊이에 비례한 추가 데미지
            int depthBonus = Dungeon.depth / 4;
            dmg += depthBonus;

            if (ch.pos != cell) {
                dmg *= 0.67f;
            }

            dmg -= ch.drRoll();

            ch.damage(Math.round(dmg), this);
        }
    }


    @Override
    public String desc() {
        int baseMin = 8;
        int baseMax = 16;
        int spw6Level = Math.max(0, Statistics.spw6);
        
        // spw6 레벨에 따른 피해량 계산
        int minDamage = Math.round(baseMin * (float) Math.pow(1.5, spw6Level));
        int maxDamage = Math.round(baseMax * (float) Math.pow(1.5, spw6Level));
        
        // 깊이에 따른 추가 데미지
        int depthBonus = Dungeon.depth / 2;
        minDamage += depthBonus;
        maxDamage += depthBonus;
        
        String desc = Messages.get(this, "desc", minDamage, maxDamage);
        
        if (fuse == null) {
            return desc + "\n\n" + Messages.get(this, "desc_fuse");
        } else {
            return desc + "\n\n" + Messages.get(this, "desc_burning");
        }
    }

    @Override
    public int value() {
        return quantity * (10);
    }
}
