# 일일 도전(경쟁 모드) 랭킹 시스템 구현 가이드

## 개요
이 문서는 죠죠의 기묘한 던전(Shattered Pixel Dungeon 기반)에 구현된 일일 도전(경쟁 모드) 랭킹 시스템의 전체 아키텍처와 구현 방법을 설명합니다.

---

## 1. 시스템 아키텍처

### 1.1 핵심 컴포넌트

```
[클라이언트]                          [백엔드]
   │                                    │
   ├─ HeroSelectScene                  │
   │  └─ 닉네임 입력 및 검증            │
   │                                    │
   ├─ Rankings.java                    │
   │  └─ 점수 제출 (게임 종료 시)     ───→ Supabase
   │                                    │  (daily_rankings 테이블)
   ├─ Ranking.java                     │
   │  └─ 점수 조회 (캐싱 + 새로고침) ←─── Supabase
   │                                    │
   ├─ AboutScene.java                  │
   │  └─ 랭킹 화면 표시 (클릭 시 상세)│
   │                                    │
   └─ WndRanking.java                  │
      └─ 상세 정보 표시 (아이템/특성)  │
```

---

## 2. 데이터베이스 스키마 (Supabase)

### 2.1 테이블 생성

```sql
CREATE TABLE daily_rankings (
    id BIGSERIAL PRIMARY KEY,
    date DATE NOT NULL,
    score INTEGER NOT NULL,
    player_name TEXT NOT NULL,
    depth INTEGER DEFAULT 0,
    level INTEGER DEFAULT 0,
    hero_class TEXT,
    armor_tier INTEGER DEFAULT 0,
    win BOOLEAN DEFAULT false,
    ascending BOOLEAN DEFAULT false,
    game_data TEXT,  -- Rankings.Record.gameData의 JSON 직렬화
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- 인덱스: date와 score로 빠른 조회
CREATE INDEX idx_daily_rankings_date_score ON daily_rankings(date DESC, score DESC);
```

### 2.2 주요 필드 설명

- **date**: 일일 도전 날짜 (yyyy-MM-dd)
- **score**: 플레이어 점수
- **player_name**: 플레이어 닉네임 (10자 제한)
- **depth, level, hero_class**: 기본 정보 (리스트 표시용)
- **armor_tier, win, ascending**: 추가 정보
- **game_data**: 전체 게임 데이터를 JSON으로 직렬화 (Bundle.toString())
  - 아이템, 특성, 배지, Statistics 등 모든 정보 포함
  - 랭킹 클릭 시 WndRanking으로 상세 정보 표시

---

## 3. 핵심 파일 및 역할

### 3.1 데이터 모델

**DailyRankingEntry.java** (`services/src/main/java/.../services/rankings/`)
```java
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
    public String gameData;  // JSON 문자열
    
    public static boolean networkFailed = false; // 네트워크 상태 추적
}
```

### 3.2 서비스 레이어

**RankingService.java** (`services/src/main/java/.../services/rankings/`)
- 추상 클래스, 점수 제출 및 조회 메서드 정의
- `submitDailyScore()`: 점수 제출
- `getDailyRankings()`: 특정 날짜의 랭킹 조회

**SupabaseRankingService.java** (`services/rankings/supabaseRankings/src/main/java/`)
- RankingService 구현체
- Supabase REST API 호출
- JSON 직렬화/역직렬화
- 네트워크 실패 시 `DailyRankingEntry.networkFailed` 플래그 설정

**Ranking.java** (`core/src/main/java/.../services/rankings/`)
- 싱글톤 wrapper
- 캐싱 로직 (`CHECK_DELAY = 3초`)
- `checkForRankings(date, force)`: force=true이면 캐시 무시
- `clearRankings()`: 캐시 클리어
- `isLoading()`, `hasChecked()`: 로딩 상태 확인

### 3.3 UI 레이어

**AboutScene.java** (`core/src/main/java/.../scenes/`)
- 경쟁 모드 랭킹 메인 화면
- 자동 새로고침 (3초마다)
- 로딩/에러/데이터 없음 상태 구분
- RankingRow 클릭 시 상세 정보 표시

**WndRanking.java** (`core/src/main/java/.../windows/`)
- 플레이어 상세 정보 윈도우
- 탭: Stats, Talents, Items, Badges, Challenges
- 일일 도전 랭킹의 경우 타이틀에 플레이어 닉네임 표시

