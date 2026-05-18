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
import com.shatteredpixel.shatteredpixeldungeon.deckbuilder.DeckCard;
import com.shatteredpixel.shatteredpixeldungeon.deckbuilder.DeckCardRarity;
import com.shatteredpixel.shatteredpixeldungeon.deckbuilder.DeckCardType;
import com.shatteredpixel.shatteredpixeldungeon.deckbuilder.DeckPotion;
import com.shatteredpixel.shatteredpixeldungeon.deckbuilder.DeckRelic;
import com.shatteredpixel.shatteredpixeldungeon.deckbuilder.DeckShop;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.Button;
import com.shatteredpixel.shatteredpixeldungeon.ui.IconButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.TalentIcon;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndMessage;
import com.watabou.noosa.Camera;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.RectF;

import java.io.IOException;

public class DeckShopScene extends PixelScene {

	private static final int CARD_W = 42;
	private static final int CARD_H = 54;
	private static final int GAP = 5;
	private static int currentPage;
	private static int currentCardPage;
	private static int currentGoodsPage;

	private DeckShop.Offer[] offers;
	private int page;
	private RenderedTextBlock goldText;

	@Override
	public void create() {
		inGameScene = true;
		super.create();
		DeckBuilderRun.initIfNeeded();
		offers = DeckBuilderRun.shopOffersForCurrentNode();
		page = currentPage;

		int w = Camera.main.width;
		int h = Camera.main.height;
		RectF insets = getCommonInsets();
		addBackground(w, h);

		RenderedTextBlock title = renderTextBlock("상점", 13);
		title.hardlight(Window.TITLE_COLOR);
		title.setPos((w - title.width()) / 2f, insets.top + 10);
		add(title);

		goldText = renderTextBlock(DeckBuilderRun.gold + " 골드", 8);
		goldText.hardlight(0xFFFFD66B);
		goldText.setPos(insets.left + 12, title.top() + 4);
		add(goldText);

		IconButton exit = new IconButton(Icons.EXIT.get()) {
			@Override
			protected void onClick() {
				saveRun();
				Game.switchScene(TitleScene.class);
			}
		};
		exit.setRect(w - insets.right - 28, insets.top + 8, 20, 20);
		add(exit);

		layoutOffersResponsive(w, h, insets, title.bottom() + 12);
		fadeIn();
	}

	private void addBackground(int w, int h) {
		add(new ColorBlock(w, h, 0xFF101513));
		Image splash = new Image(Assets.Splashes.TENDENCY);
		float scale = Math.max(w / splash.width(), h / splash.height());
		splash.scale.set(scale);
		splash.x = (w - splash.width()) / 2f;
		splash.y = (h - splash.height()) / 2f;
		splash.am = 0.34f;
		add(splash);
		ColorBlock veil = new ColorBlock(w, h, 0xFF07100B);
		veil.am = 0.58f;
		add(veil);
	}

