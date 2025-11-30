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
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Utest;
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

import java.util.ArrayList;
import java.util.Iterator;

/**
 * 모바일 최적화 전투 시스템 윈도우
 * - 공격 턴: 타이밍을 맞춰서 적에게 피해
 * - 방어 턴: 4방향 버튼으로 하트를 이동하여 탄막 피하기
 */
public class WndUndertaleGame extends Window {

	private static final int WIDTH = 140;
	private static final int HEIGHT = 200; // 높이 증가로 공간 확보

	// 전투 상태
	private enum BattleState {
		INTRO,          // 전투 시작
		PLAYER_TURN,    // 플레이어 공격 턴 (타이밍 게임)
		ENEMY_TURN,     // 적 공격 턴 (탄막 피하기)
		TURN_RESULT,    // 턴 결과 표시
		BATTLE_END      // 전투 종료
	}

	private BattleState battleState = BattleState.INTRO;
	private boolean arrowStopped = false;

	// 전투 참가자
	private Utest enemy;
	private Callback onVictory;

	// HP
	private int playerHP;
	private int playerMaxHP;
	private int enemyHP;
	private int enemyMaxHP;

	// UI 요소 - 고정
	private RenderedTextBlock titleText;
	private RenderedTextBlock playerHPText;
	private RenderedTextBlock enemyHPText;
	private ColorBlock playerHPBar;
	private ColorBlock playerHPBarBg;
	private ColorBlock enemyHPBar;
	private ColorBlock enemyHPBarBg;

	// UI 요소 - 동적
	private RenderedTextBlock infoText;
	private RedButton actionButton;

	// === 공격 턴 (타이밍 게임) ===
	private ColorBlock attackBarRed;
	private ColorBlock attackBarOrange;
	private ColorBlock attackBarYellow;
	private ColorBlock attackBarGreen;
	private ColorBlock attackArrow;

	private float attackBarWidth = 110;
	private float attackBarHeight = 12;
	private float attackBarLeft;
	private float attackBarTop = 35;
	private float attackArrowPos = 0;
	private float attackArrowSpeed = 160f;
	private boolean attackArrowMovingRight = true;

	// === 방어 턴 (탄막 피하기) ===
	private ColorBlock defenseArea;
	private ColorBlock defenseAreaBorder;
	private ColorBlock playerHeart;

	private float defenseAreaSize;
	private float defenseAreaLeft;
	private float defenseAreaTop;
	private float heartSize = 12; // 하트 크기 증가 (8 -> 12)
	private float heartX, heartY;
	private float heartMoveSpeed = 120f; // 하트 이동 속도

	// 4방향 버튼
	private RedButton upButton;
	private RedButton downButton;
	private RedButton leftButton;
	private RedButton rightButton;
	private RenderedTextBlock controlHintText; // 조작 힌트
	
	// 현재 누르고 있는 방향 (0=없음, 1=위, 2=아래, 3=왼쪽, 4=오른쪽)
	private int pressedDirection = 0;

	// 탄막 리스트
	private ArrayList<Projectile> projectiles = new ArrayList<>();
	private float projectileSpawnTimer = 0;
	private float defenseTurnTimer = 0;
	private static final float DEFENSE_TURN_DURATION = 6f;
	private RenderedTextBlock timerText;
	private float timerTextY = 0;

	// 탄막 클래스
	private class Projectile {
		ColorBlock visual;
		float x, y;
		float vx, vy;
		float size;

		Projectile(float startX, float startY, float velX, float velY, float sz) {
			x = startX;
			y = startY;
			vx = velX;
			vy = velY;
			size = sz;
			visual = new ColorBlock(size, size, 0xFFFFFFFF);
			visual.x = x;
			visual.y = y;
			add(visual);
		}

		void update(float delta) {
			x += vx * delta;
			y += vy * delta;
			visual.x = x;
			visual.y = y;
		}

		boolean isOutOfBounds() {
			return x < defenseAreaLeft - 15 || x > defenseAreaLeft + defenseAreaSize + 15 ||
					y < defenseAreaTop - 15 || y > defenseAreaTop + defenseAreaSize + 15;
		}

