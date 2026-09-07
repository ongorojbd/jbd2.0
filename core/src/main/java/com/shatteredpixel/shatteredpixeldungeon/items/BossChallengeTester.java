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

package com.shatteredpixel.shatteredpixeldungeon.items;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;

import java.util.ArrayList;

//테스트용 아이템.
//현재 게임(런)에서 BOSS_CHALLENGE_1~4 인게임 뱃지 중 REQUIRED개 이상을 획득했을 때만 사용할 수 있다.
//계정 전역 해금 여부(Badges.isUnlocked)와는 무관하게, 이번 런에서 달성했는지(Badges.isLocallyUnlocked)를 본다.
public class BossChallengeTester extends Item {

	public static final String AC_USE = "USE";

	//사용에 필요한 달성 개수
	public static final int REQUIRED = 3;

	private static final Badges.Badge[] CHALLENGES = {
			Badges.Badge.BOSS_CHALLENGE_1,
			Badges.Badge.BOSS_CHALLENGE_2,
			Badges.Badge.BOSS_CHALLENGE_3,
			Badges.Badge.BOSS_CHALLENGE_4,
	};

	{
		image = ItemSpriteSheet.TG;

		//defaultAction 이 있어야 퀵슬롯에 등록 가능 (QuickSlotButton.itemSelector)
		defaultAction = AC_USE;

		unique = true;
		bones = false;
	}

	@Override
	public boolean isUpgradable() {
		return false;
	}

	@Override
	public boolean isIdentified() {
		return true;
	}

	private static int metCount() {
		int count = 0;
		for (Badges.Badge b : CHALLENGES) {
			if (Badges.isLocallyUnlocked(b)) {
				count++;
			}
		}
		return count;
	}

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions(hero);
//		if (metCount() >= REQUIRED) {
//			actions.add(AC_USE);
//		}
		actions.add(AC_USE);
		return actions;
	}

	@Override
	public void execute(Hero hero, String action) {
		super.execute(hero, action);

		if (action.equals(AC_USE)) {

			if (metCount() < REQUIRED) {
				GLog.w(Messages.get(this, "use_fail", metCount(), REQUIRED));
				return;
			}

			if (Dungeon.depth > 25 && Dungeon.branch == 0) {
				InterlevelScene.mode = InterlevelScene.Mode.RETURN;
				InterlevelScene.returnDepth = 20;
				InterlevelScene.returnBranch = 2;
				InterlevelScene.returnPos = -1;
				InterlevelScene.bossChallengeReturn = true;
				Game.switchScene(InterlevelScene.class);

				GLog.p(Messages.get(this, "use_success"));
				CellEmitter.get(hero.pos).start(Speck.factory(Speck.STAR), 0.1f, 8);
				hero.sprite.operate(hero.pos);
				hero.spendAndNext(1f);
				detach(curUser.belongings.backpack);
				updateQuickslot();
			} else {
				GLog.p(Messages.get(this, "use_info"));
			}
		}
	}

	@Override
	public String desc() {
		StringBuilder builder = new StringBuilder(Messages.get(this, "desc", REQUIRED));

		for (int i = 0; i < CHALLENGES.length; i++) {
			boolean done = Badges.isLocallyUnlocked(CHALLENGES[i]);
			String state = Messages.get(this, done ? "state_done" : "state_notyet");
			builder.append("\n").append(Messages.get(this, "status_line",
					i + 1, CHALLENGES[i].title(), state));
		}

		builder.append("\n\n").append(Messages.get(this, "progress", metCount(), REQUIRED));
		return builder.toString();
	}

	@Override
	public int value() {
		return 10;
	}
}
