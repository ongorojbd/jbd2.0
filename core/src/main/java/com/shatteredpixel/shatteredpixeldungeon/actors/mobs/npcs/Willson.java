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

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AscensionChallenge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Antonio;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Kawasiri;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Willsonmob;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.CorpseDust;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Embers;
import com.shatteredpixel.shatteredpixeldungeon.levels.HallsLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.PrisonLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Rotberry;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.RohanSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.WillsonSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndQuest;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndWandmaker;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class Willson extends NPC {

    {
        spriteClass = WillsonSprite.class;

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
                GameScene.show(new WndQuest(Willson.this, Messages.get(Willson.class, "intro_1")) {
                    @Override
                    public void hide() {
                        super.hide();
                        GameScene.show(new WndOptions(
                                sprite(),
                                Messages.titleCase(name()),
                                Messages.get(Willson.class, "intro_2"),
                                Messages.get(Willson.class, "1"),
                                Messages.get(Willson.class, "2")
                        ) {
                            @Override
                            protected void onSelect(int index) {
                                if (index == 0) {
                                    switch (Random.Int(3)) {
                                        case 0:
                                            InterlevelScene.mode = InterlevelScene.Mode.RETURN;
                                            InterlevelScene.returnDepth = 17;
                                            InterlevelScene.returnBranch = 1;
                                            InterlevelScene.returnPos = -2;
                                            Game.switchScene(InterlevelScene.class);
                                            break;
                                        case 1:
                                            InterlevelScene.mode = InterlevelScene.Mode.RETURN;
                                            InterlevelScene.returnDepth = 21;
                                            InterlevelScene.returnBranch = 1;
                                            InterlevelScene.returnPos = -2;
                                            Game.switchScene(InterlevelScene.class);
                                            break;
                                        case 2:
                                            InterlevelScene.mode = InterlevelScene.Mode.RETURN;
                                            InterlevelScene.returnDepth = 23;
                                            InterlevelScene.returnBranch = 1;
                                            InterlevelScene.returnPos = -2;
                                            Game.switchScene(InterlevelScene.class);
                                            break;
                                    }
                                    destroy();
                                    sprite.killAndErase();
                                    die(null);

                                    Willsonmob willsonmob = new Willsonmob();
                                    willsonmob.state = willsonmob.HUNTING;
                                    willsonmob.pos = pos;
                                    GameScene.add( willsonmob );
                                    willsonmob.beckon(hero.pos);

                                } else if (index == 1) {

                                }
                            }
                        });

                    }
                });
            }
        });

        return true;
    }

    public void flee() {
        destroy();
        sprite.die();
    }

    public static void spawn(HallsLevel level) {
        if (Dungeon.depth == 22 && !Dungeon.bossLevel() && Badges.isUnlocked(Badges.Badge.VICTORY)) {

            Willson npc = new Willson();
            do {
                npc.pos = level.randomRespawnCell(npc);
            } while (
                    npc.pos == -1 ||
                            level.heaps.get(npc.pos) != null ||
                            level.traps.get(npc.pos) != null ||
                            level.findMob(npc.pos) != null ||
                            !level.passable[npc.pos + PathFinder.CIRCLE4[0]] || !level.passable[npc.pos + PathFinder.CIRCLE4[1]] ||
                            !level.passable[npc.pos + PathFinder.CIRCLE4[2]] || !level.passable[npc.pos + PathFinder.CIRCLE4[3]]);
            level.mobs.add(npc);

            Level.set(npc.pos, Terrain.EMPTY, level);
        }
    }
}




