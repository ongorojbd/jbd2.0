# 덱빌딩 모드 시스템 정리

Shattered Pixel Dungeon에 추가된 슬레이 더 스파이어 스타일 덱빌딩 모드.

- 패키지 루트: `com.shatteredpixel.shatteredpixeldungeon.deckbuilder`
- 주요 씬 루트: `com.shatteredpixel.shatteredpixeldungeon.scenes`
- 콘텐츠 수량은 계속 늘어날 수 있으므로 `DeckCard`, `DeckRelic`, `DeckEnemy`, `DeckPotion` enum을 기준으로 확인한다.
- 마지막 구조 확인: 2026-06-01

---

## 진입 흐름

1. `WndDungeonMode`에서 `DECKBUILDER` 또는 `DECKBUILDER_TUTORIAL` 선택
2. `Dungeon.selectedMode`와 `SPDSettings.deckbuilder` 설정
3. 새 게임 생성 시 `Dungeon.deckbuilderlevel = true`
4. 일반 계단 출구에서 `Level.activateTransition()`이 `DeckBuilderMapScene`으로 전환
5. 맵 노드 선택 후 `Statistics.deckBuilderMapNode`에 노드 타입 저장
6. `InterlevelScene`/각 씬이 노드 타입에 따라 전투, 상점, 이벤트, 휴식, 보물 씬으로 분기

관련 파일:

| 파일 | 역할 |
|------|------|
| `windows/WndDungeonMode.java` | 일반/전투조류/덱빌더/튜토리얼 모드 선택 |
| `Dungeon.java` | `GameMode.DECKBUILDER`, `deckbuilderlevel`, 덱빌더용 레벨 생성 |
| `Level.java` | 덱빌더 모드에서 일반 계단 이동을 맵 씬으로 가로챔 |
| `InterlevelScene.java` | 저장 복원 및 노드 타입별 씬 복귀 |
| `Statistics.java` | 맵 노드, 경로, 맵 배열 저장 |

---

## 주요 패키지 파일

| 파일 | 역할 |
|------|------|
| `DeckBuilderRun.java` | 런 전역 상태, 보상, 상점/휴식/보물 상태 접근점 |
| `DeckBuilderRunBundle.java` | 런 상태 저장/복원 |
| `DeckBuilderCombat.java` | 전투 상태, 턴, 드로우/버림/소멸, 카드 플레이 |
| `DeckBuilderMap.java` | 맵 생성, 링크, 노드 타입 |
| `DeckCard.java` | 카드 enum, 카드 기본 수치와 특수 오버라이드 |
| `DeckCardPool.java` | 보상 카드 풀, 직업/중립/상태/저주 분류 정책 |
| `DeckCardCode.java` | 카드 int code 인코딩/디코딩 |
| `DeckCardEffects.java` | 카드 효과 구현체 |
| `DeckCardText.java` | 카드 설명/키워드 텍스트 |
| `DeckDiscover.java` | 발견 카드 풀 정책 |
| `DeckEnemy.java` | 적 enum, HP/인텐트 패턴 |
| `DeckEnemyIntent.java` | 인텐트 ID 실행 로직 |
| `DeckRelic.java` | 유물 enum 및 획득 효과 |
| `DeckPotion.java` / `DeckPotionPolicy.java` | 포션 enum, 드랍 정책 |
| `DeckRewardPolicy.java` | 카드/골드/유물/포션 보상 생성 |
| `DeckShop.java` / `DeckShopTransaction.java` | 상점 상품과 구매 처리 |
| `DeckRunStart.java` | 새 런 초기화, 시작 유물 선택 |
| `DeckStartingProfile.java` | 직업별 시작 덱 |
| `DeckRunInventory.java` | 덱/유물/포션 조작 유틸 |
| `DeckWandCards.java` | 완드 카드 충전/처리 |

---

## 주요 씬 파일

| 파일 | 역할 |
|------|------|
| `DeckBuilderMapScene.java` | 맵 표시, 노드 선택, 노드별 씬 전환 |
| `DeckBattleScene.java` | 전투 UI, 카드 사용, 적 표시, 전투 보상 |
| `DeckShopScene.java` | 상점 UI |
| `DeckRestScene.java` | 휴식/강화 UI |
| `DeckTreasureScene.java` | 보물 상자 UI |
| `DeckEventScene.java` | 조우 이벤트 UI |
| `DeckRelicChoiceScene.java` | 시작 유물 선택과 pending 유물 이벤트 처리 |
| `DeckRunHud.java` | 덱빌더 런 HUD |
| `DeckRewardWindow.java` | 닫기 방지 보상 창 |
| `DeckRewardRow.java` | 전투 보상 행 UI |

---

## 카드 시스템

### 카드 코드

카드는 단일 `int code`로 저장된다.

| 비트 | 의미 |
|------|------|
| 0-7 | 카드 ID |
| 8-11 | 업그레이드 레벨 |
| 12-15 | 현재 충전량, 주로 완드 카드 |
| 16+ | 런타임 키워드 비트 |

자주 쓰는 API:

```java
DeckCard card = DeckCard.byCode(code);
int id = DeckCard.id(code);
int level = DeckCard.upgradeLevel(code);
int charge = DeckCard.currentCharge(code);
int upgraded = DeckCard.upgrade(code);
int withRetain = DeckCard.withKeyword(code, DeckCardKeyword.RETAIN);
```

### 카드 타입

`DeckCardType`:

- `ATTACK`
- `SKILL`
- `POWER`
- `STATUS`
- `CURSE`

상태/저주 판정은 직접 타입 비교보다 `DeckCardPool` helper를 우선 사용한다.

```java
DeckCardPool.isStatus(card);
DeckCardPool.isCurse(card);
DeckCardPool.isStatusOrCurse(card);
```

### 키워드

`DeckCardKeyword`:

| 키워드 | bit | 의미 |
|--------|-----|------|
| `EXHAUST` | 1 | 사용하면 이번 전투에서 제거 |
| `RETAIN` | 2 | 턴 종료 시 손에 유지 |
| `CAST_ON_DRAW` | 4 | 뽑을 때 자동 시전 |
| `AIM` | 8 | 손패 정중앙에서 사용하면 강화 효과 |
| `THROW` | 16 | 손패 양끝에서 사용하면 강화 효과 |
| `VANGUARD` | 32 | 전투 시작 첫 패에 포함 |
| `TRANSIENT` | 64 | 턴 종료 시 손에 있으면 소멸 |
| `ZERO_COST` | 128 | 임시 비용 0 처리용 내부 키워드 |

`AIM`/`THROW`는 `DeckCard.effectiveCodeForPlay()`에서 조건이 맞으면 일시적으로 업그레이드 레벨을 올려 처리한다.

### 카드 풀

보상 풀 필터링은 `DeckCardPool`로 분리되어 있다.

```java
DeckCardPool.rewardPool(heroClass, classOnly, neutralOnly);
DeckCardPool.rewardFallback(heroClass);
DeckCardPool.isClassCard(card);
DeckCardPool.isNeutralCard(card);
DeckCardPool.isRewardCard(card, heroClass, classOnly, neutralOnly);
```

