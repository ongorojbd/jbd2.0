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
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Act1;
import com.shatteredpixel.shatteredpixeldungeon.sprites.Act1Sprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.EmporioSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ShopkeeperSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.So1Sprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.So2Sprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.TsujiAyaSprite;
import com.watabou.noosa.audio.Music;
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
import com.shatteredpixel.shatteredpixeldungeon.deckbuilder.DeckRelic;
import com.shatteredpixel.shatteredpixeldungeon.deckbuilder.DeckRewardPolicy;
import com.shatteredpixel.shatteredpixeldungeon.levels.SewerLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.sprites.AlbinoSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.AlchemistSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.BlacksmithSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ButterflySprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.Butterfly2Sprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CausticSlimeSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GhostSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GnollGeomancerSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GnollTricksterSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ImpSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.PiranhaSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.RotLasherSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SlimeSprite;
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

	private static final int UPGRADE_SHRINE  = 0;
	private static final int PURIFIER        = 1;
	private static final int TRANSMOGRIFIER  = 2;
	private static final int GOLDEN_SHRINE   = 3;
	private static final int BLUE_WOMAN      = 4;
	private static final int LABORATORY      = 5;
	private static final int DUPLICATOR      = 6;
	private static final int SHINING_LIGHT   = 7;
	private static final int CLERIC          = 8;
	private static final int WORLD_OF_GOOP          = 9;
	private static final int LIVING_WALL             = 10;
	private static final int BIG_FISH                = 11;
	private static final int SHAPESHIFTER_FOREST     = 12;
	private static final int UNREST_SITE             = 13;
	private static final int THIS_OR_THAT            = 14;
	private static final int JUNGLE_MAZE_ADVENTURE  = 15;
	private static final int AROMA_OF_CHAOS          = 16;
	private static final int DOORS_OF_LIGHT_AND_DARK = 17;
	private static final int MAUSOLEUM               = 18;
	private static final int WHISPERING_HOLLOW      = 19;
	private static final int AVDOL_GHOST             = 20;

	private static final int HOST_BLACKSMITH       = 0;
	private static final int HOST_ALCHEMIST        = 1;
	private static final int HOST_WARLOCK          = 2;
	private static final int HOST_IMP              = 3;
	private static final int HOST_BUTTERFLY        = 4;
	private static final int HOST_ALBINO           = 5;
	private static final int HOST_TRICKSTER        = 6;
	private static final int HOST_BUTTERFLY2       = 7;
	private static final int HOST_GEOMANCER        = 8;
	private static final int HOST_CAUSTIC_SLIME    = 9;
	private static final int HOST_LASHER           = 10;
	private static final int HOST_PIRANHA          = 11;
	private static final int HOST_SLIME            = 12;
	private static final int HOST_GHOST            = 13;

	private static final int ICON_TALENT = -1;

	private static final DeckEventDef[] EVENT_DEFS = {
			new DeckEventDef(UPGRADE_SHRINE, "강화의 DISC", "던전에서 강화의 DISC를 발견했습니다.\n\n카드 한 장을 선택해 강화할 수 있습니다.", ICON_TALENT, ItemSpriteSheet.SCROLL_ISAZ, true),
			new DeckEventDef(PURIFIER, "폭발의 명령 DISC", "던전에서 폭발의 명령 DISC를 발견했습니다.\n\n카드 한 장을 선택해 제거할 수 있습니다.", ICON_TALENT, ItemSpriteSheet.STONE_BLAST, true),
			new DeckEventDef(TRANSMOGRIFIER, "변환의 DISC", "던전에서 변환의 DISC를 발견했습니다.\n\n카드 한 장을 선택해 변환할 수 있습니다.", ICON_TALENT, ItemSpriteSheet.SCROLL_BERKANAN, true),
			new DeckEventDef(GOLDEN_SHRINE, "함정의 방", "방 안에 엄청난 양의 골드 더미가 있지만, 동시에 수많은 함정이 깔려 있습니다.", ICON_TALENT, ItemSpriteSheet.GOLD, true, 50, false),
			new DeckEventDef(BLUE_WOMAN, "스기모토 레이미", "던전에서 스기모토 레이미를 마주쳤습니다.", ICON_TALENT, HOST_TRICKSTER, 50, false),
			new DeckEventDef(LABORATORY, "포션의 방", "각종 포션이 놓여 있는 방을 발견했습니다.", ICON_TALENT, ItemSpriteSheet.POTION_GOLDEN, true),
			new DeckEventDef(DUPLICATOR, "에르메스 코스텔로", "던전에서 에르메스 코스텔로를 마주쳤습니다.\n\n카드 한 장을 선택해 복제할 수 있습니다.", ICON_TALENT, HOST_BUTTERFLY),
			new DeckEventDef(SHINING_LIGHT, "낭떠러지 방", "아래로 떨어지라고 말하는 듯 보이는 표지판과 낭떠러지가 있는 방을 발견했습니다.\n\n이 방에서 낭떠러지로 떨어지면 어떻게 될까요?", ICON_TALENT, ItemSpriteSheet.SOMETHING, true),
			new DeckEventDef(CLERIC, "푸 파이터즈", "던전에서 푸 파이터즈를 마주쳤습니다.\n\n\"인간은 입으로 안 마시면 수분을 흡수할 수 없다니, 불편하네.\"", ICON_TALENT, HOST_GEOMANCER, 35, false),
			new DeckEventDef(WORLD_OF_GOOP, "낭떠러지와 상자", "방의 네 귀퉁이에는 황금 열쇠가, 방의 중앙 네 칸에는 상자가 있고 나머지는 모두 낭떠러지인 방을 발견했습니다.", ICON_TALENT, ItemSpriteSheet.GOLDEN_KEY, true, 50, false),
			new DeckEventDef(LIVING_WALL, "엠포리오 아르니뇨", "던전에서 엠포리오를 마주쳤습니다.\n\n\"여기서 얘기하기도 좀 그러니까 내 방으로 올래?\"", ICON_TALENT, HOST_LASHER),
			new DeckEventDef(BIG_FISH, "에코즈의 알 방", "방 중앙에 기묘한 수풀이 있고, 방의 가장자리에 직화구이 고기와 마르게리타 피자가 놓여 있는 방을 발견했습니다.", ICON_TALENT, HOST_PIRANHA),
			new DeckEventDef(SHAPESHIFTER_FOREST, "츠지 아야", "던전에서 츠지 아야를 마주쳤습니다.\n\n\"난 '행복한 얼굴'을 만들어주는 에스테티션이에요.\"", ICON_TALENT, HOST_SLIME, 100, false),
			new DeckEventDef(UNREST_SITE, "식물 방", "하늘빛 이슬개구리풀과 회복풀이 자라 있는 방을 발견했습니다.\n\n회복풀을 밟자 주변의 적들이 다가오기 시작합니다. 그럼에도 불구하고 휴식을 취할까요?", ICON_TALENT, ItemSpriteSheet.SEED_SUNGRASS, true, 0, true),
			new DeckEventDef(THIS_OR_THAT, "미로 방", "거대한 미로가 있고 미로의 한쪽 끝에 상자가 있는 방을 발견했습니다.", ICON_TALENT, ItemSpriteSheet.CHEST, true),
			new DeckEventDef(JUNGLE_MAZE_ADVENTURE, "적들이 가득 찬 방", "에니그마의 함정으로 도배되고 중앙에 상자가 놓여 있는 방을 발견했습니다.\n\n함정을 건드리면 수많은 적들이 나타날 것 같습니다..", ICON_TALENT, ItemSpriteSheet.BONES, true),
			new DeckEventDef(AROMA_OF_CHAOS, "기억 DISC의 방", "2개의 기억 DISC가 있는 방을 발견했습니다.\n\n하나는 강화의 DISC, 다른 하나는 변환의 DISC로 보입니다.", ICON_TALENT, ItemSpriteSheet.SCROLL_KAUNAN, true),
			new DeckEventDef(DOORS_OF_LIGHT_AND_DARK, "환영 열쇠와 상자 방", "상자 2개가 놓인 방을 발견했습니다. 상자를 열기 위해서는 환영 열쇠가 필요하지만 열쇠는 1개 뿐입니다..", ICON_TALENT, ItemSpriteSheet.CRYSTAL_KEY, true),
			new DeckEventDef(MAUSOLEUM, "무덤", "던전에서 무덤을 발견했습니다. 위험을 감수하고 무덤을 파보면 왠지 쓸만한 물건이 들어 있을 것 같습니다.", ICON_TALENT, ItemSpriteSheet.TOMB, true),
			new DeckEventDef(WHISPERING_HOLLOW, "로카카카", "등가교환의 능력을 가진 과일인 로카카카 2개를 발견했습니다.", ICON_TALENT, ItemSpriteSheet.RO1, true, 50, false),
			new DeckEventDef(AVDOL_GHOST, "무함마드 압둘의 유령", "나도 한때 이 카이로 사막을 탐험했지만, 어느 스탠드의 습격으로 목숨을 잃었다..\n\n이제 난 이곳에 갇혀 복수를 이루기 전까진 떠날 수 없지..\n\n다음에 만나는 강적을 없애줘.. 그 놈이 내 목숨을 앗아갔으니..", ICON_TALENT, HOST_GHOST),
	};

	private static final int MODE_UPGRADE   = 0;
	private static final int MODE_REMOVE    = 1;
	private static final int MODE_TRANSFORM = 2;
	private static final int MODE_DUPLICATE = 3;

	private static final int CARD_W = 42;
	private static final int CARD_H = 54;
	private static final int CARD_GAP = 5;
	private static final int CARDS_PER_PAGE = 4;

	private int eventType;
	private int cardSelectionMode;
	private boolean resolved;

	@Override
	public void create() {
		inGameScene = true;
		super.create();

		DeckBuilderRun.initIfNeeded();
		eventType = eventType();
		DeckBuilderRun.lastEventType = eventType;
		switch (eventType) {
			case UPGRADE_SHRINE: cardSelectionMode = MODE_UPGRADE;    break;
			case PURIFIER:       cardSelectionMode = MODE_REMOVE;     break;
			case TRANSMOGRIFIER: cardSelectionMode = MODE_TRANSFORM;  break;
			case DUPLICATOR:     cardSelectionMode = MODE_DUPLICATE;  break;
			default:             cardSelectionMode = -1;              break;
		}
		saveRun();

		Music.INSTANCE.playTracks(SewerLevel.SEWER_TRACK_LIST, SewerLevel.SEWER_TRACK_CHANCES, false);

		int w = Camera.main.width;
		int h = Camera.main.height;
		RectF insets = getCommonInsets();
		float usableW = w - insets.left - insets.right;

		add(new ColorBlock(w, h, 0xFF10140F));
		addRunHud(insets);
		addExitButton(insets, w);

		DeckEventDef def = eventDef(eventType);
		RenderedTextBlock title = renderTextBlock(titleText(), 12);
		title.hardlight(Window.TITLE_COLOR);
		title.setPos(insets.left + (usableW - title.width()) / 2f, insets.top + 12);
		align(title);
		add(title);

		RenderedTextBlock body = renderTextBlock(descriptionText(), 7);
		body.hardlight(0xFFD8D1BD);
		body.maxWidth((int)Math.min(230, usableW - 16));
		body.setPos(insets.left + (usableW - body.width()) / 2f, title.bottom() + 14);
		align(body);
		add(body);

		boolean wide = usableW >= h * 1.15f;
		float buttonW = Math.min(wide ? 205 : 250, usableW - 22);
		boolean manyButtons = eventType == BLUE_WOMAN || eventType == CLERIC
				|| eventType == LIVING_WALL || eventType == BIG_FISH;
		int buttonCount = eventType == BLUE_WOMAN ? 4 : manyButtons ? 3 : 2;
		float buttonH = manyButtons ? 28 : 38;
		float buttonGap = manyButtons ? 6 : 8;
		float buttonX = insets.left + (usableW - buttonW) / 2f;
		float buttonStackH = buttonH * buttonCount + buttonGap * (buttonCount - 1);
		float prayY;

		Image host = def.host();
		if (wide) {
			host.scale.set(Math.max(3.2f, Math.min(4.5f, h / 65f)));
			float hostGap = 18;
			float groupW = Math.min(usableW - 20, host.width() + hostGap + buttonW);
			float groupX = insets.left + (usableW - groupW) / 2f;
			host.x = groupX;
			buttonX = Math.min(groupX + host.width() + hostGap, insets.left + usableW - buttonW - 8);

			float textRight = buttonX - 18;
			float textW = Math.min(230, textRight - insets.left - 8);
			if (textW >= 90) {
				body.maxWidth((int)textW);
				body.setPos(insets.left + Math.max(0, (textRight - insets.left - body.width()) / 2f), title.bottom() + 14);
				align(body);
			}

			float minButtonY = title.bottom() + 30;
			float maxButtonY = h - insets.bottom - buttonStackH - 14;
			if (maxButtonY < minButtonY && buttonCount >= 3) {
				buttonGap = Math.min(buttonGap, 4);
				float availableH = h - insets.bottom - minButtonY - 14;
				buttonH = Math.max(20, Math.min(buttonH, (availableH - buttonGap * (buttonCount - 1)) / buttonCount));
				buttonStackH = buttonH * buttonCount + buttonGap * (buttonCount - 1);
				maxButtonY = h - insets.bottom - buttonStackH - 14;
			}
			prayY = Math.max(minButtonY, maxButtonY);
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
		ArrayList<Integer> pool = new ArrayList<>();
		for (DeckEventDef def : EVENT_DEFS) {
			if (def.available()) pool.add(def.id);
		}
		pool.remove(Integer.valueOf(DeckBuilderRun.lastEventType));
		return pool.get((int) Math.floorMod(hash, pool.size()));
	}

	private String titleText() {
		return eventDef(eventType).title;
	}

	private String descriptionText() {
		return eventDef(eventType).description;
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
				cardSelectionMode == MODE_UPGRADE ? "강화할 카드 선택"
						: cardSelectionMode == MODE_REMOVE ? "제거할 카드 선택"
						: cardSelectionMode == MODE_DUPLICATE ? "복제할 카드 선택"
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
			DeckCard card = DeckCard.byCode(code);
			if (DeckCardPool.isQuest(card)) continue;
			if (cardSelectionMode == MODE_UPGRADE && DeckCard.upgrade(code) == code) continue;
			if ((cardSelectionMode == MODE_REMOVE || cardSelectionMode == MODE_TRANSFORM)
					&& card.hasKeyword(code, DeckCardKeyword.PERMANENT)) continue;
			choices.add(i);
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

		if (cardSelectionMode == MODE_UPGRADE) {
			RenderedTextBlock upgrade = renderTextBlock("강화 효과\n" + upgradePreviewText(cardCode), 6);
			upgrade.maxWidth(width - 14);
			upgrade.hardlight(0xFFD5F27A);
			upgrade.setPos(7, pos);
			win.add(upgrade);
			pos += (int)upgrade.height() + 8;
		}

		String confirmLabel = cardSelectionMode == MODE_UPGRADE ? "강화"
				: cardSelectionMode == MODE_DUPLICATE ? "복제"
				: cardSelectionMode == MODE_TRANSFORM ? "변환"
				: eventType == CLERIC ? "제거" : "제거";
		RedButton confirm = new RedButton(confirmLabel, 6) {
			@Override
			protected void onClick() {
				if (resolved) return;
				if (cardSelectionMode == MODE_UPGRADE) {
					DeckBuilderRun.upgradeCardAt(deckIndex);
				} else if (cardSelectionMode == MODE_REMOVE) {
					if (eventType == CLERIC) DeckBuilderRun.gold -= 50;
					DeckBuilderRun.removeCardAt(deckIndex);
				} else if (cardSelectionMode == MODE_DUPLICATE) {
					duplicateCard(deckIndex);
				} else {
					transformCard(deckIndex);
				}
				resolved = true;
				Sample.INSTANCE.play(cardSelectionMode == MODE_UPGRADE ? Assets.Sounds.READ : Assets.Sounds.BURNING);
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
		} else if (eventType == LABORATORY) {
			addLaboratoryButtons(buttonX, buttonW, prayY, buttonH, buttonGap);
		} else if (eventType == DUPLICATOR) {
			addDuplicatorButtons(buttonX, buttonW, prayY, buttonH, buttonGap);
		} else if (eventType == SHINING_LIGHT) {
			addShiningLightButtons(buttonX, buttonW, prayY, buttonH, buttonGap);
		} else if (eventType == CLERIC) {
			addClericButtons(buttonX, buttonW, prayY, buttonH, buttonGap);
		} else if (eventType == WORLD_OF_GOOP) {
			addWorldOfGoopButtons(buttonX, buttonW, prayY, buttonH, buttonGap);
		} else if (eventType == LIVING_WALL) {
			addLivingWallButtons(buttonX, buttonW, prayY, buttonH, buttonGap);
		} else if (eventType == BIG_FISH) {
			addBigFishButtons(buttonX, buttonW, prayY, buttonH, buttonGap);
		} else if (eventType == SHAPESHIFTER_FOREST) {
			addShapeshifterForestButtons(buttonX, buttonW, prayY, buttonH, buttonGap);
		} else if (eventType == UNREST_SITE) {
			addUnrestSiteButtons(buttonX, buttonW, prayY, buttonH, buttonGap);
		} else if (eventType == THIS_OR_THAT) {
			addThisOrThatButtons(buttonX, buttonW, prayY, buttonH, buttonGap);
		} else if (eventType == JUNGLE_MAZE_ADVENTURE) {
			addJungleMazeAdventureButtons(buttonX, buttonW, prayY, buttonH, buttonGap);
		} else if (eventType == AROMA_OF_CHAOS) {
			addAromaOfChaosButtons(buttonX, buttonW, prayY, buttonH, buttonGap);
		} else if (eventType == DOORS_OF_LIGHT_AND_DARK) {
			addDoorsOfLightAndDarkButtons(buttonX, buttonW, prayY, buttonH, buttonGap);
		} else if (eventType == MAUSOLEUM) {
			addMausoleumButtons(buttonX, buttonW, prayY, buttonH, buttonGap);
		} else if (eventType == WHISPERING_HOLLOW) {
			addWhisperingHollowButtons(buttonX, buttonW, prayY, buttonH, buttonGap);
		} else if (eventType == AVDOL_GHOST) {
			addAvdolGhostButtons(buttonX, buttonW, prayY, buttonH, buttonGap);
		} else {
			addCardEventButtons(buttonX, buttonW, prayY, buttonH, buttonGap);
		}
	}

	private void addCardEventButtons(float buttonX, float buttonW, float prayY, float buttonH, float buttonGap) {
		int accentColor = eventType == UPGRADE_SHRINE ? 0xFFD5F27A
				: eventType == PURIFIER ? 0xFF8FE6FF : 0xFFD478E8;
		EventChoiceButton pray = new EventChoiceButton("사용한다", actionText(), accentColor) {
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
		EventChoiceButton pray = new EventChoiceButton("일부 골드만 획득한다", "100 골드를 획득합니다.", 0xFFD5F27A) {
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

		EventChoiceButton blasphemy = new EventChoiceButton("함정을 밟고 모든 골드를 획득한다", "275 골드를 획득합니다. 저주 카드 '부식의 저주'를 받습니다.", 0xFFD4844A) {
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

	private void addLaboratoryButtons(float buttonX, float buttonW, float prayY, float buttonH, float buttonGap) {
		EventChoiceButton search = new EventChoiceButton("포션을 획득한다", "무작위 포션 3개를 획득합니다.", 0xFF7EE8A0) {
			@Override protected void onClick() {
				if (resolved) return;
				for (int i = 0; i < 3; i++) {
					DeckBuilderRun.addPotion(DeckPotionPolicy.randomPotion(DeckPotionPolicy.rollRarity()));
				}
				resolved = true;
				Sample.INSTANCE.play(Assets.Sounds.DRINK);
				leaveEvent();
			}
		};
		search.setRect(buttonX, prayY, buttonW, buttonH);
		add(search);

		EventChoiceButton leave = new EventChoiceButton("떠난다", "아무 일도 일어나지 않습니다.", 0xFFB8A77D) {
			@Override protected void onClick() { leaveEvent(); }
		};
		leave.setRect(buttonX, prayY + buttonH + buttonGap, buttonW, buttonH);
		add(leave);
	}

	private void addDuplicatorButtons(float buttonX, float buttonW, float prayY, float buttonH, float buttonGap) {
		EventChoiceButton duplicate = new EventChoiceButton("기도", "카드 한 장을 선택해 복제합니다.", 0xFF88AAFF) {
			@Override protected void onClick() {
				if (resolved) return;
				showCardSelection(0);
			}
		};
		duplicate.setRect(buttonX, prayY, buttonW, buttonH);
		add(duplicate);

		EventChoiceButton leave = new EventChoiceButton("떠난다", "아무 일도 일어나지 않습니다.", 0xFFB8A77D) {
			@Override protected void onClick() { leaveEvent(); }
		};
		leave.setRect(buttonX, prayY + buttonH + buttonGap, buttonW, buttonH);
		add(leave);
	}

	private void addShiningLightButtons(float buttonX, float buttonW, float prayY, float buttonH, float buttonGap) {
		int hpLoss = Math.max(1, DeckBuilderRun.playerHT / 5);
		String enterDesc = "최대 HP의 20% (" + hpLoss + " HP)를 잃고, 덱의 카드 중 2장을 무작위로 강화합니다.";
		EventChoiceButton enter = new EventChoiceButton("낭떠러지로 떨어진다", enterDesc, 0xFFFFEE88) {
			@Override protected void onClick() {
				if (resolved) return;
				int loss = Math.max(1, DeckBuilderRun.playerHT / 5);
				DeckBuilderRun.playerHP = Math.max(0, DeckBuilderRun.playerHP - loss);
				upgradeRandomCards(2);
				resolved = true;
				Sample.INSTANCE.play(Assets.Sounds.FALLING);
				leaveEvent();
			}
		};
		enter.setRect(buttonX, prayY, buttonW, buttonH);
		add(enter);

		EventChoiceButton leave = new EventChoiceButton("떠난다", "아무 일도 일어나지 않습니다.", 0xFFB8A77D) {
			@Override protected void onClick() { leaveEvent(); }
		};
		leave.setRect(buttonX, prayY + buttonH + buttonGap, buttonW, buttonH);
		add(leave);
	}

	private void addClericButtons(float buttonX, float buttonW, float prayY, float buttonH, float buttonGap) {
		int healAmount = Math.max(1, DeckBuilderRun.playerHT / 4);
		String healDesc = "35 골드를 잃고 최대 HP의 25% (" + healAmount + " HP)를 회복합니다.";
		EventChoiceButton heal = new EventChoiceButton("플랑크톤 치유 (35골드)", healDesc, 0xFF7EE8A0) {
			@Override protected void onClick() {
				if (resolved) return;
				if (DeckBuilderRun.gold < 35) return;
				DeckBuilderRun.gold -= 35;
				int amount = Math.max(1, DeckBuilderRun.playerHT / 4);
				DeckBuilderRun.playerHP = Math.min(DeckBuilderRun.playerHT, DeckBuilderRun.playerHP + amount);
				resolved = true;
				Sample.INSTANCE.play(Assets.Sounds.DRINK);
				leaveEvent();
			}
		};
		heal.setRect(buttonX, prayY, buttonW, buttonH);
		add(heal);

		EventChoiceButton purify = new EventChoiceButton("F.F탄 (50골드)", "50 골드를 잃고 카드 한 장을 선택해 덱에서 제거합니다.", 0xFF8FE6FF) {
			@Override protected void onClick() {
				if (resolved) return;
				if (DeckBuilderRun.gold < 50) return;
				cardSelectionMode = MODE_REMOVE;
				showCardSelection(0);
			}
		};
		purify.setRect(buttonX, prayY + buttonH + buttonGap, buttonW, buttonH);
		add(purify);

		EventChoiceButton leave = new EventChoiceButton("떠나기", "아무 일도 일어나지 않습니다.", 0xFFB8A77D) {
			@Override protected void onClick() { leaveEvent(); }
		};
		leave.setRect(buttonX, prayY + (buttonH + buttonGap) * 2, buttonW, buttonH);
		add(leave);
	}

	private void addWorldOfGoopButtons(float buttonX, float buttonW, float prayY, float buttonH, float buttonGap) {
		EventChoiceButton grab = new EventChoiceButton("모든 골드를 챙긴다", "75 골드를 획득하고 11 HP를 잃습니다.", 0xFFD5F27A) {
			@Override protected void onClick() {
				if (resolved) return;
				DeckBuilderRun.gold += 75;
				DeckBuilderRun.playerHP = Math.max(0, DeckBuilderRun.playerHP - 11);
				resolved = true;
				Sample.INSTANCE.play(Assets.Sounds.GOLD);
				leaveEvent();
			}
		};
		grab.setRect(buttonX, prayY, buttonW, buttonH);
		add(grab);

		EventChoiceButton avoid = new EventChoiceButton("돌아간다", "20~50 골드를 잃습니다.", 0xFFB8A77D) {
			@Override protected void onClick() {
				if (resolved) return;
				int loss = 20 + Random.Int(31);
				DeckBuilderRun.gold = Math.max(0, DeckBuilderRun.gold - loss);
				resolved = true;
				Sample.INSTANCE.play(Assets.Sounds.EAT);
				leaveEvent();
			}
		};
		avoid.setRect(buttonX, prayY + buttonH + buttonGap, buttonW, buttonH);
		add(avoid);
	}

	private void addLivingWallButtons(float buttonX, float buttonW, float prayY, float buttonH, float buttonGap) {
		EventChoiceButton forget = new EventChoiceButton("권총의 유령", "카드 한 장을 선택해 제거합니다.", 0xFF8FE6FF) {
			@Override protected void onClick() {
				if (resolved) return;
				cardSelectionMode = MODE_REMOVE;
				showCardSelection(0);
			}
		};
		forget.setRect(buttonX, prayY, buttonW, buttonH);
		add(forget);

		EventChoiceButton change = new EventChoiceButton("유령 배낭", "카드 한 장을 선택해 무작위 카드로 변화시킵니다.", 0xFFD478E8) {
			@Override protected void onClick() {
				if (resolved) return;
				cardSelectionMode = MODE_TRANSFORM;
				showCardSelection(0);
			}
		};
		change.setRect(buttonX, prayY + buttonH + buttonGap, buttonW, buttonH);
		add(change);

		EventChoiceButton grow = new EventChoiceButton("피아노의 유령", "카드 한 장을 선택해 강화합니다.", 0xFFD5F27A) {
			@Override protected void onClick() {
				if (resolved) return;
				cardSelectionMode = MODE_UPGRADE;
				showCardSelection(0);
			}
		};
		grow.setRect(buttonX, prayY + (buttonH + buttonGap) * 2, buttonW, buttonH);
		add(grow);
	}

	private void addBigFishButtons(float buttonX, float buttonW, float prayY, float buttonH, float buttonGap) {
		int healAmount = Math.max(1, DeckBuilderRun.playerHT * 30 / 100);
		EventChoiceButton banana = new EventChoiceButton("직화구이 고기를 먹는다", "최대 HP의 30% (" + healAmount + " HP)만큼 체력을 회복합니다.", 0xFF7EE8A0) {
			@Override protected void onClick() {
				if (resolved) return;
				int amount = Math.max(1, DeckBuilderRun.playerHT * 30 / 100);
				DeckBuilderRun.playerHP = Math.min(DeckBuilderRun.playerHT, DeckBuilderRun.playerHP + amount);
				resolved = true;
				Sample.INSTANCE.play(Assets.Sounds.EAT);
				leaveEvent();
			}
		};
		banana.setRect(buttonX, prayY, buttonW, buttonH);
		add(banana);

		EventChoiceButton donut = new EventChoiceButton("마르게리타 피자를 먹는다", "최대 체력이 5 증가합니다.", 0xFFFFEE88) {
			@Override protected void onClick() {
				if (resolved) return;
				DeckBuilderRun.playerHT += 5;
				DeckBuilderRun.playerHP = Math.min(DeckBuilderRun.playerHT, DeckBuilderRun.playerHP + 5);
				resolved = true;
				Sample.INSTANCE.play(Assets.Sounds.EAT);
				leaveEvent();
			}
		};
		donut.setRect(buttonX, prayY + buttonH + buttonGap, buttonW, buttonH);
		add(donut);

		EventChoiceButton box = new EventChoiceButton("에코즈 ACT.1과 싸운다", "무작위 유물을 1개 획득합니다. 저주 카드 '부식의 저주'를 받습니다.", 0xFFD4844A) {
			@Override protected void onClick() {
				if (resolved) return;
				DeckRelic relic = DeckRelic.randomAvailable(DeckRewardPolicy.rollRelicRarity());
				if (relic != null) DeckBuilderRun.addRelic(relic);
				DeckBuilderRun.addCard(DeckCard.REGRET);
				resolved = true;
				Sample.INSTANCE.play(Assets.Sounds.CURSED);
				leaveEvent();
			}
		};
		box.setRect(buttonX, prayY + (buttonH + buttonGap) * 2, buttonW, buttonH);
		add(box);
	}

	private void addShapeshifterForestButtons(float buttonX, float buttonW, float prayY, float buttonH, float buttonGap) {
		EventChoiceButton crowd = new EventChoiceButton("사랑과 맺어지는 메이크", "모든 골드를 잃습니다. 덱의 카드 중 2장을 무작위로 변화시킵니다.", 0xFFD478E8) {
			@Override protected void onClick() {
				if (resolved) return;
				DeckBuilderRun.gold = 0;
				transformRandomCards(2);
				resolved = true;
				Sample.INSTANCE.play(Assets.Sounds.READ);
				leaveEvent();
			}
		};
		crowd.setRect(buttonX, prayY, buttonW, buttonH);
		add(crowd);

		EventChoiceButton loner = new EventChoiceButton("프로포즈받는 메이크", "최대 체력이 5 증가합니다.", 0xFF7EE8A0) {
			@Override protected void onClick() {
				if (resolved) return;
				DeckBuilderRun.playerHT += 5;
				DeckBuilderRun.playerHP = Math.min(DeckBuilderRun.playerHT, DeckBuilderRun.playerHP + 5);
				resolved = true;
				leaveEvent();
			}
		};
		loner.setRect(buttonX, prayY + buttonH + buttonGap, buttonW, buttonH);
		add(loner);
	}

	private void addUnrestSiteButtons(float buttonX, float buttonW, float prayY, float buttonH, float buttonGap) {
		EventChoiceButton rest = new EventChoiceButton("그래도 휴식한다", "체력을 모두 회복합니다. 저주 카드 '친화적인 저주'를 받습니다.", 0xFF7EE8A0) {
			@Override protected void onClick() {
				if (resolved) return;
				DeckBuilderRun.playerHP = DeckBuilderRun.playerHT;
				DeckBuilderRun.addCard(DeckCard.SLEEP_DEPRIVATION);
				resolved = true;
				Sample.INSTANCE.play(Assets.Sounds.DRINK);
				leaveEvent();
			}
		};
		rest.setRect(buttonX, prayY, buttonW, buttonH);
		add(rest);

		EventChoiceButton chop = new EventChoiceButton("적들과 싸운다", "최대 체력을 8 잃습니다. 무작위 유물을 1개 획득합니다.", 0xFFD5F27A) {
			@Override protected void onClick() {
				if (resolved) return;
				DeckBuilderRun.playerHT = Math.max(1, DeckBuilderRun.playerHT - 8);
				DeckBuilderRun.playerHP = Math.min(DeckBuilderRun.playerHT, DeckBuilderRun.playerHP);
				DeckRelic relic = DeckRelic.randomAvailable(DeckRewardPolicy.rollRelicRarity());
				if (relic != null) DeckBuilderRun.addRelic(relic);
				resolved = true;
				Sample.INSTANCE.play(Assets.Sounds.ALERT);
				leaveEvent();
			}
		};
		chop.setRect(buttonX, prayY + buttonH + buttonGap, buttonW, buttonH);
		add(chop);
	}

	private void addThisOrThatButtons(float buttonX, float buttonW, float prayY, float buttonH, float buttonGap) {
		EventChoiceButton thisChoice = new EventChoiceButton("돌아간다", "체력을 6 잃습니다. 골드를 41~68 얻습니다.", 0xFFD5F27A) {
			@Override protected void onClick() {
				if (resolved) return;
				DeckBuilderRun.playerHP = Math.max(0, DeckBuilderRun.playerHP - 6);
				DeckBuilderRun.gold += 41 + Random.Int(28);
				resolved = true;
				Sample.INSTANCE.play(Assets.Sounds.GOLD);
				leaveEvent();
			}
		};
		thisChoice.setRect(buttonX, prayY, buttonW, buttonH);
		add(thisChoice);

		EventChoiceButton thatChoice = new EventChoiceButton("미로를 돌파한다", "저주 카드 '희생의 저주'를 덱에 추가합니다. 무작위 유물을 1개 얻습니다.", 0xFFD4844A) {
			@Override protected void onClick() {
				if (resolved) return;
				DeckBuilderRun.addCard(DeckCard.CLUMSINESS);
				DeckRelic relic = DeckRelic.randomAvailable(DeckRewardPolicy.rollRelicRarity());
				if (relic != null) DeckBuilderRun.addRelic(relic);
				resolved = true;
				Sample.INSTANCE.play(Assets.Sounds.CURSED);
				leaveEvent();
			}
		};
		thatChoice.setRect(buttonX, prayY + buttonH + buttonGap, buttonW, buttonH);
		add(thatChoice);
	}

	private void addJungleMazeAdventureButtons(float buttonX, float buttonW, float prayY, float buttonH, float buttonGap) {
		EventChoiceButton alone = new EventChoiceButton("싸운다", "골드를 135~165 얻습니다. 체력을 18 잃습니다.", 0xFFD5F27A) {
			@Override protected void onClick() {
				if (resolved) return;
				DeckBuilderRun.gold += 135 + Random.Int(31);
				DeckBuilderRun.playerHP = Math.max(0, DeckBuilderRun.playerHP - 18);
				resolved = true;
				Sample.INSTANCE.play(Assets.Sounds.GOLD);
				leaveEvent();
			}
		};
		alone.setRect(buttonX, prayY, buttonW, buttonH);
		add(alone);

		EventChoiceButton together = new EventChoiceButton("돌아간다", "골드를 35~65 얻습니다.", 0xFF7EE8A0) {
			@Override protected void onClick() {
				if (resolved) return;
				DeckBuilderRun.gold += 35 + Random.Int(31);
				resolved = true;
				Sample.INSTANCE.play(Assets.Sounds.GOLD);
				leaveEvent();
			}
		};
		together.setRect(buttonX, prayY + buttonH + buttonGap, buttonW, buttonH);
		add(together);
	}

	private void addAromaOfChaosButtons(float buttonX, float buttonW, float prayY, float buttonH, float buttonGap) {
		EventChoiceButton submit = new EventChoiceButton("변환의 DISC를 사용한다", "덱에 있는 카드를 1장 변화시킵니다.", 0xFFD478E8) {
			@Override protected void onClick() {
				if (resolved) return;
				cardSelectionMode = MODE_TRANSFORM;
				showCardSelection(0);
			}
		};
		submit.setRect(buttonX, prayY, buttonW, buttonH);
		add(submit);

		EventChoiceButton focus = new EventChoiceButton("강화의 DISC를 사용한다", "덱에 있는 카드를 1장 강화합니다.", 0xFFD5F27A) {
			@Override protected void onClick() {
				if (resolved) return;
				cardSelectionMode = MODE_UPGRADE;
				showCardSelection(0);
			}
		};
		focus.setRect(buttonX, prayY + buttonH + buttonGap, buttonW, buttonH);
		add(focus);
	}

	private void addDoorsOfLightAndDarkButtons(float buttonX, float buttonW, float prayY, float buttonH, float buttonGap) {
		EventChoiceButton light = new EventChoiceButton("왼쪽 상자를 연다", "무작위 카드를 2장 강화합니다.", 0xFFFFEE88) {
			@Override protected void onClick() {
				if (resolved) return;
				upgradeRandomCards(2);
				resolved = true;
				Sample.INSTANCE.play(Assets.Sounds.READ);
				leaveEvent();
			}
		};
		light.setRect(buttonX, prayY, buttonW, buttonH);
		add(light);

		EventChoiceButton dark = new EventChoiceButton("오른쪽 상자를 연다", "덱에서 카드를 1장 제거합니다.", 0xFF8FE6FF) {
			@Override protected void onClick() {
				if (resolved) return;
				cardSelectionMode = MODE_REMOVE;
				showCardSelection(0);
			}
		};
		dark.setRect(buttonX, prayY + buttonH + buttonGap, buttonW, buttonH);
		add(dark);
	}

	private void addMausoleumButtons(float buttonX, float buttonW, float prayY, float buttonH, float buttonGap) {
		EventChoiceButton open = new EventChoiceButton("무덤을 건든다", "무작위 유물을 1개 획득합니다. 50% 확률로 저주 카드 '변위의 저주'를 받습니다.", 0xFFD4844A) {
			@Override protected void onClick() {
				if (resolved) return;
				DeckRelic relic = DeckRelic.randomAvailable(DeckRewardPolicy.rollRelicRarity());
				if (relic != null) DeckBuilderRun.addRelic(relic);
				if (Random.Int(2) == 0) {
					DeckBuilderRun.addCard(DeckCard.STRUGGLE);
					Sample.INSTANCE.play(Assets.Sounds.CURSED);
				}
				resolved = true;
				leaveEvent();
			}
		};
		open.setRect(buttonX, prayY, buttonW, buttonH);
		add(open);

		EventChoiceButton leave = new EventChoiceButton("떠나기", "아무 일도 일어나지 않습니다.", 0xFFB8A77D) {
			@Override protected void onClick() { leaveEvent(); }
		};
		leave.setRect(buttonX, prayY + buttonH + buttonGap, buttonW, buttonH);
		add(leave);
	}

	private void addWhisperingHollowButtons(float buttonX, float buttonW, float prayY, float buttonH, float buttonGap) {
		EventChoiceButton tradeGold = new EventChoiceButton("작은 로카카카를 먹는다", "골드를 50 잃습니다. 무작위 포션을 2개 획득합니다.", 0xFF8FE6FF) {
			@Override protected void onClick() {
				if (resolved) return;
				if (DeckBuilderRun.gold < 50) return;
				DeckBuilderRun.gold -= 50;
				for (int i = 0; i < 2; i++) {
					DeckBuilderRun.addPotion(DeckPotionPolicy.randomPotion(DeckPotionPolicy.rollRarity()));
				}
				resolved = true;
				Sample.INSTANCE.play(Assets.Sounds.DRINK);
				leaveEvent();
			}
		};
		tradeGold.setRect(buttonX, prayY, buttonW, buttonH);
		add(tradeGold);

		EventChoiceButton embrace = new EventChoiceButton("큰 로카카카를 먹는다", "체력을 9 잃습니다. 변화시킬 카드를 1장 선택합니다.", 0xFFD478E8) {
			@Override protected void onClick() {
				if (resolved) return;
				DeckBuilderRun.playerHP = Math.max(0, DeckBuilderRun.playerHP - 9);
				cardSelectionMode = MODE_TRANSFORM;
				showCardSelection(0);
			}
		};
		embrace.setRect(buttonX, prayY + buttonH + buttonGap, buttonW, buttonH);
		add(embrace);
	}

	private void addAvdolGhostButtons(float buttonX, float buttonW, float prayY, float buttonH, float buttonGap) {
		EventChoiceButton accept = new EventChoiceButton("퀘스트를 수락한다", "덱에 '압둘의 퀘스트'를 추가합니다.", 0xFF7EE8A0) {
			@Override protected void onClick() {
				if (resolved) return;
				DeckBuilderRun.addCard(DeckCard.ABDUL_QUEST);
				resolved = true;
				Sample.INSTANCE.play(Assets.Sounds.GHOST);
				leaveEvent();
			}
		};
		accept.setRect(buttonX, prayY, buttonW, buttonH);
		add(accept);

		EventChoiceButton pass = new EventChoiceButton("지나간다", "퀘스트를 받지 않고 떠납니다.", 0xFFB8A77D) {
			@Override protected void onClick() { leaveEvent(); }
		};
		pass.setRect(buttonX, prayY + buttonH + buttonGap, buttonW, buttonH);
		add(pass);
	}

	private void duplicateCard(int deckIndex) {
		int code = DeckBuilderRun.deck.get(deckIndex);
		DeckCard card = DeckCard.byCode(code);
		DeckBuilderRun.addCard(card);
	}

	private void upgradeRandomCards(int count) {
		ArrayList<Integer> upgradable = new ArrayList<>();
		for (int i = 0; i < DeckBuilderRun.deck.size(); i++) {
			int code = DeckBuilderRun.deck.get(i);
			DeckCard card = DeckCard.byCode(code);
			if (DeckCardPool.isStatusOrCurse(card)) continue;
			if (DeckCard.upgrade(code) == code) continue;
			upgradable.add(i);
		}
		for (int i = 0; i < count && !upgradable.isEmpty(); i++) {
			int pick = Random.Int(upgradable.size());
			int idx = upgradable.remove(pick);
			DeckBuilderRun.upgradeCardAt(idx);
		}
	}

	private void transformRandomCards(int count) {
		ArrayList<Integer> indices = new ArrayList<>();
		for (int i = 0; i < DeckBuilderRun.deck.size(); i++) {
			int code = DeckBuilderRun.deck.get(i);
			DeckCard card = DeckCard.byCode(code);
			if (DeckCardPool.isQuest(card) || card.hasKeyword(code, DeckCardKeyword.PERMANENT)) continue;
			indices.add(i);
		}
		for (int i = 0; i < count && !indices.isEmpty(); i++) {
			int pick = Random.Int(indices.size());
			int idx = indices.remove(pick);
			transformCard(idx);
		}
	}

	private void transformCard(int deckIndex) {
		DeckCard[] pool = DeckCard.rewardPool(DeckBuilderRun.heroClass(), false, false);
		int currentCode = DeckBuilderRun.deck.get(deckIndex);
		DeckCard current = DeckCard.byCode(currentCode);
		if (current.hasKeyword(currentCode, DeckCardKeyword.PERMANENT)) return;
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

	private static DeckEventDef eventDef(int id) {
		for (DeckEventDef def : EVENT_DEFS) {
			if (def.id == id) return def;
		}
		return EVENT_DEFS[0];
	}

	private static class DeckEventDef {
		private final int id;
		private final String title;
		private final String description;
		private final int icon;
		private final int host;
		private final int hostItemId;
		private final int minGold;
		private final boolean lowHpOnly;

		private DeckEventDef(int id, String title, String description, int icon, int host) {
			this(id, title, description, icon, host, -1, 0, false);
		}

		private DeckEventDef(int id, String title, String description, int icon, int host, int minGold, boolean lowHpOnly) {
			this(id, title, description, icon, host, -1, minGold, lowHpOnly);
		}

		private DeckEventDef(int id, String title, String description, int icon, int itemHost, boolean itemSprite) {
			this(id, title, description, icon, -1, itemHost, 0, false);
		}

		private DeckEventDef(int id, String title, String description, int icon, int itemHost, boolean itemSprite, int minGold, boolean lowHpOnly) {
			this(id, title, description, icon, -1, itemHost, minGold, lowHpOnly);
		}

		private DeckEventDef(int id, String title, String description, int icon, int host, int hostItemId, int minGold, boolean lowHpOnly) {
			this.id = id;
			this.title = title;
			this.description = description;
			this.icon = icon;
			this.host = host;
			this.hostItemId = hostItemId;
			this.minGold = minGold;
			this.lowHpOnly = lowHpOnly;
		}

		private boolean available() {
			if (DeckBuilderRun.gold < minGold) return false;
			return !lowHpOnly || DeckBuilderRun.playerHP < DeckBuilderRun.playerHT * 70 / 100;
		}

		private Image icon() {
			return icon == ICON_TALENT ? Icons.TALENT.get() : new ItemSprite(icon);
		}

		private Image host() {
			if (hostItemId >= 0) return new ItemSprite(hostItemId);
			switch (host) {
				case HOST_BLACKSMITH:    return new BlacksmithSprite();
				case HOST_ALCHEMIST:     return new AlchemistSprite();
				case HOST_WARLOCK:       return new EmporioSprite();
				case HOST_IMP:           return new ImpSprite();
				case HOST_BUTTERFLY:     return new So2Sprite();
				case HOST_ALBINO:        return new AlbinoSprite();
				case HOST_TRICKSTER:     return new ShopkeeperSprite();
				case HOST_BUTTERFLY2:    return new Butterfly2Sprite();
				case HOST_GEOMANCER:     return new So1Sprite();
				case HOST_CAUSTIC_SLIME: return new CausticSlimeSprite();
				case HOST_LASHER:        return new EmporioSprite();
				case HOST_PIRANHA:       return new Act1Sprite();
				case HOST_SLIME:         return new TsujiAyaSprite();
				default:                 return new GhostSprite();
			}
		}
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
