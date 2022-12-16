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

package com.shatteredpixel.shatteredpixeldungeon.levels.traps;


import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Bcom;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Cream;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Arrays;

public class BcomTrap extends Trap {

    private static final ArrayList<Class<?extends Mob>> RARE = new ArrayList<>(Arrays.asList(
            Bcom.class, Bcom.class));

    @Override
    public void activate() {

        int nMobs = 4;


        GameScene.flash(0x660000);
        Sample.INSTANCE.play(Assets.Sounds.MIMIC, 1, 1);

        ArrayList<Integer> candidates = new ArrayList<>();

        for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
            int p = pos + PathFinder.NEIGHBOURS8[i];
            if (Actor.findChar( p ) == null && (Dungeon.level.passable[p] || Dungeon.level.avoid[p])) {
                candidates.add( p );
            }
        }

        ArrayList<Integer> respawnPoints = new ArrayList<>();

        while (nMobs > 0 && candidates.size() > 0) {
            int index = Random.index( candidates );

            respawnPoints.add( candidates.remove( index ) );
            nMobs--;
        }

        ArrayList<Mob> mobs = new ArrayList<>();

        int summoned = 0;
        for (Integer point : respawnPoints) {
            summoned++;
            Mob mob;
            switch (summoned){
                case 0: default:
                    mob = new Cream();
                    break;
            }

            mob.maxLvl = Hero.MAX_LEVEL;
            mob.state = mob.WANDERING;
            mob.pos = point;
            GameScene.add(mob);
            mobs.add(mob);
        }

        //important to process the visuals and pressing of cells last, so spawned mobs have a chance to occupy cells first
        Trap t;
        for (Mob mob : mobs){
            //manually trigger traps first to avoid sfx spam
            if ((t = Dungeon.level.traps.get(mob.pos)) != null && t.active){
                if (t.disarmedByActivation) t.disarm();
                t.reveal();
                t.activate();
            }
            ScrollOfTeleportation.appear(mob, mob.pos);
            Dungeon.level.occupyCell(mob);
        }

    }
}