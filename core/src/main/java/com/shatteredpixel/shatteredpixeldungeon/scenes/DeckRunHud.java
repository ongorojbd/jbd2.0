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
import com.shatteredpixel.shatteredpixeldungeon.deckbuilder.DeckRelic;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.Button;
import com.shatteredpixel.shatteredpixeldungeon.ui.IconButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndMessage;
import com.watabou.noosa.Camera;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Game;
import com.watabou.noosa.ui.Component;

import java.util.ArrayList;

public class DeckRunHud extends Component {

	public interface PotionHandler {
		void onPotion(int slot, DeckPotion potion);
		void onDiscardPotion(int slot, DeckPotion potion);
		boolean canUsePotion();
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
				showRelicWindow();
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

	public void givePotionPointerPriority() {
		if (potionButtons != null) {
			for (PotionSlotButton potionButton : potionButtons) {
				potionButton.givePointerPriority();
			}
		}
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

	private void showRelicWindow() {
		final Window win = new Window();
		int width = 180;
		int padding = 5;
		int pos = 7;

		RenderedTextBlock titleBlock = PixelScene.renderTextBlock("유물 목록", 9);
		titleBlock.hardlight(Window.TITLE_COLOR);
		titleBlock.setPos((width - titleBlock.width()) / 2f, pos);
		win.add(titleBlock);
		pos += 18;

		Component content = new Component();
		float contentPos = 0;

		if (DeckBuilderRun.relics.isEmpty()) {
			RenderedTextBlock empty = PixelScene.renderTextBlock("획득한 유물이 없습니다.", 6);
			empty.hardlight(0xFF888888);
			empty.setPos((width - 10 - empty.width()) / 2f, contentPos);
			content.add(empty);
			contentPos += empty.height() + padding;
		} else {
			for (int id : DeckBuilderRun.relics) {
				DeckRelic relic = DeckRelic.byId(id);

				RenderedTextBlock nameTxt = PixelScene.renderTextBlock(relic.title, 7);
				nameTxt.hardlight(Window.TITLE_COLOR);
				nameTxt.maxWidth(width - padding * 2);
				nameTxt.setPos(padding, contentPos);
				content.add(nameTxt);
				contentPos += nameTxt.height() + 2;

				RenderedTextBlock descTxt = PixelScene.renderTextBlock(relic.description, 6);
				descTxt.hardlight(0xFFD8D1BD);
				descTxt.maxWidth(width - padding * 2);
				descTxt.setPos(padding, contentPos);
				content.add(descTxt);
				contentPos += descTxt.height() + 8;
			}
		}
		content.setSize(width - 2, contentPos);

		int maxScrollH = Math.min(150, Camera.main.height - 80);
		int scrollH = (int) Math.min(maxScrollH, contentPos);
		int scrollTop = pos;
		pos += scrollH + 4;

		RedButton close = new RedButton("닫기", 6) {
			@Override
			protected void onClick() {
				win.hide();
			}
		};
		close.setRect((width - 100) / 2f, pos, 100, 16);
		win.add(close);
		pos += 22;

		ScrollPane scrollPane = new ScrollPane(content);
		win.add(scrollPane);
		win.resize(width, pos);           // resize 먼저 → 윈도우 카메라 위치 확정
		scrollPane.setRect(0, scrollTop, width, scrollH);  // 그 다음 setRect → 올바른 카메라 위치 사용
		Game.scene().addToFront(win);
	}

	private void showPotionWindow(final int slot, final DeckPotion potion) {
		final Window win = new Window();
		int width = 150;
		int pos = 7;

		RenderedTextBlock title = PixelScene.renderTextBlock(potion.title + "(" + potion.rarity.label + ")", 8);
		title.hardlight(Window.TITLE_COLOR);
		title.maxWidth(width - 14);
		title.setPos((width - title.width()) / 2f, pos);
		win.add(title);
		pos += (int)title.height() + 8;

		RenderedTextBlock desc = PixelScene.renderTextBlock(potion.description, 6);
		desc.maxWidth(width - 14);
		desc.hardlight(0xFFD8D1BD);
		desc.setPos(7, pos);
		win.add(desc);
		pos += (int)desc.height() + 8;

		if (potionHandler != null) {
			if (potionHandler.canUsePotion()) {
				RedButton use = new RedButton("사용", 6) {
					@Override
					protected void onClick() {
						win.hide();
						potionHandler.onPotion(slot, potion);
					}
				};
				use.setRect(7, pos, 62, 16);
				win.add(use);

				RedButton discard = new RedButton("버리기", 6) {
					@Override
					protected void onClick() {
						win.hide();
						potionHandler.onDiscardPotion(slot, potion);
					}
				};
				discard.setRect(width - 69, pos, 62, 16);
				win.add(discard);
			} else {
				RedButton discard = new RedButton("버리기", 6) {
					@Override
					protected void onClick() {
						win.hide();
						potionHandler.onDiscardPotion(slot, potion);
					}
				};
				discard.setRect((width - 80) / 2f, pos, 80, 16);
				win.add(discard);
			}
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
