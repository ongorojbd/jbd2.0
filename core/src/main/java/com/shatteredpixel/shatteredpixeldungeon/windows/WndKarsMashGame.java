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
 */

package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
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

public class WndKarsMashGame extends Window {

    public static WndKarsMashGame instance = null;

    private static final int WIDTH = 130;
    private static final int HEIGHT = 118;
    private static final float LIMIT = 5f;
    private static final int REQUIRED_TAPS = 30;

    private final Callback onSuccess;
    private final Callback onFail;

    private float timeLeft = LIMIT;
    private int taps = 0;
    private boolean finished = false;

    private RenderedTextBlock timerText;
    private RenderedTextBlock countText;
    private RenderedTextBlock resultText;
    private ColorBlock progress;
    private RedButton mashButton;
    private RedButton closeButton;

    public WndKarsMashGame(Callback onSuccess, Callback onFail) {
        super();

        instance = this;
        this.onSuccess = onSuccess;
        this.onFail = onFail;

        resize(WIDTH, HEIGHT);
        setupUI();
    }

    private void setupUI() {
        RenderedTextBlock title = PixelScene.renderTextBlock(Messages.get(this, "title"), 9);
        title.hardlight(TITLE_COLOR);
        title.setPos((WIDTH - title.width()) / 2, 4);
        add(title);

        RenderedTextBlock instruction = PixelScene.renderTextBlock(Messages.get(this, "instruction", REQUIRED_TAPS), 6);
        instruction.maxWidth(WIDTH - 10);
        instruction.setPos((WIDTH - instruction.width()) / 2, 18);
        add(instruction);

        ColorBlock bg = new ColorBlock(WIDTH - 20, 8, 0xFF333333);
        bg.x = 10;
        bg.y = 44;
        add(bg);

        progress = new ColorBlock(0, 8, 0xFFFF00FF);
        progress.x = 10;
        progress.y = 44;
        add(progress);

        timerText = PixelScene.renderTextBlock("", 7);
        timerText.setPos(10, 56);
        add(timerText);

        countText = PixelScene.renderTextBlock("", 7);
        countText.setPos(10, 67);
        add(countText);

        mashButton = new RedButton(Messages.get(this, "mash")) {
            @Override
            protected void onClick() {
                if (finished) return;
                taps++;
                Sample.INSTANCE.play(Assets.Sounds.HIT, 0.8f, 1.2f + taps * 0.01f);
                Camera.main.shake(1, 0.08f);
                updateText();
                if (taps >= REQUIRED_TAPS) {
                    finish(true);
                }
            }
        };
        mashButton.setRect(10, 82, WIDTH - 20, 22);
        add(mashButton);

        updateText();
    }

    private void updateText() {
        timerText.text(Messages.get(this, "timer", Math.max(0, (int)Math.ceil(timeLeft))));
        countText.text(Messages.get(this, "count", taps, REQUIRED_TAPS));
        progress.size((WIDTH - 20) * Math.min(1f, taps / (float)REQUIRED_TAPS), 8);
    }

    private void finish(boolean success) {
        if (finished) return;

        finished = true;
        mashButton.enable(false);
        mashButton.visible = false;
        mashButton.active = false;

        if (success) {
            Sample.INSTANCE.play(Assets.Sounds.HIT_PARRY);
            if (onSuccess != null) onSuccess.call();
            hide();
            return;
        } else {
            GameScene.flash(0xFF00FF);
            Camera.main.shake(7, 0.7f);
            Sample.INSTANCE.play(Assets.Sounds.BLAST);
            resultText = PixelScene.renderTextBlock(Messages.get(this, "fail"), 7);
            resultText.hardlight(0xFF4444);
            if (onFail != null) onFail.call();
        }

        resultText.maxWidth(WIDTH - 10);
        resultText.setPos((WIDTH - resultText.width()) / 2, 80);
        add(resultText);

        closeButton = new RedButton(Messages.get(this, "close")) {
            @Override
            protected void onClick() {
                hide();
            }
        };
        closeButton.setRect(10, 100, WIDTH - 20, 16);
        add(closeButton);
    }

    @Override
    public void update() {
        super.update();

        if (!finished) {
            timeLeft -= Game.elapsed;
            if (timeLeft <= 0) {
                timeLeft = 0;
                updateText();
                finish(false);
            } else {
                updateText();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (finished) {
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
