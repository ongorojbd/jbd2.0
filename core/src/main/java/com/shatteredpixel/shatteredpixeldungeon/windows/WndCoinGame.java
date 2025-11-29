/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2025 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class WndCoinGame extends Window {

	// === 게임 상태 저장용 static 변수 (탭 전환 시 유지) ===
	private static int savedWater = 0;
	private static boolean savedIsPlayerTurn = true;
	private static int savedStateOrdinal = 0; // GameState의 ordinal 저장
	private static int savedSelectedCoins = 0; // 선택한 동전 수 저장
	private static boolean savedTurnActionDone = false; // 현재 턴에서 액션을 완료했는지
	private static boolean gameInProgress = false;
	private static boolean gameAlreadyPlayed = false; // 이미 플레이했는지 여부

	// 반응형 크기 설정
	private int WIDTH;
	private int HEIGHT;
	
	// 동적 레이아웃 변수
	private float cupY;
	private float cupSize;
	private float arrowAreaWidth;
	private float arrowAreaLeft;
	private float arrowAreaTop;
	private float buttonAreaY;

	// 게임 상태
	private enum GameState {
		COIN_SELECT,    // 동전 선택 단계
		ARROW_MOVING,   // 화살표 이동 중 (STOP 대기)
		ARROW_STOPPED,  // 화살표 정지 완료, 결과 계산
		DARBY_TURN,     // 다비 턴
		GAME_OVER       // 게임 종료
	}

	private GameState state = GameState.COIN_SELECT;
	private boolean isPlayerTurn = true;

	// 물컵 관련
	private static final int MAX_WATER = 100;
	private int currentWater = 0;

	// 동전 선택
	private int selectedCoins = 0;

	// 화살표 관련
	private float arrowAPos = 0;      // 화살표 A (자동, 계속 움직임)
	private float arrowBPos = 0;      // 화살표 B (플레이어/AI 조작)
	private boolean arrowAMovingRight = true;
	private boolean arrowBMovingRight = false;
	private boolean arrowsStopped = false;  // 둘 다 동시에 멈춤
	private float arrowASpeed = 300f;  // 화살표 A 속도 (더 빠르게)
	private float arrowBSpeed = 200f;  // 화살표 B 속도

	// UI 요소
	private RenderedTextBlock titleText;
	private RenderedTextBlock turnText;
	private RenderedTextBlock waterText;
	private RenderedTextBlock instructionText;
	private RenderedTextBlock gapText;
	
	private ColorBlock cupBackground;
	private ColorBlock cupWater;
	private ColorBlock cupOutline1, cupOutline2, cupOutline3;

	private ColorBlock arrowArea;
	private ColorBlock arrowA;
	private ColorBlock arrowB;
	private ColorBlock centerLine;
	
	private RedButton[] coinButtons = new RedButton[5];
	private RedButton stopButton;
	private RedButton continueButton;

	// 다비 AI 관련
	private float darbyThinkTimer = 0;
	private float darbyActionTimer = 0;
	private int darbyCoins = 0;
	private boolean darbyNeedsToSelectCoins = false;
	private boolean darbyNeedsToStopArrow = false;
	private float darbyStopTargetTime = 0;
	
	// 버튼 클릭 방지 타이머 (결과 표시 직후 실수로 터치하는 것 방지)
	private float clickGuardTimer = 0;
	private static final float CLICK_GUARD_DURATION = 0.5f;
	
	// 클릭 처리 중 플래그 (중복 클릭 방지)
	private boolean processingClick = false;

	// 보상 관련 (최대 체력 변화)
	private static final int HP_CHANGE = 10;

	public WndCoinGame() {
		super();

		// 화면 방향에 따른 크기 조절
		boolean landscape = PixelScene.landscape();
		
		if (landscape) {
			WIDTH = 160;
			HEIGHT = 155;
			cupSize = 32;
			cupY = 26;
			arrowAreaWidth = 120;
			arrowAreaLeft = 20;
			arrowAreaTop = 68;
			buttonAreaY = 105;
		} else {
			WIDTH = 120;
			HEIGHT = 180;
			cupSize = 36;
			cupY = 26;
			arrowAreaWidth = 100;
			arrowAreaLeft = 10;
			arrowAreaTop = 75;
			buttonAreaY = 115;
		}

		resize(WIDTH, HEIGHT);

		// 이미 플레이한 경우
		if (gameAlreadyPlayed && !gameInProgress) {
			showAlreadyPlayed();
			return;
		}

		// 저장된 게임 상태 복원
		if (gameInProgress) {
			currentWater = savedWater;
			isPlayerTurn = savedIsPlayerTurn;
			state = GameState.values()[savedStateOrdinal];
			selectedCoins = savedSelectedCoins;
		} else {
			// 새 게임 시작
			currentWater = 0;
			isPlayerTurn = true;
			state = GameState.COIN_SELECT;
			selectedCoins = 0;
			savedTurnActionDone = false;
			gameInProgress = true;
		}

		setupMainUI();
		
		// 저장된 상태에 따라 UI 복원
		restoreUIState();
	}
	
	// 저장된 상태에 맞게 UI 복원
	private void restoreUIState() {
		switch (state) {
			case COIN_SELECT:
				if (isPlayerTurn) {
					// 이미 동전을 선택했으면 화살표 단계로
					if (savedTurnActionDone && selectedCoins > 0) {
						startArrowPhase();
					} else {
						setupCoinSelectUI();
					}
				} else {
					startDarbyTurn();
				}
				break;
			case ARROW_STOPPED:
				// 결과 확인 대기 상태 - 계속 버튼 표시
				showContinueButton();
				break;
			case DARBY_TURN:
				startDarbyTurn();
				break;
			case ARROW_MOVING:
				// 화살표 이동 중이었다면 화살표 단계 다시 시작 (동전은 이미 선택됨)
				if (isPlayerTurn) {
					startArrowPhase();
				} else {
					startDarbyTurn();
				}
				break;
			default:
				if (isPlayerTurn) {
					if (savedTurnActionDone && selectedCoins > 0) {
						startArrowPhase();
					} else {
						setupCoinSelectUI();
					}
				} else {
					startDarbyTurn();
				}
				break;
		}
	}
	
	// 계속 버튼만 표시 (ARROW_STOPPED 상태 복원용)
	private void showContinueButton() {
		clearActionUI();
		
		instructionText = PixelScene.renderTextBlock(Messages.get(this, "continue_prompt"), 6);
		instructionText.setPos((WIDTH - instructionText.width()) / 2, buttonAreaY + 5);
		add(instructionText);

		continueButton = new RedButton(Messages.get(this, "continue")) {
			@Override
			protected void onClick() {
				// 클릭 가드 중이거나 처리 중이면 무시
				if (clickGuardTimer > 0 || processingClick) return;
				// ARROW_STOPPED 상태에서만 클릭 허용
				if (state != GameState.ARROW_STOPPED) return;
				processingClick = true;
				nextTurn();
			}
		};
		continueButton.setRect(10, buttonAreaY + 22, WIDTH - 20, 18);
		add(continueButton);
		
		// 다비 턴일 때는 클릭 가드 활성화
		if (!isPlayerTurn) {
			clickGuardTimer = CLICK_GUARD_DURATION;
		}
	}
	
	// 이미 플레이한 경우 메시지 표시
	private void showAlreadyPlayed() {
		RenderedTextBlock msg = PixelScene.renderTextBlock(
				Messages.get(this, "already_played"), 7);
		msg.maxWidth(WIDTH - 20);
		msg.setPos((WIDTH - msg.width()) / 2, HEIGHT / 2 - 20);
		add(msg);

		RedButton exitBtn = new RedButton(Messages.get(this, "close")) {
			@Override
			protected void onClick() {
				hide();
			}
		};
		exitBtn.setRect(20, HEIGHT / 2 + 10, WIDTH - 40, 20);
		add(exitBtn);
	}

	// 현재 상태 저장
	private void saveGameState() {
		savedWater = currentWater;
		savedIsPlayerTurn = isPlayerTurn;
		savedStateOrdinal = state.ordinal();
		savedSelectedCoins = selectedCoins;
	}
	
	// 턴 액션 완료 표시 (동전 선택 후 호출)
	private void markTurnActionDone() {
		savedTurnActionDone = true;
		saveGameState();
	}
	
	// 턴 전환 시 액션 완료 플래그 리셋
	private void resetTurnAction() {
		savedTurnActionDone = false;
	}

	// 게임 종료 시 저장 상태 초기화
	public static void resetGameState() {
		savedWater = 0;
		savedIsPlayerTurn = true;
		savedStateOrdinal = 0;
		savedSelectedCoins = 0;
		savedTurnActionDone = false;
		gameInProgress = false;
	}
	
	// 새 던전 시작 시 호출 (게임 플레이 가능하도록 초기화)
	public static void resetForNewGame() {
		resetGameState();
		gameAlreadyPlayed = false;
	}
	
	// 게임이 이미 플레이되었는지 확인
	public static boolean hasBeenPlayed() {
		return gameAlreadyPlayed;
	}

	private void setupMainUI() {
		// 제목
		titleText = PixelScene.renderTextBlock(Messages.get(this, "title"), 9);
		titleText.hardlight(TITLE_COLOR);
		titleText.setPos((WIDTH - titleText.width()) / 2, 2);
		add(titleText);

		// 턴 표시
		if (isPlayerTurn) {
			turnText = PixelScene.renderTextBlock(Messages.get(this, "player_turn"), 7);
			turnText.hardlight(0x44FF44);
		} else {
			turnText = PixelScene.renderTextBlock(Messages.get(this, "darby_turn"), 7);
			turnText.hardlight(0xFF6666);
		}
		turnText.setPos((WIDTH - turnText.width()) / 2, 14);
		add(turnText);

		// === 물컵 시각화 ===
		float cupX = WIDTH / 2 - cupSize / 2;

		// 컵 배경 (빈 공간)
		cupBackground = new ColorBlock(cupSize - 4, cupSize - 4, 0xFF1a1a2e);
		cupBackground.x = cupX + 2;
		cupBackground.y = cupY + 2;
		add(cupBackground);

		// 물 (아래에서부터 차오름)
		updateWaterVisual();

		// 컵 테두리 (왼쪽, 오른쪽, 아래)
		cupOutline1 = new ColorBlock(2, cupSize, 0xFF8899AA); // 왼쪽
		cupOutline1.x = cupX;
		cupOutline1.y = cupY;
		add(cupOutline1);

		cupOutline2 = new ColorBlock(2, cupSize, 0xFF8899AA); // 오른쪽
		cupOutline2.x = cupX + cupSize - 2;
		cupOutline2.y = cupY;
		add(cupOutline2);

		cupOutline3 = new ColorBlock(cupSize, 2, 0xFF8899AA); // 아래
		cupOutline3.x = cupX;
		cupOutline3.y = cupY + cupSize - 2;
		add(cupOutline3);

		// 물 양 텍스트
		waterText = PixelScene.renderTextBlock(
				Messages.get(this, "water_display", currentWater, MAX_WATER), 6);
		waterText.hardlight(0x66CCFF);
		waterText.setPos((WIDTH - waterText.width()) / 2, cupY + cupSize + 2);
		add(waterText);

		// === 화살표 영역 ===
		// 배경
		arrowArea = new ColorBlock(Math.round(arrowAreaWidth + 6), 28, 0x33333355);
		arrowArea.x = Math.round(arrowAreaLeft - 3);
		arrowArea.y = Math.round(arrowAreaTop - 3);
		add(arrowArea);

		// 중앙선 (목표 지점)
		centerLine = new ColorBlock(2, 28, 0x88FFFF00);
		centerLine.x = Math.round(arrowAreaLeft + arrowAreaWidth / 2 - 1);
		centerLine.y = Math.round(arrowAreaTop - 3);
		add(centerLine);

		// 화살표 A (빨간색 - 자동)
		arrowA = new ColorBlock(5, 10, 0xFFFF4444);
		arrowA.x = Math.round(arrowAreaLeft + arrowAPos - 2);
		arrowA.y = Math.round(arrowAreaTop);
		add(arrowA);

		// 화살표 B (파란색 - 조작 가능)
		arrowB = new ColorBlock(5, 10, 0xFF44AAFF);
		arrowB.x = Math.round(arrowAreaLeft + arrowBPos - 2);
		arrowB.y = Math.round(arrowAreaTop + 12);
		add(arrowB);

		// 간격 표시 텍스트 (초기에는 숨김)
		gapText = PixelScene.renderTextBlock("", 6);
		gapText.setPos((WIDTH - gapText.width()) / 2, arrowAreaTop + 25);
		add(gapText);
	}

	private void updateWaterVisual() {
		float cupX = WIDTH / 2 - cupSize / 2;
		float innerWidth = cupSize - 4;
		float innerHeight = cupSize - 4;

		if (cupWater != null) {
			remove(cupWater);
		}

		float waterRatio = Math.min(1f, (float) currentWater / MAX_WATER);
		float waterHeight = innerHeight * waterRatio;
		
		int waterColor;
		if (waterRatio < 0.5f) {
			waterColor = 0xFF4488FF; // 파란색
		} else if (waterRatio < 0.75f) {
			Music.INSTANCE.play(Assets.Music.HALLS_BOSS, true);
			waterColor = 0xFFFFAA00; // 주황색
		} else {
			Music.INSTANCE.play(Assets.Music.KOICHI, true);
			waterColor = 0xFFFF4444; // 빨간색 (위험)
		}

		if (waterHeight > 0) {
			cupWater = new ColorBlock(innerWidth, waterHeight, waterColor);
			cupWater.x = cupX + 2;
			cupWater.y = cupY + 2 + (innerHeight - waterHeight);
			add(cupWater);
		}
	}

	private void setupCoinSelectUI() {
		state = GameState.COIN_SELECT;
		clearActionUI();
		
		// 클릭 처리 플래그 리셋
		processingClick = false;
		
		// 간격 텍스트 숨기기
		if (gapText != null) {
			gapText.visible = false;
		}

		// 안내 텍스트
		instructionText = PixelScene.renderTextBlock(
				Messages.get(this, "select_coins"), 6);
		instructionText.setPos((WIDTH - instructionText.width()) / 2, buttonAreaY);
		add(instructionText);

		// 동전 선택 버튼 (1~5)
		float btnWidth = 18;
		float btnGap = 3;
		float totalWidth = btnWidth * 5 + btnGap * 4;
		float startX = (WIDTH - totalWidth) / 2;

		for (int i = 0; i < 5; i++) {
			final int coins = i + 1;
			coinButtons[i] = new RedButton(String.valueOf(coins)) {
				@Override
				protected void onClick() {
					// 처리 중이거나 상태가 맞지 않으면 무시
					if (processingClick || state != GameState.COIN_SELECT || !isPlayerTurn) return;
					processingClick = true;
					selectCoins(coins);
				}
			};
			coinButtons[i].setRect(startX + i * (btnWidth + btnGap), buttonAreaY + 14, btnWidth, 18);
			add(coinButtons[i]);
		}
	}

	private void selectCoins(int coins) {
		selectedCoins = coins;
		Sample.INSTANCE.play(Assets.Sounds.CLICK);
		markTurnActionDone(); // 동전 선택 완료 표시
		startArrowPhase();
	}

	private void startArrowPhase() {
		state = GameState.ARROW_MOVING;
		clearActionUI();
		
		// 클릭 처리 플래그 리셋
		processingClick = false;

		// 화살표 초기화 - 랜덤 위치에서 시작
		arrowAPos = Random.Float(arrowAreaWidth * 0.1f, arrowAreaWidth * 0.9f);
		arrowBPos = Random.Float(arrowAreaWidth * 0.1f, arrowAreaWidth * 0.9f);
		arrowAMovingRight = Random.Int(2) == 0;
		arrowBMovingRight = Random.Int(2) == 0;
		arrowsStopped = false;

		// 안내 텍스트
		instructionText = PixelScene.renderTextBlock(
				Messages.get(this, "stop_arrow_b", selectedCoins), 6);
		instructionText.maxWidth(WIDTH - 10);
		instructionText.setPos((WIDTH - instructionText.width()) / 2, buttonAreaY);
		add(instructionText);

		// STOP 버튼
		stopButton = new RedButton(Messages.get(this, "stop")) {
			@Override
			protected void onClick() {
				// 처리 중이거나 상태가 맞지 않으면 무시
				if (processingClick) return;
				if (state == GameState.ARROW_MOVING && !arrowsStopped && isPlayerTurn) {
					processingClick = true;
					stopArrows();
				}
			}
		};
		stopButton.setRect(10, buttonAreaY + 20, WIDTH - 20, 20);
		add(stopButton);

		// 간격 텍스트 숨기기
		if (gapText != null) {
			gapText.visible = false;
		}
	}

	private void stopArrows() {
		// 둘 다 동시에 멈춤
		arrowsStopped = true;
		Sample.INSTANCE.play(Assets.Sounds.HIT);
		
		if (stopButton != null) {
			stopButton.enable(false);
		}

		// 결과 계산
		calculateResult();
	}

	private void calculateResult() {
		state = GameState.ARROW_STOPPED;
		
		// 클릭 처리 플래그 리셋 (계속 버튼 클릭 가능하도록)
		processingClick = false;
		
		// 간격 계산 (0 ~ arrowAreaWidth)
		float gap = Math.abs(arrowAPos - arrowBPos);
		float gapRatio = gap / arrowAreaWidth; // 0 ~ 1
		
		// 물 추가량 = 동전 수 × 간격비율 × 기본 배수
		int waterAdded = Math.round(selectedCoins * gapRatio * 15);
		waterAdded = Math.max(1, waterAdded); // 최소 1
		
		// 간격 표시
		gapText.text(Messages.get(this, "gap_result", (int)gap, waterAdded));
		gapText.hardlight(gapRatio < 0.3f ? 0x44FF44 : (gapRatio < 0.6f ? 0xFFFF00 : 0xFF4444));
		gapText.setPos((WIDTH - gapText.width()) / 2, arrowAreaTop + 25);
		gapText.visible = true;

		// 물 추가
		currentWater += waterAdded;

		// 상태 저장
		saveGameState();

		// UI 업데이트
		updateWaterVisual();
		waterText.text(Messages.get(this, "water_display", currentWater, MAX_WATER));
		waterText.setPos((WIDTH - waterText.width()) / 2, cupY + cupSize + 2);

		// 패배 체크
		if (currentWater > MAX_WATER) {
			if (isPlayerTurn) {
				showGameOver(false); // 플레이어 패배
			} else {
				showGameOver(true); // 플레이어 승리
			}
			return;
		}

		// 다음 턴으로
		clearActionUI();
		
		String resultMsg = isPlayerTurn ? 
				Messages.get(this, "player_added", waterAdded) :
				Messages.get(this, "darby_added", waterAdded);
		
		instructionText = PixelScene.renderTextBlock(resultMsg, 6);
		instructionText.hardlight(isPlayerTurn ? 0x88FF88 : 0xFF8888);
		instructionText.setPos((WIDTH - instructionText.width()) / 2, buttonAreaY + 5);
		add(instructionText);

		continueButton = new RedButton(Messages.get(this, "continue")) {
			@Override
			protected void onClick() {
				// 클릭 가드 중이거나 처리 중이면 무시
				if (clickGuardTimer > 0 || processingClick) return;
				// ARROW_STOPPED 상태에서만 클릭 허용
				if (state != GameState.ARROW_STOPPED) return;
				processingClick = true;
				nextTurn();
			}
		};
		continueButton.setRect(10, buttonAreaY + 22, WIDTH - 20, 18);
		add(continueButton);
		
		// 다비 턴일 때는 클릭 가드 활성화 (실수 터치 방지)
		if (!isPlayerTurn) {
			clickGuardTimer = CLICK_GUARD_DURATION;
		}
	}

	private void nextTurn() {
		isPlayerTurn = !isPlayerTurn;
		selectedCoins = 0; // 새 턴 시작 시 동전 선택 초기화
		
		// 턴 전환 시 액션 완료 플래그 리셋
		resetTurnAction();
		
		// 클릭 처리 플래그 리셋
		processingClick = false;
		
		// 간격 텍스트 숨기기
		if (gapText != null) {
			gapText.visible = false;
		}
		
		// 상태 저장
		saveGameState();
		
		// 턴 표시 업데이트
		if (isPlayerTurn) {
			turnText.text(Messages.get(this, "player_turn"));
			turnText.hardlight(0x44FF44);
			setupCoinSelectUI();
		} else {
			turnText.text(Messages.get(this, "darby_turn"));
			turnText.hardlight(0xFF6666);
			startDarbyTurn();
		}
		turnText.setPos((WIDTH - turnText.width()) / 2, 14);
	}

	private void startDarbyTurn() {
		state = GameState.DARBY_TURN;
		clearActionUI();
		
		// 클릭 처리 플래그 리셋
		processingClick = false;
		
		// 간격 텍스트 숨기기
		if (gapText != null) {
			gapText.visible = false;
		}

		// 다비 생각 중...
		instructionText = PixelScene.renderTextBlock(
				Messages.get(this, "darby_thinking"), 6);
		instructionText.hardlight(0xFFAAAA);
		instructionText.setPos((WIDTH - instructionText.width()) / 2, buttonAreaY + 10);
		add(instructionText);

		// AI 동전 선택 (전략적)
		darbyThinkTimer = 0;
		darbyActionTimer = 0;
		darbyNeedsToSelectCoins = true;
		darbyNeedsToStopArrow = false;
	}

	private int darbySelectCoins() {
		// 남은 여유 공간
		int remaining = MAX_WATER - currentWater;
		
		// 안전하게 플레이: 여유가 적으면 적은 동전, 많으면 공격적으로
		if (remaining > 60) {
			return Random.IntRange(3, 5); // 공격적
		} else if (remaining > 40) {
			return Random.IntRange(2, 4); // 중간
		} else if (remaining > 20) {
			return Random.IntRange(1, 3); // 조심
		} else {
			return Random.IntRange(1, 2); // 매우 조심
		}
	}

	private void darbyStartArrowPhase() {
		// 화살표 초기화
		arrowAPos = Random.Float(arrowAreaWidth * 0.1f, arrowAreaWidth * 0.9f);
		arrowBPos = Random.Float(arrowAreaWidth * 0.1f, arrowAreaWidth * 0.9f);
		arrowAMovingRight = Random.Int(2) == 0;
		arrowBMovingRight = Random.Int(2) == 0;
		arrowsStopped = false;

		state = GameState.ARROW_MOVING;
		darbyNeedsToStopArrow = true;
		darbyActionTimer = 0;
		// 다비는 1~2초 사이에 멈춤
		darbyStopTargetTime = 1f + Random.Float(1f);

		// 안내 텍스트 업데이트
		if (instructionText != null) {
			remove(instructionText);
		}
		instructionText = PixelScene.renderTextBlock(
				Messages.get(this, "darby_coins", darbyCoins), 6);
		instructionText.hardlight(0xFFAAAA);
		instructionText.setPos((WIDTH - instructionText.width()) / 2, buttonAreaY + 10);
		add(instructionText);

		// 간격 텍스트 숨기기
		if (gapText != null) {
			gapText.visible = false;
		}
	}

	private void showGameOver(boolean playerWon) {
		state = GameState.GAME_OVER;
		clearActionUI();
		
		// 게임 정상 종료 - 재플레이 불가
		gameAlreadyPlayed = true;
		
		// 게임 종료 시 저장 상태 초기화
		resetGameState();

		if (playerWon) {
			Sample.INSTANCE.play(Assets.Sounds.LEVELUP);
			
			// 최대 체력 증가
			Dungeon.hero.HT += HP_CHANGE;
			Dungeon.hero.HP = Math.min(Dungeon.hero.HP + HP_CHANGE, Dungeon.hero.HT);

			instructionText = PixelScene.renderTextBlock(
					Messages.get(this, "player_wins"), 7);
			instructionText.hardlight(0x44FF44);
			instructionText.setPos((WIDTH - instructionText.width()) / 2, buttonAreaY);
			add(instructionText);

			Music.INSTANCE.play(Assets.Music.JONATHAN, true);

			RenderedTextBlock rewardText = PixelScene.renderTextBlock(
					Messages.get(this, "hp_up", HP_CHANGE), 6);
			rewardText.hardlight(0xFFFF00);
			rewardText.setPos((WIDTH - rewardText.width()) / 2, buttonAreaY + 12);
			add(rewardText);
		} else {
			Sample.INSTANCE.play(Assets.Sounds.FALLING);
			Music.INSTANCE.play(Assets.Music.TENDENCY3, true);

			// 최대 체력 감소
			Dungeon.hero.HT = Math.max(1, Dungeon.hero.HT - HP_CHANGE);
			Dungeon.hero.HP = Math.min(Dungeon.hero.HP, Dungeon.hero.HT);

			instructionText = PixelScene.renderTextBlock(
					Messages.get(this, "darby_wins"), 7);
			instructionText.hardlight(0xFF4444);
			instructionText.setPos((WIDTH - instructionText.width()) / 2, buttonAreaY);
			add(instructionText);

			RenderedTextBlock lostText = PixelScene.renderTextBlock(
					Messages.get(this, "hp_down", HP_CHANGE), 6);
			lostText.hardlight(0xFF6666);
			lostText.setPos((WIDTH - lostText.width()) / 2, buttonAreaY + 12);
			add(lostText);
		}

		RedButton closeBtn = new RedButton(Messages.get(this, "close")) {
			@Override
			protected void onClick() {
				Music.INSTANCE.play(Assets.Music.EMPO, true);
				hide();
			}
		};
		closeBtn.setRect(10, buttonAreaY + 26, WIDTH - 20, 18);
		add(closeBtn);
	}

	private void clearActionUI() {
		if (instructionText != null) { remove(instructionText); instructionText = null; }
		if (stopButton != null) { remove(stopButton); stopButton = null; }
		if (continueButton != null) { remove(continueButton); continueButton = null; }
		for (int i = 0; i < 5; i++) {
			if (coinButtons[i] != null) { 
				remove(coinButtons[i]); 
				coinButtons[i] = null; 
			}
		}
	}

	@Override
	public void update() {
		super.update();
		
		// 클릭 가드 타이머 감소
		if (clickGuardTimer > 0) {
			clickGuardTimer -= Game.elapsed;
		}

		if (state == GameState.ARROW_MOVING && !arrowsStopped) {
			// 화살표 A 이동 (항상 움직임, 더 빠름)
			updateArrowA();

			// 화살표 B 이동
			updateArrowB();

			// 다비 턴일 때 AI가 화살표 멈추기
			if (!isPlayerTurn && darbyNeedsToStopArrow) {
				darbyActionTimer += Game.elapsed;
				
				if (darbyActionTimer >= darbyStopTargetTime) {
					darbyNeedsToStopArrow = false;
					stopArrows();
				}
			}
		}

		// 다비 턴 처리
		if (state == GameState.DARBY_TURN && darbyNeedsToSelectCoins) {
			darbyThinkTimer += Game.elapsed;
			
			// 1초 후 동전 선택
			if (darbyThinkTimer >= 1f) {
				darbyNeedsToSelectCoins = false;
				darbyCoins = darbySelectCoins();
				selectedCoins = darbyCoins;
				darbyStartArrowPhase();
			}
		}
	}

	private void updateArrowA() {
		if (arrowA == null) return;

		float delta = Game.elapsed * arrowASpeed;

		if (arrowAMovingRight) {
			arrowAPos += delta;
			if (arrowAPos >= arrowAreaWidth) {
				arrowAPos = arrowAreaWidth;
				arrowAMovingRight = false;
			}
		} else {
			arrowAPos -= delta;
			if (arrowAPos <= 0) {
				arrowAPos = 0;
				arrowAMovingRight = true;
			}
		}

		// 픽셀 정렬로 렌더링 문제 방지
		arrowA.x = Math.round(arrowAreaLeft + arrowAPos - 2);
		arrowA.y = Math.round(arrowAreaTop);
	}

	private void updateArrowB() {
		if (arrowB == null) return;

		float delta = Game.elapsed * arrowBSpeed;

		if (arrowBMovingRight) {
			arrowBPos += delta;
			if (arrowBPos >= arrowAreaWidth) {
				arrowBPos = arrowAreaWidth;
				arrowBMovingRight = false;
			}
		} else {
			arrowBPos -= delta;
			if (arrowBPos <= 0) {
				arrowBPos = 0;
				arrowBMovingRight = true;
			}
		}

		// 픽셀 정렬로 렌더링 문제 방지
		arrowB.x = Math.round(arrowAreaLeft + arrowBPos - 2);
		arrowB.y = Math.round(arrowAreaTop + 12);
	}

	@Override
	public void onBackPressed() {
		if (state == GameState.GAME_OVER) {
			super.onBackPressed();
		}
		// 게임 중에는 뒤로가기 비활성화
	}
	
	@Override
	public void destroy() {
		// 창을 닫을 때는 상태만 저장 (기권 아님, 일시정지)
		// 게임 상태는 유지되어 나중에 다시 열면 이어서 할 수 있음
		if (gameInProgress && state != GameState.GAME_OVER) {
			saveGameState();
		}
		super.destroy();
	}
}
