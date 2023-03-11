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
		add_v1_4_1_Changes(changeInfos);
		add_v1_4_Changes(changeInfos);
		add_v1_3_3_Changes(changeInfos);
	}

	public static void add_Coming_Soon( ArrayList<ChangeInfo> changeInfos ) {

		ChangeInfo changes = new ChangeInfo("Coming Soon", true, "");
		changes.hardlight(0xCCCCCC);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton( new Image(Assets.Sprites.RESEARCHER, 0, 15, 12, 15), "신규 영웅 : 죠린",
			""));
	}


	public static void add_v1_4_1_Changes( ArrayList<ChangeInfo> changeInfos ) {
		ChangeInfo changes = new ChangeInfo("2.0g", true, "");
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes = new ChangeInfo("신규 캐릭터", false, null);
		changes.hardlight(CharSprite.POSITIVE);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton( new Image(Assets.Sprites.RESEARCHER, 0, 0, 12, 15), "죠스케",
				"4부의 주인공 히가시카타 죠스케가 플레이어블 캐릭터로 추가되었습니다!"));

		changes = new ChangeInfo("4부 적", false, null);
		changes.hardlight(CharSprite.WARNING);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton( new Image(Assets.Sprites.BCOM, 0, 0, 15, 15), "배드 컴퍼니",
				"6층에 신규 적인 배드 컴퍼니가 등장합니다."));
		changes.addButton( new ChangeButton( new Image(Assets.Sprites.BOYTWO, 0, 0, 13, 18), "보이 투 맨",
				"무작위 계층에 희귀한 확률로 보이 투 맨이 출몰합니다."));
		changes.addButton( new ChangeButton( new Image(Assets.Sprites.STOWER, 0, 0, 17, 22), "슈퍼 플라이",
				"무작위 계층에 희귀한 확률로 슈퍼 플라이가 출몰합니다."));

		changes = new ChangeInfo("4부 NPC", false, null);
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton( new Image(Assets.Sprites.YUKAKO, 0, 0, 12, 15), "야마기시 유카코",
				"특정 계층에 일정 확률로 유카코 npc가 등장합니다."));
		changes.addButton( new ChangeButton( new Image(Assets.Sprites.RETONIO, 0, 0, 12, 15), "토니오 트루사르디",
				"특정 계층에 일정 확률로 토니오 npc가 등장합니다."));
		changes.addButton( new ChangeButton( new Image(Assets.Sprites.ROHAN, 0, 0, 12, 14), "키시베 로한",
				"특정 계층에 일정 확률로 로한 npc가 등장합니다."));

		changes = new ChangeInfo("아이템 조정", false, null);
		changes.hardlight(CharSprite.NEGATIVE);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton( Icons.get(Icons.PREFS), "아이템 조정",
				"다양한 무기들이 리워크되었습니다.\n\n" +
						"반발의 상형문자가 원거리 적을 튕겨내지 않습니다.\n\n" +
						"이제 충전의 석가면이 레퀴엠 브로치의 능력도 충전합니다.\n\n"+
						"30층 상자에 스타 플라티나 오버 헤븐의 DISC가 추가되었습니다."));
	}

	public static void add_v1_4_Changes( ArrayList<ChangeInfo> changeInfos ) {
		ChangeInfo changes = new ChangeInfo("2.0f", true, "");
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes = new ChangeInfo("스틸 볼 런 레이스", false, null);
		changes.hardlight(CharSprite.POSITIVE);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.MAP1), "스틸 볼 런 레이스 지도 1st",
				"코코 잠보를 통해 제작 가능한 신규 아이템인 스틸 볼 런 레이스 지도가 추가되었습니다.\n\n" +
						"스틸 볼 런 레이스 지도 1st를 사용하면 _성인의 안구_의 위치를 알 수 있습니다."));
		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.MAP2), "스틸 볼 런 레이스 지도 2nd",
				"코코 잠보를 통해 제작 가능한 신규 아이템인 스틸 볼 런 레이스 지도가 추가되었습니다.\n\n" +
						"스틸 볼 런 레이스 지도 2nd를 사용하면 _성인의 척추_의 위치를 알 수 있습니다."));
		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.MAP3), "스틸 볼 런 레이스 지도 3rd",
				"코코 잠보를 통해 제작 가능한 신규 아이템인 스틸 볼 런 레이스 지도가 추가되었습니다.\n\n" +
						"스틸 볼 런 레이스 지도 3rd를 사용하면 _성인의 심장_의 위치를 알 수 있습니다."));
		changes.addButton( new ChangeButton(new BuffIcon(BuffIndicator.SACRIFICE, true), "성인의 유해",
				"지도를 통해 성인의 유해를 흡수하면 특수 효과가 제공됩니다.\n\n_성인의 유해를 3개 모으면 D4C 러브 트레인이 발현됩니다._"));
		changes.addButton( new ChangeButton(new BuffIcon(BuffIndicator.RAGE, true), "D4C 러브 트레인",
				"성인의 유해 3개가 모였을 시 발현되는 강력한 효과가 추가되었습니다."));
		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.MAP0), "스틸 볼 런 레이스 지도 4~9th",
				"???"));
		changes = new ChangeInfo("특수 보스", false, null);
		changes.hardlight(CharSprite.WARNING);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton( new Image(Assets.Sprites.CIVIL, 0, 0, 12, 17), "시빌 워",
				"성인의 유해 앞을 가로막는 신규 적인 시빌 워가 추가되었습니다."));
		changes.addButton( new ChangeButton( new Image(Assets.Sprites.BMORE, 0, 0, 12, 16), "블랙모어",
				"성인의 유해 앞을 가로막는 신규 적인 블랙모어가 추가되었습니다."));
		changes.addButton( new ChangeButton( new Image(Assets.Sprites.DIEGO, 0, 0, 12, 15), "디에고 브란도",
				"성인의 유해 앞을 가로막는 신규 적인 디에고 브란도가 추가되었습니다."));
		changes.addButton( new ChangeButton( new Image(Assets.Sprites.PUCCI, 0, 0, 12, 15), "퍼니 발렌타인",
				"추격자들을 보낸 장본인인 퍼니 발렌타인이 직접 보스로 등장합니다."));

		changes = new ChangeInfo("신규 NPC", false, null);
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton( new Image(Assets.Sprites.WEZA, 0, 0, 12, 15), "웨더 리포트",
				"21~24층에 신규 NPC인 웨더 리포트가 추가되었습니다."));


		changes = new ChangeInfo("???", false, null);
		changes.hardlight(CharSprite.NEGATIVE);
		changeInfos.add(changes);


		changes.addButton( new ChangeButton( new Image(Assets.Sprites.WORLD21, 0, 0, 40, 40), "THE WORLD",
				"스틸 볼 런 레이스의 종착점에 기다리는 것은.."));
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
				"받는 피해 증가 75% -> 50%"));

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.EXOTIC_BERKANAN), "웨더 리포트의 기억 DISC",
				"조합법이 변경되었습니다.\n\n변환의 DISC + DISC의 정수"));


		changes = new ChangeInfo("하향", false, null);
		changes.hardlight(CharSprite.NEGATIVE);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton( Icons.get(Icons.TALENT), "시련 난이도 조정",
				"아이즈 오브 헤븐 : 천국에 도달한 DIO 사망 시 펫 숍의 기억 DISC로 바로 26층으로 올라갈 수 있습니다.\n\n자유인의 광상곡 : 오시리스신의 퀘스트가 정상적으로 진행되도록 수정되었습니다.\n\n타올라라 용의 꿈 : 이제 흉에 방향에서 공격할 시 데미지가 그대로 반사됩니다. 압둘에게도 흉의 방향이 적용됩니다."));
	}


}
