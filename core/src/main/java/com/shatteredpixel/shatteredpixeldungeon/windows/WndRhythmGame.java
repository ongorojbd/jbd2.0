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

package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.food.PhantomMeat;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.noosa.tweeners.ScaleTweener;
import com.watabou.utils.Callback;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Iterator;

public class WndRhythmGame extends Window {

    // 현재 열려있는 인스턴스 추적 (중복 창 방지)
    public static WndRhythmGame instance = null;

    // 창 크기
    private int WIDTH;
    private int HEIGHT;

    // 게임 영역 관련 상수
    private float gameAreaWidth;
    private float gameAreaHeight;
    private float gameAreaLeft;
    private float gameAreaTop;

    // 레인 관련 (3개)
    private static final int LANE_COUNT = 3;
    private float laneWidth;
    private float[] laneXPositions; // 각 레인의 X 위치
    private float hitLineY; // 판정선 Y 위치

    // 노트 관련
    private ArrayList<Note> notes = new ArrayList<>();
    private float noteSpeed = 200f; // 노트 떨어지는 속도
    private float noteSpawnTimer = 0;
    private float noteSpawnInterval = 0.6f; // 기본 스폰 간격

    // 게임 시간 관련
    private static final float GAME_DURATION = 15.0f; // 15초
    private float gameTimer = 0;

    // 판정 관련
    private static final float PERFECT_WINDOW = 0.08f; // Perfect 판정 범위 (초) - 더 좁게
    private static final float GOOD_WINDOW = 0.25f;    // Good 판정 범위 (초)
    private static final float MIN_NOTE_INTERVAL = 0.3f; // 최소 노트 간격 (초) - 동시 도달 방지

    // 점수 및 통계
    private int perfectCount = 0;
    private int goodCount = 0;
    private int missCount = 0;
    private int totalNotes = 0;

    // UI 요소
    private RenderedTextBlock titleText;
    private RenderedTextBlock timerText;
    private RenderedTextBlock scoreText;

    private ColorBlock gameAreaBackground;
    private ColorBlock gameAreaBorder;
    private ColorBlock[] laneDividers; // 레인 구분선
    private ColorBlock[] hitLines;     // 각 레인의 판정선
    private ColorBlock[] perfectZones;  // 퍼펙트 판정 영역 (시각적 표시)

    // 버튼 (3개 레인)
    private RedButton[] laneButtons = new RedButton[LANE_COUNT];
    private RedButton closeButton;

    // 게임 상태
    private enum GameState {
        COUNTDOWN,  // 카운트다운 중
        PLAYING,    // 게임 진행 중
        FINISHED    // 게임 종료
    }

    private GameState state = GameState.COUNTDOWN;
    private float countdownTimer = 0.5f; // 0.5초 카운트다운

    // 콜백 (결과 전달용)
    private Callback onSuccess;
    private Callback onFail;

    // 노트 클래스 (내부)
    private class Note {
        ColorBlock noteBlock;
        int lane; // 0, 1, 2 중 하나
        float y;
        boolean active = true;
        boolean hit = false; // 이미 판정되었는지

        Note(int laneIndex, float startY) {
            this.lane = laneIndex;
            this.y = startY;

            // 노트 블록 생성 (각 레인 색상 다르게)
            int[] laneColors = {0xFF4444FF, 0xFFFF4444, 0xFF44FF44}; // 파랑, 빨강, 초록
            noteBlock = new ColorBlock(laneWidth - 4, 12, laneColors[laneIndex]);
            add(noteBlock);

            updatePosition();
        }

        void updatePosition() {
            float x = laneXPositions[lane];
            noteBlock.x = gameAreaLeft + x + 2;
            noteBlock.y = gameAreaTop + y;
        }