기존 호환 API도 유지된다.

```java
DeckCard.rewardPool();
DeckCard.rewardPool(heroClass, classOnly, neutralOnly);
DeckCard.rewardFallback(heroClass);
```

보상 카드 조건:

- `card.reward == true`
- 해당 직업의 시작 카드 제외
- `classOnly`면 현재 직업 카드만
- `neutralOnly`면 직업 카드 제외
- 일반 보상 풀은 현재 직업 카드 + 중립 카드

### 카드 그룹

카드는 `DeckCard` enum에 정의된다. 카드 ID는 저장/복원과 호환성에 영향을 주므로, 기존 카드의 ID는 변경하지 않고 새 카드는 뒤에 추가한다.

주요 그룹:

- 공용 기본 카드와 중립 카드
- 직업 카드
- STATUS 카드
- CURSE 카드
- 완드/발견/선택형 카드
- 지속/특수 카드

대표 카드:

- 공용: `VACCINE_SNAKE`, `STAFF`, `RIPPLE_WALL`, `IGNITE`, `MAGIC_MISSILE_WAND`, `DRAMATIC_ENTRANCE`, `PURE`, `HEADBUTT`, `FOOTWORK`, `SURGE`, `CALAMITY`
- HUNTRESS: `SHIV`, `SCORPION_THROW`, `PHANTOM_BLADES`, `ACCURACY`, `KNIFE_TRAP`, `LEADING_STRIKE`, `CLOAK_AND_DAGGER`
- JOHNNY: `ROTATING_NAIL`, `TUSK_EQUIPMENT_DISC`, `TRACKING_BULLET_HOLE`, `SPIN_TRAINING`, `PROUD_STARVER`, `LESSON_FIVE`
- WARRIOR: `WEAKNESS_STAB`, `BARRAGE`, `FLOW_SLASH`, `ACCEL_STAB`, `BARRICADE`, `ENTRENCH`, `RUPTURE`, `BLOODLETTING`, `FIRESEA`, `REND`
- 저주: `RULE_COMPLIANCE`, `STRUGGLE`, `DECAY`, `DEBT`, `WOUND`, `CLUMSINESS`, `SLEEP_DEPRIVATION`, `SHAME`, `SUSPICION`, `GUILT`, `REGRET`, `SPORE_INVASION`

---

## 시작 덱

`DeckStartingProfile`에서 정의한다.

| 직업 | 시작 덱 |
|------|---------|
| WARRIOR | STRIKE 5, GUARD 4, WEAKNESS_STAB, BARRAGE, FLOW_SLASH, ACCEL_STAB, HYPERVENTILATE, BARRICADE, BASH |
| MAGE | STRIKE 5, GUARD 4, MAGE_STAFF |
| HUNTRESS | STRIKE 5, GUARD 4, FOUNDATION_BOX |
| JOHNNY | STRIKE 5, GUARD 4, TUSK_EQUIPMENT_DISC |

시작 카드는 보상 풀에서 제외된다.

---

## 전투 시스템

### 용어 표기

덱빌딩 모드의 표시 텍스트는 아래 명칭을 사용한다.

| 내부/이전 명칭 | 표시 명칭 |
|----------------|-----------|
| 힘, strength | 공격력 |
| 취약, vulnerable | 피해 증폭 |
| 약화, weak | 공격력 저하 |

내부 변수명과 저장 키는 호환성을 위해 `strength`, `vulnerable`, `weak`을 유지할 수 있지만, 카드/포션/유물/버프/로그/강화 미리보기 텍스트에는 표시 명칭을 사용한다.

예외:

- 포션 고유명사 `힘의 물약`은 그대로 사용한다. 효과 설명은 `공격력을 2 얻습니다.`처럼 표시 명칭을 사용한다.

### 기본 상수

`DeckBuilderRun`:

| 상수 | 값 | 의미 |
|------|----|------|
| `STARTING_ENERGY` | 3 | 기본 턴 에너지 |
| `STARTING_HAND_SIZE` | 5 | 턴 시작 드로우 |
| `DEFAULT_MAX_HAND_SIZE` | 10 | 손패 최대 |
| `MAX_ENERGY_CAP` | 10 | 에너지 상한 |
| `MAX_POTION_SLOTS` | 3 | 포션 슬롯 |

### 전투 더미

`DeckBuilderCombat`:

- `drawPile`
- `hand`
- `discardPile`
- `exhaustPile`

### 턴 흐름

1. `startTurnState()`
2. `drawTurnHand()`
3. 플레이어가 `play(handIndex)`로 카드 사용
4. `endTurn()`에서 손패 정리, 적 인텐트 실행, 다음 턴 시작

### 카드 플레이 처리

`DeckBuilderCombat.play()`:

1. 플레이 가능 여부와 에너지 확인
2. `effectiveCodeForPlay()`로 AIM/THROW/임시 키워드 반영
3. `DeckCardEffect.apply()` 실행
4. POWER는 전투 중 제거, EXHAUST는 `exhaustPile`, 나머지는 `discardPile`
5. 처치/유물/카드 특수 후처리

### 피해 연출 원칙

피해 이펙트는 카드/유물/포션 개별 효과에 직접 붙이는 것보다, 실제 피해가 적용되는 공통 경로를 우선 사용한다.

- 적 피해는 `DeckBuilderCombat.damageEnemy(...)`가 실제 피해량을 계산하고 `lastDamageEvents`에 기록한다.
- 플레이어 HP 피해는 `DeckBuilderCombat.loseHP(...)`가 실제 피해량을 계산하고 `lastDamageEvents`에 기록한다.
- `DeckBattleScene`은 카드 투사체로 이미 표시한 공격 피해를 제외하고, 남은 `lastDamageEvents`를 기본 피격 연출로 표시한다.
- 새 카드/유물/포션이 피해를 준다면 가능하면 직접 HP를 깎지 말고 `damageEnemy(...)` 또는 `loseHP(...)`를 호출한다.
- 특수한 투사체/사운드가 필요한 카드만 별도 연출을 추가하고, 기본 피격/피해 숫자 표시는 공통 피해 이벤트 기록을 신뢰한다.

### 연타(다단 히트) 이펙트 시스템

한 카드가 여러 번 타격하는 연타 연출은 `DeckPlayResult.Builder`의 **wave** 메커니즘으로 구현한다.

**wave 개념**

- 효과 내에서 `result.addAttackHit(enemyIndex, dealt)` 를 호출하면 현재 wave 번호에 해당하는 타격이 기록된다.
- `result.nextWave()` 를 호출하면 내부 wave 카운터가 1 증가한다.
- wave 0 타격은 씬에서 즉시 표시, wave 1 이상은 `0.25f × wave` 초 지연 후 순차 표시된다.

**일반 카드 플레이 핸들러 (wave-aware)**

`DeckBattleScene`의 일반 플레이 핸들러는 wave를 자동으로 인식한다.

- wave 0 타격: 즉시 `spawnCardAttack()` 호출.
- wave 1+: `DelayedActionEffect(w * 0.25f, ...)` 로 순차 시전.
- `finishDelay` 는 `maxWave * 0.25f + 0.38f` 로 갱신.

