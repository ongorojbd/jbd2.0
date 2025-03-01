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
		add_jolyne_Changes(changeInfos);
	}

	public static void add_Coming_Soon(ArrayList<ChangeInfo> changeInfos) {

		ChangeInfo changes = new ChangeInfo("Coming Soon", true, "");
		changes.hardlight(0xCCCCCC);
		changeInfos.add(changes);

		changes.addButton(new ChangeButton(HeroSprite.avatar(HeroClass.WARRIOR, 6), "죠나단 리워크",
				"죠나단의 능력 리워크를 통해 플레이 경험을 개선할 예정입니다."));
		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.TRINKET_CATA), "신규 위험한 물건",
				"죠나단 리워크와 함께 신규 위험한 물건과 신규 희귀 적도 추가됩니다."));
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
		changes.addButton( new ChangeButton(HeroSprite.avatar(HeroClass.CLERIC, 6), "죠린 전용 레퀴엠 능력",
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