		boolean hitsHeart() {
			float hCenterX = heartX + heartSize / 2;
			float hCenterY = heartY + heartSize / 2;
			float pCenterX = x + size / 2;
			float pCenterY = y + size / 2;

			float dist = (float) Math.sqrt(
					(hCenterX - pCenterX) * (hCenterX - pCenterX) +
							(hCenterY - pCenterY) * (hCenterY - pCenterY)
			);
			// 충돌 판정 완화 (0.7f -> 0.85f)
			return dist < (heartSize / 2 + size / 2) * 0.85f;
		}

		void destroy() {
			visual.killAndErase();
			remove(visual);
		}
	}

	public WndUndertaleGame(Utest enemy, Callback onVictory) {
		super();

		this.enemy = enemy;
		this.onVictory = onVictory;

		// HP 초기화
		playerHP = Dungeon.hero.HP;
		playerMaxHP = Dungeon.hero.HT;
		enemyHP = enemy.HP;
		enemyMaxHP = enemy.HT;

		attackBarLeft = (WIDTH - attackBarWidth) / 2;

		// 방어 영역 크기 계산 (버튼이 없어져서 더 크게 가능)
		defenseAreaSize = 100; // 버튼 없으니 더 크게!
		defenseAreaLeft = (WIDTH - defenseAreaSize) / 2;

		resize(WIDTH, HEIGHT);

		setupMainUI();
		showIntro();
	}

	private void setupMainUI() {
		// 제목 (작게, 상단)
		titleText = PixelScene.renderTextBlock(Messages.get(this, "title"), 6);
		titleText.hardlight(TITLE_COLOR);
		titleText.setPos((WIDTH - titleText.width()) / 2, 2);
		add(titleText);

		// HP 바 영역 (명확하게 분리)
		float hpBarY = 10;
		float hpBarWidth = 50;
		float hpBarHeight = 6;

		// 플레이어 HP (왼쪽)
		playerHPBarBg = new ColorBlock(hpBarWidth, hpBarHeight, 0xFF333333);
		playerHPBarBg.x = 5;
		playerHPBarBg.y = hpBarY;
		add(playerHPBarBg);

		playerHPBar = new ColorBlock(hpBarWidth * playerHP / playerMaxHP, hpBarHeight, 0xFFFF4444);
		playerHPBar.x = 5;
		playerHPBar.y = hpBarY;
		add(playerHPBar);

		playerHPText = PixelScene.renderTextBlock(Messages.get(this, "player_hp", playerHP, playerMaxHP), 4);
		playerHPText.setPos(5, hpBarY + hpBarHeight + 2);
		add(playerHPText);

		// 적 HP (오른쪽)
		enemyHPBarBg = new ColorBlock(hpBarWidth, hpBarHeight, 0xFF333333);
		enemyHPBarBg.x = WIDTH - 5 - hpBarWidth;
		enemyHPBarBg.y = hpBarY;
		add(enemyHPBarBg);

		enemyHPBar = new ColorBlock(hpBarWidth * enemyHP / enemyMaxHP, hpBarHeight, 0xFF44FF44);
		enemyHPBar.x = WIDTH - 5 - hpBarWidth;
		enemyHPBar.y = hpBarY;
		add(enemyHPBar);

		enemyHPText = PixelScene.renderTextBlock(Messages.get(this, "enemy_hp", enemyHP, enemyMaxHP), 4);
		enemyHPText.setPos(WIDTH - 5 - enemyHPText.width(), hpBarY + hpBarHeight + 2);
		add(enemyHPText);
	}