        void update(float elapsed) {
            y += noteSpeed * elapsed;
            updatePosition();

            // 화면 밖으로 나가면 제거 (맨 밑까지 내려온 경우)
            if (y > gameAreaHeight + 20) {
                active = false;
                if (!hit) {
                    // 아직 판정되지 않았으면 Miss
                    hit = true;
                    missCount++;
                    totalNotes++;
                }
                noteBlock.destroy();
            }
        }

        void destroy() {
            if (noteBlock != null) {
                noteBlock.destroy();
                remove(noteBlock);
            }
        }

        // 판정 (버튼을 눌렀을 때 호출)
        String judge() {
            if (hit) return null; // 이미 판정됨

            // 퍼펙트 영역 범위 계산
            float perfectZoneHeight = PERFECT_WINDOW * noteSpeed;
            float perfectZoneTop = hitLineY - perfectZoneHeight / 2;
            float perfectZoneBottom = hitLineY + perfectZoneHeight / 2;
            
            // 노트의 중심점 (노트 높이 12픽셀)
            float noteCenterY = y + 6;
            
            // 노트의 중심점이 퍼펙트 영역 안에 있는지 확인
            boolean isCenterInPerfectZone = (noteCenterY >= perfectZoneTop && noteCenterY <= perfectZoneBottom);
            
            // 판정선까지의 거리 (굿 판정 범위 확인용)
            float distance = Math.abs(noteCenterY - hitLineY);
            float timeDiff = distance / noteSpeed;

            if (isCenterInPerfectZone) {
                // 노트의 중심점이 퍼펙트 영역 안에 있음
                hit = true;
                active = false;
                perfectCount++;
                totalNotes++;
                // 퍼펙트 판정 시 임팩트 효과: 노트가 커지면서 사라짐
                noteBlock.parent.add(new ScaleTweener(noteBlock, new PointF(2.5f, 2.5f), 0.2f));
                noteBlock.parent.add(new AlphaTweener(noteBlock, 0f, 0.2f) {
                    @Override
                    protected void onComplete() {
                        super.onComplete();
                        noteBlock.destroy();
                    }
                });
                return "PERFECT";
            } else if (timeDiff <= GOOD_WINDOW) {
                // 퍼펙트 영역 밖에 걸쳐있지만 굿 판정 범위 안
                hit = true;
                active = false;
                goodCount++;
                totalNotes++;
                noteBlock.destroy();
                return "GOOD";
            }
            // 너무 일찍 누름 (판정선 위에 있음)
            return null;
        }
    }

    public WndRhythmGame(Callback onSuccess, Callback onFail) {
        super();

        instance = this;

        this.onSuccess = onSuccess;
        this.onFail = onFail;

        // 고정 설정
        noteSpeed = 300f;
        noteSpawnInterval = 0.8f;

        // 화면 방향에 따른 크기 조절
        boolean landscape = PixelScene.landscape();

        if (landscape) {
            WIDTH = 200;
            HEIGHT = 180;
            gameAreaWidth = 180;
            gameAreaHeight = 100;
            gameAreaLeft = 10;
            gameAreaTop = 35;
        } else {
            WIDTH = 160;
            HEIGHT = 220;
            gameAreaWidth = 140;
            gameAreaHeight = 120;
            gameAreaLeft = 10;
            gameAreaTop = 40;
        }

        resize(WIDTH, HEIGHT);

        // 레인 설정
        laneWidth = gameAreaWidth / LANE_COUNT;
        laneXPositions = new float[LANE_COUNT];
        for (int i = 0; i < LANE_COUNT; i++) {
            laneXPositions[i] = i * laneWidth;
        }

        // 판정선 위치 (게임 영역 하단에서 약간 위)
        hitLineY = gameAreaHeight - 30;

        setupUI();
    }


