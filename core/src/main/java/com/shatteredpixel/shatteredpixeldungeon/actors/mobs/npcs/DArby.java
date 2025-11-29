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
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DArbySprite;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndCoinGame;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Music;
import com.watabou.utils.Callback;

public class DArby extends NPC {

	{
		spriteClass = DArbySprite.class;

		properties.add(Property.IMMOVABLE);
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

		Game.runOnRenderThread(new Callback() {
			@Override
			public void call() {
				// 이미 플레이한 경우
				if (WndCoinGame.hasBeenPlayed()) {
					GameScene.show(new WndOptions(
							sprite(),
							Messages.titleCase(name()),
							Messages.get(DArby.class, "already_played"),
							Messages.get(DArby.class, "leave")
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
							Music.INSTANCE.play(Assets.Music.YUUKI, true);
						} else if (index == 1) {
							// 규칙 설명
							GameScene.show(new WndOptions(
									sprite(),
									Messages.get(DArby.class, "rules_title"),
									Messages.get(DArby.class, "rules_desc"),
									Messages.get(DArby.class, "understood")
							));
						}
						// index == 2: 그냥 나가기
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
}

