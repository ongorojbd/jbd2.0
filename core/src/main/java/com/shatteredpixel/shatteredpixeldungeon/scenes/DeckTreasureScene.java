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
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.deckbuilder.DeckBuilderMap;
import com.shatteredpixel.shatteredpixeldungeon.deckbuilder.DeckBuilderRun;
import com.shatteredpixel.shatteredpixeldungeon.deckbuilder.DeckRelic;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
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

public class DeckTreasureScene extends PixelScene {

	private DeckRelic relic;
	private int chest;

	@Override
	public void create() {
		inGameScene = true;
		super.create();
		DeckBuilderRun.initIfNeeded();
		relic = DeckBuilderRun.treasureRelicForCurrentNode();
		chest = DeckBuilderRun.treasureChestForCurrentNode();
		saveRun();

		int w = Camera.main.width;
		int h = Camera.main.height;
		RectF insets = getCommonInsets();

		add(new ColorBlock(w, h, 0xFF10140F));
		ColorBlock veil = new ColorBlock(w, h, 0xFF000000);
		veil.am = 0.18f;
		add(veil);
		addRunHud(insets);
		addExitButton(insets, w);

		RenderedTextBlock title = renderTextBlock(chestName(), 12);
		title.hardlight(Window.TITLE_COLOR);
		title.setPos((w - title.width()) / 2f, insets.top + 12);
		add(title);

		RenderedTextBlock text = renderTextBlock("상자가 놓여 있다.\n상자를 열어 유물을 획득한다.", 7);
		text.hardlight(0xFFD8D1BD);
		text.maxWidth(Math.min(220, w - (int)insets.left - (int)insets.right - 24));

		RelicTakeButton take = new RelicTakeButton();
		float buttonW = Math.min(230, w - insets.left - insets.right - 28);

		float buttonH = 44;
		float buttonY = h - insets.bottom - buttonH - 12;
		float minTextButtonGap = 10;
		float textGap = 18;
		float chestY = title.bottom() + 20;
		float chestScale = chest == 2 ? 3.3f : chest == 1 ? 3.6f : 3.1f;
		float maxTextY = buttonY - minTextButtonGap - text.height();
		float maxChestH = maxTextY - textGap - chestY;
		if (maxChestH < 20) {
			chestY = title.bottom() + 8;
			textGap = 8;
			maxChestH = maxTextY - textGap - chestY;
		}
		if (maxChestH > 0) {
			chestScale = Math.min(chestScale, Math.max(1.5f, chestScale * maxChestH / (16f * chestScale)));
		}

		ItemSprite chestSprite = new ItemSprite(chestIcon());
		chestSprite.scale.set(chestScale);
		chestSprite.x = (w - chestSprite.width()) / 2f;
		chestSprite.y = chestY;
		align(chestSprite);
		add(chestSprite);

		float textY = Math.min(chestSprite.y + chestSprite.height() + textGap, maxTextY);
		text.setPos((w - text.width()) / 2f, textY);
		add(text);

		if (buttonY < text.bottom() + minTextButtonGap) {
			buttonY = text.bottom() + minTextButtonGap;
		}
		buttonY = Math.min(buttonY, h - insets.bottom - buttonH - 4);
		take.setRect((w - buttonW) / 2f, buttonY, buttonW, buttonH);
		add(take);

		fadeIn();
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

	private int chestIcon() {
		if (chest == 2) return ItemSpriteSheet.CRYSTAL_CHEST;
		if (chest == 1) return ItemSpriteSheet.LOCKED_CHEST;
		return ItemSpriteSheet.CHEST;
	}

	private String chestName() {
		if (chest == 2) return "SPW 재단의 청색 투하물";
		if (chest == 1) return "SPW 재단의 금색 투하물";
		return "SPW 재단의 투하물";
	}

	private void takeRelic() {
		if (relic == null) {
			saveRun();
			leaveTreasure();
			return;
		}
		if (DeckBuilderRun.claimTreasureRelic()) {
			Sample.INSTANCE.play(Assets.Sounds.ITEM);
			saveRun();
		}
		leaveTreasure();
	}

	private void leaveTreasure() {
		DeckBuilderRun.clearTreasure();
		Statistics.deckBuilderMapNode = DeckBuilderMap.NONE;
		LevelTransition transition = Dungeon.level == null ? null : Dungeon.level.getTransition(LevelTransition.Type.REGULAR_EXIT);
		if (transition == null && Dungeon.level != null) {
			transition = new LevelTransition(Dungeon.level, Dungeon.hero.pos, LevelTransition.Type.REGULAR_EXIT);
		}
		DeckBuilderMapScene.curTransition = transition;
		saveRun();
		Game.switchScene(DeckBuilderMapScene.class);
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

	private class RelicTakeButton extends Button {
		private ColorBlock shadow;
		private ColorBlock bg;
		private ColorBlock accent;
		private ItemSprite icon;
		private RenderedTextBlock title;
		private RenderedTextBlock desc;

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
			icon = new ItemSprite();
			add(icon);
			title = renderTextBlock(7);
			title.hardlight(Window.TITLE_COLOR);
			add(title);
			desc = renderTextBlock(5);
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
			icon.view(relic == null ? ItemSpriteSheet.SOMETHING : relic.icon, null);
			icon.x = x + 10;
			icon.y = y + 8;
			title.text(relic == null ? "비어 있음" : relic.titleWithRarity());
			title.maxWidth((int)(width - 40));
			title.setPos(x + 31, y + 7);
			desc.text(relic == null ? "획득할 유물이 없습니다." : relic.description);
			desc.maxWidth((int)(width - 20));
			desc.setPos(x + 10, Math.max(y + 27, title.bottom() + 2));
		}

		@Override
		protected void onClick() {
			takeRelic();
		}
	}
}