**카드별 별도 핸들러가 있는 경우**

- `ONSLAUGHT`: wave 0..N을 0.25f 간격으로 순차 표시 (별도 핸들러).
- `BARRAGE`: wave 0(즉시) → wave 1(0.38f 지연) 별도 핸들러.
- 위 두 카드는 `return;` 으로 일반 핸들러에 진입하지 않는다.
- 그 외 다단 히트 카드(MALICE, REND, KNIFE_TRAP 등)는 **일반 wave-aware 핸들러**를 통과한다.

**새 연타 카드 추가 패턴**

```java
// 이펙트 내부 — 첫 타격은 nextWave() 없이, 이후 각 타격마다 nextWave() 먼저 호출
result.addAttackHit(combat.enemyIndex(target), dealt); // wave 0
result.nextWave();
result.addAttackHit(combat.enemyIndex(target), dealt2); // wave 1 → 0.25초 후 표시
result.nextWave();
result.addAttackHit(combat.enemyIndex(target), dealt3); // wave 2 → 0.50초 후 표시
```

- 여러 적을 동시 타격(ALL_ENEMIES)하는 경우 한 wave 안에 모두 기록해도 된다.
- 별도 특수 연출이 필요한 경우에만 씬에 카드별 핸들러 추가; 그 외엔 일반 핸들러로 자동 처리된다.

**참조 구현**

- `DeckCardEffects.KnifeTrap` — 소멸 더미 전갈탄 수만큼 nextWave()로 분리해 순차 타격 연출.
- `DeckCardEffects.OnslaughtBonus` — `playerConsecutiveStrike` 수만큼 nextWave().
- `DeckCardEffects.MaliceBonus` — 조건 충족 시 1~2회 추가 타격을 nextWave()로 분리.

**endTurn 중 발생하는 플레이어→적 피해**

`endTurn()` 내부에서 발생한 피해(오렌지 폭탄 폭발 등)는 `damageEnemy()` 호출로 `lastDamageEvents`에 자동 기록된다.

- **wand 경로**: `spawnUnanimatedDamageEvents()`가 이미 호출되어 피해 숫자 표시.
- **simple endTurn 버튼 경로**: `lastOrangeBombTotalDamage > 0`일 때만 `spawnUnanimatedDamageEvents()` 추가 호출.
- 폭발 로그는 `enemyTurnLog()` 내에서 `lastOrangeBombTotalDamage`를 참조해 출력.

### 무작위 카드 자동 시전 시스템

카드 효과로 다른 카드를 자동 시전할 때 사용하는 공통 인프라.

**`DeckPlayResult.isRandomPlay`**  
자동 시전된 결과가 무작위였음을 표시하는 플래그. `true`이면 씬에서 `CardRevealEffect`(카드 이미지 0.6초 팝업)를 먼저 보여준 후 공격 애니메이션을 실행한다.

**`DeckBuilderCombat.playRandomFromDrawPile(int count)`**  
드로우 더미에서 무작위 카드 `count`장을 꺼내 효과를 즉시 적용하고 `lastAutoPlayResults`에 추가. 드로우 더미가 비면 버린 카드 더미를 리셔플해서 계속 진행. STATUS/CURSE/unplayable 카드는 효과 없이 버림. POWER 카드는 이 경로로 시전 시 버린 카드 더미로(powersPlayed 미등록).

**`DeckBuilderCombat.surgeActive`**  
`SURGE` 지속 카드 사용 시 `true`. `endTurn()`에서 손패의 무작위 공격 카드 1장을 무작위 적에게 자동 시전하고 결과를 `lastTurnEndAutoPlayResults`에 추가.

**로그 표시**  
`DeckBattleScene.buildRandomPlayLog(ArrayList<DeckPlayResult>)`로 `isRandomPlay=true` 결과 목록에서 시전 카드명을 모아 `" → 카드명1, 카드명2 시전"` 형태의 접미사를 생성. 재난 시전 시 카드 플레이 로그에 덧붙이고, 쇄도 발동 시 턴 종료 로그 앞에 붙인다.

**새 무작위 시전 카드 추가 시:**
- 드로우 더미 기반: `combat.playRandomFromDrawPile(count)` 재사용
- 손패 기반: `endTurn()`의 `surgeActive` 블록 참고
- `Builder(card, true)` 또는 `builder.isRandomPlay = true` 설정하면 카드 공개 연출과 로그가 자동으로 따라옴

**`CAST_ON_DRAW` 카드도 동일한 카드 공개 연출 적용:**  
`playAutoDrawnCard()`에서 `play()` 결과를 `isRandomPlay = true`로 재구성해 `lastAutoPlayResults`에 추가. 별도 처리 없이 `spawnAutoPlayEffects()`가 자동으로 `CardRevealEffect`를 표시한다.  
→ 새 `CAST_ON_DRAW` 카드를 추가하면 카드 공개 연출은 자동으로 적용됨.

현재 카드:
- `SURGE`(66): 지속, 비용 2(강화 1). 턴 종료 시 손패 무작위 공격 카드 → 무작위 적 시전
- `CALAMITY`(67): 보조, 비용 2(강화 1). 드로우 더미 무작위 카드 2장(강화 3장) 즉시 시전
- `ROTATING_NAIL`(15): `CAST_ON_DRAW` — 뽑을 때 무작위 적에게 피해, 카드 공개 연출 포함

### 전투 중 선택형 카드

두 가지 계열이 있다.

#### 1. 발견(Discover) — 정해진 풀에서 N장 중 1장 선택

`DeckDiscover` + `DeckBattleScene.showDiscoverWindow()` 흐름으로 처리.  
선택한 카드는 손패로 이동하며, 보통 임시(`TRANSIENT`/`EXHAUST`/`ZERO_COST`) 처리.  
현재 카드: `FOUNDATION_BOX`, `WEAPON_DISCOVER`, 각종 포션 발견 효과 등.

#### 2. 카드 더미 선택(Pile Select) — 특정 더미 전체에서 조건에 맞는 카드를 1장 선택

발견과 달리 **전체 목록**을 보여주고 선택하는 방식. 페이지네이션 적용.  
선택 창은 닫기 불가(`onBackPressed` 빈 구현) — 반드시 선택해야 함.  
효과(`DeckCardEffect.apply()`)는 비워두고, **씬 분기로 처리**한다.

구현 방식:
- 카드 사용 직전 해당 더미의 스냅샷을 찍음
- `combat.play()` 후 씬에서 `card == DeckCard.XXX` 분기로 선택 창 호출
- 선택 결과를 더미에서 제거하고 원하는 위치에 삽입

현재 카드 및 참조 구현:

| 카드 | 선택 범위 | 효과 | 참조 메서드 |
|------|-----------|------|------------|
| `HEADBUTT`(박치기, id=25) | 버린 카드 더미 전체 | 선택한 카드를 드로우 더미 맨 앞에 올림 | `showHeadbuttDiscardSelectWindow()` |