	private void updateHPDisplay() {
		float hpBarWidth = 50;
		float hpBarY = 10;
		float hpBarHeight = 6;

		// 플레이어 HP 바
		if (playerHPBar != null) {
			playerHPBar.killAndErase();
			remove(playerHPBar);
		}
		float pWidth = Math.max(1, hpBarWidth * playerHP / playerMaxHP);
		playerHPBar = new ColorBlock(pWidth, hpBarHeight, 0xFFFF4444);
		playerHPBar.x = 5;
		playerHPBar.y = hpBarY;
		add(playerHPBar);

		playerHPText.text(Messages.get(this, "player_hp", playerHP, playerMaxHP));
		playerHPText.setPos(5, hpBarY + hpBarHeight + 2);

		// 적 HP 바
		if (enemyHPBar != null) {
			enemyHPBar.killAndErase();
			remove(enemyHPBar);
		}
		float eWidth = Math.max(1, hpBarWidth * enemyHP / enemyMaxHP);
		enemyHPBar = new ColorBlock(eWidth, hpBarHeight, 0xFF44FF44);
		enemyHPBar.x = WIDTH - 5 - hpBarWidth;
		enemyHPBar.y = hpBarY;
		add(enemyHPBar);

		enemyHPText.text(Messages.get(this, "enemy_hp", enemyHP, enemyMaxHP));
		enemyHPText.setPos(WIDTH - 5 - enemyHPText.width(), hpBarY + hpBarHeight + 2);
	}

	private void clearDynamicUI() {
		if (infoText != null) { infoText.killAndErase(); remove(infoText); infoText = null; }
		if (actionButton != null) { actionButton.killAndErase(); remove(actionButton); actionButton = null; }
		clearAttackUI();
		clearDefenseUI();
	}

	private void showIntro() {
		battleState = BattleState.INTRO;
		clearDynamicUI();

		infoText = PixelScene.renderTextBlock(Messages.get(this, "battle_start"), 6);
		infoText.setPos((WIDTH - infoText.width()) / 2, 50);
		add(infoText);

		actionButton = new RedButton(Messages.get(this, "start_fight")) {
			@Override
			protected void onClick() {
				startEnemyTurn();
			}
		};
		actionButton.setRect(10, HEIGHT - 30, WIDTH - 20, 20);
		add(actionButton);
	}

	// ==================== 공격 턴 ====================

	private void startPlayerTurn() {
		battleState = BattleState.PLAYER_TURN;
		arrowStopped = false;
		clearDynamicUI();

		// 공격 바 크기 조정
		attackBarWidth = 120;
		attackBarHeight = 14;
		attackBarLeft = (WIDTH - attackBarWidth) / 2;
		attackBarTop = 50; // HP 바 아래로 이동

		infoText = PixelScene.renderTextBlock(Messages.get(this, "your_turn"), 5);
		infoText.hardlight(0x44FF44);
		infoText.setPos((WIDTH - infoText.width()) / 2, 35);
		add(infoText);

		// 공격 바 생성
		float center = attackBarWidth / 2;

		// 빨간 배경 (전체 100%)
		attackBarRed = new ColorBlock(attackBarWidth, attackBarHeight, 0xFFCC2222);
		attackBarRed.x = attackBarLeft;
		attackBarRed.y = attackBarTop;
		add(attackBarRed);

		// 주황 영역 (중앙 60%)
		float orangeWidth = attackBarWidth * 0.6f;
		attackBarOrange = new ColorBlock(orangeWidth, attackBarHeight, 0xFFDD7700);
		attackBarOrange.x = attackBarLeft + center - orangeWidth / 2;
		attackBarOrange.y = attackBarTop;
		add(attackBarOrange);

		// 노란 영역 (중앙 30%)
		float yellowWidth = attackBarWidth * 0.3f;
		attackBarYellow = new ColorBlock(yellowWidth, attackBarHeight, 0xFFDDCC00);
		attackBarYellow.x = attackBarLeft + center - yellowWidth / 2;
		attackBarYellow.y = attackBarTop;
		add(attackBarYellow);

		// 초록 영역 (중앙 10%)
		float greenWidth = attackBarWidth * 0.1f;
		attackBarGreen = new ColorBlock(greenWidth, attackBarHeight, 0xFF22CC22);
		attackBarGreen.x = attackBarLeft + center - greenWidth / 2;
		attackBarGreen.y = attackBarTop;
		add(attackBarGreen);

		// 화살표
		attackArrowPos = Random.Float(attackBarWidth * 0.1f, attackBarWidth * 0.9f);
		attackArrowMovingRight = Random.Int(2) == 0;

		attackArrow = new ColorBlock(3, attackBarHeight + 6, 0xFFFFFFFF);
		attackArrow.x = attackBarLeft + attackArrowPos - 1;
		attackArrow.y = attackBarTop - 3;
		add(attackArrow);

		// 공격 버튼 (딜레이 제거, 즉시 활성화)
		actionButton = new RedButton(Messages.get(this, "attack")) {
			@Override
			protected void onClick() {
				if (!arrowStopped) {
					doAttack();
				}
			}
		};
		actionButton.setRect(10, HEIGHT - 30, WIDTH - 20, 20);
		add(actionButton);
	}

