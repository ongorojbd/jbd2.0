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
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Crab;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mimic;
import com.shatteredpixel.shatteredpixeldungeon.deckbuilder.DeckBuilderCombat;
import com.shatteredpixel.shatteredpixeldungeon.deckbuilder.DeckCardCode;
import com.shatteredpixel.shatteredpixeldungeon.deckbuilder.DeckBuilderMap;
import com.shatteredpixel.shatteredpixeldungeon.deckbuilder.DeckBuilderRun;
import com.shatteredpixel.shatteredpixeldungeon.deckbuilder.DeckCard;
import com.shatteredpixel.shatteredpixeldungeon.deckbuilder.DeckCardKeyword;
import com.shatteredpixel.shatteredpixeldungeon.deckbuilder.DeckCardPool;
import com.shatteredpixel.shatteredpixeldungeon.deckbuilder.DeckCardRarity;
import com.shatteredpixel.shatteredpixeldungeon.deckbuilder.DeckCardTarget;
import com.shatteredpixel.shatteredpixeldungeon.deckbuilder.DeckCardText;
import com.shatteredpixel.shatteredpixeldungeon.deckbuilder.DeckCardType;
import com.shatteredpixel.shatteredpixeldungeon.deckbuilder.DeckCombatEnemy;
import com.shatteredpixel.shatteredpixeldungeon.deckbuilder.DeckCombatRewardState;
import com.shatteredpixel.shatteredpixeldungeon.deckbuilder.DeckDiscover;
import com.shatteredpixel.shatteredpixeldungeon.deckbuilder.DeckEnemy;
import com.shatteredpixel.shatteredpixeldungeon.deckbuilder.DeckEnemyIntent;
import com.shatteredpixel.shatteredpixeldungeon.deckbuilder.DeckPlayResult;
import com.shatteredpixel.shatteredpixeldungeon.deckbuilder.DeckPotion;
import com.shatteredpixel.shatteredpixeldungeon.deckbuilder.DeckPotionPolicy;
import com.shatteredpixel.shatteredpixeldungeon.deckbuilder.DeckRelic;
import com.shatteredpixel.shatteredpixeldungeon.items.Amulet;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Sword;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.SewerLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.shatteredpixel.shatteredpixeldungeon.tiles.TerrainFeaturesTilemap;
import com.shatteredpixel.shatteredpixeldungeon.sprites.AlbinoSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.BeeSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CausticSlimeSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CivilSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.FetidRatSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GnollTricksterSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GreatCrabSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HermitCrabSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MimicSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.RatKingSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SlimeSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.VampireSprite;
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
import com.shatteredpixel.shatteredpixeldungeon.sprites.SpeedwagonSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.WraithSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ZombieSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIcon;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.Button;
import com.shatteredpixel.shatteredpixeldungeon.ui.IconButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.TalentIcon;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.ui.Component;
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

    private static final boolean DECKBUILDER_BETA_LIMITS = true;

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
    private boolean selectingForBurningPact;
    private boolean selectingForDaggerThrowDiscard;
    private boolean selectingForHiddenDagger;
    private boolean selectingForPendingExhaust;
    private boolean selectingForStrategyRetain;
    private boolean touchOfInsanityActive;
    private int touchOfInsanitySlot;
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
    private Window hudPopupWindow;
    private float spriteScale;
    private boolean rewardOpen;
    private int tutorialMessageLocks;
    private boolean endingRun;
    private ArrayList<Float> pendingDiscardStartsX = new ArrayList<>();
    private ArrayList<Float> pendingDiscardStartsY = new ArrayList<>();
    private ArrayList<Float> pendingTransientExhaustX = new ArrayList<>();
    private ArrayList<Float> pendingTransientExhaustY = new ArrayList<>();
    private boolean pendingPileShuffle;
    private int pendingDrawVisuals;
    private boolean needsHandDraw = false;
    private String pendingTurnEndAutoPlayLog = "";

    // Tutorial highlight border (4-sided pulsing golden frame)
    private ColorBlock tutBorderTop, tutBorderBottom, tutBorderLeft, tutBorderRight;
    private float tutHighlightTime = 0f;

    private static final PlayerBuffSpec[] PLAYER_STATUS_BUFFS = new PlayerBuffSpec[]{
            new PlayerBuffSpec(BuffIndicator.UPGRADE, 1f, 0.5f, 0f, "공격력", "공격 카드가 주는 피해가 이 수치만큼 증가합니다.",
                    combat -> Math.max(0, combat.playerStrength + combat.playerTurnStrength)),
            new PlayerBuffSpec(BuffIndicator.UPGRADE, 0.45f, 0.75f, 1f, "방어력 증가", "공격/스킬 카드를 통해 얻는 보호막이 이 수치만큼 증가합니다.",
                    combat -> Math.max(0, combat.playerDexterity)),
            new PlayerBuffSpec(BuffIndicator.DEGRADE, "공격력 감소", "공격 카드가 주는 피해가 수치만큼 감소합니다.",
                    combat -> Math.max(0, -(combat.playerStrength + combat.playerTurnStrength))),
            new PlayerBuffSpec(BuffIndicator.CRIPPLE, "민첩 감소", "보호막을 얻을 때 획득량이 수치만큼 감소합니다.",
                    combat -> Math.max(0, -combat.playerDexterity)),
            new PlayerBuffSpec(BuffIndicator.WEAKNESS, "공격력 저하", "공격 카드 피해가 25% 감소합니다. 턴이 시작될 때마다 1 감소합니다.",
                    combat -> combat.playerWeak),
            new PlayerBuffSpec(BuffIndicator.CORRUPT, "피해 증폭", "받는 공격 피해가 50% 증가합니다. 턴이 시작될 때마다 1 감소합니다.",
                    combat -> combat.playerVulnerable),
            new PlayerBuffSpec(BuffIndicator.PARALYSIS, "자석화", "공격 카드 비용이 1 증가합니다. 턴이 끝날 때마다 1 감소합니다.",
                    combat -> combat.playerEntangle),
            new PlayerBuffSpec(BuffIndicator.UPGRADE, 1f, 0f, 0f, "연속 타격", "공격 카드의 피해가 이 수치만큼 증가합니다. 공격 이외의 카드를 사용하면 소멸합니다.",
                    combat -> combat.playerConsecutiveStrike),
            new PlayerBuffSpec(BuffIndicator.TRINITY_FORM, 1f, 0.4f, 0f, "반격", "적이 체력 피해를 줄 때 해당 적에게 반격 수치만큼 피해를 줍니다. 턴이 끝나면 사라집니다.",
                    combat -> combat.playerThorns),
            new PlayerBuffSpec(BuffIndicator.MOMENTUM, "유아화", "공격 카드의 피해가 30% 감소합니다.",
                    combat -> combat.playerDamageReduction > 0 ? 1 : 0, false),
            new PlayerBuffSpec(BuffIndicator.DEGRADE, "방어력 저하", "보호막을 얻을 때마다 방어력 저하 1당 획득량이 25% 감소합니다. 턴이 끝날 때마다 1 감소합니다.",
                    combat -> combat.playerBlockReduction),
            new PlayerBuffSpec(BuffIndicator.IMMUNITY, "정화의 보호막", "상태이상을 받을 때 정화의 보호막을 1 차감하고 그 효과를 무효화합니다.",
                    combat -> combat.playerArtifact),
            new PlayerBuffSpec(BuffIndicator.BLESS, "축복", "체력 피해를 받을 때 피해를 1로 줄입니다. 턴이 시작될 때마다 1 감소합니다.",
                    combat -> combat.playerBlessed),
            new PlayerBuffSpec(BuffIndicator.HEALING, "재생", "턴이 끝날 때마다 이 수치만큼 보호막을 얻습니다. 체력 피해를 받을 때마다 1 감소합니다.",
                    combat -> combat.playerRegen),
            new PlayerBuffSpec(BuffIndicator.TIME, 1f, 0.45f, 0.05f, "오렌지 폭탄", "표시된 턴 수가 0이 되면 모든 적에게 폭탄 피해를 줍니다.",
                    DeckBuilderCombat::orangeBombTimer)
    };

    private static final EnemyStatusBuffSpec[] ENEMY_STATUS_BUFFS = new EnemyStatusBuffSpec[]{
            new EnemyStatusBuffSpec(BuffIndicator.CORRUPT, "피해 증폭", "해당 대상이 받는 모든 공격 피해가 1.5배 증가합니다.",
                    enemy -> enemy.vulnerable),
            new EnemyStatusBuffSpec(BuffIndicator.WEAKNESS, "공격력 저하", "공격 피해가 25% 감소합니다. 턴이 끝날 때마다 1 감소합니다.",
                    enemy -> enemy.attackDown),
            new EnemyStatusBuffSpec(BuffIndicator.DEGRADE, "방어력 저하", "보호막을 얻을 때 획득량이 감소합니다.",
                    enemy -> enemy.blockReduction),
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
            new EnemyStatusBuffSpec(BuffIndicator.POISON, 0.55f, 0.25f, 0.85f, "종언", "적의 턴 종료 시 이 수치만큼 체력을 잃습니다.",
                    enemy -> enemy.demise),
            new EnemyStatusBuffSpec(BuffIndicator.POISON, 0.35f, 0.85f, 0.35f, "지속 피해", "적의 턴 시작 시 보호막을 무시하고 이 수치만큼 피해를 받은 뒤 1 감소합니다.",
                    enemy -> enemy.persistentDamage),
            new EnemyStatusBuffSpec(BuffIndicator.IMMUNITY, "정화의 보호막", "상태이상에 걸릴 때 정화의 보호막을 1 차감하고 그 효과를 무효화합니다.",
                    enemy -> enemy.artifact),
            new EnemyStatusBuffSpec(BuffIndicator.BLESS, "축복", "체력 피해를 받을 때 피해를 1로 줄입니다. 턴이 끝날 때마다 1 감소합니다.",
                    enemy -> enemy.blessed),
            new EnemyStatusBuffSpec(BuffIndicator.COMBO, "광물화", "턴이 끝날 때마다 이 수치만큼 공격력을 얻습니다.",
                    enemy -> enemy.ritual),
            new EnemyStatusBuffSpec(BuffIndicator.PREPARATION, "암흑공간", "턴 종료 시 1 감소합니다. 0이 되면 플레이어를 집어삼키며, 부활 효과를 무시하고 즉사시킵니다.",
                    enemy -> enemy.kind == DeckEnemy.CREAM ? enemy.darkSpace : 0)
    };

    private static final EnemyBuffSpec[] ENEMY_TRAIT_BUFFS = new EnemyBuffSpec[]{
            new EnemyBuffSpec(BuffIndicator.IMBUE, "분열", "체력이 절반 이하가 되면 행동을 취소하고 둘로 나뉩니다.",
                    (scene, enemy) -> enemy.kind == DeckEnemy.LARGE_SLIME && !enemy.splitUsed),
            new EnemyBuffSpec(BuffIndicator.LIGHT_SHIELD, "젠틀리 위프스", "피해를 받거나 3턴이 지나기 전까지 행동하지 않고 매 턴 보호막을 얻습니다.",
                    (scene, enemy) -> enemy.kind == DeckEnemy.LAGAVULIN && !enemy.splitUsed),
            new EnemyBuffSpec(BuffIndicator.INVERT_MARK, "타겟 고정", "이번 턴 행동하지 않습니다.",
                    (scene, enemy) -> enemy.kind == DeckEnemy.LAGAVULIN && enemy.intent == DeckBuilderCombat.RESULT_LAGAVULIN_STUN),
            new EnemyBuffSpec(BuffIndicator.OOZE, "산성 체액", "턴이 끝날 때마다 공격력이 1 증가합니다.",
                    (scene, enemy) -> enemy.kind == DeckEnemy.BYRDONIS),
            new EnemyBuffSpec(BuffIndicator.AMOK, "쥐 떼의 분노", "동료가 쓰러지면 공격력이 2 증가합니다.",
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

        if (Statistics.deckBuilderMapNode == DeckBuilderMap.ELITE) {
            Music.INSTANCE.play(Assets.Music.ELITE, true);
        } else if (combat.depth >= 17 && Statistics.deckBuilderMapNode == DeckBuilderMap.BOSS) {
            Music.INSTANCE.play(Assets.Music.SEWERS_BOSS, true);
        } else if (combat.depth <= 16) {
            Music.INSTANCE.playTracks(SewerLevel.SEWER_TRACK_LIST, SewerLevel.SEWER_TRACK_CHANCES, false);
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
                } else if (selectingForDaggerThrowDiscard || selectingForPendingExhaust) {
                    return;
                } else if (selectingForPure || selectingForBurningPact || selectingForHiddenDagger || selectingForStrategyRetain || touchOfInsanityActive) {
                    selectingForPure = false;
                    selectingForBurningPact = false;
                    selectingForHiddenDagger = false;
                    selectingForStrategyRetain = false;
                    touchOfInsanityActive = false;
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
                showDrawPileWindow();
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
        logText.maxWidth(w - (int) insets.left - (int) insets.right - 18);
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
                } else if (selectingForBurningPact) {
                    confirmBurningPactSelection();
                } else if (selectingForHiddenDagger) {
                    confirmHiddenDaggerSelection();
                } else if (selectingForDaggerThrowDiscard) {
                    confirmDaggerThrowDiscardSelection();
                } else if (selectingForPendingExhaust) {
                    confirmPendingExhaustSelection();
                } else if (selectingForStrategyRetain) {
                    confirmStrategyRetainSelection();
                } else if (touchOfInsanityActive) {
                    confirmTouchOfInsanity();
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
                if (combatLocked || tutorialMessageOpen()) return;
                if (startEnemyTurn()) return;
                int result = combat.endTurn();
	                saveCombatState();
	                spawnPoisonDartDamageEffect();
	                spawnPriceOfSinDamageEffect();
	                spawnBurnDamageEffect();
	                float orangeBombDelay = spawnOrangeBombExplosionEffects(0.0f);
	                spawnUnanimatedEnemyDamageEvents(orangeBombDelay);
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
                if (combatLocked || tutorialMessageOpen()) return;
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

        // Tutorial highlight border — added last so it renders above card buttons
        if (DeckBuilderRun.tutorialMode) {
            tutBorderTop = new ColorBlock(1, 2, 0xFFFFE060);
            tutBorderTop.visible = false;
            add(tutBorderTop);
            tutBorderBottom = new ColorBlock(1, 2, 0xFFFFE060);
            tutBorderBottom.visible = false;
            add(tutBorderBottom);
            tutBorderLeft = new ColorBlock(2, 1, 0xFFFFE060);
            tutBorderLeft.visible = false;
            add(tutBorderLeft);
            tutBorderRight = new ColorBlock(2, 1, 0xFFFFE060);
            tutBorderRight.visible = false;
            add(tutBorderRight);
            refreshTutorialHighlight();
        }
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
            if (combat.turn <= 0) {
                combat.startTurn();
            } else {
                combat.drawTurnHand();
            }
            int drawn = combat.hand.size() - handBefore;
            pendingDrawVisuals = drawn;
            pendingPileShuffle = discardPileBefore > 0 && combat.discardPile.size() < discardPileBefore;
            saveCombatState();
            if (combat.lastEntropyTransformCount > 0) {
                spawnFloatingText("엔트로피: 변환", playerCenterX(), playerCenterY() - 24, 0xFFFF8C00);
            }
            showTutorialTurnPrompt();
        }
        Sample.INSTANCE.play(Assets.Sounds.ITEM);
        showTitleBanner("내 턴", combat.turn + "턴", 0xFF9EE6FF, 0.78f, new Runnable() {
            @Override
            public void run() {
                final float drawEndTime = spawnPendingTurnPileEffects();
                if (!resolveAutoPlayResultsAfterDraw()) {
                    float damageEndTime = spawnUnanimatedDamageEvents(new ArrayList<DeckPlayResult>(), drawEndTime + 0.05f);
                    final float totalDelay = Math.max(drawEndTime, damageEndTime);
                    if (combat.won()) {
                        addEffect(new DelayedActionEffect(totalDelay + 0.1f, new Runnable() {
                            @Override
                            public void run() {
                                showReward();
                            }
                        }));
                    } else if (beginGambleChipSelectionIfNeeded(totalDelay)) {
                        // The selection overlay refreshes the scene after the title/damage effects finish.
                    } else if (totalDelay > 0f) {
                        addEffect(new DelayedActionEffect(totalDelay, new Runnable() {
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

    private boolean beginGambleChipSelectionIfNeeded(float delay) {
        if (combat.turn != 1 || combat.gambleChipOffered || combat.hand.isEmpty()
                || !DeckBuilderRun.hasRelic(DeckRelic.GAMBLE_CHIP)) {
            return false;
        }
        combat.gambleChipOffered = true;
        saveCombatState();
        addEffect(new DelayedActionEffect(delay, new Runnable() {
            @Override
            public void run() {
                gamblerBrewActive = true;
                gamblerBrewSlot = -1;
                pureSelectedIndices.clear();
                showGamblerBrewBanner();
                refresh();
            }
        }));
        return true;
    }

    private boolean startEnemyTurn() {
        if (beginStrategyRetainSelectionIfNeeded()) return true;
        combatLocked = true;
        hideCardInfo();
        resolveEnemyTurn();
        return true;
    }

    private boolean beginStrategyRetainSelectionIfNeeded() {
        if (selectingForStrategyRetain || combat.strategyRetainCount <= 0 || combat.hand.isEmpty()) return false;
        pureSelectedIndices.clear();
        pureHandIndex = -1;
        pureMaxSelect = Math.min(combat.strategyRetainCount, combat.hand.size());
        selectingForStrategyRetain = true;
        showStrategyRetainSelectionBanner();
        refresh();
        return true;
    }

    private void finishEnemyTurnAfterStrategyRetain() {
        combatLocked = true;
        hideCardInfo();
        resolveEnemyTurn();
    }

    private void resolveEnemyTurn() {
        ArrayList<Float> discardStartsX = new ArrayList<>();
        ArrayList<Float> discardStartsY = new ArrayList<>();
        ArrayList<Float> transientExhaustX = new ArrayList<>();
        ArrayList<Float> transientExhaustY = new ArrayList<>();
        for (int i = 0; i < combat.hand.size(); i++) {
            int code = combat.hand.get(i);
            if (retainedAtEndTurn(i, code)) continue;
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
	        if (combat.lastTurnEndRegenBlock > 0) {
	            spawnFloatingText("+보호막 " + combat.lastTurnEndRegenBlock + " (재생)", playerCenterX(), playerCenterY() - 42, 0xFF99EEFF);
	            updatePlayerHpUi();
	        }
	        spawnPoisonDartDamageEffect();
	        spawnPriceOfSinDamageEffect();
	        spawnBurnDamageEffect();
	        float orangeBombDelay = spawnOrangeBombExplosionEffects(0.08f);
	        float relicDamageDelay = spawnUnanimatedDamageEvents(new ArrayList<DeckPlayResult>(), Math.max(0.12f, orangeBombDelay));
        pendingTurnEndAutoPlayLog = buildRandomPlayLog(combat.lastTurnEndAutoPlayResults);
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

        addEffect(new DelayedActionEffect(Math.max(wandDelay, relicDamageDelay), new Runnable() {
            @Override
            public void run() {
                if (combat.won() || combat.playerDead()) {
                    resolveEnemyTurnAfterWands(result);
                } else {
                    Sample.INSTANCE.play(Assets.Sounds.ITEM);
                    showTitleBanner("적 턴", "", 0xFFFF7474, 0.72f, new Runnable() {
                        @Override
                        public void run() {
                            resolveEnemyTurnAfterWands(result);
                        }
                    });
                }
            }
        }));
    }

    private void resolveEnemyTurnAfterWands(final int result) {
        if (combat.won()) {
            log(enemyTurnLog(result));
            spawnEnemyActions();
            float attackDelay = Math.max(0.36f, 0.62f + combat.lastEnemyActions.size() * 0.08f);
            float endTurnDelay = spawnEnemyEndTurnDamageEffects(attackDelay);
            float totalDelay = Math.max(attackDelay, endTurnDelay);
            addEffect(new DelayedActionEffect(totalDelay, new Runnable() {
                @Override
                public void run() {
                    showReward();
                }
            }));
            return;
        }

        spawnEnemyActions();
        if (combat.playerDead()) {
            updatePlayerHpUi();
            Sample.INSTANCE.play(Assets.Sounds.DEATH);
            log("패배했습니다. 랭킹 화면으로 돌아갑니다.");
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
            float attackDelay = Math.max(0.36f, 0.62f + combat.lastEnemyActions.size() * 0.08f);
            float endTurnDelay = spawnEnemyEndTurnDamageEffects(attackDelay);
            float totalDelay = Math.max(attackDelay, endTurnDelay);
            addEffect(new DelayedActionEffect(totalDelay, new Runnable() {
                @Override
                public void run() {
                    if (combat.won()) {
                        showReward();
                    } else {
                        if (!combat.playerDead()) combat.startTurnState();
                        needsHandDraw = true;
                        showPlayerTurnTitle();
                    }
                }
            }));
        }
    }

    private float spawnEnemyEndTurnDamageEffects(float startDelay) {
        if (combat.lastEnemyEndTurnDamageEvents.isEmpty()) return 0f;
        float delay = startDelay;
        for (DeckBuilderCombat.DamageEvent event : combat.lastEnemyEndTurnDamageEvents) {
            if (event.damage <= 0 || event.playerTarget()) continue;
            final float capturedDelay = delay;
            final DeckBuilderCombat.DamageEvent captured = event;
            addEffect(new DelayedActionEffect(capturedDelay, new Runnable() {
                @Override
                public void run() {
                    spawnEnemyDamageImpact(captured.enemyIndex, captured.damage, 0xFFFF8844, "종언", captured.enemyHpAfter);
                }
            }));
            delay += 0.16f;
        }
        return delay + 0.1f;
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
        CARD_H = Math.min((int) (h * 0.28f), (int) (CARD_W * 1.2f));

        // HP bar width: a bit wider than a card, capped at original 78
        ACTOR_HP_W = Math.min(78, Math.max(46, CARD_W + 6));

        // Scale sprites down on very small virtual screens so they fit the combat zone
        spriteScale = h < 240 ? 2f : (h < 320 ? 2.5f : 3f);
    }

    private void addBackground(int w, int h, RectF insets) {
        add(new ColorBlock(w, h, 0xFF141414));

        Image splash = new Image(Assets.Splashes.DECK1);
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
        int endTurnW = Math.min(w < 300 ? 64 : 76, (int) (w * 0.22f));
        int targetW = Math.min(38, Math.max(30, (int) (w * 0.09f)));
        float targetH = Math.max(14, logBtnH - 5);
        targetButton.setRect(right - targetW, stageTop + 2, targetW, targetH);
        logText.maxWidth((int) (right - left - endTurnW - 8));
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
            if (view.enemy.kind == DeckEnemy.CREAM) {
                cx += (view.enemy.darkSpace - 3) * 8f;
            }
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
            view.hp.size(ACTOR_HP_W * view.enemy.hp / (float) view.enemy.ht, ACTOR_HP_H);
            float hpFillWidth = view.hp.width();
            float rawShieldW = view.enemy.block * ACTOR_HP_W / (float) Math.max(1, view.enemy.ht);
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
        playerHp.size(ACTOR_HP_W * hp / (float) DeckBuilderRun.playerHT, ACTOR_HP_H);

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

    private void showDiscoverWindow() {
        combatLocked = true;
        final DeckCard[] choices = combat.pendingDiscoverChoices;
        if (choices == null || choices.length == 0) {
            combat.pendingDiscoverChoices = null;
            combatLocked = false;
            refresh();
            return;
        }
        final boolean zeroCost = combat.pendingDiscoverZeroCost;
        final boolean discoverTransient = combat.pendingDiscoverTransient;
        final boolean discoverUpgraded = combat.pendingDiscoverUpgraded;

        final Window win = new DeckRewardWindow();
        int cardW = 62;
        int cardGap = 10;
        int totalCardW = choices.length * cardW + Math.max(0, choices.length - 1) * cardGap;
        int width = Math.max(220, totalCardW + 20);
        int pos = 7;

        RenderedTextBlock title = renderTextBlock("발견", 9);
        title.hardlight(Window.TITLE_COLOR);
        title.setPos((width - title.width()) / 2f, pos);
        win.add(title);
        pos += 18;

        int startX = (width - totalCardW) / 2;
        for (int i = 0; i < choices.length; i++) {
            final DeckCard card = choices[i];
            int tempCode = discoverUpgraded ? DeckCardCode.upgrade(card.code()) : card.code();
            if (zeroCost) tempCode = DeckCard.withKeyword(tempCode, DeckCardKeyword.ZERO_COST);
            if (discoverTransient) {
                tempCode = DeckCard.withKeyword(tempCode, DeckCardKeyword.TRANSIENT);
                tempCode = DeckCard.withKeyword(tempCode, DeckCardKeyword.EXHAUST);
            }
            final int code = tempCode;
            RewardCardButton button = new RewardCardButton(card, true) {
                @Override
                protected int cardCode() {
                    return code;
                }

                @Override
                protected void onClick() {
                    showDiscoverDetailWindow(win, card, code);
                }
            };
            button.setRect(startX + i * (cardW + cardGap), pos, cardW, 64);
            win.add(button);
        }
        pos += 70;

        win.resize(width, pos);
        addToFront(win);
        bringRunHudToFront();
    }

    private void showDiscoverDetailWindow(final Window discoverWindow, final DeckCard card, final int cardCode) {
        final Window win = new DeckRewardWindow();
        int width = 170;
        int pos = 7;

        RenderedTextBlock title = renderTextBlock(cardDetailTitle(card, cardCode), 8);
        title.hardlight(Window.TITLE_COLOR);
        title.setPos((width - title.width()) / 2f, pos);
        win.add(title);
        pos += 16;

        RenderedTextBlock desc = renderTextBlock(DeckCardText.rulesAndKeywordText(card, cardCode, combat), 6);
        desc.maxWidth(width - 14);
        desc.hardlight(0xFFD8D1BD);
        desc.setPos(7, pos);
        win.add(desc);
        pos += (int) desc.height() + 8;

        RedButton take = new RedButton("가져가기", 6) {
            @Override
            protected void onClick() {
                Sample.INSTANCE.play(Assets.Sounds.ITEM);
                combat.addToHand(cardCode);
                combat.pendingDiscoverChoices = null;
                saveCombatState();
                win.hide();
                discoverWindow.hide();
                refresh();
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

    private void refresh() {
        if (rewardOpen) return;
        if (combat.pendingDiscoverChoices != null) {
            showDiscoverWindow();
            return;
        }
        if (combat.pendingStratagemSelect) {
            combat.pendingStratagemSelect = false;
            showStratagemSelectWindow();
            return;
        }
        if ((combat.pendingDaggerThrowDiscard || combat.pendingHandDiscardSelectCount > 0) && !selectingForDaggerThrowDiscard) {
            restoreDaggerThrowDiscardSelection();
            return;
        }
        if (combat.pendingHandExhaustSelectCount > 0 && !selectingForPendingExhaust) {
            restorePendingExhaustSelection();
            return;
        }
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
        if (runHud != null) runHud.refresh();

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

        // Keep tutorial highlight on top of dynamically-added card buttons
        if (tutBorderTop != null) {
            remove(tutBorderTop);
            add(tutBorderTop);
            remove(tutBorderBottom);
            add(tutBorderBottom);
            remove(tutBorderLeft);
            add(tutBorderLeft);
            remove(tutBorderRight);
            add(tutBorderRight);
            refreshTutorialHighlight();
        }

        bringHudPopupToFront();
    }

    @Override
    public void update() {
        super.update();

        float playerBob = (float) Math.sin(Game.timeTotal * 2.4f) * 2f;
        float enemyBob = (float) Math.sin(Game.timeTotal * 2.1f + 1.2f) * 2f;
        playerHitTime = Math.max(0, playerHitTime - Game.elapsed);
        playerGuardTime = Math.max(0, playerGuardTime - Game.elapsed);
        float playerShake = playerHitTime > 0 ? (float) Math.sin(playerHitTime * 92f) * 4f * playerHitTime / 0.22f : 0;
        float guardLift = playerGuardTime > 0 ? -3f * (float) Math.sin(playerGuardTime / 0.34f * Math.PI) : 0;
        playerSprite.x = playerBaseX + playerShake;
        playerSprite.y = playerBaseY + playerBob + guardLift;
        for (EnemyView view : enemyViews) {
            view.hitTime = Math.max(0, view.hitTime - Game.elapsed);
            view.attackTime = Math.max(0, view.attackTime - Game.elapsed);
            float enemyShake = view.hitTime > 0 ? (float) Math.sin(view.hitTime * 92f) * 4f * view.hitTime / 0.22f : 0;
            float enemyLunge = view.attackTime > 0 ? -10f * (float) Math.sin(view.attackTime / 0.22f * Math.PI) : 0;
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

        // Pulse tutorial highlight border
        if (tutBorderTop != null && tutBorderTop.visible) {
            tutHighlightTime += Game.elapsed;
            float pulse = 0.5f + 0.5f * (float) Math.abs(Math.sin(tutHighlightTime * 3.8f));
            float am = 0.45f + 0.50f * pulse;
            tutBorderTop.am = tutBorderBottom.am = tutBorderLeft.am = tutBorderRight.am = am;
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

    private void showBurningPactSelectionBanner() {
        int w = Camera.main.width;
        int h = Camera.main.height;
        int shadeH = 44;
        pureSelectionShade.size(w, shadeH);
        pureSelectionShade.y = (h - shadeH) / 2f;
        pureSelectionShade.visible = true;

        pureSelectionAccent.color(0xFFFF8A4A);
        pureSelectionAccent.y = pureSelectionShade.y + shadeH;
        pureSelectionAccent.visible = true;

        pureSelectionTitle.text("소멸할 카드 1장을 선택하세요.");
        pureSelectionTitle.hardlight(0xFFFF8A4A);
        pureSelectionTitle.setPos((w - pureSelectionTitle.width()) / 2f, pureSelectionShade.y + 6);
        pureSelectionTitle.visible = true;

        pureSelectionSubtitle.visible = false;

        pureSelectorConfirm.text("확인 (0/1)");
        pureSelectorConfirm.setRect((w - 90) / 2f, pureSelectionTitle.bottom() + 4, 90, 14);
        pureSelectorConfirm.visible = true;

        selectionBackdropArea.active = true;
    }

    private void showDaggerThrowDiscardBanner() {
        int w = Camera.main.width;
        int h = Camera.main.height;
        int shadeH = 44;
        pureSelectionShade.size(w, shadeH);
        pureSelectionShade.y = (h - shadeH) / 2f;
        pureSelectionShade.visible = true;

        pureSelectionAccent.color(0xFFFFD84D);
        pureSelectionAccent.y = pureSelectionShade.y + shadeH;
        pureSelectionAccent.visible = true;

        int count = Math.max(1, combat.pendingHandDiscardSelectCount);
        pureSelectionTitle.text("버릴 카드 " + count + "장을 선택하세요.");
        pureSelectionTitle.hardlight(0xFFFFD84D);
        pureSelectionTitle.setPos((w - pureSelectionTitle.width()) / 2f, pureSelectionShade.y + 6);
        pureSelectionTitle.visible = true;

        pureSelectionSubtitle.visible = false;

        pureSelectorConfirm.text("확인 (0/" + count + ")");
        pureSelectorConfirm.setRect((w - 90) / 2f, pureSelectionTitle.bottom() + 4, 90, 14);
        pureSelectorConfirm.visible = true;

        selectionBackdropArea.active = true;
    }

    private void showPendingExhaustSelectionBanner() {
        int w = Camera.main.width;
        int h = Camera.main.height;
        int shadeH = 44;
        pureSelectionShade.size(w, shadeH);
        pureSelectionShade.y = (h - shadeH) / 2f;
        pureSelectionShade.visible = true;

        pureSelectionAccent.color(0xFFFF8A4A);
        pureSelectionAccent.y = pureSelectionShade.y + shadeH;
        pureSelectionAccent.visible = true;

        int count = Math.max(1, combat.pendingHandExhaustSelectCount);
        pureSelectionTitle.text("소멸할 카드 " + count + "장을 선택하세요.");
        pureSelectionTitle.hardlight(0xFFFF8A4A);
        pureSelectionTitle.setPos((w - pureSelectionTitle.width()) / 2f, pureSelectionShade.y + 6);
        pureSelectionTitle.visible = true;

        pureSelectionSubtitle.visible = false;

        pureSelectorConfirm.text("확인 (0/" + count + ")");
        pureSelectorConfirm.setRect((w - 90) / 2f, pureSelectionTitle.bottom() + 4, 90, 14);
        pureSelectorConfirm.visible = true;

        selectionBackdropArea.active = true;
    }

    private void showHiddenDaggerSelectionBanner() {
        int w = Camera.main.width;
        int h = Camera.main.height;
        int shadeH = 44;
        pureSelectionShade.size(w, shadeH);
        pureSelectionShade.y = (h - shadeH) / 2f;
        pureSelectionShade.visible = true;

        pureSelectionAccent.color(0xFFFFD84D);
        pureSelectionAccent.y = pureSelectionShade.y + shadeH;
        pureSelectionAccent.visible = true;

        pureSelectionTitle.text("버릴 카드 2장을 선택하세요.");
        pureSelectionTitle.hardlight(0xFFFFD84D);
        pureSelectionTitle.setPos((w - pureSelectionTitle.width()) / 2f, pureSelectionShade.y + 6);
        pureSelectionTitle.visible = true;

        pureSelectionSubtitle.visible = false;

        pureSelectorConfirm.text("확인 (0/" + pureMaxSelect + ")");
        pureSelectorConfirm.setRect((w - 90) / 2f, pureSelectionTitle.bottom() + 4, 90, 14);
        pureSelectorConfirm.visible = true;

        selectionBackdropArea.active = true;
    }

    private void showStrategyRetainSelectionBanner() {
        int w = Camera.main.width;
        int h = Camera.main.height;
        int shadeH = 44;
        pureSelectionShade.size(w, shadeH);
        pureSelectionShade.y = (h - shadeH) / 2f;
        pureSelectionShade.visible = true;

        pureSelectionAccent.color(0xFF9EE6FF);
        pureSelectionAccent.y = pureSelectionShade.y + shadeH;
        pureSelectionAccent.visible = true;

        pureSelectionTitle.text("보존할 카드를 최대 " + pureMaxSelect + "장 선택하세요.");
        pureSelectionTitle.hardlight(0xFF9EE6FF);
        pureSelectionTitle.setPos((w - pureSelectionTitle.width()) / 2f, pureSelectionShade.y + 6);
        pureSelectionTitle.visible = true;

        pureSelectionSubtitle.visible = false;

        pureSelectorConfirm.text("확인 (" + pureSelectedIndices.size() + "/" + pureMaxSelect + ")");
        pureSelectorConfirm.setRect((w - 90) / 2f, pureSelectionTitle.bottom() + 4, 90, 14);
        pureSelectorConfirm.visible = true;

        selectionBackdropArea.active = true;
    }

    private void showTouchOfInsanityBanner() {
        int w = Camera.main.width;
        int h = Camera.main.height;
        int shadeH = 44;
        pureSelectionShade.size(w, shadeH);
        pureSelectionShade.y = (h - shadeH) / 2f;
        pureSelectionShade.visible = true;

        pureSelectionAccent.color(0xFFFF66CC);
        pureSelectionAccent.y = pureSelectionShade.y + shadeH;
        pureSelectionAccent.visible = true;

        pureSelectionTitle.text("비용 없이 사용할 카드 1장을 선택하세요.");
        pureSelectionTitle.hardlight(0xFFFF66CC);
        pureSelectionTitle.setPos((w - pureSelectionTitle.width()) / 2f, pureSelectionShade.y + 6);
        pureSelectionTitle.visible = true;

        pureSelectionSubtitle.visible = false;

        pureSelectorConfirm.text("확인 (0/1)");
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
        if (touchOfInsanityActive) {
            pureSelectorConfirm.text("확인 (" + pureSelectedIndices.size() + "/1)");
        } else if (selectingForBurningPact) {
            pureSelectorConfirm.text("확인 (" + pureSelectedIndices.size() + "/1)");
        } else if (selectingForDaggerThrowDiscard) {
            pureSelectorConfirm.text("확인 (" + pureSelectedIndices.size() + "/" + pureMaxSelect + ")");
        } else if (selectingForHiddenDagger) {
            pureSelectorConfirm.text("확인 (" + pureSelectedIndices.size() + "/" + pureMaxSelect + ")");
        } else if (selectingForPendingExhaust) {
            pureSelectorConfirm.text("확인 (" + pureSelectedIndices.size() + "/" + pureMaxSelect + ")");
        } else if (selectingForStrategyRetain) {
            pureSelectorConfirm.text("확인 (" + pureSelectedIndices.size() + "/" + pureMaxSelect + ")");
        } else if (gamblerBrewActive) {
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
            if (gamblerBrewSlot >= 0) {
                DeckBuilderRun.removePotion(gamblerBrewSlot);
                if (runHud != null) runHud.refresh();
            }
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

    private void confirmTouchOfInsanity() {
        if (pureSelectedIndices.isEmpty()) return;
        int idx = pureSelectedIndices.get(0);
        if (idx >= 0 && idx < combat.hand.size()) {
            combat.hand.set(idx, DeckCard.withKeyword(combat.hand.get(idx), DeckCardKeyword.ZERO_COST));
            DeckBuilderRun.removePotion(touchOfInsanitySlot);
            if (runHud != null) runHud.refresh();
            log("광기의 손길: 선택한 카드를 이번 전투 동안 비용 없이 사용합니다.");
            saveCombatState();
        }
        pureSelectedIndices.clear();
        touchOfInsanityActive = false;
        hidePureSelectionBanner();
        pureSelectionAccent.color(0xFF7EC8D4);
        pureSelectionTitle.hardlight(0xFF7EC8D4);
        refresh();
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
            combat.exhaustCard(code);
            float[] pos = idxToPos.get(idx);
            addEffect(new ExhaustEffect(pos[0], pos[1]));
            Sample.INSTANCE.play(Assets.Sounds.BURNING);
        }
        pureSelectedIndices.clear();
        selectingForPure = false;
        hidePureSelectionBanner();
        executePlayCard(adjustedPureIndex, -1);
    }

    private void confirmBurningPactSelection() {
        if (pureSelectedIndices.isEmpty()) return;
        Map<Integer, float[]> idxToPos = new LinkedHashMap<>();
        for (int idx : pureSelectedIndices) {
            float ex = idx < cardButtons.size() ? cardButtons.get(idx).centerX() : playerCenterX();
            float ey = idx < cardButtons.size() ? cardButtons.get(idx).centerY() : playerCenterY();
            idxToPos.put(idx, new float[]{ex, ey});
        }
        int adjustedIndex = pureHandIndex;
        for (int idx : pureSelectedIndices) {
            if (idx < pureHandIndex) adjustedIndex--;
        }
        java.util.Collections.sort(pureSelectedIndices, java.util.Collections.reverseOrder());
        for (int idx : pureSelectedIndices) {
            int code = combat.hand.get(idx);
            combat.hand.remove(idx);
            combat.exhaustCard(code);
            float[] pos = idxToPos.get(idx);
            addEffect(new ExhaustEffect(pos[0], pos[1]));
            Sample.INSTANCE.play(Assets.Sounds.BURNING);
        }
        pureSelectedIndices.clear();
        selectingForBurningPact = false;
        hidePureSelectionBanner();
        executePlayCard(adjustedIndex, -1);
    }

    private void confirmHiddenDaggerSelection() {
        if (pureSelectedIndices.size() < pureMaxSelect) return;
        int adjustedIndex = pureHandIndex;
        for (int idx : pureSelectedIndices) {
            if (idx < pureHandIndex) adjustedIndex--;
        }
        java.util.Collections.sort(pureSelectedIndices, java.util.Collections.reverseOrder());
        int discarded = 0;
        for (int idx : pureSelectedIndices) {
            if (idx >= 0 && idx < combat.hand.size()) {
                int code = DeckCardCode.withoutCostOverride(combat.hand.remove(idx));
                combat.discardPile.add(code);
                discarded++;
            }
        }
        pureSelectedIndices.clear();
        selectingForHiddenDagger = false;
        hidePureSelectionBanner();
        log("移대뱶瑜?" + discarded + "??踰꾨졇?듬땲??");
        executePlayCard(adjustedIndex, -1);
    }

    private void confirmDaggerThrowDiscardSelection() {
        if (pureSelectedIndices.size() < pureMaxSelect) return;
        java.util.Collections.sort(pureSelectedIndices, java.util.Collections.reverseOrder());
        int discarded = 0;
        for (int idx : pureSelectedIndices) {
            if (idx >= 0 && idx < combat.hand.size()) {
                int code = DeckCardCode.withoutCostOverride(combat.hand.remove(idx));
                combat.discardPile.add(code);
                discarded++;
            }
        }
        pureSelectedIndices.clear();
        selectingForDaggerThrowDiscard = false;
        combat.pendingDaggerThrowDiscard = false;
        combat.pendingHandDiscardSelectCount = 0;
        log("카드를 " + discarded + "장 버렸습니다.");
        saveCombatState();
        hidePureSelectionBanner();
        refresh();
    }

    private void confirmPendingExhaustSelection() {
        if (pureSelectedIndices.size() < pureMaxSelect) return;
        Map<Integer, float[]> idxToPos = new LinkedHashMap<>();
        for (int idx : pureSelectedIndices) {
            float ex = idx < cardButtons.size() ? cardButtons.get(idx).centerX() : playerCenterX();
            float ey = idx < cardButtons.size() ? cardButtons.get(idx).centerY() : playerCenterY();
            idxToPos.put(idx, new float[]{ex, ey});
        }
        java.util.Collections.sort(pureSelectedIndices, java.util.Collections.reverseOrder());
        int exhausted = 0;
        for (int idx : pureSelectedIndices) {
            if (idx >= 0 && idx < combat.hand.size()) {
                int code = DeckCardCode.withoutCostOverride(combat.hand.remove(idx));
                combat.exhaustCard(code);
                float[] pos = idxToPos.get(idx);
                addEffect(new ExhaustEffect(pos[0], pos[1]));
                Sample.INSTANCE.play(Assets.Sounds.BURNING);
                exhausted++;
            }
        }
        pureSelectedIndices.clear();
        selectingForPendingExhaust = false;
        combat.pendingHandExhaustSelectCount = 0;
        log("카드를 " + exhausted + "장 소멸시켰습니다.");
        saveCombatState();
        hidePureSelectionBanner();
        refresh();
    }

    private void confirmStrategyRetainSelection() {
        combat.endTurnSelectedRetainIndices.clear();
        combat.endTurnSelectedRetainIndices.addAll(pureSelectedIndices);
        pureSelectedIndices.clear();
        selectingForStrategyRetain = false;
        hidePureSelectionBanner();
        finishEnemyTurnAfterStrategyRetain();
    }

    private void playCard(int index) {
        if (combatLocked || tutorialMessageOpen() || index < 0 || index >= combat.hand.size()) {
            return;
        }
        int cardCode = combat.hand.get(index);
        DeckCard card = DeckCard.byCode(cardCode);
        if (!tutorialAllowsCard(card, cardCode)) {
            return;
        }
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
        if (card == DeckCard.BURNING_PACT && combat.hand.size() > 1) {
            selectingForBurningPact = true;
            pureHandIndex = index;
            pureSelectedIndices.clear();
            pureMaxSelect = 1;
            showBurningPactSelectionBanner();
            refresh();
            return;
        }
        if (card == DeckCard.HIDDEN_DAGGER && combat.hand.size() > 1) {
            selectingForHiddenDagger = true;
            pureHandIndex = index;
            pureSelectedIndices.clear();
            pureMaxSelect = Math.min(2, combat.hand.size() - 1);
            showHiddenDaggerSelectionBanner();
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
        final ArrayList<Integer> handBeforePlay = new ArrayList<>(combat.hand);
        final ArrayList<float[]> handPositionsBeforePlay = new ArrayList<>();
        for (int i = 0; i < combat.hand.size(); i++) {
            float cx = i < cardButtons.size() ? cardButtons.get(i).centerX() : playerCenterX();
            float cy = i < cardButtons.size() ? cardButtons.get(i).centerY() : playerCenterY();
            handPositionsBeforePlay.add(new float[]{cx, cy});
        }
        DeckCard wandCard = null;
        if (targetWandIndex >= 0 && targetWandIndex < combat.hand.size()) {
            wandCard = DeckCard.byCode(combat.hand.get(targetWandIndex));
        }
        final ArrayList<Integer> headbuttDiscardSnapshot = (card == DeckCard.HEADBUTT) ? new ArrayList<>(combat.discardPile) : null;
        int potionCountBefore = DeckBuilderRun.potions.size();
        DeckPlayResult result = combat.play(index, targetWandIndex);
        if (!result.played) {
            log("에너지가 부족합니다.");
            return;
        }
        if (runHud != null && DeckBuilderRun.potions.size() != potionCountBefore) {
            runHud.refresh();
        }
        if (result.gold > 0) {
            spawnFloatingText("+" + result.gold + " 골드", playerCenterX(), playerCenterY() - 30, 0xFFFFD84D);
            if (runHud != null) runHud.refresh();
        }
        advanceTutorialAfterCard(card, cardCode);

        saveCombatState();
        combatLocked = true;

        if (card.hasKeyword(cardCode, DeckCardKeyword.EXHAUST)) {
            final float ex = startX;
            final float ey = startY;
            addEffect(new DelayedActionEffect(0.1f, new Runnable() {
                @Override
                public void run() {
                    addEffect(new ExhaustEffect(ex, ey));
                    Sample.INSTANCE.play(Assets.Sounds.BURNING);
                }
            }));
        }
        if (result.exhausted) {
            spawnEffectExhausts(handBeforePlay, handPositionsBeforePlay, index);
        }

        boolean cardUseSoundPlayed = false;
        if (card.type == DeckCardType.POWER) {
            Sample.INSTANCE.play(Assets.Sounds.CHARMS);
            cardUseSoundPlayed = true;
            final float px = playerCenterX();
            final float py = playerCenterY();
            addEffect(new PowerEffect(px, py));
        }

        if (card == DeckCard.RIPPLE_WALL) {
            Sample.INSTANCE.play(Assets.Sounds.DEWDROP);
            cardUseSoundPlayed = true;
        } else if (card == DeckCard.SCORPION_THROW) {
            Sample.INSTANCE.play(Assets.Sounds.PLANT);
            cardUseSoundPlayed = true;
        } else if (card == DeckCard.SHIV || card == DeckCard.SPECIAL_SHIV) {
            Sword.giorno();
            cardUseSoundPlayed = true;
        } else if (card == DeckCard.ROTATING_NAIL) {
            Sample.INSTANCE.play(Assets.Sounds.EVOKE);
            cardUseSoundPlayed = true;
        }

        if (!cardUseSoundPlayed && card.type == DeckCardType.SKILL) {
            Sample.INSTANCE.play(Assets.Sounds.MISS);
        }

        if (result.heal > 0) {
            spawnHealEffect(result.heal);
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
            final float totalDelay = Math.max(hits.size() * 0.18f + 0.22f, spawnUnanimatedDamageEvents(result, 0.24f));
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
                if (hit.isAttack) {
                    spawnCardAttack(card, startX, startY, hit);
                    finishDelay = Math.max(finishDelay, 0.38f);
                }
            }
            finishDelay = Math.max(finishDelay, spawnUnanimatedDamageEvents(result, 0.24f));
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

        if (card == DeckCard.OSIRIS_GOD) {
            float finishDelay = 0f;
            for (DeckPlayResult.Hit hit : result.hits) {
                if (hit.isAttack) {
                    spawnCardAttack(card, startX, startY, hit);
                    finishDelay = Math.max(finishDelay, 0.38f);
                }
            }
            finishDelay = Math.max(finishDelay, spawnUnanimatedDamageEvents(result, 0.24f));
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
                        showDiscardToHandSelectWindow(combat.pendingDiscardHandSelectCount, 0);
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
            final float totalDelay = Math.max(hits.size() * 0.18f + 0.22f, spawnUnanimatedDamageEvents(result, 0.24f));
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

        if (card == DeckCard.ONSLAUGHT) {
            int maxWave = 0;
            for (DeckPlayResult.Hit h : result.hits) {
                if (h.wave > maxWave) maxWave = h.wave;
            }
            final float waveInterval = 0.25f;
            for (int w = 0; w <= maxWave; w++) {
                for (DeckPlayResult.Hit hit : result.hits) {
                    if (hit.wave != w || !hit.isAttack) continue;
                    final DeckPlayResult.Hit capturedHit = hit;
                    if (w == 0) {
                        spawnCardAttack(card, startX, startY, capturedHit);
                    } else {
                        final float delay = w * waveInterval;
                        addEffect(new DelayedActionEffect(delay, new Runnable() {
                            @Override
                            public void run() {
                                spawnCardAttack(card, startX, startY, capturedHit);
                            }
                        }));
                    }
                }
            }
            final float finishDelay = Math.max(result.hits.isEmpty() ? 0f : maxWave * waveInterval + 0.38f,
                    spawnUnanimatedDamageEvents(result, 0.24f));
            log(card.title(logCardCode) + ": " + cardRulesText(card, logCardCode));
            if (combat.playerDead()) {
                updatePlayerHpUi();
                addEffect(new DelayedActionEffect(finishDelay, new Runnable() {
                    @Override
                    public void run() {
                        playDeath(playerSprite);
                        finishRunDeath();
                    }
                }));
            } else if (combat.won()) {
                addEffect(new DelayedActionEffect(finishDelay + 0.3f, new Runnable() {
                    @Override
                    public void run() {
                        showReward();
                    }
                }));
            } else {
                addEffect(new DelayedActionEffect(finishDelay, new Runnable() {
                    @Override
                    public void run() {
                        refresh();
                    }
                }));
            }
            return;
        }

        if (card == DeckCard.BARRAGE) {
            final ArrayList<DeckPlayResult.Hit> wave0 = new ArrayList<>();
            final ArrayList<DeckPlayResult.Hit> wave1 = new ArrayList<>();
            for (DeckPlayResult.Hit h : result.hits) {
                if (h.wave == 0) wave0.add(h);
                else wave1.add(h);
            }
            for (DeckPlayResult.Hit hit : wave0) {
                if (hit.isAttack) spawnCardAttack(card, startX, startY, hit);
            }
            final float wave1Delay = wave0.isEmpty() ? 0f : 0.38f;
            for (int i = 0; i < wave1.size(); i++) {
                final DeckPlayResult.Hit hit = wave1.get(i);
                addEffect(new DelayedActionEffect(wave1Delay, new Runnable() {
                    @Override
                    public void run() {
                        if (hit.isAttack) spawnCardAttack(card, startX, startY, hit);
                    }
                }));
            }
            final float finishDelay2 = Math.max(wave1.isEmpty() ? (wave0.isEmpty() ? 0f : 0.38f) : wave1Delay + 0.38f,
                    spawnUnanimatedDamageEvents(result, 0.24f));
            log(card.title(logCardCode) + ": " + cardRulesText(card, logCardCode));
            if (combat.playerDead()) {
                updatePlayerHpUi();
                addEffect(new DelayedActionEffect(finishDelay2, new Runnable() {
                    @Override
                    public void run() {
                        playDeath(playerSprite);
                        finishRunDeath();
                    }
                }));
            } else if (combat.won()) {
                addEffect(new DelayedActionEffect(finishDelay2 + 0.3f, new Runnable() {
                    @Override
                    public void run() {
                        showReward();
                    }
                }));
            } else {
                addEffect(new DelayedActionEffect(finishDelay2, new Runnable() {
                    @Override
                    public void run() {
                        refresh();
                    }
                }));
            }
            return;
        }

        if (card == DeckCard.ARMAMENTS && combat.pendingHandCardUpgrade) {
            combat.pendingHandCardUpgrade = false;
            if (result.block > 0) {
                spawnShieldEffect(playerCenterX(), playerCenterY(), "+" + result.block);
            }
            float fd = spawnUnanimatedDamageEvents(result, 0.12f);
            log(card.title(logCardCode) + ": " + cardRulesText(card, logCardCode));
            if (resolveTerminalAfterPassiveDamage(fd)) return;
            addEffect(new DelayedActionEffect(Math.max(0.18f, fd), new Runnable() {
                @Override
                public void run() {
                    showArmamentsHandUpgradeWindow(0);
                }
            }));
            return;
        }

        if (combat.pendingHandCopySelectCopies > 0) {
            float fd = spawnUnanimatedDamageEvents(result, 0.12f);
            log(card.title(logCardCode) + ": " + cardRulesText(card, logCardCode));
            if (resolveTerminalAfterPassiveDamage(fd)) return;
            addEffect(new DelayedActionEffect(Math.max(0.18f, fd), new Runnable() {
                @Override
                public void run() {
                    showDualWieldHandSelectWindow(combat.pendingHandCopySelectCopies, 0);
                }
            }));
            return;
        }

        if (combat.pendingHandCardToDrawPileTop) {
            combat.pendingHandCardToDrawPileTop = false;
            if (result.draw > 0) {
                spawnDrawPileEffects(result.draw, 0.08f);
            }
            float fd = 0f;
            for (DeckPlayResult.Hit hit : result.hits) {
                if (hit.isAttack) {
                    spawnCardAttack(card, startX, startY, hit);
                    fd = Math.max(fd, 0.38f);
                }
            }
            fd = Math.max(fd, spawnUnanimatedDamageEvents(result, 0.12f));
            log(card.title(logCardCode) + ": " + cardRulesText(card, logCardCode));
            if (resolveTerminalAfterPassiveDamage(fd)) return;
            addEffect(new DelayedActionEffect(Math.max(0.25f, fd), new Runnable() {
                @Override
                public void run() {
                    showForesightHandSelectWindow(0);
                }
            }));
            return;
        }

        if (combat.pendingDiscardToDrawPileTop) {
            combat.pendingDiscardToDrawPileTop = false;
            final ArrayList<Integer> discardSnapshot = new ArrayList<>(combat.discardPile);
            if (result.block > 0) {
                spawnShieldEffect(playerCenterX(), playerCenterY(), "+" + result.block);
            }
            float fd = spawnUnanimatedDamageEvents(result, 0.12f);
            log(card.title(logCardCode) + ": " + cardRulesText(card, logCardCode));
            if (resolveTerminalAfterPassiveDamage(fd)) return;
            addEffect(new DelayedActionEffect(Math.max(0.25f, fd), new Runnable() {
                @Override
                public void run() {
                    if (discardSnapshot.isEmpty()) {
                        refresh();
                    } else {
                        showHeadbuttDiscardSelectWindow(discardSnapshot, 0);
                    }
                }
            }));
            return;
        }

        if (combat.pendingDiscardHandSelectCount > 0 && card != DeckCard.OSIRIS_GOD) {
            if (result.block > 0) {
                spawnShieldEffect(playerCenterX(), playerCenterY(), "+" + result.block);
            }
            float fd = spawnUnanimatedDamageEvents(result, 0.12f);
            log(card.title(logCardCode) + ": " + cardRulesText(card, logCardCode));
            if (resolveTerminalAfterPassiveDamage(fd)) return;
            addEffect(new DelayedActionEffect(Math.max(0.1f, fd), new Runnable() {
                @Override
                public void run() {
                    showDiscardToHandSelectWindow(combat.pendingDiscardHandSelectCount, 0);
                }
            }));
            return;
        }

        if (card == DeckCard.SHITTIM_BOX && combat.pendingAllCardDiscover) {
            combat.pendingAllCardDiscover = false;
            float fd = spawnUnanimatedDamageEvents(result, 0.12f);
            log(card.title(logCardCode) + ": " + cardRulesText(card, logCardCode));
            if (resolveTerminalAfterPassiveDamage(fd)) return;
            addEffect(new DelayedActionEffect(Math.max(0.12f, fd), new Runnable() {
                @Override
                public void run() {
                    showShittimBoxSelectWindow("", 0);
                }
            }));
            return;
        }

        if (card == DeckCard.RELIC_SELECTION_BOX && combat.pendingAllRelicDiscover) {
            combat.pendingAllRelicDiscover = false;
            float fd = spawnUnanimatedDamageEvents(result, 0.12f);
            log(card.title(logCardCode) + ": " + cardRulesText(card, logCardCode));
            if (resolveTerminalAfterPassiveDamage(fd)) return;
            addEffect(new DelayedActionEffect(Math.max(0.12f, fd), new Runnable() {
                @Override
                public void run() {
                    showRelicSelectionBoxWindow("", 0);
                }
            }));
            return;
        }

        if (card == DeckCard.POTION_SELECTION_BOX && combat.pendingAllPotionDiscover) {
            combat.pendingAllPotionDiscover = false;
            float fd = spawnUnanimatedDamageEvents(result, 0.12f);
            log(card.title(logCardCode) + ": " + cardRulesText(card, logCardCode));
            if (resolveTerminalAfterPassiveDamage(fd)) return;
            addEffect(new DelayedActionEffect(Math.max(0.12f, fd), new Runnable() {
                @Override
                public void run() {
                    if (DeckBuilderRun.potions.size() >= DeckBuilderRun.maxPotionSlots()) {
                        addToFront(new WndMessage("포션\n\n빈 포션 슬롯이 없습니다."));
                        refresh();
                    } else {
                        showPotionSelectionBoxWindow(0);
                    }
                }
            }));
            return;
        }

        if (combat.pendingDrawPileTypeSelect != null) {
            final DeckCardType selectType = combat.pendingDrawPileTypeSelect;
            combat.pendingDrawPileTypeSelect = null;
            float fd = spawnUnanimatedDamageEvents(result, 0.12f);
            log(card.title(logCardCode) + ": " + cardRulesText(card, logCardCode));
            if (resolveTerminalAfterPassiveDamage(fd)) return;
            addEffect(new DelayedActionEffect(Math.max(0.12f, fd), new Runnable() {
                @Override
                public void run() {
                    showDrawPileTypeSelectWindow(selectType, 0);
                }
            }));
            return;
        }

        if (card == DeckCard.SCOUT_STRIKE && combat.pendingDrawPilePeek) {
            combat.pendingDrawPilePeek = false;
            float fd = 0f;
            for (DeckPlayResult.Hit hit : result.hits) {
                if (hit.isAttack) {
                    spawnCardAttack(card, startX, startY, hit);
                    fd = Math.max(fd, 0.38f);
                }
            }
            fd = Math.max(fd, spawnUnanimatedDamageEvents(result, 0.24f));
            log(card.title(logCardCode) + ": " + cardRulesText(card, logCardCode));
            if (combat.playerDead()) {
                updatePlayerHpUi();
                final float deadDelay = fd;
                addEffect(new DelayedActionEffect(deadDelay > 0f ? deadDelay : 0.45f, new Runnable() {
                    @Override
                    public void run() {
                        playDeath(playerSprite);
                        finishRunDeath();
                    }
                }));
            } else if (combat.won()) {
                final float wonDelay = fd;
                addEffect(new DelayedActionEffect(Math.max(0.9f, wonDelay + 0.3f), new Runnable() {
                    @Override
                    public void run() {
                        showReward();
                    }
                }));
            } else {
                final float peekDelay = fd;
                addEffect(new DelayedActionEffect(Math.max(0.1f, peekDelay), new Runnable() {
                    @Override
                    public void run() {
                        showScoutStrikeSelectWindow();
                    }
                }));
            }
            return;
        }

        float finishDelay = 0f;
        if (!result.shuffles.isEmpty()) {
            finishDelay = Math.max(finishDelay, spawnShuffleEffects(result, startX, startY));
        }

        int maxHitWave = 0;
        for (DeckPlayResult.Hit h : result.hits) {
            if (h.wave > maxHitWave) maxHitWave = h.wave;
        }
        final float hitWaveInterval = 0.25f;
        for (int w = 0; w <= maxHitWave; w++) {
            for (DeckPlayResult.Hit hit : result.hits) {
                if (hit.wave != w) continue;
                final DeckPlayResult.Hit capturedHit = hit;
                if (w == 0) {
                    if (hit.isAttack) {
                        spawnCardAttack(card, startX, startY, hit);
                        finishDelay = Math.max(finishDelay, 0.38f);
                    }
                    if (hit.vulnerable > 0) {
                        EnemyView view = enemyView(hit.enemyIndex);
                        if (view != null) {
                            spawnFloatingText("피해 증폭 +" + hit.vulnerable, enemyCenterX(view), enemyCenterY(view) - 24, 0xFFFFD66B);
                        }
                    }
                } else {
                    final float wDelay = w * hitWaveInterval;
                    addEffect(new DelayedActionEffect(wDelay, new Runnable() {
                        @Override
                        public void run() {
                            if (capturedHit.isAttack)
                                spawnCardAttack(card, startX, startY, capturedHit);
                            if (capturedHit.vulnerable > 0) {
                                EnemyView view = enemyView(capturedHit.enemyIndex);
                                if (view != null) {
                                    spawnFloatingText("피해 증폭 +" + capturedHit.vulnerable, enemyCenterX(view), enemyCenterY(view) - 24, 0xFFFFD66B);
                                }
                            }
                        }
                    }));
                    finishDelay = Math.max(finishDelay, w * hitWaveInterval + 0.38f);
                }
            }
        }
        if (result.block > 0) {
            spawnShieldEffect(playerCenterX(), playerCenterY(), "+" + result.block);
        }
        finishDelay = Math.max(finishDelay, spawnUnanimatedDamageEvents(result, 0.24f));
        if (result.strength > 0) {
            spawnFloatingText("공격력 +" + result.strength, playerCenterX(), playerCenterY() - 24, 0xFFFFD84D);
        }
        if (result.dexterity > 0) {
            spawnFloatingText("방어력 증가 +" + result.dexterity, playerCenterX(), playerCenterY() - 36, 0xFF77C8FF);
        }
        if (result.draw > 0) {
            finishDelay = Math.max(finishDelay, spawnDrawPileEffects(result.draw, 0.08f));
        }
        String autoPlaySuffix = buildRandomPlayLog(combat.lastAutoPlayResults);
        finishDelay = Math.max(finishDelay, spawnAutoPlayEffects());
        log(card.title(logCardCode) + ": " + cardRulesText(card, logCardCode) + autoPlaySuffix);
        final boolean endTurnAfterCard = card == DeckCard.CONCLUSION || combat.endTurnRequested;
        if (endTurnAfterCard) combat.endTurnRequested = false;
        final boolean discardAfterCard = (card == DeckCard.DAGGER_THROW || combat.pendingHandDiscardSelectCount > 0) && !combat.hand.isEmpty();
        if (combat.playerDead()) {
            updatePlayerHpUi();
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
                    } else if (discardAfterCard) {
                        beginDaggerThrowDiscardSelection();
                    } else if (endTurnAfterCard) {
                        startEnemyTurn();
                    } else {
                        refresh();
                    }
                }
            }));
        } else if (discardAfterCard) {
            beginDaggerThrowDiscardSelection();
        } else if (endTurnAfterCard) {
            startEnemyTurn();
        } else {
            refresh();
        }
    }

    private void beginDaggerThrowDiscardSelection() {
        if (combat.hand.isEmpty()) {
            combat.pendingDaggerThrowDiscard = false;
            combat.pendingHandDiscardSelectCount = 0;
            saveCombatState();
            refresh();
            return;
        }
        combat.pendingDaggerThrowDiscard = true;
        if (combat.pendingHandDiscardSelectCount <= 0) combat.pendingHandDiscardSelectCount = 1;
        combat.pendingHandDiscardSelectCount = Math.min(combat.pendingHandDiscardSelectCount, combat.hand.size());
        saveCombatState();
        restoreDaggerThrowDiscardSelection();
    }

    private void restoreDaggerThrowDiscardSelection() {
        selectingForDaggerThrowDiscard = true;
        pureHandIndex = -1;
        pureSelectedIndices.clear();
        if (combat.pendingHandDiscardSelectCount <= 0) combat.pendingHandDiscardSelectCount = 1;
        pureMaxSelect = Math.min(combat.pendingHandDiscardSelectCount, combat.hand.size());
        showDaggerThrowDiscardBanner();
        refresh();
    }

    private void restorePendingExhaustSelection() {
        if (combat.hand.isEmpty()) {
            combat.pendingHandExhaustSelectCount = 0;
            saveCombatState();
            refresh();
            return;
        }
        selectingForPendingExhaust = true;
        pureHandIndex = -1;
        pureSelectedIndices.clear();
        pureMaxSelect = Math.min(Math.max(1, combat.pendingHandExhaustSelectCount), combat.hand.size());
        showPendingExhaustSelectionBanner();
        refresh();
    }

    private boolean resolveTerminalAfterPassiveDamage(float finishDelay) {
        if (combat.playerDead()) {
            updatePlayerHpUi();
            addEffect(new DelayedActionEffect(finishDelay > 0f ? finishDelay : 0.45f, new Runnable() {
                @Override
                public void run() {
                    playDeath(playerSprite);
                    finishRunDeath();
                }
            }));
            return true;
        }
        if (combat.won()) {
            addEffect(new DelayedActionEffect(Math.max(0.9f, finishDelay + 0.3f), new Runnable() {
                @Override
                public void run() {
                    showReward();
                }
            }));
            return true;
        }
        return false;
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
                return !rewardOpen && !combatLocked && !tutorialMessageOpen();
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
        if (combatLocked || rewardOpen || tutorialMessageOpen() || potion == null) return;
        int potionHeal = DeckBuilderRun.onPotionUsed();
        if (potionHeal > 0) {
            spawnFloatingText("체력 +" + potionHeal, playerCenterX(), playerCenterY() - 42, 0xFF66FF99);
        }
        if (DeckBuilderRun.hasRelic(DeckRelic.OJIRO_MEMORY_DISC)) {
            combat.playerTurnStrength += 3;
            spawnFloatingText("공격력 +3", playerCenterX(), playerCenterY() - 30, 0xFFFFD84D);
        }

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
                DeckCombatEnemy fireTarget = combat.target();
                int dealt = combat.damageEnemy(fireTarget, 20, false);
                EnemyView fireView = enemyView(combat.enemyIndex(fireTarget));
                if (fireView != null) {
                    addEffect(new ImpactEffect(enemyCenterX(fireView), enemyCenterY(fireView), 0xFFFF7A35));
                    spawnFloatingText("-" + dealt, enemyCenterX(fireView), enemyCenterY(fireView) - 16, 0xFFFF705A);
                    fireView.name.text(fireView.enemy.name + "  " + fireView.enemy.hp + "/" + fireView.enemy.ht);
                    fireView.hp.size(ACTOR_HP_W * fireView.enemy.hp / (float) fireView.enemy.ht, ACTOR_HP_H);
                    if (!fireTarget.alive()) {
                        playDeath(fireView.sprite);
                        hideEnemyUI(fireView);
                    }
                }
                log(potion.title + ": 대상에게 20 피해를 입혔습니다.");
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
            case ATTACK:
                useDiscoverPotion(slot, potion, DeckDiscover.Pool.ATTACK);
                break;
            case FLEX:
                DeckBuilderRun.removePotion(slot);
                if (runHud != null) runHud.refresh();
                Sample.INSTANCE.play(Assets.Sounds.DRINK);
                combat.playerTurnStrength += 5;
                spawnFloatingText("공격력 +5", playerCenterX(), playerCenterY() - 24, 0xFFFFD84D);
                log(potion.title + ": 공격력을 5 얻었습니다.");
                saveCombatState();
                refresh();
                break;
            case COLORLESS:
                useDiscoverPotion(slot, potion, DeckDiscover.Pool.NEUTRAL_ONLY);
                break;
            case DEXTERITY:
                DeckBuilderRun.removePotion(slot);
                if (runHud != null) runHud.refresh();
                Sample.INSTANCE.play(Assets.Sounds.DRINK);
                combat.playerDexterity += 2;
                spawnFloatingText("방어력 증가 +2", playerCenterX(), playerCenterY() - 24, 0xFF77C8FF);
                log(potion.title + ": 방어력 증가를 2 얻었습니다.");
                saveCombatState();
                refresh();
                break;
            case BLOCK:
                DeckBuilderRun.removePotion(slot);
                if (runHud != null) runHud.refresh();
                Sample.INSTANCE.play(Assets.Sounds.DRINK);
                int block = combat.gainBlock(12);
                spawnShieldEffect(playerCenterX(), playerCenterY(), "+" + block);
                log(potion.title + ": 보호막을 " + block + " 얻었습니다.");
                saveCombatState();
                refresh();
                break;
            case SPEED:
                DeckBuilderRun.removePotion(slot);
                if (runHud != null) runHud.refresh();
                Sample.INSTANCE.play(Assets.Sounds.DRINK);
                combat.playerDexterity += 5;
                combat.playerTurnDexterity += 5;
                spawnFloatingText("방어력 증가 +5", playerCenterX(), playerCenterY() - 24, 0xFF77C8FF);
                log(potion.title + ": 방어력 증가를 5 얻었습니다.");
                saveCombatState();
                refresh();
                break;
            case SKILL:
                useDiscoverPotion(slot, potion, DeckDiscover.Pool.SKILL);
                break;
            case WEAK:
                useDebuffPotion(slot, potion, false);
                break;
            case ENERGY:
                DeckBuilderRun.removePotion(slot);
                if (runHud != null) runHud.refresh();
                Sample.INSTANCE.play(Assets.Sounds.DRINK);
                combat.energy = Math.min(DeckBuilderRun.MAX_ENERGY_CAP, combat.energy + 2);
                spawnFloatingText("에너지 +2", playerCenterX(), playerCenterY() - 24, 0xFFAAFF66);
                log(potion.title + ": 에너지를 2 얻었습니다.");
                saveCombatState();
                refresh();
                break;
            case VULNERABLE:
                useDebuffPotion(slot, potion, true);
                break;
            case POWER:
                useDiscoverPotion(slot, potion, DeckDiscover.Pool.POWER);
                break;
            case EXPLOSIVE_AMPHULE:
                DeckBuilderRun.removePotion(slot);
                if (runHud != null) runHud.refresh();
                combatLocked = true;
                Sample.INSTANCE.play(Assets.Sounds.SHATTER);
                Sample.INSTANCE.play(Assets.Sounds.BLAST);
                for (DeckCombatEnemy enemy : combat.enemies) {
                    if (!enemy.alive()) continue;
                    int ampuleDamage = combat.damageEnemy(enemy, 10, false);
                    EnemyView view = enemyView(combat.enemyIndex(enemy));
                    if (view != null) {
                        addEffect(new ImpactEffect(enemyCenterX(view), enemyCenterY(view), 0xFFFF9040));
                        spawnFloatingText("-" + ampuleDamage, enemyCenterX(view), enemyCenterY(view) - 16, 0xFFFF705A);
                        view.name.text(view.enemy.name + "  " + view.enemy.hp + "/" + view.enemy.ht);
                        view.hp.size(ACTOR_HP_W * view.enemy.hp / (float) view.enemy.ht, ACTOR_HP_H);
                        if (!enemy.alive()) {
                            playDeath(view.sprite);
                            hideEnemyUI(view);
                        }
                    }
                }
                log(potion.title + ": 모든 적에게 10 피해를 줬습니다.");
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
            case FORTIFIER:
                DeckBuilderRun.removePotion(slot);
                if (runHud != null) runHud.refresh();
                Sample.INSTANCE.play(Assets.Sounds.DRINK);
                int oldBlock = combat.block;
                combat.block *= 3;
                spawnShieldEffect(playerCenterX(), playerCenterY(), "+" + Math.max(0, combat.block - oldBlock));
                log(potion.title + ": 현재 보호막을 3배로 만들었습니다.");
                saveCombatState();
                refresh();
                break;
            case TOUCH_OF_INSANITY:
                if (combat.hand.isEmpty()) return;
                Sample.INSTANCE.play(Assets.Sounds.DRINK);
                touchOfInsanityActive = true;
                touchOfInsanitySlot = slot;
                pureSelectedIndices.clear();
                showTouchOfInsanityBanner();
                refresh();
                break;
            case RADIANT_TINCTURE:
                DeckBuilderRun.removePotion(slot);
                if (runHud != null) runHud.refresh();
                Sample.INSTANCE.play(Assets.Sounds.DRINK);
                combat.energy = Math.min(DeckBuilderRun.MAX_ENERGY_CAP, combat.energy + 1);
                combat.bonusEnergyTurns += 3;
                spawnFloatingText("에너지 +1", playerCenterX(), playerCenterY() - 24, 0xFFAAFF66);
                log(potion.title + ": 에너지를 1 얻고 다음 3턴 에너지 +1을 준비했습니다.");
                saveCombatState();
                refresh();
                break;
            case CLARITY_EXTRACT:
                DeckBuilderRun.removePotion(slot);
                if (runHud != null) runHud.refresh();
                Sample.INSTANCE.play(Assets.Sounds.DRINK);
                combat.draw(1);
                combat.bonusDrawTurns += 3;
                spawnDrawPileEffects(1, 0.05f);
                log(potion.title + ": 카드를 1장 뽑고 다음 3턴 추가 드로우를 준비했습니다.");
                saveCombatState();
                if (!resolveAutoPlayResultsAfterDraw()) refresh();
                break;
            case CURE_ALL:
                DeckBuilderRun.removePotion(slot);
                if (runHud != null) runHud.refresh();
                Sample.INSTANCE.play(Assets.Sounds.DRINK);
                combat.energy = Math.min(DeckBuilderRun.MAX_ENERGY_CAP, combat.energy + 1);
                combat.draw(2);
                spawnFloatingText("에너지 +1", playerCenterX(), playerCenterY() - 24, 0xFFAAFF66);
                spawnDrawPileEffects(2, 0.05f);
                log(potion.title + ": 에너지를 1 얻고 카드를 2장 뽑았습니다.");
                saveCombatState();
                if (!resolveAutoPlayResultsAfterDraw()) refresh();
                break;
            case HEART_OF_IRON:
                DeckBuilderRun.removePotion(slot);
                if (runHud != null) runHud.refresh();
                Sample.INSTANCE.play(Assets.Sounds.DRINK);
                combat.playerRegen += 7;
                spawnFloatingText("재생 +7", playerCenterX(), playerCenterY() - 24, 0xFF80FF80);
                log(potion.title + ": 재생을 7 얻었습니다.");
                saveCombatState();
                refresh();
                break;
            case FYSH_OIL:
                DeckBuilderRun.removePotion(slot);
                if (runHud != null) runHud.refresh();
                Sample.INSTANCE.play(Assets.Sounds.DRINK);
                combat.playerStrength += 1;
                combat.playerDexterity += 1;
                spawnFloatingText("공격력 +1 / 방어력 증가 +1", playerCenterX(), playerCenterY() - 24, 0xFFFFD84D);
                log(potion.title + ": 공격력을 1, 방어력 증가를 1 얻었습니다.");
                saveCombatState();
                refresh();
                break;
            case DUPLICATOR:
                DeckBuilderRun.removePotion(slot);
                if (runHud != null) runHud.refresh();
                Sample.INSTANCE.play(Assets.Sounds.DRINK);
                combat.duplicateNextCards += 1;
                spawnFloatingText("다음 카드 복제", playerCenterX(), playerCenterY() - 24, 0xFFE8C8FF);
                log(potion.title + ": 이번 턴에 사용하는 다음 카드를 1번 추가로 사용합니다.");
                saveCombatState();
                refresh();
                break;
            case BINDING:
                useBindingPotion(slot, potion);
                break;
            case STABLE_SERUM:
                DeckBuilderRun.removePotion(slot);
                if (runHud != null) runHud.refresh();
                Sample.INSTANCE.play(Assets.Sounds.DRINK);
                combat.retainHandTurns = Math.max(combat.retainHandTurns, 2);
                spawnFloatingText("보존 2턴", playerCenterX(), playerCenterY() - 24, 0xFFB6F2FF);
                log(potion.title + ": 손에 있는 카드를 2턴 동안 보존합니다.");
                saveCombatState();
                refresh();
                break;
            case LIQUID_BRONZE:
                DeckBuilderRun.removePotion(slot);
                if (runHud != null) runHud.refresh();
                Sample.INSTANCE.play(Assets.Sounds.DRINK);
                combat.playerPermanentThorns += 3;
                combat.playerThorns += 3;
                spawnFloatingText("반격 +3", playerCenterX(), playerCenterY() - 24, 0xFFFFB36B);
                log(potion.title + ": 반격을 3 얻었습니다. (영구)");
                saveCombatState();
                refresh();
                break;
            case FORGE_BLESSING:
                DeckBuilderRun.removePotion(slot);
                if (runHud != null) runHud.refresh();
                Sample.INSTANCE.play(Assets.Sounds.CHARMS);
                for (int i = 0; i < combat.hand.size(); i++) {
                    combat.hand.set(i, DeckCardCode.upgrade(combat.hand.get(i)));
                }
                spawnFloatingText("손패 강화", playerCenterX(), playerCenterY() - 24, 0xFFFFD84D);
                log(potion.title + ": 손에 있는 모든 카드를 강화했습니다.");
                saveCombatState();
                refresh();
                break;
            case REGEN:
                DeckBuilderRun.removePotion(slot);
                if (runHud != null) runHud.refresh();
                Sample.INSTANCE.play(Assets.Sounds.DRINK);
                int beforeHp = DeckBuilderRun.playerHP;
                DeckBuilderRun.playerHP = Math.min(DeckBuilderRun.playerHT, DeckBuilderRun.playerHP + 8);
                spawnFloatingText("체력 +" + (DeckBuilderRun.playerHP - beforeHp), playerCenterX(), playerCenterY() - 24, 0xFF80FF80);
                log(potion.title + ": 체력을 " + (DeckBuilderRun.playerHP - beforeHp) + " 회복했습니다.");
                saveCombatState();
                refresh();
                break;
            case POWDERED_DEMISE:
                usePowderedDemise(slot, potion);
                break;
            case GIGANTIFICATION:
                DeckBuilderRun.removePotion(slot);
                if (runHud != null) runHud.refresh();
                Sample.INSTANCE.play(Assets.Sounds.DRINK);
                combat.nextAttackDamageMultiplier = Math.max(combat.nextAttackDamageMultiplier, 3);
                spawnFloatingText("다음 공격 x3", playerCenterX(), playerCenterY() - 24, 0xFFFFD84D);
                log(potion.title + ": 다음 공격 카드의 피해량이 3배가 됩니다.");
                saveCombatState();
                refresh();
                break;
            case FRUIT_JUICE:
                DeckBuilderRun.removePotion(slot);
                if (runHud != null) runHud.refresh();
                Sample.INSTANCE.play(Assets.Sounds.DRINK);
                DeckBuilderRun.playerHT += 5;
                DeckBuilderRun.playerHP += 5;
                spawnFloatingText("최대 체력 +5", playerCenterX(), playerCenterY() - 24, 0xFF80FF80);
                log(potion.title + ": 최대 체력을 5 얻었습니다.");
                saveCombatState();
                refresh();
                break;
            case BEETLE_JUICE:
                DeckBuilderRun.removePotion(slot);
                if (runHud != null) runHud.refresh();
                Sample.INSTANCE.play(Assets.Sounds.DRINK);
                combat.incomingDamageReductionTurns = Math.max(combat.incomingDamageReductionTurns, 4);
                spawnFloatingText("피해 -30%", playerCenterX(), playerCenterY() - 24, 0xFFB6D8FF);
                log(potion.title + ": 다음 4턴 동안 적 공격 피해가 30% 감소합니다.");
                saveCombatState();
                refresh();
                break;
            case MAZALETHS_GIFT:
                DeckBuilderRun.removePotion(slot);
                if (runHud != null) runHud.refresh();
                Sample.INSTANCE.play(Assets.Sounds.DRINK);
                combat.strengthPerTurn += 1;
                spawnFloatingText("턴 시작 공격력 +1", playerCenterX(), playerCenterY() - 24, 0xFFFFD84D);
                log(potion.title + ": 턴이 시작할 때마다 공격력을 1 얻습니다.");
                saveCombatState();
                refresh();
                break;
            case BOTTLED_POTENTIAL:
                DeckBuilderRun.removePotion(slot);
                if (runHud != null) runHud.refresh();
                Sample.INSTANCE.play(Assets.Sounds.DRINK);
                combat.shuffleAllCardsIntoDrawPile();
                combat.draw(5);
                spawnDrawPileEffects(5, 0.05f);
                log(potion.title + ": 모든 카드를 뽑을 카드 더미에 섞고 카드를 5장 뽑았습니다.");
                saveCombatState();
                if (!resolveAutoPlayResultsAfterDraw()) refresh();
                break;
            case SHIP_IN_A_BOTTLE:
                DeckBuilderRun.removePotion(slot);
                if (runHud != null) runHud.refresh();
                Sample.INSTANCE.play(Assets.Sounds.DRINK);
                int shipBlock = combat.gainBlock(10);
                combat.nextTurnBlock += 10;
                spawnShieldEffect(playerCenterX(), playerCenterY(), "+" + shipBlock);
                log(potion.title + ": 보호막을 10 얻고 다음 턴 보호막 10을 준비했습니다.");
                saveCombatState();
                refresh();
                break;
            case FAIRY_IN_A_BOTTLE:
                log(potion.title + ": 사망 시 자동으로 발동하는 포션입니다.");
                break;
            case SHACKLING:
                DeckBuilderRun.removePotion(slot);
                if (runHud != null) runHud.refresh();
                Sample.INSTANCE.play(Assets.Sounds.SHATTER);
                for (DeckCombatEnemy enemy : combat.enemies) {
                    if (enemy.alive()) enemy.turnStrengthLoss += 7;
                }
                log(potion.title + ": 이번 턴 동안 모든 적이 공격력을 7 잃습니다.");
                saveCombatState();
                refresh();
                break;
            case SNECKO_OIL:
                DeckBuilderRun.removePotion(slot);
                if (runHud != null) runHud.refresh();
                Sample.INSTANCE.play(Assets.Sounds.DRINK);
                combat.draw(7);
                combat.randomizeHandCostsThisTurn();
                spawnDrawPileEffects(7, 0.05f);
                log(potion.title + ": 카드를 7장 뽑고 손의 카드 비용을 무작위로 바꿨습니다.");
                saveCombatState();
                if (!resolveAutoPlayResultsAfterDraw()) refresh();
                break;
            case LIQUID_MEMORIES:
                if (combat.discardPile.isEmpty()) return;
                Sample.INSTANCE.play(Assets.Sounds.DRINK);
                showLiquidMemoriesDiscardSelectWindow(slot, potion, new ArrayList<>(combat.discardPile), 0);
                break;
            case ENTROPIC_BREW:
                DeckBuilderRun.removePotion(slot);
                for (int i = DeckBuilderRun.potions.size(); i < DeckBuilderRun.maxPotionSlots(); i++) {
                    DeckBuilderRun.addPotion(DeckPotionPolicy.randomPotion(DeckPotionPolicy.rollRarity()));
                }
                if (runHud != null) runHud.refresh();
                Sample.INSTANCE.play(Assets.Sounds.DRINK);
                log(potion.title + ": 비어있는 포션 슬롯을 무작위 포션으로 채웠습니다.");
                saveCombatState();
                refresh();
                break;
            case PRECOGNITION_DROPLET:
                if (combat.drawPile.isEmpty()) return;
                Sample.INSTANCE.play(Assets.Sounds.DRINK);
                showPrecognitionDrawSelectWindow(slot, potion, new ArrayList<>(combat.drawPile), 0);
                break;
            case OROBIC_ACID:
                DeckBuilderRun.removePotion(slot);
                if (runHud != null) runHud.refresh();
                Sample.INSTANCE.play(Assets.Sounds.DRINK);
                addRandomZeroCostPotionCard(DeckDiscover.Pool.ATTACK);
                addRandomZeroCostPotionCard(DeckDiscover.Pool.SKILL);
                addRandomZeroCostPotionCard(DeckDiscover.Pool.POWER);
                log(potion.title + ": 무작위 공격, 보조, 지속 카드를 손으로 가져왔습니다.");
                saveCombatState();
                refresh();
                break;
            case DISTILLED_CHAOS:
                DeckBuilderRun.removePotion(slot);
                if (runHud != null) runHud.refresh();
                Sample.INSTANCE.play(Assets.Sounds.DRINK);
                combat.playTopFromDrawPile(3);
                log(potion.title + ": 뽑을 카드 더미에서 카드 3장을 시전했습니다." + buildRandomPlayLog(combat.lastAutoPlayResults));
                saveCombatState();
                if (!resolveAutoPlayResultsAfterDraw()) refresh();
                break;
            case LUCKY_TONIC:
                DeckBuilderRun.removePotion(slot);
                if (runHud != null) runHud.refresh();
                Sample.INSTANCE.play(Assets.Sounds.DRINK);
                combat.preventHpLossTurns = Math.max(combat.preventHpLossTurns, 1);
                spawnFloatingText("HP 피해 무효", playerCenterX(), playerCenterY() - 24, 0xFFAAFF66);
                log(potion.title + ": 다음 턴에 체력을 잃지 않습니다.");
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

    private void useDiscoverPotion(int slot, DeckPotion potion, DeckDiscover.Pool pool) {
        DeckBuilderRun.removePotion(slot);
        if (runHud != null) runHud.refresh();
        Sample.INSTANCE.play(Assets.Sounds.DRINK);
        DeckDiscover discover = new DeckDiscover(pool, 3, true, false);
        combat.pendingDiscoverChoices = discover.rollChoices(combat);
        combat.pendingDiscoverZeroCost = true;
        combat.pendingDiscoverTransient = true;
        combat.pendingDiscoverUpgraded = false;
        combat.pendingDiscoverPlayAfterPick = false;
        log(potion.title + ": 카드를 선택합니다.");
        saveCombatState();
        refresh();
    }

    private void useDebuffPotion(int slot, DeckPotion potion, boolean vulnerable) {
        DeckBuilderRun.removePotion(slot);
        if (runHud != null) runHud.refresh();
        Sample.INSTANCE.play(Assets.Sounds.SHATTER);
        DeckCombatEnemy target = combat.target();
        EnemyView view = enemyView(combat.enemyIndex(target));
        boolean applied = combat.applyEnemyDebuff(target);
        if (applied) {
            if (vulnerable) {
                target.vulnerable += 3;
            } else {
                target.attackDown += 3;
            }
        }
        if (view != null) {
            spawnFloatingText(vulnerable ? "피해 증폭 +3" : "공격력 저하 +3", enemyCenterX(view), enemyCenterY(view) - 24, vulnerable ? 0xFFFFD66B : 0xFFB6D8FF);
        }
        log(potion.title + ": " + (vulnerable ? "피해 증폭" : "공격력 저하") + "을 3 부여했습니다.");
        saveCombatState();
        refresh();
    }

    private void useBindingPotion(int slot, DeckPotion potion) {
        DeckBuilderRun.removePotion(slot);
        if (runHud != null) runHud.refresh();
        Sample.INSTANCE.play(Assets.Sounds.SHATTER);
        for (DeckCombatEnemy enemy : combat.enemies) {
            if (!enemy.alive()) continue;
            if (!combat.applyEnemyDebuff(enemy)) continue;
            enemy.attackDown += 1;
            enemy.vulnerable += 1;
        }
        log(potion.title + ": 모든 적에게 공격력 저하 1, 피해 증폭 1을 부여했습니다.");
        saveCombatState();
        refresh();
    }

    private void usePowderedDemise(int slot, DeckPotion potion) {
        DeckBuilderRun.removePotion(slot);
        if (runHud != null) runHud.refresh();
        Sample.INSTANCE.play(Assets.Sounds.SHATTER);
        DeckCombatEnemy target = combat.target();
        if (combat.applyEnemyDebuff(target)) {
            target.demise += 9;
        }
        EnemyView view = enemyView(combat.enemyIndex(target));
        if (view != null) {
            spawnFloatingText("종언 +9", enemyCenterX(view), enemyCenterY(view) - 24, 0xFFB68CFF);
        }
        log(potion.title + ": 대상에게 종언을 9 부여했습니다.");
        saveCombatState();
        refresh();
    }

    private void addRandomZeroCostPotionCard(DeckDiscover.Pool pool) {
        DeckCard[] choices = new DeckDiscover(pool, 1, true, false).rollChoices(combat);
        if (choices.length == 0) return;
        int code = choices[0].code();
        code = DeckCard.withKeyword(code, DeckCardKeyword.ZERO_COST);
        code = DeckCard.withKeyword(code, DeckCardKeyword.TRANSIENT);
        code = DeckCard.withKeyword(code, DeckCardKeyword.EXHAUST);
        combat.addToHand(code);
    }

    private void showLiquidMemoriesDiscardSelectWindow(final int slot, final DeckPotion potion, final ArrayList<Integer> snapshot, final int page) {
        showPotionPileSelectWindow("버린 카드 더미에서 가져올 카드 선택", snapshot, page, new PotionPilePickHandler() {
            @Override
            public void onPick(int snapIndex, int code) {
                combat.discardPile.remove(snapIndex);
                combat.addToHand(DeckCard.withKeyword(code, DeckCardKeyword.ZERO_COST));
                DeckBuilderRun.removePotion(slot);
                if (runHud != null) runHud.refresh();
                log(potion.title + ": 버린 카드 더미에서 카드를 1장 가져왔습니다.");
                saveCombatState();
                refresh();
            }
        });
    }

    private void showPrecognitionDrawSelectWindow(final int slot, final DeckPotion potion, final ArrayList<Integer> snapshot, final int page) {
        showPotionPileSelectWindow("뽑을 카드 더미에서 가져올 카드 선택", snapshot, page, new PotionPilePickHandler() {
            @Override
            public void onPick(int snapIndex, int code) {
                combat.drawPile.remove(snapIndex);
                combat.addToHand(code);
                DeckBuilderRun.removePotion(slot);
                if (runHud != null) runHud.refresh();
                log(potion.title + ": 뽑을 카드 더미에서 카드를 1장 가져왔습니다.");
                saveCombatState();
                refresh();
            }
        });
    }

    private interface PotionPilePickHandler {
        void onPick(int snapIndex, int code);
    }

    private void showPotionPileSelectWindow(final String titleText, final ArrayList<Integer> snapshot, final int page, final PotionPilePickHandler handler) {
        final int total = snapshot.size();
        if (total == 0) {
            refresh();
            return;
        }

        final int CARD_W = 42;
        final int CARD_H = 54;
        final int CARD_GAP = 5;
        final int CARDS_PER_PAGE = 4;

        final int maxPage = Math.max(0, (total - 1) / CARDS_PER_PAGE);
        final int currentPage = Math.max(0, Math.min(page, maxPage));
        final int first = currentPage * CARDS_PER_PAGE;
        final int count = Math.min(CARDS_PER_PAGE, total - first);
        final int totalCardW = count * CARD_W + (count - 1) * CARD_GAP;
        final int width = Math.max(196, totalCardW + 20);

        final Window win = new Window() {
            @Override
            public void onBackPressed() {
            }
        };

        int pos = 7;
        RenderedTextBlock title = renderTextBlock(titleText, 8);
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
                @Override
                protected DeckCard card() {
                    return DeckCard.byCode(code);
                }

                @Override
                protected int cardCode() {
                    return code;
                }

                @Override
                protected void onClick() {
                    handler.onPick(snapIndex, code);
                    win.hide();
                }
            };
            cardBtn.setRect(startX + col * (CARD_W + CARD_GAP), pos, CARD_W, CARD_H);
            win.add(cardBtn);
        }
        pos += CARD_H + 9;

        if (maxPage > 0) {
            RedButton prev = new RedButton("이전", 6) {
                @Override
                protected void onClick() {
                    win.hide();
                    showPotionPileSelectWindow(titleText, snapshot, currentPage - 1, handler);
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
                    showPotionPileSelectWindow(titleText, snapshot, currentPage + 1, handler);
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

    private String buildRandomPlayLog(ArrayList<DeckPlayResult> results) {
        StringBuilder names = new StringBuilder();
        for (DeckPlayResult r : results) {
            if (!r.isRandomPlay || r.card == null) continue;
            if (names.length() > 0) names.append(", ");
            names.append(r.card.title(r.card.code()));
        }
        if (names.length() == 0) return "";
        return " > " + names + " 시전";
    }

    private String enemyTurnLog(int damage) {
        int slimyCount = 0;
        for (DeckBuilderCombat.EnemyAction action : combat.lastEnemyActions) {
            if (action.shuffledCard == DeckCard.SLIMY) {
                slimyCount += action.shuffledCount;
            }
        }
        int turnEndCardDamage = combat.lastTurnEndStatusDamage;
        int poisonDamage = Math.max(0, turnEndCardDamage - combat.lastTurnEndCurseDamage);
        int enemyDamage = Math.max(0, damage - turnEndCardDamage);
        String text = "";
        if (combat.lastCombatBreathingBlock > 0) {
            text += "전투 호흡: 보호막 " + combat.lastCombatBreathingBlock + "을 얻었습니다. ";
        }
        if (!pendingTurnEndAutoPlayLog.isEmpty()) {
            text += pendingTurnEndAutoPlayLog + " ";
            pendingTurnEndAutoPlayLog = "";
        }
        text += enemyDamage > 0 ? "적들이 총 " + enemyDamage + " 피해를 입혔습니다." : "피해를 막았습니다.";
        if (poisonDamage > 0) {
            text += " 독침 " + combat.lastTurnEndPoisonDarts + "장으로 " + poisonDamage + " 피해를 받았습니다.";
        }
        if (combat.lastTurnEndRegretDamage > 0) {
            text += " 후회로 " + combat.lastTurnEndRegretDamage + " 피해를 받았습니다.";
        }
        int otherCurseDamage = combat.lastTurnEndCurseDamage - combat.lastTurnEndRegretDamage;
        if (otherCurseDamage > 0) {
            text += " 저주로 " + otherCurseDamage + " 피해를 받았습니다.";
        }
        if (slimyCount > 0) {
            text += " 점액투성이 " + slimyCount + "장을 버린 카드 더미에 섞어 넣었습니다.";
        }
        if (combat.lastPriceOfSinDamage > 0) {
            text += " 죄의 대가로 " + combat.lastPriceOfSinDamage + " 피해를 받았습니다.";
        }
        if (combat.lastTurnEndBurnDamage > 0) {
            text += " 화상으로 " + combat.lastTurnEndBurnDamage + " 피해를 받았습니다.";
        }
        if (combat.lastOrangeBombTotalDamage > 0) {
            text += " 오렌지 폭탄 폭발! 총 " + combat.lastOrangeBombTotalDamage + " 피해.";
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
        float damageDelay = spawnUnanimatedDamageEvents(combat.lastAutoPlayResults, 0.24f);
        float delay = Math.max(spawnAutoPlayEffects(), damageDelay);
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

        if (pendingDiscardStartsX.isEmpty() && !pendingPileShuffle && pendingDrawVisuals <= 0)
            return 0f;
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
        float revealCenterX = Camera.main.width / 2f;
        float revealCenterY = autoPlayRevealCenterY();
        float time = 0.1f;
        for (DeckPlayResult auto : autoResults) {
            if (!auto.played || auto.card == null) continue;
            if (auto.isRandomPlay) {
                final DeckCard revealCard = auto.card;
                final float revealDelay = time;
                addEffect(new DelayedActionEffect(revealDelay, new Runnable() {
                    @Override
                    public void run() {
                        addEffect(new CardRevealEffect(revealCard, revealCenterX, revealCenterY));
                    }
                }));
                time += 0.28f; // 카드 이미지가 보인 후 공격 애니메이션 시작
                boolean hasAttackHit = false;
                for (DeckPlayResult.Hit hit : auto.hits) {
                    if (hit.isAttack) {
                        hasAttackHit = true;
                        break;
                    }
                }
                if (hasAttackHit) {
                    for (DeckPlayResult.Hit hit : auto.hits) {
                        if (!hit.isAttack) continue;
                        final DeckCard autoCard = auto.card;
                        final DeckPlayResult.Hit autoHit = hit;
                        final float attackDelay = time;
                        addEffect(new DelayedActionEffect(attackDelay, new Runnable() {
                            @Override
                            public void run() {
                                spawnCardAttack(autoCard, revealCenterX, revealCenterY, autoHit);
                            }
                        }));
                        time += 0.22f;
                    }
                    time += 0.2f;
                } else {
                    time += 0.1f;
                }
            } else {
                for (DeckPlayResult.Hit hit : auto.hits) {
                    if (!hit.isAttack) continue;
                    final DeckCard autoCard = auto.card;
                    final DeckPlayResult.Hit autoHit = hit;
                    final float delay = time;
                    addEffect(new DelayedActionEffect(delay, new Runnable() {
                        @Override
                        public void run() {
                            if (autoCard == DeckCard.ROTATING_NAIL) {
                                Sample.INSTANCE.play(Assets.Sounds.EVOKE);
                            }
                            spawnCardAttack(autoCard, sourceX, sourceY, autoHit);
                        }
                    }));
                    time += 0.16f;
                }
            }
        }
        return time <= 0.1f ? 0f : time + 0.38f;
    }

    private float autoPlayRevealCenterY() {
        float safeTop = targetButton == null ? 48f : Math.max(48f, targetButton.bottom() + 10f);
        float safeBottom = Math.max(safeTop + 8f, handY - CARD_H * 0.75f);
        float preferred = Math.max(Camera.main.height * 0.42f, handY - CARD_H * 1.65f);
        return Math.max(safeTop, Math.min(preferred, safeBottom));
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
                if (hit.damage > 0) spawnFloatingText("-" + hit.damage, enemyCenterX(target), enemyCenterY(target) - 16, 0xFFFF705A);
                target.name.text(target.enemy.name + "  " + capturedHp + "/" + target.enemy.ht);
                target.hp.size(ACTOR_HP_W * capturedHp / (float) target.enemy.ht, ACTOR_HP_H);
                if (!target.enemy.alive() && target.hpBg.visible) {
                    playDeath(target.sprite);
                    hideEnemyUI(target);
                }
            }
        }));
    }

    private float spawnUnanimatedDamageEvents(DeckPlayResult result, float startDelay) {
        ArrayList<DeckPlayResult> animatedResults = new ArrayList<>();
        if (result != null) animatedResults.add(result);
        animatedResults.addAll(combat.lastAutoPlayResults);
        return spawnUnanimatedDamageEvents(animatedResults, startDelay);
    }

    private float spawnUnanimatedDamageEvents(ArrayList<DeckPlayResult> animatedResults, float startDelay) {
        return spawnUnanimatedDamageEvents(animatedResults, startDelay, true);
    }

    private float spawnUnanimatedEnemyDamageEvents(float startDelay) {
        return spawnUnanimatedDamageEvents(new ArrayList<DeckPlayResult>(), startDelay, false);
    }

    private float spawnOrangeBombExplosionEffects(float startDelay) {
        if (combat.lastOrangeBombExplosions <= 0) return 0f;
        float delay = startDelay;
        for (int i = 0; i < combat.lastOrangeBombExplosions; i++) {
            final float capturedDelay = delay;
            addEffect(new DelayedActionEffect(capturedDelay, new Runnable() {
                @Override
                public void run() {
                    Sample.INSTANCE.play(Assets.Sounds.BLAST);
                    syncEnemyHpBarsToCombatState(true);
                    for (EnemyView view : enemyViews) {
                        if (!view.sprite.visible) continue;
                        addEffect(new ImpactEffect(enemyCenterX(view), enemyCenterY(view), 0xFFFF9040));
                    }
                }
            }));
            delay += 0.16f;
        }
        return delay + 0.08f;
    }

    private void syncEnemyHpBarsToCombatState(boolean keepDeadVisible) {
        for (EnemyView view : enemyViews) {
            int hp = Math.max(0, view.enemy.hp);
            view.displayHp = hp;
            view.name.text(view.enemy.name + "  " + hp + "/" + view.enemy.ht);
            view.hp.size(ACTOR_HP_W * hp / (float) view.enemy.ht, ACTOR_HP_H);
            float hpFillWidth = view.hp.width();
            float rawShieldW = view.enemy.block * ACTOR_HP_W / (float) Math.max(1, view.enemy.ht);
            view.shield.size(Math.max(0, Math.min(rawShieldW, ACTOR_HP_W - hpFillWidth)), ACTOR_HP_H);
            if (hp <= 0 && !keepDeadVisible) {
                hideEnemyUI(view);
            }
        }
    }

    private float spawnUnanimatedDamageEvents(ArrayList<DeckPlayResult> animatedResults, float startDelay, boolean includePlayerDamage) {
        if (combat.lastDamageEvents.isEmpty()) return 0f;
        ArrayList<String> animatedAttackHits = new ArrayList<>();
        for (DeckPlayResult animatedResult : animatedResults) {
            if (animatedResult == null) continue;
            for (DeckPlayResult.Hit hit : animatedResult.hits) {
                if (hit.isAttack && hit.damage > 0) {
                    animatedAttackHits.add(hit.enemyIndex + ":" + hit.damage);
                }
            }
        }
        float delay = startDelay;
        boolean spawned = false;
        for (DeckBuilderCombat.DamageEvent event : combat.lastDamageEvents) {
            if (event.damage <= 0) continue;
            if (event.playerTarget() && !includePlayerDamage) continue;
            if (!event.playerTarget()) {
                String key = event.enemyIndex + ":" + event.damage;
                if (animatedAttackHits.remove(key)) continue;
            }
            final DeckBuilderCombat.DamageEvent captured = event;
            final float capturedDelay = delay;
            addEffect(new DelayedActionEffect(capturedDelay, new Runnable() {
                @Override
                public void run() {
                    if (captured.playerTarget()) {
                        spawnPlayerDamageImpact(captured.damage, null);
                    } else {
                        spawnEnemyDamageImpact(captured.enemyIndex, captured.damage, 0xFFFFC05A, null, captured.enemyHpAfter);
                    }
                }
            }));
            delay += 0.12f;
            spawned = true;
        }
        combat.lastDamageEvents.clear();
        return spawned ? delay + 0.22f : 0f;
    }

    private void spawnEnemyDamageImpact(int enemyIndex, int damage, int color, String prefix) {
        spawnEnemyDamageImpact(enemyIndex, damage, color, prefix, -1);
    }

    private void spawnEnemyDamageImpact(int enemyIndex, int damage, int color, String prefix, int hpAfter) {
        EnemyView target = enemyView(enemyIndex);
        if (target == null || damage <= 0) return;
        target.displayHp = hpAfter >= 0 ? Math.max(0, hpAfter) : Math.max(0, target.displayHp - damage);
        int capturedHp = target.displayHp;
        target.hitTime = 0.22f;
        Sample.INSTANCE.play(Assets.Sounds.HIT_SLASH);
        addEffect(new ImpactEffect(enemyCenterX(target), enemyCenterY(target), color));
        spawnFloatingText((prefix == null ? "" : prefix) + "-" + damage, enemyCenterX(target), enemyCenterY(target) - 16, 0xFFFF705A);
        target.name.text(target.enemy.name + "  " + capturedHp + "/" + target.enemy.ht);
        target.hp.size(ACTOR_HP_W * capturedHp / (float) target.enemy.ht, ACTOR_HP_H);
        if (!target.enemy.alive()) {
            playDeath(target.sprite);
            hideEnemyUI(target);
        }
    }

    private void spawnPlayerDamageImpact(int damage, String label) {
        if (damage <= 0) return;
        playerHitTime = 0.22f;
        Sample.INSTANCE.play(Assets.Sounds.HIT);
        addEffect(new SlashEffect(playerCenterX(), playerCenterY(), 0xFFFF5A5A));
        spawnFloatingText((label == null ? "" : label + " ") + "-" + damage, playerCenterX(), playerCenterY() - 18, 0xFFFF705A);
        updatePlayerHpUi();
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
                    if (action.counterDamage > 0) {
                        view.displayHp = Math.max(0, view.displayHp - action.counterDamage);
                        final int capturedHp = view.displayHp;
                        final EnemyView counterView = view;
                        spawnFloatingText("반격 -" + action.counterDamage, enemyCenterX(counterView), enemyCenterY(counterView) - 16, 0xFFFF9040);
                        counterView.name.text(counterView.enemy.name + "  " + capturedHp + "/" + counterView.enemy.ht);
                        counterView.hp.size(ACTOR_HP_W * capturedHp / (float) counterView.enemy.ht, ACTOR_HP_H);
                        if (!counterView.enemy.alive()) {
                            playDeath(counterView.sprite);
                            hideEnemyUI(counterView);
                        }
                    }
                }
            }));
        }
    }

    private void spawnPoisonDartDamageEffect() {
        int poisonDamage = Math.max(0, combat.lastTurnEndStatusDamage - combat.lastTurnEndCurseDamage);
        if (poisonDamage <= 0 && combat.lastTurnEndCurseDamage <= 0) return;
        if (poisonDamage > 0) {
            playerHitTime = 0.22f;
            Sample.INSTANCE.play(Assets.Sounds.HIT_MAGIC, 0.9f, 0.85f);
            addEffect(new PoisonDartEffect(playerCenterX(), playerCenterY()));
            spawnFloatingText("독침 -" + poisonDamage, playerCenterX(), playerCenterY() - 22, 0xFF8CFF5A);
        }
        if (combat.lastTurnEndRegretDamage > 0) {
            spawnPlayerDamageImpact(combat.lastTurnEndRegretDamage, "후회");
        }
        int otherCurseDamage = combat.lastTurnEndCurseDamage - combat.lastTurnEndRegretDamage;
        if (otherCurseDamage > 0) {
            spawnPlayerDamageImpact(otherCurseDamage, "저주");
        }
        updatePlayerHpUi();
    }

	private void spawnPriceOfSinDamageEffect() {
		if (combat.lastPriceOfSinDamage <= 0) return;
		spawnPlayerDamageImpact(combat.lastPriceOfSinDamage, "죄의 대가");
	}

	private void spawnBurnDamageEffect() {
		if (combat.lastTurnEndBurnDamage <= 0) return;
		spawnPlayerDamageImpact(combat.lastTurnEndBurnDamage, "화상");
	}

    private void spawnShieldEffect(float x, float y, String text) {
        playGuard(playerSprite);
        playerGuardTime = 0.34f;
        Sample.INSTANCE.play(Assets.Sounds.HIT_PARRY);
        addEffect(new ShieldEffect(x, y));
        spawnFloatingText(text, x, y - 18, 0xFF8EDBFF);
    }

    private void spawnHealEffect(int amount) {
        final float x = playerCenterX();
        final float y = playerCenterY();
        Sample.INSTANCE.play(Assets.Sounds.CHARMS);
        addEffect(new HealEffect(x, y));
        spawnFloatingText("체력 +" + amount, x, y - 24, 0xFF80FF80);
        updatePlayerHpUi();
    }

    private void spawnEffectExhausts(ArrayList<Integer> handBeforePlay, ArrayList<float[]> positionsBeforePlay, int playedIndex) {
        ArrayList<Integer> remaining = new ArrayList<>(combat.hand);
        for (int i = 0; i < handBeforePlay.size() && i < positionsBeforePlay.size(); i++) {
            if (i == playedIndex) continue;
            int code = handBeforePlay.get(i);
            int stillInHand = remaining.indexOf(code);
            if (stillInHand >= 0) {
                remaining.remove(stillInHand);
                continue;
            }
            final float[] pos = positionsBeforePlay.get(i);
            addEffect(new DelayedActionEffect(0.08f, new Runnable() {
                @Override
                public void run() {
                    addEffect(new ExhaustEffect(pos[0], pos[1]));
                    Sample.INSTANCE.play(Assets.Sounds.BURNING);
                }
            }));
        }
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

    private void showArmamentsHandUpgradeWindow(final int page) {
        final ArrayList<Integer> upgradeable = new ArrayList<>();
        final ArrayList<Integer> upgradeableIndices = new ArrayList<>();
        for (int i = 0; i < combat.hand.size(); i++) {
            int code = combat.hand.get(i);
            if (DeckCardCode.upgrade(code) != code) {
                upgradeable.add(code);
                upgradeableIndices.add(i);
            }
        }
        if (upgradeable.isEmpty()) {
            refresh();
            return;
        }

        final int ARM_CARD_W = 42;
        final int ARM_CARD_H = 54;
        final int ARM_CARD_GAP = 5;
        final int ARM_CARDS_PER_PAGE = 4;
        final int total = upgradeable.size();
        final int maxPage = Math.max(0, (total - 1) / ARM_CARDS_PER_PAGE);
        final int currentPage = Math.max(0, Math.min(page, maxPage));
        final int first = currentPage * ARM_CARDS_PER_PAGE;
        final int count = Math.min(ARM_CARDS_PER_PAGE, total - first);
        final int totalCardW = count * ARM_CARD_W + (count - 1) * ARM_CARD_GAP;
        final int width = Math.max(196, totalCardW + 20);

        final Window win = new Window() {
            @Override
            public void onBackPressed() {
            }
        };

        int pos = 7;
        RenderedTextBlock title = renderTextBlock("강화할 카드를 선택하세요.", 8);
        title.hardlight(Window.TITLE_COLOR);
        title.maxWidth(width - 14);
        title.setPos((width - title.width()) / 2f, pos);
        win.add(title);
        pos += (int) title.height() + 8;

        final int startX = (width - totalCardW) / 2;
        for (int i = 0; i < count; i++) {
            final int snapIndex = first + i;
            final int code = upgradeable.get(snapIndex);
            final int handIdx = upgradeableIndices.get(snapIndex);
            final int col = i;
            CardViewButton btn = new CardViewButton() {
                @Override
                protected DeckCard card() {
                    return DeckCard.byCode(code);
                }

                @Override
                protected int cardCode() {
                    return code;
                }

                @Override
                protected void onClick() {
                    combat.hand.set(handIdx, DeckCardCode.upgrade(code));
                    saveCombatState();
                    win.hide();
                    refresh();
                }
            };
            btn.setRect(startX + col * (ARM_CARD_W + ARM_CARD_GAP), pos, ARM_CARD_W, ARM_CARD_H);
            win.add(btn);
        }
        pos += ARM_CARD_H + 9;

        if (maxPage > 0) {
            RedButton prev = new RedButton("이전", 6) {
                @Override
                protected void onClick() {
                    win.hide();
                    showArmamentsHandUpgradeWindow(currentPage - 1);
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
                    showArmamentsHandUpgradeWindow(currentPage + 1);
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

    private void showDualWieldHandSelectWindow(final int copies, final int page) {
        final ArrayList<Integer> selectable = new ArrayList<>();
        for (int code : combat.hand) {
            DeckCardType t = DeckCard.byCode(code).type;
            if (t == DeckCardType.ATTACK || t == DeckCardType.POWER) {
                selectable.add(code);
            }
        }
        if (selectable.isEmpty()) {
            combat.pendingHandCopySelectCopies = 0;
            saveCombatState();
            refresh();
            return;
        }

        final int DW_CARD_W = 42;
        final int DW_CARD_H = 54;
        final int DW_CARD_GAP = 5;
        final int DW_CARDS_PER_PAGE = 4;
        final int total = selectable.size();
        final int maxPage = Math.max(0, (total - 1) / DW_CARDS_PER_PAGE);
        final int currentPage = Math.max(0, Math.min(page, maxPage));
        final int first = currentPage * DW_CARDS_PER_PAGE;
        final int count = Math.min(DW_CARDS_PER_PAGE, total - first);
        final int totalCardW = count * DW_CARD_W + (count - 1) * DW_CARD_GAP;
        final int width = Math.max(196, totalCardW + 20);

        final Window win = new Window() {
            @Override
            public void onBackPressed() {
            }
        };

        int pos = 7;
        RenderedTextBlock title = renderTextBlock("복사할 카드를 선택하세요.", 8);
        title.hardlight(Window.TITLE_COLOR);
        title.maxWidth(width - 14);
        title.setPos((width - title.width()) / 2f, pos);
        win.add(title);
        pos += (int) title.height() + 8;

        final int startX = (width - totalCardW) / 2;
        for (int i = 0; i < count; i++) {
            final int snapIndex = first + i;
            final int code = selectable.get(snapIndex);
            final int col = i;
            CardViewButton btn = new CardViewButton() {
                @Override
                protected DeckCard card() {
                    return DeckCard.byCode(code);
                }

                @Override
                protected int cardCode() {
                    return code;
                }

                @Override
                protected void onClick() {
                    combat.pendingHandCopySelectCopies = 0;
                    for (int c = 0; c < copies; c++) {
                        combat.addToHand(code);
                    }
                    saveCombatState();
                    win.hide();
                    refresh();
                }
            };
            btn.setRect(startX + col * (DW_CARD_W + DW_CARD_GAP), pos, DW_CARD_W, DW_CARD_H);
            win.add(btn);
        }
        pos += DW_CARD_H + 9;

        if (maxPage > 0) {
            RedButton prev = new RedButton("이전", 6) {
                @Override
                protected void onClick() {
                    win.hide();
                    showDualWieldHandSelectWindow(copies, currentPage - 1);
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
                    showDualWieldHandSelectWindow(copies, currentPage + 1);
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

    private void showForesightHandSelectWindow(final int page) {
        if (combat.hand.isEmpty()) {
            refresh();
            return;
        }

        final int FS_CARD_W = 42;
        final int FS_CARD_H = 54;
        final int FS_CARD_GAP = 5;
        final int FS_CARDS_PER_PAGE = 4;
        final int total = combat.hand.size();
        final int maxPage = Math.max(0, (total - 1) / FS_CARDS_PER_PAGE);
        final int currentPage = Math.max(0, Math.min(page, maxPage));
        final int first = currentPage * FS_CARDS_PER_PAGE;
        final int count = Math.min(FS_CARDS_PER_PAGE, total - first);
        final int totalCardW = count * FS_CARD_W + (count - 1) * FS_CARD_GAP;
        final int width = Math.max(196, totalCardW + 20);

        final Window win = new Window() {
            @Override
            public void onBackPressed() {
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
            final int handIdx = first + i;
            final int code = combat.hand.get(handIdx);
            final int col = i;
            CardViewButton btn = new CardViewButton() {
                @Override
                protected DeckCard card() {
                    return DeckCard.byCode(code);
                }

                @Override
                protected int cardCode() {
                    return code;
                }

                @Override
                protected void onClick() {
                    combat.hand.remove(handIdx);
                    combat.drawPile.add(0, code);
                    saveCombatState();
                    win.hide();
                    refresh();
                }
            };
            btn.setRect(startX + col * (FS_CARD_W + FS_CARD_GAP), pos, FS_CARD_W, FS_CARD_H);
            win.add(btn);
        }
        pos += FS_CARD_H + 9;

        if (maxPage > 0) {
            RedButton prev = new RedButton("이전", 6) {
                @Override
                protected void onClick() {
                    win.hide();
                    showForesightHandSelectWindow(currentPage - 1);
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
                    showForesightHandSelectWindow(currentPage + 1);
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

    private void showShittimBoxCardDetailWindow(final Window selectWin, final DeckCard card) {
        final Window win = new DeckRewardWindow();
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
        pos += (int) desc.height() + 8;

        RedButton take = new RedButton("가져오기", 6) {
            @Override
            protected void onClick() {
                Sample.INSTANCE.play(Assets.Sounds.ITEM);
                combat.addToHand(card.code());
                saveCombatState();
                win.hide();
                selectWin.hide();
                refresh();
            }
        };
        take.setRect(7, pos, 74, 18);
        win.add(take);

        RedButton back = new RedButton("돌아가기", 6) {
            @Override
            protected void onClick() {
                win.hide();
            }
        };
        back.setRect(width - 81, pos, 74, 18);
        win.add(back);
        pos += 24;

        win.resize(width, pos);
        addToFront(win);
    }

    private void showShittimBoxSelectWindow(final String filter, final int page) {
        final DeckCard[] all = DeckCard.values();
        final ArrayList<DeckCard> filtered = new ArrayList<>();
        for (DeckCard c : all) {
            if (filter.isEmpty() || c.title(c.code()).contains(filter)) {
                filtered.add(c);
            }
        }

        final int SB_CARD_W = 42;
        final int SB_CARD_H = 54;
        final int SB_CARD_GAP = 5;
        final int SB_CARDS_PER_PAGE = 4;
        final int total = filtered.size();
        final int maxPage = Math.max(0, (total - 1) / SB_CARDS_PER_PAGE);
        final int currentPage = Math.max(0, Math.min(page, maxPage));
        final int first = currentPage * SB_CARDS_PER_PAGE;
        final int count = Math.min(SB_CARDS_PER_PAGE, total - first);
        final int totalCardW = count * SB_CARD_W + (count - 1) * SB_CARD_GAP;
        final int width = Math.max(220, totalCardW + 20);

        final Window win = new Window() {
            @Override
            public void onBackPressed() {
            }
        };

        int pos = 7;
        RenderedTextBlock title = renderTextBlock("[싯딤의 상자] 손에 추가할 카드 선택", 8);
        title.hardlight(Window.TITLE_COLOR);
        title.maxWidth(width - 14);
        title.setPos((width - title.width()) / 2f, pos);
        win.add(title);
        pos += (int) title.height() + 6;

        final int startX = (width - totalCardW) / 2;
        for (int i = 0; i < count; i++) {
            final DeckCard picked = filtered.get(first + i);
            final int col = i;
            CardViewButton btn = new CardViewButton() {
                @Override
                protected DeckCard card() {
                    return picked;
                }

                @Override
                protected int cardCode() {
                    return picked.code();
                }

                @Override
                protected void onClick() {
                    showShittimBoxCardDetailWindow(win, picked);
                }
            };
            btn.setRect(startX + col * (SB_CARD_W + SB_CARD_GAP), pos, SB_CARD_W, SB_CARD_H);
            win.add(btn);
        }
        pos += SB_CARD_H + 6;

        RenderedTextBlock pageText = renderTextBlock((currentPage + 1) + " / " + (maxPage + 1) + "  (전체 " + total + "장)", 6);
        pageText.hardlight(0xFFD8D1BD);
        pageText.setPos((width - pageText.width()) / 2f, pos);
        win.add(pageText);
        pos += (int) pageText.height() + 4;

        RedButton prev = new RedButton("이전", 6) {
            @Override
            protected void onClick() {
                win.hide();
                showShittimBoxSelectWindow(filter, currentPage - 1);
            }
        };
        prev.enable(currentPage > 0);
        prev.setRect(10, pos, 58, 18);
        win.add(prev);

        RedButton next = new RedButton("다음", 6) {
            @Override
            protected void onClick() {
                win.hide();
                showShittimBoxSelectWindow(filter, currentPage + 1);
            }
        };
        next.enable(currentPage < maxPage);
        next.setRect(width - 68, pos, 58, 18);
        win.add(next);
        pos += 23;

        win.resize(width, pos + 4);
        addToFront(win);
    }

    private void showRelicSelectionBoxWindow(final String filter, final int page) {
        final DeckRelic[] all = DeckRelic.values();
        final ArrayList<DeckRelic> filtered = new ArrayList<>();
        for (DeckRelic relic : all) {
            if (filter.isEmpty() || relic.titleWithRarity().contains(filter) || relic.description.contains(filter)) {
                filtered.add(relic);
            }
        }

        final int ROW_H = 24;
        final int ROW_GAP = 3;
        final int ROWS_PER_PAGE = 7;
        final int total = filtered.size();
        final int maxPage = Math.max(0, (total - 1) / ROWS_PER_PAGE);
        final int currentPage = Math.max(0, Math.min(page, maxPage));
        final int first = currentPage * ROWS_PER_PAGE;
        final int count = Math.min(ROWS_PER_PAGE, total - first);
        final int width = 230;

        final Window win = new Window() {
            @Override
            public void onBackPressed() {
            }
        };

        int pos = 7;
        RenderedTextBlock title = renderTextBlock("[유물 선택 상자] 획득할 유물 선택", 8);
        title.hardlight(Window.TITLE_COLOR);
        title.maxWidth(width - 14);
        title.setPos((width - title.width()) / 2f, pos);
        win.add(title);
        pos += (int) title.height() + 6;

        for (int i = 0; i < count; i++) {
            final DeckRelic picked = filtered.get(first + i);
            DeckRewardRow row = new DeckRewardRow(picked.icon, picked.titleWithRarity()) {
                @Override
                protected void onClick() {
                    DeckBuilderRun.addRelic(picked);
                    Sample.INSTANCE.play(Assets.Sounds.ITEM);
                    saveCombatState();
                    if (runHud != null) runHud.refresh();
                    win.hide();
                    refresh();
                }
            };
            row.setRect(10, pos, width - 20, ROW_H);
            win.add(row);
            pos += ROW_H + ROW_GAP;
        }

        RenderedTextBlock pageText = renderTextBlock((currentPage + 1) + " / " + (maxPage + 1) + "  (전체 " + total + "개)", 6);
        pageText.hardlight(0xFFD8D1BD);
        pageText.setPos((width - pageText.width()) / 2f, pos);
        win.add(pageText);
        pos += (int) pageText.height() + 4;

        RedButton prev = new RedButton("이전", 6) {
            @Override
            protected void onClick() {
                win.hide();
                showRelicSelectionBoxWindow(filter, currentPage - 1);
            }
        };
        prev.enable(currentPage > 0);
        prev.setRect(10, pos, 58, 18);
        win.add(prev);

        RedButton next = new RedButton("다음", 6) {
            @Override
            protected void onClick() {
                win.hide();
                showRelicSelectionBoxWindow(filter, currentPage + 1);
            }
        };
        next.enable(currentPage < maxPage);
        next.setRect(width - 68, pos, 58, 18);
        win.add(next);
        pos += 23;

        win.resize(width, pos + 4);
        addToFront(win);
    }

    private void showPotionSelectionBoxWindow(final int page) {
        final DeckPotion[] all = DeckPotion.values();
        final int ROW_H = 24;
        final int ROW_GAP = 3;
        final int ROWS_PER_PAGE = 7;
        final int total = all.length;
        final int maxPage = Math.max(0, (total - 1) / ROWS_PER_PAGE);
        final int currentPage = Math.max(0, Math.min(page, maxPage));
        final int first = currentPage * ROWS_PER_PAGE;
        final int count = Math.min(ROWS_PER_PAGE, total - first);
        final int width = 230;

        final Window win = new Window() {
            @Override
            public void onBackPressed() {
            }
        };

        int pos = 7;
        RenderedTextBlock title = renderTextBlock("[물약 선택 상자] 획득할 물약 선택", 8);
        title.hardlight(Window.TITLE_COLOR);
        title.maxWidth(width - 14);
        title.setPos((width - title.width()) / 2f, pos);
        win.add(title);
        pos += (int) title.height() + 6;

        for (int i = 0; i < count; i++) {
            final DeckPotion picked = all[first + i];
            DeckRewardRow row = new DeckRewardRow(picked.image, picked.title) {
                @Override
                protected void onClick() {
                    if (!DeckBuilderRun.addPotion(picked)) {
                        win.hide();
                        DeckBattleScene.this.addToFront(new WndMessage("포션\n\n빈 포션 슬롯이 없습니다."));
                        refresh();
                        return;
                    }
                    Sample.INSTANCE.play(Assets.Sounds.ITEM);
                    saveCombatState();
                    if (runHud != null) runHud.refresh();
                    win.hide();
                    refresh();
                }
            };
            row.setRect(10, pos, width - 20, ROW_H);
            win.add(row);
            pos += ROW_H + ROW_GAP;
        }

        RenderedTextBlock pageText = renderTextBlock((currentPage + 1) + " / " + (maxPage + 1) + "  (전체 " + total + "개)", 6);
        pageText.hardlight(0xFFD8D1BD);
        pageText.setPos((width - pageText.width()) / 2f, pos);
        win.add(pageText);
        pos += (int) pageText.height() + 4;

        RedButton prev = new RedButton("이전", 6) {
            @Override
            protected void onClick() {
                win.hide();
                showPotionSelectionBoxWindow(currentPage - 1);
            }
        };
        prev.enable(currentPage > 0);
        prev.setRect(10, pos, 58, 18);
        win.add(prev);

        RedButton next = new RedButton("다음", 6) {
            @Override
            protected void onClick() {
                win.hide();
                showPotionSelectionBoxWindow(currentPage + 1);
            }
        };
        next.enable(currentPage < maxPage);
        next.setRect(width - 68, pos, 58, 18);
        win.add(next);
        pos += 23;

        win.resize(width, pos + 4);
        addToFront(win);
    }

    private void showScoutStrikeSelectWindow() {
        if (combat.drawPile.isEmpty()) {
            refresh();
            return;
        }

        final int count = Math.min(3, combat.drawPile.size());
        final int[] topCodes = new int[count];
        for (int i = 0; i < count; i++) {
            topCodes[i] = combat.drawPile.get(i);
        }

        final int SS_CARD_W = 42;
        final int SS_CARD_H = 54;
        final int SS_CARD_GAP = 5;
        final int totalCardW = count * SS_CARD_W + (count - 1) * SS_CARD_GAP;
        final int width = Math.max(196, totalCardW + 20);

        final Window win = new Window() {
            @Override
            public void onBackPressed() {
            }
        };

        int pos = 7;
        RenderedTextBlock title = renderTextBlock("손으로 가져올 카드 선택", 8);
        title.hardlight(Window.TITLE_COLOR);
        title.maxWidth(width - 14);
        title.setPos((width - title.width()) / 2f, pos);
        win.add(title);
        pos += (int) title.height() + 8;

        final int startX = (width - totalCardW) / 2;
        for (int i = 0; i < count; i++) {
            final int pickedCode = topCodes[i];
            final int drawIdx = i;
            final int col = i;
            CardViewButton btn = new CardViewButton() {
                @Override
                protected DeckCard card() {
                    return DeckCard.byCode(pickedCode);
                }

                @Override
                protected int cardCode() {
                    return pickedCode;
                }

                @Override
                protected void onClick() {
                    win.hide();
                    combat.drawPile.remove(drawIdx);
                    combat.addToHand(pickedCode);
                    saveCombatState();
                    refresh();
                }
            };
            btn.setRect(startX + col * (SS_CARD_W + SS_CARD_GAP), pos, SS_CARD_W, SS_CARD_H);
            win.add(btn);
        }
        pos += SS_CARD_H + 9;

        win.resize(width, pos + 4);
        addToFront(win);
    }

    private void showDrawPileTypeSelectWindow(final DeckCardType type, final int page) {
        final ArrayList<Integer> drawIndexes = new ArrayList<>();
        for (int i = 0; i < combat.drawPile.size(); i++) {
            if (DeckCard.byCode(combat.drawPile.get(i)).type == type) {
                drawIndexes.add(i);
            }
        }
        if (drawIndexes.isEmpty()) {
            refresh();
            return;
        }

        final int DP_CARD_W = 42;
        final int DP_CARD_H = 54;
        final int DP_CARD_GAP = 5;
        final int DP_CARDS_PER_PAGE = 4;
        final int maxPage = Math.max(0, (drawIndexes.size() - 1) / DP_CARDS_PER_PAGE);
        final int currentPage = Math.max(0, Math.min(page, maxPage));
        final int first = currentPage * DP_CARDS_PER_PAGE;
        final int count = Math.min(DP_CARDS_PER_PAGE, drawIndexes.size() - first);
        final int totalCardW = count * DP_CARD_W + (count - 1) * DP_CARD_GAP;
        final int width = Math.max(216, totalCardW + 20);

        final Window win = new Window() {
            @Override
            public void onBackPressed() {
            }
        };

        int pos = 7;
        RenderedTextBlock title = renderTextBlock(typeLabel(type) + " 카드 선택", 8);
        title.hardlight(Window.TITLE_COLOR);
        title.maxWidth(width - 14);
        title.setPos((width - title.width()) / 2f, pos);
        win.add(title);
        pos += (int) title.height() + 8;

        final int startX = (width - totalCardW) / 2;
        for (int i = 0; i < count; i++) {
            final int drawIdx = drawIndexes.get(first + i);
            final int pickedCode = combat.drawPile.get(drawIdx);
            final int col = i;
            CardViewButton btn = new CardViewButton() {
                @Override
                protected DeckCard card() {
                    return DeckCard.byCode(pickedCode);
                }

                @Override
                protected int cardCode() {
                    return pickedCode;
                }

                @Override
                protected void onClick() {
                    win.hide();
                    if (drawIdx >= 0 && drawIdx < combat.drawPile.size() && combat.drawPile.get(drawIdx) == pickedCode) {
                        combat.drawPile.remove(drawIdx);
                    } else {
                        combat.drawPile.remove((Integer) pickedCode);
                    }
                    combat.addToHand(pickedCode);
                    saveCombatState();
                    refresh();
                }
            };
            btn.setRect(startX + col * (DP_CARD_W + DP_CARD_GAP), pos, DP_CARD_W, DP_CARD_H);
            win.add(btn);
        }
        pos += DP_CARD_H + 8;

        RedButton prev = new RedButton("이전", 6) {
            @Override
            protected void onClick() {
                win.hide();
                showDrawPileTypeSelectWindow(type, currentPage - 1);
            }
        };
        prev.enable(currentPage > 0);
        prev.setRect(10, pos, 58, 18);
        win.add(prev);

        RedButton next = new RedButton("다음", 6) {
            @Override
            protected void onClick() {
                win.hide();
                showDrawPileTypeSelectWindow(type, currentPage + 1);
            }
        };
        next.enable(currentPage < maxPage);
        next.setRect(width - 68, pos, 58, 18);
        win.add(next);
        pos += 23;

        win.resize(width, pos + 4);
        addToFront(win);
    }

    private String typeLabel(DeckCardType type) {
        if (type == DeckCardType.ATTACK) return "공격";
        if (type == DeckCardType.SKILL) return "보조";
        if (type == DeckCardType.POWER) return "지속";
        return type.label;
    }

    private void showStratagemSelectWindow() {
        if (combat.drawPile.isEmpty()) {
            refresh();
            return;
        }

        final int count = Math.min(3, combat.drawPile.size());
        final int[] topCodes = new int[count];
        for (int i = 0; i < count; i++) {
            topCodes[i] = combat.drawPile.get(i);
        }

        final int ST_CARD_W = 42;
        final int ST_CARD_H = 54;
        final int ST_CARD_GAP = 5;
        final int totalCardW = count * ST_CARD_W + (count - 1) * ST_CARD_GAP;
        final int width = Math.max(196, totalCardW + 20);

        final Window win = new Window() {
            @Override
            public void onBackPressed() {
            }
        };

        int pos = 7;
        RenderedTextBlock title = renderTextBlock("책략: 덱 리셔플 - 카드 1장 선택", 8);
        title.hardlight(Window.TITLE_COLOR);
        title.maxWidth(width - 14);
        title.setPos((width - title.width()) / 2f, pos);
        win.add(title);
        pos += (int) title.height() + 8;

        final int startX = (width - totalCardW) / 2;
        for (int i = 0; i < count; i++) {
            final int pickedCode = topCodes[i];
            final int drawIdx = i;
            final int col = i;
            CardViewButton btn = new CardViewButton() {
                @Override
                protected DeckCard card() {
                    return DeckCard.byCode(pickedCode);
                }

                @Override
                protected int cardCode() {
                    return pickedCode;
                }

                @Override
                protected void onClick() {
                    win.hide();
                    combat.drawPile.remove(drawIdx);
                    combat.addToHand(pickedCode);
                    saveCombatState();
                    refresh();
                }
            };
            btn.setRect(startX + col * (ST_CARD_W + ST_CARD_GAP), pos, ST_CARD_W, ST_CARD_H);
            win.add(btn);
        }
        pos += ST_CARD_H + 9;

        win.resize(width, pos + 4);
        addToFront(win);
    }

    private void showHeadbuttDiscardSelectWindow(final ArrayList<Integer> snapshot, final int page) {
        final int total = snapshot.size();
        if (total == 0) {
            refresh();
            return;
        }

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
                @Override
                protected DeckCard card() {
                    return DeckCard.byCode(code);
                }

                @Override
                protected int cardCode() {
                    return code;
                }

                @Override
                protected void onClick() {
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
                @Override
                protected void onClick() {
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
                @Override
                protected void onClick() {
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

    private void showDiscardToHandSelectWindow(final int remaining, final int page) {
        if (remaining <= 0 || combat.discardPile.isEmpty()) {
            combat.pendingDiscardHandSelectCount = 0;
            saveCombatState();
            if (combat.won()) showReward();
            else refresh();
            return;
        }

        final int total = combat.discardPile.size();
        final int DH_CARD_W = 42;
        final int DH_CARD_H = 54;
        final int DH_CARD_GAP = 5;
        final int DH_CARDS_PER_PAGE = 4;

        final int maxPage = Math.max(0, (total - 1) / DH_CARDS_PER_PAGE);
        final int currentPage = Math.max(0, Math.min(page, maxPage));
        final int first = currentPage * DH_CARDS_PER_PAGE;
        final int count = Math.min(DH_CARDS_PER_PAGE, total - first);
        final int totalCardW = count * DH_CARD_W + (count - 1) * DH_CARD_GAP;
        final int width = Math.max(216, totalCardW + 20);

        final Window win = new Window() {
            @Override
            public void onBackPressed() {
            }
        };

        int pos = 7;
        RenderedTextBlock title = renderTextBlock("손으로 가져올 카드 선택 (" + remaining + "장)", 8);
        title.hardlight(Window.TITLE_COLOR);
        title.maxWidth(width - 14);
        title.setPos((width - title.width()) / 2f, pos);
        win.add(title);
        pos += (int) title.height() + 8;

        final int startX = (width - totalCardW) / 2;
        for (int i = 0; i < count; i++) {
            final int discardIndex = first + i;
            final int code = combat.discardPile.get(discardIndex);
            final int col = i;
            CardViewButton cardBtn = new CardViewButton() {
                @Override
                protected DeckCard card() {
                    return DeckCard.byCode(code);
                }

                @Override
                protected int cardCode() {
                    return code;
                }

                @Override
                protected void onClick() {
                    win.hide();
                    if (discardIndex >= 0 && discardIndex < combat.discardPile.size() && combat.discardPile.get(discardIndex) == code) {
                        combat.discardPile.remove(discardIndex);
                    } else {
                        combat.discardPile.remove((Integer) code);
                    }
                    combat.addToHand(code);
                    combat.pendingDiscardHandSelectCount = Math.max(0, remaining - 1);
                    saveCombatState();
                    showDiscardToHandSelectWindow(combat.pendingDiscardHandSelectCount, 0);
                }
            };
            cardBtn.setRect(startX + col * (DH_CARD_W + DH_CARD_GAP), pos, DH_CARD_W, DH_CARD_H);
            win.add(cardBtn);
        }
        pos += DH_CARD_H + 8;

        if (maxPage > 0) {
            RedButton prev = new RedButton("이전", 6) {
                @Override
                protected void onClick() {
                    win.hide();
                    showDiscardToHandSelectWindow(remaining, currentPage - 1);
                }
            };
            prev.enable(currentPage > 0);
            prev.setRect(10, pos, 58, 18);
            win.add(prev);

            RedButton next = new RedButton("다음", 6) {
                @Override
                protected void onClick() {
                    win.hide();
                    showDiscardToHandSelectWindow(remaining, currentPage + 1);
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

    private void showDrawPileWindow() {
        final Window win = new Window();
        int width = 180;
        int pos = 7;

        RenderedTextBlock drawTitle = renderTextBlock("남은 카드 목록", 9);
        drawTitle.hardlight(Window.TITLE_COLOR);
        drawTitle.setPos((width - drawTitle.width()) / 2f, pos);
        win.add(drawTitle);
        pos += 15;

        // 스크롤 가능한 콘텐츠 구성
        Component content = new Component();
        float contentPos = 0;

        contentPos = addPileToContent(content, combat.drawPile, width, contentPos);
        contentPos += 8;

        if (!combat.powersPlayed.isEmpty()) {
            ColorBlock divider = new ColorBlock(width - 10, 1, 0xFF555555);
            divider.x = 5;
            divider.y = contentPos;
            content.add(divider);
            contentPos += 6;

            RenderedTextBlock powerTitle = renderTextBlock("사용한 지속 카드", 9);
            powerTitle.hardlight(0xFFFFD66B);
            powerTitle.setPos((width - powerTitle.width()) / 2f, contentPos);
            content.add(powerTitle);
            contentPos += 15;

            contentPos = addPileToContent(content, combat.powersPlayed, width, contentPos);
            contentPos += 8;
        }

        content.setSize(width - 2, contentPos);

        int scrollTop = pos;
        int maxScrollH = Math.min(150, Camera.main.height - 80);
        int scrollH = (int) Math.min(maxScrollH, contentPos);
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
        win.resize(width, pos);
        scrollPane.setRect(0, scrollTop, width, scrollH);
        addToFront(win);
    }

    private void showDiscardWindow() {
        final Window win = new Window();
        int width = 180;
        int pos = 7;

        // 버린 카드 섹션 제목
        RenderedTextBlock discardTitle = renderTextBlock("버린 카드 목록", 9);
        discardTitle.hardlight(Window.TITLE_COLOR);
        discardTitle.setPos((width - discardTitle.width()) / 2f, pos);
        win.add(discardTitle);
        pos += 15;

        // 스크롤 가능한 콘텐츠 구성
        Component content = new Component();
        float contentPos = 0;

        contentPos = addPileToContent(content, combat.discardPile, width, contentPos);
        contentPos += 8;

        // 구분선
        ColorBlock divider = new ColorBlock(width - 10, 1, 0xFF555555);
        divider.x = 5;
        divider.y = contentPos;
        content.add(divider);
        contentPos += 6;

        // 소멸 카드 섹션
        RenderedTextBlock exhaustTitle = renderTextBlock("소멸된 카드 목록", 9);
        exhaustTitle.hardlight(0xFFFF8888);
        exhaustTitle.setPos((width - exhaustTitle.width()) / 2f, contentPos);
        content.add(exhaustTitle);
        contentPos += 15;

        contentPos = addPileToContent(content, combat.exhaustPile, width, contentPos);
        contentPos += 8;

        content.setSize(width - 2, contentPos);

        int scrollTop = pos;
        int maxScrollH = Math.min(150, Camera.main.height - 80);
        int scrollH = (int) Math.min(maxScrollH, contentPos);
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
        win.resize(width, pos);
        scrollPane.setRect(0, scrollTop, width, scrollH);
        addToFront(win);
    }

    private float addPileToContent(Component content, ArrayList<Integer> pile, int width, float pos) {
        if (pile.isEmpty()) {
            RenderedTextBlock empty = renderTextBlock("(없음)", 6);
            empty.hardlight(0xFF888888);
            empty.setPos((width - empty.width()) / 2f, pos);
            content.add(empty);
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
                content.add(cardLine);
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
        hideTutorialHighlight();
        hideCardInfo();
        endTurn.visible = false;
        targetButton.visible = false;
        for (CardButton button : cardButtons) {
            button.visible = false;
        }
        refreshEnemyViews();
        // 소멸은 이 전투에서만 카드를 제거하는 것. 다음 전투에서는 다시 사용 가능하므로 영구 삭제하지 않음.
        combat.exhaustPile.clear();
        for (int i = DeckBuilderRun.deck.size() - 1; i >= 0; i--) {
            if (DeckCardPool.isStatus(DeckCard.byCode(DeckBuilderRun.deck.get(i)))) {
                DeckBuilderRun.deck.remove(i);
            }
        }
        for (int i = DeckBuilderRun.deck.size() - 1; i >= 0; i--) {
            int code = DeckBuilderRun.deck.get(i);
            if (DeckCard.byCode(code) == DeckCard.GUILT) {
                int newCode = DeckCardCode.upgrade(code);
                if (DeckCard.upgradeLevel(newCode) >= 5) {
                    DeckBuilderRun.deck.remove(i);
                } else {
                    DeckBuilderRun.deck.set(i, newCode);
                }
            }
        }
        if (DeckBuilderRun.hasRelic(DeckRelic.MEAT_ON_THE_BONE) && DeckBuilderRun.playerHP * 2 <= DeckBuilderRun.playerHT) {
            DeckBuilderRun.playerHP = Math.min(DeckBuilderRun.playerHT, DeckBuilderRun.playerHP + 12);
        }
        combat.applyCombatRewardBonuses();
        saveCombatState();
        Sample.INSTANCE.play(Assets.Sounds.LEVELUP);
        boolean useFullRewardScreen = true;
        if (useFullRewardScreen) {
            DeckCombatRewardState rewards = DeckBuilderRun.combatRewardForCurrentNode(Statistics.deckBuilderMapNode);
            saveCombatState();
            showCombatRewardWindow(rewards);
            showTutorialRewardPrompt();
            return;
        }
        final DeckCard[] rewards = DeckBuilderRun.rewardChoices();
        final Window reward = new DeckRewardWindow();
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

    private void showTutorialTurnPrompt() {
        if (!DeckBuilderRun.tutorialMode) return;
        int step = DeckBuilderRun.tutorialStep;
        if (step == 0) {
            // 1단계: 공격(ATTACK) 카드 소개
            showTutorialMessage(
                    "카드 타입 — 공격 카드\n\n" +
                            "먼저 공격 카드 설명부터 들어둬!\n\n" +
                            "공격 카드는 적에게 직접 피해를 가하는 카드다! 카드 상단의 숫자가 소모 에너지니까 잘 봐두라고!\n\n" +
                            "자, 공격 카드를 내서 한 방 먹여봐!!");
        } else if (step >= 3 && step < 8 && combat.turn >= 2) {
            // 2턴~, 세 타입 모두 사용 완료 후 물약 튜토리얼
            DeckBuilderRun.tutorialStep = 8;
            showTutorialMessage(
                    "물약 안내\n\n" +
                            "화면 상단에 물약 슬롯이 보이지?!\n\n" +
                            "물약은 전투 중 언제든지 쓸 수 있는 일회성 아이템이야!" +
                            "지금 갖고 있는 '화염 물약'은 모든 적에게 피해를 10이나 준다고!\n\n" +
                            "위기의 순간, 아니면 결정적인 한 방이 필요할 때 전략적으로 쓰는거야. 잊지 마!");
        }
    }

    private boolean tutorialMessageOpen() {
        return tutorialMessageLocks > 0;
    }

    private void showTutorialMessage(String text) {
        tutorialMessageLocks++;
        combatLocked = true;
        ShatteredPixelDungeon.scene().addToFront(new WndMessage(text) {
            @Override
            public void hide() {
                super.hide();
                tutorialMessageLocks = Math.max(0, tutorialMessageLocks - 1);
                if (!tutorialMessageOpen() && !rewardOpen) {
                    combatLocked = false;
                    refresh();
                }
            }
        });
    }

    private boolean tutorialAllowsCard(DeckCard card, int cardCode) {
        if (!DeckBuilderRun.tutorialMode) return true;
        int step = DeckBuilderRun.tutorialStep;
        if (step == 0 && card.type != DeckCardType.ATTACK) {
            showTutorialMessage(
                    "지금은 공격 카드를 사용할 차례야.\n\n" +
                            "손에서 공격 타입 카드를 골라서 내봐!");
            return false;
        }
        if (step == 1 && card.type != DeckCardType.SKILL) {
            showTutorialMessage(
                    "이제 보조 카드를 사용할 차례야.\n\n" +
                            "손에서 보조 타입 카드를 골라서 내봐!");
            return false;
        }
        if (step == 2 && card.type != DeckCardType.POWER) {
            showTutorialMessage(
                    "이제 지속 카드를 사용할 차례야.\n\n" +
                            "손에서 지속 타입 카드를 골라서 내봐!");
            return false;
        }
        return true;
    }

    private void advanceTutorialAfterCard(DeckCard card, int cardCode) {
        if (!DeckBuilderRun.tutorialMode) return;
        int step = DeckBuilderRun.tutorialStep;
        if (step == 0 && card.type == DeckCardType.ATTACK) {
            DeckBuilderRun.tutorialStep = 1;
            showTutorialMessage(
                    "잘했어! 공격 카드를 잘 사용했군! 다음은 보조 카드야!\n\n" +
                            "보조 카드는 방어, 드로우.. 다양한 지원 효과를 가진 카드야!" +
                            "체력 회복 수단은 한정적이기 때문에 보조 카드를 통해 적의 피해를 최소화하는 전략이 가장 중요해!\n\n" +
                            "적 밑에 뜨는 숫자도 봐둬. 그게 다음 턴에 받을 피해야. 미리 보고 대비하면 훨씬 편하겠지?\n\n" +
                            "손패에서 보조 카드를 찾아 써봐!");
        } else if (step == 1 && card.type == DeckCardType.SKILL) {
            DeckBuilderRun.tutorialStep = 2;
            showTutorialMessage(
                    "그렇지! 이제 적 공격도 좀 막을 수 있겠네. 다음은 지속 카드야!\n\n" +
                            "지속 카드는 말 그대로 한 번 쓰면 전투가 끝날 때까지 효과가 계속 지속되는 카드야.\n\n" +
                            "그리고 이게 중요해! 한 번 쓴 지속 카드는 버린 카드로 가지 않고, 그 전투에서 깔끔하게 사라진다!\n\n" +
                            "손패에서 지속 카드를 찾아 써봐!");
        } else if (step == 2 && card.type == DeckCardType.POWER) {
            DeckBuilderRun.tutorialStep = 3;
            showTutorialMessage(
                    "세 가지 카드 타입을 전부 써봤어! 대단해 죠스타 씨!\n\n" +
                            "[카드 순환 방식]\n" +
                            "카드가 어떻게 도는지도 알려줄게. 쓰거나 남은 카드는 턴이 끝날 때 전부 버린 카드 더미로 이동하지!" +
                            "뽑을 카드가 바닥나면 버린 카드를 다시 섞어서 남은 카드에 채워주니까 걱정 안 해도 된다는 말씀!\n\n" +
                            "이제 마음껏 카드를 써봐!");
        }
    }

    private boolean enemyIsAttacking() {
        if (combat == null) return false;
        for (DeckCombatEnemy enemy : combat.enemies) {
            if (enemy.alive() && enemy.intent > 0) return true;
        }
        return false;
    }

    private void showTutorialRewardPrompt() {
        if (!DeckBuilderRun.tutorialMode) return; // 이미 종료됐거나 튜토리얼 아님
        // 튜토리얼 전투 종료 > 즉시 tutorialMode 해제
        DeckBuilderRun.tutorialMode = false;
        // addToFront: 마지막에 추가한 창이 가장 위에 표시됨
        // 카드 보상 안내를 먼저 추가 > 전리품 창 바로 위에 위치
        // 카드 보상 안내
        showTutorialMessage(
                        "전투가 끝나면, 카드 보상을 하나 선택해서 덱에 넣을 수 있어!\n\n" +
                        "[카드 고르는 팁]\n" +
                        "여기 나오는 카드 중엔 해당 영웅의 전용 카드도 있고, 아무나 쓰는 공용 카드도 있어." +
                        "그런데 무조건 다 챙기는 게 좋은 건 아니야. 덱이 두꺼워질수록 정작 원하는 카드는 잘 안 나오거든.\n\n" +
                        "그냥 강해 보이는 카드보다, 지금 덱이랑 잘 맞는 카드를 고르는 게 좋아.\n\n" +
                        "카드 하나 고르면 튜토리얼도 여기서 끝이야. 그럼 스피드왜건은 쿨하게 떠나주지!");

        // 물약 안내를 2턴에 보여주지 못했다면 여기서 보충
        if (DeckBuilderRun.tutorialStep < 8) {
            showTutorialMessage(
                    "참, 물약 얘기를 빼먹었네.\n\n" +
                            "화면 위쪽에 물약 슬롯 보이지?\n\n" +
                            "물약은 전투 중 언제든 쓸 수 있는 유용한 일회성 아이템이야!" +
                            "지금 가지고 있는 '화염 물약'은 모든 적에게 피해를 10이나 줄 수 있는거 같은데?\n\n" +
                            "위기에 처했을 때나 결정적인 순간에 전략적으로 물약을 써먹으라고!");
        }
    }

    // ─── Tutorial highlight helpers ───────────────────────────────────────────

    private void refreshTutorialHighlight() {
        if (!DeckBuilderRun.tutorialMode || tutBorderTop == null) {
            hideTutorialHighlight();
            return;
        }
        int step = DeckBuilderRun.tutorialStep;
        // step 0>1>2 단계 안내 중에만 손패 영역 하이라이트
        if (step < 3) {
            int sw = Camera.main.width;
            RectF insets = getCommonInsets();
            float handLeft = insets.left + counterW + 4;
            float handRight = sw - insets.right - counterW - 4;
            setTutorialHighlight(handLeft, handY, handRight - handLeft, CARD_H);
        } else {
            hideTutorialHighlight();
        }
    }

    private void setTutorialHighlight(float x, float y, float w, float h) {
        int pad = 3;
        tutBorderTop.x = x - pad;
        tutBorderTop.y = y - pad;
        tutBorderTop.size(w + pad * 2, 2);
        tutBorderBottom.x = x - pad;
        tutBorderBottom.y = y + h + pad - 2;
        tutBorderBottom.size(w + pad * 2, 2);
        tutBorderLeft.x = x - pad;
        tutBorderLeft.y = y - pad;
        tutBorderLeft.size(2, h + pad * 2);
        tutBorderRight.x = x + w + pad - 2;
        tutBorderRight.y = y - pad;
        tutBorderRight.size(2, h + pad * 2);
        tutBorderTop.visible = tutBorderBottom.visible = tutBorderLeft.visible = tutBorderRight.visible = true;
        tutHighlightTime = 0f;
    }

    private void hideTutorialHighlight() {
        if (tutBorderTop == null) return;
        tutBorderTop.visible = tutBorderBottom.visible = tutBorderLeft.visible = tutBorderRight.visible = false;
    }

    private void showCombatRewardWindow(final DeckCombatRewardState rewards) {
        final Window reward = new DeckRewardWindow();
        int width = 210;
        int pos = 7;

        RenderedTextBlock title = renderTextBlock("전리품!", 11);
        title.hardlight(Window.TITLE_COLOR);
        title.setPos((width - title.width()) / 2f, pos);
        reward.add(title);
        pos += 22;

        DeckRewardRow goldRow = new DeckRewardRow(com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet.GOLD, rewards.gold + " 골드") {
            @Override
            protected void onClick() {
                if (claimed) return;
                claimed = true;
                DeckBuilderRun.gainGold(rewards.gold);
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
            DeckRewardRow relicRow = new DeckRewardRow(relic.icon, relic.titleWithRarity()) {
                @Override
                protected void onClick() {
                    if (claimed) return;
                    showRelicRewardWindow(reward, this, relic, rewards, relicIndex);
                }
            };
            relicRow.claimed = rewards.relicClaimed != null && i < rewards.relicClaimed.length && rewards.relicClaimed[i];
            relicRow.setRect(10, pos, width - 20, 24);
            reward.add(relicRow);
            pos += 29;
        }

        final DeckPotion rewardPotion = rewards.potion();
        if (rewardPotion != null) {
            DeckRewardRow potionRow = new DeckRewardRow(rewardPotion.image, rewardPotion.title) {
                @Override
                protected void onClick() {
                    if (claimed) return;
                    showPotionRewardWindow(reward, this, rewardPotion, rewards);
                }
            };
            potionRow.claimed = rewards.potionClaimed;
            potionRow.setRect(10, pos, width - 20, 24);
            reward.add(potionRow);
            pos += 29;
        }

        DeckRewardRow cardRow = new DeckRewardRow(ItemSpriteSheet.DECK, "덱에 추가할 카드를 선택하세요") {
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
                if (Dungeon.selectedMode == Dungeon.GameMode.DECKBUILDER_TUTORIAL) {
                    finishTutorialRun();
                } else {
                    continueToFloor();
                }
            }
        };
        done.setRect((width - 100) / 2f, pos, 100, 18);
        reward.add(done);
        pos += 24;

        reward.resize(width, pos);
        addToFront(reward);
        bringRunHudToFront();
    }

    private void showRelicRewardWindow(final Window rewardWindow, final DeckRewardRow relicRow, final DeckRelic relic,
                                       final DeckCombatRewardState rewards, final int relicIndex) {
        final Window win = new DeckRewardWindow();
        int width = 190;
        int pos = 7;

        RenderedTextBlock title = renderTextBlock(relic.titleWithRarity(), 8);
        title.hardlight(Window.TITLE_COLOR);
        title.maxWidth(width - 14);
        title.setPos((width - title.width()) / 2f, pos);
        win.add(title);
        pos += (int) title.height() + 6;

        RenderedTextBlock desc = renderTextBlock(relic.description, 6);
        desc.maxWidth(width - 14);
        desc.hardlight(0xFFD8D1BD);
        desc.setPos(7, pos);
        win.add(desc);
        pos += (int) desc.height() + 8;

        RedButton take = new RedButton("가져가기", 6) {
            @Override
            protected void onClick() {
                if (relicRow.claimed) return;
                Sample.INSTANCE.play(Assets.Sounds.ITEM);
                relicRow.claimed = true;
                DeckBuilderRun.addRelic(relic);
                if (rewards.relicClaimed != null && relicIndex < rewards.relicClaimed.length)
                    rewards.relicClaimed[relicIndex] = true;
                if (runHud != null) runHud.refresh();
                relicRow.text.text("획득 완료: " + relic.titleWithRarity());
                relicRow.text.hardlight(0xFF9A9A9A);
                saveCombatState();
                win.hide();
                rewardWindow.hide();
                showCombatRewardWindow(rewards);
            }
        };
        take.setRect(7, pos, 82, 18);
        win.add(take);

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
        bringRunHudToFront();
    }

    private void showPotionRewardWindow(final Window rewardWindow, final DeckRewardRow potionRow, final DeckPotion potion,
                                        final DeckCombatRewardState rewards) {
        final Window win = new DeckRewardWindow();
        int width = 190;
        int pos = 7;

        RenderedTextBlock title = renderTextBlock(potion.title, 8);
        title.hardlight(Window.TITLE_COLOR);
        title.maxWidth(width - 14);
        title.setPos((width - title.width()) / 2f, pos);
        win.add(title);
        pos += (int) title.height() + 6;

        RenderedTextBlock desc = renderTextBlock(potion.description, 6);
        desc.maxWidth(width - 14);
        desc.hardlight(0xFFD8D1BD);
        desc.setPos(7, pos);
        win.add(desc);
        pos += (int) desc.height() + 8;

        RedButton take = new RedButton("가져가기", 6) {
            @Override
            protected void onClick() {
                if (potionRow.claimed) return;
                if (!DeckBuilderRun.addPotion(potion)) {
                    win.hide();
                    DeckBattleScene.this.addToFront(new WndMessage("포션\n\n빈 포션 슬롯이 없습니다."));
                    return;
                }
                Sample.INSTANCE.play(Assets.Sounds.ITEM);
                potionRow.claimed = true;
                rewards.potionClaimed = true;
                if (runHud != null) runHud.refresh();
                potionRow.text.text("획득 완료: " + potion.title);
                potionRow.text.hardlight(0xFF9A9A9A);
                saveCombatState();
                win.hide();
                rewardWindow.hide();
                showCombatRewardWindow(rewards);
            }
        };
        take.setRect(7, pos, 82, 18);
        win.add(take);

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
        bringRunHudToFront();
    }

    private void showCardRewardWindow(final Window parent, final DeckCard[] cards, final DeckRewardRow cardRow, final DeckCombatRewardState rewards) {
        final Window win = new DeckRewardWindow();
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
                if (DeckBuilderRun.hasRelic(DeckRelic.SUSPICIOUS_TEA)) {
                    DeckBuilderRun.playerHT += 2;
                    DeckBuilderRun.playerHP += 2;
                }
                cardRow.text.text("카드 보상 건너뜀");
                cardRow.text.hardlight(0xFF9A9A9A);
                saveCombatState();
                win.hide();
                if (Dungeon.selectedMode == Dungeon.GameMode.DECKBUILDER_TUTORIAL) {
                    finishTutorialRun();
                }
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

    private void showCardTakeWindow(final Window cardWindow, final DeckRewardRow cardRow, final DeckCard card, final DeckCombatRewardState rewards) {
        final Window win = new DeckRewardWindow();
        int width = 170;
        int pos = 7;

        RenderedTextBlock title = renderTextBlock(cardDetailTitle(card, card.code()), 8);
        title.hardlight(Window.TITLE_COLOR);
        title.setPos((width - title.width()) / 2f, pos);
        win.add(title);
        pos += 16;

        RenderedTextBlock desc = renderTextBlock(DeckCardText.rulesAndKeywordText(card, card.code()), 6);
        desc.maxWidth(width - 14);
        desc.hardlight(0xFFD8D1BD);
        desc.setPos(7, pos);
        win.add(desc);
        pos += (int) desc.height() + 8;

        RedButton take = new RedButton("가져가기", 6) {
            @Override
            protected void onClick() {
                Sample.INSTANCE.play(Assets.Sounds.ITEM);
                DeckBuilderRun.addCard(card);
                DeckBuilderRun.consumeUpgradedCardReward();
                cardRow.claimed = true;
                rewards.cardClaimed = true;
                cardRow.text.text("획득 완료: " + card.title(card.code()));
                cardRow.text.hardlight(0xFF9A9A9A);
                saveCombatState();
                win.hide();
                cardWindow.hide();
                if (Dungeon.selectedMode == Dungeon.GameMode.DECKBUILDER_TUTORIAL) {
                    finishTutorialRun();
                }
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

    void addHudPopupToFront(Window win) {
        hudPopupWindow = win;
        addToFront(win);
    }

    private void bringHudPopupToFront() {
        if (hudPopupWindow != null && hudPopupWindow.parent == this) {
            bringToFront(hudPopupWindow);
        }
    }

    private void continueToFloor() {
        boolean bossCleared = Statistics.deckBuilderMapNode == DeckBuilderMap.BOSS;
        DeckBuilderRun.clearCombat();
        Statistics.deckBuilderMapNode = DeckBuilderMap.NONE;
        if (bossCleared) {
            if (DECKBUILDER_BETA_LIMITS && Dungeon.depth == DeckBuilderMap.bossDepthForAct(1)) {
                showDeckBuilderBetaCompleteMessage();
                return;
            }
            if (DeckBuilderMap.isFinalBossDepth(Dungeon.depth)) {
                finishRunVictory();
                return;
            }
            int nextActStart = DeckBuilderMap.nextActStartDepth(Dungeon.depth);
            if (nextActStart > 0) {
                DeckBuilderRun.prepareNextAct();
                Statistics.deckBuilderMapPath = -1;
                Dungeon.depth = nextActStart;
                DeckBuilderMapScene.curTransition = null;
                try {
                    Level level = Dungeon.levelHasBeenGenerated(Dungeon.depth, Dungeon.branch)
                            ? Dungeon.loadLevel(GamesInProgress.curSlot)
                            : Dungeon.newLevel();
                    Dungeon.switchLevel(level, -1);
                } catch (IOException e) {
                    Game.reportException(e);
                }
                saveCombatState();
                Game.switchScene(DeckBuilderMapScene.class);
                return;
            }
        }
        LevelTransition transition = Dungeon.level == null ? null : Dungeon.level.getTransition(LevelTransition.Type.REGULAR_EXIT);
        if (transition == null && Dungeon.level != null) {
            transition = new LevelTransition(Dungeon.level, Dungeon.hero.pos, LevelTransition.Type.REGULAR_EXIT);
        }
        DeckBuilderMapScene.curTransition = transition;
        saveCombatState();
        Game.switchScene(DeckBuilderMapScene.class);
    }

    private void showDeckBuilderBetaCompleteMessage() {
        if (endingRun) return;
        endingRun = true;
        combatLocked = true;
        hideCardInfo();
        Window win = new WndMessage(
                "카드 배틀 모드는 아직 베타 버전입니다.\n\n" +
                "현재 빌드의 카드 배틀 모드는 17층 보스까지 플레이할 수 있습니다.\n\n" +
                "이 메시지를 클릭하면 엔딩으로 이동합니다.") {
            {
                Button clickArea = new Button() {
                    @Override
                    protected void onClick() {
                        hide();
                        finishDeckBuilderBetaVictory();
                    }
                };
                clickArea.setRect(0, 0, PixelScene.uiCamera.width, PixelScene.uiCamera.height);
                add(clickArea);
            }

            @Override
            public void onBackPressed() {
                hide();
                finishDeckBuilderBetaVictory();
            }
        };
        addToFront(win);
    }

    private void finishDeckBuilderBetaVictory() {
        if (Dungeon.hero != null) {
            Dungeon.win(Amulet.class);
        }
        Dungeon.deleteGame(GamesInProgress.curSlot, true);
        Game.switchScene(SurfaceScene.class);
    }

    private void finishRunVictory() {
        if (endingRun) return;
        endingRun = true;
        combatLocked = true;
        hideCardInfo();
        DeckBuilderRun.clearCombat();
        if (Dungeon.hero != null) {
            Dungeon.win(DeckBuilderVictory.class);
        }
        Dungeon.deleteGame(GamesInProgress.curSlot, true);
        Game.switchScene(RankingsScene.class);
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

    /**
     * 튜토리얼 완료 — 게임 데이터를 삭제하고 타이틀 화면으로 돌아갑니다.
     */
    private void finishTutorialRun() {
        DeckBuilderRun.clearCombat();
        Dungeon.deleteGame(GamesInProgress.curSlot, true);
        Game.switchScene(TitleScene.class);
    }

    private void showCardInfo(int cardCode) {
        showCardInfo(cardCode, -1, true);
    }

    private void showCardInfo(int cardCode, float preferredY) {
        showCardInfo(cardCode, preferredY, true);
    }

    private void showCardInfo(int cardCode, float preferredY, boolean dynamicCombatText) {
        cardInfo.show(cardCode, preferredY, dynamicCombatText);
        addToFront(cardInfo);
    }

    private void hideCardInfo() {
        cardInfo.visible = false;
    }

    private boolean retainedAtEndTurn(int cardCode) {
        DeckCard card = DeckCard.byCode(cardCode);
        return card.hasKeyword(cardCode, DeckCardKeyword.RETAIN) || (card == DeckCard.SHIV && combat.shivRetain);
    }

    private boolean retainedAtEndTurn(int handIndex, int cardCode) {
        return combat.endTurnSelectedRetainIndices.contains(handIndex) || retainedAtEndTurn(cardCode);
    }

    private void log(String text) {
        logText.text(text);
    }

    private String cardRulesText(DeckCard card, int cardCode) {
        return cardRulesText(card, cardCode, true);
    }

    private String cardRulesText(DeckCard card, int cardCode, boolean dynamicCombatText) {
        return DeckCardText.rulesText(card, cardCode, dynamicCombatText ? combat : null);
    }

    private String cardDetailTitle(DeckCard card, int cardCode) {
        return DeckCardText.detailTitle(card, cardCode);
    }

    private String titleText() {
        return Dungeon.depth + "층";
    }

    public static class DeckBuilderRetire {
    }

    public static class DeckBuilderVictory {
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
        if (kind == DeckEnemy.CIVIL_WAR) {
            return new CivilSprite() {
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
        if (kind == DeckEnemy.NUKESAKU) {
            return new RatKingSprite() {
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
        if (kind == DeckEnemy.SICIGIN) {
            return new ZombieSprite() {
                @Override
                public void die() {
                    play(die);
                }

                @Override
                public synchronized void onComplete(Animation anim) {
                    if (anim == attack || anim == run) idle();
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
            return new CausticSlimeSprite() {
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
        if (kind == DeckEnemy.TUTORIAL_DUMMY) {
            return new RatSprite() {
                @Override
                public void die() {
                    play(die);
                }

                @Override
                public synchronized void onComplete(Animation anim) {
                    if (anim == attack || anim == run) idle();
                }
            };
        }
        if (kind == DeckEnemy.LAGAVULIN) {
            return new GreatCrabSprite() {
                @Override
                public void die() {
                    play(die);
                }

                @Override
                public synchronized void onComplete(Animation anim) {
                    if (anim == attack || anim == run) idle();
                }
            };
        }
        if (kind == DeckEnemy.BYRDONIS) {
            return new FetidRatSprite() {
                @Override
                public void die() {
                    play(die);
                }

                @Override
                public synchronized void onComplete(Animation anim) {
                    if (anim == attack || anim == run) idle();
                }
            };
        }
        if (kind == DeckEnemy.RAT_JAGGED) {
            return new GnollTricksterSprite() {
                @Override
                public void die() {
                    play(die);
                }

                @Override
                public synchronized void onComplete(Animation anim) {
                    if (anim == attack || anim == run) idle();
                }
            };
        }
        if (kind == DeckEnemy.RAT_SMOOTH) {
            return new GnollTricksterSprite() {
                @Override
                public void die() {
                    play(die);
                }

                @Override
                public synchronized void onComplete(Animation anim) {
                    if (anim == attack || anim == run) idle();
                }
            };
        }
        if (kind == DeckEnemy.HAUNTED_SHIP) {
            return new SlimeSprite() {
                @Override
                public void die() {
                    play(die);
                }

                @Override
                public synchronized void onComplete(Animation anim) {
                    if (anim == attack || anim == run) idle();
                }
            };
        }
        if (kind == DeckEnemy.CALCIFIED_FANATIC) {
            return new MimicSprite() {
                @Override
                public void die() {
                    play(die);
                }

                @Override
                public synchronized void onComplete(Animation anim) {
                    if (anim == attack || anim == run) idle();
                }
            };
        }
        if (kind == DeckEnemy.ROGUE_SEAWEED) {
            return new BeeSprite() {
                @Override
                public void die() {
                    play(die);
                }

                @Override
                public synchronized void onComplete(Animation anim) {
                    if (anim == attack || anim == run) idle();
                }
            };
        }
        if (kind == DeckEnemy.STAGGERING_VINE) {
            return new HermitCrabSprite() {
                @Override
                public void die() {
                    play(die);
                }

                @Override
                public synchronized void onComplete(Animation anim) {
                    if (anim == attack || anim == run) idle();
                }
            };
        }
        if (kind == DeckEnemy.ANGLERFISH) {
            return new CrabSprite() {
                @Override
                public void die() {
                    play(die);
                }

                @Override
                public synchronized void onComplete(Animation anim) {
                    if (anim == attack || anim == run) idle();
                }
            };
        }
        if (kind == DeckEnemy.GEB_GOD) {
            return new SpeedwagonSprite() {
                @Override
                public void die() {
                    play(die);
                }

                @Override
                public synchronized void onComplete(Animation anim) {
                    if (anim == attack || anim == run) idle();
                }
            };
        }
        // 매핑되지 않은 적의 기본 스프라이트
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
        if (selectingForDaggerThrowDiscard || selectingForPendingExhaust) {
            return;
        }
        if (selectingForPure || selectingForBurningPact || selectingForHiddenDagger || selectingForStrategyRetain || touchOfInsanityActive) {
            selectingForPure = false;
            selectingForBurningPact = false;
            selectingForHiddenDagger = false;
            selectingForStrategyRetain = false;
            touchOfInsanityActive = false;
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
            float angle = (float) (Math.atan2(dy, dx) * 180f / Math.PI);
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
            out[1] = sy + (ty - sy) * e - (float) Math.sin(clamped * Math.PI) * 9f;
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
            float dist = (float) Math.sqrt(tdx * tdx + tdy * tdy);
            float trailAngle = (float) (Math.atan2(tdy, tdx) * 180f / Math.PI);

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
            float cy = sy + (ty - sy) * e - (float) Math.sin(p * Math.PI) * 14f;
            float alpha = 1f - Math.max(0, p - 0.72f) / 0.28f;

            outer.x = cx - 3;
            outer.y = cy - 3;
            outer.am = alpha;

            inner.x = cx - 1;
            inner.y = cy - 1;
            inner.am = alpha;
        }
    }

    private class CardRevealEffect extends BattleEffect {

        private final ColorBlock edge;
        private final ColorBlock face;
        private final Image art;
        private final float cx;
        private final float cy;

        private CardRevealEffect(DeckCard card, float cx, float cy) {
            super(0.6f);
            this.cx = cx;
            this.cy = cy;
            edge = new ColorBlock(1, 1, card.type.borderColor);
            add(edge);
            face = new ColorBlock(1, 1, card.rarity.faceColor);
            add(face);
            Image tmp = null;
            if (card.trapIcon != null) {
                try { tmp = TerrainFeaturesTilemap.getTrapVisual(card.trapIcon.newInstance()); } catch (Exception ignored) {}
            } else if (card.buffIconInt() >= 0) {
                try { tmp = new BuffIcon(card.buffIconInt(), true); } catch (Exception ignored) {}
            } else if (card.talentIcon != null) {
                tmp = new TalentIcon(card.talentIcon);
            } else {
                tmp = new ItemSprite(card.icon());
                tmp.visible = card.icon() != 0;
            }
            art = tmp;
            if (art != null) { art.scale.set(1.4f); add(art); }
            updateEffect(0);
        }

        @Override
        protected void updateEffect(float p) {
            float alpha;
            if (p < 0.18f) alpha = p / 0.18f;
            else if (p < 0.65f) alpha = 1f;
            else alpha = 1f - (p - 0.65f) / 0.35f;
            float scale = 0.6f + alpha * 0.4f;
            float w = 34 * scale;
            float h = 46 * scale;
            edge.x = cx - w / 2f;
            edge.y = cy - h / 2f;
            edge.size(w, h);
            edge.am = alpha;
            face.x = cx - (w - 4) / 2f;
            face.y = cy - (h - 4) / 2f;
            face.size(w - 4, h - 4);
            face.am = alpha;
            if (art != null) {
                art.x = cx - art.width() / 2f;
                art.y = cy - art.height() / 2f;
                art.am = alpha;
            }
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
        private final Image art;

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
            Image tmp = null;
            if (card.trapIcon != null) {
                try { tmp = TerrainFeaturesTilemap.getTrapVisual(card.trapIcon.newInstance()); } catch (Exception ignored) {}
            } else if (card.buffIconInt() >= 0) {
                try { tmp = new BuffIcon(card.buffIconInt(), true); } catch (Exception ignored) {}
            } else if (card.talentIcon != null) {
                tmp = new TalentIcon(card.talentIcon);
            } else {
                tmp = new ItemSprite(card.icon());
                tmp.visible = card.icon() != 0;
            }
            art = tmp != null ? tmp : new ItemSprite(0);
            art.scale.set(1.2f);
            updateEffect(0);
            add(art);
        }

        @Override
        protected void updateEffect(float p) {
            float e = p * p * (3f - 2f * p);
            float cx = sx + (tx - sx) * e;
            float cy = sy + (ty - sy) * e - (float) Math.sin(p * Math.PI) * 18f;
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
        private final Image art;

        private ShuffleIntoDrawPileEffect(DeckCard card, float sx, float sy, float tx, float ty) {
            super(0.38f);
            this.card = card;
            this.sx = sx;
            this.sy = sy;
            this.tx = tx;
            this.ty = ty;
            edge = new ColorBlock(1, 1, card.type.borderColor);
            add(edge);
            face = new ColorBlock(1, 1, DeckCardPool.isNeutralCard(card) || !card.reward ? card.rarity.faceColor : card.classFaceColor());
            add(face);
            streakA = new ColorBlock(1, 1, 0xFFB6F2FF);
            add(streakA);
            streakB = new ColorBlock(1, 1, 0xFFFF9AE8);
            add(streakB);
            Image tmp = null;
            if (card.trapIcon != null) {
                try { tmp = TerrainFeaturesTilemap.getTrapVisual(card.trapIcon.newInstance()); } catch (Exception ignored) {}
            } else if (card.buffIconInt() >= 0) {
                try { tmp = new BuffIcon(card.buffIconInt(), true); } catch (Exception ignored) {}
            } else if (card.talentIcon != null) {
                tmp = new TalentIcon(card.talentIcon);
            } else {
                tmp = new ItemSprite(card.icon());
                tmp.visible = card.icon() != 0;
            }
            art = tmp != null ? tmp : new ItemSprite(0);
            art.scale.set(1.0f);
            updateEffect(0);
            add(art);
        }

        @Override
        protected void updateEffect(float p) {
            float e = p * p * (3f - 2f * p);
            float arc = (float) Math.sin(p * Math.PI) * 18f;
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
            float cy = sy + (ty - sy) * e - (float) Math.sin(p * Math.PI) * 12f;
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
            float[][] dirs = {{-1, -1}, {1, -1}, {-1, 1}, {1, 1}};
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

    // 체력 회복 카드 사용 시 초록 십자 이펙트
    private class HealEffect extends BattleEffect {

        private final ColorBlock vertical;
        private final ColorBlock horizontal;
        private final ColorBlock glow;
        private final float x;
        private final float y;

        private HealEffect(float x, float y) {
            super(0.42f);
            this.x = x;
            this.y = y;
            glow = new ColorBlock(1, 1, 0xAA80FF80);
            vertical = new ColorBlock(1, 1, 0xFF80FF80);
            horizontal = new ColorBlock(1, 1, 0xFF80FF80);
            add(glow);
            add(vertical);
            add(horizontal);
        }

        @Override
        protected void updateEffect(float p) {
            float alpha = 1f - p;
            float size = 18f + p * 14f;
            float thickness = Math.max(2f, 4f - p);
            glow.x = x - size / 2f;
            glow.y = y - size / 2f;
            glow.size(size, size);
            glow.am = alpha * 0.35f;
            vertical.x = x - thickness / 2f;
            vertical.y = y - size / 2f;
            vertical.size(thickness, size);
            vertical.am = alpha;
            horizontal.x = x - size / 2f;
            horizontal.y = y - thickness / 2f;
            horizontal.size(size, thickness);
            horizontal.am = alpha;
        }
    }

    // 파워 카드 사용 시 황금빛 방사형 폭발 이펙트
    private class PowerEffect extends BattleEffect {

        private final ColorBlock top, bottom, left, right;
        private final ColorBlock[] corners = new ColorBlock[4];
        private final ColorBlock glow;
        private final float cx, cy;

        private PowerEffect(float cx, float cy) {
            super(0.56f);
            this.cx = cx;
            this.cy = cy;
            // 중심 글로우 (밝은 황백색)
            glow = new ColorBlock(1, 1, 0xFFFFFFBB);
            add(glow);
            // 상하좌우 막대 (밝은 금색)
            top = new ColorBlock(1, 1, 0xFFFFE060);
            add(top);
            bottom = new ColorBlock(1, 1, 0xFFFFE060);
            add(bottom);
            left = new ColorBlock(1, 1, 0xFFFFE060);
            add(left);
            right = new ColorBlock(1, 1, 0xFFFFE060);
            add(right);
            // 대각선 파티클 (짙은 금색)
            for (int i = 0; i < corners.length; i++) {
                corners[i] = new ColorBlock(1, 1, 0xFFFFD84D);
                add(corners[i]);
            }
        }

        @Override
        protected void updateEffect(float p) {
            // 페이드: 빠른 등장(0>0.15) > 유지(0.15>0.5) > 서서히 소멸(0.5>1.0)
            float alpha;
            if (p < 0.15f) alpha = p / 0.15f;
            else if (p < 0.5f) alpha = 1f;
            else alpha = 1f - (p - 0.5f) / 0.5f;

            // 중심 글로우: 크게 시작해서 서서히 줄어들며 사라짐
            float gs = 12f - p * 7f;
            glow.size(gs, gs);
            glow.x = cx - gs / 2f;
            glow.y = cy - gs / 2f;
            glow.am = alpha * 0.85f;

            // 상하좌우 막대가 중심에서 바깥으로 뻗어나감
            float dist = 3f + p * 24f;
            float bw = 3f;
            float bh = Math.max(1.5f, 7f - p * 5f);
            top.size(bw, bh);
            top.x = cx - bw / 2f;
            top.y = cy - dist - bh;
            top.am = alpha;
            bottom.size(bw, bh);
            bottom.x = cx - bw / 2f;
            bottom.y = cy + dist;
            bottom.am = alpha;
            left.size(bh, bw);
            left.x = cx - dist - bh;
            left.y = cy - bw / 2f;
            left.am = alpha;
            right.size(bh, bw);
            right.x = cx + dist;
            right.y = cy - bw / 2f;
            right.am = alpha;

            // 대각선 코너 파티클
            float cd = dist * 0.75f;
            float cs = Math.max(1.5f, 4f - p * 2.5f);
            float[][] cdir = {{-1f, -1f}, {1f, -1f}, {-1f, 1f}, {1f, 1f}};
            for (int i = 0; i < corners.length; i++) {
                corners[i].size(cs, cs);
                corners[i].x = cx + cdir[i][0] * cd - cs / 2f;
                corners[i].y = cy + cdir[i][1] * cd - cs / 2f;
                corners[i].am = alpha * 0.8f;
            }
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

        private void show(int cardCode, float preferredY, boolean dynamicCombatText) {
            DeckCard card = DeckCard.byCode(cardCode);
            visible = true;
            title.text(cardDetailTitle(card, cardCode));
            body.text(cardRulesText(card, cardCode, dynamicCombatText));
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
            if (card.strength(cardCode) > 0)
                text += (text.length() > 0 ? "\n" : "") + "공격력: 공격 카드의 피해가 증가합니다.";
            for (DeckCardKeyword keyword : DeckCardKeyword.values()) {
                if (card.hasKeyword(cardCode, keyword)) {
                    text += (text.length() > 0 ? "\n" : "") + keyword.label + ": " + keyword.description;
                }
            }
            if (card.handPenalty > 0)
                text += (text.length() > 0 ? "\n" : "") + "방해: 손에 있으면 공격 카드 피해가 감소합니다.";
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
            if (selectingForPure || selectingForBurningPact || selectingForHiddenDagger || selectingForStrategyRetain || selectingForDaggerThrowDiscard || selectingForPendingExhaust) {
                return handIndex != pureHandIndex;
            }
            if (touchOfInsanityActive) {
                return true;
            }
            if (gamblerBrewActive) {
                return true;
            }
            if (card() == DeckCard.BURNING_PACT && combat.hand.size() <= 1) {
                return false;
            }
            if (card() == DeckCard.END_OF_PACT && combat.exhaustPile.size() < 3) {
                return false;
            }
            if (card().unplayable(cardCode())) {
                return false;
            }
            return !combatLocked && !tutorialMessageOpen() && handIndex < combat.hand.size() && combat.cardCost(cardCode()) <= combat.energy;
        }

        @Override
        protected void layout() {
            super.layout();
            if ((selectingForPure || selectingForBurningPact || selectingForHiddenDagger || selectingForStrategyRetain || selectingForDaggerThrowDiscard || selectingForPendingExhaust || touchOfInsanityActive || gamblerBrewActive) && pureSelectedIndices.contains(handIndex)) {
                edge.color(0xFFA8F26A);
                edge.am = 1.0f;
                face.am = 0.92f;
            } else if (!selectingForPure && !selectingForBurningPact && !selectingForHiddenDagger && !selectingForStrategyRetain && !selectingForDaggerThrowDiscard && !selectingForPendingExhaust && !gamblerBrewActive && !selectingWandForStaff
                    && combat != null && card().conditionMet(cardCode(), combat)) {
                edge.color(0xFFA8F26A);
                edge.am = 1.0f;
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
            if (selectingForPure || selectingForBurningPact || selectingForHiddenDagger || selectingForStrategyRetain || selectingForDaggerThrowDiscard || selectingForPendingExhaust || touchOfInsanityActive || gamblerBrewActive) {
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
            if (combatLocked || tutorialMessageOpen()) return;
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
            if (selectingForPure || selectingForBurningPact || selectingForHiddenDagger || selectingForStrategyRetain || selectingForDaggerThrowDiscard || selectingForPendingExhaust || touchOfInsanityActive || gamblerBrewActive) return;
            if (!activeTouch || !enabled()) return;
            float dx = event.current.x - event.start.x;
            float dy = event.current.y - event.start.y;
            if (!dragging && dx * dx + dy * dy < 36) return;
            dragging = true;
            clickReady = false;
            com.watabou.utils.PointF p = camera().screenToCamera((int) event.current.x, (int) event.current.y);
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
            if (selectingForPure || selectingForBurningPact || selectingForHiddenDagger || selectingForStrategyRetain || selectingForDaggerThrowDiscard || selectingForPendingExhaust || touchOfInsanityActive || gamblerBrewActive) {
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
            if (selectingForPure || selectingForBurningPact || selectingForHiddenDagger || selectingForStrategyRetain || selectingForDaggerThrowDiscard || selectingForPendingExhaust) {
                if (handIndex == pureHandIndex) {
                    selectingForPure = false;
                    selectingForBurningPact = false;
                    selectingForHiddenDagger = false;
                    selectingForStrategyRetain = false;
                    selectingForDaggerThrowDiscard = false;
                    selectingForPendingExhaust = false;
                    hidePureSelectionBanner();
                    pureSelectedIndices.clear();
                    refresh();
                } else {
                    Integer idx = handIndex;
                    if (pureSelectedIndices.contains(idx)) {
                        pureSelectedIndices.remove(idx);
                    } else if (pureSelectedIndices.size() < pureMaxSelect) {
                        if (selectingForBurningPact || ((selectingForDaggerThrowDiscard || selectingForPendingExhaust) && pureMaxSelect <= 1)) pureSelectedIndices.clear();
                        pureSelectedIndices.add(idx);
                    }
                    updatePureConfirmButton();
                    refresh();
                }
                return;
            }
            if (touchOfInsanityActive) {
                Integer idx = handIndex;
                if (pureSelectedIndices.contains(idx)) {
                    pureSelectedIndices.remove(idx);
                } else {
                    pureSelectedIndices.clear();
                    pureSelectedIndices.add(idx);
                }
                updatePureConfirmButton();
                refresh();
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

    private class RewardCardButton extends CardViewButton {

        private final DeckCard card;
        private final boolean dynamicCombatText;

        private RewardCardButton(DeckCard card) {
            this(card, false);
        }

        private RewardCardButton(DeckCard card, boolean dynamicCombatText) {
            super();
            this.card = card;
            this.dynamicCombatText = dynamicCombatText;
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
            showCardInfo(cardCode(), Camera.main.height * 0.14f, dynamicCombatText);
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
        protected Image trapArt;
        protected Image buffArt;
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

            face.color(DeckCardPool.isNeutralCard(card) || !card.reward ? card.rarity.faceColor : card.classFaceColor());
            face.x = x + 2;
            face.y = y + 2;
            face.size(width - 4, height - 4);
            face.am = enabled ? 0.96f : 0.68f;

            cost.text(card.cost(cardCode) < 0 ? "X" : String.valueOf(displayCost(card, cardCode)));
            cost.hardlight(enabled ? 0xFFFFD84D : 0xFF8A7A42);
            cost.setPos(x + 4, y + 4);

            title.text(card.title(cardCode));
            int titleColor = card.rarity == DeckCardRarity.COMMON ? 0xFFFFFFFF : card.rarity.labelColor;
            title.hardlight(enabled ? titleColor : 0xFF8A8A8A);
            title.maxWidth((int) width - 14);
            title.setPos(x + 12, y + 5);

            typeLabel.text(card.type.label);
            typeLabel.hardlight(enabled ? cardLabelColor(card) : 0xFF8A8A8A);
            typeLabel.maxWidth((int) width - 10);
            float labelX = x + (width - typeLabel.width()) / 2f;
            float labelY = y + height - typeLabel.height() - 6;
            typeLabel.setPos(labelX, labelY);

            int maxCharge = DeckCard.maxCharge(cardCode);
            if (maxCharge > 0) {
                chargeLabel.visible = true;
                int currentCharge = DeckCard.currentCharge(cardCode);
                chargeLabel.text(currentCharge + "/" + maxCharge);
                chargeLabel.hardlight(0xFFFF00);
                chargeLabel.maxWidth((int) width - 10);
                float chargeX = x + (width - chargeLabel.width()) / 2f;
                float chargeY = labelY - chargeLabel.height() - 1;
                chargeLabel.setPos(chargeX, chargeY);
            } else {
                chargeLabel.visible = false;
            }

            float artH = Math.max(18, height * 0.46f);
            artPanel.color(DeckCardPool.isNeutralCard(card) || !card.reward ? card.rarity.panelColor : card.classPanelColor());
            artPanel.x = x + 5;
            artPanel.y = y + height * 0.28f;
            artPanel.size(width - 10, artH);
            artPanel.am = enabled ? 0.30f : 0.12f;

            if (card == DeckCard.SLIMY) {
                if (art != null) art.visible = false;
                if (talentArt != null) talentArt.visible = false;
                if (trapArt != null) trapArt.visible = false;
                if (buffArt != null) buffArt.visible = false;
                spriteArt.visible = true;
                spriteArt.texture(Assets.Sprites.RAT);
                TextureFilm gnollFilm = new TextureFilm(spriteArt.texture, 16, 15);
                spriteArt.frame(gnollFilm.get(0));
                spriteArt.scale.set(1.6f);
                spriteArt.x = artPanel.x + (artPanel.width() - spriteArt.width()) / 2f;
                spriteArt.y = artPanel.y + (artPanel.height() - spriteArt.height()) / 2f;
                align(spriteArt);
            } else if (card == DeckCard.PRICE_OF_SIN) {
                if (art != null) art.visible = false;
                if (talentArt != null) talentArt.visible = false;
                if (trapArt != null) trapArt.visible = false;
                if (buffArt != null) buffArt.visible = false;
                spriteArt.visible = true;
                spriteArt.texture(Assets.Sprites.CIVIL);
                TextureFilm gnollFilm = new TextureFilm(spriteArt.texture, 12, 17);
                spriteArt.frame(gnollFilm.get(0));
                spriteArt.scale.set(1.6f);
                spriteArt.x = artPanel.x + (artPanel.width() - spriteArt.width()) / 2f;
                spriteArt.y = artPanel.y + (artPanel.height() - spriteArt.height()) / 2f;
                align(spriteArt);
            } else if (card.talentIcon != null) {
                if (art != null) art.visible = false;
                if (trapArt != null) trapArt.visible = false;
                if (buffArt != null) buffArt.visible = false;
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
            } else if (card.trapIcon != null) {
                if (art != null) art.visible = false;
                if (talentArt != null) talentArt.visible = false;
                if (buffArt != null) buffArt.visible = false;
                spriteArt.visible = false;
                if (trapArt != null) remove(trapArt);
                try {
                    trapArt = TerrainFeaturesTilemap.getTrapVisual(card.trapIcon.newInstance());
                } catch (Exception ignored) { trapArt = null; }
                if (trapArt != null) {
                    trapArt.scale.set(1.25f);
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
                    buffArt.scale.set(1.25f);
                    buffArt.x = artPanel.x + (artPanel.width() - buffArt.width()) / 2f;
                    buffArt.y = artPanel.y + (artPanel.height() - buffArt.height()) / 2f;
                    align(buffArt);
                    buffArt.visible = true;
                    add(buffArt);
                }
            } else {
                if (talentArt != null) talentArt.visible = false;
                if (trapArt != null) trapArt.visible = false;
                if (buffArt != null) buffArt.visible = false;
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
            if (DeckCardPool.isStatusOrCurse(card)) {
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