    private void setupUI() {
        // 제목
        titleText = PixelScene.renderTextBlock(Messages.get(this, "title"), 9);
        titleText.hardlight(0xFFDD44);
        titleText.setPos((WIDTH - titleText.width()) / 2, 3);
        add(titleText);

        // 타이머 텍스트
        timerText = PixelScene.renderTextBlock("", 7);
        timerText.hardlight(0xFFFFFF);
        timerText.setPos((WIDTH - timerText.width()) / 2, 16);
        add(timerText);

        // 점수 텍스트
        scoreText = PixelScene.renderTextBlock("", 6);
        scoreText.hardlight(0xFFFFFF);
        scoreText.setPos((WIDTH - scoreText.width()) / 2, 25);
        add(scoreText);

        // 게임 영역 테두리
        gameAreaBorder = new ColorBlock(gameAreaWidth + 4, gameAreaHeight + 4, 0xFF444466);
        gameAreaBorder.x = gameAreaLeft - 2;
        gameAreaBorder.y = gameAreaTop - 2;
        add(gameAreaBorder);

        // 게임 영역 배경
        gameAreaBackground = new ColorBlock(gameAreaWidth, gameAreaHeight, 0xFF1a1a2e);
        gameAreaBackground.x = gameAreaLeft;
        gameAreaBackground.y = gameAreaTop;
        add(gameAreaBackground);

        // 레인 구분선
        laneDividers = new ColorBlock[LANE_COUNT - 1];
        for (int i = 0; i < LANE_COUNT - 1; i++) {
            laneDividers[i] = new ColorBlock(2, gameAreaHeight, 0xFF3a3a5e);
            laneDividers[i].x = gameAreaLeft + (i + 1) * laneWidth - 1;
            laneDividers[i].y = gameAreaTop;
            add(laneDividers[i]);
        }

        // 퍼펙트 판정 영역 (각 레인마다) - 먼저 추가해서 뒤에 표시
        perfectZones = new ColorBlock[LANE_COUNT];
        float perfectZoneHeight = PERFECT_WINDOW * noteSpeed;
        for (int i = 0; i < LANE_COUNT; i++) {
            perfectZones[i] = new ColorBlock(laneWidth - 2, perfectZoneHeight, 0xAAFFDD00); // 반투명 노란색 (ARGB: AA=알파, FFDD00=노란색)
            perfectZones[i].x = gameAreaLeft + laneXPositions[i] + 1;
            perfectZones[i].y = gameAreaTop + hitLineY - perfectZoneHeight / 2;
            add(perfectZones[i]);
        }

        // 판정선 제거 (노란색 선 없음)
        hitLines = new ColorBlock[LANE_COUNT];
        for (int i = 0; i < LANE_COUNT; i++) {
            hitLines[i] = null; // 더 이상 표시하지 않음
        }

        // 레인 버튼 (3개)
        float buttonY = gameAreaTop + gameAreaHeight + 8;
        float buttonWidth = (WIDTH - 30) / LANE_COUNT;
        float buttonHeight = 32;

        String[] buttonLabels = {Messages.get(this, "catch1"), Messages.get(this, "catch2"), Messages.get(this, "catch3")};
        int buttonTextColor = 0xFFFFDD00; // 노란색으로 통일

        for (int i = 0; i < LANE_COUNT; i++) {
            final int laneIndex = i;
            laneButtons[i] = new RedButton(buttonLabels[i]) {
                @Override
                protected void onClick() {
                    if (state == GameState.PLAYING) {
                        hitLane(laneIndex);
                    }
                }
            };
            laneButtons[i].setRect(10 + i * (buttonWidth + 5), buttonY, buttonWidth, buttonHeight);
            laneButtons[i].enable(false);
            laneButtons[i].alpha(0.5f);
            // 버튼 텍스트 색상 노란색으로 통일
            laneButtons[i].textColor(buttonTextColor);
            add(laneButtons[i]);
        }
    }

