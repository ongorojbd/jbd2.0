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
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.effects.BadgeBanner;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.SandalsOfNature;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.WoollyBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfArcana;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfWealth;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfAntiMagic;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfMetamorphosis;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.TelekineticGrab;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfTransfusion;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.ChangesScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ElementalSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SpectralNecromancerSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIcon;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.TalentIcon;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.Image;

import java.util.ArrayList;

public class v1_X_Changes {

	public static void addAllChanges( ArrayList<ChangeInfo> changeInfos ){
		add_Coming_Soon(changeInfos);
		add_v1_3_3_Changes(changeInfos);
		add_v1_3_Changes(changeInfos);
	}

	public static void add_Coming_Soon( ArrayList<ChangeInfo> changeInfos ) {

		ChangeInfo changes = new ChangeInfo("Coming Soon", true, "");
		changes.hardlight(0xCCCCCC);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton( new Image(Assets.Sprites.RESEARCHER, 0, 0, 12, 15), "신규 영웅 : 죠스케",
			""));

		changes.addButton( new ChangeButton( new Image(Assets.Sprites.RESEARCHER, 0, 15, 12, 15), "신규 영웅 : 죠린",
			""));
	}

	public static void add_v1_3_3_Changes( ArrayList<ChangeInfo> changeInfos ) {
		ChangeInfo changes = new ChangeInfo("2.0e", true, "");
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes = new ChangeInfo("새로운 요소", false, null);
		changes.hardlight(CharSprite.POSITIVE);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.RO1), "로카카카",
				"신규 아이템인 로카카카가 추가되었습니다.\n\n" +
						"로카카카는 한 게임 내에서 단 1번만 사용할 수 있는 강력한 아이템입니다.\n\n" +
						"코코 잠보의 특수 아이템 탭에서 제조할 수 있습니다."));
		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.RO2), "신 로카카카",
				"신규 아이템인 신 로카카카가 추가되었습니다.\n\n" +
						"신 로카카카는 한 게임 내에서 단 3번만 사용할 수 있는 강력한 아이템입니다.\n\n" +
						"코코 잠보의 특수 아이템 탭에서 제조할 수 있습니다."));
		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.RO3), "LOCACACA 6251",
				"강해지는 대신 엄청난 등가교환을 감수해야 하는 숙련자 전용 약물이 새로 추가되었습니다."));
		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.RAM), "마법의 램프",
				"상점 전용 신규 아이템인 마법의 램프가 추가되었습니다."));
		changes.addButton( new ChangeButton( new Image(Assets.Sprites.WILLA, 0, 0, 12, 16), "윌 A. 체펠리",
				"죠죠를 지원하는 동료가 추가되었습니다.\n\n상점 전용 신규 아이템인 체펠리의 혼을 사용하여 소환할 수 있습니다."));
		changes.addButton( new ChangeButton( new Image(Assets.Sprites.TUSK3, 0, 0, 12, 15), "시저 체펠리",
				"죠죠를 지원하는 동료가 추가되었습니다.\n\n상점 전용 신규 아이템인 체펠리의 혼을 사용하여 소환할 수 있습니다."));
		changes.addButton( new ChangeButton( new Image(Assets.Sprites.TUSK3, 0, 15, 12, 15), "자이로 체펠리",
				"죠죠를 지원하는 동료가 추가되었습니다.\n\n상점 전용 신규 아이템인 체펠리의 혼을 사용하여 소환할 수 있습니다."));


		changes = new ChangeInfo("신규 시스템", false, null);
		changes.hardlight(CharSprite.WARNING);
		changeInfos.add(changes);


		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.KINGN), "8부 스탠드",
				"로카카카를 통해 획득할 수 있는 8부의 히가시카타 가족 스탠드 8종이 추가되었습니다.\n\n상점에서 구매할수도 있습니다."));
		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.KINGT), "8부 스탠드",
				"로카카카를 통해 획득할 수 있는 8부의 히가시카타 가족 스탠드 8종이 추가되었습니다.\n\n상점에서 구매할수도 있습니다."));
		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.KINGS), "8부 스탠드",
				"로카카카를 통해 획득할 수 있는 8부의 히가시카타 가족 스탠드 8종이 추가되었습니다.\n\n상점에서 구매할수도 있습니다."));
		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.KINGA), "8부 스탠드",
				"로카카카를 통해 획득할 수 있는 8부의 히가시카타 가족 스탠드 8종이 추가되었습니다.\n\n상점에서 구매할수도 있습니다."));
		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.KINGW), "8부 스탠드",
				"로카카카를 통해 획득할 수 있는 8부의 히가시카타 가족 스탠드 8종이 추가되었습니다.\n\n상점에서 구매할수도 있습니다."));
		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.KINGC), "8부 스탠드",
				"로카카카를 통해 획득할 수 있는 8부의 히가시카타 가족 스탠드 8종이 추가되었습니다.\n\n상점에서 구매할수도 있습니다."));
		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.KINGB), "8부 스탠드",
				"로카카카를 통해 획득할 수 있는 8부의 히가시카타 가족 스탠드 8종이 추가되었습니다.\n\n상점에서 구매할수도 있습니다."));
		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.KINGM), "8부 스탠드",
				"로카카카를 통해 획득할 수 있는 8부의 히가시카타 가족 스탠드 8종이 추가되었습니다.\n\n상점에서 구매할수도 있습니다."));
		changes.addButton( new ChangeButton( new Image(Assets.Sprites.TROLL, 0, 0, 9, 15), "11~15층 퀘스트 변경",
				"크레이지 다이아몬드 대신 죠니 관련 퀘스트가 새로 추가되었습니다.\n\n디스크 15개 수집 퀘스트가 삭제되었습니다."));
		changes.addButton( new ChangeButton( new Image(Assets.Sprites.KEEPER, 0, 0, 15, 15), "상점 개편",
				"상점에서만 구할수 있는 희귀한 전용 아이템들이 추가되었습니다.\n\n1/5 확률로 _체페리의 혼_을 판매합니다.\n\n1/15 확률로 _메가톤맨_을 판매합니다.\n\n1/30 확률로 ???를 판매합니다."));
		changes.addButton( new ChangeButton(BadgeBanner.image( Badges.Badge.MIH.image ), "신규 고난이도 뱃지",
				"고난이도 뱃지 2종이 추가되었습니다."));


		changes = new ChangeInfo("상향", false, null);
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.AMULET), "메이드 인 헤븐의 DISC",
				"받는 피해 증가 75% -> 30%"));

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.EXOTIC_BERKANAN), "웨더 리포트의 기억 DISC",
				"조합법이 변경되었습니다.\n\n변환의 DISC + DISC의 정수"));


		changes = new ChangeInfo("하향", false, null);
		changes.hardlight(CharSprite.NEGATIVE);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton( Icons.get(Icons.TALENT), "시련 난이도 조정",
				"아이즈 오브 헤븐 : 천국에 도달한 DIO 사망 시 펫 숍의 기억 DISC로 바로 26층으로 올라갈 수 있습니다.\n\n자유인의 광상곡 : 오시리스신의 퀘스트가 정상적으로 진행되도록 수정되었습니다.\n\n타올라라 용의 꿈 : 이제 흉에 방향에서 공격할 시 데미지가 그대로 반사됩니다. 압둘에게도 흉의 방향이 적용됩니다."));
	}

	public static void add_v1_3_Changes( ArrayList<ChangeInfo> changeInfos ) {
		ChangeInfo changes = new ChangeInfo("2.0d", true, "");
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes = new ChangeInfo("새로운 요소", false, null);
		changes.hardlight(CharSprite.POSITIVE);
		changeInfos.add(changes);


		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.AMULET), "메이드 인 헤븐의 DISC",
				"_최강의 DISC인 메이드 인 헤븐의 DISC가 추가되었습니다!_\n\n메이드 인 헤븐의 DISC를 제작하기 위한 과정의 재료 아이템들이 대거 추가되었습니다.\n\n메이드 인 헤븐의 DISC는 제작 난이도가 상당히 높습니다."));


		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.ARTIFACT_CAPE), "골드 익스피리언스 레퀴엠의 장비 DISC",
				"???\n" +
						"" +
						"습득 방법은 SPW 재단의 기록에서 확인할 수 있습니다."));

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.SKULL), "실버 채리엇 레퀴엠의 장비 DISC",
				"???\n" +
						"" +
						"습득 방법은 SPW 재단의 기록에서 확인할 수 있습니다."));
		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.RING_SAPPHIRE), "혼돈의 석가면",
				"속성/상형문자의 발동 확률과 효과를 강화시켜주는 석가면이 추가되었습니다!"
		));
		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.GUIDE_PAGE), "SPW재단의 기록",
				"_신규 수집 아이템인 SPW재단의 기록이 새로 추가됩니다!_\n\n" +
						"SPW재단의 기록은 한 계층당 6개, 무려 30개에 달하는 양으로 죠죠의 기묘한 던전의 팁이나 배경 설정이 기록되어 있으며, 던전 내에서 일정 확률로 발견할 수 있습니다."));
		changes.addButton( new ChangeButton( new Image(Assets.Sprites.SCORPIO, 0, 18, 18, 17), "신규 적",
				"희귀하게 볼 수 있는 적이 3종 추가되었습니다."));
		changes.addButton( new ChangeButton(new TalentIcon(Talent.THIEFS_INTUITION), "BGM 추가",
				"이제 일부 보스들의 체력이 낮아질 경우,\n_처형 BGM_이 나옵니다.\n\n" +
						"천국의 DISC 획득 후, _역행 전용 BGM_이 추가되었습니다."));
		changes.addButton( new ChangeButton(new BuffIcon(BuffIndicator.LOCKED_FLOOR, true), "캐릭터 상호작용",
				"이제 보스와 대면하면 현재 캐릭터에 따라서 특수 등장대사가 출력됩니다." +
						"" +
						"" +
						""));
		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.KIT), "보스 파생 아이템",
				"이제 모든 보스가 1/10 확률로 드롭하는 강자의 잔재를 이용해서\n\n보스 파생 아이템을 제작할 수 있습니다."));

		Image ic = Icons.get(Icons.SEED);
		ic.hardlight(1f, 1.5f, 0.67f);





		changes = new ChangeInfo("리워크", false, null);
		changes.hardlight(CharSprite.WARNING);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(new Image(Assets.Sprites.REBEL, 144, 0, 16, 16), "천국에 도달한 DIO",
				"천국에 도달한 DIO의 패턴이 새롭게 리워크되었습니다."));

		changes.addButton( new ChangeButton( new Image(Assets.Sprites.YOG, 0, 2, 20, 19), "DIO",
				"악의 각성 시련 활성화 시,\n최고로 High해진 모습으로 등장하게 됩니다."));

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.AJA), "에이자의 석가면 리워크",
				"에이자의 석가면의 효과가 리워크되었습니다." +
						""));
		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.ARTIFACT_GREAVES), "녹색 아기의 장비 DISC",
				"골드 익스피리언스의 장비 DISC -> 녹색 아기의 장비 DISC\n\n" +
						"사용 효과가 리워크되었습니다." ));

		changes.addButton( new ChangeButton(new Image(Assets.Sprites.WARRIOR, 0, 90, 12, 15), "용기의 파문전사 리워크",
				"용기의 파문전사가 리워크 되었습니다!\n" +
						"_-_ 부활 능력이 특성으로 이관되었습니다.\n" +
						"_-_ 용기의 파문전사의 액티브 효과가 추가됩니다.\n" +
						"_-_ 용기의 파문전사의 특성이 변경되었습니다."));


		changes.addButton( new ChangeButton( new Image(Assets.Sprites.PUCCI, 0, 15, 12, 15), "악의 각성 변경",
				"악의 각성 시련 활성화 시 천국에 도달한 DIO의 패턴이 일부 변경됩니다."));

		changes.addButton( new ChangeButton(new TalentIcon(Talent.EXCESS_CHARGE), "일부 3단계 특성 리워크",
				"파문 방출 : 이제 적석 발사 후 근접 공격 피해량과 더불어, 적석의 추가 효과도 강화됩니다.\n\n" +
						"파문 스승의 가르침 : 에이자의 적석이 전부 충전되어 있을 때 적석의 충전량을 소모하면, 강화 수치에 비례한 보호막이 부여됩니다.\n\n" +
						"빛의 영수증이다! : 이제 골드 대신 아이템을 떨어트릴 확률이 증가합니다." ));

		changes.addButton( new ChangeButton(BadgeBanner.image( Badges.Badge.YORIHIMES_ALL_CLASSES.image ), "신규 엔드 컨텐츠 뱃지",
				"천국에 도달한 DIO 관련 신규 뱃지가 3종 추가되었습니다."));


		changes = new ChangeInfo("상향", false, null);
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.RING_AGATE), "행운의 석가면",
						"강화 레벨 +1/+3/+5/+7/+9에서\n+1/+2/+3/+4/+5강 장비 확정 드랍으로 상향"));

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.WAND_TRANSFUSION), "소프트&웨트의 사격 DISC",
				"특수한 적에게 가하는 강화 레벨당 데미지 0.5~1 -> 1~2" ));

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.TELE_GRAB), "자철석",
				"이제 같은 장소에 겹쳐있는 아이템이나 적에게 꽃힌 여러 개의 투척 무기들을 전부 회수할 수 있습니다."));

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.SHORTSWORD, new ItemSprite.Glowing(0x000000)), "짜증나는 저주",
				"5가지 새로운 대사가 추가되었습니다."));

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.SAI), "왕 첸의 갈퀴손",
				"자정부터 새벽 5시 사이, 공격력이 25% 더 강해집니다."));

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.CHEST), "30층 개편",
				"30층에 신규 보물상자가 추가되었습니다."));

		changes.addButton( new ChangeButton( new Image(Assets.Sprites.HUNTRESS, 0, 90, 12, 15), "레퀴엠 죠르노",
				"비선공으로 변경되었습니다.\n\n이제 어떠한 방식으로 데미지를 가해도 버프가 발동됩니다.\n\n반사 능력이 삭제되었습니다.\n\n드롭 아이템이 변경되었습니다."));
		ic = Icons.get(Icons.CALENDAR);
		ic.hardlight(1.2f, 0.5f, 1.7f);
		changes.addButton( new ChangeButton(ic, "일일 도전 리플레이 모드",
				"이제 플레이한 일일 도전을 다시 반복해서 할 수 있습니다.\n\n" +
						"리플레이 모드 기록은 24시간이 지나면 삭제됩니다." ));

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "nerfs"), false, null);
		changes.hardlight(CharSprite.NEGATIVE);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.EXOTIC_RAIDO), "푸치신부의 기억 DISC",
				"이 DISC를 사용하면 모든 이로운 장비 DISC 효과도 차단합니다." +
						""));

		changes.addButton( new ChangeButton(new Image(Assets.Sprites.MUDA, 0, 0, 40, 40), "더 월드 오버 헤븐",
				"체력이 300 감소했습니다." +
						""));

	}

	public static void add_v1_2_Changes( ArrayList<ChangeInfo> changeInfos ) {
		ChangeInfo changes = new ChangeInfo("2.0c-2", true, "");
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes = new ChangeInfo("히든 보스", false, null);
		changes.hardlight(Window.SHPX_COLOR);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(new Image(Assets.Sprites.REBEL, 144, 0, 16, 16), "천국에 도달한 DIO",
				"신규 시련 전용 강력한 진 최종보스가 추가되었습니다!" +
				"" +
				"" +
				""));

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.CRYSTAL_KEY), "신규 시련",
				"신규 시련인 아이즈 오브 헤븐을 활성화해야 26층 이후가 개방됩니다."));

		Image ic = Icons.get(Icons.SEED);
		ic.hardlight(1f, 1.5f, 0.67f);

		changes = new ChangeInfo("신규 각성 무기", false, null);
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.AJA), "에이자의 석가면",
				"" +
				""));

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.PINK), "핑크 다크의 소년",
				"" +
				"" +
				"" +
				""));

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.GREATSWORD), "신규 무기",
				"스카이 하이가 새로 추가되었습니다." +
				"" +
				""));

		ic = Icons.get(Icons.SEED);
		ic.hardlight(1f, 1.5f, 0.67f);
		changes.addButton( new ChangeButton(ic, "시드 고정",
				"" +
				"" +
				"" +
				""));

		ic = Icons.get(Icons.CALENDAR);
		ic.hardlight(0.5f, 1f, 2f);
		changes.addButton( new ChangeButton(ic, "일일 도전",
				"" +
						""));

		changes = new ChangeInfo("리워크", false, null);
		changes.hardlight(CharSprite.WARNING);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.SCIMITAR), "아키라의 일렉기타",
				"" +
						""));

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.ROUND_SHIELD), "바람의 프로텍터",
				"" +
						""));

	}





}
