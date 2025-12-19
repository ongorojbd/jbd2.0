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
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.Camera;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Iterator;

public class WndDiegoDodgeGame extends Window {

    // 현재 열려있는 인스턴스 추적 (중복 창 방지)
    public static WndDiegoDodgeGame instance = null;

    // 창 크기
    private int WIDTH;
    private int HEIGHT;

    // 게임 영역 관련 상수
    private float gameAreaWidth;
    private float gameAreaHeight;
    private float gameAreaLeft;
    private float gameAreaTop;

    // 플레이어 관련
    private float playerX;           // 플레이어 X 위치 (0 ~ gameAreaWidth)
    private float playerSpeed = 120f; // 플레이어 이동 속도
    private static final float PLAYER_WIDTH = 16f;
    private static final float PLAYER_HEIGHT = 20f;

    // 이동 상태
    private boolean movingLeft = false;
    private boolean movingRight = false;

    // 나이프(탄막) 관련
    private ArrayList<Knife> knives = new ArrayList<>();
    private float knifeSpawnTimer = 0;
    private float knifeSpawnInterval = 0.8f; // 기본 스폰 간격
    private float knifeSpeed = 130f;         // 나이프 속도

    // 게임 시간 관련
    private static final float GAME_DURATION = 5.0f; // 5초
    private float gameTimer = 0;

    // 무적 시간 (연속 피격 방지)
    private float invincibilityTimer = 0;
    private static final float INVINCIBILITY_DURATION = 0.8f;

    // 피격 횟수 추적 (완벽한 회피 체크용)
    private int hitCount = 0;

    // 난이도 (phase에 따라)
    private int difficulty;

    // UI 요소
    private RenderedTextBlock titleText;
    private RenderedTextBlock timerText;

    private ColorBlock gameAreaBackground;
    private ColorBlock gameAreaBorder;
    private ColorBlock player;

    // 플레이어 외곽선 (시각적 구분용)
    private ColorBlock playerBorderTop;
    private ColorBlock playerBorderBottom;
    private ColorBlock playerBorderLeft;
    private ColorBlock playerBorderRight;

    private RedButton leftButton;
    private RedButton rightButton;
    private RedButton closeButton;

    // 게임 상태
    private enum GameState {
        COUNTDOWN,  // 카운트다운 중
        PLAYING,    // 게임 진행 중
        SUCCESS,    // 성공
        FAIL        // 실패
    }

    private GameState state = GameState.COUNTDOWN;
    private float countdownTimer = 0.5f; // 0.5초 카운트다운

    // 콜백 (결과 전달용)
    private Callback onSuccess;
    private Callback onFail;

    // 나이프 클래스 (내부)
    private class Knife {
        ColorBlock knifeBlock;  // 나이프 전체 (하나의 막대기)

        float x, y;
        float velocityY;
        float velocityX;
        boolean active = true;
        float rotation = 0;

        Knife(float startX, float startY, float vY, float vX) {
            this.x = startX;
            this.y = startY;
            this.velocityY = vY;
            this.velocityX = vX;
            this.rotation = Random.Float(360f); // 랜덤 초기 회전

            // 나이프 전체를 하나의 막대기로 (은색 그라데이션 느낌)
            knifeBlock = new ColorBlock(4, 18, 0xFFBBBBBB);
            add(knifeBlock);

            updatePosition();
        }

        void updatePosition() {
            float centerX = gameAreaLeft + x;
            float centerY = gameAreaTop + y;

            // 나이프 중심에 위치
            knifeBlock.x = centerX - 2;
            knifeBlock.y = centerY - 9;
        }

        void update(float elapsed) {
            y += velocityY * elapsed;
            x += velocityX * elapsed;

            // 회전 (적당한 속도로 회전)
            rotation += 360f * elapsed * 1.0f;
            if (rotation > 360f) rotation -= 360f;

            // 위치 업데이트
            updatePosition();

            // 게임 영역 밖으로 나가면 비활성화
            if (y > gameAreaHeight + 20) {
                active = false;
            }
        }