	private void doAttack() {
		arrowStopped = true;
		battleState = BattleState.TURN_RESULT;

		if (actionButton != null) {
			actionButton.enable(false);
		}

		// 중심에서의 거리 계산
		float center = attackBarWidth / 2;
		float distFromCenter = Math.abs(attackArrowPos - center);
		float distRatio = distFromCenter / center;

		// 데미지 및 결과 결정
		int damage;
		String hitType;
		int hitColor;

		if (distRatio <= 0.05f) {
			damage = Random.IntRange(40, 50);
			hitType = "critical_hit";
			hitColor = 0x44FF44;
			Sample.INSTANCE.play(Assets.Sounds.HIT_STRONG);
			Camera.main.shake(6, 0.4f);
		} else if (distRatio <= 0.15f) {
			damage = Random.IntRange(28, 38);
			hitType = "strong_hit";
			hitColor = 0xFFFF00;
			Sample.INSTANCE.play(Assets.Sounds.HIT_STRONG);
			Camera.main.shake(4, 0.3f);
		} else if (distRatio <= 0.30f) {
			damage = Random.IntRange(18, 28);
			hitType = "normal_hit";
			hitColor = 0xFFAA00;
			Sample.INSTANCE.play(Assets.Sounds.HIT);
			Camera.main.shake(2, 0.2f);
		} else {
			damage = Random.IntRange(8, 18);
			hitType = "weak_hit";
			hitColor = 0xFF8888;
			Sample.INSTANCE.play(Assets.Sounds.HIT);
		}

		enemyHP -= damage;
		if (enemyHP < 0) enemyHP = 0;

		// 결과 표시
		if (infoText != null) {
			infoText.killAndErase();
			remove(infoText);
		}
		infoText = PixelScene.renderTextBlock(Messages.get(this, hitType, damage), 6);
		infoText.hardlight(hitColor);
		infoText.setPos((WIDTH - infoText.width()) / 2, 50);
		add(infoText);

		enemy.takeDamage(damage);
		updateHPDisplay();

		// 공격 UI 정리
		clearAttackUI();

		// 버튼 교체
		if (actionButton != null) {
			actionButton.killAndErase();
			remove(actionButton);
		}

		if (enemyHP <= 0) {
			endBattle(true);
		} else {
			actionButton = new RedButton(Messages.get(this, "continue")) {
				@Override
				protected void onClick() {
					startEnemyTurn();
				}
			};
			actionButton.setRect(10, HEIGHT - 30, WIDTH - 20, 20);
			add(actionButton);
		}
	}

	private void clearAttackUI() {
		if (attackBarRed != null) { attackBarRed.killAndErase(); remove(attackBarRed); attackBarRed = null; }
		if (attackBarOrange != null) { attackBarOrange.killAndErase(); remove(attackBarOrange); attackBarOrange = null; }
		if (attackBarYellow != null) { attackBarYellow.killAndErase(); remove(attackBarYellow); attackBarYellow = null; }
		if (attackBarGreen != null) { attackBarGreen.killAndErase(); remove(attackBarGreen); attackBarGreen = null; }
		if (attackArrow != null) { attackArrow.killAndErase(); remove(attackArrow); attackArrow = null; }
	}

	// ==================== 방어 턴 (터치 드래그) ====================