> 새 “더미 선택” 카드 추가 시: `showHeadbuttDiscardSelectWindow()`를 참고해 새 메서드를 만들거나, 더미 종류(drawPile/discardPile)와 필터 조건만 바꿔서 재사용한다. 조건 필터가 필요하면 스냅샷 생성 시 필터링하면 됨.

#### 3. 손패 선택 — 손패에서 특정 조건의 카드를 1장 선택

`PURE`(순수, id=24)의 손패 선택 화면/흐름을 재사용한다.  
현재 카드: `PURE`, `MAGE_STAFF`(완드 선택).

#### 공통 원칙

- “발견”이면 → `DeckDiscover` + `showDiscoverWindow()` 재사용
- “손패에서 선택”이면 → `PURE` 흐름 재사용
- “더미(버린/드로우) 전체에서 선택”이면 → `showHeadbuttDiscardSelectWindow()` 참조해 새 메서드 작성
- 포션으로 발견한 임시 카드는 `ZERO_COST`와 필요 시 `TRANSIENT`/`EXHAUST` 런타임 키워드를 붙여 처리한다.

### 전투 종료

전투 승리 시:

- 전투 중 생성된 소멸 더미는 영구 덱에서 제거하지 않음
- `STATUS` 카드는 런 덱에서 제거
- `GUILT`는 전투 종료마다 진행도 증가, 조건 충족 시 제거
- `MEAT_ON_THE_BONE` 등 전투 종료 유물 처리
- `DeckCombatRewardState` 기반 보상 창 표시

---

## 적 시스템

적은 `DeckEnemy` enum에 정의된다. 새 적을 추가할 때는 인텐트 패턴, 보상/노드 배치, 저장/복원 영향을 함께 확인한다.

주요 적:

- `TUTORIAL_DUMMY`
- `GEB_GOD`
- `HORUS`
- `CREAM`
- `CIVIL_WAR`
- `NUKESAKU`
- `SETESH`
- `NDOUL`
- `THE_FOOL`
- `TOWER_OF_GREY`
- `JUDGEMENT`
- `LARGE_SLIME`
- `MEDIUM_SLIME`
- `CLASH`
- `KHNUM`
- `RAMPAGING_BULL`
- `AVDOL`
- `LAGAVULIN`
- `TARKUS`
- `VANILLA_ICE`
- 그 외 `DeckEnemy.java` 참조

`DeckCombatEnemy` 상태 필드:

- `hp`, `ht`
- `intent`
- `vulnerable`
- `strength`
- `block`
- `thorns`
- `platedArmor`
- `artifact`
- `tricky`
- `blockReduction`
- `venom`
- 기타 전투 패턴용 상태

인텐트 실행은 `DeckEnemyIntent.byId(intent).apply(...)`가 담당한다.

음수 인텐트는 `DeckBuilderCombat.RESULT_*` 특수 행동이다.
양수 인텐트는 기본 공격 피해로 해석된다.

### 플레이어 디버프 상태 필드

| 필드 | 용어 | 효과 | 감소 위치 |
|------|------|------|-----------|
| `playerWeak` | **공격력 저하** | 내 공격 카드 피해 25% 감소 | `startTurnState()`에서 `--` |
| `playerBlockReduction` | **방어력 저하** | 보호막 획득 카드 효율 25% 감소 (스택 수 무관 flat) | `endTurn()`에서 `--` |
| `playerDamageReduction` | **유아화** | 내 전체 딜 `(100-N)%`로 감소 | 감소 없음 (전투 내내 지속) |
| `playerEntangle` | **뒤얽힘** | 공격 카드 비용 +1 | `endTurn()`에서 `--` |

**중요:** `playerBlockReduction`은 스택이 쌓여도 항상 flat 25% 감소이고, 스택 수 = 지속 턴 수다. (예: 2스택 = 2턴간 25% 감소, 50%가 아님)

### 인텐트 예고 텍스트 규칙

- **디버프 부여**: `"공격력 저하 N 부여"`, `"방어력 저하 부여 (N턴)"`, `"유아화 부여"`, `"뒤얽힘 N 부여"`
- **덱에 상태이상 카드 삽입**: `"독침 N장 섞어 넣음"`, `"점액투성이 N장 섞어 넣음"`, `"따개비 N장 섞어 넣음"`
- 효과 수치 설명(-25% 등)은 예고 텍스트에 포함하지 않는다.
- `blockReduction(id, amount)` 헬퍼: `amount`가 지속 턴 수이므로 `"방어력 저하 부여 (N턴)"` 형식 사용.

---

## 런 상태

`DeckBuilderRun`은 현재 static 런 상태 저장소다.

핵심 필드:

```java
initialized
playerHP, playerHT
maxEnergy, handSize, maxHandSize
ArrayList<Integer> deck
ArrayList<Integer> relics
ArrayList<Integer> potions
gold
cardRareOffset
potionDropChance
startingRelicChosen
startingRelicChoices
currentCombat
```

상태 객체:

- `DeckShopState shop`
- `DeckTreasureState treasure`
- `DeckRestState rest`
- `DeckCombatRewardState reward`

pending 유물/이벤트 필드:

- `pendingCardTransform`
- `pendingNeutralDiscover`
- `pendingCardReward`
- `pendingCardRemove`
- `pendingCardRemoveCount`
- `pendingCardUpgrade`
- `pendingOtherClassCardReward`
- `pendingRareCardChoice`
- `fishingRodProgress`
- `upgradedCardRewardCount`
- `firstTreasureEmpty`

튜토리얼 필드:

- `tutorialMode`
- `tutorialStep`
- `tutorialMapMessageShown`

---

## 저장/복원

저장 위치:

- 덱빌더 런 상태: `DeckBuilderRunBundle`
- 맵 상태: `Statistics`
- 모드 플래그: `Dungeon`

복원 흐름:

- `Statistics.restoreFromBundle()`에서 맵 배열 복원
- `DeckBuilderRunBundle.restore()`에서 런 상태, 상점/보물/휴식/보상/전투 복원
- `InterlevelScene.restore()`에서 현재 노드와 `currentCombat`에 따라 적절한 씬으로 복귀

주의:

- 새 필드를 `DeckBuilderRun`에 추가하면 `reset()`, `DeckBuilderRunBundle.store()`, `restore()`를 함께 갱신해야 한다.
- 전투 중 저장 가능한 필드는 `DeckBuilderCombat.storeInBundle()`/`restoreFromBundle()`도 같이 확인한다.

---

## 맵 시스템

`DeckBuilderMap`:

| 상수 | 값 |
|------|----|
| `GRID_COLUMNS` | 7 |
| `ACT_COUNT` | 3 |
| `ACT_LENGTH` | 17 |
| `MAP_FLOORS` | 15 |
| `FIRST_DEPTH` | 2 |
| `BOSS_DEPTH` | 17, 1막 보스 호환 상수 |
| `BOSS_COLUMN` | 3 |
| `MAX_DEPTH` | 51 |
| `MAP_VERSION` | 20 |

노드 타입:

