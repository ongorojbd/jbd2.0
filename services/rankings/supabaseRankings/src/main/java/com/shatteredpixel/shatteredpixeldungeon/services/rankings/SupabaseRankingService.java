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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.watabou.noosa.Game;

import java.util.ArrayList;

public class SupabaseRankingService extends RankingService {

	// TODO: Supabase 프로젝트 생성 후 아래 값들을 설정하세요
	// Supabase 대시보드 > Settings > API에서 확인 가능
	private static final String SUPABASE_URL = "https://tbgdnucmfjmvrpvukxeg.supabase.co";
	private static final String SUPABASE_ANON_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InRiZ2RudWNtZmptdnJwdnVreGVnIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjQ1Nzc4ODAsImV4cCI6MjA4MDE1Mzg4MH0.aiX_RYCQG_mMV8eqbtOBr1MO3jW9uXmBRXdKzsJ_qa4";

	@Override
	public void submitDailyScore(String date, int score, String playerName, 
	                            int depth, int level, String heroClass,
	                            int armorTier, boolean win, boolean ascending,
	                            String gameData,
	                            boolean useMetered, SubmitResultCallback callback) {
		
		// WiFi 체크 제거 - 항상 데이터로도 사용 가능
		// if (!useMetered && !Game.platform.connectedToUnmeteredNetwork()) {
		// 	callback.onSubmitFailed();
		// 	return;
		// }

		// JSON 본문 생성 (playerName, heroClass, gameData 이스케이프 처리)
		String escapedPlayerName = playerName.replace("\\", "\\\\").replace("\"", "\\\"");
		String escapedHeroClass = heroClass.replace("\\", "\\\\").replace("\"", "\\\"");
		// gameData는 이미 JSON이므로 직접 포함 (따옴표로 감싸서 문자열로 저장)
		String escapedGameData = gameData != null ? gameData.replace("\\", "\\\\").replace("\"", "\\\"") : "";
		
		StringBuilder jsonBuilder = new StringBuilder();
		jsonBuilder.append("{\"date\":\"").append(date).append("\"");
		jsonBuilder.append(",\"score\":").append(score);
		jsonBuilder.append(",\"player_name\":\"").append(escapedPlayerName).append("\"");
		jsonBuilder.append(",\"depth\":").append(depth);
		jsonBuilder.append(",\"level\":").append(level);
		jsonBuilder.append(",\"hero_class\":\"").append(escapedHeroClass).append("\"");
		jsonBuilder.append(",\"armor_tier\":").append(armorTier);
		jsonBuilder.append(",\"win\":").append(win);
		jsonBuilder.append(",\"ascending\":").append(ascending);
		if (gameData != null && !gameData.isEmpty()) {
			jsonBuilder.append(",\"game_data\":\"").append(escapedGameData).append("\"");
		}
		jsonBuilder.append("}");
		String jsonBody = jsonBuilder.toString();

		Net.HttpRequest httpPost = new Net.HttpRequest(Net.HttpMethods.POST);
		httpPost.setUrl(SUPABASE_URL + "/rest/v1/daily_rankings");
		httpPost.setHeader("Content-Type", "application/json");
		httpPost.setHeader("apikey", SUPABASE_ANON_KEY);
		httpPost.setHeader("Authorization", "Bearer " + SUPABASE_ANON_KEY);
		httpPost.setHeader("Prefer", "return=minimal");
		httpPost.setContent(jsonBody);

		Gdx.net.sendHttpRequest(httpPost, new Net.HttpResponseListener() {
			@Override
			public void handleHttpResponse(Net.HttpResponse httpResponse) {
				int statusCode = httpResponse.getStatus().getStatusCode();
				String responseBody = "";
				try {
					responseBody = httpResponse.getResultAsString();
				} catch (Exception e) {
					// 응답 본문 읽기 실패
				}
				
				if (statusCode >= 200 && statusCode < 300) {
					Game.reportException(new Exception("Supabase submit SUCCESS: " + statusCode + " | Body: " + responseBody));
					callback.onSubmitSuccess();
				} else {
					Game.reportException(new Exception("Supabase submit failed: " + statusCode + " | Body: " + responseBody + " | Request: " + jsonBody));
					callback.onSubmitFailed();
				}
			}

			@Override
			public void failed(Throwable t) {
				Game.reportException(new Exception("Supabase submit network error: " + t.getMessage() + " | Request: " + jsonBody, t));
				callback.onSubmitFailed();
			}

			@Override
			public void cancelled() {
				Game.reportException(new Exception("Supabase submit cancelled | Request: " + jsonBody));
				callback.onSubmitFailed();
			}
		});
	}