	private void startEnemyTurn() {
		battleState = BattleState.ENEMY_TURN;
		defenseTurnTimer = 0;
		projectileSpawnTimer = 0;
		projectiles.clear();
		clearDynamicUI();

		// 레이아웃 계산 (HP 바 아래부터 시작)
		float infoTextY = 35;
		timerTextY = infoTextY + 8;

		// 방어 영역 크기 조정 (버튼 공간 확보)
		defenseAreaSize = 90;
		defenseAreaLeft = (WIDTH - defenseAreaSize) / 2;
		defenseAreaTop = timerTextY + 6;

		// 정보 텍스트
		infoText = PixelScene.renderTextBlock(Messages.get(this, "enemy_turn"), 5);
		infoText.hardlight(0xFF4444);
		infoText.setPos((WIDTH - infoText.width()) / 2, infoTextY);
		add(infoText);

		// 타이머 텍스트
		timerText = PixelScene.renderTextBlock(
				Messages.get(this, "survive", (int)DEFENSE_TURN_DURATION), 5);
		timerText.setPos((WIDTH - timerText.width()) / 2, timerTextY);
		add(timerText);

		// 방어 영역 테두리
		defenseAreaBorder = new ColorBlock(defenseAreaSize + 4, defenseAreaSize + 4, 0xFFFFFFFF);
		defenseAreaBorder.x = defenseAreaLeft - 2;
		defenseAreaBorder.y = defenseAreaTop - 2;
		add(defenseAreaBorder);

		// 방어 영역 배경
		defenseArea = new ColorBlock(defenseAreaSize, defenseAreaSize, 0xFF111111);
		defenseArea.x = defenseAreaLeft;
		defenseArea.y = defenseAreaTop;
		add(defenseArea);

		// 하트 (중앙 시작)
		heartX = defenseAreaLeft + defenseAreaSize / 2 - heartSize / 2;
		heartY = defenseAreaTop + defenseAreaSize / 2 - heartSize / 2;

		playerHeart = new ColorBlock(heartSize, heartSize, 0xFFFF0000);
		playerHeart.x = heartX;
		playerHeart.y = heartY;
		add(playerHeart);

		// 4방향 버튼 생성 (방어 영역 아래, 화면 하단 고정)
		float buttonSize = 24;
		float buttonY = defenseAreaTop + defenseAreaSize + 8;
		float centerX = WIDTH / 2;
		pressedDirection = 0;

		// 조작 힌트 텍스트 (버튼 위)
		controlHintText = PixelScene.renderTextBlock(Messages.get(this, "button_hint"), 4);
		controlHintText.hardlight(0xAAAAAA);
		controlHintText.setPos((WIDTH - controlHintText.width()) / 2, buttonY - 8);
		add(controlHintText);

		// 위 버튼
		upButton = new RedButton("↑") {
			@Override
			protected void onPointerDown() {
				pressedDirection = 1;
			}
			@Override
			protected void onPointerUp() {
				if (pressedDirection == 1) pressedDirection = 0;
			}
		};
		upButton.setRect(centerX - buttonSize / 2, buttonY, buttonSize, buttonSize);
		add(upButton);

		// 아래 버튼
		downButton = new RedButton("↓") {
			@Override
			protected void onPointerDown() {
				pressedDirection = 2;
			}
			@Override
			protected void onPointerUp() {
				if (pressedDirection == 2) pressedDirection = 0;
			}
		};
		downButton.setRect(centerX - buttonSize / 2, buttonY + buttonSize + 3, buttonSize, buttonSize);
		add(downButton);

		// 왼쪽 버튼
		leftButton = new RedButton("←") {
			@Override
			protected void onPointerDown() {
				pressedDirection = 3;
			}
			@Override
			protected void onPointerUp() {
				if (pressedDirection == 3) pressedDirection = 0;
			}
		};
		leftButton.setRect(centerX - buttonSize - 3, buttonY + buttonSize / 2 + 1, buttonSize, buttonSize);
		add(leftButton);

		// 오른쪽 버튼
		rightButton = new RedButton("→") {
			@Override
			protected void onPointerDown() {
				pressedDirection = 4;
			}
			@Override
			protected void onPointerUp() {
				if (pressedDirection == 4) pressedDirection = 0;
			}
		};
		rightButton.setRect(centerX + 3, buttonY + buttonSize / 2 + 1, buttonSize, buttonSize);
		add(rightButton);
	}

