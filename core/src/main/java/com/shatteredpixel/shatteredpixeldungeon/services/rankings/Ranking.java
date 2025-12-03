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

package com.shatteredpixel.shatteredpixeldungeon.services.rankings;

import java.util.ArrayList;
import java.util.Date;

public class Ranking {

	public static RankingService service;

	public static boolean supportsRankings(){
		return service != null;
	}

	private static Date lastCheck = null;
	private static String lastCheckedDate = null;
	private static final long CHECK_DELAY = 1000*3; //3 seconds (실시간 업데이트를 위해 짧게 설정)

	/**
	 * Get daily rankings for a specific date
	 * @param date Date string in format "yyyy-MM-dd"
	 */
	public static void checkForRankings(String date){
		checkForRankings(date, false);
	}

	/**
	 * Get daily rankings for a specific date
	 * @param date Date string in format "yyyy-MM-dd"
	 * @param force Force refresh even if within CHECK_DELAY
	 */
	public static void checkForRankings(String date, boolean force){
		if (!supportsRankings()) return;
		
		// force=true일 때는 항상 새로고침, 날짜가 다르면 항상 새로고침
		boolean shouldRefresh = force || 
		                       lastCheckedDate == null || 
		                       !lastCheckedDate.equals(date) ||
		                       lastCheck == null || 
		                       (new Date().getTime() - lastCheck.getTime()) >= CHECK_DELAY;
		
		if (!shouldRefresh) return;

		// 항상 데이터로도 사용 가능하도록 true로 설정
		service.getDailyRankings(date, true, new RankingService.RankingResultCallback() {
			@Override
			public void onRankingReceived(ArrayList<DailyRankingEntry> rankings) {
				lastCheck = new Date();
				lastCheckedDate = date;
				Ranking.rankings = rankings;
			}

			@Override
			public void onConnectionFailed() {
				// 연결 실패 시에도 날짜는 유지 (다음 요청 시 다시 시도)
				Ranking.rankings = null;
			}
		});
	}

	private static ArrayList<DailyRankingEntry> rankings;

	public static synchronized boolean rankingsAvailable(){
		return rankings != null && !rankings.isEmpty();
	}

	/**
	 * 랭킹 데이터가 로딩 중인지 확인 (null이면 아직 로딩 중)
	 */
	public static synchronized boolean isLoading(){
		return rankings == null;
	}

	/**
	 * 랭킹 요청이 완료되었는지 확인 (null이 아니면 완료, 빈 배열일 수도 있음)
	 */
	public static synchronized boolean hasChecked(){
		return rankings != null;
	}

	public static synchronized ArrayList<DailyRankingEntry> rankings(){
		if (rankings == null) return new ArrayList<>();
		return new ArrayList<>(rankings);
	}

	public static synchronized void clearRankings(){
		rankings = null;
		lastCheck = null;
		lastCheckedDate = null;
	}

}

