# Supabase 경쟁 모드 랭킹 시스템 설정 가이드

이 가이드는 Supabase를 사용하여 경쟁 모드 랭킹 시스템을 설정하는 방법을 설명합니다.

## 1. Supabase 프로젝트 생성

1. [Supabase](https://supabase.com)에 가입하고 로그인
2. "New Project" 클릭
3. 프로젝트 이름과 데이터베이스 비밀번호 설정
4. 리전 선택 (가장 가까운 리전 권장)
5. 프로젝트 생성 완료 대기 (약 2분)

## 2. 데이터베이스 테이블 생성

Supabase 대시보드에서 SQL Editor로 이동하여 다음 SQL을 실행:

```sql
-- 일일 랭킹 테이블 생성
CREATE TABLE daily_rankings (
  id BIGSERIAL PRIMARY KEY,
  date DATE NOT NULL,
  score INTEGER NOT NULL,
  player_name TEXT NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- 인덱스 생성 (조회 성능 향상)
CREATE INDEX idx_daily_rankings_date_score ON daily_rankings(date DESC, score DESC);

-- RLS (Row Level Security) 정책 설정
ALTER TABLE daily_rankings ENABLE ROW LEVEL SECURITY;

-- 모든 사용자가 읽기 가능 (랭킹 조회)
CREATE POLICY "Anyone can read rankings"
  ON daily_rankings FOR SELECT
  USING (true);

-- 모든 사용자가 쓰기 가능 (점수 제출)
CREATE POLICY "Anyone can insert rankings"
  ON daily_rankings FOR INSERT
  WITH CHECK (true);
```

## 3. API 키 확인

1. Supabase 대시보드에서 **Settings** > **API** 이동
2. 다음 정보 확인:
   - **Project URL**: `https://YOUR_PROJECT_ID.supabase.co`
   - **anon/public key**: `YOUR_ANON_KEY`

## 4. 코드에 API 키 설정

`services/rankings/supabaseRankings/src/main/java/com/shatteredpixel/shatteredpixeldungeon/services/rankings/SupabaseRankingService.java` 파일을 열고:

```java
// TODO: Supabase 프로젝트 생성 후 아래 값들을 설정하세요
private static final String SUPABASE_URL = "https://YOUR_PROJECT_ID.supabase.co";
private static final String SUPABASE_ANON_KEY = "YOUR_ANON_KEY";
```

위 부분을 실제 값으로 교체하세요.

## 5. 테스트

1. 게임을 빌드하고 실행
2. 경쟁 모드를 완료하여 점수 제출
3. 랭킹 조회 기능 테스트

## 6. 보안 고려사항

현재 설정은 모든 사용자가 점수를 제출할 수 있습니다. 더 엄격한 보안이 필요하다면:

- **Rate Limiting**: Supabase Edge Functions 사용
- **점수 검증**: 서버 측에서 비정상적인 점수 필터링
- **인증**: 사용자 인증 시스템 추가

## 문제 해결

### 연결 실패
- 인터넷 연결 확인
- Supabase 프로젝트가 활성화되어 있는지 확인
- API 키가 올바른지 확인

### 점수 제출 실패
- Supabase 대시보드에서 테이블이 생성되었는지 확인
- RLS 정책이 올바르게 설정되었는지 확인
- Supabase 로그에서 에러 확인

## 추가 기능

### 닉네임 시스템
사용자가 닉네임을 설정할 수 있도록 하려면:
1. 로컬에 닉네임 저장 (SPDSettings 사용)
2. 점수 제출 시 닉네임 사용

### 랭킹 UI
랭킹을 표시하는 UI 창을 만들려면:
- `WndDailyRanking.java` 생성
- `Ranking.checkForRankings()` 호출
- `Ranking.rankings()`로 데이터 가져오기
- 리스트로 표시