	// 방향 버튼으로 하트 이동 (update에서 호출)
	private void updateHeartMovement(float delta) {
		if (battleState != BattleState.ENEMY_TURN || pressedDirection == 0) return;

		int dx = 0, dy = 0;
		switch (pressedDirection) {
			case 1: dy = -1; break; // 위
			case 2: dy = 1; break;  // 아래
			case 3: dx = -1; break; // 왼쪽
			case 4: dx = 1; break;  // 오른쪽
		}

		float moveAmount = heartMoveSpeed * delta;
		float newX = heartX + dx * moveAmount;
		float newY = heartY + dy * moveAmount;

		// 방어 영역 내로 제한
		heartX = Math.max(defenseAreaLeft, Math.min(newX, defenseAreaLeft + defenseAreaSize - heartSize));
		heartY = Math.max(defenseAreaTop, Math.min(newY, defenseAreaTop + defenseAreaSize - heartSize));

		// 하트 위치 업데이트
		if (playerHeart != null) {
			playerHeart.x = heartX;
			playerHeart.y = heartY;
		}
	}

	private void spawnProjectile() {
		int side = Random.Int(4);
		float x, y, vx, vy;
		// 속도 조정: 더 예측 가능하게 (50-80 -> 40-60)
		float speed = 40f + Random.Float(20f);

		switch (side) {
			case 0: // 위
				x = defenseAreaLeft + Random.Float(defenseAreaSize);
				y = defenseAreaTop - 8;
				// 더 직선적으로 (랜덤 요소 감소)
				vx = Random.Float(-10, 10);
				vy = speed;
				break;
			case 1: // 아래
				x = defenseAreaLeft + Random.Float(defenseAreaSize);
				y = defenseAreaTop + defenseAreaSize + 8;
				vx = Random.Float(-10, 10);
				vy = -speed;
				break;
			case 2: // 왼쪽
				x = defenseAreaLeft - 8;
				y = defenseAreaTop + Random.Float(defenseAreaSize);
				vx = speed;
				vy = Random.Float(-10, 10);
				break;
			default: // 오른쪽
				x = defenseAreaLeft + defenseAreaSize + 8;
				y = defenseAreaTop + Random.Float(defenseAreaSize);
				vx = -speed;
				vy = Random.Float(-10, 10);
				break;
		}

		// 탄막 크기 약간 증가 (5 -> 6)
		projectiles.add(new Projectile(x, y, vx, vy, 6));
	}

	private void updateDefense(float delta) {
		defenseTurnTimer += delta;

		// 타이머 업데이트
		int remaining = (int)(DEFENSE_TURN_DURATION - defenseTurnTimer) + 1;
		if (timerText != null) {
			timerText.text(Messages.get(this, "survive", Math.max(0, remaining)));
			timerText.setPos((WIDTH - timerText.width()) / 2, timerTextY);
		}

		// 탄막 생성 (더 예측 가능한 패턴)
		projectileSpawnTimer += delta;
		// 생성 간격 증가 (더 여유롭게)
		float spawnInterval = defenseTurnTimer < 3f ? 0.8f : 0.6f;
		if (projectileSpawnTimer >= spawnInterval) {
			projectileSpawnTimer = 0;
			spawnProjectile();
			// 후반부에도 한 번에 하나씩만 생성
			if (defenseTurnTimer > 3f && Random.Float() < 0.5f) {
				spawnProjectile();
			}
		}

		// 탄막 업데이트
		Iterator<Projectile> iter = projectiles.iterator();
		while (iter.hasNext()) {
			Projectile p = iter.next();
			p.update(delta);

			if (p.hitsHeart()) {
				int damage = Random.IntRange(5, 10);
				playerHP -= damage;
				if (playerHP < 0) playerHP = 0;

				Dungeon.hero.damage(damage, enemy);
				Sample.INSTANCE.play(Assets.Sounds.HIT);
				Camera.main.shake(2, 0.15f);

				updateHPDisplay();

				p.destroy();
				iter.remove();

				if (playerHP <= 0 || !Dungeon.hero.isAlive()) {
					endBattle(false);
					return;
				}
				continue;
			}

			if (p.isOutOfBounds()) {
				p.destroy();
				iter.remove();
			}
		}

		// 턴 종료
		if (defenseTurnTimer >= DEFENSE_TURN_DURATION) {
			endEnemyTurn();
		}
	}

