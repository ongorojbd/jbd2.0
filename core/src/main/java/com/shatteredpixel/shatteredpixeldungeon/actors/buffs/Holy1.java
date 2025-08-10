/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2020 Evan Debenham
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

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Civil;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Sbr4;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;

public class Holy1 extends Buff {

    {
        type = buffType.POSITIVE;
        announced = true;
    }

    public static final float DURATION = 1_000_000;

    @Override
    public int icon() {
        return BuffIndicator.SACRIFICE;
    }

    @Override
    public String toString() {
        return Messages.get(this, "name");
    }

    @Override
    public boolean act() {
        spend( TICK );


        if (target instanceof Hero) {

            Hero hero = (Hero) target;

            if (hero.buff(Holy1.class) != null && hero.buff(Holy2.class) != null && hero.buff(Holy3.class) != null) {
                GameScene.flash(0xFF6600);
                Camera.main.shake(31, 5f);
                GLog.p(Messages.get(Civil.class, "6"));
                new Flare(6, 32).color(0xFF6600, true).show(hero.sprite, 5f);

                Buff.affect(hero, D4C.class);
                Buff.detach(hero, Holy1.class);
                Buff.detach(hero, Holy2.class);
                Buff.detach(hero, Holy3.class);

                if (!Dungeon.tendencylevel) {
                    Sbr4 pick = new Sbr4();
                    Dungeon.level.drop( pick, Dungeon.hero.pos ).sprite.drop();
                }

            }
        }

        return true;
    }

}