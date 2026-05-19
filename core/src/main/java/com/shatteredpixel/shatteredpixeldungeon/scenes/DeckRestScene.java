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
import com.shatteredpixel.shatteredpixeldungeon.deckbuilder.DeckCardText;
import com.shatteredpixel.shatteredpixeldungeon.deckbuilder.DeckCardType;
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
import java.util.ArrayList;

public class DeckRestScene extends PixelScene {

	private static final int CARD_W = 42;
	private static final int CARD_H = 54;
	private static final int CARD_GAP = 5;
	private static final int CARDS_PER_PAGE = 4;

	private boolean used;

	@Override
	public void create() {
		inGameScene = true;
		super.create();

		DeckBuilderRun.initRestForCurrentNode();
		used = DeckBuilderRun.restUsedForCurrentNode();
		saveRun();

		int w = Camera.main.width;
		int h = Camera.main.height;
		RectF insets = getCommonInsets();
		float usableW = w - insets.left - insets.right;

		addBackground(w, h);
		addRunHud(insets);
		addExitButton(insets, w);

		ItemSprite icon = new ItemSprite(ItemSpriteSheet.TORCH);
		icon.scale.set(1.8f);
		RenderedTextBlock title = renderTextBlock("휴식 층", 12);
		title.hardlight(Window.TITLE_COLOR);
		float titleW = icon.width() + 7 + title.width();
		icon.x = insets.left + (usableW - titleW) / 2f;
		icon.y = insets.top + 13;
		title.setPos(icon.x + icon.width() + 7, icon.y + (icon.height() - title.height()) / 2f);
		align(icon);
		align(title);
		add(icon);
		add(title);

		RenderedTextBlock body = renderTextBlock("잠시 숨을 고를 수 있는 안전한 장소다.\n한 가지 행동을 선택한다.", 7);
		body.hardlight(0xFFD8D1BD);
		body.maxWidth((int)Math.min(260, usableW - 18));
		body.setPos(insets.left + (usableW - body.width()) / 2f, title.bottom() + 14);
		align(body);
		add(body);

		float buttonW = Math.min(260, usableW - 22);
		float buttonH = 40;
		float buttonX = insets.left + (usableW - buttonW) / 2f;
		float buttonStackH = buttonH * 2 + 8;
		float startY = Math.max(body.bottom() + 14, h - insets.bottom - buttonStackH - 18);

		addRestOptions(buttonX, startY, buttonW, buttonH);

		fadeIn();
	}