**TalentsPane.java** (`core/src/main/java/.../ui/`)
- 특성 표시 UI
- `Mode.INFO`에서 실제 talents 데이터 기준으로 tier 표시
- 배지가 없어도 데이터가 있으면 tier 3-4 표시

### 3.4 게임 로직

**Rankings.java** (`core/src/main/java/.../)
- `submit()`: 게임 종료 시 점수 저장
- `submitDailyScore()`: 일일 도전 점수를 Supabase에 전송
- `saveGameData()`: Rankings.Record.gameData에 게임 데이터 저장
- `loadGameData()`: gameData를 Dungeon.hero로 복원
- `createRecordFromDailyEntry()`: DailyRankingEntry를 Rankings.Record로 변환

**HeroSelectScene.java** (`core/src/main/java/.../scenes/`)
- 일일 도전 시작 시 닉네임 입력 받기
- 닉네임 검증: 빈 값 차단, 부적절한 단어 필터링
- `SPDSettings.playerNickname()` 저장

**Badges.java** (`core/src/main/java/.../)
- `DAILY_VICTORY` 배지: 일일 도전 클리어 시 획득
- `validateDailyVictory()`: 일일 도전 승리 검증
- `displayBadge()`, `unlock()`: DAILY_VICTORY는 커스텀 시드(일일 도전)에서도 허용

---

## 4. 데이터 흐름

### 4.1 점수 제출 (게임 종료 시)

```
1. 플레이어가 일일 도전에서 게임 종료 (사망 또는 클리어)
   └─ Rankings.submit(win, cause) 호출

2. Rankings.saveGameData(record)
   └─ Dungeon.hero, Statistics, Badges 등을 Bundle로 직렬화
   └─ record.gameData에 저장

3. Rankings.submitDailyScore(record, playerName)
   └─ gameData.toString()으로 JSON 문자열 변환
   └─ Ranking.service.submitDailyScore(..., gameDataJson, ...)

4. SupabaseRankingService.submitDailyScore()
   └─ JSON 본문 생성 (이스케이프 처리)
   └─ POST /rest/v1/daily_rankings
   └─ 성공: Ranking.checkForRankings(date, true) 호출
   └─ 실패: 로그 기록, 게임은 정상 진행
```

### 4.2 랭킹 조회 (AboutScene 진입 시)

```
1. AboutScene.create()
   └─ Ranking.clearRankings() (캐시 클리어)
   └─ Ranking.checkForRankings(currentDate, force=true)

2. Ranking.checkForRankings(date, force)
   └─ force=false이고 CHECK_DELAY 미만이면 건너뛰기
   └─ Ranking.service.getDailyRankings(date, ...)

3. SupabaseRankingService.getDailyRankings()
   └─ GET /rest/v1/daily_rankings?date=eq.{date}&order=score.desc&limit=10
   └─ 성공: DailyRankingEntry 리스트로 파싱
   └─ 실패: DailyRankingEntry.networkFailed = true

4. AboutScene.updateRankingsDisplay()
   └─ Ranking.isLoading() 확인
   └─ 로딩 중: "랭킹 데이터를 불러오는 중입니다."
   └─ 네트워크 실패: "랭킹을 불러오지 못했습니다."
   └─ 데이터 없음: "오늘 도전한 플레이어가 없습니다."
   └─ 데이터 있음: RankingRow 리스트 표시
```

### 4.3 상세 정보 표시 (RankingRow 클릭 시)

```
1. RankingRow.onClick()
   └─ entry.gameData가 있는지 확인

2. Rankings.createRecordFromDailyEntry(entry)
   └─ DailyRankingEntry → Rankings.Record 변환
   └─ gameData JSON 문자열을 Bundle.read()로 역직렬화
   └─ ByteArrayInputStream + Bundle.read()

3. new WndRanking(record)
   └─ Rankings.INSTANCE.loadGameData(record)
   └─ Dungeon.hero 복원
   └─ 탭: Stats, Talents, Items, Badges, Challenges

4. TalentsPane (Mode.INFO)
   └─ 배지 체크 후 실제 talents 데이터 확인
   └─ talents.get(i).isEmpty()가 아니면 해당 tier까지 표시
```

