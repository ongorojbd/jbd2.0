/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2026 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.items;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NPC;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.SpellSprite;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ChallengeParticle;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.Door;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Arrays;

public class P2 extends Item {

    public static final String AC_USE = "USE";

    {
        image = ItemSpriteSheet.TORCH;
        stackable = true;
        defaultAction = AC_USE;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_USE);
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);

        if (action.equals(AC_USE)) {

            // NPC를 제외하고 시야 내 가장 가까운 적 자동 탐색
            Mob nearest = null;
            float nearDist = Float.MAX_VALUE;
            for (Mob mob : Dungeon.level.mobs) {
                if (mob instanceof NPC) continue;
                if (mob.isAlive() && Dungeon.level.heroFOV[mob.pos]) {
                    float dist = Dungeon.level.trueDistance(hero.pos, mob.pos);
                    if (dist < nearDist) {
                        nearDist = dist;
                        nearest = mob;
                    }
                }
            }

            if (nearest == null) {
                GLog.w(Messages.get(this, "no_target"));
                return;
            }

            // 1단계: pathfinding으로 적까지의 경로 탐색 (벽 우회 가능, 적 칸 통과 허용)
            PathFinder.Path pathToEnemy = Dungeon.findPath(hero, nearest.pos,
                    Dungeon.level.passable, Dungeon.level.heroFOV, false);

            if (pathToEnemy == null || pathToEnemy.isEmpty()) {
                GLog.w(Messages.get(this, "no_path"));
                return;
            }

            Integer[] segment = pathToEnemy.toArray(new Integer[0]);
            int enemyCell = segment[segment.length - 1]; // = nearest.pos

            // 2단계: 적을 지나 벽까지 Ballistica로 연장
            // 진입 방향: 경로 마지막 두 칸 (또는 hero → enemy) 으로 계산
            int prevCell = segment.length >= 2 ? segment[segment.length - 2] : hero.pos;
            int w = Dungeon.level.width();
            int dCol = Integer.signum((enemyCell % w) - (prevCell % w));
            int dRow = Integer.signum((enemyCell / w) - (prevCell / w));
            int extendDir = dRow * w + dCol;

            ArrayList<Integer> combined = new ArrayList<>(Arrays.asList(segment));

            // extendDir != 0이고 extendTarget이 맵 범위 안일 때만 연장
            int extendTarget = enemyCell + extendDir;
            if (extendDir != 0
                    && extendTarget >= 0
                    && extendTarget < Dungeon.level.length()) {

                Ballistica extension = new Ballistica(enemyCell, extendTarget, Ballistica.STOP_SOLID);
                // extension.path.get(0) = enemyCell(이미 포함됨), index 1부터 추가
                for (int i = 1; i <= extension.dist; i++) {
                    combined.add(extension.path.get(i));
                }
            }

            final Integer[] fullPath = combined.toArray(new Integer[0]);

            Sample.INSTANCE.play(Assets.Sounds.HORSE);

            GLog.i(Messages.get(this, "used"));
            detach(hero.belongings.backpack);

            hero.spend(2f);
            hero.busy();

            dashStep(hero, fullPath, 0);
        }
    }

    // U2의 DashTile() + FishSprite.DashATile() 구조를 Hero에 맞게 이식
    private static void dashStep(final Hero hero, final Integer[] pathArr, final int counter) {
        if (counter < pathArr.length) {
            final int cell = pathArr[counter];

            PixelScene.shake(0.8f, 0.125f);

            hero.sprite.jump(hero.pos, cell, 0f, 0.07f, new Callback() {
                @Override
                public void call() {

                    if (Dungeon.level.map[hero.pos] == Terrain.OPEN_DOOR) {
                        Door.leave(hero.pos);
                    }

                    CellEmitter.get(hero.pos).burst(ChallengeParticle.FACTORY, 2);

                    // 경로 위의 적 통과하면서 타격 (NPC 제외)
                    Char ch = Actor.findChar(cell);
                    if (ch != null && ch != hero && !(ch instanceof NPC)) {
                        Sample.INSTANCE.play(Assets.Sounds.HIT_STRONG);
                        int dmg = Random.NormalIntRange(50, 60);
                        dmg = Math.max(0, dmg - ch.drRoll());
                        ch.damage(Math.round(dmg * 1.2f), hero);
                    }

                    hero.pos = cell;
                    Dungeon.level.occupyCell(hero);

                    // 다음 타일로 계속 돌진
                    hero.sprite.jump(hero.pos, hero.pos, 0f, 0.04f, new Callback() {
                        @Override
                        public void call() {
                            dashStep(hero, pathArr, counter + 1);
                        }
                    });
                }
            });

        } else {
            // 경로 끝(벽) 도달: 주변 8칸 뜯어먹기 강타
            PixelScene.shake(1.1f, 0.3f);
            Sample.INSTANCE.play(Assets.Sounds.EAT);

            for (int i : PathFinder.NEIGHBOURS8) {
                CellEmitter.get(hero.pos + i).burst(ChallengeParticle.FACTORY, 5);

                Char ch = Actor.findChar(hero.pos + i);
                if (ch != null && ch.isAlive() && ch != hero && !(ch instanceof NPC)) {
                    int dmg = Random.NormalIntRange(50, 60);
                    dmg = Math.max(0, dmg - ch.drRoll());
                    ch.damage(Math.round(dmg * 1.2f), hero);
                }
            }

            Buff.prolong(hero, Paralysis.class, 3f);
            Dungeon.observe();
            GameScene.updateFog();
            hero.checkVisibleMobs();
            hero.next();
        }
    }
}
