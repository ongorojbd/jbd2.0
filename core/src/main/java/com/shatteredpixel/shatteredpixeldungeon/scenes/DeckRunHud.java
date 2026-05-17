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

import com.shatteredpixel.shatteredpixeldungeon.deckbuilder.DeckBuilderRun;
import com.shatteredpixel.shatteredpixeldungeon.deckbuilder.DeckPotion;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.Button;
import com.shatteredpixel.shatteredpixeldungeon.ui.IconButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndMessage;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Game;
import com.watabou.noosa.ui.Component;

public class DeckRunHud extends Component {

	public interface PotionHandler {
		void onPotion(int slot, DeckPotion potion);
	}

	private final PotionHandler potionHandler;
	private IconButton relicButton;
	private ItemSprite goldIcon;
	private RenderedTextBlock goldText;
	private PotionSlotButton[] potionButtons;

	public DeckRunHud(PotionHandler potionHandler) {
		this.potionHandler = potionHandler;
	}

	@Override
	protected void createChildren() {
		relicButton = new IconButton(Icons.BACKPACK_LRG.get()) {
			@Override
			protected void onClick() {
				Game.scene().addToFront(new WndMessage("유물\n\n" + DeckBuilderRun.relicListText()));
			}
		};
		add(relicButton);

		goldIcon = new ItemSprite(ItemSpriteSheet.GOLD);
		add(goldIcon);

		goldText = PixelScene.renderTextBlock(DeckBuilderRun.gold + "", 7);
		goldText.hardlight(Window.TITLE_COLOR);
		add(goldText);

		potionButtons = new PotionSlotButton[DeckBuilderRun.MAX_POTION_SLOTS];
		for (int i = 0; i < potionButtons.length; i++) {
			potionButtons[i] = new PotionSlotButton(i);
			add(potionButtons[i]);
		}
	}

	@Override
	protected void layout() {
		super.layout();
		float pos = x;
		relicButton.setRect(pos, y, 20, 20);
		pos += 23;

		goldIcon.x = pos;
		goldIcon.y = y + 2;
		pos += 15;

		goldText.text(DeckBuilderRun.gold + "");
		goldText.setPos(pos, y + (20 - goldText.height()) / 2f);
		PixelScene.align(goldText);
		pos += Math.max(20, goldText.width() + 6);

		for (PotionSlotButton potionButton : potionButtons) {
			potionButton.setRect(pos, y, 20, 20);
			pos += 22;
		}
	}

	public void refresh() {
		if (goldText != null) goldText.text(DeckBuilderRun.gold + "");
		if (potionButtons != null) {
			for (PotionSlotButton potionButton : potionButtons) {
				potionButton.refresh();
			}
		}
		layout();
	}

	private class PotionSlotButton extends Button {

		private final int slot;
		private ColorBlock bg;
		private ItemSprite potionSprite;

		private PotionSlotButton(int slot) {
			this.slot = slot;
		}

		@Override
		protected void createChildren() {
			super.createChildren();
			bg = new ColorBlock(1, 1, 0xFF222821);
			bg.am = 0.88f;
			add(bg);
			potionSprite = new ItemSprite();
			add(potionSprite);
		}

		@Override
		protected void layout() {
			super.layout();
			bg.x = x;
			bg.y = y;
			bg.size(width, height);
			refresh();
		}

		private void refresh() {
			DeckPotion potion = DeckBuilderRun.potionAt(slot);
			potionSprite.view(potion == null ? ItemSpriteSheet.POTION_HOLDER : potion.image, null);
			potionSprite.x = x + (width - potionSprite.width()) / 2f;
			potionSprite.y = y + (height - potionSprite.height()) / 2f;
		}

		@Override
		protected void onClick() {
			DeckPotion potion = DeckBuilderRun.potionAt(slot);
			if (potion == null) {
				Game.scene().addToFront(new WndMessage("포션\n\n빈 포션 슬롯입니다."));
			} else {
				showPotionWindow(slot, potion);
			}
		}
	}

	private void showPotionWindow(final int slot, final DeckPotion potion) {
		final Window win = new Window();
		int width = 150;
		int pos = 7;

		RenderedTextBlock title = PixelScene.renderTextBlock(potion.title, 8);
		title.hardlight(Window.TITLE_COLOR);
		title.setPos((width - title.width()) / 2f, pos);
		win.add(title);
		pos += 16;

		RenderedTextBlock desc = PixelScene.renderTextBlock(potion.description, 6);
		desc.maxWidth(width - 14);
		desc.hardlight(0xFFD8D1BD);
		desc.setPos(7, pos);
		win.add(desc);
		pos += (int)desc.height() + 8;

		if (potionHandler != null) {
			RedButton use = new RedButton("사용", 6) {
				@Override
				protected void onClick() {
					win.hide();
					potionHandler.onPotion(slot, potion);
				}
			};
			use.setRect(7, pos, 62, 16);
			win.add(use);

			RedButton close = new RedButton("닫기", 6) {
				@Override
				protected void onClick() {
					win.hide();
				}
			};
			close.setRect(width - 69, pos, 62, 16);
			win.add(close);
			pos += 22;
		} else {
			RedButton close = new RedButton("닫기", 6) {
				@Override
				protected void onClick() {
					win.hide();
				}
			};
			close.setRect((width - 80) / 2f, pos, 80, 16);
			win.add(close);
			pos += 22;
		}

		win.resize(width, pos);
		Game.scene().addToFront(win);
	}
}
