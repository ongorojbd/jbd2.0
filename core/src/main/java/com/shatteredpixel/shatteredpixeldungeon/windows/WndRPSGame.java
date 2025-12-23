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
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Boytwo;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfDivineInspiration;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Spw;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class WndRPSGame extends Window {

	// 반응형 크기
	private int WIDTH;
	private int HEIGHT;

	// 가위(0), 바위(1), 보(2)
	private static final int SCISSORS = 0;
	private static final int ROCK = 1;
	private static final int PAPER = 2;

	// 게임 상태
	private enum GameState {
		SELECTING,      // 선택 중
		SHOWING_NPC,    // NPC 선택 보여주는 중
		RESULT,         // 결과 표시
		GAME_OVER       // 게임 종료
	}

	private GameState state = GameState.SELECTING;

	// === static 변수로 점수 유지 (라운드 간 누적) ===
	private static int savedPlayerWins = 0;
	private static int savedNpcWins = 0;
	private static int savedDraws = 0;
	private static boolean gameInProgress = false;

	private int playerWins = 0;
	private int npcWins = 0;
	private int draws = 0;
	private static final int WINS_TO_WIN = 3;  // 3번 이기면 승리
	private static final int LOSES_TO_LOSE = 3; // 3번 지면 패배

	// 레이아웃 변수
	private float choiceBoxSize;
	private float choiceY;
	private float buttonY;
	private float quitBtnY;

	// UI 요소
	private RenderedTextBlock titleText;
	private RenderedTextBlock scoreText;
	private RenderedTextBlock instructionText;
	private RenderedTextBlock playerChoiceLabel;
	private RenderedTextBlock npcChoiceLabel;
	private RenderedTextBlock resultText;
	private RenderedTextBlock playerChoiceDisplay;
	private RenderedTextBlock npcChoiceDisplay;

	private ColorBlock playerChoiceBox;
	private ColorBlock npcChoiceBox;

	private RedButton scissorsBtn;
	private RedButton rockBtn;
	private RedButton paperBtn;
	private RedButton continueBtn;
	private RedButton quitBtn;

	// 선택 결과
	private int playerChoice = -1;
	private int npcChoice = -1;

	// 애니메이션 타이머
	private float animTimer = 0;
	private float npcShowDelay = 0.3f;  // NPC 선택 공개 딜레이 (빠르게)

	// NPC 참조
	private Boytwo npc;

	public WndRPSGame(Boytwo npc) {
		super();
		this.npc = npc;

		// 화면 방향에 따른 반응형 크기 설정
		boolean landscape = PixelScene.landscape();
		if (landscape) {
			WIDTH = 160;
			HEIGHT = 145;
			choiceBoxSize = 45;
			choiceY = 48;
			buttonY = 100;
			quitBtnY = 125;
		} else {
			WIDTH = 130;
			HEIGHT = 165;
			choiceBoxSize = 40;
			choiceY = 52;
			buttonY = 105;
			quitBtnY = 130;
		}

		resize(WIDTH, HEIGHT);

		// 저장된 점수 복원
		if (gameInProgress) {
			playerWins = savedPlayerWins;
			npcWins = savedNpcWins;
			draws = savedDraws;
		} else {
			playerWins = 0;
			npcWins = 0;
			draws = 0;
			gameInProgress = true;
		}

		setupUI();
	}

	// 게임 상태 저장
	private void saveGameState() {
		savedPlayerWins = playerWins;
		savedNpcWins = npcWins;
		savedDraws = draws;
	}

	// 게임 종료 시 상태 초기화
	public static void resetGameState() {
		savedPlayerWins = 0;
		savedNpcWins = 0;
		savedDraws = 0;
		gameInProgress = false;
	}

	private void setupUI() {
		// 제목
		titleText = PixelScene.renderTextBlock(Messages.get(this, "title"), 9);
		titleText.hardlight(TITLE_COLOR);
		titleText.setPos((WIDTH - titleText.width()) / 2, 2);
		add(titleText);

		// 점수판
		updateScoreText();

		// 안내 텍스트
		instructionText = PixelScene.renderTextBlock(Messages.get(this, "instruction"), 6);
		instructionText.maxWidth(WIDTH - 8);
		instructionText.setPos((WIDTH - instructionText.width()) / 2, 30);
		add(instructionText);

		// === 선택 표시 영역 ===
		// 플레이어 선택 라벨
		playerChoiceLabel = PixelScene.renderTextBlock(Messages.get(this, "you"), 6);
		playerChoiceLabel.setPos(10 + (choiceBoxSize - playerChoiceLabel.width()) / 2, choiceY - 9);
		add(playerChoiceLabel);

		// 플레이어 선택 박스
		playerChoiceBox = new ColorBlock(choiceBoxSize, choiceBoxSize, 0xFF334455);
		playerChoiceBox.x = 10;
		playerChoiceBox.y = choiceY;
		add(playerChoiceBox);

		// VS 텍스트
		RenderedTextBlock vsText = PixelScene.renderTextBlock("VS", 8);
		vsText.hardlight(0xFFFF00);
		vsText.setPos((WIDTH - vsText.width()) / 2, choiceY + choiceBoxSize / 2 - 4);
		add(vsText);

		// NPC 선택 라벨
		npcChoiceLabel = PixelScene.renderTextBlock(Messages.get(this, "npc"), 6);
		npcChoiceLabel.setPos(WIDTH - 10 - choiceBoxSize + (choiceBoxSize - npcChoiceLabel.width()) / 2, choiceY - 9);
		add(npcChoiceLabel);

		// NPC 선택 박스
		npcChoiceBox = new ColorBlock(choiceBoxSize, choiceBoxSize, 0xFF553344);
		npcChoiceBox.x = WIDTH - 10 - choiceBoxSize;
		npcChoiceBox.y = choiceY;
		add(npcChoiceBox);

		// === 버튼 영역 ===
		setupChoiceButtons();
		setupQuitButton();
	}

	private void updateScoreText() {
		if (scoreText != null) {
			remove(scoreText);
		}

		String score = Messages.get(this, "score", playerWins, npcWins, draws);
		scoreText = PixelScene.renderTextBlock(score, 6);
		scoreText.hardlight(0x88CCFF);
		scoreText.setPos((WIDTH - scoreText.width()) / 2, 16);
		add(scoreText);
	}

	private void setupChoiceButtons() {
		float btnWidth = (WIDTH - 30) / 3f;  // 양쪽 여백 10, 버튼 간격 5*2
		float btnHeight = 20;
		float gap = 5;
		float startX = 10;

		// 가위 버튼
		scissorsBtn = new RedButton(Messages.get(this, "scissors")) {
			@Override
			protected void onClick() {
				if (state == GameState.SELECTING) {
					makeChoice(SCISSORS);
				}
			}
		};
		scissorsBtn.setRect(startX, buttonY, btnWidth, btnHeight);
		add(scissorsBtn);

		// 바위 버튼
		rockBtn = new RedButton(Messages.get(this, "rock")) {
			@Override
			protected void onClick() {
				if (state == GameState.SELECTING) {
					makeChoice(ROCK);
				}
			}
		};
		rockBtn.setRect(startX + btnWidth + gap, buttonY, btnWidth, btnHeight);
		add(rockBtn);

		// 보 버튼
		paperBtn = new RedButton(Messages.get(this, "paper")) {
			@Override
			protected void onClick() {
				if (state == GameState.SELECTING) {
					makeChoice(PAPER);
				}
			}
		};
		paperBtn.setRect(startX + (btnWidth + gap) * 2, buttonY, btnWidth, btnHeight);
		add(paperBtn);
	}

	private void setupQuitButton() {
		quitBtn = new RedButton(Messages.get(this, "quit")) {
			@Override
			protected void onClick() {
				if (state == GameState.SELECTING || state == GameState.RESULT) {
					// 그만두기 = 승패 이력 초기화
					resetGameState();
					Dungeon.hero.spendAndNext(1f);
					hide();
				}
			}
		};
		quitBtn.setRect(10, quitBtnY, WIDTH - 20, 16);
		add(quitBtn);
	}

	private void makeChoice(int choice) {
		playerChoice = choice;
		npcChoice = Random.Int(3);  // NPC 랜덤 선택

		state = GameState.SHOWING_NPC;
		animTimer = 0;

		// 버튼 비활성화
		scissorsBtn.enable(false);
		rockBtn.enable(false);
		paperBtn.enable(false);
		quitBtn.enable(false);

		// 플레이어 선택 표시
		showPlayerChoice();

		// 사운드
		Sample.INSTANCE.play(Assets.Sounds.CLICK);

		// 안내 텍스트 - 간단하게만 표시
		instructionText.text("...");
		instructionText.setPos((WIDTH - instructionText.width()) / 2, 30);
	}

	private void showPlayerChoice() {
		// 기존 표시 제거
		if (playerChoiceDisplay != null) {
			remove(playerChoiceDisplay);
		}

		// 플레이어 선택 박스 위에 텍스트 표시
		String choiceName = getChoiceName(playerChoice);

		playerChoiceDisplay = PixelScene.renderTextBlock(choiceName, 8);
		playerChoiceDisplay.setPos(
				playerChoiceBox.x + (choiceBoxSize - playerChoiceDisplay.width()) / 2,
				playerChoiceBox.y + (choiceBoxSize - playerChoiceDisplay.height()) / 2
		);
		add(playerChoiceDisplay);

		// 박스 색상 변경
		playerChoiceBox.hardlight(getChoiceColor(playerChoice));
	}

	private void showNpcChoice() {
		// 기존 표시 제거
		if (npcChoiceDisplay != null) {
			remove(npcChoiceDisplay);
		}

		// NPC 선택 박스 위에 텍스트 표시
		String choiceName = getChoiceName(npcChoice);

		npcChoiceDisplay = PixelScene.renderTextBlock(choiceName, 8);
		npcChoiceDisplay.setPos(
				npcChoiceBox.x + (choiceBoxSize - npcChoiceDisplay.width()) / 2,
				npcChoiceBox.y + (choiceBoxSize - npcChoiceDisplay.height()) / 2
		);
		add(npcChoiceDisplay);

		// 박스 색상 변경
		npcChoiceBox.hardlight(getChoiceColor(npcChoice));

		// 사운드
		Sample.INSTANCE.play(Assets.Sounds.PUFF);
	}

	private String getChoiceName(int choice) {
		switch (choice) {
			case SCISSORS: return Messages.get(this, "scissors");
			case ROCK: return Messages.get(this, "rock");
			case PAPER: return Messages.get(this, "paper");
			default: return "?";
		}
	}

	private int getChoiceColor(int choice) {
		switch (choice) {
			case SCISSORS: return 0xFFFF4444;  // 빨강
			case ROCK: return 0xFF888888;      // 회색
			case PAPER: return 0xFF4488FF;     // 파랑
			default: return 0xFFFFFFFF;
		}
	}

	private void calculateResult() {
		state = GameState.RESULT;

		// 승패 판정 (수정됨)
		// 가위(0) > 보(2)
		// 바위(1) > 가위(0)
		// 보(2) > 바위(1)
		// 즉, (npc + 1) % 3 == player → 플레이어 승리

		int result; // 0: 무승부, 1: 플레이어 승, -1: 플레이어 패
		if (playerChoice == npcChoice) {
			result = 0;
			draws++;
		} else if ((npcChoice + 1) % 3 == playerChoice) {
			// NPC가 낸 것의 다음 것이 플레이어 → 플레이어 승리
			result = 1;
			playerWins++;
		} else {
			result = -1;
			npcWins++;
		}

		// 점수 저장
		saveGameState();

		// 점수 업데이트
		updateScoreText();

		// 안내 텍스트 → 결과로 변경
		String resultMsg;
		int resultColor;
		if (result == 0) {
			resultMsg = Messages.get(this, "draw");
			resultColor = 0xFFFF00;
			Sample.INSTANCE.play(Assets.Sounds.ITEM);
		} else if (result == 1) {
			resultMsg = Messages.get(this, "win");
			resultColor = 0x44FF44;
			Sample.INSTANCE.play(Assets.Sounds.BADGE);
		} else {
			resultMsg = Messages.get(this, "lose");
			resultColor = 0xFF4444;
			Sample.INSTANCE.play(Assets.Sounds.BLAST);
			// 패배 시 즉시 체력 감소
			applyLossPenalty();
		}

		instructionText.text(resultMsg);
		instructionText.hardlight(resultColor);
		instructionText.setPos((WIDTH - instructionText.width()) / 2, 30);

		// 게임 종료 체크
		if (playerWins >= WINS_TO_WIN) {
			// 플레이어 최종 승리
			showGameOver(true);
		} else if (npcWins >= LOSES_TO_LOSE) {
			// 플레이어 최종 패배
			showGameOver(false);
		} else {
			// 계속 버튼 표시
			showContinueButton();
		}
	}

	private void showContinueButton() {
		// 그만두기 버튼 숨기기
		if (quitBtn != null) {
			quitBtn.visible = false;
			quitBtn.active = false;
		}

		continueBtn = new RedButton(Messages.get(this, "next_round")) {
			@Override
			protected void onClick() {
				nextRound();
			}
		};
		continueBtn.setRect(10, quitBtnY, WIDTH - 20, 16);
		add(continueBtn);
	}

	private void nextRound() {
		// 저장된 상태로 새 창 열기
		saveGameState();
		hide();
		GameScene.show(new WndRPSGame(npc));
	}

	private void applyLossPenalty() {
		// 패배 페널티: 현재 체력의 1/3 피해
		int damage = Math.max(5, Dungeon.hero.HP / 3);
		Dungeon.hero.damage(damage, npc);
		Sample.INSTANCE.play(Assets.Sounds.HIT);

		if (!Dungeon.hero.isAlive()) {
			Dungeon.fail(Boytwo.class);
		}
	}

	private void showGameOver(boolean playerWon) {
		state = GameState.GAME_OVER;

		// 그만두기 버튼 숨기기
		if (quitBtn != null) {
			quitBtn.visible = false;
			quitBtn.active = false;
		}

		// 기존 버튼들 숨기기
		scissorsBtn.visible = false;
		scissorsBtn.active = false;
		rockBtn.visible = false;
		rockBtn.active = false;
		paperBtn.visible = false;
		paperBtn.active = false;

		if (playerWon) {
			// 승리 보상
			Sample.INSTANCE.play(Assets.Sounds.TALE);

			instructionText.text(Messages.get(this, "victory"));
			instructionText.hardlight(0x44FF44);
			instructionText.setPos((WIDTH - instructionText.width()) / 2, 30);

			resultText = PixelScene.renderTextBlock(Messages.get(this, "reward"), 6);
			resultText.hardlight(0xFFFF00);
			resultText.setPos((WIDTH - resultText.width()) / 2, buttonY);
			add(resultText);
		} else {
			instructionText.text(Messages.get(this, "defeat"));
			instructionText.hardlight(0xFF4444);
			instructionText.setPos((WIDTH - instructionText.width()) / 2, 30);

			resultText = PixelScene.renderTextBlock(Messages.get(this, "penalty_total"), 6);
			resultText.hardlight(0xFF6666);
			resultText.setPos((WIDTH - resultText.width()) / 2, buttonY);
			add(resultText);
		}

		// 종료 버튼
		continueBtn = new RedButton(Messages.get(this, "close")) {
			@Override
			protected void onClick() {
				// 결과 적용
				if (playerWins >= WINS_TO_WIN) {
					// 승리 보상: 아이템 드롭 후 NPC 사라짐
					Dungeon.level.drop(new PotionOfDivineInspiration().identify(), npc.pos).sprite.drop(npc.pos);
					Dungeon.level.drop(new Gold(Random.IntRange(100, 300)), npc.pos).sprite.drop(npc.pos);
					GLog.n(Messages.get(Boytwo.class, "defeated"));
					npc.die(null);
				}
				// 패배 시에는 이미 매 라운드마다 체력이 깎였으므로 추가 처리 없음
				resetGameState();
				Dungeon.hero.spendAndNext(1f);
				hide();
			}
		};
		continueBtn.setRect(10, quitBtnY, WIDTH - 20, 16);
		add(continueBtn);
	}

	@Override
	public void update() {
		super.update();

		if (state == GameState.SHOWING_NPC) {
			animTimer += Game.elapsed;

			if (animTimer >= npcShowDelay) {
				showNpcChoice();
				calculateResult();
			}
		}
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
		// 게임 도중 창이 닫히면 상태 저장
		if (gameInProgress && state != GameState.GAME_OVER) {
			saveGameState();
		}
		super.destroy();
	}
}
