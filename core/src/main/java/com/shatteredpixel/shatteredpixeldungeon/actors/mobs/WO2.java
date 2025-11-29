/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2021 Evan Debenham
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
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Dominion;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Dread;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicalSleep;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Sleep;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vulnerable;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.TargetedCell;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MudaSprite;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class WO2 extends Mob {

    {
        spriteClass = MudaSprite.class;

        HP = HT = 500;

        defenseSkill = 25;
        EXP = 0;
        maxLvl = 30;
        viewDistance = 10;

        properties.add(Property.BOSS);
        immunities.add(Dominion.class);
        immunities.add(Terror.class);
        immunities.add(Dread.class);
        immunities.add(Amok.class);
        immunities.add(Blindness.class);
        immunities.add(Sleep.class);
        immunities.add(MagicalSleep.class);
    }

    private int wireCD = 5;
    private int wireWindup = 0;
    private ArrayList<Integer> wireCells = new ArrayList<>();
    private boolean wireActive = false;
    private int wireStage = 0; // 0: small, 1: mid, 2: large
    private ArrayList<Integer> wireRing1 = new ArrayList<>();
    private ArrayList<Integer> wireRing2 = new ArrayList<>();
    private ArrayList<Integer> wireRing3 = new ArrayList<>();

    private int chessboardCD = 2; // 처음부터 사용 가능
    private int chessboardWindup = 0;
    private boolean chessboardActive = false;
    private int chessboardStage = 0; // 0: white explode, 1: black telegraph, 2: black explode
    private ArrayList<Integer> chessboardWhite = new ArrayList<>();
    private ArrayList<Integer> chessboardBlack = new ArrayList<>();

    private int teleportCD = 15; // 텔레포트 패턴 쿨다운

    private static final String WIRE_CD = "wire_cd";
    private static final String WIRE_WIND = "wire_wind";
    private static final String CHESS_CD = "chess_cd";
    private static final String CHESS_WIND = "chess_wind";
    private static final String TELEPORT_CD = "teleport_cd";

    public static class WODamage {
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(35, 65);
    }

    @Override
    public int attackSkill(Char target) {
        return 65;
    }

    @Override
    public int drRoll() {
        int dr;
        if (Dungeon.isChallenged(Challenges.STRONGER_BOSSES)) {
            dr = Random.NormalIntRange(5, 19);
        } else {
            dr = Random.NormalIntRange(5, 15);
        }
        return dr;
    }


    @Override
    protected boolean act() {
        if (wireCD > 0) wireCD--;
        if (chessboardCD > 0) chessboardCD--;
        if (teleportCD > 0) teleportCD--;
        this.sprite.add(CharSprite.State.INVISIBLE);
        // resolve chessboard windup (체스판 패턴 중에도 자유롭게 행동 가능)
        if (chessboardWindup > 0) {
            chessboardWindup--;
            if (chessboardWindup == 0 && chessboardActive) {
                resolveChessboardTick();
                // 폭발 후에도 다른 행동 가능
                return super.act();
            } else {
                // show telegraph during windup (only for stages 0 and 2 which are waiting for explosion)
                if (chessboardStage == 0) {
                    for (int c : chessboardWhite) {
                        sprite.parent.addToBack(new TargetedCell(c, 0xFF00FF));
                    }
                } else if (chessboardStage == 2) {
                    for (int c : chessboardBlack) {
                        sprite.parent.addToBack(new TargetedCell(c, 0xFF00FF));
                    }
                }
                // telegraph만 표시하고 다른 행동 계속
            }
        }

        // resolve wire windup
        if (wireWindup > 0) {
            wireWindup--;
            if (wireWindup == 0 && wireActive) {
                resolveWireTick();
                // do not consume the turn; allow other actions/movement this act
                return super.act();
            }
        }

        // chessboard pattern has highest priority
        if (enemy != null && !chessboardActive && chessboardCD <= 0) {
            telegraphChessboard();
            return true;
        }

        // teleport pattern - 영웅 주변으로 텔레포트
        if (enemy != null && !wireActive && teleportCD <= 0 && Random.Int(4) == 0) {
            doTeleportToHero();
            return true;
        }

        // if not in wire pattern and cooldown is ready, trigger it
        if (enemy != null && !wireActive && wireCD <= 0 && Random.Int(3) == 0) {
            telegraphWire();
            return true;
        }

        return super.act();
    }

    @Override
    public void damage(int dmg, Object src) {
        if (dmg >= 200) {
            //takes 20/21/22/23/24/25/26/27/28/29/30 dmg
            // at   20/22/25/29/34/40/47/55/64/74/85 incoming dmg
            dmg = 200;
        }
        super.damage(dmg, src);
    }

    // 와이어(회오리 바람) - 도넛 3단계(작은→중간→큰)로 3턴 연속 타격
    private boolean telegraphWire() {
        wireCells.clear();
        wireRing1.clear();
        wireRing2.clear();
        wireRing3.clear();
        int w = Dungeon.level.width();
        int cx = pos % w, cy = pos / w;
        // build three rings with tight thickness for better readability
        buildRing(cx, cy, 1.0, 0.6, wireRing1);
        buildRing(cx, cy, 2.0, 0.6, wireRing2);
        buildRing(cx, cy, 3.0, 0.6, wireRing3);
        // show telegraph for first (small) ring only
        for (int c : wireRing1) {
            sprite.parent.addToBack(new TargetedCell(c, 0xFF00FF));
        }
        Sample.INSTANCE.play(Assets.Sounds.D11);
        sprite.showStatus(CharSprite.WARNING, Messages.get(WO.class, "s1"));
        wireActive = true;
        wireStage = 0;
        wireWindup = 1; // resolve small ring next turn
        spend(1f);
        return true;
    }

    private void resolveWireTick() {
        Sample.INSTANCE.play(Assets.Sounds.RAY);
        Dungeon.hero.interrupt();
        ArrayList<Integer> ring = (wireStage == 0 ? wireRing1 : wireStage == 1 ? wireRing2 : wireRing3);
        for (int c : ring) {
            CellEmitter.get(c).burst(MagicMissile.WardParticle.UP, 8);
            Char ch = Actor.findChar(c);
            if (ch != null && ch == Dungeon.hero) {
                int bonus = (wireStage == 2 ? 2 : 0);

                int dmg = Random.NormalIntRange(35, 45) + bonus;
                ch.damage(dmg, new WODamage());

                Buff.prolong(ch, Vulnerable.class, Vulnerable.DURATION);

                if (ch == Dungeon.hero && !ch.isAlive()) Dungeon.fail(getClass());
            }
        }
        // next stage or finish
        if (wireStage < 2) {
            wireStage++;
            // show next ring telegraph
            ArrayList<Integer> nextRing = (wireStage == 1 ? wireRing2 : wireRing3);
            for (int c : nextRing) {
                sprite.parent.addToBack(new TargetedCell(c, 0xFF00FF));
            }
            wireWindup = 1;
        } else {
            wireActive = false;
            wireRing1.clear();
            wireRing2.clear();
            wireRing3.clear();
            wireCD = 6;
        }
    }

    private void buildRing(int cx, int cy, double radius, double thickness, ArrayList<Integer> out) {
        int w = Dungeon.level.width();
        int len = Dungeon.level.length();
        for (int c = 0; c < len; c++) {
            if (!Dungeon.level.insideMap(c) || Dungeon.level.solid[c]) continue;
            int x = c % w, y = c / w;
            double dist = Math.hypot(x - cx, y - cy);
            if (Math.abs(dist - radius) <= thickness) out.add(c);
        }
    }

    // 체스판 패턴 - 흰색 영역 → 검정색 영역 순차 폭발
    private boolean telegraphChessboard() {
        chessboardWhite.clear();
        chessboardBlack.clear();

        int w = Dungeon.level.width();
        int len = Dungeon.level.length();

        // 맵의 모든 셀을 체스판 패턴으로 분류
        for (int c = 0; c < len; c++) {
            if (!Dungeon.level.insideMap(c) || Dungeon.level.solid[c]) continue;
            int x = c % w, y = c / w;
            if ((x + y) % 2 == 0) {
                chessboardWhite.add(c); // 흰색 (짝수)
            } else {
                chessboardBlack.add(c); // 검정색 (홀수)
            }
        }

        // 흰색 영역 표시
        for (int c : chessboardWhite) {
            sprite.parent.addToBack(new TargetedCell(c, 0xFF00FF));
        }

        Sample.INSTANCE.play(Assets.Sounds.D12);
        Camera.main.shake(9, 0.5f);
        sprite.showStatus(CharSprite.WARNING, Messages.get(WO.class, "s2"));
        chessboardActive = true;
        chessboardStage = 0;
        chessboardWindup = 1; // 1턴 후 폭발
        spend(1f);
        return true;
    }

    private void resolveChessboardTick() {
        if (chessboardStage == 0) {
            // Stage 0: 흰색 타일 폭발
            Sample.INSTANCE.play(Assets.Sounds.BLAST);
            Dungeon.hero.interrupt();

            for (int c : chessboardWhite) {
                CellEmitter.get(c).burst(MagicMissile.WardParticle.UP, 8);
                WandOfBlastWave.BlastWave.blast(c, 1);
            }
            for (int c : chessboardWhite) {
                Char ch = Actor.findChar(c);
                if (ch != null && ch.alignment != alignment) {
                    int dmg = Random.NormalIntRange(45, 55);
                    ch.damage(dmg, new WODamage());
                    if (ch == Dungeon.hero && !ch.isAlive()) {
                        Dungeon.fail(getClass());
                    }
                }
            }

            // 다음 단계: 검정색 표시 대기
            chessboardStage = 1;
            chessboardWindup = 1;

        } else if (chessboardStage == 1) {
            // Stage 1: 검정색 타일 표시
            for (int c : chessboardBlack) {
                sprite.parent.addToBack(new TargetedCell(c, 0xFF00FF));
            }
            Sample.INSTANCE.play(Assets.Sounds.CHARGEUP);
            Camera.main.shake(9, 0.5f);
            sprite.showStatus(CharSprite.WARNING, Messages.get(WO.class, "s2"));

            // 다음 단계: 검정색 폭발 대기
            chessboardStage = 2;
            chessboardWindup = 1;

        } else {
            // Stage 2: 검정색 타일 폭발
            Sample.INSTANCE.play(Assets.Sounds.BLAST);
            Dungeon.hero.interrupt();

            for (int c : chessboardBlack) {
                CellEmitter.get(c).burst(MagicMissile.WardParticle.UP, 8);
                WandOfBlastWave.BlastWave.blast(c, 1);
            }
            for (int c : chessboardBlack) {
                Char ch = Actor.findChar(c);
                if (ch != null && ch.alignment != alignment) {
                    int dmg = Random.NormalIntRange(45, 55);
                    ch.damage(dmg, new WODamage());
                    if (ch == Dungeon.hero && !ch.isAlive()) {
                        Dungeon.fail(getClass());
                    }
                }
            }

            // 패턴 종료
            chessboardActive = false;
            chessboardWhite.clear();
            chessboardBlack.clear();
            chessboardCD = 8;
        }
    }

    // 텔레포트 패턴 - 영웅 주변 8방향 중 하나로 텔레포트
    private void doTeleportToHero() {
        ArrayList<Integer> validCells = new ArrayList<>();

        for (int i : PathFinder.NEIGHBOURS8) {
            int cell = Dungeon.hero.pos + i;
            if (Dungeon.level.passable[cell] && Actor.findChar(cell) == null) {
                validCells.add(cell);
            }
        }

        if (!validCells.isEmpty()) {
            int targetCell = Random.element(validCells);
            ScrollOfTeleportation.appear(this, targetCell);
            WO.d2class();
            sprite.showStatus(CharSprite.WARNING, Messages.get(WO.class, "s3"));
            teleportCD = 8;
            spend(1f);
        }
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(WIRE_CD, wireCD);
        bundle.put(WIRE_WIND, wireWindup);
        bundle.put(CHESS_CD, chessboardCD);
        bundle.put(CHESS_WIND, chessboardWindup);
        bundle.put(TELEPORT_CD, teleportCD);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        wireCD = bundle.getInt(WIRE_CD);
        wireWindup = bundle.getInt(WIRE_WIND);
        chessboardCD = bundle.getInt(CHESS_CD);
        chessboardWindup = bundle.getInt(CHESS_WIND);
        teleportCD = bundle.getInt(TELEPORT_CD);
    }

}