---

## 5. 주요 기능 및 특징

### 5.1 닉네임 시스템
- **입력 위치**: HeroSelectScene - 일일 도전 시작 시
- **최대 길이**: 10자
- **필수 여부**: 경쟁 모드 참가 시 반드시 입력
- **검증**: `containsInappropriateWords()` - 부적절한 단어 필터링
- **저장**: `SPDSettings.playerNickname()`

### 5.2 캐싱 및 새로고침
- **CHECK_DELAY**: 3초 (빠른 업데이트)
- **force 모드**: 딜레이 무시하고 즉시 새로고침
- **자동 새로고침**: AboutScene에서 3초마다 자동으로 체크
- **수동 새로고침**: 날짜 옆 새로고침 버튼 (Icons.RANDOMIZE)

### 5.3 로딩 상태 관리
- **isLoading()**: rankings가 null이면 로딩 중
- **wasLoading 플래그**: 로딩 상태 변경 시 즉시 UI 업데이트
- **LOADING_TIMEOUT**: 10초 (타임아웃 후 "데이터 없음" 처리)
- **networkFailed**: 네트워크 에러와 데이터 없음 구분

### 5.4 전체 게임 데이터 저장
- **gameData 필드**: Rankings.Record의 gameData Bundle을 JSON으로 직렬화
- **저장 내용**:
  - Hero (장비, 인벤토리, 퀵슬롯)
  - Statistics (점수, 골드, 처치한 적 등)
  - Badges (획득한 배지)
  - Challenges (활성화된 시련)
  - Talents (모든 tier의 특성)
- **복원**: Bundle.read(InputStream)으로 JSON → Bundle 변환

### 5.5 배지 시스템
- **DAILY_VICTORY**: 일일 도전(경쟁 모드)에서 천국의 DISC 획득 시
- **특별 처리**: 일일 도전은 커스텀 시드로 취급되지만, DAILY_VICTORY만 예외로 허용
  - `displayBadge()`: `badge != Badge.DAILY_VICTORY` 조건 추가
  - `unlock()`: `badge == Badge.DAILY_VICTORY` 조건 추가

---

## 6. 주요 화면 흐름

### 6.1 일일 도전 시작
```
TitleScene (메인 화면)
  └─ HeroSelectScene (영웅 선택)
      └─ 일일 도전(경쟁 모드) 버튼 클릭
          └─ 닉네임 입력 WndTextInput
              ├─ 빈 문자열: "닉네임을 입력하세요" 경고
              ├─ 부적절한 단어: "부적절한 닉네임" 경고
              └─ 정상: 게임 시작 (InterlevelScene)
```

### 6.2 게임 종료 (사망)
```
GameScene.gameOver()
  └─ Rankings.submit(false, cause)
      └─ Rankings.submitDailyScore() → Supabase 전송
  └─ "플레이어 랭킹 보러 가기" 버튼 표시
      └─ 클릭 시:
          ├─ Ranking.clearRankings()
          ├─ Ranking.checkForRankings(date, true)
          └─ AboutScene으로 이동
```

### 6.3 게임 클리어
```
Amulet 획득 → AmuletScene
  └─ "나가기" 버튼 클릭
      ├─ Rankings.submit(true, Amulet.class)
      │   └─ Badges.validateDailyVictory() → DAILY_VICTORY 배지
      │   └─ Rankings.submitDailyScore() → Supabase 전송
      └─ 1초 딜레이 후 AboutScene으로 이동

지상 탈출 → SurfaceScene
  └─ "나가기" 버튼 클릭
      └─ 1초 딜레이 후 AboutScene으로 이동
```

### 6.4 랭킹 화면 (AboutScene)
```
AboutScene
  ├─ 헤더: 날짜 + 새로고침 버튼
  ├─ 랭킹 리스트 (RankingRow × 10)
  │   ├─ 1~3위: Flare 효과 + 금/은/동색
  │   └─ 클릭 시:
  │       ├─ gameData 있음: WndRanking (전체 상세 정보)
  │       └─ gameData 없음: WndTitledMessage (간단한 정보)
  └─ 하단: 도움말, 개발자 정보 버튼
```

---

## 7. 구현 시 주의사항

