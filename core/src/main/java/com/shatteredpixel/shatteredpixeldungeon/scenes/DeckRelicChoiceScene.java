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
import com.shatteredpixel.shatteredpixeldungeon.deckbuilder.DeckCard;
import com.shatteredpixel.shatteredpixeldungeon.deckbuilder.DeckCardCode;
import com.shatteredpixel.shatteredpixeldungeon.deckbuilder.DeckCardKeyword;
import com.shatteredpixel.shatteredpixeldungeon.deckbuilder.DeckCardPool;
import com.shatteredpixel.shatteredpixeldungeon.deckbuilder.DeckRelic;
import com.shatteredpixel.shatteredpixeldungeon.deckbuilder.DeckRewardPolicy;
import com.shatteredpixel.shatteredpixeldungeon.levels.SewerLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.shatteredpixel.shatteredpixeldungeon.tiles.TerrainFeaturesTilemap;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DArbySprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.Button;
import com.shatteredpixel.shatteredpixeldungeon.ui.IconButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndMessage;
import com.shatteredpixel.shatteredpixeldungeon.deckbuilder.DeckCardRarity;
import com.shatteredpixel.shatteredpixeldungeon.deckbuilder.DeckCardText;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIcon;
import com.shatteredpixel.shatteredpixeldungeon.ui.TalentIcon;
import com.watabou.noosa.Camera;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;
import com.watabou.utils.RectF;

import java.util.ArrayList;

import java.io.IOException;

public class DeckRelicChoiceScene extends PixelScene {

	private static final int CARD_W = 42;
	private static final int CARD_H = 54;
	private static final int CARD_GAP = 5;
	private static final int CARDS_PER_PAGE = 4;

	private boolean choosing;

	@Override
	public void create() {
		inGameScene = true;
		super.create();

		DeckBuilderRun.initIfNeeded();

		// Relic already chosen but pending events weren't resolved (e.g., crash recovery)
		if (DeckBuilderRun.startingRelicChosen && !DeckBuilderRun.needsStartingRelicChoice()) {
			int w = Camera.main.width, h = Camera.main.height;
			RectF insets = getCommonInsets();
			add(new ColorBlock(w, h, 0xFF10140F));
			addRunHud(insets);
			addExitButton(insets, w);
			prepareFirstMapChoice();
			DeckBuilderMap.ensureReadyForNextChoice();
			choosing = true;
			processPendingRelicEvent();
			return;
		}

		DeckRelic[] choices = DeckBuilderRun.startingRelicChoices();
		saveRun();
		Music.INSTANCE.playTracks(SewerLevel.SEWER_TRACK_LIST, SewerLevel.SEWER_TRACK_CHANCES, false);
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
				addToFront(new WndMessage("아이템\n\n" + DeckBuilderRun.relicListText()));
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
		if (choosing) return;
		choosing = true;

		Sample.INSTANCE.play(Assets.Sounds.DA2);
		DeckBuilderRun.chooseStartingRelic(relic);
		prepareFirstMapChoice();
		DeckBuilderMap.ensureReadyForNextChoice();
		processPendingRelicEvent();
	}

	private void processPendingRelicEvent() {
		if (DeckBuilderRun.pendingCardTransform) {
			showDeckCardPicker(0, false, "변환할 카드 선택", false, false, new PickCallback() {
				@Override public void onPick(int deckIndex) {
					transformCard(deckIndex);
					DeckBuilderRun.pendingCardTransform = false;
					saveRun();
					processPendingRelicEvent();
				}
			});
			return;
		}
		if (DeckBuilderRun.pendingNeutralDiscover) {
			showNeutralDiscoverWindow();
			return;
		}
		if (DeckBuilderRun.pendingCardReward || DeckBuilderRun.pendingCardRewardCount > 0) {
			showCardRewardWindow();
			return;
		}
		if (DeckBuilderRun.pendingCardRemove) {
			showDeckCardPicker(0, false, "제거할 카드 선택", false, true, new PickCallback() {
				@Override public void onPick(int deckIndex) {
					DeckBuilderRun.removeCardAt(deckIndex);
					DeckBuilderRun.pendingCardRemove = false;
					saveRun();
					processPendingRelicEvent();
				}
			});
			return;
		}
		if (DeckBuilderRun.pendingCardRemoveCount > 0) {
			int remaining = DeckBuilderRun.pendingCardRemoveCount;
			String removeTitle = remaining > 1 ? "제거할 카드 선택 (" + remaining + "장 남음)" : "제거할 카드 선택";
			showDeckCardPicker(0, false, removeTitle, false, true, new PickCallback() {
				@Override public void onPick(int deckIndex) {
					DeckBuilderRun.removeCardAt(deckIndex);
					DeckBuilderRun.pendingCardRemoveCount--;
					saveRun();
					processPendingRelicEvent();
				}
			});
			return;
		}
		if (DeckBuilderRun.pendingCardUpgrade) {
			showDeckCardPicker(0, true, "강화할 카드 선택", true, true, new PickCallback() {
				@Override public void onPick(int deckIndex) {
					DeckBuilderRun.upgradeCardAt(deckIndex);
					DeckBuilderRun.pendingCardUpgrade = false;
					saveRun();
					processPendingRelicEvent();
				}
			});
			return;
		}
		if (DeckBuilderRun.pendingRareCardChoice > 0) {
			showRareCardChoiceWindow();
			return;
		}
		if (DeckBuilderRun.pendingRareNeutralCardChoice > 0) {
			showRareNeutralCardChoiceWindow();
			return;
		}
		if (DeckBuilderRun.pendingOtherClassCardReward > 0) {
			showOtherClassCardRewardWindow();
			return;
		}
		goToMap();
	}

