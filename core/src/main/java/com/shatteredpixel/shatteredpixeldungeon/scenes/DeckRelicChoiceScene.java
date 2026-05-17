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

package com.shatteredpixel.shatteredpixeldungeon.scenes;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.deckbuilder.DeckBuilderMap;
import com.shatteredpixel.shatteredpixeldungeon.deckbuilder.DeckBuilderRun;
import com.shatteredpixel.shatteredpixeldungeon.deckbuilder.DeckRelic;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DArbySprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.Button;
import com.shatteredpixel.shatteredpixeldungeon.ui.IconButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndMessage;
import com.watabou.noosa.Camera;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.RectF;

import java.io.IOException;

public class DeckRelicChoiceScene extends PixelScene {

	@Override
	public void create() {
		inGameScene = true;
		super.create();

		DeckBuilderRun.initIfNeeded();
		DeckRelic[] choices = DeckBuilderRun.startingRelicChoices();
		saveRun();
		if (Dungeon.level != null) {
			Dungeon.level.playLevelMusic();
		}
		Sample.INSTANCE.play(Assets.Sounds.DA3);

		int w = Camera.main.width;
		int h = Camera.main.height;
		RectF insets = getCommonInsets();
		float left = insets.left + 8;
		float right = w - insets.right - 8;
		float width = right - left;

		add(new ColorBlock(w, h, 0xFF10140F));
		addRunHud(insets);
		addExitButton(insets, w);

		RenderedTextBlock title = renderTextBlock("다니엘 J. 다비", 11);
		title.hardlight(Window.TITLE_COLOR);
		title.setPos(insets.left + (w - insets.left - insets.right - title.width()) / 2f, insets.top + 10);
		align(title);
		add(title);

		boolean wide = width >= h * 1.15f;
		DArbySprite darby = new DArbySprite();
		darby.scale.set(wide ? Math.max(1.9f, Math.min(2.5f, h / 95f)) : 3.0f);
		darby.x = insets.left + (w - insets.left - insets.right - darby.width()) / 2f;
		darby.y = title.bottom() + (wide ? 8 : 14);
		align(darby);
		add(darby);

		RenderedTextBlock speech = renderTextBlock("제 이름은 다비, D'.A.R.B.Y.\n\n" + "당신의 직감을 믿고 한 장 뽑아보시겠습니까?\n선택한 능력이 앞으로의 여정에 큰 힘이 되어줄 겁니다.\n\n그럼, 게임을 시작해 볼까요?", 7);
		speech.hardlight(0xFFD8D1BD);
		speech.maxWidth((int)Math.min(220, width));
		speech.setPos(insets.left + (w - insets.left - insets.right - speech.width()) / 2f, darby.y + darby.height() + (wide ? 6 : 12));
		align(speech);
		add(speech);

		layoutRelicChoices(choices, insets, w, h, width, speech.bottom(), wide);

		fadeIn();
	}

	private void addRelicButton(RectF insets) {
		IconButton relicButton = new IconButton(Icons.BACKPACK_LRG.get()) {
			@Override
			protected void onClick() {
				addToFront(new WndMessage("유물\n\n" + DeckBuilderRun.relicListText()));
			}
		};
		relicButton.setRect(insets.left + 4, insets.top + 4, 20, 20);
		add(relicButton);
	}

	private void addRunHud(RectF insets) {
		DeckRunHud hud = new DeckRunHud(null);
		hud.setRect(insets.left + 4, insets.top + 4, 150, 20);
		add(hud);
	}

	private void addExitButton(RectF insets, int w) {
		IconButton exit = new IconButton(Icons.EXIT.get()) {
			@Override
			protected void onClick() {
				saveRun();
				Game.switchScene(TitleScene.class);
			}
		};
		exit.setRect(w - insets.right - 24, insets.top + 4, 20, 20);
		add(exit);
	}

	private void choose(DeckRelic relic) {
		if (DeckBuilderMapScene.curTransition == null && Dungeon.level != null) {
			DeckBuilderMapScene.curTransition = Dungeon.level.getTransition(LevelTransition.Type.REGULAR_EXIT);
		}
		Sample.INSTANCE.play(Assets.Sounds.DA2);
		DeckBuilderRun.chooseStartingRelic(relic);
		DeckBuilderMap.ensureReadyForNextChoice();
		saveRun();
		Game.switchScene(DeckBuilderMapScene.class);
	}

	private void layoutRelicChoices(DeckRelic[] choices, RectF insets, int w, int h, float width, float speechBottom, boolean wide) {
		float gap = wide ? 5 : 6;
		if (wide && choices.length > 0) {
			float buttonW = Math.min(118, (width - gap * (choices.length - 1)) / choices.length);
			float buttonH = Math.max(34, Math.min(44, h - speechBottom - insets.bottom - 8));
			float totalW = choices.length * buttonW + (choices.length - 1) * gap;
			float x = insets.left + (w - insets.left - insets.right - totalW) / 2f;
			float y = h - insets.bottom - buttonH - 7;
			for (int i = 0; i < choices.length; i++) {
				RelicChoiceButton button = new RelicChoiceButton(choices[i]);
				button.setRect(x + i * (buttonW + gap), y, buttonW, buttonH);
				add(button);
			}
		} else {
			float buttonW = Math.min(220, width);
			float buttonH = Math.max(30, Math.min(36, (h - speechBottom - insets.bottom - 18) / Math.max(1, choices.length)));
			float y = speechBottom + 8;
			for (int i = 0; i < choices.length; i++) {
				RelicChoiceButton button = new RelicChoiceButton(choices[i]);
				button.setRect(insets.left + (w - insets.left - insets.right - buttonW) / 2f, y + i * (buttonH + gap), buttonW, buttonH);
				add(button);
			}
		}
	}

	private void saveRun() {
		try {
			Dungeon.saveAll();
		} catch (IOException e) {
			Game.reportException(e);
		}
	}

	@Override
	protected void onBackPressed() {
	}

	private class RelicChoiceButton extends Button {

		private final DeckRelic relic;
		private ColorBlock shadow;
		private ColorBlock bg;
		private ColorBlock accent;
		private RenderedTextBlock title;
		private RenderedTextBlock desc;

		private RelicChoiceButton(DeckRelic relic) {
			this.relic = relic;
		}

		@Override
		protected void createChildren() {
			super.createChildren();
			shadow = new ColorBlock(1, 1, 0xFF000000);
			shadow.am = 0.38f;
			add(shadow);
			bg = new ColorBlock(1, 1, 0xFF222821);
			bg.am = 0.96f;
			add(bg);
			accent = new ColorBlock(1, 1, 0xFFD5F27A);
			add(accent);
			title = renderTextBlock(6);
			title.hardlight(Window.TITLE_COLOR);
			add(title);
			desc = renderTextBlock(4);
			desc.hardlight(0xFFD8D1BD);
			add(desc);
		}

		@Override
		protected void layout() {
			super.layout();
			shadow.x = x + 2;
			shadow.y = y + 2;
			shadow.size(width, height);
			bg.x = x;
			bg.y = y;
			bg.size(width, height);
			accent.x = x;
			accent.y = y;
			accent.size(3, height);
			title.text(relic.title);
			title.maxWidth((int)(width - 14));
			title.setPos(x + 8, y + 5);
			desc.text(relic.description);
			desc.maxWidth((int)(width - 16));
			desc.setPos(x + 8, Math.min(title.bottom() + 2, y + height - desc.height() - 4));
		}

		@Override
		protected void onClick() {
			choose(relic);
		}
	}
}
