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
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.WoollyBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfMetamorphosis;
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
		add_v1_3_Changes(changeInfos);
		add_v1_2_Changes(changeInfos);
	}

	public static void add_Coming_Soon( ArrayList<ChangeInfo> changeInfos ) {

		ChangeInfo changes = new ChangeInfo("Coming Soon", true, "");
		changes.hardlight(0xCCCCCC);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton( new Image(Assets.Sprites.GUARDIAN, 0, 0, 12, 15), "죠스케",
			""));

		changes.addButton( new ChangeButton( new Image(Assets.Sprites.RESEARCHER, 0, 15, 12, 15), "죠린",
			""));

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.PICKAXE),"크레이지 다이아몬드 퀘스트 변경" ,
			""));

	}


	public static void add_v1_3_Changes( ArrayList<ChangeInfo> changeInfos ) {
		ChangeInfo changes = new ChangeInfo("2.0d", true, "");
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);


		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.AMULET), "2.0d 업데이트",
				"찬미하라..!! 천국이 태어 났음을!!\n새로운 세계의 막이 올랐음을..!!\n\n헨델의 [메시아] 최고의 명연..\n가디너 지휘 82년 녹음!" +
						"\n실로! 이 상황에 걸맞는 곡이다!\n소리가 빛을 발하는것만 같군!\n\n헨델은 자신의 최고 걸작을 죽기 전날까지도 계속 연주 했다고 하지!"));


		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.ARTIFACT_CAPE), "골드 익스피리언스 레퀴엠의 장비 DISC",
				"???\n" +
						"" +
						"습득 방법은 SPW 재단의 기록에서 확인할 수 있습니다."));

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.SKULL), "실버 채리엇 레퀴엠의 장비 DISC",
				"???\n" +
						"" +
						"습득 방법은 SPW 재단의 기록에서 확인할 수 있습니다."));
		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.RING_SAPPHIRE), "혼돈의 석가면",
				"속성/상형문자의 발동 확률과 효과를 강화시켜주는 석가면이 추가됩니다!"
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
		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.ARTIFACT_BEACON), "맨 인 더 미러의 장비 DISC",
				"보스별로 일정한 확률로 드랍되는 레어 아이템이 추가되었습니다.\n\n" +
						"5층 보스를 격파하면 1/5 확률로 드롭됩니다."
						));
		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.KIT), "제 3의 폭탄",
				"보스별로 일정한 확률로 드랍되는 레어 아이템이 추가되었습니다.\n\n" +
						"15층의 ???를 격파하면 1/5 확률로 드롭됩니다."
					));
		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.BEACON), "DIO의 뼈",
				"보스별로 일정한 확률로 드랍되는 레어 아이템이 추가되었습니다.\n\n" +
						"20층 또는 25층 또는 30층 보스를 격파하면 1/15 확률로 드롭됩니다."));

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.ELIXIR_MIGHT), "광기의 피",
				"보스별로 일정한 확률로 드랍되는 레어 아이템이 추가되었습니다.\n\n" +
						"_25층 보스를 격파하면 1/30 확률로 드롭됩니다._"));

		Image ic = Icons.get(Icons.SEED);
		ic.hardlight(1f, 1.5f, 0.67f);





		changes = new ChangeInfo("리워크", false, null);
		changes.hardlight(CharSprite.WARNING);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(new Image(Assets.Sprites.REBEL, 144, 0, 16, 16), "진 : 천국에 도달한 DIO",
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

		changes = new ChangeInfo("상향", false, null);
		changes.hardlight(Window.SHPX_COLOR);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.RING_AGATE), "행운의 석가면",
						"강화 레벨 +1/+3/+5/+7/+9에서\n+1/+2/+3/+4/+5강 장비 확정 드랍으로 상향"));

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.WAND_TRANSFUSION), "소프트&웨트의 사격 DISC",
				"특수한 적에게 가하는 강화 레벨당 데미지 0.5~1 -> 1~2" ));

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.TELE_GRAB), "자력석",
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

		changes.addButton( new ChangeButton( new Image(Assets.Sprites.SOLDIER, 0, 0, 12, 15), "26층 이후 적들 하향",
				"레퀴엠 죠르노를 제외한 모든 적들의 체력이 감소되었습니다."));

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
