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
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.deckbuilder.DeckBuilderCombat;
import com.shatteredpixel.shatteredpixeldungeon.deckbuilder.DeckBuilderMap;
import com.shatteredpixel.shatteredpixeldungeon.deckbuilder.DeckBuilderRun;
import com.shatteredpixel.shatteredpixeldungeon.deckbuilder.DeckCard;
import com.shatteredpixel.shatteredpixeldungeon.deckbuilder.DeckCardKeyword;
import com.shatteredpixel.shatteredpixeldungeon.deckbuilder.DeckCardRarity;
import com.shatteredpixel.shatteredpixeldungeon.deckbuilder.DeckCardTarget;
import com.shatteredpixel.shatteredpixeldungeon.deckbuilder.DeckCombatEnemy;
import com.shatteredpixel.shatteredpixeldungeon.deckbuilder.DeckEnemy;
import com.shatteredpixel.shatteredpixeldungeon.deckbuilder.DeckPlayResult;
import com.shatteredpixel.shatteredpixeldungeon.deckbuilder.DeckPotion;
import com.shatteredpixel.shatteredpixeldungeon.deckbuilder.DeckRelic;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Sword;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GnollSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HeroSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.PucciSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.RatSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SnakeSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.StowerSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.WamuuSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.WraithSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIcon;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.Button;
import com.shatteredpixel.shatteredpixeldungeon.ui.IconButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.TalentIcon;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.windows.IconTitle;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndMessage;
import com.watabou.input.PointerEvent;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Camera;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.PointerArea;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.RectF;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class DeckBattleScene extends PixelScene {

	private int CARD_W;
	private int CARD_H;
	private int ACTOR_HP_W;
	private static final int ACTOR_HP_H = 5;
	private int ENERGY_ORB;
	private int counterW;
	private float cardGap;
	private float handY;

	private DeckBuilderCombat combat;
	private RenderedTextBlock playerStatus;
	private RenderedTextBlock enemyStatus;
	private RenderedTextBlock intentStatus;
	private RenderedTextBlock enemyRoster;
	private RenderedTextBlock pileStatus;
	private RenderedTextBlock deckCounter;
	private RenderedTextBlock discardCounter;
	private RenderedTextBlock logText;
	private RenderedTextBlock energyLabel;
	private ColorBlock playerHp;
	private ColorBlock enemyHp;
	private ColorBlock playerHpBg;
	private ColorBlock enemyHpBg;
	private ColorBlock playerShieldBar;
	private RenderedTextBlock playerShieldLabel;
	private DeckBuffButton playerStrengthBuff;
	private DeckBuffButton playerAgeDownBuff;
	private ColorBlock deckCounterBg;
	private ColorBlock discardCounterBg;
	private PointerArea deckCounterArea;
	private PointerArea discardCounterArea;
	private CharSprite playerSprite;
	private CharSprite enemySprite;
	private ArrayList<EnemyView> enemyViews = new ArrayList<>();
	private float playerBaseX;
	private float playerBaseY;
	private float enemyBaseX;
	private float enemyBaseY;
	private float playerHitTime;
	private float enemyHitTime;
	private float enemyAttackTime;
	private float playerGuardTime;
	private boolean combatLocked;
	private ArrayList<CardButton> cardButtons = new ArrayList<>();
	private ArrayList<BattleEffect> effects = new ArrayList<>();
	private CardInfoPopup cardInfo;
	private RedButton endTurn;
	private RedButton targetButton;
	private DeckRunHud runHud;
	private float spriteScale;
	private boolean rewardOpen;
	private boolean endingRun;

	@Override
	public void create() {
		inGameScene = true;
		super.create();

		boolean freshCombat = shouldStartFreshCombat();
		combat = DeckBuilderRun.combatForNode(Statistics.deckBuilderMapNode);
		try {
			Dungeon.saveAll();
		} catch (IOException e) {
			Game.reportException(e);
		}

		if (Statistics.deckBuilderMapNode == DeckBuilderMap.BOSS) {
			Music.INSTANCE.play(Assets.Music.SEWERS_BOSS, true);
		} else if (Dungeon.level != null) {
			Dungeon.level.playLevelMusic();
		}

		int w = Camera.main.width;
		int h = Camera.main.height;
		RectF insets = getCommonInsets();

		computeDimensions(w, h, insets);
		addBackground(w, h, insets);

		IconTitle title = new IconTitle(Icons.STAIRS.get(), titleText());
		title.setSize(220, 0);
		title.setPos(insets.left + (w - insets.left - insets.right - title.reqWidth()) / 2f, insets.top + 5);
		align(title);
		add(title);
		addRunHud(insets);
		addExitButton(insets, w);

		playerSprite = new HeroSprite();
		playerSprite.scale.set(spriteScale);
		add(playerSprite);

		createEnemyViews();

		playerStatus = renderTextBlock(8);
		playerStatus.hardlight(0xFFFFFFFF);
		add(playerStatus);
		enemyStatus = renderTextBlock(8);
		enemyStatus.hardlight(0xFFFFFFFF);
		enemyStatus.visible = false;
		add(enemyStatus);
		intentStatus = renderTextBlock(7);
		intentStatus.hardlight(0xFFFF5555);
		intentStatus.visible = false;
		add(intentStatus);
		enemyRoster = renderTextBlock(6);
		enemyRoster.hardlight(0xFFD8D1BD);
		add(enemyRoster);
		pileStatus = renderTextBlock(8);
		pileStatus.hardlight(0xFF8EEBFF);
		add(pileStatus);
		energyLabel = renderTextBlock(9);
		energyLabel.hardlight(0xFFFFD66B);
		add(energyLabel);
		deckCounterBg = new ColorBlock(1, 1, 0xFF15191C);
		add(deckCounterBg);
		deckCounter = renderTextBlock(8);
		deckCounter.hardlight(0xFF9EE6FF);
		add(deckCounter);
		discardCounterBg = new ColorBlock(1, 1, 0xFF1C1715);
		add(discardCounterBg);
		discardCounter = renderTextBlock(8);
		discardCounter.hardlight(0xFFFFC07A);
		add(discardCounter);

		deckCounterArea = new PointerArea(0, 0, 1, 1) {
			@Override
			protected void onClick(PointerEvent event) {
                showPileWindow("남은 카드 목록", combat.drawPile);
			}
		};
		add(deckCounterArea);
		discardCounterArea = new PointerArea(0, 0, 1, 1) {
			@Override
			protected void onClick(PointerEvent event) {
                showDiscardWindow();
			}
		};
		add(discardCounterArea);

		playerHpBg = new ColorBlock(1, 1, 0xFF3B1616);
		add(playerHpBg);
		playerHp = new ColorBlock(1, 1, 0xFF4DDA6A);
		add(playerHp);
		enemyHpBg = new ColorBlock(1, 1, 0xFF3B1616);
		enemyHpBg.visible = false;
		add(enemyHpBg);
		enemyHp = new ColorBlock(1, 1, 0xFFE04F45);
		enemyHp.visible = false;
		add(enemyHp);
		playerShieldBar = new ColorBlock(1, 1, 0xFF4A90D9);
		playerShieldBar.am = 0.88f;
		playerShieldBar.visible = false;
		add(playerShieldBar);
		playerShieldLabel = renderTextBlock(6);
		playerShieldLabel.hardlight(0xFF8EDBFF);
		playerShieldLabel.visible = false;
		add(playerShieldLabel);
        playerStrengthBuff = new DeckBuffButton(BuffIndicator.RAGE, "공격력", "공격 카드가 주는 피해가 이 수치만큼 증가합니다.");
		add(playerStrengthBuff);
		playerAgeDownBuff = new DeckBuffButton(BuffIndicator.WEAKNESS, "연령 저하", "공격 카드의 피해가 30% 감소합니다.");
		add(playerAgeDownBuff);

		logText = renderTextBlock(6);
		logText.maxWidth(w - (int)insets.left - (int)insets.right - 18);
		logText.hardlight(0xFFD8D1BD);
		add(logText);

		cardInfo = new CardInfoPopup();
		cardInfo.visible = false;
		add(cardInfo);

        endTurn = new RedButton("턴 종료", 7) {
			@Override
			protected void onClick() {
				if (combatLocked) return;
				if (startEnemyTurn()) return;
				int result = combat.endTurn();
				saveCombatState();
				spawnEnemyActions();

				if (combat.playerDead()) {
					Sample.INSTANCE.play(Assets.Sounds.DEATH);
                    log("Defeated. Returning to the deck map.");
					addEffect(new DelayedActionEffect(0.24f, new Runnable() {
						@Override
						public void run() {
							playDeath(playerSprite);
						}
					}));
					addEffect(new DelayedActionEffect(0.95f, new Runnable() {
						@Override
						public void run() {
							finishRunDeath();
						}
					}));
				} else {
                    log(result > 0 ? "Enemies dealt " + result + " damage." : "Blocked.");
					addEffect(new DelayedActionEffect(0.42f, new Runnable() {
						@Override
						public void run() {
							refresh();
						}
					}));
				}
			}
		};
		add(endTurn);
        targetButton = new RedButton("대상", 6) {
			@Override
			protected void onClick() {
				if (combatLocked) return;
				selectNextTarget();
			}
		};
		add(targetButton);

		layoutStatic(w, h, insets, title.bottom());
		refresh();
		if (freshCombat) {
			combatLocked = true;
			showBattleStartTitle();
		}
		fadeIn();
	}

	private boolean shouldStartFreshCombat() {
		DeckBuilderCombat current = DeckBuilderRun.currentCombat;
		return current == null
				|| current.nodeType != Statistics.deckBuilderMapNode
				|| current.depth != Math.max(1, Dungeon.depth)
				|| current.won()
				|| current.playerDead();
	}

	private void showBattleStartTitle() {
		Sample.INSTANCE.play(Assets.Sounds.DESCEND);
        showTitleBanner("전투 시작", "", 0xFFFFF27A, 0.95f, new Runnable() {
			@Override
			public void run() {
				showPlayerTurnTitle();
			}
		});
	}

	private void showPlayerTurnTitle() {
		Sample.INSTANCE.play(Assets.Sounds.ITEM);
        showTitleBanner("내 턴", combat.turn + "턴", 0xFF9EE6FF, 0.78f, new Runnable() {
			@Override
			public void run() {
				refresh();
			}
		});
	}

	private boolean startEnemyTurn() {
		combatLocked = true;
		hideCardInfo();
		Sample.INSTANCE.play(Assets.Sounds.ITEM);
        showTitleBanner("적 턴", "", 0xFFFF7474, 0.72f, new Runnable() {
			@Override
			public void run() {
				resolveEnemyTurn();
			}
		});
		return true;
	}

	private void resolveEnemyTurn() {
		int result = combat.endTurn();
		saveCombatState();
		spawnEnemyActions();

		if (combat.playerDead()) {
			Sample.INSTANCE.play(Assets.Sounds.DEATH);
            log("패배했습니다. 덱빌딩 맵으로 돌아갑니다.");
			addEffect(new DelayedActionEffect(0.24f, new Runnable() {
				@Override
				public void run() {
					playDeath(playerSprite);
				}
			}));
			addEffect(new DelayedActionEffect(0.95f, new Runnable() {
				@Override
				public void run() {
					finishRunDeath();
				}
			}));
		} else {
            log(result > 0 ? "적들이 총 " + result + " 피해를 입혔습니다." : "방어했습니다.");
			float delay = Math.max(0.36f, 0.62f + combat.lastEnemyActions.size() * 0.08f);
			addEffect(new DelayedActionEffect(delay, new Runnable() {
				@Override
				public void run() {
					showPlayerTurnTitle();
				}
			}));
		}
	}

	private void showTitleBanner(String title, String subtitle, int color, float duration, Runnable onDone) {
		addEffect(new TitleBannerEffect(title, subtitle, color, duration, onDone));
	}

	private void createEnemyViews() {
		for (int i = 0; i < combat.enemies.size(); i++) {
			EnemyView view = new EnemyView(combat.enemies.get(i), i);
			view.sprite = enemySprite(view.enemy.kind);
			view.sprite.scale.set(spriteScale);
			view.sprite.flipHorizontal = true;
			add(view.sprite);

			view.name = renderTextBlock(7);
			view.name.hardlight(0xFFFFFFFF);
			add(view.name);

			view.intent = renderTextBlock(6);
			view.intent.hardlight(0xFFFF5555);
			add(view.intent);

			view.hpBg = new ColorBlock(1, 1, 0xFF3B1616);
			add(view.hpBg);
			view.hp = new ColorBlock(1, 1, 0xFFE04F45);
			add(view.hp);
			view.shield = new ColorBlock(1, 1, 0xFF4A90D9);
			view.shield.am = 0.88f;
			view.shield.visible = false;
			add(view.shield);
			view.shieldLabel = renderTextBlock(6);
			view.shieldLabel.hardlight(0xFF8EDBFF);
			view.shieldLabel.visible = false;
			add(view.shieldLabel);
			view.targetMark = new ColorBlock(1, 1, 0xFFFFD66B);
			view.targetMark.am = 0.72f;
			add(view.targetMark);

			view.area = new PointerArea(0, 0, 1, 1) {
				@Override
				protected void onClick(PointerEvent event) {
					combat.setTarget(view.index);
					refresh();
				}
			};
			add(view.area);

            view.vulnerableBuff = new DeckBuffButton(BuffIndicator.VULNERABLE, "취약", "해당 대상이 받는 모든 공격 피해가 1.5배 증가합니다.");
			add(view.vulnerableBuff);
            view.strengthBuff = new DeckBuffButton(BuffIndicator.RAGE, "공격력", "공격 피해가 이 수치만큼 증가합니다.");
			add(view.strengthBuff);
            view.thornsBuff = new DeckBuffButton(BuffIndicator.THORNS, "반격", "공격 카드로 공격한 대상에게 피해를 되돌립니다.");
			add(view.thornsBuff);
			enemyViews.add(view);
		}
		if (!enemyViews.isEmpty()) {
			enemySprite = enemyViews.get(0).sprite;
		}
	}

	private void computeDimensions(int w, int h, RectF insets) {
		float usableW = w - insets.left - insets.right;

		// Energy orb size scales with available height
		if (h < 280) {
			ENERGY_ORB = 10;
		} else if (h < 380) {
			ENERGY_ORB = 14;
		} else {
			ENERGY_ORB = 20;
		}

		// Counter pill width flanking the hand, clamped to available space.
		counterW = (int) Math.min(88, Math.max(36, usableW * 0.19f));

		// Gap between cards: tighter on narrow screens
		cardGap = usableW < 240 ? 3f : 5f;

		// Card width: 5 cards must fit in the space between the two counter pills
		float handLeft = insets.left + counterW + 4;
		float handRight = w - insets.right - counterW - 4;
		float handWidth = Math.max(0, handRight - handLeft);
		CARD_W = (int) Math.min(72, Math.max(28, (handWidth - 4 * cardGap) / 5f));
		// Card height: at least CARD_W*1.2 for readability, but capped at 28% of screen height
		// so the combat area is never starved even on short mobile screens.
		CARD_H = Math.min((int)(h * 0.28f), (int)(CARD_W * 1.2f));

		// HP bar width: a bit wider than a card, capped at original 78
		ACTOR_HP_W = Math.min(78, Math.max(46, CARD_W + 6));

		// Scale sprites down on very small virtual screens so they fit the combat zone
		spriteScale = h < 240 ? 2f : (h < 320 ? 2.5f : 3f);
	}

	private void addBackground(int w, int h, RectF insets) {
		add(new ColorBlock(w, h, 0xFF141414));

		Image splash = new Image(Assets.Splashes.TENDENCY);
		float splashScale = Math.max(w / splash.width(), h / splash.height());
		splash.scale.set(splashScale);
		splash.x = (w - splash.width()) / 2f;
		splash.y = (h - splash.height()) / 2f;
		splash.am = 0.66f;
		add(splash);

		ColorBlock vignette = new ColorBlock(w, h, 0xFF000000);
		vignette.am = 0.44f;
		add(vignette);

		float stageX = insets.left + 6;
		float stageY = insets.top + 26;
		float handRailY = h - insets.bottom - CARD_H - 20;
		float stageW = w - insets.left - insets.right - 12;
		float stageH = Math.max(10, handRailY - stageY - 4);

		ColorBlock stage = new ColorBlock(stageW, stageH, 0xFF20221D);
		stage.x = stageX;
		stage.y = stageY;
		stage.am = 0.48f;
		add(stage);

		ColorBlock stageShade = new ColorBlock(stage.width, stage.height, 0xFF151611);
		stageShade.x = stage.x;
		stageShade.y = stage.y;
		stageShade.am = 0.22f;
		add(stageShade);

		ColorBlock handRail = new ColorBlock(w, CARD_H + 28, 0xFF1A1B1D);
		handRail.x = 0;
		handRail.y = handRailY;
		handRail.am = 0.86f;
		add(handRail);

		ColorBlock railTop = new ColorBlock(w, 2, 0xFFB08B45);
		railTop.x = 0;
		railTop.y = handRailY;
		railTop.am = 0.55f;
		add(railTop);
	}

	private void layoutStatic(int w, int h, RectF insets, float titleBottom) {
		float left = insets.left + 12;
		float right = w - insets.right - 12;
		float stageTop = titleBottom + 8;

		// Hand rail top; cards are drawn 12px below it (inside the rail)
		float handRailY = h - insets.bottom - CARD_H - 20;
		handY = handRailY + 12;

		// Log + end-turn button row sits between the stage and the hand rail
		// Use a slightly shorter button row on small screens to reclaim vertical space
		float logBtnH = h < 320 ? 18 : 22;
		float logAreaTop = handRailY - logBtnH - 4;

		// Energy orb row just below stage top
		float energyY = stageTop + 2;
		float energyAreaBottom = energyY + ENERGY_ORB + 4;

		// Sprite vertical placement:
		//   minCombatY: name label lives above the sprite; leave top clearance.
		//   maxCombatY: intent labels live below sprites; leave clearance above the log row.
		float spriteH = Math.max(20f, Math.max(playerSprite.height(), maxEnemySpriteHeight()));
		float minCombatY = energyAreaBottom + 16f;
		float maxCombatY = logAreaTop - spriteH - 20f;
		float combatY = maxCombatY <= minCombatY
				? minCombatY
				: (minCombatY + maxCombatY) / 2f - 15f;

		float centerOffset = Math.max(0, w * 0.12f);
		playerSprite.x = left + 8 + centerOffset;
		playerSprite.y = combatY;
		playerBaseX = playerSprite.x;
		playerBaseY = playerSprite.y;
		playerSprite.flipHorizontal = false;
		align(playerSprite);

		layoutEnemyViews(right, combatY);

		playerHpBg.size(ACTOR_HP_W, ACTOR_HP_H);
		playerHp.size(ACTOR_HP_W, ACTOR_HP_H);
		enemyHpBg.size(ACTOR_HP_W, ACTOR_HP_H);
		enemyHp.size(ACTOR_HP_W, ACTOR_HP_H);

		// End-turn button (right-aligned) and log text (left-aligned), same row
		// Cap width at about 22% of screen width; keep narrow mobile screens readable.
		int endTurnW = Math.min(w < 300 ? 64 : 76, (int)(w * 0.22f));
		int targetW = Math.min(38, Math.max(30, (int)(w * 0.09f)));
		float targetH = Math.max(14, logBtnH - 5);
		targetButton.setRect(right - targetW, stageTop + 2, targetW, targetH);
		logText.maxWidth((int)(right - left - endTurnW - 8));
		logText.setPos(left, logAreaTop + 4);

		// Deck / discard counter pills flanking the card hand
		deckCounterBg.x = insets.left + 2;
		deckCounterBg.y = handY + CARD_H / 2f;
		deckCounterBg.size(counterW, CARD_H / 2f);
		discardCounterBg.x = w - insets.right - counterW - 2;
		discardCounterBg.y = handY + CARD_H / 2f;
		discardCounterBg.size(counterW, CARD_H / 2f);

		endTurn.setRect(discardCounterBg.x, handY + (CARD_H / 2f - logBtnH) / 2f, discardCounterBg.width(), logBtnH);

		deckCounterArea.x = deckCounterBg.x;
		deckCounterArea.y = deckCounterBg.y;
		deckCounterArea.width = deckCounterBg.width();
		deckCounterArea.height = deckCounterBg.height();
		discardCounterArea.x = discardCounterBg.x;
		discardCounterArea.y = discardCounterBg.y;
		discardCounterArea.width = discardCounterBg.width();
		discardCounterArea.height = discardCounterBg.height();
	}

	private float maxEnemySpriteHeight() {
		float height = 0;
		for (EnemyView view : enemyViews) {
			height = Math.max(height, view.sprite.height());
		}
		return height;
	}

	private void layoutEnemyViews(float right, float combatY) {
		float centerOffset = Math.max(0, Camera.main.width * 0.12f);
		int count = Math.max(1, enemyViews.size());
		float spacing = count == 1 ? 0 : Math.min(50, Math.max(34, Camera.main.width * 0.08f));
		float groupW = (count - 1) * spacing;
		float groupRight = right - 18 - centerOffset;
		for (int i = 0; i < enemyViews.size(); i++) {
			EnemyView view = enemyViews.get(i);
			float cx = groupRight - groupW + i * spacing;
			view.sprite.x = cx - view.sprite.width() / 2f;
			view.sprite.y = combatY + (i % 2 == 0 ? 0 : 5);
			view.baseX = view.sprite.x;
			view.baseY = view.sprite.y;
			view.sprite.flipHorizontal = true;
			align(view.sprite);
		}
		EnemyView target = targetView();
		if (target != null) {
			enemySprite = target.sprite;
			enemyBaseX = target.baseX;
			enemyBaseY = target.baseY;
		}
	}

	private void refreshEnemyViews() {
		for (EnemyView view : enemyViews) {
			boolean alive = view.enemy.alive();
			boolean targeted = view.index == combat.targetIndex && alive;
			view.sprite.visible = alive;
			view.name.visible = alive;
			view.intent.visible = alive;
			view.hpBg.visible = alive;
			view.hp.visible = alive;
			view.area.active = alive;
			view.targetMark.visible = targeted;

			if (!alive) {
				view.vulnerableBuff.visible = false;
				view.strengthBuff.visible = false;
				view.thornsBuff.visible = false;
				view.shield.visible = false;
				view.shieldLabel.visible = false;
				continue;
			}
			
			view.vulnerableBuff.setStacks(view.enemy.vulnerable);
			view.strengthBuff.setStacks(view.enemy.strength);
			view.thornsBuff.setStacks(view.enemy.thorns);
			view.shield.visible = view.enemy.block > 0;
			if (view.enemy.block > 0) {
				view.shieldLabel.text(String.valueOf(view.enemy.block));
				view.shieldLabel.visible = true;
			} else {
				view.shieldLabel.visible = false;
			}
			
			view.name.text(view.enemy.name + "  " + view.enemy.hp + "/" + view.enemy.ht);
			view.name.hardlight(targeted ? Window.TITLE_COLOR : 0xFFFFFFFF);
			view.intent.text(intentText(view.enemy));
			view.intent.hardlight(view.enemy.intent < 0 && view.enemy.intent != DeckBuilderCombat.RESULT_SLIMY_INJECT ? 0xFFFFD66B
					: view.enemy.intent == DeckBuilderCombat.RESULT_SLIMY_INJECT ? 0xFF88FF88 : 0xFFFF5555);
			view.hp.size(ACTOR_HP_W * view.enemy.hp / (float)view.enemy.ht, ACTOR_HP_H);
			float hpFillWidth = view.hp.width();
			float rawShieldW = view.enemy.block * ACTOR_HP_W / (float)Math.max(1, view.enemy.ht);
			view.shield.size(Math.max(0, Math.min(rawShieldW, ACTOR_HP_W - hpFillWidth)), ACTOR_HP_H);
		}
	}

	private String intentText(DeckCombatEnemy enemy) {
		if (enemy.intent < DeckBuilderCombat.RESULT_SLIMY_INJECT) {
			switch (enemy.intent) {
				case DeckBuilderCombat.RESULT_AGE_DOWN:
                    return "예고: 연령 저하";
				case DeckBuilderCombat.RESULT_STRENGTH_7:
                    return "예고: 공격력 +7";
				case DeckBuilderCombat.RESULT_STRENGTH_2:
                    return "예고: 공격력 +2";
				case DeckBuilderCombat.RESULT_ATTACK_6_BLOCK_5:
                    return "예고: " + Math.max(0, 6 + enemy.strength) + " 피해 + 방어 5";
				case DeckBuilderCombat.RESULT_MASSACRE:
                    return "예고: 3x" + Math.max(0, 3 + enemy.strength) + " 피해";
				case DeckBuilderCombat.RESULT_TOWER_NEEDLE:
                    return "예고: 반격 +2";
			}
		}
		if (enemy.intent > 0 && enemy.strength != 0) {
            return "공격 예고: " + Math.max(0, enemy.intent + enemy.strength) + " 피해";
		}
		return enemy.intent == DeckBuilderCombat.RESULT_SLIMY_INJECT
                ? "예고: 점액투성이"
                : "공격 예고: " + enemy.intent + " 피해";
	}

	private void refresh() {
		if (rewardOpen) return;
		combatLocked = false;
		for (CardButton button : cardButtons) {
			button.destroy();
			remove(button);
		}
		cardButtons.clear();

		playerStatus.text(Dungeon.hero.heroClass.title() + "  " + DeckBuilderRun.playerHP + "/" + DeckBuilderRun.playerHT);
		DeckCombatEnemy target = combat.target();
		targetButton.visible = combat.enemies.size() > 1;
		refreshEnemyViews();
		enemyRoster.visible = false;
		pileStatus.visible = false;
        energyLabel.text("에너지: " + combat.energy + "/" + combat.maxEnergy);
		energyLabel.setPos(deckCounterBg.x + (deckCounterBg.width() - energyLabel.width()) / 2f,
				handY + (CARD_H / 2f - energyLabel.height()) / 2f);

		float textY = deckCounterBg.y + (deckCounterBg.height() - 8) / 2f;
        deckCounter.text("남은 카드: " + combat.drawPile.size());
		deckCounter.maxWidth(counterW - 4);
		deckCounter.setPos(deckCounterBg.x + (deckCounterBg.width() - deckCounter.width()) / 2f, textY);
        discardCounter.text("버린 카드: " + combat.discardPile.size());
		discardCounter.maxWidth(counterW - 4);
		discardCounter.setPos(discardCounterBg.x + (discardCounterBg.width() - discardCounter.width()) / 2f, textY);
		playerHp.size(ACTOR_HP_W * DeckBuilderRun.playerHP / (float)DeckBuilderRun.playerHT, ACTOR_HP_H);

		float hpFillWidth = playerHp.width();
		float rawShieldW = combat.block * ACTOR_HP_W / (float) Math.max(1, DeckBuilderRun.playerHT);
		float shieldW = Math.max(0, Math.min(rawShieldW, ACTOR_HP_W - hpFillWidth));
		playerShieldBar.size(shieldW, ACTOR_HP_H);
		playerShieldBar.visible = combat.block > 0;
		if (combat.block > 0) {
			playerShieldLabel.text(String.valueOf(combat.block));
			playerShieldLabel.visible = true;
		} else {
			playerShieldLabel.visible = false;
		}
		playerStrengthBuff.setStacks(combat.playerStrength + combat.playerTurnStrength);
		playerAgeDownBuff.setStacks(combat.playerDamageReduction > 0 ? combat.playerDamageReduction : 0);

		positionActorHud();

		int w = Camera.main.width;
		RectF insets = getCommonInsets();
		float handLeft = insets.left + counterW + 4;
		float handRight = w - insets.right - counterW - 4;
		float handWidth = Math.max(0, handRight - handLeft);
		int handCount = combat.hand.size();
		float step = handCount <= 1
				? 0
				: Math.min(CARD_W + cardGap, Math.max(8, (handWidth - CARD_W) / (handCount - 1)));
		float total = handCount <= 0 ? 0 : CARD_W + (handCount - 1) * step;
		float start = handLeft + (handRight - handLeft - total) / 2f;

		for (int i = 0; i < handCount; i++) {
			CardButton button = new CardButton(i);
			button.setRect(start + i * step, handY, CARD_W, CARD_H);
			cardButtons.add(button);
			add(button);
		}
	}

	@Override
	public void update() {
		super.update();

		float playerBob = (float)Math.sin(Game.timeTotal * 2.4f) * 2f;
		float enemyBob = (float)Math.sin(Game.timeTotal * 2.1f + 1.2f) * 2f;
		playerHitTime = Math.max(0, playerHitTime - Game.elapsed);
		playerGuardTime = Math.max(0, playerGuardTime - Game.elapsed);
		float playerShake = playerHitTime > 0 ? (float)Math.sin(playerHitTime * 92f) * 4f * playerHitTime / 0.22f : 0;
		float guardLift = playerGuardTime > 0 ? -3f * (float)Math.sin(playerGuardTime / 0.34f * Math.PI) : 0;
		playerSprite.x = playerBaseX + playerShake;
		playerSprite.y = playerBaseY + playerBob + guardLift;
		for (EnemyView view : enemyViews) {
			view.hitTime = Math.max(0, view.hitTime - Game.elapsed);
			view.attackTime = Math.max(0, view.attackTime - Game.elapsed);
			float enemyShake = view.hitTime > 0 ? (float)Math.sin(view.hitTime * 92f) * 4f * view.hitTime / 0.22f : 0;
			float enemyLunge = view.attackTime > 0 ? -10f * (float)Math.sin(view.attackTime / 0.22f * Math.PI) : 0;
			float bob = enemyBob + view.index * 0.7f;
			view.sprite.x = view.baseX + enemyShake + enemyLunge;
			view.sprite.y = view.baseY + bob;
		}
		positionActorHud();

		for (int i = effects.size() - 1; i >= 0; i--) {
			BattleEffect effect = effects.get(i);
			if (effect.done) {
				effect.killAndErase();
				effects.remove(i);
			}
		}
	}

	private void playCard(int index) {
		if (combatLocked || index < 0 || index >= combat.hand.size()) {
			return;
		}
		int cardCode = combat.hand.get(index);
		DeckCard card = DeckCard.byCode(cardCode);
		float startX = index < cardButtons.size() ? cardButtons.get(index).centerX() : playerCenterX();
		float startY = index < cardButtons.size() ? cardButtons.get(index).centerY() : playerCenterY();
		DeckPlayResult result = combat.play(index);
		if (!result.played) {
            log("에너지가 부족합니다.");
			return;
		}

		saveCombatState();
		combatLocked = true;

		if (card == DeckCard.SCORPION_THROW) {
			Sample.INSTANCE.play(Assets.Sounds.PLANT);
		} else if (card == DeckCard.SHIV) {
			Sword.giorno();
		}

		if (card == DeckCard.KNIFE_TRAP && result.hits.size() > 1) {
			// 칼날 함정: 단도를 하나씩 순차적으로 날림
			final float fx = startX;
			final float fy = startY;
			final ArrayList<DeckPlayResult.Hit> hits = result.hits;
			for (int i = 0; i < hits.size(); i++) {
				final DeckPlayResult.Hit hit = hits.get(i);
				final float delay = i * 0.18f;
				addEffect(new DelayedActionEffect(delay, new Runnable() {
					@Override
					public void run() {
						spawnCardAttack(DeckCard.SHIV, fx, fy, hit);
					}
				}));
			}
			final float totalDelay = hits.size() * 0.18f + 0.22f;
			log(card.title(cardCode) + ": " + cardRulesText(card, cardCode));
			if (combat.playerDead()) {
				addEffect(new DelayedActionEffect(totalDelay, new Runnable() {
					@Override
					public void run() {
						playDeath(playerSprite);
						finishRunDeath();
					}
				}));
			} else if (combat.won()) {
				addEffect(new DelayedActionEffect(totalDelay + 0.3f, new Runnable() {
					@Override
					public void run() {
						showReward();
					}
				}));
			} else {
				addEffect(new DelayedActionEffect(totalDelay, new Runnable() {
					@Override
					public void run() {
						refresh();
					}
				}));
			}
			return;
		}

		for (DeckPlayResult.Hit hit : result.hits) {
			if (hit.damage > 0) {
				spawnCardAttack(card, startX, startY, hit);
			}
			if (hit.vulnerable > 0) {
				EnemyView view = enemyView(hit.enemyIndex);
				if (view != null) {
                    spawnFloatingText("피해 증폭 +" + hit.vulnerable, enemyCenterX(view), enemyCenterY(view) - 24, 0xFFFFD66B);
				}
			}
		}
		if (result.block > 0) {
			spawnShieldEffect(playerCenterX(), playerCenterY(), "+" + result.block);
		}
		if (result.strength > 0) {
            spawnFloatingText("공격력 +" + result.strength, playerCenterX(), playerCenterY() - 24, 0xFFFFD84D);
		}
		log(card.title(cardCode) + ": " + cardRulesText(card, cardCode));
		if (combat.playerDead()) {
			playerHitTime = 0.22f;
			Sample.INSTANCE.play(Assets.Sounds.HIT);
            spawnFloatingText("가시", playerCenterX(), playerCenterY() - 18, 0xFFFF705A);
			addEffect(new DelayedActionEffect(0.45f, new Runnable() {
				@Override
				public void run() {
					playDeath(playerSprite);
					finishRunDeath();
				}
			}));
		} else if (combat.won()) {
			addEffect(new DelayedActionEffect(0.9f, new Runnable() {
				@Override
				public void run() {
					showReward();
				}
			}));
		} else if (result.damage > 0) {
			addEffect(new DelayedActionEffect(0.38f, new Runnable() {
				@Override
				public void run() {
					refresh();
				}
			}));
		} else {
			refresh();
		}
	}

	private String enemyCountText() {
		if (combat.enemies.size() <= 1) return "";
		return "  [" + (combat.targetIndex + 1) + "/" + combat.enemies.size() + "]";
	}

	private String enemyRosterText() {
		String text = "";
		for (int i = 0; i < combat.enemies.size(); i++) {
			DeckCombatEnemy enemy = combat.enemies.get(i);
			if (text.length() > 0) text += "  ";
			text += (i == combat.targetIndex ? ">" : "") + (i + 1) + "." + enemy.name + " " + enemy.hp + "/" + enemy.ht;
		}
		return text;
	}

	private void selectNextTarget() {
		if (combat.enemies.size() <= 1) return;
		for (int step = 1; step <= combat.enemies.size(); step++) {
			int index = (combat.targetIndex + step) % combat.enemies.size();
			if (combat.enemies.get(index).alive()) {
				combat.setTarget(index);
				saveCombatState();
				refresh();
				return;
			}
		}
	}

	private void saveCombatState() {
		try {
			Dungeon.saveAll();
		} catch (IOException e) {
			Game.reportException(e);
		}
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
		runHud = new DeckRunHud(new DeckRunHud.PotionHandler() {
			@Override
			public void onPotion(int slot, DeckPotion potion) {
				usePotion(slot, potion);
			}
		});
		runHud.setRect(insets.left + 4, insets.top + 4, 150, 20);
		add(runHud);
	}

	private void addExitButton(RectF insets, int w) {
		IconButton exit = new IconButton(Icons.EXIT.get()) {
			@Override
			protected void onClick() {
				Game.switchScene(TitleScene.class);
			}
		};
		exit.setRect(w - insets.right - 24, insets.top + 4, 20, 20);
		add(exit);
	}

	private void usePotion(int slot, DeckPotion potion) {
		if (combatLocked || rewardOpen || potion == null) return;
		DeckBuilderRun.removePotion(slot);
		if (runHud != null) runHud.refresh();

		switch (potion) {
			case HASTE:
				Sample.INSTANCE.play(Assets.Sounds.DRINK);
				combat.draw(3);
                log(potion.title + ": 카드를 3장 뽑았습니다.");
				saveCombatState();
				refresh();
				break;
			case FIRE:
				combatLocked = true;
				Sample.INSTANCE.play(Assets.Sounds.SHATTER);
				Sample.INSTANCE.play(Assets.Sounds.BURNING);
				for (int i = 0; i < combat.enemies.size(); i++) {
					DeckCombatEnemy enemy = combat.enemies.get(i);
					if (!enemy.alive()) continue;
					enemy.hp = Math.max(0, enemy.hp - 10);
					EnemyView view = enemyView(i);
					if (view != null) {
						addEffect(new ImpactEffect(enemyCenterX(view), enemyCenterY(view), 0xFFFF7A35));
						spawnFloatingText("-10", enemyCenterX(view), enemyCenterY(view) - 16, 0xFFFF705A);
						if (!enemy.alive()) playDeath(view.sprite);
					}
				}
                log(potion.title + ": 모든 적에게 10 피해를 입혔습니다.");
				saveCombatState();
				if (combat.won()) {
					addEffect(new DelayedActionEffect(0.55f, new Runnable() {
						@Override
						public void run() {
							showReward();
						}
					}));
				} else {
					addEffect(new DelayedActionEffect(0.38f, new Runnable() {
						@Override
						public void run() {
							refresh();
						}
					}));
				}
				break;
			case STRENGTH:
				Sample.INSTANCE.play(Assets.Sounds.DRINK);
				combat.playerStrength += 2;
                spawnFloatingText("공격력 +2", playerCenterX(), playerCenterY() - 24, 0xFFFFD84D);
                log(potion.title + ": 공격력을 2 얻었습니다.");
				saveCombatState();
				refresh();
				break;
		}
	}

	private float playerCenterX() {
		return playerSprite.x + playerSprite.width() / 2f;
	}

	private float playerCenterY() {
		return playerSprite.y + playerSprite.height() / 2f;
	}

	private float enemyCenterX() {
		EnemyView view = targetView();
		return view == null ? 0 : enemyCenterX(view);
	}

	private float enemyCenterY() {
		EnemyView view = targetView();
		return view == null ? 0 : enemyCenterY(view);
	}

	private float enemyCenterX(EnemyView view) {
		return view.sprite.x + view.sprite.width() / 2f;
	}

	private float enemyCenterY(EnemyView view) {
		return view.sprite.y + view.sprite.height() / 2f;
	}

	private void positionActorHud() {
		positionHudFor(playerSprite, playerStatus, playerHpBg, playerHp);
		positionEnemyHuds();
		playerShieldBar.x = playerHp.x + playerHp.width();
		playerShieldBar.y = playerHp.y;
		if (playerShieldBar.visible) {
			playerShieldLabel.setPos(playerHpBg.x + ACTOR_HP_W + 3, playerHp.y - 1);
		}
		if (playerStrengthBuff.visible) {
			float buffWidth = playerStrengthBuff.width() + (playerAgeDownBuff.visible ? playerAgeDownBuff.width() + 2 : 0);
			float buffX = playerHpBg.x + ACTOR_HP_W / 2f - buffWidth / 2f;
			playerStrengthBuff.setPos(buffX, playerStatus.top() - playerStrengthBuff.height() - 2);
			if (playerAgeDownBuff.visible) {
				playerAgeDownBuff.setPos(buffX + playerStrengthBuff.width() + 2, playerStrengthBuff.top());
			}
		} else if (playerAgeDownBuff.visible) {
			playerAgeDownBuff.setPos(playerHpBg.x + ACTOR_HP_W / 2f - playerAgeDownBuff.width() / 2f,
					playerStatus.top() - playerAgeDownBuff.height() - 2);
		}
	}

	private EnemyView targetView() {
		if (combat.targetIndex >= 0 && combat.targetIndex < enemyViews.size()) {
			return enemyViews.get(combat.targetIndex);
		}
		return enemyViews.isEmpty() ? null : enemyViews.get(0);
	}

	private EnemyView enemyView(int index) {
		return index >= 0 && index < enemyViews.size() ? enemyViews.get(index) : null;
	}

	private void positionEnemyHuds() {
		for (EnemyView view : enemyViews) {
			if (!view.sprite.visible) continue;
			float cx = view.sprite.x + view.sprite.width() / 2f;
			float nameY = view.sprite.y - 20;
			float top = nameY;
			float buffWidth = 0;
			if (view.vulnerableBuff.visible) buffWidth += view.vulnerableBuff.width() + 2;
			if (view.strengthBuff.visible) buffWidth += view.strengthBuff.width() + 2;
			if (view.thornsBuff.visible) buffWidth += view.thornsBuff.width() + 2;
			if (buffWidth > 0) {
				buffWidth -= 2;
				float buffX = cx - buffWidth / 2f;
				float buffY = nameY - view.vulnerableBuff.height() - 2;
				if (view.vulnerableBuff.visible) {
					view.vulnerableBuff.setPos(buffX, buffY);
					buffX += view.vulnerableBuff.width() + 2;
				}
				if (view.strengthBuff.visible) {
					view.strengthBuff.setPos(buffX, buffY);
					buffX += view.strengthBuff.width() + 2;
				}
				if (view.thornsBuff.visible) {
					view.thornsBuff.setPos(buffX, buffY);
				}
				top = buffY;
			}
			view.name.setPos(cx - view.name.width() / 2f, nameY);
			view.hpBg.x = cx - ACTOR_HP_W / 2f;
			view.hpBg.y = nameY + 12;
			view.hpBg.size(ACTOR_HP_W, ACTOR_HP_H);
			view.hp.x = view.hpBg.x;
			view.hp.y = view.hpBg.y;
			view.shield.x = view.hp.x + view.hp.width();
			view.shield.y = view.hp.y;
			if (view.shieldLabel.visible) {
				view.shieldLabel.setPos(view.hpBg.x + ACTOR_HP_W + 3, view.hp.y - 1);
			}
			
			float intentY = view.sprite.y + view.sprite.height() + 5;
			view.intent.setPos(cx - view.intent.width() / 2f, intentY);
			
			view.targetMark.x = cx - ACTOR_HP_W / 2f;
			view.targetMark.y = view.hpBg.y + ACTOR_HP_H + 1;
			view.targetMark.size(ACTOR_HP_W, 1);
			view.area.x = view.sprite.x - 6;
			view.area.y = top - 2;
			view.area.width = view.sprite.width() + 12;
			view.area.height = view.sprite.y + view.sprite.height() + 28 - view.area.y;
		}
	}

	private void positionHudFor(CharSprite sprite, RenderedTextBlock name, ColorBlock hpBg, ColorBlock hp) {
		float cx = sprite.x + sprite.width() / 2f;
		float top = sprite.y - 19;
		name.setPos(cx - name.width() / 2f, top);
		hpBg.x = cx - ACTOR_HP_W / 2f;
		hpBg.y = top + 12;
		hp.x = hpBg.x;
		hp.y = hpBg.y;

		RenderedTextBlock lower = sprite == playerSprite ? pileStatus : intentStatus;
		if (sprite == playerSprite) {
			lower.setPos(cx - lower.width() / 2f, sprite.y + sprite.height() + 6);
		} else {
			float intentY = name.top() - lower.height() - 2;
			lower.setPos(cx - lower.width() / 2f, intentY);
		}
	}

	private void addEffect(BattleEffect effect) {
		effects.add(effect);
		add(effect);
	}

	private void spawnCardAttack(DeckCard card, float startX, float startY, DeckPlayResult.Hit hit) {
		playAttack(playerSprite);
		final EnemyView target = enemyView(hit.enemyIndex);
		if (target == null) return;
		addEffect(new FlyingCardEffect(card, startX, startY, enemyCenterX(target), enemyCenterY(target)));
		addEffect(new DelayedActionEffect(0.22f, new Runnable() {
			@Override
			public void run() {
				target.hitTime = 0.22f;
				Sample.INSTANCE.play(Assets.Sounds.HIT_SLASH);
				addEffect(new ImpactEffect(enemyCenterX(target), enemyCenterY(target), 0xFFFFC05A));
				spawnFloatingText("-" + hit.damage, enemyCenterX(target), enemyCenterY(target) - 16, 0xFFFF705A);
				target.name.text(target.enemy.name + "  " + target.enemy.hp + "/" + target.enemy.ht);
				target.hp.size(ACTOR_HP_W * target.enemy.hp / (float)target.enemy.ht, ACTOR_HP_H);
				if (!target.enemy.alive()) {
					playDeath(target.sprite);
				}
			}
		}));
	}

	private void spawnEnemyActions() {
		int attackOrdinal = 0;
		for (DeckBuilderCombat.EnemyAction action : combat.lastEnemyActions) {
			EnemyView view = enemyView(action.enemyIndex);
			if (view == null) continue;
			if (action.slimyInject) {
                spawnFloatingText("[점액투성이]", enemyCenterX(view), enemyCenterY(view) - 24, 0xFF88FF88);
				continue;
			}
                if (action.label != null) {
                    spawnFloatingText(action.label, enemyCenterX(view), enemyCenterY(view) - 24, 0xFFFFD66B);
                }
                if (action.damage <= 0 && !action.blocked) {
                    continue;
                }
                float delay = attackOrdinal * 0.08f;
				attackOrdinal++;
				addEffect(new DelayedActionEffect(delay, new Runnable() {
				@Override
				public void run() {
					playAttack(view.sprite);
					view.attackTime = 0.22f;
					addEffect(new AttackTrailEffect(enemyCenterX(view), enemyCenterY(view), playerCenterX(), playerCenterY()));
				}
			}));
			addEffect(new DelayedActionEffect(delay + 0.18f, new Runnable() {
				@Override
				public void run() {
					if (action.damage > 0) {
						playerHitTime = 0.22f;
						Sample.INSTANCE.play(Assets.Sounds.HIT);
						addEffect(new SlashEffect(playerCenterX(), playerCenterY(), 0xFFFF5A5A));
						spawnFloatingText("-" + action.damage, playerCenterX(), playerCenterY() - 18, 0xFFFF705A);
					} else {
						spawnShieldEffect(playerCenterX(), playerCenterY(), "BLOCK");
					}
				}
			}));
		}
	}

	private void spawnShieldEffect(float x, float y, String text) {
		playGuard(playerSprite);
		playerGuardTime = 0.34f;
		Sample.INSTANCE.play(Assets.Sounds.HIT_PARRY);
		addEffect(new ShieldEffect(x, y));
		spawnFloatingText(text, x, y - 18, 0xFF8EDBFF);
	}

	private void spawnFloatingText(String text, float x, float y, int color) {
		addEffect(new FloatingTextEffect(text, x, y, color));
	}

	private void playAttack(CharSprite sprite) {
		if (sprite != null && sprite.attack != null) {
			sprite.play(sprite.attack);
		}
	}

	private void playGuard(CharSprite sprite) {
		if (sprite != null) {
			if (sprite.idle != null) sprite.play(sprite.idle);
		}
	}

	private void playDeath(CharSprite sprite) {
		if (sprite != null && sprite.die != null) {
			sprite.play(sprite.die);
		}
	}

	private void showDiscardWindow() {
		final Window win = new Window();
		int width = 180;
		int pos = 7;

		// 버린 카드 섹션
		RenderedTextBlock discardTitle = renderTextBlock("버린 카드 목록", 9);
		discardTitle.hardlight(Window.TITLE_COLOR);
		discardTitle.setPos((width - discardTitle.width()) / 2f, pos);
		win.add(discardTitle);
		pos += 15;

		pos = addPileToWindow(win, combat.discardPile, width, pos);
		pos += 8;

		// 구분선
		ColorBlock divider = new ColorBlock(width - 10, 1, 0xFF555555);
		divider.x = 5;
		divider.y = pos;
		win.add(divider);
		pos += 6;

		// 소멸 카드 섹션
		RenderedTextBlock exhaustTitle = renderTextBlock("소멸된 카드 목록", 9);
		exhaustTitle.hardlight(0xFFFF8888);
		exhaustTitle.setPos((width - exhaustTitle.width()) / 2f, pos);
		win.add(exhaustTitle);
		pos += 15;

		pos = addPileToWindow(win, combat.exhaustPile, width, pos);
		pos += 8;

		RedButton close = new RedButton("닫기", 6) {
			@Override
			protected void onClick() {
				win.hide();
			}
		};
		close.setRect((width - 100) / 2f, pos, 100, 16);
		win.add(close);
		pos += 22;

		win.resize(width, pos);
		addToFront(win);
	}

	private int addPileToWindow(Window win, ArrayList<Integer> pile, int width, int pos) {
		if (pile.isEmpty()) {
			RenderedTextBlock empty = renderTextBlock("(없음)", 6);
			empty.hardlight(0xFF888888);
			empty.setPos((width - empty.width()) / 2f, pos);
			win.add(empty);
			pos += 12;
		} else {
			LinkedHashMap<Integer, Integer> counts = new LinkedHashMap<>();
			for (int id : pile) {
				Integer prev = counts.get(id);
				counts.put(id, prev == null ? 1 : prev + 1);
			}
			for (Map.Entry<Integer, Integer> entry : counts.entrySet()) {
				int cardCode = entry.getKey();
				DeckCard card = DeckCard.byCode(cardCode);
				int count = entry.getValue();
				String line = count > 1 ? card.title(cardCode) + " x" + count : card.title(cardCode);
				RenderedTextBlock cardLine = renderTextBlock(line, 6);
				cardLine.maxWidth(width - 10);
				cardLine.hardlight(0xFFDDD8C8);
				cardLine.setPos(5, pos);
				win.add(cardLine);
				pos += (int) cardLine.height() + 3;
			}
		}
		return pos;
	}

	private void showPileWindow(String windowTitle, ArrayList<Integer> pile) {
		final Window win = new Window();
		int width = 180;
		int pos = 7;

		RenderedTextBlock titleBlock = renderTextBlock(windowTitle, 9);
		titleBlock.hardlight(Window.TITLE_COLOR);
		titleBlock.setPos((width - titleBlock.width()) / 2f, pos);
		win.add(titleBlock);
		pos += 15;

		if (pile.isEmpty()) {
            RenderedTextBlock empty = renderTextBlock("(없음)", 6);
			empty.hardlight(0xFF888888);
			empty.setPos((width - empty.width()) / 2f, pos);
			win.add(empty);
			pos += 12;
		} else {
			LinkedHashMap<Integer, Integer> counts = new LinkedHashMap<>();
			for (int id : pile) {
				Integer prev = counts.get(id);
				counts.put(id, prev == null ? 1 : prev + 1);
			}
			for (Map.Entry<Integer, Integer> entry : counts.entrySet()) {
				int cardCode = entry.getKey();
				DeckCard card = DeckCard.byCode(cardCode);
				int count = entry.getValue();
				String line = count > 1 ? card.title(cardCode) + " x" + count : card.title(cardCode);

				RenderedTextBlock cardLine = renderTextBlock(line, 6);
				cardLine.maxWidth(width - 10);
				cardLine.hardlight(0xFFDDD8C8);
				cardLine.setPos(5, pos);
				win.add(cardLine);
				pos += (int) cardLine.height() + 3;
			}
		}

		pos += 4;
        RedButton close = new RedButton("닫기", 6) {
			@Override
			protected void onClick() {
				win.hide();
			}
		};
		close.setRect((width - 100) / 2f, pos, 100, 16);
		win.add(close);
		pos += 22;

		win.resize(width, pos);
		addToFront(win);
	}

	private void showReward() {
		if (rewardOpen) return;
		rewardOpen = true;
		combatLocked = true;
		hideCardInfo();
		endTurn.visible = false;
		targetButton.visible = false;
		for (CardButton button : cardButtons) {
			button.visible = false;
		}
		// Remove cards that were exhausted for the whole run.
		DeckBuilderRun.applyExhaust(combat.exhaustPile);
		Sample.INSTANCE.play(Assets.Sounds.LEVELUP);
		boolean useFullRewardScreen = true;
		if (useFullRewardScreen) {
			showCombatRewardWindow(new CombatReward(Statistics.deckBuilderMapNode));
			return;
		}
		final DeckCard[] rewards = DeckBuilderRun.rewardChoices();
		final Window reward = new Window();
		int cardW = 62;
		int cardGap = 10;
		int totalCardW = rewards.length * cardW + Math.max(0, rewards.length - 1) * cardGap;
		int width = Math.max(220, totalCardW + 20);
		int pos = 7;

        RenderedTextBlock title = renderTextBlock("카드 보상", 9);
		title.hardlight(Window.TITLE_COLOR);
		title.setPos((width - title.width()) / 2f, pos);
		reward.add(title);
		pos += 18;

		int startX = (width - totalCardW) / 2;
		for (int i = 0; i < rewards.length; i++) {
			final DeckCard card = rewards[i];
			RewardCardButton button = new RewardCardButton(card) {
				@Override
				protected void onClick() {
					Sample.INSTANCE.play(Assets.Sounds.ITEM);
					DeckBuilderRun.addCard(card);
					reward.hide();
					continueToFloor();
				}
			};
			button.setRect(startX + i * (cardW + cardGap), pos, cardW, 64);
			reward.add(button);
		}
		pos += 70;

        RedButton skip = new RedButton("건너뛰기", 6) {
			@Override
			protected void onClick() {
				reward.hide();
				continueToFloor();
			}
		};
		skip.setRect((width - 126) / 2f, pos, 126, 18);
		reward.add(skip);
		pos += 24;

		reward.resize(width, pos);
		addToFront(reward);
	}

	private void showCombatRewardWindow(final CombatReward rewards) {
		final Window reward = new Window();
		int width = 210;
		int pos = 7;

        RenderedTextBlock title = renderTextBlock("전리품!", 11);
		title.hardlight(Window.TITLE_COLOR);
		title.setPos((width - title.width()) / 2f, pos);
		reward.add(title);
		pos += 22;

        RewardRow goldRow = new RewardRow(com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet.GOLD, rewards.gold + " 골드") {
			@Override
			protected void onClick() {
				if (claimed) return;
				claimed = true;
				DeckBuilderRun.gold += rewards.gold;
				if (runHud != null) runHud.refresh();
				Sample.INSTANCE.play(Assets.Sounds.GOLD);
                text.text("획득 완료: " + rewards.gold + " 골드");
				text.hardlight(0xFF9A9A9A);
				saveCombatState();
			}
		};
		goldRow.setRect(10, pos, width - 20, 24);
		reward.add(goldRow);
		pos += 29;

		for (int i = 0; i < rewards.relics.length; i++) {
			final DeckRelic relic = rewards.relics[i];
			RewardRow relicRow = new RewardRow(relic.icon, relic.title) {
				@Override
				protected void onClick() {
					if (claimed) return;
					claimed = true;
					DeckBuilderRun.addRelic(relic);
                    text.text("획득 완료: " + relic.title);
					text.hardlight(0xFF9A9A9A);
					saveCombatState();
				}
			};
			relicRow.setRect(10, pos, width - 20, 24);
			reward.add(relicRow);
			pos += 29;
		}

		if (rewards.potion != null) {
			RewardRow potionRow = new RewardRow(rewards.potion.image, rewards.potion.title) {
				@Override
				protected void onClick() {
					if (claimed) return;
					if (!DeckBuilderRun.addPotion(rewards.potion)) {
                        addToFront(new WndMessage("포션\n\n빈 포션 슬롯이 없습니다."));
						return;
					}
					claimed = true;
					if (runHud != null) runHud.refresh();
                    text.text("획득 완료: " + rewards.potion.title);
					text.hardlight(0xFF9A9A9A);
					saveCombatState();
				}
			};
			potionRow.setRect(10, pos, width - 20, 24);
			reward.add(potionRow);
			pos += 29;
		}

        RewardRow cardRow = new RewardRow(com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet.WONDROUS_RESIN, "덱에 추가할 카드를 선택하세요") {
			@Override
			protected void onClick() {
				if (claimed) return;
				showCardRewardWindow(reward, rewards.cards, this);
			}
		};
		cardRow.setRect(10, pos, width - 20, 24);
		reward.add(cardRow);
		pos += 32;

        RedButton done = new RedButton("계속", 6) {
			@Override
			protected void onClick() {
				reward.hide();
				continueToFloor();
			}
		};
		done.setRect((width - 100) / 2f, pos, 100, 18);
		reward.add(done);
		pos += 24;

		reward.resize(width, pos);
		addToFront(reward);
	}

	private void showCardRewardWindow(final Window parent, final DeckCard[] cards, final RewardRow cardRow) {
		final Window win = new Window();
		int cardW = 62;
		int cardGap = 10;
		int totalCardW = cards.length * cardW + Math.max(0, cards.length - 1) * cardGap;
		int width = Math.max(235, totalCardW + 20);
		int pos = 7;

        RenderedTextBlock title = renderTextBlock("카드 보상", 9);
		title.hardlight(Window.TITLE_COLOR);
		title.setPos((width - title.width()) / 2f, pos);
		win.add(title);
		pos += 18;

		int startX = (width - totalCardW) / 2;
		for (int i = 0; i < cards.length; i++) {
			final DeckCard card = cards[i];
			RewardCardButton button = new RewardCardButton(card) {
				@Override
				protected void onClick() {
					showCardTakeWindow(win, cardRow, card);
				}
			};
			button.setRect(startX + i * (cardW + cardGap), pos, cardW, 64);
			win.add(button);
		}
		pos += 70;

        RedButton skip = new RedButton("건너뛰기", 6) {
			@Override
			protected void onClick() {
				cardRow.claimed = true;
                cardRow.text.text("카드 보상 건너뜀");
				cardRow.text.hardlight(0xFF9A9A9A);
				win.hide();
			}
		};
		skip.setRect(20, pos, 90, 18);
		win.add(skip);

        RedButton close = new RedButton("닫기", 6) {
			@Override
			protected void onClick() {
				win.hide();
			}
		};
		close.setRect(width - 110, pos, 90, 18);
		win.add(close);
		pos += 24;

		win.resize(width, pos);
		addToFront(win);
	}

    private void showCardTakeWindow(final Window cardWindow, final RewardRow cardRow, final DeckCard card) {
		final Window win = new Window();
		int width = 170;
		int pos = 7;

		RenderedTextBlock title = renderTextBlock(cardDetailTitle(card, card.code()), 8);
		title.hardlight(Window.TITLE_COLOR);
		title.setPos((width - title.width()) / 2f, pos);
		win.add(title);
		pos += 16;

		RenderedTextBlock desc = renderTextBlock(cardRulesText(card, card.code()), 6);
		desc.maxWidth(width - 14);
		desc.hardlight(0xFFD8D1BD);
		desc.setPos(7, pos);
		win.add(desc);
		pos += (int)desc.height() + 8;

        RedButton take = new RedButton("가져가기", 6) {
			@Override
			protected void onClick() {
				Sample.INSTANCE.play(Assets.Sounds.ITEM);
				DeckBuilderRun.addCard(card);
				cardRow.claimed = true;
                cardRow.text.text("획득 완료: " + card.title(card.code()));
				cardRow.text.hardlight(0xFF9A9A9A);
				saveCombatState();
				win.hide();
				cardWindow.hide();
			}
		};
		take.setRect(7, pos, 74, 18);
		win.add(take);

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

	private void continueToFloor() {
		DeckBuilderRun.clearCombat();
		Statistics.deckBuilderMapNode = DeckBuilderMap.NONE;
		LevelTransition transition = Dungeon.level == null ? null : Dungeon.level.getTransition(LevelTransition.Type.REGULAR_EXIT);
		if (transition == null && Dungeon.level != null) {
			transition = new LevelTransition(Dungeon.level, Dungeon.hero.pos, LevelTransition.Type.REGULAR_EXIT);
		}
		DeckBuilderMapScene.curTransition = transition;
		saveCombatState();
		Game.switchScene(DeckBuilderMapScene.class);
	}

	private void finishRunDeath() {
		if (endingRun) return;
		endingRun = true;
		combatLocked = true;
		hideCardInfo();
		DeckBuilderRun.playerHP = 0;
		DeckBuilderRun.clearCombat();
		if (Dungeon.hero != null) {
			Dungeon.hero.HP = 0;
			Dungeon.fail(DeckBuilderRetire.class);
		}
		Dungeon.deleteGame(GamesInProgress.curSlot, true);
		Game.switchScene(RankingsScene.class);
	}

	private void showCardInfo(int cardCode) {
		cardInfo.show(cardCode, -1);
		addToFront(cardInfo);
	}

	private void showCardInfo(int cardCode, float preferredY) {
		cardInfo.show(cardCode, preferredY);
		addToFront(cardInfo);
	}

	private void hideCardInfo() {
		cardInfo.visible = false;
	}

	private void log(String text) {
		logText.text(text);
	}

	private String cardRulesText(DeckCard card, int cardCode) {
		if (card == DeckCard.SCORPION_THROW) {
			int bonus = DeckCard.upgradeLevel(cardCode) > 0 ? 2 : 1;
			return "전갈탄을 1장 손으로 가져옵니다. 카드를 " + bonus + "장 뽑습니다.";
		}
		if (card == DeckCard.PHANTOM_BLADES) {
			int bonus = DeckCard.upgradeLevel(cardCode) > 0 ? 12 : 9;
			return "모든 전갈탄에 " + DeckCardKeyword.RETAIN.label + "이 부여됩니다. 매 턴마다 처음으로 사용하는 전갈탄의 피해량이 +" + bonus + " 증가합니다.";
		}
		if (card == DeckCard.ACCURACY) {
			int bonus = DeckCard.upgradeLevel(cardCode) > 0 ? 6 : 4;
			return "전갈탄의 피해량이 +" + bonus + " 증가합니다.";
		}
		if (card == DeckCard.KNIFE_TRAP) {
			String base = "소멸한 카드 더미에 있는 모든 전갈탄을 선택한 적에게 사용합니다.";
			return DeckCard.upgradeLevel(cardCode) > 0 ? base + " (강화된 단도로 사용)" : base;
		}
		if (card == DeckCard.LEADING_STRIKE) {
			return "피해를 " + combat.cardDamage(card, cardCode, combat.target()) + " 줍니다. 전갈탄을 " + card.shivs(cardCode) + "장 손으로 가져옵니다.";
		}
		if (card == DeckCard.CLOAK_AND_DAGGER) {
			return "보호막을 " + card.block(cardCode) + " 얻습니다. 전갈탄을 " + card.shivs(cardCode) + "장 손으로 가져옵니다.";
		}

		String text = "";
		if (card.damage(cardCode) > 0) {
			text += damageRulesText(card, cardCode);
		}
		if (card.block(cardCode) > 0) text += (text.length() > 0 ? " " : "") + "보호막을 " + card.block(cardCode) + " 얻습니다.";
		if (card.draw(cardCode) > 0) text += (text.length() > 0 ? " " : "") + card.draw(cardCode) + "장 뽑기";
		if (card.vulnerable(cardCode) > 0) text += (text.length() > 0 ? " " : "") + "피해 증폭 " + card.vulnerable(cardCode) + " 부여";
		if (card.strength(cardCode) > 0) text += (text.length() > 0 ? " " : "") + "공격력 " + card.strength(cardCode) + " 획득";
		if (card.target == DeckCardTarget.RANDOM_ENEMY) text += (text.length() > 0 ? " " : "") + "무작위 대상";
		if (card.handPenalty > 0) text += (text.length() > 0 ? " " : "") + "손패: 공격 -" + card.handPenalty;
		if (card.hasKeyword(cardCode, DeckCardKeyword.EXHAUST)) text += (text.length() > 0 ? " " : "") + DeckCardKeyword.EXHAUST.label;
		if (card.hasKeyword(cardCode, DeckCardKeyword.RETAIN)) text += (text.length() > 0 ? " " : "") + DeckCardKeyword.RETAIN.label;
		return text;
	}

	private String cardDetailTitle(DeckCard card, int cardCode) {
		return card.title(cardCode) + ": 비용 " + card.cost(cardCode);
	}

	private String damageRulesText(DeckCard card, int cardCode) {
		if (card.target == DeckCardTarget.ALL_ENEMIES || card.target == DeckCardTarget.RANDOM_ENEMY) {
			int min = Integer.MAX_VALUE;
			int max = Integer.MIN_VALUE;
			for (DeckCombatEnemy enemy : combat.aliveEnemies()) {
				int damage = combat.cardDamage(card, cardCode, enemy);
				min = Math.min(min, damage);
				max = Math.max(max, damage);
			}
			if (min == Integer.MAX_VALUE) min = max = combat.cardDamage(card, cardCode);
			String value = min == max ? String.valueOf(min) : min + "-" + max;
            return card.target == DeckCardTarget.ALL_ENEMIES ? "모든 적에게 피해를 " + value + " 줍니다.": "피해를 " + value + " 줍니다.";
		}
        return "피해를 " + combat.cardDamage(card, cardCode, combat.target()) + " 줍니다.";
	}

	private String titleText() {
        return Dungeon.depth + "층";
	}

	public static class DeckBuilderRetire {
	}

	private CharSprite enemySprite(DeckEnemy kind) {
		if (kind == DeckEnemy.TOWER_OF_GREY) {
			return new WraithSprite() {
				@Override
				public void die() {
					play(die);
				}
				@Override
				public synchronized void onComplete(Animation anim) {
					if (anim == attack || anim == run) {
						idle();
					}
				}
			};
		}
		if (kind == DeckEnemy.SETESH) {
			return new GnollSprite() {
				@Override
				public void die() {
					play(die);
				}
				@Override
				public synchronized void onComplete(Animation anim) {
					if (anim == attack || anim == run) {
						idle();
					}
				}
			};
		}
		if (kind == DeckEnemy.NDOUL) {
			return new RatSprite() {
				@Override
				public void die() {
					play(die);
				}
				@Override
				public synchronized void onComplete(Animation anim) {
					if (anim == attack || anim == run) {
						idle();
					}
				}
			};
		}
		if (kind == DeckEnemy.THE_FOOL) {
			return new SnakeSprite() {
				@Override
				public void die() {
					play(die);
				}
				@Override
				public synchronized void onComplete(Animation anim) {
					if (anim == attack || anim == run) {
						idle();
					}
				}
			};
		}
		if (kind == DeckEnemy.CREAM) {
			return new DeckMobSprite(Assets.Sprites.DIOBRANDO, 12, 15,
					new int[]{0, 0, 0, 1, 0, 0, 1, 1},
					new int[]{3, 4, 5, 6, 7, 8},
					new int[]{9, 10, 11, 0},
					new int[]{13, 14, 15, 16});
		}
		if (kind == DeckEnemy.HORUS) {
			return new DeckMobSprite(Assets.Sprites.KARS, 14, 15,
					new int[]{0, 0, 0, 1, 0, 0, 1, 1},
					new int[]{2, 3, 4, 5, 6, 7},
					new int[]{8, 9, 0},
					new int[]{10, 11, 12});
		}
		// Regular combat enemy uses the slime sprite for now.
		return new RatSprite() {
			@Override
			public void die() {
				play(die);
			}
			@Override
			public synchronized void onComplete(Animation anim) {
				if (anim == attack || anim == run) {
					idle();
				}
				// ch is null here, so skip the base completion callback.
			}
		};
	}

	@Override
	protected void onBackPressed() {
	}

	private static class DeckMobSprite extends CharSprite {

		private DeckMobSprite(Object asset, int frameWidth, int frameHeight,
							  int[] idleFrames, int[] runFrames, int[] attackFrames, int[] dieFrames) {
			super();
			texture(asset);
			TextureFilm frames = new TextureFilm(texture, frameWidth, frameHeight);

			idle = new Animation(12, true);
			idle.frames(animationFrames(frames, idleFrames));

			run = new Animation(15, true);
			run.frames(animationFrames(frames, runFrames));

			attack = new Animation(15, false);
			attack.frames(animationFrames(frames, attackFrames));

			die = new Animation(15, false);
			die.frames(animationFrames(frames, dieFrames));

			play(idle);
		}

		private RectF[] animationFrames(TextureFilm film, int[] ids) {
			RectF[] result = new RectF[ids.length];
			for (int i = 0; i < ids.length; i++) {
				result[i] = film.get(ids[i]);
				if (result[i] == null) result[i] = film.get(0);
			}
			return result;
		}

		@Override
		public synchronized void onComplete(Animation anim) {
			if (anim == attack || anim == run) {
				idle();
			}
		}
	}

	private class DeckBuffButton extends Button {
		private final BuffIcon icon;
		private final BitmapText text;
		private final String label;
		private final String desc;
		private int stacks = 0;

		public DeckBuffButton(int iconId, String label, String desc) {
			super();
			this.label = label;
			this.desc = desc;
			icon = new BuffIcon(iconId, false);
			add(icon);
			text = new BitmapText(PixelScene.pixelFont);
			text.hardlight(0xFFFFFFFF);
			add(text);
			setSize(7, 7);
		}

		public void setStacks(int stacks) {
			this.stacks = stacks;
			if (stacks > 0) {
				text.text(Integer.toString(stacks));
				text.measure();
				text.visible = true;
				setSize(icon.width() + 2 + text.width(), Math.max(icon.height(), text.height()));
			} else {
				text.visible = false;
				setSize(icon.width(), icon.height());
			}
			visible = stacks > 0;
			if (visible) {
				layout();
			}
		}

		@Override
		protected void layout() {
			super.layout();
			icon.x = this.x;
			icon.y = this.y + (this.height() - icon.height()) / 2f;
			
			text.scale.set(1f);
			text.x = icon.x + icon.width() + 2;
			text.y = this.y + (this.height() - text.height()) / 2f;
		}

		@Override
		protected void onClick() {
			DeckBattleScene.this.add(new WndMessage(label + "\n\n" + desc));
		}

		@Override
		protected void onPointerDown() {
			Sample.INSTANCE.play(Assets.Sounds.CLICK);
		}
	}

	private static class EnemyView {
		private final DeckCombatEnemy enemy;
		private final int index;
		private CharSprite sprite;
		private RenderedTextBlock name;
		private RenderedTextBlock intent;
		private ColorBlock hpBg;
		private ColorBlock hp;
		private ColorBlock shield;
		private RenderedTextBlock shieldLabel;
		private ColorBlock targetMark;
		private PointerArea area;
		private DeckBuffButton vulnerableBuff;
		private DeckBuffButton strengthBuff;
		private DeckBuffButton thornsBuff;
		private float baseX;
		private float baseY;
		private float hitTime;
		private float attackTime;

		private EnemyView(DeckCombatEnemy enemy, int index) {
			this.enemy = enemy;
			this.index = index;
		}
	}

	private abstract class BattleEffect extends com.watabou.noosa.Group {

		protected float age;
		protected final float duration;
		protected boolean done;

		protected BattleEffect(float duration) {
			this.duration = duration;
		}

		@Override
		public void update() {
			super.update();
			age += Game.elapsed;
			float p = Math.min(1f, age / duration);
			updateEffect(p);
			if (p >= 1f) done = true;
		}

		protected abstract void updateEffect(float p);
	}

	private class DelayedActionEffect extends BattleEffect {

		private final Runnable action;
		private boolean fired;

		private DelayedActionEffect(float delay, Runnable action) {
			super(delay);
			this.action = action;
		}

		@Override
		public void update() {
			age += Game.elapsed;
			if (!fired && age >= duration) {
				fired = true;
				action.run();
				done = true;
			}
		}

		@Override
		protected void updateEffect(float p) {
		}
	}

	private class TitleBannerEffect extends BattleEffect {

		private final ColorBlock shade;
		private final ColorBlock accent;
		private final RenderedTextBlock title;
		private final RenderedTextBlock subtitle;
		private final Runnable onDone;
		private boolean fired;

		private TitleBannerEffect(String titleText, String subtitleText, int color, float duration, Runnable onDone) {
			super(duration);
			this.onDone = onDone;

			int w = Camera.main.width;
			int h = Camera.main.height;
			shade = new ColorBlock(w, 44, 0xFF050505);
			shade.x = 0;
			shade.y = h / 2f - 24;
			add(shade);

			accent = new ColorBlock(w, 2, color);
			accent.x = 0;
			accent.y = shade.y + shade.height;
			add(accent);

			title = renderTextBlock(titleText, 14);
			title.hardlight(color);
			title.setPos((w - title.width()) / 2f, shade.y + 7);
			align(title);
			add(title);

			subtitle = renderTextBlock(subtitleText == null ? "" : subtitleText, 6);
			subtitle.hardlight(0xFFD8D1BD);
			subtitle.setPos((w - subtitle.width()) / 2f, title.bottom() + 1);
			align(subtitle);
			subtitle.visible = subtitleText != null && subtitleText.length() > 0;
			add(subtitle);
			updateEffect(0);
		}

		@Override
		protected void updateEffect(float p) {
			float alpha;
			if (p < 0.18f) {
				alpha = p / 0.18f;
			} else if (p > 0.82f) {
				alpha = (1f - p) / 0.18f;
			} else {
				alpha = 1f;
			}
			alpha = Math.max(0, Math.min(1, alpha));
			shade.am = 0.72f * alpha;
			accent.am = alpha;
			title.alpha(alpha);
			subtitle.alpha(alpha);
			float lift = (1f - alpha) * 5f;
			title.setPos((Camera.main.width - title.width()) / 2f, shade.y + 7 - lift);
			subtitle.setPos((Camera.main.width - subtitle.width()) / 2f, title.bottom() + 1);
			if (p >= 1f && !fired) {
				fired = true;
				if (onDone != null) onDone.run();
			}
		}
	}

	private class AttackTrailEffect extends BattleEffect {

		private final ColorBlock trail;
		private final ColorBlock core;
		private final float sx;
		private final float sy;
		private final float tx;
		private final float ty;

		private AttackTrailEffect(float sx, float sy, float tx, float ty) {
			super(0.22f);
			this.sx = sx;
			this.sy = sy;
			this.tx = tx;
			this.ty = ty;
			trail = new ColorBlock(1, 1, 0xFFFF4E4E);
			add(trail);
			core = new ColorBlock(1, 1, 0xFFFFE0D6);
			add(core);
			updateEffect(0);
		}

		@Override
		protected void updateEffect(float p) {
			float e = p * p * (3f - 2f * p);
			float x = sx + (tx - sx) * e;
			float y = sy + (ty - sy) * e;
			float dx = tx - sx;
			float dy = ty - sy;
			float angle = (float)(Math.atan2(dy, dx) * 180f / Math.PI);
			float alpha = 1f - Math.max(0, p - 0.72f) / 0.28f;
			trail.x = x - 20;
			trail.y = y - 2;
			trail.size(40, 4);
			trail.angle = angle;
			trail.am = alpha * 0.85f;
			core.x = x - 8;
			core.y = y - 1;
			core.size(16, 2);
			core.angle = angle;
			core.am = alpha;
		}
	}

	private class FlyingCardEffect extends BattleEffect {

		private final DeckCard card;
		private final float sx;
		private final float sy;
		private final float tx;
		private final float ty;
		private final ColorBlock edge;
		private final ColorBlock face;
		private final ItemSprite art;

		private FlyingCardEffect(DeckCard card, float sx, float sy, float tx, float ty) {
			super(0.28f);
			this.card = card;
			this.sx = sx;
			this.sy = sy;
			this.tx = tx;
			this.ty = ty;
			edge = new ColorBlock(1, 1, card.type.borderColor);
			add(edge);
			face = new ColorBlock(1, 1, card.rarity.faceColor);
			add(face);
			art = new ItemSprite(card.icon);
			art.scale.set(1.2f);
			updateEffect(0);
			add(art);
		}

		@Override
		protected void updateEffect(float p) {
			float e = p * p * (3f - 2f * p);
			float cx = sx + (tx - sx) * e;
			float cy = sy + (ty - sy) * e - (float)Math.sin(p * Math.PI) * 18f;
			float alpha = 1f - Math.max(0, p - 0.72f) / 0.28f;
			edge.x = cx - 15;
			edge.y = cy - 20;
			edge.size(30, 40);
			edge.am = alpha;
			face.x = cx - 13;
			face.y = cy - 18;
			face.size(26, 36);
			face.am = alpha;
			art.x = cx - art.width() * art.scale.x / 2f;
			art.y = cy - art.height() * art.scale.y / 2f;
			art.am = alpha;
		}
	}

	private class FloatingTextEffect extends BattleEffect {

		private final RenderedTextBlock text;
		private final float x;
		private final float y;

		private FloatingTextEffect(String value, float x, float y, int color) {
			super(0.72f);
			this.x = x;
			this.y = y;
			text = renderTextBlock(value, 9);
			text.hardlight(color);
			add(text);
		}

		@Override
		protected void updateEffect(float p) {
			text.setPos(x - text.width() / 2f, y - p * 24f);
			text.alpha(1f - p);
		}
	}

	private class ImpactEffect extends BattleEffect {

		private final ColorBlock horizontal;
		private final ColorBlock vertical;
		private final float x;
		private final float y;

		private ImpactEffect(float x, float y, int color) {
			super(0.24f);
			this.x = x;
			this.y = y;
			horizontal = new ColorBlock(1, 1, color);
			add(horizontal);
			vertical = new ColorBlock(1, 1, color);
			add(vertical);
		}

		@Override
		protected void updateEffect(float p) {
			float alpha = 1f - p;
			float len = 12 + p * 34;
			horizontal.x = x - len / 2f;
			horizontal.y = y - 1;
			horizontal.size(len, 2);
			horizontal.am = alpha;
			vertical.x = x - 1;
			vertical.y = y - len / 2f;
			vertical.size(2, len);
			vertical.am = alpha;
		}
	}

	private class SlashEffect extends BattleEffect {

		private final ColorBlock slashA;
		private final ColorBlock slashB;
		private final float x;
		private final float y;

		private SlashEffect(float x, float y, int color) {
			super(0.28f);
			this.x = x;
			this.y = y;
			slashA = new ColorBlock(1, 1, color);
			add(slashA);
			slashB = new ColorBlock(1, 1, 0xFFFFD1D1);
			add(slashB);
		}

		@Override
		protected void updateEffect(float p) {
			float alpha = 1f - p;
			float offset = p * 16f;
			slashA.x = x - 24 + offset;
			slashA.y = y - 14;
			slashA.size(46, 3);
			slashA.angle = -28;
			slashA.am = alpha;
			slashB.x = x - 18 + offset;
			slashB.y = y + 4;
			slashB.size(34, 2);
			slashB.angle = -28;
			slashB.am = alpha * 0.8f;
		}
	}

	private class ShieldEffect extends BattleEffect {

		private final ColorBlock top;
		private final ColorBlock bottom;
		private final ColorBlock left;
		private final ColorBlock right;
		private final float x;
		private final float y;

		private ShieldEffect(float x, float y) {
			super(0.34f);
			this.x = x;
			this.y = y;
			top = new ColorBlock(1, 1, 0xFF8EDBFF);
			bottom = new ColorBlock(1, 1, 0xFF8EDBFF);
			left = new ColorBlock(1, 1, 0xFF8EDBFF);
			right = new ColorBlock(1, 1, 0xFF8EDBFF);
			add(top);
			add(bottom);
			add(left);
			add(right);
		}

		@Override
		protected void updateEffect(float p) {
			float alpha = 1f - p;
			float w = 32 + p * 18;
			float h = 42 + p * 18;
			top.x = x - w / 2f;
			top.y = y - h / 2f;
			top.size(w, 2);
			top.am = alpha;
			bottom.x = top.x;
			bottom.y = y + h / 2f;
			bottom.size(w, 2);
			bottom.am = alpha;
			left.x = x - w / 2f;
			left.y = y - h / 2f;
			left.size(2, h);
			left.am = alpha;
			right.x = x + w / 2f;
			right.y = left.y;
			right.size(2, h);
			right.am = alpha;
		}
	}

	private class CardInfoPopup extends com.watabou.noosa.Group {

		private final ColorBlock bg;
		private final ColorBlock edge;
		private final RenderedTextBlock title;
		private final RenderedTextBlock body;
		private final RenderedTextBlock keywords;

		private CardInfoPopup() {
			bg = new ColorBlock(1, 1, 0xCC050505);
			bg.am = 0.82f;
			add(bg);
			edge = new ColorBlock(1, 1, 0xFFB08B45);
			add(edge);
			title = renderTextBlock(8);
			title.hardlight(Window.TITLE_COLOR);
			add(title);
			body = renderTextBlock(7);
			body.hardlight(0xFFE8E0D0);
			add(body);
			keywords = renderTextBlock(6);
			keywords.hardlight(0xFF9EE6FF);
			add(keywords);
		}

		private void show(int cardCode, float preferredY) {
			DeckCard card = DeckCard.byCode(cardCode);
			visible = true;
            title.text(cardDetailTitle(card, cardCode));
            body.text(cardRulesText(card, cardCode));
			body.maxWidth(142);
			keywords.text(keywordText(card, cardCode));
			keywords.maxWidth(142);
			keywords.visible = keywords.text().length() > 0;

			float width = 154;
			float height = 36 + body.height() + (keywords.text().length() > 0 ? keywords.height() + 5 : 0);
			float x = (Camera.main.width - width) / 2f;
			float y = preferredY >= 0 ? preferredY : Math.max(24, handY - height - 10);
			y = Math.max(16, Math.min(y, Camera.main.height - height - 16));

			bg.x = x;
			bg.y = y;
			bg.size(width, height);
			edge.x = x;
			edge.y = y;
			edge.size(width, 2);
			title.setPos(x + 7, y + 6);
			body.setPos(x + 7, title.bottom() + 5);
			keywords.setPos(x + 7, body.bottom() + 5);
		}

		private String keywordText(DeckCard card, int cardCode) {
			String text = "";
			if (card.vulnerable(cardCode) > 0) text += "피해 증폭: 받는 공격 피해가 50% 증가합니다.";
			if (card.strength(cardCode) > 0) text += (text.length() > 0 ? "\n" : "") + "공격력: 공격 카드의 피해가 증가합니다.";
			for (DeckCardKeyword keyword : DeckCardKeyword.values()) {
				if (card.hasKeyword(cardCode, keyword)) {
					text += (text.length() > 0 ? "\n" : "") + keyword.label + ": " + keyword.description;
				}
			}
			if (card.handPenalty > 0) text += (text.length() > 0 ? "\n" : "") + "방해: 손에 있으면 공격 카드 피해가 감소합니다.";
			return text;
		}

	}

	private class CardButton extends CardViewButton {

		private final int handIndex;
		private float homeX;
		private float homeY;
		private boolean dragging;
		private boolean useOnRelease;

		private CardButton(int handIndex) {
			super();
			this.handIndex = handIndex;
		}

		@Override
		protected DeckCard card() {
			if (handIndex < 0 || handIndex >= combat.hand.size()) return DeckCard.STRIKE;
			return DeckCard.byCode(cardCode());
		}

		@Override
		protected int cardCode() {
			if (handIndex < 0 || handIndex >= combat.hand.size()) return DeckCard.STRIKE.code();
			return combat.hand.get(handIndex);
		}

		@Override
		protected boolean enabled() {
			return !combatLocked && handIndex < combat.hand.size() && card().cost(cardCode()) <= combat.energy;
		}

		@Override
		protected void onPointerDown() {
			super.onPointerDown();
			homeX = x;
			homeY = y;
			dragging = false;
			useOnRelease = false;
			clickReady = false;
			showCardInfo(cardCode());
		}

		@Override
		protected void onDrag(PointerEvent event) {
			if (!enabled()) return;
			float dx = event.current.x - event.start.x;
			float dy = event.current.y - event.start.y;
			if (!dragging && dx * dx + dy * dy < 36) return;
			dragging = true;
			clickReady = false;
			com.watabou.utils.PointF p = camera().screenToCamera((int)event.current.x, (int)event.current.y);
			setRect(p.x - width / 2f, p.y - height / 2f, width, height);
			useOnRelease = y < handY - CARD_H * 0.45f;
			edge.am = useOnRelease ? 1f : 0.78f;
		}

		@Override
		protected void onPointerUp() {
			super.onPointerUp();
			hideCardInfo();
			if (dragging && useOnRelease && enabled()) {
				playCard(handIndex);
			} else {
				setRect(homeX, homeY, width, height);
			}
			dragging = false;
			useOnRelease = false;
		}

		@Override
		protected void onClick() {
		}
	}

	private class CombatReward {
		private final int gold;
		private final DeckRelic[] relics;
		private final DeckPotion potion;
		private final DeckCard[] cards;

		private CombatReward(int nodeType) {
			gold = DeckBuilderRun.rollRewardGold(nodeType);
			relics = DeckBuilderRun.rollRewardRelics(nodeType);
			potion = DeckBuilderRun.rollRewardPotion(nodeType);
			cards = DeckBuilderRun.rewardChoicesForNode(nodeType);
		}
	}

	private class RewardRow extends Button {
		private final int iconImage;
		private ItemSprite icon;
		private final String label;
		private ColorBlock bg;
		protected RenderedTextBlock text;
		protected boolean claimed;

		private RewardRow(int iconImage, String label) {
			this.iconImage = iconImage;
			this.label = label;
		}

		@Override
		protected void createChildren() {
			super.createChildren();
			bg = new ColorBlock(1, 1, 0xFF255C5F);
			bg.am = 0.92f;
			add(bg);
			icon = new ItemSprite();
			add(icon);
			text = renderTextBlock("", 6);
			text.hardlight(0xFFD8D1BD);
			add(text);
		}

		@Override
		protected void layout() {
			super.layout();
			bg.x = x;
			bg.y = y;
			bg.size(width, height);
			bg.am = claimed ? 0.45f : 0.92f;
			icon.view(iconImage, null);
			icon.x = x + 7;
			icon.y = y + (height - icon.height()) / 2f;
			if (!claimed) text.text(label);
			text.maxWidth((int)(width - 34));
			text.setPos(x + 31, y + (height - text.height()) / 2f);
		}
	}

	private class RewardCardButton extends CardViewButton {

		private final DeckCard card;

		private RewardCardButton(DeckCard card) {
			super();
			this.card = card;
		}

		@Override
		protected DeckCard card() {
			return card;
		}

		@Override
		protected int cardCode() {
			return card.code();
		}

		@Override
		protected void onPointerDown() {
			super.onPointerDown();
			showCardInfo(cardCode(), Camera.main.height * 0.14f);
		}

		@Override
		protected void onPointerUp() {
			super.onPointerUp();
			hideCardInfo();
		}
	}

	private abstract class CardViewButton extends com.shatteredpixel.shatteredpixeldungeon.ui.Button {

		protected ColorBlock shadow;
		protected ColorBlock edge;
		protected ColorBlock face;
		protected ColorBlock artPanel;
		protected ItemSprite art;
		protected Image spriteArt;
		protected TalentIcon talentArt;
		protected RenderedTextBlock cost;
		protected RenderedTextBlock title;
		protected RenderedTextBlock typeLabel;
		protected RenderedTextBlock rules;

		@Override
		protected void createChildren() {
			super.createChildren();
			shadow = new ColorBlock(1, 1, 0xFF000000);
			shadow.visible = false;
			add(shadow);
			edge = new ColorBlock(1, 1, 0xFFFFFFFF);
			edge.visible = false;
			add(edge);
			face = new ColorBlock(1, 1, 0xFF262626);
			face.visible = false;
			add(face);
			artPanel = new ColorBlock(1, 1, 0xFF111111);
			artPanel.visible = false;
			add(artPanel);
			spriteArt = new Image();
			spriteArt.visible = false;
			add(spriteArt);
		cost = renderTextBlock(8);
		cost.visible = false;
		add(cost);
		title = renderTextBlock(5);
		title.visible = false;
		add(title);
		typeLabel = renderTextBlock(5);
		typeLabel.visible = false;
		add(typeLabel);
		rules = renderTextBlock(6);
		rules.visible = false;
		add(rules);
		}

		@Override
		protected void layout() {
			super.layout();
			int cardCode = cardCode();
			DeckCard card = card();
			boolean enabled = enabled();

			shadow.visible = true;
			edge.visible = true;
			face.visible = true;
			artPanel.visible = true;
			cost.visible = true;
			title.visible = true;
			typeLabel.visible = true;
			rules.visible = false;

			shadow.x = x + 2;
			shadow.y = y + 2;
			shadow.size(width, height);
			shadow.am = 0.45f;

			edge.color(card.type.borderColor);
			edge.x = x;
			edge.y = y;
			edge.size(width, height);
			edge.am = enabled ? 0.95f : 0.45f;

			face.color(card.deckClass == null || !card.reward ? card.rarity.faceColor : 0xFF1A3D6B);
			face.x = x + 2;
			face.y = y + 2;
			face.size(width - 4, height - 4);
			face.am = enabled ? 0.96f : 0.68f;

		cost.text(String.valueOf(card.cost(cardCode)));
		cost.hardlight(enabled ? 0xFFFFD84D : 0xFF8A7A42);
		cost.setPos(x + 4, y + 4);

            title.text(card.title(cardCode));
		int titleColor = card.rarity == DeckCardRarity.COMMON ? 0xFFFFFFFF : card.rarity.labelColor;
		title.hardlight(enabled ? titleColor : 0xFF8A8A8A);
		title.maxWidth((int)width - 14);
		title.setPos(x + 12, y + 5);

		typeLabel.text(card.type.label);
		typeLabel.hardlight(enabled ? cardLabelColor(card) : 0xFF8A8A8A);
		typeLabel.maxWidth((int)width - 10);
		float labelX = x + (width - typeLabel.width()) / 2f;
		float labelY = y + height - typeLabel.height() - 6;
		typeLabel.setPos(labelX, labelY);

		float artH = Math.max(18, height * 0.46f);
		artPanel.color(card.deckClass == null || !card.reward ? card.rarity.panelColor : 0xFF2D5F9D);
		artPanel.x = x + 5;
		artPanel.y = y + height * 0.28f;
		artPanel.size(width - 10, artH);
		artPanel.am = enabled ? 0.30f : 0.12f;

		if (card == DeckCard.SLIMY) {
			if (art != null) art.visible = false;
			if (talentArt != null) talentArt.visible = false;
			spriteArt.visible = true;
			spriteArt.texture(Assets.Sprites.RAT);
			TextureFilm gnollFilm = new TextureFilm(spriteArt.texture, 16, 15);
			spriteArt.frame(gnollFilm.get(0));
			spriteArt.scale.set(1.6f);
			spriteArt.x = artPanel.x + (artPanel.width() - spriteArt.width()) / 2f;
			spriteArt.y = artPanel.y + (artPanel.height() - spriteArt.height()) / 2f;
			align(spriteArt);
		} else if (card.talentIcon != null) {
			if (art != null) art.visible = false;
			spriteArt.visible = false;
			if (talentArt != null) {
				remove(talentArt);
			}
			talentArt = new TalentIcon(card.talentIcon);
			add(talentArt);
			talentArt.scale.set(1.25f);
			talentArt.x = artPanel.x + (artPanel.width() - talentArt.width()) / 2f;
			talentArt.y = artPanel.y + (artPanel.height() - talentArt.height()) / 2f;
			align(talentArt);
			talentArt.visible = true;
		} else {
			if (talentArt != null) talentArt.visible = false;
			if (art == null) {
				art = new ItemSprite(card.icon);
				art.visible = false;
				add(art);
			}
			spriteArt.visible = false;
			art.view(card.icon, null);
			art.scale.set(1.25f);
			art.x = artPanel.x + (artPanel.width() - art.width()) / 2f;
			art.y = artPanel.y + (artPanel.height() - art.height()) / 2f;
			align(art);
			art.visible = true;
		}

		rules.visible = false;
		}

		private int cardLabelColor(DeckCard card) {
			if (card.type == com.shatteredpixel.shatteredpixeldungeon.deckbuilder.DeckCardType.STATUS
					|| card.type == com.shatteredpixel.shatteredpixeldungeon.deckbuilder.DeckCardType.CURSE) {
				return card.type.labelColor;
			}
			return card.rarity.labelColor;
		}

		protected abstract DeckCard card();

		protected abstract int cardCode();

		protected boolean enabled() {
			return true;
		}
	}
}
