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

public abstract class RankingService {

	public static abstract class RankingResultCallback {
		public abstract void onRankingReceived(ArrayList<DailyRankingEntry> rankings);
		public abstract void onConnectionFailed();
	}

	public static abstract class SubmitResultCallback {
		public abstract void onSubmitSuccess();
		public abstract void onSubmitFailed();
	}

	/**
	 * Submit a daily challenge score to the server
	 * @param date Date string in format "yyyy-MM-dd"
	 * @param score Player's score
	 * @param playerName Player's name (can be anonymous ID)
	 * @param depth Maximum depth reached
	 * @param level Hero level
	 * @param heroClass Hero class name
	 * @param armorTier Hero armor tier
	 * @param win Whether the player won
	 * @param ascending Whether ascending
	 * @param gameData Serialized game data (JSON)
	 * @param useMetered Whether to use metered network
	 * @param callback Callback for result
	 */
	public abstract void submitDailyScore(String date, int score, String playerName, 
	                                      int depth, int level, String heroClass,
	                                      int armorTier, boolean win, boolean ascending,
	                                      String gameData,
	                                      boolean useMetered, SubmitResultCallback callback);

	/**
	 * Get top 10 daily rankings for a specific date
	 * @param date Date string in format "yyyy-MM-dd"
	 * @param useMetered Whether to use metered network
	 * @param callback Callback for result
	 */
	public abstract void getDailyRankings(String date, boolean useMetered, 
	                                      RankingResultCallback callback);

}

