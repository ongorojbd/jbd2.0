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
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class WndQteBossGame extends Window {

    // 현재 열려있는 인스턴스 추적 (중복 창 방지)
    public static WndQteBossGame instance = null;

    // 팔레트
    private static final int COL_BORDER   = 0xFF3d3d63;
    private static final int COL_PANEL_BG = 0xFF14141f;
    private static final int COL_SLOT_BG  = 0xFF1c1c2c;
    private static final int COL_GOLD     = 0xFFFFD24A;
    private static final int COL_DIM      = 0xFF55556a;

    private enum Direction {
        UP    ( "상", 0xFF6FE3FF ),
        DOWN  ( "하", 0xFFFF8C5A ),
        LEFT  ( "좌", 0xFFC792FF ),
        RIGHT ( "우", 0xFF7CFF8A );

        // 픽셀/한글 폰트에 없는 ▲▼◀▶ 는 모바일에서 "?"로 깨지므로 한글 글자를 쓴다
        final String glyph;
        final int color;

        Direction( String glyph, int color ) {
            this.glyph = glyph;
            this.color = color;
        }
    }

    // 버튼은 항상 이 순서로 한 줄에 배치 (상, 하, 좌, 우)
    private static final Direction[] BUTTON_ORDER = {Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT};

    // 화면 크기 (가로/세로 모드에 맞춰 결정, 항상 최소 지원 화면보다 작게)
    private final int WIDTH;
    private final int HEIGHT;
    private final float slotSize;
    private final int arrowFontSize;
    private final float btnW;
    private final float btnH;

    // 난이도(보스 페이즈)에 따른 제한 시간 - 순간 반응을 요구할 만큼 짧게
    private final float timeLimit;

    // 연속으로 입력해야 하는 방향 수 (페이즈가 오를수록 증가)
    private final int sequenceLength;
    private int stepsDone = 0;

    // 게임 상태
    private enum GameState {
        COUNTDOWN,  // 짧고 불규칙한 대기 (예측 방지)
        SHOW,       // 방향 표시, 입력 대기
        SUCCESS,
        FAIL
    }

    private GameState state = GameState.COUNTDOWN;
    private float countdownTimer;
    private float showTimer = 0f;

    private Direction target;

    private RenderedTextBlock titleText;
    private RenderedTextBlock promptText;
    private RenderedTextBlock arrowText;

    private ColorBlock slotBorder;
    private ColorBlock slotBg;

    private ColorBlock timerBarFrame;
    private ColorBlock timerBarBack;
    private ColorBlock timerBarFill;

    private RedButton[] dirButtons = new RedButton[4];
    private RedButton closeButton;

    private Callback onSuccess;
    private Callback onFail;

    public WndQteBossGame(int phase, Callback onSuccess, Callback onFail) {
        this(phase, 1, onSuccess, onFail);
    }

    public WndQteBossGame(int phase, int sequenceLength, Callback onSuccess, Callback onFail) {
        super();

        instance = this;

        this.sequenceLength = Math.max(1, sequenceLength);
        this.onSuccess = onSuccess;
        this.onFail = onFail;

        boolean landscape = PixelScene.landscape();
        if (landscape) {
            WIDTH = 205;
            HEIGHT = 108;
            slotSize = 34;
            arrowFontSize = 18;
            btnW = 44;
            btnH = 22;
        } else {
            WIDTH = 126;
            HEIGHT = 136;
            slotSize = 42;
            arrowFontSize = 22;
            btnW = 24;
            btnH = 26;
        }

        switch (Math.max(1, Math.min(3, phase))) {
            case 3:
            case 2:
                timeLimit = 0.55f;
                break;
            default:
                timeLimit = 0.75f;
        }

        // 카운트다운 길이를 살짝 무작위로 하여 타이밍 예측(선입력)을 막는다
        countdownTimer = Random.Float(0.3f, 0.6f);

        target = Random.element(Direction.values());

        resize(WIDTH, HEIGHT);

        setupUI();
    }

    private void setupUI() {
        // 배경 패널 (테두리 + 배경)
        ColorBlock panelBorder = new ColorBlock(WIDTH - 6, HEIGHT - 22, COL_BORDER);
        panelBorder.x = 3;
        panelBorder.y = 19;
        add(panelBorder);

        ColorBlock panelBg = new ColorBlock(WIDTH - 8, HEIGHT - 24, COL_PANEL_BG);
        panelBg.x = 4;
        panelBg.y = 20;
        add(panelBg);

        titleText = PixelScene.renderTextBlock(Messages.get(this, "title"), 8);
        titleText.hardlight(COL_GOLD);
        titleText.setPos((WIDTH - titleText.width()) / 2, 2);
        add(titleText);

        promptText = PixelScene.renderTextBlock(Messages.get(this, "ready"), 7);
        promptText.hardlight(0xFFAAAAAA);
        promptText.setPos((WIDTH - promptText.width()) / 2, 11);
        add(promptText);

        // 방향 표시 슬롯 (테두리 + 배경)
        float slotX = (WIDTH - slotSize) / 2f;
        float slotY = 22;

        slotBorder = new ColorBlock(slotSize + 4, slotSize + 4, COL_DIM);
        slotBorder.x = slotX - 2;
        slotBorder.y = slotY - 2;
        add(slotBorder);

        slotBg = new ColorBlock(slotSize, slotSize, COL_SLOT_BG);
        slotBg.x = slotX;
        slotBg.y = slotY;
        add(slotBg);

        arrowText = PixelScene.renderTextBlock("?", arrowFontSize);
        arrowText.hardlight(COL_DIM);
        arrowText.setPos((WIDTH - arrowText.width()) / 2, slotY + (slotSize - arrowText.height()) / 2f);
        add(arrowText);

        float barY = slotY + slotSize + 8;
        float barMargin = 12;

        timerBarFrame = new ColorBlock(WIDTH - barMargin * 2 + 4, 9, COL_BORDER);
        timerBarFrame.x = barMargin - 2;
        timerBarFrame.y = barY - 1;
        add(timerBarFrame);

        timerBarBack = new ColorBlock(WIDTH - barMargin * 2, 7, 0xFF23233a);
        timerBarBack.x = barMargin;
        timerBarBack.y = barY;
        add(timerBarBack);

        timerBarFill = new ColorBlock(WIDTH - barMargin * 2, 7, 0xFF44FF44);
        timerBarFill.x = barMargin;
        timerBarFill.y = barY;
        add(timerBarFill);

        // 방향 버튼을 한 줄로 배치 (세로 폭을 최소화해 모바일 화면에 맞춤)
        float gap = 5;
        float totalW = 4 * btnW + 3 * gap;
        float startX = (WIDTH - totalW) / 2f;
        float btnY = barY + 15;

        for (int i = 0; i < BUTTON_ORDER.length; i++) {
            float x = startX + i * (btnW + gap);
            dirButtons[i] = makeDirButton(BUTTON_ORDER[i], x, btnY);
        }
    }

    private RedButton makeDirButton(final Direction dir, float x, float y) {
        RedButton button = new RedButton(dir.glyph, 12) {
            @Override
            protected void onClick() {
                super.onClick();
                onDirectionPressed(dir);
            }
        };
        button.setRect(x, y, btnW, btnH);
        button.textColor(dir.color);
        button.enable(false);
        button.alpha(0.5f);
        add(button);
        return button;
    }

    private void setButtonsEnabled(boolean enabled) {
        for (RedButton button : dirButtons) {
            button.enable(enabled);
            button.alpha(enabled ? 1f : 0.5f);
        }
    }

    private void onDirectionPressed(Direction pressed) {
        if (state != GameState.SHOW) return;

        if (pressed == target) {
            stepsDone++;
            if (stepsDone >= sequenceLength) {
                onGameSuccess();
            } else {
                nextInput();
            }
        } else {
            onGameFail();
        }
    }

    // 다음 방향 입력으로 진행 (직전과 다른 방향을 뽑아 반응을 강제)
    private void nextInput() {
        Direction prev = target;
        do {
            target = Random.element(Direction.values());
        } while (target == prev);

        showTimer = 0f;

        arrowText.text(target.glyph);
        arrowText.hardlight(target.color);
        arrowText.alpha(1f);
        arrowText.setPos((WIDTH - arrowText.width()) / 2, arrowText.top());
        slotBorder.color(target.color);

        promptText.text(Messages.get(this, "go") + "  " + (stepsDone + 1) + "/" + sequenceLength);
        promptText.hardlight(COL_GOLD);
        promptText.setPos((WIDTH - promptText.width()) / 2, 11);

        Sample.INSTANCE.play(Assets.Sounds.CHARGEUP);
    }

    private void onGameSuccess() {
        state = GameState.SUCCESS;
        setButtonsEnabled(false);

        promptText.text(Messages.get(this, "success"));
        promptText.hardlight(0x44FF44);
        promptText.setPos((WIDTH - promptText.width()) / 2, 11);

        arrowText.hardlight(0x44FF44);
        slotBorder.color(0xFF44FF44);

        Sample.INSTANCE.play(Assets.Sounds.HIT_PARRY);

        if (onSuccess != null) {
            onSuccess.call();
            onSuccess = null;
        }

        showCloseButton();
    }

    private void onGameFail() {
        state = GameState.FAIL;
        setButtonsEnabled(false);

        promptText.text(Messages.get(this, "fail"));
        promptText.hardlight(0xFF4444);
        promptText.setPos((WIDTH - promptText.width()) / 2, 11);

        arrowText.hardlight(0xFF4444);
        slotBorder.color(0xFFFF4444);

        if (onFail != null) {
            onFail.call();
            onFail = null;
        }

        showCloseButton();
    }

    private void showCloseButton() {
        float row = dirButtons[0].top();

        for (RedButton button : dirButtons) {
            button.visible = button.active = false;
        }

        timerBarFrame.visible = false;
        timerBarBack.visible = false;
        timerBarFill.visible = false;

        closeButton = new RedButton(Messages.get(this, "close")) {
            @Override
            protected void onClick() {
                hide();
            }
        };
        closeButton.setRect((WIDTH - (4 * btnW + 3 * 5)) / 2f, row, 4 * btnW + 3 * 5, btnH);
        add(closeButton);
    }

    @Override
    public void update() {
        super.update();

        float elapsed = Game.elapsed;

        if (state == GameState.COUNTDOWN) {
            countdownTimer -= elapsed;
            if (countdownTimer <= 0) {
                state = GameState.SHOW;
                showTimer = 0f;

                arrowText.text(target.glyph);
                arrowText.hardlight(target.color);
                arrowText.alpha(1f);
                arrowText.setPos((WIDTH - arrowText.width()) / 2, arrowText.top());
                slotBorder.color(target.color);

                promptText.text(Messages.get(this, "go") + (sequenceLength > 1 ? "  1/" + sequenceLength : ""));
                promptText.hardlight(COL_GOLD);
                promptText.setPos((WIDTH - promptText.width()) / 2, 11);

                setButtonsEnabled(true);
                Sample.INSTANCE.play(Assets.Sounds.CHARGEUP);
            }
        } else if (state == GameState.SHOW) {
            showTimer += elapsed;

            float remaining = timeLimit - showTimer;
            if (remaining < 0) remaining = 0;

            float frac = remaining / timeLimit;
            timerBarFill.size(timerBarBack.width() * frac, timerBarBack.height());

            if (frac <= 0.3f) {
                timerBarFill.color(0xFFFF4444);
                // 시간이 얼마 남지 않았음을 알리는 긴박한 점멸 효과
                float blinkCycle = (showTimer * 14f) % 1f;
                arrowText.alpha(blinkCycle < 0.5f ? 1f : 0.35f);
            } else if (frac <= 0.55f) {
                timerBarFill.color(0xFFFFAA44);
            } else {
                timerBarFill.color(0xFF44FF44);
            }

            if (remaining <= 0) {
                onGameFail();
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
        super.destroy();
    }
}
