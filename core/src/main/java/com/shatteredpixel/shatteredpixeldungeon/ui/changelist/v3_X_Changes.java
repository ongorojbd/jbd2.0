/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2022 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.ui.changelist;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.DM300;
import com.shatteredpixel.shatteredpixeldungeon.effects.BadgeBanner;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.ChangesScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DM300Sprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HermitCrabSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HeroSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.RollerSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIcon;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.TalentIcon;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.Image;

import java.util.ArrayList;

public class v3_X_Changes {

    public static void addAllChanges(ArrayList<ChangeInfo> changeInfos) {
        add_Coming_Soon(changeInfos);
        add_v3_1_Changes(changeInfos);
        add_a_Changes(changeInfos);
        add_jolyne_Changes(changeInfos);
    }

    public static void add_Coming_Soon(ArrayList<ChangeInfo> changeInfos) {

        ChangeInfo changes = new ChangeInfo("곧 출시 예정", true, "");
        changes.hardlight(0xCCCCCC);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(Icons.get(Icons.SHPX), "개요",
                "다음 주요 업데이트는 v3.0c로, 투척 무기의 대대적인 개편을 포함할 예정입니다!\n" +
                        "\n" +
                        "7월 중순이나 말경에 소식을 전해드릴 수 있을 것으로 예상됩니다."));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.THROWING_SPEAR), "투척 무기 개편",
                "v3.0c에서 계획된 가장 큰 콘텐츠 변화는 투척 무기 개편입니다!\n" +
                        "\n" +
                        "계획은 투척 무기가 사격 DISC처럼 작동하도록 하는 것입니다. 고정된 수량을 가진 '세트'로 드롭되지만, 하나의 단위로 업그레이드할 수 있게 됩니다. 이렇게 하면 더 이상 하나씩 업그레이드할 필요가 없어져 투척 무기에 업그레이드를 투자하는 것이 훨씬 더 매력적이 될 것입니다."));

		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.JOJO7), "???",
                "스틸 볼 런 애니화 개봉일에 맞춘 추가 업데이트가 있을 예정입니다."));

    }

    public static void add_v3_1_Changes(ArrayList<ChangeInfo> changeInfos) {

        ChangeInfo changes = new ChangeInfo("v3.0b", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(CharSprite.POSITIVE);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.SEAL), "죠나단 미니 리워크",
                "죠나단의 파문의 보호막 능력이 소규모 개편되었습니다!\n" +
                        "\n" +
                        "파문의 보호막은 이제 죠나단의 체력이 50% 이하일 때 재사용 대기시간과 함께 발동됩니다. 신규 플레이어들에게는 여전히 사용하기 쉬우면서도 더욱 임팩트 있고 상호작용적으로 느껴질 것입니다.",

                "파문의 보호막의 상세 변경사항은 다음과 같습니다:\n" +
                        "- 파문의 보호막은 더 이상 수동적으로 쌓이지 않으며, 이제 죠나단이 체력 50% 이하로 피해를 받기 직전에 즉시 발동됩니다.\n" +
                        "- 최대 보호막은 이제 브로치 등급에 기반하며, 5-13까지 상승합니다.(파문의 호흡 특성으로 최대 15).\n" +
                        "- 이 보호막은 감소하지 않지만, 전투 후 잠시 후에 사라집니다.\n" +
                        "- 이 보호막은 150턴의 재사용 대기시간을 가지며, 사용하지 않은 보호막은 재사용 대기시간의 최대 50%를 돌려줍니다.\n" +
                        "- 파문의 보호막은 은 이제 저주받지 않은 것으로 알려진 브로치에 부여할 수 있습니다.\n" +
                        "- 파문의 보호막을 교체할 때, 죠나단은 이제 파문의 보호막을 교체할지 묻는 안내를 받습니다.",

                "다양한 죠나단의 특성들이 조정되었습니다:\n" +
                        "- 더 패션 특성은 이제 어떤 보호막이 깨져도 발동되며, +2/+3에서 +3/+5 추가 피해를 부여합니다.\n" +
                        "- 파문의 호흡 특성은 변경되지 않았으며, 여전히 1 또는 2 최대 보호막을 부여합니다.\n" +
                        "- 파문의 양극 특성은 이제 파문의 보호막을 최대 50%/75%를 충전하는 대신 최대 HP의 6.5%/10%와 일반 보호막을 부여합니다.\n" +
                        "- 파문의 회복력 특성은 이제 파문의 보호막을 재충전하는 대신 파문의 보호막의 재사용 대기시간을 감소시킵니다.\n" +
                        "- 각성의 파문전사는 콤보가 있는 한 파문의 보호막의 보호막을 유지합니다.\n" +
                        "- 용기의 파문전사의 파문 에너지는 이제 자체적인 별도 보호막으로, 파문의 보호막의 최대 보호막과는 별도로 증가합니다."));

        changes.addButton(new ChangeButton(Icons.STAIRS.get(), "새로운 방과 지형 유형",
                "이번 업데이트는 새로운 방 유형이 포함되어 있습니다!\n" +
                        "\n" +
                        "- 새로운 지형과 장식물이 각 지역에 추가되었습니다!\n" +
                        "- 5개의 새로운 표준 방이 이러한 새로운 장식물을 사용하여 추가되었으며, 지역당 하나씩입니다.\n" +
                        "- 8개의 기존 표준 방이 새로운 장식물을 사용하도록 수정되었습니다.\n" +
                        "- 10개의 새로운 입구/출구 변형 표준 방도 추가되었습니다. 지역당 두 개씩입니다.\n" +
                        "- 보스 방도 마찬가지로 몇 곳에서 이러한 새로운 지형 유형을 사용합니다\n" +
                        "- 평범한 빈 방은 더 이상 일반적으로 생성되지 않습니다."));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.FERRET_TUFT), "신규 위험한 물건",
                "신규 위험한 물건이 게임에 추가되었습니다!\n" +
                        "\n" +
                        "6부의 스탠드인 구구 돌즈가 새로 추가되었습니다!\n" +
                        "\n" +
                        "구구 돌즈를 강화하면 적을 포함한 모든 캐릭터가 추가 회피율을 얻게 됩니다! 손해처럼 보일 수 있지만, 적의 회피력에 대응하는 방법은 많다는 점을 기억하세요."));

        changes.addButton(new ChangeButton(BadgeBanner.image(Badges.Badge.TAKING_THE_MICK.image), "새로운 배지",
                "v3.0b에는 각각 특정 도전을 테마로 한 4개의 새로운 배지도 포함되어 있습니다:\n" +
                        "\n" +
                        "- 무엇보다도 안전이 제일은 적에 대해 지형을 사용하는 것을 요구하는 골드 등급 배지입니다.\n" +
                        "- 등가교환의 댓가는 한 번에 많은 버프/디버프를 가지는 것을 요구하는 플래티넘 등급 배지입니다.\n" +
                        "- 왓 어 원더풀 월드는 적을 죽이지 않고 역행에서 생존하는 것을 요구하는 다이아몬드 등급 배지입니다.\n" +
                        "- Lesson 5는 이걸 위해..!는 매우 높은 강화 수치의 손톱탄으로 25층의 보스를 물리치는 것을 요구하는 다이아몬드 등급 배지입니다.\n" +
                        "\n" +
                        "리얼리티 수집 배지의 난이도도 줄였습니다. 이제 모든 희귀 적이 아닌 10종류의 희귀 적을 발견하는 것을 요구합니다."));

        changes.addButton(new ChangeButton(new Image(new HermitCrabSprite()), "새로운 희귀 적",
                "2마리의 새로운 희귀 적이 카이로 사막에 추가되었습니다:\n" +
                        "\n" +
                        "날뛰는 소는 예외적으로 강하지만, 전투를 경계합니다. 도발하지 않으면 공격하지 않으므로 그냥 지나가게 할 수 있지만, 희귀한 전리품을 가지고 있을 수도 있습니다...\n" +
                        "\n" +
                        "머라이어는 자력을 사용하는 까다로운 적입니다. 싸우기는 다소 어렵지만, 브로치를 드롭할 확률을 가지고 있습니다."));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(CharSprite.WARNING);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(Icons.get(Icons.RANKINGS), "탐험 및 퀘스트 점수",
                "다양한 점수 카테고리의 난이도 균형을 조금 더 맞추기 위해 점수 계산 방식을 조정했습니다:\n" +
                        "\n" +
                        "탐험 점수는 이제 완전히 탐험되지 않은 방의 수에 기반합니다. 각 층에 대해, 1/2/3개 이상의 놓친 방에 대해 점수가 50%/20%/0%로 감소합니다. '완전히 탐험된' 것으로 간주되는 기준은 변경되지 않았습니다.\n" +
                        "\n" +
                        "퀘스트 점수는 이제 보스 점수와 유사한 방식으로 감소될 수 있습니다. 페널티를 피하기 위해 잘 위치하도록 하세요! 이 감소에는 예고된 공격/효과와 항상 피할 수 있어야 하는 일반 공격이 포함됩니다. 공격이 때때로 피할 수 없는 경우에는 페널티가 적용되기 전에 여유가 있습니다."));

        changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), Messages.get(ChangesScene.class, "misc"),
                "하이라이트:\n" +
                        "- 아이템의 사용자 정의 메모를 이제 아이템의 정보 창에서 생성하거나 편집할 수 있습니다\n" +
                        "- 모든 물약이 이제 특정 효과의 지속시간을 새로고침합니다. 이전에는 일부 물약 효과의 지속시간이 계속 추가될 수 있었습니다\n" +
                        "- 잃어버린 배낭에 대한 랜드마크 항목이 추가되었습니다",

                "아이템:\n" +
                        "- 자철석의 판매/에너지 가치를 약간 줄여서 고급 투척 무기를 에너지로 효과적으로 변환하는 데 사용할 수 없도록 했습니다\n" +
                        "- 식별되지 않은 사격 DISC를 이제 에이자의 적석에 주입할 수 있습니다\n" +
                        "- 부분적으로 식별된 아이템을 이제 저지먼트의 장비 DISC와 함께 사용할 수 있습니다\n" +
                        "- LOCACACA 6251의 패널티가 받는 피해 증가량이 3배에서 2배로 감소되었습니다\n" +
                        "- 이제 각성 무기는 착용 즉시 감정됩니다.\n" +
                        "\n" +
                        "캐릭터:\n" +
                        "- 디아볼로의 AI가 개선되어, 이제 영웅을 공격할 수 없을 때 대상을 바꿀 수 있습니다\n" +
                        "- 윌슨 필립스 상원의원이 게임을 한번 클리어해야 등장하도록 변경되었습니다\n" +
                        "- 엔야 할멈을 물리치면 이제 플레이어의 악몽 디버프가 해제됩니다."));


        changes = new ChangeInfo(Messages.get(ChangesScene.class, "buffs"), false, null);
        changes.hardlight(CharSprite.POSITIVE);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(HeroSprite.avatar(HeroClass.CLERIC, 6), "죠린 버프",
                "전체적으로 죠린은 초기 출시 이후 승률은 낮지만, 매우 인기가 높기 때문에 일부는 사람들이 아직 알아가고 있는 과정일 것으로 예상됩니다. 현재로서는 다른 것들에 비해 약한 특정 죠린 메커니즘에 대한 버프에 집중하고 있습니다.\n" +
                        "\n" +
                        "기본 직업:\n" +
                        "- 실 펀치의 기본 피해가 2-6에서 2-8로 증가\n" +
                        "- 절단 실 & 뫼비우스의 띠가 이제 즉시 시전됩니다\n" +
                        "- 실 그물의 지속시간이 4에서 5로 증가\n" +
                        "- 동작 탐지가 이제 즉시 시전됩니다\n" +
                        "\n" +
                        "결착의 스탠드사:\n" +
                        "- 신속 꿰매기의 치유량이 10/15/20에서 15/20/25로 증가\n" +
                        "- 불굴의 근성의 피해 저항이 15%/23%/30%에서 20%/30%/40%로 증가",

                		"의지의 스탠드사:\n" +
                        "- 표적 상태의 추가 피해가 레벨에서 5+레벨로 증가\n" +
                        "- 실 전개가 이제 표적 상태를 발동하고 다시 적용합니다\n" +
                        "- 1000구다!의 재사용 대기시간이 50에서 30으로 감소\n" +
                        "- 폭우의 치유량이 10에서 15로 증가\n" +
                        "- 폭우의 속박 시간이 1턴에서 2턴으로 증가\n" +
                        "- 수갑 데스매치가 이제 즉시 시전됩니다\n" +
                        "\n" +
                        "스탠드 전개:\n" +
                        "- 전투 개입의 지속시간 연장이 1/2/3/4에서 3/4/5/6으로 증가\n" +
                        "- 광역 실 전개의 추가 피해가 능력 당 5-10에서 능력 당 +33%로 증가\n" +
                        "\n" +
                        "동료의 유대:\n" +
                        "- 전담 방어의 지속시간이 6/8/10/12에서 10/13/17/20으로 증가"));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "nerfs"), false, null);
        changes.hardlight(CharSprite.NEGATIVE);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(HeroSprite.avatar(HeroClass.CLERIC, 6), "죠린 너프",
                "몇 가지 죠린 능력의 일부가 조정되었습니다:\n" +
                        "\n" +
                        "- 빈틈 노리기 특성의 피해가 +4/+6에서 +3/+5로 감소\n" +
                        "- 징벌방의 식사법의 충전량 획득이 +1/+1.5에서 +0.67/+1로 감소\n" +
                        "- 잠행이 더 이상 즉시 시전되지 않습니다\n" +
                        "- 실 결계의 범위가 이제 30으로 제한됩니다\n" +
                        "- \"실\"을 뻗으면!의 충전 비용이 1에서 2로 증가\n" +
                        "- 엄폐의 충전 비용이 1에서 2로 증가했지만, 지속시간이 +50%로 증가"));

    }

    private static void add_a_Changes(ArrayList<ChangeInfo> changeInfos) {
        ChangeInfo changes = new ChangeInfo("v3.0a", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo("새로운 요소", false, null);
        changes.hardlight(CharSprite.POSITIVE);
        changeInfos.add(changes);
        changes.addButton(new ChangeButton(new Image(Assets.Sprites.WILLSON, 0, 0, 23, 14), "신규 npc",
                "이제 오시리스신의 지도가 아닌 카이로 시내에 있는 상원의원을 통해서 미니 던전에 입장할 수 있습니다."));
        changes.addButton(new ChangeButton(new Image(Assets.Sprites.VAMPIRE, 0, 0, 12, 16), "미니 던전 추가",
                "신규 미니 던전인 _쌍두룡의 방_이 추가되었습니다!"));
        changes = new ChangeInfo("변경", false, null);
        changes.hardlight(CharSprite.WARNING);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.CM), "편의성 개선",
                "메이드 인 헤븐의 장비 DISC의 전 단계인, C-MOON(각성)의 DISC의 능력이 추가되었습니다.\n\n" +
                        "이제 로한과 도박을 할 때 보유 골드와 획득 골드가 출력됩니다.\n\n" +
                        "시드 검색 및 시드 분석이 클리어 포인트를 소모하는 방식으로 변경되었습니다."));
    }

    private static void add_jolyne_Changes(ArrayList<ChangeInfo> changeInfos) {
        ChangeInfo changes = new ChangeInfo("v3.0", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo("새로운 요소", false, null);
        changes.hardlight(CharSprite.POSITIVE);
        changeInfos.add(changes);
        changes.addButton(new ChangeButton(HeroSprite.avatar(HeroClass.CLERIC, 1), "신규 캐릭터: 죠린!",
                "_죠린이 드디어 6번째 플레이어블 캐릭터로 추가되었습니다!_\n\n" +
                        "죠린은 스톤 프리의 장비 DISC를 사용해서 다양한 상황에 능숙하게 대처할 수 있습니다.\n\n" +
                        "죠린은 다른 캐릭터와 다른 독특한 해금 조건을 가지고 있습니다."));
        changes.addButton(new ChangeButton(HeroSprite.avatar(HeroClass.CLERIC, 2), "죠린의 보조 직업",
                "보조 직업은 두 번째 보스를 처치한 후에 선택할 수 있습니다.\n\n" +
                        "_의지의 스탠드사_: 의지의 스탠드사는 강력한 장거리 능력을 얻고, 강화된 실 펀치를 사용할 수 있습니다.\n\n" +
                        "_결착의 스탠드사_: 결착의 스탠드사는 근접 전투와 무기 및 방어구와의 연계에 중점을 둔 다양하고 강력한 능력을 얻습니다."));
        changes.addButton(new ChangeButton(HeroSprite.avatar(HeroClass.CLERIC, 6), "죠린 전용 레퀴엠 능력",
                "레퀴엠 능력은 네 번째 보스를 처치한 후에 선택할 수 있습니다.\n\n" +
                        "_스탠드 전개_: 죠린은 스탠드 전개를 통해서 몸을 실로 변형하고 새로운 능력, 추가 공격 범위, 보호막을 얻습니다.\n\n" +
                        "_버닝 다운 더 하우스_: 엠포리오의 도움으로 버닝 다운 더 하우스의 능력을 사용할 수 있습니다.\n\n" +
                        "_동료의 유대_: 기존 동료를 강화하거나 스톤 프리를 소환할 수 있습니다."));

        changes = new ChangeInfo("변경", false, null);
        changes.hardlight(CharSprite.WARNING);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(Icons.DISPLAY.get(), "UI 및 그래픽 개선",
                "이제 게임 진행 화면에서 최대 6개의 진행 중인 게임이 표시되고 정렬 옵션이 추가되었습니다."));
        changes.addButton(new ChangeButton(new BuffIcon(BuffIndicator.LOCKED_FLOOR, true), "보스 컷씬 추가",
                "이제 각 보스에 전용 컷씬이 추가됩니다."));

        changes = new ChangeInfo("상향", false, null);
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);
        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.CHAOTIC_CENSER), "위험한 물건 상향",
                "쇼트 키 No. 2: 적이 있을 때만 가스를 방출하도록 변경되었습니다.\n\n" +
                        "헤이 야!: 대미지 증가율이 향상되었습니다.\n\n" +
                        "초콜릿 디스코: 함정 탐지 기능이 추가되었습니다.\n\n" +
                        "낡은 워크맨: 식별 기능이 개선되었습니다."));

        changes = new ChangeInfo("하향", false, null);
        changes.hardlight(CharSprite.NEGATIVE);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.MOSSY_CLUMP), "위험한 물건 하향",
                "개구리 가죽: 잔디 바닥 생성 확률이 감소되었습니다.(1/3 잔디, 2/3 물)"));

    }

}
