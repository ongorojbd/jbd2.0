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
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TuskEquipmentDisc;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Sword;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class WndTuskAiming extends Window {

	// 반응형 크기 설정
	private int WIDTH;
	private int HEIGHT;

	// 게임 상태
	private enum GameState {
		AIMING,
		COMPLETE
	}

	private GameState state = GameState.AIMING;

	// 타이밍 바 관련
	private float barWidth;
	private float barHeight;
	private float barLeft;
	private float barTop;

	// 영역 비율
	// [빨강|주황|노랑|초록|노랑|주황|빨강]
	// 빨강 > 주황 > 노랑 > 초록, 합계 1.0
	private static final float RED_RATIO    = 0.185f;  // 양쪽 각각 18.5%
	private static final float ORANGE_RATIO = 0.145f;  // 양쪽 각각 14.5%
	private static final float YELLOW_RATIO = 0.13f;   // 양쪽 각각 13%
	private static final float GREEN_RATIO  = 0.08f;   // 중앙 8%

	// 색상
	private static final int COLOR_RED    = 0xFFCC3333;
	private static final int COLOR_ORANGE = 0xFFDD7733;
	private static final int COLOR_YELLOW = 0xFFDDCC33;
	private static final int COLOR_GREEN  = 0xFF33BB55;
	private static final int COLOR_BORDER = 0xFF222222;
	private static final int COLOR_MARKER = 0xFFFF88CC;
	private static final int COLOR_BAR_HIGHLIGHT = 0x22FFFFFF;

	// 데미지 계산을 위한 히트 타입 (비율 대신 고정 데미지 사용)
	private static final String HIT_TYPE_PERFECT = "perfect";
	private static final String HIT_TYPE_GREAT = "great";
	private static final String HIT_TYPE_GOOD = "good";
	private static final String HIT_TYPE_MISS = "miss";

	// 마커 관련
	private float markerPos;
	private float markerSpeed;
	// 한 번 왕복하는데 걸리는 시간 (초 단위) - 0.5f, 0.6f, 0.7f 중 무작위
	private float baseCycleTime;
	private boolean markerMovingRight = true;

	// 2발 모드 관련
	private int totalShots;
	private int currentShot = 0;
	private ArrayList<Float> damageResults = new ArrayList<>();

	// UI 요소
	private ColorBlock barBackground;
	private ColorBlock barHighlight;
	private ColorBlock barRedLeft, barOrangeLeft, barYellowLeft, barGreen, barYellowRight, barOrangeRight, barRedRight;
	private ColorBlock markerMain, markerTop, markerBottom;

	private RenderedTextBlock titleText;
	private RenderedTextBlock shotCountText;

	private RedButton fireButton;

	// shotCount Y 위치 캐시
	private float shotCountY;

	private TuskEquipmentDisc artifact;
	private Char target;

	public WndTuskAiming(TuskEquipmentDisc artifact, Char target) {
		super();

		// blocker는 유지하여 바깥 클릭 시 취소 가능하도록 함

		this.artifact = artifact;
		this.target = target;

		totalShots = artifact.isUpgraded() ? 2 : 1;

		boolean landscape = PixelScene.landscape();
		float uiW = PixelScene.uiCamera.width;
		float uiH = PixelScene.uiCamera.height;

		// 크롬 여유를 두되, X는 꽤 넓게
		int maxWidth  = (int)(uiW * 0.8f);
		int maxHeight = (int)(uiH * 0.75f);

		float targetWidth, targetHeight;

		if (landscape) {
			targetWidth  = uiW * 0.31625f; // 가로 모드에서 UI 너비 15% 더 넓힘 (0.275 * 1.15 = 0.31625)
			targetHeight = uiH * 0.24f; // 가로 모드에서 UI 크기 줄임
		} else {
			// 모바일 세로 화면 > 더 낮게, 가로는 넓게
			targetWidth  = uiW * 0.94f;
			targetHeight = uiH * 0.22f;  // 🔥 세로 높이 추가로 더 줄임
		}

		WIDTH  = Math.min((int)targetWidth,  maxWidth);
		HEIGHT = Math.min((int)targetHeight, maxHeight);

		if (!landscape) {
			// 세로 모드에서 가로가 확실히 더 길게
			if (WIDTH < HEIGHT * 1.3f) {
				WIDTH = (int)(HEIGHT * 1.3f);
			}
		}

		// 최소 크기
		WIDTH  = Math.max(WIDTH,  110);
		HEIGHT = Math.max(HEIGHT,  64);  // 🔥 더 얇게

		// 조준 바 X축 크기 설정
		if (landscape) {
			// 가로 모드: 조준 바를 더 넓게 (여백을 더 줄여서)
			barWidth = WIDTH - 6; // 8에서 6으로 줄여서 더 넓게
		} else {
			// 세로 모드에서는 최대화 (좌우 여백 6px 정도만)
			barWidth = WIDTH - 12;
		}
		if (barWidth < 60) barWidth = 60;

		// 마커 속도를 시간 기반으로 계산 (한 번 왕복하는데 baseCycleTime초 소요)
		// 왕복 거리 = barWidth * 2
		// TuskEquipmentDisc에서 저장된 속도 가져오기 (처음 한 번만 랜덤 결정)
		baseCycleTime = artifact.getAimingCycleTime();
		float cycleTime = baseCycleTime;
		
		// provoked_anger talent에 따른 조준 속도 감소 (사이클 타임 증가)
		Hero hero = Dungeon.hero;
		if (hero != null && hero.hasTalent(Talent.J13)) {
			int talentLevel = hero.pointsInTalent(Talent.J13);
			if (talentLevel >= 1) {
                float speedMultiplier = 1.0f - (0.075f * talentLevel);
				cycleTime /= speedMultiplier; // 속도가 느려지면 사이클 타임이 증가
			}
		}
		
		// TacticalScope 버프에 따른 조준 속도 35% 감소
		if (hero != null && hero.buff(com.shatteredpixel.shatteredpixeldungeon.actors.buffs.TacticalScope.ScopeActive.class) != null) {
			cycleTime /= 0.65f; // 속도를 35% 감소
		}
		
		// 속도 = 거리 / 시간
		markerSpeed = (barWidth * 2) / cycleTime;

		resize(WIDTH, HEIGHT);

		setupUI();
		startAiming();
	}


	private void setupUI() {
		boolean landscape = PixelScene.landscape();
		// 세로 모드에서 타이틀을 더 위로 올리기 위해 padding 조정
		float padding = landscape ? 6 : 3;
		float y = padding;

		// 제목
		titleText = PixelScene.renderTextBlock(Messages.get(this, "title"), 8);
		titleText.hardlight(TITLE_COLOR);
		titleText.setPos((WIDTH - titleText.width()) / 2f, y);
		add(titleText);

		y += titleText.height() + 2;

		// 샷 카운트
		if (totalShots > 1) {
			shotCountY = y;
			updateShotCountText();
			y += shotCountText.height() + 2;
		}

		// 이제 instruction 텍스트는 없음 > 바로 바 영역 계산

		// 하단 버튼 위치 계산
		float buttonHeight = landscape ? 16 : 18; // 가로 모드에서 버튼 높이 살짝 늘림 (14 -> 16)
		float buttonBottomMargin = landscape ? 4 : 6; // 가로 모드에서 여백 줄임
		float buttonTop = HEIGHT - (buttonHeight + buttonBottomMargin);

		// 바 + 여백 가능한 범위 (세로 모드에서 여백 줄임)
		float spacing = landscape ? 4 : 2;
		float availableHeight = buttonTop - y - spacing;
		if (availableHeight < 16) availableHeight = 16;

		// 기본 바 높이 계산
		barHeight = availableHeight * 0.55f; // 0.5f에서 0.55f로 증가

		if (barHeight < 14f) barHeight = 14f; // 최소 12f에서 14f로 증가
		if (barHeight > 28f) barHeight = 28f; // 최대 24f에서 28f로 증가

		// 모바일 세로일 경우 더 얇게
		if (!PixelScene.landscape()) {
			barHeight *= 0.9f; // 0.85f에서 0.9f로 증가 (덜 줄임)
			if (barHeight < 12f) barHeight = 12f; // 최소 10f에서 12f로 증가
		}

		// 바 배치 (가운데 정렬)
		barTop = y + (availableHeight - barHeight) / 2f;
		barLeft = (WIDTH - barWidth) / 2f;

		createTimingBar();

		// 버튼
		float buttonWidth = landscape ? WIDTH - 12 : WIDTH - 20; // 가로 모드에서 버튼 너비 더 넓게 (20에서 12로)
		fireButton = new RedButton(Messages.get(this, "fire")) {
			@Override
			protected void onClick() {
				if (state == GameState.AIMING) {
					fire();
				}
			}
		};
		fireButton.setRect((WIDTH - buttonWidth) / 2f, buttonTop, buttonWidth, buttonHeight);
		add(fireButton);
	}

	private void updateShotCountText() {
		if (shotCountText != null) {
			shotCountText.destroy();
			remove(shotCountText);
		}
		shotCountText = PixelScene.renderTextBlock(
				Messages.get(this, "shot_count", currentShot + 1, totalShots), 7);
		shotCountText.hardlight(0xFFDD44);
		shotCountText.setPos((WIDTH - shotCountText.width()) / 2f, shotCountY);
		add(shotCountText);
	}

	private void createTimingBar() {
		float x = barLeft;
		int border = 2;

		barBackground = new ColorBlock(barWidth + border * 2, barHeight + border * 2, COLOR_BORDER);
		barBackground.x = barLeft - border;
		barBackground.y = barTop - border;
		add(barBackground);

		barHighlight = new ColorBlock(barWidth, 1, COLOR_BAR_HIGHLIGHT);
		barHighlight.x = barLeft;
		barHighlight.y = barTop + 1;
		add(barHighlight);

		// 영역 생성
		float redW    = barWidth * RED_RATIO;
		float orangeW = barWidth * ORANGE_RATIO;
		float yellowW = barWidth * YELLOW_RATIO;
		float greenW  = barWidth * GREEN_RATIO;

		barRedLeft = new ColorBlock(redW, barHeight, COLOR_RED);
		barRedLeft.x = x;
		barRedLeft.y = barTop;
		add(barRedLeft);
		x += redW;

		barOrangeLeft = new ColorBlock(orangeW, barHeight, COLOR_ORANGE);
		barOrangeLeft.x = x;
		barOrangeLeft.y = barTop;
		add(barOrangeLeft);
		x += orangeW;

		barYellowLeft = new ColorBlock(yellowW, barHeight, COLOR_YELLOW);
		barYellowLeft.x = x;
		barYellowLeft.y = barTop;
		add(barYellowLeft);
		x += yellowW;

		barGreen = new ColorBlock(greenW, barHeight, COLOR_GREEN);
		barGreen.x = x;
		barGreen.y = barTop;
		add(barGreen);
		x += greenW;

		barYellowRight = new ColorBlock(yellowW, barHeight, COLOR_YELLOW);
		barYellowRight.x = x;
		barYellowRight.y = barTop;
		add(barYellowRight);
		x += yellowW;

		barOrangeRight = new ColorBlock(orangeW, barHeight, COLOR_ORANGE);
		barOrangeRight.x = x;
		barOrangeRight.y = barTop;
		add(barOrangeRight);
		x += orangeW;

		barRedRight = new ColorBlock(redW, barHeight, COLOR_RED);
		barRedRight.x = x;
		barRedRight.y = barTop;
		add(barRedRight);

		float markerH = barHeight + (PixelScene.landscape() ? 10 : 8);
		markerMain = new ColorBlock(4, markerH, COLOR_MARKER);
		add(markerMain);

		float arrowH = PixelScene.landscape() ? 4 : 3;
		float arrowW = 8;

		markerTop = new ColorBlock(arrowW, arrowH, COLOR_MARKER);
		add(markerTop);
		markerBottom = new ColorBlock(arrowW, arrowH, COLOR_MARKER);
		add(markerBottom);
	}

	private void startAiming() {
		state = GameState.AIMING;

		markerPos = Random.Float(barWidth * 0.1f, barWidth * 0.9f);
		markerMovingRight = Random.Int(2) == 0;

		updateMarkerPosition();
		fireButton.enable(true);

		if (totalShots > 1) updateShotCountText();
	}

	private void updateMarkerPosition() {
		float cx = barLeft + markerPos;

		markerMain.x = cx - markerMain.width() / 2f;
		markerMain.y = barTop - (markerMain.height() - barHeight) / 2f;

		markerTop.x = cx - markerTop.width() / 2f;
		markerTop.y = barTop - markerTop.height() - 2;

		markerBottom.x = cx - markerBottom.width() / 2f;
		markerBottom.y = barTop + barHeight + 2;
	}

	private void fire() {
		fireButton.enable(false);
		Sample.INSTANCE.play(Assets.Sounds.HIT);

		// 판정
		float ratio = markerPos / barWidth;
		String hitType;

		float gStart = RED_RATIO + ORANGE_RATIO + YELLOW_RATIO;
		float gEnd   = gStart + GREEN_RATIO;
		float yStart = RED_RATIO + ORANGE_RATIO;
		float yEnd   = 1f - RED_RATIO - ORANGE_RATIO;
		float oStart = RED_RATIO;
		float oEnd   = 1f - RED_RATIO;

		if (ratio >= gStart && ratio <= gEnd) {
			hitType = HIT_TYPE_PERFECT;
            Sword.tp();
		}
		else if ((ratio >= yStart && ratio < gStart) || (ratio > gEnd && ratio <= yEnd)) {
			hitType = HIT_TYPE_GREAT;
		}
		else if ((ratio >= oStart && ratio < yStart) || (ratio > yEnd && ratio <= oEnd)) {
			hitType = HIT_TYPE_GOOD;
		}
		else {
			hitType = HIT_TYPE_MISS;
		}

		// 고정 데미지 계산 (강화 수치 기반)
		int damage = artifact.calculateDamage(hitType, target);
		float dmgRatio = hitType.equals(HIT_TYPE_PERFECT) ? 0.50f : 
		                hitType.equals(HIT_TYPE_GREAT) ? 0.30f :
		                hitType.equals(HIT_TYPE_GOOD) ? 0.15f : 0f;
		
		damageResults.add(dmgRatio);
		currentShot++;

		boolean alive = artifact.onSingleShotComplete(target, dmgRatio, hitType, damage);

		// 각 발사 완료 후 관통된 적들 처리 (레벨 3 이상일 때)
		if (artifact.level() >= 3) {
			artifact.processPiercedTargets();
		}

		if (currentShot < totalShots && alive) {
			startAiming();
		} else {
			complete();
		}
	}

	private void complete() {
		state = GameState.COMPLETE;
		
		// TacticalScope 버프 제거 및 쿨타임 시작
		Hero hero = Dungeon.hero;
		if (hero != null) {
			com.shatteredpixel.shatteredpixeldungeon.actors.buffs.TacticalScope.ScopeActive scopeBuff = 
				hero.buff(com.shatteredpixel.shatteredpixeldungeon.actors.buffs.TacticalScope.ScopeActive.class);
			if (scopeBuff != null) {
				scopeBuff.detach();
				// 발사 완료 후 쿨타임 시작
				com.shatteredpixel.shatteredpixeldungeon.actors.buffs.TacticalScope scope = 
					hero.buff(com.shatteredpixel.shatteredpixeldungeon.actors.buffs.TacticalScope.class);
				if (scope != null) {
					scope.resetCharge();
				}
			}
		}
		
		// 창을 먼저 닫아서 즉시 게임 화면으로 돌아가도록 함
		hide();
		// 창이 닫힌 후 게임 로직 처리
		artifact.onAllShotsComplete();
	}

	@Override
	public void update() {
		super.update();

		if (state == GameState.AIMING) {
			float delta = Game.elapsed * markerSpeed;

			if (markerMovingRight) {
				markerPos += delta;
				if (markerPos >= barWidth) {
					markerPos = barWidth;
					markerMovingRight = false;
				}
			} else {
				markerPos -= delta;
				if (markerPos <= 0) {
					markerPos = 0;
					markerMovingRight = true;
				}
			}

			updateMarkerPosition();
		}
	}

	@Override
	public void onBackPressed() {
		if (state == GameState.COMPLETE) {
			super.onBackPressed();
		} else {
			// 조준 중 취소 처리
			cancel();
		}
	}
	
	private void cancel() {
		// 조준 취소 처리
		fireButton.enable(false);
		
		// TacticalScope 버프 제거 및 쿨타임 시작
		Hero hero = Dungeon.hero;
		if (hero != null) {
			com.shatteredpixel.shatteredpixeldungeon.actors.buffs.TacticalScope.ScopeActive scopeBuff = 
				hero.buff(com.shatteredpixel.shatteredpixeldungeon.actors.buffs.TacticalScope.ScopeActive.class);
			if (scopeBuff != null) {
				scopeBuff.detach();
				com.shatteredpixel.shatteredpixeldungeon.actors.buffs.TacticalScope scope = 
					hero.buff(com.shatteredpixel.shatteredpixeldungeon.actors.buffs.TacticalScope.class);
				if (scope != null) {
					scope.resetCharge();
				}
			}
		}
		
		// 창을 먼저 닫아서 즉시 게임 화면으로 돌아가도록 함
		hide();
		// 취소 시에도 상태 리셋 (버그 방지: isFirstShot 리셋)
		// 발사가 있었으면 완료 처리 (턴 소모 포함), 없었으면 상태만 리셋
		if (currentShot > 0) {
			// 이미 발사가 있었으면 완료 처리 (턴 소모 포함)
			artifact.onAllShotsComplete();
		} else {
			// 발사가 없었으면 상태만 리셋 (턴 소모 없음)
			artifact.resetShotState();
		}
	}
}
