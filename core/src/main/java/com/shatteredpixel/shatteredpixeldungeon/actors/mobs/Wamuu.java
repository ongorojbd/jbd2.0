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

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.TargetedCell;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SparkParticle;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.ConeAOE;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ShamanSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.WamuuSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Wamuu extends Mob {

    {
        // Placeholder sprite
        spriteClass = WamuuSprite.class;

        HP = HT = 420;
        defenseSkill = 22;
        EXP = 25;
        maxLvl = 30;

        properties.add(Property.BOSS);
        properties.add(Property.DEMONIC);
    }

    private int phase = 0;
    private int sandstormCD = 8;       // 신사폭풍
    private int protectorCD = 12;      // 바람의 프로텍터
    private int finalModeCD = 18;      // 혼설삽
    private int sandstormWindup = 0;   // 신사폭풍 예고 대기턴
    private ArrayList<Integer> sandstormCells = new ArrayList<>();
    private int riftTurns = 0;         // 혼설삽: 전장 절단 패턴 남은 턴
    private int riftSafeCell = -1;     // 안전 타일

    private static final String PHASE = "phase";
    private static final String SAND_CD = "sand_cd";
    private static final String PROT_CD = "prot_cd";
    private static final String FINAL_CD = "final_cd";
    private static final String SAND_WINDUP = "sand_windup";
    private static final String SAND_CELLS = "sand_cells";
    private static final String RIFT_TURNS = "rift_turns";
    private static final String RIFT_SAFE = "rift_safe";

    @Override
    public void notice() {
        super.notice();
        if (!BossHealthBar.isAssigned()) {
            BossHealthBar.assignBoss(this);
        }
        sprite.showStatus(CharSprite.NEGATIVE, Messages.get(this, "notice"));
    }

    @Override
    public void damage(int dmg, Object src) {
        BossHealthBar.assignBoss(this);
        super.damage(dmg, src);

        if (phase == 0 && HP < HT * 2 / 3) {
            phase = 1;
            sprite.showStatus(CharSprite.WARNING, Messages.get(this, "focus_wind"));
            sandstormCD = Math.min(sandstormCD, 5);
            protectorCD = Math.min(protectorCD, 8);
        }
        if (phase == 1 && HP < HT / 3) {
            phase = 2;
            sprite.showStatus(CharSprite.WARNING, Messages.get(this, "final_mode"));
            finalModeCD = 10;
        }
    }

    @Override
    protected boolean act() {
        if (!BossHealthBar.isAssigned()) {
            BossHealthBar.assignBoss(this);
        }

        if (sandstormCD > 0) sandstormCD--;
        if (protectorCD > 0) protectorCD--;
        if (finalModeCD > 0) finalModeCD--;

        // resolve delayed sandstorm if telegraphed last turn
        if (sandstormWindup > 0) {
            sandstormWindup--;
            if (sandstormWindup == 0 && !sandstormCells.isEmpty()) {
                resolveSandstorm();
                return true;
            }
        }

        // Resolve rift pattern if active
        if (riftTurns > 0) {
            resolveRift();
            return true;
        }

        if (enemy != null) {
            // Priority: Final Mode in phase 2, then Sandstorm when close, then Protector
            if (phase >= 2 && finalModeCD <= 0) {
                if (finalMode()) return true;
            }
            if (sandstormCD <= 0 && Dungeon.level.distance(pos, enemy.pos) <= 4 && sandstormWindup == 0 && sandstormCells.isEmpty()) {
                if (telegraphSandstorm()) return true;
            }
            if (phase >= 1 && protectorCD <= 0) {
                if (windProtector()) return true;
            }
        }

        return super.act();
    }

    // 신사폭풍: 양팔 회전으로 큰 전방 원뿔 영역 절삭 피해, 출혈 부여
    private boolean telegraphSandstorm() {
        if (enemy == null) return false;
        // Compute and store target area
        Ballistica aim = new Ballistica(pos, enemy.pos, Ballistica.WONT_STOP);
        ConeAOE cone = new ConeAOE(aim, Float.POSITIVE_INFINITY, 70, Ballistica.STOP_SOLID);
        sandstormCells.clear();
        for (int cell : cone.cells) {
            if (!Dungeon.level.insideMap(cell) || Dungeon.level.solid[cell]) continue;
            sandstormCells.add(cell);
            // show telegraph
            sprite.parent.addToBack(new TargetedCell(cell, 0xFF00FF));
            CellEmitter.center(cell).burst(SparkParticle.FACTORY, 1);
        }
        Sample.INSTANCE.play(Assets.Sounds.CHARGEUP);
        sprite.showStatus(CharSprite.WARNING, Messages.get(this, "sandstorm_ready"));
        sandstormWindup = 1; // resolve next turn
        spend(1f);
        return true;
    }

    private void resolveSandstorm() {
        // Execute stored sandstorm
        Sample.INSTANCE.play(Assets.Sounds.BLAST);
        sprite.showStatus(CharSprite.WARNING, Messages.get(this, "sandstorm"));
        for (int cell : sandstormCells) {
            if (!Dungeon.level.insideMap(cell) || Dungeon.level.solid[cell]) continue;
            CellEmitter.center(cell).burst(SparkParticle.FACTORY, 2);
            Char ch = Actor.findChar(cell);
            if (ch != null && ch.alignment != alignment) {
                int dmg = Random.NormalIntRange(18, 26);
                if (phase >= 1) dmg += 4;
                ch.damage(dmg, this);
                Buff.affect(ch, Bleeding.class).set(0.35f * dmg);
                if (ch == Dungeon.hero && !ch.isAlive()) {
                    Dungeon.fail(getClass());
                }
            }
        }
        sandstormCells.clear();
        sandstormCD = phase >= 1 ? 7 : 9;
        spend(1f);
    }

    // 바람의 프로텍터: 잠시 투명화 및 접근전 대비
    private boolean windProtector() {
        Sample.INSTANCE.play(Assets.Sounds.CHARGEUP);
        sprite.showStatus(CharSprite.POSITIVE, Messages.get(this, "protector"));
        // brief invisibility, thematically refraction
        Buff.affect(this, Invisibility.class, 4f);
        // visual hint around Wamuu
        for (int i : PathFinder.NEIGHBOURS9) {
            int p = pos + i;
            if (Dungeon.level.insideMap(p) && !Dungeon.level.solid[p]) {
                CellEmitter.center(p).burst(SparkParticle.FACTORY, 1);
            }
        }
        protectorCD = 10;
        spend(1f);
        return true;
    }

    // 혼설삽: 10턴간 전장 전체를 절단, 무작위 1타일만 안전
    private boolean finalMode() {
        // choose a safe passable tile
        riftSafeCell = chooseSafeCell();
        riftTurns = 10;

        Sample.INSTANCE.play(Assets.Sounds.RAY);
        sprite.showStatus(CharSprite.NEGATIVE, Messages.get(this, "final_cast"));

        // initial telegraph across the map
        telegraphRift();

        // Wamuu strains body at start
        this.damage(Random.NormalIntRange(4, 8), this);

        finalModeCD = 16;
        spend(1f);
        return true;
    }

    private int chooseSafeCell() {
        // strictly choose an EMPTY terrain tile
        ArrayList<Integer> candidates = new ArrayList<>();
        int len = Dungeon.level.length();
        for (int c = 0; c < len; c++) {
            if (!Dungeon.level.insideMap(c)) continue;
            if (Dungeon.level.map[c] == Terrain.EMPTY && Actor.findChar(c) == null) {
                candidates.add(c);
            }
        }
        if (!candidates.isEmpty()) {
            return candidates.get(Random.Int(candidates.size()));
        }
        // fallback: any passable tile
        for (int c = 0; c < len; c++) {
            if (!Dungeon.level.insideMap(c)) continue;
            if (Dungeon.level.passable[c] && !Dungeon.level.solid[c] && Actor.findChar(c) == null) {
                candidates.add(c);
            }
        }
        if (!candidates.isEmpty()) {
            return candidates.get(Random.Int(candidates.size()));
        }
        // last resort
        return pos;
    }

    private void telegraphRift() {
        int len = Dungeon.level.length();
        for (int c = 0; c < len; c++) {
            if (!Dungeon.level.insideMap(c) || Dungeon.level.solid[c]) continue;
            if (c == riftSafeCell) continue;
            sprite.parent.addToBack(new TargetedCell(c, 0xFF00FF));
        }
        // mark safe cell with a softer hint
        sprite.parent.addToBack(new TargetedCell(riftSafeCell, 0x00FF00));
    }

    private void resolveRift() {
        // If 마지막 턴이면 피해 적용, 아니면 예고만 유지
        if (riftTurns == 1) {
            int len = Dungeon.level.length();
            for (int c = 0; c < len; c++) {
                if (!Dungeon.level.insideMap(c) || Dungeon.level.solid[c]) continue;
                if (c == riftSafeCell) continue;
                // VFX and damage
                sprite.parent.addToBack(new TargetedCell(c, 0xFF00FF));
                CellEmitter.center(c).burst(BlastParticle.FACTORY, 2);
                Char ch = Actor.findChar(c);
                if (ch != null && ch.alignment != alignment) {
                    int dmg = Random.NormalIntRange(28, 42);
                    if (phase >= 2) dmg += 6;
                    ch.damage(dmg, this);
                    if (ch == Dungeon.hero && !ch.isAlive()) {
                        Dungeon.fail(getClass());
                    }
                }
            }
            riftTurns = 0;
            riftSafeCell = -1;
            spend(1f);
        } else {
            // 유지 예고
            telegraphRift();
            riftTurns--;
            spend(1f);
        }
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(PHASE, phase);
        bundle.put(SAND_CD, sandstormCD);
        bundle.put(PROT_CD, protectorCD);
        bundle.put(FINAL_CD, finalModeCD);
        bundle.put(SAND_WINDUP, sandstormWindup);
        int[] cells = new int[sandstormCells.size()];
        for (int i = 0; i < sandstormCells.size(); i++) cells[i] = sandstormCells.get(i);
        bundle.put(SAND_CELLS, cells);
        bundle.put(RIFT_TURNS, riftTurns);
        bundle.put(RIFT_SAFE, riftSafeCell);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        phase = bundle.getInt(PHASE);
        sandstormCD = bundle.getInt(SAND_CD);
        protectorCD = bundle.getInt(PROT_CD);
        finalModeCD = bundle.getInt(FINAL_CD);
        sandstormWindup = bundle.getInt(SAND_WINDUP);
        sandstormCells.clear();
        for (int c : bundle.getIntArray(SAND_CELLS)) sandstormCells.add(c);
        riftTurns = bundle.getInt(RIFT_TURNS);
        riftSafeCell = bundle.getInt(RIFT_SAFE);
    }

    @Override
    public int damageRoll() { return Random.NormalIntRange(12, 22); }

    @Override
    public int attackSkill(Char target) { return 30; }

    @Override
    public int drRoll() { return Random.NormalIntRange(2, 6); }
}


