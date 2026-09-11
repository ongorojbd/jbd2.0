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

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import static com.shatteredpixel.shatteredpixeldungeon.Statistics.spw38;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AscensionChallenge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.CorpseDust;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Embers;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Greenbaby;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Kinga;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Kingc;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Kingm;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Kings;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Kingt;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Kingw;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.PhaseShift;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Willa;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Xray;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfAdvanceguard;
import com.shatteredpixel.shatteredpixeldungeon.levels.CavesLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Rotberry;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.YasuSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndQuest;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndWandmaker;
import com.watabou.noosa.Game;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class Yasuho extends NPC {

    {
        spriteClass = YasuSprite.class;

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
    public int defenseSkill(Char enemy) {
        return INFINITE_EVASION;
    }

    @Override
    public void damage(int dmg, Object src) {
        //do nothing
    }

    @Override
    public boolean add(Buff buff) {
        return false;
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

        if (spw38 > 0) {
            String msg = Messages.get(this, "intro_3", Messages.titleCase(Dungeon.hero.name()));
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    GameScene.show(new WndQuest(Yasuho.this, msg));
                }
            });
        } else {

            String msg1 = "";
            String msg2 = "";

            msg1 += Messages.get(this, "intro_1");
            msg2 += Messages.get(this, "intro_2");

            final String msg1Final = msg1;
            final String msg2Final = msg2;

            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    GameScene.show(new WndQuest(Yasuho.this, msg1Final) {
                        @Override
                        public void hide() {
                            super.hide();

                            Item n = new PhaseShift().quantity(2);

                            if (n.doPickUp( Dungeon.hero )) {
                                GLog.p( Messages.capitalize(Messages.get(Dungeon.hero, "you_now_have", n.name()) ));
                            } else {
                                Dungeon.level.drop(n, Dungeon.hero.pos ).sprite.drop();
                            }

                            GameScene.show(new WndQuest(Yasuho.this, msg2Final));
                            spw38++;
                        }
                    });
                }
            });
        }
        return true;
    }

    public void flee() {
        destroy();
        sprite.die();
    }

}