	private void addBackground(int w, int h) {
		add(new ColorBlock(w, h, 0xFF10140F));
		Image splash = new Image(Assets.Splashes.TENDENCY);
		float scale = Math.max(w / splash.width(), h / splash.height());
		splash.scale.set(scale);
		splash.x = (w - splash.width()) / 2f;
		splash.y = (h - splash.height()) / 2f;
		splash.am = 0.30f;
		add(splash);
		ColorBlock veil = new ColorBlock(w, h, 0xFF050805);
		veil.am = 0.62f;
		add(veil);
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

	private void addRestOptions(float x, float y, float width, float height) {
		ArrayList<RestChoiceButton> buttons = new ArrayList<>();

		buttons.add(new RestChoiceButton("휴식", "최대 체력의 30%를 회복합니다.  회복량: " + DeckBuilderRun.restHealAmount(), 0xFF66E28B, DeckBuilderRun.canRestAtRestSite()) {
			@Override
			protected void onClick() {
				if (!enabled) {
					addToFront(new WndMessage("휴식\n\n지금은 휴식할 수 없습니다."));
					return;
				}
				if (DeckBuilderRun.restAtRestSite()) {
					Sample.INSTANCE.play(Assets.Sounds.DRINK);
					saveRun();
					leaveRest();
				}
			}
		});

		buttons.add(new RestChoiceButton("강화", "덱의 카드 한 장을 선택해 강화합니다.", 0xFFD5F27A, DeckBuilderRun.canSmithAtRestSite()) {
			@Override
			protected void onClick() {
				if (!enabled) {
					addToFront(new WndMessage("강화\n\n강화할 수 있는 카드가 없습니다."));
					return;
				}
				showCardSelection(0);
			}
		});

		for (int i = 0; i < buttons.size(); i++) {
			RestChoiceButton button = buttons.get(i);
			button.setRect(x, y + i * (height + 8), width, height);
			add(button);
		}

		if (used) {
			RenderedTextBlock done = renderTextBlock("이미 이 휴식 층에서 행동을 선택했습니다.", 6);
			done.hardlight(0xFFAAAFA4);
			done.setPos(x + (width - done.width()) / 2f, y + buttons.size() * (height + 8) + 2);
			add(done);
		}
	}

	private void showCardSelection(int page) {
		ArrayList<Integer> choices = upgradableDeckIndices();
		if (choices.isEmpty()) {
			addToFront(new WndMessage("강화\n\n강화할 수 있는 카드가 없습니다."));
			return;
		}

		final int total = choices.size();
		final int maxPage = Math.max(0, (total - 1) / CARDS_PER_PAGE);
		final int currentPage = Math.max(0, Math.min(page, maxPage));
		final int first = currentPage * CARDS_PER_PAGE;
		final int count = Math.min(CARDS_PER_PAGE, total - first);
		final int cols = Math.max(1, count);
		final int rows = (count + cols - 1) / cols;

		final Window win = new Window();
		int totalCardW = cols * CARD_W + (cols - 1) * CARD_GAP;
		int width = Math.max(196, totalCardW + 20);
		int pos = 7;

		RenderedTextBlock title = renderTextBlock("강화할 카드 선택", 9);
		title.hardlight(Window.TITLE_COLOR);
		title.setPos((width - title.width()) / 2f, pos);
		win.add(title);
		pos += 17;

		RenderedTextBlock help = renderTextBlock("카드를 선택하면 강화 효과를 확인할 수 있습니다.", 5);
		help.hardlight(0xFFAAAFA4);
		help.maxWidth(width - 18);
		help.setPos((width - help.width()) / 2f, pos);
		win.add(help);
		pos += (int)help.height() + 8;

		int startX = (width - totalCardW) / 2;
		for (int i = 0; i < count; i++) {
			final int deckIndex = choices.get(first + i);
			final int code = DeckBuilderRun.deck.get(deckIndex);
			CardChoiceButton card = new CardChoiceButton(code) {
				@Override
				protected void onClick() {
					confirmSmith(win, deckIndex, code);
				}
			};
			int col = i % cols;
			int row = i / cols;
			card.setRect(startX + col * (CARD_W + CARD_GAP), pos + row * (CARD_H + CARD_GAP), CARD_W, CARD_H);
			win.add(card);
		}
		pos += rows * CARD_H + Math.max(0, rows - 1) * CARD_GAP + 9;

		if (maxPage > 0) {
			RedButton prev = new RedButton("이전", 6) {
				@Override
				protected void onClick() {
					win.hide();
					showCardSelection(currentPage - 1);
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
					showCardSelection(currentPage + 1);
				}
			};
			next.enable(currentPage < maxPage);
			next.setRect(width - 68, pos, 58, 18);
			win.add(next);
			pos += 23;
		}

		RedButton cancel = new RedButton("취소", 6) {
			@Override
			protected void onClick() {
				win.hide();
			}
		};
		cancel.setRect((width - 100) / 2f, pos, 100, 18);
		win.add(cancel);
		pos += 25;

		win.resize(width, pos);
		addToFront(win);
	}

	private void confirmSmith(final Window cardWindow, final int deckIndex, final int cardCode) {
		final Window win = new Window();
		DeckCard card = DeckCard.byCode(cardCode);
		int width = 190;
		int pos = 7;

		RenderedTextBlock title = renderTextBlock(DeckCardText.detailTitle(card, cardCode), 8);
		title.hardlight(Window.TITLE_COLOR);
		title.maxWidth(width - 14);
		title.setPos((width - title.width()) / 2f, pos);
		win.add(title);
		pos += (int)title.height() + 8;

		RenderedTextBlock desc = renderTextBlock(DeckCardText.rulesText(card, cardCode), 6);
		desc.hardlight(0xFFD8D1BD);
		desc.maxWidth(width - 14);
		desc.setPos(7, pos);
		win.add(desc);
		pos += (int)desc.height() + 8;

		RenderedTextBlock preview = renderTextBlock("강화 효과\n" + upgradePreviewText(cardCode), 6);
		preview.hardlight(0xFFD5F27A);
		preview.maxWidth(width - 14);
		preview.setPos(7, pos);
		win.add(preview);
		pos += (int)preview.height() + 8;

		RedButton confirm = new RedButton("강화", 6) {
			@Override
			protected void onClick() {
				if (DeckBuilderRun.smithAtRestSite(deckIndex)) {
					Sample.INSTANCE.play(Assets.Sounds.EVOKE);
					saveRun();
					win.hide();
					cardWindow.hide();
					leaveRest();
				}
			}
		};
		confirm.setRect(7, pos, 82, 18);
		win.add(confirm);

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

	private ArrayList<Integer> upgradableDeckIndices() {
		ArrayList<Integer> choices = new ArrayList<>();
		for (int i = 0; i < DeckBuilderRun.deck.size(); i++) {
			int code = DeckBuilderRun.deck.get(i);
			if (DeckCard.upgrade(code) != code) choices.add(i);
		}
		return choices;
	}

	private String upgradePreviewText(int cardCode) {
		if (cardCode >= 0) return DeckCardText.upgradePreviewText(cardCode);
		int upgraded = DeckCard.upgrade(cardCode);
		DeckCard card = DeckCard.byCode(cardCode);
		if (upgraded == cardCode) return "더 이상 강화할 수 없습니다.";
		if (card == DeckCard.SPIN_TRAINING) return "회전하는 손톱 피해량 +1 > +2";
		if (card == DeckCard.LESSON_FIVE) return "비용 2 > 1";

		String text = "";
		if (card.cost(cardCode) != card.cost(upgraded)) text += append(text, "비용 " + card.cost(cardCode) + " > " + card.cost(upgraded));
		if (card.damage(cardCode) != card.damage(upgraded)) text += append(text, "피해 " + card.damage(cardCode) + " > " + card.damage(upgraded));
		if (card.block(cardCode) != card.block(upgraded)) text += append(text, "방어 " + card.block(cardCode) + " > " + card.block(upgraded));
		if (card.draw(cardCode) != card.draw(upgraded)) text += append(text, "드로우 " + card.draw(cardCode) + " > " + card.draw(upgraded));
		if (card.vulnerable(cardCode) != card.vulnerable(upgraded)) text += append(text, "피해 증폭 " + card.vulnerable(cardCode) + " > " + card.vulnerable(upgraded));
		if (card.strength(cardCode) != card.strength(upgraded)) text += append(text, "힘 " + card.strength(cardCode) + " > " + card.strength(upgraded));
		if (card.shivs(cardCode) != card.shivs(upgraded)) text += append(text, "단도 " + card.shivs(cardCode) + " > " + card.shivs(upgraded));
		return text.length() > 0 ? text : "강화 효과가 아직 정의되지 않았습니다.";
	}

	private String append(String text, String value) {
		return (text.length() > 0 ? "\n" : "") + value;
	}

	private void leaveRest() {
		DeckBuilderRun.clearRest();
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

	private class RestChoiceButton extends Button {

		private final String label;
		private final String desc;
		private final int accentColor;
		protected final boolean enabled;
		private ColorBlock shadow;
		private ColorBlock bg;
		private ColorBlock accent;
		private RenderedTextBlock title;
		private RenderedTextBlock body;

		private RestChoiceButton(String label, String desc, int accentColor, boolean available) {
			this.label = label;
			this.desc = desc;
			this.accentColor = accentColor;
			this.enabled = available && !used;
		}

		@Override
		protected void createChildren() {
			super.createChildren();
			shadow = new ColorBlock(1, 1, 0xFF000000);
			shadow.am = 0.38f;
			add(shadow);
			bg = new ColorBlock(1, 1, 0xFF222821);
			add(bg);
			accent = new ColorBlock(1, 1, accentColor);
			add(accent);
			title = renderTextBlock(7);
			add(title);
			body = renderTextBlock(5);
			add(body);
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
			bg.am = enabled ? 0.96f : 0.48f;
			accent.x = x;
			accent.y = y;
			accent.size(3, height);
			accent.am = enabled ? 1f : 0.35f;
			title.text(label);
			title.hardlight(enabled ? Window.TITLE_COLOR : 0xFF777777);
			title.maxWidth((int)(width - 18));
			title.setPos(x + 9, y + 6);
			body.text(desc);
			body.hardlight(enabled ? 0xFFD8D1BD : 0xFF777777);
			body.maxWidth((int)(width - 18));
			body.setPos(x + 9, title.bottom() + 2);
		}
	}

	private class CardUpgradeRow extends Button {

		private final int cardCode;
		private ColorBlock bg;
		private ColorBlock accent;
		private RenderedTextBlock title;
		private RenderedTextBlock preview;

		private CardUpgradeRow(int cardCode) {
			this.cardCode = cardCode;
		}

		@Override
		protected void createChildren() {
			super.createChildren();
			bg = new ColorBlock(1, 1, 0xFF1F2521);
			bg.am = 0.95f;
			add(bg);
			accent = new ColorBlock(1, 1, 0xFFD5F27A);
			add(accent);
			title = renderTextBlock(6);
			title.hardlight(Window.TITLE_COLOR);
			add(title);
			preview = renderTextBlock(5);
			preview.hardlight(0xFFD8D1BD);
			add(preview);
		}

		@Override
		protected void layout() {
			super.layout();
			DeckCard card = DeckCard.byCode(cardCode);
			bg.x = x;
			bg.y = y;
			bg.size(width, height);
			accent.x = x;
			accent.y = y;
			accent.size(3, height);
			title.text(card.title(cardCode) + "  비용 " + card.cost(cardCode) + "  " + card.type.label);
			title.maxWidth((int)(width - 14));
			title.setPos(x + 8, y + 4);
			preview.text(upgradePreviewText(cardCode).replace("\n", " / "));
			preview.maxWidth((int)(width - 14));
			preview.setPos(x + 8, title.bottom() + 2);
		}
	}

	private class CardChoiceButton extends Button {

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

		private CardChoiceButton(int cardCode) {
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
			shadow.size(width, height);
			shadow.am = 0.45f;

			edge.color(card.type.borderColor);
			edge.x = x;
			edge.y = y;
			edge.size(width, height);
			edge.am = 0.95f;

			face.color(card.deckClass == null || !card.reward ? card.rarity.faceColor : card.classFaceColor());
			face.x = x + 2;
			face.y = y + 2;
			face.size(width - 4, height - 4);
			face.am = 0.96f;

			cost.text(String.valueOf(card.cost(cardCode)));
			cost.hardlight(0xFFFFD84D);
			cost.setPos(x + 4, y + 4);

			title.text(card.title(cardCode));
			title.hardlight(card.rarity == DeckCardRarity.COMMON ? 0xFFFFFFFF : card.rarity.labelColor);
			title.maxWidth((int)width - 15);
			title.setPos(x + 13, y + 5);

			float artH = Math.max(20, height * 0.45f);
			artPanel.color(card.deckClass == null || !card.reward ? card.rarity.panelColor : card.classPanelColor());
			artPanel.x = x + 5;
			artPanel.y = y + height * 0.30f;
			artPanel.size(width - 10, artH);
			artPanel.am = 0.32f;

			layoutArt(card);

			typeLabel.text(card.type.label);
			typeLabel.hardlight(cardLabelColor(card));
			typeLabel.maxWidth((int)width - 10);
			typeLabel.setPos(x + (width - typeLabel.width()) / 2f, y + height - typeLabel.height() - 6);
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
				talentArt.scale.set(1.15f);
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
				art.scale.set(1.15f);
				art.x = artPanel.x + (artPanel.width() - art.width()) / 2f;
				art.y = artPanel.y + (artPanel.height() - art.height()) / 2f;
				align(art);
				art.visible = true;
			}
		}

		private int cardLabelColor(DeckCard card) {
			if (card.type == DeckCardType.STATUS || card.type == DeckCardType.CURSE) {
				return card.type.labelColor;
			}
			return card.rarity.labelColor;
		}
	}
}