	private void layoutOffers(int w, int h, RectF insets, float startY) {
		float contentW = w - insets.left - insets.right - 24;
		float contentX = insets.left + 12;
		float bottom = h - insets.bottom - 34;
		float y = startY;

		if (page == 0) {
			ColorBlock cardPanel = new ColorBlock(contentW, bottom - y, 0xFF121A17);
			cardPanel.x = contentX;
			cardPanel.y = y;
			cardPanel.am = 0.72f;
			add(cardPanel);
			y += 8;

			int cardCols = Math.max(2, Math.min(4, (int)((contentW - 18 + GAP) / (CARD_W + GAP))));
			int rows = Math.max(1, (int)((bottom - y + GAP) / (CARD_H + 17)));
			int capacity = Math.max(1, cardCols * rows);
			int shown = 0;
			float cardStartX = contentX + (contentW - (cardCols * CARD_W + (cardCols - 1) * GAP)) / 2f;
			for (int i = 0; i < Math.min(7, offers.length) && shown < capacity; i++) {
				if (sold(i)) continue;
				int row = shown / cardCols;
				int col = shown % cardCols;
				ShopCardButton card = new ShopCardButton(i);
				card.setRect(cardStartX + col * (CARD_W + GAP), y + row * (CARD_H + 20), CARD_W, CARD_H + 14);
				add(card);
				shown++;
			}
			if (shown == 0) emptyText("구매할 카드가 없습니다.", contentX, y + 22, contentW);
		} else {
			ColorBlock itemPanel = new ColorBlock(contentW, Math.max(10, bottom - y - 48), 0xFF121A17);
			itemPanel.x = contentX;
			itemPanel.y = y;
			itemPanel.am = 0.72f;
			add(itemPanel);
			y += 8;
			float rowW = Math.min(240, contentW);
			float rowX = contentX + (contentW - rowW) / 2f;
			int row = 0;
			for (int i = 7; i < offers.length; i++) {
				if (sold(i)) continue;
				float yy = y + row * 27;
				if (yy + 24 > bottom - 44) break;
				ShopItemRow item = new ShopItemRow(i);
				item.setRect(rowX, yy, rowW, 23);
				add(item);
				row++;
			}
			if (row == 0) emptyText("구매할 유물과 포션이 없습니다.", contentX, y + 22, contentW);

			ShopRemoveRow remove = new ShopRemoveRow();
			float removeY = y + row * 27 + 6;
			remove.setRect(rowX, Math.min(removeY, bottom - 24), rowW, 24);
			add(remove);
		}

		addPageButtons(w, insets, bottom + 4);

		RedButton leave = new RedButton("떠난다", 7) {
			@Override
			protected void onClick() {
				leaveShop();
			}
		};
		leave.setRect((w - 110) / 2f, h - insets.bottom - 26, 110, 20);
		add(leave);
	}

	private void layoutOffersResponsive(int w, int h, RectF insets, float startY) {
		float contentW = w - insets.left - insets.right - 24;
		float contentX = insets.left + 12;
		float bottom = h - insets.bottom - 34;
		float y = startY;

		if (page == 0) {
			ColorBlock panel = new ColorBlock(contentW, bottom - y, 0xFF121A17);
			panel.x = contentX;
			panel.y = y;
			panel.am = 0.72f;
			add(panel);
			y += 8;

			int cols = Math.max(2, Math.min(7, (int)((contentW - 18 + GAP) / (CARD_W + GAP))));
			int[] indices = visibleCardOfferIndices();
			int count = indices.length;
			float startX = contentX + (contentW - (cols * CARD_W + (cols - 1) * GAP)) / 2f;
			for (int shown = 0; shown < count; shown++) {
				int i = indices[shown];
				ShopCardButton card = new ShopCardButton(i);
				card.setRect(startX + (shown % cols) * (CARD_W + GAP), y + (shown / cols) * (CARD_H + 16), CARD_W, CARD_H + 12);
				add(card);
			}
			if (count == 0) emptyText("구매할 카드가 없습니다.", contentX, y + 22, contentW);
		} else {
			ColorBlock panel = new ColorBlock(contentW, bottom - y, 0xFF121A17);
			panel.x = contentX;
			panel.y = y;
			panel.am = 0.72f;
			add(panel);
			y += 8;
			float cellGap = 7;
			float rowW = Math.min(118, (contentW - cellGap - 16) / 2f);
			float gridW = rowW * 2 + cellGap;
			float rowX = contentX + (contentW - gridW) / 2f;
			int[] indices = visibleGoodsOfferIndices();
			int count = indices.length;
			for (int i = 0; i < count; i++) {
				ShopItemRow item = new ShopItemRow(indices[i]);
				item.setRect(rowX + (i % 2) * (rowW + cellGap), y + (i / 2) * 25, rowW, 22);
				add(item);
			}
			if (count == 0) emptyText("구매할 유물과 포션이 없습니다.", contentX, y + 22, contentW);

			ShopRemoveRow remove = new ShopRemoveRow();
			remove.setRect(contentX + (contentW - Math.min(240, contentW)) / 2f, bottom - 24, Math.min(240, contentW), 24);
			add(remove);
		}

		addPageButtons(w, insets, bottom + 4);

		RedButton leave = new RedButton("떠난다", 7) {
			@Override
			protected void onClick() {
				leaveShop();
			}
		};
		leave.setRect((w - 110) / 2f, h - insets.bottom - 26, 110, 20);
		add(leave);
	}