    private void hitLane(int laneIndex) {
        // 해당 레인의 가장 가까운 노트 찾기
        Note closestNote = null;
        float closestDistance = Float.MAX_VALUE;

        for (Note note : notes) {
            if (note.active && !note.hit && note.lane == laneIndex) {
                float distance = Math.abs(note.y - hitLineY);
                if (distance < closestDistance) {
                    closestDistance = distance;
                    closestNote = note;
                }
            }
        }

        if (closestNote != null) {
            // 퍼펙트 판정 범위 밖에 있으면 Miss
            float perfectZoneHeight = PERFECT_WINDOW * noteSpeed;
            float perfectZoneTop = hitLineY - perfectZoneHeight / 2;
            float perfectZoneBottom = hitLineY + perfectZoneHeight / 2;
            float noteTop = closestNote.y;
            float noteBottom = closestNote.y + 12;
            
            // 노트가 퍼펙트 영역과 완전히 멀리 떨어져 있는지 확인
            boolean isFarFromPerfect = (noteBottom < perfectZoneTop || noteTop > perfectZoneBottom);
            
            // 굿 판정 범위도 벗어났는지 확인
            float timeDiff = closestDistance / noteSpeed;
            boolean isOutOfGoodRange = timeDiff > GOOD_WINDOW;
            
            if (isFarFromPerfect && isOutOfGoodRange) {
                // 퍼펙트 판정과 멀리 떨어져 있고 굿 범위도 벗어남 -> Miss
                closestNote.hit = true;
                closestNote.active = false;
                missCount++;
                totalNotes++;
                closestNote.noteBlock.destroy();
                Sample.INSTANCE.play(Assets.Sounds.MISS);
                updateScore();
            } else {
                // 판정 가능 범위 안에 있음
                String judgment = closestNote.judge();
                if (judgment != null) {
                    // 판정 이펙트
                    if (judgment.equals("PERFECT")) {
                        Sample.INSTANCE.play(Assets.Sounds.HIT_PARRY);
                        Camera.main.shake(2, 0.2f);
                    } else if (judgment.equals("GOOD")) {
                        Sample.INSTANCE.play(Assets.Sounds.HIT);
                        Camera.main.shake(1, 0.1f);
                    }
                    updateScore();
                } else {
                    // 너무 일찍 누름 (아직 판정 범위에 도달하지 않음)
                    Sample.INSTANCE.play(Assets.Sounds.MISS);
                }
            }
        } else {
            // 해당 레인에 노트가 없음
            Sample.INSTANCE.play(Assets.Sounds.MISS);
        }
    }

