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

public class DailyRankingEntry {
	public int rank;
	public String playerName;
	public int score;
	public String date;
	public int depth;
	public int level;
	public String heroClass;
	public int armorTier;
	public boolean win;
	public boolean ascending;
	public String gameData; // 직렬화된 게임 데이터 (JSON)
	
	// 네트워크 요청 상태를 추적하기 위한 플래그
	public static boolean networkFailed = false;
	
	public DailyRankingEntry() {
	}
	
	public DailyRankingEntry(int rank, String playerName, int score, String date) {
		this.rank = rank;
		this.playerName = playerName;
		this.score = score;
		this.date = date;
	}
}