	private void addPageButtons(int w, RectF insets, float y) {
		RedButton cards = new RedButton("카드", 6) {
			@Override
			protected void onClick() {
				if (page != 0) {
					currentPage = 0;
					currentCardPage = 0;
					Game.switchScene(DeckShopScene.class);
				}
			}
		};
		cards.enable(page != 0);
		cards.setRect(insets.left + 14, y, 64, 18);
		add(cards);

		RedButton goods = new RedButton("물품", 6) {
			@Override
			protected void onClick() {
				if (page != 1) {
					currentPage = 1;
					currentGoodsPage = 0;
					Game.switchScene(DeckShopScene.class);
				}
			}
		};
		goods.enable(page != 1);
		goods.setRect(w - insets.right - 78, y, 64, 18);
		add(goods);
	}

	private int[] visibleCardOfferIndices() {
		int count = 0;
		for (int i = 0; i < Math.min(7, offers.length); i++) {
			if (!sold(i)) count++;
		}
		int[] indices = new int[count];
		int p = 0;
		for (int i = 0; i < Math.min(7, offers.length); i++) {
			if (!sold(i)) indices[p++] = i;
		}
		return indices;
	}

	private int[] visibleGoodsOfferIndices() {
		int count = 0;
		for (int i = 7; i < offers.length; i++) {
			if (!sold(i)) count++;
		}
		int[] indices = new int[count];
		int p = 0;
		for (int i = 7; i < offers.length; i++) {
			if (!sold(i)) indices[p++] = i;
		}
		return indices;
	}

	private void addSubPageButtons(float x, float width, float y, final int subPage, final int maxPage, final boolean cards) {
		if (maxPage <= 0) return;
		RedButton prev = new RedButton("이전", 6) {
			@Override
			protected void onClick() {
				if (cards) currentCardPage = Math.max(0, subPage - 1);
				else currentGoodsPage = Math.max(0, subPage - 1);
				Game.switchScene(DeckShopScene.class);
			}
		};
		prev.enable(subPage > 0);
		prev.setRect(x + 8, y, 58, 18);
		add(prev);

		RenderedTextBlock pageText = renderTextBlock((subPage + 1) + " / " + (maxPage + 1), 6);
		pageText.hardlight(0xFFD8D1BD);
		pageText.setPos(x + (width - pageText.width()) / 2f, y + 5);
		add(pageText);

		RedButton next = new RedButton("다음", 6) {
			@Override
			protected void onClick() {
				if (cards) currentCardPage = Math.min(maxPage, subPage + 1);
				else currentGoodsPage = Math.min(maxPage, subPage + 1);
				Game.switchScene(DeckShopScene.class);
			}
		};
		next.enable(subPage < maxPage);
		next.setRect(x + width - 66, y, 58, 18);
		add(next);
	}

	private boolean sold(int index) {
		return DeckBuilderRun.shopSold != null && index >= 0 && index < DeckBuilderRun.shopSold.length && DeckBuilderRun.shopSold[index];
	}

	private void emptyText(String text, float x, float y, float width) {
		RenderedTextBlock empty = renderTextBlock(text, 7);
		empty.hardlight(0xFFAAAFA4);
		empty.maxWidth((int)width - 14);
		empty.setPos(x + (width - empty.width()) / 2f, y);
		add(empty);
	}

	private void sectionLabel(String text, float x, float y, int color) {
		RenderedTextBlock label = renderTextBlock(text, 7);
		label.hardlight(color);
		label.setPos(x, y);
		add(label);
	}