        void destroy() {
            if (knifeBlock != null) {
                knifeBlock.destroy();
                remove(knifeBlock);
            }
        }

        // 충돌 검사 (회전하는 나이프에 맞춰 원형 충돌 박스 사용)
        boolean checkCollision(float px, float py, float pw, float ph) {
            // 나이프 중심점
            float knifeCenterX = x;
            float knifeCenterY = y;
            float knifeRadius = 6f; // 나이프 반지름 (단순화되어 약간 줄임)

            // 플레이어 중심점
            float playerCenterX = px + pw / 2;
            float playerCenterY = py + ph / 2;
            float playerRadius = Math.min(pw, ph) / 2;

            // 원형 충돌 검사 (두 중심점 사이 거리)
            float dx = knifeCenterX - playerCenterX;
            float dy = knifeCenterY - playerCenterY;
            float distance = (float) Math.sqrt(dx * dx + dy * dy);

            // 두 반지름의 합보다 가까우면 충돌
            return distance < (knifeRadius + playerRadius - 2); // 약간의 여유
        }
    }

    public WndDiegoDodgeGame(int difficulty, Callback onSuccess, Callback onFail) {
        super();

        instance = this;

        this.difficulty = difficulty;
        this.onSuccess = onSuccess;
        this.onFail = onFail;

        // 난이도에 따른 설정 조절
        adjustDifficulty();

        // 화면 방향에 따른 크기 조절
        boolean landscape = PixelScene.landscape();

        if (landscape) {
            WIDTH = 180;
            HEIGHT = 160;
            gameAreaWidth = 160;
            gameAreaHeight = 90;
            gameAreaLeft = 10;
            gameAreaTop = 35;
        } else {
            WIDTH = 140;
            HEIGHT = 200;
            gameAreaWidth = 120;
            gameAreaHeight = 110;
            gameAreaLeft = 10;
            gameAreaTop = 40;
        }

        resize(WIDTH, HEIGHT);

        // 플레이어 시작 위치 (중앙)
        playerX = (gameAreaWidth - PLAYER_WIDTH) / 2;

        setupUI();
    }

    private void adjustDifficulty() {
        // 모든 페이즈에서 동일한 난이도 (난이도 3 수준)
        knifeSpawnInterval = 0.25f;  // 나이프 스폰 간격
        knifeSpeed = 130f;  // 나이프 속도
        playerSpeed = 140f;  // 플레이어 속도
    }