| 코드 | 노드 | 이동 씬 |
|------|------|---------|
| 0 | `NONE` | 없음 |
| 1 | `COMBAT` | `DeckBattleScene` |
| 2 | `ELITE` | `DeckBattleScene` |
| 3 | `SHOP` | `DeckShopScene` |
| 4 | `EVENT` | `DeckEventScene` |
| 5 | `REST` | `DeckRestScene` |
| 6 | `BOSS` | `DeckBattleScene` |
| 7 | `TREASURE` | `DeckTreasureScene` |

막 구성:

| 막 | 시작/유물 선택 | 일반 맵 | 보스 |
|----|----------------|---------|------|
| 1막 | 1층 | 2-16층 | 17층 |
| 2막 | 18층 | 19-33층 | 34층 |
| 3막 | 35층 | 36-50층 | 51층 |

보스 클리어 후 다음 막이 있으면:

- `DeckBuilderRun.prepareNextAct()` 호출
- `startingRelicChosen = false`
- 시작 유물 선택 흐름으로 복귀
- 다음 막 첫 일반 맵으로 이어지는 transition 준비

3막 보스(51층)를 클리어하면 덱빌딩 모드 승리로 처리한다.

맵 생성:

- 각 막은 7열, 일반 맵 15층 + 보스층
- 내부 경로 수 `PATH_COUNT = 6`
- 각 막의 일반 맵 시작층부터 보스까지 링크 생성
- 보스층은 중앙 1노드만 사용
- 1층, 18층, 35층은 맵 노드가 없는 막 시작/유물 선택 구간
- 맵 버전이 다르거나 유효하지 않으면 재생성

---

## 보상 시스템

`DeckRewardPolicy`:

- 카드 보상: 기본 4장 선택지
- 첫 선택지는 직업 카드 확률 100%
- 두 번째 60%, 세 번째 20%, 네 번째 0%
- 레어 확률은 `cardRareOffset`으로 조정
- 레어가 나오면 `cardRareOffset = -5`
- 커먼이 많이 나오면 offset 상승

전투 보상 상태:

`DeckCombatRewardState`:

- `gold`
- `relics`
- `potion`
- `cards`
- `goldClaimed`
- `relicClaimed`
- `potionClaimed`
- `cardClaimed`

전투 보상 UI:

- `DeckBattleScene.showCombatRewardWindow()`
- `DeckRewardWindow`: 보상 창 닫기 방지
- `DeckRewardRow`: 골드/유물/포션/카드 보상 행
- `RewardCardButton`: 카드 선택 UI, 아직 `DeckBattleScene` 내부 클래스

---

## 유물 시스템

유물은 `DeckRelic` enum에 정의된다. 새 유물을 추가할 때는 중복 등장 규칙, 보상/상점 풀, 획득 즉시 효과, pending 선택 이벤트, 저장/복원을 함께 확인한다.

`DeckRelicType`:

- `STARTER`
- `PENALTY_STARTER`
- `CLASS_STARTER`
- `COMMON`
- `UNCOMMON`
- `RARE`
- `SHOP`
- `ANCIENT`

시작 유물:

- `STARTER`: 시작 선택지 1-2번 슬롯
- `PENALTY_STARTER`: 시작 선택지 3번 슬롯, 강한 보상 + 패널티
- `CLASS_STARTER`: 직업별 자동 지급

대표 유물:

- 시작: `NEW_LEAF`, `SMALL_CAPSULE`, `LEAD_PAPERWEIGHT`, `LOST_COFFER`, `POMANDER`
- 페널티 시작: `LEAFY_POULTICE`, `STARTER_LARGE_CAPSULE`, `HEFTY_TABLET`, `PRECARIOUS_SHEARS`, `SILVER_CRUCIBLE`, `CURSED_PEARL`, `NEOWS_BONES`
- 일반 보상: `BAG_OF_MARBLES`, `VAJRA`, `ANCHOR`, `LANTERN`, `GREMLIN_HORN`, `BLACK_STAR`, `MEAT_ON_THE_BONE`, `MOLTEN_EGG`
- 상점: `CAULDRON`, `TOOLBOX`, `RINGING_TRIANGLE`, `DOLLYS_MIRROR`, `MEMBERSHIP_CARD`, `MINIATURE_TENT`
- 직업 시작: `WAVE_RUSH`

획득 시 즉시 효과는 `DeckRelic.onAcquire()`에서 처리한다.
선택 UI가 필요한 효과는 pending 필드를 세팅하고 `DeckRelicChoiceScene.processPendingRelicEvent()`에서 순차 처리한다.

유물 중복 규칙:

- 런에서 이미 소지한 유물은 다시 등장하거나 획득되지 않는다.
- `DeckRunInventory.addRelic()`는 이미 소지한 유물이면 추가하지 않고 즉시 반환한다.
- 보상/보물/시작 유물 랜덤 풀은 `DeckBuilderRun.hasRelic(relic)`로 소지 유물을 제외한다.
- 상점 유물 상품도 소지 유물을 제외하며, 같은 상점 안에서 동일 유물을 중복 판매하지 않는다.
- 상점처럼 한 번에 여러 유물을 생성하는 경우, 이번 생성에서 이미 고른 유물 id를 제외 목록으로 넘겨야 한다.
- 시작 유물은 일반 보상/보물/상점 풀에 나오면 안 된다.
- `NUTRITIOUS_OYSTER`, `ARCANE_SCROLL`는 시작 유물과 겹치므로 `rewardPool()`에서 제외한다.

---

## 포션 시스템

포션은 `DeckPotion` enum에 정의된다. 새 포션을 추가할 때는 `DeckPotionPolicy`의 드랍/희귀도 정책과 전투 UI 처리 흐름을 함께 확인한다.

| 포션 | 효과 |
|------|------|
| `HASTE` | 카드 3장 드로우 |
| `FIRE` | 대상 적에게 피해 20 |
| `STRENGTH` | 공격력 2 획득 |
| `GAMBLERS_BREW` | 손패를 원하는 만큼 버리고 같은 수 드로우 |
| `ATTACK` | 무작위 공격 카드 3장 중 1장 발견, 이번 턴 비용 0 |
| `FLEX` | 이번 턴 공격력 5 획득 |
| `COLORLESS` | 무작위 공용 카드 3장 중 1장 발견, 이번 턴 비용 0 |
| `DEXTERITY` | 방어력 증가 2 획득 |
| `BLOCK` | 보호막 12 획득 |
| `SPEED` | 이번 턴 방어력 증가 5 획득 |
| `SKILL` | 무작위 보조 카드 3장 중 1장 발견, 이번 턴 비용 0 |
| `WEAK` | 대상 적에게 공격력 저하 3 부여 |
| `ENERGY` | 에너지 2 획득 |
| `VULNERABLE` | 대상 적에게 피해 증폭 3 부여 |
| `POWER` | 무작위 지속 카드 3장 중 1장 발견, 이번 턴 비용 0 |
| `FORTIFIER` | 현재 보호막을 3배로 만듦 |
| `TOUCH_OF_INSANITY` | 손패 1장을 선택해 이번 전투 동안 비용 0 |
| `RADIANT_TINCTURE` | 에너지 1 획득, 다음 3턴 에너지 +1 |
| `CLARITY_EXTRACT` | 카드 1장 드로우, 다음 3턴 시작 시 추가 1장 드로우 |
| `CURE_ALL` | 에너지 1 획득, 카드 2장 드로우 |
| `HEART_OF_IRON` | 재생 7 획득 |
| `FYSH_OIL` | 공격력 1, 방어력 증가 1 획득 |
| `DUPLICATOR` | 이번 턴 다음 카드 1번 추가 사용 |
| `BINDING` | 모든 적에게 공격력 저하 1, 방어력 저하 1 부여 |
| `STABLE_SERUM` | 손패 보존 2턴 |
| `LIQUID_BRONZE` | 반격 3 획득 |
| `FORGE_BLESSING` | 손의 모든 카드를 강화 |
| `REGEN` | 체력 8 회복 |
| `POWDERED_DEMISE` | 대상에게 턴 종료 체력 손실 9 부여 |