    private void spawnNote() {
        // 동시에 도달할 수 있는 노트가 있는지 확인
        // 각 레인별로 판정선에 도달할 때까지의 시간을 계산
        float[] laneArrivalTimes = new float[LANE_COUNT];
        for (int i = 0; i < LANE_COUNT; i++) {
            laneArrivalTimes[i] = Float.MAX_VALUE; // 초기값: 도달 시간 없음
        }
        
        for (Note note : notes) {
            if (note.active) {
                // 판정선까지의 거리
                float distanceToHitLine = hitLineY - note.y;
                if (distanceToHitLine > 0) {
                    // 판정선에 도달할 때까지의 시간
                    float arrivalTime = distanceToHitLine / noteSpeed;
                    // 더 가까운 노트의 시간으로 업데이트
                    if (arrivalTime < laneArrivalTimes[note.lane]) {
                        laneArrivalTimes[note.lane] = arrivalTime;
                    }
                }
            }
        }
        
        // 비어있는 레인 찾기 (동시 도달 방지)
        ArrayList<Integer> availableLanes = new ArrayList<>();
        for (int i = 0; i < LANE_COUNT; i++) {
            boolean canSpawn = true;
            
            // 이 레인에 노트가 있고, 곧 도달할 예정이면 스폰 불가
            if (laneArrivalTimes[i] < MIN_NOTE_INTERVAL) {
                canSpawn = false;
            }
            
            // 다른 레인의 노트와 동시에 도달할 수 있는지 확인
            if (canSpawn) {
                for (int j = 0; j < LANE_COUNT; j++) {
                    if (i != j && laneArrivalTimes[j] < Float.MAX_VALUE) {
                        // 새 노트가 판정선에 도달할 때까지의 시간
                        float newNoteArrivalTime = (hitLineY - (-15)) / noteSpeed;
                        // 시간 차이가 최소 간격보다 작으면 동시 도달 가능
                        if (Math.abs(newNoteArrivalTime - laneArrivalTimes[j]) < MIN_NOTE_INTERVAL) {
                            canSpawn = false;
                            break;
                        }
                    }
                }
            }
            
            if (canSpawn) {
                availableLanes.add(i);
            }
        }
        
        // 비어있는 레인이 없으면 최소 간격을 두고 스폰
        if (availableLanes.isEmpty()) {
            // 가장 늦게 도달하는 레인 찾기
            float maxArrivalTime = 0;
            int bestLane = 0;
            for (int i = 0; i < LANE_COUNT; i++) {
                float arrivalTime = laneArrivalTimes[i] < Float.MAX_VALUE ? laneArrivalTimes[i] : 0;
                if (arrivalTime > maxArrivalTime) {
                    maxArrivalTime = arrivalTime;
                    bestLane = i;
                }
            }
            availableLanes.add(bestLane);
        }
        
        // 비어있는 레인 중 랜덤 선택
        int randomLane = availableLanes.get(Random.Int(availableLanes.size()));
        Note note = new Note(randomLane, -15);
        notes.add(note);
    }

    private void updateScore() {
        // Miss는 화면에 표시하지 않음
        scoreText.text(String.format("P:%d G:%d", perfectCount, goodCount));
        scoreText.setPos((WIDTH - scoreText.width()) / 2, 25);
    }

    private void cleanupNotes() {
        Iterator<Note> it = notes.iterator();
        while (it.hasNext()) {
            Note note = it.next();
            if (!note.active) {
                note.destroy();
                it.remove();
            }
        }
    }

    private void finishGame() {
        state = GameState.FINISHED;

        // 버튼 비활성화
        for (RedButton btn : laneButtons) {
            btn.enable(false);
        }

        // 등급 계산 (퍼펙트 횟수 기준: A=16회 이상, B=10회 이상, C=그 외)
        String grade;
        String text;

        if (perfectCount >= 16) {
            grade = "A";
            text = "완벽한 캐치야! 호흡이 척척 맞네!";
        } else if (perfectCount >= 10) {
            grade = "B";
            text = "오오, 제법인데?";
        } else {
            grade = "C";
            text = "아, 타이밍이 아쉬워!";
        }

        // 결과 표시
        String resultMessage = Messages.get(this, "success") + " [" + grade + "]: " + text;
        timerText.text(resultMessage);
        timerText.hardlight(0x44FF44);
        Sample.INSTANCE.play(Assets.Sounds.SO1);
        Camera.main.shake(3, 0.3f);

        // 보상 지급
        if (Dungeon.hero != null) {
            if (grade.equals("A")) {
                // A 등급: PhantomMeat 2개
                PhantomMeat meat1 = new PhantomMeat();
                PhantomMeat meat2 = new PhantomMeat();
                if (meat1.doPickUp(Dungeon.hero)) {
                    if (meat2.doPickUp(Dungeon.hero)) {
                        GLog.p(Messages.get(this, "reward_a"));
                    } else {
                        Dungeon.level.drop(meat2, Dungeon.hero.pos).sprite.drop();
                        GLog.p(Messages.get(this, "reward_a"));
                    }
                } else {
                    Dungeon.level.drop(meat1, Dungeon.hero.pos).sprite.drop();
                    Dungeon.level.drop(meat2, Dungeon.hero.pos).sprite.drop();
                }
            } else if (grade.equals("B")) {
                // B 등급: PhantomMeat 1개
                PhantomMeat meat = new PhantomMeat();
                if (meat.doPickUp(Dungeon.hero)) {
                    GLog.p(Messages.get(this, "reward_b"));
                } else {
                    Dungeon.level.drop(meat, Dungeon.hero.pos).sprite.drop();
                }
            } else {
                // C 등급: 50 골드
                Gold gold = new Gold(50);
                if (gold.doPickUp(Dungeon.hero)) {
                    GLog.p(Messages.get(this, "reward_c"));
                } else {
                    Dungeon.level.drop(gold, Dungeon.hero.pos).sprite.drop();
                }
            }
        }

        if (onSuccess != null) {
            onSuccess.call();
            onSuccess = null;
        }
        timerText.setPos((WIDTH - timerText.width()) / 2, 16);

        // 남은 노트 제거
        for (Note note : notes) {
            note.destroy();
        }
        notes.clear();

        showCloseButton();
    }