    private void setupUI() {
        // 제목
        titleText = PixelScene.renderTextBlock(Messages.get(this, "title"), 9);
        titleText.hardlight(0xFFDD44);
        titleText.setPos((WIDTH - titleText.width()) / 2, 3);
        add(titleText);

        // 타이머 텍스트
        timerText = PixelScene.renderTextBlock(Messages.get(this, "countdown"), 7);
        timerText.hardlight(0xFFFFFF);
        timerText.setPos((WIDTH - timerText.width()) / 2, 16);
        add(timerText);

        // 게임 영역 테두리
        gameAreaBorder = new ColorBlock(gameAreaWidth + 4, gameAreaHeight + 4, 0xFF444466);
        gameAreaBorder.x = gameAreaLeft - 2;
        gameAreaBorder.y = gameAreaTop - 2;
        add(gameAreaBorder);

        // 게임 영역 배경 (어두운 보라색 그라데이션 느낌)
        gameAreaBackground = new ColorBlock(gameAreaWidth, gameAreaHeight, 0xFF1a1a2e);
        gameAreaBackground.x = gameAreaLeft;
        gameAreaBackground.y = gameAreaTop;
        add(gameAreaBackground);

        // 바닥 라인 (플레이어 위치 표시)
        ColorBlock floorLine = new ColorBlock(gameAreaWidth, 2, 0xFF3a3a5e);
        floorLine.x = gameAreaLeft;
        floorLine.y = gameAreaTop + gameAreaHeight - PLAYER_HEIGHT - 5;
        add(floorLine);

        // 플레이어 외곽선 (먼저 추가해서 뒤에 표시)
        float playerY = gameAreaHeight - PLAYER_HEIGHT - 5;

        playerBorderTop = new ColorBlock(PLAYER_WIDTH + 1, 1, 0xFF00FFFF);
        playerBorderBottom = new ColorBlock(PLAYER_WIDTH + 1, 1, 0xFF00FFFF);
        playerBorderLeft = new ColorBlock(1, PLAYER_HEIGHT + 1, 0xFF00FFFF);
        playerBorderRight = new ColorBlock(1, PLAYER_HEIGHT + 1, 0xFF00FFFF);
        add(playerBorderTop);
        add(playerBorderBottom);
        add(playerBorderLeft);
        add(playerBorderRight);

        // 플레이어 (시안색 사각형)
        player = new ColorBlock(PLAYER_WIDTH, PLAYER_HEIGHT, 0xFF00CCCC);
        player.x = gameAreaLeft + playerX;
        player.y = gameAreaTop + playerY;
        add(player);

        updatePlayerBorders();

        // 조작 버튼
        float buttonY = gameAreaTop + gameAreaHeight + 8;
        float buttonWidth = (WIDTH - 30) / 2;
        float buttonHeight = 28;

        leftButton = new RedButton("◀ " + Messages.get(this, "left")) {
            @Override
            protected void onPointerDown() {
                super.onPointerDown();
                if (state == GameState.PLAYING) {
                    movingLeft = true;
                }
            }

            @Override
            protected void onPointerUp() {
                super.onPointerUp();
                movingLeft = false;
            }
        };
        leftButton.setRect(10, buttonY, buttonWidth, buttonHeight);
        leftButton.enable(false);
        leftButton.alpha(0.5f);
        add(leftButton);

        rightButton = new RedButton(Messages.get(this, "right") + " ▶") {
            @Override
            protected void onPointerDown() {
                super.onPointerDown();
                if (state == GameState.PLAYING) {
                    movingRight = true;
                }
            }

            @Override
            protected void onPointerUp() {
                super.onPointerUp();
                movingRight = false;
            }
        };
        rightButton.setRect(WIDTH - buttonWidth - 10, buttonY, buttonWidth, buttonHeight);
        rightButton.enable(false);
        rightButton.alpha(0.5f);
        add(rightButton);
    }

    private void updatePlayerBorders() {
        float playerY = gameAreaHeight - PLAYER_HEIGHT - 5;
        float px = gameAreaLeft + playerX;
        float py = gameAreaTop + playerY;

        playerBorderTop.x = px - 0.5f;
        playerBorderTop.y = py - 1;

        playerBorderBottom.x = px - 0.5f;
        playerBorderBottom.y = py + PLAYER_HEIGHT;

        playerBorderLeft.x = px - 1;
        playerBorderLeft.y = py - 0.5f;

        playerBorderRight.x = px + PLAYER_WIDTH;
        playerBorderRight.y = py - 0.5f;
    }

    private void spawnKnife() {
        // 랜덤 위치에서 나이프 생성
        float spawnX = Random.Float(10, gameAreaWidth - 10);
        float velocityX = Random.Float(-20, 20); // 약간의 X방향 이동

        // 가끔 플레이어 방향으로 조준된 나이프
        if (Random.Int(3) == 0) {
            float targetX = playerX + PLAYER_WIDTH / 2;
            velocityX = (targetX - spawnX) * 0.3f;
        }

        Knife knife = new Knife(spawnX, -15, knifeSpeed, velocityX);
        knives.add(knife);

        Sample.INSTANCE.play(Assets.Sounds.MISS, 0.5f, Random.Float(1.5f, 2.0f));
    }

