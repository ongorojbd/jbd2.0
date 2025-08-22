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
import com.shatteredpixel.shatteredpixeldungeon.sprites.ImpSprite;
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
        add_v3_2_2_Changes(changeInfos);
        add_v3_2_1_Changes(changeInfos);
        add_v3_2_Changes(changeInfos);
        add_v3_1_Changes(changeInfos);
        add_a_Changes(changeInfos);
        add_jolyne_Changes(changeInfos);
    }

    public static void add_Coming_Soon(ArrayList<ChangeInfo> changeInfos) {

        ChangeInfo changes = new ChangeInfo("곧 출시 예정", true, "");
        changes.hardlight(0xCCCCCC);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new Image(new ImpSprite()), "17~19층 퀘스트 개편",
                "3.0d에서 제공될 주요 콘텐츠 개선사항은 오시리스신 퀘스트 개편입니다.\n" +
                        "죠니 퀘스트 개편과 마찬가지로, 이 새로운 퀘스트는 독특한 게임플레이를 가진 던전의 새로운 하위 지역으로 플레이어를 보낼 것입니다."));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.JOJO7), "???",
                "스틸 볼 런 애니화 개봉일에 맞춘 추가 업데이트가 있을 예정입니다."));

    }

    public static void add_v3_2_2_Changes(ArrayList<ChangeInfo> changeInfos) {

        ChangeInfo changes = new ChangeInfo("v3.0c2", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new Image(Assets.Sprites.VAMPIRESOLDIER, 0, 0, 12, 15), "전투조류 추가 업데이트",
                "전투조류 던전의 추가 컨텐츠가 있습니다!\n" +
                        "\n" +
                        "- 신규 적, 보스가 추가되었습니다.\n" +
                        "- 신규 보상이 추가되었습니다."));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(CharSprite.WARNING);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.KUNAI), "투척 무기 변경",
                "투척 무기들은 전반적으로 매우 좋은 성과를 보이고 있습니다! 여전히 인기도는 낮지만, 사람들이 실제로 투척 무기를 업그레이드하기 시작했고, 투척 무기들이 동급의 근접 무기들과 경쟁력을 갖추게 되었습니다.\n" +
                        "\n" +
                        "상향:\n" +
                        "- 렉킹 볼과 황금의 회전의 철구를 이제 땅에서 즉시 주워들 수 있습니다.\n" +
                        "- 완전생물의 다람쥐의 기본 내구도가 5에서 8로 증가.\n" +
                        "- 완전생물의 다람쥐의 피해 증가량이 1-2에서 1-3으로 증가.\n" +
                        "- DISC가 심어진 독개구리의 기본 피해량이 5-20에서 다시 5-25로 증가."
                ,
                "하향:\n" +
                        "- 크래커 볼리의 불구 지속시간이 10에서 5로 감소.\n" +
                        "- 노토리어스 B.I.G의 피해 증가량이 1-4에서 1-3으로 감소.\n" +
                        "- 노토리어스 B.I.G을 투척했을 때 돌아오는 턴이 3턴에서 4턴으로 증가."));
    }

    public static void add_v3_2_1_Changes(ArrayList<ChangeInfo> changeInfos) {

        ChangeInfo changes = new ChangeInfo("v3.0c1", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.TENS), "전투조류 추가 업데이트",
                "전투조류 던전의 추가 컨텐츠가 있습니다!\n" +
                        "\n" +
                        "- 신규 적, 보스가 추가되었습니다.\n" +
                        "- 신규 보상이 추가되었습니다.\n" +
                        "- 상점 물품이 리워크되었습니다."));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(CharSprite.WARNING);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), Messages.get(ChangesScene.class, "misc"),
                "버그 수정:\n" +
                        "- 총기류 무기가 발사되지 않던 버그가 수정되었습니다.\n" +
                        "- 코코 잠보에서 무기류를 선택하지 못하던 버그가 수정되었습니다.\n" +
                        "- 오렌지 폭탄으로 피해를 줄 수 없었던 버그가 수정되었습니다."));
    }

    public static void add_v3_2_Changes(ArrayList<ChangeInfo> changeInfos) {

        ChangeInfo changes = new ChangeInfo("v3.0c", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.THROWING_SPEAR), "투척 무기 리워크",
                "투척 무기가 업그레이드할 가치가 있도록 대대적으로 개편되었습니다!\n" +
                        "\n" +
                        "- 투척 무기는 이제 3개 세트로 등장하며, 세트는 섞이지 않습니다.\n" +
                        "- 투척 무기 기본 내구도가 3x5/8/12로 증가했습니다 (기존 2x5/10/15에서).\n" +
                        "- 세트는 단위로 업그레이드되며 (3개 모두), 업그레이드하면 세트가 완전히 수리됩니다.\n" +
                        "- 업그레이드는 이제 내구도를 1.5배 증가시킵니다 (기존 3배에서 감소).\n" +
                        "- 투척 무기의 기본 업그레이드당 데미지 증가가 1티어로 감소했습니다 (기존 2티어에서).\n" +
                        "- 세트는 속성 부여, 저주, 강화, 미식별 등이 가능합니다.\n" +
                        "- 세트는 자연 업그레이드, 속성 부여, 또는 저주와 함께 등장할 수 있습니다.\n" +
                        "- 몇몇 특별한 방에서 더 높은 가치의 투척 무기 세트가 등장할 확률이 있습니다.\n" +
                        "\n" +
                        "보우건 탄환은 이러한 변경사항의 영향을 받지 않으며, 사실상 모두 같은 세트에 속하고 여전히 업그레이드할 수 없습니다."));

        changes.addButton(new ChangeButton(new Image(Assets.Sprites.STURO, 0, 0, 16, 14), "신규 던전: 전투조류",
                "_신규 던전_이 추가되었습니다!\n" +
                        "\n" +
                        "- 전투조류 던전은 첫 클리어 이후 캐릭터 선택 화면에서 언제든지 선택하여 플레이할 수 있습니다!\n" +
                        "- 기존 던전과는 다른 독립적인 진행 방식을 제공합니다.\n" +
                        "- 새로운 적, 아이템, 그리고 환경 요소들이 포함되어 있습니다.\n" +
                        "- 전투조류 관련 신규 뱃지도 추가되었습니다!" +
                        "\n\n" +
                        "베타 버전 안내:\n" +
                        "- 현재 전투조류 던전은 베타 테스트 단계입니다.\n" +
                        "- 밸런스 조정과 컨텐츠 추가가 지속적으로 이루어질 예정입니다.\n" +
                        "- 플레이어 피드백을 바탕으로 추가 개선사항이 적용될 수 있습니다."));

        changes.addButton(new ChangeButton(new BuffIcon(BuffIndicator.ILLUMINATED, true), "명중 및 회피 아이콘",
                "정확도나 회피력을 변경하는 거의 모든 효과에 대해, 해당 효과가 공격 명중이나 실패의 원인일 때 표시되는 아이콘이 추가되었습니다!\n" +
                        "\n" +
                        "이를 통해서 다양한 버프/디버프가 명중률에 얼마나 차이를 만드는지 훨씬 쉽게 알 수 있게 됩니다.\n" +
                        "\n" +
                        "총 12개의 명중 아이콘과 11개의 실패 아이콘이 있습니다."));

        changes.addButton(new ChangeButton(new Image(Assets.Sprites.PASSIONE, 0, 15, 12, 15), "파시오네 호위팀",
                "- 파시오네 조직의 간부인 폴포가 이제 특정 비밀 방에서 등장합니다.\n" +
                        "- 폴포에게 보수를 지불하면 파시오네 호위팀 중 한 명을 무작위로 고용할 수 있습니다\n" +
                        "- 호위팀원들은 각각 고유한 능력과 특성을 가지고 있습니다"));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(CharSprite.WARNING);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.LIQUID_METAL), "수리 물약",
                "수리 물약도 투척 무기의 전반적인 변경사항에 맞춰 변경되었습니다:\n" +
                        "\n" +
                        "- 제작법 조정: 이제 식별되고 저주받지 않은 투척 무기 세트 하나를 필요로 하며 항상 3 에너지를 소모합니다.\n" +
                        "- 수리 물약은 이제 세트에서 분실/파손된 투척 무기를 교체할 수 있습니다 (일반적인 3개 한도까지)\n" +
                        "- 수리 물약의 업그레이드당 증가율이 2배에서 1.33배로 감소했습니다."));

        changes.addButton(new ChangeButton(new TalentIcon(Talent.SURVIVALISTS_INTUITION), "죠르노 특성 변경",
                "- 냉철함 특성은 이제 죠르노가 +1에서 투척 무기를 3배 속도로 식별하거나 +2에서 사용 시 즉시 식별할 수 있게 해줍니다.\n\n이전에는 +1에서 모든 아이템의 식별 속도를 1.75배, +2에서 2.5배 증가시켰습니다."));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.JOJO1), "만화책 변경",
                "이제 만화책을 상점에 1000골드에 판매할 수 있습니다."));

        changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), Messages.get(ChangesScene.class, "misc"),
                "주요 사항:\n" +
                        "- 죠니의 장비 선택 옵션이 이제 2개의 근접 무기와 1개의 갑옷에 추가로 투척 무기를 제공합니다\n" +
                        "- 투척 무기(전갈 투척 포함)는 이제 대상을 겨누지 않으면 항상 1턴의 투척 지연이 있습니다\n" +
                        "- 역행은 이제 천국의 DISC가 처음 약화될 때 항상 플레이어에게 알립니다\n" +
                        "- 쇼트 키 No. 2는 더 이상 수동적인 적을 겨누지 않습니다\n" +
                        "\n" +
                        "투척 무기:\n" +
                        "- 강화는 이제 공격 속도에 미치는 영향에 따라 투척 무기 내구도에 영향을 줍니다\n" +
                        "- 폭발 저주는 이제 발동 시 투척 무기의 사용 횟수를 소모합니다\n" +
                        "기타:\n" +
                        "- 대부분의 튜토리얼/가이드북 텍스트를 더 간결하게 개선했습니다\n" +
                        "- 관찰의 명령 DISC의 추측 창이 이제 어떤 아이템을 추측하는지 보여줍니다"));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "buffs"), false, null);
        changes.hardlight(CharSprite.POSITIVE);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new TalentIcon(Talent.PROJECTILE_MOMENTUM), "투척 무기 특성 강화",
                "투척 무기 변경의 일환으로 투척 무기와 상호작용하는 일부 특성이 강화됩니다:\n" +
                        "\n" +
                        "- 나이프 투척 특성의 정확도 증가가 대폭 상승하여 +1/2/3에서 +50/100/150%가 됩니다 (기존 +20/40/60%에서).\n" +
                        "\n" +
                        "- 자세 교정 특성은 이제 투척 무기와 전갈 투척 모두에 속성이 부여되어 있으면 두 속성 모두 발동할 수 있습니다."));

        changes.addButton(new ChangeButton(HeroSprite.avatar(HeroClass.CLERIC, 6), "죠린 상향",
                "- 동작 탐지 능력의 지속시간이 30턴에서 50턴으로 증가했습니다.\n" +
                        "\n" +
                        "의지의 스탠드사 버프:\n" +
                        "- 실 펀치의 무료 사용 재사용 대기시간이 100턴에서 50턴으로 감소했습니다.\n" +
                        "- 표적 상태는 이제 모든 능력으로 직접 대상이 된 적에게 적용됩니다."));

        changes.addButton(new ChangeButton(HeroSprite.avatar(HeroClass.WARRIOR, 6), "죠나단 상향",
                "- 흔들림 없는 용기는 이제 +1/2/3에서 연속 공격 수치와 보호막의 감소를 33/67/100% 늦춥니다.\n" +
                        "\n" +
                        "- 연속 공격은 이제 적을 죽이면 15턴 동안 지속됩니다.\n" +
                        "- 정신적인 폭발력의 연속 공격 지속시간 증가가 +1/2/3에서 30/45/60턴으로 증가했습니다 (기존 15/30/45턴에서)."));

        changes.addButton(new ChangeButton(HeroSprite.avatar(HeroClass.MAGE, 6), "적석의 수호자 상향",
                "적석의 수호자의 일부 근접 공격 효과가 더 흥미롭고 강력하게 변경되었습니다:\n" +
                        "\n" +
                        "- 매지션즈 레드 융합: 이제 화염을 폭발시켜 날려버릴 확률이 발생하고, 적에게 데미지를 줍니다.\n" +
                        "- 레드 핫 칠리 페퍼 융합: 이제 번개 면역과 추가 전류 사거리를 부여합니다.\n" +
                        "- C-MOON 융합: 이제 적에게 부여된 마비를 소모하여 큰 추가 피해를 줍니다."));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "nerfs"), false, null);
        changes.hardlight(CharSprite.NEGATIVE);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.TOMAHAWK), "투척 무기 하향",
                "투척 무기 리워크에 따른 조정이 있습니다.\n" +
                        "\n" +
                        "- 킹 크림슨의 주먹의 즉시 투척 조건이 단순한 20턴 쿨다운으로 변경되었습니다\n" +
                        "- 완전생물의 다람쥐의 기본 데미지가 6-15에서 6-12로 감소했습니다\n" +
                        "- 크래커 볼리의 피해 증가량이 1-3에서 1-2로 감소했습니다\n" +
                        "- 노토리어스 B.I.G의 내구도가 8에서 5로 감소했습니다\n" +
                        "- DIO의 나이프의 피해 증가량이 1-4에서 1-3으로 감소했습니다\n" +
                        "- DIO의 나이프의 출혈 확률이 60%에서 33%로 감소했지만, 이제 적의 방어력을 무시하는 별도의 판정입니다\n" +
                        "- DISC가 심어진 독개구리의 기본 데미지가 10-25에서 10-20으로 감소했습니다"));

        changes.addButton(new ChangeButton(new TalentIcon(Talent.SHARED_UPGRADES), "투척 무기 특성 하향",
                "투척 무기 변경의 일환으로 일부 특성도 너프됩니다:\n" +
                        "\n" +
                        "- 나이프 투척 특성의 데미지 증가율이 +1/2/3에서 +10/20/30%로 감소했습니다 (기존 +15/30/45%에서)\n" +
                        "\n" +
                        "- 생명력 주입 특성의 내구도 증가율이 +1/+2에서 +33%/+50%로 감소했습니다 (기존 +50%/+75%에서)\n" +
                        "- 자세 교정 특성이 이제 투척 무기 레벨당 고정 +16.67% 데미지 증가와 +1 지속시간을 부여하지만, 특성 레벨 1/2/3에서 +33/67/100% 데미지와 +2/4/6 지속시간으로 제한됩니다."));

        changes.addButton(new ChangeButton(HeroSprite.avatar(HeroClass.WARRIOR, 6), "용기의 파문전사 하향",
                "- 파문 에너지의 획득 및 손실 속도가 25% 감소했습니다\n" +
                        "- 파문 가드의 보호막이 10+2*레벨에서 8+2*레벨로 감소했습니다 (브로치의 레벨)"));

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