	private void endEnemyTurn() {
		battleState = BattleState.TURN_RESULT;
		clearDefenseUI();

		infoText = PixelScene.renderTextBlock(Messages.get(this, "survived"), 6);
		infoText.hardlight(0x44FF44);
		infoText.setPos((WIDTH - infoText.width()) / 2, 50);
		add(infoText);

		Sample.INSTANCE.play(Assets.Sounds.LEVELUP);

		actionButton = new RedButton(Messages.get(this, "continue")) {
			@Override
			protected void onClick() {
				startPlayerTurn();
			}
		};
		actionButton.setRect(10, HEIGHT - 30, WIDTH - 20, 20);
		add(actionButton);
	}

	private void clearDefenseUI() {
		for (Projectile p : projectiles) {
			p.destroy();
		}
		projectiles.clear();
		pressedDirection = 0;

		if (defenseAreaBorder != null) { defenseAreaBorder.killAndErase(); remove(defenseAreaBorder); defenseAreaBorder = null; }
		if (defenseArea != null) { defenseArea.killAndErase(); remove(defenseArea); defenseArea = null; }
		if (playerHeart != null) { playerHeart.killAndErase(); remove(playerHeart); playerHeart = null; }
		if (timerText != null) { timerText.killAndErase(); remove(timerText); timerText = null; }
		if (controlHintText != null) { controlHintText.killAndErase(); remove(controlHintText); controlHintText = null; }
		if (upButton != null) { upButton.killAndErase(); remove(upButton); upButton = null; }
		if (downButton != null) { downButton.killAndErase(); remove(downButton); downButton = null; }
		if (leftButton != null) { leftButton.killAndErase(); remove(leftButton); leftButton = null; }
		if (rightButton != null) { rightButton.killAndErase(); remove(rightButton); rightButton = null; }
	}

	// ==================== 전투 종료 ====================

	private void endBattle(boolean playerWon) {
		battleState = BattleState.BATTLE_END;
		clearDynamicUI();

		if (playerWon) {
			Sample.INSTANCE.play(Assets.Sounds.BOSS);

			infoText = PixelScene.renderTextBlock(Messages.get(this, "victory"), 7);
			infoText.hardlight(0xFFFF00);
			infoText.setPos((WIDTH - infoText.width()) / 2, 50);
			add(infoText);

			actionButton = new RedButton(Messages.get(this, "close")) {
				@Override
				protected void onClick() {
					if (onVictory != null) {
						onVictory.call();
					}
					hide();
				}
			};
		} else {
			Sample.INSTANCE.play(Assets.Sounds.DEATH);
			GameScene.flash(0xFF0000);

			infoText = PixelScene.renderTextBlock(Messages.get(this, "defeat"), 7);
			infoText.hardlight(0xFF4444);
			infoText.setPos((WIDTH - infoText.width()) / 2, 50);
			add(infoText);

			actionButton = new RedButton(Messages.get(this, "close")) {
				@Override
				protected void onClick() {
					hide();
				}
			};
		}
		actionButton.setRect(10, HEIGHT - 30, WIDTH - 20, 20);
		add(actionButton);
	}

	@Override
	public void update() {
		super.update();

		if (battleState == BattleState.PLAYER_TURN && !arrowStopped) {
			// 화살표 이동
			float delta = Game.elapsed * attackArrowSpeed;

			if (attackArrowMovingRight) {
				attackArrowPos += delta;
				if (attackArrowPos >= attackBarWidth) {
					attackArrowPos = attackBarWidth;
					attackArrowMovingRight = false;
				}
			} else {
				attackArrowPos -= delta;
				if (attackArrowPos <= 0) {
					attackArrowPos = 0;
					attackArrowMovingRight = true;
				}
			}

			if (attackArrow != null) {
				attackArrow.x = attackBarLeft + attackArrowPos - 1;
			}
		}

		if (battleState == BattleState.ENEMY_TURN) {
			updateDefense(Game.elapsed);
			updateHeartMovement(Game.elapsed);
		}
	}

	@Override
	public void onBackPressed() {
		if (battleState == BattleState.BATTLE_END) {
			super.onBackPressed();
		}
	}
}