    private void checkCollisions() {
        // 무적 시간 중이면 충돌 체크 안 함
        if (invincibilityTimer > 0) {
            return;
        }

        float playerY = gameAreaHeight - PLAYER_HEIGHT - 5;

        for (Knife knife : knives) {
            if (knife.active && knife.checkCollision(playerX, playerY, PLAYER_WIDTH, PLAYER_HEIGHT)) {
                knife.active = false;

                // 실제 플레이어에게 피해 (최대 체력의 15%)
                int hpCost = Math.max(1, (int)(Dungeon.hero.HT * 0.15f));
                Dungeon.hero.damage(hpCost, this);
                if (!Dungeon.hero.isAlive()) {
                    Dungeon.fail(getClass());
                }

                // 피격 횟수 증가
                hitCount++;

                // 피격 이펙트
                Sample.INSTANCE.play(Assets.Sounds.HIT_STRONG);
                Camera.main.shake(5, 0.3f);
                GameScene.flash(0x80FF0000);

                // 무적 시간 설정
                invincibilityTimer = INVINCIBILITY_DURATION;

                // 플레이어가 죽으면 게임 실패
                if (!Dungeon.hero.isAlive()) {
                    player.color(0xFFFF0000);
                    onGameFail();
                } else {
                    // 아직 살아있으면 경고 색상으로 깜빡임
                    player.color(0xFFFF8844);
                }

                break;
            }
        }
    }

    private void cleanupKnives() {
        Iterator<Knife> it = knives.iterator();
        while (it.hasNext()) {
            Knife knife = it.next();
            if (!knife.active) {
                knife.destroy();
                it.remove();
            }
        }
    }

    private void onGameSuccess() {
        state = GameState.SUCCESS;

        // 버튼 비활성화
        leftButton.enable(false);
        rightButton.enable(false);
        movingLeft = false;
        movingRight = false;

        // 결과 표시
        timerText.text(Messages.get(this, "success"));
        timerText.hardlight(0x44FF44);
        timerText.setPos((WIDTH - timerText.width()) / 2, 16);

        Sample.INSTANCE.play(Assets.Sounds.HIT_PARRY);
        Camera.main.shake(3, 0.3f);

        // 플레이어 색상 변경 (성공 표시)
        player.color(0xFF44FF44);
        playerBorderTop.color(0xFF88FF88);
        playerBorderBottom.color(0xFF88FF88);
        playerBorderLeft.color(0xFF88FF88);
        playerBorderRight.color(0xFF88FF88);

        // 남은 나이프 제거
        for (Knife knife : knives) {
            knife.destroy();
        }
        knives.clear();

        // 성공 콜백 호출
        if (onSuccess != null) {
            onSuccess.call();
            onSuccess = null;
        }

        showCloseButton();
    }

    private void onGameFail() {
        state = GameState.FAIL;

        // 버튼 비활성화
        leftButton.enable(false);
        rightButton.enable(false);
        movingLeft = false;
        movingRight = false;

        // 결과 표시
        timerText.text(Messages.get(this, "fail"));
        timerText.hardlight(0xFFFF4444);
        timerText.setPos((WIDTH - timerText.width()) / 2, 16);

        // 남은 나이프 제거
        for (Knife knife : knives) {
            knife.destroy();
        }
        knives.clear();

        // 실패 콜백 호출
        if (onFail != null) {
            onFail.call();
            onFail = null;
        }

        showCloseButton();
    }

