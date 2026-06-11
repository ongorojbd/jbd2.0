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
import com.shatteredpixel.shatteredpixeldungeon.deckbuilder.DeckPotionPolicy;
import com.shatteredpixel.shatteredpixeldungeon.deckbuilder.DeckCard;
import com.shatteredpixel.shatteredpixeldungeon.deckbuilder.DeckCardKeyword;
import com.shatteredpixel.shatteredpixeldungeon.deckbuilder.DeckCardPool;
import com.shatteredpixel.shatteredpixeldungeon.deckbuilder.DeckCardRarity;
import com.shatteredpixel.shatteredpixeldungeon.deckbuilder.DeckCardTarget;
import com.shatteredpixel.shatteredpixeldungeon.deckbuilder.DeckCardText;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.sprites.AlchemistSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.BlacksmithSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ButterflySprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ImpSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.WarlockSprite;
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
import com.watabou.utils.Random;
import com.watabou.utils.RectF;

import java.io.IOException;
import java.util.ArrayList;

public class DeckEventScene extends PixelScene {

	private static final int UPGRADE_SHRINE = 0;
	private static final int PURIFIER = 1;
	private static final int TRANSMOGRIFIER = 2;
	private static final int GOLDEN_SHRINE = 3;
	private static final int BLUE_WOMAN = 4;

	private static final int CARD_W = 42;
	private static final int CARD_H = 54;
	private static final int CARD_GAP = 5;
	private static final int CARDS_PER_PAGE = 4;

	private int eventType;
	private boolean resolved;

