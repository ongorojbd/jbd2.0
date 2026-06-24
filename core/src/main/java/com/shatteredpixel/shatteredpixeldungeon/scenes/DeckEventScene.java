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
			new DeckEventDef(UPGRADE_SHRINE, "강화 성소", "강화 성소가 놓여 있다.\n\n기도하면 카드 한 장을 선택해 강화한다.", ICON_TALENT, HOST_BLACKSMITH),
			new DeckEventDef(PURIFIER, "정화 성소", "정화 성소가 놓여 있다.\n\n기도하면 카드 한 장을 선택해 제거한다.", ItemSpriteSheet.OBLIVION_SHARD, HOST_ALCHEMIST),
			new DeckEventDef(TRANSMOGRIFIER, "변환 성소", "변환 성소가 놓여 있다.\n\n기도하면 카드 한 장을 선택해 카드풀 내 무작위 카드로 변화시킨다.", ItemSpriteSheet.SCROLL_MANNAZ, HOST_WARLOCK),
			new DeckEventDef(GOLDEN_SHRINE, "황금 성소", "고대의 영혼을 기리는 공들인 성소가 놓여 있다.", ItemSpriteSheet.GOLD, HOST_IMP, 50, false),
			new DeckEventDef(BLUE_WOMAN, "파란 옷의 여자", "어두운 곳에서 시야가 밝아지자, 어떤 여성이 다짜고짜 외친다.\n\n\"포션 사세요, 당장!\"", ItemSpriteSheet.POTION_AZURE, HOST_BUTTERFLY, 50, false),
			new DeckEventDef(LABORATORY, "연구실", "먼지가 쌓인 연구실이 있다. 선반에는 각종 포션이 놓여 있다.", ItemSpriteSheet.POTION_IVORY, HOST_ALBINO),
			new DeckEventDef(DUPLICATOR, "복제 성소", "복제 성소가 놓여 있다.\n\n기도하면 카드 한 장을 선택해 복제한다.", ItemSpriteSheet.ARTIFACT_SPELLBOOK, HOST_TRICKSTER),
			new DeckEventDef(SHINING_LIGHT, "밝은 빛", "알 수 없는 밝은 빛이 앞을 가로막고 있다.", ItemSpriteSheet.ARTIFACT_CHALICE1, HOST_BUTTERFLY2),
			new DeckEventDef(CLERIC, "성직자", "낡은 제의를 걸친 성직자가 앉아 있다.\n\n\"도움이 필요한가?\"", ItemSpriteSheet.ARTIFACT_CHALICE2, HOST_GEOMANCER, 35, false),
			new DeckEventDef(WORLD_OF_GOOP, "끈적이 천지", "바닥이 온통 끈적이는 슬라임 덩어리로 가득 차 있다.", ItemSpriteSheet.DEWDROP, HOST_CAUSTIC_SLIME, 50, false),
			new DeckEventDef(LIVING_WALL, "살아있는 벽", "갑자기 살아있는 벽이 등장하여 길을 막는다.\n\n\"망각, 변화, 성장. 셋 중 하나를 고르라.\"", ItemSpriteSheet.SEED_EARTHROOT, HOST_LASHER),
			new DeckEventDef(BIG_FISH, "월척", "천장에서 바나나, 도넛, 상자가 내려와 있고 부스럭거리는 소리가 들린다. 아무래도 하나만 선택할 수 있는 듯하다.", ItemSpriteSheet.RATION, HOST_PIRANHA),
			new DeckEventDef(SHAPESHIFTER_FOREST, "변성체의 숲", "결정화된 나무들로 가득한 숲에서 변성체 무리가 당신을 반깁니다.\n\n구석에서 무리와 어울리지 못한 외톨이 한 마리가 불안해하고 있습니다.", ItemSpriteSheet.ARTIFACT_HORN1, HOST_SLIME, 100, false),
			new DeckEventDef(UNREST_SITE, "불안한 휴식 장소", "한적한 휴식 장소를 발견했습니다. 불을 피우자 불길이 옆으로 퍼져 기름진 숲을 향해 번져 갑니다.", ItemSpriteSheet.TORCH, HOST_GHOST, 0, true),
			new DeckEventDef(THIS_OR_THAT, "이거 아님 저거?", "근처의 구멍에서 갑자기, 보물이 담긴 수상한 자루와 명백히 저주받은 유물을 움켜진 손이 튀어나옵니다.\n\n\"이거... 아님 저거?\"\n날카롭게 긁어대는 목소리가 아래쪽에서 속삭입니다.", ItemSpriteSheet.GOLD, HOST_IMP),
			new DeckEventDef(JUNGLE_MAZE_ADVENTURE, "정글 미로 탐험", "당신은 공터에서 거대한 미로를 내려다보며 손짓하고 있는 오합지졸 모험가 무리를 만났습니다.\n\n함께 나아가면 더 수월하겠지만, 얻는 전리품은 나눠야 할 것입니다.", ItemSpriteSheet.MAP0, HOST_TRICKSTER),
			new DeckEventDef(AROMA_OF_CHAOS, "혼돈의 향기", "울창한 덤불을 헤치고 나와 공터에 다다른 당신은, 정체를 알 수 없는 그리움에 사로잡힙니다.\n\n꽃 향기와 썩은 냄새, 그리고 전혀 다른 어떤 향기가 한데 뒤섞여 풍겨옵니다.", ItemSpriteSheet.SEED_STARFLOWER, HOST_BUTTERFLY2),
			new DeckEventDef(DOORS_OF_LIGHT_AND_DARK, "빛과 어둠의 문", "방금 전까지만 해도 존재하지 않았던 출입구가 어느새 생겨나 있습니다.\n\n안으로 들어서자, 희미하게 빛나는 두 개의 문과 잘 차려입은 문지기가 보입니다.", ItemSpriteSheet.GOLDEN_KEY, HOST_WARLOCK),
			new DeckEventDef(MAUSOLEUM, "영묘", "검은 안개가 새어 나오는 관이 있다.", ItemSpriteSheet.TOMB, HOST_GHOST),
			new DeckEventDef(WHISPERING_HOLLOW, "속삭이는 골짜기", "당신은 죽은 나무들로 이뤄진 골짜기를 지나던 중, 우연히 뼈처럼 새하얀 색의 나무 한 그루를 발견합니다. 무언가를 보호하는 갈비뼈처럼 안쪽으로 휘어진 가지에는, 점토 장식이 매달려 있습니다.\n\n정말 소름끼치는 나무입니다. 나무는 속삭입니다.\n\n...거래하라.....", ItemSpriteSheet.SEED_EARTHROOT, HOST_LASHER, 50, false),
			new DeckEventDef(AVDOL_GHOST, "무함마드 압둘의 유령", "나도 한때 이 카이로 사막을 탐험했지만, 어느 스탠드의 습격으로 목숨을 잃었다..\n\n이제 난 이곳에 갇혀 복수를 이루기 전까진 떠날 수 없지..\n\n다음에 만나는 강적을 없애줘.. 그 놈이 내 목숨을 앗아갔으니..", ItemSpriteSheet.SCROLL_LAGUZ, HOST_GHOST),
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

		CharSprite host = def.host();
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
				: eventType == CLERIC ? "정화" : "제거";
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
				Sample.INSTANCE.play(cardSelectionMode == MODE_UPGRADE ? Assets.Sounds.EVOKE : Assets.Sounds.CURSED);
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

	private void addLaboratoryButtons(float buttonX, float buttonW, float prayY, float buttonH, float buttonGap) {
		EventChoiceButton search = new EventChoiceButton("포션을 좀 찾아본다", "무작위 포션 3개를 획득합니다.", 0xFF7EE8A0) {
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
		EventChoiceButton enter = new EventChoiceButton("들어간다", enterDesc, 0xFFFFEE88) {
			@Override protected void onClick() {
				if (resolved) return;
				int loss = Math.max(1, DeckBuilderRun.playerHT / 5);
				DeckBuilderRun.playerHP = Math.max(0, DeckBuilderRun.playerHP - loss);
				upgradeRandomCards(2);
				resolved = true;
				Sample.INSTANCE.play(Assets.Sounds.EVOKE);
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
		EventChoiceButton heal = new EventChoiceButton("회복 (35골드)", healDesc, 0xFF7EE8A0) {
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

		EventChoiceButton purify = new EventChoiceButton("정화 (50골드)", "50 골드를 잃고 카드 한 장을 선택해 덱에서 제거합니다.", 0xFF8FE6FF) {
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
		EventChoiceButton grab = new EventChoiceButton("골드를 챙긴다", "75 골드를 획득하고 11 HP를 잃습니다.", 0xFFD5F27A) {
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

		EventChoiceButton avoid = new EventChoiceButton("건드리지 않는다", "20~50 골드를 잃습니다.", 0xFFB8A77D) {
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
		EventChoiceButton forget = new EventChoiceButton("망각", "카드 한 장을 선택해 제거합니다.", 0xFF8FE6FF) {
			@Override protected void onClick() {
				if (resolved) return;
				cardSelectionMode = MODE_REMOVE;
				showCardSelection(0);
			}
		};
		forget.setRect(buttonX, prayY, buttonW, buttonH);
		add(forget);

		EventChoiceButton change = new EventChoiceButton("변화", "카드 한 장을 선택해 무작위 카드로 변화시킵니다.", 0xFFD478E8) {
			@Override protected void onClick() {
				if (resolved) return;
				cardSelectionMode = MODE_TRANSFORM;
				showCardSelection(0);
			}
		};
		change.setRect(buttonX, prayY + buttonH + buttonGap, buttonW, buttonH);
		add(change);

		EventChoiceButton grow = new EventChoiceButton("성장", "카드 한 장을 선택해 강화합니다.", 0xFFD5F27A) {
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
		EventChoiceButton banana = new EventChoiceButton("바나나", "최대 HP의 30% (" + healAmount + " HP)만큼 체력을 회복합니다.", 0xFF7EE8A0) {
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

		EventChoiceButton donut = new EventChoiceButton("도넛", "최대 체력이 5 증가합니다.", 0xFFFFEE88) {
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

		EventChoiceButton box = new EventChoiceButton("상자", "무작위 유물을 1개 획득합니다. 저주 카드 '후회'를 받습니다.", 0xFFD4844A) {
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
		EventChoiceButton crowd = new EventChoiceButton("무리", "모든 골드를 잃습니다. 덱의 카드 중 2장을 무작위로 변화시킵니다.", 0xFFD478E8) {
			@Override protected void onClick() {
				if (resolved) return;
				DeckBuilderRun.gold = 0;
				transformRandomCards(2);
				resolved = true;
				Sample.INSTANCE.play(Assets.Sounds.CURSED);
				leaveEvent();
			}
		};
		crowd.setRect(buttonX, prayY, buttonW, buttonH);
		add(crowd);

		EventChoiceButton loner = new EventChoiceButton("외톨이", "최대 체력이 5 증가합니다.", 0xFF7EE8A0) {
			@Override protected void onClick() {
				if (resolved) return;
				DeckBuilderRun.playerHT += 5;
				DeckBuilderRun.playerHP = Math.min(DeckBuilderRun.playerHT, DeckBuilderRun.playerHP + 5);
				resolved = true;
				Sample.INSTANCE.play(Assets.Sounds.EVOKE);
				leaveEvent();
			}
		};
		loner.setRect(buttonX, prayY + buttonH + buttonGap, buttonW, buttonH);
		add(loner);
	}

	private void addUnrestSiteButtons(float buttonX, float buttonW, float prayY, float buttonH, float buttonGap) {
		EventChoiceButton rest = new EventChoiceButton("그래도 휴식한다", "체력을 모두 회복합니다. 저주 카드 '수면 부족'을 받습니다.", 0xFF7EE8A0) {
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

		EventChoiceButton chop = new EventChoiceButton("나무들을 베어낸다", "최대 체력을 8 잃습니다. 무작위 유물을 1개 획득합니다.", 0xFFD5F27A) {
			@Override protected void onClick() {
				if (resolved) return;
				DeckBuilderRun.playerHT = Math.max(1, DeckBuilderRun.playerHT - 8);
				DeckBuilderRun.playerHP = Math.min(DeckBuilderRun.playerHT, DeckBuilderRun.playerHP);
				DeckRelic relic = DeckRelic.randomAvailable(DeckRewardPolicy.rollRelicRarity());
				if (relic != null) DeckBuilderRun.addRelic(relic);
				resolved = true;
				Sample.INSTANCE.play(Assets.Sounds.EVOKE);
				leaveEvent();
			}
		};
		chop.setRect(buttonX, prayY + buttonH + buttonGap, buttonW, buttonH);
		add(chop);
	}

	private void addThisOrThatButtons(float buttonX, float buttonW, float prayY, float buttonH, float buttonGap) {
		EventChoiceButton thisChoice = new EventChoiceButton("이거", "체력을 6 잃습니다. 골드를 41~68 얻습니다.", 0xFFD5F27A) {
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

		EventChoiceButton thatChoice = new EventChoiceButton("저거", "저주 카드 '서투름'을 덱에 추가합니다. 무작위 유물을 1개 얻습니다.", 0xFFD4844A) {
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
		EventChoiceButton alone = new EventChoiceButton("홀로 탐색한다", "골드를 135~165 얻습니다. 체력을 18 잃습니다.", 0xFFD5F27A) {
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

		EventChoiceButton together = new EventChoiceButton("협력한다", "골드를 35~65 얻습니다.", 0xFF7EE8A0) {
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
		EventChoiceButton submit = new EventChoiceButton("향기에 몸을 맡긴다", "덱에 있는 카드를 1장 변화시킵니다.", 0xFFD478E8) {
			@Override protected void onClick() {
				if (resolved) return;
				cardSelectionMode = MODE_TRANSFORM;
				showCardSelection(0);
			}
		};
		submit.setRect(buttonX, prayY, buttonW, buttonH);
		add(submit);

		EventChoiceButton focus = new EventChoiceButton("정신을 붙잡는다", "덱에 있는 카드를 1장 강화합니다.", 0xFFD5F27A) {
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
		EventChoiceButton light = new EventChoiceButton("빛의 문", "무작위 카드를 2장 강화합니다.", 0xFFFFEE88) {
			@Override protected void onClick() {
				if (resolved) return;
				upgradeRandomCards(2);
				resolved = true;
				Sample.INSTANCE.play(Assets.Sounds.EVOKE);
				leaveEvent();
			}
		};
		light.setRect(buttonX, prayY, buttonW, buttonH);
		add(light);

		EventChoiceButton dark = new EventChoiceButton("어둠의 문", "덱에서 카드를 1장 제거합니다.", 0xFF8FE6FF) {
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
		EventChoiceButton open = new EventChoiceButton("열어본다", "무작위 유물을 1개 획득합니다. 50% 확률로 저주 카드 '몸부림'을 받습니다.", 0xFFD4844A) {
			@Override protected void onClick() {
				if (resolved) return;
				DeckRelic relic = DeckRelic.randomAvailable(DeckRewardPolicy.rollRelicRarity());
				if (relic != null) DeckBuilderRun.addRelic(relic);
				if (Random.Int(2) == 0) DeckBuilderRun.addCard(DeckCard.STRUGGLE);
				resolved = true;
				Sample.INSTANCE.play(Assets.Sounds.CURSED);
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
		EventChoiceButton tradeGold = new EventChoiceButton("골드를 거래한다", "골드를 50 잃습니다. 무작위 포션을 2개 생성합니다.", 0xFF8FE6FF) {
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

		EventChoiceButton embrace = new EventChoiceButton("나무를 끌어안는다", "체력을 9 잃습니다. 변화시킬 카드를 1장 선택합니다.", 0xFFD478E8) {
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
				Sample.INSTANCE.play(Assets.Sounds.CURSED);
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
		private final int minGold;
		private final boolean lowHpOnly;

		private DeckEventDef(int id, String title, String description, int icon, int host) {
			this(id, title, description, icon, host, 0, false);
		}

		private DeckEventDef(int id, String title, String description, int icon, int host, int minGold, boolean lowHpOnly) {
			this.id = id;
			this.title = title;
			this.description = description;
			this.icon = icon;
			this.host = host;
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

		private CharSprite host() {
			switch (host) {
				case HOST_BLACKSMITH:    return new BlacksmithSprite();
				case HOST_ALCHEMIST:     return new AlchemistSprite();
				case HOST_WARLOCK:       return new WarlockSprite();
				case HOST_IMP:           return new ImpSprite();
				case HOST_BUTTERFLY:     return new ButterflySprite();
				case HOST_ALBINO:        return new AlbinoSprite();
				case HOST_TRICKSTER:     return new GnollTricksterSprite();
				case HOST_BUTTERFLY2:    return new Butterfly2Sprite();
				case HOST_GEOMANCER:     return new GnollGeomancerSprite();
				case HOST_CAUSTIC_SLIME: return new CausticSlimeSprite();
				case HOST_LASHER:        return new RotLasherSprite();
				case HOST_PIRANHA:       return new PiranhaSprite();
				case HOST_SLIME:         return new SlimeSprite();
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