드랍 확률은 `DeckPotionPolicy`와 `DeckBuilderRun.potionDropChance`가 관리한다.

---

## 이벤트 시스템

### 구현 파일

`DeckEventScene.java` 단일 파일에 모든 이벤트 로직이 들어 있다.

### 이벤트 타입 결정

`DeckEventScene.eventType()`에서 시드, 깊이, 맵 경로를 해시해 이벤트 풀에서 결정적으로 1종을 선택한다.

이벤트 메타데이터는 `DeckEventScene.EVENT_DEFS`의 `DeckEventDef` 배열에 모여 있다.

`DeckEventDef`가 담당하는 것:

- 이벤트 ID
- 표시 이름
- 설명 본문
- 아이콘
- 호스트 스프라이트
- 최소 골드 조건
- 낮은 HP 조건

- 기본 풀 이벤트는 모든 막에서 등장한다.
- 골드/체력 조건이 있는 이벤트는 `DeckEventDef.available()`이 true일 때만 풀에 들어간다.
- `DeckBuilderRun.lastEventType`을 풀에서 제거해 같은 이벤트가 연속으로 나오지 않게 한다.

### 현재 이벤트 목록

현재 `DeckEventScene` 이벤트는 20종이다.

| 상수 | 이름 | 등장 조건 | 설명 |
|------|------|-----------|------|
| `UPGRADE_SHRINE = 0` | 강화 성소 | 기본 | 덱에서 카드 1장 선택 → 강화 |
| `PURIFIER = 1` | 정화 성소 | 기본 | 덱에서 카드 1장 선택 → 제거 |
| `TRANSMOGRIFIER = 2` | 변환 성소 | 기본 | 덱에서 카드 1장 선택 → 무작위 카드로 변화 |
| `GOLDEN_SHRINE = 3` | 황금 성소 | 골드 50 이상 | 100골드 또는 275골드+후회 |
| `BLUE_WOMAN = 4` | 파란 옷의 여자 | 골드 50 이상 | 골드로 무작위 포션 1/2/3개 구매 |
| `LABORATORY = 5` | 연구실 | 기본 | 무작위 포션 3개 획득 |
| `DUPLICATOR = 6` | 복제 성소 | 기본 | 덱에서 카드 1장 선택 → 복제 |
| `SHINING_LIGHT = 7` | 밝은 빛 | 기본 | 최대 HP 20% 손실, 무작위 카드 2장 강화 |
| `CLERIC = 8` | 성직자 | 골드 35 이상 | 35골드 회복 또는 50골드 카드 제거 |
| `WORLD_OF_GOOP = 9` | 끈적이 천지 | 골드 50 이상 | 75골드+11HP 손실 또는 20~50골드 손실 |
| `LIVING_WALL = 10` | 살아있는 벽 | 기본 | 제거/변화/강화 중 1개 선택 |
| `BIG_FISH = 11` | 월척 | 기본 | 회복/최대 체력+5/유물+후회 중 1개 선택 |
| `SHAPESHIFTER_FOREST = 12` | 변성체의 숲 | 골드 100 이상 | 모든 골드 손실+무작위 카드 2장 변화 또는 최대 체력+5 |
| `UNREST_SITE = 13` | 불안한 휴식 장소 | 현재 HP 70% 미만 | 전체 회복+수면 부족 또는 최대 체력 -8+무작위 유물 |
| `THIS_OR_THAT = 14` | 이거 아님 저거? | 기본 | HP -6+41~68골드 또는 서투름+무작위 유물 |
| `JUNGLE_MAZE_ADVENTURE = 15` | 정글 미로 탐험 | 기본 | 135~165골드+HP -18 또는 35~65골드 |
| `AROMA_OF_CHAOS = 16` | 혼돈의 향기 | 기본 | 카드 1장 변화 또는 카드 1장 강화 |
| `DOORS_OF_LIGHT_AND_DARK = 17` | 빛과 어둠의 문 | 기본 | 무작위 카드 2장 강화 또는 카드 1장 제거 |
| `MAUSOLEUM = 18` | 영묘 | 기본 | 무작위 유물 1개, 50% 확률로 몸부림 또는 떠나기 |
| `WHISPERING_HOLLOW = 19` | 속삭이는 골짜기 | 골드 50 이상 | 50골드로 무작위 포션 2개 또는 HP -9+카드 1장 변화 |

### UI 구조

```
EventChoiceButton("기도", actionText, accentColor)  → showCardSelection()
EventChoiceButton("떠난다", ...)                    → leaveEvent()
```

- **`EventChoiceButton`**: 라벨+설명+왼쪽 색상 강조선 버튼
- **`showCardSelection(page)`**: 페이지네이션 카드 선택 창 (`CardChoiceButton` 4장/페이지)
- **`showCardConfirmWindow(...)`**: 카드 상세+강화 미리보기+최종 확인 창
- **`selectableDeckIndices()`**: `UPGRADE_SHRINE`이면 최대 업그레이드 카드 제외, 나머지는 전체
- **`resolved` 플래그**: 중복 실행 방지

완료/스킵 후 `leaveEvent()` → `DeckBuilderMapScene`으로 복귀.

### 이벤트 추가 패턴

현재 구조는 이벤트 메타데이터를 `EVENT_DEFS`로 모으고, 선택지/효과 실행만 전용 메서드에 둔다.

이벤트를 추가하면 보통 아래 위치를 갱신한다.

- 이벤트 상수
- `EVENT_DEFS`에 `DeckEventDef` 추가
- `addEventButtons()` 분기
- 전용 `addXxxButtons()` 메서드

새 이벤트가 새 스프라이트 계열을 써야 한다면:

- `HOST_*` 상수 추가
- `DeckEventDef.host()` switch에 생성 분기 추가

카드 선택이 필요한 이벤트:
- `showCardSelection()` / `showCardConfirmWindow()` 흐름 재사용
- `selectableDeckIndices()`에 필터 조건만 추가

카드 선택이 없는 이벤트(고정 효과, 조건 분기 등):
- "기도" 버튼 `onClick()`에서 직접 효과 적용 후 `leaveEvent()` 호출
- 선택지가 2개 이상이면 `EventChoiceButton`을 추가하면 됨