    private void showCloseButton() {
        // 기존 버튼 숨기기
        for (RedButton btn : laneButtons) {
            btn.visible = false;
            btn.active = false;
        }

        // 닫기 버튼 표시
        float buttonY = gameAreaTop + gameAreaHeight + 8;
        closeButton = new RedButton(Messages.get(this, "close")) {
            @Override
            protected void onClick() {
                hide();
            }
        };
        closeButton.setRect(20, buttonY, WIDTH - 40, 32);
        add(closeButton);
    }

    @Override
    public void update() {
        super.update();

        float elapsed = Game.elapsed;

        if (state == GameState.COUNTDOWN) {
            countdownTimer -= elapsed;

            int countdown = (int) Math.ceil(countdownTimer);
            if (countdown > 0) {
                // 카운트다운 숫자는 표시하지 않음
                timerText.text("");
            } else {
                timerText.text(Messages.get(this, "start"));
                timerText.hardlight(0xFF44FF44);
            }
            timerText.setPos((WIDTH - timerText.width()) / 2, 16);

            if (countdownTimer <= 0) {
                state = GameState.PLAYING;
                for (RedButton btn : laneButtons) {
                    btn.enable(true);
                    btn.alpha(1f);
                }
                Sample.INSTANCE.play(Assets.Sounds.FF);
            }
        }
        else if (state == GameState.PLAYING) {
            gameTimer += elapsed;

            // 타이머 표시 업데이트
            float remaining = GAME_DURATION - gameTimer;
            if (remaining < 0) remaining = 0;
            timerText.text(String.format("%.1f", remaining));
            if (remaining <= 2f) {
                timerText.hardlight(0xFF44FF44);
            } else if (remaining <= 5f) {
                timerText.hardlight(0xFFFFAA44);
            } else {
                timerText.hardlight(0xFFFFFFFF);
            }
            timerText.setPos((WIDTH - timerText.width()) / 2, 16);

            // 노트 스폰
            noteSpawnTimer += elapsed;
            if (noteSpawnTimer >= noteSpawnInterval) {
                spawnNote();
                noteSpawnTimer = 0;

                // 후반부에는 가끔 2개씩 스폰
                if (gameTimer > GAME_DURATION * 0.6f && Random.Int(4) == 0) {
                    spawnNote();
                }
            }

            // 노트 업데이트
            for (Note note : notes) {
                if (note.active) {
                    note.update(elapsed);
                }
            }

            // 비활성화된 노트 정리
            cleanupNotes();

            // 게임 종료 체크
            if (gameTimer >= GAME_DURATION) {
                finishGame();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // 게임 중에는 뒤로가기 비활성화
        if (state == GameState.FINISHED) {
            super.onBackPressed();
        }
    }

    @Override
    public void destroy() {
        if (instance == this) {
            instance = null;
        }

        // 모든 노트 정리
        for (Note note : notes) {
            note.destroy();
        }
        notes.clear();

        super.destroy();
    }
}

