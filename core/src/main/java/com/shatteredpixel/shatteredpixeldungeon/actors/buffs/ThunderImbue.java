/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2023 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Electricity;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.ThunderBolt;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SparkParticle;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.utils.PointF;

import java.util.ArrayList;

public class ThunderImbue extends Buff {

    {
        type = buffType.POSITIVE;
        announced = true;
    }

    public static final float DURATION = 1_000_000;

    public void proc(Char enemy, int damage){
        if (!enemy.isImmune(Electricity.class)) {
            enemy.damage(Hero.heroDamageIntRange(2, 10), new Electricity());
        }
        thunderEffect(enemy.sprite);
    }

    public static void thunderEffect(CharSprite sprite) {
        if (sprite != null) {
            ArrayList<ThunderBolt.Arc> arcs = new ArrayList<>();
            sprite.centerEmitter().burst(SparkParticle.FACTORY, 3);
            sprite.flash();
            arcs.add(new ThunderBolt.Arc(new PointF( sprite.center().x, sprite.center().y-35 ), sprite.center()));
            hero.sprite.parent.addToFront( new ThunderBolt( arcs, null ) );
        }
    }

    @Override
    public int icon() {
        return BuffIndicator.FURY;
    }

    {
        immunities.add( Electricity.class );
    }

}
