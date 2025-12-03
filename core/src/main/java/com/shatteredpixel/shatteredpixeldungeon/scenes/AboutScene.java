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

package com.shatteredpixel.shatteredpixeldungeon.scenes;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Rankings;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.services.rankings.DailyRankingEntry;
import com.shatteredpixel.shatteredpixeldungeon.services.rankings.Ranking;
import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.ui.Archs;
import com.shatteredpixel.shatteredpixeldungeon.ui.Button;
import com.shatteredpixel.shatteredpixeldungeon.ui.ExitButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.IconButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.StyledButton;
import com.shatteredpixel.shatteredpixeldungeon.windows.IconTitle;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndRanking;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndTitledMessage;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Camera;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.ui.Component;
import com.watabou.utils.GameMath;
import com.watabou.utils.RectF;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class AboutScene extends PixelScene {

    private static final float ROW_HEIGHT_MAX = 22;
    private static final float ROW_HEIGHT_MIN = 14;
    private static final float MAX_ROW_WIDTH = 180;
    private static final float GAP = 4;

    private Archs archs;
    private String currentDate;
    private float refreshTimer = 0;
    private static final float REFRESH_INTERVAL = 3f; // 3초마다 확인
    private float loadingTimer = 0;
    private static final float LOADING_TIMEOUT = 10f; // 10초 후 타임아웃
    private RenderedTextBlock statusText;
    private Component rankingContainer;
    private boolean wasLoading = false; // 이전 프레임의 로딩 상태 추적

	@Override
	public void create() {
		super.create();

        Music.INSTANCE.playTracks(
                new String[]{Assets.Music.THEME_1},
                new float[]{1},
                false);

        uiCamera.visible = false;

		int w = Camera.main.width;
		int h = Camera.main.height;
		RectF insets = getCommonInsets();

        archs = new Archs();
        archs.setSize(w, h);
        add(archs);

        // 어두운 배경 레이어
		add(new ColorBlock(w, h, 0x88000000));

        w -= insets.left + insets.right;
        h -= insets.top + insets.bottom;

        // 날짜 선택 (오늘 날짜)
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ROOT);
        format.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        currentDate = format.format(new Date(Game.realTime));

        // 제목
        Image titleIcon = Icons.CALENDAR.get();
        titleIcon.hardlight(0.5f, 1f, 2f); // 경쟁 모드 색깔과 동일
        IconTitle title = new IconTitle(titleIcon, "플레이어 랭킹");
        title.setSize(200, 0);
        title.setPos(
                insets.left + (w - title.reqWidth()) / 2f,
                insets.top + (20 - title.height()) / 2f
        );
        align(title);
        add(title);

        // 랭킹 컨테이너
        rankingContainer = new Component();
        add(rankingContainer);

		// 랭킹 로드 (강제 새로고침)
		if (Ranking.supportsRankings()) {
			// 캐시를 클리어하고 최신 데이터 로드
			Ranking.clearRankings();
			Ranking.checkForRankings(currentDate, true); // 강제 새로고침
			loadingTimer = 0; // 로딩 타이머 초기화
			wasLoading = true; // 초기 상태는 로딩 중
		}

        updateRankingsDisplay();

        // 날짜 표시
        RenderedTextBlock dateText = PixelScene.renderTextBlock("날짜: " + currentDate, 7);
        dateText.hardlight(0x88FFFF);
        float dateTextX = insets.left + (w - dateText.width() - 20) / 2; // 새로고침 버튼 공간 확보
        float dateTextY = insets.top + h - dateText.height() - 50;
        dateText.setPos(
                dateTextX,
                dateTextY
        );
        align(dateText);
        add(dateText);

        // 새로고침 버튼 (날짜 오른쪽)
        IconButton refreshBtn = new IconButton(Icons.get(Icons.RANDOMIZE)) {
            @Override
            protected void onClick() {
                super.onClick();
                // 강제 새로고침
                if (Ranking.supportsRankings()) {
                    Ranking.clearRankings(); // 캐시 클리어
                    Ranking.checkForRankings(currentDate, true); // 강제 새로고침
                    loadingTimer = 0; // 로딩 타이머 리셋
                    wasLoading = true; // 로딩 상태로 설정
                    updateRankingsDisplay(); // 즉시 UI 업데이트
                }
            }
        };
        // 날짜 텍스트와 y축 중앙 정렬
        float refreshBtnY = dateTextY + (dateText.height() - 16) / 2f;
        refreshBtn.setRect(
                dateTextX + dateText.width() + 4,
                refreshBtnY,
                16,
                16
        );
        align(refreshBtn);
        add(refreshBtn);

        // 도움말 버튼 (개발자 정보 버튼 위)
        StyledButton helpBtn = new StyledButton(Chrome.Type.GREY_BUTTON_TR, "도움말", 7) {
            @Override
            protected void onClick() {
                super.onClick();
                ShatteredPixelDungeon.scene().addToFront(new WndTitledMessage(
                        Icons.get(Icons.INFO),
                        "플레이어 랭킹",
                        "오늘의 경쟁 모드 플레이어 랭킹을 확인하세요!\n\n" +
                                "랭킹은 새로운 기록이 등록되면 실시간으로 반영되며, 기록 상세 보기도 제공됩니다.\n\n" +
                                "점수가 높을수록 상위에 표시되며, 1~3위는 특별한 색상으로 빛납니다."
                ));
            }
        };
        helpBtn.setRect(
                insets.left + (w - 80) / 2,
                insets.top + h - 45, // 도움말, 개발자 정보 위치
                80,
                18
        );
        align(helpBtn);
        add(helpBtn);

        // 개발자 소개 버튼 (하단 중앙)
        StyledButton creditsBtn = new StyledButton(Chrome.Type.GREY_BUTTON_TR, "개발자 정보", 7) {
            @Override
            protected void onClick() {
                super.onClick();
                ShatteredPixelDungeon.switchScene(CreditScene.class);
            }
        };
        creditsBtn.setRect(
                insets.left + (w - 80) / 2,
                insets.top + h - 25,
                80,
                18
        );
        align(creditsBtn);
        add(creditsBtn);

        ExitButton btnExit = new ExitButton();
        btnExit.setPos(Camera.main.width - btnExit.width() - insets.right, insets.top);
        add(btnExit);

        fadeIn();
    }

    private void updateRankingsDisplay() {
        if (rankingContainer == null) return;

        rankingContainer.clear();

        int w = Camera.main.width;
        int h = Camera.main.height;
        RectF insets = getCommonInsets();
        w -= insets.left + insets.right;
        h -= insets.top + insets.bottom;

        // 랭킹 리스트
        ArrayList<DailyRankingEntry> rankings = Ranking.rankings();
        // 로딩 중인지 확인: rankings가 null이면 아직 로딩 중
        // 단, 타임아웃이 지나면 데이터 없음으로 처리
        boolean isLoading = Ranking.supportsRankings() && Ranking.isLoading() && loadingTimer < LOADING_TIMEOUT;

        if (isLoading) {
            // 로딩 중 메시지
            statusText = PixelScene.renderTextBlock("랭킹 데이터를 불러오는 중입니다.\n인터넷 연결이 필요합니다.", 8);
            statusText.hardlight(0x88FFFF);
            statusText.setPos(
                    insets.left + (w - statusText.width()) / 2,
                    insets.top + (h - statusText.height()) / 2
            );
            align(statusText);
            rankingContainer.add(statusText);
        } else if (rankings.size() > 0) {
            // 각 항목에 최대한 공간 할당
            float rowHeight = GameMath.gate(ROW_HEIGHT_MIN, (h - 120) / rankings.size(), ROW_HEIGHT_MAX);

            float left = (w - Math.min(MAX_ROW_WIDTH, w)) / 2 + GAP;
            float top = (h - rowHeight * rankings.size()) / 2 - 10; // 더 위로 올림

            int pos = 0;
            for (DailyRankingEntry entry : rankings) {
                RankingRow row = new RankingRow(pos, entry);
                float offset = 0;
                if (rowHeight <= 16) {
                    offset = (pos % 2 == 1) ? 5 : -5;
                }
                float rowY = insets.top + top + pos * rowHeight;
                row.setRect(insets.left + left + offset, rowY, w - left * 2, rowHeight);
                rankingContainer.add(row);

                pos++;
            }
        } else {
            // 네트워크 실패와 데이터 없음 구분
            String message;
            int color;
            if (DailyRankingEntry.networkFailed) {
                message = "랭킹을 불러오지 못했습니다.\n인터넷 연결을 확인해주세요.";
                color = 0xFF6666; // 빨간색
            } else {
                message = "오늘 도전한 플레이어가 없습니다.";
                color = 0xCCCCCC; // 회색
            }
            statusText = PixelScene.renderTextBlock(message, 8);
            statusText.hardlight(color);
            statusText.setPos(
                    insets.left + (w - statusText.width()) / 2,
                    insets.top + (h - statusText.height()) / 2
            );
            align(statusText);
            rankingContainer.add(statusText);
        }
    }

    @Override
    public void update() {
        super.update();

        // 로딩 타이머 업데이트
        boolean isLoading = Ranking.supportsRankings() && Ranking.isLoading();
        if (isLoading) {
            loadingTimer += Game.elapsed;
        } else {
            loadingTimer = 0; // 로딩 완료되면 타이머 리셋
        }

        // 로딩 상태가 변경되었을 때 즉시 UI 업데이트
        // (로딩 중 -> 완료, 또는 완료 -> 로딩 중으로 변경된 경우)
        if (wasLoading != isLoading) {
            updateRankingsDisplay();
            wasLoading = isLoading;
        }

        // 주기적으로 랭킹 확인 및 업데이트
        refreshTimer += Game.elapsed;
        if (refreshTimer >= REFRESH_INTERVAL) {
            refreshTimer = 0;
            if (Ranking.supportsRankings()) {
                // 로딩 중이 아니거나 타임아웃이 지났을 때만 새로 요청
                if (!Ranking.isLoading() || loadingTimer >= LOADING_TIMEOUT) {
                    // 주기적으로 자동 새로고침 (CHECK_DELAY 체크)
                    Ranking.checkForRankings(currentDate, true);
                    loadingTimer = 0; // 새 요청 시작 시 타이머 리셋
                }
            }
        }
	}

	@Override
	protected void onBackPressed() {
		ShatteredPixelDungeon.switchScene(TitleScene.class);
	}

    public static class RankingRow extends Button {

        private static final float GAP = 4;

        // 순위별 색상
        private static final int[] TEXT_1ST = {0xFFD700, 0xFFA500}; // 금색
        private static final int[] TEXT_2ND = {0xE0E0E0, 0xC8C8C8}; // 은색
        private static final int[] TEXT_3RD = {0xCD7F32, 0x8B6914}; // 동색
        private static final int[] TEXT_TOP = {0xFFFF88, 0xB2B25F};
        private static final int[] TEXT_NORMAL = {0xDDDDDD, 0x888888};
        private static final int FLARE_1ST = 0xFFD700;
        private static final int FLARE_2ND = 0xE0E0E0;
        private static final int FLARE_3RD = 0xCD7F32;
        private static final int FLARE_TOP = 0x888866;

        protected Image shield;
        private Flare flare;
        private BitmapText position;
        private RenderedTextBlock name;
        private BitmapText score;
        private Image steps;
        private BitmapText depth;
        private Image classIcon;
        private BitmapText level;
        private ColorBlock bg;
        
        private DailyRankingEntry rankingEntry;

        public RankingRow(int pos, DailyRankingEntry entry) {
			super();
			
			this.rankingEntry = entry;

            // 배경 추가 (짝수/홀수 행 구분)
            bg = new ColorBlock(1, 1, pos % 2 == 0 ? 0x22000000 : 0x11000000);
            addToBack(bg);

            // 상위 3위는 Flare 효과
            if (pos == 0) {
                flare = new Flare(8, 28);
                flare.angularSpeed = 90;
                flare.color(FLARE_1ST);
                addToBack(flare);
            } else if (pos == 1) {
                flare = new Flare(7, 26);
                flare.angularSpeed = 90;
                flare.color(FLARE_2ND);
                addToBack(flare);
            } else if (pos == 2) {
                flare = new Flare(6, 24);
                flare.angularSpeed = 90;
                flare.color(FLARE_3RD);
                addToBack(flare);
            }

            position.text(Integer.toString(entry.rank));
            position.measure();

            name.text(entry.playerName);
            score.text(Integer.toString(entry.score));
            score.measure();

            // depth 표시
            if (entry.depth != 0) {
                depth.text(Integer.toString(entry.depth));
                depth.measure();
                steps.copy(Icons.STAIRS.get());
                add(steps);
                add(depth);
			}

            // heroClass 아이콘 표시 (먼저 추가하여 뒤에 배치)
            if (entry.heroClass != null && !entry.heroClass.isEmpty()) {
                try {
                    HeroClass heroClassEnum = HeroClass.valueOf(entry.heroClass);
                    classIcon.copy(Icons.get(heroClassEnum));
                    add(classIcon);
                } catch (IllegalArgumentException e) {
                    // 잘못된 heroClass 문자열인 경우 무시
                }
            }

            // level 표시 (나중에 추가하여 아이콘 위에 표시)
            if (entry.level != 0) {
                level.text(Integer.toString(entry.level));
                level.measure();
                add(level);
            }

            int odd = pos % 2;

            // 순위별 색상 적용
            if (entry.rank == 1) {
                shield.copy(Icons.get(Icons.CHALLENGE_GREY));
                shield.hardlight(1f, 0.84f, 0f); // 금색
                position.hardlight(TEXT_1ST[odd]);
                name.hardlight(TEXT_1ST[odd]);
                score.hardlight(TEXT_1ST[odd]);
                if (depth != null) depth.hardlight(TEXT_1ST[odd]);
                if (level != null) level.hardlight(TEXT_1ST[odd]);
            } else if (entry.rank == 2) {
                shield.copy(Icons.get(Icons.CHALLENGE_GREY));
                shield.hardlight(0.88f, 0.88f, 0.88f); // 더 밝은 은색
                position.hardlight(TEXT_2ND[odd]);
                name.hardlight(TEXT_2ND[odd]);
                score.hardlight(TEXT_2ND[odd]);
                if (depth != null) depth.hardlight(TEXT_2ND[odd]);
                if (level != null) level.hardlight(TEXT_2ND[odd]);
            } else if (entry.rank == 3) {
                shield.copy(Icons.get(Icons.CHALLENGE_GREY));
                shield.hardlight(0.8f, 0.5f, 0.2f); // 동색
                position.hardlight(TEXT_3RD[odd]);
                name.hardlight(TEXT_3RD[odd]);
                score.hardlight(TEXT_3RD[odd]);
                if (depth != null) depth.hardlight(TEXT_3RD[odd]);
                if (level != null) level.hardlight(TEXT_3RD[odd]);
            } else if (entry.rank <= 10) {
                shield.copy(Icons.get(Icons.CHALLENGE_GREY));
                // 나머지는 트로피 아이콘 그대로 (색상 변경 없음)
                position.hardlight(TEXT_TOP[odd]);
                name.hardlight(TEXT_TOP[odd]);
                score.hardlight(TEXT_TOP[odd]);
                if (depth != null) depth.hardlight(TEXT_TOP[odd]);
                if (level != null) level.hardlight(TEXT_TOP[odd]);
            } else {
                shield.copy(Icons.get(Icons.CHALLENGE_GREY));
                // 나머지는 트로피 아이콘 그대로 (색상 변경 없음)
                position.hardlight(TEXT_NORMAL[odd]);
                name.hardlight(TEXT_NORMAL[odd]);
                score.hardlight(TEXT_NORMAL[odd]);
                if (depth != null) depth.hardlight(TEXT_NORMAL[odd]);
                if (level != null) level.hardlight(TEXT_NORMAL[odd]);
            }
        }

					@Override
        protected void createChildren() {
            super.createChildren();

            shield = new Image(Icons.get(Icons.CHALLENGE_GREY));
            add(shield);

            position = new BitmapText(PixelScene.pixelFont);
            add(position);

            name = renderTextBlock(7);
            add(name);

            score = new BitmapText(PixelScene.pixelFont);
            add(score);

            depth = new BitmapText(PixelScene.pixelFont);
            steps = new Image();

            classIcon = new Image();

            level = new BitmapText(PixelScene.pixelFont);
		}

		@Override
		protected void layout() {
			super.layout();

            bg.x = x;
            bg.y = y;
            bg.size(width, height);

            // RankingsScene과 동일한 고정 위치로 배치
            shield.x = x + (16 - shield.width) / 2f;
            shield.y = y + (height - shield.height) / 2f;
            align(shield);

            position.x = shield.x + (shield.width - position.width()) / 2f;
            position.y = shield.y + (shield.height - position.height()) / 2f + 1;
            align(position);

            if (flare != null) {
                flare.point(shield.center());
					}

            // classIcon을 오른쪽 끝에 고정 배치
            if (classIcon != null && classIcon.visible) {
                classIcon.x = x + width - 16 + (16 - classIcon.width()) / 2f;
                classIcon.y = shield.y + (16 - classIcon.height()) / 2f;
                align(classIcon);

                // level을 classIcon 위에 배치
                if (level != null && level.visible) {
                    level.x = classIcon.x + (classIcon.width - level.width()) / 2f;
                    level.y = classIcon.y + (classIcon.height - level.height()) / 2f + 1;
                    align(level);
                }
            }

            // steps와 depth를 오른쪽에서 두 번째 위치에 고정 배치
            if (steps != null && steps.visible) {
                steps.x = x + width - 32 + (16 - steps.width()) / 2f;
                steps.y = shield.y + (16 - steps.height()) / 2f;
                align(steps);

                if (depth != null && depth.visible) {
                    depth.x = steps.x + (steps.width - depth.width()) / 2f;
                    depth.y = steps.y + (steps.height - depth.height()) / 2f + 1;
                    align(depth);
                }
            }

            // score를 steps 왼쪽에 고정 배치 (steps가 있으면 steps 기준, 없으면 classIcon 기준)
            if (steps != null && steps.visible) {
                score.x = steps.x - 16 - score.width();
            } else if (classIcon != null && classIcon.visible) {
                score.x = classIcon.x - 16 - score.width();
				} else {
                score.x = x + width - 16 - score.width();
            }
            score.y = shield.y + (shield.height - score.height()) / 2f + 1;
            align(score);

            // name을 왼쪽에서 고정 위치에 배치 (RankingsScene의 desc와 동일)
            float nameLeft = x + 16 + GAP;
            float nameRight;
            if (steps != null && steps.visible) {
                nameRight = steps.x;
            } else if (score != null) {
                nameRight = score.x;
            } else if (classIcon != null && classIcon.visible) {
                nameRight = classIcon.x;
            } else {
                nameRight = x + width - 16;
            }
            name.maxWidth((int) (nameRight - nameLeft));
            name.setPos(nameLeft, shield.y + (shield.height - name.height()) / 2f + 1);
            align(name);
        }

        @Override
        protected void onClick() {
            // 클릭 시 상세 정보 표시
            if (rankingEntry != null && rankingEntry.gameData != null && !rankingEntry.gameData.isEmpty()) {
                Rankings.Record record = Rankings.createRecordFromDailyEntry(rankingEntry);
                if (record != null && record.gameData != null) {
                    ShatteredPixelDungeon.scene().addToFront(new WndRanking(record));
                } else {
                    // gameData가 없으면 간단한 정보만 표시
                    showSimpleInfo();
                }
            } else {
                // gameData가 없으면 간단한 정보만 표시
                showSimpleInfo();
            }
		}
		
		private void showSimpleInfo() {
            if (rankingEntry != null) {
                String info = rankingEntry.playerName + "\n\n" +
                        "점수: " + rankingEntry.score + "\n" +
                        "층: " + rankingEntry.depth + "\n" +
                        "레벨: " + rankingEntry.level;
                ShatteredPixelDungeon.scene().addToFront(new WndTitledMessage(
                        Icons.get(Icons.RANKINGS),
                        "플레이어 정보",
                        info
                ));
            }
        }
	}
}