    private void showCloseButton() {
        // 기존 버튼 숨기기
        leftButton.visible = false;
        leftButton.active = false;
        rightButton.visible = false;
        rightButton.active = false;

        // 닫기 버튼 표시
        float buttonY = gameAreaTop + gameAreaHeight + 8;
        closeButton = new RedButton(Messages.get(this, "close")) {
            @Override
            protected void onClick() {
                hide();
            }
        };
        closeButton.setRect(20, buttonY, WIDTH - 40, 28);
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
                leftButton.enable(true);
                leftButton.alpha(1f);
                rightButton.enable(true);
                rightButton.alpha(1f);
                Sample.INSTANCE.play(Assets.Sounds.CHARGEUP);
            }
        }
        else if (state == GameState.PLAYING) {
            gameTimer += elapsed;

            // 무적 시간 감소
            if (invincibilityTimer > 0) {
                invincibilityTimer -= elapsed;

                // 무적 시간 동안 플레이어 깜빡임 효과
                float blinkInterval = 0.1f;
                int blinkCycle = (int)(invincibilityTimer / blinkInterval);
                if (blinkCycle % 2 == 0) {
                    player.alpha(0.3f);
                    playerBorderTop.alpha(0.3f);
                    playerBorderBottom.alpha(0.3f);
                    playerBorderLeft.alpha(0.3f);
                    playerBorderRight.alpha(0.3f);
                } else {
                    player.alpha(1.0f);
                    playerBorderTop.alpha(1.0f);
                    playerBorderBottom.alpha(1.0f);
                    playerBorderLeft.alpha(1.0f);
                    playerBorderRight.alpha(1.0f);
                }

                // 무적 시간 종료 시 색상 복구
                if (invincibilityTimer <= 0) {
                    player.color(0xFF00CCCC);
                    player.alpha(1.0f);
                    playerBorderTop.alpha(1.0f);
                    playerBorderBottom.alpha(1.0f);
                    playerBorderLeft.alpha(1.0f);
                    playerBorderRight.alpha(1.0f);
                }
            }

            // 타이머 표시 업데이트
            float remaining = GAME_DURATION - gameTimer;
            if (remaining < 0) remaining = 0;
            timerText.text(String.format("%.1f", remaining));
            if (remaining <= 1.5f) {
                timerText.hardlight(0xFF44FF44); // 거의 끝나갈 때 초록색
            } else if (remaining <= 3f) {
                timerText.hardlight(0xFFFFAA44); // 중간쯤에 주황색
            } else {
                timerText.hardlight(0xFFFFFFFF);
            }
            timerText.setPos((WIDTH - timerText.width()) / 2, 16);

            // 플레이어 이동
            if (movingLeft) {
                playerX -= playerSpeed * elapsed;
                if (playerX < 0) playerX = 0;
            }
            if (movingRight) {
                playerX += playerSpeed * elapsed;
                if (playerX > gameAreaWidth - PLAYER_WIDTH) {
                    playerX = gameAreaWidth - PLAYER_WIDTH;
                }
            }

            // 플레이어 위치 업데이트
            float playerY = gameAreaHeight - PLAYER_HEIGHT - 5;
            player.x = gameAreaLeft + playerX;
            player.y = gameAreaTop + playerY;
            updatePlayerBorders();

            // 나이프 스폰
            knifeSpawnTimer += elapsed;

            // 시간이 지날수록 스폰 간격 감소 (더 어려워짐)
            float currentInterval = knifeSpawnInterval * (1f - gameTimer / GAME_DURATION * 0.3f);

            if (knifeSpawnTimer >= currentInterval) {
                spawnKnife();
                knifeSpawnTimer = 0;

                // 후반부에는 가끔 2개씩 스폰
                if (gameTimer > GAME_DURATION * 0.6f && Random.Int(3) == 0) {
                    spawnKnife();
                }
            }

            // 나이프 업데이트
            for (Knife knife : knives) {
                if (knife.active) {
                    knife.update(elapsed);
                }
            }

            // 충돌 검사
            checkCollisions();

            // 비활성화된 나이프 정리
            cleanupKnives();

            // 게임 종료 체크 (시간 다 됐고 살아있고 한 대도 안 맞았으면 성공)
            if (gameTimer >= GAME_DURATION && Dungeon.hero.isAlive()) {
                if (hitCount == 0) {
                    onGameSuccess();
                } else {
                    onGameFail();
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        // 게임 중에는 뒤로가기 비활성화
        if (state == GameState.SUCCESS || state == GameState.FAIL) {
            super.onBackPressed();
        }
    }

    @Override
    public void destroy() {
        if (instance == this) {
            instance = null;
        }

        // 모든 나이프 정리
        for (Knife knife : knives) {
            knife.destroy();
        }
        knives.clear();

        super.destroy();
    }
}
