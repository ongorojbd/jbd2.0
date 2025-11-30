/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2025 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AscensionChallenge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DArbySprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DArbyOldSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.WammuChariot2Sprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.WammuChariotSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.WammuThirdSprite;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndCoinGame;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;

public class DArby extends NPC {

	{
		spriteClass = DArbySprite.class;

		properties.add(Property.IMMOVABLE);
	}

	@Override
	protected boolean act() {
		if (Dungeon.hero.buff(AscensionChallenge.class) != null){
			die(null);
			return true;
		}
		
		// spw30 > 0이면 스프라이트를 DArbyOldSprite로 변경
		if (Statistics.spw30 > 0 && spriteClass != DArbyOldSprite.class) {
			if (sprite != null) sprite.killAndErase();
			spriteClass = DArbyOldSprite.class;
			GameScene.addSprite(this);
		}
		
		return super.act();
	}

	@Override
	public int defenseSkill(Char enemy) {
		return INFINITE_EVASION;
	}

	@Override
	public void damage(int dmg, Object src) {
		// 무적
	}

	@Override
	public boolean add(Buff buff) {
		return false;
	}

	@Override
	public boolean reset() {
		return true;
	}

	@Override
	public boolean interact(Char c) {
		sprite.turnTo(pos, c.pos);

		if (c != Dungeon.hero) {
			return super.interact(c);
		}

		// spw30 > 0이면 패배한 다비의 대사
		if (Statistics.spw30 > 0) {
			Sample.INSTANCE.play(Assets.Sounds.DA4);
			Game.runOnRenderThread(new Callback() {
				@Override
				public void call() {
					GameScene.show(new WndOptions(
							sprite(),
							Messages.get(DArby.class, "name"),
							Messages.get(DArby.class, "defeated")
					));
				}
			});
			return true;
		}

		if (Statistics.spw30 == 0) {
			Sample.INSTANCE.play(Assets.Sounds.DA1);
		}

		Game.runOnRenderThread(new Callback() {
			@Override
			public void call() {
				if (Statistics.spw20 == 1) {
					GameScene.show(new WndOptions(
							sprite(),
							Messages.get(DArby.class, "name"),
							Messages.get(DArby.class, "already_played1")
					));
					return;
				} else if (Statistics.spw16 == 1) {
					GameScene.show(new WndOptions(
							sprite(),
							Messages.get(DArby.class, "name"),
							Messages.get(DArby.class, "already_played2")
					));
					return;
				}

				GameScene.show(new WndOptions(
						sprite(),
						Messages.titleCase(name()),
						Messages.get(DArby.class, "greeting"),
						Messages.get(DArby.class, "play"),
						Messages.get(DArby.class, "rules"),
						Messages.get(DArby.class, "leave")
				) {
					@Override
					protected void onSelect(int index) {
						if (index == 0) {
							// 게임 시작
							GameScene.show(new WndCoinGame());
							Statistics.spw16++;
							Sample.INSTANCE.play(Assets.Sounds.DA2);
							Music.INSTANCE.play(Assets.Music.YUUKI, true);
						} else if (index == 1) {
							// 규칙 설명
							GameScene.show(new WndOptions(
									sprite(),
									Messages.get(DArby.class, "rules_title"),
									Messages.get(DArby.class, "rules_desc"),
									Messages.get(DArby.class, "understood")
							));
							Sample.INSTANCE.play(Assets.Sounds.DA3);
						} else {
							// index == 2: 그냥 나가기
						}
					}
				});
			}
		});
		return true;
	}

	@Override
	public String description() {
		return Messages.get(this, "desc");
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);

		// restore sprite based on phase
		if (Statistics.spw30 == 0) spriteClass = DArbySprite.class;
		else spriteClass = DArbyOldSprite.class;
	}
}