	@Override
	public void getDailyRankings(String date, boolean useMetered, 
	                            RankingResultCallback callback) {
		
		// WiFi 체크 제거 - 항상 데이터로도 사용 가능
		// if (!useMetered && !Game.platform.connectedToUnmeteredNetwork()) {
		// 	callback.onConnectionFailed();
		// 	return;
		// }

		// Supabase REST API: date 컬럼으로 필터링
		// 현재 Rankings.submit()은 게임 시작 날짜를 date에 저장하므로,
		// date 컬럼을 사용하여 조회하고 클라이언트 측에서 추가 검증
		String url = SUPABASE_URL + "/rest/v1/daily_rankings" +
		             "?date=eq." + date +
		             "&order=score.desc" +
		             "&limit=10";
		
		// 디버깅: 조회 날짜 로그
		Game.reportException(new Exception("Fetching daily rankings from Supabase: date=" + date + ", url=" + url));

		Net.HttpRequest httpGet = new Net.HttpRequest(Net.HttpMethods.GET);
		httpGet.setUrl(url);
		httpGet.setHeader("apikey", SUPABASE_ANON_KEY);
		httpGet.setHeader("Authorization", "Bearer " + SUPABASE_ANON_KEY);

		Gdx.net.sendHttpRequest(httpGet, new Net.HttpResponseListener() {
			@Override
			public void handleHttpResponse(Net.HttpResponse httpResponse) {
				try {
					String responseText = httpResponse.getResultAsString();
					JsonReader jsonReader = new JsonReader();
					JsonValue jsonValue = jsonReader.parse(responseText);

					ArrayList<DailyRankingEntry> rankings = new ArrayList<>();
					
					if (jsonValue.isArray()) {
						int rank = 1;
						for (JsonValue entry : jsonValue) {
							String entryDate = entry.getString("date", "");
							
							// date 컬럼만 사용 (Rankings.submit()에서 게임 시작 날짜를 저장)
							// 조회 날짜와 정확히 일치하는 것만 포함
							if (!entryDate.equals(date)) {
								continue; // 날짜가 일치하지 않으면 건너뛰기
							}
							
							DailyRankingEntry rankingEntry = new DailyRankingEntry();
							rankingEntry.rank = rank++;
							rankingEntry.playerName = entry.getString("player_name", "Anonymous");
							rankingEntry.score = entry.getInt("score", 0);
							rankingEntry.date = entryDate;
							rankingEntry.depth = entry.getInt("depth", 0);
							rankingEntry.level = entry.getInt("level", 0);
							rankingEntry.heroClass = entry.getString("hero_class", "");
							rankingEntry.armorTier = entry.getInt("armor_tier", 0);
							rankingEntry.win = entry.getBoolean("win", false);
							rankingEntry.ascending = entry.getBoolean("ascending", false);
							rankingEntry.gameData = entry.getString("game_data", null);
							rankings.add(rankingEntry);
						}
					}

					DailyRankingEntry.networkFailed = false; // 성공
					callback.onRankingReceived(rankings);
				} catch (Exception e) {
					Game.reportException(e);
					DailyRankingEntry.networkFailed = true; // 파싱 실패
					callback.onConnectionFailed();
				}
			}

			@Override
			public void failed(Throwable t) {
				Game.reportException(t);
				DailyRankingEntry.networkFailed = true; // 네트워크 실패
				callback.onConnectionFailed();
			}

			@Override
			public void cancelled() {
				DailyRankingEntry.networkFailed = true; // 취소됨
				callback.onConnectionFailed();
			}
		});
	}
}

