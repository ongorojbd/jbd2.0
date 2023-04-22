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
import com.shatteredpixel.shatteredpixeldungeon.sprites.HeroSprite;
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
	}

	public static void add_Coming_Soon( ArrayList<ChangeInfo> changeInfos ) {

		ChangeInfo changes = new ChangeInfo("Coming Soon", true, "");
		changes.hardlight(0xCCCCCC);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton( new Image(Assets.Sprites.SUPRESSION, 0, 0, 12, 15), "영웅 밸런스 조정",
				"2.0H에서는 전체적인 영웅 밸런스 조정이 있을 예정입니다."));
		changes.addButton( new ChangeButton( new Image(Assets.Sprites.ATOM, 0, 0, 12, 15), "신규 적",
				"또한 2.0H에서는 신규 적 2종이 추가됩니다.\n\n2.0H 업데이트는 4월 말~5월 초 사이 예정입니다."));
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
		changes.addButton( new ChangeButton(new BuffIcon(BuffIndicator.MONK_ENERGY, true), "보조 직업 UI 개선",
				"다음 보조 직업들의 시각적 인터페이스가 개선되었습니다 :\n\n용기의 파문전사 각성의 파문전사\n파괴의 스탠드사 속도의 스탠드사\n분노의 스탠드사 수복의 스탠드사\n의식 폭주의 스탠드사"));

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

		changes = new ChangeInfo("밸런스 조정", false, null);
		changes.hardlight(CharSprite.NEGATIVE);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton( Icons.get(Icons.PREFS), "아이템 조정",
				"다양한 무기들이 리워크되었습니다.\n\n" +
						"반발의 상형문자가 원거리 적을 튕겨내지 않습니다.\n\n" +
						"이제 충전의 석가면이 레퀴엠 브로치의 능력도 충전합니다."));
		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.SPOH), "26~30층 변경점",
				"30층 상자에 스타 플라티나 오버 헤븐의 DISC가 추가되었습니다.\n\n" +
						"26~29층에 천국에 도달한 DIO를 지원하는 적이 추가되었습니다.\n\n"+
						"죠나단, 죠셉의 패턴이 변경되었습니다.\n\n" +
						"천국에 도달한 DIO의 패턴이 추가/변경되었습니다."));
		changes.addButton( new ChangeButton(HeroSprite.avatar(HeroClass.DUELIST, 6), "죠스케 상향",
				"_-_ _무기 충전 속도_가 12.5% 상승합니다.\n\n_-_ _다이아몬드는 부서지지 않는다_ 특성의 추가 데미지가 2에서 3으로 상승합니다.\n_-_ _원상복귀_ 특성의 재사용 대기시간이 30턴에서 20턴으로 감소합니다.\n_-_ _황금의 정신_ 특성의 에너지 증가량이 25/50/100/150%에서 33/67/100/150로 증가합니다.\n"+
						"_-_ _개체 변형_ 특성의 필요 에너지 조건이 100/85/70%에서 100/80/60%로 감소합니다.\n\n_-_ _크레이지 D 능력 발현 - 악퉁 베이비_ :\n은신 지속 시간 6/5/4턴에서 8/6/4턴으로 증가.\n_-_ _크레이지 D 능력 발현 - '헌팅'하러 가자!_ :\n+15/10%에서 +40/30%로 피해량 증가.\n_-_ _크레이지 D 능력 발현 - 가드 브레이커_ :\n+50/45/40/35%에서 +65/60/55/50%로 피해량 증가.\n"+
						"_-_ _크레이지 D 능력 발현 - 연속 공격_ :\n+30/25/20%에서 +40/35/30%로 피해량 증가.\n_-_ _크레이지 D 능력 발현 - 차지 어택_ :\n힘을 축적할 때마다 +20%에서 +33%로 피해량 증가."
		));
		changes.addButton( new ChangeButton( new Image(Assets.Sprites.KOUSAKU, 0, 0, 12, 15), "카와지리 보상 강화",
				"카와지리 처치 시, 영구히 지속되는 전용 버프가 추가되었습니다."));

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


		changes = new ChangeInfo(".", false, null);
		changes.hardlight(CharSprite.NEGATIVE);
		changeInfos.add(changes);


		changes.addButton( new ChangeButton( new Image(Assets.Sprites.WORLD21, 0, 0, 40, 40), "THE WORLD",
				"스틸 볼 런 레이스의 종착점에 기다리는 것은.."));
	}

}
