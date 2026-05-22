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
import com.shatteredpixel.shatteredpixeldungeon.deckbuilder.DeckCardText;
import com.shatteredpixel.shatteredpixeldungeon.deckbuilder.DeckCardType;
import com.shatteredpixel.shatteredpixeldungeon.deckbuilder.DeckCombatEnemy;
import com.shatteredpixel.shatteredpixeldungeon.deckbuilder.DeckCombatRewardState;
import com.shatteredpixel.shatteredpixeldungeon.deckbuilder.DeckEnemy;
import com.shatteredpixel.shatteredpixeldungeon.deckbuilder.DeckEnemyIntent;
import com.shatteredpixel.shatteredpixeldungeon.deckbuilder.DeckPlayResult;
import com.shatteredpixel.shatteredpixeldungeon.deckbuilder.DeckPotion;
import com.shatteredpixel.shatteredpixeldungeon.deckbuilder.DeckRelic;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Sword;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.sprites.AlbinoSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CrabSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.FishSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GnollExileSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GnollSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GooSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HeroSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.PiranhaSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.PucciSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.RatSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SnakeSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.StatueSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.StowerSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SwarmSprite;
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
	private static final int NO_REWARD_ICON = -1;
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
	private final ArrayList<DeckBuffButton> playerBuffs = new ArrayList<>();
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
	private boolean selectingWandForStaff;
	private int mageStaffHandIndex;
	private ColorBlock wandSelectionShade;
	private ColorBlock wandSelectionAccent;
	private RenderedTextBlock wandSelectionTitle;
	private RenderedTextBlock wandSelectionSubtitle;
	private boolean selectingForPure;
	private int pureHandIndex;
	private final ArrayList<Integer> pureSelectedIndices = new ArrayList<>();
	private int pureMaxSelect;
	private boolean gamblerBrewActive;
	private int gamblerBrewSlot;
	private PointerArea selectionBackdropArea;
	private ColorBlock pureSelectionShade;
	private ColorBlock pureSelectionAccent;
	private RenderedTextBlock pureSelectionTitle;
	private RenderedTextBlock pureSelectionSubtitle;
	private RedButton pureSelectorConfirm;
	private ArrayList<CardButton> cardButtons = new ArrayList<>();
	private ArrayList<BattleEffect> effects = new ArrayList<>();
	private CardInfoPopup cardInfo;
	private RedButton endTurn;
	private RedButton targetButton;
	private DeckRunHud runHud;
	private float spriteScale;
	private boolean rewardOpen;
	private boolean endingRun;
	private ArrayList<Float> pendingDiscardStartsX = new ArrayList<>();
	private ArrayList<Float> pendingDiscardStartsY = new ArrayList<>();
	private ArrayList<Float> pendingTransientExhaustX = new ArrayList<>();
	private ArrayList<Float> pendingTransientExhaustY = new ArrayList<>();
	private boolean pendingPileShuffle;
	private int pendingDrawVisuals;
	private boolean needsHandDraw = false;

	private static final PlayerBuffSpec[] PLAYER_STATUS_BUFFS = new PlayerBuffSpec[]{
			new PlayerBuffSpec(BuffIndicator.UPGRADE, 1f, 0.5f, 0f, "공격력", "공격 카드가 주는 피해가 이 수치만큼 증가합니다.",
					combat -> Math.max(0, combat.playerStrength + combat.playerTurnStrength)),
			new PlayerBuffSpec(BuffIndicator.DEGRADE, "공격력 감소", "공격 카드가 주는 피해가 수치만큼 감소합니다.",
					combat -> Math.max(0, -(combat.playerStrength + combat.playerTurnStrength))),
			new PlayerBuffSpec(BuffIndicator.CRIPPLE, "민첩 감소", "보호막을 얻을 때 획득량이 수치만큼 감소합니다.",
					combat -> Math.max(0, -combat.playerDexterity)),
			new PlayerBuffSpec(BuffIndicator.WEAKNESS, "약화", "공격 카드 피해가 25% 감소합니다. 턴이 시작될 때마다 1 감소합니다.",
					combat -> combat.playerWeak),
			new PlayerBuffSpec(BuffIndicator.ROOTS, "얽힘", "공격 카드 비용이 1 증가합니다. 턴이 끝날 때마다 1 감소합니다.",
					combat -> combat.playerEntangle),
			new PlayerBuffSpec(BuffIndicator.MOMENTUM, "유아화", "공격 카드의 피해가 30% 감소합니다.",
					combat -> combat.playerDamageReduction > 0 ? 1 : 0, false),
			new PlayerBuffSpec(BuffIndicator.DEGRADE, "방어력 저하", "보호막을 얻을 때마다 방어력 저하 1당 획득량이 25% 감소합니다. 턴이 끝날 때마다 1 감소합니다.",
					combat -> combat.playerBlockReduction)
	};

	private static final EnemyStatusBuffSpec[] ENEMY_STATUS_BUFFS = new EnemyStatusBuffSpec[]{
			new EnemyStatusBuffSpec(BuffIndicator.CORRUPT, "피해 증폭", "해당 대상이 받는 모든 공격 피해가 1.5배 증가합니다.",
					enemy -> enemy.vulnerable),
			new EnemyStatusBuffSpec(BuffIndicator.WEAKNESS, "공격력 저하", "공격 피해가 25% 감소합니다. 턴이 끝날 때마다 1 감소합니다.",
					enemy -> enemy.attackDown),
			new EnemyStatusBuffSpec(BuffIndicator.UPGRADE, 1f, 0.5f, 0f, "공격력", "공격 피해가 이 수치만큼 증가합니다.",
					enemy -> enemy.strength),
			new EnemyStatusBuffSpec(BuffIndicator.TRINITY_FORM, 1.2f, 1.2f, 0.2f, "반격", "공격 카드로 공격한 대상에게 피해를 반격 수치만큼 되돌립니다.",
					enemy -> enemy.thorns),
			new EnemyStatusBuffSpec(BuffIndicator.HEALING, "재생", "턴이 끝날 때마다 이 수치만큼 보호막을 얻습니다. 체력 피해를 받을 때마다 1 감소합니다.",
					enemy -> enemy.platedArmor),
			new EnemyStatusBuffSpec(BuffIndicator.INVISIBLE, "까다로움", "다음에 체력을 잃을 때, 대신 체력을 1만 잃습니다.",
					enemy -> enemy.tricky),
			new EnemyStatusBuffSpec(BuffIndicator.POISON, 0.2f, 0.5f, 0.2f, "독극물", "막히지 않은 공격 피해를 주면 이 수치만큼 공격력을 얻습니다.",
					enemy -> enemy.kind == DeckEnemy.LAGAVULIN ? 0 : enemy.venom),
			new EnemyStatusBuffSpec(BuffIndicator.IMMUNITY, "정화의 보호막", "상태이상에 걸릴 때 정화의 보호막을 1 차감하고 그 효과를 무효화합니다.",
					enemy -> enemy.artifact),
			new EnemyStatusBuffSpec(BuffIndicator.SACRIFICE, 1.15f, 0.35f, 1.25f, "의식", "턴이 끝날 때마다 이 수치만큼 공격력을 얻습니다.",
					enemy -> enemy.ritual)
	};

	private static final EnemyBuffSpec[] ENEMY_TRAIT_BUFFS = new EnemyBuffSpec[]{
			new EnemyBuffSpec(BuffIndicator.IMBUE, "분열", "체력이 절반 이하가 되면 행동을 취소하고 둘로 나뉩니다.",
					(scene, enemy) -> enemy.kind == DeckEnemy.LARGE_SLIME && !enemy.splitUsed),
			new EnemyBuffSpec(BuffIndicator.MAGIC_SLEEP, "수면", "피해를 받거나 3턴이 지나기 전까지 행동하지 않고 매 턴 보호막을 얻습니다.",
					(scene, enemy) -> enemy.kind == DeckEnemy.LAGAVULIN && !enemy.splitUsed),
			new EnemyBuffSpec(BuffIndicator.PARALYSIS, "기절", "이번 턴 행동하지 않습니다.",
					(scene, enemy) -> enemy.kind == DeckEnemy.LAGAVULIN && enemy.intent == DeckBuilderCombat.RESULT_LAGAVULIN_STUN),
			new EnemyBuffSpec(BuffIndicator.UPGRADE, 1f, 0.5f, 0f, "사냥 본능", "턴이 끝날 때마다 공격력이 1 증가합니다.",
					(scene, enemy) -> enemy.kind == DeckEnemy.BYRDONIS),
			new EnemyBuffSpec(BuffIndicator.RAGE, 1.2f, 0.35f, 0.25f, "쥐 떼의 분노", "짝이 쓰러지면 공격력이 2 증가합니다.",
					(scene, enemy) -> scene.hasAliveRatPartner(enemy))
	};

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

		selectionBackdropArea = new PointerArea(0, 0, w, h) {
			@Override
			protected void onClick(PointerEvent event) {
				if (selectingWandForStaff) {
					selectingWandForStaff = false;
					hideWandSelectionBanner();
					refresh();
				} else if (selectingForPure) {
					selectingForPure = false;
					hidePureSelectionBanner();
					pureSelectedIndices.clear();
					refresh();
				} else if (gamblerBrewActive) {
					confirmGamblerBrew();
				}
			}
		};
		selectionBackdropArea.active = false;
		add(selectionBackdropArea);

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
		for (PlayerBuffSpec spec : PLAYER_STATUS_BUFFS) {
			DeckBuffButton buff = spec.createButton(this);
			playerBuffs.add(buff);
			add(buff);
		}

		logText = renderTextBlock(6);
		logText.maxWidth(w - (int)insets.left - (int)insets.right - 18);
		logText.hardlight(0xFFD8D1BD);
		add(logText);

		cardInfo = new CardInfoPopup();
		cardInfo.visible = false;
		add(cardInfo);

		wandSelectionShade = new ColorBlock(w, 44, 0xFF050505);
		wandSelectionShade.x = 0;
		wandSelectionShade.y = insets.top + 24;
		wandSelectionShade.am = 0.72f;
		wandSelectionShade.visible = false;
		add(wandSelectionShade);

		wandSelectionAccent = new ColorBlock(w, 2, 0xFFC07AFF);
		wandSelectionAccent.x = 0;
		wandSelectionAccent.y = wandSelectionShade.y + wandSelectionShade.height;
		wandSelectionAccent.visible = false;
		add(wandSelectionAccent);

		wandSelectionTitle = renderTextBlock("발사할 완드를 손패에서 선택하세요.", 9);
		wandSelectionTitle.hardlight(0xFFC07AFF);
		wandSelectionTitle.visible = false;
		add(wandSelectionTitle);

		wandSelectionSubtitle = renderTextBlock("(지팡이를 다시 누르면 취소됩니다)", 6);
		wandSelectionSubtitle.hardlight(0xFFD8D1BD);
		wandSelectionSubtitle.visible = false;
		add(wandSelectionSubtitle);

		pureSelectionShade = new ColorBlock(w, 58, 0xFF050505);
		pureSelectionShade.x = 0;
		pureSelectionShade.y = insets.top + 24;
		pureSelectionShade.am = 0.72f;
		pureSelectionShade.visible = false;
		add(pureSelectionShade);

		pureSelectionAccent = new ColorBlock(w, 2, 0xFF7EC8D4);
		pureSelectionAccent.x = 0;
		pureSelectionAccent.y = pureSelectionShade.y + pureSelectionShade.height;
		pureSelectionAccent.visible = false;
		add(pureSelectionAccent);

		pureSelectionTitle = renderTextBlock("소멸할 카드를 최대 N장 선택하세요.", 9);
		pureSelectionTitle.hardlight(0xFF7EC8D4);
		pureSelectionTitle.visible = false;
		add(pureSelectionTitle);

		pureSelectionSubtitle = renderTextBlock("(순수 카드를 다시 누르면 취소됩니다)", 6);
		pureSelectionSubtitle.hardlight(0xFFD8D1BD);
		pureSelectionSubtitle.visible = false;
		add(pureSelectionSubtitle);

		pureSelectorConfirm = new RedButton("확인 (0/3)", 6) {
			@Override
			protected void onClick() {
				if (selectingForPure) {
					confirmPureSelection();
				} else if (gamblerBrewActive) {
					confirmGamblerBrew();
				}
			}
		};
		pureSelectorConfirm.visible = false;
		add(pureSelectorConfirm);

        endTurn = new RedButton("턴 종료", 7) {
			@Override
			protected void onClick() {
				if (combatLocked) return;
				if (startEnemyTurn()) return;
				int result = combat.endTurn();
				saveCombatState();
				spawnEnemyActions();

				if (combat.playerDead()) {
					updatePlayerHpUi();
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
                    log(enemyTurnLog(result));
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
        targetButton = new RedButton("대상 변경", 6) {
			@Override
			protected void onClick() {
				if (combatLocked) return;
				selectNextTarget();
			}
		};
		add(targetButton);

		layoutStatic(w, h, insets, title.bottom());
		refresh();
		if (combat.won()) {
			showReward();
		} else if (freshCombat) {
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
				|| current.playerDead();
	}

	private void showBattleStartTitle() {
		Sample.INSTANCE.play(Assets.Sounds.DESCEND);
        showTitleBanner("전투 시작", "", 0xFFFFF27A, 0.95f, new Runnable() {
			@Override
			public void run() {
				needsHandDraw = true;
				showPlayerTurnTitle();
			}
		});
	}

	private void showPlayerTurnTitle() {
		if (needsHandDraw) {
			needsHandDraw = false;
			int handBefore = combat.hand.size();
			int drawPileBefore = combat.drawPile.size();
			int discardPileBefore = combat.discardPile.size();
			combat.drawTurnHand();
			int drawn = combat.hand.size() - handBefore;
			pendingDrawVisuals = drawn;
			pendingPileShuffle = discardPileBefore > 0 && combat.discardPile.size() < discardPileBefore;
			saveCombatState();
		}
		Sample.INSTANCE.play(Assets.Sounds.ITEM);
        showTitleBanner("내 턴", combat.turn + "턴", 0xFF9EE6FF, 0.78f, new Runnable() {
			@Override
			public void run() {
				final float drawEndTime = spawnPendingTurnPileEffects();
				if (!resolveAutoPlayResultsAfterDraw()) {
					if (drawEndTime > 0f) {
						addEffect(new DelayedActionEffect(drawEndTime, new Runnable() {
							@Override
							public void run() {
								refresh();
							}
						}));
					} else {
						refresh();
					}
				}
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
		ArrayList<Float> discardStartsX = new ArrayList<>();
		ArrayList<Float> discardStartsY = new ArrayList<>();
		ArrayList<Float> transientExhaustX = new ArrayList<>();
		ArrayList<Float> transientExhaustY = new ArrayList<>();
		for (int i = 0; i < combat.hand.size(); i++) {
			int code = combat.hand.get(i);
			if (retainedAtEndTurn(code)) continue;
			float cx = i < cardButtons.size() ? cardButtons.get(i).centerX() : Camera.main.width / 2f;
			float cy = i < cardButtons.size() ? cardButtons.get(i).centerY() : handY + CARD_H / 2f;
			if (DeckCard.byCode(code).hasKeyword(code, DeckCardKeyword.TRANSIENT)) {
				transientExhaustX.add(cx);
				transientExhaustY.add(cy);
			} else {
				discardStartsX.add(cx);
				discardStartsY.add(cy);
			}
		}
		int result = combat.endTurn();
		spawnPoisonDartDamageEffect();
		float wandDelay = spawnTurnEndAutoPlayEffects();
		pendingDiscardStartsX.clear();
		pendingDiscardStartsX.addAll(discardStartsX);
		pendingDiscardStartsY.clear();
		pendingDiscardStartsY.addAll(discardStartsY);
		pendingTransientExhaustX.clear();
		pendingTransientExhaustX.addAll(transientExhaustX);
		pendingTransientExhaustY.clear();
		pendingTransientExhaustY.addAll(transientExhaustY);
		pendingPileShuffle = false;
		pendingDrawVisuals = 0;

		addEffect(new DelayedActionEffect(wandDelay, new Runnable() {
			@Override
			public void run() {
				resolveEnemyTurnAfterWands(result);
			}
		}));
	}

	private void resolveEnemyTurnAfterWands(final int result) {
		if (combat.won()) {
			showReward();
			return;
		}

		spawnEnemyActions();
		if (combat.playerDead()) {
			updatePlayerHpUi();
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
            log(enemyTurnLog(result));
			float delay = Math.max(0.36f, 0.62f + combat.lastEnemyActions.size() * 0.08f);
			addEffect(new DelayedActionEffect(delay, new Runnable() {
				@Override
				public void run() {
					if (combat.won()) {
						showReward();
					} else {
						needsHandDraw = true;
						showPlayerTurnTitle();
					}
				}
			}));
		}
	}

	private void showTitleBanner(String title, String subtitle, int color, float duration, Runnable onDone) {
		addEffect(new TitleBannerEffect(title, subtitle, color, duration, onDone));
	}

	private void createEnemyViews() {
		for (int i = 0; i < combat.enemies.size(); i++) {
			addEnemyView(i);
		}
		if (!enemyViews.isEmpty()) {
			enemySprite = enemyViews.get(0).sprite;
		}
	}

	private void ensureEnemyViews() {
		if (enemyViews.size() >= combat.enemies.size()) return;
		for (int i = enemyViews.size(); i < combat.enemies.size(); i++) {
			addEnemyView(i);
		}
		RectF insets = getCommonInsets();
		layoutEnemyViews(Camera.main.width - insets.right - 12, playerBaseY);
	}

	private void addEnemyView(int index) {
		EnemyView view = new EnemyView(combat.enemies.get(index), index);
		view.sprite = enemySprite(view.enemy.kind);
		view.sprite.scale.set(view.enemy.kind == DeckEnemy.RAMPAGING_BULL ? spriteScale * 0.6f : spriteScale);
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

		for (EnemyStatusBuffSpec spec : ENEMY_STATUS_BUFFS) {
			DeckBuffButton buff = spec.createButton(this);
			view.statusBuffs.add(buff);
			add(buff);
		}
		for (EnemyBuffSpec spec : ENEMY_TRAIT_BUFFS) {
			DeckBuffButton buff = spec.createButton(this);
			view.traitBuffs.add(buff);
			add(buff);
		}
		enemyViews.add(view);
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
				hideStatusBuffs(view);
				hideTraitBuffs(view);
				view.shield.visible = false;
				view.shieldLabel.visible = false;
				continue;
			}
			
			refreshStatusBuffs(view);
			refreshTraitBuffs(view);
			view.shield.visible = view.enemy.block > 0;
			if (view.enemy.block > 0) {
				view.shieldLabel.text(String.valueOf(view.enemy.block));
				view.shieldLabel.visible = true;
			} else {
				view.shieldLabel.visible = false;
			}
			
			view.displayHp = view.enemy.hp;
			view.name.text(view.enemy.name + "  " + view.enemy.hp + "/" + view.enemy.ht);
			view.name.hardlight(targeted ? Window.TITLE_COLOR : 0xFFFFFFFF);
			view.intent.text(intentText(view.enemy));
			view.intent.hardlight(0xFFFF5555);
			view.hp.size(ACTOR_HP_W * view.enemy.hp / (float)view.enemy.ht, ACTOR_HP_H);
			float hpFillWidth = view.hp.width();
			float rawShieldW = view.enemy.block * ACTOR_HP_W / (float)Math.max(1, view.enemy.ht);
			view.shield.size(Math.max(0, Math.min(rawShieldW, ACTOR_HP_W - hpFillWidth)), ACTOR_HP_H);
		}
	}

	private String intentText(DeckCombatEnemy enemy) {
		return DeckEnemyIntent.text(enemy);
	}

	private void refreshStatusBuffs(EnemyView view) {
		for (int i = 0; i < ENEMY_STATUS_BUFFS.length; i++) {
			ENEMY_STATUS_BUFFS[i].apply(this, view.enemy, view.statusBuffs.get(i));
		}
	}

	private void hideStatusBuffs(EnemyView view) {
		for (DeckBuffButton buff : view.statusBuffs) {
			buff.visible = false;
		}
	}

	private void refreshTraitBuffs(EnemyView view) {
		for (int i = 0; i < ENEMY_TRAIT_BUFFS.length; i++) {
			ENEMY_TRAIT_BUFFS[i].apply(this, view.enemy, view.traitBuffs.get(i));
		}
	}

	private void hideTraitBuffs(EnemyView view) {
		for (DeckBuffButton buff : view.traitBuffs) {
			buff.visible = false;
		}
	}

	private boolean hasAliveRatPartner(DeckCombatEnemy enemy) {
		if (enemy == null || !enemy.alive()) return false;
		DeckEnemy partner = null;
		if (enemy.kind == DeckEnemy.RAT_JAGGED) {
			partner = DeckEnemy.RAT_SMOOTH;
		} else if (enemy.kind == DeckEnemy.RAT_SMOOTH) {
			partner = DeckEnemy.RAT_JAGGED;
		}
		if (partner == null) return false;
		for (DeckCombatEnemy other : combat.enemies) {
			if (other != enemy && other.kind == partner && other.alive()) return true;
		}
		return false;
	}

	private void updatePlayerHpUi() {
		int hp = Math.max(0, DeckBuilderRun.playerHP);
		playerStatus.text(Dungeon.hero.heroClass.title() + "  " + hp + "/" + DeckBuilderRun.playerHT);
		playerHp.size(ACTOR_HP_W * hp / (float)DeckBuilderRun.playerHT, ACTOR_HP_H);

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
	}

	private void hideEnemyUI(EnemyView view) {
		if (view == null) return;
		view.name.text(view.enemy.name + "  0/" + view.enemy.ht);
		view.hp.size(0, ACTOR_HP_H);
		view.name.visible = false;
		view.intent.visible = false;
		view.hpBg.visible = false;
		view.hp.visible = false;
		view.area.active = false;
		hideStatusBuffs(view);
		hideTraitBuffs(view);
		view.shield.visible = false;
		view.shieldLabel.visible = false;
		view.targetMark.visible = false;
	}

	private void refresh() {
		if (rewardOpen) return;
		combatLocked = false;
		for (CardButton button : cardButtons) {
			button.destroy();
			remove(button);
		}
		cardButtons.clear();

		updatePlayerHpUi();
		DeckCombatEnemy target = combat.target();
		ensureEnemyViews();
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
		refreshPlayerBuffs();

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

	private boolean hasWandInHand(int staffIndex) {
		for (int i = 0; i < combat.hand.size(); i++) {
			if (i == staffIndex) continue;
			int code = combat.hand.get(i);
			if (DeckCard.maxCharge(code) > 0) {
				return true;
			}
		}
		return false;
	}

	private void showWandSelectionBanner() {
		int w = Camera.main.width;
		int h = Camera.main.height;
		int shadeH = 30;
		wandSelectionShade.size(w, shadeH);
		wandSelectionShade.y = (h - shadeH) / 2f;
		wandSelectionShade.visible = true;

		wandSelectionAccent.color(0xFFFFD84D);
		wandSelectionAccent.y = wandSelectionShade.y + shadeH;
		wandSelectionAccent.visible = true;

		wandSelectionTitle.hardlight(0xFFFFD84D);
		wandSelectionTitle.setPos((w - wandSelectionTitle.width()) / 2f, wandSelectionShade.y + (shadeH - wandSelectionTitle.height()) / 2f);
		wandSelectionTitle.visible = true;

		wandSelectionSubtitle.visible = false;

		selectionBackdropArea.active = true;
	}

	private void hideWandSelectionBanner() {
		wandSelectionShade.visible = false;
		wandSelectionAccent.visible = false;
		wandSelectionTitle.visible = false;
		wandSelectionSubtitle.visible = false;
		selectionBackdropArea.active = false;
	}

	private void showPureSelectionBanner() {
		int w = Camera.main.width;
		int h = Camera.main.height;
		int shadeH = 44;
		pureSelectionShade.size(w, shadeH);
		pureSelectionShade.y = (h - shadeH) / 2f;
		pureSelectionShade.visible = true;

		pureSelectionAccent.color(0xFFFFD84D);
		pureSelectionAccent.y = pureSelectionShade.y + shadeH;
		pureSelectionAccent.visible = true;

		pureSelectionTitle.text("소멸할 카드를 최대 " + pureMaxSelect + "장 선택하세요.");
		pureSelectionTitle.hardlight(0xFFFFD84D);
		pureSelectionTitle.setPos((w - pureSelectionTitle.width()) / 2f, pureSelectionShade.y + 6);
		pureSelectionTitle.visible = true;

		pureSelectionSubtitle.visible = false;

		pureSelectorConfirm.text("확인 (0/" + pureMaxSelect + ")");
		pureSelectorConfirm.setRect((w - 90) / 2f, pureSelectionTitle.bottom() + 4, 90, 14);
		pureSelectorConfirm.visible = true;

		selectionBackdropArea.active = true;
	}

	private void hidePureSelectionBanner() {
		pureSelectionShade.visible = false;
		pureSelectionAccent.visible = false;
		pureSelectionTitle.visible = false;
		pureSelectionSubtitle.visible = false;
		pureSelectorConfirm.visible = false;
		selectionBackdropArea.active = false;
	}

	private void updatePureConfirmButton() {
		if (gamblerBrewActive) {
			pureSelectorConfirm.text("확인 (" + pureSelectedIndices.size() + "장 교환)");
		} else {
			pureSelectorConfirm.text("확인 (" + pureSelectedIndices.size() + "/" + pureMaxSelect + ")");
		}
	}

	private void showGamblerBrewBanner() {
		int w = Camera.main.width;
		int h = Camera.main.height;
		int shadeH = 44;
		pureSelectionShade.size(w, shadeH);
		pureSelectionShade.y = (h - shadeH) / 2f;
		pureSelectionShade.visible = true;

		pureSelectionAccent.color(0xFFFFD84D);
		pureSelectionAccent.y = pureSelectionShade.y + shadeH;
		pureSelectionAccent.visible = true;

		pureSelectionTitle.text("버릴 카드를 선택하세요.");
		pureSelectionTitle.hardlight(0xFFFFD84D);
		pureSelectionTitle.setPos((w - pureSelectionTitle.width()) / 2f, pureSelectionShade.y + 6);
		pureSelectionTitle.visible = true;

		pureSelectionSubtitle.visible = false;

		pureSelectorConfirm.text("확인 (0장 교환)");
		pureSelectorConfirm.setRect((w - 100) / 2f, pureSelectionTitle.bottom() + 4, 100, 14);
		pureSelectorConfirm.visible = true;

		selectionBackdropArea.active = true;
	}

	private void confirmGamblerBrew() {
		int count = pureSelectedIndices.size();
		java.util.Collections.sort(pureSelectedIndices, java.util.Collections.reverseOrder());
		for (int idx : pureSelectedIndices) {
			int code = combat.hand.get(idx);
			combat.hand.remove(idx);
			combat.discardPile.add(code);
		}
		pureSelectedIndices.clear();
		gamblerBrewActive = false;
		hidePureSelectionBanner();
		// Restore pure selection banner accent color
		pureSelectionAccent.color(0xFF7EC8D4);
		pureSelectionTitle.hardlight(0xFF7EC8D4);
		if (count > 0) {
			DeckBuilderRun.removePotion(gamblerBrewSlot);
			if (runHud != null) runHud.refresh();
			combat.draw(count);
			spawnDrawPileEffects(count, 0.05f);
		}
		log("도박꾼의 영액: " + count + "장을 버리고 " + count + "장을 뽑았습니다.");
		saveCombatState();
		if (!resolveAutoPlayResultsAfterDraw()) {
			if (combat.won()) {
				showReward();
			} else {
				refresh();
			}
		}
	}

	private void confirmPureSelection() {
		Map<Integer, float[]> idxToPos = new LinkedHashMap<>();
		for (int idx : pureSelectedIndices) {
			float ex = idx < cardButtons.size() ? cardButtons.get(idx).centerX() : playerCenterX();
			float ey = idx < cardButtons.size() ? cardButtons.get(idx).centerY() : playerCenterY();
			idxToPos.put(idx, new float[]{ex, ey});
		}
		int adjustedPureIndex = pureHandIndex;
		for (int idx : pureSelectedIndices) {
			if (idx < pureHandIndex) adjustedPureIndex--;
		}
		java.util.Collections.sort(pureSelectedIndices, java.util.Collections.reverseOrder());
		for (int idx : pureSelectedIndices) {
			int code = combat.hand.get(idx);
			combat.hand.remove(idx);
			combat.exhaustPile.add(code);
			float[] pos = idxToPos.get(idx);
			addEffect(new ExhaustEffect(pos[0], pos[1]));
			Sample.INSTANCE.play(Assets.Sounds.BURNING);
		}
		pureSelectedIndices.clear();
		selectingForPure = false;
		hidePureSelectionBanner();
		executePlayCard(adjustedPureIndex, -1);
	}

	private void playCard(int index) {
		if (combatLocked || index < 0 || index >= combat.hand.size()) {
			return;
		}
		int cardCode = combat.hand.get(index);
		DeckCard card = DeckCard.byCode(cardCode);
		if (card == DeckCard.MAGE_STAFF && hasWandInHand(index)) {
			selectingWandForStaff = true;
			mageStaffHandIndex = index;
			showWandSelectionBanner();
			refresh();
			return;
		}
		if (card == DeckCard.PURE && combat.hand.size() > 1) {
			selectingForPure = true;
			pureHandIndex = index;
			pureSelectedIndices.clear();
			pureMaxSelect = DeckCard.upgradeLevel(cardCode) > 0 ? 5 : 3;
			showPureSelectionBanner();
			refresh();
			return;
		}
		executePlayCard(index, -1);
	}

	private void executePlayCard(int index, int targetWandIndex) {
		if (index < 0 || index >= combat.hand.size()) {
			return;
		}
		int cardCode = combat.hand.get(index);
		DeckCard card = DeckCard.byCode(cardCode);
		int logCardCode = card.effectiveCodeForPlay(cardCode, combat, index);
		float startX = index < cardButtons.size() ? cardButtons.get(index).centerX() : playerCenterX();
		float startY = index < cardButtons.size() ? cardButtons.get(index).centerY() : playerCenterY();
		DeckCard wandCard = null;
		if (targetWandIndex >= 0 && targetWandIndex < combat.hand.size()) {
			wandCard = DeckCard.byCode(combat.hand.get(targetWandIndex));
		}
		final ArrayList<Integer> headbuttDiscardSnapshot = (card == DeckCard.HEADBUTT) ? new ArrayList<>(combat.discardPile) : null;
		DeckPlayResult result = combat.play(index, targetWandIndex);
		if (!result.played) {
            log("에너지가 부족합니다.");
			return;
		}

		saveCombatState();
		combatLocked = true;

		if (card.hasKeyword(cardCode, DeckCardKeyword.EXHAUST)) {
			final float ex = startX;
			final float ey = startY;
			addEffect(new DelayedActionEffect(0.1f, new Runnable() {
				@Override public void run() {
					addEffect(new ExhaustEffect(ex, ey));
					Sample.INSTANCE.play(Assets.Sounds.BURNING);
				}
			}));
		}

		if (card.type == DeckCardType.POWER) {
			Sample.INSTANCE.play(Assets.Sounds.CHARMS);
		}

		if (card == DeckCard.SCORPION_THROW) {
			Sample.INSTANCE.play(Assets.Sounds.PLANT);
		} else if (card == DeckCard.SHIV) {
			Sword.giorno();
		} else if (card == DeckCard.ROTATING_NAIL) {
			Sample.INSTANCE.play(Assets.Sounds.EVOKE);
		}

		if (card == DeckCard.MAGE_STAFF && wandCard != null) {
			final float fx = startX;
			final float fy = startY;
			final ArrayList<DeckPlayResult.Hit> hits = result.hits;
			final DeckCard finalWandCard = wandCard;
			for (int i = 0; i < hits.size(); i++) {
				final DeckPlayResult.Hit hit = hits.get(i);
				final float delay = i * 0.18f;
				final boolean isFirst = (i == 0);
				addEffect(new DelayedActionEffect(delay, new Runnable() {
					@Override
					public void run() {
						spawnCardAttack(isFirst ? DeckCard.MAGE_STAFF : finalWandCard, fx, fy, hit);
					}
				}));
			}
			final float totalDelay = hits.size() * 0.18f + 0.22f;
			log(card.title(logCardCode) + ": " + cardRulesText(card, logCardCode));
			if (combat.playerDead()) {
				updatePlayerHpUi();
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

		if (card == DeckCard.HEADBUTT) {
			float finishDelay = 0f;
			for (DeckPlayResult.Hit hit : result.hits) {
				if (hit.damage > 0) {
					spawnCardAttack(card, startX, startY, hit);
					finishDelay = Math.max(finishDelay, 0.38f);
				}
			}
			log(card.title(logCardCode) + ": " + cardRulesText(card, logCardCode));
			if (combat.playerDead()) {
				updatePlayerHpUi();
				final float d = finishDelay;
				addEffect(new DelayedActionEffect(d > 0f ? d : 0.45f, new Runnable() {
					@Override
					public void run() {
						playDeath(playerSprite);
						finishRunDeath();
					}
				}));
			} else if (combat.won()) {
				final float d = finishDelay;
				addEffect(new DelayedActionEffect(Math.max(0.9f, d + 0.3f), new Runnable() {
					@Override
					public void run() {
						showReward();
					}
				}));
			} else {
				final float d = finishDelay;
				addEffect(new DelayedActionEffect(Math.max(0.1f, d), new Runnable() {
					@Override
					public void run() {
						if (headbuttDiscardSnapshot == null || headbuttDiscardSnapshot.isEmpty()) {
							refresh();
						} else {
							showHeadbuttDiscardSelectWindow(headbuttDiscardSnapshot, 0);
						}
					}
				}));
			}
			return;
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
			log(card.title(logCardCode) + ": " + cardRulesText(card, logCardCode));
			if (combat.playerDead()) {
				updatePlayerHpUi();
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

		float finishDelay = 0f;
		if (!result.shuffles.isEmpty()) {
			finishDelay = Math.max(finishDelay, spawnShuffleEffects(result, startX, startY));
		}

		for (DeckPlayResult.Hit hit : result.hits) {
			if (hit.damage > 0) {
				spawnCardAttack(card, startX, startY, hit);
				finishDelay = Math.max(finishDelay, 0.38f);
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
		if (result.draw > 0) {
			finishDelay = Math.max(finishDelay, spawnDrawPileEffects(result.draw, 0.08f));
		}
		finishDelay = Math.max(finishDelay, spawnAutoPlayEffects());
		log(card.title(logCardCode) + ": " + cardRulesText(card, logCardCode));
		if (combat.playerDead()) {
			updatePlayerHpUi();
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
			addEffect(new DelayedActionEffect(Math.max(0.9f, finishDelay + 0.3f), new Runnable() {
				@Override
				public void run() {
					showReward();
				}
			}));
		} else if (finishDelay > 0f) {
			addEffect(new DelayedActionEffect(finishDelay, new Runnable() {
				@Override
				public void run() {
					if (combat.won()) {
						showReward();
					} else {
						refresh();
					}
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

			@Override
			public void onDiscardPotion(int slot, DeckPotion potion) {
				discardPotion(slot, potion);
			}

			@Override
			public boolean canUsePotion() {
				return !rewardOpen;
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

		switch (potion) {
			case HASTE:
				DeckBuilderRun.removePotion(slot);
				if (runHud != null) runHud.refresh();
				Sample.INSTANCE.play(Assets.Sounds.DRINK);
				combat.draw(3);
				spawnDrawPileEffects(3, 0.05f);
                log(potion.title + ": 카드를 3장 뽑았습니다.");
				saveCombatState();
				if (!resolveAutoPlayResultsAfterDraw()) {
					if (combat.won()) {
						showReward();
					} else {
						refresh();
					}
				}
				break;
			case FIRE:
				DeckBuilderRun.removePotion(slot);
				if (runHud != null) runHud.refresh();
				combatLocked = true;
				Sample.INSTANCE.play(Assets.Sounds.SHATTER);
				Sample.INSTANCE.play(Assets.Sounds.BURNING);
				for (int i = 0; i < combat.enemies.size(); i++) {
					DeckCombatEnemy enemy = combat.enemies.get(i);
					if (!enemy.alive()) continue;
					int dealt = combat.damageEnemy(enemy, 10, false);
					EnemyView view = enemyView(i);
					if (view != null) {
						addEffect(new ImpactEffect(enemyCenterX(view), enemyCenterY(view), 0xFFFF7A35));
						spawnFloatingText("-" + dealt, enemyCenterX(view), enemyCenterY(view) - 16, 0xFFFF705A);
						view.name.text(view.enemy.name + "  " + view.enemy.hp + "/" + view.enemy.ht);
						view.hp.size(ACTOR_HP_W * view.enemy.hp / (float)view.enemy.ht, ACTOR_HP_H);
						if (!enemy.alive()) {
							playDeath(view.sprite);
							hideEnemyUI(view);
						}
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
				DeckBuilderRun.removePotion(slot);
				if (runHud != null) runHud.refresh();
				Sample.INSTANCE.play(Assets.Sounds.DRINK);
				combat.playerStrength += 2;
                spawnFloatingText("공격력 +2", playerCenterX(), playerCenterY() - 24, 0xFFFFD84D);
                log(potion.title + ": 공격력을 2 얻었습니다.");
				saveCombatState();
				refresh();
				break;
			case GAMBLERS_BREW:
				Sample.INSTANCE.play(Assets.Sounds.DRINK);
				gamblerBrewActive = true;
				gamblerBrewSlot = slot;
				pureSelectedIndices.clear();
				showGamblerBrewBanner();
				refresh();
				break;
		}
	}

	private void discardPotion(int slot, DeckPotion potion) {
		if (potion == null) return;
		DeckBuilderRun.removePotion(slot);
		if (runHud != null) runHud.refresh();
		Sample.INSTANCE.play(Assets.Sounds.CLICK);
		log(potion.title + ": 버렸습니다.");
		saveCombatState();
		if (!combatLocked && !rewardOpen) refresh();
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
		positionPlayerBuffs();
	}

	private void refreshPlayerBuffs() {
		for (int i = 0; i < PLAYER_STATUS_BUFFS.length; i++) {
			PLAYER_STATUS_BUFFS[i].apply(this, combat, playerBuffs.get(i));
		}
	}

	private void positionPlayerBuffs() {
		float buffWidth = 0;
		for (DeckBuffButton buff : playerBuffs) {
			if (buff.visible) buffWidth += buff.width() + 2;
		}
		if (buffWidth <= 0) return;
		buffWidth -= 2;
		float buffX = playerHpBg.x + ACTOR_HP_W / 2f - buffWidth / 2f;
		float buffY = playerStatus.top() - (playerBuffs.isEmpty() ? 7 : playerBuffs.get(0).height()) - 2;
		for (DeckBuffButton buff : playerBuffs) {
			if (!buff.visible) continue;
			buff.setPos(buffX, buffY);
			buffX += buff.width() + 2;
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
			DeckBuffButton[] buffs = enemyBuffs(view);
			for (DeckBuffButton buff : buffs) {
				if (buff.visible) buffWidth += buff.width() + 2;
			}
			if (buffWidth > 0) {
				buffWidth -= 2;
				float buffX = cx - buffWidth / 2f;
				float buffY = nameY - 9;
				for (DeckBuffButton buff : buffs) {
					if (buff.visible) {
						buff.setPos(buffX, buffY);
						buffX += buff.width() + 2;
					}
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

	private String enemyTurnLog(int damage) {
		int slimyCount = 0;
		for (DeckBuilderCombat.EnemyAction action : combat.lastEnemyActions) {
			if (action.shuffledCard == DeckCard.SLIMY) {
				slimyCount += action.shuffledCount;
			}
		}
		int statusDamage = combat.lastTurnEndStatusDamage;
		int enemyDamage = Math.max(0, damage - statusDamage);
		String text = enemyDamage > 0 ? "적들이 총 " + enemyDamage + " 피해를 입혔습니다." : "피해를 막았습니다.";
		if (statusDamage > 0) {
			text += " 독침 " + combat.lastTurnEndPoisonDarts + "장으로 " + statusDamage + " 피해를 받았습니다.";
		}
		if (slimyCount > 0) {
			text += " 점액투성이 " + slimyCount + "장을 버린 카드 더미에 섞어 넣었습니다.";
		}
		return text;
	}

	private DeckBuffButton[] enemyBuffs(EnemyView view) {
		ArrayList<DeckBuffButton> buffs = new ArrayList<>();
		buffs.addAll(view.statusBuffs);
		buffs.addAll(view.traitBuffs);
		return buffs.toArray(new DeckBuffButton[0]);
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

	private boolean resolveAutoPlayResultsAfterDraw() {
		float delay = spawnAutoPlayEffects();
		if (delay <= 0f) return false;
		combatLocked = true;
		addEffect(new DelayedActionEffect(delay, new Runnable() {
			@Override
			public void run() {
				if (combat.won()) {
					showReward();
				} else if (combat.playerDead()) {
					updatePlayerHpUi();
					playDeath(playerSprite);
					finishRunDeath();
				} else {
					refresh();
				}
			}
		}));
		return true;
	}

	private float spawnShuffleEffects(DeckPlayResult result, float startX, float startY) {
		float deckX = deckCounterBg == null ? Camera.main.width * 0.12f : deckCounterBg.x + deckCounterBg.width() / 2f;
		float deckY = deckCounterBg == null ? handY + CARD_H * 0.75f : deckCounterBg.y + deckCounterBg.height() / 2f;
		int sequence = 0;
		for (DeckPlayResult.Shuffle shuffle : result.shuffles) {
			for (int i = 0; i < shuffle.count; i++) {
				final DeckCard shuffledCard = shuffle.card;
				final float delay = sequence * 0.08f;
				final float sx = startX + (i - (shuffle.count - 1) / 2f) * 7f;
				final float sy = startY - 4f;
				addEffect(new DelayedActionEffect(delay, new Runnable() {
					@Override
					public void run() {
						Sample.INSTANCE.play(Assets.Sounds.MISS, 0.6f, 1.15f);
						addEffect(new ShuffleIntoDrawPileEffect(shuffledCard, sx, sy, deckX, deckY));
					}
				}));
				sequence++;
			}
		}
		return sequence == 0 ? 0f : sequence * 0.08f + 0.38f;
	}

	private float spawnTurnPileEffects(ArrayList<Float> discardStartsX, ArrayList<Float> discardStartsY, boolean shuffled, int drawCount) {
		float deckX = deckCounterBg == null ? Camera.main.width * 0.12f : deckCounterBg.x + deckCounterBg.width() / 2f;
		float deckY = deckCounterBg == null ? handY + CARD_H * 0.75f : deckCounterBg.y + deckCounterBg.height() / 2f;
		float discardX = discardCounterBg == null ? Camera.main.width * 0.88f : discardCounterBg.x + discardCounterBg.width() / 2f;
		float discardY = discardCounterBg == null ? handY + CARD_H * 0.75f : discardCounterBg.y + discardCounterBg.height() / 2f;
		float handX = Camera.main.width / 2f;
		float handCenterY = handY + CARD_H / 2f;
		int discardVisuals = Math.min(5, discardStartsX.size());
		for (int i = 0; i < discardVisuals; i++) {
			final float sx = discardStartsX.get(i);
			final float sy = discardStartsY.get(i);
			final float delay = i * 0.035f;
			addEffect(new DelayedActionEffect(delay, new Runnable() {
				@Override
				public void run() {
					addEffect(new PileCardFlowEffect(sx, sy, discardX, discardY, 0xFFFFC07A));
				}
			}));
		}
		if (shuffled) {
			addEffect(new DelayedActionEffect(0.22f, new Runnable() {
				@Override
				public void run() {
					Sample.INSTANCE.play(Assets.Sounds.PLANT, 0.55f, 0.95f);
					addEffect(new PileCardFlowEffect(discardX, discardY, deckX, deckY, 0xFF9EE6FF));
				}
			}));
		}
		int drawVisuals = Math.min(5, drawCount);
		float drawDelay = shuffled ? 0.42f : 0.18f;
		for (int i = 0; i < drawVisuals; i++) {
			final float tx = handX + (i - (drawVisuals - 1) / 2f) * Math.min(CARD_W * 0.36f, 14f);
			final float delay = drawDelay + i * 0.045f;
			addEffect(new DelayedActionEffect(delay, new Runnable() {
				@Override
				public void run() {
					Sample.INSTANCE.play(Assets.Sounds.MISS, 0.7f, 1.0f);
					addEffect(new PileCardFlowEffect(deckX, deckY, tx, handCenterY, 0xFF8EDBFF));
				}
			}));
		}
		return drawVisuals > 0 ? drawDelay + drawVisuals * 0.045f + 0.34f : 0f;
	}

	private float spawnPendingTurnPileEffects() {
		for (int i = 0; i < pendingTransientExhaustX.size(); i++) {
			final float ex = pendingTransientExhaustX.get(i);
			final float ey = pendingTransientExhaustY.get(i);
			final float delay = i * 0.08f;
			addEffect(new DelayedActionEffect(delay, new Runnable() {
				@Override
				public void run() {
					addEffect(new ExhaustEffect(ex, ey));
					Sample.INSTANCE.play(Assets.Sounds.BURNING);
				}
			}));
		}
		pendingTransientExhaustX.clear();
		pendingTransientExhaustY.clear();

		if (pendingDiscardStartsX.isEmpty() && !pendingPileShuffle && pendingDrawVisuals <= 0) return 0f;
		float endTime = spawnTurnPileEffects(pendingDiscardStartsX, pendingDiscardStartsY, pendingPileShuffle, pendingDrawVisuals);
		pendingDiscardStartsX.clear();
		pendingDiscardStartsY.clear();
		pendingPileShuffle = false;
		pendingDrawVisuals = 0;
		return endTime;
	}

	private float spawnDrawPileEffects(int drawCount, float delayStart) {
		float deckX = deckCounterBg == null ? Camera.main.width * 0.12f : deckCounterBg.x + deckCounterBg.width() / 2f;
		float deckY = deckCounterBg == null ? handY + CARD_H * 0.75f : deckCounterBg.y + deckCounterBg.height() / 2f;
		float handX = Camera.main.width / 2f;
		float handCenterY = handY + CARD_H / 2f;
		int drawVisuals = Math.min(5, Math.max(0, drawCount));
		for (int i = 0; i < drawVisuals; i++) {
			final float tx = handX + (i - (drawVisuals - 1) / 2f) * Math.min(CARD_W * 0.36f, 14f);
			final float delay = delayStart + i * 0.045f;
			addEffect(new DelayedActionEffect(delay, new Runnable() {
				@Override
				public void run() {
					Sample.INSTANCE.play(Assets.Sounds.MISS, 0.7f, 1.0f);
					addEffect(new PileCardFlowEffect(deckX, deckY, tx, handCenterY, 0xFF8EDBFF));
				}
			}));
		}
		return drawVisuals == 0 ? 0f : delayStart + drawVisuals * 0.045f + 0.34f;
	}

	private float spawnAutoPlayEffects() {
		return spawnAutoPlayEffects(combat.lastAutoPlayResults);
	}

	private float spawnTurnEndAutoPlayEffects() {
		return spawnAutoPlayEffects(combat.lastTurnEndAutoPlayResults);
	}

	private float spawnAutoPlayEffects(ArrayList<DeckPlayResult> sourceResults) {
		if (sourceResults.isEmpty()) return 0f;
		ArrayList<DeckPlayResult> autoResults = new ArrayList<>(sourceResults);
		sourceResults.clear();
		float sourceX = deckCounterBg == null ? Camera.main.width * 0.12f : deckCounterBg.x + deckCounterBg.width() / 2f;
		float sourceY = deckCounterBg == null ? handY + CARD_H * 0.75f : deckCounterBg.y + deckCounterBg.height() / 2f;
		int sequence = 0;
		for (DeckPlayResult auto : autoResults) {
			if (!auto.played || auto.card == null) continue;
			for (DeckPlayResult.Hit hit : auto.hits) {
				if (hit.damage <= 0) continue;
				final DeckCard autoCard = auto.card;
				final DeckPlayResult.Hit autoHit = hit;
				final float delay = 0.16f + sequence * 0.16f;
				addEffect(new DelayedActionEffect(delay, new Runnable() {
					@Override
					public void run() {
						if (autoCard == DeckCard.ROTATING_NAIL) {
							Sample.INSTANCE.play(Assets.Sounds.EVOKE);
						}
						spawnCardAttack(autoCard, sourceX, sourceY, autoHit);
					}
				}));
				sequence++;
			}
		}
		return sequence == 0 ? 0f : 0.16f + sequence * 0.16f + 0.38f;
	}

	private void spawnCardAttack(final DeckCard card, float startX, float startY, final DeckPlayResult.Hit hit) {
		playAttack(playerSprite);
		final EnemyView target = enemyView(hit.enemyIndex);
		if (target == null) return;
		target.displayHp = Math.max(0, target.displayHp - hit.damage);
		final int capturedHp = target.displayHp;
		if (card == DeckCard.ROTATING_NAIL) {
			addEffect(new NailShotEffect(startX, startY, enemyCenterX(target), enemyCenterY(target)));
		} else if (card == DeckCard.MAGIC_MISSILE_WAND) {
			Sample.INSTANCE.play(Assets.Sounds.ZAP);
			addEffect(new WandShotEffect(startX, startY, enemyCenterX(target), enemyCenterY(target)));
		} else {
			addEffect(new FlyingCardEffect(card, startX, startY, enemyCenterX(target), enemyCenterY(target)));
		}
		addEffect(new DelayedActionEffect(0.22f, new Runnable() {
			@Override
			public void run() {
				target.hitTime = 0.22f;
				if (card == DeckCard.MAGIC_MISSILE_WAND) {
					Sample.INSTANCE.play(Assets.Sounds.HIT_MAGIC);
					addEffect(new ImpactEffect(enemyCenterX(target), enemyCenterY(target), 0xFFFFFFFF));
				} else {
					Sample.INSTANCE.play(Assets.Sounds.HIT_SLASH);
					addEffect(new ImpactEffect(enemyCenterX(target), enemyCenterY(target), card == DeckCard.ROTATING_NAIL ? 0xFFFFD0F0 : 0xFFFFC05A));
				}
				spawnFloatingText("-" + hit.damage, enemyCenterX(target), enemyCenterY(target) - 16, 0xFFFF705A);
				target.name.text(target.enemy.name + "  " + capturedHp + "/" + target.enemy.ht);
				target.hp.size(ACTOR_HP_W * capturedHp / (float)target.enemy.ht, ACTOR_HP_H);
				if (!target.enemy.alive()) {
					playDeath(target.sprite);
					hideEnemyUI(target);
				}
			}
		}));
	}

	private void spawnEnemyActions() {
		int attackOrdinal = 0;
		for (DeckBuilderCombat.EnemyAction action : combat.lastEnemyActions) {
			EnemyView view = enemyView(action.enemyIndex);
			if (view == null) continue;
			if (action.shuffledCard != null && action.shuffledCount > 0) {
				for (int i = 0; i < action.shuffledCount; i++) {
					final float delay = attackOrdinal * 0.08f + i * 0.07f;
					final float sx = enemyCenterX(view);
					final float sy = enemyCenterY(view);
					final float tx = discardCounterBg == null ? Camera.main.width * 0.88f : discardCounterBg.x + discardCounterBg.width() / 2f;
					final float ty = discardCounterBg == null ? handY + CARD_H * 0.75f : discardCounterBg.y + discardCounterBg.height() / 2f;
					final DeckCard shuffledCard = action.shuffledCard;
					addEffect(new DelayedActionEffect(delay, new Runnable() {
						@Override
						public void run() {
							Sample.INSTANCE.play(Assets.Sounds.PLANT, 0.6f, 1.05f);
							addEffect(new ShuffleIntoDrawPileEffect(shuffledCard, sx, sy, tx, ty));
						}
					}));
				}
				spawnFloatingText(action.shuffledCard.title(action.shuffledCard.code()) + " +" + action.shuffledCount,
						enemyCenterX(view), enemyCenterY(view) - 24, 0xFF88FF88);
			}
                if (action.label != null) {
                    spawnFloatingText(action.label, enemyCenterX(view), enemyCenterY(view) - 24, 0xFFFFD66B);
                    if (action.damage <= 0 && !action.blocked) {
                        Sample.INSTANCE.play(Assets.Sounds.CHARGEUP, 0.9f, 1.1f);
                        addEffect(new BuffEffect(enemyCenterX(view), enemyCenterY(view), 0xFFFFD66B));
                    }
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
						updatePlayerHpUi();
					} else {
						spawnShieldEffect(playerCenterX(), playerCenterY(), "BLOCK");
					}
				}
			}));
		}
	}

	private void spawnPoisonDartDamageEffect() {
		if (combat.lastTurnEndStatusDamage <= 0) return;
		playerHitTime = 0.22f;
		Sample.INSTANCE.play(Assets.Sounds.HIT_MAGIC, 0.9f, 0.85f);
		addEffect(new PoisonDartEffect(playerCenterX(), playerCenterY()));
		spawnFloatingText("독침 -" + combat.lastTurnEndStatusDamage, playerCenterX(), playerCenterY() - 22, 0xFF8CFF5A);
		updatePlayerHpUi();
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

	private void showHeadbuttDiscardSelectWindow(final ArrayList<Integer> snapshot, final int page) {
		final int total = snapshot.size();
		if (total == 0) { refresh(); return; }

		final int HB_CARD_W = 42;
		final int HB_CARD_H = 54;
		final int HB_CARD_GAP = 5;
		final int HB_CARDS_PER_PAGE = 4;

		final int maxPage = Math.max(0, (total - 1) / HB_CARDS_PER_PAGE);
		final int currentPage = Math.max(0, Math.min(page, maxPage));
		final int first = currentPage * HB_CARDS_PER_PAGE;
		final int count = Math.min(HB_CARDS_PER_PAGE, total - first);
		final int totalCardW = count * HB_CARD_W + (count - 1) * HB_CARD_GAP;
		final int width = Math.max(196, totalCardW + 20);

		final Window win = new Window() {
			@Override
			public void onBackPressed() {
				// Must select a card
			}
		};

		int pos = 7;

		RenderedTextBlock title = renderTextBlock("뽑을 카드 더미 맨 위에 올릴 카드 선택", 8);
		title.hardlight(Window.TITLE_COLOR);
		title.maxWidth(width - 14);
		title.setPos((width - title.width()) / 2f, pos);
		win.add(title);
		pos += (int) title.height() + 8;

		final int startX = (width - totalCardW) / 2;
		for (int i = 0; i < count; i++) {
			final int snapIndex = first + i;
			final int code = snapshot.get(snapIndex);
			final int col = i;
			CardViewButton cardBtn = new CardViewButton() {
				@Override protected DeckCard card() { return DeckCard.byCode(code); }
				@Override protected int cardCode() { return code; }
				@Override protected void onClick() {
					combat.discardPile.remove(snapIndex);
					combat.drawPile.add(0, code);
					win.hide();
					refresh();
				}
			};
			cardBtn.setRect(startX + col * (HB_CARD_W + HB_CARD_GAP), pos, HB_CARD_W, HB_CARD_H);
			win.add(cardBtn);
		}
		pos += HB_CARD_H + 9;

		if (maxPage > 0) {
			RedButton prev = new RedButton("이전", 6) {
				@Override protected void onClick() {
					win.hide();
					showHeadbuttDiscardSelectWindow(snapshot, currentPage - 1);
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
					showHeadbuttDiscardSelectWindow(snapshot, currentPage + 1);
				}
			};
			next.enable(currentPage < maxPage);
			next.setRect(width - 68, pos, 58, 18);
			win.add(next);
			pos += 23;
		}

		win.resize(width, pos + 4);
		addToFront(win);
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
		refreshEnemyViews();
		// 소멸은 이 전투에서만 카드를 제거하는 것. 다음 전투에서는 다시 사용 가능하므로 영구 삭제하지 않음.
		combat.exhaustPile.clear();
		saveCombatState();
		Sample.INSTANCE.play(Assets.Sounds.LEVELUP);
		boolean useFullRewardScreen = true;
		if (useFullRewardScreen) {
			DeckCombatRewardState rewards = DeckBuilderRun.combatRewardForCurrentNode(Statistics.deckBuilderMapNode);
			saveCombatState();
			showCombatRewardWindow(rewards);
			return;
		}
		final DeckCard[] rewards = DeckBuilderRun.rewardChoices();
		final Window reward = new RewardWindow();
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
		bringRunHudToFront();
	}

	private void showCombatRewardWindow(final DeckCombatRewardState rewards) {
		final Window reward = new RewardWindow();
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
				rewards.goldClaimed = true;
				if (runHud != null) runHud.refresh();
				Sample.INSTANCE.play(Assets.Sounds.GOLD);
                text.text("획득 완료: " + rewards.gold + " 골드");
				text.hardlight(0xFF9A9A9A);
				saveCombatState();
			}
		};
		goldRow.claimed = rewards.goldClaimed;
		goldRow.setRect(10, pos, width - 20, 24);
		reward.add(goldRow);
		pos += 29;

		for (int i = 0; rewards.relics != null && i < rewards.relics.length; i++) {
			final int relicIndex = i;
			final DeckRelic relic = rewards.relicAt(i);
			if (relic == null) continue;
			RewardRow relicRow = new RewardRow(NO_REWARD_ICON, relic.title) {
				@Override
				protected void onClick() {
					if (claimed) return;
					claimed = true;
					DeckBuilderRun.addRelic(relic);
					if (rewards.relicClaimed != null && relicIndex < rewards.relicClaimed.length) rewards.relicClaimed[relicIndex] = true;
                    text.text("획득 완료: " + relic.title);
					text.hardlight(0xFF9A9A9A);
					saveCombatState();
				}
			};
			relicRow.claimed = rewards.relicClaimed != null && i < rewards.relicClaimed.length && rewards.relicClaimed[i];
			relicRow.setRect(10, pos, width - 20, 24);
			reward.add(relicRow);
			pos += 29;
		}

		final DeckPotion rewardPotion = rewards.potion();
		if (rewardPotion != null) {
			RewardRow potionRow = new RewardRow(rewardPotion.image, rewardPotion.title) {
				@Override
				protected void onClick() {
					if (claimed) return;
					if (!DeckBuilderRun.addPotion(rewardPotion)) {
                        addToFront(new WndMessage("포션\n\n빈 포션 슬롯이 없습니다."));
						return;
					}
					claimed = true;
					rewards.potionClaimed = true;
					if (runHud != null) runHud.refresh();
                    text.text("획득 완료: " + rewardPotion.title);
					text.hardlight(0xFF9A9A9A);
					saveCombatState();
				}
			};
			potionRow.claimed = rewards.potionClaimed;
			potionRow.setRect(10, pos, width - 20, 24);
			reward.add(potionRow);
			pos += 29;
		}

        RewardRow cardRow = new RewardRow(com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet.WONDROUS_RESIN, "덱에 추가할 카드를 선택하세요") {
			@Override
			protected void onClick() {
				if (claimed) return;
				showCardRewardWindow(reward, rewards.cardChoices(), this, rewards);
			}
		};
		cardRow.claimed = rewards.cardClaimed;
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
		bringRunHudToFront();
	}

	private void showCardRewardWindow(final Window parent, final DeckCard[] cards, final RewardRow cardRow, final DeckCombatRewardState rewards) {
		final Window win = new RewardWindow();
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
					showCardTakeWindow(win, cardRow, card, rewards);
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
				rewards.cardClaimed = true;
                cardRow.text.text("카드 보상 건너뜀");
				cardRow.text.hardlight(0xFF9A9A9A);
				saveCombatState();
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
		bringRunHudToFront();
	}

    private void showCardTakeWindow(final Window cardWindow, final RewardRow cardRow, final DeckCard card, final DeckCombatRewardState rewards) {
		final Window win = new RewardWindow();
		int width = 170;
		int pos = 7;

		RenderedTextBlock title = renderTextBlock(cardDetailTitle(card, card.code()), 8);
		title.hardlight(Window.TITLE_COLOR);
		title.setPos((width - title.width()) / 2f, pos);
		win.add(title);
		pos += 16;

		RenderedTextBlock desc = renderTextBlock(DeckCardText.rulesAndKeywordText(card, card.code(), combat), 6);
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
				rewards.cardClaimed = true;
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
		bringRunHudToFront();
	}

	private void bringRunHudToFront() {
		if (runHud != null) {
			bringToFront(runHud);
			runHud.givePotionPointerPriority();
		}
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

	private boolean retainedAtEndTurn(int cardCode) {
		DeckCard card = DeckCard.byCode(cardCode);
		return card.hasKeyword(cardCode, DeckCardKeyword.RETAIN) || (card == DeckCard.SHIV && combat.shivRetain);
	}

	private void log(String text) {
		logText.text(text);
	}

	private String cardRulesText(DeckCard card, int cardCode) {
		return DeckCardText.rulesText(card, cardCode, combat);
	}

	private String cardDetailTitle(DeckCard card, int cardCode) {
		return DeckCardText.detailTitle(card, cardCode);
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
			return new GooSprite() {
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
		if (kind == DeckEnemy.HORUS) {
			return new CrabSprite() {
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
		if (kind == DeckEnemy.KHNUM) {
			return new AlbinoSprite() {
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
		if (kind == DeckEnemy.LARGE_SLIME || kind == DeckEnemy.MEDIUM_SLIME) {
			return new SwarmSprite() {
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
		if (kind == DeckEnemy.RAMPAGING_BULL) {
			CharSprite sprite = new GnollExileSprite() {
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
			sprite.scale.set(0.6f);
			return sprite;
		}
		if (kind == DeckEnemy.JUDGEMENT) {
			return new StatueSprite() {
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
		if (kind == DeckEnemy.CLASH) {
			return new PiranhaSprite() {
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
		if (selectingWandForStaff) {
			selectingWandForStaff = false;
			hideWandSelectionBanner();
			refresh();
			return;
		}
		if (selectingForPure) {
			selectingForPure = false;
			hidePureSelectionBanner();
			pureSelectedIndices.clear();
			refresh();
			return;
		}
		if (gamblerBrewActive) {
			confirmGamblerBrew();
			return;
		}
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

		public DeckBuffButton(int iconId, float r, float g, float b, String label, String desc) {
			this(iconId, label, desc);
			tint(r, g, b);
		}

		public DeckBuffButton tint(float r, float g, float b) {
			icon.hardlight(r, g, b);
			return this;
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

		public void setActive(boolean active) {
			this.stacks = active ? 1 : 0;
			text.visible = false;
			setSize(icon.width(), icon.height());
			visible = active;
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

	private interface PlayerBuffStacks {
		int stacks(DeckBuilderCombat combat);
	}

	private interface EnemyBuffStacks {
		int stacks(DeckCombatEnemy enemy);
	}

	private interface EnemyBuffCondition {
		boolean active(DeckBattleScene scene, DeckCombatEnemy enemy);
	}

	private static class CombatBuffSpec {
		private final int iconId;
		private final float r;
		private final float g;
		private final float b;
		private final boolean tinted;
		private final String label;
		private final String desc;

		private CombatBuffSpec(int iconId, String label, String desc) {
			this.iconId = iconId;
			this.r = 1f;
			this.g = 1f;
			this.b = 1f;
			this.tinted = false;
			this.label = label;
			this.desc = desc;
		}

		private CombatBuffSpec(int iconId, float r, float g, float b, String label, String desc) {
			this.iconId = iconId;
			this.r = r;
			this.g = g;
			this.b = b;
			this.tinted = true;
			this.label = label;
			this.desc = desc;
		}

		protected DeckBuffButton createButton(DeckBattleScene scene) {
			return tinted
					? scene.new DeckBuffButton(iconId, r, g, b, label, desc)
					: scene.new DeckBuffButton(iconId, label, desc);
		}
	}

	private static class PlayerBuffSpec extends CombatBuffSpec {
		private final PlayerBuffStacks stacks;
		private final boolean showStacks;

		private PlayerBuffSpec(int iconId, String label, String desc, PlayerBuffStacks stacks) {
			this(iconId, label, desc, stacks, true);
		}

		private PlayerBuffSpec(int iconId, String label, String desc, PlayerBuffStacks stacks, boolean showStacks) {
			super(iconId, label, desc);
			this.stacks = stacks;
			this.showStacks = showStacks;
		}

		private PlayerBuffSpec(int iconId, float r, float g, float b, String label, String desc, PlayerBuffStacks stacks) {
			super(iconId, r, g, b, label, desc);
			this.stacks = stacks;
			this.showStacks = true;
		}

		private void apply(DeckBattleScene scene, DeckBuilderCombat combat, DeckBuffButton button) {
			int value = stacks.stacks(combat);
			if (showStacks) {
				button.setStacks(value);
			} else {
				button.setActive(value > 0);
			}
		}
	}

	private static class EnemyStatusBuffSpec extends CombatBuffSpec {
		private final EnemyBuffStacks stacks;

		private EnemyStatusBuffSpec(int iconId, String label, String desc, EnemyBuffStacks stacks) {
			super(iconId, label, desc);
			this.stacks = stacks;
		}

		private EnemyStatusBuffSpec(int iconId, float r, float g, float b, String label, String desc, EnemyBuffStacks stacks) {
			super(iconId, r, g, b, label, desc);
			this.stacks = stacks;
		}

		private void apply(DeckBattleScene scene, DeckCombatEnemy enemy, DeckBuffButton button) {
			button.setStacks(stacks.stacks(enemy));
		}
	}

	private static class EnemyBuffSpec extends CombatBuffSpec {
		private final EnemyBuffCondition condition;

		private EnemyBuffSpec(int iconId, String label, String desc, EnemyBuffCondition condition) {
			super(iconId, label, desc);
			this.condition = condition;
		}

		private EnemyBuffSpec(int iconId, float r, float g, float b, String label, String desc, EnemyBuffCondition condition) {
			super(iconId, r, g, b, label, desc);
			this.condition = condition;
		}

		private void apply(DeckBattleScene scene, DeckCombatEnemy enemy, DeckBuffButton button) {
			button.setActive(condition.active(scene, enemy));
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
		private final ArrayList<DeckBuffButton> statusBuffs = new ArrayList<>();
		private final ArrayList<DeckBuffButton> traitBuffs = new ArrayList<>();
		private float baseX;
		private float baseY;
		private float hitTime;
		private float attackTime;
		private int displayHp;

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

	private class NailShotEffect extends BattleEffect {

		private final float sx;
		private final float sy;
		private final float tx;
		private final float ty;
		private final ColorBlock trailOuter;
		private final ColorBlock trailInner;

		private NailShotEffect(float sx, float sy, float tx, float ty) {
			super(0.28f);
			this.sx = sx;
			this.sy = sy;
			this.tx = tx;
			this.ty = ty;

			trailOuter = new ColorBlock(1, 1, 0xFFFF65C8);
			add(trailOuter);
			trailInner = new ColorBlock(1, 1, 0xFFFFFBD0);
			add(trailInner);

			updateEffect(0);
		}

		private void getArcPos(float progress, float[] out) {
			float clamped = Math.max(0f, Math.min(1f, progress));
			float e = clamped * clamped * (3f - 2f * clamped);
			out[0] = sx + (tx - sx) * e;
			out[1] = sy + (ty - sy) * e - (float)Math.sin(clamped * Math.PI) * 9f;
		}

		@Override
		protected void updateEffect(float p) {
			float alpha = 1f - Math.max(0, p - 0.74f) / 0.26f;

			float[] pos = new float[2];
			getArcPos(p, pos);

			float pTrail = p - 0.12f;
			float[] posTrail = new float[2];
			getArcPos(pTrail, posTrail);

			float tdx = pos[0] - posTrail[0];
			float tdy = pos[1] - posTrail[1];
			float dist = (float)Math.sqrt(tdx * tdx + tdy * tdy);
			float trailAngle = (float)(Math.atan2(tdy, tdx) * 180f / Math.PI);

			float midX = (pos[0] + posTrail[0]) / 2f;
			float midY = (pos[1] + posTrail[1]) / 2f;

			trailOuter.x = midX - dist / 2f;
			trailOuter.y = midY - 1.25f;
			trailOuter.size(dist, 2.5f);
			trailOuter.angle = trailAngle;
			trailOuter.am = alpha * 0.6f;

			trailInner.x = midX - (dist * 0.8f) / 2f;
			trailInner.y = midY - 0.5f;
			trailInner.size(dist * 0.8f, 1f);
			trailInner.angle = trailAngle;
			trailInner.am = alpha * 0.8f;
		}
	}

	private class WandShotEffect extends BattleEffect {

		private final float sx;
		private final float sy;
		private final float tx;
		private final float ty;
		private final ColorBlock inner;
		private final ColorBlock outer;

		private WandShotEffect(float sx, float sy, float tx, float ty) {
			super(0.28f);
			this.sx = sx;
			this.sy = sy;
			this.tx = tx;
			this.ty = ty;

			outer = new ColorBlock(6, 6, 0xFFFFFFFF);
			add(outer);
			inner = new ColorBlock(2, 2, 0xFFFFFFFF);
			add(inner);

			updateEffect(0);
		}

		@Override
		protected void updateEffect(float p) {
			float e = p * p * (3f - 2f * p);
			float cx = sx + (tx - sx) * e;
			float cy = sy + (ty - sy) * e - (float)Math.sin(p * Math.PI) * 14f;
			float alpha = 1f - Math.max(0, p - 0.72f) / 0.28f;

			outer.x = cx - 3;
			outer.y = cy - 3;
			outer.am = alpha;

			inner.x = cx - 1;
			inner.y = cy - 1;
			inner.am = alpha;
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
			art = new ItemSprite(card.icon());
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

	private class ShuffleIntoDrawPileEffect extends BattleEffect {

		private final DeckCard card;
		private final float sx;
		private final float sy;
		private final float tx;
		private final float ty;
		private final ColorBlock edge;
		private final ColorBlock face;
		private final ColorBlock streakA;
		private final ColorBlock streakB;
		private final ItemSprite art;

		private ShuffleIntoDrawPileEffect(DeckCard card, float sx, float sy, float tx, float ty) {
			super(0.38f);
			this.card = card;
			this.sx = sx;
			this.sy = sy;
			this.tx = tx;
			this.ty = ty;
			edge = new ColorBlock(1, 1, card.type.borderColor);
			add(edge);
			face = new ColorBlock(1, 1, card.deckClass == null || !card.reward ? card.rarity.faceColor : card.classFaceColor());
			add(face);
			streakA = new ColorBlock(1, 1, 0xFFB6F2FF);
			add(streakA);
			streakB = new ColorBlock(1, 1, 0xFFFF9AE8);
			add(streakB);
			art = new ItemSprite(card.icon());
			art.scale.set(1.0f);
			updateEffect(0);
			add(art);
		}

		@Override
		protected void updateEffect(float p) {
			float e = p * p * (3f - 2f * p);
			float arc = (float)Math.sin(p * Math.PI) * 18f;
			float cx = sx + (tx - sx) * e;
			float cy = sy + (ty - sy) * e - arc;
			float alpha = 1f - Math.max(0, p - 0.78f) / 0.22f;
			float scale = 1f - 0.42f * p;

			edge.x = cx - 15 * scale;
			edge.y = cy - 20 * scale;
			edge.size(30 * scale, 40 * scale);
			edge.angle = p * 420f;
			edge.am = alpha * 0.92f;

			face.x = cx - 13 * scale;
			face.y = cy - 18 * scale;
			face.size(26 * scale, 36 * scale);
			face.angle = edge.angle;
			face.am = alpha * 0.86f;

			streakA.x = cx - 22;
			streakA.y = cy - 1;
			streakA.size(44 * (1f - p * 0.35f), 2);
			streakA.angle = edge.angle + 28;
			streakA.am = alpha * 0.75f;

			streakB.x = cx - 16;
			streakB.y = cy + 6;
			streakB.size(32 * (1f - p * 0.35f), 2);
			streakB.angle = edge.angle - 35;
			streakB.am = alpha * 0.65f;

			art.scale.set(scale);
			art.x = cx - art.width() * art.scale.x / 2f;
			art.y = cy - art.height() * art.scale.y / 2f;
			art.angle = edge.angle * 0.35f;
			art.am = alpha;
		}
	}

	private class PileCardFlowEffect extends BattleEffect {

		private final float sx;
		private final float sy;
		private final float tx;
		private final float ty;
		private final ColorBlock edge;
		private final ColorBlock face;
		private final int color;

		private PileCardFlowEffect(float sx, float sy, float tx, float ty, int color) {
			super(0.34f);
			this.sx = sx;
			this.sy = sy;
			this.tx = tx;
			this.ty = ty;
			this.color = color;
			edge = new ColorBlock(1, 1, color);
			add(edge);
			face = new ColorBlock(1, 1, 0xFF242421);
			add(face);
			updateEffect(0);
		}

		@Override
		protected void updateEffect(float p) {
			float e = p * p * (3f - 2f * p);
			float cx = sx + (tx - sx) * e;
			float cy = sy + (ty - sy) * e - (float)Math.sin(p * Math.PI) * 12f;
			float alpha = 1f - Math.max(0, p - 0.76f) / 0.24f;
			float scale = 0.72f - 0.18f * p;
			edge.x = cx - 10 * scale;
			edge.y = cy - 14 * scale;
			edge.size(20 * scale, 28 * scale);
			edge.angle = 18f + p * 240f;
			edge.am = alpha * 0.95f;
			edge.hardlight(color);
			face.x = cx - 8 * scale;
			face.y = cy - 12 * scale;
			face.size(16 * scale, 24 * scale);
			face.angle = edge.angle;
			face.am = alpha * 0.9f;
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

	private class PoisonDartEffect extends BattleEffect {

		private final ColorBlock aura;
		private final ColorBlock dartA;
		private final ColorBlock dartB;
		private final ColorBlock dartC;
		private final float x;
		private final float y;

		private PoisonDartEffect(float x, float y) {
			super(0.34f);
			this.x = x;
			this.y = y;
			aura = new ColorBlock(1, 1, 0xFF5AFF62);
			aura.am = 0.45f;
			add(aura);
			dartA = new ColorBlock(1, 1, 0xFFB9FF7A);
			dartB = new ColorBlock(1, 1, 0xFF63E85D);
			dartC = new ColorBlock(1, 1, 0xFF2C8F3F);
			add(dartA);
			add(dartB);
			add(dartC);
		}

		@Override
		protected void updateEffect(float p) {
			float alpha = 1f - p;
			float spread = p * 18f;
			aura.x = x - 13 - spread / 2f;
			aura.y = y - 13 - spread / 2f;
			aura.size(26 + spread, 26 + spread);
			aura.am = alpha * 0.28f;

			positionDart(dartA, x - 18 + spread, y - 16 + spread * 0.35f, 26, 3, -28, alpha);
			positionDart(dartB, x + 14 - spread * 0.7f, y - 12 + spread * 0.5f, 22, 3, 38, alpha * 0.9f);
			positionDart(dartC, x - 6 + spread * 0.25f, y + 16 - spread, 20, 2, 84, alpha * 0.8f);
		}

		private void positionDart(ColorBlock dart, float cx, float cy, float w, float h, float angle, float alpha) {
			dart.x = cx - w / 2f;
			dart.y = cy - h / 2f;
			dart.size(w, h);
			dart.angle = angle;
			dart.am = alpha;
		}
	}

	private class BuffEffect extends BattleEffect {

		private final ColorBlock top;
		private final ColorBlock bottom;
		private final ColorBlock left;
		private final ColorBlock right;
		private final float x;
		private final float y;

		private BuffEffect(float x, float y, int color) {
			super(0.34f);
			this.x = x;
			this.y = y;
			top = new ColorBlock(1, 1, color);
			bottom = new ColorBlock(1, 1, color);
			left = new ColorBlock(1, 1, color);
			right = new ColorBlock(1, 1, color);
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
			right.y = y - h / 2f;
			right.size(2, h);
			right.am = alpha;
		}
	}

	private class ExhaustEffect extends BattleEffect {

		private final ColorBlock[] sparks = new ColorBlock[4];
		private final float cx, cy;

		private ExhaustEffect(float cx, float cy) {
			super(0.42f);
			this.cx = cx;
			this.cy = cy;
			for (int i = 0; i < sparks.length; i++) {
				sparks[i] = new ColorBlock(1, 1, 0xFFFF7830);
				add(sparks[i]);
			}
		}

		@Override
		protected void updateEffect(float p) {
			float alpha = (float) Math.pow(1f - p, 0.5f);
			float dist = p * 18f;
			float sz = Math.max(1f, 4f - p * 3f);
			float[][] dirs = {{-1,-1},{1,-1},{-1,1},{1,1}};
			for (int i = 0; i < sparks.length; i++) {
				sparks[i].size(sz, sz);
				sparks[i].x = cx + dirs[i][0] * dist - sz / 2f;
				sparks[i].y = cy + dirs[i][1] * dist - sz / 2f;
				sparks[i].am = alpha;
			}
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
			if (card != null) return DeckCardText.keywordText(card, cardCode);
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
		private boolean activeTouch;

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
			if (selectingWandForStaff) {
				int code = cardCode();
				return DeckCard.maxCharge(code) > 0;
			}
			if (selectingForPure) {
				return handIndex != pureHandIndex;
			}
			if (gamblerBrewActive) {
				return true;
			}
			if (card().unplayable(cardCode())) {
				return false;
			}
			return !combatLocked && handIndex < combat.hand.size() && combat.cardCost(cardCode()) <= combat.energy;
		}

		@Override
		protected void layout() {
			super.layout();
			if ((selectingForPure || gamblerBrewActive) && pureSelectedIndices.contains(handIndex)) {
				edge.color(0xFFA8F26A);
				edge.am = 1.0f;
				face.am = 0.92f;
			}
		}

		@Override
		protected int displayCost(DeckCard card, int cardCode) {
			return combat == null ? card.cost(cardCode) : combat.cardCost(cardCode);
		}

		@Override
		protected void onPointerDown() {
			if (selectingWandForStaff) {
				activeTouch = true;
				super.onPointerDown();
				homeX = x;
				homeY = y;
				dragging = false;
				useOnRelease = false;
				clickReady = true;
				showCardInfo(cardCode());
				return;
			}
			if (selectingForPure || gamblerBrewActive) {
				activeTouch = true;
				super.onPointerDown();
				homeX = x;
				homeY = y;
				dragging = false;
				useOnRelease = false;
				clickReady = true;
				showCardInfo(cardCode());
				return;
			}
			if (combatLocked) return;
			activeTouch = true;
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
			if (selectingWandForStaff) return;
			if (selectingForPure || gamblerBrewActive) return;
			if (!activeTouch || !enabled()) return;
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
			if (!activeTouch) return;
			activeTouch = false;
			super.onPointerUp();
			hideCardInfo();
			if (selectingWandForStaff) {
				if (clickReady) onClick();
				return;
			}
			if (selectingForPure || gamblerBrewActive) {
				if (clickReady) {
					clickReady = false;
					onClick();
				}
				return;
			}
			boolean shouldPlay = dragging && useOnRelease && enabled();
			setRect(homeX, homeY, width, height);
			dragging = false;
			useOnRelease = false;
			if (shouldPlay) {
				playCard(handIndex);
			}
		}

		@Override
		protected void onClick() {
			if (selectingWandForStaff) {
				int code = cardCode();
				if (DeckCard.maxCharge(code) > 0 && handIndex != mageStaffHandIndex) {
					selectingWandForStaff = false;
					hideWandSelectionBanner();
					combatLocked = true;
					executePlayCard(mageStaffHandIndex, handIndex);
				} else if (handIndex == mageStaffHandIndex) {
					selectingWandForStaff = false;
					hideWandSelectionBanner();
					refresh();
				}
				return;
			}
			if (selectingForPure) {
				if (handIndex == pureHandIndex) {
					selectingForPure = false;
					hidePureSelectionBanner();
					pureSelectedIndices.clear();
					refresh();
				} else {
					Integer idx = handIndex;
					if (pureSelectedIndices.contains(idx)) {
						pureSelectedIndices.remove(idx);
					} else if (pureSelectedIndices.size() < pureMaxSelect) {
						pureSelectedIndices.add(idx);
					}
					updatePureConfirmButton();
					refresh();
				}
				return;
			}
			if (gamblerBrewActive) {
				Integer idx = handIndex;
				if (pureSelectedIndices.contains(idx)) {
					pureSelectedIndices.remove(idx);
				} else {
					pureSelectedIndices.add(idx);
				}
				updatePureConfirmButton();
				refresh();
			}
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

	private class RewardWindow extends Window {
		@Override
		public void onBackPressed() {
			// Rewards must be resolved through their buttons; closing them by tapping outside can strand the run.
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
			float textX = x + 10;
			if (iconImage >= 0) {
				if (icon == null) {
					icon = new ItemSprite(iconImage, null);
					add(icon);
				}
				icon.x = x + 7;
				icon.y = y + (height - icon.height()) / 2f;
				textX = x + 31;
			}
			text.text(claimed ? "획득 완료" : label);
			text.hardlight(claimed ? 0xFF9A9A9A : 0xFFD8D1BD);
			text.maxWidth((int)(width - (textX - x) - 7));
			text.setPos(textX, y + (height - text.height()) / 2f);
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
		protected RenderedTextBlock chargeLabel;
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
		chargeLabel = renderTextBlock(5);
		chargeLabel.visible = false;
		add(chargeLabel);
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

			face.color(card.deckClass == null || !card.reward ? card.rarity.faceColor : card.classFaceColor());
			face.x = x + 2;
			face.y = y + 2;
			face.size(width - 4, height - 4);
			face.am = enabled ? 0.96f : 0.68f;

		cost.text(String.valueOf(displayCost(card, cardCode)));
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

		int maxCharge = DeckCard.maxCharge(cardCode);
		if (maxCharge > 0) {
			chargeLabel.visible = true;
			int currentCharge = DeckCard.currentCharge(cardCode);
			chargeLabel.text(currentCharge + "/" + maxCharge);
			chargeLabel.hardlight(0xFFFF00);
			chargeLabel.maxWidth((int)width - 10);
			float chargeX = x + (width - chargeLabel.width()) / 2f;
			float chargeY = labelY - chargeLabel.height() - 1;
			chargeLabel.setPos(chargeX, chargeY);
		} else {
			chargeLabel.visible = false;
		}

		float artH = Math.max(18, height * 0.46f);
		artPanel.color(card.deckClass == null || !card.reward ? card.rarity.panelColor : card.classPanelColor());
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
				art = new ItemSprite(card.icon());
				art.visible = false;
				add(art);
			}
			spriteArt.visible = false;
			art.view(card.icon(), null);
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

		protected int displayCost(DeckCard card, int cardCode) {
			return card.cost(cardCode);
		}
	}
}
