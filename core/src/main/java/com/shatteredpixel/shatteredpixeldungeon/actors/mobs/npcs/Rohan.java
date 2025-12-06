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
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AscensionChallenge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.PrisonLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.RohanSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class Rohan extends NPC {

    {
        spriteClass = RohanSprite.class;

        properties.add(Property.IMMOVABLE);
    }

    @Override
    protected boolean act() {
        if (Dungeon.hero.buff(AscensionChallenge.class) != null) {
            die(null);
            return true;
        }
        if (Statistics.duwang == 1) {
            yell(Messages.get(Rohan.class, "bi"));
            Sample.INSTANCE.play(Assets.Sounds.RO5);
            Statistics.duwang = 0;
            destroy();
            sprite.killAndErase();
            die(null);
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

        if (Statistics.duwang == 1) {
            yell(Messages.get(Rohan.class, "bi"));
            Sample.INSTANCE.play(Assets.Sounds.RO5);
            Statistics.duwang = 0;
            destroy();
            sprite.killAndErase();
            die(null);
        } else {
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    Sample.INSTANCE.play(Assets.Sounds.RO1);
                    GameScene.show(new WndOptions(
                            sprite(),
                            Messages.titleCase(name()),
                            Messages.get(Rohan.class, "i"),
                            Messages.get(Rohan.class, "1"),
                            Messages.get(Rohan.class, "2")
                    ) {
                        @Override
                        protected void onSelect(int index) {
                            if (index == 0) {
                                Game.runOnRenderThread(new Callback() {
                                    @Override
                                    public void call() {
                                        Sample.INSTANCE.play(Assets.Sounds.RO2);
                                        GameScene.show(new WndOptions(
                                                sprite(),
                                                Messages.titleCase(name()),
                                                Messages.get(Rohan.class, "4", Dungeon.gold),
                                                Messages.get(Rohan.class, "5"),
                                                Messages.get(Rohan.class, "6"),
                                                Messages.get(Rohan.class, "7"),
                                                Messages.get(Rohan.class, "8"),
                                                Messages.get(Rohan.class, "9")
                                        ) {
                                            @Override
                                            protected void onSelect(int index) {
                                                if (index == 0) {
                                                    if (Dungeon.gold > 49) {
                                                        Statistics.duwang += 1;
                                                        Sample.INSTANCE.play(Assets.Sounds.GOLD);
                                                        switch (Random.Int(2)) {
                                                            case 0:
                                                                Dungeon.gold += 50;
                                                                GLog.p(Messages.get(Rohan.class, "w", 50));
                                                                yell(Messages.get(Rohan.class, "lose"));
                                                                Sample.INSTANCE.play(Assets.Sounds.RO4);
                                                                break;
                                                            case 1:
                                                                Dungeon.gold -= 50;
                                                                GLog.h(Messages.get(Rohan.class, "l"));
                                                                yell(Messages.get(Rohan.class, "win"));
                                                                Sample.INSTANCE.play(Assets.Sounds.RO3);
                                                                break;
                                                        }


                                                    } else
                                                        yell(Messages.get(Rohan.class, "no"));
                                                } else if (index == 1) {
                                                    if (Dungeon.gold > 99) {
                                                        Statistics.duwang += 1;
                                                        Sample.INSTANCE.play(Assets.Sounds.GOLD);
                                                        switch (Random.Int(2)) {
                                                            case 0:
                                                                Dungeon.gold += 100;
                                                                GLog.p(Messages.get(Rohan.class, "w", 100));
                                                                yell(Messages.get(Rohan.class, "lose"));
                                                                Sample.INSTANCE.play(Assets.Sounds.RO4);
                                                                break;
                                                            case 1:
                                                                Dungeon.gold -= 100;
                                                                Sample.INSTANCE.play(Assets.Sounds.RO3);
                                                                GLog.h(Messages.get(Rohan.class, "l"));
                                                                yell(Messages.get(Rohan.class, "win"));
                                                                break;
                                                        }

                                                    } else
                                                        yell(Messages.get(Rohan.class, "no"));
                                                } else if (index == 2) {
                                                    if (Dungeon.gold > 249) {
                                                        Statistics.duwang += 1;
                                                        Sample.INSTANCE.play(Assets.Sounds.GOLD);
                                                        switch (Random.Int(2)) {
                                                            case 0:
                                                                Dungeon.gold += 250;
                                                                GLog.p(Messages.get(Rohan.class, "w", 250));
                                                                yell(Messages.get(Rohan.class, "lose"));
                                                                Sample.INSTANCE.play(Assets.Sounds.RO4);
                                                                break;
                                                            case 1:
                                                                Dungeon.gold -= 250;
                                                                GLog.h(Messages.get(Rohan.class, "l"));
                                                                yell(Messages.get(Rohan.class, "win"));
                                                                Sample.INSTANCE.play(Assets.Sounds.RO3);
                                                                break;
                                                        }

                                                    } else
                                                        yell(Messages.get(Rohan.class, "no"));
                                                } else if (index == 3) {
                                                    if (Dungeon.gold > 499) {
                                                        Statistics.duwang += 1;
                                                        Sample.INSTANCE.play(Assets.Sounds.GOLD);
                                                        switch (Random.Int(2)) {
                                                            case 0:
                                                                Dungeon.gold += 500;
                                                                GLog.p(Messages.get(Rohan.class, "w", 500));
                                                                yell(Messages.get(Rohan.class, "lose"));
                                                                Sample.INSTANCE.play(Assets.Sounds.RO4);
                                                                break;
                                                            case 1:
                                                                Dungeon.gold -= 500;
                                                                GLog.h(Messages.get(Rohan.class, "l"));
                                                                yell(Messages.get(Rohan.class, "win"));
                                                                Sample.INSTANCE.play(Assets.Sounds.RO3);
                                                                break;
                                                        }


                                                    } else
                                                        yell(Messages.get(Rohan.class, "no"));
                                                } else if (index == 4) {
                                                    if (Dungeon.gold > 999) {
                                                        Statistics.duwang += 1;
                                                        Sample.INSTANCE.play(Assets.Sounds.GOLD);
                                                        switch (Random.Int(2)) {
                                                            case 0:
                                                                Dungeon.gold += 1000;
                                                                GLog.p(Messages.get(Rohan.class, "w", 1000));
                                                                yell(Messages.get(Rohan.class, "lose"));
                                                                Sample.INSTANCE.play(Assets.Sounds.RO4);
                                                                break;
                                                            case 1:
                                                                Dungeon.gold -= 1000;
                                                                GLog.h(Messages.get(Rohan.class, "l"));
                                                                yell(Messages.get(Rohan.class, "win"));
                                                                Sample.INSTANCE.play(Assets.Sounds.RO3);
                                                                break;
                                                        }
                                                    } else
                                                        yell(Messages.get(Rohan.class, "no"));
                                                }
                                            }
                                        });
                                    }
                                });
                            } else if (index == 1) {
                                Sample.INSTANCE.play(Assets.Sounds.RO4);
                                yell(Messages.get(Rohan.class, "play"));

                            }
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

    public static void spawn(PrisonLevel level) {
        // 독립적인 시드 오프셋을 사용
        Random.pushGenerator(Dungeon.seedCurDepth() + 999999L);
        boolean shouldSpawn = Random.Int(2) == 0;
        Random.popGenerator();
        
        if (shouldSpawn) {
            if (Dungeon.depth == 6 && !Dungeon.bossLevel()) {

                Rohan npc = new Rohan();
                do {
                    npc.pos = level.randomRespawnCell(npc);
                } while (
                        npc.pos == -1 ||
                                level.heaps.get(npc.pos) != null ||
                                level.traps.get(npc.pos) != null ||
                                level.findMob(npc.pos) != null ||
                                level.map[npc.pos] == Terrain.GRASS ||
                                level.map[npc.pos] == Terrain.HIGH_GRASS ||
                                level.map[npc.pos] == Terrain.FURROWED_GRASS ||
                                !(level.passable[npc.pos + PathFinder.CIRCLE4[0]] && level.passable[npc.pos + PathFinder.CIRCLE4[2]]) ||
                                !(level.passable[npc.pos + PathFinder.CIRCLE4[1]] && level.passable[npc.pos + PathFinder.CIRCLE4[3]]));
                level.mobs.add(npc);

                // 풀 타일을 EMPTY로 변경하여 겹침 방지 (do-while에서 이미 제외했지만 안전을 위해)
                if (level.map[npc.pos] == Terrain.GRASS ||
                        level.map[npc.pos] == Terrain.HIGH_GRASS ||
                        level.map[npc.pos] == Terrain.FURROWED_GRASS) {
                    Level.set(npc.pos, Terrain.EMPTY, level);
                } else if (level.map[npc.pos] != Terrain.EMPTY_DECO) {
                    Level.set(npc.pos, Terrain.EMPTY, level);
                }
            }
        }
    }
}