	private void showOfferConfirm(final int index) {
		if (index < 0 || index >= offers.length || DeckBuilderRun.shopSold[index]) return;
		final DeckShop.Offer offer = offers[index];
		final Window win = new Window();
		int width = 190;
		int pos = 7;

		RenderedTextBlock title = renderTextBlock(offerTitle(offer), 8);
		title.hardlight(Window.TITLE_COLOR);
		title.maxWidth(width - 14);
		title.setPos((width - title.width()) / 2f, pos);
		win.add(title);
		pos += (int)title.height() + 7;

		RenderedTextBlock desc = renderTextBlock(offerDescription(offer), 6);
		desc.maxWidth(width - 14);
		desc.hardlight(0xFFD8D1BD);
		desc.setPos(7, pos);
		win.add(desc);
		pos += (int)desc.height() + 8;

		RenderedTextBlock price = renderTextBlock(offer.price + " 골드", 7);
		price.hardlight(DeckBuilderRun.gold >= offer.price ? 0xFFFFD66B : 0xFFFF6B6B);
		price.setPos((width - price.width()) / 2f, pos);
		win.add(price);
		pos += 18;

		RedButton buy = new RedButton("구매", 6) {
			@Override
			protected void onClick() {
				if (DeckBuilderRun.buyShopOffer(index)) {
					Sample.INSTANCE.play(Assets.Sounds.GOLD);
					saveRun();
					win.hide();
					Game.switchScene(DeckShopScene.class);
				} else {
					Sample.INSTANCE.play(Assets.Sounds.CLICK);
					addToFront(new WndMessage("상점\n\n구매할 수 없습니다."));
				}
			}
		};
		buy.enable(DeckBuilderRun.gold >= offer.price);
		buy.setRect(7, pos, 82, 18);
		win.add(buy);

		RedButton close = new RedButton("닫기", 6) {
			@Override
			protected void onClick() {
				win.hide();
			}
		};
		close.setRect(width - 89, pos, 82, 18);
		win.add(close);
		pos += 24;

		win.resize(width, pos);
		addToFront(win);
	}

	private void showRemoveSelection(int page) {
		if (DeckBuilderRun.shopRemoveUsed) {
			addToFront(new WndMessage("카드 제거\n\n이 상점에서는 이미 카드 제거를 사용했습니다."));
			return;
		}
		if (DeckBuilderRun.gold < DeckShop.removePrice()) {
			addToFront(new WndMessage("카드 제거\n\n골드가 부족합니다."));
			return;
		}
		if (DeckBuilderRun.deck.isEmpty()) {
			addToFront(new WndMessage("카드 제거\n\n덱에 카드가 없습니다."));
			return;
		}
		final int cardsPerPage = 4;
		final int total = DeckBuilderRun.deck.size();
		final int maxPage = Math.max(0, (total - 1) / cardsPerPage);
		final int currentPage = Math.max(0, Math.min(page, maxPage));
		final int first = currentPage * cardsPerPage;
		final int count = Math.min(cardsPerPage, total - first);
		final Window win = new Window();
		int cols = Math.max(1, count);
		int rows = 1;
		int width = Math.max(196, cols * CARD_W + (cols - 1) * GAP + 20);
		int pos = 7;
		RenderedTextBlock title = renderTextBlock("제거할 카드 선택", 9);
		title.hardlight(Window.TITLE_COLOR);
		title.setPos((width - title.width()) / 2f, pos);
		win.add(title);
		pos += 17;
		RenderedTextBlock help = renderTextBlock("카드를 선택하면 제거 여부를 확인할 수 있습니다.", 5);
		help.hardlight(0xFFAAAFA4);
		help.maxWidth(width - 18);
		help.setPos((width - help.width()) / 2f, pos);
		win.add(help);
		pos += (int)help.height() + 8;
		int startX = (width - (cols * CARD_W + (cols - 1) * GAP)) / 2;
		for (int i = 0; i < count; i++) {
			final int deckIndex = first + i;
			CardMiniButton card = new CardMiniButton(DeckBuilderRun.deck.get(deckIndex)) {
				@Override
				protected void onClick() {
					confirmRemove(win, deckIndex);
				}
			};
			card.setRect(startX + (i % cols) * (CARD_W + GAP), pos + (i / cols) * (CARD_H + GAP), CARD_W, CARD_H);
			win.add(card);
		}
		pos += rows * CARD_H + Math.max(0, rows - 1) * GAP + 9;
		if (maxPage > 0) {
			RedButton prev = new RedButton("이전", 6) {
				@Override
				protected void onClick() {
					win.hide();
					showRemoveSelection(currentPage - 1);
				}
			};
			prev.enable(currentPage > 0);
			prev.setRect(10, pos, 58, 18);
			win.add(prev);

			RenderedTextBlock pageText = renderTextBlock((currentPage + 1) + " / " + (maxPage + 1), 6);
			pageText.hardlight(0xFFD8D1BD);
			pageText.setPos((width - pageText.width()) / 2f, pos + 5);
			win.add(pageText);

			RedButton next = new RedButton("다음", 6) {
				@Override
				protected void onClick() {
					win.hide();
					showRemoveSelection(currentPage + 1);
				}
			};
			next.enable(currentPage < maxPage);
			next.setRect(width - 68, pos, 58, 18);
			win.add(next);
			pos += 23;
		}
		RedButton close = new RedButton("취소", 6) {
			@Override
			protected void onClick() {
				win.hide();
			}
		};
		close.setRect((width - 100) / 2f, pos, 100, 18);
		win.add(close);
		pos += 24;
		win.resize(width, pos);
		addToFront(win);
	}