### 7.1 Bundle 직렬화
- **Bundle.toString()**: JSONObject.toString()을 호출하여 JSON 문자열 생성
- **Bundle.read(InputStream)**: JSON 문자열 → Bundle 변환
- **org.json import 금지**: core 모듈에서는 org.json 직접 접근 불가
  - ByteArrayInputStream + Bundle.read() 사용

### 7.2 네트워크 에러 처리
- **점수 제출 실패**: 게임은 정상 진행, 로그만 기록
- **랭킹 조회 실패**: "랭킹을 불러오지 못했습니다" (빨간색) 표시
- **데이터 없음**: "오늘 도전한 플레이어가 없습니다" (회색) 표시
- **networkFailed 플래그**: 에러 상태와 데이터 없음 구분

### 7.3 특성(Talents) 표시 문제
- **WndRanking.TalentsTab**: tier 제한 제거 (TalentsPane에서 처리)
- **TalentsPane (Mode.INFO)**:
  - 먼저 로컬 플레이어의 배지로 기본 tier 결정
  - 이후 실제 talents 데이터가 존재하면 해당 tier까지 표시
  - 본인이 배지를 획득하지 않아도 다른 플레이어의 tier 3-4 특성 표시 가능

### 7.4 로딩 시간 최적화
- **점수 제출 후**: `clearRankings()` 대신 `checkForRankings(date, true)` 사용
  - 이전 캐시 유지 → 로딩 화면 없이 바로 표시
- **AboutScene 진입 시**: `clearRankings()` + `checkForRankings(date, true)`
  - 최신 데이터 보장
- **클리어 후 이동**: 1초 딜레이 추가 (점수 제출 완료 대기)

### 7.5 UI/UX 개선
- **순위별 색상**: 1위(금), 2위(은), 3위(동), 4-10위(노랑), 나머지(회색)
- **Flare 효과**: 1-3위에만 표시
- **새로고침 버튼**: 날짜 텍스트 오른쪽에 배치, y축 중앙 정렬
- **구분선**: 각 행 사이에 `ColorBlock separator` (색상: 0xFF2E2E2E)

---

## 8. 디버깅 팁

### 8.1 로그 확인
모든 주요 동작은 `Game.reportException()`으로 로그를 남깁니다:
- 점수 제출 시도/성공/실패
- 랭킹 조회 요청/응답
- JSON 파싱 에러

---

## 9. 요약

이 시스템은 Supabase를 백엔드로 사용하여 일일 도전(경쟁 모드)의 실시간 랭킹을 제공합니다. 
전체 게임 데이터를 JSON으로 직렬화하여 저장하므로, 플레이어는 다른 플레이어의 상세 정보 
(아이템, 특성, 배지 등)를 모두 확인할 수 있습니다. 

네트워크 실패 시에도 게임은 정상적으로 진행되며, 사용자 경험을 해치지 않도록 
로딩 상태, 에러 상태, 데이터 없음 상태를 명확히 구분하여 표시합니다.

**핵심 설계 원칙**:
1. 네트워크 실패에도 게임 진행 가능
2. 실시간 업데이트 (3초 간격)
3. 전체 게임 데이터 저장으로 상세 정보 제공
4. 배지 예외 처리 (DAILY_VICTORY)
5. 깔끔한 UI/UX (색상, 정렬, 메시지)

---

## 110. 빠른 참조

### 주요 메서드
- `Ranking.checkForRankings(date, force)` - 랭킹 조회
- `Ranking.clearRankings()` - 캐시 클리어
- `Rankings.createRecordFromDailyEntry()` - DailyRankingEntry → Record
- `Rankings.saveGameData()` - 게임 데이터 직렬화
- `Rankings.loadGameData()` - 게임 데이터 역직렬화
- `Badges.validateDailyVictory()` - 일일 도전 배지

### 주요 설정
- `Ranking.CHECK_DELAY = 3000` (3초)
- `AboutScene.REFRESH_INTERVAL = 3f` (3초)
- `AboutScene.LOADING_TIMEOUT = 10f` (10초)
- 닉네임 최대 길이: 10자
- 랭킹 표시 개수: 10명

### Supabase 설정
- URL: `https://tbgdnucmfjmvrpvukxeg.supabase.co`
- 테이블: `daily_rankings`
- 인덱스: `idx_daily_rankings_date_score` (date DESC, score DESC)

