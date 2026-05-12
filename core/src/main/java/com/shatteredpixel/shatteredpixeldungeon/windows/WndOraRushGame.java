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
 */

package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.Camera;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;

public class WndOraRushGame extends Window {

	public interface ResultCallback {
		void call(int punches, int requiredPunches);
	}

	public static WndOraRushGame instance = null;

	private static final int WIDTH = 136;
	private static final int HEIGHT = 128;
	private static final float LIMIT = 5f;
	private static final int REQUIRED_PUNCHES = 30;

	private final ResultCallback onFinish;

	private float timeLeft = LIMIT;
	private int punches = 0;
	private boolean finished = false;
	private boolean leftNext = true;

	private RenderedTextBlock timerText;
	private RenderedTextBlock countText;
	private ColorBlock progress;
	private RedButton leftButton;
	private RedButton rightButton;

	public WndOraRushGame(ResultCallback onFinish) {
		super();
		instance = this;
		this.onFinish = onFinish;
		resize(WIDTH, HEIGHT);
		setupUI();
	}

	private void setupUI() {
		RenderedTextBlock title = PixelScene.renderTextBlock(Messages.get(this, "title"), 9);
		title.hardlight(TITLE_COLOR);
		title.setPos((WIDTH - title.width()) / 2, 4);
		add(title);

		RenderedTextBlock instruction = PixelScene.renderTextBlock(Messages.get(this, "instruction", REQUIRED_PUNCHES), 6);
		instruction.maxWidth(WIDTH - 10);
		instruction.setPos((WIDTH - instruction.width()) / 2, 18);
		add(instruction);

		ColorBlock bg = new ColorBlock(WIDTH - 20, 8, 0xFF333333);
		bg.x = 10;
		bg.y = 44;
		add(bg);

		progress = new ColorBlock(0, 8, 0xFFFFCC33);
		progress.x = 10;
		progress.y = 44;
		add(progress);

		timerText = PixelScene.renderTextBlock("", 7);
		timerText.setPos(10, 56);
		add(timerText);

		countText = PixelScene.renderTextBlock("", 7);
		countText.setPos(10, 67);
		add(countText);

		leftButton = new RedButton(Messages.get(this, "left")) {
			@Override
			protected void onClick() {
				punch(true);
			}
		};
		leftButton.setRect(10, 84, 55, 45);
		add(leftButton);

		rightButton = new RedButton(Messages.get(this, "right")) {
			@Override
			protected void onClick() {
				punch(false);
			}
		};
		rightButton.setRect(WIDTH - 65, 84, 55, 45);
		add(rightButton);

		updateText();
	}

	private void punch(boolean left) {
		if (finished || left != leftNext) return;

		punches++;
		leftNext = !leftNext;
		Sample.INSTANCE.play(Assets.Sounds.HIT, 0.8f, 1.1f + punches * 0.01f);
		Camera.main.shake(1, 0.06f);
		updateText();

		if (punches >= REQUIRED_PUNCHES) {
			finish();
		}
	}

	private void updateText() {
		timerText.text(Messages.get(this, "timer", Math.max(0, (int)Math.ceil(timeLeft))));
		countText.text(Messages.get(this, "count", punches, REQUIRED_PUNCHES));
		progress.size((WIDTH - 20) * Math.min(1f, punches / (float)REQUIRED_PUNCHES), 8);
		leftButton.enable(!finished && leftNext);
		rightButton.enable(!finished && !leftNext);
	}

	private void finish() {
		if (finished) return;
		finished = true;
		updateText();
		if (onFinish != null) onFinish.call(punches, REQUIRED_PUNCHES);
		hide();
	}

	@Override
	public void update() {
		super.update();

		if (!finished) {
			timeLeft -= Game.elapsed;
			if (timeLeft <= 0) {
				timeLeft = 0;
				updateText();
				finish();
			} else {
				updateText();
			}
		}
	}

	@Override
	public void onBackPressed() {
	}

	@Override
	public void destroy() {
		if (instance == this) {
			instance = null;
		}
		super.destroy();
	}
}