	private void confirmRemove(final Window cardWindow, final int deckIndex) {
		final int price = DeckShop.removePrice();
		final Window win = new Window();
		int width = 170;
		int pos = 7;
		DeckCard card = DeckCard.byCode(DeckBuilderRun.deck.get(deckIndex));
		RenderedTextBlock title = renderTextBlock(card.title(DeckBuilderRun.deck.get(deckIndex)), 8);
		title.hardlight(Window.TITLE_COLOR);
		title.setPos((width - title.width()) / 2f, pos);
		win.add(title);
		pos += 18;
		RenderedTextBlock desc = renderTextBlock("이 카드를 덱에서 제거합니다.\n비용: " + price + " 골드", 6);
		desc.maxWidth(width - 14);
		desc.setPos(7, pos);
		win.add(desc);
		pos += (int)desc.height() + 8;
		RedButton remove = new RedButton("제거", 6) {
			@Override
			protected void onClick() {
				if (DeckBuilderRun.buyCardRemoval(deckIndex)) {
					Sample.INSTANCE.play(Assets.Sounds.CURSED);
					saveRun();
					win.hide();
					cardWindow.hide();
					Game.switchScene(DeckShopScene.class);
				}
			}
		};
		remove.setRect(7, pos, 74, 18);
		win.add(remove);
		RedButton close = new RedButton("닫기", 6) {
			@Override
			protected void onClick() {
				win.hide();
			}
		};
		close.setRect(width - 81, pos, 74, 18);
		win.add(close);
		pos += 24;
		win.resize(width, pos);
		addToFront(win);
	}

	private String offerTitle(DeckShop.Offer offer) {
		if (offer.type == DeckShop.CARD) return DeckCard.values()[offer.id].title(DeckCard.values()[offer.id].code());
		if (offer.type == DeckShop.POTION) return DeckPotion.byId(offer.id).title;
		if (offer.type == DeckShop.RELIC) return DeckRelic.byId(offer.id).title;
		return "카드 제거";
	}

	private String offerDescription(DeckShop.Offer offer) {
		if (offer.type == DeckShop.CARD) {
			DeckCard card = DeckCard.values()[offer.id];
			return card.rarity.label + " " + card.type.label + "\n" + cardRulesText(card, card.code());
		}
		if (offer.type == DeckShop.POTION) return DeckPotion.byId(offer.id).description;
		if (offer.type == DeckShop.RELIC) return DeckRelic.byId(offer.id).description;
		return "카드 한 장을 덱에서 제거합니다.";
	}

	private String cardRulesText(DeckCard card, int cardCode) {
		String text = "";
		if (card.damage(cardCode) > 0) text += "피해 " + card.damage(cardCode);
		if (card.block(cardCode) > 0) text += append(text, "방어 " + card.block(cardCode));
		if (card.draw(cardCode) > 0) text += append(text, "카드 " + card.draw(cardCode) + "장 뽑기");
		if (card.vulnerable(cardCode) > 0) text += append(text, "취약 " + card.vulnerable(cardCode));
		if (card.strength(cardCode) > 0) text += append(text, "힘 " + card.strength(cardCode));
		return text.length() == 0 ? "특별한 효과가 있습니다." : text;
	}

	private String append(String text, String value) {
		return (text.length() > 0 ? " / " : "") + value;
	}

