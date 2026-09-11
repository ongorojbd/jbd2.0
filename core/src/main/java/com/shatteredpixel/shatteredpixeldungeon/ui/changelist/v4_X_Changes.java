/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2026 Evan Debenham
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
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.ChangesScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ImpSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.PucciSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SkeletonSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.TalentIcon;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.Image;
import com.watabou.utils.DeviceCompat;

import java.util.ArrayList;

public class v4_X_Changes {

    public static void addAllChanges( ArrayList<ChangeInfo> changeInfos ){

        add_Coming_Soon(changeInfos);
        add_v4_0_Changes(changeInfos);
    }

    public static void add_Coming_Soon( ArrayList<ChangeInfo> changeInfos ) {

        ChangeInfo changes = new ChangeInfo("출시 예정", true, "");
        changes.hardlight(0xCCCCCC);
        changeInfos.add(changes);

        changes.addButton( new ChangeButton( new ItemSprite(ItemSpriteSheet.MAP), "새로운 아이템",
                "게임플레이 콘텐츠 측면에서 다양한 아이템 카테고리에 몇 가지 새로운 아이템을 추가하는 데 집중할 예정입니다."));
    }

    public static void add_v4_0_Changes( ArrayList<ChangeInfo> changeInfos ) {

        ChangeInfo changes = new ChangeInfo("v4.0a", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new Image(new ImpSprite()), "신규 대규모 퀘스트",
                "DIO의 저택 층의 퀘스트가 완전히 개편되어 역대 최대 규모의 퀘스트가 되었습니다!\n" +
                        "\n" +
                        "이제 DIO의 저택에서는 오시리스신 대신 가출소녀 앤이 등장하며, 위협과 보물로 가득 찬 저택 창고를 털어오라는 임무를 줍니다!\n\n퀘스트 구성 요소는 다음과 같습니다:\n" +
                        "\n" +
                        "- 기존 던전 3개 층에 달하는 엄청난 규모의 신규 서브 지역 및 20여 종의 신규 방 추가\n" +
                        "- 약 10개 방에 설치된 3가지의 신규 고정형 함정 요소를 활용 가능\n" +
                        "- 6~19층 지역까지 아우르는 다양한 신규 변종 적들 등장\n" +
                        "- 저티어 보상은 쉽게, 고티어 보상은 철저한 경비를 뚫고 획득하는 독자적인 성장의 재미\n" +
                        "- 침입자들의 실력을 검증하기 위해 특별히 설계된 신규 보스 방\n" +
                        "\n" +
                        "창고 공략에 성공하면 앤이 아이템 하나를 밖으로 가지고 나올 수 있게 허락해 줍니다! 창고 안의 수많은 아이템들은 기존 보상이었던 고강화 돌가면 못지않게 강력하지만, 선택의 폭은 훨씬 더 넓어졌습니다. 퀘스트를 마치면 예전처럼 상점을 이용할 수 있습니다."));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.TG), "히든 던전: TG 대학병원",
                "TG 대학병원이 마침내 모습을 드러냈습니다!\n\n" +
                        "TG 대학병원은 _죠죠 8부_를 테마로 한 _고난이도_ 던전으로, 바위 인간의 스탠드와 추적자들이 배회하고 있습니다.\n\n" +
                        "- 최악의 난이도, 극한의 시련: 끈질기게 목숨을 노리는 바위 인간측 스탠드와 기괴한 재앙의 위협이 플레이어를 기다립니다.\n\n" +
                        "- 압도적인 보상: 공략은 대단히 까다롭지만, 성공 시 강력한 특수 아이템들을 보상으로 획득할 수 있습니다.\n\n" +
                        "TG 대학병원 입장 아이템은 6층 상점에서 구매할 수 있습니다."));
        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.WAND_KIRA), "신규 사격 DISC",
                "강력한 사격 DISC인 _킬러 퀸의 사격 DISC_가 추가되었습니다!\n\n" +
                        "킬러 퀸의 사격 DISC는 TG 대학병원에서 획득할 수 있습니다."));

        changes.addButton( new ChangeButton( ChangeIcons.V40_CITY_CARPET, "환경 비주얼 개편!",
                "죠기던의 픽셀 아트 비주얼 개편 작업이 적용되었습니다!\n" +
                        "\n" +
                        "이번 업데이트에서는 전체 지역에 적용되는 주요 변경 사항 하나와 DIO의 저택 및 퀘스트 방 위주의 작업이 이루어졌습니다:\n" +
                        "\n" +
                        "- 벽과 바닥이 만나는 경계면에 음영 효과가 추가되었습니다.\n" +
                        "- DIO의 저택에 새로운 형태의 특수 바닥 타일이 추가되었습니다.\n" +
                        "- 자이로 퀘스트 및 관련 그래픽이 전면 개편되었습니다.\n" +
                        "- 화이트 스네이크 퀘스트 비주얼이 개편되었습니다.\n"));
        
        changes.addButton( new ChangeButton( ChangeIcons.V40_GREATSWORD_CRYSTAL, "신규 속성 및 저주!",
                "무기에 적용할 수 있는 4가지 신규 속성과 2가지 신규 저주가 추가되었습니다!\n" +
                        "\n" +
                        "- 맹독의 속성: 시간에 따라 중첩되는 중독 피해를 주는 일반 속성입니다.\n" +
                        "- 예리한 속성: 피해량의 일부를 중첩되지 않는 출혈 피해로 전환하는 희귀 속성입니다.\n" +
                        "- 섬뜩한 속성: 주 대상을 제외한 주변의 모든 적에게 공포를 부여하는 희귀 속성입니다.\n" +
                        "- 수정 속성: 피해량을 높여주지만 제한된 내구도를 신경 써서 관리해야 하는 특급 속성입니다.\n" +
                        "- 고압의 저주: 간헐적으로 물기둥을 분출시켜 플레이어와 적을 모두 튕겨내는 저주입니다.\n" +
                        "- 경이로운 저주: 무작위 저주받은 사격 DISC 효과를 발동시키는 저주입니다."));

        changes.addButton( new ChangeButton( ChangeIcons.V13_BUFF_AGGRESSION, "시련 리워크",
                "네놈.. 보고 있구나! 시련의 대처 가능성과 일관성을 높이기 위해 메커니즘을 조정했습니다.\n" +
                        "\n" +
                        "v4.0a 이전에는 적이 플레이어를 처음 발견했을 때에만 고정된 8타일 범위 내의 다른 적들이 반응했습니다. 하지만 숙련된 플레이어들은 이 효과 자체를 아예 발동시키지 않는 편법을 많이 이용하곤 했습니다.\n" +
                        "\n" +
                        "이제 도전과제 매커니즘이 변경되어, 적이 플레이어를 노출하고 있는 동안 지속적으로 발동합니다. 초기 경보 범위는 2타일이지만 시간이 지남에 따라 최대 12타일까지 점차 넓어집니다. 아주 잠깐이라도 시야를 차단하면 이 범위는 다시 초기화됩니다. 이로써 전략적인 플레이의 가치는 유지하되, 직관적이고 직관적으로 대처할 수 있도록 개선했습니다.\n" +
                        "\n" +
                        "또한 현재 경보 범위를 한눈에 확인할 수 있는 시각적 버프 아이콘이 추가되었습니다."));

        changes.addButton(new ChangeButton(new Image(new SkeletonSprite.Vault()), "신규 적 AI",
                "새로운 퀘스트의 잠입 플레이에 맞춰 작동하는 전용 적 인공지능(AI)을 구현했습니다! 이 AI가 적용된 적들은 다음과 같은 특성을 가집니다:\n" +
                        "\n" +
                        "- 근처 적의 이동 경로를 벽 너머로 파악할 수 있습니다.\n" +
                        "- 수면 상태의 적은 감지 범위가 대폭 감소합니다.\n" +
                        "- 순찰 상태의 적은 정해진 경로를 따라 이동하며, 이동 중일 때는 후방 감지 범위가 대폭 줄어듭니다.\n" +
                        "- 수색 상태는 순찰/수면 상태와 공격 상태 사이에 존재하는 신규 AI 상태입니다. 수색 상태의 적은 플레이어가 있던 위치로 이동하지만, 다시 발견하기 전까지는 공격하지 않습니다. 수색 중인 적은 시야를 놓치기 쉬우므로 문 뒤나 모퉁이로 숨는 플레이가 매우 효과적입니다.\n" +
                        "- 적이 선제공격을 받으면 수색 과정을 건너뛰고 즉시 반격 모드로 전환됩니다.\n" +
                        "\n" +
                        "현재 이 AI는 신규 퀘스트 지역에서만 사용되며, 기존 던전의 적들은 이전과 동일하게 작동합니다."));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(CharSprite.WARNING);
        changeInfos.add(changes);

        changes.addButton( new ChangeButton( ChangeIcons.V075_LONGSWORD_CORRUPTING, "기존 속성 변경 사항",
                "신규 속성이 추가됨에 따라, 기존 속성에 있던 몇 가지 매끄럽지 못했던 판정들을 개선했습니다:\n" +
                        "\n" +
                        "- 관성의 속성: 적이 다른 효과에 의해 먼저 사망하여 타격이 취소되어도, 저장된 관성 피해량이 사라지지 않도록 수정되었습니다.\n" +
                        "- 정신 지배의 속성: 무기 자체의 직접 타격뿐만 아니라 타격 전에 추가 피해가 먼저 들어가는 경우에도 정신 지배 효과가 정상 적용됩니다.\n" +
                        "- 음침한 속성: 무기 자체의 타격 전에 추가 피해가 먼저 들어가는 경우에도 효과가 더 일관되게 발동하도록 개선되었습니다."));

        changes.addButton( new ChangeButton( ChangeIcons.V081_MISC, Messages.get(ChangesScene.class, "misc"),
                "주요 변경 사항:\n" +
                        "- 이제 체력바에 지속 피해의 총량이 시각적으로 표시됩니다.\n" +
                        "- 등가교환의 명령 DISC의 편의성 및 UI가 개선되었습니다.\n" +
                        "- 보스전 시 잠기는 문 위치에 있던 아이템들이 밖으로 밀려나도록 수정되었습니다.\n" +
                        "- 다양한 코드 의존성 라이브러리가 최신 버전으로 업데이트되었습니다."));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "buffs"), false, null);
        changes.hardlight(CharSprite.POSITIVE);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.ARTIFACT_SANDALS), "아이템 상향",
                "v4.0a는 신규 콘텐츠 위주의 업데이트지만, 일부 아이템 밸런스 조정도 함께 진행되었습니다. 몇몇 아이템 성능이 상향되었습니다:\n" +
                        "\n" +
                        "- DISC가 심어진 독개구리: 기본 피해량이 10-25에서 10-30으로 증가\n" +
                        "\n" +
                        "- 녹색 아기의 장비 DISC: 레벨에 따른 충전 속도 증가율 상승 (+10강 기준 최대 +50%)\n" +
                        "- 저지먼트의 장비 DISC: 흙인형의 체력이 20+8*lvl에서 40+10*lvl로 증가하였으며, 흙인형의 공격력이 영웅처럼 힘 수치에 비례하여 증가하도록 변경\n" +
                        "- 하베스트의 장비 DISC: 레벨에 따라 충전 속도가 증가하도록 변경 (+10강 기준 최대 +50%)\n" +
                        "\n" +
                        "- 용암 암석: 레벨당 변환 확률이 12.5%에서 20%로 증가\n" +
                        "- 쇼트 키 No. 2: 적에게 실제로 유해한 가스를 분출할 확률이 훨씬 높아짐"));

        changes.addButton(new ChangeButton(
                new Image(Assets.Sprites.RESEARCHER, 0, 0, 12, 15), "특성/스킬 상향",
                "일부 영웅 스킬 및 특성도 상향되었습니다:\n" +
                        "\n" +
                        "- 크레이지 D 능력 발현 - 연속 공격: 능력 사용 후 적 처치 시 각성의 파문전사처럼 지속 시간이 15턴 연장됨\n" +
                        "- Crazy noisy bizarre town: 레퀴엠 브로치의 기본 소모 에너지가 50에서 35로 감소"));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "nerfs"), false, null);
        changes.hardlight(CharSprite.NEGATIVE);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.STONE_AGGRESSION), "아이템 하향",
                "지나치게 강력한 성능을 보이던 일부 아이템들의 성능을 조정했습니다.\n" +
                        "\n" +
                        "- 공격의 명령 DISC: 보스에게 직접 사용할 수 없도록 변경\n" +
                        "- 신속의 돌가면: 레벨당 이동 속도 증가량이 +17.5%에서 +15%로 감소\n" +
                        "- 어텀 리브스: 페널티가 커지고, 보너스 아이템 투명도 효과가 15%에서 10%로 감소"));

        changes.addButton(new ChangeButton(new TalentIcon(Talent.BARKSKIN), "특성/스킬 하향",
                "일부 영웅 스킬 및 특성에 대한 밸런스 조정이 진행되었습니다:\n" +
                        "\n" +
                        "- 생명 순환: 풀을 밟을 때는 발동하지 않으며, 신규 식물 아이템을 사용할 때 발동하도록 변경, 레벨당 방어력이 영웅 레벨의 50%에서 33%로 감소\n" +
                        "- Chase: 레퀴엠 브로치의 기본 소모 에너지가 35에서 50으로 증가\n" +
                        "- 화살의 선택 - 결착의 스탠드사: 동료 소환에 필요한 능력 사용량이 8회에서 10회로 증가\n" +
                        "\n" +
                        "신규 퀘스트 전용 맞춤 조정 사항:\n" +
                        "- 흔들림 없는 용기: 파문의 보호막이 필요하도록 변경됨\n" +
                        "- 수복의 스탠드사: 부활하거나 창고 지역에 입장할 때 에너지가 유지되지 않도록 변경"));

    }

}