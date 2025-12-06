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
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.Camera;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class WndRebelTimingGame extends Window {

	// 현재 열려있는 인스턴스 추적 (중복 창 방지)
	public static WndRebelTimingGame instance = null;

	// 창 크기
	private int WIDTH;
	private int HEIGHT;

	// 막대 관련 상수
	private float barWidth;
	private float barHeight = 20;
	private float barLeft;
	private float barTop;

	// 안전 구역 (초록색) - 막대 전체 대비 비율
	private float safeZoneRatio = 0.15f; // 15% 크기
	private float safeZoneCenter; // 안전 구역 중앙 위치 (0~1 비율)

	// 화살표 관련
	private float arrowPos = 0; // 0 ~ barWidth
	private boolean arrowMovingRight = true;
	private float arrowSpeed = 160f; // 기본 속도
	private boolean arrowStopped = false;

	// UI 요소
	private RenderedTextBlock titleText;
	private RenderedTextBlock instructionText;
	private RenderedTextBlock resultText;

	private ColorBlock barBackground; // 붉은색 막대 (위험 구역)
	private ColorBlock safeZone;      // 초록색 막대 (안전 구역)
	private ColorBlock arrow;          // 화살표 (마커)

	private RedButton stopButton;
	private RedButton closeButton;

	// 게임 상태
	private enum GameState {
		PLAYING,    // 화살표 이동 중
		STOPPED,    // 화살표 정지, 결과 표시
		FINISHED    // 완료
	}

	private GameState state = GameState.PLAYING;

	// 콜백 (결과 전달용)
	private Callback onSuccess;
	private Callback onFail;

	// 난이도 조절용
	private int difficulty; // Rebel의 Phase에 따라 조절

	// 버튼 딜레이 관련
	private float buttonDelayTimer = 0;
	private static final float BUTTON_DELAY = 1.5f; // 1.5초 딜레이
	private boolean buttonReady = false;

	public WndRebelTimingGame(int difficulty, Callback onSuccess, Callback onFail) {
		super();

		instance = this; // 인스턴스 등록

		this.difficulty = difficulty;
		this.onSuccess = onSuccess;
		this.onFail = onFail;

		// 난이도에 따른 설정 조절
		adjustDifficulty();

		// 화면 방향에 따른 크기 조절
		boolean landscape = PixelScene.landscape();

		if (landscape) {
			WIDTH = 160;
			HEIGHT = 100;
			barWidth = 140;
			barLeft = 10;
			barTop = 35;
		} else {
			WIDTH = 120;
			HEIGHT = 110;
			barWidth = 100;
			barLeft = 10;
			barTop = 40;
			// 세로 모드에서는 막대가 좁아서 어려우므로 속도를 약간 줄임 (약 20% 감소)
			arrowSpeed *= 0.8f;
		}

		resize(WIDTH, HEIGHT);

		// 안전 구역 위치 랜덤 설정 (가장자리 피함)
		safeZoneCenter = 0.2f + Random.Float(0.6f);

		// 화살표 시작 위치 랜덤
		arrowPos = Random.Float(barWidth * 0.1f, barWidth * 0.9f);
		arrowMovingRight = Random.Int(2) == 0;

		setupUI();
	}

	private void adjustDifficulty() {
		// Phase에 따라 난이도 조절
		switch (difficulty) {
			case 0:
			case 1:
				safeZoneRatio = 0.18f; // 18% - 쉬움
				arrowSpeed = 150f;
				break;
			case 2:
			case 3:
				safeZoneRatio = 0.15f; // 15% - 보통
				arrowSpeed = 180f;
				break;
			case 4:
				safeZoneRatio = 0.10f; // 12% - 어려움
				arrowSpeed = 210f;
				break;
			case 5:
			default:
				safeZoneRatio = 0.10f; // 10% - 매우 어려움
				arrowSpeed = 240f;
				break;
		}
	}

	private void setupUI() {
		// 제목
		titleText = PixelScene.renderTextBlock(Messages.get(this, "title"), 9);
		titleText.hardlight(TITLE_COLOR);
		titleText.setPos((WIDTH - titleText.width()) / 2, 3);
		add(titleText);

		// 안내 텍스트
		instructionText = PixelScene.renderTextBlock(Messages.get(this, "instruction"), 6);
		instructionText.maxWidth(WIDTH - 10);
		instructionText.setPos((WIDTH - instructionText.width()) / 2, 16);
		add(instructionText);

		// === 막대 영역 ===

		// 붉은색 막대 배경 (위험 구역)
		barBackground = new ColorBlock(barWidth, barHeight, 0xFFCC3333);
		barBackground.x = barLeft;
		barBackground.y = barTop;
		add(barBackground);

		// 초록색 안전 구역
		float safeWidth = barWidth * safeZoneRatio;
		float safeLeft = barLeft + (barWidth * safeZoneCenter) - (safeWidth / 2);
		// 범위 제한 (막대 밖으로 나가지 않도록)
		safeLeft = Math.max(barLeft, Math.min(safeLeft, barLeft + barWidth - safeWidth));

		safeZone = new ColorBlock(safeWidth, barHeight, 0xFF33CC33);
		safeZone.x = safeLeft;
		safeZone.y = barTop;
		add(safeZone);

		// 화살표 (마커)
		arrow = new ColorBlock(4, barHeight + 6, 0xFFFFFF00);
		arrow.x = barLeft + arrowPos - 2;
		arrow.y = barTop - 3;
		add(arrow);

		// STOP 버튼 (처음에는 비활성화, 딜레이 후 활성화)
		float buttonY = barTop + barHeight + 12;
		stopButton = new RedButton(Messages.get(this, "stop")) {
			@Override
			protected void onClick() {
				if (state == GameState.PLAYING && !arrowStopped && buttonReady) {
					stopArrow();
				}
			}
		};
		stopButton.setRect(10, buttonY, WIDTH - 20, 20);
		stopButton.enable(false); // 처음에는 비활성화
		stopButton.alpha(0.3f);   // 시각적으로 비활성화 표시
		add(stopButton);
	}

	private void stopArrow() {
		arrowStopped = true;
		state = GameState.STOPPED;
		Sample.INSTANCE.play(Assets.Sounds.HIT);

		stopButton.enable(false);

		// 결과 계산
		calculateResult();
	}

	private void calculateResult() {
		// 안전 구역 범위 계산
		float safeWidth = barWidth * safeZoneRatio;
		float safeStart = (barWidth * safeZoneCenter) - (safeWidth / 2);
		safeStart = Math.max(0, Math.min(safeStart, barWidth - safeWidth));
		float safeEnd = safeStart + safeWidth;

		boolean success = (arrowPos >= safeStart && arrowPos <= safeEnd);

		// 결과가 결정되면 즉시 FINISHED 상태로 전환하고 콜백 호출
		// 강제 종료로 꼼수를 부릴 수 없도록 함
		state = GameState.FINISHED;
		
		if (success) {
			// 성공! 초록색 범위에 멈춤
			Sample.INSTANCE.play(Assets.Sounds.HIT_PARRY);
			Camera.main.shake(2, 0.3f);

			resultText = PixelScene.renderTextBlock(Messages.get(this, "success"), 7);
			resultText.hardlight(0x44FF44);
			
			// 즉시 성공 콜백 호출
			if (onSuccess != null) {
				onSuccess.call();
				onSuccess = null; // 중복 호출 방지
			}
		} else {
			// 실패! 붉은색 범위에 멈춤
			Sample.INSTANCE.play(Assets.Sounds.HIT_STRONG);
			Camera.main.shake(8, 0.8f);
			GameScene.flash(0xFF0000);

			resultText = PixelScene.renderTextBlock(Messages.get(this, "fail"), 7);
			resultText.hardlight(0xFF4444);
			
			// 즉시 실패 콜백 호출 (피해 적용)
			if (onFail != null) {
				onFail.call();
				onFail = null; // 중복 호출 방지
			}
		}

		resultText.setPos((WIDTH - resultText.width()) / 2, barTop + barHeight + 8);
		add(resultText);

		// 기존 STOP 버튼 제거하고 닫기 버튼 표시
		stopButton.visible = false;
		stopButton.active = false;

		float buttonY = barTop + barHeight + 25;
		closeButton = new RedButton(Messages.get(this, "close")) {
			@Override
			protected void onClick() {
				hide(); // 단순히 창만 닫음
			}
		};
		closeButton.setRect(10, buttonY, WIDTH - 20, 18);
		add(closeButton);
	}

	@Override
	public void update() {
		super.update();

		// 버튼 딜레이 처리
		if (!buttonReady && state == GameState.PLAYING) {
			buttonDelayTimer += Game.elapsed;
			if (buttonDelayTimer >= BUTTON_DELAY) {
				buttonReady = true;
				if (stopButton != null) {
					stopButton.enable(true);
					stopButton.alpha(1f);
				}
			}
		}

		if (state == GameState.PLAYING && !arrowStopped) {
			// 화살표 이동
			float delta = Game.elapsed * arrowSpeed;

			if (arrowMovingRight) {
				arrowPos += delta;
				if (arrowPos >= barWidth) {
					arrowPos = barWidth;
					arrowMovingRight = false;
				}
			} else {
				arrowPos -= delta;
				if (arrowPos <= 0) {
					arrowPos = 0;
					arrowMovingRight = true;
				}
			}

			// 화살표 위치 업데이트
			if (arrow != null) {
				arrow.x = Math.round(barLeft + arrowPos - 2);
			}
		}
	}

	@Override
	public void onBackPressed() {
		// 게임 중에는 뒤로가기/창 바깥 클릭 완전 비활성화
		// 게임이 완료된 후에만 닫을 수 있음
		if (state == GameState.FINISHED) {
			super.onBackPressed();
		}
		// 게임 중에는 아무것도 하지 않음 (창 닫히지 않음)
	}

	@Override
	public void destroy() {
		// 인스턴스 해제
		if (instance == this) {
			instance = null;
		}
		
		// 게임이 완료되지 않은 상태에서 창이 닫히면
		// Rebel.act()에서 timingGameActive 플래그를 확인하여 창을 다시 표시하므로
		// 여기서는 다시 표시하지 않음 (중복 방지)
		
		super.destroy();
	}
}

