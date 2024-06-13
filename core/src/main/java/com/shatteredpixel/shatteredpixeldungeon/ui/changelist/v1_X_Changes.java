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
import com.shatteredpixel.shatteredpixeldungeon.sprites.HeroSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.RollerSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.TalentIcon;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.Image;

import java.util.ArrayList;

public class v1_X_Changes {

    public static void addAllChanges(ArrayList<ChangeInfo> changeInfos) {
        add_Coming_Soon(changeInfos);
        add_k_Changes(changeInfos);
        add_j_Changes(changeInfos);
        add_i_Changes(changeInfos);
    }

    public static void add_Coming_Soon(ArrayList<ChangeInfo> changeInfos) {

        ChangeInfo changes = new ChangeInfo("Coming Soon", true, "");
        changes.hardlight(0xCCCCCC);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new Image(Assets.Sprites.RESEARCHER, 0, 15, 12, 15), "신규 캐릭터",
                "3.0 업데이트에서는 6번째 플레이어블 캐릭터로 쿠죠 죠린이 추가될 예정입니다!"));
        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.JOLYNE), "스톤 프리의 장비 DISC",
                "죠린의 전용 장비 DISC로, 특성을 통해 강화되는 다양한 능력을 시전할 수 있습니다."));
        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.CUDGEL), "스톤 프리의 주먹",
                "죠린의 전용 1단계 무기로, 높은 정확도가 특징입니다."));
        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.MASTERY), "도감 추가",
                "도감에서 투척 무기, 씨앗, 명령 DISC, 속성, 상형문자, 적들의 정보를 확인할 수 있도록 추가할 예정입니다."));
    }

    public static void add_k_Changes(ArrayList<ChangeInfo> changeInfos) {

        ChangeInfo changes = new ChangeInfo("v2.0k", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo("2.0k-2", false, null);
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);
        changes.addButton(new ChangeButton(new TalentIcon(Talent.PROVOKED_ANGER), "변경사항",
                "밸런스 변경이 있습니다.\n" +
                        "\n" +
                        "_-_ _크레이지 D 능력 발현 - 고속 펀치_ :\n출혈량 증가, 출혈 면역인 적에게는 일반적인 피해를 주도록 변경\n" +
                        "_-_ _크레이지 D 능력 발현 - 차지 어택_ :\n피해량 증가\n" +
                        "_-_ _크레이지 D 능력 발현 - 치유 펀치_ :\n속성 효과 증폭량 증가\n\n" +
                        "_-_ 더 패션 특성의 피해량이 증가했습니다.\n" +
                        "_-_ 죠스케의 무기 충전량 최대치 증가 빈도가 4레벨에서 3레벨로 감소되었습니다.\n" +
                        "_-_ 4부, 6부 만화책이 모두 죠죠 포인트를 소모하도록 변경되었습니다.\n" +
                        "_-_ 시저 체펠리, 키라 요시카게, 귀도 미스타 스킨에 보이스가 추가되었습니다."));

        changes = new ChangeInfo("2.0k-1", false, null);
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(Icons.get(Icons.BUFFS), "밸런스 변경",
                "위험한 물건에 대한 밸런스 변경이 있습니다.\n" +
                        "\n" +
                        "_- 개구리 가죽_ : 업그레이드 비용 증가\n" +
                        "\n" +
                        "_- 토오루의 인형_ : 보상 증가\n" +
                        "_-_ 원더 오브 U의 능력치가 20% 감소했습니다.\n" +
                        "_-_ 숨어있는 원더 오브 U를 처음 건드린 경우의 공격 피해는 유지됩니다.\n" +
                        "_-_ 이제 기존 보상에 무작위 아이템을 추가로 획득할 수 있습니다.\n" +
                        "\n" +
                        "_- 초콜릿 디스코_ : 업그레이드 비용 감소\n" +
                        "_- 밤의 지배자_ : 업그레이드 비용 감소\n" +
                        "_- 헤이 야!_ : 업그레이드 비용 감소"));

        changes = new ChangeInfo("새로운 요소", false, null);
        changes.hardlight(CharSprite.POSITIVE);
        changeInfos.add(changes);
        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.TRINKET_CATA), "신규 아이템 타입 :\n위험한 물건",
                "_새로운 아이템 타입인 위험한 물건 시리즈가 추가되었습니다!_\n\n" +
                        "위험한 물건은 게임 플레이에 도움이 되지만 특정한 상황에서는 이름 그대로 위험해질 수 있는 아이템입니다.\n\n" +
                        "게임 초반에 미식별 상태의 위험한 물건을 획득하면 코코 잠보에서 식별할 수 있으며,\n죠죠 포인트를 통해서 강화도 가능합니다.\n\n" +
                        "현재 _11종_의 위험한 물건이 추가되었습니다. 사용처에 맞는 적절한 유형을 골라보세요!"));
        changes.addButton(new ChangeButton(Icons.get(Icons.MAGNIFY), "신규 던전 구조",
                "다양한 계층에 새로운 구조가 추가되었습니다!\n\n" +
                        "신규 방과 신규 형태의 입/출구 방이 계층별로 새로 추가되었습니다.\n\n" +
                        "또한, 층을 내려갔을때 바로 적을 만나지 않도록 조정되었습니다."));
        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.MAP0), "신규 히든 퀘스트",
                "지도를 통해 이동할 수 있는 새로운 퀘스트가 추가되었습니다.\n\n" +
                        "오시리스신을 통해 전용 지도를 획득할 수 있습니다."));
        changes = new ChangeInfo("변경", false, null);
        changes.hardlight(CharSprite.WARNING);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(HeroSprite.avatar(HeroClass.DUELIST, 1), "죠스케 무기 능력",
                "죠스케의 무기 능력이 더욱 강력하고 영향력 있도록 변경되었습니다.\n\n" +
                        "_-_ 무기 능력 충전 속도가 33% 감소했습니다.\n" +
                        "_-_ 무기 능력 충전량 한도가 3-10에서 2-8로 감소했습니다.\n" +
                        "_-_ 무기 능력이 전반적으로 강화되었습니다.\n" +
                        "_-_ 무기 능력 충전 속도가 낮아진 것을 고려하여 일부 특성이 조정되었습니다.\n" +
                        "_-_ 이제 무기 능력 설명에 피해 범위가 직접적으로 표시됩니다.\n" +
                        "무기 능력과 특성 버프 관련 사항은 상향 및 변경 항목을 참조해주세요.\n\n" +
                        "죠스케의 세부 직업에도 약간의 변경 사항이 있습니다.\n" +
                        "_-_ 분노의 스탠드사의 두 무기는 이제 충전 횟수를 공유하며, 분노의 스탠드사의 최대 충전 횟수와 충전 속도가 향상됩니다.\n" +
                        "_-_ 분노의 스탠드사의 전의 상승 특성의 효과가 다양한 능력 사용을 장려할 수 있도록 새롭게 리워크되었습니다."));
        changes.addButton(new ChangeButton(new Image(Assets.Environment.TILES_EMPO, 0, 64, 16, 16), "아이템 제작",
                "아이템 제작 시스템의 편의가 개선되었습니다!\n\n" +
                        "_-_ 상황에 맞는 적절한 무작위 물약/기억 DISC 효과를 제공하는 새로운 제작 아이템이 2종 추가되었습니다.\n" +
                        "_-_ 물약의 정수와 DISC의 정수가 삭제되었으며, 이 아이템들을 재료로 하는 아이템은 이제 제작 시 8~9의 죠죠 포인트가 더 필요합니다.\n" +
                        "_-_ 아쿠아마린, 오팔이 물약 타입으로 변경되었습니다.\n" +
                        "_-_ 높은 가치의 물약 및 기억 DISC를 죠죠 포인트로 전환 시, 추가 죠죠 포인트를 획득할 수 있습니다.\n" +
                        "_-_ 코코 잠보는 이제 각 계층의 3층 또는 4층에서 반드시 생성됩니다.\n" +
                        "_-_ 아이템 제작 화면에 다양한 UI 개선이 이루어졌습니다.\n\n" +
                        "다양한 제작 아이템들의 비용, 생산량에 관한 변경도 이루어졌습니다. 더 자세한 내용은 상향 및 하향 항목을 확인해주세요."));
        changes.addButton(new ChangeButton(new TalentIcon(Talent.AGGRESSIVE_BARRIER), "특성 변경",
                "무기 능력 변경을 고려하여 죠스케의 특성이 변경되었습니다.\n\n" +
                        "_-_ _명품 교복_ 특성의 보호막 양이 3에서 3/5로 증가하고, 요구하는 최소 체력의 양이 40%/60% 이하에서 50% 이하로 변경되었습니다.\n" +
                        "_-_ _이탈리아 요리_ 특성을 통해 얻을 수 있는 충전량이 1/1.5에서 0.67/1로 감소했습니다.\n" +
                        "_-_ _융합_ 특성을 통해 얻을 수 있는 추가 충전량의 턴이 10턴/6턴에서 15턴/10턴으로 감소했습니다.\n\n" +
                        "또한 등급 1 특성들 중에서 사용률이 떨어지는 특성들이 리워크되었습니다.\n\n" +
                        "_-_ _재단의 지원_ 특성은 이제 더 적은 수의 특성 전용 식량을 획득할 수 있으며, 섭취 시 약간의 치유 효과와 함께 스타 플라티나의 장비 DISC의 충전량이 증가합니다.\n" +
                        "_-_ _더 패션_과 _호흡법 교정 마스크_ 특성은 전투 보너스를 제공하는 새로운 능력으로 리워크되었습니다.\n" +
                        "_-_ _원기 회복_ 특성이 요구하는 최소 체력의 양이 30%로 고정되었습니다."));
        changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), "변경사항",
                "_-_ 이제 용액,보석 아이템이 물약, 기억 DISC 관련 특성을 발동시킵니다.\n" +
                        "_-_ 이제 도발을 건 적을 공격하려고 시도하면 경고 메시지가 표시됩니다.\n\n" +
                        "_-_ 각성의 파문전사, 수복의 스탠드사의 설명에 간단한 능력 설명이 추가되었습니다.\n" +
                        "_-_ 각성의 파문전사, 수복의 스탠드사의 능력이 강화되면 이제 능력의 설명이 변경됩니다.\n\n" +
                        "_-_ 투척 무기들이 파괴될 때의 메시지가 추가됩니다.\n" +
                        "_-_ 씨앗이 발린 탄환은 이제 다른 투척 무기와 마찬가지로 내구도가 100을 넘을 시 영구히 사용할 수 있습니다.\n\n" +
                        "_-_ 저지먼트의 장비 DISC의 설명에 흙인형의 힘 수치가 표시됩니다.\n" +
                        "_-_ 카와지리 코사쿠의 등장 확률이 1/2로 변경되었습니다.\n"));
        changes.addButton(new ChangeButton(Icons.get(Icons.CHALLENGE_OFF), "시련 변경",
                "일부 위험한 물건에 기존 시련의 능력이 추가됨에 따라, 초콜릿 디스코, 자유인의 광상곡 시련이 삭제되었습니다."));
        changes.addButton(new ChangeButton(Icons.get(Icons.AUDIO), "캐릭터 보이스 추가",
                "일부 캐릭터들의 음성이 추가되었습니다."));
        changes.addButton(new ChangeButton(new Image(Assets.Sprites.IMP, 0, 0, 12, 14), "오시리스신",
                "게임을 한번 클리어한 상태일 경우만 지도를 떨어트리도록 변경되었습니다.\n\n지도를 떨어트릴 확률이 1/2로 변경되었습니다.\n\n신규 지도를 떨어트릴 확률이 추가되었습니다."));

        changes = new ChangeInfo("상향", false, null);
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.RAPIER), "무기 능력 상향",
                "_-_ _크레이지 D 능력 발현 - 속공 펀치_ :\n피해량 증가\n" +
                        "_-_ _크레이지 D 능력 발현 - 도라라라 러시_ :\n피해량 증가, 도라라라 러시로 적을 처치할 경우 턴을 소모하지 않음\n" +
                        "_-_ _크레이지 D 능력 발현 - 가드 브레이커_ :\n기습 실패 시 일반적인 피해량을 주도록 변경\n" +
                        "_-_ _크레이지 D 능력 발현 - 악퉁 베이비_ :\n요구 충전량 1로 감소, 투명화 지속 시간 증가\n" +
                        "_-_ _크레이지 D 능력 발현 - 연속 공격_ :\n피해량 증가, 공격 적중 시 연속 공격 지속 시간 증가(각성의 파문전사와 같음)\n" +
                        "_-_ _크레이지 D 능력 발현 - '헌팅'하러 가자!_ :\n피해량 증가\n" +
                        "_-_ _크레이지 D 능력 발현 - 죠셉의 가호_ :\n요구 충전량 1로 감소, 효과 지속 시간 증가\n" +
                        "_-_ _크레이지 D 능력 발현 - 고속 펀치_ :\n요구 충전량 1로 감소\n" +
                        "\n" +
                        " ",
                "_-_ _크레이지 D 능력 발현 - 잠재된 폭발력_ :\n요구 충전량 1로 감소, 효과 지속 시간 증가, 정확도 증가\n" +
                        "_-_ _크레이지 D 능력 발현 - 방벽 생성_ :\n지속 시간이 끝나거나 공격하기 전까지 방벽이 계속 유지됨\n" +
                        "_-_ _크레이지 D 능력 발현 - 혈액 커터_ :\n피해량 증가, 공격 범위 내에 있는 모든 적들에게 확정으로 명중\n" +
                        "_-_ _크레이지 D 능력 발현 - 차지 어택_ :\n요구 충전량 1로 감소\n" +
                        "_-_ _크레이지 D 능력 발현 - 치유 펀치_ :\n속성 증폭 수치 증가\n" +
                        "_-_ _크레이지 D 능력 발현 - 비장의 한발_ :\n5x5에서 7x7로 범위 증가, 씨앗을 바른 탄환의 내구도 증가\n" +
                        "_-_ _크레이지 D 능력 발현 - 치유 펀치_ :\n속성 증폭 수치 증가\n" +
                        "_-_ _크레이지 D 능력 발현 - 각오 모드_ :\n각오 모드로 적을 처치할 경우 턴을 소모하지 않음\n" +
                        "_-_ _크레이지 D 능력 발현 - 광폭화_ :\n추가 피해량 제공, 공격할 때마다 충전량이 소모되는 형식에서 충전 속도가 감소되는 형식으로 변경\n"));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.BREW_BLIZZARD), "제작 아이템 상향",
                "다수의 제작 아이템의 죠죠 포인트 요구량이 감소되었습니다.\n\n" +
                        "_-_ _산성 용액_ : 죠죠 포인트 요구량 2 -> 1\n" +
                        "_-_ _눈보라 용액_ : 죠죠 포인트 요구량 11 -> 8\n" +
                        "_-_ _전격 용액_ : 죠죠 포인트 요구량 14 -> 10\n" +
                        "_-_ _폭포수 용액_ : 죠죠 포인트 요구량 11 -> 8\n" +
                        "_-_ _괴염왕 용액_ : 죠죠 포인트 요구량 14 -> 10\n" +
                        "_-_ _자철석_ : 죠죠 포인트 요구량 11 -> 10\n" +
                        "_-_ _자수정_ : 죠죠 포인트 요구량 15 -> 12\n" +
                        "_-_ _토파즈_ : 죠죠 포인트 요구량 13 -> 12\n" +
                        "_-_ _루비_ : 죠죠 포인트 요구량 17 -> 12\n" +
                        "\n" +
                        " ",
                "일부 제작 아이템의 성능이 상향되었습니다.\n\n" +
                        "_-_ _디스토션 용액_ : 죠죠 포인트 요구량 10 -> 8\n가스 확산 속도가 빨라지고 효과가 끝나도 일정 시간동안 해당 가스에 면역이 됩니다.\n" +
                        "_-_ _기화냉동 용액_ : 죠죠 포인트 요구량 14 -> 6\n공격 시마다 2의 한기 부여 -> 3의 한기 부여\n" +
                        "_-_ _에메랄드_ : 죠죠 포인트 요구량 14 -> 6\n재료가 포코로코의 기억 DISC 대신 충전의 DISC로 변경됩니다.\n" +
                        "_-_ _검은 진주_ : 죠죠 포인트 요구량 6 -> 8\n제작 개수가 4개에서 5개로 증가합니다.\n" +
                        "_-_ _슈퍼 에이자_ : 더 간단한 조합법으로 변경되었습니다.\n" +
                        "_-_ _흑요석_ : 죠죠 포인트 요구량 15 -> 10\n흑요석을 강화했다면, 이제 사용 후에도 강화 효과가 유지됩니다."));
        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.PICKAXE), "압둘 & 죠니 퀘스트 보상 상향",
                "_-_ 압둘 퀘스트 보상 장비에 속성 및 상형문자가 부여되어 있을 확률이 기존 10%에서 20%로 상향됩니다.\n\n" +
                        "_-_ 죠니 퀘스트 보상으로 장비 선택을 고른 경우, 해당 장비에 속성 및 상형문자가 부여되어 있을 확률이 기존 0%에서 30%로 상향됩니다."));


        changes = new ChangeInfo("하향", false, null);
        changes.hardlight(CharSprite.NEGATIVE);
        changeInfos.add(changes);
        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.WALL), "벽의 눈 하향",
                "벽의 눈을 통해 획득하는 죠죠 포인트가 감소합니다."));
        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.ELIXIR_MIGHT), "제작 아이템 하향",
                "_-_ _시생인의 용액_ : 죠죠 포인트 요구량 14 -> 16\n" +
                        "_-_ _사파이어_ : 죠죠 포인트 요구량 13 -> 10\n제작 개수가 8개에서 6개로 감소합니다."));
    }


    public static void add_j_Changes(ArrayList<ChangeInfo> changeInfos) {

        ChangeInfo changes = new ChangeInfo("v2.0j", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo("새로운 요소", false, null);
        changes.hardlight(CharSprite.POSITIVE);
        changeInfos.add(changes);
        changes.addButton(new ChangeButton(BadgeBanner.image(Badges.Badge.YORIHIMES_ALL_CLASSES.image), "죠죠의 기묘한 던전 5주년",
                "죠죠의 기묘한 던전이 출시 후 5주년을 맞이했습니다!\n\n" +
                        "앞으로도 흥미진진한 콘텐츠와 업데이트가 기다리고 있으니, 기대해주시기 바랍니다.\n\n함께 해주셔서 진심으로 감사합니다!"));
        changes.addButton(new ChangeButton(new Image(Assets.Sprites.TROLL, 0, 0, 9, 15), "죠니 퀘스트 & 보상 리메이크",
                "12~14층에서 수행할 수 있는 죠니의 퀘스트가 대폭 개편되었습니다."));
        changes.addButton(new ChangeButton(Icons.get(Icons.ENTER), "DIO의 저택 비밀 공간",
                "DIO의 저택의 숨겨진 비밀 공간이 추가되었습니다.\n\n난이도는 어렵지만 특별한 보상을 획득할 수 있습니다."));
        changes.addButton(new ChangeButton(new Image(Assets.Sprites.DIOBRANDO2, 0, 0, 13, 16), "디오의 성 신규 히든보스",
                "디오의 성에서 만날 수 있는 히든보스가 추가되었습니다.\n\n히든 보스 처치 시, 신규 교환권인 _특별 교환권_ 2개를 획득할 수 있습니다."));
        changes = new ChangeInfo("변경", false, null);
        changes.hardlight(CharSprite.WARNING);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), "변경사항",
                "전투 시, 물리 공격 / 스탠드 공격 등을 직관적으로 나타내는 아이콘이 추가되었습니다.\n\nLOCACACA 6251의 효과가 리뉴얼되었습니다.\n\n천국에 도달한 DIO의 체력이 1500으로 감소했습니다.\n\n천국에 도달한 DIO의 5타 추가 피해량이\n10에서 20으로 증가합니다.\n\n히로세 야스호의 퀘스트 보상이 무작위 8부 스탠드로 변경되었습니다.\n\n이제 5부 책에서 더이상 빙결풀의 효과가 발동하지 않습니다.\n\n이제 특정한 날짜에 더 다양한 종류의 음식이 등장합니다.\n\n신규 배지 3종이 추가되었습니다.\n\n이제 캐릭터가 사망한 후, 유품에서 해당 캐릭터의 고유 아이템이 대신 등장합니다.\n\n일부 적들의 크기가 조정되었습니다."));
        changes.addButton(new ChangeButton(new TalentIcon(Talent.LIQUID_WILLPOWER), "특성 리워크",
                "자주 사용되지 않는 특성들이 리워크 및 상향되었습니다."));
        changes.addButton(new ChangeButton(Icons.get(Icons.INFO), "업데이트 알림",
                "업데이트가 출시될 경우, 메인화면에 업데이트를 알려주는 기능이 추가되었습니다."));
        changes.addButton(new ChangeButton(new Image(Assets.Sprites.COM, 0, 0, 15, 13), "시드 검색 / 시드 분석",
                "이제 엠포리오의 방에 있는 컴퓨터를 통해,\n특별 교환권 1개를 소모하여 시드 검색 및 시드 분석을 할 수 있습니다."));

        changes = new ChangeInfo("상향\n", false, null);
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        Image i = new Image(Assets.Sprites.ROLLER, 785, 0, 157, 144);
        i.scale.set(PixelScene.align(0.1f));
        changes.addButton(new ChangeButton(i, "로드롤러다!",
                "이제 DIO의 함정을 밟으면 로드롤러가 소환됩니다.\n\n_DIO의 패턴에 로드롤러가 추가되었습니다._"));
    }

    public static void add_i_Changes(ArrayList<ChangeInfo> changeInfos) {
        ChangeInfo changes = new ChangeInfo("v2.0i", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo("새로운 요소", false, null);
        changes.hardlight(CharSprite.POSITIVE);
        changeInfos.add(changes);
        changes.addButton(new ChangeButton(new Image(Assets.Sprites.DIOBRANDO, 0, 0, 12, 15), "신규 던전 : 디오의 성",
                "신규 던전인 _디오의 성_이 추가되었습니다!\n\n디오의 성은 기존 던전과는 별개의 고난이도 던전으로, 디오의 성에 입장하기 위해서는 클리어 포인트를 이용해 _입장권_을 구매해야 합니다.\n\n보상으로 _스킨 교환권_을 얻을 수 있습니다."));
        changes.addButton(new ChangeButton(BadgeBanner.image(Badges.Badge.BRANDOKILL.image), "신규 뱃지",
                "신규 던전 : 디오의 성 관련 뱃지 2종이 추가되었습니다."));
        changes.addButton(new ChangeButton(Icons.get(Icons.TALENT), "클리어 포인트",
                "_클리어 포인트_는 게임을 클리어하면 획득할 수 있는 영구적으로 보존되는 재화입니다.\n\n클리어 포인트는 모든 캐릭터가 공용으로 획득하고 사용할 수 있으며, _신규 던전 : 디오의 성 입장권_을 구매하는데 사용됩니다."));
        changes.addButton(new ChangeButton(new Image(Assets.Sprites.EMPORIO, 0, 0, 10, 14), "신규 NPC",
                "신규 NPC인 엠포리오를 통해서 유령의 방으로 이동할 수 있습니다.\n\n엠포리오는 게임을 한번 이상 클리어했다면, 던전 1층에 스폰됩니다.\n\n엠포리오의 방에 있는 특정 npc에게 신규 접속 기념 _공짜 입장권_을 받을 수 있습니다."));
        changes.addButton(new ChangeButton(new Image(Assets.Sprites.ANNASUI, 0, 0, 11, 16), "스킨",
                "_캐릭터 스킨_이 새로 추가되었습니다!\n\n스킨은 캐릭터별로 적용할 수 있는 고유의 모습이며, 엠포리오의 방에서 디오의 성 클리어 보상인 _스킨 교환권_을 통해 안나수이에게 구입할 수 있습니다."));
        changes.addButton(new ChangeButton(new Image(Assets.Sprites.PUCCI4, 0, 0, 12, 15), "히든 보스",
                "천국의 DISC 획득 후, 일순하는 과정에서 히든 보스가 추가되었습니다.\n\n처치 시, _클리어 포인트 4_를 획득할 수 있습니다."));
        changes.addButton(new ChangeButton(new Image(Assets.Sprites.ZOMBIET, 0, 0, 14, 16), "신규 적",
                "디오의 성에서만 등장하는 고유한 적들이 대거 추가되었습니다!\n\n디오의 성에서는 디오가 만들어낸 _시생인_들이 적으로 등장합니다."));

        changes = new ChangeInfo("변경", false, null);
        changes.hardlight(CharSprite.WARNING);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(Icons.get(Icons.STAIRS), "편의성 개선",
                "31층에서 천국의 DISC 획득 후\n계단을 올라갈 시, 25층으로 한번에 이동하도록 변경되었습니다."));
    }

}