	@Override
	public void create() {
		inGameScene = true;
		super.create();

		DeckBuilderRun.initIfNeeded();
		eventType = eventType();
		saveRun();

		if (Dungeon.level != null) {
			Dungeon.level.playLevelMusic();
		}

		int w = Camera.main.width;
		int h = Camera.main.height;
		RectF insets = getCommonInsets();
		float usableW = w - insets.left - insets.right;

		add(new ColorBlock(w, h, 0xFF10140F));
		addRunHud(insets);
		addExitButton(insets, w);

		Image icon;
		if      (eventType == UPGRADE_SHRINE)  icon = Icons.TALENT.get();
		else if (eventType == PURIFIER)        icon = new ItemSprite(ItemSpriteSheet.OBLIVION_SHARD);
		else if (eventType == TRANSMOGRIFIER)  icon = new ItemSprite(ItemSpriteSheet.SCROLL_MANNAZ);
		else if (eventType == GOLDEN_SHRINE)   icon = new ItemSprite(ItemSpriteSheet.GOLD);
		else                                   icon = new ItemSprite(ItemSpriteSheet.POTION_AZURE);
		RenderedTextBlock title = renderTextBlock(titleText(), 12);
		title.hardlight(Window.TITLE_COLOR);
		float titleW = icon.width() + 6 + title.width();
		icon.x = insets.left + (usableW - titleW) / 2f;
		icon.y = insets.top + 12;
		title.setPos(icon.x + icon.width() + 6, icon.y + (icon.height() - title.height()) / 2f);
		align(icon);
		align(title);
		add(icon);
		add(title);

		RenderedTextBlock body = renderTextBlock(descriptionText(), 7);
		body.hardlight(0xFFD8D1BD);
		body.maxWidth((int)Math.min(230, usableW - 16));
		body.setPos(insets.left + (usableW - body.width()) / 2f, title.bottom() + 14);
		align(body);
		add(body);

		boolean wide = usableW >= h * 1.15f;
		float buttonW = Math.min(wide ? 205 : 250, usableW - 22);
		int buttonCount = (eventType == BLUE_WOMAN) ? 4 : 2;
		float buttonH = (eventType == BLUE_WOMAN) ? 28 : 38;
		float buttonGap = (eventType == BLUE_WOMAN) ? 6 : 8;
		float buttonX = insets.left + (usableW - buttonW) / 2f;
		float buttonStackH = buttonH * buttonCount + buttonGap * (buttonCount - 1);
		float prayY;

		CharSprite host;
		if      (eventType == UPGRADE_SHRINE)  host = new BlacksmithSprite();
		else if (eventType == PURIFIER)        host = new AlchemistSprite();
		else if (eventType == TRANSMOGRIFIER)  host = new WarlockSprite();
		else if (eventType == GOLDEN_SHRINE)   host = new ImpSprite();
		else                                   host = new ButterflySprite();
		if (wide) {
			host.scale.set(Math.max(3.2f, Math.min(4.5f, h / 65f)));
			float hostGap = 18;
			float groupW = Math.min(usableW - 20, host.width() + hostGap + buttonW);
			float groupX = insets.left + (usableW - groupW) / 2f;
			host.x = groupX;
			prayY = Math.min(h - insets.bottom - buttonStackH - 14, Math.max(body.bottom() + 18, body.bottom() + 42));
			buttonX = Math.min(groupX + host.width() + hostGap, insets.left + usableW - buttonW - 8);
			host.y = prayY + (buttonStackH - host.height()) / 2f;
			host.visible = true;
		} else {
			prayY = Math.max(body.bottom() + 12, h - insets.bottom - buttonStackH - 14);
			float hostTop = body.bottom() + 10;
			float hostSpace = prayY - hostTop - 8;
			float hostScale = Math.max(1.5f, Math.min(3.1f, h / 120f));
			host.scale.set(hostScale);
			if (host.height() > hostSpace && hostSpace > 18) {
				host.scale.set(Math.max(1f, hostScale * hostSpace / host.height()));
			}
			host.visible = hostSpace > 18;
			host.x = insets.left + (usableW - host.width()) / 2f;
			host.y = hostTop + Math.max(0, (hostSpace - host.height()) / 2f);
		}
		align(host);
		add(host);

		addEventButtons(buttonX, buttonW, prayY, buttonH, buttonGap);

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

	private int eventType() {
		long hash = Dungeon.seed;
		hash ^= Dungeon.depth * 0x9E3779B97F4A7C15L;
		hash ^= Statistics.deckBuilderMapPath * 0xBF58476D1CE4E5B9L;
		hash ^= (hash >>> 33);
		int n = DeckBuilderRun.gold >= 50 ? 5 : 4;
		return (int) Math.floorMod(hash, n);
	}

	private String titleText() {
		switch (eventType) {
			case UPGRADE_SHRINE:  return "강화 성소";
			case PURIFIER:        return "정화 성소";
			case TRANSMOGRIFIER:  return "변환 성소";
			case GOLDEN_SHRINE:   return "황금 성소";
			default:              return "파란 옷의 여자";
		}
	}

	private String descriptionText() {
		switch (eventType) {
			case UPGRADE_SHRINE:  return "강화 성소가 놓여 있다.\n\n기도하면 카드 한 장을 선택해 강화한다.";
			case PURIFIER:        return "정화 성소가 놓여 있다.\n\n기도하면 카드 한 장을 선택해 제거한다.";
			case TRANSMOGRIFIER:  return "변환 성소가 놓여 있다.\n\n기도하면 카드 한 장을 선택해 카드풀 내 무작위 카드로 변화시킨다.";
			case GOLDEN_SHRINE:   return "고대의 영혼을 기리는 공들인 성소가 놓여 있다.";
			default:              return "어두운 곳에서 시야가 밝아지자, 어떤 여성이 다짜고짜 외친다.\n\n\"포션 사세요, 당장!\"";
		}
	}

	private String actionText() {
		switch (eventType) {
			case UPGRADE_SHRINE: return "카드 한 장을 선택해 강화합니다.";
			case PURIFIER:       return "카드 한 장을 선택해 제거합니다.";
			default:             return "카드 한 장을 선택해 무작위 카드로 변화시킵니다.";
		}
	}

	private void showCardSelection(int page) {
		ArrayList<Integer> choices = selectableDeckIndices();
		if (choices.isEmpty()) {
			addToFront(new WndMessage("덱\n\n선택할 수 있는 카드가 없습니다."));
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

		RenderedTextBlock title = renderTextBlock(
				eventType == UPGRADE_SHRINE ? "강화할 카드 선택"
				: eventType == PURIFIER ? "제거할 카드 선택"
				: "변환할 카드 선택", 9);
		title.hardlight(Window.TITLE_COLOR);
		title.setPos((width - title.width()) / 2f, pos);
		win.add(title);
		pos += 17;

		RenderedTextBlock help = renderTextBlock("카드를 선택하면 효과를 확인할 수 있습니다.", 5);
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
					showCardConfirmWindow(win, deckIndex, code);
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

	private ArrayList<Integer> selectableDeckIndices() {
		ArrayList<Integer> choices = new ArrayList<>();
		for (int i = 0; i < DeckBuilderRun.deck.size(); i++) {
			int code = DeckBuilderRun.deck.get(i);
			if (eventType != UPGRADE_SHRINE || DeckCard.upgrade(code) != code) {
				choices.add(i);
			}
		}
		return choices;
	}

	private void showCardConfirmWindow(final Window cardWindow, final int deckIndex, final int cardCode) {
		final Window win = new Window();
		final DeckCard card = DeckCard.byCode(cardCode);
		int width = 190;
		int pos = 7;

		RenderedTextBlock title = renderTextBlock(cardDetailTitle(card, cardCode), 8);
		title.hardlight(Window.TITLE_COLOR);
		title.maxWidth(width - 14);
		title.setPos((width - title.width()) / 2f, pos);
		win.add(title);
		pos += (int)title.height() + 8;

		RenderedTextBlock desc = renderTextBlock(DeckCardText.rulesAndKeywordText(card, cardCode), 6);
		desc.maxWidth(width - 14);
		desc.hardlight(0xFFD8D1BD);
		desc.setPos(7, pos);
		win.add(desc);
		pos += (int)desc.height() + 8;

		if (eventType == UPGRADE_SHRINE) {
			RenderedTextBlock upgrade = renderTextBlock("강화 효과\n" + upgradePreviewText(cardCode), 6);
			upgrade.maxWidth(width - 14);
			upgrade.hardlight(0xFFD5F27A);
			upgrade.setPos(7, pos);
			win.add(upgrade);
			pos += (int)upgrade.height() + 8;
		}

		RedButton confirm = new RedButton(eventType == UPGRADE_SHRINE ? "강화" : eventType == PURIFIER ? "제거" : "변환", 6) {
			@Override
			protected void onClick() {
				if (resolved) return;
				if (eventType == UPGRADE_SHRINE) {
					DeckBuilderRun.upgradeCardAt(deckIndex);
				} else if (eventType == PURIFIER) {
					DeckBuilderRun.removeCardAt(deckIndex);
				} else {
					transformCard(deckIndex);
				}
				resolved = true;
				Sample.INSTANCE.play(eventType == UPGRADE_SHRINE ? Assets.Sounds.EVOKE : Assets.Sounds.CURSED);
				win.hide();
				cardWindow.hide();
				leaveEvent();
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

	private void addEventButtons(float buttonX, float buttonW, float prayY, float buttonH, float buttonGap) {
		if (eventType == GOLDEN_SHRINE) {
			addGoldenShrineButtons(buttonX, buttonW, prayY, buttonH, buttonGap);
		} else if (eventType == BLUE_WOMAN) {
			addBlueWomanButtons(buttonX, buttonW, prayY, buttonH, buttonGap);
		} else {
			addCardEventButtons(buttonX, buttonW, prayY, buttonH, buttonGap);
		}
	}

	private void addCardEventButtons(float buttonX, float buttonW, float prayY, float buttonH, float buttonGap) {
		int accentColor = eventType == UPGRADE_SHRINE ? 0xFFD5F27A
				: eventType == PURIFIER ? 0xFF8FE6FF : 0xFFD478E8;
		EventChoiceButton pray = new EventChoiceButton("기도", actionText(), accentColor) {
			@Override protected void onClick() {
				if (resolved) return;
				showCardSelection(0);
			}
		};
		pray.setRect(buttonX, prayY, buttonW, buttonH);
		add(pray);

		EventChoiceButton leave = new EventChoiceButton("떠난다", "아무 일도 일어나지 않습니다.", 0xFFB8A77D) {
			@Override protected void onClick() { leaveEvent(); }
		};
		leave.setRect(buttonX, prayY + buttonH + buttonGap, buttonW, buttonH);
		add(leave);
	}

	private void addGoldenShrineButtons(float buttonX, float buttonW, float prayY, float buttonH, float buttonGap) {
		EventChoiceButton pray = new EventChoiceButton("기도", "100 골드를 획득합니다.", 0xFFD5F27A) {
			@Override protected void onClick() {
				if (resolved) return;
				DeckBuilderRun.gold += 100;
				resolved = true;
				Sample.INSTANCE.play(Assets.Sounds.GOLD);
				leaveEvent();
			}
		};
		pray.setRect(buttonX, prayY, buttonW, buttonH);
		add(pray);

		EventChoiceButton blasphemy = new EventChoiceButton("신성 모독", "275 골드를 획득합니다. 저주 카드 '후회'를 받습니다.", 0xFFD4844A) {
			@Override protected void onClick() {
				if (resolved) return;
				DeckBuilderRun.gold += 275;
				DeckBuilderRun.addCard(DeckCard.REGRET);
				resolved = true;
				Sample.INSTANCE.play(Assets.Sounds.CURSED);
				leaveEvent();
			}
		};
		blasphemy.setRect(buttonX, prayY + buttonH + buttonGap, buttonW, buttonH);
		add(blasphemy);
	}

	private void addBlueWomanButtons(float buttonX, float buttonW, float prayY, float buttonH, float buttonGap) {
		int[] costs = {20, 30, 40};
		int[] counts = {1, 2, 3};
		String[] labels = {"포션 1개 구매", "포션 2개 구매", "포션 3개 구매"};

		for (int i = 0; i < 3; i++) {
			final int cost = costs[i];
			final int count = counts[i];
			String desc = cost + " 골드를 잃고 무작위 포션 " + count + "개를 얻습니다.";
			EventChoiceButton btn = new EventChoiceButton(labels[i], desc, 0xFF8FE6FF) {
				@Override protected void onClick() {
					if (resolved) return;
					if (DeckBuilderRun.gold < cost) return;
					DeckBuilderRun.gold -= cost;
					for (int j = 0; j < count; j++) {
						DeckBuilderRun.addPotion(DeckPotionPolicy.randomPotion(DeckPotionPolicy.rollRarity()));
					}
					resolved = true;
					Sample.INSTANCE.play(Assets.Sounds.DRINK);
					leaveEvent();
				}
			};
			btn.setRect(buttonX, prayY + i * (buttonH + buttonGap), buttonW, buttonH);
			add(btn);
		}

		EventChoiceButton pass = new EventChoiceButton("사지 않는다", "아무 일도 일어나지 않습니다.", 0xFFB8A77D) {
			@Override protected void onClick() { leaveEvent(); }
		};
		pass.setRect(buttonX, prayY + 3 * (buttonH + buttonGap), buttonW, buttonH);
		add(pass);
	}

	private void transformCard(int deckIndex) {
		DeckCard[] pool = DeckCard.rewardPool(DeckBuilderRun.heroClass(), false, false);
		DeckCard current = DeckCard.byCode(DeckBuilderRun.deck.get(deckIndex));
		ArrayList<DeckCard> filtered = new ArrayList<>();
		for (DeckCard c : pool) { if (c != current) filtered.add(c); }
		if (filtered.isEmpty()) return;
		DeckBuilderRun.deck.set(deckIndex, filtered.get(Random.Int(filtered.size())).code());
	}

	private void leaveEvent() {
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

	private String cardDetailTitle(DeckCard card, int cardCode) {
		return DeckCardText.detailTitle(card, cardCode);
	}

	private String cardRulesText(DeckCard card, int cardCode) {
		return DeckCardText.rulesText(card, cardCode);
	}

	private String upgradePreviewText(int cardCode) {
		return DeckCardText.upgradePreviewText(cardCode);
	}

	private String keywordText(DeckCard card, int cardCode) {
		return DeckCardText.keywordText(card, cardCode);
	}

	@Override
	protected void onBackPressed() {
	}

	private class EventChoiceButton extends Button {

		private final String label;
		private final String desc;
		private final int accentColor;
		private ColorBlock shadow;
		private ColorBlock bg;
		private ColorBlock accent;
		private RenderedTextBlock title;
		private RenderedTextBlock body;

		private EventChoiceButton(String label, String desc, int accentColor) {
			this.label = label;
			this.desc = desc;
			this.accentColor = accentColor;
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
			title = renderTextBlock(7);
			title.hardlight(Window.TITLE_COLOR);
			add(title);
			body = renderTextBlock(5);
			body.hardlight(0xFFD8D1BD);
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
			accent.x = x;
			accent.y = y;
			accent.size(3, height);
			title.text(label);
			title.maxWidth((int)(width - 18));
			title.setPos(x + 9, y + 6);
			body.text(desc);
			body.maxWidth((int)(width - 18));
			body.setPos(x + 9, title.bottom() + 2);
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

			face.color(DeckCardPool.isNeutralCard(card) || !card.reward ? card.rarity.faceColor : card.classFaceColor());
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
			artPanel.color(DeckCardPool.isNeutralCard(card) || !card.reward ? card.rarity.panelColor : card.classPanelColor());
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
					art = new ItemSprite(card.icon());
					add(art);
				}
				spriteArt.visible = false;
				art.view(card.icon(), null);
				art.scale.set(1.15f);
				art.x = artPanel.x + (artPanel.width() - art.width()) / 2f;
				art.y = artPanel.y + (artPanel.height() - art.height()) / 2f;
				align(art);
				art.visible = true;
			}
		}

		private int cardLabelColor(DeckCard card) {
			if (DeckCardPool.isStatusOrCurse(card)) {
				return card.type.labelColor;
			}
			return card.rarity.labelColor;
		}
	}
}
