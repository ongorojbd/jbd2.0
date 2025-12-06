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

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Polpo;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.PolpoItem;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

public class Doom extends Buff {

    {
        type = buffType.NEGATIVE;
        announced = true;
    }

    public static final float DURATION = 1_000_000;
    
    private float left = DURATION;

    @Override
    public void fx(boolean on) {
        if (on) target.sprite.add(CharSprite.State.DARKENED);
        else if (target.invisible == 0) target.sprite.remove(CharSprite.State.DARKENED);
    }

    @Override
    public int icon() {
        return BuffIndicator.CORRUPT;
    }

    @Override
    public boolean act() {
        spend(TICK);

        // 시간 제한이 있는 경우 (일시적 Doom)
        if (left < DURATION) {
            left -= 1f;
            if (left <= 0) {
                detach();
                return true;
            }
        }

        if (target instanceof Hero && Statistics.polpoQuest) {
            if (Statistics.deepestFloor >= Statistics.polpocount && Dungeon.hero.belongings.getItem(PolpoItem.class) != null) {

                // grant reward buff and remove the quest item first
                Buff.affect(hero, PolpoBuff.class);

                PolpoItem item = Dungeon.hero.belongings.getItem(PolpoItem.class);
                if (item != null) {
                    item.detach(hero.belongings.backpack);
                }

                if (hero.sprite != null) {
                    new Flare(6, 32).color(0xFF6600, true).show(hero.sprite, 3f);
                    hero.sprite.remove(CharSprite.State.DARKENED);
                }
                Sample.INSTANCE.play(Assets.Sounds.BADGE);
                GLog.p(Messages.get(Polpo.class, "clear"));

                // finally remove the Doom debuff
                Buff.detach(hero, Doom.class);
            }
        }
        return true;
    }

    public void setDuration(float duration) {
        left = duration;
    }

    @Override
    public String desc() {
        if (left < DURATION) {
            return Messages.get(this, "desc", dispTurns(Math.max(0, left)));
        }
        return Messages.get(this, "desc");
    }

    private static final String LEFT = "left";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(LEFT, left);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        if (bundle.contains(LEFT)) {
            left = bundle.getFloat(LEFT);
        } else {
            left = DURATION;
        }
    }
}