	private void goToMap() {
		saveRun();
		Game.runOnRenderThread(new Callback() {
			@Override public void call() {
				Game.switchScene(DeckBuilderMapScene.class);
			}
		});
	}

	private interface PickCallback { void onPick(int deckIndex); }

	private void transformCard(int deckIndex) {
		DeckCard[] pool = DeckCard.rewardPool(DeckBuilderRun.heroClass(), false, false);
		int currentCode = DeckBuilderRun.deck.get(deckIndex);
		DeckCard current = DeckCard.byCode(currentCode);
		if (current.hasKeyword(currentCode, DeckCardKeyword.PERMANENT)) return;
		ArrayList<DeckCard> filtered = new ArrayList<>();
		for (DeckCard c : pool) {
			if (c != current) filtered.add(c);
		}
		if (filtered.isEmpty()) return;
		DeckCard newCard = filtered.get(Random.Int(filtered.size()));
		DeckBuilderRun.deck.set(deckIndex, newCard.code());
	}

	private void showDeckCardPicker(final int page, final boolean upgradableOnly, final String titleStr,
			final boolean isUpgrade, final boolean needsConfirm, final PickCallback onConfirm) {
		ArrayList<Integer> indices = new ArrayList<>();
		for (int i = 0; i < DeckBuilderRun.deck.size(); i++) {
			int code = DeckBuilderRun.deck.get(i);
			if (upgradableOnly && DeckCardCode.upgrade(code) == code) continue;
			if (!isUpgrade && DeckCard.byCode(code).hasKeyword(code, DeckCardKeyword.PERMANENT)) continue;
			indices.add(i);
		}
		if (indices.isEmpty()) {
			if (isUpgrade) {
				DeckBuilderRun.pendingCardUpgrade = false;
			} else {
				DeckBuilderRun.pendingCardTransform = false;
				DeckBuilderRun.pendingCardRemove = false;
				DeckBuilderRun.pendingCardRemoveCount = 0;
			}
			saveRun();
			processPendingRelicEvent();
			return;
		}

		final int total = indices.size();
		final int maxPage = Math.max(0, (total - 1) / CARDS_PER_PAGE);
		final int currentPage = Math.max(0, Math.min(page, maxPage));
		final int first = currentPage * CARDS_PER_PAGE;
		final int count = Math.min(CARDS_PER_PAGE, total - first);

		final Window win = new Window() {
			@Override public void onBackPressed() {}
		};
		int totalCardW = count * CARD_W + (count - 1) * CARD_GAP;
		int width = Math.max(196, totalCardW + 20);
		int pos = 7;

		RenderedTextBlock title = renderTextBlock(titleStr, 9);
		title.hardlight(Window.TITLE_COLOR);
		title.setPos((width - title.width()) / 2f, pos);
		win.add(title);
		pos += 17;

		String helpMsg = needsConfirm ? "카드를 선택하면 효과를 확인할 수 있습니다." : "변환할 카드를 선택합니다.";
		RenderedTextBlock help = renderTextBlock(helpMsg, 5);
		help.hardlight(0xFFAAAFA4);
		help.maxWidth(width - 18);
		help.setPos((width - help.width()) / 2f, pos);
		win.add(help);
		pos += (int) help.height() + 8;

		int startX = (width - totalCardW) / 2;
		for (int i = 0; i < count; i++) {
			final int deckIndex = indices.get(first + i);
			final int code = DeckBuilderRun.deck.get(deckIndex);
			CardChoiceButton btn = new CardChoiceButton(code) {
				@Override protected void onClick() {
					if (needsConfirm) {
						showDeckConfirmWindow(win, deckIndex, code, isUpgrade, onConfirm);
					} else {
						onConfirm.onPick(deckIndex);
						win.hide();
					}
				}
			};
			btn.setRect(startX + i * (CARD_W + CARD_GAP), pos, CARD_W, CARD_H);
			win.add(btn);
		}
		pos += CARD_H + 9;

		if (maxPage > 0) {
			RedButton prev = new RedButton("이전", 6) {
				@Override protected void onClick() {
					win.hide();
					showDeckCardPicker(currentPage - 1, upgradableOnly, titleStr, isUpgrade, needsConfirm, onConfirm);
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
				@Override protected void onClick() {
					win.hide();
					showDeckCardPicker(currentPage + 1, upgradableOnly, titleStr, isUpgrade, needsConfirm, onConfirm);
				}
			};
			next.enable(currentPage < maxPage);
			next.setRect(width - 68, pos, 58, 18);
			win.add(next);
			pos += 23;
		}

		win.resize(width, pos);
		addToFront(win);
	}

	private void showDeckConfirmWindow(final Window cardWin, final int deckIndex, final int cardCode,
			final boolean isUpgrade, final PickCallback onConfirm) {
		final Window win = new Window();
		final DeckCard card = DeckCard.byCode(cardCode);
		int width = 190;
		int pos = 7;

		RenderedTextBlock title = renderTextBlock(DeckCardText.detailTitle(card, cardCode), 8);
		title.hardlight(Window.TITLE_COLOR);
		title.maxWidth(width - 14);
		title.setPos((width - title.width()) / 2f, pos);
		win.add(title);
		pos += (int) title.height() + 8;

		RenderedTextBlock desc = renderTextBlock(DeckCardText.rulesAndKeywordText(card, cardCode), 6);
		desc.maxWidth(width - 14);
		desc.hardlight(0xFFD8D1BD);
		desc.setPos(7, pos);
		win.add(desc);
		pos += (int) desc.height() + 8;

		if (isUpgrade) {
			RenderedTextBlock upgradePreview = renderTextBlock("강화 효과\n" + DeckCardText.upgradePreviewText(cardCode), 6);
			upgradePreview.maxWidth(width - 14);
			upgradePreview.hardlight(0xFFD5F27A);
			upgradePreview.setPos(7, pos);
			win.add(upgradePreview);
			pos += (int) upgradePreview.height() + 8;
		}

		RedButton confirm = new RedButton(isUpgrade ? "강화" : "제거", 6) {
			@Override protected void onClick() {
				onConfirm.onPick(deckIndex);
				win.hide();
				cardWin.hide();
			}
		};
		confirm.setRect(7, pos, 82, 18);
		win.add(confirm);

		RedButton close = new RedButton("닫기", 6) {
			@Override protected void onClick() {
				win.hide();
			}
		};
		close.setRect(width - 89, pos, 82, 18);
		win.add(close);
		pos += 24;

		win.resize(width, pos);
		addToFront(win);
	}

	private void showRewardCardConfirmWindow(final Window cardWin, final DeckCard card,
			final String confirmLabel, final Callback onConfirm) {
		final Window win = new Window();
		int width = 190;
		int pos = 7;
		int cardCode = card.code();

		RenderedTextBlock title = renderTextBlock(DeckCardText.detailTitle(card, cardCode), 8);
		title.hardlight(Window.TITLE_COLOR);
		title.maxWidth(width - 14);
		title.setPos((width - title.width()) / 2f, pos);
		win.add(title);
		pos += (int) title.height() + 8;

		RenderedTextBlock desc = renderTextBlock(DeckCardText.rulesAndKeywordText(card, cardCode), 6);
		desc.maxWidth(width - 14);
		desc.hardlight(0xFFD8D1BD);
		desc.setPos(7, pos);
		win.add(desc);
		pos += (int) desc.height() + 8;

		RedButton confirm = new RedButton(confirmLabel, 6) {
			@Override protected void onClick() {
				onConfirm.call();
				win.hide();
				cardWin.hide();
			}
		};
		confirm.setRect(7, pos, 82, 18);
		win.add(confirm);

		RedButton close = new RedButton("닫기", 6) {
			@Override protected void onClick() {
				win.hide();
			}
		};
		close.setRect(width - 89, pos, 82, 18);
		win.add(close);
		pos += 24;

		win.resize(width, pos);
		addToFront(win);
	}

	private void showRareCardChoiceWindow() {
		ArrayList<DeckCard> pool = new ArrayList<>();
		for (DeckCard c : DeckCard.rewardPool()) {
			if (c.rarity == DeckCardRarity.RARE) pool.add(c);
		}
		for (int i = pool.size() - 1; i > 0; i--) {
			int j = Random.Int(i + 1);
			DeckCard tmp = pool.get(i); pool.set(i, pool.get(j)); pool.set(j, tmp);
		}
		final DeckCard[] choices = pool.subList(0, Math.min(3, pool.size())).toArray(new DeckCard[0]);

		final Window win = new Window() {
			@Override public void onBackPressed() {}
		};
		int cols = Math.min(3, choices.length);
		int cardW = 42, cardH = 54, gap = 8;
		int contentW = cols * cardW + (cols - 1) * gap;
		int width = Math.max(160, contentW + 20);
		int pos = 7;

		RenderedTextBlock title = renderTextBlock("희귀 카드 선택", 9);
		title.hardlight(Window.TITLE_COLOR);
		title.setPos((width - title.width()) / 2f, pos);
		win.add(title);
		pos += 17;

		RenderedTextBlock desc = renderTextBlock("카드를 선택하면 효과를 확인할 수 있습니다.", 5);
		desc.hardlight(0xFFAAAFA4);
		desc.maxWidth(width - 14);
		desc.setPos((width - desc.width()) / 2f, pos);
		win.add(desc);
		pos += (int) desc.height() + 8;

		int startX = (width - contentW) / 2;
		for (int i = 0; i < choices.length; i++) {
			final DeckCard card = choices[i];
			CardChoiceButton btn = new CardChoiceButton(card.code()) {
				@Override protected void onClick() {
					showRewardCardConfirmWindow(win, card, "선택", new Callback() {
						@Override public void call() {
							DeckBuilderRun.addCard(card);
							DeckBuilderRun.pendingRareCardChoice--;
							saveRun();
							processPendingRelicEvent();
						}
					});
				}
			};
			btn.setRect(startX + i * (cardW + gap), pos, cardW, cardH);
			win.add(btn);
		}
		pos += cardH + 8;
		win.resize(width, pos);
		addToFront(win);
	}

	private void showRareNeutralCardChoiceWindow() {
		ArrayList<DeckCard> pool = new ArrayList<>();
		for (DeckCard c : DeckCard.rewardPool()) {
			if (c.rarity == DeckCardRarity.RARE && DeckCardPool.isNeutralCard(c)) pool.add(c);
		}
		for (int i = pool.size() - 1; i > 0; i--) {
			int j = Random.Int(i + 1);
			DeckCard tmp = pool.get(i); pool.set(i, pool.get(j)); pool.set(j, tmp);
		}
		final DeckCard[] choices = pool.subList(0, Math.min(3, pool.size())).toArray(new DeckCard[0]);

		final Window win = new Window() {
			@Override public void onBackPressed() {}
		};
		int cols = Math.min(3, choices.length);
		int cardW = 42, cardH = 54, gap = 8;
		int contentW = cols * cardW + (cols - 1) * gap;
		int width = Math.max(160, contentW + 20);
		int pos = 7;

		RenderedTextBlock title = renderTextBlock("희귀 공용 카드 선택", 9);
		title.hardlight(Window.TITLE_COLOR);
		title.setPos((width - title.width()) / 2f, pos);
		win.add(title);
		pos += 17;

		RenderedTextBlock desc = renderTextBlock("카드를 선택하면 효과를 확인할 수 있습니다.", 5);
		desc.hardlight(0xFFAAAFA4);
		desc.maxWidth(width - 14);
		desc.setPos((width - desc.width()) / 2f, pos);
		win.add(desc);
		pos += (int) desc.height() + 8;

		int startX = (width - contentW) / 2;
		for (int i = 0; i < choices.length; i++) {
			final DeckCard card = choices[i];
			CardChoiceButton btn = new CardChoiceButton(card.code()) {
				@Override protected void onClick() {
					showRewardCardConfirmWindow(win, card, "선택", new Callback() {
						@Override public void call() {
							DeckBuilderRun.addCard(card);
							DeckBuilderRun.pendingRareNeutralCardChoice--;
							saveRun();
							processPendingRelicEvent();
						}
					});
				}
			};
			btn.setRect(startX + i * (cardW + gap), pos, cardW, cardH);
			win.add(btn);
		}
		pos += cardH + 8;
		win.resize(width, pos);
		addToFront(win);
	}

	private void showNeutralDiscoverWindow() {
		DeckCard[] pool = DeckCard.rewardPool(null, false, true);
		ArrayList<DeckCard> shuffled = new ArrayList<>();
		for (DeckCard c : pool) shuffled.add(c);
		for (int i = shuffled.size() - 1; i > 0; i--) {
			int j = Random.Int(i + 1);
			DeckCard tmp = shuffled.get(i);
			shuffled.set(i, shuffled.get(j));
			shuffled.set(j, tmp);
		}
		final DeckCard[] choices = new DeckCard[Math.min(2, shuffled.size())];
		for (int i = 0; i < choices.length; i++) choices[i] = shuffled.get(i);

		final Window win = new Window() {
			@Override public void onBackPressed() {}
		};
		int cardW = 42, cardH = 54, gap = 8;
		int contentW = choices.length * cardW + (choices.length - 1) * gap;
		int width = Math.max(140, contentW + 20);
		int pos = 7;

		RenderedTextBlock title = renderTextBlock("중립 카드 선택", 9);
		title.hardlight(Window.TITLE_COLOR);
		title.setPos((width - title.width()) / 2f, pos);
		win.add(title);
		pos += 17;

		RenderedTextBlock desc = renderTextBlock("카드를 선택하면 효과를 확인할 수 있습니다.", 5);
		desc.hardlight(0xFFAAAFA4);
		desc.maxWidth(width - 14);
		desc.setPos((width - desc.width()) / 2f, pos);
		win.add(desc);
		pos += (int) desc.height() + 8;

		int startX = (width - contentW) / 2;
		for (int i = 0; i < choices.length; i++) {
			final DeckCard card = choices[i];
			CardChoiceButton btn = new CardChoiceButton(card.code()) {
				@Override protected void onClick() {
					showRewardCardConfirmWindow(win, card, "선택", new Callback() {
						@Override public void call() {
							DeckBuilderRun.addCard(card);
							DeckBuilderRun.pendingNeutralDiscover = false;
							saveRun();
							processPendingRelicEvent();
						}
					});
				}
			};
			btn.setRect(startX + i * (cardW + gap), pos, cardW, cardH);
			win.add(btn);
		}
		pos += cardH + 8;
		win.resize(width, pos);
		addToFront(win);
	}

	private void showCardRewardWindow() {
		DeckRewardPolicy.CardReward reward = DeckRewardPolicy.rollCardChoices(
				DeckBuilderMap.COMBAT, DeckBuilderRun.cardRareOffset, DeckBuilderRun.heroClass());
		DeckBuilderRun.cardRareOffset = reward.nextCardRareOffset;
		final DeckCard[] choices = reward.choices;

		final Window win = new Window() {
			@Override public void onBackPressed() {}
		};
		int cols = Math.min(4, choices.length);
		int cardW = 42, cardH = 54, gap = 5;
		int contentW = cols * cardW + (cols - 1) * gap;
		int width = Math.max(180, contentW + 20);
		int pos = 7;

		RenderedTextBlock title = renderTextBlock("카드 보상", 9);
		title.hardlight(Window.TITLE_COLOR);
		title.setPos((width - title.width()) / 2f, pos);
		win.add(title);
		pos += 17;

		RenderedTextBlock desc = renderTextBlock("카드를 선택하면 효과를 확인할 수 있습니다.", 5);
		desc.hardlight(0xFFAAAFA4);
		desc.maxWidth(width - 14);
		desc.setPos((width - desc.width()) / 2f, pos);
		win.add(desc);
		pos += (int) desc.height() + 8;

		int startX = (width - contentW) / 2;
		for (int i = 0; i < choices.length; i++) {
			final DeckCard card = choices[i];
			CardChoiceButton btn = new CardChoiceButton(card.code()) {
				@Override protected void onClick() {
					showRewardCardConfirmWindow(win, card, "선택", new Callback() {
						@Override public void call() {
							DeckBuilderRun.addCard(card);
							DeckBuilderRun.consumeUpgradedCardReward();
							consumePendingCardReward();
							saveRun();
							processPendingRelicEvent();
						}
					});
				}
			};
			btn.setRect(startX + i * (cardW + gap), pos, cardW, cardH);
			win.add(btn);
		}
		pos += cardH + 8;

		RedButton skip = new RedButton("건너뛰기", 6) {
			@Override protected void onClick() {
				consumePendingCardReward();
				saveRun();
				win.hide();
				processPendingRelicEvent();
			}
		};
		skip.setRect((width - 80) / 2f, pos, 80, 16);
		win.add(skip);
		pos += 22;

		win.resize(width, pos);
		addToFront(win);
	}

	private void consumePendingCardReward() {
		if (DeckBuilderRun.pendingCardRewardCount > 0) {
			DeckBuilderRun.pendingCardRewardCount--;
		} else {
			DeckBuilderRun.pendingCardReward = false;
		}
	}

	private void showOtherClassCardRewardWindow() {
		com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass heroClass = DeckBuilderRun.heroClass();
		ArrayList<DeckCard> pool = new ArrayList<>();
		for (DeckCard c : DeckCard.values()) {
			if (DeckCardPool.isOtherClassRewardCard(c, heroClass)) pool.add(c);
		}
		for (int i = pool.size() - 1; i > 0; i--) {
			int j = Random.Int(i + 1);
			DeckCard tmp = pool.get(i); pool.set(i, pool.get(j)); pool.set(j, tmp);
		}
		int count = Math.min(4, pool.size());
		final DeckCard[] choices = pool.subList(0, count).toArray(new DeckCard[0]);

		final int remaining = DeckBuilderRun.pendingOtherClassCardReward;
		final Window win = new Window() {
			@Override public void onBackPressed() {}
		};
		int cols = Math.min(4, choices.length);
		int cardW = 42, cardH = 54, gap = 5;
		int contentW = cols * cardW + (cols - 1) * gap;
		int width = Math.max(180, contentW + 20);
		int pos = 7;

		RenderedTextBlock title = renderTextBlock("다른 캐릭터의 카드 보상 (" + remaining + "회 남음)", 8);
		title.hardlight(Window.TITLE_COLOR);
		title.maxWidth(width - 10);
		title.setPos((width - title.width()) / 2f, pos);
		win.add(title);
		pos += 17;

		RenderedTextBlock desc = renderTextBlock("카드를 선택하면 효과를 확인할 수 있습니다.", 5);
		desc.hardlight(0xFFAAAFA4);
		desc.maxWidth(width - 14);
		desc.setPos((width - desc.width()) / 2f, pos);
		win.add(desc);
		pos += (int) desc.height() + 8;

		int startX = (width - contentW) / 2;
		for (int i = 0; i < choices.length; i++) {
			final DeckCard card = choices[i];
			CardChoiceButton btn = new CardChoiceButton(card.code()) {
				@Override protected void onClick() {
					showRewardCardConfirmWindow(win, card, "선택", new Callback() {
						@Override public void call() {
							DeckBuilderRun.addCard(card);
							DeckBuilderRun.pendingOtherClassCardReward--;
							saveRun();
							processPendingRelicEvent();
						}
					});
				}
			};
			btn.setRect(startX + i * (cardW + gap), pos, cardW, cardH);
			win.add(btn);
		}
		pos += cardH + 8;

		RedButton skip = new RedButton("건너뛰기", 6) {
			@Override protected void onClick() {
				DeckBuilderRun.pendingOtherClassCardReward--;
				saveRun();
				win.hide();
				processPendingRelicEvent();
			}
		};
		skip.setRect((width - 80) / 2f, pos, 80, 16);
		win.add(skip);
		pos += 22;

		win.resize(width, pos);
		addToFront(win);
	}

	private void prepareFirstMapChoice() {
		Statistics.deckBuilderMapNode = DeckBuilderMap.NONE;
		Statistics.deckBuilderMapPath = -1;

		if (Dungeon.depth < 1) {
			Dungeon.depth = 1;
		}

		int targetDepth = DeckBuilderMap.targetDepthAfter(Dungeon.depth);
		if (DeckBuilderMapScene.curTransition != null
				&& DeckBuilderMapScene.curTransition.destDepth == targetDepth) {
			return;
		}
		if (Dungeon.level == null) {
			return;
		}

		LevelTransition transition = Dungeon.level.getTransition(LevelTransition.Type.REGULAR_EXIT);
		if (transition == null || transition.destDepth != targetDepth) {
			int cell = Dungeon.hero == null ? Dungeon.level.entrance() : Dungeon.hero.pos;
			transition = new LevelTransition(
					Dungeon.level,
					cell,
					LevelTransition.Type.REGULAR_EXIT,
					targetDepth,
					Dungeon.branch,
					LevelTransition.Type.REGULAR_ENTRANCE);
		}
		DeckBuilderMapScene.curTransition = transition;
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

	private class MiniCardButton extends Button {
		private final int cardCode;
		private ColorBlock shadow;
		private ColorBlock edge;
		private ColorBlock face;
		private ItemSprite art;
		private TalentIcon talentArt;
		private Image trapArt;
		private Image buffArt;
		private Image spriteArt;
		private RenderedTextBlock cost;
		private RenderedTextBlock titleText;

		MiniCardButton(int cardCode) { this.cardCode = cardCode; }

		@Override protected void createChildren() {
			super.createChildren();
			shadow = new ColorBlock(1, 1, 0xFF000000); shadow.am = 0.38f; add(shadow);
			edge = new ColorBlock(1, 1, 0xFFFFFFFF); add(edge);
			face = new ColorBlock(1, 1, 0xFF262626); add(face);
			cost = renderTextBlock(7); add(cost);
			titleText = renderTextBlock(5); add(titleText);
		}

		@Override protected void layout() {
			super.layout();
			DeckCard card = DeckCard.byCode(cardCode);
			shadow.x = x + 2; shadow.y = y + 2; shadow.size(width, height);
			edge.color(card.type.borderColor); edge.x = x; edge.y = y; edge.size(width, height); edge.am = 0.92f;
			face.color(DeckCardPool.isNeutralCard(card) || !card.reward ? card.rarity.faceColor : card.classFaceColor());
			face.x = x + 2; face.y = y + 2; face.size(width - 4, height - 4); face.am = 0.94f;
			if (card.talentIcon != null) {
				if (art != null) art.visible = false;
				if (trapArt != null) trapArt.visible = false;
				if (buffArt != null) buffArt.visible = false;
				if (spriteArt != null) spriteArt.visible = false;
				if (talentArt != null) remove(talentArt);
				talentArt = new TalentIcon(card.talentIcon);
				add(talentArt);
				talentArt.scale.set(0.95f);
				talentArt.x = x + (width - talentArt.width()) / 2f;
				talentArt.y = y + height * 0.30f;
				align(talentArt);
			} else if (card.trapIcon != null) {
				if (art != null) art.visible = false;
				if (talentArt != null) talentArt.visible = false;
				if (buffArt != null) buffArt.visible = false;
				if (spriteArt != null) spriteArt.visible = false;
				if (trapArt != null) remove(trapArt);
				try {
					trapArt = TerrainFeaturesTilemap.getTrapVisual(Reflection.newInstance(card.trapIcon));
				} catch (Exception ignored) { trapArt = null; }
				if (trapArt != null) {
					trapArt.scale.set(1.1f);
					trapArt.x = x + (width - trapArt.width()) / 2f;
					trapArt.y = y + height * 0.30f;
					align(trapArt);
					trapArt.visible = true;
					add(trapArt);
				}
			} else if (card.buffIconInt() >= 0) {
				if (art != null) art.visible = false;
				if (talentArt != null) talentArt.visible = false;
				if (trapArt != null) trapArt.visible = false;
				if (spriteArt != null) spriteArt.visible = false;
				if (buffArt != null) remove(buffArt);
				try {
					buffArt = new BuffIcon(card.buffIconInt(), true);
				} catch (Exception ignored) { buffArt = null; }
				if (buffArt != null) {
					buffArt.scale.set(1.1f);
					buffArt.x = x + (width - buffArt.width()) / 2f;
					buffArt.y = y + height * 0.30f;
					align(buffArt);
					buffArt.visible = true;
					add(buffArt);
				}
			} else if (card.spriteClass != null) {
				if (art != null) art.visible = false;
				if (talentArt != null) talentArt.visible = false;
				if (trapArt != null) trapArt.visible = false;
				if (buffArt != null) buffArt.visible = false;
				if (spriteArt != null) remove(spriteArt);
				try { spriteArt = Reflection.newInstance(card.spriteClass).forceIdling(); } catch (Exception ignored) { spriteArt = new Image(); }
				add(spriteArt);
				spriteArt.scale.set(1.0f);
				spriteArt.x = x + (width - spriteArt.width()) / 2f;
				spriteArt.y = y + height * 0.30f;
				align(spriteArt);
				spriteArt.visible = true;
			} else if (card.charSprite != null) {
				if (art != null) art.visible = false;
				if (talentArt != null) talentArt.visible = false;
				if (trapArt != null) trapArt.visible = false;
				if (buffArt != null) buffArt.visible = false;
				if (spriteArt == null) { spriteArt = new Image(); add(spriteArt); }
				spriteArt.texture(card.charSprite.tex);
				TextureFilm charFilm = new TextureFilm(spriteArt.texture, card.charSprite.w, card.charSprite.h);
				spriteArt.frame(charFilm.get(card.charSprite.frame));
				spriteArt.scale.set(card.charSprite.scale);
				spriteArt.x = x + (width - spriteArt.width()) / 2f;
				spriteArt.y = y + height * 0.30f;
				align(spriteArt);
				spriteArt.visible = true;
			} else {
				if (talentArt != null) talentArt.visible = false;
				if (trapArt != null) trapArt.visible = false;
				if (buffArt != null) buffArt.visible = false;
				if (spriteArt != null) spriteArt.visible = false;
				if (art == null) {
					art = new ItemSprite(card.icon());
					add(art);
				}
				art.visible = true;
				art.view(card.icon(), null); art.scale.set(1.1f);
				art.x = x + (width - art.width()) / 2f; art.y = y + height * 0.30f; align(art);
			}
			cost.text(String.valueOf(card.cost(cardCode))); cost.hardlight(0xFFFFD84D); cost.setPos(x + 3, y + 3);
			titleText.text(card.title(cardCode)); titleText.hardlight(0xFFFFFFFF); titleText.maxWidth((int) width - 5);
			titleText.setPos(x + (width - titleText.width()) / 2f, y + height - titleText.height() - 4);
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
		private Image trapArt;
		private Image buffArt;
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
			title.maxWidth((int) width - 15);
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
			typeLabel.maxWidth((int) width - 10);
			typeLabel.setPos(x + (width - typeLabel.width()) / 2f, y + height - typeLabel.height() - 6);
		}

		private void layoutArt(DeckCard card) {
			if (card.spriteClass != null) {
				if (art != null) art.visible = false;
				if (talentArt != null) talentArt.visible = false;
				if (trapArt != null) trapArt.visible = false;
				if (buffArt != null) buffArt.visible = false;
				remove(spriteArt);
				try { spriteArt = Reflection.newInstance(card.spriteClass).forceIdling(); } catch (Exception ignored) { spriteArt = new Image(); }
				add(spriteArt);
				spriteArt.visible = true;
				spriteArt.scale.set(1.2f);
				spriteArt.x = artPanel.x + (artPanel.width() - spriteArt.width()) / 2f;
				spriteArt.y = artPanel.y + (artPanel.height() - spriteArt.height()) / 2f;
				align(spriteArt);
			} else if (card.charSprite != null) {
				if (art != null) art.visible = false;
				if (talentArt != null) talentArt.visible = false;
				if (trapArt != null) trapArt.visible = false;
				if (buffArt != null) buffArt.visible = false;
				spriteArt.visible = true;
				spriteArt.texture(card.charSprite.tex);
				TextureFilm charFilm = new TextureFilm(spriteArt.texture, card.charSprite.w, card.charSprite.h);
				spriteArt.frame(charFilm.get(card.charSprite.frame));
				spriteArt.scale.set(card.charSprite.scale);
				spriteArt.x = artPanel.x + (artPanel.width() - spriteArt.width()) / 2f;
				spriteArt.y = artPanel.y + (artPanel.height() - spriteArt.height()) / 2f;
				align(spriteArt);
			} else if (card.talentIcon != null) {
				if (art != null) art.visible = false;
				if (trapArt != null) trapArt.visible = false;
				if (buffArt != null) buffArt.visible = false;
				spriteArt.visible = false;
				if (talentArt != null) remove(talentArt);
				talentArt = new TalentIcon(card.talentIcon);
				add(talentArt);
				talentArt.scale.set(1.15f);
				talentArt.x = artPanel.x + (artPanel.width() - talentArt.width()) / 2f;
				talentArt.y = artPanel.y + (artPanel.height() - talentArt.height()) / 2f;
				align(talentArt);
			} else if (card.trapIcon != null) {
				if (art != null) art.visible = false;
				if (talentArt != null) talentArt.visible = false;
				if (buffArt != null) buffArt.visible = false;
				spriteArt.visible = false;
				if (trapArt != null) remove(trapArt);
				try {
					trapArt = TerrainFeaturesTilemap.getTrapVisual(Reflection.newInstance(card.trapIcon));
				} catch (Exception ignored) { trapArt = null; }
				if (trapArt != null) {
					trapArt.scale.set(1.15f);
					trapArt.x = artPanel.x + (artPanel.width() - trapArt.width()) / 2f;
					trapArt.y = artPanel.y + (artPanel.height() - trapArt.height()) / 2f;
					align(trapArt);
					trapArt.visible = true;
					add(trapArt);
				}
			} else if (card.buffIconInt() >= 0) {
				if (art != null) art.visible = false;
				if (talentArt != null) talentArt.visible = false;
				if (trapArt != null) trapArt.visible = false;
				spriteArt.visible = false;
				if (buffArt != null) remove(buffArt);
				try {
					buffArt = new BuffIcon(card.buffIconInt(), true);
				} catch (Exception ignored) { buffArt = null; }
				if (buffArt != null) {
					buffArt.scale.set(1.15f);
					buffArt.x = artPanel.x + (artPanel.width() - buffArt.width()) / 2f;
					buffArt.y = artPanel.y + (artPanel.height() - buffArt.height()) / 2f;
					align(buffArt);
					buffArt.visible = true;
					add(buffArt);
				}
			} else if (card.charSprite != null) {
				if (art != null) art.visible = false;
				if (talentArt != null) talentArt.visible = false;
				if (trapArt != null) trapArt.visible = false;
				if (buffArt != null) buffArt.visible = false;
				spriteArt.visible = true;
				spriteArt.texture(card.charSprite.tex);
				TextureFilm charFilm = new TextureFilm(spriteArt.texture, card.charSprite.w, card.charSprite.h);
				spriteArt.frame(charFilm.get(card.charSprite.frame));
				spriteArt.scale.set(card.charSprite.scale);
				spriteArt.x = artPanel.x + (artPanel.width() - spriteArt.width()) / 2f;
				spriteArt.y = artPanel.y + (artPanel.height() - spriteArt.height()) / 2f;
				align(spriteArt);
			} else {
				if (talentArt != null) talentArt.visible = false;
				if (trapArt != null) trapArt.visible = false;
				if (buffArt != null) buffArt.visible = false;
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

	private class RelicChoiceButton extends Button {

		private final DeckRelic relic;
		private ColorBlock shadow;
		private ColorBlock bg;
		private ColorBlock accent;
		private ItemSprite icon;
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
			icon = new ItemSprite();
			add(icon);
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
			icon.view(relic.icon, null);
			icon.x = x + 8;
			icon.y = y + 6;
			title.text(relic.titleWithRarity());
			title.maxWidth((int)(width - 34));
			title.setPos(x + 28, y + 5);
			desc.text(relic.description);
			desc.maxWidth((int)(width - 16));
			desc.setPos(x + 8, Math.max(y + 24, Math.min(title.bottom() + 2, y + height - desc.height() - 4)));
		}

		@Override
		protected void onClick() {
			choose(relic);
		}
	}
}