	private void leaveShop() {
		currentPage = 0;
		currentCardPage = 0;
		currentGoodsPage = 0;
		DeckBuilderRun.clearShop();
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

	private class ShopCardButton extends CardMiniButton {
		private final int index;
		private RenderedTextBlock price;
		private RenderedTextBlock saleText;

		private ShopCardButton(int index) {
			super(DeckCard.values()[offers[index].id].code());
			this.index = index;
		}

		@Override
		protected void createChildren() {
			super.createChildren();
			price = renderTextBlock(6);
			add(price);
			saleText = renderTextBlock("SALE", 5);
			saleText.hardlight(0xFFFFF27A);
			add(saleText);
		}

		@Override
		protected void layout() {
			super.layout();
			price.text(offers[index].price + "G");
			price.hardlight(DeckBuilderRun.gold >= offers[index].price ? 0xFFFFD66B : 0xFFFF7777);
			price.setPos(x + (width - price.width()) / 2f, y + height - 7);
			saleText.visible = offers[index].sale;
			saleText.setPos(x + width - saleText.width() - 3, y + 3);
		}

		@Override
		protected void onClick() {
			showOfferConfirm(index);
		}
	}

	private class ShopItemRow extends Button {
		private final int index;
		private ColorBlock bg;
		private ItemSprite icon;
		private RenderedTextBlock name;
		private RenderedTextBlock price;

		private ShopItemRow(int index) {
			this.index = index;
		}

		@Override
		protected void createChildren() {
			super.createChildren();
			bg = new ColorBlock(1, 1, 0xFF1D241F);
			add(bg);
			icon = new ItemSprite();
			add(icon);
			name = renderTextBlock(6);
			add(name);
			price = renderTextBlock(6);
			add(price);
		}

		@Override
		protected void layout() {
			super.layout();
			DeckShop.Offer offer = offers[index];
			bg.x = x;
			bg.y = y;
			bg.size(width, height);
			bg.am = DeckBuilderRun.shopSold != null && DeckBuilderRun.shopSold[index] ? 0.35f : 0.82f;
			icon.view(offerIcon(offer), null);
			icon.x = x + 4;
			icon.y = y + (height - icon.height()) / 2f;
			name.text(offerTitle(offer));
			name.maxWidth((int)width - 42);
			name.hardlight(offer.type == DeckShop.RELIC ? 0xFFD5F27A : 0xFFFFFFFF);
			name.setPos(x + 24, y + 3);
			price.text((DeckBuilderRun.shopSold != null && DeckBuilderRun.shopSold[index] ? "완료" : offer.price + "G"));
			price.hardlight(DeckBuilderRun.gold >= offer.price ? 0xFFFFD66B : 0xFFFF7777);
			price.setPos(x + width - price.width() - 5, y + height - price.height() - 3);
		}

		@Override
		protected void onClick() {
			showOfferConfirm(index);
		}
	}

	private class ShopRemoveRow extends Button {
		private ColorBlock bg;
		private ItemSprite icon;
		private RenderedTextBlock text;
		private RenderedTextBlock price;

		@Override
		protected void createChildren() {
			super.createChildren();
			bg = new ColorBlock(1, 1, 0xFF261E25);
			add(bg);
			icon = new ItemSprite(ItemSpriteSheet.SCROLL_NAUDIZ);
			add(icon);
			text = renderTextBlock(6);
			add(text);
			price = renderTextBlock(6);
			add(price);
		}

		@Override
		protected void layout() {
			super.layout();
			bg.x = x;
			bg.y = y;
			bg.size(width, height);
			bg.am = DeckBuilderRun.shopRemoveUsed ? 0.35f : 0.86f;
			icon.x = x + 5;
			icon.y = y + (height - icon.height()) / 2f;
			text.text("카드 제거");
			text.hardlight(0xFFFFD5F0);
			text.setPos(x + 28, y + 6);
			price.text(DeckBuilderRun.shopRemoveUsed ? "완료" : DeckShop.removePrice() + "G");
			price.hardlight(DeckBuilderRun.gold >= DeckShop.removePrice() ? 0xFFFFD66B : 0xFFFF7777);
			price.setPos(x + width - price.width() - 6, y + 6);
		}

		@Override
		protected void onClick() {
			showRemoveSelection(0);
		}
	}

	private int offerIcon(DeckShop.Offer offer) {
		if (offer.type == DeckShop.POTION) return DeckPotion.byId(offer.id).image;
		if (offer.type == DeckShop.RELIC) return DeckRelic.byId(offer.id).icon;
		return ItemSpriteSheet.SOMETHING;
	}

	private class CardMiniButton extends Button {
		private final int cardCode;
		private ColorBlock shadow;
		private ColorBlock edge;
		private ColorBlock face;
		private ColorBlock artPanel;
		private ItemSprite art;
		private Image spriteArt;
		private TalentIcon talentArt;
		private RenderedTextBlock cost;
		private RenderedTextBlock title;
		private RenderedTextBlock typeLabel;

		private CardMiniButton(int cardCode) {
			this.cardCode = cardCode;
		}

		@Override
		protected void createChildren() {
			super.createChildren();
			shadow = new ColorBlock(1, 1, 0xFF000000);
			add(shadow);
			edge = new ColorBlock(1, 1, 0xFFFFFFFF);
			add(edge);
			face = new ColorBlock(1, 1, 0xFF262626);
			add(face);
			artPanel = new ColorBlock(1, 1, 0xFF111111);
			add(artPanel);
			spriteArt = new Image();
			spriteArt.visible = false;
			add(spriteArt);
			cost = renderTextBlock(8);
			add(cost);
			title = renderTextBlock(5);
			add(title);
			typeLabel = renderTextBlock(5);
			add(typeLabel);
		}

		@Override
		protected void layout() {
			super.layout();
			DeckCard card = DeckCard.byCode(cardCode);
			shadow.x = x + 2;
			shadow.y = y + 2;
			shadow.size(width, height - 10);
			shadow.am = 0.45f;
			edge.color(card.type.borderColor);
			edge.x = x;
			edge.y = y;
			edge.size(width, height - 10);
			face.color(card.deckClass == null || !card.reward ? card.rarity.faceColor : card.classFaceColor());
			face.x = x + 2;
			face.y = y + 2;
			face.size(width - 4, height - 14);
			cost.text(String.valueOf(card.cost(cardCode)));
			cost.hardlight(0xFFFFD84D);
			cost.setPos(x + 4, y + 4);
			title.text(card.title(cardCode));
			title.hardlight(card.rarity == DeckCardRarity.COMMON ? 0xFFFFFFFF : card.rarity.labelColor);
			title.maxWidth((int)width - 15);
			title.setPos(x + 13, y + 5);
			artPanel.color(card.deckClass == null || !card.reward ? card.rarity.panelColor : card.classPanelColor());
			artPanel.x = x + 5;
			artPanel.y = y + height * 0.30f;
			artPanel.size(width - 10, Math.max(20, height * 0.38f));
			artPanel.am = 0.32f;
			layoutArt(card);
			typeLabel.text(card.type.label);
			typeLabel.hardlight(card.type == DeckCardType.STATUS ? card.type.labelColor : card.rarity.labelColor);
			typeLabel.maxWidth((int)width - 10);
			typeLabel.setPos(x + (width - typeLabel.width()) / 2f, y + height - typeLabel.height() - 16);
		}

		private void layoutArt(DeckCard card) {
			if (card == DeckCard.SLIMY) {
				if (art != null) art.visible = false;
				if (talentArt != null) talentArt.visible = false;
				spriteArt.visible = true;
				spriteArt.texture(Assets.Sprites.RAT);
				TextureFilm film = new TextureFilm(spriteArt.texture, 16, 15);
				spriteArt.frame(film.get(0));
				spriteArt.scale.set(1.5f);
				spriteArt.x = artPanel.x + (artPanel.width() - spriteArt.width()) / 2f;
				spriteArt.y = artPanel.y + (artPanel.height() - spriteArt.height()) / 2f;
				align(spriteArt);
			} else if (card.talentIcon != null) {
				if (art != null) art.visible = false;
				spriteArt.visible = false;
				if (talentArt != null) remove(talentArt);
				talentArt = new TalentIcon(card.talentIcon);
				add(talentArt);
				talentArt.scale.set(1.0f);
				talentArt.x = artPanel.x + (artPanel.width() - talentArt.width()) / 2f;
				talentArt.y = artPanel.y + (artPanel.height() - talentArt.height()) / 2f;
				align(talentArt);
			} else {
				if (talentArt != null) talentArt.visible = false;
				if (art == null) {
					art = new ItemSprite(card.icon);
					add(art);
				}
				spriteArt.visible = false;
				art.view(card.icon, null);
				art.scale.set(1.0f);
				art.x = artPanel.x + (artPanel.width() - art.width()) / 2f;
				art.y = artPanel.y + (artPanel.height() - art.height()) / 2f;
				align(art);
				art.visible = true;
			}
		}
	}
}