현재 구조는 기존보다 확장 지점이 줄었지만, 선택지 action은 아직 `DeckEventScene` 내부 메서드에 남아 있다. 이벤트가 더 늘면 다음 리팩토링 후보는 `EventChoice` 정의 객체와 action 분리다.

---

## 상점/보물/휴식

상점:

- `DeckShop`
- `DeckShopState`
- `DeckShopTransaction`
- `DeckShopBalancePolicy`
- 카드 구매, 유물 구매, 카드 제거
- `shopRemoveCount`로 제거 가격 증가
- 유물 상품은 총 3개를 생성한다.
- 보상 유물 풀(`COMMON`/`UNCOMMON`/`RARE`, `rewardPool() == true`)에서 2개를 생성한다. 희귀도 롤은 보상 유물 희귀도 롤과 동일하다.
- 상점 유물 풀(`DeckRelicType.SHOP`)에서 1개를 생성한다. 상점 유물은 희귀도 롤을 하지 않고 상점 유물 풀에서 무작위로 1개를 뽑는다.
- 상점 유물 상품은 이미 소지한 유물과 같은 상점 내 이미 나온 유물을 제외한다.
- 보상 유물 판매가: 일반 143-157G, 특별 238-262G, 희귀 285-315G.
- 상점 유물 판매가: 143-157G.
- 덱빌딩 모드의 상점 가격과 전투 밸런스는 챌린지/승천 개수에 따른 보정을 받지 않는다.

보물:

- `DeckTreasureState`
- 상자 등급에 따라 유물 희귀도 결정
- `firstTreasureEmpty`가 있으면 첫 보물 상자 비움

휴식:

- `DeckRestState`
- 회복 또는 카드 강화
- `MINIATURE_TENT` 보유 시 선택지 둘 다 사용 가능
- 강화 대상은 상태/저주 제외

---

## 튜토리얼 모드

`Dungeon.GameMode.DECKBUILDER_TUTORIAL`:

- `DeckBuilderRun.tutorialMode = true`
- 전투 적은 `TUTORIAL_DUMMY`
- `tutorialStep`으로 공격/보조/지속 카드 사용 순서 제한
- `DeckBattleScene`에서 튜토리얼 메시지와 손패 하이라이트 표시
- 전투 보상 안내 후 튜토리얼 런 종료

튜토리얼 메시지/하이라이트 관련 메서드:

- `showTutorialTurnPrompt()`
- `tutorialAllowsCard()`
- `advanceTutorialAfterCard()`
- `showTutorialRewardPrompt()`
- `refreshTutorialHighlight()`

---

## 개발 패턴

### 카드 추가

1. `DeckCard` enum에 카드 추가
2. 기본 수치로 표현 안 되면 `DeckCardEffects`에 효과 추가
3. 설명이 어색하면 `DeckCardText` 또는 effect의 `rulesText()` 확인
4. 보상 카드면 `reward = true`
5. 직업 카드면 `deckClass` 지정
6. 시작 카드면 `DeckStartingProfile`에 추가
7. 카테고리 필요 시 `DeckCardCategoryTable` 갱신

### 유물 추가

1. `DeckRelic` enum에 추가
2. 타입과 희귀도 지정
3. 즉시 효과는 `onAcquire()` 구현
4. UI 선택이 필요하면 `DeckBuilderRun.pending*` 필드와 `DeckRelicChoiceScene.processPendingRelicEvent()` 갱신
5. 새 런 상태가 필요하면 `DeckBuilderRunBundle` 저장/복원 추가

### 전투 상태 추가

1. `DeckBuilderCombat` 필드 추가
2. `startTurnState()`, `endTurn()`, `play()` 중 필요한 위치 연결
3. 저장 필요 시 `storeInBundle()`/`restoreFromBundle()` 갱신
4. UI 표시 필요 시 `DeckBattleScene`의 버프/상태 버튼 쪽 갱신

### 선택 UI 추가/재사용

1. 카드를 발견하는 효과는 `DeckDiscover` 풀 정책을 추가하거나 재사용하고, `DeckBattleScene.showDiscoverWindow()` 흐름으로 처리한다.
2. 손패 카드를 고르는 효과는 ID 24번 `PURE`(순수)의 손패 선택 화면/흐름을 재사용한다.
3. 포션/유물/카드마다 같은 목적의 선택 UI를 새로 만들지 않는다.

### 새 씬 상태 추가

1. 상태 객체를 deckbuilder 패키지에 둔다.
2. `DeckBuilderRun`에 static state 추가
3. `DeckBuilderRunBundle`에 저장/복원 추가
4. 복원 후 씬 분기는 `InterlevelScene.restore()` 확인

---

## 빠른 참조

```java
DeckBuilderRun.initIfNeeded();
DeckBuilderRun.addCard(DeckCard.BASH);
DeckBuilderRun.addRelic(DeckRelic.VAJRA);
DeckBuilderRun.addPotion(DeckPotion.HASTE);

DeckBuilderCombat combat = DeckBuilderRun.combatForNode(DeckBuilderMap.COMBAT);
DeckPlayResult result = combat.play(handIndex);
int damageTaken = combat.endTurn();

DeckCard card = DeckCard.byCode(code);
int upgraded = DeckCard.upgrade(code);
boolean badCard = DeckCardPool.isStatusOrCurse(card);

DeckCard[] pool = DeckCardPool.rewardPool(DeckBuilderRun.heroClass(), false, false);
boolean hasRelic = DeckBuilderRun.hasRelic(DeckRelic.BLACK_STAR);
```

---

## 현재 정리 상태

최근 구조 정리:

- 카드 보상 풀/분류 로직을 `DeckCardPool`로 분리
- 전투 보상 창 공통 UI를 `DeckRewardWindow`, `DeckRewardRow`로 분리
- `DeckBattleScene`에는 아직 카드 렌더링, 전투 애니메이션, 보상, 튜토리얼 코드가 많이 남아 있음

다음에 분리하기 좋은 후보:

- `RewardCardButton` / `CardViewButton` 카드 렌더링 공통화
- 튜토리얼 메시지/하이라이트 컨트롤러 분리
- 전투 보상 창 전체를 별도 helper/window로 이동
- 카드 목록을 enum 단일 파일에서 데이터/효과/풀 정책으로 더 분리

---

## 검증

구조 변경 후 최소 검증:

```powershell
.\gradlew.bat :core:compileJava
```

현재 문서 갱신 직전 코드 기준으로 `:core:compileJava` 성공 확인.

---

## 2026-06-01 희귀 포션 구현 메모

2026-06-01 구현 당시 `DeckPotion` 스냅샷은 총 45종이었다. 정확한 최신 수량은 `DeckPotion` enum을 기준으로 확인한다.

2026-06-01 추가 일반 포션:

- `EXPLOSIVE_AMPHULE` 폭발성 앰플: 모든 적에게 피해 10.

추가된 희귀 등급 포션:

- `GIGANTIFICATION` 거대화 포션: 다음에 사용하는 공격 카드 피해량 3배.
- `FRUIT_JUICE` 과일 주스: 최대 체력 +5, 현재 체력도 +5.
- `BEETLE_JUICE` 딱정벌레 주스: 다음 4턴 동안 적 공격 피해 30% 감소.
- `MAZALETHS_GIFT` 마잘레스의 선물: 턴 시작 시 공격력 +1.
- `BOTTLED_POTENTIAL` 병 속의 가능성: 손/버린 더미/소멸 더미까지 뽑을 카드 더미에 섞고 5장 드로우.
- `SHIP_IN_A_BOTTLE` 병 속의 배: 즉시 보호막 10, 다음 턴 시작 시 보호막 10.
- `FAIRY_IN_A_BOTTLE` 병 속의 요정: 사망 판정 시 포션 슬롯에서 자동 제거되고 최대 체력의 30%만큼 회복.
- `SHACKLING` 속박 포션: 이번 턴 동안 모든 적 공격력 -7.
- `SNECKO_OIL` 스네코 기름: 7장 드로우 후 이번 턴 손의 카드 비용을 0~3으로 무작위 변경.
- `LIQUID_MEMORIES` 액상 기억: 버린 카드 더미 선택 UI로 1장을 손으로 가져오고 비용 0 부여.
- `ENTROPIC_BREW` 엔트로피의 영액: 현재 포션을 제거한 뒤 빈 슬롯을 무작위 포션으로 채움.
- `PRECOGNITION_DROPLET` 예견의 방울: 뽑을 카드 더미 선택 UI로 1장을 손으로 가져옴.
- `OROBIC_ACID` 오로바스산: 무작위 공격/보조/지속 카드를 각각 1장씩 손에 추가하고 `ZERO_COST`, `TRANSIENT`, `EXHAUST` 부여.
- `DISTILLED_CHAOS` 정제된 혼돈: `DeckBuilderCombat.playTopFromDrawPile(3)`로 뽑을 카드 더미 위에서부터 3장을 자동 시전하고 `CardRevealEffect` 로그를 재사용.
- `LUCKY_TONIC` 행운의 활력소: 다음 적 턴/턴 종료 처리 중 HP 손실을 0으로 만듦.

선택형 포션 규칙:

- 카드 발견형 포션은 기존 `DeckDiscover` + `showDiscoverWindow()` 흐름을 사용한다.
- 손패 선택형 포션은 `PURE`(id 24) 계열 손패 선택 흐름을 재사용한다.
- 더미 선택형 포션은 `showHeadbuttDiscardSelectWindow()` 계열과 동일한 카드 버튼/페이지 UI를 쓰되, 대상 더미와 선택 후 동작만 분리한다.

---

## 카드 이펙트 규칙

- 카드 효과가 손패/더미의 카드를 소멸시키면 실제 소멸 처리와 함께 `ExhaustEffect` 계열 시각 효과도 표시한다.
- 카드 효과가 체력을 회복시키면 실제 회복량을 `DeckPlayResult.heal`에 기록하고, 전투 씬에서 회복 이펙트와 회복량 텍스트를 표시한다.

---

## 덱빌더 버프/디버프 효과 정리

전투 상태 효과를 추가하거나 수정할 때는 `DeckBuilderCombat`/`DeckCombatEnemy`의 상태 필드, 저장/복원, 턴 감소 타이밍, `DeckBattleScene`의 버프 아이콘 표시를 함께 확인한다.

| 표시명 | 대상 | 내부 필드 | 효과 | 감소/소모 타이밍 | UI 아이콘 |
|---|---|---|---|---|---|
| 공격력 | 플레이어 | `playerStrength + playerTurnStrength` | 공격 카드 피해 증가 | `playerTurnStrength`는 턴 종료 시 제거 | `PLAYER_STATUS_BUFFS` |
| 방어력 증가 | 플레이어 | `playerDexterity` | 카드로 얻는 보호막 증가 | 지속 | `PLAYER_STATUS_BUFFS` |
| 공격력 저하 | 플레이어 | `playerWeak` | 공격 카드 피해 25% 감소 | 턴 시작 시 1 감소 | `PLAYER_STATUS_BUFFS` |
| 방어력 저하 | 플레이어 | `playerBlockReduction` | 보호막 획득량이 스택당 25% 감소 | 턴 종료 시 1 감소 | `PLAYER_STATUS_BUFFS` |
| 정화의 보호막 | 플레이어 | `playerArtifact` | 상태이상을 받을 때 1 소모하고 무효화 | `applyPlayerDebuff()`에서 소모 | `PLAYER_STATUS_BUFFS` |
| 축복 | 플레이어 | `playerBlessed` | 체력 피해를 받을 때 피해를 1로 제한 | 턴 시작 시 1 감소 | `PLAYER_STATUS_BUFFS` |
| 얽힘 | 플레이어 | `playerEntangle` | 공격 카드 비용 +1 | 턴 종료 시 1 감소 | `PLAYER_STATUS_BUFFS` |
| 유아화 | 플레이어 | `playerDamageReduction` | 공격 카드 피해 30% 감소 | 별도 지속값 | `PLAYER_STATUS_BUFFS` |
| 반격 | 플레이어 | `playerThorns` | 적이 체력 피해를 주면 반격 피해 | 턴 종료 후 0 | `PLAYER_STATUS_BUFFS` |
| 피해 증폭 | 적 | `vulnerable` | 받는 공격 피해 1.5배 | 적 행동 후 1 감소 | `ENEMY_STATUS_BUFFS` |
| 공격력 저하 | 적 | `attackDown` | 공격 피해 25% 감소 | 적 행동 후 1 감소 | `ENEMY_STATUS_BUFFS` |
| 방어력 저하 | 적 | `blockReduction` | 보호막 획득량 감소 | 현재 적 상태 표시 | `ENEMY_STATUS_BUFFS` |
| 정화의 보호막 | 적 | `artifact` | 상태이상을 받을 때 1 소모하고 무효화 | `applyEnemyDebuff(enemy)`에서 소모 | `ENEMY_STATUS_BUFFS` |
| 축복 | 적 | `blessed` | 체력 피해를 받을 때 피해를 1로 제한 | 적 행동 후 1 감소 | `ENEMY_STATUS_BUFFS` |
| 재생 | 적 | `platedArmor` | 턴 종료 시 보호막 획득, 체력 피해를 받으면 1 감소 | 적 턴 처리 | `ENEMY_STATUS_BUFFS` |
| 까다로움 | 적 | `tricky` | 다음 체력 피해를 1로 제한 | 피해를 받을 때 소모 | `ENEMY_STATUS_BUFFS` |

주의:

- 플레이어에게 상태이상을 부여하는 효과는 직접 `playerWeak++`처럼 더하기보다 가능하면 `applyPlayerDebuff()`를 거쳐 정화의 보호막을 소모/무효화한다.
- 적에게 상태이상을 부여하는 효과는 `applyEnemyDebuff(enemy)`를 거쳐 정화의 보호막을 소모/무효화한다.
- 새 버프/디버프 필드를 추가하면 `DeckBuilderCombat.storeInBundle()`/`restoreFromBundle()`에 저장/복원을 추가한다.
- UI 표시가 필요한 상태는 `DeckBattleScene.PLAYER_STATUS_BUFFS` 또는 `ENEMY_STATUS_BUFFS`에 추가한다.