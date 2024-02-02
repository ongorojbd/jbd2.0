/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2022 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AscensionChallenge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Wezamob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Whsnake;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.ScrollOfPolymorph;
import com.shatteredpixel.shatteredpixeldungeon.journal.Notes;
import com.shatteredpixel.shatteredpixeldungeon.levels.HallsLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.PrisonLevel;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.WandmakerSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.WezaSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.WhsnakeSprite;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndQuest;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class Whitesnake extends NPC {

    {
        spriteClass = WhsnakeSprite.class;

        properties.add(Property.IMMOVABLE);
    }

    @Override
    protected boolean act() {
        if (Dungeon.hero.buff(AscensionChallenge.class) != null) {
            die(null);
            return true;
        }
        return super.act();
    }

    @Override
    public void beckon(int cell) {
        //do nothing
    }

    @Override
    protected boolean getCloser(int target) {
        return true;
    }

    @Override
    protected boolean getFurther(int target) {
        return true;
    }

    @Override
    public boolean add(Buff buff) {
        return false;
    }

    @Override
    public int defenseSkill(Char enemy) {
        return INFINITE_EVASION;
    }

    @Override
    public void damage(int dmg, Object src) {
    }

    @Override
    public boolean reset() {
        return true;
    }

    @Override
    public boolean interact(Char c) {

        sprite.turnTo(pos, Dungeon.hero.pos);

        if (c != Dungeon.hero) {
            return true;
        }

        Game.runOnRenderThread(new Callback() {
            @Override
            public void call() {
                GameScene.show(new WndOptions(
                                       sprite(),
                                       Messages.titleCase(name()),
                                       Messages.get(Whitesnake.class, "0"),
                                       Messages.get(Whitesnake.class, "1"),
                                       Messages.get(Whitesnake.class, "2")
                               ) {
                                   @Override
                                   protected void onSelect(int index) {
                                       if (index == 0) {
                                           Sample.INSTANCE.play(Assets.Sounds.MIMIC);
                                           yell(Messages.get(Whitesnake.class, "4"));

                                           destroy();
                                           sprite.killAndErase();
                                           die(null);

                                           Whsnake Whsnake = new Whsnake();
                                           Whsnake.state = Whsnake.HUNTING;
                                           Whsnake.pos = pos;
                                           GameScene.add(Whsnake);
                                           Whsnake.beckon(Dungeon.hero.pos);

                                       }  else {

                                       }
                                   }
                               }
                );
            }
        });


        return true;
    }


    public void flee() {
        destroy();
        sprite.die();
    }

}
