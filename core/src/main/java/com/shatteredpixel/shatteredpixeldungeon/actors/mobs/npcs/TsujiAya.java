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

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AscensionChallenge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfMetamorphosis;
import com.shatteredpixel.shatteredpixeldungeon.levels.CavesLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.PrisonLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.SewerLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.TsujiAyaSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.lang.reflect.Field;

public class TsujiAya extends NPC {

    private static final String USED_KEY = "used";
    private boolean used = false;

    {
        spriteClass = TsujiAyaSprite.class;

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
    public boolean interact(Char c) {
        sprite.turnTo(pos, c.pos);

        if (c != Dungeon.hero) {
            return true;
        }

        if (used) {
            yell(Messages.get(this, "already_used"));
            die(null);
            return true;
        }

        if (Dungeon.gold < 100) {
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    GameScene.show(new WndOptions(
                            sprite(),
                            Messages.titleCase(Messages.get(TsujiAya.this, "name")),
                            Messages.get(TsujiAya.this, "not_enough_gold"),
                            Messages.get(TsujiAya.this, "ok")));
                }
            });
            return true;
        }

        Game.runOnRenderThread(new Callback() {
            @Override
            public void call() {
                GameScene.show(new WndOptions(
                        sprite(),
                        Messages.titleCase(Messages.get(TsujiAya.this, "name")),
                        Messages.get(TsujiAya.this, "offer", 100),
                        Messages.get(TsujiAya.this, "yes"),
                        Messages.get(TsujiAya.this, "no")) {
                    @Override
                    protected void onSelect(int index) {
                        if (index == 0) {
                            if (Dungeon.gold >= 100) {
                                Dungeon.gold -= 100;
                                Sample.INSTANCE.play(Assets.Sounds.GOLD);
                                GLog.p(Messages.get(TsujiAya.class, "paid", 100));
                                
                                used = true;
                                
                                // 가짜 ScrollOfMetamorphosis 아이템을 만들어서 사용
                                ScrollOfMetamorphosis fakeScroll = new ScrollOfMetamorphosis();
                                fakeScroll.identify();
                                
                                try {
                                    // Reflection을 사용해서 protected 필드 설정
                                    Field curItemField = Item.class.getDeclaredField("curItem");
                                    curItemField.setAccessible(true);
                                    curItemField.set(null, fakeScroll);
                                    
                                    Field curUserField = Item.class.getDeclaredField("curUser");
                                    curUserField.setAccessible(true);
                                    curUserField.set(null, Dungeon.hero);
                                    
                                    Field identifiedByUseField = ScrollOfMetamorphosis.class.getDeclaredField("identifiedByUse");
                                    identifiedByUseField.setAccessible(true);
                                    identifiedByUseField.set(null, false);
                                } catch (Exception e) {
                                    GLog.w("Failed to set fields: " + e.getMessage());
                                }
                                
                                GameScene.show(new ScrollOfMetamorphosis.WndMetamorphChoose());
                            } else {
                                GLog.w(Messages.get(TsujiAya.class, "not_enough_gold"));
                            }
                        }
                    }
                });
            }
        });

        return true;
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
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(USED_KEY, used);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        used = bundle.getBoolean(USED_KEY);
    }

    public static void spawn(SewerLevel level) {
        // 독립적인 시드 오프셋을 사용
        Random.pushGenerator(Dungeon.seedCurDepth());
        boolean shouldSpawn = Random.Int(10) == 0;
        Random.popGenerator();

        if (shouldSpawn) {
            if (Dungeon.depth == 2 && !Dungeon.bossLevel()) {

                TsujiAya npc = new TsujiAya();
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

    public static void spawn(PrisonLevel level) {
        // 독립적인 시드 오프셋을 사용
        Random.pushGenerator(Dungeon.seedCurDepth());
        boolean shouldSpawn = Random.Int(5) == 0;
        Random.popGenerator();

        if (shouldSpawn) {
            if (Dungeon.depth == 7 && !Dungeon.bossLevel()) {

                TsujiAya npc = new TsujiAya();
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

    public static void spawn(CavesLevel level) {
        // 독립적인 시드 오프셋을 사용
        Random.pushGenerator(Dungeon.seedCurDepth());
        boolean shouldSpawn = Random.Int(5) == 0;
        Random.popGenerator();

        if (shouldSpawn) {
            if (Dungeon.depth == 11 && !Dungeon.bossLevel()) {

                TsujiAya npc = new TsujiAya();
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

